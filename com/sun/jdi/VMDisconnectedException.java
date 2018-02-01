/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class VMDisconnectedException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 2892975269768351637L;
/*    */ 
/*    */   public VMDisconnectedException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public VMDisconnectedException(String paramString)
/*    */   {
/* 44 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VMDisconnectedException
 * JD-Core Version:    0.6.2
 */