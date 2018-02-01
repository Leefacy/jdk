/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ public class TokenMgrError extends Error
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */ 
/*     */   protected static final String addEscapes(String str)
/*     */   {
/*  96 */     StringBuffer retval = new StringBuffer();
/*     */ 
/*  98 */     for (int i = 0; i < str.length(); i++) {
/*  99 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000':
/* 102 */         break;
/*     */       case '\b':
/* 104 */         retval.append("\\b");
/* 105 */         break;
/*     */       case '\t':
/* 107 */         retval.append("\\t");
/* 108 */         break;
/*     */       case '\n':
/* 110 */         retval.append("\\n");
/* 111 */         break;
/*     */       case '\f':
/* 113 */         retval.append("\\f");
/* 114 */         break;
/*     */       case '\r':
/* 116 */         retval.append("\\r");
/* 117 */         break;
/*     */       case '"':
/* 119 */         retval.append("\\\"");
/* 120 */         break;
/*     */       case '\'':
/* 122 */         retval.append("\\'");
/* 123 */         break;
/*     */       case '\\':
/* 125 */         retval.append("\\\\");
/* 126 */         break;
/*     */       default:
/*     */         char ch;
/* 128 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/* 129 */           String s = "0000" + Integer.toString(ch, 16);
/* 130 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/* 132 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 137 */     return retval.toString();
/*     */   }
/*     */ 
/*     */   protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar)
/*     */   {
/* 157 */     return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : new StringBuilder().append("\"")
/* 156 */       .append(addEscapes(String.valueOf(curChar)))
/* 156 */       .append("\"").append(" (").append(curChar).append("), ").toString()) + "after : \"" + 
/* 157 */       addEscapes(errorAfter) + 
/* 157 */       "\"";
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 170 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public TokenMgrError()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TokenMgrError(String message, int reason)
/*     */   {
/* 183 */     super(message);
/* 184 */     this.errorCode = reason;
/*     */   }
/*     */ 
/*     */   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason)
/*     */   {
/* 189 */     this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.compact.TokenMgrError
 * JD-Core Version:    0.6.2
 */