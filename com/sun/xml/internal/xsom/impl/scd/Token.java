/*     */ package com.sun.xml.internal.xsom.impl.scd;
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
/*     */   public String toString()
/*     */   {
/*  83 */     return this.image;
/*     */   }
/*     */ 
/*     */   public static final Token newToken(int ofKind)
/*     */   {
/* 100 */     switch (ofKind) {
/*     */     }
/* 102 */     return new Token();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.scd.Token
 * JD-Core Version:    0.6.2
 */