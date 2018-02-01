/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class Xor extends BinaryExpr
/*    */ {
/*    */   protected Xor(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("^", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Number localNumber = (Number)left().evaluate();
/* 55 */       localObject1 = (Number)right().evaluate();
/*    */ 
/* 57 */       if (((localNumber instanceof Float)) || ((localNumber instanceof Double)) || ((localObject1 instanceof Float)) || ((localObject1 instanceof Double)))
/*    */       {
/* 59 */         localObject2 = new String[] { Util.getMessage("EvaluationException.xor"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */       }
/*    */ 
/* 69 */       Object localObject2 = (BigInteger)coerceToTarget((BigInteger)localNumber);
/* 70 */       BigInteger localBigInteger = (BigInteger)coerceToTarget((BigInteger)localObject1);
/* 71 */       value(((BigInteger)localObject2).xor(localBigInteger));
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 76 */       Object localObject1 = { Util.getMessage("EvaluationException.xor"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 77 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject1));
/*    */     }
/* 79 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Xor
 * JD-Core Version:    0.6.2
 */