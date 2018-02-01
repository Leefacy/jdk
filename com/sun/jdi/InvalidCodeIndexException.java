/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ @Deprecated
/*    */ public class InvalidCodeIndexException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 7416010225133747805L;
/*    */ 
/*    */   public InvalidCodeIndexException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidCodeIndexException(String paramString)
/*    */   {
/* 46 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InvalidCodeIndexException
 * JD-Core Version:    0.6.2
 */