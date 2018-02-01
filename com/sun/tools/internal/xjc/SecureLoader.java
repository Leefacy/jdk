/*    */ package com.sun.tools.internal.xjc;
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
/*    */       public ClassLoader run() {
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
/*    */       public ClassLoader run() {
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
/*    */       public ClassLoader run() {
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
/*    */         public ClassLoader run() {
/* 82 */           Thread.currentThread().setContextClassLoader(this.val$cl);
/* 83 */           return null;
/*    */         }
/*    */       });
/*    */   }
/*    */ 
/*    */   static ClassLoader getParentClassLoader(ClassLoader cl)
/*    */   {
/* 90 */     if (System.getSecurityManager() == null) {
/* 91 */       return cl.getParent();
/*    */     }
/* 93 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public ClassLoader run() {
/* 96 */         return this.val$cl.getParent();
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.SecureLoader
 * JD-Core Version:    0.6.2
 */