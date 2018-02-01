/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class InconsistentDebugInfoException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 7964236415376861808L;
/*    */ 
/*    */   public InconsistentDebugInfoException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InconsistentDebugInfoException(String paramString)
/*    */   {
/* 45 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InconsistentDebugInfoException
 * JD-Core Version:    0.6.2
 */