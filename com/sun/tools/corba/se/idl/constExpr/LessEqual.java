/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class LessEqual extends BinaryExpr
/*    */ {
/*    */   protected LessEqual(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("<=", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Object localObject1 = left().evaluate();
/* 55 */       localObject2 = right().evaluate();
/* 56 */       if ((localObject1 instanceof Boolean))
/*    */       {
/* 58 */         localObject3 = new String[] { Util.getMessage("EvaluationException.lessEqual"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 59 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject3));
/*    */       }
/*    */ 
/* 63 */       Object localObject3 = (Number)localObject1;
/* 64 */       Number localNumber = (Number)right().evaluate();
/* 65 */       if (((localObject3 instanceof Float)) || ((localObject3 instanceof Double)) || ((localNumber instanceof Float)) || ((localNumber instanceof Double))) {
/* 66 */         value(new Boolean(((Number)localObject3).doubleValue() <= localNumber.doubleValue()));
/*    */       }
/*    */       else {
/* 69 */         value(new Boolean(((BigInteger)localObject3).compareTo((BigInteger)localNumber) <= 0));
/*    */       }
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 74 */       Object localObject2 = { Util.getMessage("EvaluationException.lessEqual"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 75 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */     }
/* 77 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.LessEqual
 * JD-Core Version:    0.6.2
 */