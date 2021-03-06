/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.Mapping;
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class JAXBMapping
/*    */ {
/*    */   private QName elementName;
/*    */   private JAXBTypeAndAnnotation type;
/*    */   private List<JAXBProperty> wrapperStyleDrilldown;
/*    */ 
/*    */   public JAXBMapping()
/*    */   {
/*    */   }
/*    */ 
/*    */   JAXBMapping(Mapping rawModel)
/*    */   {
/* 65 */     this.elementName = rawModel.getElement();
/* 66 */     TypeAndAnnotation typeAndAnno = rawModel.getType();
/* 67 */     this.type = new JAXBTypeAndAnnotation(typeAndAnno);
/* 68 */     List list = rawModel.getWrapperStyleDrilldown();
/* 69 */     if (list == null) {
/* 70 */       this.wrapperStyleDrilldown = null;
/*    */     } else {
/* 72 */       this.wrapperStyleDrilldown = new ArrayList(list.size());
/* 73 */       for (Property p : list)
/* 74 */         this.wrapperStyleDrilldown.add(new JAXBProperty(p));
/*    */     }
/*    */   }
/*    */ 
/*    */   public QName getElementName()
/*    */   {
/* 83 */     return this.elementName;
/*    */   }
/*    */ 
/*    */   public void setElementName(QName elementName) {
/* 87 */     this.elementName = elementName;
/*    */   }
/*    */ 
/*    */   public JAXBTypeAndAnnotation getType()
/*    */   {
/* 92 */     return this.type;
/*    */   }
/*    */ 
/*    */   public List<JAXBProperty> getWrapperStyleDrilldown()
/*    */   {
/* 99 */     return this.wrapperStyleDrilldown;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.JAXBMapping
 * JD-Core Version:    0.6.2
 */