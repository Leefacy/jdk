/*    */ package com.sun.tools.internal.ws.wsdl.document.http;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class HTTPBinding extends ExtensionImpl
/*    */ {
/*    */   private String _verb;
/*    */ 
/*    */   public HTTPBinding(Locator locator)
/*    */   {
/* 41 */     super(locator);
/*    */   }
/*    */ 
/*    */   public QName getElementName() {
/* 45 */     return HTTPConstants.QNAME_BINDING;
/*    */   }
/*    */ 
/*    */   public String getVerb() {
/* 49 */     return this._verb;
/*    */   }
/*    */ 
/*    */   public void setVerb(String s) {
/* 53 */     this._verb = s;
/*    */   }
/*    */ 
/*    */   public void validateThis() {
/* 57 */     if (this._verb == null)
/* 58 */       failValidation("validation.missingRequiredAttribute", "verb");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.http.HTTPBinding
 * JD-Core Version:    0.6.2
 */