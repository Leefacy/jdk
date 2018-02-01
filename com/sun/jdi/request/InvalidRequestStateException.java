/*    */ package com.sun.jdi.request;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class InvalidRequestStateException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -3774632428543322148L;
/*    */ 
/*    */   public InvalidRequestStateException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidRequestStateException(String paramString)
/*    */   {
/* 48 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.InvalidRequestStateException
 * JD-Core Version:    0.6.2
 */