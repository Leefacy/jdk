/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class BooleanNot extends UnaryExpr
/*    */ {
/*    */   protected BooleanNot(Expression paramExpression)
/*    */   {
/* 47 */     super("!", paramExpression);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Object localObject1 = operand().evaluate();
/*    */ 
/* 60 */       if ((localObject1 instanceof Number))
/*    */       {
/* 62 */         if ((localObject1 instanceof BigInteger))
/* 63 */           localObject2 = new Boolean(((BigInteger)localObject1).compareTo(zero) != 0);
/*    */         else
/* 65 */           localObject2 = new Boolean(((Number)localObject1).longValue() != 0L);
/*    */       }
/*    */       else {
/* 68 */         localObject2 = (Boolean)localObject1;
/*    */       }
/* 70 */       value(new Boolean(!((Boolean)localObject2).booleanValue()));
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 74 */       Object localObject2 = { Util.getMessage("EvaluationException.booleanNot"), operand().value().getClass().getName() };
/* 75 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", (String[])localObject2));
/*    */     }
/* 77 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.BooleanNot
 * JD-Core Version:    0.6.2
 */