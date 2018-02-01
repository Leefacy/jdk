/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public class JStringLiteral extends JExpressionImpl
/*    */ {
/*    */   public final String str;
/*    */ 
/*    */   JStringLiteral(String what)
/*    */   {
/* 40 */     this.str = what;
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f)
/*    */   {
/* 46 */     f.p(JExpr.quotify('"', this.str));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JStringLiteral
 * JD-Core Version:    0.6.2
 */