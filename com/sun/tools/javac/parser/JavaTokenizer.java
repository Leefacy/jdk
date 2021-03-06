/*     */ package com.sun.tools.javac.parser;
/*     */ 
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import com.sun.tools.javac.util.Position.LineMap;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ public class JavaTokenizer
/*     */ {
/*     */   private static final boolean scannerDebug = false;
/*     */   private boolean allowHexFloats;
/*     */   private boolean allowBinaryLiterals;
/*     */   private boolean allowUnderscoresInLiterals;
/*     */   private Source source;
/*     */   private final Log log;
/*     */   private final Tokens tokens;
/*     */   protected Tokens.TokenKind tk;
/*     */   protected int radix;
/*     */   protected Name name;
/*  86 */   protected int errPos = -1;
/*     */   protected UnicodeReader reader;
/*     */   protected ScannerFactory fac;
/*  94 */   private static final boolean hexFloatsWork = hexFloatsWork();
/*     */ 
/*     */   private static boolean hexFloatsWork() {
/*     */     try { Float.valueOf("0x1.0p1");
/*  98 */       return true; } catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   protected JavaTokenizer(ScannerFactory paramScannerFactory, CharBuffer paramCharBuffer)
/*     */   {
/* 115 */     this(paramScannerFactory, new UnicodeReader(paramScannerFactory, paramCharBuffer));
/*     */   }
/*     */ 
/*     */   protected JavaTokenizer(ScannerFactory paramScannerFactory, char[] paramArrayOfChar, int paramInt) {
/* 119 */     this(paramScannerFactory, new UnicodeReader(paramScannerFactory, paramArrayOfChar, paramInt));
/*     */   }
/*     */ 
/*     */   protected JavaTokenizer(ScannerFactory paramScannerFactory, UnicodeReader paramUnicodeReader) {
/* 123 */     this.fac = paramScannerFactory;
/* 124 */     this.log = paramScannerFactory.log;
/* 125 */     this.tokens = paramScannerFactory.tokens;
/* 126 */     this.source = paramScannerFactory.source;
/* 127 */     this.reader = paramUnicodeReader;
/* 128 */     this.allowBinaryLiterals = this.source.allowBinaryLiterals();
/* 129 */     this.allowHexFloats = this.source.allowHexFloats();
/* 130 */     this.allowUnderscoresInLiterals = this.source.allowUnderscoresInLiterals();
/*     */   }
/*     */ 
/*     */   protected void lexError(int paramInt, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 136 */     this.log.error(paramInt, paramString, paramArrayOfObject);
/* 137 */     this.tk = Tokens.TokenKind.ERROR;
/* 138 */     this.errPos = paramInt;
/*     */   }
/*     */ 
/*     */   private void scanLitChar(int paramInt)
/*     */   {
/* 144 */     if (this.reader.ch == '\\') {
/* 145 */       if ((this.reader.peekChar() == '\\') && (!this.reader.isUnicode())) {
/* 146 */         this.reader.skipChar();
/* 147 */         this.reader.putChar('\\', true);
/*     */       } else {
/* 149 */         this.reader.scanChar();
/* 150 */         switch (this.reader.ch) { case '0':
/*     */         case '1':
/*     */         case '2':
/*     */         case '3':
/*     */         case '4':
/*     */         case '5':
/*     */         case '6':
/*     */         case '7':
/* 153 */           int i = this.reader.ch;
/* 154 */           int j = this.reader.digit(paramInt, 8);
/* 155 */           this.reader.scanChar();
/* 156 */           if (('0' <= this.reader.ch) && (this.reader.ch <= '7')) {
/* 157 */             j = j * 8 + this.reader.digit(paramInt, 8);
/* 158 */             this.reader.scanChar();
/* 159 */             if ((i <= 51) && ('0' <= this.reader.ch) && (this.reader.ch <= '7')) {
/* 160 */               j = j * 8 + this.reader.digit(paramInt, 8);
/* 161 */               this.reader.scanChar();
/*     */             }
/*     */           }
/* 164 */           this.reader.putChar((char)j);
/* 165 */           break;
/*     */         case 'b':
/* 167 */           this.reader.putChar('\b', true); break;
/*     */         case 't':
/* 169 */           this.reader.putChar('\t', true); break;
/*     */         case 'n':
/* 171 */           this.reader.putChar('\n', true); break;
/*     */         case 'f':
/* 173 */           this.reader.putChar('\f', true); break;
/*     */         case 'r':
/* 175 */           this.reader.putChar('\r', true); break;
/*     */         case '\'':
/* 177 */           this.reader.putChar('\'', true); break;
/*     */         case '"':
/* 179 */           this.reader.putChar('"', true); break;
/*     */         case '\\':
/* 181 */           this.reader.putChar('\\', true); break;
/*     */         default:
/* 183 */           lexError(this.reader.bp, "illegal.esc.char", new Object[0]); break; }
/*     */       }
/*     */     }
/* 186 */     else if (this.reader.bp != this.reader.buflen)
/* 187 */       this.reader.putChar(true);
/*     */   }
/*     */ 
/*     */   private void scanDigits(int paramInt1, int paramInt2) {
/*     */     int i;
/*     */     int j;
/*     */     do {
/* 195 */       if (this.reader.ch != '_') {
/* 196 */         this.reader.putChar(false);
/*     */       }
/* 198 */       else if (!this.allowUnderscoresInLiterals) {
/* 199 */         lexError(paramInt1, "unsupported.underscore.lit", new Object[] { this.source.name });
/* 200 */         this.allowUnderscoresInLiterals = true;
/*     */       }
/*     */ 
/* 203 */       i = this.reader.ch;
/* 204 */       j = this.reader.bp;
/* 205 */       this.reader.scanChar();
/* 206 */     }while ((this.reader.digit(paramInt1, paramInt2) >= 0) || (this.reader.ch == '_'));
/* 207 */     if (i == 95)
/* 208 */       lexError(j, "illegal.underscore", new Object[0]);
/*     */   }
/*     */ 
/*     */   private void scanHexExponentAndSuffix(int paramInt)
/*     */   {
/* 214 */     if ((this.reader.ch == 'p') || (this.reader.ch == 'P')) {
/* 215 */       this.reader.putChar(true);
/* 216 */       skipIllegalUnderscores();
/* 217 */       if ((this.reader.ch == '+') || (this.reader.ch == '-')) {
/* 218 */         this.reader.putChar(true);
/*     */       }
/* 220 */       skipIllegalUnderscores();
/* 221 */       if (('0' <= this.reader.ch) && (this.reader.ch <= '9')) {
/* 222 */         scanDigits(paramInt, 10);
/* 223 */         if (!this.allowHexFloats) {
/* 224 */           lexError(paramInt, "unsupported.fp.lit", new Object[] { this.source.name });
/* 225 */           this.allowHexFloats = true;
/*     */         }
/* 227 */         else if (!hexFloatsWork) {
/* 228 */           lexError(paramInt, "unsupported.cross.fp.lit", new Object[0]);
/*     */         }
/*     */       } else { lexError(paramInt, "malformed.fp.lit", new Object[0]); }
/*     */     } else {
/* 232 */       lexError(paramInt, "malformed.fp.lit", new Object[0]);
/*     */     }
/* 234 */     if ((this.reader.ch == 'f') || (this.reader.ch == 'F')) {
/* 235 */       this.reader.putChar(true);
/* 236 */       this.tk = Tokens.TokenKind.FLOATLITERAL;
/* 237 */       this.radix = 16;
/*     */     } else {
/* 239 */       if ((this.reader.ch == 'd') || (this.reader.ch == 'D')) {
/* 240 */         this.reader.putChar(true);
/*     */       }
/* 242 */       this.tk = Tokens.TokenKind.DOUBLELITERAL;
/* 243 */       this.radix = 16;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scanFraction(int paramInt)
/*     */   {
/* 250 */     skipIllegalUnderscores();
/* 251 */     if (('0' <= this.reader.ch) && (this.reader.ch <= '9')) {
/* 252 */       scanDigits(paramInt, 10);
/*     */     }
/* 254 */     int i = this.reader.sp;
/* 255 */     if ((this.reader.ch == 'e') || (this.reader.ch == 'E')) {
/* 256 */       this.reader.putChar(true);
/* 257 */       skipIllegalUnderscores();
/* 258 */       if ((this.reader.ch == '+') || (this.reader.ch == '-')) {
/* 259 */         this.reader.putChar(true);
/*     */       }
/* 261 */       skipIllegalUnderscores();
/* 262 */       if (('0' <= this.reader.ch) && (this.reader.ch <= '9')) {
/* 263 */         scanDigits(paramInt, 10);
/* 264 */         return;
/*     */       }
/* 266 */       lexError(paramInt, "malformed.fp.lit", new Object[0]);
/* 267 */       this.reader.sp = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scanFractionAndSuffix(int paramInt)
/*     */   {
/* 274 */     this.radix = 10;
/* 275 */     scanFraction(paramInt);
/* 276 */     if ((this.reader.ch == 'f') || (this.reader.ch == 'F')) {
/* 277 */       this.reader.putChar(true);
/* 278 */       this.tk = Tokens.TokenKind.FLOATLITERAL;
/*     */     } else {
/* 280 */       if ((this.reader.ch == 'd') || (this.reader.ch == 'D')) {
/* 281 */         this.reader.putChar(true);
/*     */       }
/* 283 */       this.tk = Tokens.TokenKind.DOUBLELITERAL;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scanHexFractionAndSuffix(int paramInt, boolean paramBoolean)
/*     */   {
/* 290 */     this.radix = 16;
/* 291 */     Assert.check(this.reader.ch == '.');
/* 292 */     this.reader.putChar(true);
/* 293 */     skipIllegalUnderscores();
/* 294 */     if (this.reader.digit(paramInt, 16) >= 0) {
/* 295 */       paramBoolean = true;
/* 296 */       scanDigits(paramInt, 16);
/*     */     }
/* 298 */     if (!paramBoolean)
/* 299 */       lexError(paramInt, "invalid.hex.number", new Object[0]);
/*     */     else
/* 301 */       scanHexExponentAndSuffix(paramInt);
/*     */   }
/*     */ 
/*     */   private void skipIllegalUnderscores() {
/* 305 */     if (this.reader.ch == '_') {
/* 306 */       lexError(this.reader.bp, "illegal.underscore", new Object[0]);
/* 307 */       while (this.reader.ch == '_')
/* 308 */         this.reader.scanChar();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scanNumber(int paramInt1, int paramInt2)
/*     */   {
/* 317 */     this.radix = paramInt2;
/* 318 */     int i = paramInt2 == 8 ? 10 : paramInt2;
/* 319 */     boolean bool = false;
/* 320 */     if (this.reader.digit(paramInt1, i) >= 0) {
/* 321 */       bool = true;
/* 322 */       scanDigits(paramInt1, i);
/*     */     }
/* 324 */     if ((paramInt2 == 16) && (this.reader.ch == '.')) {
/* 325 */       scanHexFractionAndSuffix(paramInt1, bool);
/* 326 */     } else if ((bool) && (paramInt2 == 16) && ((this.reader.ch == 'p') || (this.reader.ch == 'P'))) {
/* 327 */       scanHexExponentAndSuffix(paramInt1);
/* 328 */     } else if ((i == 10) && (this.reader.ch == '.')) {
/* 329 */       this.reader.putChar(true);
/* 330 */       scanFractionAndSuffix(paramInt1);
/* 331 */     } else if ((i == 10) && ((this.reader.ch == 'e') || (this.reader.ch == 'E') || (this.reader.ch == 'f') || (this.reader.ch == 'F') || (this.reader.ch == 'd') || (this.reader.ch == 'D')))
/*     */     {
/* 335 */       scanFractionAndSuffix(paramInt1);
/*     */     }
/* 337 */     else if ((this.reader.ch == 'l') || (this.reader.ch == 'L')) {
/* 338 */       this.reader.scanChar();
/* 339 */       this.tk = Tokens.TokenKind.LONGLITERAL;
/*     */     } else {
/* 341 */       this.tk = Tokens.TokenKind.INTLITERAL;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scanIdent()
/*     */   {
/* 351 */     this.reader.putChar(true);
/*     */     while (true)
/* 353 */       switch (this.reader.ch) { case '$':
/*     */       case '0':
/*     */       case '1':
/*     */       case '2':
/*     */       case '3':
/*     */       case '4':
/*     */       case '5':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/*     */       case 'A':
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'E':
/*     */       case 'F':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'I':
/*     */       case 'J':
/*     */       case 'K':
/*     */       case 'L':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'S':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       case 'Z':
/*     */       case '_':
/*     */       case 'a':
/*     */       case 'b':
/*     */       case 'c':
/*     */       case 'd':
/*     */       case 'e':
/*     */       case 'f':
/*     */       case 'g':
/*     */       case 'h':
/*     */       case 'i':
/*     */       case 'j':
/*     */       case 'k':
/*     */       case 'l':
/*     */       case 'm':
/*     */       case 'n':
/*     */       case 'o':
/*     */       case 'p':
/*     */       case 'q':
/*     */       case 'r':
/*     */       case 's':
/*     */       case 't':
/*     */       case 'u':
/*     */       case 'v':
/*     */       case 'w':
/*     */       case 'x':
/*     */       case 'y':
/*     */       case 'z':
/* 369 */         break;
/*     */       case '\000':
/*     */       case '\001':
/*     */       case '\002':
/*     */       case '\003':
/*     */       case '\004':
/*     */       case '\005':
/*     */       case '\006':
/*     */       case '\007':
/*     */       case '\b':
/*     */       case '\016':
/*     */       case '\017':
/*     */       case '\020':
/*     */       case '\021':
/*     */       case '\022':
/*     */       case '\023':
/*     */       case '\024':
/*     */       case '\025':
/*     */       case '\026':
/*     */       case '\027':
/*     */       case '\030':
/*     */       case '\031':
/*     */       case '\033':
/*     */       case '':
/* 377 */         this.reader.scanChar();
/* 378 */         break;
/*     */       case '\032':
/* 380 */         if (this.reader.bp >= this.reader.buflen) {
/* 381 */           this.name = this.reader.name();
/* 382 */           this.tk = this.tokens.lookupKind(this.name);
/* 383 */           return;
/*     */         }
/* 385 */         this.reader.scanChar();
/* 386 */         break;
/*     */       case '\t':
/*     */       case '\n':
/*     */       case '\013':
/*     */       case '\f':
/*     */       case '\r':
/*     */       case '\034':
/*     */       case '\035':
/*     */       case '\036':
/*     */       case '\037':
/*     */       case ' ':
/*     */       case '!':
/*     */       case '"':
/*     */       case '#':
/*     */       case '%':
/*     */       case '&':
/*     */       case '\'':
/*     */       case '(':
/*     */       case ')':
/*     */       case '*':
/*     */       case '+':
/*     */       case ',':
/*     */       case '-':
/*     */       case '.':
/*     */       case '/':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case '[':
/*     */       case '\\':
/*     */       case ']':
/*     */       case '^':
/*     */       case '`':
/*     */       case '{':
/*     */       case '|':
/*     */       case '}':
/*     */       case '~':
/*     */       default:
/*     */         boolean bool;
/* 388 */         if (this.reader.ch < '')
/*     */         {
/* 390 */           bool = false;
/*     */         } else {
/* 392 */           if (Character.isIdentifierIgnorable(this.reader.ch)) {
/* 393 */             this.reader.scanChar();
/* 394 */             continue;
/*     */           }
/* 396 */           char c = this.reader.scanSurrogates();
/* 397 */           if (c != 0) {
/* 398 */             this.reader.putChar(c);
/* 399 */             bool = Character.isJavaIdentifierPart(
/* 400 */               Character.toCodePoint(c, this.reader.ch));
/*     */           }
/*     */           else {
/* 402 */             bool = Character.isJavaIdentifierPart(this.reader.ch);
/*     */           }
/*     */         }
/*     */ 
/* 406 */         if (!bool) {
/* 407 */           this.name = this.reader.name();
/* 408 */           this.tk = this.tokens.lookupKind(this.name);
/* 409 */           return;
/*     */         }
/*     */ 
/* 412 */         this.reader.putChar(true);
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean isSpecial(char paramChar)
/*     */   {
/* 419 */     switch (paramChar) { case '!':
/*     */     case '%':
/*     */     case '&':
/*     */     case '*':
/*     */     case '+':
/*     */     case '-':
/*     */     case ':':
/*     */     case '<':
/*     */     case '=':
/*     */     case '>':
/*     */     case '?':
/*     */     case '@':
/*     */     case '^':
/*     */     case '|':
/*     */     case '~':
/* 424 */       return true;
/*     */     }
/* 426 */     return false;
/*     */   }
/*     */ 
/*     */   private void scanOperator()
/*     */   {
/*     */     while (true)
/*     */     {
/* 435 */       this.reader.putChar(false);
/* 436 */       Name localName = this.reader.name();
/* 437 */       Tokens.TokenKind localTokenKind = this.tokens.lookupKind(localName);
/* 438 */       if (localTokenKind == Tokens.TokenKind.IDENTIFIER) {
/* 439 */         this.reader.sp -= 1;
/*     */       }
/*     */       else {
/* 442 */         this.tk = localTokenKind;
/* 443 */         this.reader.scanChar();
/* 444 */         if (!isSpecial(this.reader.ch))
/*     */           break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Tokens.Token readToken()
/*     */   {
/* 452 */     this.reader.sp = 0;
/* 453 */     this.name = null;
/* 454 */     this.radix = 0;
/*     */ 
/* 456 */     int i = 0;
/* 457 */     int j = 0;
/* 458 */     List localList = null;
/*     */     try {
/*     */       int k;
/*     */       while (true) {
/* 462 */         i = this.reader.bp;
/* 463 */         switch (this.reader.ch) {
/*     */         case '\t':
/*     */         case '\f':
/*     */         case ' ':
/*     */           do
/* 468 */             this.reader.scanChar();
/* 469 */           while ((this.reader.ch == ' ') || (this.reader.ch == '\t') || (this.reader.ch == '\f'));
/* 470 */           processWhiteSpace(i, this.reader.bp);
/* 471 */           break;
/*     */         case '\n':
/* 473 */           this.reader.scanChar();
/* 474 */           processLineTerminator(i, this.reader.bp);
/* 475 */           break;
/*     */         case '\r':
/* 477 */           this.reader.scanChar();
/* 478 */           if (this.reader.ch == '\n') {
/* 479 */             this.reader.scanChar();
/*     */           }
/* 481 */           processLineTerminator(i, this.reader.bp);
/* 482 */           break;
/*     */         case '$':
/*     */         case 'A':
/*     */         case 'B':
/*     */         case 'C':
/*     */         case 'D':
/*     */         case 'E':
/*     */         case 'F':
/*     */         case 'G':
/*     */         case 'H':
/*     */         case 'I':
/*     */         case 'J':
/*     */         case 'K':
/*     */         case 'L':
/*     */         case 'M':
/*     */         case 'N':
/*     */         case 'O':
/*     */         case 'P':
/*     */         case 'Q':
/*     */         case 'R':
/*     */         case 'S':
/*     */         case 'T':
/*     */         case 'U':
/*     */         case 'V':
/*     */         case 'W':
/*     */         case 'X':
/*     */         case 'Y':
/*     */         case 'Z':
/*     */         case '_':
/*     */         case 'a':
/*     */         case 'b':
/*     */         case 'c':
/*     */         case 'd':
/*     */         case 'e':
/*     */         case 'f':
/*     */         case 'g':
/*     */         case 'h':
/*     */         case 'i':
/*     */         case 'j':
/*     */         case 'k':
/*     */         case 'l':
/*     */         case 'm':
/*     */         case 'n':
/*     */         case 'o':
/*     */         case 'p':
/*     */         case 'q':
/*     */         case 'r':
/*     */         case 's':
/*     */         case 't':
/*     */         case 'u':
/*     */         case 'v':
/*     */         case 'w':
/*     */         case 'x':
/*     */         case 'y':
/*     */         case 'z':
/* 496 */           scanIdent();
/* 497 */           break;
/*     */         case '0':
/* 499 */           this.reader.scanChar();
/* 500 */           if ((this.reader.ch == 'x') || (this.reader.ch == 'X')) {
/* 501 */             this.reader.scanChar();
/* 502 */             skipIllegalUnderscores();
/* 503 */             if (this.reader.ch == '.')
/* 504 */               scanHexFractionAndSuffix(i, false);
/* 505 */             else if (this.reader.digit(i, 16) < 0)
/* 506 */               lexError(i, "invalid.hex.number", new Object[0]);
/*     */             else
/* 508 */               scanNumber(i, 16);
/*     */           }
/* 510 */           else if ((this.reader.ch == 'b') || (this.reader.ch == 'B')) {
/* 511 */             if (!this.allowBinaryLiterals) {
/* 512 */               lexError(i, "unsupported.binary.lit", new Object[] { this.source.name });
/* 513 */               this.allowBinaryLiterals = true;
/*     */             }
/* 515 */             this.reader.scanChar();
/* 516 */             skipIllegalUnderscores();
/* 517 */             if (this.reader.digit(i, 2) < 0)
/* 518 */               lexError(i, "invalid.binary.number", new Object[0]);
/*     */             else
/* 520 */               scanNumber(i, 2);
/*     */           }
/*     */           else {
/* 523 */             this.reader.putChar('0');
/* 524 */             if (this.reader.ch == '_') {
/* 525 */               k = this.reader.bp;
/*     */               do
/* 527 */                 this.reader.scanChar();
/* 528 */               while (this.reader.ch == '_');
/* 529 */               if (this.reader.digit(i, 10) < 0) {
/* 530 */                 lexError(k, "illegal.underscore", new Object[0]);
/*     */               }
/*     */             }
/* 533 */             scanNumber(i, 8);
/*     */           }
/* 535 */           break;
/*     */         case '1':
/*     */         case '2':
/*     */         case '3':
/*     */         case '4':
/*     */         case '5':
/*     */         case '6':
/*     */         case '7':
/*     */         case '8':
/*     */         case '9':
/* 538 */           scanNumber(i, 10);
/* 539 */           break;
/*     */         case '.':
/* 541 */           this.reader.scanChar();
/* 542 */           if (('0' <= this.reader.ch) && (this.reader.ch <= '9')) {
/* 543 */             this.reader.putChar('.');
/* 544 */             scanFractionAndSuffix(i);
/* 545 */           } else if (this.reader.ch == '.') {
/* 546 */             k = this.reader.bp;
/* 547 */             this.reader.putChar('.'); this.reader.putChar('.', true);
/* 548 */             if (this.reader.ch == '.') {
/* 549 */               this.reader.scanChar();
/* 550 */               this.reader.putChar('.');
/* 551 */               this.tk = Tokens.TokenKind.ELLIPSIS;
/*     */             } else {
/* 553 */               lexError(k, "illegal.dot", new Object[0]);
/*     */             }
/*     */           } else {
/* 556 */             this.tk = Tokens.TokenKind.DOT;
/*     */           }
/* 558 */           break;
/*     */         case ',':
/* 560 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.COMMA; break;
/*     */         case ';':
/* 562 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.SEMI; break;
/*     */         case '(':
/* 564 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.LPAREN; break;
/*     */         case ')':
/* 566 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.RPAREN; break;
/*     */         case '[':
/* 568 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.LBRACKET; break;
/*     */         case ']':
/* 570 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.RBRACKET; break;
/*     */         case '{':
/* 572 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.LBRACE; break;
/*     */         case '}':
/* 574 */           this.reader.scanChar(); this.tk = Tokens.TokenKind.RBRACE; break;
/*     */         case '/':
/* 576 */           this.reader.scanChar();
/* 577 */           if (this.reader.ch == '/') {
/*     */             do
/* 579 */               this.reader.scanCommentChar();
/* 580 */             while ((this.reader.ch != '\r') && (this.reader.ch != '\n') && (this.reader.bp < this.reader.buflen));
/* 581 */             if (this.reader.bp < this.reader.buflen)
/* 582 */               localList = addComment(localList, processComment(i, this.reader.bp, Tokens.Comment.CommentStyle.LINE));
/*     */           }
/*     */           else {
/* 585 */             if (this.reader.ch != '*') break label1531;
/* 586 */             k = 0;
/* 587 */             this.reader.scanChar();
/*     */             Tokens.Comment.CommentStyle localCommentStyle;
/* 589 */             if (this.reader.ch == '*') {
/* 590 */               localCommentStyle = Tokens.Comment.CommentStyle.JAVADOC;
/* 591 */               this.reader.scanCommentChar();
/* 592 */               if (this.reader.ch == '/')
/* 593 */                 k = 1;
/*     */             }
/*     */             else {
/* 596 */               localCommentStyle = Tokens.Comment.CommentStyle.BLOCK;
/*     */             }
/* 598 */             while ((k == 0) && (this.reader.bp < this.reader.buflen)) {
/* 599 */               if (this.reader.ch == '*') {
/* 600 */                 this.reader.scanChar();
/* 601 */                 if (this.reader.ch == '/') break; 
/*     */               }
/* 603 */               else { this.reader.scanCommentChar(); }
/*     */ 
/*     */             }
/* 606 */             if (this.reader.ch != '/') break label1517;
/* 607 */             this.reader.scanChar();
/* 608 */             localList = addComment(localList, processComment(i, this.reader.bp, localCommentStyle)); } break;
/*     */         case '\'':
/*     */         case '"':
/*     */         case '\013':
/*     */         case '\016':
/*     */         case '\017':
/*     */         case '\020':
/*     */         case '\021':
/*     */         case '\022':
/*     */         case '\023':
/*     */         case '\024':
/*     */         case '\025':
/*     */         case '\026':
/*     */         case '\027':
/*     */         case '\030':
/*     */         case '\031':
/*     */         case '\032':
/*     */         case '\033':
/*     */         case '\034':
/*     */         case '\035':
/*     */         case '\036':
/*     */         case '\037':
/*     */         case '!':
/*     */         case '#':
/*     */         case '%':
/*     */         case '&':
/*     */         case '*':
/*     */         case '+':
/*     */         case '-':
/*     */         case ':':
/*     */         case '<':
/*     */         case '=':
/*     */         case '>':
/*     */         case '?':
/*     */         case '@':
/*     */         case '\\':
/*     */         case '^':
/*     */         case '`':
/* 611 */         case '|': }  } label1517: lexError(i, "unclosed.comment", new Object[0]);
/* 612 */       break label2078;
/*     */ 
/* 614 */       label1531: if (this.reader.ch == '=') {
/* 615 */         this.tk = Tokens.TokenKind.SLASHEQ;
/* 616 */         this.reader.scanChar();
/*     */       } else {
/* 618 */         this.tk = Tokens.TokenKind.SLASH;
/*     */ 
/* 620 */         break label2078;
/*     */ 
/* 622 */         this.reader.scanChar();
/* 623 */         if (this.reader.ch == '\'') {
/* 624 */           lexError(i, "empty.char.lit", new Object[0]);
/*     */         } else {
/* 626 */           if ((this.reader.ch == '\r') || (this.reader.ch == '\n'))
/* 627 */             lexError(i, "illegal.line.end.in.char.lit", new Object[0]);
/* 628 */           scanLitChar(i);
/* 629 */           k = this.reader.ch;
/* 630 */           if (this.reader.ch == '\'') {
/* 631 */             this.reader.scanChar();
/* 632 */             this.tk = Tokens.TokenKind.CHARLITERAL;
/*     */           } else {
/* 634 */             lexError(i, "unclosed.char.lit", new Object[0]);
/*     */           }
/*     */ 
/* 637 */           break label2078;
/*     */ 
/* 639 */           this.reader.scanChar();
/* 640 */           while ((this.reader.ch != '"') && (this.reader.ch != '\r') && (this.reader.ch != '\n') && (this.reader.bp < this.reader.buflen))
/* 641 */             scanLitChar(i);
/* 642 */           if (this.reader.ch == '"') {
/* 643 */             this.tk = Tokens.TokenKind.STRINGLITERAL;
/* 644 */             this.reader.scanChar();
/*     */           } else {
/* 646 */             lexError(i, "unclosed.str.lit", new Object[0]);
/*     */ 
/* 648 */             break label2078;
/*     */ 
/* 650 */             if (isSpecial(this.reader.ch)) {
/* 651 */               scanOperator();
/*     */             }
/*     */             else
/*     */             {
/*     */               boolean bool;
/* 654 */               if (this.reader.ch < '')
/*     */               {
/* 656 */                 k = 0;
/*     */               } else {
/* 658 */                 char c = this.reader.scanSurrogates();
/* 659 */                 if (c != 0) {
/* 660 */                   this.reader.putChar(c);
/*     */ 
/* 662 */                   bool = Character.isJavaIdentifierStart(
/* 663 */                     Character.toCodePoint(c, this.reader.ch));
/*     */                 }
/*     */                 else {
/* 665 */                   bool = Character.isJavaIdentifierStart(this.reader.ch);
/*     */                 }
/*     */               }
/* 668 */               if (bool) {
/* 669 */                 scanIdent();
/* 670 */               } else if ((this.reader.bp == this.reader.buflen) || ((this.reader.ch == '\032') && (this.reader.bp + 1 == this.reader.buflen))) {
/* 671 */                 this.tk = Tokens.TokenKind.EOF;
/* 672 */                 i = this.reader.buflen;
/*     */               }
/*     */               else
/*     */               {
/* 676 */                 String str = (' ' < this.reader.ch) && (this.reader.ch < '') ? 
/* 675 */                   String.format("%s", new Object[] { 
/* 675 */                   Character.valueOf(this.reader.ch) }) : 
/* 676 */                   String.format("\\u%04x", new Object[] { 
/* 676 */                   Integer.valueOf(this.reader.ch) });
/*     */ 
/* 677 */                 lexError(i, "illegal.char", new Object[] { str });
/* 678 */                 this.reader.scanChar();
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 684 */       label2078: j = this.reader.bp;
/*     */       Object localObject1;
/* 685 */       switch (1.$SwitchMap$com$sun$tools$javac$parser$Tokens$Token$Tag[this.tk.tag.ordinal()]) { case 1:
/* 686 */         localObject1 = new Tokens.Token(this.tk, i, j, localList); return localObject1;
/*     */       case 2:
/* 687 */         localObject1 = new Tokens.NamedToken(this.tk, i, j, this.name, localList); return localObject1;
/*     */       case 3:
/* 688 */         localObject1 = new Tokens.StringToken(this.tk, i, j, this.reader.chars(), localList); return localObject1;
/*     */       case 4:
/* 689 */         localObject1 = new Tokens.NumericToken(this.tk, i, j, this.reader.chars(), this.radix, localList); return localObject1; }
/* 690 */       throw new AssertionError();
/*     */     }
/*     */     finally
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   List<Tokens.Comment> addComment(List<Tokens.Comment> paramList, Tokens.Comment paramComment)
/*     */   {
/* 706 */     return paramList == null ? 
/* 705 */       List.of(paramComment) : 
/* 705 */       paramList
/* 706 */       .prepend(paramComment);
/*     */   }
/*     */ 
/*     */   public int errPos()
/*     */   {
/* 712 */     return this.errPos;
/*     */   }
/*     */ 
/*     */   public void errPos(int paramInt)
/*     */   {
/* 718 */     this.errPos = paramInt;
/*     */   }
/*     */ 
/*     */   protected Tokens.Comment processComment(int paramInt1, int paramInt2, Tokens.Comment.CommentStyle paramCommentStyle)
/*     */   {
/* 731 */     char[] arrayOfChar = this.reader.getRawCharacters(paramInt1, paramInt2);
/* 732 */     return new BasicComment(new UnicodeReader(this.fac, arrayOfChar, arrayOfChar.length), paramCommentStyle);
/*     */   }
/*     */ 
/*     */   protected void processWhiteSpace(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void processLineTerminator(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Position.LineMap getLineMap()
/*     */   {
/* 763 */     return Position.makeLineMap(this.reader.getRawCharacters(), this.reader.buflen, false);
/*     */   }
/*     */ 
/*     */   protected static class BasicComment<U extends UnicodeReader>
/*     */     implements Tokens.Comment
/*     */   {
/*     */     Tokens.Comment.CommentStyle cs;
/*     */     U comment_reader;
/* 778 */     protected boolean deprecatedFlag = false;
/* 779 */     protected boolean scanned = false;
/*     */ 
/*     */     protected BasicComment(U paramU, Tokens.Comment.CommentStyle paramCommentStyle) {
/* 782 */       this.comment_reader = paramU;
/* 783 */       this.cs = paramCommentStyle;
/*     */     }
/*     */ 
/*     */     public String getText() {
/* 787 */       return null;
/*     */     }
/*     */ 
/*     */     public int getSourcePos(int paramInt) {
/* 791 */       return -1;
/*     */     }
/*     */ 
/*     */     public Tokens.Comment.CommentStyle getStyle() {
/* 795 */       return this.cs;
/*     */     }
/*     */ 
/*     */     public boolean isDeprecated() {
/* 799 */       if ((!this.scanned) && (this.cs == Tokens.Comment.CommentStyle.JAVADOC)) {
/* 800 */         scanDocComment();
/*     */       }
/* 802 */       return this.deprecatedFlag;
/*     */     }
/*     */ 
/*     */     protected void scanDocComment()
/*     */     {
/*     */       try {
/* 808 */         int i = 0;
/*     */ 
/* 810 */         this.comment_reader.bp += 3;
/* 811 */         this.comment_reader.ch = this.comment_reader.buf[this.comment_reader.bp];
/*     */ 
/* 814 */         label509: while (this.comment_reader.bp < this.comment_reader.buflen)
/*     */         {
/* 817 */           while ((this.comment_reader.bp < this.comment_reader.buflen) && ((this.comment_reader.ch == ' ') || (this.comment_reader.ch == '\t') || (this.comment_reader.ch == '\f'))) {
/* 818 */             this.comment_reader.scanCommentChar();
/*     */           }
/*     */ 
/* 822 */           while ((this.comment_reader.bp < this.comment_reader.buflen) && (this.comment_reader.ch == '*')) {
/* 823 */             this.comment_reader.scanCommentChar();
/* 824 */             if (this.comment_reader.ch == '/') {
/* 825 */               return;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 830 */           while ((this.comment_reader.bp < this.comment_reader.buflen) && ((this.comment_reader.ch == ' ') || (this.comment_reader.ch == '\t') || (this.comment_reader.ch == '\f'))) {
/* 831 */             this.comment_reader.scanCommentChar();
/*     */           }
/*     */ 
/* 834 */           i = 0;
/*     */ 
/* 836 */           if (!this.deprecatedFlag) {
/* 837 */             String str = "@deprecated";
/* 838 */             int j = 0;
/* 839 */             while ((this.comment_reader.bp < this.comment_reader.buflen) && (this.comment_reader.ch == str.charAt(j))) {
/* 840 */               this.comment_reader.scanCommentChar();
/* 841 */               j++;
/* 842 */               if (j == str.length()) {
/* 843 */                 i = 1;
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 849 */           if ((i != 0) && (this.comment_reader.bp < this.comment_reader.buflen)) {
/* 850 */             if (Character.isWhitespace(this.comment_reader.ch)) {
/* 851 */               this.deprecatedFlag = true;
/* 852 */             } else if (this.comment_reader.ch == '*') {
/* 853 */               this.comment_reader.scanCommentChar();
/* 854 */               if (this.comment_reader.ch == '/') {
/* 855 */                 this.deprecatedFlag = true;
/* 856 */                 return;
/*     */               }
/*     */             }
/*     */           }
/*     */           while (true)
/*     */           {
/* 862 */             if (this.comment_reader.bp >= this.comment_reader.buflen) break label509;
/* 863 */             switch (this.comment_reader.ch) {
/*     */             case '*':
/* 865 */               this.comment_reader.scanCommentChar();
/* 866 */               if (this.comment_reader.ch == '/') {
/* 867 */                 return;
/*     */               }
/*     */               break;
/*     */             case '\r':
/* 871 */               this.comment_reader.scanCommentChar();
/* 872 */               if (this.comment_reader.ch != '\n')
/*     */               {
/*     */                 break;
/*     */               }
/*     */             case '\n':
/* 877 */               this.comment_reader.scanCommentChar();
/* 878 */               break;
/*     */             default:
/* 880 */               this.comment_reader.scanCommentChar();
/*     */             }
/*     */           }
/*     */         }
/* 884 */         return;
/*     */       } finally {
/* 886 */         this.scanned = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.JavaTokenizer
 * JD-Core Version:    0.6.2
 */