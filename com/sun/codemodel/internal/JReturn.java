/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ class JReturn
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression expr;
/*    */ 
/*    */   JReturn(JExpression expr)
/*    */   {
/* 46 */     this.expr = expr;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 50 */     f.p("return ");
/* 51 */     if (this.expr != null) f.g(this.expr);
/* 52 */     f.p(';').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JReturn
 * JD-Core Version:    0.6.2
 */