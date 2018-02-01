/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ class JThrow
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression expr;
/*    */ 
/*    */   JThrow(JExpression expr)
/*    */   {
/* 47 */     this.expr = expr;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 51 */     f.p("throw");
/* 52 */     f.g(this.expr);
/* 53 */     f.p(';').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JThrow
 * JD-Core Version:    0.6.2
 */