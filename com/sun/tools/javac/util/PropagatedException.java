/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ public class PropagatedException extends RuntimeException
/*    */ {
/*    */   static final long serialVersionUID = -6065309339888775367L;
/*    */ 
/*    */   public PropagatedException(RuntimeException paramRuntimeException)
/*    */   {
/* 43 */     super(paramRuntimeException);
/*    */   }
/*    */ 
/*    */   public RuntimeException getCause()
/*    */   {
/* 48 */     return (RuntimeException)super.getCause();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.PropagatedException
 * JD-Core Version:    0.6.2
 */