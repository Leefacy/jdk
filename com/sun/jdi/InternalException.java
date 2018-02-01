/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class InternalException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -9171606393104480607L;
/*    */   private int errorCode;
/*    */ 
/*    */   public InternalException()
/*    */   {
/* 42 */     this.errorCode = 0;
/*    */   }
/*    */ 
/*    */   public InternalException(String paramString) {
/* 46 */     super(paramString);
/* 47 */     this.errorCode = 0;
/*    */   }
/*    */ 
/*    */   public InternalException(int paramInt)
/*    */   {
/* 52 */     this.errorCode = paramInt;
/*    */   }
/*    */ 
/*    */   public InternalException(String paramString, int paramInt) {
/* 56 */     super(paramString);
/* 57 */     this.errorCode = paramInt;
/*    */   }
/*    */ 
/*    */   public int errorCode() {
/* 61 */     return this.errorCode;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InternalException
 * JD-Core Version:    0.6.2
 */