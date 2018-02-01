/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class NoSuchEntityException extends ValidationException
/*    */ {
/*    */   public NoSuchEntityException(QName name)
/*    */   {
/* 38 */     super("entity.notFoundByQName", new Object[] { name
/* 40 */       .getLocalPart(), name.getNamespaceURI() });
/*    */   }
/*    */ 
/*    */   public NoSuchEntityException(String id) {
/* 44 */     super("entity.notFoundByID", new Object[] { id });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 48 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException
 * JD-Core Version:    0.6.2
 */