/*    */ package com.sun.tools.internal.jxc.ap;
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
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.SecureLoader
 * JD-Core Version:    0.6.2
 */