/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ 
/*    */ public class ParseException extends JAXWSExceptionBase
/*    */ {
/*    */   public ParseException(String key, Object[] args)
/*    */   {
/* 39 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public ParseException(Localizable message) {
/* 43 */     super("localized.error", new Object[] { message });
/*    */   }
/*    */ 
/*    */   public ParseException(Throwable throwable) {
/* 47 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 51 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ParseException
 * JD-Core Version:    0.6.2
 */