/*    */ package com.sun.tools.javah;
/*    */ 
/*    */ public class InternalError extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 8411861562497165022L;
/*    */ 
/*    */   InternalError(String paramString, Throwable paramThrowable)
/*    */   {
/* 37 */     super("Internal error: " + paramString);
/* 38 */     initCause(paramThrowable);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.InternalError
 * JD-Core Version:    0.6.2
 */