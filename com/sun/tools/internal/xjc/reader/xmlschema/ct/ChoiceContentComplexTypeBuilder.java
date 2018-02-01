/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ParticleBinder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*    */ import com.sun.xml.internal.xsom.XSTerm;
/*    */ import java.util.Collections;
/*    */ 
/*    */ final class ChoiceContentComplexTypeBuilder extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 44 */     if (!this.bgmBuilder.getGlobalBinding().isChoiceContentPropertyEnabled()) {
/* 45 */       return false;
/*    */     }
/* 47 */     if (ct.getBaseType() != this.schemas.getAnyType())
/*    */     {
/* 53 */       return false;
/*    */     }
/* 55 */     XSParticle p = ct.getContentType().asParticle();
/* 56 */     if (p == null) {
/* 57 */       return false;
/*    */     }
/* 59 */     XSModelGroup mg = getTopLevelModelGroup(p);
/*    */ 
/* 61 */     if (mg.getCompositor() != XSModelGroup.CHOICE) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (p.isRepeated()) {
/* 65 */       return false;
/*    */     }
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   private XSModelGroup getTopLevelModelGroup(XSParticle p)
/*    */   {
/* 73 */     XSModelGroup mg = p.getTerm().asModelGroup();
/* 74 */     if (p.getTerm().isModelGroupDecl())
/* 75 */       mg = p.getTerm().asModelGroupDecl().getModelGroup();
/* 76 */     return mg;
/*    */   }
/*    */ 
/*    */   public void build(XSComplexType ct) {
/* 80 */     XSParticle p = ct.getContentType().asParticle();
/*    */ 
/* 82 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */ 
/* 84 */     this.bgmBuilder.getParticleBinder().build(p, Collections.singleton(p));
/*    */ 
/* 86 */     this.green.attContainer(ct);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.ChoiceContentComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */