/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*    */ import com.sun.tools.internal.xjc.model.CAdapter;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*    */ import java.util.List;
/*    */ 
/*    */ final class ElementMappingImpl extends AbstractMappingImpl<CElementInfo>
/*    */ {
/*    */   private final TypeAndAnnotation taa;
/*    */ 
/*    */   protected ElementMappingImpl(JAXBModelImpl parent, CElementInfo elementInfo)
/*    */   {
/* 48 */     super(parent, elementInfo);
/*    */ 
/* 50 */     TypeUse t = ((CElementInfo)this.clazz).getContentType();
/* 51 */     if (((CElementInfo)this.clazz).getProperty().isCollection())
/* 52 */       t = TypeUseFactory.makeCollection(t);
/* 53 */     CAdapter a = ((CElementInfo)this.clazz).getProperty().getAdapter();
/* 54 */     if (a != null)
/* 55 */       t = TypeUseFactory.adapt(t, a);
/* 56 */     this.taa = new TypeAndAnnotationImpl(parent.outline, t);
/*    */   }
/*    */ 
/*    */   public TypeAndAnnotation getType() {
/* 60 */     return this.taa;
/*    */   }
/*    */ 
/*    */   public final List<Property> calcDrilldown() {
/* 64 */     CElementPropertyInfo p = ((CElementInfo)this.clazz).getProperty();
/*    */ 
/* 66 */     if (p.getAdapter() != null) {
/* 67 */       return null;
/*    */     }
/* 69 */     if (p.isCollection())
/*    */     {
/* 71 */       return null;
/*    */     }
/* 73 */     CTypeInfo typeClass = (CTypeInfo)p.ref().get(0);
/*    */ 
/* 75 */     if (!(typeClass instanceof CClassInfo))
/*    */     {
/* 77 */       return null;
/*    */     }
/* 79 */     CClassInfo ci = (CClassInfo)typeClass;
/*    */ 
/* 82 */     if (ci.isAbstract()) {
/* 83 */       return null;
/*    */     }
/*    */ 
/* 86 */     if (!ci.isOrdered()) {
/* 87 */       return null;
/*    */     }
/* 89 */     return buildDrilldown(ci);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.impl.s2j.ElementMappingImpl
 * JD-Core Version:    0.6.2
 */