/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ final class JAtom extends JExpressionImpl
/*    */ {
/*    */   private final String what;
/*    */ 
/*    */   JAtom(String what)
/*    */   {
/* 37 */     this.what = what;
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f) {
/* 41 */     f.p(this.what);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAtom
 * JD-Core Version:    0.6.2
 */