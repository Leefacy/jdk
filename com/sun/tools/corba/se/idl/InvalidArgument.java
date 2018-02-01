/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ public class InvalidArgument extends Exception
/*    */ {
/* 61 */   private String message = null;
/*    */ 
/*    */   public InvalidArgument(String paramString)
/*    */   {
/* 48 */     this.message = (Util.getMessage("InvalidArgument.1", paramString) + "\n\n" + Util.getMessage("usage"));
/*    */   }
/*    */ 
/*    */   public InvalidArgument()
/*    */   {
/* 53 */     this.message = (Util.getMessage("InvalidArgument.2") + "\n\n" + Util.getMessage("usage"));
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 58 */     return this.message;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.InvalidArgument
 * JD-Core Version:    0.6.2
 */