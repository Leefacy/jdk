/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ public class FatalError extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */ 
/*    */   public FatalError(JCDiagnostic paramJCDiagnostic)
/*    */   {
/* 44 */     super(paramJCDiagnostic.toString());
/*    */   }
/*    */ 
/*    */   public FatalError(JCDiagnostic paramJCDiagnostic, Throwable paramThrowable)
/*    */   {
/* 53 */     super(paramJCDiagnostic.toString(), paramThrowable);
/*    */   }
/*    */ 
/*    */   public FatalError(String paramString)
/*    */   {
/* 61 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.FatalError
 * JD-Core Version:    0.6.2
 */