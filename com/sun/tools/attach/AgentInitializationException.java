/*    */ package com.sun.tools.attach;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class AgentInitializationException extends Exception
/*    */ {
/*    */   static final long serialVersionUID = -1508756333332806353L;
/*    */   private int returnValue;
/*    */ 
/*    */   public AgentInitializationException()
/*    */   {
/* 57 */     this.returnValue = 0;
/*    */   }
/*    */ 
/*    */   public AgentInitializationException(String paramString)
/*    */   {
/* 67 */     super(paramString);
/* 68 */     this.returnValue = 0;
/*    */   }
/*    */ 
/*    */   public AgentInitializationException(String paramString, int paramInt)
/*    */   {
/* 80 */     super(paramString);
/* 81 */     this.returnValue = paramInt;
/*    */   }
/*    */ 
/*    */   public int returnValue()
/*    */   {
/* 92 */     return this.returnValue;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.AgentInitializationException
 * JD-Core Version:    0.6.2
 */