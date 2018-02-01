/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ final class JCast extends JExpressionImpl
/*    */ {
/*    */   private final JType type;
/*    */   private final JExpression object;
/*    */ 
/*    */   JCast(JType type, JExpression object)
/*    */   {
/* 55 */     this.type = type;
/* 56 */     this.object = object;
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f) {
/* 60 */     f.p("((").g(this.type).p(')').g(this.object).p(')');
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JCast
 * JD-Core Version:    0.6.2
 */