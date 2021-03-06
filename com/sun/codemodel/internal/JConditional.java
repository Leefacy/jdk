/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ public class JConditional
/*     */   implements JStatement
/*     */ {
/*  37 */   private JExpression test = null;
/*     */ 
/*  42 */   private JBlock _then = new JBlock();
/*     */ 
/*  47 */   private JBlock _else = null;
/*     */ 
/*     */   JConditional(JExpression test)
/*     */   {
/*  56 */     this.test = test;
/*     */   }
/*     */ 
/*     */   public JBlock _then()
/*     */   {
/*  65 */     return this._then;
/*     */   }
/*     */ 
/*     */   public JBlock _else()
/*     */   {
/*  74 */     if (this._else == null) this._else = new JBlock();
/*  75 */     return this._else;
/*     */   }
/*     */ 
/*     */   public JConditional _elseif(JExpression boolExp)
/*     */   {
/*  82 */     return _else()._if(boolExp);
/*     */   }
/*     */ 
/*     */   public void state(JFormatter f) {
/*  86 */     if (this.test == JExpr.TRUE) {
/*  87 */       this._then.generateBody(f);
/*  88 */       return;
/*     */     }
/*  90 */     if (this.test == JExpr.FALSE) {
/*  91 */       this._else.generateBody(f);
/*  92 */       return;
/*     */     }
/*     */ 
/*  95 */     if (JOp.hasTopOp(this.test))
/*  96 */       f.p("if ").g(this.test);
/*     */     else {
/*  98 */       f.p("if (").g(this.test).p(')');
/*     */     }
/* 100 */     f.g(this._then);
/* 101 */     if (this._else != null)
/* 102 */       f.p("else").g(this._else);
/* 103 */     f.nl();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JConditional
 * JD-Core Version:    0.6.2
 */