/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ParticleBinder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.SimpleTypeBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSTerm;
/*    */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
/*    */ import java.util.Stack;
/*    */ 
/*    */ final class FreshComplexTypeBuilder extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 52 */     return (ct.getBaseType() == this.schemas.getAnyType()) && 
/* 52 */       (!ct
/* 52 */       .isMixed());
/*    */   }
/*    */ 
/*    */   public void build(final XSComplexType ct) {
/* 56 */     XSContentType contentType = ct.getContentType();
/*    */ 
/* 58 */     contentType.visit(new XSContentTypeVisitor() {
/*    */       public void simpleType(XSSimpleType st) {
/* 60 */         FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */ 
/* 62 */         FreshComplexTypeBuilder.this.simpleTypeBuilder.refererStack.push(ct);
/* 63 */         TypeUse use = FreshComplexTypeBuilder.this.simpleTypeBuilder.build(st);
/* 64 */         FreshComplexTypeBuilder.this.simpleTypeBuilder.refererStack.pop();
/*    */ 
/* 66 */         BIProperty prop = BIProperty.getCustomization(ct);
/* 67 */         CPropertyInfo p = prop.createValueProperty("Value", false, ct, use, BGMBuilder.getName(st));
/* 68 */         FreshComplexTypeBuilder.this.selector.getCurrentBean().addProperty(p);
/*    */       }
/*    */ 
/*    */       public void particle(XSParticle p)
/*    */       {
/* 74 */         FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, FreshComplexTypeBuilder.this.bgmBuilder
/* 75 */           .getParticleBinder().checkFallback(p) ? ComplexTypeBindingMode.FALLBACK_CONTENT : ComplexTypeBindingMode.NORMAL);
/*    */ 
/* 77 */         FreshComplexTypeBuilder.this.bgmBuilder.getParticleBinder().build(p);
/*    */ 
/* 79 */         XSTerm term = p.getTerm();
/* 80 */         if ((term.isModelGroup()) && (term.asModelGroup().getCompositor() == XSModelGroup.ALL))
/* 81 */           FreshComplexTypeBuilder.this.selector.getCurrentBean().setOrdered(false);
/*    */       }
/*    */ 
/*    */       public void empty(XSContentType e)
/*    */       {
/* 86 */         FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */       }
/*    */     });
/* 91 */     this.green.attContainer(ct);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.FreshComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */