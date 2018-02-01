/*     */ package com.sun.tools.internal.ws.processor.util;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorException;
/*     */ import com.sun.tools.internal.ws.util.ClassNameInfo;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class DirectoryUtil
/*     */ {
/*     */   public static File getOutputDirectoryFor(String theClass, File rootDir)
/*     */     throws GeneratorException
/*     */   {
/*  43 */     File outputDir = null;
/*  44 */     String qualifiedClassName = theClass;
/*  45 */     String packagePath = null;
/*  46 */     String packageName = ClassNameInfo.getQualifier(qualifiedClassName);
/*  47 */     if ((packageName != null) && (packageName.length() > 0)) {
/*  48 */       packagePath = packageName.replace('.', File.separatorChar);
/*     */     }
/*     */ 
/*  52 */     if (rootDir != null)
/*     */     {
/*  55 */       if (packagePath != null)
/*     */       {
/*  58 */         outputDir = new File(rootDir, packagePath);
/*     */ 
/*  61 */         ensureDirectory(outputDir);
/*     */       }
/*     */       else
/*     */       {
/*  65 */         outputDir = rootDir;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  70 */       String workingDirPath = System.getProperty("user.dir");
/*  71 */       File workingDir = new File(workingDirPath);
/*     */ 
/*  74 */       if (packagePath == null)
/*     */       {
/*  77 */         outputDir = workingDir;
/*     */       }
/*     */       else
/*     */       {
/*  81 */         outputDir = new File(workingDir, packagePath);
/*     */ 
/*  84 */         ensureDirectory(outputDir);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  89 */     return outputDir;
/*     */   }
/*     */ 
/*     */   public static String getRelativePathfromCommonBase(File file, File base) throws IOException {
/*  93 */     String basePath = base.getCanonicalPath();
/*  94 */     String filePath = file.getCanonicalPath();
/*  95 */     return filePath.substring(basePath.length());
/*     */   }
/*     */ 
/*     */   private static void ensureDirectory(File dir) throws GeneratorException
/*     */   {
/* 100 */     if (!dir.exists()) {
/* 101 */       boolean created = dir.mkdirs();
/* 102 */       if ((!created) || (!dir.exists()))
/*     */       {
/* 104 */         throw new GeneratorException("generator.cannot.create.dir", new Object[] { dir
/* 104 */           .getAbsolutePath() });
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.util.DirectoryUtil
 * JD-Core Version:    0.6.2
 */