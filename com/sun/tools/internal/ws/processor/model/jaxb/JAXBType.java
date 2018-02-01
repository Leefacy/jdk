/*     */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class JAXBType extends AbstractType
/*     */ {
/*     */   private JAXBMapping jaxbMapping;
/*     */   private JAXBModel jaxbModel;
/* 122 */   private boolean unwrapped = false;
/*     */   private List<JAXBProperty> wrapperChildren;
/*     */ 
/*     */   public JAXBType(JAXBType jaxbType)
/*     */   {
/*  44 */     setName(jaxbType.getName());
/*  45 */     this.jaxbMapping = jaxbType.getJaxbMapping();
/*  46 */     this.jaxbModel = jaxbType.getJaxbModel();
/*  47 */     init();
/*     */   }
/*     */   public JAXBType() {
/*     */   }
/*     */ 
/*     */   public JAXBType(QName name, JavaType type) {
/*  53 */     super(name, type);
/*     */   }
/*     */ 
/*     */   public JAXBType(QName name, JavaType type, JAXBMapping jaxbMapping, JAXBModel jaxbModel) {
/*  57 */     super(name, type);
/*  58 */     this.jaxbMapping = jaxbMapping;
/*  59 */     this.jaxbModel = jaxbModel;
/*  60 */     init();
/*     */   }
/*     */ 
/*     */   public void accept(JAXBTypeVisitor visitor) throws Exception {
/*  64 */     visitor.visit(this);
/*     */   }
/*     */ 
/*     */   private void init() {
/*  68 */     if (this.jaxbMapping != null)
/*  69 */       this.wrapperChildren = this.jaxbMapping.getWrapperStyleDrilldown();
/*     */     else
/*  71 */       this.wrapperChildren = new ArrayList();
/*     */   }
/*     */ 
/*     */   public boolean isUnwrappable() {
/*  75 */     return (this.jaxbMapping != null) && (this.jaxbMapping.getWrapperStyleDrilldown() != null);
/*     */   }
/*     */ 
/*     */   public boolean hasWrapperChildren() {
/*  79 */     return this.wrapperChildren.size() > 0;
/*     */   }
/*     */ 
/*     */   public boolean isLiteralType() {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   public List<JAXBProperty> getWrapperChildren() {
/*  87 */     return this.wrapperChildren;
/*     */   }
/*     */ 
/*     */   public void setWrapperChildren(List<JAXBProperty> children) {
/*  91 */     this.wrapperChildren = children;
/*     */   }
/*     */ 
/*     */   public JAXBMapping getJaxbMapping() {
/*  95 */     return this.jaxbMapping;
/*     */   }
/*     */ 
/*     */   public void setJaxbMapping(JAXBMapping jaxbMapping) {
/*  99 */     this.jaxbMapping = jaxbMapping;
/* 100 */     init();
/*     */   }
/*     */ 
/*     */   public void setUnwrapped(boolean unwrapped) {
/* 104 */     this.unwrapped = unwrapped;
/*     */   }
/*     */ 
/*     */   public boolean isUnwrapped() {
/* 108 */     return this.unwrapped;
/*     */   }
/*     */ 
/*     */   public JAXBModel getJaxbModel()
/*     */   {
/* 114 */     return this.jaxbModel;
/*     */   }
/*     */ 
/*     */   public void setJaxbModel(JAXBModel jaxbModel) {
/* 118 */     this.jaxbModel = jaxbModel;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.JAXBType
 * JD-Core Version:    0.6.2
 */