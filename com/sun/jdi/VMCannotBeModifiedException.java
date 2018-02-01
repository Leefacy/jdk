/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class VMCannotBeModifiedException extends UnsupportedOperationException
/*    */ {
/*    */   private static final long serialVersionUID = -4063879815130164009L;
/*    */ 
/*    */   public VMCannotBeModifiedException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public VMCannotBeModifiedException(String paramString)
/*    */   {
/* 43 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VMCannotBeModifiedException
 * JD-Core Version:    0.6.2
 */