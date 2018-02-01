/*     */ package com.sun.tools.javac.parser;
/*     */ 
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import com.sun.tools.javac.util.Position.LineMap;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ public class JavadocTokenizer extends JavaTokenizer
/*     */ {
/*     */   protected JavadocTokenizer(ScannerFactory paramScannerFactory, CharBuffer paramCharBuffer)
/*     */   {
/*  52 */     super(paramScannerFactory, paramCharBuffer);
/*     */   }
/*     */ 
/*     */   protected JavadocTokenizer(ScannerFactory paramScannerFactory, char[] paramArrayOfChar, int paramInt)
/*     */   {
/*  59 */     super(paramScannerFactory, paramArrayOfChar, paramInt);
/*     */   }
/*     */ 
/*     */   protected Tokens.Comment processComment(int paramInt1, int paramInt2, Tokens.Comment.CommentStyle paramCommentStyle)
/*     */   {
/*  64 */     char[] arrayOfChar = this.reader.getRawCharacters(paramInt1, paramInt2);
/*  65 */     return new JavadocComment(new DocReader(this.fac, arrayOfChar, arrayOfChar.length, paramInt1), paramCommentStyle);
/*     */   }
/*     */ 
/*     */   public Position.LineMap getLineMap()
/*     */   {
/* 444 */     char[] arrayOfChar = this.reader.getRawCharacters();
/* 445 */     return Position.makeLineMap(arrayOfChar, arrayOfChar.length, true);
/*     */   }
/*     */ 
/*     */   static class DocReader extends UnicodeReader
/*     */   {
/*     */     int col;
/*     */     int startPos;
/* 103 */     int[] pbuf = new int[''];
/*     */ 
/* 108 */     int pp = 0;
/*     */ 
/*     */     DocReader(ScannerFactory paramScannerFactory, char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 111 */       super(paramArrayOfChar, paramInt1);
/* 112 */       this.startPos = paramInt2;
/*     */     }
/*     */ 
/*     */     protected void convertUnicode()
/*     */     {
/* 117 */       if ((this.ch == '\\') && (this.unicodeConversionBp != this.bp)) {
/* 118 */         this.bp += 1; this.ch = this.buf[this.bp]; this.col += 1;
/* 119 */         if (this.ch == 'u') {
/*     */           do {
/* 121 */             this.bp += 1; this.ch = this.buf[this.bp]; this.col += 1;
/* 122 */           }while (this.ch == 'u');
/* 123 */           int i = this.bp + 3;
/* 124 */           if (i < this.buflen) {
/* 125 */             int j = digit(this.bp, 16);
/* 126 */             int k = j;
/* 127 */             while ((this.bp < i) && (j >= 0)) {
/* 128 */               this.bp += 1; this.ch = this.buf[this.bp]; this.col += 1;
/* 129 */               j = digit(this.bp, 16);
/* 130 */               k = (k << 4) + j;
/*     */             }
/* 132 */             if (j >= 0) {
/* 133 */               this.ch = ((char)k);
/* 134 */               this.unicodeConversionBp = this.bp;
/* 135 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 140 */           this.bp -= 1;
/* 141 */           this.ch = '\\';
/* 142 */           this.col -= 1;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void scanCommentChar()
/*     */     {
/* 149 */       scanChar();
/* 150 */       if (this.ch == '\\')
/* 151 */         if ((peekChar() == '\\') && (!isUnicode())) {
/* 152 */           putChar(this.ch, false);
/* 153 */           this.bp += 1; this.col += 1;
/*     */         } else {
/* 155 */           convertUnicode();
/*     */         }
/*     */     }
/*     */ 
/*     */     protected void scanChar()
/*     */     {
/* 162 */       this.bp += 1;
/* 163 */       this.ch = this.buf[this.bp];
/* 164 */       switch (this.ch) {
/*     */       case '\r':
/* 166 */         this.col = 0;
/* 167 */         break;
/*     */       case '\n':
/* 169 */         if ((this.bp == 0) || (this.buf[(this.bp - 1)] != '\r'))
/* 170 */           this.col = 0; break;
/*     */       case '\t':
/* 174 */         this.col = (this.col / 8 * 8 + 8);
/* 175 */         break;
/*     */       case '\\':
/* 177 */         this.col += 1;
/* 178 */         convertUnicode();
/* 179 */         break;
/*     */       default:
/* 181 */         this.col += 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void putChar(char paramChar, boolean paramBoolean)
/*     */     {
/* 194 */       if ((this.pp == 0) || (this.sp - this.pbuf[(this.pp - 2)] != this.startPos + this.bp - this.pbuf[(this.pp - 1)]))
/*     */       {
/* 196 */         if (this.pp + 1 >= this.pbuf.length) {
/* 197 */           int[] arrayOfInt = new int[this.pbuf.length * 2];
/* 198 */           System.arraycopy(this.pbuf, 0, arrayOfInt, 0, this.pbuf.length);
/* 199 */           this.pbuf = arrayOfInt;
/*     */         }
/* 201 */         this.pbuf[this.pp] = this.sp;
/* 202 */         this.pbuf[(this.pp + 1)] = (this.startPos + this.bp);
/* 203 */         this.pp += 2;
/*     */       }
/* 205 */       super.putChar(paramChar, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class JavadocComment extends JavaTokenizer.BasicComment<JavadocTokenizer.DocReader>
/*     */   {
/* 214 */     private String docComment = null;
/* 215 */     private int[] docPosns = null;
/*     */ 
/*     */     JavadocComment(JavadocTokenizer.DocReader paramDocReader, Tokens.Comment.CommentStyle paramCommentStyle) {
/* 218 */       super(paramCommentStyle);
/*     */     }
/*     */ 
/*     */     public String getText()
/*     */     {
/* 223 */       if ((!this.scanned) && (this.cs == Tokens.Comment.CommentStyle.JAVADOC)) {
/* 224 */         scanDocComment();
/*     */       }
/* 226 */       return this.docComment;
/*     */     }
/*     */ 
/*     */     public int getSourcePos(int paramInt)
/*     */     {
/* 237 */       if (paramInt == -1)
/* 238 */         return -1;
/* 239 */       if ((paramInt < 0) || (paramInt > this.docComment.length()))
/* 240 */         throw new StringIndexOutOfBoundsException(String.valueOf(paramInt));
/* 241 */       if (this.docPosns == null)
/* 242 */         return -1;
/* 243 */       int i = 0;
/* 244 */       int j = this.docPosns.length;
/* 245 */       while (i < j - 2)
/*     */       {
/* 247 */         int k = (i + j) / 4 * 2;
/* 248 */         if (this.docPosns[k] < paramInt) {
/* 249 */           i = k; } else {
/* 250 */           if (this.docPosns[k] == paramInt) {
/* 251 */             return this.docPosns[(k + 1)];
/*     */           }
/* 253 */           j = k;
/*     */         }
/*     */       }
/* 255 */       return this.docPosns[(i + 1)] + (paramInt - this.docPosns[i]);
/*     */     }
/*     */ 
/*     */     protected void scanDocComment()
/*     */     {
/*     */       try
/*     */       {
/* 262 */         int i = 1;
/*     */ 
/* 265 */         ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/*     */ 
/* 267 */         ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/*     */ 
/* 270 */         while ((((JavadocTokenizer.DocReader)this.comment_reader).bp < ((JavadocTokenizer.DocReader)this.comment_reader).buflen) && (((JavadocTokenizer.DocReader)this.comment_reader).ch == '*')) {
/* 271 */           ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/*     */         }
/*     */ 
/* 274 */         if ((((JavadocTokenizer.DocReader)this.comment_reader).bp < ((JavadocTokenizer.DocReader)this.comment_reader).buflen) && (((JavadocTokenizer.DocReader)this.comment_reader).ch == '/')) {
/* 275 */           this.docComment = "";
/* 276 */           return;
/*     */         }
/*     */ 
/* 280 */         if (((JavadocTokenizer.DocReader)this.comment_reader).bp < ((JavadocTokenizer.DocReader)this.comment_reader).buflen)
/* 281 */           if (((JavadocTokenizer.DocReader)this.comment_reader).ch == '\n') {
/* 282 */             ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 283 */             i = 0;
/* 284 */           } else if (((JavadocTokenizer.DocReader)this.comment_reader).ch == '\r') {
/* 285 */             ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 286 */             if (((JavadocTokenizer.DocReader)this.comment_reader).ch == '\n') {
/* 287 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 288 */               i = 0;
/*     */             }
/*     */           }
/*     */         int j;
/* 299 */         while (((JavadocTokenizer.DocReader)this.comment_reader).bp < ((JavadocTokenizer.DocReader)this.comment_reader).buflen) {
/* 300 */           j = ((JavadocTokenizer.DocReader)this.comment_reader).bp;
/* 301 */           char c = ((JavadocTokenizer.DocReader)this.comment_reader).ch;
/*     */ 
/* 306 */           while (((JavadocTokenizer.DocReader)this.comment_reader).bp < ((JavadocTokenizer.DocReader)this.comment_reader).buflen) {
/* 307 */             switch (((JavadocTokenizer.DocReader)this.comment_reader).ch) {
/*     */             case ' ':
/* 309 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 310 */               break;
/*     */             case '\t':
/* 312 */               ((JavadocTokenizer.DocReader)this.comment_reader).col = ((((JavadocTokenizer.DocReader)this.comment_reader).col - 1) / 8 * 8 + 8);
/* 313 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 314 */               break;
/*     */             case '\f':
/* 316 */               ((JavadocTokenizer.DocReader)this.comment_reader).col = 0;
/* 317 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 318 */               break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 342 */           if (((JavadocTokenizer.DocReader)this.comment_reader).ch == '*')
/*     */           {
/*     */             do
/* 345 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 346 */             while (((JavadocTokenizer.DocReader)this.comment_reader).ch == '*');
/*     */ 
/* 349 */             if (((JavadocTokenizer.DocReader)this.comment_reader).ch == '/')
/*     */             {
/* 352 */               break;
/*     */             }
/* 354 */           } else if (i == 0)
/*     */           {
/* 357 */             ((JavadocTokenizer.DocReader)this.comment_reader).bp = j;
/* 358 */             ((JavadocTokenizer.DocReader)this.comment_reader).ch = c;
/*     */           }
/*     */ 
/* 363 */           while (((JavadocTokenizer.DocReader)this.comment_reader).bp < ((JavadocTokenizer.DocReader)this.comment_reader).buflen) {
/* 364 */             switch (((JavadocTokenizer.DocReader)this.comment_reader).ch)
/*     */             {
/*     */             case '*':
/* 368 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 369 */               if (((JavadocTokenizer.DocReader)this.comment_reader).ch == '/')
/*     */               {
/*     */                 break label833;
/*     */               }
/*     */ 
/* 376 */               ((JavadocTokenizer.DocReader)this.comment_reader).putChar('*', false);
/* 377 */               break;
/*     */             case '\t':
/*     */             case ' ':
/* 380 */               ((JavadocTokenizer.DocReader)this.comment_reader).putChar(((JavadocTokenizer.DocReader)this.comment_reader).ch, false);
/* 381 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 382 */               break;
/*     */             case '\f':
/* 384 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 385 */               break;
/*     */             case '\r':
/* 387 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 388 */               if (((JavadocTokenizer.DocReader)this.comment_reader).ch != '\n')
/*     */               {
/* 390 */                 ((JavadocTokenizer.DocReader)this.comment_reader).putChar('\n', false);
/* 391 */               }break;
/*     */             case '\n':
/* 398 */               ((JavadocTokenizer.DocReader)this.comment_reader).putChar(((JavadocTokenizer.DocReader)this.comment_reader).ch, false);
/* 399 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/* 400 */               break;
/*     */             default:
/* 403 */               ((JavadocTokenizer.DocReader)this.comment_reader).putChar(((JavadocTokenizer.DocReader)this.comment_reader).ch, false);
/* 404 */               ((JavadocTokenizer.DocReader)this.comment_reader).scanCommentChar();
/*     */             }
/*     */           }
/* 407 */           i = 0;
/*     */         }
/*     */ 
/* 410 */         label833: if (((JavadocTokenizer.DocReader)this.comment_reader).sp > 0) {
/* 411 */           j = ((JavadocTokenizer.DocReader)this.comment_reader).sp - 1;
/*     */ 
/* 413 */           while (j > -1) {
/* 414 */             switch (((JavadocTokenizer.DocReader)this.comment_reader).sbuf[j]) {
/*     */             case '*':
/* 416 */               j--;
/* 417 */               break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 422 */           ((JavadocTokenizer.DocReader)this.comment_reader).sp = (j + 1);
/*     */ 
/* 425 */           this.docComment = ((JavadocTokenizer.DocReader)this.comment_reader).chars();
/* 426 */           this.docPosns = new int[((JavadocTokenizer.DocReader)this.comment_reader).pp];
/* 427 */           System.arraycopy(((JavadocTokenizer.DocReader)this.comment_reader).pbuf, 0, this.docPosns, 0, this.docPosns.length);
/*     */         } else {
/* 429 */           this.docComment = "";
/*     */         }
/*     */       } finally {
/* 432 */         this.scanned = true;
/* 433 */         this.comment_reader = null;
/* 434 */         if ((this.docComment != null) && 
/* 435 */           (this.docComment
/* 435 */           .matches("(?sm).*^\\s*@deprecated( |$).*")))
/*     */         {
/* 436 */           this.deprecatedFlag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.JavadocTokenizer
 * JD-Core Version:    0.6.2
 */