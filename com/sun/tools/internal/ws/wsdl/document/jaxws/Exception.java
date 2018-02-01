/*    */ package com.sun.tools.internal.ws.wsdl.document.jaxws;
/*    */ 
/*    */ public class Exception
/*    */ {
/*    */   private CustomName className;
/*    */ 
/*    */   public Exception()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Exception(CustomName name)
/*    */   {
/* 39 */     this.className = name;
/*    */   }
/*    */ 
/*    */   public CustomName getClassName()
/*    */   {
/* 47 */     return this.className;
/*    */   }
/*    */ 
/*    */   public void setClassName(CustomName className)
/*    */   {
/* 53 */     this.className = className;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.jaxws.Exception
 * JD-Core Version:    0.6.2
 */