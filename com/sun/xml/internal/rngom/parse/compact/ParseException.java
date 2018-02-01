/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ public class ParseException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public String[] tokenImage;
/* 179 */   protected String eol = System.getProperty("line.separator", "\n");
/*     */ 
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal)
/*     */   {
/*  79 */     super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
/*  80 */     this.currentToken = currentTokenVal;
/*  81 */     this.expectedTokenSequences = expectedTokenSequencesVal;
/*  82 */     this.tokenImage = tokenImageVal;
/*     */   }
/*     */ 
/*     */   public ParseException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ParseException(String message)
/*     */   {
/* 101 */     super(message);
/*     */   }
/*     */ 
/*     */   private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage)
/*     */   {
/* 136 */     String eol = System.getProperty("line.separator", "\n");
/* 137 */     StringBuffer expected = new StringBuffer();
/* 138 */     int maxSize = 0;
/* 139 */     for (int i = 0; i < expectedTokenSequences.length; i++) {
/* 140 */       if (maxSize < expectedTokenSequences[i].length) {
/* 141 */         maxSize = expectedTokenSequences[i].length;
/*     */       }
/* 143 */       for (int j = 0; j < expectedTokenSequences[i].length; j++) {
/* 144 */         expected.append(tokenImage[expectedTokenSequences[i][j]]).append(' ');
/*     */       }
/* 146 */       if (expectedTokenSequences[i][(expectedTokenSequences[i].length - 1)] != 0) {
/* 147 */         expected.append("...");
/*     */       }
/* 149 */       expected.append(eol).append("    ");
/*     */     }
/* 151 */     String retval = "Encountered \"";
/* 152 */     Token tok = currentToken.next;
/* 153 */     for (int i = 0; i < maxSize; i++) {
/* 154 */       if (i != 0) retval = retval + " ";
/* 155 */       if (tok.kind == 0) {
/* 156 */         retval = retval + tokenImage[0];
/* 157 */         break;
/*     */       }
/* 159 */       retval = retval + " " + tokenImage[tok.kind];
/* 160 */       retval = retval + " \"";
/* 161 */       retval = retval + add_escapes(tok.image);
/* 162 */       retval = retval + " \"";
/* 163 */       tok = tok.next;
/*     */     }
/* 165 */     retval = retval + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
/* 166 */     retval = retval + "." + eol;
/* 167 */     if (expectedTokenSequences.length == 1)
/* 168 */       retval = retval + "Was expecting:" + eol + "    ";
/*     */     else {
/* 170 */       retval = retval + "Was expecting one of:" + eol + "    ";
/*     */     }
/* 172 */     retval = retval + expected.toString();
/* 173 */     return retval;
/*     */   }
/*     */ 
/*     */   static String add_escapes(String str)
/*     */   {
/* 187 */     StringBuffer retval = new StringBuffer();
/*     */ 
/* 189 */     for (int i = 0; i < str.length(); i++) {
/* 190 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000':
/* 193 */         break;
/*     */       case '\b':
/* 195 */         retval.append("\\b");
/* 196 */         break;
/*     */       case '\t':
/* 198 */         retval.append("\\t");
/* 199 */         break;
/*     */       case '\n':
/* 201 */         retval.append("\\n");
/* 202 */         break;
/*     */       case '\f':
/* 204 */         retval.append("\\f");
/* 205 */         break;
/*     */       case '\r':
/* 207 */         retval.append("\\r");
/* 208 */         break;
/*     */       case '"':
/* 210 */         retval.append("\\\"");
/* 211 */         break;
/*     */       case '\'':
/* 213 */         retval.append("\\'");
/* 214 */         break;
/*     */       case '\\':
/* 216 */         retval.append("\\\\");
/* 217 */         break;
/*     */       default:
/*     */         char ch;
/* 219 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/* 220 */           String s = "0000" + Integer.toString(ch, 16);
/* 221 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/* 223 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 228 */     return retval.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.compact.ParseException
 * JD-Core Version:    0.6.2
 */