/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public final class JCase
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression label;
/* 41 */   private JBlock body = null;
/*    */ 
/* 46 */   private boolean isDefaultCase = false;
/*    */ 
/*    */   JCase(JExpression label)
/*    */   {
/* 52 */     this(label, false);
/*    */   }
/*    */ 
/*    */   JCase(JExpression label, boolean isDefaultCase)
/*    */   {
/* 60 */     this.label = label;
/* 61 */     this.isDefaultCase = isDefaultCase;
/*    */   }
/*    */ 
/*    */   public JExpression label() {
/* 65 */     return this.label;
/*    */   }
/*    */ 
/*    */   public JBlock body() {
/* 69 */     if (this.body == null) this.body = new JBlock(false, true);
/* 70 */     return this.body;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 74 */     f.i();
/* 75 */     if (!this.isDefaultCase)
/* 76 */       f.p("case ").g(this.label).p(':').nl();
/*    */     else {
/* 78 */       f.p("default:").nl();
/*    */     }
/* 80 */     if (this.body != null)
/* 81 */       f.s(this.body);
/* 82 */     f.o();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JCase
 * JD-Core Version:    0.6.2
 */