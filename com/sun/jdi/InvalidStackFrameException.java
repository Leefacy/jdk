/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class InvalidStackFrameException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1919378296505827922L;
/*    */ 
/*    */   public InvalidStackFrameException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidStackFrameException(String paramString)
/*    */   {
/* 43 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InvalidStackFrameException
 * JD-Core Version:    0.6.2
 */