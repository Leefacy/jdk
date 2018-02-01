/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.DefaultExprFactory;
/*     */ import com.sun.tools.corba.se.idl.constExpr.ExprFactory;
/*     */ 
/*     */ public class Factories
/*     */ {
/*     */   public GenFactory genFactory()
/*     */   {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   public SymtabFactory symtabFactory()
/*     */   {
/*  77 */     return new DefaultSymtabFactory();
/*     */   }
/*     */ 
/*     */   public ExprFactory exprFactory()
/*     */   {
/*  84 */     return new DefaultExprFactory();
/*     */   }
/*     */ 
/*     */   public Arguments arguments()
/*     */   {
/*  91 */     return new Arguments();
/*     */   }
/*     */ 
/*     */   public String[] languageKeywords()
/*     */   {
/* 106 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Factories
 * JD-Core Version:    0.6.2
 */