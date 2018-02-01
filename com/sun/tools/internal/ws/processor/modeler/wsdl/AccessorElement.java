/*    */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ class AccessorElement
/*    */ {
/*    */   private QName type;
/*    */   private String name;
/*    */ 
/*    */   public AccessorElement(String name, QName type)
/*    */   {
/* 47 */     this.type = type;
/* 48 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public QName getType()
/*    */   {
/* 54 */     return this.type;
/*    */   }
/*    */ 
/*    */   public void setType(QName type)
/*    */   {
/* 60 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 66 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 72 */     this.name = name;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.wsdl.AccessorElement
 * JD-Core Version:    0.6.2
 */