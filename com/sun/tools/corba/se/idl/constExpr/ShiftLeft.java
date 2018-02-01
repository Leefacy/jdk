/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class ShiftLeft extends BinaryExpr
/*    */ {
/*    */   protected ShiftLeft(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("<<", paramExpression1, paramExpression2);
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
/* 59 */         localObject2 = new String[] { Util.getMessage("EvaluationException.left"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */       }
/*    */ 
/* 66 */       Object localObject2 = (BigInteger)coerceToTarget(localNumber);
/* 67 */       BigInteger localBigInteger1 = (BigInteger)localObject1;
/*    */ 
/* 69 */       BigInteger localBigInteger2 = ((BigInteger)localObject2).shiftLeft(localBigInteger1.intValue());
/*    */ 
/* 71 */       if (type().indexOf("short") >= 0)
/* 72 */         localBigInteger2 = localBigInteger2.mod(twoPow16);
/* 73 */       else if (type().indexOf("long") >= 0)
/* 74 */         localBigInteger2 = localBigInteger2.mod(twoPow32);
/* 75 */       else if (type().indexOf("long long") >= 0) {
/* 76 */         localBigInteger2 = localBigInteger2.mod(twoPow64);
/*    */       }
/* 78 */       value(coerceToTarget(localBigInteger2));
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 83 */       Object localObject1 = { Util.getMessage("EvaluationException.left"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 84 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject1));
/*    */     }
/* 86 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.ShiftLeft
 * JD-Core Version:    0.6.2
 */