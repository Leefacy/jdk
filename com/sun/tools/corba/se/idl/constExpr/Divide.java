/*     */ package com.sun.tools.corba.se.idl.constExpr;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Util;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class Divide extends BinaryExpr
/*     */ {
/*     */   protected Divide(Expression paramExpression1, Expression paramExpression2)
/*     */   {
/*  57 */     super("/", paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   public Object evaluate()
/*     */     throws EvaluationException
/*     */   {
/*     */     try
/*     */     {
/*  67 */       Number localNumber = (Number)left().evaluate();
/*  68 */       localObject1 = (Number)right().evaluate();
/*     */ 
/*  70 */       int i = ((localNumber instanceof Float)) || ((localNumber instanceof Double)) ? 1 : 0;
/*  71 */       int j = ((localObject1 instanceof Float)) || ((localObject1 instanceof Double)) ? 1 : 0;
/*     */ 
/*  73 */       if ((i != 0) && (j != 0)) {
/*  74 */         value(new Double(localNumber.doubleValue() / ((Number)localObject1).doubleValue())); } else {
/*  75 */         if ((i != 0) || (j != 0))
/*     */         {
/*  79 */           localObject2 = new String[] { Util.getMessage("EvaluationException.divide"), 
/*  78 */             left().value().getClass().getName(), 
/*  79 */             right().value().getClass().getName() };
/*  80 */           throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject2));
/*     */         }
/*     */ 
/*  84 */         Object localObject2 = (BigInteger)localNumber; BigInteger localBigInteger = (BigInteger)localObject1;
/*  85 */         value(((BigInteger)localObject2).divide(localBigInteger));
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 127 */       Object localObject1 = { Util.getMessage("EvaluationException.divide"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 128 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", (String[])localObject1));
/*     */     }
/* 130 */     return value();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.Divide
 * JD-Core Version:    0.6.2
 */