/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClass;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import java.util.List;
/*     */ 
/*     */ final class MixedComplexTypeBuilder extends CTBuilder
/*     */ {
/*     */   public boolean isApplicable(XSComplexType ct)
/*     */   {
/*  46 */     XSType bt = ct.getBaseType();
/*  47 */     if ((bt == this.schemas.getAnyType()) && (ct.isMixed())) {
/*  48 */       return true;
/*     */     }
/*     */ 
/*  51 */     if ((bt.isComplexType()) && 
/*  52 */       (!bt
/*  52 */       .asComplexType().isMixed()) && 
/*  53 */       (ct
/*  53 */       .isMixed()) && 
/*  54 */       (ct
/*  54 */       .getDerivationMethod() == 1)) {
/*  55 */       if ((!this.bgmBuilder.isGenerateMixedExtensions()) && (ct.getContentType().asParticle() == null)) {
/*  56 */         return false;
/*     */       }
/*  58 */       return true;
/*     */     }
/*     */ 
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   public void build(XSComplexType ct) {
/*  65 */     XSContentType contentType = ct.getContentType();
/*     */ 
/*  67 */     boolean generateMixedExtensions = this.bgmBuilder.isGenerateMixedExtensions();
/*  68 */     if ((generateMixedExtensions) && (
/*  69 */       (ct.getBaseType() != this.schemas.getAnyType()) || (!ct.isMixed()))) {
/*  70 */       XSComplexType baseType = ct.getBaseType().asComplexType();
/*     */ 
/*  72 */       CClass baseClass = this.selector.bindToType(baseType, ct, true);
/*  73 */       this.selector.getCurrentBean().setBaseClass(baseClass);
/*     */     }
/*     */ 
/*  77 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_CONTENT);
/*  78 */     BIProperty prop = BIProperty.getCustomization(ct);
/*     */     CPropertyInfo p;
/*     */     CPropertyInfo p;
/*  82 */     if (generateMixedExtensions) {
/*  83 */       List cType = ct.getSubtypes();
/*  84 */       boolean isSubtyped = (cType != null) && (cType.size() > 0);
/*     */       CPropertyInfo p;
/*  86 */       if (contentType.asEmpty() != null)
/*     */       {
/*     */         CPropertyInfo p;
/*  87 */         if (isSubtyped)
/*  88 */           p = prop.createContentExtendedMixedReferenceProperty("Content", ct, null);
/*     */         else
/*  90 */           p = prop.createValueProperty("Content", false, ct, CBuiltinLeafInfo.STRING, null);
/*     */       }
/*     */       else
/*     */       {
/*     */         CPropertyInfo p;
/*  92 */         if (contentType.asParticle() == null) {
/*  93 */           p = prop.createContentExtendedMixedReferenceProperty("Content", ct, null);
/*     */         } else {
/*  95 */           RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/*  96 */           p = prop.createContentExtendedMixedReferenceProperty("Content", ct, ts);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       CPropertyInfo p;
/* 100 */       if (contentType.asEmpty() != null) {
/* 101 */         p = prop.createValueProperty("Content", false, ct, CBuiltinLeafInfo.STRING, null);
/*     */       } else {
/* 103 */         RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/* 104 */         p = prop.createReferenceProperty("Content", false, ct, ts, true, false, true, false);
/*     */       }
/*     */     }
/*     */ 
/* 108 */     this.selector.getCurrentBean().addProperty(p);
/*     */ 
/* 111 */     this.green.attContainer(ct);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.MixedComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */