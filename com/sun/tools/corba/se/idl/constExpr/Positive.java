/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class Positive extends UnaryExpr
/*    */ {
/*    */   protected Positive(Expression paramExpression)
/*    */   {
/* 47 */     super("+", paramExpression);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Number localNumber = (Number)operand().evaluate();
/*    */ 
/* 56 */       if (((localNumber instanceof Float)) || ((localNumber instanceof Double))) {
/* 57 */         value(new Double(localNumber.doubleValue()));
/*    */       }
/*    */       else
/*    */       {
/* 62 */         value(((BigInteger)localNumber).multiply(BigInteger.valueOf(((BigInteger)localNumber).signum())));
/*    */       }
/*    */ 
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 68 */       String[] arrayOfString = { Util.getMessage("EvaluationException.pos"), operand().value().getClass().getName() };
/* 69 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", arrayOfString));
/*    */     }
/* 71 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Positive
 * JD-Core Version:    0.6.2
 */