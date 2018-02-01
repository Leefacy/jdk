/*     */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.QNameAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class SOAPHeaderFault extends ExtensionImpl
/*     */ {
/*     */   private String _encodingStyle;
/*     */   private String _namespace;
/*     */   private String _part;
/*     */   private QName _message;
/* 122 */   private SOAPUse _use = SOAPUse.LITERAL;
/*     */ 
/*     */   public SOAPHeaderFault(Locator locator)
/*     */   {
/*  43 */     super(locator);
/*     */   }
/*     */ 
/*     */   public QName getElementName() {
/*  47 */     return SOAPConstants.QNAME_HEADERFAULT;
/*     */   }
/*     */ 
/*     */   public String getNamespace() {
/*  51 */     return this._namespace;
/*     */   }
/*     */ 
/*     */   public void setNamespace(String s) {
/*  55 */     this._namespace = s;
/*     */   }
/*     */ 
/*     */   public SOAPUse getUse() {
/*  59 */     return this._use;
/*     */   }
/*     */ 
/*     */   public void setUse(SOAPUse u) {
/*  63 */     this._use = u;
/*     */   }
/*     */ 
/*     */   public boolean isEncoded() {
/*  67 */     return this._use == SOAPUse.ENCODED;
/*     */   }
/*     */ 
/*     */   public boolean isLiteral() {
/*  71 */     return this._use == SOAPUse.LITERAL;
/*     */   }
/*     */ 
/*     */   public String getEncodingStyle() {
/*  75 */     return this._encodingStyle;
/*     */   }
/*     */ 
/*     */   public void setEncodingStyle(String s) {
/*  79 */     this._encodingStyle = s;
/*     */   }
/*     */ 
/*     */   public String getPart() {
/*  83 */     return this._part;
/*     */   }
/*     */ 
/*     */   public void setMessage(QName message) {
/*  87 */     this._message = message;
/*     */   }
/*     */ 
/*     */   public QName getMessage() {
/*  91 */     return this._message;
/*     */   }
/*     */ 
/*     */   public void setPart(String s) {
/*  95 */     this._part = s;
/*     */   }
/*     */ 
/*     */   public void withAllQNamesDo(QNameAction action) {
/*  99 */     super.withAllQNamesDo(action);
/*     */ 
/* 101 */     if (this._message != null)
/* 102 */       action.perform(this._message);
/*     */   }
/*     */ 
/*     */   public void validateThis()
/*     */   {
/* 107 */     if (this._message == null) {
/* 108 */       failValidation("validation.missingRequiredAttribute", "message");
/*     */     }
/* 110 */     if (this._part == null) {
/* 111 */       failValidation("validation.missingRequiredAttribute", "part");
/*     */     }
/* 113 */     if (this._use == SOAPUse.ENCODED)
/* 114 */       throw new ValidationException("validation.unsupportedUse.encoded", new Object[] { Integer.valueOf(getLocator().getLineNumber()), getLocator().getSystemId() });
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeaderFault
 * JD-Core Version:    0.6.2
 */