/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ class SecureLoader
/*    */ {
/*    */   static ClassLoader getContextClassLoader()
/*    */   {
/* 39 */     if (System.getSecurityManager() == null) {
/* 40 */       return Thread.currentThread().getContextClassLoader();
/*    */     }
/* 42 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 45 */         return Thread.currentThread().getContextClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static ClassLoader getClassClassLoader(Class c)
/*    */   {
/* 52 */     if (System.getSecurityManager() == null) {
/* 53 */       return c.getClassLoader();
/*    */     }
/* 55 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 58 */         return this.val$c.getClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static ClassLoader getSystemClassLoader()
/*    */   {
/* 65 */     if (System.getSecurityManager() == null) {
/* 66 */       return ClassLoader.getSystemClassLoader();
/*    */     }
/* 68 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 71 */         return ClassLoader.getSystemClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static void setContextClassLoader(ClassLoader cl)
/*    */   {
/* 78 */     if (System.getSecurityManager() == null)
/* 79 */       Thread.currentThread().setContextClassLoader(cl);
/*    */     else
/* 81 */       AccessController.doPrivileged(new PrivilegedAction()
/*    */       {
/*    */         public Object run() {
/* 84 */           Thread.currentThread().setContextClassLoader(this.val$cl);
/* 85 */           return null;
/*    */         }
/*    */       });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.fmt.SecureLoader
 * JD-Core Version:    0.6.2
 */