/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class ParseException extends Exception
/*     */ {
/*     */   protected boolean specialConstructor;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public List<String> tokenImage;
/* 167 */   protected String eol = System.getProperty("line.separator", "\n");
/*     */ 
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, List<String> tokenImageVal)
/*     */   {
/*  59 */     super("");
/*  60 */     this.specialConstructor = true;
/*  61 */     this.currentToken = currentTokenVal;
/*  62 */     this.expectedTokenSequences = expectedTokenSequencesVal;
/*  63 */     this.tokenImage = tokenImageVal;
/*     */   }
/*     */ 
/*     */   public ParseException()
/*     */   {
/*  78 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */   public ParseException(String message) {
/*  82 */     super(message);
/*  83 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 125 */     if (!this.specialConstructor) {
/* 126 */       return super.getMessage();
/*     */     }
/* 128 */     StringBuffer expected = new StringBuffer();
/* 129 */     int maxSize = 0;
/* 130 */     for (int i = 0; i < this.expectedTokenSequences.length; i++) {
/* 131 */       if (maxSize < this.expectedTokenSequences[i].length) {
/* 132 */         maxSize = this.expectedTokenSequences[i].length;
/*     */       }
/* 134 */       for (int j = 0; j < this.expectedTokenSequences[i].length; j++) {
/* 135 */         expected.append((String)this.tokenImage.get(this.expectedTokenSequences[i][j])).append(" ");
/*     */       }
/* 137 */       if (this.expectedTokenSequences[i][(this.expectedTokenSequences[i].length - 1)] != 0) {
/* 138 */         expected.append("...");
/*     */       }
/* 140 */       expected.append(this.eol).append("    ");
/*     */     }
/* 142 */     String retval = "Encountered \"";
/* 143 */     Token tok = this.currentToken.next;
/* 144 */     for (int i = 0; i < maxSize; i++) {
/* 145 */       if (i != 0) retval = retval + " ";
/* 146 */       if (tok.kind == 0) {
/* 147 */         retval = retval + (String)this.tokenImage.get(0);
/* 148 */         break;
/*     */       }
/* 150 */       retval = retval + add_escapes(tok.image);
/* 151 */       tok = tok.next;
/*     */     }
/* 153 */     retval = retval + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn;
/* 154 */     retval = retval + "." + this.eol;
/* 155 */     if (this.expectedTokenSequences.length == 1)
/* 156 */       retval = retval + "Was expecting:" + this.eol + "    ";
/*     */     else {
/* 158 */       retval = retval + "Was expecting one of:" + this.eol + "    ";
/*     */     }
/* 160 */     retval = retval + expected.toString();
/* 161 */     return retval;
/*     */   }
/*     */ 
/*     */   protected String add_escapes(String str)
/*     */   {
/* 175 */     StringBuffer retval = new StringBuffer();
/*     */ 
/* 177 */     for (int i = 0; i < str.length(); i++) {
/* 178 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000':
/* 181 */         break;
/*     */       case '\b':
/* 183 */         retval.append("\\b");
/* 184 */         break;
/*     */       case '\t':
/* 186 */         retval.append("\\t");
/* 187 */         break;
/*     */       case '\n':
/* 189 */         retval.append("\\n");
/* 190 */         break;
/*     */       case '\f':
/* 192 */         retval.append("\\f");
/* 193 */         break;
/*     */       case '\r':
/* 195 */         retval.append("\\r");
/* 196 */         break;
/*     */       case '"':
/* 198 */         retval.append("\\\"");
/* 199 */         break;
/*     */       case '\'':
/* 201 */         retval.append("\\'");
/* 202 */         break;
/*     */       case '\\':
/* 204 */         retval.append("\\\\");
/* 205 */         break;
/*     */       default:
/*     */         char ch;
/* 207 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/* 208 */           String s = "0000" + Integer.toString(ch, 16);
/* 209 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/* 211 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 216 */     return retval.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.scd.ParseException
 * JD-Core Version:    0.6.2
 */