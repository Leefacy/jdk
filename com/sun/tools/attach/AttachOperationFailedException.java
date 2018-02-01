/*    */ package com.sun.tools.attach;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class AttachOperationFailedException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 2140308168167478043L;
/*    */ 
/*    */   public AttachOperationFailedException(String paramString)
/*    */   {
/* 52 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.AttachOperationFailedException
 * JD-Core Version:    0.6.2
 */