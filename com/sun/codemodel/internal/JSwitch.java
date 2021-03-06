/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class JSwitch
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 45 */   private List<JCase> cases = new ArrayList();
/*    */ 
/* 50 */   private JCase defaultCase = null;
/*    */ 
/*    */   JSwitch(JExpression test)
/*    */   {
/* 56 */     this.test = test;
/*    */   }
/*    */   public JExpression test() {
/* 59 */     return this.test;
/*    */   }
/* 61 */   public Iterator<JCase> cases() { return this.cases.iterator(); }
/*    */ 
/*    */   public JCase _case(JExpression label) {
/* 64 */     JCase c = new JCase(label);
/* 65 */     this.cases.add(c);
/* 66 */     return c;
/*    */   }
/*    */ 
/*    */   public JCase _default()
/*    */   {
/* 73 */     this.defaultCase = new JCase(null, true);
/* 74 */     return this.defaultCase;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 78 */     if (JOp.hasTopOp(this.test))
/* 79 */       f.p("switch ").g(this.test).p(" {").nl();
/*    */     else {
/* 81 */       f.p("switch (").g(this.test).p(')').p(" {").nl();
/*    */     }
/* 83 */     for (JCase c : this.cases)
/* 84 */       f.s(c);
/* 85 */     if (this.defaultCase != null)
/* 86 */       f.s(this.defaultCase);
/* 87 */     f.p('}').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JSwitch
 * JD-Core Version:    0.6.2
 */