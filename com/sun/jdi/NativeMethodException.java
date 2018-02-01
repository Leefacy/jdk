/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class NativeMethodException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 3924951669039469992L;
/*    */ 
/*    */   public NativeMethodException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NativeMethodException(String paramString)
/*    */   {
/* 44 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.NativeMethodException
 * JD-Core Version:    0.6.2
 */