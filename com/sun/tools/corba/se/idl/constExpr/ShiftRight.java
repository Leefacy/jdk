/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class ShiftRight extends BinaryExpr
/*    */ {
/*    */   protected ShiftRight(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super(">>", paramExpression1, paramExpression2);
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
/* 59 */         localObject2 = new String[] { Util.getMessage("EvaluationException.right"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */       }
/*    */ 
/* 66 */       Object localObject2 = (BigInteger)coerceToTarget((BigInteger)localNumber);
/* 67 */       BigInteger localBigInteger = (BigInteger)localObject1;
/*    */ 
/* 70 */       if (((BigInteger)localObject2).signum() == -1) {
/* 71 */         if (type().equals("short"))
/* 72 */           localObject2 = ((BigInteger)localObject2).add(twoPow16);
/* 73 */         else if (type().equals("long"))
/* 74 */           localObject2 = ((BigInteger)localObject2).add(twoPow32);
/* 75 */         else if (type().equals("long long"))
/* 76 */           localObject2 = ((BigInteger)localObject2).add(twoPow64);
/*    */       }
/* 78 */       value(((BigInteger)localObject2).shiftRight(localBigInteger.intValue()));
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 83 */       Object localObject1 = { Util.getMessage("EvaluationException.right"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 84 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject1));
/*    */     }
/* 86 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.ShiftRight
 * JD-Core Version:    0.6.2
 */