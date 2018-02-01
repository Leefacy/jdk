/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class Negative extends UnaryExpr
/*    */ {
/*    */   protected Negative(Expression paramExpression)
/*    */   {
/* 47 */     super("-", paramExpression);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Number localNumber = (Number)operand().evaluate();
/*    */ 
/* 56 */       if (((localNumber instanceof Float)) || ((localNumber instanceof Double))) {
/* 57 */         value(new Double(-localNumber.doubleValue()));
/*    */       }
/*    */       else
/*    */       {
/* 62 */         localObject = (BigInteger)localNumber;
/* 63 */         value(((BigInteger)localObject).multiply(BigInteger.valueOf(-1L)));
/*    */       }
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 68 */       Object localObject = { Util.getMessage("EvaluationException.neg"), operand().value().getClass().getName() };
/* 69 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", (String[])localObject));
/*    */     }
/* 71 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Negative
 * JD-Core Version:    0.6.2
 */