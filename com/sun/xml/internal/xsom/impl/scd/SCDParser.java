/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ 
/*     */ public class SCDParser
/*     */   implements SCDParserConstants
/*     */ {
/*     */   private NamespaceContext nsc;
/*     */   public SCDParserTokenManager token_source;
/*     */   SimpleCharStream jj_input_stream;
/*     */   public Token token;
/*     */   public Token jj_nt;
/*     */   private int jj_ntk;
/*     */   private int jj_gen;
/* 406 */   private final int[] jj_la1 = new int[19];
/*     */   private static int[] jj_la1_0;
/*     */   private static int[] jj_la1_1;
/* 516 */   private Vector jj_expentries = new Vector();
/*     */   private int[] jj_expentry;
/* 518 */   private int jj_kind = -1;
/*     */ 
/*     */   public SCDParser(String text, NamespaceContext nsc)
/*     */   {
/*  37 */     this(new StringReader(text));
/*  38 */     this.nsc = nsc;
/*     */   }
/*     */   private String trim(String s) {
/*  41 */     return s.substring(1, s.length() - 1);
/*     */   }
/*     */   private String resolvePrefix(String prefix) throws ParseException {
/*     */     try {
/*  45 */       String r = this.nsc.getNamespaceURI(prefix);
/*     */ 
/*  47 */       if (prefix.equals(""))
/*  48 */         return r;
/*  49 */       if (!r.equals(""))
/*  50 */         return r;
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/*  54 */     throw new ParseException("Unbound prefix: " + prefix);
/*     */   }
/*     */ 
/*     */   public final UName QName() throws ParseException
/*     */   {
/*  59 */     Token l = null;
/*  60 */     Token p = jj_consume_token(12);
/*  61 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */     case 15:
/*  63 */       jj_consume_token(15);
/*  64 */       l = jj_consume_token(12);
/*  65 */       break;
/*     */     default:
/*  67 */       this.jj_la1[0] = this.jj_gen;
/*     */     }
/*     */ 
/*  70 */     if (l == null) {
/*  71 */       return new UName(resolvePrefix(""), p.image);
/*     */     }
/*  73 */     return new UName(resolvePrefix(p.image), l.image);
/*     */   }
/*     */ 
/*     */   public final String Prefix()
/*     */     throws ParseException
/*     */   {
/*  79 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */     case 12:
/*  81 */       Token p = jj_consume_token(12);
/*  82 */       return resolvePrefix(p.image);
/*     */     }
/*     */ 
/*  85 */     this.jj_la1[1] = this.jj_gen;
/*  86 */     return resolvePrefix("");
/*     */   }
/*     */ 
/*     */   public final List RelativeSchemaComponentPath()
/*     */     throws ParseException
/*     */   {
/*  92 */     List steps = new ArrayList();
/*     */ 
/*  94 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */     case 16:
/*     */     case 17:
/*  97 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 16:
/*  99 */         jj_consume_token(16);
/* 100 */         steps.add(new Step.Any(Axis.ROOT));
/* 101 */         break;
/*     */       case 17:
/* 103 */         jj_consume_token(17);
/* 104 */         steps.add(new Step.Any(Axis.DESCENDANTS));
/* 105 */         break;
/*     */       default:
/* 107 */         this.jj_la1[2] = this.jj_gen;
/* 108 */         jj_consume_token(-1);
/* 109 */         throw new ParseException();
/*     */       }
/*     */       break;
/*     */     default:
/* 113 */       this.jj_la1[3] = this.jj_gen;
/*     */     }
/*     */ 
/* 116 */     Step s = Step();
/* 117 */     steps.add(s);
/*     */     while (true)
/*     */     {
/* 120 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*     */       {
/*     */       case 16:
/*     */       case 17:
/* 124 */         break;
/*     */       default:
/* 126 */         this.jj_la1[4] = this.jj_gen;
/* 127 */         break;
/*     */       }
/* 129 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 16:
/* 131 */         jj_consume_token(16);
/* 132 */         break;
/*     */       case 17:
/* 134 */         jj_consume_token(17);
/* 135 */         steps.add(new Step.Any(Axis.DESCENDANTS));
/* 136 */         break;
/*     */       default:
/* 138 */         this.jj_la1[5] = this.jj_gen;
/* 139 */         jj_consume_token(-1);
/* 140 */         throw new ParseException();
/*     */       }
/* 142 */       s = Step();
/* 143 */       steps.add(s);
/*     */     }
/* 145 */     return steps;
/*     */   }
/*     */ 
/*     */   public final Step Step()
/*     */     throws ParseException
/*     */   {
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/*     */     Step s;
/* 151 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */     case 18:
/*     */     case 19:
/* 154 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 18:
/* 156 */         jj_consume_token(18);
/* 157 */         break;
/*     */       case 19:
/* 159 */         jj_consume_token(19);
/* 160 */         break;
/*     */       default:
/* 162 */         this.jj_la1[6] = this.jj_gen;
/* 163 */         jj_consume_token(-1);
/* 164 */         throw new ParseException();
/*     */       }
/* 166 */       s = NameOrWildcard(Axis.ATTRIBUTE);
/* 167 */       break;
/*     */     case 12:
/*     */     case 20:
/*     */     case 45:
/* 171 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 20:
/* 173 */         jj_consume_token(20);
/* 174 */         break;
/*     */       default:
/* 176 */         this.jj_la1[7] = this.jj_gen;
/*     */       }
/*     */ 
/* 179 */       Step s = NameOrWildcard(Axis.ELEMENT);
/* 180 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 182 */         Predicate(s);
/* 183 */         break;
/*     */       default:
/* 185 */         this.jj_la1[8] = this.jj_gen;
/*     */       }
/*     */ 
/* 188 */       break;
/*     */     case 21:
/* 190 */       jj_consume_token(21);
/* 191 */       s = NameOrWildcard(Axis.SUBSTITUTION_GROUP);
/* 192 */       break;
/*     */     case 22:
/*     */     case 23:
/* 195 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 22:
/* 197 */         jj_consume_token(22);
/* 198 */         break;
/*     */       case 23:
/* 200 */         jj_consume_token(23);
/* 201 */         break;
/*     */       default:
/* 203 */         this.jj_la1[9] = this.jj_gen;
/* 204 */         jj_consume_token(-1);
/* 205 */         throw new ParseException();
/*     */       }
/* 207 */       s = NameOrWildcardOrAnonymous(Axis.TYPE_DEFINITION);
/* 208 */       break;
/*     */     case 24:
/* 210 */       jj_consume_token(24);
/* 211 */       s = NameOrWildcard(Axis.BASETYPE);
/* 212 */       break;
/*     */     case 25:
/* 214 */       jj_consume_token(25);
/* 215 */       s = NameOrWildcard(Axis.PRIMITIVE_TYPE);
/* 216 */       break;
/*     */     case 26:
/* 218 */       jj_consume_token(26);
/* 219 */       s = NameOrWildcardOrAnonymous(Axis.ITEM_TYPE);
/* 220 */       break;
/*     */     case 27:
/* 222 */       jj_consume_token(27);
/* 223 */       Step s = NameOrWildcardOrAnonymous(Axis.MEMBER_TYPE);
/* 224 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 226 */         Predicate(s);
/* 227 */         break;
/*     */       default:
/* 229 */         this.jj_la1[10] = this.jj_gen;
/*     */       }
/*     */ 
/* 232 */       break;
/*     */     case 28:
/* 234 */       jj_consume_token(28);
/* 235 */       s = NameOrWildcardOrAnonymous(Axis.SCOPE);
/* 236 */       break;
/*     */     case 29:
/* 238 */       jj_consume_token(29);
/* 239 */       s = NameOrWildcard(Axis.ATTRIBUTE_GROUP);
/* 240 */       break;
/*     */     case 30:
/* 242 */       jj_consume_token(30);
/* 243 */       s = NameOrWildcard(Axis.MODEL_GROUP_DECL);
/* 244 */       break;
/*     */     case 31:
/* 246 */       jj_consume_token(31);
/* 247 */       s = NameOrWildcard(Axis.IDENTITY_CONSTRAINT);
/* 248 */       break;
/*     */     case 32:
/* 250 */       jj_consume_token(32);
/* 251 */       s = NameOrWildcard(Axis.REFERENCED_KEY);
/* 252 */       break;
/*     */     case 33:
/* 254 */       jj_consume_token(33);
/* 255 */       s = NameOrWildcard(Axis.NOTATION);
/* 256 */       break;
/*     */     case 34:
/* 258 */       jj_consume_token(34);
/* 259 */       Step s = new Step.Any(Axis.MODELGROUP_SEQUENCE);
/* 260 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 262 */         Predicate(s);
/* 263 */         break;
/*     */       default:
/* 265 */         this.jj_la1[11] = this.jj_gen;
/*     */       }
/*     */ 
/* 268 */       break;
/*     */     case 35:
/* 270 */       jj_consume_token(35);
/* 271 */       Step s = new Step.Any(Axis.MODELGROUP_CHOICE);
/* 272 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 274 */         Predicate(s);
/* 275 */         break;
/*     */       default:
/* 277 */         this.jj_la1[12] = this.jj_gen;
/*     */       }
/*     */ 
/* 280 */       break;
/*     */     case 36:
/* 282 */       jj_consume_token(36);
/* 283 */       Step s = new Step.Any(Axis.MODELGROUP_ALL);
/* 284 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 286 */         Predicate(s);
/* 287 */         break;
/*     */       default:
/* 289 */         this.jj_la1[13] = this.jj_gen;
/*     */       }
/*     */ 
/* 292 */       break;
/*     */     case 37:
/* 294 */       jj_consume_token(37);
/* 295 */       Step s = new Step.Any(Axis.MODELGROUP_ANY);
/* 296 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 298 */         Predicate(s);
/* 299 */         break;
/*     */       default:
/* 301 */         this.jj_la1[14] = this.jj_gen;
/*     */       }
/*     */ 
/* 304 */       break;
/*     */     case 38:
/* 306 */       jj_consume_token(38);
/* 307 */       Step s = new Step.Any(Axis.WILDCARD);
/* 308 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */       case 13:
/* 310 */         Predicate(s);
/* 311 */         break;
/*     */       default:
/* 313 */         this.jj_la1[15] = this.jj_gen;
/*     */       }
/*     */ 
/* 316 */       break;
/*     */     case 39:
/* 318 */       jj_consume_token(39);
/* 319 */       s = new Step.Any(Axis.ATTRIBUTE_WILDCARD);
/* 320 */       break;
/*     */     case 40:
/* 322 */       jj_consume_token(40);
/* 323 */       s = new Step.Any(Axis.FACET);
/* 324 */       break;
/*     */     case 41:
/* 326 */       jj_consume_token(41);
/* 327 */       Token n = jj_consume_token(14);
/* 328 */       s = new Step.Facet(Axis.FACET, n.image);
/* 329 */       break;
/*     */     case 42:
/* 331 */       jj_consume_token(42);
/* 332 */       s = new Step.Any(Axis.DESCENDANTS);
/* 333 */       break;
/*     */     case 43:
/* 335 */       jj_consume_token(43);
/* 336 */       String p = Prefix();
/* 337 */       s = new Step.Schema(Axis.X_SCHEMA, p);
/* 338 */       break;
/*     */     case 44:
/* 340 */       jj_consume_token(44);
/* 341 */       s = new Step.Any(Axis.X_SCHEMA);
/* 342 */       break;
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     default:
/* 344 */       this.jj_la1[16] = this.jj_gen;
/* 345 */       jj_consume_token(-1);
/* 346 */       throw new ParseException();
/*     */     }
/*     */     Step s;
/* 348 */     return s;
/*     */   }
/*     */ 
/*     */   public final Step NameOrWildcard(Axis a)
/*     */     throws ParseException
/*     */   {
/* 354 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */     case 12:
/* 356 */       UName un = QName();
/* 357 */       return new Step.Named(a, un);
/*     */     case 45:
/* 360 */       jj_consume_token(45);
/* 361 */       return new Step.Any(a);
/*     */     }
/*     */ 
/* 364 */     this.jj_la1[17] = this.jj_gen;
/* 365 */     jj_consume_token(-1);
/* 366 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */   public final Step NameOrWildcardOrAnonymous(Axis a)
/*     */     throws ParseException
/*     */   {
/* 373 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*     */     case 12:
/* 375 */       UName un = QName();
/* 376 */       return new Step.Named(a, un);
/*     */     case 45:
/* 379 */       jj_consume_token(45);
/* 380 */       return new Step.Any(a);
/*     */     case 46:
/* 383 */       jj_consume_token(46);
/* 384 */       return new Step.AnonymousType(a);
/*     */     }
/*     */ 
/* 387 */     this.jj_la1[18] = this.jj_gen;
/* 388 */     jj_consume_token(-1);
/* 389 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */   public final int Predicate(Step s)
/*     */     throws ParseException
/*     */   {
/* 396 */     Token t = jj_consume_token(13);
/* 397 */     return s.predicate = Integer.parseInt(trim(t.image));
/*     */   }
/*     */ 
/*     */   private static void jj_la1_0()
/*     */   {
/* 414 */     jj_la1_0 = new int[] { 32768, 4096, 196608, 196608, 196608, 196608, 786432, 1048576, 8192, 12582912, 8192, 8192, 8192, 8192, 8192, 8192, -258048, 4096, 4096 };
/*     */   }
/*     */   private static void jj_la1_1() {
/* 417 */     jj_la1_1 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16383, 8192, 24576 };
/*     */   }
/*     */ 
/*     */   public SCDParser(InputStream stream) {
/* 421 */     this(stream, null);
/*     */   }
/*     */   public SCDParser(InputStream stream, String encoding) {
/*     */     try { this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
/* 425 */     this.token_source = new SCDParserTokenManager(this.jj_input_stream);
/* 426 */     this.token = new Token();
/* 427 */     this.jj_ntk = -1;
/* 428 */     this.jj_gen = 0;
/* 429 */     for (int i = 0; i < 19; i++) this.jj_la1[i] = -1; 
/*     */   }
/*     */ 
/*     */   public void ReInit(InputStream stream)
/*     */   {
/* 433 */     ReInit(stream, null);
/*     */   }
/*     */   public void ReInit(InputStream stream, String encoding) {
/*     */     try { this.jj_input_stream.ReInit(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
/* 437 */     this.token_source.ReInit(this.jj_input_stream);
/* 438 */     this.token = new Token();
/* 439 */     this.jj_ntk = -1;
/* 440 */     this.jj_gen = 0;
/* 441 */     for (int i = 0; i < 19; i++) this.jj_la1[i] = -1; 
/*     */   }
/*     */ 
/*     */   public SCDParser(Reader stream)
/*     */   {
/* 445 */     this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 446 */     this.token_source = new SCDParserTokenManager(this.jj_input_stream);
/* 447 */     this.token = new Token();
/* 448 */     this.jj_ntk = -1;
/* 449 */     this.jj_gen = 0;
/* 450 */     for (int i = 0; i < 19; i++) this.jj_la1[i] = -1; 
/*     */   }
/*     */ 
/*     */   public void ReInit(Reader stream)
/*     */   {
/* 454 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 455 */     this.token_source.ReInit(this.jj_input_stream);
/* 456 */     this.token = new Token();
/* 457 */     this.jj_ntk = -1;
/* 458 */     this.jj_gen = 0;
/* 459 */     for (int i = 0; i < 19; i++) this.jj_la1[i] = -1; 
/*     */   }
/*     */ 
/*     */   public SCDParser(SCDParserTokenManager tm)
/*     */   {
/* 463 */     this.token_source = tm;
/* 464 */     this.token = new Token();
/* 465 */     this.jj_ntk = -1;
/* 466 */     this.jj_gen = 0;
/* 467 */     for (int i = 0; i < 19; i++) this.jj_la1[i] = -1; 
/*     */   }
/*     */ 
/*     */   public void ReInit(SCDParserTokenManager tm)
/*     */   {
/* 471 */     this.token_source = tm;
/* 472 */     this.token = new Token();
/* 473 */     this.jj_ntk = -1;
/* 474 */     this.jj_gen = 0;
/* 475 */     for (int i = 0; i < 19; i++) this.jj_la1[i] = -1;
/*     */   }
/*     */ 
/*     */   private final Token jj_consume_token(int kind)
/*     */     throws ParseException
/*     */   {
/* 480 */     Token oldToken;
/* 480 */     if ((oldToken = this.token).next != null) this.token = this.token.next; else
/* 481 */       this.token = (this.token.next = this.token_source.getNextToken());
/* 482 */     this.jj_ntk = -1;
/* 483 */     if (this.token.kind == kind) {
/* 484 */       this.jj_gen += 1;
/* 485 */       return this.token;
/*     */     }
/* 487 */     this.token = oldToken;
/* 488 */     this.jj_kind = kind;
/* 489 */     throw generateParseException();
/*     */   }
/*     */ 
/*     */   public final Token getNextToken() {
/* 493 */     if (this.token.next != null) this.token = this.token.next; else
/* 494 */       this.token = (this.token.next = this.token_source.getNextToken());
/* 495 */     this.jj_ntk = -1;
/* 496 */     this.jj_gen += 1;
/* 497 */     return this.token;
/*     */   }
/*     */ 
/*     */   public final Token getToken(int index) {
/* 501 */     Token t = this.token;
/* 502 */     for (int i = 0; i < index; i++) {
/* 503 */       if (t.next != null) t = t.next; else
/* 504 */         t = t.next = this.token_source.getNextToken();
/*     */     }
/* 506 */     return t;
/*     */   }
/*     */ 
/*     */   private final int jj_ntk() {
/* 510 */     if ((this.jj_nt = this.token.next) == null) {
/* 511 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
/*     */     }
/* 513 */     return this.jj_ntk = this.jj_nt.kind;
/*     */   }
/*     */ 
/*     */   public ParseException generateParseException()
/*     */   {
/* 521 */     this.jj_expentries.removeAllElements();
/* 522 */     boolean[] la1tokens = new boolean[47];
/* 523 */     for (int i = 0; i < 47; i++) {
/* 524 */       la1tokens[i] = false;
/*     */     }
/* 526 */     if (this.jj_kind >= 0) {
/* 527 */       la1tokens[this.jj_kind] = true;
/* 528 */       this.jj_kind = -1;
/*     */     }
/* 530 */     for (int i = 0; i < 19; i++) {
/* 531 */       if (this.jj_la1[i] == this.jj_gen) {
/* 532 */         for (int j = 0; j < 32; j++) {
/* 533 */           if ((jj_la1_0[i] & 1 << j) != 0) {
/* 534 */             la1tokens[j] = true;
/*     */           }
/* 536 */           if ((jj_la1_1[i] & 1 << j) != 0) {
/* 537 */             la1tokens[(32 + j)] = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 542 */     for (int i = 0; i < 47; i++) {
/* 543 */       if (la1tokens[i] != 0) {
/* 544 */         this.jj_expentry = new int[1];
/* 545 */         this.jj_expentry[0] = i;
/* 546 */         this.jj_expentries.addElement(this.jj_expentry);
/*     */       }
/*     */     }
/* 549 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 550 */     for (int i = 0; i < this.jj_expentries.size(); i++) {
/* 551 */       exptokseq[i] = ((int[])(int[])this.jj_expentries.elementAt(i));
/*     */     }
/* 553 */     return new ParseException(this.token, exptokseq, tokenImage);
/*     */   }
/*     */ 
/*     */   public final void enable_tracing()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void disable_tracing()
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 410 */     jj_la1_0();
/* 411 */     jj_la1_1();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.scd.SCDParser
 * JD-Core Version:    0.6.2
 */