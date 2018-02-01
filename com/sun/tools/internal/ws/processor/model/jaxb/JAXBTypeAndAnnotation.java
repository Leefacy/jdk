/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ import com.sun.codemodel.internal.JAnnotatable;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*    */ 
/*    */ public class JAXBTypeAndAnnotation
/*    */ {
/*    */   TypeAndAnnotation typeAnn;
/*    */   JType type;
/*    */ 
/*    */   public JAXBTypeAndAnnotation(TypeAndAnnotation typeAnn)
/*    */   {
/* 41 */     this.typeAnn = typeAnn;
/* 42 */     this.type = typeAnn.getTypeClass();
/*    */   }
/*    */ 
/*    */   public JAXBTypeAndAnnotation(JType type) {
/* 46 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public JAXBTypeAndAnnotation(TypeAndAnnotation typeAnn, JType type) {
/* 50 */     this.typeAnn = typeAnn;
/* 51 */     this.type = type;
/*    */   }
/*    */ 
/*    */   public void annotate(JAnnotatable typeVar) {
/* 55 */     if (this.typeAnn != null)
/* 56 */       this.typeAnn.annotate(typeVar);
/*    */   }
/*    */ 
/*    */   public JType getType() {
/* 60 */     return this.type;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 64 */     return this.type.fullName();
/*    */   }
/*    */ 
/*    */   public TypeAndAnnotation getTypeAnn() {
/* 68 */     return this.typeAnn;
/*    */   }
/*    */ 
/*    */   public void setTypeAnn(TypeAndAnnotation typeAnn) {
/* 72 */     this.typeAnn = typeAnn;
/*    */   }
/*    */ 
/*    */   public void setType(JType type) {
/* 76 */     this.type = type;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation
 * JD-Core Version:    0.6.2
 */