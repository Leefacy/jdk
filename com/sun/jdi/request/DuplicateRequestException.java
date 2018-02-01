/*    */ package com.sun.jdi.request;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class DuplicateRequestException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -3719784920313411060L;
/*    */ 
/*    */   public DuplicateRequestException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DuplicateRequestException(String paramString)
/*    */   {
/* 43 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.DuplicateRequestException
 * JD-Core Version:    0.6.2
 */