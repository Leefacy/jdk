/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ public class InternalError extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 8114054446416187030L;
/*    */   public final Object[] args;
/*    */ 
/*    */   InternalError(Throwable paramThrowable, Object[] paramArrayOfObject)
/*    */   {
/* 37 */     super("Internal error", paramThrowable);
/* 38 */     this.args = paramArrayOfObject;
/*    */   }
/*    */ 
/*    */   InternalError(Object[] paramArrayOfObject) {
/* 42 */     super("Internal error");
/* 43 */     this.args = paramArrayOfObject;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.InternalError
 * JD-Core Version:    0.6.2
 */