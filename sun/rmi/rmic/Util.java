/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.File;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public class Util
/*     */   implements Constants
/*     */ {
/*     */   public static File getOutputDirectoryFor(Identifier paramIdentifier, File paramFile, BatchEnvironment paramBatchEnvironment)
/*     */   {
/*  62 */     Object localObject = null;
/*  63 */     String str1 = paramIdentifier.getFlatName().toString().replace('.', '$');
/*  64 */     String str2 = str1;
/*  65 */     String str3 = null;
/*  66 */     String str4 = paramIdentifier.getQualifier().toString();
/*     */ 
/*  68 */     if (str4.length() > 0) {
/*  69 */       str2 = str4 + "." + str1;
/*  70 */       str3 = str4.replace('.', File.separatorChar);
/*     */     }
/*     */ 
/*  75 */     if (paramFile != null)
/*     */     {
/*  79 */       if (str3 != null)
/*     */       {
/*  83 */         localObject = new File(paramFile, str3);
/*     */ 
/*  87 */         ensureDirectory((File)localObject, paramBatchEnvironment);
/*     */       }
/*     */       else
/*     */       {
/*  93 */         localObject = paramFile;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  99 */       String str5 = System.getProperty("user.dir");
/* 100 */       File localFile = new File(str5);
/*     */ 
/* 104 */       if (str3 == null)
/*     */       {
/* 108 */         localObject = localFile;
/*     */       }
/*     */       else
/*     */       {
/* 114 */         localObject = new File(localFile, str3);
/*     */ 
/* 118 */         ensureDirectory((File)localObject, paramBatchEnvironment);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 124 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static void ensureDirectory(File paramFile, BatchEnvironment paramBatchEnvironment) {
/* 128 */     if (!paramFile.exists()) {
/* 129 */       paramFile.mkdirs();
/* 130 */       if (!paramFile.exists()) {
/* 131 */         paramBatchEnvironment.error(0L, "rmic.cannot.create.dir", paramFile.getAbsolutePath());
/* 132 */         throw new InternalError();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.Util
 * JD-Core Version:    0.6.2
 */