/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClass;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ final class RestrictedComplexTypeBuilder extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 48 */     XSType baseType = ct.getBaseType();
/*    */ 
/* 51 */     return (baseType != this.schemas.getAnyType()) && 
/* 50 */       (baseType
/* 50 */       .isComplexType()) && 
/* 51 */       (ct
/* 51 */       .getDerivationMethod() == 2);
/*    */   }
/*    */ 
/*    */   public void build(XSComplexType ct)
/*    */   {
/* 56 */     if (this.bgmBuilder.getGlobalBinding().isRestrictionFreshType())
/*    */     {
/* 58 */       new FreshComplexTypeBuilder().build(ct);
/* 59 */       return;
/*    */     }
/*    */ 
/* 62 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/* 65 */     CClass baseClass = this.selector.bindToType(baseType, ct, true);
/* 66 */     assert (baseClass != null);
/*    */ 
/* 68 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/*    */ 
/* 70 */     if (this.bgmBuilder.isGenerateMixedExtensions())
/*    */     {
/* 73 */       boolean forceFallbackInExtension = (baseType.isMixed()) && 
/* 72 */         (ct
/* 72 */         .isMixed()) && 
/* 73 */         (ct
/* 73 */         .getExplicitContent() != null) && (this.bgmBuilder.inExtensionMode);
/*    */ 
/* 75 */       if (forceFallbackInExtension) {
/* 76 */         this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */ 
/* 78 */         BIProperty prop = BIProperty.getCustomization(ct);
/*    */ 
/* 81 */         XSParticle particle = ct.getContentType().asParticle();
/* 82 */         if (particle != null) {
/* 83 */           RawTypeSet ts = RawTypeSetBuilder.build(particle, false);
/* 84 */           CPropertyInfo p = prop.createDummyExtendedMixedReferenceProperty("Content", ct, ts);
/* 85 */           this.selector.getCurrentBean().addProperty(p);
/*    */         }
/*    */       }
/*    */       else {
/* 89 */         this.builder.recordBindingMode(ct, this.builder.getBindingMode(baseType));
/*    */       }
/*    */     } else {
/* 92 */       this.builder.recordBindingMode(ct, this.builder.getBindingMode(baseType));
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.RestrictedComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */