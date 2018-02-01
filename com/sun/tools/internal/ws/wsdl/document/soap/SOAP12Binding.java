/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class SOAP12Binding extends SOAPBinding
/*    */ {
/*    */   public SOAP12Binding(Locator locator)
/*    */   {
/* 34 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 38 */     return SOAP12Constants.QNAME_BINDING;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Binding
 * JD-Core Version:    0.6.2
 */