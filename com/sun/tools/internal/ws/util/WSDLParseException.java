/*    */ package com.sun.tools.internal.ws.util;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ 
/*    */ public class WSDLParseException extends JAXWSExceptionBase
/*    */ {
/*    */   public WSDLParseException(String key, Object[] args)
/*    */   {
/* 36 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public WSDLParseException(Throwable throwable) {
/* 40 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 44 */     return "com.sun.tools.internal.ws.resources.util";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.util.WSDLParseException
 * JD-Core Version:    0.6.2
 */