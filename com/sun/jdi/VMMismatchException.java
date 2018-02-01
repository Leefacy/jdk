/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class VMMismatchException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 289169358790459564L;
/*    */ 
/*    */   public VMMismatchException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public VMMismatchException(String paramString)
/*    */   {
/* 44 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VMMismatchException
 * JD-Core Version:    0.6.2
 */