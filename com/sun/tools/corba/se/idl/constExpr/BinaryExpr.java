/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ public abstract class BinaryExpr extends Expression
/*    */ {
/* 61 */   private String _op = "";
/* 62 */   private Expression _left = null;
/* 63 */   private Expression _right = null;
/*    */ 
/*    */   public BinaryExpr(String paramString, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     this._op = paramString;
/* 48 */     this._left = paramExpression1;
/* 49 */     this._right = paramExpression2;
/*    */   }
/*    */   public void op(String paramString) {
/* 52 */     this._op = (paramString == null ? "" : paramString); } 
/* 53 */   public String op() { return this._op; } 
/*    */   public void left(Expression paramExpression) {
/* 55 */     this._left = paramExpression; } 
/* 56 */   public Expression left() { return this._left; } 
/*    */   public void right(Expression paramExpression) {
/* 58 */     this._right = paramExpression; } 
/* 59 */   public Expression right() { return this._right; }
/*    */ 
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.BinaryExpr
 * JD-Core Version:    0.6.2
 */