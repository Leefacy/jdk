/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class BooleanAnd extends BinaryExpr
/*    */ {
/*    */   protected BooleanAnd(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("&&", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Object localObject1 = left().evaluate();
/* 55 */       localObject2 = right().evaluate();
/*    */       Boolean localBoolean1;
/* 63 */       if ((localObject1 instanceof Number))
/*    */       {
/* 65 */         if ((localObject1 instanceof BigInteger))
/* 66 */           localBoolean1 = new Boolean(((BigInteger)localObject1).compareTo(BigInteger.valueOf(0L)) != 0);
/*    */         else
/* 68 */           localBoolean1 = new Boolean(((Number)localObject1).longValue() != 0L);
/*    */       }
/*    */       else
/* 71 */         localBoolean1 = (Boolean)localObject1;
/*    */       Boolean localBoolean2;
/* 76 */       if ((localObject2 instanceof Number))
/*    */       {
/* 78 */         if ((localObject2 instanceof BigInteger))
/* 79 */           localBoolean2 = new Boolean(((BigInteger)localObject2).compareTo(zero) != 0);
/*    */         else
/* 81 */           localBoolean2 = new Boolean(((Number)localObject2).longValue() != 0L);
/*    */       }
/*    */       else {
/* 84 */         localBoolean2 = (Boolean)localObject2;
/*    */       }
/* 86 */       value(new Boolean((localBoolean1.booleanValue()) && (localBoolean2.booleanValue())));
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 90 */       Object localObject2 = { Util.getMessage("EvaluationException.booleanAnd"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 91 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */     }
/* 93 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.BooleanAnd
 * JD-Core Version:    0.6.2
 */