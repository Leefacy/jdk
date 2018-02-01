/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ final class JArrayCompRef extends JExpressionImpl
/*    */   implements JAssignmentTarget
/*    */ {
/*    */   private final JExpression array;
/*    */   private final JExpression index;
/*    */ 
/*    */   JArrayCompRef(JExpression array, JExpression index)
/*    */   {
/* 54 */     if ((array == null) || (index == null)) {
/* 55 */       throw new NullPointerException();
/*    */     }
/* 57 */     this.array = array;
/* 58 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f) {
/* 62 */     f.g(this.array).p('[').g(this.index).p(']');
/*    */   }
/*    */ 
/*    */   public JExpression assign(JExpression rhs) {
/* 66 */     return JExpr.assign(this, rhs);
/*    */   }
/*    */   public JExpression assignPlus(JExpression rhs) {
/* 69 */     return JExpr.assignPlus(this, rhs);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JArrayCompRef
 * JD-Core Version:    0.6.2
 */