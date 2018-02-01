/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.MethodTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.tree.VariableTree;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.Plugin;
/*     */ import com.sun.source.util.TaskEvent;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.source.util.TreePathScanner;
/*     */ import com.sun.tools.javac.api.JavacTaskImpl;
/*     */ import com.sun.tools.javac.api.JavacTool;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class DocLint
/*     */   implements Plugin
/*     */ {
/*     */   public static final String XMSGS_OPTION = "-Xmsgs";
/*     */   public static final String XMSGS_CUSTOM_PREFIX = "-Xmsgs:";
/*     */   private static final String STATS = "-stats";
/*     */   public static final String XIMPLICIT_HEADERS = "-XimplicitHeaders:";
/*     */   public static final String XCUSTOM_TAGS_PREFIX = "-XcustomTags:";
/*     */   public static final String TAGS_SEPARATOR = ",";
/*     */   List<File> javacBootClassPath;
/*     */   List<File> javacClassPath;
/*     */   List<File> javacSourcePath;
/*     */   List<String> javacOpts;
/*     */   List<File> javacFiles;
/* 238 */   boolean needHelp = false;
/*     */   Env env;
/*     */   Checker checker;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  86 */     DocLint localDocLint = new DocLint();
/*     */     try {
/*  88 */       localDocLint.run(paramArrayOfString);
/*     */     } catch (BadArgs localBadArgs) {
/*  90 */       System.err.println(localBadArgs.getMessage());
/*  91 */       System.exit(1);
/*     */     } catch (IOException localIOException) {
/*  93 */       System.err.println(localDocLint.localize("dc.main.ioerror", new Object[] { localIOException.getLocalizedMessage() }));
/*  94 */       System.exit(2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run(String[] paramArrayOfString)
/*     */     throws DocLint.BadArgs, IOException
/*     */   {
/* 118 */     PrintWriter localPrintWriter = new PrintWriter(System.out);
/*     */     try {
/* 120 */       run(localPrintWriter, paramArrayOfString);
/*     */ 
/* 122 */       localPrintWriter.flush(); } finally { localPrintWriter.flush(); }
/*     */   }
/*     */ 
/*     */   public void run(PrintWriter paramPrintWriter, String[] paramArrayOfString) throws DocLint.BadArgs, IOException
/*     */   {
/* 127 */     this.env = new Env();
/* 128 */     processArgs(paramArrayOfString);
/*     */ 
/* 130 */     if (this.needHelp) {
/* 131 */       showHelp(paramPrintWriter);
/*     */     }
/* 133 */     if ((this.javacFiles.isEmpty()) && 
/* 134 */       (!this.needHelp)) {
/* 135 */       paramPrintWriter.println(localize("dc.main.no.files.given", new Object[0]));
/*     */     }
/*     */ 
/* 138 */     JavacTool localJavacTool = JavacTool.create();
/*     */ 
/* 140 */     JavacFileManager localJavacFileManager = new JavacFileManager(new Context(), false, null);
/* 141 */     localJavacFileManager.setSymbolFileEnabled(false);
/* 142 */     localJavacFileManager.setLocation(StandardLocation.PLATFORM_CLASS_PATH, this.javacBootClassPath);
/* 143 */     localJavacFileManager.setLocation(StandardLocation.CLASS_PATH, this.javacClassPath);
/* 144 */     localJavacFileManager.setLocation(StandardLocation.SOURCE_PATH, this.javacSourcePath);
/*     */ 
/* 146 */     JavacTask localJavacTask = localJavacTool.getTask(paramPrintWriter, localJavacFileManager, null, this.javacOpts, null, localJavacFileManager
/* 147 */       .getJavaFileObjectsFromFiles(this.javacFiles));
/*     */ 
/* 148 */     Iterable localIterable = localJavacTask.parse();
/* 149 */     ((JavacTaskImpl)localJavacTask).enter();
/*     */ 
/* 151 */     this.env.init(localJavacTask);
/* 152 */     this.checker = new Checker(this.env);
/*     */ 
/* 154 */     DeclScanner local1 = new DeclScanner()
/*     */     {
/*     */       void visitDecl(Tree paramAnonymousTree, Name paramAnonymousName) {
/* 157 */         TreePath localTreePath = getCurrentPath();
/* 158 */         DocCommentTree localDocCommentTree = DocLint.this.env.trees.getDocCommentTree(localTreePath);
/*     */ 
/* 160 */         DocLint.this.checker.scan(localDocCommentTree, localTreePath);
/*     */       }
/*     */     };
/* 164 */     local1.scan(localIterable, null);
/*     */ 
/* 166 */     reportStats(paramPrintWriter);
/*     */ 
/* 168 */     Context localContext = ((JavacTaskImpl)localJavacTask).getContext();
/* 169 */     JavaCompiler localJavaCompiler = JavaCompiler.instance(localContext);
/* 170 */     localJavaCompiler.printCount("error", localJavaCompiler.errorCount());
/* 171 */     localJavaCompiler.printCount("warn", localJavaCompiler.warningCount());
/*     */   }
/*     */ 
/*     */   void processArgs(String[] paramArrayOfString) throws DocLint.BadArgs {
/* 175 */     this.javacOpts = new ArrayList();
/* 176 */     this.javacFiles = new ArrayList();
/*     */ 
/* 178 */     if (paramArrayOfString.length == 0) {
/* 179 */       this.needHelp = true;
/*     */     }
/* 181 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 182 */       String str = paramArrayOfString[i];
/* 183 */       if ((str.matches("-Xmax(errs|warns)")) && (i + 1 < paramArrayOfString.length)) {
/* 184 */         if (paramArrayOfString[(++i)].matches("[0-9]+")) {
/* 185 */           this.javacOpts.add(str);
/* 186 */           this.javacOpts.add(paramArrayOfString[i]);
/*     */         } else {
/* 188 */           throw new BadArgs("dc.bad.value.for.option", new Object[] { str, paramArrayOfString[i] });
/*     */         }
/* 190 */       } else if (str.equals("-stats")) {
/* 191 */         this.env.messages.setStatsEnabled(true);
/* 192 */       } else if ((str.equals("-bootclasspath")) && (i + 1 < paramArrayOfString.length)) {
/* 193 */         this.javacBootClassPath = splitPath(paramArrayOfString[(++i)]);
/* 194 */       } else if ((str.equals("-classpath")) && (i + 1 < paramArrayOfString.length)) {
/* 195 */         this.javacClassPath = splitPath(paramArrayOfString[(++i)]);
/* 196 */       } else if ((str.equals("-cp")) && (i + 1 < paramArrayOfString.length)) {
/* 197 */         this.javacClassPath = splitPath(paramArrayOfString[(++i)]);
/* 198 */       } else if ((str.equals("-sourcepath")) && (i + 1 < paramArrayOfString.length)) {
/* 199 */         this.javacSourcePath = splitPath(paramArrayOfString[(++i)]);
/* 200 */       } else if (str.equals("-Xmsgs")) {
/* 201 */         this.env.messages.setOptions(null);
/* 202 */       } else if (str.startsWith("-Xmsgs:")) {
/* 203 */         this.env.messages.setOptions(str.substring(str.indexOf(":") + 1));
/* 204 */       } else if (str.startsWith("-XcustomTags:")) {
/* 205 */         this.env.setCustomTags(str.substring(str.indexOf(":") + 1));
/* 206 */       } else if ((str.equals("-h")) || (str.equals("-help")) || (str.equals("--help")) || 
/* 207 */         (str
/* 207 */         .equals("-?")) || 
/* 207 */         (str.equals("-usage"))) {
/* 208 */         this.needHelp = true; } else {
/* 209 */         if (str.startsWith("-")) {
/* 210 */           throw new BadArgs("dc.bad.option", new Object[] { str });
/*     */         }
/* 212 */         while (i < paramArrayOfString.length)
/* 213 */           this.javacFiles.add(new File(paramArrayOfString[(i++)]));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void showHelp(PrintWriter paramPrintWriter) {
/* 219 */     String str1 = localize("dc.main.usage", new Object[0]);
/* 220 */     for (String str2 : str1.split("\n"))
/* 221 */       paramPrintWriter.println(str2);
/*     */   }
/*     */ 
/*     */   List<File> splitPath(String paramString) {
/* 225 */     ArrayList localArrayList = new ArrayList();
/* 226 */     for (String str : paramString.split(File.pathSeparator)) {
/* 227 */       if (str.length() > 0)
/* 228 */         localArrayList.add(new File(str));
/*     */     }
/* 230 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 246 */     return "doclint";
/*     */   }
/*     */ 
/*     */   public void init(JavacTask paramJavacTask, String[] paramArrayOfString)
/*     */   {
/* 251 */     init(paramJavacTask, paramArrayOfString, true);
/*     */   }
/*     */ 
/*     */   public void init(JavacTask paramJavacTask, String[] paramArrayOfString, boolean paramBoolean)
/*     */   {
/* 259 */     this.env = new Env();
/*     */     Object localObject;
/* 260 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 261 */       localObject = paramArrayOfString[i];
/* 262 */       if (((String)localObject).equals("-Xmsgs")) {
/* 263 */         this.env.messages.setOptions(null);
/* 264 */       } else if (((String)localObject).startsWith("-Xmsgs:")) {
/* 265 */         this.env.messages.setOptions(((String)localObject).substring(((String)localObject).indexOf(":") + 1));
/* 266 */       } else if (((String)localObject).matches("-XimplicitHeaders:[1-6]")) {
/* 267 */         char c = ((String)localObject).charAt(((String)localObject).length() - 1);
/* 268 */         this.env.setImplicitHeaders(Character.digit(c, 10));
/* 269 */       } else if (((String)localObject).startsWith("-XcustomTags:")) {
/* 270 */         this.env.setCustomTags(((String)localObject).substring(((String)localObject).indexOf(":") + 1));
/*     */       } else {
/* 272 */         throw new IllegalArgumentException((String)localObject);
/*     */       }
/*     */     }
/* 274 */     this.env.init(paramJavacTask);
/*     */ 
/* 276 */     this.checker = new Checker(this.env);
/*     */ 
/* 278 */     if (paramBoolean) {
/* 279 */       final DeclScanner local2 = new DeclScanner()
/*     */       {
/*     */         void visitDecl(Tree paramAnonymousTree, Name paramAnonymousName) {
/* 282 */           TreePath localTreePath = getCurrentPath();
/* 283 */           DocCommentTree localDocCommentTree = DocLint.this.env.trees.getDocCommentTree(localTreePath);
/*     */ 
/* 285 */           DocLint.this.checker.scan(localDocCommentTree, localTreePath);
/*     */         }
/*     */       };
/* 289 */       localObject = new TaskListener()
/*     */       {
/* 310 */         Queue<CompilationUnitTree> todo = new LinkedList();
/*     */ 
/*     */         public void started(TaskEvent paramAnonymousTaskEvent)
/*     */         {
/* 292 */           switch (DocLint.4.$SwitchMap$com$sun$source$util$TaskEvent$Kind[paramAnonymousTaskEvent.getKind().ordinal()])
/*     */           {
/*     */           case 1:
/*     */             CompilationUnitTree localCompilationUnitTree;
/* 295 */             while ((localCompilationUnitTree = (CompilationUnitTree)this.todo.poll()) != null)
/* 296 */               local2.scan(localCompilationUnitTree, null);
/*     */           }
/*     */         }
/*     */ 
/*     */         public void finished(TaskEvent paramAnonymousTaskEvent)
/*     */         {
/* 303 */           switch (DocLint.4.$SwitchMap$com$sun$source$util$TaskEvent$Kind[paramAnonymousTaskEvent.getKind().ordinal()]) {
/*     */           case 2:
/* 305 */             this.todo.add(paramAnonymousTaskEvent.getCompilationUnit());
/*     */           }
/*     */         }
/*     */       };
/* 313 */       paramJavacTask.addTaskListener((TaskListener)localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void scan(TreePath paramTreePath) {
/* 318 */     DocCommentTree localDocCommentTree = this.env.trees.getDocCommentTree(paramTreePath);
/* 319 */     this.checker.scan(localDocCommentTree, paramTreePath);
/*     */   }
/*     */ 
/*     */   public void reportStats(PrintWriter paramPrintWriter) {
/* 323 */     this.env.messages.reportStats(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public static boolean isValidOption(String paramString)
/*     */   {
/* 332 */     if (paramString.equals("-Xmsgs"))
/* 333 */       return true;
/* 334 */     if (paramString.startsWith("-Xmsgs:"))
/* 335 */       return Messages.Options.isValidOptions(paramString.substring("-Xmsgs:".length()));
/* 336 */     return false;
/*     */   }
/*     */ 
/*     */   private String localize(String paramString, Object[] paramArrayOfObject) {
/* 340 */     Messages localMessages = this.env != null ? this.env.messages : new Messages(null);
/* 341 */     return localMessages.localize(paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public class BadArgs extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     final String code;
/*     */     final Object[] args;
/*     */ 
/*     */     BadArgs(String paramArrayOfObject, Object[] arg3)
/*     */     {
/* 105 */       super();
/* 106 */       this.code = paramArrayOfObject;
/* 107 */       this.args = arrayOfObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class DeclScanner extends TreePathScanner<Void, Void>
/*     */   {
/*     */     abstract void visitDecl(Tree paramTree, Name paramName);
/*     */ 
/*     */     public Void visitCompilationUnit(CompilationUnitTree paramCompilationUnitTree, Void paramVoid)
/*     */     {
/* 351 */       if (paramCompilationUnitTree.getPackageName() != null) {
/* 352 */         visitDecl(paramCompilationUnitTree, null);
/*     */       }
/* 354 */       return (Void)super.visitCompilationUnit(paramCompilationUnitTree, paramVoid);
/*     */     }
/*     */ 
/*     */     public Void visitClass(ClassTree paramClassTree, Void paramVoid)
/*     */     {
/* 359 */       visitDecl(paramClassTree, paramClassTree.getSimpleName());
/* 360 */       return (Void)super.visitClass(paramClassTree, paramVoid);
/*     */     }
/*     */ 
/*     */     public Void visitMethod(MethodTree paramMethodTree, Void paramVoid)
/*     */     {
/* 365 */       visitDecl(paramMethodTree, paramMethodTree.getName());
/*     */ 
/* 367 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitVariable(VariableTree paramVariableTree, Void paramVoid)
/*     */     {
/* 372 */       visitDecl(paramVariableTree, paramVariableTree.getName());
/* 373 */       return (Void)super.visitVariable(paramVariableTree, paramVoid);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.DocLint
 * JD-Core Version:    0.6.2
 */