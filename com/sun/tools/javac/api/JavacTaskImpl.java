/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.comp.Attr;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.main.CommandLine;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.main.Main;
/*     */ import com.sun.tools.javac.main.Main.Result;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.parser.JavacParser;
/*     */ import com.sun.tools.javac.parser.Parser;
/*     */ import com.sun.tools.javac.parser.ParserFactory;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.annotation.processing.Processor;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class JavacTaskImpl extends BasicJavacTask
/*     */ {
/*     */   private Main compilerMain;
/*     */   private JavaCompiler compiler;
/*     */   private Locale locale;
/*     */   private String[] args;
/*     */   private String[] classNames;
/*     */   private List<JavaFileObject> fileObjects;
/*     */   private Map<JavaFileObject, JCTree.JCCompilationUnit> notYetEntered;
/*     */   private ListBuffer<Env<AttrContext>> genList;
/*  76 */   private final AtomicBoolean used = new AtomicBoolean();
/*     */   private Iterable<? extends Processor> processors;
/*  79 */   private Main.Result result = null;
/*     */ 
/* 249 */   private boolean parsed = false;
/*     */ 
/*     */   JavacTaskImpl(Main paramMain, String[] paramArrayOfString1, String[] paramArrayOfString2, Context paramContext, List<JavaFileObject> paramList)
/*     */   {
/*  86 */     super(null, false);
/*  87 */     this.compilerMain = paramMain;
/*  88 */     this.args = paramArrayOfString1;
/*  89 */     this.classNames = paramArrayOfString2;
/*  90 */     this.context = paramContext;
/*  91 */     this.fileObjects = paramList;
/*  92 */     setLocale(Locale.getDefault());
/*     */ 
/*  94 */     paramMain.getClass();
/*  95 */     paramArrayOfString1.getClass();
/*  96 */     paramList.getClass();
/*     */   }
/*     */ 
/*     */   JavacTaskImpl(Main paramMain, Iterable<String> paramIterable1, Context paramContext, Iterable<String> paramIterable2, Iterable<? extends JavaFileObject> paramIterable)
/*     */   {
/* 104 */     this(paramMain, toArray(paramIterable1), toArray(paramIterable2), paramContext, toList(paramIterable));
/*     */   }
/*     */ 
/*     */   private static String[] toArray(Iterable<String> paramIterable) {
/* 108 */     ListBuffer localListBuffer = new ListBuffer();
/* 109 */     if (paramIterable != null)
/* 110 */       for (String str : paramIterable)
/* 111 */         localListBuffer.append(str);
/* 112 */     return (String[])localListBuffer.toArray(new String[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   private static List<JavaFileObject> toList(Iterable<? extends JavaFileObject> paramIterable) {
/* 116 */     if (paramIterable == null)
/* 117 */       return List.nil();
/* 118 */     ListBuffer localListBuffer = new ListBuffer();
/* 119 */     for (JavaFileObject localJavaFileObject : paramIterable)
/* 120 */       localListBuffer.append(localJavaFileObject);
/* 121 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public Main.Result doCall() {
/* 125 */     if (!this.used.getAndSet(true)) {
/* 126 */       initContext();
/* 127 */       this.notYetEntered = new HashMap();
/* 128 */       this.compilerMain.setAPIMode(true);
/* 129 */       this.result = this.compilerMain.compile(this.args, this.classNames, this.context, this.fileObjects, this.processors);
/* 130 */       cleanup();
/* 131 */       return this.result;
/*     */     }
/* 133 */     throw new IllegalStateException("multiple calls to method 'call'");
/*     */   }
/*     */ 
/*     */   public Boolean call()
/*     */   {
/* 138 */     return Boolean.valueOf(doCall().isOK());
/*     */   }
/*     */ 
/*     */   public void setProcessors(Iterable<? extends Processor> paramIterable) {
/* 142 */     paramIterable.getClass();
/*     */ 
/* 144 */     if (this.used.get())
/* 145 */       throw new IllegalStateException();
/* 146 */     this.processors = paramIterable;
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale paramLocale) {
/* 150 */     if (this.used.get())
/* 151 */       throw new IllegalStateException();
/* 152 */     this.locale = paramLocale;
/*     */   }
/*     */ 
/*     */   private void prepareCompiler() throws IOException {
/* 156 */     if (this.used.getAndSet(true)) {
/* 157 */       if (this.compiler == null)
/* 158 */         throw new IllegalStateException();
/*     */     } else {
/* 160 */       initContext();
/* 161 */       this.compilerMain.log = Log.instance(this.context);
/* 162 */       this.compilerMain.setOptions(Options.instance(this.context));
/* 163 */       this.compilerMain.filenames = new LinkedHashSet();
/* 164 */       Collection localCollection = this.compilerMain.processArgs(CommandLine.parse(this.args), this.classNames);
/* 165 */       if ((localCollection != null) && (!localCollection.isEmpty()))
/* 166 */         throw new IllegalArgumentException("Malformed arguments " + toString(localCollection, " "));
/* 167 */       this.compiler = JavaCompiler.instance(this.context);
/* 168 */       this.compiler.keepComments = true;
/* 169 */       this.compiler.genEndPos = true;
/*     */ 
/* 171 */       this.compiler.initProcessAnnotations(this.processors);
/* 172 */       this.notYetEntered = new HashMap();
/* 173 */       for (JavaFileObject localJavaFileObject : this.fileObjects)
/* 174 */         this.notYetEntered.put(localJavaFileObject, null);
/* 175 */       this.genList = new ListBuffer();
/*     */ 
/* 178 */       this.args = null;
/* 179 */       this.classNames = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   <T> String toString(Iterable<T> paramIterable, String paramString) {
/* 184 */     String str = "";
/* 185 */     StringBuilder localStringBuilder = new StringBuilder();
/* 186 */     for (Iterator localIterator = paramIterable.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 187 */       localStringBuilder.append(str);
/* 188 */       localStringBuilder.append(localObject.toString());
/* 189 */       str = paramString;
/*     */     }
/* 191 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void initContext() {
/* 195 */     this.context.put(JavacTask.class, this);
/*     */ 
/* 197 */     this.context.put(Locale.class, this.locale);
/*     */   }
/*     */ 
/*     */   void cleanup() {
/* 201 */     if (this.compiler != null)
/* 202 */       this.compiler.close();
/* 203 */     this.compiler = null;
/* 204 */     this.compilerMain = null;
/* 205 */     this.args = null;
/* 206 */     this.classNames = null;
/* 207 */     this.context = null;
/* 208 */     this.fileObjects = null;
/* 209 */     this.notYetEntered = null;
/*     */   }
/*     */ 
/*     */   public JavaFileObject asJavaFileObject(File paramFile)
/*     */   {
/* 221 */     JavacFileManager localJavacFileManager = (JavacFileManager)this.context.get(JavaFileManager.class);
/* 222 */     return localJavacFileManager.getRegularFile(paramFile);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends CompilationUnitTree> parse()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 233 */       prepareCompiler();
/* 234 */       List localList = this.compiler.parseFiles(this.fileObjects);
/* 235 */       for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)((Iterator)localObject1).next();
/* 236 */         JavaFileObject localJavaFileObject = localJCCompilationUnit.getSourceFile();
/* 237 */         if (this.notYetEntered.containsKey(localJavaFileObject))
/* 238 */           this.notYetEntered.put(localJavaFileObject, localJCCompilationUnit);
/*     */       }
/* 240 */       return localList;
/*     */     }
/*     */     finally {
/* 243 */       this.parsed = true;
/* 244 */       if ((this.compiler != null) && (this.compiler.log != null))
/* 245 */         this.compiler.log.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterable<? extends TypeElement> enter()
/*     */     throws IOException
/*     */   {
/* 259 */     return enter(null);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends TypeElement> enter(Iterable<? extends CompilationUnitTree> paramIterable)
/*     */     throws IOException
/*     */   {
/* 273 */     if ((paramIterable == null) && (this.notYetEntered != null) && (this.notYetEntered.isEmpty())) {
/* 274 */       return List.nil();
/*     */     }
/* 276 */     prepareCompiler();
/*     */ 
/* 278 */     ListBuffer localListBuffer = null;
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 280 */     if (paramIterable == null)
/*     */     {
/* 285 */       if (this.notYetEntered.size() > 0) {
/* 286 */         if (!this.parsed)
/* 287 */           parse();
/* 288 */         for (localObject1 = this.fileObjects.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JavaFileObject)((Iterator)localObject1).next();
/* 289 */           localObject3 = (JCTree.JCCompilationUnit)this.notYetEntered.remove(localObject2);
/* 290 */           if (localObject3 != null) {
/* 291 */             if (localListBuffer == null)
/* 292 */               localListBuffer = new ListBuffer();
/* 293 */             localListBuffer.append(localObject3);
/*     */           }
/*     */         }
/* 296 */         this.notYetEntered.clear();
/*     */       }
/*     */     }
/*     */     else {
/* 300 */       for (localObject1 = paramIterable.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (CompilationUnitTree)((Iterator)localObject1).next();
/* 301 */         if ((localObject2 instanceof JCTree.JCCompilationUnit)) {
/* 302 */           if (localListBuffer == null)
/* 303 */             localListBuffer = new ListBuffer();
/* 304 */           localListBuffer.append((JCTree.JCCompilationUnit)localObject2);
/* 305 */           this.notYetEntered.remove(((CompilationUnitTree)localObject2).getSourceFile());
/*     */         }
/*     */         else {
/* 308 */           throw new IllegalArgumentException(localObject2.toString());
/*     */         }
/*     */       }
/*     */     }
/* 312 */     if (localListBuffer == null)
/* 313 */       return List.nil();
/*     */     try
/*     */     {
/* 316 */       localObject1 = this.compiler.enterTrees(localListBuffer.toList());
/*     */ 
/* 318 */       if (this.notYetEntered.isEmpty()) {
/* 319 */         this.compiler = this.compiler.processAnnotations((List)localObject1);
/*     */       }
/* 321 */       localObject2 = new ListBuffer();
/* 322 */       for (localObject3 = ((List)localObject1).iterator(); ((Iterator)localObject3).hasNext(); ) { JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)((Iterator)localObject3).next();
/* 323 */         for (JCTree localJCTree : localJCCompilationUnit.defs) {
/* 324 */           if (localJCTree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 325 */             JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localJCTree;
/* 326 */             if (localJCClassDecl.sym != null)
/* 327 */               ((ListBuffer)localObject2).append(localJCClassDecl.sym);
/*     */           }
/*     */         }
/*     */       }
/* 331 */       return ((ListBuffer)localObject2).toList();
/*     */     }
/*     */     finally {
/* 334 */       this.compiler.log.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterable<? extends Element> analyze()
/*     */     throws IOException
/*     */   {
/* 344 */     return analyze(null);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends Element> analyze(Iterable<? extends TypeElement> paramIterable)
/*     */     throws IOException
/*     */   {
/* 359 */     enter(null);
/*     */ 
/* 361 */     final ListBuffer localListBuffer = new ListBuffer();
/*     */     try {
/* 363 */       if (paramIterable == null) {
/* 364 */         handleFlowResults(this.compiler.flow(this.compiler.attribute(this.compiler.todo)), localListBuffer);
/*     */       } else {
/* 366 */         Filter local1 = new Filter(localListBuffer) {
/*     */           public void process(Env<AttrContext> paramAnonymousEnv) {
/* 368 */             JavacTaskImpl.this.handleFlowResults(JavacTaskImpl.this.compiler.flow(JavacTaskImpl.this.compiler.attribute(paramAnonymousEnv)), localListBuffer);
/*     */           }
/*     */         };
/* 371 */         local1.run(this.compiler.todo, paramIterable);
/*     */       }
/*     */     } finally {
/* 374 */       this.compiler.log.flush();
/*     */     }
/* 376 */     return localListBuffer;
/*     */   }
/*     */ 
/*     */   private void handleFlowResults(Queue<Env<AttrContext>> paramQueue, ListBuffer<Element> paramListBuffer) {
/* 380 */     for (Env localEnv : paramQueue) {
/* 381 */       switch (3.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localEnv.tree.getTag().ordinal()]) {
/*     */       case 1:
/* 383 */         JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localEnv.tree;
/* 384 */         if (localJCClassDecl.sym != null)
/* 385 */           paramListBuffer.append(localJCClassDecl.sym); break;
/*     */       case 2:
/* 388 */         JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)localEnv.tree;
/* 389 */         if (localJCCompilationUnit.packge != null)
/* 390 */           paramListBuffer.append(localJCCompilationUnit.packge);
/*     */         break;
/*     */       }
/*     */     }
/* 394 */     this.genList.addAll(paramQueue);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> generate()
/*     */     throws IOException
/*     */   {
/* 404 */     return generate(null);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> generate(Iterable<? extends TypeElement> paramIterable)
/*     */     throws IOException
/*     */   {
/* 417 */     final ListBuffer localListBuffer = new ListBuffer();
/*     */     try {
/* 419 */       analyze(null);
/*     */ 
/* 421 */       if (paramIterable == null) {
/* 422 */         this.compiler.generate(this.compiler.desugar(this.genList), localListBuffer);
/* 423 */         this.genList.clear();
/*     */       }
/*     */       else {
/* 426 */         Filter local2 = new Filter(localListBuffer) {
/*     */           public void process(Env<AttrContext> paramAnonymousEnv) {
/* 428 */             JavacTaskImpl.this.compiler.generate(JavacTaskImpl.this.compiler.desugar(ListBuffer.of(paramAnonymousEnv)), localListBuffer);
/*     */           }
/*     */         };
/* 431 */         local2.run(this.genList, paramIterable);
/*     */       }
/* 433 */       if (this.genList.isEmpty()) {
/* 434 */         this.compiler.reportDeferredDiagnostics();
/* 435 */         cleanup();
/*     */       }
/*     */     }
/*     */     finally {
/* 439 */       if (this.compiler != null)
/* 440 */         this.compiler.log.flush();
/*     */     }
/* 442 */     return localListBuffer;
/*     */   }
/*     */ 
/*     */   public TypeMirror getTypeMirror(Iterable<? extends Tree> paramIterable)
/*     */   {
/* 447 */     Object localObject = null;
/* 448 */     for (Tree localTree : paramIterable)
/* 449 */       localObject = localTree;
/* 450 */     return ((JCTree)localObject).type;
/*     */   }
/*     */ 
/*     */   public JavacElements getElements() {
/* 454 */     if (this.context == null)
/* 455 */       throw new IllegalStateException();
/* 456 */     return JavacElements.instance(this.context);
/*     */   }
/*     */ 
/*     */   public JavacTypes getTypes() {
/* 460 */     if (this.context == null)
/* 461 */       throw new IllegalStateException();
/* 462 */     return JavacTypes.instance(this.context);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends Tree> pathFor(CompilationUnitTree paramCompilationUnitTree, Tree paramTree) {
/* 466 */     return TreeInfo.pathFor((JCTree)paramTree, (JCTree.JCCompilationUnit)paramCompilationUnitTree).reverse();
/*     */   }
/*     */ 
/*     */   public Type parseType(String paramString, TypeElement paramTypeElement)
/*     */   {
/* 496 */     if ((paramString == null) || (paramString.equals("")))
/* 497 */       throw new IllegalArgumentException();
/* 498 */     this.compiler = JavaCompiler.instance(this.context);
/* 499 */     JavaFileObject localJavaFileObject = this.compiler.log.useSource(null);
/* 500 */     ParserFactory localParserFactory = ParserFactory.instance(this.context);
/* 501 */     Attr localAttr = Attr.instance(this.context);
/*     */     try {
/* 503 */       CharBuffer localCharBuffer = CharBuffer.wrap((paramString + "").toCharArray(), 0, paramString.length());
/* 504 */       JavacParser localJavacParser = localParserFactory.newParser(localCharBuffer, false, false, false);
/* 505 */       JCTree.JCExpression localJCExpression = localJavacParser.parseType();
/* 506 */       return localAttr.attribType(localJCExpression, (Symbol.TypeSymbol)paramTypeElement);
/*     */     } finally {
/* 508 */       this.compiler.log.useSource(localJavaFileObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class Filter
/*     */   {
/*     */     Filter()
/*     */     {
/*     */     }
/*     */ 
/*     */     void run(Queue<Env<AttrContext>> paramQueue, Iterable<? extends TypeElement> paramIterable)
/*     */     {
/* 471 */       HashSet localHashSet = new HashSet();
/* 472 */       for (Object localObject1 = paramIterable.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (TypeElement)((Iterator)localObject1).next();
/* 473 */         localHashSet.add(localObject2);
/*     */       }
/*     */       Object localObject2;
/* 475 */       localObject1 = new ListBuffer();
/* 476 */       while (paramQueue.peek() != null) {
/* 477 */         localObject2 = (Env)paramQueue.remove();
/* 478 */         Symbol.ClassSymbol localClassSymbol = ((Env)localObject2).enclClass.sym;
/* 479 */         if ((localClassSymbol != null) && (localHashSet.contains(localClassSymbol.outermostClass())))
/* 480 */           process((Env)localObject2);
/*     */         else {
/* 482 */           localObject1 = ((ListBuffer)localObject1).append(localObject2);
/*     */         }
/*     */       }
/* 485 */       paramQueue.addAll((Collection)localObject1);
/*     */     }
/*     */ 
/*     */     abstract void process(Env<AttrContext> paramEnv);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.JavacTaskImpl
 * JD-Core Version:    0.6.2
 */