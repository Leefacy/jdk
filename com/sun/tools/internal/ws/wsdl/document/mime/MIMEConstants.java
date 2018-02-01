/*    */ package com.sun.tools.internal.ws.wsdl.document.mime;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface MIMEConstants
/*    */ {
/*    */   public static final String NS_WSDL_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";
/* 41 */   public static final QName QNAME_CONTENT = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "content");
/* 42 */   public static final QName QNAME_MULTIPART_RELATED = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "multipartRelated");
/* 43 */   public static final QName QNAME_PART = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "part");
/* 44 */   public static final QName QNAME_MIME_XML = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "mimeXml");
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.mime.MIMEConstants
 * JD-Core Version:    0.6.2
 */