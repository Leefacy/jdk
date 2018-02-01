/*     */ package com.sun.tools.extcheck;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class ExtCheck
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private String targetSpecTitle;
/*     */   private String targetSpecVersion;
/*     */   private String targetSpecVendor;
/*     */   private String targetImplTitle;
/*     */   private String targetImplVersion;
/*     */   private String targetImplVendor;
/*     */   private String targetsealed;
/*     */   private boolean verboseFlag;
/*     */ 
/*     */   static ExtCheck create(File paramFile, boolean paramBoolean)
/*     */   {
/*  79 */     return new ExtCheck(paramFile, paramBoolean);
/*     */   }
/*     */ 
/*     */   private ExtCheck(File paramFile, boolean paramBoolean) {
/*  83 */     this.verboseFlag = paramBoolean;
/*  84 */     investigateTarget(paramFile);
/*     */   }
/*     */ 
/*     */   private void investigateTarget(File paramFile)
/*     */   {
/*  89 */     verboseMessage("Target file:" + paramFile);
/*  90 */     Manifest localManifest = null;
/*     */     try {
/*  92 */       File localFile = new File(paramFile.getCanonicalPath());
/*  93 */       URL localURL = ParseUtil.fileToEncodedURL(localFile);
/*  94 */       if (localURL != null) {
/*  95 */         JarLoader localJarLoader = new JarLoader(localURL);
/*  96 */         JarFile localJarFile = localJarLoader.getJarFile();
/*  97 */         localManifest = localJarFile.getManifest();
/*     */       }
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 100 */       error("Malformed URL ");
/*     */     } catch (IOException localIOException) {
/* 102 */       error("IO Exception ");
/*     */     }
/* 104 */     if (localManifest == null)
/* 105 */       error("No manifest available in " + paramFile);
/* 106 */     Attributes localAttributes = localManifest.getMainAttributes();
/* 107 */     if (localAttributes != null) {
/* 108 */       this.targetSpecTitle = localAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 109 */       this.targetSpecVersion = localAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 110 */       this.targetSpecVendor = localAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 111 */       this.targetImplTitle = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/* 112 */       this.targetImplVersion = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 113 */       this.targetImplVendor = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 114 */       this.targetsealed = localAttributes.getValue(Attributes.Name.SEALED);
/*     */     } else {
/* 116 */       error("No attributes available in the manifest");
/*     */     }
/* 118 */     if (this.targetSpecTitle == null)
/* 119 */       error("The target file does not have a specification title");
/* 120 */     if (this.targetSpecVersion == null)
/* 121 */       error("The target file does not have a specification version");
/* 122 */     verboseMessage("Specification title:" + this.targetSpecTitle);
/* 123 */     verboseMessage("Specification version:" + this.targetSpecVersion);
/* 124 */     if (this.targetSpecVendor != null)
/* 125 */       verboseMessage("Specification vendor:" + this.targetSpecVendor);
/* 126 */     if (this.targetImplVersion != null)
/* 127 */       verboseMessage("Implementation version:" + this.targetImplVersion);
/* 128 */     if (this.targetImplVendor != null)
/* 129 */       verboseMessage("Implementation vendor:" + this.targetImplVendor);
/* 130 */     verboseMessage("");
/*     */   }
/*     */ 
/*     */   boolean checkInstalledAgainstTarget()
/*     */   {
/* 143 */     String str = System.getProperty("java.ext.dirs");
/*     */     File[] arrayOfFile;
/* 145 */     if (str != null) {
/* 146 */       StringTokenizer localStringTokenizer = new StringTokenizer(str, File.pathSeparator);
/*     */ 
/* 148 */       i = localStringTokenizer.countTokens();
/* 149 */       arrayOfFile = new File[i];
/* 150 */       for (int j = 0; j < i; j++)
/* 151 */         arrayOfFile[j] = new File(localStringTokenizer.nextToken());
/*     */     }
/*     */     else {
/* 154 */       arrayOfFile = new File[0];
/*     */     }
/*     */ 
/* 157 */     boolean bool = true;
/* 158 */     for (int i = 0; i < arrayOfFile.length; i++) {
/* 159 */       String[] arrayOfString = arrayOfFile[i].list();
/* 160 */       if (arrayOfString != null) {
/* 161 */         for (int k = 0; k < arrayOfString.length; k++) {
/*     */           try {
/* 163 */             File localFile1 = new File(arrayOfFile[i], arrayOfString[k]);
/* 164 */             File localFile2 = new File(localFile1.getCanonicalPath());
/* 165 */             URL localURL = ParseUtil.fileToEncodedURL(localFile2);
/* 166 */             if (localURL != null)
/* 167 */               bool = (bool) && (checkURLRecursively(1, localURL));
/*     */           }
/*     */           catch (MalformedURLException localMalformedURLException) {
/* 170 */             error("Malformed URL");
/*     */           } catch (IOException localIOException) {
/* 172 */             error("IO Exception");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 177 */     if (bool)
/* 178 */       generalMessage("No conflicting installed jar found.");
/*     */     else {
/* 180 */       generalMessage("Conflicting installed jar found.  Use -verbose for more information.");
/*     */     }
/*     */ 
/* 183 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean checkURLRecursively(int paramInt, URL paramURL)
/*     */     throws IOException
/*     */   {
/* 197 */     verboseMessage("Comparing with " + paramURL);
/* 198 */     JarLoader localJarLoader = new JarLoader(paramURL);
/* 199 */     JarFile localJarFile = localJarLoader.getJarFile();
/* 200 */     Manifest localManifest = localJarFile.getManifest();
/* 201 */     if (localManifest != null) {
/* 202 */       Attributes localAttributes = localManifest.getMainAttributes();
/* 203 */       if (localAttributes != null) {
/* 204 */         localObject = localAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 205 */         String str1 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 206 */         String str2 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 207 */         String str3 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/*     */ 
/* 209 */         String str4 = localAttributes
/* 209 */           .getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/*     */ 
/* 210 */         String str5 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 211 */         String str6 = localAttributes.getValue(Attributes.Name.SEALED);
/* 212 */         if ((localObject != null) && 
/* 213 */           (((String)localObject).equals(this.targetSpecTitle)) && 
/* 214 */           (str1 != null) && (
/* 215 */           (str1.equals(this.targetSpecVersion)) || 
/* 216 */           (isNotOlderThan(str1, this.targetSpecVersion))))
/*     */         {
/* 217 */           verboseMessage("");
/* 218 */           verboseMessage("CONFLICT DETECTED ");
/* 219 */           verboseMessage("Conflicting file:" + paramURL);
/* 220 */           verboseMessage("Installed Version:" + str1);
/*     */ 
/* 222 */           if (str3 != null) {
/* 223 */             verboseMessage("Implementation Title:" + str3);
/*     */           }
/* 225 */           if (str4 != null) {
/* 226 */             verboseMessage("Implementation Version:" + str4);
/*     */           }
/* 228 */           if (str5 != null) {
/* 229 */             verboseMessage("Implementation Vendor:" + str5);
/*     */           }
/* 231 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 238 */     boolean bool1 = true;
/* 239 */     Object localObject = localJarLoader.getClassPath();
/* 240 */     if (localObject != null) {
/* 241 */       for (int i = 0; i < localObject.length; i++) {
/* 242 */         if (paramURL != null) {
/* 243 */           boolean bool2 = checkURLRecursively(paramInt + 1, localObject[i]);
/* 244 */           bool1 = (bool2) && (bool1);
/*     */         }
/*     */       }
/*     */     }
/* 248 */     return bool1;
/*     */   }
/*     */ 
/*     */   private boolean isNotOlderThan(String paramString1, String paramString2)
/*     */     throws NumberFormatException
/*     */   {
/* 259 */     if ((paramString1 == null) || (paramString1.length() < 1)) {
/* 260 */       throw new NumberFormatException("Empty version string");
/*     */     }
/*     */ 
/* 264 */     StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString2, ".", true);
/* 265 */     StringTokenizer localStringTokenizer2 = new StringTokenizer(paramString1, ".", true);
/* 266 */     while ((localStringTokenizer1.hasMoreTokens()) || (localStringTokenizer2.hasMoreTokens()))
/*     */     {
/*     */       int i;
/* 269 */       if (localStringTokenizer1.hasMoreTokens())
/* 270 */         i = Integer.parseInt(localStringTokenizer1.nextToken());
/*     */       else
/* 272 */         i = 0;
/*     */       int j;
/* 274 */       if (localStringTokenizer2.hasMoreTokens())
/* 275 */         j = Integer.parseInt(localStringTokenizer2.nextToken());
/*     */       else {
/* 277 */         j = 0;
/*     */       }
/* 279 */       if (j < i)
/* 280 */         return false;
/* 281 */       if (j > i) {
/* 282 */         return true;
/*     */       }
/*     */ 
/* 285 */       if (localStringTokenizer1.hasMoreTokens())
/* 286 */         localStringTokenizer1.nextToken();
/* 287 */       if (localStringTokenizer2.hasMoreTokens()) {
/* 288 */         localStringTokenizer2.nextToken();
/*     */       }
/*     */     }
/*     */ 
/* 292 */     return true;
/*     */   }
/*     */ 
/*     */   void verboseMessage(String paramString)
/*     */   {
/* 300 */     if (this.verboseFlag)
/* 301 */       System.err.println(paramString);
/*     */   }
/*     */ 
/*     */   void generalMessage(String paramString)
/*     */   {
/* 306 */     System.err.println(paramString);
/*     */   }
/*     */ 
/*     */   static void error(String paramString)
/*     */     throws RuntimeException
/*     */   {
/* 313 */     throw new RuntimeException(paramString);
/*     */   }
/*     */ 
/*     */   private static class JarLoader
/*     */   {
/*     */     private final URL base;
/*     */     private JarFile jar;
/*     */     private URL csu;
/*     */ 
/*     */     JarLoader(URL paramURL)
/*     */     {
/* 331 */       String str = paramURL + "!/";
/* 332 */       URL localURL = null;
/*     */       try {
/* 334 */         localURL = new URL("jar", "", str);
/* 335 */         this.jar = findJarFile(paramURL);
/* 336 */         this.csu = paramURL;
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 338 */         ExtCheck.error("Malformed url " + str);
/*     */       } catch (IOException localIOException) {
/* 340 */         ExtCheck.error("IO Exception occurred");
/*     */       }
/* 342 */       this.base = localURL;
/*     */     }
/*     */ 
/*     */     URL getBaseURL()
/*     */     {
/* 350 */       return this.base;
/*     */     }
/*     */ 
/*     */     JarFile getJarFile() {
/* 354 */       return this.jar;
/*     */     }
/*     */ 
/*     */     private JarFile findJarFile(URL paramURL) throws IOException
/*     */     {
/* 359 */       if ("file".equals(paramURL.getProtocol())) {
/* 360 */         localObject = paramURL.getFile().replace('/', File.separatorChar);
/* 361 */         File localFile = new File((String)localObject);
/* 362 */         if (!localFile.exists()) {
/* 363 */           throw new FileNotFoundException((String)localObject);
/*     */         }
/* 365 */         return new JarFile((String)localObject);
/*     */       }
/* 367 */       Object localObject = getBaseURL().openConnection();
/*     */ 
/* 369 */       return ((JarURLConnection)localObject).getJarFile();
/*     */     }
/*     */ 
/*     */     URL[] getClassPath()
/*     */       throws IOException
/*     */     {
/* 377 */       Manifest localManifest = this.jar.getManifest();
/* 378 */       if (localManifest != null) {
/* 379 */         Attributes localAttributes = localManifest.getMainAttributes();
/* 380 */         if (localAttributes != null) {
/* 381 */           String str = localAttributes.getValue(Attributes.Name.CLASS_PATH);
/* 382 */           if (str != null) {
/* 383 */             return parseClassPath(this.csu, str);
/*     */           }
/*     */         }
/*     */       }
/* 387 */       return null;
/*     */     }
/*     */ 
/*     */     private URL[] parseClassPath(URL paramURL, String paramString)
/*     */       throws MalformedURLException
/*     */     {
/* 397 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 398 */       URL[] arrayOfURL = new URL[localStringTokenizer.countTokens()];
/* 399 */       int i = 0;
/* 400 */       while (localStringTokenizer.hasMoreTokens()) {
/* 401 */         String str = localStringTokenizer.nextToken();
/* 402 */         arrayOfURL[i] = new URL(paramURL, str);
/* 403 */         i++;
/*     */       }
/* 405 */       return arrayOfURL;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.extcheck.ExtCheck
 * JD-Core Version:    0.6.2
 */