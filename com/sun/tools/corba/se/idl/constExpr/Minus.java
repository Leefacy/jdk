/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class Minus extends BinaryExpr
/*    */ {
/*    */   protected Minus(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("-", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Number localNumber = (Number)left().evaluate();
/* 55 */       localObject1 = (Number)right().evaluate();
/*    */ 
/* 57 */       int i = ((localNumber instanceof Float)) || ((localNumber instanceof Double)) ? 1 : 0;
/* 58 */       int j = ((localObject1 instanceof Float)) || ((localObject1 instanceof Double)) ? 1 : 0;
/*    */ 
/* 60 */       if ((i != 0) && (j != 0)) {
/* 61 */         value(new Double(localNumber.doubleValue() - ((Number)localObject1).doubleValue())); } else {
/* 62 */         if ((i != 0) || (j != 0))
/*    */         {
/* 64 */           localObject2 = new String[] { Util.getMessage("EvaluationException.minus"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 65 */           throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */         }
/*    */ 
/* 70 */         Object localObject2 = (BigInteger)localNumber; BigInteger localBigInteger = (BigInteger)localObject1;
/* 71 */         value(((BigInteger)localObject2).subtract(localBigInteger));
/*    */       }
/*    */ 
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 77 */       Object localObject1 = { Util.getMessage("EvaluationException.minus"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 78 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject1));
/*    */     }
/* 80 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Minus
 * JD-Core Version:    0.6.2
 */