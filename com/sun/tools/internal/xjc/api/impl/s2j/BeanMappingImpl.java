/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import java.util.List;
/*    */ 
/*    */ final class BeanMappingImpl extends AbstractMappingImpl<CClassInfo>
/*    */ {
/* 43 */   private final TypeAndAnnotationImpl taa = new TypeAndAnnotationImpl(this.parent.outline, (TypeUse)this.clazz);
/*    */ 
/*    */   BeanMappingImpl(JAXBModelImpl parent, CClassInfo classInfo) {
/* 46 */     super(parent, classInfo);
/* 47 */     assert (classInfo.isElement());
/*    */   }
/*    */ 
/*    */   public TypeAndAnnotation getType() {
/* 51 */     return this.taa;
/*    */   }
/*    */ 
/*    */   public final String getTypeClass() {
/* 55 */     return getClazz();
/*    */   }
/*    */ 
/*    */   public List<Property> calcDrilldown() {
/* 59 */     if (!((CClassInfo)this.clazz).isOrdered())
/* 60 */       return null;
/* 61 */     return buildDrilldown((CClassInfo)this.clazz);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.impl.s2j.BeanMappingImpl
 * JD-Core Version:    0.6.2
 */