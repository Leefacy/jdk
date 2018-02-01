/*      */ package com.sun.tools.javac.processing;
/*      */ 
/*      */ import com.sun.source.util.JavacTask;
/*      */ import com.sun.source.util.TaskEvent;
/*      */ import com.sun.source.util.TaskEvent.Kind;
/*      */ import com.sun.tools.javac.api.BasicJavacTask;
/*      */ import com.sun.tools.javac.api.JavacTrees;
/*      */ import com.sun.tools.javac.api.MultiTaskListener;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.comp.CompileStates.CompileState;
/*      */ import com.sun.tools.javac.comp.Todo;
/*      */ import com.sun.tools.javac.file.FSInfo;
/*      */ import com.sun.tools.javac.file.JavacFileManager;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.ClassReader.BadClassFile;
/*      */ import com.sun.tools.javac.main.JavaCompiler;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.model.JavacElements;
/*      */ import com.sun.tools.javac.model.JavacTypes;
/*      */ import com.sun.tools.javac.parser.Tokens;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssignOp;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Abort;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.ClientCodeException;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticFlag;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.JavacMessages;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DeferredDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.ServiceLoader;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.ServiceConfigurationError;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.annotation.processing.Filer;
/*      */ import javax.annotation.processing.Messager;
/*      */ import javax.annotation.processing.ProcessingEnvironment;
/*      */ import javax.annotation.processing.Processor;
/*      */ import javax.annotation.processing.RoundEnvironment;
/*      */ import javax.lang.model.SourceVersion;
/*      */ import javax.lang.model.element.AnnotationMirror;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.element.ExecutableElement;
/*      */ import javax.lang.model.element.PackageElement;
/*      */ import javax.lang.model.element.TypeElement;
/*      */ import javax.lang.model.type.DeclaredType;
/*      */ import javax.lang.model.util.ElementScanner8;
/*      */ import javax.lang.model.util.Elements;
/*      */ import javax.tools.Diagnostic.Kind;
/*      */ import javax.tools.DiagnosticListener;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.JavaFileObject.Kind;
/*      */ import javax.tools.StandardJavaFileManager;
/*      */ import javax.tools.StandardLocation;
/*      */ 
/*      */ public class JavacProcessingEnvironment
/*      */   implements ProcessingEnvironment, Closeable
/*      */ {
/*      */   private final Options options;
/*      */   private final boolean printProcessorInfo;
/*      */   private final boolean printRounds;
/*      */   private final boolean verbose;
/*      */   private final boolean lint;
/*      */   private final boolean fatalErrors;
/*      */   private final boolean werror;
/*      */   private final boolean showResolveErrors;
/*      */   private final JavacFiler filer;
/*      */   private final JavacMessager messager;
/*      */   private final JavacElements elementUtils;
/*      */   private final JavacTypes typeUtils;
/*      */   private DiscoveredProcessors discoveredProcs;
/*      */   private final Map<String, String> processorOptions;
/*      */   private final Set<String> unmatchedProcessorOptions;
/*      */   private final Set<String> platformAnnotations;
/*  130 */   private Set<Symbol.PackageSymbol> specifiedPackages = Collections.emptySet();
/*      */   Log log;
/*      */   JCDiagnostic.Factory diags;
/*      */   Source source;
/*      */   private ClassLoader processorClassLoader;
/*      */   private SecurityException processorClassLoaderException;
/*      */   private JavacMessages messages;
/*      */   private MultiTaskListener taskListener;
/*      */   private Context context;
/* 1352 */   private static final TreeScanner treeCleaner = new TreeScanner() {
/*      */     public void scan(JCTree paramAnonymousJCTree) {
/* 1354 */       super.scan(paramAnonymousJCTree);
/* 1355 */       if (paramAnonymousJCTree != null)
/* 1356 */         paramAnonymousJCTree.type = null; 
/*      */     }
/*      */ 
/* 1359 */     public void visitTopLevel(JCTree.JCCompilationUnit paramAnonymousJCCompilationUnit) { paramAnonymousJCCompilationUnit.packge = null;
/* 1360 */       super.visitTopLevel(paramAnonymousJCCompilationUnit); }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl) {
/* 1363 */       paramAnonymousJCClassDecl.sym = null;
/* 1364 */       super.visitClassDef(paramAnonymousJCClassDecl);
/*      */     }
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramAnonymousJCMethodDecl) {
/* 1367 */       paramAnonymousJCMethodDecl.sym = null;
/* 1368 */       super.visitMethodDef(paramAnonymousJCMethodDecl);
/*      */     }
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl) {
/* 1371 */       paramAnonymousJCVariableDecl.sym = null;
/* 1372 */       super.visitVarDef(paramAnonymousJCVariableDecl);
/*      */     }
/*      */     public void visitNewClass(JCTree.JCNewClass paramAnonymousJCNewClass) {
/* 1375 */       paramAnonymousJCNewClass.constructor = null;
/* 1376 */       super.visitNewClass(paramAnonymousJCNewClass);
/*      */     }
/*      */     public void visitAssignop(JCTree.JCAssignOp paramAnonymousJCAssignOp) {
/* 1379 */       paramAnonymousJCAssignOp.operator = null;
/* 1380 */       super.visitAssignop(paramAnonymousJCAssignOp);
/*      */     }
/*      */     public void visitUnary(JCTree.JCUnary paramAnonymousJCUnary) {
/* 1383 */       paramAnonymousJCUnary.operator = null;
/* 1384 */       super.visitUnary(paramAnonymousJCUnary);
/*      */     }
/*      */     public void visitBinary(JCTree.JCBinary paramAnonymousJCBinary) {
/* 1387 */       paramAnonymousJCBinary.operator = null;
/* 1388 */       super.visitBinary(paramAnonymousJCBinary);
/*      */     }
/*      */     public void visitSelect(JCTree.JCFieldAccess paramAnonymousJCFieldAccess) {
/* 1391 */       paramAnonymousJCFieldAccess.sym = null;
/* 1392 */       super.visitSelect(paramAnonymousJCFieldAccess);
/*      */     }
/*      */     public void visitIdent(JCTree.JCIdent paramAnonymousJCIdent) {
/* 1395 */       paramAnonymousJCIdent.sym = null;
/* 1396 */       super.visitIdent(paramAnonymousJCIdent);
/*      */     }
/*      */     public void visitAnnotation(JCTree.JCAnnotation paramAnonymousJCAnnotation) {
/* 1399 */       paramAnonymousJCAnnotation.attribute = null;
/* 1400 */       super.visitAnnotation(paramAnonymousJCAnnotation);
/*      */     }
/* 1352 */   };
/*      */ 
/* 1448 */   private static final Pattern allMatches = Pattern.compile(".*");
/* 1449 */   public static final Pattern noMatches = Pattern.compile("(\\P{all})+");
/*      */ 
/*      */   public static JavacProcessingEnvironment instance(Context paramContext)
/*      */   {
/*  159 */     JavacProcessingEnvironment localJavacProcessingEnvironment = (JavacProcessingEnvironment)paramContext.get(JavacProcessingEnvironment.class);
/*  160 */     if (localJavacProcessingEnvironment == null)
/*  161 */       localJavacProcessingEnvironment = new JavacProcessingEnvironment(paramContext);
/*  162 */     return localJavacProcessingEnvironment;
/*      */   }
/*      */ 
/*      */   protected JavacProcessingEnvironment(Context paramContext) {
/*  166 */     this.context = paramContext;
/*  167 */     paramContext.put(JavacProcessingEnvironment.class, this);
/*  168 */     this.log = Log.instance(paramContext);
/*  169 */     this.source = Source.instance(paramContext);
/*  170 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  171 */     this.options = Options.instance(paramContext);
/*  172 */     this.printProcessorInfo = this.options.isSet(Option.XPRINTPROCESSORINFO);
/*  173 */     this.printRounds = this.options.isSet(Option.XPRINTROUNDS);
/*  174 */     this.verbose = this.options.isSet(Option.VERBOSE);
/*  175 */     this.lint = Lint.instance(paramContext).isEnabled(Lint.LintCategory.PROCESSING);
/*  176 */     if ((this.options.isSet(Option.PROC, "only")) || (this.options.isSet(Option.XPRINT))) {
/*  177 */       JavaCompiler localJavaCompiler = JavaCompiler.instance(paramContext);
/*  178 */       localJavaCompiler.shouldStopPolicyIfNoError = CompileStates.CompileState.PROCESS;
/*      */     }
/*  180 */     this.fatalErrors = this.options.isSet("fatalEnterError");
/*  181 */     this.showResolveErrors = this.options.isSet("showResolveErrors");
/*  182 */     this.werror = this.options.isSet(Option.WERROR);
/*  183 */     this.platformAnnotations = initPlatformAnnotations();
/*      */ 
/*  187 */     this.filer = new JavacFiler(paramContext);
/*  188 */     this.messager = new JavacMessager(paramContext, this);
/*  189 */     this.elementUtils = JavacElements.instance(paramContext);
/*  190 */     this.typeUtils = JavacTypes.instance(paramContext);
/*  191 */     this.processorOptions = initProcessorOptions(paramContext);
/*  192 */     this.unmatchedProcessorOptions = initUnmatchedProcessorOptions();
/*  193 */     this.messages = JavacMessages.instance(paramContext);
/*  194 */     this.taskListener = MultiTaskListener.instance(paramContext);
/*  195 */     initProcessorClassLoader();
/*      */   }
/*      */ 
/*      */   public void setProcessors(Iterable<? extends Processor> paramIterable) {
/*  199 */     Assert.checkNull(this.discoveredProcs);
/*  200 */     initProcessorIterator(this.context, paramIterable);
/*      */   }
/*      */ 
/*      */   private Set<String> initPlatformAnnotations() {
/*  204 */     HashSet localHashSet = new HashSet();
/*  205 */     localHashSet.add("java.lang.Deprecated");
/*  206 */     localHashSet.add("java.lang.Override");
/*  207 */     localHashSet.add("java.lang.SuppressWarnings");
/*  208 */     localHashSet.add("java.lang.annotation.Documented");
/*  209 */     localHashSet.add("java.lang.annotation.Inherited");
/*  210 */     localHashSet.add("java.lang.annotation.Retention");
/*  211 */     localHashSet.add("java.lang.annotation.Target");
/*  212 */     return Collections.unmodifiableSet(localHashSet);
/*      */   }
/*      */ 
/*      */   private void initProcessorClassLoader() {
/*  216 */     JavaFileManager localJavaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/*      */     try
/*      */     {
/*  219 */       this.processorClassLoader = (localJavaFileManager.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) ? localJavaFileManager
/*  220 */         .getClassLoader(StandardLocation.ANNOTATION_PROCESSOR_PATH) : 
/*  220 */         localJavaFileManager
/*  221 */         .getClassLoader(StandardLocation.CLASS_PATH));
/*      */ 
/*  223 */       if ((this.processorClassLoader != null) && ((this.processorClassLoader instanceof Closeable))) {
/*  224 */         JavaCompiler localJavaCompiler = JavaCompiler.instance(this.context);
/*  225 */         localJavaCompiler.closeables = localJavaCompiler.closeables.prepend((Closeable)this.processorClassLoader);
/*      */       }
/*      */     } catch (SecurityException localSecurityException) {
/*  228 */       this.processorClassLoaderException = localSecurityException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initProcessorIterator(Context paramContext, Iterable<? extends Processor> paramIterable) {
/*  233 */     Log localLog = Log.instance(paramContext);
/*      */     Object localObject;
/*  236 */     if (this.options.isSet(Option.XPRINT)) {
/*      */       try {
/*  238 */         Processor localProcessor = (Processor)PrintingProcessor.class.newInstance();
/*  239 */         localObject = com.sun.tools.javac.util.List.of(localProcessor).iterator();
/*      */       } catch (Throwable localThrowable) {
/*  241 */         AssertionError localAssertionError = new AssertionError("Problem instantiating PrintingProcessor.");
/*      */ 
/*  243 */         localAssertionError.initCause(localThrowable);
/*  244 */         throw localAssertionError;
/*      */       }
/*  246 */     } else if (paramIterable != null) {
/*  247 */       localObject = paramIterable.iterator();
/*      */     } else {
/*  249 */       String str = this.options.get(Option.PROCESSOR);
/*  250 */       if (this.processorClassLoaderException == null)
/*      */       {
/*  256 */         if (str != null)
/*  257 */           localObject = new NameProcessIterator(str, this.processorClassLoader, localLog);
/*      */         else {
/*  259 */           localObject = new ServiceIterator(this.processorClassLoader, localLog);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  268 */         localObject = handleServiceLoaderUnavailability("proc.cant.create.loader", this.processorClassLoaderException);
/*      */       }
/*      */     }
/*      */ 
/*  272 */     this.discoveredProcs = new DiscoveredProcessors((Iterator)localObject);
/*      */   }
/*      */ 
/*      */   private Iterator<Processor> handleServiceLoaderUnavailability(String paramString, Exception paramException)
/*      */   {
/*  287 */     JavaFileManager localJavaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/*      */ 
/*  289 */     if ((localJavaFileManager instanceof JavacFileManager)) {
/*  290 */       localObject = (JavacFileManager)localJavaFileManager;
/*      */ 
/*  293 */       Iterable localIterable = localJavaFileManager.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) ? ((StandardJavaFileManager)localObject)
/*  292 */         .getLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) : 
/*  292 */         ((StandardJavaFileManager)localObject)
/*  293 */         .getLocation(StandardLocation.CLASS_PATH);
/*      */ 
/*  295 */       if (needClassLoader(this.options.get(Option.PROCESSOR), localIterable))
/*  296 */         handleException(paramString, paramException);
/*      */     }
/*      */     else {
/*  299 */       handleException(paramString, paramException);
/*      */     }
/*      */ 
/*  302 */     Object localObject = Collections.emptyList();
/*  303 */     return ((java.util.List)localObject).iterator();
/*      */   }
/*      */ 
/*      */   private void handleException(String paramString, Exception paramException)
/*      */   {
/*  311 */     if (paramException != null) {
/*  312 */       this.log.error(paramString, new Object[] { paramException.getLocalizedMessage() });
/*  313 */       throw new Abort(paramException);
/*      */     }
/*  315 */     this.log.error(paramString, new Object[0]);
/*  316 */     throw new Abort();
/*      */   }
/*      */ 
/*      */   public boolean atLeastOneProcessor()
/*      */   {
/*  447 */     return this.discoveredProcs.iterator().hasNext();
/*      */   }
/*      */ 
/*      */   private Map<String, String> initProcessorOptions(Context paramContext) {
/*  451 */     Options localOptions = Options.instance(paramContext);
/*  452 */     Set localSet = localOptions.keySet();
/*  453 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*      */ 
/*  455 */     for (String str1 : localSet) {
/*  456 */       if ((str1.startsWith("-A")) && (str1.length() > 2)) {
/*  457 */         int i = str1.indexOf('=');
/*  458 */         String str2 = null;
/*  459 */         Object localObject = null;
/*      */ 
/*  461 */         if (i == -1) {
/*  462 */           str2 = str1.substring(2);
/*  463 */         } else if (i >= 3) {
/*  464 */           str2 = str1.substring(2, i);
/*      */ 
/*  466 */           localObject = i < str1.length() - 1 ? str1
/*  466 */             .substring(i + 1) : 
/*  466 */             null;
/*      */         }
/*  468 */         localLinkedHashMap.put(str2, localObject);
/*      */       }
/*      */     }
/*      */ 
/*  472 */     return Collections.unmodifiableMap(localLinkedHashMap);
/*      */   }
/*      */ 
/*      */   private Set<String> initUnmatchedProcessorOptions() {
/*  476 */     HashSet localHashSet = new HashSet();
/*  477 */     localHashSet.addAll(this.processorOptions.keySet());
/*  478 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   private void discoverAndRunProcs(Context paramContext, Set<TypeElement> paramSet, com.sun.tools.javac.util.List<Symbol.ClassSymbol> paramList, com.sun.tools.javac.util.List<Symbol.PackageSymbol> paramList1)
/*      */   {
/*  661 */     HashMap localHashMap = new HashMap(paramSet
/*  661 */       .size());
/*      */ 
/*  663 */     for (Object localObject1 = paramSet.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (TypeElement)((Iterator)localObject1).next();
/*  664 */       localHashMap.put(((TypeElement)localObject2).getQualifiedName().toString(), localObject2);
/*      */     }
/*      */ 
/*  669 */     if (localHashMap.size() == 0) {
/*  670 */       localHashMap.put("", null);
/*      */     }
/*  672 */     localObject1 = this.discoveredProcs.iterator();
/*      */ 
/*  679 */     Object localObject2 = new LinkedHashSet();
/*  680 */     ((Set)localObject2).addAll(paramList);
/*  681 */     ((Set)localObject2).addAll(paramList1);
/*  682 */     localObject2 = Collections.unmodifiableSet((Set)localObject2);
/*      */ 
/*  684 */     JavacRoundEnvironment localJavacRoundEnvironment = new JavacRoundEnvironment(false, false, (Set)localObject2, this);
/*      */ 
/*  689 */     while ((localHashMap.size() > 0) && (((JavacProcessingEnvironment.DiscoveredProcessors.ProcessorStateIterator)localObject1).hasNext())) {
/*  690 */       ProcessorState localProcessorState = ((JavacProcessingEnvironment.DiscoveredProcessors.ProcessorStateIterator)localObject1).next();
/*  691 */       HashSet localHashSet = new HashSet();
/*  692 */       LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/*      */ 
/*  694 */       for (Map.Entry localEntry : localHashMap.entrySet()) {
/*  695 */         String str = (String)localEntry.getKey();
/*  696 */         if (localProcessorState.annotationSupported(str)) {
/*  697 */           localHashSet.add(str);
/*  698 */           TypeElement localTypeElement = (TypeElement)localEntry.getValue();
/*  699 */           if (localTypeElement != null) {
/*  700 */             localLinkedHashSet.add(localTypeElement);
/*      */           }
/*      */         }
/*      */       }
/*  704 */       if ((localHashSet.size() > 0) || (localProcessorState.contributed)) {
/*  705 */         boolean bool = callProcessor(localProcessorState.processor, localLinkedHashSet, localJavacRoundEnvironment);
/*  706 */         localProcessorState.contributed = true;
/*  707 */         localProcessorState.removeSupportedOptions(this.unmatchedProcessorOptions);
/*      */ 
/*  709 */         if ((this.printProcessorInfo) || (this.verbose)) {
/*  710 */           this.log.printLines("x.print.processor.info", new Object[] { localProcessorState.processor
/*  711 */             .getClass().getName(), localHashSet
/*  712 */             .toString(), 
/*  713 */             Boolean.valueOf(bool) });
/*      */         }
/*      */ 
/*  716 */         if (bool) {
/*  717 */           localHashMap.keySet().removeAll(localHashSet);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  722 */     localHashMap.remove("");
/*      */ 
/*  724 */     if ((this.lint) && (localHashMap.size() > 0))
/*      */     {
/*  726 */       localHashMap.keySet().removeAll(this.platformAnnotations);
/*  727 */       if (localHashMap.size() > 0) {
/*  728 */         this.log = Log.instance(paramContext);
/*  729 */         this.log.warning("proc.annotations.without.processors", new Object[] { localHashMap
/*  730 */           .keySet() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  735 */     ((JavacProcessingEnvironment.DiscoveredProcessors.ProcessorStateIterator)localObject1).runContributingProcs(localJavacRoundEnvironment);
/*      */ 
/*  738 */     if (this.options.isSet("displayFilerState"))
/*  739 */       this.filer.displayState();
/*      */   }
/*      */ 
/*      */   private boolean callProcessor(Processor paramProcessor, Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment)
/*      */   {
/*      */     try
/*      */     {
/*  794 */       return paramProcessor.process(paramSet, paramRoundEnvironment);
/*      */     } catch (ClassReader.BadClassFile localBadClassFile) {
/*  796 */       this.log.error("proc.cant.access.1", new Object[] { localBadClassFile.sym, localBadClassFile.getDetailValue() });
/*  797 */       return false;
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*  799 */       StringWriter localStringWriter = new StringWriter();
/*  800 */       localCompletionFailure.printStackTrace(new PrintWriter(localStringWriter));
/*  801 */       this.log.error("proc.cant.access", new Object[] { localCompletionFailure.sym, localCompletionFailure.getDetailValue(), localStringWriter.toString() });
/*  802 */       return false;
/*      */     } catch (ClientCodeException localClientCodeException) {
/*  804 */       throw localClientCodeException;
/*      */     } catch (Throwable localThrowable) {
/*  806 */       throw new AnnotationProcessingError(localThrowable);
/*      */     }
/*      */   }
/*      */ 
/*      */   public JavaCompiler doProcessing(Context paramContext, com.sun.tools.javac.util.List<JCTree.JCCompilationUnit> paramList, com.sun.tools.javac.util.List<Symbol.ClassSymbol> paramList1, Iterable<? extends Symbol.PackageSymbol> paramIterable, Log.DeferredDiagnosticHandler paramDeferredDiagnosticHandler)
/*      */   {
/* 1163 */     this.log = Log.instance(paramContext);
/*      */ 
/* 1165 */     LinkedHashSet localLinkedHashSet1 = new LinkedHashSet();
/* 1166 */     for (Object localObject = paramIterable.iterator(); ((Iterator)localObject).hasNext(); ) { Symbol.PackageSymbol localPackageSymbol = (Symbol.PackageSymbol)((Iterator)localObject).next();
/* 1167 */       localLinkedHashSet1.add(localPackageSymbol); }
/* 1168 */     this.specifiedPackages = Collections.unmodifiableSet(localLinkedHashSet1);
/*      */ 
/* 1170 */     localObject = new Round(paramContext, paramList, paramList1, paramDeferredDiagnosticHandler);
/*      */     boolean bool2;
/*      */     do
/*      */     {
/* 1176 */       ((Round)localObject).run(false, false);
/*      */ 
/* 1180 */       bool1 = ((Round)localObject).unrecoverableError();
/* 1181 */       bool2 = moreToDo();
/*      */ 
/* 1183 */       ((Round)localObject).showDiagnostics((bool1) || (this.showResolveErrors));
/*      */ 
/* 1187 */       localObject = ((Round)localObject).next(new LinkedHashSet(this.filer
/* 1188 */         .getGeneratedSourceFileObjects()), new LinkedHashMap(this.filer
/* 1189 */         .getGeneratedClasses()));
/*      */ 
/* 1192 */       if (((Round)localObject).unrecoverableError())
/* 1193 */         bool1 = true;
/*      */     }
/* 1195 */     while ((bool2) && (!bool1));
/*      */ 
/* 1198 */     ((Round)localObject).run(true, bool1);
/* 1199 */     ((Round)localObject).showDiagnostics(true);
/*      */ 
/* 1201 */     this.filer.warnIfUnclosedFiles();
/* 1202 */     warnIfUnmatchedOptions();
/*      */ 
/* 1215 */     if ((this.messager.errorRaised()) || ((this.werror) && 
/* 1216 */       (((Round)localObject)
/* 1216 */       .warningCount() > 0) && (((Round)localObject).errorCount() > 0))) {
/* 1217 */       bool1 = true;
/*      */     }
/*      */ 
/* 1220 */     LinkedHashSet localLinkedHashSet2 = new LinkedHashSet(this.filer
/* 1220 */       .getGeneratedSourceFileObjects());
/* 1221 */     paramList = cleanTrees(((Round)localObject).roots);
/*      */ 
/* 1223 */     JavaCompiler localJavaCompiler = ((Round)localObject).finalCompiler();
/*      */ 
/* 1225 */     if (localLinkedHashSet2.size() > 0) {
/* 1226 */       paramList = paramList.appendList(localJavaCompiler.parseFiles(localLinkedHashSet2));
/*      */     }
/* 1228 */     boolean bool1 = (bool1) || (localJavaCompiler.errorCount() > 0);
/*      */ 
/* 1231 */     close();
/*      */ 
/* 1233 */     if (!this.taskListener.isEmpty()) {
/* 1234 */       this.taskListener.finished(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING));
/*      */     }
/* 1236 */     if (bool1) {
/* 1237 */       if (localJavaCompiler.errorCount() == 0)
/* 1238 */         localJavaCompiler.log.nerrors += 1;
/* 1239 */       return localJavaCompiler;
/*      */     }
/*      */ 
/* 1242 */     localJavaCompiler.enterTreesIfNeeded(paramList);
/*      */ 
/* 1244 */     return localJavaCompiler;
/*      */   }
/*      */ 
/*      */   private void warnIfUnmatchedOptions() {
/* 1248 */     if (!this.unmatchedProcessorOptions.isEmpty())
/* 1249 */       this.log.warning("proc.unmatched.processor.options", new Object[] { this.unmatchedProcessorOptions.toString() });
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 1257 */     this.filer.close();
/* 1258 */     if (this.discoveredProcs != null)
/* 1259 */       this.discoveredProcs.close();
/* 1260 */     this.discoveredProcs = null;
/*      */   }
/*      */ 
/*      */   private com.sun.tools.javac.util.List<Symbol.ClassSymbol> getTopLevelClasses(com.sun.tools.javac.util.List<? extends JCTree.JCCompilationUnit> paramList) {
/* 1264 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1265 */     for (JCTree.JCCompilationUnit localJCCompilationUnit : paramList) {
/* 1266 */       for (JCTree localJCTree : localJCCompilationUnit.defs) {
/* 1267 */         if (localJCTree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 1268 */           Symbol.ClassSymbol localClassSymbol = ((JCTree.JCClassDecl)localJCTree).sym;
/* 1269 */           Assert.checkNonNull(localClassSymbol);
/* 1270 */           localList = localList.prepend(localClassSymbol);
/*      */         }
/*      */       }
/*      */     }
/* 1274 */     return localList.reverse();
/*      */   }
/*      */ 
/*      */   private com.sun.tools.javac.util.List<Symbol.ClassSymbol> getTopLevelClassesFromClasses(com.sun.tools.javac.util.List<? extends Symbol.ClassSymbol> paramList) {
/* 1278 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1279 */     for (Symbol.ClassSymbol localClassSymbol : paramList) {
/* 1280 */       if (!isPkgInfo(localClassSymbol)) {
/* 1281 */         localList = localList.prepend(localClassSymbol);
/*      */       }
/*      */     }
/* 1284 */     return localList.reverse();
/*      */   }
/*      */ 
/*      */   private com.sun.tools.javac.util.List<Symbol.PackageSymbol> getPackageInfoFiles(com.sun.tools.javac.util.List<? extends JCTree.JCCompilationUnit> paramList) {
/* 1288 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1289 */     for (JCTree.JCCompilationUnit localJCCompilationUnit : paramList) {
/* 1290 */       if (isPkgInfo(localJCCompilationUnit.sourcefile, JavaFileObject.Kind.SOURCE)) {
/* 1291 */         localList = localList.prepend(localJCCompilationUnit.packge);
/*      */       }
/*      */     }
/* 1294 */     return localList.reverse();
/*      */   }
/*      */ 
/*      */   private com.sun.tools.javac.util.List<Symbol.PackageSymbol> getPackageInfoFilesFromClasses(com.sun.tools.javac.util.List<? extends Symbol.ClassSymbol> paramList) {
/* 1298 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1299 */     for (Symbol.ClassSymbol localClassSymbol : paramList) {
/* 1300 */       if (isPkgInfo(localClassSymbol)) {
/* 1301 */         localList = localList.prepend((Symbol.PackageSymbol)localClassSymbol.owner);
/*      */       }
/*      */     }
/* 1304 */     return localList.reverse();
/*      */   }
/*      */ 
/*      */   private static <T> com.sun.tools.javac.util.List<T> join(com.sun.tools.javac.util.List<T> paramList1, com.sun.tools.javac.util.List<T> paramList2)
/*      */   {
/* 1309 */     return paramList1.appendList(paramList2);
/*      */   }
/*      */ 
/*      */   private boolean isPkgInfo(JavaFileObject paramJavaFileObject, JavaFileObject.Kind paramKind) {
/* 1313 */     return paramJavaFileObject.isNameCompatible("package-info", paramKind);
/*      */   }
/*      */ 
/*      */   private boolean isPkgInfo(Symbol.ClassSymbol paramClassSymbol) {
/* 1317 */     return (isPkgInfo(paramClassSymbol.classfile, JavaFileObject.Kind.CLASS)) && (paramClassSymbol.packge().package_info == paramClassSymbol);
/*      */   }
/*      */ 
/*      */   private boolean needClassLoader(String paramString, Iterable<? extends File> paramIterable)
/*      */   {
/* 1325 */     if (paramString != null) {
/* 1326 */       return true;
/*      */     }
/* 1328 */     URL[] arrayOfURL = new URL[1];
/* 1329 */     for (File localFile : paramIterable) {
/*      */       try {
/* 1331 */         arrayOfURL[0] = localFile.toURI().toURL();
/* 1332 */         if (ServiceProxy.hasService(Processor.class, arrayOfURL))
/* 1333 */           return true;
/*      */       } catch (MalformedURLException localMalformedURLException) {
/* 1335 */         throw new AssertionError(localMalformedURLException);
/*      */       }
/*      */       catch (ServiceProxy.ServiceConfigurationError localServiceConfigurationError) {
/* 1338 */         this.log.error("proc.bad.config.file", new Object[] { localServiceConfigurationError.getLocalizedMessage() });
/* 1339 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1343 */     return false;
/*      */   }
/*      */ 
/*      */   private static <T extends JCTree> com.sun.tools.javac.util.List<T> cleanTrees(com.sun.tools.javac.util.List<T> paramList) {
/* 1347 */     for (JCTree localJCTree : paramList)
/* 1348 */       treeCleaner.scan(localJCTree);
/* 1349 */     return paramList;
/*      */   }
/*      */ 
/*      */   private boolean moreToDo()
/*      */   {
/* 1406 */     return this.filer.newFiles();
/*      */   }
/*      */ 
/*      */   public Map<String, String> getOptions()
/*      */   {
/* 1417 */     return this.processorOptions;
/*      */   }
/*      */ 
/*      */   public Messager getMessager() {
/* 1421 */     return this.messager;
/*      */   }
/*      */ 
/*      */   public Filer getFiler() {
/* 1425 */     return this.filer;
/*      */   }
/*      */ 
/*      */   public JavacElements getElementUtils() {
/* 1429 */     return this.elementUtils;
/*      */   }
/*      */ 
/*      */   public JavacTypes getTypeUtils() {
/* 1433 */     return this.typeUtils;
/*      */   }
/*      */ 
/*      */   public SourceVersion getSourceVersion() {
/* 1437 */     return Source.toSourceVersion(this.source);
/*      */   }
/*      */ 
/*      */   public Locale getLocale() {
/* 1441 */     return this.messages.getCurrentLocale();
/*      */   }
/*      */ 
/*      */   public Set<Symbol.PackageSymbol> getSpecifiedPackages() {
/* 1445 */     return this.specifiedPackages;
/*      */   }
/*      */ 
/*      */   private static Pattern importStringToPattern(String paramString, Processor paramProcessor, Log paramLog)
/*      */   {
/* 1457 */     if (isValidImportString(paramString)) {
/* 1458 */       return validImportStringToPattern(paramString);
/*      */     }
/* 1460 */     paramLog.warning("proc.malformed.supported.string", new Object[] { paramString, paramProcessor.getClass().getName() });
/* 1461 */     return noMatches;
/*      */   }
/*      */ 
/*      */   public static boolean isValidImportString(String paramString)
/*      */   {
/* 1470 */     if (paramString.equals("*")) {
/* 1471 */       return true;
/*      */     }
/* 1473 */     boolean bool = true;
/* 1474 */     String str1 = paramString;
/* 1475 */     int i = str1.indexOf('*');
/*      */ 
/* 1477 */     if (i != -1)
/*      */     {
/* 1479 */       if (i == str1.length() - 1)
/*      */       {
/* 1481 */         if (i - 1 >= 0) {
/* 1482 */           bool = str1.charAt(i - 1) == '.';
/*      */ 
/* 1484 */           str1 = str1.substring(0, str1.length() - 2);
/*      */         }
/*      */       }
/* 1487 */       else return false;
/*      */ 
/*      */     }
/*      */ 
/* 1491 */     if (bool) {
/* 1492 */       String[] arrayOfString1 = str1.split("\\.", str1.length() + 2);
/* 1493 */       for (String str2 : arrayOfString1)
/* 1494 */         bool &= SourceVersion.isIdentifier(str2);
/*      */     }
/* 1496 */     return bool;
/*      */   }
/*      */ 
/*      */   public static Pattern validImportStringToPattern(String paramString) {
/* 1500 */     if (paramString.equals("*")) {
/* 1501 */       return allMatches;
/*      */     }
/* 1503 */     String str = paramString.replace(".", "\\.");
/*      */ 
/* 1505 */     if (str.endsWith("*")) {
/* 1506 */       str = str.substring(0, str.length() - 1) + ".+";
/*      */     }
/*      */ 
/* 1509 */     return Pattern.compile(str);
/*      */   }
/*      */ 
/*      */   public Context getContext()
/*      */   {
/* 1517 */     return this.context;
/*      */   }
/*      */ 
/*      */   public ClassLoader getProcessorClassLoader()
/*      */   {
/* 1524 */     return this.processorClassLoader;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1528 */     return "javac ProcessingEnvironment";
/*      */   }
/*      */ 
/*      */   public static boolean isValidOptionName(String paramString) {
/* 1532 */     for (String str : paramString.split("\\.", -1)) {
/* 1533 */       if (!SourceVersion.isIdentifier(str))
/* 1534 */         return false;
/*      */     }
/* 1536 */     return true;
/*      */   }
/*      */ 
/*      */   public static class ComputeAnnotationSet extends ElementScanner8<Set<TypeElement>, Set<TypeElement>>
/*      */   {
/*      */     final Elements elements;
/*      */ 
/*      */     public ComputeAnnotationSet(Elements paramElements)
/*      */     {
/*  752 */       this.elements = paramElements;
/*      */     }
/*      */ 
/*      */     public Set<TypeElement> visitPackage(PackageElement paramPackageElement, Set<TypeElement> paramSet)
/*      */     {
/*  758 */       return paramSet;
/*      */     }
/*      */ 
/*      */     public Set<TypeElement> visitType(TypeElement paramTypeElement, Set<TypeElement> paramSet)
/*      */     {
/*  764 */       scan(paramTypeElement.getTypeParameters(), paramSet);
/*  765 */       return (Set)super.visitType(paramTypeElement, paramSet);
/*      */     }
/*      */ 
/*      */     public Set<TypeElement> visitExecutable(ExecutableElement paramExecutableElement, Set<TypeElement> paramSet)
/*      */     {
/*  771 */       scan(paramExecutableElement.getTypeParameters(), paramSet);
/*  772 */       return (Set)super.visitExecutable(paramExecutableElement, paramSet);
/*      */     }
/*      */ 
/*      */     void addAnnotations(Element paramElement, Set<TypeElement> paramSet)
/*      */     {
/*  777 */       for (AnnotationMirror localAnnotationMirror : this.elements.getAllAnnotationMirrors(paramElement)) {
/*  778 */         Element localElement = localAnnotationMirror.getAnnotationType().asElement();
/*  779 */         paramSet.add((TypeElement)localElement);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Set<TypeElement> scan(Element paramElement, Set<TypeElement> paramSet)
/*      */     {
/*  785 */       addAnnotations(paramElement, paramSet);
/*  786 */       return (Set)super.scan(paramElement, paramSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   class DiscoveredProcessors
/*      */     implements Iterable<JavacProcessingEnvironment.ProcessorState>
/*      */   {
/*      */     Iterator<? extends Processor> processorIterator;
/*      */     ArrayList<JavacProcessingEnvironment.ProcessorState> procStateList;
/*      */ 
/*      */     public ProcessorStateIterator iterator()
/*      */     {
/*  637 */       return new ProcessorStateIterator(this);
/*      */     }
/*      */ 
/*      */     DiscoveredProcessors()
/*      */     {
/*      */       Object localObject;
/*  641 */       this.processorIterator = localObject;
/*  642 */       this.procStateList = new ArrayList();
/*      */     }
/*      */ 
/*      */     public void close()
/*      */     {
/*  649 */       if ((this.processorIterator != null) && ((this.processorIterator instanceof JavacProcessingEnvironment.ServiceIterator)))
/*      */       {
/*  651 */         ((JavacProcessingEnvironment.ServiceIterator)this.processorIterator).close();
/*      */       }
/*      */     }
/*      */ 
/*      */     class ProcessorStateIterator
/*      */       implements Iterator<JavacProcessingEnvironment.ProcessorState>
/*      */     {
/*      */       JavacProcessingEnvironment.DiscoveredProcessors psi;
/*      */       Iterator<JavacProcessingEnvironment.ProcessorState> innerIter;
/*      */       boolean onProcInterator;
/*      */ 
/*      */       ProcessorStateIterator(JavacProcessingEnvironment.DiscoveredProcessors arg2)
/*      */       {
/*      */         Object localObject;
/*  583 */         this.psi = localObject;
/*  584 */         this.innerIter = localObject.procStateList.iterator();
/*  585 */         this.onProcInterator = false;
/*      */       }
/*      */ 
/*      */       public JavacProcessingEnvironment.ProcessorState next() {
/*  589 */         if (!this.onProcInterator) {
/*  590 */           if (this.innerIter.hasNext()) {
/*  591 */             return (JavacProcessingEnvironment.ProcessorState)this.innerIter.next();
/*      */           }
/*  593 */           this.onProcInterator = true;
/*      */         }
/*      */ 
/*  596 */         if (this.psi.processorIterator.hasNext()) {
/*  597 */           JavacProcessingEnvironment.ProcessorState localProcessorState = new JavacProcessingEnvironment.ProcessorState((Processor)this.psi.processorIterator.next(), JavacProcessingEnvironment.this.log, JavacProcessingEnvironment.this.source, JavacProcessingEnvironment.this);
/*      */ 
/*  599 */           this.psi.procStateList.add(localProcessorState);
/*  600 */           return localProcessorState;
/*      */         }
/*  602 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       public boolean hasNext() {
/*  606 */         if (this.onProcInterator) {
/*  607 */           return this.psi.processorIterator.hasNext();
/*      */         }
/*  609 */         return (this.innerIter.hasNext()) || (this.psi.processorIterator.hasNext());
/*      */       }
/*      */ 
/*      */       public void remove() {
/*  613 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       public void runContributingProcs(RoundEnvironment paramRoundEnvironment)
/*      */       {
/*  622 */         if (!this.onProcInterator) {
/*  623 */           Set localSet = Collections.emptySet();
/*  624 */           while (this.innerIter.hasNext()) {
/*  625 */             JavacProcessingEnvironment.ProcessorState localProcessorState = (JavacProcessingEnvironment.ProcessorState)this.innerIter.next();
/*  626 */             if (localProcessorState.contributed)
/*  627 */               JavacProcessingEnvironment.this.callProcessor(localProcessorState.processor, localSet, paramRoundEnvironment);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class NameProcessIterator
/*      */     implements Iterator<Processor>
/*      */   {
/*  385 */     Processor nextProc = null;
/*      */     Iterator<String> names;
/*      */     ClassLoader processorCL;
/*      */     Log log;
/*      */ 
/*      */     NameProcessIterator(String paramString, ClassLoader paramClassLoader, Log paramLog)
/*      */     {
/*  391 */       this.names = Arrays.asList(paramString.split(",")).iterator();
/*  392 */       this.processorCL = paramClassLoader;
/*  393 */       this.log = paramLog;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*  397 */       if (this.nextProc != null) {
/*  398 */         return true;
/*      */       }
/*  400 */       if (!this.names.hasNext()) {
/*  401 */         return false;
/*      */       }
/*  403 */       String str = (String)this.names.next();
/*      */       Processor localProcessor;
/*      */       try
/*      */       {
/*      */         try {
/*  409 */           localProcessor = (Processor)this.processorCL
/*  409 */             .loadClass(str)
/*  409 */             .newInstance();
/*      */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  411 */           this.log.error("proc.processor.not.found", new Object[] { str });
/*  412 */           return false;
/*      */         } catch (ClassCastException localClassCastException) {
/*  414 */           this.log.error("proc.processor.wrong.type", new Object[] { str });
/*  415 */           return false;
/*      */         } catch (Exception localException) {
/*  417 */           this.log.error("proc.processor.cant.instantiate", new Object[] { str });
/*  418 */           return false;
/*      */         }
/*      */       } catch (ClientCodeException localClientCodeException) {
/*  421 */         throw localClientCodeException;
/*      */       } catch (Throwable localThrowable) {
/*  423 */         throw new AnnotationProcessingError(localThrowable);
/*      */       }
/*  425 */       this.nextProc = localProcessor;
/*  426 */       return true;
/*      */     }
/*      */ 
/*      */     public Processor next()
/*      */     {
/*  433 */       if (hasNext()) {
/*  434 */         Processor localProcessor = this.nextProc;
/*  435 */         this.nextProc = null;
/*  436 */         return localProcessor;
/*      */       }
/*  438 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  442 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ProcessorState
/*      */   {
/*      */     public Processor processor;
/*      */     public boolean contributed;
/*      */     private ArrayList<Pattern> supportedAnnotationPatterns;
/*      */     private ArrayList<String> supportedOptionNames;
/*      */ 
/*      */     ProcessorState(Processor paramProcessor, Log paramLog, Source paramSource, ProcessingEnvironment paramProcessingEnvironment)
/*      */     {
/*  496 */       this.processor = paramProcessor;
/*  497 */       this.contributed = false;
/*      */       try
/*      */       {
/*  500 */         this.processor.init(paramProcessingEnvironment);
/*      */ 
/*  502 */         checkSourceVersionCompatibility(paramSource, paramLog);
/*      */ 
/*  504 */         this.supportedAnnotationPatterns = new ArrayList();
/*  505 */         for (localIterator = this.processor.getSupportedAnnotationTypes().iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/*  506 */           this.supportedAnnotationPatterns.add(JavacProcessingEnvironment.importStringToPattern(str, this.processor, paramLog));
/*      */         }
/*      */ 
/*  511 */         this.supportedOptionNames = new ArrayList();
/*  512 */         for (localIterator = this.processor.getSupportedOptions().iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/*  513 */           if (checkOptionName(str, paramLog))
/*  514 */             this.supportedOptionNames.add(str);
/*      */         }
/*      */       }
/*      */       catch (ClientCodeException localClientCodeException)
/*      */       {
/*      */         Iterator localIterator;
/*      */         String str;
/*  518 */         throw localClientCodeException;
/*      */       } catch (Throwable localThrowable) {
/*  520 */         throw new AnnotationProcessingError(localThrowable);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void checkSourceVersionCompatibility(Source paramSource, Log paramLog)
/*      */     {
/*  531 */       SourceVersion localSourceVersion = this.processor.getSupportedSourceVersion();
/*      */ 
/*  533 */       if (localSourceVersion.compareTo(Source.toSourceVersion(paramSource)) < 0)
/*  534 */         paramLog.warning("proc.processor.incompatible.source.version", new Object[] { localSourceVersion, this.processor
/*  536 */           .getClass().getName(), paramSource.name });
/*      */     }
/*      */ 
/*      */     private boolean checkOptionName(String paramString, Log paramLog)
/*      */     {
/*  542 */       boolean bool = JavacProcessingEnvironment.isValidOptionName(paramString);
/*  543 */       if (!bool)
/*  544 */         paramLog.error("proc.processor.bad.option.name", new Object[] { paramString, this.processor
/*  546 */           .getClass().getName() });
/*  547 */       return bool;
/*      */     }
/*      */ 
/*      */     public boolean annotationSupported(String paramString) {
/*  551 */       for (Pattern localPattern : this.supportedAnnotationPatterns) {
/*  552 */         if (localPattern.matcher(paramString).matches())
/*  553 */           return true;
/*      */       }
/*  555 */       return false;
/*      */     }
/*      */ 
/*      */     public void removeSupportedOptions(Set<String> paramSet)
/*      */     {
/*  562 */       paramSet.removeAll(this.supportedOptionNames);
/*      */     }
/*      */   }
/*      */ 
/*      */   class Round
/*      */   {
/*      */     final int number;
/*      */     final Context context;
/*      */     final JavaCompiler compiler;
/*      */     final Log log;
/*      */     final Log.DeferredDiagnosticHandler deferredDiagnosticHandler;
/*      */     com.sun.tools.javac.util.List<JCTree.JCCompilationUnit> roots;
/*      */     Map<String, JavaFileObject> genClassFiles;
/*      */     Set<TypeElement> annotationsPresent;
/*      */     com.sun.tools.javac.util.List<Symbol.ClassSymbol> topLevelClasses;
/*      */     com.sun.tools.javac.util.List<Symbol.PackageSymbol> packageInfoFiles;
/*      */ 
/*      */     private Round(Context paramInt1, int paramInt2, int paramInt3, int paramDeferredDiagnosticHandler, Log.DeferredDiagnosticHandler arg6)
/*      */     {
/*  840 */       this.context = paramInt1;
/*  841 */       this.number = paramInt2;
/*      */ 
/*  843 */       this.compiler = JavaCompiler.instance(paramInt1);
/*  844 */       this.log = Log.instance(paramInt1);
/*  845 */       this.log.nerrors = paramInt3;
/*  846 */       this.log.nwarnings = paramDeferredDiagnosticHandler;
/*  847 */       if (paramInt2 == 1)
/*      */       {
/*      */         Object localObject;
/*  848 */         Assert.checkNonNull(localObject);
/*  849 */         this.deferredDiagnosticHandler = localObject;
/*      */       } else {
/*  851 */         this.deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log);
/*      */       }
/*      */ 
/*  855 */       JavacProcessingEnvironment.this.context = paramInt1;
/*      */ 
/*  858 */       this.topLevelClasses = com.sun.tools.javac.util.List.nil();
/*  859 */       this.packageInfoFiles = com.sun.tools.javac.util.List.nil();
/*      */     }
/*      */ 
/*      */     Round(com.sun.tools.javac.util.List<JCTree.JCCompilationUnit> paramList, com.sun.tools.javac.util.List<Symbol.ClassSymbol> paramDeferredDiagnosticHandler, Log.DeferredDiagnosticHandler arg4)
/*      */     {
/*  865 */       this(paramList, 1, 0, 0, localDeferredDiagnosticHandler);
/*  866 */       this.roots = paramDeferredDiagnosticHandler;
/*  867 */       this.genClassFiles = new HashMap();
/*      */ 
/*  869 */       this.compiler.todo.clear();
/*      */       Object localObject;
/*  874 */       this.topLevelClasses = JavacProcessingEnvironment.this
/*  875 */         .getTopLevelClasses(paramDeferredDiagnosticHandler)
/*  875 */         .prependList(localObject.reverse());
/*      */ 
/*  877 */       this.packageInfoFiles = JavacProcessingEnvironment.this.getPackageInfoFiles(paramDeferredDiagnosticHandler);
/*      */ 
/*  879 */       findAnnotationsPresent();
/*      */     }
/*      */ 
/*      */     private Round(Set<JavaFileObject> paramMap, Map<String, JavaFileObject> arg3)
/*      */     {
/*  885 */       this(paramMap.nextContext(), paramMap.number + 1, paramMap.compiler.log.nerrors, paramMap.compiler.log.nwarnings, null);
/*      */ 
/*  890 */       this.genClassFiles = paramMap.genClassFiles;
/*      */       Iterable localIterable;
/*  892 */       com.sun.tools.javac.util.List localList1 = this.compiler.parseFiles(localIterable);
/*  893 */       this.roots = JavacProcessingEnvironment.cleanTrees(paramMap.roots).appendList(localList1);
/*      */ 
/*  896 */       if (unrecoverableError()) {
/*  897 */         return;
/*      */       }
/*  899 */       enterClassFiles(this.genClassFiles);
/*      */       Map localMap;
/*  900 */       com.sun.tools.javac.util.List localList2 = enterClassFiles(localMap);
/*  901 */       this.genClassFiles.putAll(localMap);
/*  902 */       enterTrees(this.roots);
/*      */ 
/*  904 */       if (unrecoverableError()) {
/*  905 */         return;
/*      */       }
/*  907 */       this.topLevelClasses = JavacProcessingEnvironment.join(
/*  908 */         JavacProcessingEnvironment.access$400(JavacProcessingEnvironment.this, localList1), 
/*  909 */         JavacProcessingEnvironment.access$700(JavacProcessingEnvironment.this, localList2));
/*      */ 
/*  911 */       this.packageInfoFiles = JavacProcessingEnvironment.join(
/*  912 */         JavacProcessingEnvironment.access$500(JavacProcessingEnvironment.this, localList1), 
/*  913 */         JavacProcessingEnvironment.access$900(JavacProcessingEnvironment.this, localList2));
/*      */ 
/*  915 */       findAnnotationsPresent();
/*      */     }
/*      */ 
/*      */     Round next(Set<JavaFileObject> paramSet, Map<String, JavaFileObject> paramMap)
/*      */     {
/*      */       try {
/*  921 */         return new Round(JavacProcessingEnvironment.this, this, paramSet, paramMap);
/*      */       } finally {
/*  923 */         this.compiler.close(false);
/*      */       }
/*      */     }
/*      */ 
/*      */     JavaCompiler finalCompiler()
/*      */     {
/*      */       try {
/*  930 */         Context localContext = nextContext();
/*  931 */         JavacProcessingEnvironment.this.context = localContext;
/*  932 */         JavaCompiler localJavaCompiler1 = JavaCompiler.instance(localContext);
/*  933 */         localJavaCompiler1.log.initRound(this.compiler.log);
/*  934 */         return localJavaCompiler1;
/*      */       } finally {
/*  936 */         this.compiler.close(false);
/*      */       }
/*      */     }
/*      */ 
/*      */     int errorCount()
/*      */     {
/*  944 */       return this.compiler.errorCount();
/*      */     }
/*      */ 
/*      */     int warningCount()
/*      */     {
/*  949 */       return this.compiler.warningCount();
/*      */     }
/*      */ 
/*      */     boolean unrecoverableError()
/*      */     {
/*  954 */       if (JavacProcessingEnvironment.this.messager.errorRaised()) {
/*  955 */         return true;
/*      */       }
/*  957 */       for (JCDiagnostic localJCDiagnostic : this.deferredDiagnosticHandler.getDiagnostics()) {
/*  958 */         switch (JavacProcessingEnvironment.2.$SwitchMap$javax$tools$Diagnostic$Kind[localJCDiagnostic.getKind().ordinal()]) {
/*      */         case 1:
/*  960 */           if (JavacProcessingEnvironment.this.werror) {
/*  961 */             return true;
/*      */           }
/*      */           break;
/*      */         case 2:
/*  965 */           if ((JavacProcessingEnvironment.this.fatalErrors) || (!localJCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.RECOVERABLE))) {
/*  966 */             return true;
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*  971 */       return false;
/*      */     }
/*      */ 
/*      */     void findAnnotationsPresent()
/*      */     {
/*  977 */       JavacProcessingEnvironment.ComputeAnnotationSet localComputeAnnotationSet = new JavacProcessingEnvironment.ComputeAnnotationSet(JavacProcessingEnvironment.this.elementUtils);
/*      */ 
/*  979 */       this.annotationsPresent = new LinkedHashSet();
/*  980 */       for (Iterator localIterator = this.topLevelClasses.iterator(); localIterator.hasNext(); ) { localObject = (Symbol.ClassSymbol)localIterator.next();
/*  981 */         localComputeAnnotationSet.scan((Element)localObject, this.annotationsPresent);
/*      */       }
/*  982 */       Object localObject;
/*  982 */       for (localIterator = this.packageInfoFiles.iterator(); localIterator.hasNext(); ) { localObject = (Symbol.PackageSymbol)localIterator.next();
/*  983 */         localComputeAnnotationSet.scan((Element)localObject, this.annotationsPresent); }
/*      */     }
/*      */ 
/*      */     private com.sun.tools.javac.util.List<Symbol.ClassSymbol> enterClassFiles(Map<String, JavaFileObject> paramMap)
/*      */     {
/*  988 */       ClassReader localClassReader = ClassReader.instance(this.context);
/*  989 */       Names localNames = Names.instance(this.context);
/*  990 */       com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/*      */ 
/*  992 */       for (Map.Entry localEntry : paramMap.entrySet()) {
/*  993 */         Name localName1 = localNames.fromString((String)localEntry.getKey());
/*  994 */         JavaFileObject localJavaFileObject = (JavaFileObject)localEntry.getValue();
/*  995 */         if (localJavaFileObject.getKind() != JavaFileObject.Kind.CLASS)
/*  996 */           throw new AssertionError(localJavaFileObject);
/*      */         Symbol.ClassSymbol localClassSymbol;
/*  998 */         if (JavacProcessingEnvironment.this.isPkgInfo(localJavaFileObject, JavaFileObject.Kind.CLASS)) {
/*  999 */           Name localName2 = Convert.packagePart(localName1);
/* 1000 */           Symbol.PackageSymbol localPackageSymbol = localClassReader.enterPackage(localName2);
/* 1001 */           if (localPackageSymbol.package_info == null)
/* 1002 */             localPackageSymbol.package_info = localClassReader.enterClass(Convert.shortName(localName1), localPackageSymbol);
/* 1003 */           localClassSymbol = localPackageSymbol.package_info;
/* 1004 */           if (localClassSymbol.classfile == null)
/* 1005 */             localClassSymbol.classfile = localJavaFileObject;
/*      */         } else {
/* 1007 */           localClassSymbol = localClassReader.enterClass(localName1, localJavaFileObject);
/* 1008 */         }localList = localList.prepend(localClassSymbol);
/*      */       }
/* 1010 */       return localList.reverse();
/*      */     }
/*      */ 
/*      */     private void enterTrees(com.sun.tools.javac.util.List<JCTree.JCCompilationUnit> paramList)
/*      */     {
/* 1015 */       this.compiler.enterTrees(paramList);
/*      */     }
/*      */ 
/*      */     void run(boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1020 */       printRoundInfo(paramBoolean1);
/*      */ 
/* 1022 */       if (!JavacProcessingEnvironment.this.taskListener.isEmpty())
/* 1023 */         JavacProcessingEnvironment.this.taskListener.started(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING_ROUND));
/*      */       try
/*      */       {
/* 1026 */         if (paramBoolean1) {
/* 1027 */           JavacProcessingEnvironment.this.filer.setLastRound(true);
/* 1028 */           Set localSet = Collections.emptySet();
/* 1029 */           JavacRoundEnvironment localJavacRoundEnvironment = new JavacRoundEnvironment(true, paramBoolean2, localSet, JavacProcessingEnvironment.this);
/*      */ 
/* 1033 */           JavacProcessingEnvironment.this.discoveredProcs.iterator().runContributingProcs(localJavacRoundEnvironment);
/*      */         } else {
/* 1035 */           JavacProcessingEnvironment.this.discoverAndRunProcs(this.context, this.annotationsPresent, this.topLevelClasses, this.packageInfoFiles);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Throwable localThrowable)
/*      */       {
/* 1041 */         this.deferredDiagnosticHandler.reportDeferredDiagnostics();
/* 1042 */         this.log.popDiagnosticHandler(this.deferredDiagnosticHandler);
/* 1043 */         throw localThrowable;
/*      */       } finally {
/* 1045 */         if (!JavacProcessingEnvironment.this.taskListener.isEmpty())
/* 1046 */           JavacProcessingEnvironment.this.taskListener.finished(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING_ROUND));
/*      */       }
/*      */     }
/*      */ 
/*      */     void showDiagnostics(boolean paramBoolean) {
/* 1051 */       EnumSet localEnumSet = EnumSet.allOf(Diagnostic.Kind.class);
/* 1052 */       if (!paramBoolean)
/*      */       {
/* 1054 */         localEnumSet.remove(Diagnostic.Kind.ERROR);
/*      */       }
/* 1056 */       this.deferredDiagnosticHandler.reportDeferredDiagnostics(localEnumSet);
/* 1057 */       this.log.popDiagnosticHandler(this.deferredDiagnosticHandler);
/*      */     }
/*      */ 
/*      */     private void printRoundInfo(boolean paramBoolean)
/*      */     {
/* 1062 */       if ((JavacProcessingEnvironment.this.printRounds) || (JavacProcessingEnvironment.this.verbose)) {
/* 1063 */         com.sun.tools.javac.util.List localList = paramBoolean ? com.sun.tools.javac.util.List.nil() : this.topLevelClasses;
/* 1064 */         Set localSet = paramBoolean ? Collections.emptySet() : this.annotationsPresent;
/* 1065 */         this.log.printLines("x.print.rounds", new Object[] { 
/* 1066 */           Integer.valueOf(this.number), 
/* 1066 */           "{" + localList
/* 1067 */           .toString(", ") + 
/* 1067 */           "}", localSet, 
/* 1069 */           Boolean.valueOf(paramBoolean) });
/*      */       }
/*      */     }
/*      */ 
/*      */     private Context nextContext()
/*      */     {
/* 1078 */       Context localContext = new Context(this.context);
/*      */ 
/* 1080 */       Options localOptions = Options.instance(this.context);
/* 1081 */       Assert.checkNonNull(localOptions);
/* 1082 */       localContext.put(Options.optionsKey, localOptions);
/*      */ 
/* 1084 */       Locale localLocale = (Locale)this.context.get(Locale.class);
/* 1085 */       if (localLocale != null) {
/* 1086 */         localContext.put(Locale.class, localLocale);
/*      */       }
/* 1088 */       Assert.checkNonNull(JavacProcessingEnvironment.this.messages);
/* 1089 */       localContext.put(JavacMessages.messagesKey, JavacProcessingEnvironment.this.messages);
/*      */ 
/* 1093 */       Object localObject = Names.instance(this.context);
/* 1094 */       Assert.checkNonNull(localObject);
/* 1095 */       localContext.put(Names.namesKey, localObject);
/*      */ 
/* 1098 */       localObject = (DiagnosticListener)this.context.get(DiagnosticListener.class);
/* 1099 */       if (localObject != null) {
/* 1100 */         localContext.put(DiagnosticListener.class, localObject);
/*      */       }
/* 1102 */       MultiTaskListener localMultiTaskListener = (MultiTaskListener)this.context.get(MultiTaskListener.taskListenerKey);
/* 1103 */       if (localMultiTaskListener != null) {
/* 1104 */         localContext.put(MultiTaskListener.taskListenerKey, localMultiTaskListener);
/*      */       }
/* 1106 */       FSInfo localFSInfo = (FSInfo)this.context.get(FSInfo.class);
/* 1107 */       if (localFSInfo != null) {
/* 1108 */         localContext.put(FSInfo.class, localFSInfo);
/*      */       }
/* 1110 */       JavaFileManager localJavaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/* 1111 */       Assert.checkNonNull(localJavaFileManager);
/* 1112 */       localContext.put(JavaFileManager.class, localJavaFileManager);
/* 1113 */       if ((localJavaFileManager instanceof JavacFileManager)) {
/* 1114 */         ((JavacFileManager)localJavaFileManager).setContext(localContext);
/*      */       }
/*      */ 
/* 1117 */       Names localNames = Names.instance(this.context);
/* 1118 */       Assert.checkNonNull(localNames);
/* 1119 */       localContext.put(Names.namesKey, localNames);
/*      */ 
/* 1121 */       Tokens localTokens = Tokens.instance(this.context);
/* 1122 */       Assert.checkNonNull(localTokens);
/* 1123 */       localContext.put(Tokens.tokensKey, localTokens);
/*      */ 
/* 1125 */       Log localLog = Log.instance(localContext);
/* 1126 */       localLog.initRound(this.log);
/*      */ 
/* 1128 */       JavaCompiler localJavaCompiler1 = JavaCompiler.instance(this.context);
/* 1129 */       JavaCompiler localJavaCompiler2 = JavaCompiler.instance(localContext);
/* 1130 */       localJavaCompiler2.initRound(localJavaCompiler1);
/*      */ 
/* 1132 */       JavacProcessingEnvironment.this.filer.newRound(localContext);
/* 1133 */       JavacProcessingEnvironment.this.messager.newRound(localContext);
/* 1134 */       JavacProcessingEnvironment.this.elementUtils.setContext(localContext);
/* 1135 */       JavacProcessingEnvironment.this.typeUtils.setContext(localContext);
/*      */ 
/* 1137 */       JavacTask localJavacTask = (JavacTask)this.context.get(JavacTask.class);
/* 1138 */       if (localJavacTask != null) {
/* 1139 */         localContext.put(JavacTask.class, localJavacTask);
/* 1140 */         if ((localJavacTask instanceof BasicJavacTask)) {
/* 1141 */           ((BasicJavacTask)localJavacTask).updateContext(localContext);
/*      */         }
/*      */       }
/* 1144 */       JavacTrees localJavacTrees = (JavacTrees)this.context.get(JavacTrees.class);
/* 1145 */       if (localJavacTrees != null) {
/* 1146 */         localContext.put(JavacTrees.class, localJavacTrees);
/* 1147 */         localJavacTrees.updateContext(localContext);
/*      */       }
/*      */ 
/* 1150 */       this.context.clear();
/* 1151 */       return localContext;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ServiceIterator
/*      */     implements Iterator<Processor>
/*      */   {
/*      */     private Iterator<Processor> iterator;
/*      */     private Log log;
/*      */     private ServiceLoader<Processor> loader;
/*      */ 
/*      */     ServiceIterator(ClassLoader paramLog, Log arg3)
/*      */     {
/*      */       Object localObject;
/*  331 */       this.log = localObject;
/*      */       try {
/*      */         try {
/*  334 */           this.loader = ServiceLoader.load(Processor.class, paramLog);
/*  335 */           this.iterator = this.loader.iterator();
/*      */         }
/*      */         catch (Exception localException) {
/*  338 */           this.iterator = JavacProcessingEnvironment.this.handleServiceLoaderUnavailability("proc.no.service", null);
/*      */         }
/*      */       } catch (Throwable localThrowable) {
/*  341 */         localObject.error("proc.service.problem", new Object[0]);
/*  342 */         throw new Abort(localThrowable);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*      */       try {
/*  348 */         return this.iterator.hasNext();
/*      */       } catch (ServiceConfigurationError localServiceConfigurationError) {
/*  350 */         this.log.error("proc.bad.config.file", new Object[] { localServiceConfigurationError.getLocalizedMessage() });
/*  351 */         throw new Abort(localServiceConfigurationError);
/*      */       } catch (Throwable localThrowable) {
/*  353 */         throw new Abort(localThrowable);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Processor next() {
/*      */       try {
/*  359 */         return (Processor)this.iterator.next();
/*      */       } catch (ServiceConfigurationError localServiceConfigurationError) {
/*  361 */         this.log.error("proc.bad.config.file", new Object[] { localServiceConfigurationError.getLocalizedMessage() });
/*  362 */         throw new Abort(localServiceConfigurationError);
/*      */       } catch (Throwable localThrowable) {
/*  364 */         throw new Abort(localThrowable);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  369 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public void close() {
/*  373 */       if (this.loader != null)
/*      */         try {
/*  375 */           this.loader.reload();
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.processing.JavacProcessingEnvironment
 * JD-Core Version:    0.6.2
 */