/*     */ package com.sun.tools.example.debug.expr;
/*     */ 
/*     */ public class ParseException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 7978489144303647901L;
/*     */   protected boolean specialConstructor;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public String[] tokenImage;
/* 178 */   protected String eol = System.getProperty("line.separator", "\n");
/*     */ 
/*     */   public ParseException(Token paramToken, int[][] paramArrayOfInt, String[] paramArrayOfString)
/*     */   {
/*  68 */     super("");
/*  69 */     this.specialConstructor = true;
/*  70 */     this.currentToken = paramToken;
/*  71 */     this.expectedTokenSequences = paramArrayOfInt;
/*  72 */     this.tokenImage = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   public ParseException()
/*     */   {
/*  87 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */   public ParseException(String paramString) {
/*  91 */     super(paramString);
/*  92 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 135 */     if (!this.specialConstructor) {
/* 136 */       return super.getMessage();
/*     */     }
/* 138 */     String str = "";
/* 139 */     int i = 0;
/* 140 */     for (Object localObject2 : this.expectedTokenSequences) {
/* 141 */       if (i < localObject2.length) {
/* 142 */         i = localObject2.length;
/*     */       }
/* 144 */       for (int m = 0; m < localObject2.length; m++) {
/* 145 */         str = str + this.tokenImage[localObject2[m]] + " ";
/*     */       }
/* 147 */       if (localObject2[(localObject2.length - 1)] != 0) {
/* 148 */         str = str + "...";
/*     */       }
/* 150 */       str = str + this.eol + "    ";
/*     */     }
/* 152 */     ??? = "Encountered \"";
/* 153 */     Token localToken = this.currentToken.next;
/* 154 */     for (??? = 0; ??? < i; ???++) {
/* 155 */       if (??? != 0) {
/* 156 */         ??? = (String)??? + " ";
/*     */       }
/* 158 */       if (localToken.kind == 0) {
/* 159 */         ??? = (String)??? + this.tokenImage[0];
/* 160 */         break;
/*     */       }
/* 162 */       ??? = (String)??? + add_escapes(localToken.image);
/* 163 */       localToken = localToken.next;
/*     */     }
/* 165 */     ??? = (String)??? + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn + "." + this.eol;
/* 166 */     if (this.expectedTokenSequences.length == 1)
/* 167 */       ??? = (String)??? + "Was expecting:" + this.eol + "    ";
/*     */     else {
/* 169 */       ??? = (String)??? + "Was expecting one of:" + this.eol + "    ";
/*     */     }
/* 171 */     ??? = (String)??? + str;
/* 172 */     return ???;
/*     */   }
/*     */ 
/*     */   protected String add_escapes(String paramString)
/*     */   {
/* 186 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 188 */     for (int i = 0; i < paramString.length(); i++) {
/* 189 */       switch (paramString.charAt(i))
/*     */       {
/*     */       case '\000':
/* 192 */         break;
/*     */       case '\b':
/* 194 */         localStringBuffer.append("\\b");
/* 195 */         break;
/*     */       case '\t':
/* 197 */         localStringBuffer.append("\\t");
/* 198 */         break;
/*     */       case '\n':
/* 200 */         localStringBuffer.append("\\n");
/* 201 */         break;
/*     */       case '\f':
/* 203 */         localStringBuffer.append("\\f");
/* 204 */         break;
/*     */       case '\r':
/* 206 */         localStringBuffer.append("\\r");
/* 207 */         break;
/*     */       case '"':
/* 209 */         localStringBuffer.append("\\\"");
/* 210 */         break;
/*     */       case '\'':
/* 212 */         localStringBuffer.append("\\'");
/* 213 */         break;
/*     */       case '\\':
/* 215 */         localStringBuffer.append("\\\\");
/* 216 */         break;
/*     */       default:
/*     */         char c;
/* 218 */         if (((c = paramString.charAt(i)) < ' ') || (c > '~')) {
/* 219 */           String str = "0000" + Integer.toString(c, 16);
/* 220 */           localStringBuffer.append("\\u" + str.substring(str.length() - 4, str.length()));
/*     */         } else {
/* 222 */           localStringBuffer.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 227 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.expr.ParseException
 * JD-Core Version:    0.6.2
 */