/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ class JAnonymousClass extends JDefinedClass
/*    */ {
/*    */   private final JClass base;
/*    */ 
/*    */   JAnonymousClass(JClass _base)
/*    */   {
/* 42 */     super(_base.owner(), 0, null);
/* 43 */     this.base = _base;
/*    */   }
/*    */ 
/*    */   public String fullName()
/*    */   {
/* 48 */     return this.base.fullName();
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f)
/*    */   {
/* 53 */     f.t(this.base);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAnonymousClass
 * JD-Core Version:    0.6.2
 */