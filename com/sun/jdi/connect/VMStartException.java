/*    */ package com.sun.jdi.connect;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class VMStartException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 6408644824640801020L;
/*    */   Process process;
/*    */ 
/*    */   public VMStartException(Process paramProcess)
/*    */   {
/* 45 */     this.process = paramProcess;
/*    */   }
/*    */ 
/*    */   public VMStartException(String paramString, Process paramProcess)
/*    */   {
/* 50 */     super(paramString);
/* 51 */     this.process = paramProcess;
/*    */   }
/*    */ 
/*    */   public Process process() {
/* 55 */     return this.process;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.VMStartException
 * JD-Core Version:    0.6.2
 */