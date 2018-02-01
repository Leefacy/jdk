/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ 
/*    */ final class PublicObjectFactoryGenerator extends ObjectFactoryGeneratorImpl
/*    */ {
/*    */   public PublicObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage)
/*    */   {
/* 40 */     super(outline, model, targetPackage);
/*    */   }
/*    */ 
/*    */   void populate(CElementInfo ei) {
/* 44 */     populate(ei, Aspect.IMPLEMENTATION, Aspect.EXPOSED);
/*    */   }
/*    */ 
/*    */   void populate(ClassOutlineImpl cc) {
/* 48 */     populate(cc, cc.ref);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.PublicObjectFactoryGenerator
 * JD-Core Version:    0.6.2
 */