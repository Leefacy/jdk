/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class SOAPBody extends ExtensionImpl
/*    */ {
/*    */   private String _encodingStyle;
/*    */   private String _namespace;
/*    */   private String _parts;
/* 98 */   private SOAPUse _use = SOAPUse.LITERAL;
/*    */ 
/*    */   public SOAPBody(Locator locator)
/*    */   {
/* 42 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 46 */     return SOAPConstants.QNAME_BODY;
/*    */   }
/*    */ 
/*    */   public String getNamespace() {
/* 50 */     return this._namespace;
/*    */   }
/*    */ 
/*    */   public void setNamespace(String s) {
/* 54 */     this._namespace = s;
/*    */   }
/*    */ 
/*    */   public SOAPUse getUse() {
/* 58 */     return this._use;
/*    */   }
/*    */ 
/*    */   public void setUse(SOAPUse u) {
/* 62 */     this._use = u;
/*    */   }
/*    */ 
/*    */   public boolean isEncoded() {
/* 66 */     return this._use == SOAPUse.ENCODED;
/*    */   }
/*    */ 
/*    */   public boolean isLiteral() {
/* 70 */     return this._use == SOAPUse.LITERAL;
/*    */   }
/*    */ 
/*    */   public String getEncodingStyle() {
/* 74 */     return this._encodingStyle;
/*    */   }
/*    */ 
/*    */   public void setEncodingStyle(String s) {
/* 78 */     this._encodingStyle = s;
/*    */   }
/*    */ 
/*    */   public String getParts() {
/* 82 */     return this._parts;
/*    */   }
/*    */ 
/*    */   public void setParts(String s) {
/* 86 */     this._parts = s;
/*    */   }
/*    */ 
/*    */   public void validateThis() {
/* 90 */     if (this._use == SOAPUse.ENCODED)
/* 91 */       throw new ValidationException("validation.unsupportedUse.encoded", new Object[] { Integer.valueOf(getLocator().getLineNumber()), getLocator().getSystemId() });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAPBody
 * JD-Core Version:    0.6.2
 */