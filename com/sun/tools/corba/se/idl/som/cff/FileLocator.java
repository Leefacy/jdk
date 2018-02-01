/*     */ package com.sun.tools.corba.se.idl.som.cff;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public abstract class FileLocator
/*     */ {
/*  62 */   static final Properties pp = System.getProperties();
/*  63 */   static final String classPath = pp.getProperty("java.class.path", ".");
/*  64 */   static final String pathSeparator = pp.getProperty("path.separator", ";");
/*     */ 
/*     */   public static DataInputStream locateClassFile(String paramString)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/*  90 */     int i = 1;
/*     */ 
/*  92 */     String str1 = "";
/*     */ 
/*  94 */     File localFile = null;
/*     */ 
/*  97 */     StringTokenizer localStringTokenizer = new StringTokenizer(classPath, pathSeparator, false);
/*  98 */     String str2 = paramString.replace('.', File.separatorChar) + ".class";
/*     */     int j;
/*     */     String str3;
/* 101 */     while ((localStringTokenizer.hasMoreTokens()) && (i != 0)) {
/*     */       try {
/* 103 */         str1 = localStringTokenizer.nextToken(); } catch (NoSuchElementException localNoSuchElementException) {
/* 104 */         break;
/* 105 */       }j = str1.length();
/* 106 */       str3 = j > 3 ? str1.substring(j - 4) : "";
/* 107 */       if ((str3.equalsIgnoreCase(".zip")) || 
/* 108 */         (str3
/* 108 */         .equalsIgnoreCase(".jar")))
/*     */       {
/*     */         try
/*     */         {
/* 112 */           localNamedDataInputStream = locateInZipFile(str1, paramString, true, true);
/* 113 */           if (localNamedDataInputStream == null)
/*     */             continue;
/* 115 */           return localNamedDataInputStream;
/*     */         }
/*     */         catch (ZipException localZipException) {
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 124 */           localFile = new File(str1 + File.separator + str2); } catch (NullPointerException localNullPointerException) {
/* 125 */         }continue;
/* 126 */         if ((localFile != null) && (localFile.exists())) {
/* 127 */           i = 0;
/*     */         }
/*     */       }
/*     */     }
/* 131 */     if (i != 0)
/*     */     {
/* 137 */       j = paramString.lastIndexOf('.');
/*     */ 
/* 139 */       str3 = j >= 0 ? paramString
/* 139 */         .substring(j + 1) : 
/* 139 */         paramString;
/*     */ 
/* 142 */       localNamedDataInputStream = new NamedDataInputStream(new BufferedInputStream(new FileInputStream(str3 + ".class")), str3 + ".class", false);
/*     */ 
/* 145 */       return localNamedDataInputStream;
/*     */     }
/*     */ 
/* 148 */     NamedDataInputStream localNamedDataInputStream = new NamedDataInputStream(new BufferedInputStream(new FileInputStream(localFile)), str1 + File.separator + str2, false);
/*     */ 
/* 151 */     return localNamedDataInputStream;
/*     */   }
/*     */ 
/*     */   public static DataInputStream locateLocaleSpecificFileInClassPath(String paramString)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 190 */     String str1 = "_" + Locale.getDefault().toString();
/* 191 */     int i = paramString.lastIndexOf('/');
/* 192 */     int j = paramString.lastIndexOf('.');
/*     */ 
/* 194 */     DataInputStream localDataInputStream = null;
/* 195 */     int k = 0;
/*     */     String str2;
/*     */     String str3;
/* 197 */     if ((j > 0) && (j > i)) {
/* 198 */       str2 = paramString.substring(0, j);
/* 199 */       str3 = paramString.substring(j);
/*     */     } else {
/* 201 */       str2 = paramString;
/* 202 */       str3 = "";
/*     */     }
/*     */     while (true)
/*     */     {
/* 206 */       if (k != 0)
/* 207 */         localDataInputStream = locateFileInClassPath(paramString);
/*     */       else try {
/* 209 */           localDataInputStream = locateFileInClassPath(str2 + str1 + str3);
/*     */         } catch (Exception localException) {
/*     */         } if ((localDataInputStream != null) || (k != 0))
/*     */         break;
/* 213 */       int m = str1.lastIndexOf('_');
/* 214 */       if (m > 0)
/* 215 */         str1 = str1.substring(0, m);
/*     */       else
/* 217 */         k = 1;
/*     */     }
/* 219 */     return localDataInputStream;
/*     */   }
/*     */ 
/*     */   public static DataInputStream locateFileInClassPath(String paramString)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 241 */     int i = 1;
/*     */ 
/* 243 */     String str1 = "";
/* 244 */     File localFile = null;
/*     */ 
/* 248 */     String str2 = File.separatorChar == '/' ? paramString : paramString
/* 248 */       .replace(File.separatorChar, '/');
/*     */ 
/* 251 */     String str3 = File.separatorChar == '/' ? paramString : paramString
/* 251 */       .replace('/', File.separatorChar);
/*     */ 
/* 253 */     StringTokenizer localStringTokenizer = new StringTokenizer(classPath, pathSeparator, false);
/*     */     int j;
/*     */     String str4;
/* 255 */     while ((localStringTokenizer.hasMoreTokens()) && (i != 0)) {
/*     */       try {
/* 257 */         str1 = localStringTokenizer.nextToken(); } catch (NoSuchElementException localNoSuchElementException) {
/* 258 */         break;
/* 259 */       }j = str1.length();
/* 260 */       str4 = j > 3 ? str1.substring(j - 4) : "";
/* 261 */       if ((str4.equalsIgnoreCase(".zip")) || 
/* 262 */         (str4
/* 262 */         .equalsIgnoreCase(".jar")))
/*     */       {
/*     */         try
/*     */         {
/* 266 */           localNamedDataInputStream = locateInZipFile(str1, str2, false, false);
/* 267 */           if (localNamedDataInputStream == null)
/*     */             continue;
/* 269 */           return localNamedDataInputStream;
/*     */         }
/*     */         catch (ZipException localZipException) {
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 278 */           localFile = new File(str1 + File.separator + str3); } catch (NullPointerException localNullPointerException) {
/* 279 */         }continue;
/* 280 */         if ((localFile != null) && (localFile.exists())) {
/* 281 */           i = 0;
/*     */         }
/*     */       }
/*     */     }
/* 285 */     if (i != 0)
/*     */     {
/* 291 */       j = str3.lastIndexOf(File.separator);
/*     */ 
/* 293 */       str4 = j >= 0 ? str3
/* 293 */         .substring(j + 1) : 
/* 293 */         str3;
/*     */ 
/* 296 */       localNamedDataInputStream = new NamedDataInputStream(new BufferedInputStream(new FileInputStream(str4)), str4, false);
/*     */ 
/* 298 */       return localNamedDataInputStream;
/*     */     }
/*     */ 
/* 301 */     NamedDataInputStream localNamedDataInputStream = new NamedDataInputStream(new BufferedInputStream(new FileInputStream(localFile)), str1 + File.separator + str3, false);
/*     */ 
/* 304 */     return localNamedDataInputStream;
/*     */   }
/*     */ 
/*     */   public static String getFileNameFromStream(DataInputStream paramDataInputStream)
/*     */   {
/* 316 */     if ((paramDataInputStream instanceof NamedDataInputStream))
/* 317 */       return ((NamedDataInputStream)paramDataInputStream).fullyQualifiedFileName;
/* 318 */     return "";
/*     */   }
/*     */ 
/*     */   public static boolean isZipFileAssociatedWithStream(DataInputStream paramDataInputStream)
/*     */   {
/* 330 */     if ((paramDataInputStream instanceof NamedDataInputStream))
/* 331 */       return ((NamedDataInputStream)paramDataInputStream).inZipFile;
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */   private static NamedDataInputStream locateInZipFile(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws ZipException, IOException
/*     */   {
/* 342 */     ZipFile localZipFile = new ZipFile(paramString1);
/*     */ 
/* 344 */     if (localZipFile == null) {
/* 345 */       return null;
/*     */     }
/* 347 */     String str = paramBoolean1 ? paramString2
/* 347 */       .replace('.', '/') + 
/* 347 */       ".class" : paramString2;
/*     */ 
/* 363 */     ZipEntry localZipEntry = localZipFile.getEntry(str);
/* 364 */     if (localZipEntry == null) {
/* 365 */       localZipFile.close();
/* 366 */       localZipFile = null;
/* 367 */       return null;
/*     */     }
/* 369 */     Object localObject = localZipFile.getInputStream(localZipEntry);
/* 370 */     if (paramBoolean2)
/* 371 */       localObject = new BufferedInputStream((InputStream)localObject);
/* 372 */     return new NamedDataInputStream((InputStream)localObject, paramString1 + '(' + str + ')', true);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.som.cff.FileLocator
 * JD-Core Version:    0.6.2
 */