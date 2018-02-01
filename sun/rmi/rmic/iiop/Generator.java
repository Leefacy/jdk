/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.HashSet;
/*     */ import sun.rmi.rmic.IndentingWriter;
/*     */ import sun.rmi.rmic.Main;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public abstract class Generator
/*     */   implements sun.rmi.rmic.Generator, Constants
/*     */ {
/*  73 */   protected boolean alwaysGenerate = false;
/*  74 */   protected BatchEnvironment env = null;
/*  75 */   protected ContextStack contextStack = null;
/*  76 */   private boolean trace = false;
/*  77 */   protected boolean idl = false;
/*     */ 
/*     */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain)
/*     */   {
/*  87 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  88 */       if (paramArrayOfString[i] != null) {
/*  89 */         if ((paramArrayOfString[i].equalsIgnoreCase("-always")) || 
/*  90 */           (paramArrayOfString[i]
/*  90 */           .equalsIgnoreCase("-alwaysGenerate")))
/*     */         {
/*  91 */           this.alwaysGenerate = true;
/*  92 */           paramArrayOfString[i] = null;
/*  93 */         } else if (paramArrayOfString[i].equalsIgnoreCase("-xtrace")) {
/*  94 */           this.trace = true;
/*  95 */           paramArrayOfString[i] = null;
/*     */         }
/*     */       }
/*     */     }
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   protected abstract boolean parseNonConforming(ContextStack paramContextStack);
/*     */ 
/*     */   protected abstract CompoundType getTopType(ClassDefinition paramClassDefinition, ContextStack paramContextStack);
/*     */ 
/*     */   protected abstract OutputType[] getOutputTypesFor(CompoundType paramCompoundType, HashSet paramHashSet);
/*     */ 
/*     */   protected abstract String getFileNameExtensionFor(OutputType paramOutputType);
/*     */ 
/*     */   protected abstract void writeOutputFor(OutputType paramOutputType, HashSet paramHashSet, IndentingWriter paramIndentingWriter)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract boolean requireNewInstance();
/*     */ 
/*     */   public boolean requiresGeneration(File paramFile, Type paramType)
/*     */   {
/* 160 */     boolean bool = this.alwaysGenerate;
/*     */ 
/* 162 */     if (!bool)
/*     */     {
/* 169 */       ClassPath localClassPath = this.env.getClassPath();
/* 170 */       String str1 = paramType.getQualifiedName().replace('.', File.separatorChar);
/*     */ 
/* 174 */       ClassFile localClassFile = localClassPath.getFile(str1 + ".source");
/*     */ 
/* 176 */       if (localClassFile == null)
/*     */       {
/* 180 */         localClassFile = localClassPath.getFile(str1 + ".class");
/*     */       }
/*     */ 
/* 185 */       if (localClassFile != null)
/*     */       {
/* 189 */         long l1 = localClassFile.lastModified();
/*     */ 
/* 195 */         String str2 = IDLNames.replace(paramFile.getName(), ".java", ".class");
/* 196 */         String str3 = paramFile.getParent();
/* 197 */         File localFile = new File(str3, str2);
/*     */ 
/* 201 */         if (localFile.exists())
/*     */         {
/* 205 */           long l2 = localFile.lastModified();
/*     */ 
/* 209 */           bool = l2 < l1;
/*     */         }
/*     */         else
/*     */         {
/* 215 */           bool = true;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 221 */         bool = true;
/*     */       }
/*     */     }
/*     */ 
/* 225 */     return bool;
/*     */   }
/*     */ 
/*     */   protected Generator newInstance()
/*     */   {
/* 234 */     Generator localGenerator = null;
/*     */     try {
/* 236 */       localGenerator = (Generator)getClass().newInstance();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 240 */     return localGenerator;
/*     */   }
/*     */ 
/*     */   public void generate(sun.rmi.rmic.BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile)
/*     */   {
/* 262 */     this.env = ((BatchEnvironment)paramBatchEnvironment);
/* 263 */     this.contextStack = new ContextStack(this.env);
/* 264 */     this.contextStack.setTrace(this.trace);
/*     */ 
/* 270 */     this.env.setParseNonConforming(parseNonConforming(this.contextStack));
/*     */ 
/* 274 */     CompoundType localCompoundType = getTopType(paramClassDefinition, this.contextStack);
/* 275 */     if (localCompoundType != null)
/*     */     {
/* 277 */       Generator localGenerator = this;
/*     */ 
/* 281 */       if (requireNewInstance())
/*     */       {
/* 286 */         localGenerator = newInstance();
/*     */       }
/*     */ 
/* 291 */       localGenerator.generateOutputFiles(localCompoundType, this.env, paramFile);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateOutputFiles(CompoundType paramCompoundType, BatchEnvironment paramBatchEnvironment, File paramFile)
/*     */   {
/* 306 */     HashSet localHashSet = paramBatchEnvironment.alreadyChecked;
/*     */ 
/* 310 */     OutputType[] arrayOfOutputType = getOutputTypesFor(paramCompoundType, localHashSet);
/*     */ 
/* 314 */     for (int i = 0; i < arrayOfOutputType.length; i++) {
/* 315 */       OutputType localOutputType = arrayOfOutputType[i];
/* 316 */       String str = localOutputType.getName();
/* 317 */       File localFile = getFileFor(localOutputType, paramFile);
/* 318 */       boolean bool = false;
/*     */ 
/* 322 */       if (requiresGeneration(localFile, localOutputType.getType()))
/*     */       {
/* 326 */         if (localFile.getName().endsWith(".java")) {
/* 327 */           bool = compileJavaSourceFile(localOutputType);
/*     */ 
/* 331 */           if (bool) {
/* 332 */             paramBatchEnvironment.addGeneratedFile(localFile);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 339 */           IndentingWriter localIndentingWriter = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(localFile)), 4, 2147483647);
/*     */ 
/* 342 */           long l1 = 0L;
/* 343 */           if (paramBatchEnvironment.verbose()) {
/* 344 */             l1 = System.currentTimeMillis();
/*     */           }
/*     */ 
/* 347 */           writeOutputFor(arrayOfOutputType[i], localHashSet, localIndentingWriter);
/* 348 */           localIndentingWriter.close();
/*     */ 
/* 350 */           if (paramBatchEnvironment.verbose()) {
/* 351 */             long l2 = System.currentTimeMillis() - l1;
/* 352 */             paramBatchEnvironment.output(Main.getText("rmic.generated", localFile.getPath(), Long.toString(l2)));
/*     */           }
/* 354 */           if (bool)
/* 355 */             paramBatchEnvironment.parseFile(new ClassFile(localFile));
/*     */         }
/*     */         catch (IOException localIOException) {
/* 358 */           paramBatchEnvironment.error(0L, "cant.write", localFile.toString());
/* 359 */           return;
/*     */         }
/*     */ 
/*     */       }
/* 365 */       else if (paramBatchEnvironment.verbose()) {
/* 366 */         paramBatchEnvironment.output(Main.getText("rmic.previously.generated", localFile.getPath()));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected File getFileFor(OutputType paramOutputType, File paramFile)
/*     */   {
/* 386 */     Identifier localIdentifier = getOutputId(paramOutputType);
/* 387 */     File localFile = null;
/* 388 */     if (this.idl)
/* 389 */       localFile = Util.getOutputDirectoryForIDL(localIdentifier, paramFile, this.env);
/*     */     else {
/* 391 */       localFile = Util.getOutputDirectoryForStub(localIdentifier, paramFile, this.env);
/*     */     }
/* 393 */     String str = paramOutputType.getName() + getFileNameExtensionFor(paramOutputType);
/* 394 */     return new File(localFile, str);
/*     */   }
/*     */ 
/*     */   protected Identifier getOutputId(OutputType paramOutputType)
/*     */   {
/* 403 */     return paramOutputType.getType().getIdentifier();
/*     */   }
/*     */ 
/*     */   protected boolean compileJavaSourceFile(OutputType paramOutputType)
/*     */   {
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */   public class OutputType
/*     */   {
/*     */     private String name;
/*     */     private Type type;
/*     */ 
/*     */     public OutputType(String paramType, Type arg3)
/*     */     {
/* 424 */       this.name = paramType;
/*     */       Object localObject;
/* 425 */       this.type = localObject;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 429 */       return this.name;
/*     */     }
/*     */ 
/*     */     public Type getType() {
/* 433 */       return this.type;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.Generator
 * JD-Core Version:    0.6.2
 */