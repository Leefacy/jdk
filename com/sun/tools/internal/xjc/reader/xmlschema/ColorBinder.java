/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSAnnotation;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import com.sun.xml.internal.xsom.XSFacet;
/*    */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*    */ import com.sun.xml.internal.xsom.XSNotation;
/*    */ import com.sun.xml.internal.xsom.XSSchema;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSXPath;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ 
/*    */ abstract class ColorBinder extends BindingComponent
/*    */   implements XSVisitor
/*    */ {
/* 46 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/* 47 */   protected final ClassSelector selector = getClassSelector();
/*    */ 
/*    */   protected final CClassInfo getCurrentBean() {
/* 50 */     return this.selector.getCurrentBean();
/*    */   }
/*    */   protected final XSComponent getCurrentRoot() {
/* 53 */     return this.selector.getCurrentRoot();
/*    */   }
/*    */ 
/*    */   protected final void createSimpleTypeProperty(XSSimpleType type, String propName)
/*    */   {
/* 58 */     BIProperty prop = BIProperty.getCustomization(type);
/*    */ 
/* 60 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*    */ 
/* 62 */     CPropertyInfo p = prop.createValueProperty(propName, false, type, stb.buildDef(type), BGMBuilder.getName(type));
/* 63 */     getCurrentBean().addProperty(p);
/*    */   }
/*    */ 
/*    */   public final void annotation(XSAnnotation xsAnnotation)
/*    */   {
/* 71 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public final void schema(XSSchema xsSchema) {
/* 75 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public final void facet(XSFacet xsFacet) {
/* 79 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public final void notation(XSNotation xsNotation) {
/* 83 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public final void identityConstraint(XSIdentityConstraint xsIdentityConstraint) {
/* 87 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public final void xpath(XSXPath xsxPath) {
/* 91 */     throw new IllegalStateException();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ColorBinder
 * JD-Core Version:    0.6.2
 */