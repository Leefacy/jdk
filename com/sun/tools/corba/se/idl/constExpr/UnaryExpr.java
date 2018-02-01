/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ public abstract class UnaryExpr extends Expression
/*    */ {
/* 57 */   private String _op = "";
/* 58 */   private Expression _operand = null;
/*    */ 
/*    */   public UnaryExpr(String paramString, Expression paramExpression)
/*    */   {
/* 47 */     this._op = paramString;
/* 48 */     this._operand = paramExpression;
/*    */   }
/*    */   public void op(String paramString) {
/* 51 */     this._op = (paramString == null ? "" : paramString); } 
/* 52 */   public String op() { return this._op; } 
/*    */   public void operand(Expression paramExpression) {
/* 54 */     this._operand = paramExpression; } 
/* 55 */   public Expression operand() { return this._operand; }
/*    */ 
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.UnaryExpr
 * JD-Core Version:    0.6.2
 */