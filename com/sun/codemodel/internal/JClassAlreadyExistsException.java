/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public class JClassAlreadyExistsException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final JDefinedClass existing;
/*    */ 
/*    */   public JClassAlreadyExistsException(JDefinedClass _existing)
/*    */   {
/* 41 */     this.existing = _existing;
/*    */   }
/*    */ 
/*    */   public JDefinedClass getExistingClass()
/*    */   {
/* 51 */     return this.existing;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JClassAlreadyExistsException
 * JD-Core Version:    0.6.2
 */