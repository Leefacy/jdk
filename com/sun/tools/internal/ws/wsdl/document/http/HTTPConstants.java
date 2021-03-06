/*    */ package com.sun.tools.internal.ws.wsdl.document.http;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface HTTPConstants
/*    */ {
/*    */   public static final String NS_WSDL_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";
/* 41 */   public static final QName QNAME_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/http/", "address");
/* 42 */   public static final QName QNAME_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/http/", "binding");
/* 43 */   public static final QName QNAME_OPERATION = new QName("http://schemas.xmlsoap.org/wsdl/http/", "operation");
/* 44 */   public static final QName QNAME_URL_ENCODED = new QName("http://schemas.xmlsoap.org/wsdl/http/", "urlEncoded");
/* 45 */   public static final QName QNAME_URL_REPLACEMENT = new QName("http://schemas.xmlsoap.org/wsdl/http/", "urlReplacement");
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.http.HTTPConstants
 * JD-Core Version:    0.6.2
 */