/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ class ServiceProxy
/*     */ {
/*     */   private static final String prefix = "META-INF/services/";
/*     */ 
/*     */   private static void fail(Class<?> paramClass, String paramString)
/*     */     throws ServiceProxy.ServiceConfigurationError
/*     */   {
/*  60 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private static void fail(Class<?> paramClass, URL paramURL, int paramInt, String paramString) throws ServiceProxy.ServiceConfigurationError
/*     */   {
/*  65 */     fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private static boolean parse(Class<?> paramClass, URL paramURL)
/*     */     throws ServiceProxy.ServiceConfigurationError
/*     */   {
/*  85 */     InputStream localInputStream = null;
/*  86 */     BufferedReader localBufferedReader = null;
/*     */     try {
/*  88 */       localInputStream = paramURL.openStream();
/*  89 */       localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "utf-8"));
/*  90 */       int i = 1;
/*     */       String str;
/*  92 */       while ((str = localBufferedReader.readLine()) != null) {
/*  93 */         int j = str.indexOf('#');
/*  94 */         if (j >= 0) str = str.substring(0, j);
/*  95 */         str = str.trim();
/*  96 */         int k = str.length();
/*  97 */         if (k != 0) {
/*  98 */           if ((str.indexOf(' ') >= 0) || (str.indexOf('\t') >= 0))
/*  99 */             fail(paramClass, paramURL, i, "Illegal configuration-file syntax");
/* 100 */           int m = str.codePointAt(0);
/* 101 */           if (!Character.isJavaIdentifierStart(m))
/* 102 */             fail(paramClass, paramURL, i, "Illegal provider-class name: " + str);
/* 103 */           for (int n = Character.charCount(m); n < k; n += Character.charCount(m)) {
/* 104 */             m = str.codePointAt(n);
/* 105 */             if ((!Character.isJavaIdentifierPart(m)) && (m != 46))
/* 106 */               fail(paramClass, paramURL, i, "Illegal provider-class name: " + str);
/*     */           }
/* 108 */           return 1;
/*     */         }
/*     */       }
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 112 */       return false;
/*     */     } catch (IOException localIOException3) {
/* 114 */       fail(paramClass, ": " + localIOException3);
/*     */     } finally {
/*     */       try {
/* 117 */         if (localBufferedReader != null) localBufferedReader.close(); 
/*     */       }
/* 119 */       catch (IOException localIOException10) { fail(paramClass, ": " + localIOException10); }
/*     */       try
/*     */       {
/* 122 */         if (localInputStream != null) localInputStream.close(); 
/*     */       }
/* 124 */       catch (IOException localIOException11) { fail(paramClass, ": " + localIOException11); }
/*     */ 
/*     */     }
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean hasService(Class<?> paramClass, URL[] paramArrayOfURL)
/*     */     throws ServiceProxy.ServiceConfigurationError
/*     */   {
/* 136 */     for (URL localURL1 : paramArrayOfURL)
/*     */       try {
/* 138 */         String str = "META-INF/services/" + paramClass.getName();
/* 139 */         URL localURL2 = new URL(localURL1, str);
/* 140 */         boolean bool = parse(paramClass, localURL2);
/* 141 */         if (bool)
/* 142 */           return true;
/*     */       }
/*     */       catch (MalformedURLException localMalformedURLException)
/*     */       {
/*     */       }
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   static class ServiceConfigurationError extends Error
/*     */   {
/*     */     static final long serialVersionUID = 7732091036771098303L;
/*     */ 
/*     */     ServiceConfigurationError(String paramString)
/*     */     {
/*  52 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.processing.ServiceProxy
 * JD-Core Version:    0.6.2
 */