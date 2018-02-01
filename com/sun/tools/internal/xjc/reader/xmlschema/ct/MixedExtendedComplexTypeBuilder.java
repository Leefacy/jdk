/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import com.sun.tools.internal.xjc.model.CClass;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ final class MixedExtendedComplexTypeBuilder extends AbstractExtendedComplexTypeBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 45 */     if (!this.bgmBuilder.isGenerateMixedExtensions()) return false;
/*    */ 
/* 47 */     XSType bt = ct.getBaseType();
/* 48 */     if ((bt.isComplexType()) && 
/* 49 */       (bt
/* 49 */       .asComplexType().isMixed()) && 
/* 50 */       (ct
/* 50 */       .isMixed()) && 
/* 51 */       (ct
/* 51 */       .getDerivationMethod() == 1) && 
/* 52 */       (ct
/* 52 */       .getContentType().asParticle() != null) && 
/* 53 */       (ct
/* 53 */       .getExplicitContent().asEmpty() == null))
/*    */     {
/* 55 */       return true;
/*    */     }
/*    */ 
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   public void build(XSComplexType ct) {
/* 62 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/* 65 */     CClass baseClass = this.selector.bindToType(baseType, ct, true);
/* 66 */     assert (baseClass != null);
/*    */ 
/* 68 */     if (!checkIfExtensionSafe(baseType, ct))
/*    */     {
/* 70 */       this.errorReceiver.error(ct.getLocator(), Messages.ERR_NO_FURTHER_EXTENSION
/* 71 */         .format(new Object[] { baseType
/* 72 */         .getName(), ct.getName() }));
/*    */ 
/* 74 */       return;
/*    */     }
/*    */ 
/* 77 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/* 78 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_EXTENSION);
/*    */ 
/* 80 */     BIProperty prop = BIProperty.getCustomization(ct);
/*    */ 
/* 83 */     RawTypeSet ts = RawTypeSetBuilder.build(ct.getContentType().asParticle(), false);
/* 84 */     CPropertyInfo p = prop.createDummyExtendedMixedReferenceProperty("contentOverrideFor" + ct.getName(), ct, ts);
/*    */ 
/* 86 */     this.selector.getCurrentBean().addProperty(p);
/*    */ 
/* 89 */     this.green.attContainer(ct);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.MixedExtendedComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */