/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class JAXBProperty
/*    */ {
/*    */   private String name;
/*    */   private JAXBTypeAndAnnotation type;
/*    */   private QName elementName;
/*    */   private QName rawTypeName;
/*    */ 
/*    */   public JAXBProperty()
/*    */   {
/*    */   }
/*    */ 
/*    */   JAXBProperty(Property prop)
/*    */   {
/* 62 */     this.name = prop.name();
/* 63 */     this.type = new JAXBTypeAndAnnotation(prop.type());
/* 64 */     this.elementName = prop.elementName();
/* 65 */     this.rawTypeName = prop.rawName();
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 72 */     return this.name;
/*    */   }
/*    */ 
/*    */   public QName getRawTypeName() {
/* 76 */     return this.rawTypeName;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 80 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public JAXBTypeAndAnnotation getType() {
/* 84 */     return this.type;
/*    */   }
/*    */ 
/*    */   public QName getElementName()
/*    */   {
/* 91 */     return this.elementName;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.JAXBProperty
 * JD-Core Version:    0.6.2
 */