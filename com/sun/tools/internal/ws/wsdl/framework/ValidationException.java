/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ 
/*    */ public class ValidationException extends JAXWSExceptionBase
/*    */ {
/*    */   public ValidationException(String key, Object[] args)
/*    */   {
/* 38 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public ValidationException(Throwable throwable) {
/* 42 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 46 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ValidationException
 * JD-Core Version:    0.6.2
 */