/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class Not extends UnaryExpr
/*    */ {
/*    */   protected Not(Expression paramExpression)
/*    */   {
/* 47 */     super("~", paramExpression);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Number localNumber = (Number)operand().evaluate();
/*    */ 
/* 56 */       if (((localNumber instanceof Float)) || ((localNumber instanceof Double)))
/*    */       {
/* 58 */         localObject = new String[] { Util.getMessage("EvaluationException.not"), operand().value().getClass().getName() };
/* 59 */         throw new EvaluationException(Util.getMessage("EvaluationException.2", (String[])localObject));
/*    */       }
/*    */ 
/* 65 */       localObject = (BigInteger)coerceToTarget((BigInteger)localNumber);
/*    */ 
/* 68 */       if ((type().equals("short")) || (type().equals("long")) || (type().equals("long long")))
/* 69 */         value(((BigInteger)localObject).add(one).multiply(negOne));
/* 70 */       else if (type().equals("unsigned short"))
/*    */       {
/* 72 */         value(twoPow16.subtract(one).subtract((BigInteger)localObject));
/* 73 */       } else if (type().equals("unsigned long"))
/* 74 */         value(twoPow32.subtract(one).subtract((BigInteger)localObject));
/* 75 */       else if (type().equals("unsigned long long"))
/* 76 */         value(twoPow64.subtract(one).subtract((BigInteger)localObject));
/*    */       else {
/* 78 */         value(((BigInteger)localObject).not());
/*    */       }
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 83 */       Object localObject = { Util.getMessage("EvaluationException.not"), operand().value().getClass().getName() };
/* 84 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", (String[])localObject));
/*    */     }
/* 86 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Not
 * JD-Core Version:    0.6.2
 */