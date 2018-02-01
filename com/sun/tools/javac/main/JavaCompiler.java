/*      */ package com.sun.tools.javac.main;
/*      */ 
/*      */ import com.sun.source.tree.CompilationUnitTree;
/*      */ import com.sun.source.util.TaskEvent;
/*      */ import com.sun.source.util.TaskEvent.Kind;
/*      */ import com.sun.tools.javac.api.MultiTaskListener;
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.comp.Attr;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Check;
/*      */ import com.sun.tools.javac.comp.CompileStates;
/*      */ import com.sun.tools.javac.comp.CompileStates.CompileState;
/*      */ import com.sun.tools.javac.comp.Enter;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.comp.Flow;
/*      */ import com.sun.tools.javac.comp.LambdaToMethod;
/*      */ import com.sun.tools.javac.comp.Lower;
/*      */ import com.sun.tools.javac.comp.Todo;
/*      */ import com.sun.tools.javac.comp.TransTypes;
/*      */ import com.sun.tools.javac.file.JavacFileManager;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.ClassReader.BadClassFile;
/*      */ import com.sun.tools.javac.jvm.ClassReader.SourceCompleter;
/*      */ import com.sun.tools.javac.jvm.ClassWriter;
/*      */ import com.sun.tools.javac.jvm.ClassWriter.PoolOverflow;
/*      */ import com.sun.tools.javac.jvm.ClassWriter.StringOverflow;
/*      */ import com.sun.tools.javac.jvm.Gen;
/*      */ import com.sun.tools.javac.jvm.JNIWriter;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.parser.Parser;
/*      */ import com.sun.tools.javac.parser.ParserFactory;
/*      */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.Pretty;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Abort;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.BaseFileManager;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.FatalError;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticFlag;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DeferredDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Log.WriterKind;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.RichDiagnosticFormatter;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Queue;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import javax.annotation.processing.Processor;
/*      */ import javax.lang.model.SourceVersion;
/*      */ import javax.tools.Diagnostic.Kind;
/*      */ import javax.tools.DiagnosticListener;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.JavaFileObject.Kind;
/*      */ import javax.tools.StandardLocation;
/*      */ 
/*      */ public class JavaCompiler
/*      */ {
/*   81 */   protected static final Context.Key<JavaCompiler> compilerKey = new Context.Key();
/*      */   private static final String versionRBName = "com.sun.tools.javac.resources.version";
/*      */   private static ResourceBundle versionRB;
/*  189 */   private static final CompilePolicy DEFAULT_COMPILE_POLICY = CompilePolicy.BY_TODO;
/*      */   public Log log;
/*      */   JCDiagnostic.Factory diagFactory;
/*      */   protected TreeMaker make;
/*      */   protected ClassReader reader;
/*      */   protected ClassWriter writer;
/*      */   protected JNIWriter jniWriter;
/*      */   protected Enter enter;
/*      */   protected Symtab syms;
/*      */   protected Source source;
/*      */   protected Gen gen;
/*      */   protected Names names;
/*      */   protected Attr attr;
/*      */   protected Check chk;
/*      */   protected Flow flow;
/*      */   protected TransTypes transTypes;
/*      */   protected Lower lower;
/*      */   protected Annotate annotate;
/*      */   protected final Name completionFailureName;
/*      */   protected Types types;
/*      */   protected JavaFileManager fileManager;
/*      */   protected ParserFactory parserFactory;
/*      */   protected MultiTaskListener taskListener;
/*      */   protected JavaCompiler delegateCompiler;
/*  308 */   protected final ClassReader.SourceCompleter thisCompleter = new ClassReader.SourceCompleter()
/*      */   {
/*      */     public void complete(Symbol.ClassSymbol paramAnonymousClassSymbol) throws Symbol.CompletionFailure
/*      */     {
/*  312 */       JavaCompiler.this.complete(paramAnonymousClassSymbol);
/*      */     }
/*  308 */   };
/*      */   protected Options options;
/*      */   protected Context context;
/*      */   protected boolean annotationProcessingOccurred;
/*      */   protected boolean implicitSourceFilesRead;
/*      */   protected CompileStates compileStates;
/*      */   public boolean verbose;
/*      */   public boolean sourceOutput;
/*      */   public boolean stubOutput;
/*      */   public boolean attrParseOnly;
/*      */   boolean relax;
/*      */   public boolean printFlat;
/*      */   public String encoding;
/*      */   public boolean lineDebugInfo;
/*      */   public boolean genEndPos;
/*      */   protected boolean devVerbose;
/*      */   protected boolean processPcks;
/*      */   protected boolean werror;
/*  513 */   protected boolean explicitAnnotationProcessingRequested = false;
/*      */   protected CompilePolicy compilePolicy;
/*      */   protected ImplicitSourcePolicy implicitSourcePolicy;
/*      */   public boolean verboseCompilePolicy;
/*      */   public CompileStates.CompileState shouldStopPolicyIfError;
/*      */   public CompileStates.CompileState shouldStopPolicyIfNoError;
/*      */   public Todo todo;
/*  550 */   public List<Closeable> closeables = List.nil();
/*      */ 
/*  556 */   protected Set<JavaFileObject> inputFiles = new HashSet();
/*      */ 
/*  644 */   public boolean keepComments = false;
/*      */ 
/*  815 */   private boolean hasBeenUsed = false;
/*  816 */   private long start_msec = 0L;
/*  817 */   public long elapsed_msec = 0L;
/*      */ 
/*  929 */   protected boolean needRootClasses = false;
/*      */   private List<JCTree.JCClassDecl> rootClasses;
/* 1024 */   boolean processAnnotations = false;
/*      */   Log.DeferredDiagnosticHandler deferredDiagnosticHandler;
/* 1031 */   private JavacProcessingEnvironment procEnvImpl = null;
/*      */ 
/* 1360 */   HashMap<Env<AttrContext>, Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>>> desugaredEnvs = new HashMap();
/*      */ 
/*      */   public static JavaCompiler instance(Context paramContext)
/*      */   {
/*   86 */     JavaCompiler localJavaCompiler = (JavaCompiler)paramContext.get(compilerKey);
/*   87 */     if (localJavaCompiler == null)
/*   88 */       localJavaCompiler = new JavaCompiler(paramContext);
/*   89 */     return localJavaCompiler;
/*      */   }
/*      */ 
/*      */   public static String version()
/*      */   {
/*   95 */     return version("release");
/*      */   }
/*      */ 
/*      */   public static String fullVersion()
/*      */   {
/*  101 */     return version("full");
/*      */   }
/*      */ 
/*      */   private static String version(String paramString)
/*      */   {
/*  108 */     if (versionRB == null)
/*      */       try {
/*  110 */         versionRB = ResourceBundle.getBundle("com.sun.tools.javac.resources.version");
/*      */       } catch (MissingResourceException localMissingResourceException1) {
/*  112 */         return Log.getLocalizedString("version.not.available", new Object[0]);
/*      */       }
/*      */     try
/*      */     {
/*  116 */       return versionRB.getString(paramString);
/*      */     } catch (MissingResourceException localMissingResourceException2) {
/*      */     }
/*  119 */     return Log.getLocalizedString("version.not.available", new Object[0]);
/*      */   }
/*      */ 
/*      */   public JavaCompiler(Context paramContext)
/*      */   {
/*  338 */     this.context = paramContext;
/*  339 */     paramContext.put(compilerKey, this);
/*      */ 
/*  342 */     if (paramContext.get(JavaFileManager.class) == null) {
/*  343 */       JavacFileManager.preRegister(paramContext);
/*      */     }
/*  345 */     this.names = Names.instance(paramContext);
/*  346 */     this.log = Log.instance(paramContext);
/*  347 */     this.diagFactory = JCDiagnostic.Factory.instance(paramContext);
/*  348 */     this.reader = ClassReader.instance(paramContext);
/*  349 */     this.make = TreeMaker.instance(paramContext);
/*  350 */     this.writer = ClassWriter.instance(paramContext);
/*  351 */     this.jniWriter = JNIWriter.instance(paramContext);
/*  352 */     this.enter = Enter.instance(paramContext);
/*  353 */     this.todo = Todo.instance(paramContext);
/*      */ 
/*  355 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*  356 */     this.parserFactory = ParserFactory.instance(paramContext);
/*  357 */     this.compileStates = CompileStates.instance(paramContext);
/*      */     try
/*      */     {
/*  361 */       this.syms = Symtab.instance(paramContext);
/*      */     }
/*      */     catch (Symbol.CompletionFailure localCompletionFailure) {
/*  364 */       this.log.error("cant.access", new Object[] { localCompletionFailure.sym, localCompletionFailure.getDetailValue() });
/*  365 */       if ((localCompletionFailure instanceof ClassReader.BadClassFile))
/*  366 */         throw new Abort();
/*      */     }
/*  368 */     this.source = Source.instance(paramContext);
/*  369 */     Target localTarget = Target.instance(paramContext);
/*  370 */     this.attr = Attr.instance(paramContext);
/*  371 */     this.chk = Check.instance(paramContext);
/*  372 */     this.gen = Gen.instance(paramContext);
/*  373 */     this.flow = Flow.instance(paramContext);
/*  374 */     this.transTypes = TransTypes.instance(paramContext);
/*  375 */     this.lower = Lower.instance(paramContext);
/*  376 */     this.annotate = Annotate.instance(paramContext);
/*  377 */     this.types = Types.instance(paramContext);
/*  378 */     this.taskListener = MultiTaskListener.instance(paramContext);
/*      */ 
/*  380 */     this.reader.sourceCompleter = this.thisCompleter;
/*      */ 
/*  382 */     this.options = Options.instance(paramContext);
/*      */ 
/*  384 */     this.verbose = this.options.isSet(Option.VERBOSE);
/*  385 */     this.sourceOutput = this.options.isSet(Option.PRINTSOURCE);
/*  386 */     this.stubOutput = this.options.isSet("-stubs");
/*  387 */     this.relax = this.options.isSet("-relax");
/*  388 */     this.printFlat = this.options.isSet("-printflat");
/*  389 */     this.attrParseOnly = this.options.isSet("-attrparseonly");
/*  390 */     this.encoding = this.options.get(Option.ENCODING);
/*  391 */     this.lineDebugInfo = ((this.options.isUnset(Option.G_CUSTOM)) || 
/*  392 */       (this.options
/*  392 */       .isSet(Option.G_CUSTOM, "lines")));
/*      */ 
/*  393 */     this.genEndPos = ((this.options.isSet(Option.XJCOV)) || 
/*  394 */       (paramContext
/*  394 */       .get(DiagnosticListener.class) != null));
/*      */ 
/*  395 */     this.devVerbose = this.options.isSet("dev");
/*  396 */     this.processPcks = this.options.isSet("process.packages");
/*  397 */     this.werror = this.options.isSet(Option.WERROR);
/*      */ 
/*  399 */     if ((this.source.compareTo(Source.DEFAULT) < 0) && 
/*  400 */       (this.options.isUnset(Option.XLINT_CUSTOM, "-" + Lint.LintCategory.OPTIONS.option)) && 
/*  401 */       ((this.fileManager instanceof BaseFileManager)) && 
/*  402 */       (((BaseFileManager)this.fileManager).isDefaultBootClassPath())) {
/*  403 */       this.log.warning(Lint.LintCategory.OPTIONS, "source.no.bootclasspath", new Object[] { this.source.name });
/*      */     }
/*      */ 
/*  408 */     checkForObsoleteOptions(localTarget);
/*      */ 
/*  410 */     this.verboseCompilePolicy = this.options.isSet("verboseCompilePolicy");
/*      */ 
/*  412 */     if (this.attrParseOnly)
/*  413 */       this.compilePolicy = CompilePolicy.ATTR_ONLY;
/*      */     else {
/*  415 */       this.compilePolicy = CompilePolicy.decode(this.options.get("compilePolicy"));
/*      */     }
/*  417 */     this.implicitSourcePolicy = ImplicitSourcePolicy.decode(this.options.get("-implicit"));
/*      */ 
/*  419 */     this.completionFailureName = 
/*  420 */       (this.options
/*  420 */       .isSet("failcomplete") ? 
/*  420 */       this.names
/*  421 */       .fromString(this.options
/*  421 */       .get("failcomplete")) : 
/*  421 */       null);
/*      */ 
/*  424 */     this.shouldStopPolicyIfError = 
/*  427 */       (this.options
/*  427 */       .isSet("shouldStopPolicyIfError") ? 
/*  428 */       CompileStates.CompileState.valueOf(this.options
/*  428 */       .get("shouldStopPolicyIfError")) : this.options
/*  425 */       .isSet("shouldStopPolicy") ? 
/*  426 */       CompileStates.CompileState.valueOf(this.options
/*  426 */       .get("shouldStopPolicy")) : 
/*  428 */       CompileStates.CompileState.INIT);
/*      */ 
/*  430 */     this.shouldStopPolicyIfNoError = 
/*  431 */       (this.options
/*  431 */       .isSet("shouldStopPolicyIfNoError") ? 
/*  432 */       CompileStates.CompileState.valueOf(this.options
/*  432 */       .get("shouldStopPolicyIfNoError")) : 
/*  432 */       CompileStates.CompileState.GENERATE);
/*      */ 
/*  435 */     if (this.options.isUnset("oldDiags"))
/*  436 */       this.log.setDiagnosticFormatter(RichDiagnosticFormatter.instance(paramContext));
/*      */   }
/*      */ 
/*      */   private void checkForObsoleteOptions(Target paramTarget)
/*      */   {
/*  442 */     int i = 0;
/*  443 */     if (this.options.isUnset(Option.XLINT_CUSTOM, "-" + Lint.LintCategory.OPTIONS.option)) {
/*  444 */       if (this.source.compareTo(Source.JDK1_5) <= 0) {
/*  445 */         this.log.warning(Lint.LintCategory.OPTIONS, "option.obsolete.source", new Object[] { this.source.name });
/*  446 */         i = 1;
/*      */       }
/*      */ 
/*  449 */       if (paramTarget.compareTo(Target.JDK1_5) <= 0) {
/*  450 */         this.log.warning(Lint.LintCategory.OPTIONS, "option.obsolete.target", new Object[] { paramTarget.name });
/*  451 */         i = 1;
/*      */       }
/*      */ 
/*  454 */       if (i != 0)
/*  455 */         this.log.warning(Lint.LintCategory.OPTIONS, "option.obsolete.suppression", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean shouldStop(CompileStates.CompileState paramCompileState)
/*      */   {
/*  559 */     CompileStates.CompileState localCompileState = (errorCount() > 0) || (unrecoverableError()) ? this.shouldStopPolicyIfError : this.shouldStopPolicyIfNoError;
/*      */ 
/*  562 */     return paramCompileState.isAfter(localCompileState);
/*      */   }
/*      */ 
/*      */   public int errorCount()
/*      */   {
/*  568 */     if ((this.delegateCompiler != null) && (this.delegateCompiler != this)) {
/*  569 */       return this.delegateCompiler.errorCount();
/*      */     }
/*  571 */     if ((this.werror) && (this.log.nerrors == 0) && (this.log.nwarnings > 0)) {
/*  572 */       this.log.error("warnings.and.werror", new Object[0]);
/*      */     }
/*      */ 
/*  575 */     return this.log.nerrors;
/*      */   }
/*      */ 
/*      */   protected final <T> Queue<T> stopIfError(CompileStates.CompileState paramCompileState, Queue<T> paramQueue) {
/*  579 */     return shouldStop(paramCompileState) ? new ListBuffer() : paramQueue;
/*      */   }
/*      */ 
/*      */   protected final <T> List<T> stopIfError(CompileStates.CompileState paramCompileState, List<T> paramList) {
/*  583 */     return shouldStop(paramCompileState) ? List.nil() : paramList;
/*      */   }
/*      */ 
/*      */   public int warningCount()
/*      */   {
/*  589 */     if ((this.delegateCompiler != null) && (this.delegateCompiler != this)) {
/*  590 */       return this.delegateCompiler.warningCount();
/*      */     }
/*  592 */     return this.log.nwarnings;
/*      */   }
/*      */ 
/*      */   public CharSequence readSource(JavaFileObject paramJavaFileObject)
/*      */   {
/*      */     try
/*      */     {
/*  601 */       this.inputFiles.add(paramJavaFileObject);
/*  602 */       return paramJavaFileObject.getCharContent(false);
/*      */     } catch (IOException localIOException) {
/*  604 */       this.log.error("error.reading.file", new Object[] { paramJavaFileObject, JavacFileManager.getMessage(localIOException) });
/*  605 */     }return null;
/*      */   }
/*      */ 
/*      */   protected JCTree.JCCompilationUnit parse(JavaFileObject paramJavaFileObject, CharSequence paramCharSequence)
/*      */   {
/*  614 */     long l = now();
/*  615 */     JCTree.JCCompilationUnit localJCCompilationUnit = this.make.TopLevel(List.nil(), null, 
/*  616 */       List.nil());
/*      */     Object localObject;
/*  617 */     if (paramCharSequence != null) {
/*  618 */       if (this.verbose) {
/*  619 */         this.log.printVerbose("parsing.started", new Object[] { paramJavaFileObject });
/*      */       }
/*  621 */       if (!this.taskListener.isEmpty()) {
/*  622 */         localObject = new TaskEvent(TaskEvent.Kind.PARSE, paramJavaFileObject);
/*  623 */         this.taskListener.started((TaskEvent)localObject);
/*  624 */         this.keepComments = true;
/*  625 */         this.genEndPos = true;
/*      */       }
/*  627 */       localObject = this.parserFactory.newParser(paramCharSequence, keepComments(), this.genEndPos, this.lineDebugInfo);
/*  628 */       localJCCompilationUnit = ((Parser)localObject).parseCompilationUnit();
/*  629 */       if (this.verbose) {
/*  630 */         this.log.printVerbose("parsing.done", new Object[] { Long.toString(elapsed(l)) });
/*      */       }
/*      */     }
/*      */ 
/*  634 */     localJCCompilationUnit.sourcefile = paramJavaFileObject;
/*      */ 
/*  636 */     if ((paramCharSequence != null) && (!this.taskListener.isEmpty())) {
/*  637 */       localObject = new TaskEvent(TaskEvent.Kind.PARSE, localJCCompilationUnit);
/*  638 */       this.taskListener.finished((TaskEvent)localObject);
/*      */     }
/*      */ 
/*  641 */     return localJCCompilationUnit;
/*      */   }
/*      */ 
/*      */   protected boolean keepComments()
/*      */   {
/*  646 */     return (this.keepComments) || (this.sourceOutput) || (this.stubOutput);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public JCTree.JCCompilationUnit parse(String paramString)
/*      */   {
/*  655 */     JavacFileManager localJavacFileManager = (JavacFileManager)this.fileManager;
/*  656 */     return parse((JavaFileObject)localJavacFileManager.getJavaFileObjectsFromStrings(List.of(paramString)).iterator().next());
/*      */   }
/*      */ 
/*      */   public JCTree.JCCompilationUnit parse(JavaFileObject paramJavaFileObject)
/*      */   {
/*  663 */     JavaFileObject localJavaFileObject = this.log.useSource(paramJavaFileObject);
/*      */     try {
/*  665 */       JCTree.JCCompilationUnit localJCCompilationUnit1 = parse(paramJavaFileObject, readSource(paramJavaFileObject));
/*  666 */       if (localJCCompilationUnit1.endPositions != null)
/*  667 */         this.log.setEndPosTable(paramJavaFileObject, localJCCompilationUnit1.endPositions);
/*  668 */       return localJCCompilationUnit1;
/*      */     } finally {
/*  670 */       this.log.useSource(localJavaFileObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Symbol resolveBinaryNameOrIdent(String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  680 */       Name localName = this.names.fromString(paramString.replace("/", "."));
/*  681 */       return this.reader.loadClass(localName); } catch (Symbol.CompletionFailure localCompletionFailure) {
/*      */     }
/*  683 */     return resolveIdent(paramString);
/*      */   }
/*      */ 
/*      */   public Symbol resolveIdent(String paramString)
/*      */   {
/*  691 */     if (paramString.equals(""))
/*  692 */       return this.syms.errSymbol;
/*  693 */     JavaFileObject localJavaFileObject = this.log.useSource(null);
/*      */     try {
/*  695 */       Object localObject1 = null;
/*  696 */       for (CharSequence localCharSequence : paramString.split("\\.", -1)) {
/*  697 */         if (!SourceVersion.isIdentifier(localCharSequence)) {
/*  698 */           return this.syms.errSymbol;
/*      */         }
/*  700 */         localObject1 = localObject1 == null ? this.make.Ident(this.names.fromString(localCharSequence)) : this.make
/*  700 */           .Select((JCTree.JCExpression)localObject1, this.names
/*  700 */           .fromString(localCharSequence));
/*      */       }
/*      */ 
/*  703 */       ??? = this.make
/*  703 */         .TopLevel(List.nil(), null, List.nil());
/*  704 */       ((JCTree.JCCompilationUnit)???).packge = this.syms.unnamedPackage;
/*  705 */       return this.attr.attribIdent((JCTree)localObject1, (JCTree.JCCompilationUnit)???);
/*      */     } finally {
/*  707 */       this.log.useSource(localJavaFileObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   JavaFileObject printSource(Env<AttrContext> paramEnv, JCTree.JCClassDecl paramJCClassDecl)
/*      */     throws IOException
/*      */   {
/*  718 */     JavaFileObject localJavaFileObject = this.fileManager
/*  718 */       .getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, paramJCClassDecl.sym.flatname
/*  719 */       .toString(), JavaFileObject.Kind.SOURCE, null);
/*      */ 
/*  722 */     if (this.inputFiles.contains(localJavaFileObject)) {
/*  723 */       this.log.error(paramJCClassDecl.pos(), "source.cant.overwrite.input.file", new Object[] { localJavaFileObject });
/*  724 */       return null;
/*      */     }
/*  726 */     BufferedWriter localBufferedWriter = new BufferedWriter(localJavaFileObject.openWriter());
/*      */     try {
/*  728 */       new Pretty(localBufferedWriter, true).printUnit(paramEnv.toplevel, paramJCClassDecl);
/*  729 */       if (this.verbose)
/*  730 */         this.log.printVerbose("wrote.file", new Object[] { localJavaFileObject });
/*      */     } finally {
/*  732 */       localBufferedWriter.close();
/*      */     }
/*  734 */     return localJavaFileObject;
/*      */   }
/*      */ 
/*      */   JavaFileObject genCode(Env<AttrContext> paramEnv, JCTree.JCClassDecl paramJCClassDecl)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  745 */       if ((this.gen.genClass(paramEnv, paramJCClassDecl)) && (errorCount() == 0))
/*  746 */         return this.writer.writeClass(paramJCClassDecl.sym);
/*      */     } catch (ClassWriter.PoolOverflow localPoolOverflow) {
/*  748 */       this.log.error(paramJCClassDecl.pos(), "limit.pool", new Object[0]);
/*      */     } catch (ClassWriter.StringOverflow localStringOverflow) {
/*  750 */       this.log.error(paramJCClassDecl.pos(), "limit.string.overflow", new Object[] { localStringOverflow.value
/*  751 */         .substring(0, 20) });
/*      */     }
/*      */     catch (Symbol.CompletionFailure localCompletionFailure) {
/*  753 */       this.chk.completionError(paramJCClassDecl.pos(), localCompletionFailure);
/*      */     }
/*  755 */     return null;
/*      */   }
/*      */ 
/*      */   public void complete(Symbol.ClassSymbol paramClassSymbol)
/*      */     throws Symbol.CompletionFailure
/*      */   {
/*  764 */     if (this.completionFailureName == paramClassSymbol.fullname) {
/*  765 */       throw new Symbol.CompletionFailure(paramClassSymbol, "user-selected completion failure by class name");
/*  768 */     }
/*      */ JavaFileObject localJavaFileObject1 = paramClassSymbol.classfile;
/*  769 */     JavaFileObject localJavaFileObject2 = this.log.useSource(localJavaFileObject1);
/*      */     JCTree.JCCompilationUnit localJCCompilationUnit;
/*      */     try { localJCCompilationUnit = parse(localJavaFileObject1, localJavaFileObject1.getCharContent(false));
/*      */     } catch (IOException localIOException) {
/*  774 */       this.log.error("error.reading.file", new Object[] { localJavaFileObject1, JavacFileManager.getMessage(localIOException) });
/*  775 */       localJCCompilationUnit = this.make.TopLevel(List.nil(), null, List.nil());
/*      */     } finally {
/*  777 */       this.log.useSource(localJavaFileObject2);
/*      */     }
/*      */     TaskEvent localTaskEvent;
/*  780 */     if (!this.taskListener.isEmpty()) {
/*  781 */       localTaskEvent = new TaskEvent(TaskEvent.Kind.ENTER, localJCCompilationUnit);
/*  782 */       this.taskListener.started(localTaskEvent);
/*      */     }
/*      */ 
/*  785 */     this.enter.complete(List.of(localJCCompilationUnit), paramClassSymbol);
/*      */ 
/*  787 */     if (!this.taskListener.isEmpty()) {
/*  788 */       localTaskEvent = new TaskEvent(TaskEvent.Kind.ENTER, localJCCompilationUnit);
/*  789 */       this.taskListener.finished(localTaskEvent);
/*      */     }
/*      */ 
/*  792 */     if (this.enter.getEnv(paramClassSymbol) == null)
/*      */     {
/*  794 */       boolean bool = localJCCompilationUnit.sourcefile
/*  794 */         .isNameCompatible("package-info", JavaFileObject.Kind.SOURCE);
/*      */       JCDiagnostic localJCDiagnostic;
/*  796 */       if (bool) {
/*  797 */         if (this.enter.getEnv(localJCCompilationUnit.packge) == null)
/*      */         {
/*  799 */           localJCDiagnostic = this.diagFactory
/*  799 */             .fragment("file.does.not.contain.package", new Object[] { paramClassSymbol
/*  800 */             .location() });
/*      */           ClassReader tmp288_285 = this.reader; tmp288_285.getClass(); throw new ClassReader.BadClassFile(tmp288_285, paramClassSymbol, localJavaFileObject1, localJCDiagnostic);
/*      */         }
/*      */       }
/*      */       else {
/*  805 */         localJCDiagnostic = this.diagFactory
/*  805 */           .fragment("file.doesnt.contain.class", new Object[] { paramClassSymbol
/*  806 */           .getQualifiedName() });
/*      */         ClassReader tmp332_329 = this.reader; tmp332_329.getClass(); throw new ClassReader.BadClassFile(tmp332_329, paramClassSymbol, localJavaFileObject1, localJCDiagnostic);
/*      */       }
/*      */     }
/*      */ 
/*  811 */     this.implicitSourceFilesRead = true;
/*      */   }
/*      */ 
/*      */   public void compile(List<JavaFileObject> paramList)
/*      */     throws Throwable
/*      */   {
/*  821 */     compile(paramList, List.nil(), null);
/*      */   }
/*      */ 
/*      */   public void compile(List<JavaFileObject> paramList, List<String> paramList1, Iterable<? extends Processor> paramIterable)
/*      */   {
/*  836 */     if ((paramIterable != null) && (paramIterable.iterator().hasNext())) {
/*  837 */       this.explicitAnnotationProcessingRequested = true;
/*      */     }
/*      */ 
/*  840 */     if (this.hasBeenUsed)
/*  841 */       throw new AssertionError("attempt to reuse JavaCompiler");
/*  842 */     this.hasBeenUsed = true;
/*      */ 
/*  846 */     this.options.put(Option.XLINT_CUSTOM.text + "-" + Lint.LintCategory.OPTIONS.option, "true");
/*  847 */     this.options.remove(Option.XLINT_CUSTOM.text + Lint.LintCategory.OPTIONS.option);
/*      */ 
/*  849 */     this.start_msec = now();
/*      */     try
/*      */     {
/*  852 */       initProcessAnnotations(paramIterable);
/*      */ 
/*  855 */       this.delegateCompiler = 
/*  856 */         processAnnotations(
/*  857 */         enterTrees(stopIfError(CompileStates.CompileState.PARSE, 
/*  857 */         parseFiles(paramList))), 
/*  857 */         paramList1);
/*      */ 
/*  860 */       this.delegateCompiler.compile2();
/*  861 */       this.delegateCompiler.close();
/*  862 */       this.elapsed_msec = this.delegateCompiler.elapsed_msec;
/*      */     } catch (Abort localAbort) {
/*  864 */       if (this.devVerbose)
/*  865 */         localAbort.printStackTrace(System.err);
/*      */     } finally {
/*  867 */       if (this.procEnvImpl != null)
/*  868 */         this.procEnvImpl.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void compile2()
/*      */   {
/*      */     try
/*      */     {
/*  878 */       switch (2.$SwitchMap$com$sun$tools$javac$main$JavaCompiler$CompilePolicy[this.compilePolicy.ordinal()]) {
/*      */       case 1:
/*  880 */         attribute(this.todo);
/*  881 */         break;
/*      */       case 2:
/*  884 */         flow(attribute(this.todo));
/*  885 */         break;
/*      */       case 3:
/*  888 */         generate(desugar(flow(attribute(this.todo))));
/*  889 */         break;
/*      */       case 4:
/*  892 */         Queue localQueue = this.todo.groupByFile();
/*  893 */         while ((!localQueue.isEmpty()) && (!shouldStop(CompileStates.CompileState.ATTR))) {
/*  894 */           generate(desugar(flow(attribute((Queue)localQueue.remove()))));
/*      */         }
/*      */ 
/*  897 */         break;
/*      */       case 5:
/*      */       default:
/*  900 */         while (!this.todo.isEmpty()) {
/*  901 */           generate(desugar(flow(attribute((Env)this.todo.remove())))); continue;
/*      */ 
/*  905 */           Assert.error("unknown compile policy");
/*      */         }
/*      */       }
/*      */     } catch (Abort localAbort) { if (this.devVerbose) {
/*  909 */         localAbort.printStackTrace(System.err);
/*      */       }
/*      */     }
/*  912 */     if (this.verbose) {
/*  913 */       this.elapsed_msec = elapsed(this.start_msec);
/*  914 */       this.log.printVerbose("total", new Object[] { Long.toString(this.elapsed_msec) });
/*      */     }
/*      */ 
/*  917 */     reportDeferredDiagnostics();
/*      */ 
/*  919 */     if (!this.log.hasDiagnosticListener()) {
/*  920 */       printCount("error", errorCount());
/*  921 */       printCount("warn", warningCount());
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<JCTree.JCCompilationUnit> parseFiles(Iterable<JavaFileObject> paramIterable)
/*      */   {
/*  941 */     if (shouldStop(CompileStates.CompileState.PARSE)) {
/*  942 */       return List.nil();
/*      */     }
/*      */ 
/*  945 */     ListBuffer localListBuffer = new ListBuffer();
/*  946 */     HashSet localHashSet = new HashSet();
/*  947 */     for (JavaFileObject localJavaFileObject : paramIterable) {
/*  948 */       if (!localHashSet.contains(localJavaFileObject)) {
/*  949 */         localHashSet.add(localJavaFileObject);
/*  950 */         localListBuffer.append(parse(localJavaFileObject));
/*      */       }
/*      */     }
/*  953 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   public List<JCTree.JCCompilationUnit> enterTreesIfNeeded(List<JCTree.JCCompilationUnit> paramList)
/*      */   {
/*  963 */     if (shouldStop(CompileStates.CompileState.ATTR))
/*  964 */       return List.nil();
/*  965 */     return enterTrees(paramList);
/*      */   }
/*      */ 
/*      */   public List<JCTree.JCCompilationUnit> enterTrees(List<JCTree.JCCompilationUnit> paramList)
/*      */   {
/*  975 */     if (!this.taskListener.isEmpty())
/*  976 */       for (localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCCompilationUnit)((Iterator)localObject1).next();
/*  977 */         localObject3 = new TaskEvent(TaskEvent.Kind.ENTER, (CompilationUnitTree)localObject2);
/*  978 */         this.taskListener.started((TaskEvent)localObject3);
/*      */       }
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  982 */     this.enter.main(paramList);
/*      */ 
/*  984 */     if (!this.taskListener.isEmpty()) {
/*  985 */       for (localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCCompilationUnit)((Iterator)localObject1).next();
/*  986 */         localObject3 = new TaskEvent(TaskEvent.Kind.ENTER, (CompilationUnitTree)localObject2);
/*  987 */         this.taskListener.finished((TaskEvent)localObject3);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  994 */     if ((this.needRootClasses) || (this.sourceOutput) || (this.stubOutput)) {
/*  995 */       localObject1 = new ListBuffer();
/*  996 */       for (localObject2 = paramList.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (JCTree.JCCompilationUnit)((Iterator)localObject2).next();
/*  997 */         for (List localList = ((JCTree.JCCompilationUnit)localObject3).defs; 
/*  998 */           localList.nonEmpty(); 
/*  999 */           localList = localList.tail) {
/* 1000 */           if ((localList.head instanceof JCTree.JCClassDecl))
/* 1001 */             ((ListBuffer)localObject1).append((JCTree.JCClassDecl)localList.head);
/*      */         }
/*      */       }
/* 1004 */       this.rootClasses = ((ListBuffer)localObject1).toList();
/*      */     }
/*      */ 
/* 1011 */     for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCCompilationUnit)((Iterator)localObject1).next();
/* 1012 */       this.inputFiles.add(((JCTree.JCCompilationUnit)localObject2).sourcefile);
/*      */     }
/*      */ 
/* 1015 */     return paramList;
/*      */   }
/*      */ 
/*      */   public void initProcessAnnotations(Iterable<? extends Processor> paramIterable)
/*      */   {
/* 1045 */     if (this.options.isSet(Option.PROC, "none")) {
/* 1046 */       this.processAnnotations = false;
/* 1047 */     } else if (this.procEnvImpl == null) {
/* 1048 */       this.procEnvImpl = JavacProcessingEnvironment.instance(this.context);
/* 1049 */       this.procEnvImpl.setProcessors(paramIterable);
/* 1050 */       this.processAnnotations = this.procEnvImpl.atLeastOneProcessor();
/*      */ 
/* 1052 */       if (this.processAnnotations) {
/* 1053 */         this.options.put("save-parameter-names", "save-parameter-names");
/* 1054 */         this.reader.saveParameterNames = true;
/* 1055 */         this.keepComments = true;
/* 1056 */         this.genEndPos = true;
/* 1057 */         if (!this.taskListener.isEmpty())
/* 1058 */           this.taskListener.started(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING));
/* 1059 */         this.deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log);
/*      */       } else {
/* 1061 */         this.procEnvImpl.close();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public JavaCompiler processAnnotations(List<JCTree.JCCompilationUnit> paramList)
/*      */   {
/* 1068 */     return processAnnotations(paramList, List.nil());
/*      */   }
/*      */ 
/*      */   public JavaCompiler processAnnotations(List<JCTree.JCCompilationUnit> paramList, List<String> paramList1)
/*      */   {
/* 1084 */     if (shouldStop(CompileStates.CompileState.PROCESS))
/*      */     {
/* 1089 */       if (unrecoverableError()) {
/* 1090 */         this.deferredDiagnosticHandler.reportDeferredDiagnostics();
/* 1091 */         this.log.popDiagnosticHandler(this.deferredDiagnosticHandler);
/* 1092 */         return this;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1101 */     if (!this.processAnnotations)
/*      */     {
/* 1105 */       if (this.options.isSet(Option.PROC, "only")) {
/* 1106 */         this.log.warning("proc.proc-only.requested.no.procs", new Object[0]);
/* 1107 */         this.todo.clear();
/*      */       }
/*      */ 
/* 1110 */       if (!paramList1.isEmpty()) {
/* 1111 */         this.log.error("proc.no.explicit.annotation.processing.requested", new Object[] { paramList1 });
/*      */       }
/*      */ 
/* 1114 */       Assert.checkNull(this.deferredDiagnosticHandler);
/* 1115 */       return this;
/*      */     }
/*      */ 
/* 1118 */     Assert.checkNonNull(this.deferredDiagnosticHandler);
/*      */     try
/*      */     {
/* 1121 */       List localList1 = List.nil();
/* 1122 */       List localList2 = List.nil();
/*      */       Object localObject1;
/* 1123 */       if (!paramList1.isEmpty())
/*      */       {
/* 1126 */         if (!explicitAnnotationProcessingRequested()) {
/* 1127 */           this.log.error("proc.no.explicit.annotation.processing.requested", new Object[] { paramList1 });
/*      */ 
/* 1129 */           this.deferredDiagnosticHandler.reportDeferredDiagnostics();
/* 1130 */           this.log.popDiagnosticHandler(this.deferredDiagnosticHandler);
/* 1131 */           return this;
/*      */         }
/* 1133 */         int i = 0;
/* 1134 */         for (localObject1 = paramList1.iterator(); ((Iterator)localObject1).hasNext(); ) { String str = (String)((Iterator)localObject1).next();
/* 1135 */           Symbol localSymbol = resolveBinaryNameOrIdent(str);
/* 1136 */           if ((localSymbol == null) || ((localSymbol.kind == 1) && (!this.processPcks)) || (localSymbol.kind == 137))
/*      */           {
/* 1139 */             this.log.error("proc.cant.find.class", new Object[] { str });
/* 1140 */             i = 1;
/*      */           }
/*      */           else {
/*      */             try {
/* 1144 */               if (localSymbol.kind == 1)
/* 1145 */                 localSymbol.complete();
/* 1146 */               if (localSymbol.exists()) {
/* 1147 */                 if (localSymbol.kind == 1)
/* 1148 */                   localList2 = localList2.prepend((Symbol.PackageSymbol)localSymbol);
/*      */                 else
/* 1150 */                   localList1 = localList1.prepend((Symbol.ClassSymbol)localSymbol);
/* 1151 */                 continue;
/*      */               }
/* 1153 */               Assert.check(localSymbol.kind == 1);
/* 1154 */               this.log.warning("proc.package.does.not.exist", new Object[] { str });
/* 1155 */               localList2 = localList2.prepend((Symbol.PackageSymbol)localSymbol);
/*      */             } catch (Symbol.CompletionFailure localCompletionFailure2) {
/* 1157 */               this.log.error("proc.cant.find.class", new Object[] { str });
/* 1158 */               i = 1;
/*      */             }
/*      */           }
/*      */         }
/* 1162 */         if (i != 0) {
/* 1163 */           this.deferredDiagnosticHandler.reportDeferredDiagnostics();
/* 1164 */           this.log.popDiagnosticHandler(this.deferredDiagnosticHandler);
/* 1165 */           return this;
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1170 */         JavaCompiler localJavaCompiler = this.procEnvImpl.doProcessing(this.context, paramList, localList1, localList2, this.deferredDiagnosticHandler);
/*      */ 
/* 1172 */         if (localJavaCompiler != this) {
/* 1173 */           this.annotationProcessingOccurred = (localJavaCompiler.annotationProcessingOccurred = 1);
/*      */         }
/* 1175 */         return localJavaCompiler;
/*      */       } finally {
/* 1177 */         this.procEnvImpl.close();
/*      */       }
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure1) {
/* 1180 */       this.log.error("cant.access", new Object[] { localCompletionFailure1.sym, localCompletionFailure1.getDetailValue() });
/* 1181 */       this.deferredDiagnosticHandler.reportDeferredDiagnostics();
/* 1182 */       this.log.popDiagnosticHandler(this.deferredDiagnosticHandler);
/* 1183 */     }return this;
/*      */   }
/*      */ 
/*      */   private boolean unrecoverableError()
/*      */   {
/* 1188 */     if (this.deferredDiagnosticHandler != null) {
/* 1189 */       for (JCDiagnostic localJCDiagnostic : this.deferredDiagnosticHandler.getDiagnostics()) {
/* 1190 */         if ((localJCDiagnostic.getKind() == Diagnostic.Kind.ERROR) && (!localJCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.RECOVERABLE)))
/* 1191 */           return true;
/*      */       }
/*      */     }
/* 1194 */     return false;
/*      */   }
/*      */ 
/*      */   boolean explicitAnnotationProcessingRequested()
/*      */   {
/* 1200 */     return (this.explicitAnnotationProcessingRequested) || 
/* 1200 */       (explicitAnnotationProcessingRequested(this.options));
/*      */   }
/*      */ 
/*      */   static boolean explicitAnnotationProcessingRequested(Options paramOptions)
/*      */   {
/* 1208 */     return (paramOptions
/* 1205 */       .isSet(Option.PROCESSOR)) || 
/* 1206 */       (paramOptions
/* 1206 */       .isSet(Option.PROCESSORPATH)) || 
/* 1207 */       (paramOptions
/* 1207 */       .isSet(Option.PROC, "only")) || 
/* 1208 */       (paramOptions
/* 1208 */       .isSet(Option.XPRINT));
/*      */   }
/*      */ 
/*      */   public Queue<Env<AttrContext>> attribute(Queue<Env<AttrContext>> paramQueue)
/*      */   {
/* 1219 */     ListBuffer localListBuffer = new ListBuffer();
/* 1220 */     while (!paramQueue.isEmpty())
/* 1221 */       localListBuffer.append(attribute((Env)paramQueue.remove()));
/* 1222 */     return stopIfError(CompileStates.CompileState.ATTR, localListBuffer);
/*      */   }
/*      */ 
/*      */   public Env<AttrContext> attribute(Env<AttrContext> paramEnv)
/*      */   {
/* 1230 */     if (this.compileStates.isDone(paramEnv, CompileStates.CompileState.ATTR)) {
/* 1231 */       return paramEnv;
/*      */     }
/* 1233 */     if (this.verboseCompilePolicy)
/* 1234 */       printNote("[attribute " + paramEnv.enclClass.sym + "]");
/* 1235 */     if (this.verbose) {
/* 1236 */       this.log.printVerbose("checking.attribution", new Object[] { paramEnv.enclClass.sym });
/*      */     }
/* 1238 */     if (!this.taskListener.isEmpty()) {
/* 1239 */       localObject1 = new TaskEvent(TaskEvent.Kind.ANALYZE, paramEnv.toplevel, paramEnv.enclClass.sym);
/* 1240 */       this.taskListener.started((TaskEvent)localObject1);
/*      */     }
/*      */ 
/* 1243 */     Object localObject1 = this.log.useSource(paramEnv.enclClass.sym.sourcefile != null ? paramEnv.enclClass.sym.sourcefile : paramEnv.toplevel.sourcefile);
/*      */     try
/*      */     {
/* 1248 */       this.attr.attrib(paramEnv);
/* 1249 */       if ((errorCount() > 0) && (!shouldStop(CompileStates.CompileState.ATTR)))
/*      */       {
/* 1252 */         this.attr.postAttr(paramEnv.tree);
/*      */       }
/* 1254 */       this.compileStates.put(paramEnv, CompileStates.CompileState.ATTR);
/* 1255 */       if ((this.rootClasses != null) && (this.rootClasses.contains(paramEnv.enclClass)))
/*      */       {
/* 1259 */         reportPublicApi(paramEnv.enclClass.sym);
/*      */       }
/*      */ 
/* 1263 */       this.log.useSource((JavaFileObject)localObject1); } finally { this.log.useSource((JavaFileObject)localObject1); }
/*      */ 
/*      */ 
/* 1266 */     return paramEnv;
/*      */   }
/*      */ 
/*      */   public void reportPublicApi(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Queue<Env<AttrContext>> flow(Queue<Env<AttrContext>> paramQueue)
/*      */   {
/* 1284 */     ListBuffer localListBuffer = new ListBuffer();
/* 1285 */     for (Env localEnv : paramQueue) {
/* 1286 */       flow(localEnv, localListBuffer);
/*      */     }
/* 1288 */     return stopIfError(CompileStates.CompileState.FLOW, localListBuffer);
/*      */   }
/*      */ 
/*      */   public Queue<Env<AttrContext>> flow(Env<AttrContext> paramEnv)
/*      */   {
/* 1295 */     ListBuffer localListBuffer = new ListBuffer();
/* 1296 */     flow(paramEnv, localListBuffer);
/* 1297 */     return stopIfError(CompileStates.CompileState.FLOW, localListBuffer);
/*      */   }
/*      */ 
/*      */   protected void flow(Env<AttrContext> paramEnv, Queue<Env<AttrContext>> paramQueue)
/*      */   {
/* 1304 */     if (this.compileStates.isDone(paramEnv, CompileStates.CompileState.FLOW)) {
/* 1305 */       paramQueue.add(paramEnv);
/* 1306 */       return;
/*      */     }
/*      */     try
/*      */     {
/*      */       Object localObject1;
/* 1310 */       if (shouldStop(CompileStates.CompileState.FLOW))
/*      */       {
/* 1340 */         if (!this.taskListener.isEmpty()) {
/* 1341 */           localObject1 = new TaskEvent(TaskEvent.Kind.ANALYZE, paramEnv.toplevel, paramEnv.enclClass.sym);
/* 1342 */           this.taskListener.finished((TaskEvent)localObject1);
/*      */         }
/*      */       }
/* 1313 */       else if (this.relax) {
/* 1314 */         paramQueue.add(paramEnv);
/*      */ 
/* 1340 */         if (!this.taskListener.isEmpty()) {
/* 1341 */           localObject1 = new TaskEvent(TaskEvent.Kind.ANALYZE, paramEnv.toplevel, paramEnv.enclClass.sym);
/* 1342 */           this.taskListener.finished((TaskEvent)localObject1);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1318 */         if (this.verboseCompilePolicy)
/* 1319 */           printNote("[flow " + paramEnv.enclClass.sym + "]");
/* 1320 */         localObject1 = this.log.useSource(paramEnv.enclClass.sym.sourcefile != null ? paramEnv.enclClass.sym.sourcefile : paramEnv.toplevel.sourcefile);
/*      */         try
/*      */         {
/* 1325 */           this.make.at(0);
/* 1326 */           TreeMaker localTreeMaker = this.make.forToplevel(paramEnv.toplevel);
/* 1327 */           this.flow.analyzeTree(paramEnv, localTreeMaker);
/* 1328 */           this.compileStates.put(paramEnv, CompileStates.CompileState.FLOW);
/*      */ 
/* 1330 */           if (shouldStop(CompileStates.CompileState.FLOW))
/*      */           {
/* 1336 */             this.log.useSource((JavaFileObject)localObject1);
/*      */             TaskEvent localTaskEvent1;
/* 1331 */             return;
/*      */           }
/* 1333 */           paramQueue.add(paramEnv);
/*      */         }
/*      */         finally {
/* 1336 */           this.log.useSource((JavaFileObject)localObject1);
/*      */         }
/*      */ 
/* 1340 */         if (!this.taskListener.isEmpty()) {
/* 1341 */           localObject1 = new TaskEvent(TaskEvent.Kind.ANALYZE, paramEnv.toplevel, paramEnv.enclClass.sym);
/* 1342 */           this.taskListener.finished((TaskEvent)localObject1);
/*      */         }
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1340 */       if (!this.taskListener.isEmpty()) {
/* 1341 */         TaskEvent localTaskEvent2 = new TaskEvent(TaskEvent.Kind.ANALYZE, paramEnv.toplevel, paramEnv.enclClass.sym);
/* 1342 */         this.taskListener.finished(localTaskEvent2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> desugar(Queue<Env<AttrContext>> paramQueue)
/*      */   {
/* 1354 */     ListBuffer localListBuffer = new ListBuffer();
/* 1355 */     for (Env localEnv : paramQueue)
/* 1356 */       desugar(localEnv, localListBuffer);
/* 1357 */     return stopIfError(CompileStates.CompileState.FLOW, localListBuffer);
/*      */   }
/*      */ 
/*      */   protected void desugar(final Env<AttrContext> paramEnv, Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> paramQueue)
/*      */   {
/* 1370 */     if (shouldStop(CompileStates.CompileState.TRANSTYPES)) {
/* 1371 */       return;
/*      */     }
/* 1373 */     if ((this.implicitSourcePolicy == ImplicitSourcePolicy.NONE) && 
/* 1374 */       (!this.inputFiles
/* 1374 */       .contains(paramEnv.toplevel.sourcefile)))
/*      */     {
/* 1375 */       return;
/*      */     }
/*      */ 
/* 1378 */     if (this.compileStates.isDone(paramEnv, CompileStates.CompileState.LOWER)) {
/* 1379 */       paramQueue.addAll((Collection)this.desugaredEnvs.get(paramEnv));
/* 1380 */       return;
/*      */     }
/*      */ 
/* 1432 */     TreeScanner local1ScanNested = new TreeScanner()
/*      */     {
/* 1391 */       Set<Env<AttrContext>> dependencies = new LinkedHashSet();
/*      */       protected boolean hasLambdas;
/*      */ 
/*      */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl)
/*      */       {
/* 1395 */         Type localType = this.this$0.types.supertype(paramAnonymousJCClassDecl.sym.type);
/* 1396 */         int i = 0;
/* 1397 */         while ((i == 0) && (localType.hasTag(TypeTag.CLASS))) {
/* 1398 */           Symbol.ClassSymbol localClassSymbol = localType.tsym.outermostClass();
/* 1399 */           Env localEnv = this.this$0.enter.getEnv(localClassSymbol);
/* 1400 */           if ((localEnv != null) && (paramEnv != localEnv)) {
/* 1401 */             if (this.dependencies.add(localEnv)) {
/* 1402 */               boolean bool = this.hasLambdas;
/*      */               try {
/* 1404 */                 scan(localEnv.tree);
/*      */               }
/*      */               finally
/*      */               {
/* 1412 */                 this.hasLambdas = bool;
/*      */               }
/*      */             }
/* 1415 */             i = 1;
/*      */           }
/* 1417 */           localType = this.this$0.types.supertype(localType);
/*      */         }
/* 1419 */         super.visitClassDef(paramAnonymousJCClassDecl);
/*      */       }
/*      */ 
/*      */       public void visitLambda(JCTree.JCLambda paramAnonymousJCLambda) {
/* 1423 */         this.hasLambdas = true;
/* 1424 */         super.visitLambda(paramAnonymousJCLambda);
/*      */       }
/*      */ 
/*      */       public void visitReference(JCTree.JCMemberReference paramAnonymousJCMemberReference) {
/* 1428 */         this.hasLambdas = true;
/* 1429 */         super.visitReference(paramAnonymousJCMemberReference);
/*      */       }
/*      */     };
/* 1433 */     local1ScanNested.scan(paramEnv.tree);
/* 1434 */     for (Object localObject1 = local1ScanNested.dependencies.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Env)((Iterator)localObject1).next();
/* 1435 */       if (!this.compileStates.isDone((Env)localObject2, CompileStates.CompileState.FLOW))
/* 1436 */         this.desugaredEnvs.put(localObject2, desugar(flow(attribute((Env)localObject2))));
/*      */     }
/*      */     Object localObject2;
/* 1441 */     if (shouldStop(CompileStates.CompileState.TRANSTYPES)) {
/* 1442 */       return;
/*      */     }
/* 1444 */     if (this.verboseCompilePolicy) {
/* 1445 */       printNote("[desugar " + paramEnv.enclClass.sym + "]");
/*      */     }
/* 1447 */     localObject1 = this.log.useSource(paramEnv.enclClass.sym.sourcefile != null ? paramEnv.enclClass.sym.sourcefile : paramEnv.toplevel.sourcefile);
/*      */     try
/*      */     {
/* 1452 */       localObject2 = paramEnv.tree;
/*      */ 
/* 1454 */       this.make.at(0);
/* 1455 */       TreeMaker localTreeMaker = this.make.forToplevel(paramEnv.toplevel);
/*      */ 
/* 1457 */       if ((paramEnv.tree instanceof JCTree.JCCompilationUnit)) {
/* 1458 */         if ((!this.stubOutput) && (!this.sourceOutput) && (!this.printFlat)) {
/* 1459 */           if (shouldStop(CompileStates.CompileState.LOWER))
/* 1460 */             return;
/* 1461 */           localObject3 = this.lower.translateTopLevelClass(paramEnv, paramEnv.tree, localTreeMaker);
/* 1462 */           if (((List)localObject3).head != null) {
/* 1463 */             Assert.check(((List)localObject3).tail.isEmpty());
/* 1464 */             paramQueue.add(new Pair(paramEnv, (JCTree.JCClassDecl)((List)localObject3).head));
/*      */           }
/*      */         }
/* 1467 */         return;
/*      */       }
/*      */ 
/* 1470 */       if (this.stubOutput)
/*      */       {
/* 1473 */         localObject3 = (JCTree.JCClassDecl)paramEnv.tree;
/* 1474 */         if (((localObject2 instanceof JCTree.JCClassDecl)) && 
/* 1475 */           (this.rootClasses
/* 1475 */           .contains((JCTree.JCClassDecl)localObject2)) && (
/* 1475 */           ((((JCTree.JCClassDecl)localObject3).mods.flags & 0x5) != 0L) || 
/* 1477 */           (((JCTree.JCClassDecl)localObject3).sym
/* 1477 */           .packge().getQualifiedName() == this.names.java_lang))) {
/* 1478 */           paramQueue.add(new Pair(paramEnv, removeMethodBodies((JCTree.JCClassDecl)localObject3)));
/*      */         }
/* 1480 */         return;
/*      */       }
/*      */ 
/* 1483 */       if (shouldStop(CompileStates.CompileState.TRANSTYPES)) {
/* 1484 */         return;
/*      */       }
/* 1486 */       paramEnv.tree = this.transTypes.translateTopLevelClass(paramEnv.tree, localTreeMaker);
/* 1487 */       this.compileStates.put(paramEnv, CompileStates.CompileState.TRANSTYPES);
/*      */ 
/* 1489 */       if ((this.source.allowLambda()) && (local1ScanNested.hasLambdas)) {
/* 1490 */         if (shouldStop(CompileStates.CompileState.UNLAMBDA)) {
/* 1491 */           return;
/*      */         }
/* 1493 */         paramEnv.tree = LambdaToMethod.instance(this.context).translateTopLevelClass(paramEnv, paramEnv.tree, localTreeMaker);
/* 1494 */         this.compileStates.put(paramEnv, CompileStates.CompileState.UNLAMBDA);
/*      */       }
/*      */ 
/* 1497 */       if (shouldStop(CompileStates.CompileState.LOWER)) {
/* 1498 */         return;
/*      */       }
/* 1500 */       if (this.sourceOutput)
/*      */       {
/* 1503 */         localObject3 = (JCTree.JCClassDecl)paramEnv.tree;
/* 1504 */         if (((localObject2 instanceof JCTree.JCClassDecl)) && 
/* 1505 */           (this.rootClasses
/* 1505 */           .contains((JCTree.JCClassDecl)localObject2)))
/*      */         {
/* 1506 */           paramQueue.add(new Pair(paramEnv, localObject3));
/*      */         }
/* 1508 */         return;
/*      */       }
/*      */ 
/* 1512 */       Object localObject3 = this.lower.translateTopLevelClass(paramEnv, paramEnv.tree, localTreeMaker);
/* 1513 */       this.compileStates.put(paramEnv, CompileStates.CompileState.LOWER);
/*      */ 
/* 1515 */       if (shouldStop(CompileStates.CompileState.LOWER)) {
/* 1516 */         return;
/*      */       }
/*      */ 
/* 1519 */       for (Object localObject4 = localObject3; ((List)localObject4).nonEmpty(); localObject4 = ((List)localObject4).tail) {
/* 1520 */         JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)((List)localObject4).head;
/* 1521 */         paramQueue.add(new Pair(paramEnv, localJCClassDecl));
/*      */       }
/*      */     }
/*      */     finally {
/* 1525 */       this.log.useSource((JavaFileObject)localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void generate(Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> paramQueue)
/*      */   {
/* 1536 */     generate(paramQueue, null);
/*      */   }
/*      */ 
/*      */   public void generate(Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> paramQueue, Queue<JavaFileObject> paramQueue1) {
/* 1540 */     if (shouldStop(CompileStates.CompileState.GENERATE)) {
/* 1541 */       return;
/*      */     }
/* 1543 */     int i = (this.stubOutput) || (this.sourceOutput) || (this.printFlat) ? 1 : 0;
/*      */ 
/* 1545 */     for (Pair localPair : paramQueue) {
/* 1546 */       Env localEnv = (Env)localPair.fst;
/* 1547 */       JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localPair.snd;
/*      */ 
/* 1549 */       if (this.verboseCompilePolicy) {
/* 1550 */         printNote("[generate " + (i != 0 ? " source" : "code") + " " + localJCClassDecl.sym + "]");
/*      */       }
/*      */ 
/* 1555 */       if (!this.taskListener.isEmpty()) {
/* 1556 */         localObject1 = new TaskEvent(TaskEvent.Kind.GENERATE, localEnv.toplevel, localJCClassDecl.sym);
/* 1557 */         this.taskListener.started((TaskEvent)localObject1);
/*      */       }
/*      */ 
/* 1560 */       Object localObject1 = this.log.useSource(localEnv.enclClass.sym.sourcefile != null ? localEnv.enclClass.sym.sourcefile : localEnv.toplevel.sourcefile);
/*      */       try
/*      */       {
/*      */         JavaFileObject localJavaFileObject;
/* 1565 */         if (i != 0) {
/* 1566 */           localJavaFileObject = printSource(localEnv, localJCClassDecl);
/*      */         } else {
/* 1568 */           if ((this.fileManager.hasLocation(StandardLocation.NATIVE_HEADER_OUTPUT)) && 
/* 1569 */             (this.jniWriter
/* 1569 */             .needsHeader(localJCClassDecl.sym)))
/*      */           {
/* 1570 */             this.jniWriter.write(localJCClassDecl.sym);
/*      */           }
/* 1572 */           localJavaFileObject = genCode(localEnv, localJCClassDecl);
/*      */         }
/* 1574 */         if ((paramQueue1 != null) && (localJavaFileObject != null))
/* 1575 */           paramQueue1.add(localJavaFileObject);
/*      */       } catch (IOException localIOException) {
/* 1577 */         this.log.error(localJCClassDecl.pos(), "class.cant.write", new Object[] { localJCClassDecl.sym, localIOException
/* 1578 */           .getMessage() });
/* 1579 */         return;
/*      */       } finally {
/* 1581 */         this.log.useSource((JavaFileObject)localObject1);
/*      */       }
/*      */ 
/* 1584 */       if (!this.taskListener.isEmpty()) {
/* 1585 */         TaskEvent localTaskEvent = new TaskEvent(TaskEvent.Kind.GENERATE, localEnv.toplevel, localJCClassDecl.sym);
/* 1586 */         this.taskListener.finished(localTaskEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Map<JCTree.JCCompilationUnit, Queue<Env<AttrContext>>> groupByFile(Queue<Env<AttrContext>> paramQueue)
/*      */   {
/* 1594 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/* 1595 */     for (Env localEnv : paramQueue) {
/* 1596 */       Object localObject = (Queue)localLinkedHashMap.get(localEnv.toplevel);
/* 1597 */       if (localObject == null) {
/* 1598 */         localObject = new ListBuffer();
/* 1599 */         localLinkedHashMap.put(localEnv.toplevel, localObject);
/*      */       }
/* 1601 */       ((Queue)localObject).add(localEnv);
/*      */     }
/* 1603 */     return localLinkedHashMap;
/*      */   }
/*      */ 
/*      */   JCTree.JCClassDecl removeMethodBodies(JCTree.JCClassDecl paramJCClassDecl) {
/* 1607 */     final boolean bool = (paramJCClassDecl.mods.flags & 0x200) != 0L;
/*      */ 
/* 1655 */     TreeTranslator local1MethodBodyRemover = new TreeTranslator()
/*      */     {
/*      */       public void visitMethodDef(JCTree.JCMethodDecl paramAnonymousJCMethodDecl)
/*      */       {
/* 1611 */         paramAnonymousJCMethodDecl.mods.flags &= -33L;
/* 1612 */         for (JCTree.JCVariableDecl localJCVariableDecl : paramAnonymousJCMethodDecl.params)
/* 1613 */           localJCVariableDecl.mods.flags &= -17L;
/* 1614 */         paramAnonymousJCMethodDecl.body = null;
/* 1615 */         super.visitMethodDef(paramAnonymousJCMethodDecl);
/*      */       }
/*      */ 
/*      */       public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl) {
/* 1619 */         if ((paramAnonymousJCVariableDecl.init != null) && (paramAnonymousJCVariableDecl.init.type.constValue() == null))
/* 1620 */           paramAnonymousJCVariableDecl.init = null;
/* 1621 */         super.visitVarDef(paramAnonymousJCVariableDecl);
/*      */       }
/*      */ 
/*      */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl) {
/* 1625 */         ListBuffer localListBuffer = new ListBuffer();
/* 1626 */         for (List localList = paramAnonymousJCClassDecl.defs; localList.tail != null; localList = localList.tail) {
/* 1627 */           JCTree localJCTree = (JCTree)localList.head;
/* 1628 */           switch (JavaCompiler.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localJCTree.getTag().ordinal()]) {
/*      */           case 1:
/* 1630 */             if ((!bool) && ((((JCTree.JCClassDecl)localJCTree).mods.flags & 0x5) == 0L)) { if ((((JCTree.JCClassDecl)localJCTree).mods.flags & 0x2) == 0L)
/*      */               {
/* 1632 */                 if (((JCTree.JCClassDecl)localJCTree).sym
/* 1632 */                   .packge().getQualifiedName() != this.this$0.names.java_lang) break;
/*      */               } } else localListBuffer.append(localJCTree); break;
/*      */           case 2:
/* 1636 */             if ((!bool) && ((((JCTree.JCMethodDecl)localJCTree).mods.flags & 0x5) == 0L) && (((JCTree.JCMethodDecl)localJCTree).sym.name != this.this$0.names.init)) { if ((((JCTree.JCMethodDecl)localJCTree).mods.flags & 0x2) == 0L)
/*      */               {
/* 1639 */                 if (((JCTree.JCMethodDecl)localJCTree).sym
/* 1639 */                   .packge().getQualifiedName() != this.this$0.names.java_lang) break;
/*      */               } } else localListBuffer.append(localJCTree); break;
/*      */           case 3:
/* 1643 */             if ((bool) || ((((JCTree.JCVariableDecl)localJCTree).mods.flags & 0x5) != 0L) || (((((JCTree.JCVariableDecl)localJCTree).mods.flags & 0x2) == 0L) && 
/* 1644 */               (((JCTree.JCVariableDecl)localJCTree).sym
/* 1644 */               .packge().getQualifiedName() == this.this$0.names.java_lang))) {
/* 1645 */               localListBuffer.append(localJCTree);
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1651 */         paramAnonymousJCClassDecl.defs = localListBuffer.toList();
/* 1652 */         super.visitClassDef(paramAnonymousJCClassDecl);
/*      */       }
/*      */     };
/* 1656 */     return (JCTree.JCClassDecl)local1MethodBodyRemover.translate(paramJCClassDecl);
/*      */   }
/*      */ 
/*      */   public void reportDeferredDiagnostics() {
/* 1660 */     if ((errorCount() == 0) && (this.annotationProcessingOccurred) && (this.implicitSourceFilesRead) && (this.implicitSourcePolicy == ImplicitSourcePolicy.UNSET))
/*      */     {
/* 1664 */       if (explicitAnnotationProcessingRequested())
/* 1665 */         this.log.warning("proc.use.implicit", new Object[0]);
/*      */       else
/* 1667 */         this.log.warning("proc.use.proc.or.implicit", new Object[0]);
/*      */     }
/* 1669 */     this.chk.reportDeferredDiagnostics();
/* 1670 */     if (this.log.compressedOutput)
/* 1671 */       this.log.mandatoryNote(null, "compressed.diags", new Object[0]);
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 1678 */     close(true);
/*      */   }
/*      */ 
/*      */   public void close(boolean paramBoolean) {
/* 1682 */     this.rootClasses = null;
/* 1683 */     this.reader = null;
/* 1684 */     this.make = null;
/* 1685 */     this.writer = null;
/* 1686 */     this.enter = null;
/* 1687 */     if (this.todo != null)
/* 1688 */       this.todo.clear();
/* 1689 */     this.todo = null;
/* 1690 */     this.parserFactory = null;
/* 1691 */     this.syms = null;
/* 1692 */     this.source = null;
/* 1693 */     this.attr = null;
/* 1694 */     this.chk = null;
/* 1695 */     this.gen = null;
/* 1696 */     this.flow = null;
/* 1697 */     this.transTypes = null;
/* 1698 */     this.lower = null;
/* 1699 */     this.annotate = null;
/* 1700 */     this.types = null;
/*      */ 
/* 1702 */     this.log.flush();
/*      */     try {
/* 1704 */       this.fileManager.flush();
/*      */     }
/*      */     catch (IOException localIOException1)
/*      */     {
/*      */       Iterator localIterator1;
/*      */       Closeable localCloseable1;
/*      */       JCDiagnostic localJCDiagnostic1;
/* 1706 */       throw new Abort(localIOException1);
/*      */     } finally {
/* 1708 */       if ((this.names != null) && (paramBoolean))
/* 1709 */         this.names.dispose();
/* 1710 */       this.names = null;
/*      */ 
/* 1712 */       for (Closeable localCloseable2 : this.closeables) {
/*      */         try {
/* 1714 */           localCloseable2.close();
/*      */         }
/*      */         catch (IOException localIOException3)
/*      */         {
/* 1720 */           JCDiagnostic localJCDiagnostic2 = this.diagFactory.fragment("fatal.err.cant.close", new Object[0]);
/* 1721 */           throw new FatalError(localJCDiagnostic2, localIOException3);
/*      */         }
/*      */       }
/* 1724 */       this.closeables = List.nil();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printNote(String paramString) {
/* 1729 */     this.log.printRawLines(Log.WriterKind.NOTICE, paramString);
/*      */   }
/*      */ 
/*      */   public void printCount(String paramString, int paramInt)
/*      */   {
/* 1735 */     if (paramInt != 0)
/*      */     {
/*      */       String str;
/* 1737 */       if (paramInt == 1)
/* 1738 */         str = "count." + paramString;
/*      */       else
/* 1740 */         str = "count." + paramString + ".plural";
/* 1741 */       this.log.printLines(Log.WriterKind.ERROR, str, new Object[] { String.valueOf(paramInt) });
/* 1742 */       this.log.flush(Log.WriterKind.ERROR);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static long now() {
/* 1747 */     return System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   private static long elapsed(long paramLong) {
/* 1751 */     return now() - paramLong;
/*      */   }
/*      */ 
/*      */   public void initRound(JavaCompiler paramJavaCompiler) {
/* 1755 */     this.genEndPos = paramJavaCompiler.genEndPos;
/* 1756 */     this.keepComments = paramJavaCompiler.keepComments;
/* 1757 */     this.start_msec = paramJavaCompiler.start_msec;
/* 1758 */     this.hasBeenUsed = true;
/* 1759 */     this.closeables = paramJavaCompiler.closeables;
/* 1760 */     paramJavaCompiler.closeables = List.nil();
/* 1761 */     this.shouldStopPolicyIfError = paramJavaCompiler.shouldStopPolicyIfError;
/* 1762 */     this.shouldStopPolicyIfNoError = paramJavaCompiler.shouldStopPolicyIfNoError;
/*      */   }
/*      */ 
/*      */   protected static enum CompilePolicy
/*      */   {
/*  139 */     ATTR_ONLY, 
/*      */ 
/*  145 */     CHECK_ONLY, 
/*      */ 
/*  153 */     SIMPLE, 
/*      */ 
/*  161 */     BY_FILE, 
/*      */ 
/*  169 */     BY_TODO;
/*      */ 
/*      */     static CompilePolicy decode(String paramString) {
/*  172 */       if (paramString == null)
/*  173 */         return JavaCompiler.DEFAULT_COMPILE_POLICY;
/*  174 */       if (paramString.equals("attr"))
/*  175 */         return ATTR_ONLY;
/*  176 */       if (paramString.equals("check"))
/*  177 */         return CHECK_ONLY;
/*  178 */       if (paramString.equals("simple"))
/*  179 */         return SIMPLE;
/*  180 */       if (paramString.equals("byfile"))
/*  181 */         return BY_FILE;
/*  182 */       if (paramString.equals("bytodo")) {
/*  183 */         return BY_TODO;
/*      */       }
/*  185 */       return JavaCompiler.DEFAULT_COMPILE_POLICY;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static enum ImplicitSourcePolicy
/*      */   {
/*  193 */     NONE, 
/*      */ 
/*  195 */     CLASS, 
/*      */ 
/*  197 */     UNSET;
/*      */ 
/*      */     static ImplicitSourcePolicy decode(String paramString) {
/*  200 */       if (paramString == null)
/*  201 */         return UNSET;
/*  202 */       if (paramString.equals("none"))
/*  203 */         return NONE;
/*  204 */       if (paramString.equals("class")) {
/*  205 */         return CLASS;
/*      */       }
/*  207 */       return UNSET;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.main.JavaCompiler
 * JD-Core Version:    0.6.2
 */