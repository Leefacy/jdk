/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ class JContinue
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */ 
/*    */   JContinue(JLabel _label)
/*    */   {
/* 43 */     this.label = _label;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 47 */     if (this.label == null)
/* 48 */       f.p("continue;").nl();
/*    */     else
/* 50 */       f.p("continue").p(this.label.label).p(';').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JContinue
 * JD-Core Version:    0.6.2
 */