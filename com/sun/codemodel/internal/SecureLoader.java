/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ class SecureLoader
/*    */ {
/*    */   static ClassLoader getContextClassLoader()
/*    */   {
/* 37 */     if (System.getSecurityManager() == null) {
/* 38 */       return Thread.currentThread().getContextClassLoader();
/*    */     }
/* 40 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 43 */         return Thread.currentThread().getContextClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static ClassLoader getClassClassLoader(Class c)
/*    */   {
/* 50 */     if (System.getSecurityManager() == null) {
/* 51 */       return c.getClassLoader();
/*    */     }
/* 53 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 56 */         return this.val$c.getClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static ClassLoader getSystemClassLoader()
/*    */   {
/* 63 */     if (System.getSecurityManager() == null) {
/* 64 */       return ClassLoader.getSystemClassLoader();
/*    */     }
/* 66 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 69 */         return ClassLoader.getSystemClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static void setContextClassLoader(ClassLoader cl)
/*    */   {
/* 76 */     if (System.getSecurityManager() == null)
/* 77 */       Thread.currentThread().setContextClassLoader(cl);
/*    */     else
/* 79 */       AccessController.doPrivileged(new PrivilegedAction()
/*    */       {
/*    */         public Object run() {
/* 82 */           Thread.currentThread().setContextClassLoader(this.val$cl);
/* 83 */           return null;
/*    */         }
/*    */       });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.SecureLoader
 * JD-Core Version:    0.6.2
 */