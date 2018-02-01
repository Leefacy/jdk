/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import sun.misc.VMSupport;
/*     */ 
/*     */ public class PerfDataFile
/*     */ {
/* 305 */   public static final String tmpDirName = str;
/*     */   public static final String dirNamePrefix = "hsperfdata_";
/*     */   public static final String userDirNamePattern = "hsperfdata_\\S*";
/*     */   public static final String fileNamePattern = "^[0-9]+$";
/*     */   public static final String tmpFileNamePattern = "^hsperfdata_[0-9]+(_[1-2]+)?$";
/*     */ 
/*     */   public static File getFile(int paramInt)
/*     */   {
/*  99 */     if (paramInt == 0)
/*     */     {
/* 106 */       return null;
/*     */     }
/*     */ 
/* 113 */     File localFile1 = new File(tmpDirName);
/* 114 */     String[] arrayOfString = localFile1.list(new FilenameFilter() {
/*     */       public boolean accept(File paramAnonymousFile, String paramAnonymousString) {
/* 116 */         if (!paramAnonymousString.startsWith("hsperfdata_")) {
/* 117 */           return false;
/*     */         }
/* 119 */         File localFile = new File(paramAnonymousFile, paramAnonymousString);
/*     */ 
/* 121 */         return ((localFile.isDirectory()) || (localFile.isFile())) && 
/* 121 */           (localFile
/* 121 */           .canRead());
/*     */       }
/*     */     });
/* 125 */     long l1 = 0L;
/* 126 */     Object localObject = null;
/*     */ 
/* 128 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 129 */       File localFile2 = new File(tmpDirName + arrayOfString[i]);
/* 130 */       File localFile3 = null;
/*     */ 
/* 132 */       if ((localFile2.exists()) && (localFile2.isDirectory()))
/*     */       {
/* 138 */         String str = Integer.toString(paramInt);
/* 139 */         localFile3 = new File(localFile2.getName(), str);
/*     */       }
/* 141 */       else if ((localFile2.exists()) && (localFile2.isFile()))
/*     */       {
/* 146 */         localFile3 = localFile2;
/*     */       }
/*     */       else
/*     */       {
/* 150 */         localFile3 = localFile2;
/*     */       }
/*     */ 
/* 153 */       if ((localFile3.exists()) && (localFile3.isFile()) && 
/* 154 */         (localFile3
/* 154 */         .canRead())) {
/* 155 */         long l2 = localFile3.lastModified();
/* 156 */         if (l2 >= l1) {
/* 157 */           l1 = l2;
/* 158 */           localObject = localFile3;
/*     */         }
/*     */       }
/*     */     }
/* 162 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static File getFile(String paramString, int paramInt)
/*     */   {
/* 182 */     if (paramInt == 0)
/*     */     {
/* 189 */       return null;
/*     */     }
/*     */ 
/* 193 */     String str = getTempDirectory(paramString) + Integer.toString(paramInt);
/* 194 */     File localFile1 = new File(str);
/*     */ 
/* 196 */     if ((localFile1.exists()) && (localFile1.isFile()) && (localFile1.canRead())) {
/* 197 */       return localFile1;
/*     */     }
/*     */ 
/* 201 */     long l1 = 0L;
/* 202 */     File localFile2 = null;
/* 203 */     for (int i = 0; i < 2; i++) {
/* 204 */       if (i == 0) {
/* 205 */         str = getTempDirectory() + Integer.toString(paramInt);
/*     */       }
/*     */       else {
/* 208 */         str = getTempDirectory() + Integer.toString(paramInt) + 
/* 208 */           Integer.toString(i);
/*     */       }
/*     */ 
/* 211 */       localFile1 = new File(str);
/*     */ 
/* 213 */       if ((localFile1.exists()) && (localFile1.isFile()) && (localFile1.canRead())) {
/* 214 */         long l2 = localFile1.lastModified();
/* 215 */         if (l2 >= l1) {
/* 216 */           l1 = l2;
/* 217 */           localFile2 = localFile1;
/*     */         }
/*     */       }
/*     */     }
/* 221 */     return localFile2;
/*     */   }
/*     */ 
/*     */   public static int getLocalVmId(File paramFile)
/*     */   {
/*     */     try
/*     */     {
/* 238 */       return Integer.parseInt(paramFile.getName());
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException1)
/*     */     {
/* 242 */       String str = paramFile.getName();
/* 243 */       if (str.startsWith("hsperfdata_")) {
/* 244 */         int i = str.indexOf('_');
/* 245 */         int j = str.lastIndexOf('_');
/*     */         try {
/* 247 */           if (i == j) {
/* 248 */             return Integer.parseInt(str.substring(i + 1));
/*     */           }
/* 250 */           return Integer.parseInt(str.substring(i + 1, j)); } catch (NumberFormatException localNumberFormatException2) {
/*     */         }
/*     */       }
/*     */     }
/* 254 */     throw new IllegalArgumentException("file name does not match pattern");
/*     */   }
/*     */ 
/*     */   public static String getTempDirectory()
/*     */   {
/* 269 */     return tmpDirName;
/*     */   }
/*     */ 
/*     */   public static String getTempDirectory(String paramString)
/*     */   {
/* 285 */     return tmpDirName + "hsperfdata_" + paramString + File.separator;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 294 */     String str = VMSupport.getVMTemporaryDirectory();
/*     */ 
/* 302 */     if (str.lastIndexOf(File.separator) != str.length() - 1)
/* 303 */       str = str + File.separator;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.local.PerfDataFile
 * JD-Core Version:    0.6.2
 */