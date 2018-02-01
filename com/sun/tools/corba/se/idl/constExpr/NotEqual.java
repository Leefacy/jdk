/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class NotEqual extends BinaryExpr
/*    */ {
/*    */   protected NotEqual(Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 47 */     super("!=", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Object evaluate() throws EvaluationException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       Object localObject1 = left().evaluate();
/*    */       Object localObject3;
/* 55 */       if ((localObject1 instanceof Boolean))
/*    */       {
/* 57 */         localObject2 = (Boolean)localObject1;
/* 58 */         localObject3 = (Boolean)right().evaluate();
/* 59 */         value(new Boolean(((Boolean)localObject2).booleanValue() != ((Boolean)localObject3).booleanValue()));
/*    */       }
/*    */       else
/*    */       {
/* 63 */         localObject2 = (Number)localObject1;
/* 64 */         localObject3 = (Number)right().evaluate();
/*    */ 
/* 66 */         if (((localObject2 instanceof Float)) || ((localObject2 instanceof Double)) || ((localObject3 instanceof Float)) || ((localObject3 instanceof Double))) {
/* 67 */           value(new Boolean(((Number)localObject2).doubleValue() != ((Number)localObject3).doubleValue()));
/*    */         }
/*    */         else
/* 70 */           value(new Boolean(!((BigInteger)localObject2).equals((BigInteger)localObject3)));
/*    */       }
/*    */     }
/*    */     catch (ClassCastException localClassCastException)
/*    */     {
/* 75 */       Object localObject2 = { Util.getMessage("EvaluationException.notEqual"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 76 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*    */     }
/* 78 */     return value();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.NotEqual
 * JD-Core Version:    0.6.2
 */