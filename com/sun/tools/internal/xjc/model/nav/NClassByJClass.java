/*    */ package com.sun.tools.internal.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ 
/*    */ class NClassByJClass
/*    */   implements NClass
/*    */ {
/*    */   final JClass clazz;
/*    */ 
/*    */   NClassByJClass(JClass clazz)
/*    */   {
/* 39 */     this.clazz = clazz;
/*    */   }
/*    */ 
/*    */   public JClass toType(Outline o, Aspect aspect) {
/* 43 */     return this.clazz;
/*    */   }
/*    */ 
/*    */   public boolean isAbstract() {
/* 47 */     return this.clazz.isAbstract();
/*    */   }
/*    */ 
/*    */   public boolean isBoxedType() {
/* 51 */     return this.clazz.getPrimitiveType() != null;
/*    */   }
/*    */ 
/*    */   public String fullName() {
/* 55 */     return this.clazz.fullName();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.nav.NClassByJClass
 * JD-Core Version:    0.6.2
 */