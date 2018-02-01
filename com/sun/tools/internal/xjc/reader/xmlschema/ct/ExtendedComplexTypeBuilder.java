/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import com.sun.tools.internal.xjc.model.CClass;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ParticleBinder;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ final class ExtendedComplexTypeBuilder extends AbstractExtendedComplexTypeBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 44 */     XSType baseType = ct.getBaseType();
/*    */ 
/* 47 */     return (baseType != this.schemas.getAnyType()) && 
/* 46 */       (baseType
/* 46 */       .isComplexType()) && 
/* 47 */       (ct
/* 47 */       .getDerivationMethod() == 1);
/*    */   }
/*    */ 
/*    */   public void build(XSComplexType ct) {
/* 51 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/* 54 */     CClass baseClass = this.selector.bindToType(baseType, ct, true);
/* 55 */     assert (baseClass != null);
/*    */ 
/* 57 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/*    */ 
/* 60 */     ComplexTypeBindingMode baseTypeFlag = this.builder.getBindingMode(baseType);
/*    */ 
/* 62 */     XSContentType explicitContent = ct.getExplicitContent();
/*    */ 
/* 64 */     if (!checkIfExtensionSafe(baseType, ct))
/*    */     {
/* 66 */       this.errorReceiver.error(ct.getLocator(), Messages.ERR_NO_FURTHER_EXTENSION
/* 67 */         .format(new Object[] { baseType
/* 68 */         .getName(), ct.getName() }));
/*    */ 
/* 70 */       return;
/*    */     }
/*    */ 
/* 74 */     if ((explicitContent != null) && (explicitContent.asParticle() != null)) {
/* 75 */       if (baseTypeFlag == ComplexTypeBindingMode.NORMAL)
/*    */       {
/* 77 */         this.builder.recordBindingMode(ct, this.bgmBuilder
/* 78 */           .getParticleBinder().checkFallback(explicitContent.asParticle()) ? ComplexTypeBindingMode.FALLBACK_REST : ComplexTypeBindingMode.NORMAL);
/*    */ 
/* 82 */         this.bgmBuilder.getParticleBinder().build(explicitContent.asParticle());
/*    */       }
/*    */       else
/*    */       {
/* 87 */         this.builder.recordBindingMode(ct, baseTypeFlag);
/*    */       }
/*    */     }
/*    */     else {
/* 91 */       this.builder.recordBindingMode(ct, baseTypeFlag);
/*    */     }
/*    */ 
/* 95 */     this.green.attContainer(ct);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.ExtendedComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */