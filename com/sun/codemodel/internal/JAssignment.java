/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public class JAssignment extends JExpressionImpl
/*    */   implements JStatement
/*    */ {
/*    */   JAssignmentTarget lhs;
/*    */   JExpression rhs;
/* 36 */   String op = "";
/*    */ 
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs) {
/* 39 */     this.lhs = lhs;
/* 40 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs, String op) {
/* 44 */     this.lhs = lhs;
/* 45 */     this.rhs = rhs;
/* 46 */     this.op = op;
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f) {
/* 50 */     f.g(this.lhs).p(this.op + '=').g(this.rhs);
/*    */   }
/*    */ 
/*    */   public void state(JFormatter f) {
/* 54 */     f.g(this).p(';').nl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAssignment
 * JD-Core Version:    0.6.2
 */