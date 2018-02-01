/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Parser
/*     */ {
/*  40 */   private static boolean pdebug = Boolean.getBoolean("jstat.parser.debug");
/*  41 */   private static boolean ldebug = Boolean.getBoolean("jstat.lex.debug");
/*     */   private static final char OPENBLOCK = '{';
/*     */   private static final char CLOSEBLOCK = '}';
/*     */   private static final char DOUBLEQUOTE = '"';
/*     */   private static final char PERCENT_CHAR = '%';
/*     */   private static final char OPENPAREN = '(';
/*     */   private static final char CLOSEPAREN = ')';
/*     */   private static final char OPERATOR_PLUS = '+';
/*     */   private static final char OPERATOR_MINUS = '-';
/*     */   private static final char OPERATOR_MULTIPLY = '*';
/*     */   private static final char OPERATOR_DIVIDE = '/';
/*     */   private static final String OPTION = "option";
/*     */   private static final String COLUMN = "column";
/*     */   private static final String DATA = "data";
/*     */   private static final String HEADER = "header";
/*     */   private static final String WIDTH = "width";
/*     */   private static final String FORMAT = "format";
/*     */   private static final String ALIGN = "align";
/*     */   private static final String SCALE = "scale";
/*     */   private static final String START = "option";
/*  66 */   private static final Set scaleKeyWords = Scale.keySet();
/*  67 */   private static final Set alignKeyWords = Alignment.keySet();
/*  68 */   private static String[] otherKeyWords = { "option", "column", "data", "header", "width", "format", "align", "scale" };
/*     */ 
/*  72 */   private static char[] infixOps = { '+', '-', '*', '/' };
/*     */ 
/*  76 */   private static char[] delimiters = { '{', '}', '%', '(', ')' };
/*     */   private static Set<String> reservedWords;
/*     */   private StreamTokenizer st;
/*     */   private String filename;
/*     */   private Token lookahead;
/*     */   private Token previous;
/*     */   private int columnCount;
/*     */   private OptionFormat optionFormat;
/*     */ 
/*     */   public Parser(String paramString)
/*     */     throws FileNotFoundException
/*     */   {
/*  91 */     this.filename = paramString;
/*  92 */     BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
/*     */   }
/*     */ 
/*     */   public Parser(Reader paramReader) {
/*  96 */     this.st = new StreamTokenizer(paramReader);
/*     */ 
/*  99 */     this.st.ordinaryChar(47);
/* 100 */     this.st.wordChars(95, 95);
/* 101 */     this.st.slashSlashComments(true);
/* 102 */     this.st.slashStarComments(true);
/*     */ 
/* 104 */     reservedWords = new HashSet();
/* 105 */     for (int i = 0; i < otherKeyWords.length; i++) {
/* 106 */       reservedWords.add(otherKeyWords[i]);
/*     */     }
/*     */ 
/* 109 */     for (i = 0; i < delimiters.length; i++) {
/* 110 */       this.st.ordinaryChar(delimiters[i]);
/*     */     }
/*     */ 
/* 113 */     for (i = 0; i < infixOps.length; i++)
/* 114 */       this.st.ordinaryChar(infixOps[i]);
/*     */   }
/*     */ 
/*     */   private void pushBack()
/*     */   {
/* 123 */     this.lookahead = this.previous;
/* 124 */     this.st.pushBack();
/*     */   }
/*     */ 
/*     */   private void nextToken()
/*     */     throws ParserException, IOException
/*     */   {
/* 133 */     int i = this.st.nextToken();
/* 134 */     this.previous = this.lookahead;
/* 135 */     this.lookahead = new Token(this.st.ttype, this.st.sval, this.st.nval);
/* 136 */     log(ldebug, "lookahead = " + this.lookahead);
/*     */   }
/*     */ 
/*     */   private Token matchOne(Set paramSet)
/*     */     throws ParserException, IOException
/*     */   {
/* 145 */     if ((this.lookahead.ttype == -3) && 
/* 146 */       (paramSet
/* 146 */       .contains(this.lookahead.sval)))
/*     */     {
/* 147 */       Token localToken = this.lookahead;
/* 148 */       nextToken();
/* 149 */       return localToken;
/*     */     }
/* 151 */     throw new SyntaxException(this.st.lineno(), paramSet, this.lookahead);
/*     */   }
/*     */ 
/*     */   private void match(int paramInt, String paramString)
/*     */     throws ParserException, IOException
/*     */   {
/* 160 */     if ((this.lookahead.ttype == paramInt) && (this.lookahead.sval.compareTo(paramString) == 0))
/* 161 */       nextToken();
/*     */     else
/* 163 */       throw new SyntaxException(this.st.lineno(), new Token(paramInt, paramString), this.lookahead);
/*     */   }
/*     */ 
/*     */   private void match(int paramInt)
/*     */     throws ParserException, IOException
/*     */   {
/* 172 */     if (this.lookahead.ttype == paramInt)
/* 173 */       nextToken();
/*     */     else
/* 175 */       throw new SyntaxException(this.st.lineno(), new Token(paramInt), this.lookahead);
/*     */   }
/*     */ 
/*     */   private void match(char paramChar)
/*     */     throws ParserException, IOException
/*     */   {
/* 183 */     if (this.lookahead.ttype == paramChar) {
/* 184 */       nextToken();
/*     */     }
/*     */     else
/* 187 */       throw new SyntaxException(this.st.lineno(), new Token(paramChar), this.lookahead);
/*     */   }
/*     */ 
/*     */   private void matchQuotedString()
/*     */     throws ParserException, IOException
/*     */   {
/* 197 */     match('"');
/*     */   }
/*     */ 
/*     */   private void matchNumber()
/*     */     throws ParserException, IOException
/*     */   {
/* 204 */     match(-2);
/*     */   }
/*     */ 
/*     */   private void matchID()
/*     */     throws ParserException, IOException
/*     */   {
/* 211 */     match(-3);
/*     */   }
/*     */ 
/*     */   private void match(String paramString)
/*     */     throws ParserException, IOException
/*     */   {
/* 218 */     match(-3, paramString);
/*     */   }
/*     */ 
/*     */   private boolean isReservedWord(String paramString)
/*     */   {
/* 225 */     return reservedWords.contains(paramString);
/*     */   }
/*     */ 
/*     */   private boolean isInfixOperator(char paramChar)
/*     */   {
/* 232 */     for (int i = 0; i < infixOps.length; i++) {
/* 233 */       if (paramChar == infixOps[i]) {
/* 234 */         return true;
/*     */       }
/*     */     }
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */   private void scaleStmt(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/* 246 */     match("scale");
/* 247 */     Token localToken = matchOne(scaleKeyWords);
/* 248 */     paramColumnFormat.setScale(Scale.toScale(localToken.sval));
/* 249 */     String str = localToken.sval;
/* 250 */     log(pdebug, "Parsed: scale -> " + str);
/*     */   }
/*     */ 
/*     */   private void alignStmt(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/* 259 */     match("align");
/* 260 */     Token localToken = matchOne(alignKeyWords);
/* 261 */     paramColumnFormat.setAlignment(Alignment.toAlignment(localToken.sval));
/* 262 */     String str = localToken.sval;
/* 263 */     log(pdebug, "Parsed: align -> " + str);
/*     */   }
/*     */ 
/*     */   private void headerStmt(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/* 271 */     match("header");
/* 272 */     String str = this.lookahead.sval;
/* 273 */     matchQuotedString();
/* 274 */     paramColumnFormat.setHeader(str);
/* 275 */     log(pdebug, "Parsed: header -> " + str);
/*     */   }
/*     */ 
/*     */   private void widthStmt(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/* 283 */     match("width");
/* 284 */     double d = this.lookahead.nval;
/* 285 */     matchNumber();
/* 286 */     paramColumnFormat.setWidth((int)d);
/* 287 */     log(pdebug, "Parsed: width -> " + d);
/*     */   }
/*     */ 
/*     */   private void formatStmt(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/* 295 */     match("format");
/* 296 */     String str = this.lookahead.sval;
/* 297 */     matchQuotedString();
/* 298 */     paramColumnFormat.setFormat(str);
/* 299 */     log(pdebug, "Parsed: format -> " + str);
/*     */   }
/*     */ 
/*     */   private Expression primary()
/*     */     throws ParserException, IOException
/*     */   {
/* 306 */     Object localObject = null;
/*     */ 
/* 308 */     switch (this.lookahead.ttype) {
/*     */     case 40:
/* 310 */       match('(');
/* 311 */       localObject = expression();
/* 312 */       match(')');
/* 313 */       break;
/*     */     case -3:
/* 315 */       String str = this.lookahead.sval;
/* 316 */       if (isReservedWord(str)) {
/* 317 */         throw new SyntaxException(this.st.lineno(), "IDENTIFIER", "Reserved Word: " + this.lookahead.sval);
/*     */       }
/*     */ 
/* 320 */       matchID();
/* 321 */       localObject = new Identifier(str);
/* 322 */       log(pdebug, "Parsed: ID -> " + str);
/* 323 */       break;
/*     */     case -2:
/* 325 */       double d = this.lookahead.nval;
/* 326 */       matchNumber();
/* 327 */       localObject = new Literal(new Double(d));
/* 328 */       log(pdebug, "Parsed: number -> " + d);
/* 329 */       break;
/*     */     default:
/* 331 */       throw new SyntaxException(this.st.lineno(), "IDENTIFIER", this.lookahead);
/*     */     }
/* 333 */     log(pdebug, "Parsed: primary -> " + localObject);
/* 334 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Expression unary()
/*     */     throws ParserException, IOException
/*     */   {
/* 341 */     Object localObject = null;
/* 342 */     Operator localOperator = null;
/*     */     while (true)
/*     */     {
/* 345 */       switch (this.lookahead.ttype) {
/*     */       case 43:
/* 347 */         match('+');
/* 348 */         localOperator = Operator.PLUS;
/* 349 */         break;
/*     */       case 45:
/* 351 */         match('-');
/* 352 */         localOperator = Operator.MINUS;
/* 353 */         break;
/*     */       default:
/* 355 */         localObject = primary();
/* 356 */         log(pdebug, "Parsed: unary -> " + localObject);
/* 357 */         return localObject;
/*     */       }
/* 359 */       Expression localExpression = new Expression();
/* 360 */       localExpression.setOperator(localOperator);
/* 361 */       localExpression.setRight((Expression)localObject);
/* 362 */       log(pdebug, "Parsed: unary -> " + localExpression);
/* 363 */       localExpression.setLeft(new Literal(new Double(0.0D)));
/* 364 */       localObject = localExpression;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Expression multExpression()
/*     */     throws ParserException, IOException
/*     */   {
/* 372 */     Object localObject = unary();
/* 373 */     Operator localOperator = null;
/*     */     while (true)
/*     */     {
/* 376 */       switch (this.lookahead.ttype) {
/*     */       case 42:
/* 378 */         match('*');
/* 379 */         localOperator = Operator.MULTIPLY;
/* 380 */         break;
/*     */       case 47:
/* 382 */         match('/');
/* 383 */         localOperator = Operator.DIVIDE;
/* 384 */         break;
/*     */       default:
/* 386 */         log(pdebug, "Parsed: multExpression -> " + localObject);
/* 387 */         return localObject;
/*     */       }
/* 389 */       Expression localExpression = new Expression();
/* 390 */       localExpression.setOperator(localOperator);
/* 391 */       localExpression.setLeft((Expression)localObject);
/* 392 */       localExpression.setRight(unary());
/* 393 */       localObject = localExpression;
/* 394 */       log(pdebug, "Parsed: multExpression -> " + localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Expression addExpression()
/*     */     throws ParserException, IOException
/*     */   {
/* 402 */     Object localObject = multExpression();
/* 403 */     Operator localOperator = null;
/*     */     while (true)
/*     */     {
/* 406 */       switch (this.lookahead.ttype) {
/*     */       case 43:
/* 408 */         match('+');
/* 409 */         localOperator = Operator.PLUS;
/* 410 */         break;
/*     */       case 45:
/* 412 */         match('-');
/* 413 */         localOperator = Operator.MINUS;
/* 414 */         break;
/*     */       default:
/* 416 */         log(pdebug, "Parsed: addExpression -> " + localObject);
/* 417 */         return localObject;
/*     */       }
/* 419 */       Expression localExpression = new Expression();
/* 420 */       localExpression.setOperator(localOperator);
/* 421 */       localExpression.setLeft((Expression)localObject);
/* 422 */       localExpression.setRight(multExpression());
/* 423 */       localObject = localExpression;
/* 424 */       log(pdebug, "Parsed: addExpression -> " + localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Expression expression()
/*     */     throws ParserException, IOException
/*     */   {
/* 432 */     Expression localExpression = addExpression();
/* 433 */     log(pdebug, "Parsed: expression -> " + localExpression);
/* 434 */     return localExpression;
/*     */   }
/*     */ 
/*     */   private void dataStmt(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/* 441 */     match("data");
/* 442 */     Expression localExpression = expression();
/* 443 */     paramColumnFormat.setExpression(localExpression);
/* 444 */     log(pdebug, "Parsed: data -> " + localExpression);
/*     */   }
/*     */ 
/*     */   private void statementList(ColumnFormat paramColumnFormat)
/*     */     throws ParserException, IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 459 */       if (this.lookahead.ttype != -3) {
/* 460 */         return;
/*     */       }
/*     */ 
/* 463 */       if (this.lookahead.sval.compareTo("data") == 0) {
/* 464 */         dataStmt(paramColumnFormat);
/* 465 */       } else if (this.lookahead.sval.compareTo("header") == 0) {
/* 466 */         headerStmt(paramColumnFormat);
/* 467 */       } else if (this.lookahead.sval.compareTo("width") == 0) {
/* 468 */         widthStmt(paramColumnFormat);
/* 469 */       } else if (this.lookahead.sval.compareTo("format") == 0) {
/* 470 */         formatStmt(paramColumnFormat);
/* 471 */       } else if (this.lookahead.sval.compareTo("align") == 0) {
/* 472 */         alignStmt(paramColumnFormat); } else {
/* 473 */         if (this.lookahead.sval.compareTo("scale") != 0) break;
/* 474 */         scaleStmt(paramColumnFormat);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void optionList(OptionFormat paramOptionFormat)
/*     */     throws ParserException, IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 489 */       if (this.lookahead.ttype != -3) {
/* 490 */         return;
/*     */       }
/*     */ 
/* 493 */       match("column");
/* 494 */       match('{');
/* 495 */       ColumnFormat localColumnFormat = new ColumnFormat(this.columnCount++);
/* 496 */       statementList(localColumnFormat);
/* 497 */       match('}');
/* 498 */       localColumnFormat.validate();
/* 499 */       paramOptionFormat.addSubFormat(localColumnFormat);
/*     */     }
/*     */   }
/*     */ 
/*     */   private OptionFormat optionStmt()
/*     */     throws ParserException, IOException
/*     */   {
/* 507 */     match("option");
/* 508 */     String str = this.lookahead.sval;
/* 509 */     matchID();
/* 510 */     match('{');
/* 511 */     OptionFormat localOptionFormat = new OptionFormat(str);
/* 512 */     optionList(localOptionFormat);
/* 513 */     match('}');
/* 514 */     return localOptionFormat;
/*     */   }
/*     */ 
/*     */   public OptionFormat parse(String paramString)
/*     */     throws ParserException, IOException
/*     */   {
/* 522 */     nextToken();
/*     */ 
/* 529 */     while (this.lookahead.ttype != -1)
/*     */     {
/* 531 */       if ((this.lookahead.ttype != -3) || 
/* 532 */         (this.lookahead.sval
/* 532 */         .compareTo("option") != 0))
/*     */       {
/* 534 */         nextToken();
/*     */       }
/*     */       else
/*     */       {
/* 539 */         match("option");
/*     */ 
/* 541 */         if ((this.lookahead.ttype == -3) && 
/* 542 */           (this.lookahead.sval
/* 542 */           .compareTo(paramString) == 0))
/*     */         {
/* 544 */           pushBack();
/* 545 */           return optionStmt();
/*     */         }
/*     */ 
/* 548 */         nextToken();
/*     */       }
/*     */     }
/* 551 */     return null;
/*     */   }
/*     */ 
/*     */   public Set<OptionFormat> parseOptions() throws ParserException, IOException {
/* 555 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 557 */     nextToken();
/*     */ 
/* 559 */     while (this.lookahead.ttype != -1)
/*     */     {
/* 561 */       if ((this.lookahead.ttype != -3) || 
/* 562 */         (this.lookahead.sval
/* 562 */         .compareTo("option") != 0))
/*     */       {
/* 564 */         nextToken();
/*     */       }
/*     */       else
/*     */       {
/* 570 */         OptionFormat localOptionFormat = optionStmt();
/* 571 */         localHashSet.add(localOptionFormat);
/*     */       }
/*     */     }
/* 573 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   OptionFormat getOptionFormat() {
/* 577 */     return this.optionFormat;
/*     */   }
/*     */ 
/*     */   private void log(boolean paramBoolean, String paramString) {
/* 581 */     if (paramBoolean)
/* 582 */       System.out.println(paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Parser
 * JD-Core Version:    0.6.2
 */