/*    */ package com.sun.tools.internal.jxc;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class SchemaGeneratorFacade
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Throwable
/*    */   {
/*    */     try
/*    */     {
/* 39 */       ClassLoader cl = SecureLoader.getClassClassLoader(SchemaGeneratorFacade.class);
/* 40 */       if (cl == null) cl = SecureLoader.getSystemClassLoader();
/*    */ 
/* 42 */       Class driver = cl.loadClass("com.sun.tools.internal.jxc.SchemaGenerator");
/* 43 */       Method mainMethod = driver.getDeclaredMethod("main", new Class[] { [Ljava.lang.String.class });
/*    */       try {
/* 45 */         mainMethod.invoke(null, new Object[] { args });
/*    */       } catch (IllegalAccessException e) {
/* 47 */         throw e;
/*    */       } catch (InvocationTargetException e) {
/* 49 */         if (e.getTargetException() != null)
/* 50 */           throw e.getTargetException();
/*    */       }
/*    */     } catch (UnsupportedClassVersionError e) {
/* 53 */       System.err.println("schemagen requires JDK 6.0 or later. Please download it from http://www.oracle.com/technetwork/java/javase/downloads");
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.SchemaGeneratorFacade
 * JD-Core Version:    0.6.2
 */