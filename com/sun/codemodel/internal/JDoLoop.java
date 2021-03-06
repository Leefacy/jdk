/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public class JDoLoop
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 43 */   private JBlock body = null;
/*    */ 
/*    */   JDoLoop(JExpression test)
/*    */   {
/* 49 */     this.test = test;
/*    */   }
/*    */ 
/*    */   public JBlock body() {
/* 53 */     if (this.body == null) this.body = new JBlock();
/* 54 */     return this.body;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 58 */     f.p("do");
/* 59 */     if (this.body != null)
/* 60 */       f.g(this.body);
/*    */     else {
/* 62 */       f.p("{ }");
/*    */     }
/* 64 */     if (JOp.hasTopOp(this.test))
/* 65 */       f.p("while ").g(this.test);
/*    */     else {
/* 67 */       f.p("while (").g(this.test).p(')');
/*    */     }
/* 69 */     f.p(';').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JDoLoop
 * JD-Core Version:    0.6.2
 */