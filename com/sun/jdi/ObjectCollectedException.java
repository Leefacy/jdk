/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class ObjectCollectedException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1928428056197269588L;
/*    */ 
/*    */   public ObjectCollectedException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ObjectCollectedException(String paramString)
/*    */   {
/* 43 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ObjectCollectedException
 * JD-Core Version:    0.6.2
 */