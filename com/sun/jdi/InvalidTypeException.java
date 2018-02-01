/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class InvalidTypeException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 2256667231949650806L;
/*    */ 
/*    */   public InvalidTypeException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InvalidTypeException(String paramString)
/*    */   {
/* 44 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InvalidTypeException
 * JD-Core Version:    0.6.2
 */