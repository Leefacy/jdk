/*    */ package com.sun.tools.attach;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class AttachNotSupportedException extends Exception
/*    */ {
/*    */   static final long serialVersionUID = 3391824968260177264L;
/*    */ 
/*    */   public AttachNotSupportedException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public AttachNotSupportedException(String paramString)
/*    */   {
/* 61 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.AttachNotSupportedException
 * JD-Core Version:    0.6.2
 */