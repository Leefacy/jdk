/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class SOAPAddress extends ExtensionImpl
/*    */ {
/*    */   private String _location;
/*    */ 
/*    */   public SOAPAddress(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 45 */     return SOAPConstants.QNAME_ADDRESS;
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
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.soap.SOAPAddress
 * JD-Core Version:    0.6.2
 */