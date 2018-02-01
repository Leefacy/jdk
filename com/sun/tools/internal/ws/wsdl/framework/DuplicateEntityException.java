/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class DuplicateEntityException extends ValidationException
/*    */ {
/*    */   public DuplicateEntityException(GloballyKnown entity)
/*    */   {
/* 36 */     super("entity.duplicateWithType", new Object[] { entity
/* 38 */       .getElementName().getLocalPart(), entity
/* 39 */       .getName() });
/*    */   }
/*    */ 
/*    */   public DuplicateEntityException(Identifiable entity) {
/* 43 */     super("entity.duplicateWithType", new Object[] { entity
/* 45 */       .getElementName().getLocalPart(), entity
/* 46 */       .getID() });
/*    */   }
/*    */ 
/*    */   public DuplicateEntityException(Entity entity, String name) {
/* 50 */     super("entity.duplicateWithType", new Object[] { entity
/* 52 */       .getElementName().getLocalPart(), name });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 56 */     return "com.sun.tools.internal.ws.resources.wsdl";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.DuplicateEntityException
 * JD-Core Version:    0.6.2
 */