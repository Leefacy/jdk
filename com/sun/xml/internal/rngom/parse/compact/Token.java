/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Token
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public int kind;
/*     */   public int beginLine;
/*     */   public int beginColumn;
/*     */   public int endLine;
/*     */   public int endColumn;
/*     */   public String image;
/*     */   public Token next;
/*     */   public Token specialToken;
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public Token()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Token(int kind)
/*     */   {
/* 130 */     this(kind, null);
/*     */   }
/*     */ 
/*     */   public Token(int kind, String image)
/*     */   {
/* 138 */     this.kind = kind;
/* 139 */     this.image = image;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 147 */     return this.image;
/*     */   }
/*     */ 
/*     */   public static Token newToken(int ofKind, String image)
/*     */   {
/* 164 */     switch (ofKind) {
/*     */     }
/* 166 */     return new Token(ofKind, image);
/*     */   }
/*     */ 
/*     */   public static Token newToken(int ofKind)
/*     */   {
/* 172 */     return newToken(ofKind, null);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.compact.Token
 * JD-Core Version:    0.6.2
 */