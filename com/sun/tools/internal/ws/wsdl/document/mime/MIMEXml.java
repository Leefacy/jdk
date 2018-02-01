/*    */ package com.sun.tools.internal.ws.wsdl.document.mime;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class MIMEXml extends ExtensionImpl
/*    */ {
/*    */   private String _part;
/*    */ 
/*    */   public MIMEXml(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 45 */     return MIMEConstants.QNAME_MIME_XML;
/*    */   }
/*    */ 
/*    */   public String getPart() {
/* 49 */     return this._part;
/*    */   }
/*    */ 
/*    */   public void setPart(String s) {
/* 53 */     this._part = s;
/*    */   }
/*    */ 
/*    */   public void validateThis()
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.mime.MIMEXml
 * JD-Core Version:    0.6.2
 */