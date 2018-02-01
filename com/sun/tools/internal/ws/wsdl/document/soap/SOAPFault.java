/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class SOAPFault extends ExtensionImpl
/*    */ {
/*    */   private String _name;
/*    */   private String _encodingStyle;
/*    */   private String _namespace;
/* 99 */   private SOAPUse _use = SOAPUse.LITERAL;
/*    */ 
/*    */   public SOAPFault(Locator locator)
/*    */   {
/* 42 */     super(locator);
/* 43 */     this._use = SOAPUse.LITERAL;
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 47 */     return SOAPConstants.QNAME_FAULT;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 51 */     return this._name;
/*    */   }
/*    */ 
/*    */   public void setName(String s) {
/* 55 */     this._name = s;
/*    */   }
/*    */ 
/*    */   public String getNamespace() {
/* 59 */     return this._namespace;
/*    */   }
/*    */ 
/*    */   public void setNamespace(String s) {
/* 63 */     this._namespace = s;
/*    */   }
/*    */ 
/*    */   public SOAPUse getUse() {
/* 67 */     return this._use;
/*    */   }
/*    */ 
/*    */   public void setUse(SOAPUse u) {
/* 71 */     this._use = u;
/*    */   }
/*    */ 
/*    */   public boolean isEncoded() {
/* 75 */     return this._use == SOAPUse.ENCODED;
/*    */   }
/*    */ 
/*    */   public boolean isLiteral() {
/* 79 */     return this._use == SOAPUse.LITERAL;
/*    */   }
/*    */ 
/*    */   public String getEncodingStyle() {
/* 83 */     return this._encodingStyle;
/*    */   }
/*    */ 
/*    */   public void setEncodingStyle(String s) {
/* 87 */     this._encodingStyle = s;
/*    */   }
/*    */ 
/*    */   public void validateThis() {
/* 91 */     if (this._use == SOAPUse.ENCODED)
/* 92 */       throw new ValidationException("validation.unsupportedUse.encoded", new Object[] { Integer.valueOf(getLocator().getLineNumber()), getLocator().getSystemId() });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAPFault
 * JD-Core Version:    0.6.2
 */