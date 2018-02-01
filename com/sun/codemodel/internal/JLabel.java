/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public class JLabel
/*    */   implements JStatement
/*    */ {
/*    */   final String label;
/*    */ 
/*    */   JLabel(String _label)
/*    */   {
/* 45 */     this.label = _label;
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 49 */     f.p(this.label + ':').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JLabel
 * JD-Core Version:    0.6.2
 */