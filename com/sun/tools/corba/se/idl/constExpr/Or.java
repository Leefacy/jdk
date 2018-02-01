/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class Or extends BinaryExpr
/*    */ {
/*    */   protected Or(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("|", paramExpression1, paramExpression2);
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
/* 59 */         localObject2 = new String[] { Util.getMessage("EvaluationException.or"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */       }
/*    */ 
/* 66 */       Object localObject2 = toUnsigned((BigInteger)localNumber);
/* 67 */       BigInteger localBigInteger = toUnsigned((BigInteger)localObject1);
/* 68 */       value((BigInteger)coerceToTarget(((BigInteger)localObject2).or(localBigInteger)));
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 73 */       Object localObject1 = { Util.getMessage("EvaluationException.or"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 74 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject1));
/*    */     }
/* 76 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Or
 * JD-Core Version:    0.6.2
 */