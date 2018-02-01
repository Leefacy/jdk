/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class PlatformClasses
/*     */ {
/*  55 */   static String[] names = null;
/*     */ 
/*     */   public static synchronized String[] getNames()
/*     */   {
/*  59 */     if (names == null) {
/*  60 */       LinkedList localLinkedList = new LinkedList();
/*     */ 
/*  63 */       InputStream localInputStream = PlatformClasses.class
/*  63 */         .getResourceAsStream("/com/sun/tools/hat/resources/platform_names.txt");
/*     */ 
/*  64 */       if (localInputStream != null) {
/*     */         try {
/*  66 */           BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
/*     */           while (true)
/*     */           {
/*  69 */             String str = localBufferedReader.readLine();
/*  70 */             if (str == null)
/*     */               break;
/*  72 */             if (str.length() > 0) {
/*  73 */               localLinkedList.add(str);
/*     */             }
/*     */           }
/*  76 */           localBufferedReader.close();
/*  77 */           localInputStream.close();
/*     */         } catch (IOException localIOException) {
/*  79 */           localIOException.printStackTrace();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  84 */       names = (String[])localLinkedList.toArray(new String[localLinkedList.size()]);
/*     */     }
/*  86 */     return names;
/*     */   }
/*     */ 
/*     */   public static boolean isPlatformClass(JavaClass paramJavaClass)
/*     */   {
/*  94 */     if (paramJavaClass.isBootstrap()) {
/*  95 */       return true;
/*     */     }
/*     */ 
/*  98 */     String str = paramJavaClass.getName();
/*     */ 
/* 100 */     if (str.startsWith("[")) {
/* 101 */       int i = str.lastIndexOf('[');
/* 102 */       if (i != -1) {
/* 103 */         if (str.charAt(i + 1) != 'L')
/*     */         {
/* 105 */           return true;
/*     */         }
/*     */ 
/* 108 */         str = str.substring(i + 2);
/*     */       }
/*     */     }
/* 111 */     String[] arrayOfString = getNames();
/* 112 */     for (int j = 0; j < arrayOfString.length; j++) {
/* 113 */       if (str.startsWith(arrayOfString[j])) {
/* 114 */         return true;
/*     */       }
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.PlatformClasses
 * JD-Core Version:    0.6.2
 */