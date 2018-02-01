/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ @Deprecated
/*    */ public class InvalidLineNumberException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 4048709912372692875L;
/*    */ 
/*    */   public InvalidLineNumberException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidLineNumberException(String paramString)
/*    */   {
/* 46 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InvalidLineNumberException
 * JD-Core Version:    0.6.2
 */