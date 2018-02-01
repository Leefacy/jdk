/*    */ package com.sun.tools.internal.ws.wsdl.document;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class Import extends Entity
/*    */ {
/*    */   private Documentation _documentation;
/*    */   private String _location;
/*    */   private String _namespace;
/*    */ 
/*    */   public Import(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public String getNamespace() {
/* 45 */     return this._namespace;
/*    */   }
/*    */ 
/*    */   public void setNamespace(String s) {
/* 49 */     this._namespace = s;
/*    */   }
/*    */ 
/*    */   public String getLocation() {
/* 53 */     return this._location;
/*    */   }
/*    */ 
/*    */   public void setLocation(String s) {
/* 57 */     this._location = s;
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 61 */     return WSDLConstants.QNAME_IMPORT;
/*    */   }
/*    */ 
/*    */   public Documentation getDocumentation() {
/* 65 */     return this._documentation;
/*    */   }
/*    */ 
/*    */   public void setDocumentation(Documentation d) {
/* 69 */     this._documentation = d;
/*    */   }
/*    */ 
/*    */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 73 */     visitor.visit(this);
/*    */   }
/*    */ 
/*    */   public void validateThis() {
/* 77 */     if (this._location == null) {
/* 78 */       failValidation("validation.missingRequiredAttribute", "location");
/*    */     }
/* 80 */     if (this._namespace == null)
/* 81 */       failValidation("validation.missingRequiredAttribute", "namespace");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.Import
 * JD-Core Version:    0.6.2
 */