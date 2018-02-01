/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*    */ import com.sun.xml.internal.xsom.XSTerm;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ final class MultiWildcardComplexTypeBuilder extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 45 */     if (!this.bgmBuilder.model.options.contentForWildcard) {
/* 46 */       return false;
/*    */     }
/* 48 */     XSType bt = ct.getBaseType();
/* 49 */     if ((bt == this.schemas.getAnyType()) && (ct.getContentType() != null)) {
/* 50 */       XSParticle part = ct.getContentType().asParticle();
/* 51 */       if ((part != null) && (part.getTerm().isModelGroup())) {
/* 52 */         XSParticle[] parts = part.getTerm().asModelGroup().getChildren();
/* 53 */         int wildcardCount = 0;
/* 54 */         int i = 0;
/* 55 */         while ((i < parts.length) && (wildcardCount <= 1)) {
/* 56 */           if (parts[i].getTerm().isWildcard()) {
/* 57 */             wildcardCount++;
/*    */           }
/* 59 */           i++;
/*    */         }
/* 61 */         return wildcardCount > 1;
/*    */       }
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public void build(XSComplexType ct) {
/* 68 */     XSContentType contentType = ct.getContentType();
/*    */ 
/* 70 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_CONTENT);
/* 71 */     BIProperty prop = BIProperty.getCustomization(ct);
/*    */     CPropertyInfo p;
/*    */     CPropertyInfo p;
/* 75 */     if (contentType.asEmpty() != null) {
/* 76 */       p = prop.createValueProperty("Content", false, ct, CBuiltinLeafInfo.STRING, null);
/*    */     } else {
/* 78 */       RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/* 79 */       p = prop.createReferenceProperty("Content", false, ct, ts, true, false, true, false);
/*    */     }
/*    */ 
/* 82 */     this.selector.getCurrentBean().addProperty(p);
/*    */ 
/* 85 */     this.green.attContainer(ct);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.MultiWildcardComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */