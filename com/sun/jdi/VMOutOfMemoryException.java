/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class VMOutOfMemoryException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 71504228548910686L;
/*    */ 
/*    */   public VMOutOfMemoryException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public VMOutOfMemoryException(String paramString)
/*    */   {
/* 43 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VMOutOfMemoryException
 * JD-Core Version:    0.6.2
 */