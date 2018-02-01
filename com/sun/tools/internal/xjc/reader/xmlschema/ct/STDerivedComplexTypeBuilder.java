/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.SimpleTypeBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ import java.util.Stack;
/*    */ 
/*    */ final class STDerivedComplexTypeBuilder extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct)
/*    */   {
/* 47 */     return ct.getBaseType().isSimpleType();
/*    */   }
/*    */ 
/*    */   public void build(XSComplexType ct) {
/* 51 */     assert (ct.getDerivationMethod() == 1);
/*    */ 
/* 54 */     XSSimpleType baseType = ct.getBaseType().asSimpleType();
/*    */ 
/* 57 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */ 
/* 59 */     this.simpleTypeBuilder.refererStack.push(ct);
/* 60 */     TypeUse use = this.simpleTypeBuilder.build(baseType);
/* 61 */     this.simpleTypeBuilder.refererStack.pop();
/*    */ 
/* 63 */     BIProperty prop = BIProperty.getCustomization(ct);
/* 64 */     CPropertyInfo p = prop.createValueProperty("Value", false, baseType, use, BGMBuilder.getName(baseType));
/* 65 */     this.selector.getCurrentBean().addProperty(p);
/*    */ 
/* 68 */     this.green.attContainer(ct);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.STDerivedComplexTypeBuilder
 * JD-Core Version:    0.6.2
 */