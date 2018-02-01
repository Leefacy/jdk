/*    */ package com.sun.tools.attach;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class AgentLoadException extends Exception
/*    */ {
/*    */   static final long serialVersionUID = 688047862952114238L;
/*    */ 
/*    */   public AgentLoadException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public AgentLoadException(String paramString)
/*    */   {
/* 60 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.AgentLoadException
 * JD-Core Version:    0.6.2
 */