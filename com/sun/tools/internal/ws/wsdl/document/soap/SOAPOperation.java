/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class SOAPOperation extends ExtensionImpl
/*    */ {
/*    */   private String _soapAction;
/*    */   private SOAPStyle _style;
/*    */ 
/*    */   public SOAPOperation(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName()
/*    */   {
/* 46 */     return SOAPConstants.QNAME_OPERATION;
/*    */   }
/*    */ 
/*    */   public String getSOAPAction() {
/* 50 */     return this._soapAction;
/*    */   }
/*    */ 
/*    */   public void setSOAPAction(String s) {
/* 54 */     this._soapAction = s;
/*    */   }
/*    */ 
/*    */   public SOAPStyle getStyle() {
/* 58 */     return this._style;
/*    */   }
/*    */ 
/*    */   public void setStyle(SOAPStyle s) {
/* 62 */     this._style = s;
/*    */   }
/*    */ 
/*    */   public boolean isDocument() {
/* 66 */     return this._style == SOAPStyle.DOCUMENT;
/*    */   }
/*    */ 
/*    */   public boolean isRPC() {
/* 70 */     return this._style == SOAPStyle.RPC;
/*    */   }
/*    */ 
/*    */   public void validateThis()
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAPOperation
 * JD-Core Version:    0.6.2
 */