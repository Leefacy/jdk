/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class InvocationException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 6066780907971918568L;
/*    */   ObjectReference exception;
/*    */ 
/*    */   public InvocationException(ObjectReference paramObjectReference)
/*    */   {
/* 41 */     super("Exception occurred in target VM");
/* 42 */     this.exception = paramObjectReference;
/*    */   }
/*    */ 
/*    */   public ObjectReference exception() {
/* 46 */     return this.exception;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InvocationException
 * JD-Core Version:    0.6.2
 */