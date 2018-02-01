/*     */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.QNameAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class SOAPHeader extends ExtensionImpl
/*     */ {
/*     */   private String _encodingStyle;
/*     */   private String _namespace;
/*     */   private String _part;
/*     */   private QName _message;
/* 153 */   private SOAPUse _use = SOAPUse.LITERAL;
/*     */   private List _faults;
/*     */ 
/*     */   public SOAPHeader(Locator locator)
/*     */   {
/*  44 */     super(locator);
/*  45 */     this._faults = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void add(SOAPHeaderFault fault) {
/*  49 */     this._faults.add(fault);
/*     */   }
/*     */ 
/*     */   public Iterator faults() {
/*  53 */     return this._faults.iterator();
/*     */   }
/*     */ 
/*     */   public QName getElementName() {
/*  57 */     return SOAPConstants.QNAME_HEADER;
/*     */   }
/*     */ 
/*     */   public String getNamespace() {
/*  61 */     return this._namespace;
/*     */   }
/*     */ 
/*     */   public void setNamespace(String s) {
/*  65 */     this._namespace = s;
/*     */   }
/*     */ 
/*     */   public SOAPUse getUse() {
/*  69 */     return this._use;
/*     */   }
/*     */ 
/*     */   public void setUse(SOAPUse u) {
/*  73 */     this._use = u;
/*     */   }
/*     */ 
/*     */   public boolean isEncoded() {
/*  77 */     return this._use == SOAPUse.ENCODED;
/*     */   }
/*     */ 
/*     */   public boolean isLiteral() {
/*  81 */     return this._use == SOAPUse.LITERAL;
/*     */   }
/*     */ 
/*     */   public String getEncodingStyle() {
/*  85 */     return this._encodingStyle;
/*     */   }
/*     */ 
/*     */   public void setEncodingStyle(String s) {
/*  89 */     this._encodingStyle = s;
/*     */   }
/*     */ 
/*     */   public String getPart() {
/*  93 */     return this._part;
/*     */   }
/*     */ 
/*     */   public void setMessage(QName message) {
/*  97 */     this._message = message;
/*     */   }
/*     */ 
/*     */   public QName getMessage() {
/* 101 */     return this._message;
/*     */   }
/*     */ 
/*     */   public void setPart(String s) {
/* 105 */     this._part = s;
/*     */   }
/*     */ 
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/* 109 */     super.withAllSubEntitiesDo(action);
/*     */ 
/* 111 */     for (Iterator iter = this._faults.iterator(); iter.hasNext(); )
/* 112 */       action.perform((Entity)iter.next());
/*     */   }
/*     */ 
/*     */   public void withAllQNamesDo(QNameAction action)
/*     */   {
/* 117 */     super.withAllQNamesDo(action);
/*     */ 
/* 119 */     if (this._message != null)
/* 120 */       action.perform(this._message);
/*     */   }
/*     */ 
/*     */   public void accept(ExtensionVisitor visitor) throws Exception
/*     */   {
/* 125 */     visitor.preVisit(this);
/* 126 */     for (Iterator iter = this._faults.iterator(); iter.hasNext(); ) {
/* 127 */       ((SOAPHeaderFault)iter.next()).accept(visitor);
/*     */     }
/* 129 */     visitor.postVisit(this);
/*     */   }
/*     */ 
/*     */   public void validateThis() {
/* 133 */     if (this._message == null) {
/* 134 */       failValidation("validation.missingRequiredAttribute", "message");
/*     */     }
/* 136 */     if (this._part == null) {
/* 137 */       failValidation("validation.missingRequiredAttribute", "part");
/*     */     }
/*     */ 
/* 144 */     if (this._use == SOAPUse.ENCODED)
/* 145 */       throw new ValidationException("validation.unsupportedUse.encoded", new Object[] { Integer.valueOf(getLocator().getLineNumber()), getLocator().getSystemId() });
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeader
 * JD-Core Version:    0.6.2
 */