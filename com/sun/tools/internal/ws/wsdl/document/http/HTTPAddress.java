/*    */ package com.sun.tools.internal.ws.wsdl.document.http;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class HTTPAddress extends ExtensionImpl
/*    */ {
/*    */   private String _location;
/*    */ 
/*    */   public HTTPAddress(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 45 */     return HTTPConstants.QNAME_ADDRESS;
/*    */   }
/*    */ 
/*    */   public String getLocation() {
/* 49 */     return this._location;
/*    */   }
/*    */ 
/*    */   public void setLocation(String s) {
/* 53 */     this._location = s;
/*    */   }
/*    */ 
/*    */   public void validateThis() {
/* 57 */     if (this._location == null)
/* 58 */       failValidation("validation.missingRequiredAttribute", "location");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.http.HTTPAddress
 * JD-Core Version:    0.6.2
 */