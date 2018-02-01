/*    */ package com.sun.tools.internal.ws.wsdl.document.http;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class HTTPUrlReplacement extends ExtensionImpl
/*    */ {
/*    */   public HTTPUrlReplacement(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 45 */     return HTTPConstants.QNAME_URL_REPLACEMENT;
/*    */   }
/*    */ 
/*    */   public void validateThis()
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.http.HTTPUrlReplacement
 * JD-Core Version:    0.6.2
 */