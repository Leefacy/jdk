/*    */ package com.sun.xml.internal.rngom.ast.builder;
/*    */ 
/*    */ public class BuildException extends RuntimeException
/*    */ {
/*    */   private final Throwable cause;
/*    */ 
/*    */   public BuildException(Throwable cause)
/*    */   {
/* 58 */     if (cause == null)
/* 59 */       throw new NullPointerException("null cause");
/* 60 */     this.cause = cause;
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 64 */     return this.cause;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.BuildException
 * JD-Core Version:    0.6.2
 */