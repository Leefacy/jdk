/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class ClassNotPreparedException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -6120698967144079642L;
/*    */ 
/*    */   public ClassNotPreparedException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ClassNotPreparedException(String paramString)
/*    */   {
/* 45 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ClassNotPreparedException
 * JD-Core Version:    0.6.2
 */