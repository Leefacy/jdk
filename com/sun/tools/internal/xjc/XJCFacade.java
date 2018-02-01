/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URLClassLoader;
/*     */ 
/*     */ public class XJCFacade
/*     */ {
/*     */   private static final String JDK6_REQUIRED = "XJC requires JDK 6.0 or later. Please download it from http://www.oracle.com/technetwork/java/javase/downloads";
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Throwable
/*     */   {
/*  47 */     String v = "2.0";
/*     */ 
/*  49 */     for (int i = 0; i < args.length; i++) {
/*  50 */       if ((args[i].equals("-source")) && 
/*  51 */         (i + 1 < args.length)) {
/*  52 */         v = parseVersion(args[(i + 1)]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  57 */     ClassLoader oldContextCl = SecureLoader.getContextClassLoader();
/*     */     try {
/*  59 */       ClassLoader cl = ClassLoaderBuilder.createProtectiveClassLoader(SecureLoader.getClassClassLoader(XJCFacade.class), v);
/*  60 */       SecureLoader.setContextClassLoader(cl);
/*  61 */       Class driver = cl.loadClass("com.sun.tools.internal.xjc.Driver");
/*  62 */       Method mainMethod = driver.getDeclaredMethod("main", new Class[] { [Ljava.lang.String.class });
/*     */       try {
/*  64 */         mainMethod.invoke(null, new Object[] { args });
/*     */       } catch (InvocationTargetException e) {
/*  66 */         if (e.getTargetException() != null)
/*  67 */           throw e.getTargetException();
/*     */       }
/*     */     }
/*     */     catch (UnsupportedClassVersionError e)
/*     */     {
/*     */       ClassLoader cl;
/*     */       Class clUtil;
/*     */       Method release;
/*  71 */       System.err.println("XJC requires JDK 6.0 or later. Please download it from http://www.oracle.com/technetwork/java/javase/downloads");
/*     */     }
/*     */     finally
/*     */     {
/*     */       ClassLoader cl;
/*     */       Class clUtil;
/*     */       Method release;
/*  73 */       ClassLoader cl = SecureLoader.getContextClassLoader();
/*  74 */       SecureLoader.setContextClassLoader(oldContextCl);
/*     */ 
/*  77 */       while ((cl != null) && (!oldContextCl.equals(cl))) {
/*  78 */         if ((cl instanceof Closeable))
/*     */         {
/*  80 */           ((Closeable)cl).close();
/*     */         }
/*  82 */         else if ((cl instanceof URLClassLoader))
/*     */         {
/*     */           try
/*     */           {
/*  86 */             Class clUtil = oldContextCl.loadClass("sun.misc.ClassLoaderUtil");
/*  87 */             Method release = clUtil.getDeclaredMethod("releaseLoader", new Class[] { URLClassLoader.class });
/*  88 */             release.invoke(null, new Object[] { cl });
/*     */           }
/*     */           catch (ClassNotFoundException ex) {
/*  91 */             System.err.println("XJC requires JDK 6.0 or later. Please download it from http://www.oracle.com/technetwork/java/javase/downloads");
/*     */           }
/*     */         }
/*     */ 
/*  95 */         cl = SecureLoader.getParentClassLoader(cl);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String parseVersion(String version)
/*     */   {
/* 102 */     return "2.0";
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.XJCFacade
 * JD-Core Version:    0.6.2
 */