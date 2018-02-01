/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ public class TokenMgrError extends Error
/*     */ {
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */ 
/*     */   protected static final String addEscapes(String str)
/*     */   {
/*  66 */     StringBuffer retval = new StringBuffer();
/*     */ 
/*  68 */     for (int i = 0; i < str.length(); i++) {
/*  69 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000':
/*  72 */         break;
/*     */       case '\b':
/*  74 */         retval.append("\\b");
/*  75 */         break;
/*     */       case '\t':
/*  77 */         retval.append("\\t");
/*  78 */         break;
/*     */       case '\n':
/*  80 */         retval.append("\\n");
/*  81 */         break;
/*     */       case '\f':
/*  83 */         retval.append("\\f");
/*  84 */         break;
/*     */       case '\r':
/*  86 */         retval.append("\\r");
/*  87 */         break;
/*     */       case '"':
/*  89 */         retval.append("\\\"");
/*  90 */         break;
/*     */       case '\'':
/*  92 */         retval.append("\\'");
/*  93 */         break;
/*     */       case '\\':
/*  95 */         retval.append("\\\\");
/*  96 */         break;
/*     */       default:
/*     */         char ch;
/*  98 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/*  99 */           String s = "0000" + Integer.toString(ch, 16);
/* 100 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/* 102 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 107 */     return retval.toString();
/*     */   }
/*     */ 
/*     */   protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar)
/*     */   {
/* 127 */     return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : new StringBuilder().append("\"")
/* 126 */       .append(addEscapes(String.valueOf(curChar)))
/* 126 */       .append("\"").append(" (").append(curChar).append("), ").toString()) + "after : \"" + 
/* 127 */       addEscapes(errorAfter) + 
/* 127 */       "\"";
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 140 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public TokenMgrError()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TokenMgrError(String message, int reason)
/*     */   {
/* 151 */     super(message);
/* 152 */     this.errorCode = reason;
/*     */   }
/*     */ 
/*     */   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
/* 156 */     this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.scd.TokenMgrError
 * JD-Core Version:    0.6.2
 */