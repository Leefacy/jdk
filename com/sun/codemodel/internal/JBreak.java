/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ final class JBreak
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */ 
/*    */   JBreak(JLabel _label)
/*    */   {
/* 43 */     this.label = _label;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 47 */     if (this.label == null)
/* 48 */       f.p("break;").nl();
/*    */     else
/* 50 */       f.p("break").p(this.label.label).p(';').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JBreak
 * JD-Core Version:    0.6.2
 */