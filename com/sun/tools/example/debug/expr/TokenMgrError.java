/*     */ package com.sun.tools.example.debug.expr;
/*     */ 
/*     */ public class TokenMgrError extends Error
/*     */ {
/*     */   private static final long serialVersionUID = -6236440836177601522L;
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */ 
/*     */   protected static final String addEscapes(String paramString)
/*     */   {
/*  77 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/*  79 */     for (int i = 0; i < paramString.length(); i++) {
/*  80 */       switch (paramString.charAt(i))
/*     */       {
/*     */       case '\000':
/*  83 */         break;
/*     */       case '\b':
/*  85 */         localStringBuffer.append("\\b");
/*  86 */         break;
/*     */       case '\t':
/*  88 */         localStringBuffer.append("\\t");
/*  89 */         break;
/*     */       case '\n':
/*  91 */         localStringBuffer.append("\\n");
/*  92 */         break;
/*     */       case '\f':
/*  94 */         localStringBuffer.append("\\f");
/*  95 */         break;
/*     */       case '\r':
/*  97 */         localStringBuffer.append("\\r");
/*  98 */         break;
/*     */       case '"':
/* 100 */         localStringBuffer.append("\\\"");
/* 101 */         break;
/*     */       case '\'':
/* 103 */         localStringBuffer.append("\\'");
/* 104 */         break;
/*     */       case '\\':
/* 106 */         localStringBuffer.append("\\\\");
/* 107 */         break;
/*     */       default:
/*     */         char c;
/* 109 */         if (((c = paramString.charAt(i)) < ' ') || (c > '~')) {
/* 110 */           String str = "0000" + Integer.toString(c, 16);
/* 111 */           localStringBuffer.append("\\u" + str.substring(str.length() - 4, str.length()));
/*     */         } else {
/* 113 */           localStringBuffer.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 118 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static final String LexicalError(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, String paramString, char paramChar)
/*     */   {
/* 138 */     return "Lexical error at line " + paramInt2 + ", column " + paramInt3 + ".  Encountered: " + (paramBoolean ? "<EOF> " : new StringBuilder().append("\"")
/* 137 */       .append(addEscapes(String.valueOf(paramChar)))
/* 137 */       .append("\"").append(" (").append(paramChar).append("), ").toString()) + "after : \"" + 
/* 138 */       addEscapes(paramString) + 
/* 138 */       "\"";
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 152 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public TokenMgrError()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TokenMgrError(String paramString, int paramInt)
/*     */   {
/* 163 */     super(paramString);
/* 164 */     this.errorCode = paramInt;
/*     */   }
/*     */ 
/*     */   public TokenMgrError(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, String paramString, char paramChar, int paramInt4) {
/* 168 */     this(LexicalError(paramBoolean, paramInt1, paramInt2, paramInt3, paramString, paramChar), paramInt4);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.expr.TokenMgrError
 * JD-Core Version:    0.6.2
 */