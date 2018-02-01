/*     */ package com.sun.tools.example.debug.expr;
/*     */ 
/*     */ public class Token
/*     */ {
/*     */   public int kind;
/*     */   public int beginLine;
/*     */   public int beginColumn;
/*     */   public int endLine;
/*     */   public int endColumn;
/*     */   public String image;
/*     */   public Token next;
/*     */   public Token specialToken;
/*     */ 
/*     */   public final String toString()
/*     */   {
/*  93 */     return this.image;
/*     */   }
/*     */ 
/*     */   public static final Token newToken(int paramInt)
/*     */   {
/* 110 */     switch (paramInt) {
/*     */     }
/* 112 */     return new Token();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.expr.Token
 * JD-Core Version:    0.6.2
 */