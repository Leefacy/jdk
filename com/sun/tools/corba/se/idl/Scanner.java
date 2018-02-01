/*      */ package com.sun.tools.corba.se.idl;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class Scanner
/*      */ {
/*      */   static final int Star = 0;
/*      */   static final int Plus = 1;
/*      */   static final int Dot = 2;
/*      */   static final int None = 3;
/*      */   private int BOL;
/* 1543 */   private ScannerData data = new ScannerData();
/* 1544 */   private Stack dataStack = new Stack();
/* 1545 */   private Vector keywords = new Vector();
/* 1546 */   private Vector openEndedKeywords = new Vector();
/* 1547 */   private Vector wildcardKeywords = new Vector();
/*      */   private boolean verbose;
/* 1556 */   boolean escapedOK = true;
/*      */   private boolean emitAll;
/*      */   private float corbaLevel;
/*      */   private boolean debug;
/*      */ 
/*      */   Scanner(IncludeEntry paramIncludeEntry, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/*   73 */     readFile(paramIncludeEntry);
/*   74 */     this.verbose = paramBoolean1;
/*      */ 
/*   77 */     this.emitAll = paramBoolean2;
/*   78 */     sortKeywords(paramArrayOfString);
/*   79 */     this.corbaLevel = paramFloat;
/*   80 */     this.debug = paramBoolean3;
/*      */   }
/*      */ 
/*      */   void sortKeywords(String[] paramArrayOfString)
/*      */   {
/*   88 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*   89 */       if (wildcardAtEitherEnd(paramArrayOfString[i]))
/*   90 */         this.openEndedKeywords.addElement(paramArrayOfString[i]);
/*   91 */       else if (wildcardsInside(paramArrayOfString[i]))
/*   92 */         this.wildcardKeywords.addElement(paramArrayOfString[i]);
/*      */       else
/*   94 */         this.keywords.addElement(paramArrayOfString[i]);
/*      */   }
/*      */ 
/*      */   private boolean wildcardAtEitherEnd(String paramString)
/*      */   {
/*  107 */     return (paramString.startsWith("*")) || 
/*  103 */       (paramString
/*  103 */       .startsWith("+")) || 
/*  104 */       (paramString
/*  104 */       .startsWith(".")) || 
/*  105 */       (paramString
/*  105 */       .endsWith("*")) || 
/*  106 */       (paramString
/*  106 */       .endsWith("+")) || 
/*  107 */       (paramString
/*  107 */       .endsWith("."));
/*      */   }
/*      */ 
/*      */   private boolean wildcardsInside(String paramString)
/*      */   {
/*  117 */     return (paramString.indexOf("*") > 0) || 
/*  116 */       (paramString
/*  116 */       .indexOf("+") > 0) || 
/*  117 */       (paramString
/*  117 */       .indexOf(".") > 0);
/*      */   }
/*      */ 
/*      */   void readFile(IncludeEntry paramIncludeEntry)
/*      */     throws IOException
/*      */   {
/*  125 */     String str = paramIncludeEntry.name();
/*  126 */     str = str.substring(1, str.length() - 1);
/*  127 */     readFile(paramIncludeEntry, str);
/*      */   }
/*      */ 
/*      */   void readFile(IncludeEntry paramIncludeEntry, String paramString)
/*      */     throws IOException
/*      */   {
/*  135 */     this.data.fileEntry = paramIncludeEntry;
/*  136 */     this.data.filename = paramString;
/*      */ 
/*  142 */     File localFile = new File(this.data.filename);
/*  143 */     int i = (int)localFile.length();
/*  144 */     FileReader localFileReader = new FileReader(localFile);
/*      */ 
/*  146 */     String str = System.getProperty("line.separator");
/*  147 */     this.data.fileBytes = new char[i + str.length()];
/*      */ 
/*  149 */     localFileReader.read(this.data.fileBytes, 0, i);
/*  150 */     localFileReader.close();
/*      */ 
/*  153 */     for (int j = 0; j < str.length(); j++) {
/*  154 */       this.data.fileBytes[(i + j)] = str.charAt(j);
/*      */     }
/*  156 */     readChar();
/*      */   }
/*      */ 
/*      */   Token getToken()
/*      */     throws IOException
/*      */   {
/*  169 */     Token localToken = null;
/*  170 */     String str = new String("");
/*      */ 
/*  172 */     while (localToken == null) {
/*      */       try
/*      */       {
/*  175 */         this.data.oldIndex = this.data.fileIndex;
/*  176 */         this.data.oldLine = this.data.line;
/*  177 */         if (this.data.ch <= ' ') {
/*  178 */           skipWhiteSpace();
/*      */         }
/*  192 */         else if (this.data.ch == 'L')
/*      */         {
/*  195 */           readChar();
/*      */ 
/*  198 */           if (this.data.ch == '\'')
/*      */           {
/*  201 */             localToken = getCharacterToken(true);
/*  202 */             readChar();
/*      */           }
/*  205 */           else if (this.data.ch == '"')
/*      */           {
/*  210 */             readChar();
/*  211 */             localToken = new Token(204, getUntil('"'), true);
/*  212 */             readChar();
/*      */           }
/*      */           else
/*      */           {
/*  218 */             unread(this.data.ch);
/*  219 */             unread('L');
/*  220 */             readChar();
/*      */           }
/*      */ 
/*      */         }
/*  224 */         else if (((this.data.ch >= 'a') && (this.data.ch <= 'z')) || ((this.data.ch >= 'A') && (this.data.ch <= 'Z')) || (this.data.ch == '_') || 
/*  229 */           (Character.isLetter(this.data.ch)))
/*      */         {
/*  230 */           localToken = getString();
/*      */         }
/*  232 */         else if (((this.data.ch >= '0') && (this.data.ch <= '9')) || (this.data.ch == '.')) {
/*  233 */           localToken = getNumber();
/*      */         } else {
/*  235 */           switch (this.data.ch)
/*      */           {
/*      */           case ';':
/*  238 */             localToken = new Token(100);
/*  239 */             break;
/*      */           case '{':
/*  241 */             localToken = new Token(101);
/*  242 */             break;
/*      */           case '}':
/*  244 */             localToken = new Token(102);
/*  245 */             break;
/*      */           case ':':
/*  247 */             readChar();
/*  248 */             if (this.data.ch == ':') {
/*  249 */               localToken = new Token(124);
/*      */             }
/*      */             else {
/*  252 */               unread(this.data.ch);
/*  253 */               localToken = new Token(103);
/*      */             }
/*  255 */             break;
/*      */           case ',':
/*  257 */             localToken = new Token(104);
/*  258 */             break;
/*      */           case '=':
/*  260 */             readChar();
/*  261 */             if (this.data.ch == '=') {
/*  262 */               localToken = new Token(130);
/*      */             }
/*      */             else {
/*  265 */               unread(this.data.ch);
/*  266 */               localToken = new Token(105);
/*      */             }
/*  268 */             break;
/*      */           case '+':
/*  270 */             localToken = new Token(106);
/*  271 */             break;
/*      */           case '-':
/*  273 */             localToken = new Token(107);
/*  274 */             break;
/*      */           case '(':
/*  276 */             localToken = new Token(108);
/*  277 */             break;
/*      */           case ')':
/*  279 */             localToken = new Token(109);
/*  280 */             break;
/*      */           case '<':
/*  282 */             readChar();
/*  283 */             if (this.data.ch == '<') {
/*  284 */               localToken = new Token(125);
/*  285 */             } else if (this.data.ch == '=') {
/*  286 */               localToken = new Token(133);
/*      */             }
/*      */             else {
/*  289 */               unread(this.data.ch);
/*  290 */               localToken = new Token(110);
/*      */             }
/*  292 */             break;
/*      */           case '>':
/*  294 */             readChar();
/*  295 */             if (this.data.ch == '>') {
/*  296 */               localToken = new Token(126);
/*  297 */             } else if (this.data.ch == '=') {
/*  298 */               localToken = new Token(132);
/*      */             }
/*      */             else {
/*  301 */               unread(this.data.ch);
/*  302 */               localToken = new Token(111);
/*      */             }
/*  304 */             break;
/*      */           case '[':
/*  306 */             localToken = new Token(112);
/*  307 */             break;
/*      */           case ']':
/*  309 */             localToken = new Token(113);
/*  310 */             break;
/*      */           case '\'':
/*  312 */             localToken = getCharacterToken(false);
/*  313 */             break;
/*      */           case '"':
/*  315 */             readChar();
/*  316 */             localToken = new Token(204, getUntil('"', false, false, false));
/*  317 */             break;
/*      */           case '\\':
/*  319 */             readChar();
/*      */ 
/*  322 */             if ((this.data.ch == '\n') || (this.data.ch == '\r'))
/*  323 */               localToken = null;
/*      */             else
/*  325 */               localToken = new Token(116);
/*  326 */             break;
/*      */           case '|':
/*  328 */             readChar();
/*  329 */             if (this.data.ch == '|') {
/*  330 */               localToken = new Token(134);
/*      */             }
/*      */             else {
/*  333 */               unread(this.data.ch);
/*  334 */               localToken = new Token(117);
/*      */             }
/*  336 */             break;
/*      */           case '^':
/*  338 */             localToken = new Token(118);
/*  339 */             break;
/*      */           case '&':
/*  341 */             readChar();
/*  342 */             if (this.data.ch == '&') {
/*  343 */               localToken = new Token(135);
/*      */             }
/*      */             else {
/*  346 */               unread(this.data.ch);
/*  347 */               localToken = new Token(119);
/*      */             }
/*  349 */             break;
/*      */           case '*':
/*  351 */             localToken = new Token(120);
/*  352 */             break;
/*      */           case '/':
/*  354 */             readChar();
/*      */ 
/*  357 */             if (this.data.ch == '/')
/*      */             {
/*  359 */               str = getLineComment();
/*  360 */             } else if (this.data.ch == '*')
/*      */             {
/*  362 */               str = getBlockComment();
/*      */             }
/*      */             else {
/*  365 */               unread(this.data.ch);
/*  366 */               localToken = new Token(121);
/*      */             }
/*  368 */             break;
/*      */           case '%':
/*  370 */             localToken = new Token(122);
/*  371 */             break;
/*      */           case '~':
/*  373 */             localToken = new Token(123);
/*  374 */             break;
/*      */           case '#':
/*  383 */             localToken = getDirective();
/*  384 */             break;
/*      */           case '!':
/*  386 */             readChar();
/*  387 */             if (this.data.ch == '=') {
/*  388 */               localToken = new Token(131);
/*      */             }
/*      */             else {
/*  391 */               unread(this.data.ch);
/*  392 */               localToken = new Token(129);
/*      */             }
/*  394 */             break;
/*      */           case '?':
/*      */             try
/*      */             {
/*  398 */               localToken = replaceTrigraph(); } catch (InvalidCharacter localInvalidCharacter) {  } case '$':
/*      */           case '.':
/*      */           case '0':
/*      */           case '1':
/*      */           case '2':
/*      */           case '3':
/*      */           case '4':
/*      */           case '5':
/*      */           case '6':
/*      */           case '7':
/*      */           case '8':
/*      */           case '9':
/*      */           case '@':
/*      */           case 'A':
/*      */           case 'B':
/*      */           case 'C':
/*      */           case 'D':
/*      */           case 'E':
/*      */           case 'F':
/*      */           case 'G':
/*      */           case 'H':
/*      */           case 'I':
/*      */           case 'J':
/*      */           case 'K':
/*      */           case 'L':
/*      */           case 'M':
/*      */           case 'N':
/*      */           case 'O':
/*      */           case 'P':
/*      */           case 'Q':
/*      */           case 'R':
/*      */           case 'S':
/*      */           case 'T':
/*      */           case 'U':
/*      */           case 'V':
/*      */           case 'W':
/*      */           case 'X':
/*      */           case 'Y':
/*      */           case 'Z':
/*      */           case '_':
/*      */           case '`':
/*      */           case 'a':
/*      */           case 'b':
/*      */           case 'c':
/*      */           case 'd':
/*      */           case 'e':
/*      */           case 'f':
/*      */           case 'g':
/*      */           case 'h':
/*      */           case 'i':
/*      */           case 'j':
/*      */           case 'k':
/*      */           case 'l':
/*      */           case 'm':
/*      */           case 'n':
/*      */           case 'o':
/*      */           case 'p':
/*      */           case 'q':
/*      */           case 'r':
/*      */           case 's':
/*      */           case 't':
/*      */           case 'u':
/*      */           case 'v':
/*      */           case 'w':
/*      */           case 'x':
/*      */           case 'y':
/*      */           case 'z':
/*      */           default:
/*  403 */             throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */           }
/*  405 */           readChar();
/*      */         }
/*      */       }
/*      */       catch (EOFException localEOFException)
/*      */       {
/*  410 */         localToken = new Token(999);
/*      */       }
/*      */     }
/*      */ 
/*  414 */     localToken.comment = new Comment(str);
/*      */ 
/*  420 */     if (this.debug) {
/*  421 */       System.out.println("Token: " + localToken);
/*      */     }
/*  423 */     return localToken;
/*      */   }
/*      */ 
/*      */   void scanString(String paramString)
/*      */   {
/*  431 */     this.dataStack.push(this.data);
/*      */ 
/*  433 */     this.data = new ScannerData(this.data);
/*      */ 
/*  435 */     this.data.fileIndex = 0;
/*  436 */     this.data.oldIndex = 0;
/*      */ 
/*  438 */     int i = paramString.length();
/*  439 */     this.data.fileBytes = new char[i];
/*  440 */     paramString.getChars(0, i, this.data.fileBytes, 0);
/*      */ 
/*  442 */     this.data.macrodata = true;
/*      */     try {
/*  444 */       readChar();
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   void scanIncludedFile(IncludeEntry paramIncludeEntry, String paramString, boolean paramBoolean) throws IOException {
/*  452 */     this.dataStack.push(this.data);
/*  453 */     this.data = new ScannerData();
/*  454 */     this.data.indent = (((ScannerData)this.dataStack.peek()).indent + ' ');
/*  455 */     this.data.includeIsImport = paramBoolean;
/*      */     try
/*      */     {
/*  458 */       readFile(paramIncludeEntry, paramString);
/*  459 */       if ((!this.emitAll) && (paramBoolean)) {
/*  460 */         SymtabEntry.enteringInclude();
/*      */       }
/*      */ 
/*  464 */       Parser.enteringInclude();
/*      */ 
/*  466 */       if (this.verbose)
/*  467 */         System.out.println(this.data.indent + Util.getMessage("Compile.parsing", paramString));
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  471 */       this.data = ((ScannerData)this.dataStack.pop());
/*  472 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void unread(char paramChar)
/*      */   {
/*  481 */     if ((paramChar == '\n') && (!this.data.macrodata)) this.data.line -= 1;
/*  482 */     this.data.fileIndex -= 1;
/*      */   }
/*      */ 
/*      */   void readChar()
/*      */     throws IOException
/*      */   {
/*  490 */     if (this.data.fileIndex >= this.data.fileBytes.length) {
/*  491 */       if (this.dataStack.empty()) {
/*  492 */         throw new EOFException();
/*      */       }
/*      */ 
/*  504 */       if (!this.data.macrodata)
/*      */       {
/*  506 */         if ((!this.emitAll) && (this.data.includeIsImport))
/*  507 */           SymtabEntry.exitingInclude();
/*  508 */         Parser.exitingInclude();
/*      */       }
/*      */ 
/*  511 */       if ((this.verbose) && (!this.data.macrodata))
/*  512 */         System.out.println(this.data.indent + Util.getMessage("Compile.parseDone", this.data.filename));
/*  513 */       this.data = ((ScannerData)this.dataStack.pop());
/*      */     }
/*      */     else
/*      */     {
/*  517 */       this.data.ch = ((char)(this.data.fileBytes[(this.data.fileIndex++)] & 0xFF));
/*  518 */       if ((this.data.ch == '\n') && (!this.data.macrodata)) this.data.line += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getWString()
/*      */     throws IOException
/*      */   {
/*  529 */     readChar();
/*  530 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  532 */     while (this.data.ch != '"') {
/*  533 */       if (this.data.ch == '\\')
/*      */       {
/*  536 */         readChar();
/*  537 */         if (this.data.ch == 'u')
/*      */         {
/*  539 */           int i = getNDigitHexNumber(4);
/*  540 */           System.out.println("Got num: " + i);
/*  541 */           System.out.println("Which is: " + (char)i);
/*  542 */           localStringBuffer.append((char)i);
/*      */         }
/*  548 */         else if ((this.data.ch >= '0') && (this.data.ch <= '7'))
/*      */         {
/*  550 */           localStringBuffer.append((char)get3DigitOctalNumber());
/*      */         }
/*      */         else
/*      */         {
/*  557 */           localStringBuffer.append('\\');
/*  558 */           localStringBuffer.append(this.data.ch);
/*      */         }
/*      */       }
/*      */       else {
/*  562 */         localStringBuffer.append(this.data.ch);
/*      */ 
/*  566 */         readChar();
/*      */       }
/*      */     }
/*  569 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private Token getCharacterToken(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  581 */     Token localToken = null;
/*  582 */     readChar();
/*  583 */     if (this.data.ch == '\\')
/*      */     {
/*  585 */       readChar();
/*  586 */       if ((this.data.ch == 'x') || (this.data.ch == 'u'))
/*      */       {
/*  588 */         char c = this.data.ch;
/*  589 */         int j = getNDigitHexNumber(c == 'x' ? 2 : 4);
/*      */ 
/*  591 */         return new Token(201, (char)j + "\\" + c + 
/*  591 */           Integer.toString(j, 16), 
/*  591 */           paramBoolean);
/*      */       }
/*  593 */       if ((this.data.ch >= '0') && (this.data.ch <= '7'))
/*      */       {
/*  595 */         int i = get3DigitOctalNumber();
/*      */ 
/*  597 */         return new Token(201, (char)i + "\\" + 
/*  597 */           Integer.toString(i, 8), 
/*  597 */           paramBoolean);
/*      */       }
/*  599 */       return singleCharEscapeSequence(paramBoolean);
/*      */     }
/*  601 */     localToken = new Token(201, "" + this.data.ch + this.data.ch, paramBoolean);
/*  602 */     readChar();
/*  603 */     return localToken;
/*      */   }
/*      */ 
/*      */   private Token singleCharEscapeSequence(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*      */     Token localToken;
/*  612 */     if (this.data.ch == 'n')
/*      */     {
/*  614 */       localToken = new Token(201, "\n\\n", paramBoolean);
/*  615 */     } else if (this.data.ch == 't')
/*      */     {
/*  617 */       localToken = new Token(201, "\t\\t", paramBoolean);
/*  618 */     } else if (this.data.ch == 'v')
/*      */     {
/*  620 */       localToken = new Token(201, "\013\\v", paramBoolean);
/*  621 */     } else if (this.data.ch == 'b')
/*      */     {
/*  623 */       localToken = new Token(201, "\b\\b", paramBoolean);
/*  624 */     } else if (this.data.ch == 'r')
/*      */     {
/*  626 */       localToken = new Token(201, "\r\\r", paramBoolean);
/*  627 */     } else if (this.data.ch == 'f')
/*      */     {
/*  629 */       localToken = new Token(201, "\f\\f", paramBoolean);
/*  630 */     } else if (this.data.ch == 'a')
/*      */     {
/*  632 */       localToken = new Token(201, "\007\\a", paramBoolean);
/*  633 */     } else if (this.data.ch == '\\')
/*      */     {
/*  635 */       localToken = new Token(201, "\\\\\\", paramBoolean);
/*  636 */     } else if (this.data.ch == '?')
/*      */     {
/*  638 */       localToken = new Token(201, "?\\?", paramBoolean);
/*  639 */     } else if (this.data.ch == '\'')
/*      */     {
/*  641 */       localToken = new Token(201, "'\\'", paramBoolean);
/*  642 */     } else if (this.data.ch == '"')
/*      */     {
/*  644 */       localToken = new Token(201, "\"\\\"", paramBoolean);
/*      */     }
/*  646 */     else throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*  647 */     readChar();
/*  648 */     return localToken;
/*      */   }
/*      */ 
/*      */   private Token getString() throws IOException
/*      */   {
/*  653 */     StringBuffer localStringBuffer = new StringBuffer();
/*  654 */     boolean bool = false;
/*  655 */     boolean[] arrayOfBoolean = { false };
/*      */ 
/*  659 */     if (this.data.ch == '_') {
/*  660 */       localStringBuffer.append(this.data.ch);
/*  661 */       readChar();
/*  662 */       if (((bool = this.escapedOK)) && 
/*  663 */         (this.data.ch == '_'))
/*      */       {
/*  665 */         throw new InvalidCharacter(this.data.filename, currentLine(), 
/*  665 */           currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  670 */     while ((Character.isLetterOrDigit(this.data.ch)) || (this.data.ch == '_')) {
/*  671 */       localStringBuffer.append(this.data.ch);
/*  672 */       readChar();
/*      */     }
/*      */ 
/*  675 */     String str = localStringBuffer.toString();
/*      */ 
/*  680 */     if (!bool) {
/*  681 */       Token localToken = Token.makeKeywordToken(str, this.corbaLevel, this.escapedOK, arrayOfBoolean);
/*      */ 
/*  683 */       if (localToken != null) {
/*  684 */         return localToken;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  690 */     str = getIdentifier(str);
/*      */ 
/*  694 */     if (this.data.ch == '(') {
/*  695 */       readChar();
/*  696 */       return new Token(81, str, bool, arrayOfBoolean[0], false);
/*      */     }
/*      */ 
/*  699 */     return new Token(80, str, bool, arrayOfBoolean[0], false);
/*      */   }
/*      */ 
/*      */   private boolean matchesClosedWildKeyword(String paramString)
/*      */   {
/*  711 */     int i = 1;
/*  712 */     String str1 = paramString;
/*  713 */     Enumeration localEnumeration = this.wildcardKeywords.elements();
/*  714 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  716 */       int j = 3;
/*  717 */       StringTokenizer localStringTokenizer = new StringTokenizer((String)localEnumeration.nextElement(), "*+.", true);
/*  718 */       if (localStringTokenizer.hasMoreTokens())
/*      */       {
/*  720 */         String str2 = localStringTokenizer.nextToken();
/*  721 */         if (str1.startsWith(str2))
/*      */         {
/*  723 */           str1 = str1.substring(str2.length());
/*  724 */           while ((localStringTokenizer.hasMoreTokens()) && (i != 0))
/*      */           {
/*  726 */             str2 = localStringTokenizer.nextToken();
/*  727 */             if (str2.equals("*")) {
/*  728 */               j = 0;
/*  729 */             } else if (str2.equals("+")) {
/*  730 */               j = 1;
/*  731 */             } else if (str2.equals(".")) {
/*  732 */               j = 2;
/*      */             }
/*      */             else
/*      */             {
/*      */               int k;
/*  733 */               if (j == 0)
/*      */               {
/*  735 */                 k = str1.indexOf(str2);
/*  736 */                 if (k >= 0)
/*  737 */                   str1 = str1.substring(k + str2.length());
/*      */                 else
/*  739 */                   i = 0;
/*      */               }
/*  741 */               else if (j == 1)
/*      */               {
/*  743 */                 k = str1.indexOf(str2);
/*  744 */                 if (k > 0)
/*  745 */                   str1 = str1.substring(k + str2.length());
/*      */                 else
/*  747 */                   i = 0;
/*      */               }
/*  749 */               else if (j == 2)
/*      */               {
/*  751 */                 k = str1.indexOf(str2);
/*  752 */                 if (k == 1)
/*  753 */                   str1 = str1.substring(1 + str2.length());
/*      */                 else
/*  755 */                   i = 0; 
/*      */               }
/*      */             }
/*      */           }
/*  758 */           if ((i != 0) && (str1.equals("")))
/*      */             break;
/*      */         }
/*      */       }
/*      */     }
/*  763 */     return (i != 0) && (str1.equals(""));
/*      */   }
/*      */ 
/*      */   private String matchesOpenWildcard(String paramString)
/*      */   {
/*  771 */     Enumeration localEnumeration = this.openEndedKeywords.elements();
/*  772 */     String str1 = "";
/*  773 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  775 */       int i = 3;
/*  776 */       int j = 1;
/*  777 */       String str2 = paramString;
/*  778 */       StringTokenizer localStringTokenizer = new StringTokenizer((String)localEnumeration.nextElement(), "*+.", true);
/*  779 */       while ((localStringTokenizer.hasMoreTokens()) && (j != 0))
/*      */       {
/*  781 */         String str3 = localStringTokenizer.nextToken();
/*  782 */         if (str3.equals("*")) {
/*  783 */           i = 0;
/*  784 */         } else if (str3.equals("+")) {
/*  785 */           i = 1;
/*  786 */         } else if (str3.equals(".")) {
/*  787 */           i = 2;
/*      */         }
/*      */         else
/*      */         {
/*      */           int k;
/*  788 */           if (i == 0)
/*      */           {
/*  790 */             i = 3;
/*  791 */             k = str2.lastIndexOf(str3);
/*  792 */             if (k >= 0)
/*  793 */               str2 = blankOutMatch(str2, k, str3.length());
/*      */             else
/*  795 */               j = 0;
/*      */           }
/*  797 */           else if (i == 1)
/*      */           {
/*  799 */             i = 3;
/*  800 */             k = str2.lastIndexOf(str3);
/*  801 */             if (k > 0)
/*  802 */               str2 = blankOutMatch(str2, k, str3.length());
/*      */             else
/*  804 */               j = 0;
/*      */           }
/*  806 */           else if (i == 2)
/*      */           {
/*  808 */             i = 3;
/*  809 */             k = str2.lastIndexOf(str3);
/*  810 */             if (k == 1)
/*  811 */               str2 = blankOutMatch(str2, 1, str3.length());
/*      */             else
/*  813 */               j = 0;
/*      */           }
/*  815 */           else if (i == 3) {
/*  816 */             if (str2.startsWith(str3))
/*  817 */               str2 = blankOutMatch(str2, 0, str3.length());
/*      */             else {
/*  819 */               j = 0;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  825 */       if (j != 0)
/*      */       {
/*  827 */         if (i != 0)
/*      */         {
/*  829 */           if ((i != 1) || (str2.lastIndexOf(' ') == str2.length() - 1))
/*      */           {
/*  831 */             if ((i != 2) || (str2.lastIndexOf(' ') != str2.length() - 2))
/*      */             {
/*  833 */               if ((i != 3) || (str2.lastIndexOf(' ') != str2.length() - 1))
/*      */               {
/*  836 */                 j = 0;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  851 */       if (j != 0)
/*      */       {
/*  853 */         str1 = str1 + "_" + matchesOpenWildcard(str2.trim());
/*  854 */         break;
/*      */       }
/*      */     }
/*  857 */     return str1;
/*      */   }
/*      */ 
/*      */   private String blankOutMatch(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  865 */     char[] arrayOfChar = new char[paramInt2];
/*  866 */     for (int i = 0; i < paramInt2; i++)
/*  867 */       arrayOfChar[i] = ' ';
/*  868 */     return paramString.substring(0, paramInt1) + new String(arrayOfChar) + paramString.substring(paramInt1 + paramInt2);
/*      */   }
/*      */ 
/*      */   private String getIdentifier(String paramString)
/*      */   {
/*  876 */     if (this.keywords.contains(paramString))
/*      */     {
/*  878 */       paramString = '_' + paramString;
/*      */     }
/*      */     else
/*      */     {
/*  884 */       String str = "";
/*  885 */       if (matchesClosedWildKeyword(paramString)) {
/*  886 */         str = "_";
/*      */       }
/*      */       else
/*      */       {
/*  895 */         str = matchesOpenWildcard(paramString);
/*  896 */       }paramString = str + paramString;
/*      */     }
/*  898 */     return paramString;
/*      */   }
/*      */ 
/*      */   private Token getDirective()
/*      */     throws IOException
/*      */   {
/*  906 */     readChar();
/*  907 */     String str = new String();
/*  908 */     while (((this.data.ch >= 'a') && (this.data.ch <= 'z')) || ((this.data.ch >= 'A') && (this.data.ch <= 'Z')))
/*      */     {
/*  910 */       str = str + this.data.ch;
/*  911 */       readChar();
/*      */     }
/*  913 */     unread(this.data.ch);
/*  914 */     for (int i = 0; i < Token.Directives.length; i++) {
/*  915 */       if (str.equals(Token.Directives[i]))
/*  916 */         return new Token(300 + i);
/*      */     }
/*  918 */     return new Token(313, str);
/*      */   }
/*      */ 
/*      */   private Token getNumber()
/*      */     throws IOException
/*      */   {
/*  926 */     if (this.data.ch == '.')
/*  927 */       return getFractionNoInteger();
/*  928 */     if (this.data.ch == '0') {
/*  929 */       return isItHex();
/*      */     }
/*  931 */     return getInteger();
/*      */   }
/*      */ 
/*      */   private Token getFractionNoInteger()
/*      */     throws IOException
/*      */   {
/*  939 */     readChar();
/*  940 */     if ((this.data.ch >= '0') && (this.data.ch <= '9')) {
/*  941 */       return getFraction(".");
/*      */     }
/*  943 */     return new Token(127);
/*      */   }
/*      */ 
/*      */   private Token getFraction(String paramString)
/*      */     throws IOException
/*      */   {
/*  951 */     while ((this.data.ch >= '0') && (this.data.ch <= '9'))
/*      */     {
/*  953 */       paramString = paramString + this.data.ch;
/*  954 */       readChar();
/*      */     }
/*  956 */     if ((this.data.ch == 'e') || (this.data.ch == 'E')) {
/*  957 */       return getExponent(paramString + 'E');
/*      */     }
/*  959 */     return new Token(203, paramString);
/*      */   }
/*      */ 
/*      */   private Token getExponent(String paramString)
/*      */     throws IOException
/*      */   {
/*  967 */     readChar();
/*  968 */     if ((this.data.ch == '+') || (this.data.ch == '-'))
/*      */     {
/*  970 */       paramString = paramString + this.data.ch;
/*  971 */       readChar();
/*      */     }
/*  973 */     else if ((this.data.ch < '0') || (this.data.ch > '9')) {
/*  974 */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*  975 */     }while ((this.data.ch >= '0') && (this.data.ch <= '9'))
/*      */     {
/*  977 */       paramString = paramString + this.data.ch;
/*  978 */       readChar();
/*      */     }
/*  980 */     return new Token(203, paramString);
/*      */   }
/*      */ 
/*      */   private Token isItHex()
/*      */     throws IOException
/*      */   {
/*  988 */     readChar();
/*  989 */     if (this.data.ch == '.')
/*      */     {
/*  991 */       readChar();
/*  992 */       return getFraction("0.");
/*      */     }
/*  994 */     if ((this.data.ch == 'x') || (this.data.ch == 'X'))
/*  995 */       return getHexNumber("0x");
/*  996 */     if ((this.data.ch == '8') || (this.data.ch == '9'))
/*  997 */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*  998 */     if ((this.data.ch >= '0') && (this.data.ch <= '7'))
/*  999 */       return getOctalNumber();
/* 1000 */     if ((this.data.ch == 'e') || (this.data.ch == 'E')) {
/* 1001 */       return getExponent("0E");
/*      */     }
/* 1003 */     return new Token(202, "0");
/*      */   }
/*      */ 
/*      */   private Token getOctalNumber()
/*      */     throws IOException
/*      */   {
/* 1011 */     String str = "0" + this.data.ch;
/* 1012 */     readChar();
/* 1013 */     while ((this.data.ch >= '0') && (this.data.ch <= '9'))
/*      */     {
/* 1015 */       if ((this.data.ch == '8') || (this.data.ch == '9'))
/* 1016 */         throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/* 1017 */       str = str + this.data.ch;
/* 1018 */       readChar();
/*      */     }
/* 1020 */     return new Token(202, str);
/*      */   }
/*      */ 
/*      */   private Token getHexNumber(String paramString)
/*      */     throws IOException
/*      */   {
/* 1028 */     readChar();
/* 1029 */     if (((this.data.ch < '0') || (this.data.ch > '9')) && ((this.data.ch < 'a') || (this.data.ch > 'f')) && ((this.data.ch < 'A') || (this.data.ch > 'F'))) {
/* 1030 */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */     }
/* 1032 */     while (((this.data.ch >= '0') && (this.data.ch <= '9')) || ((this.data.ch >= 'a') && (this.data.ch <= 'f')) || ((this.data.ch >= 'A') && (this.data.ch <= 'F')))
/*      */     {
/* 1034 */       paramString = paramString + this.data.ch;
/* 1035 */       readChar();
/*      */     }
/* 1037 */     return new Token(202, paramString);
/*      */   }
/*      */ 
/*      */   private int getNDigitHexNumber(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1045 */     readChar();
/* 1046 */     if (!isHexChar(this.data.ch))
/*      */     {
/* 1048 */       throw new InvalidCharacter(this.data.filename, currentLine(), 
/* 1048 */         currentLineNumber(), currentLinePosition(), this.data.ch);
/* 1049 */     }String str = "" + this.data.ch;
/* 1050 */     readChar();
/* 1051 */     for (int i = 2; i <= paramInt; i++)
/*      */     {
/* 1053 */       if (!isHexChar(this.data.ch))
/*      */         break;
/* 1055 */       str = str + this.data.ch;
/* 1056 */       readChar();
/*      */     }
/*      */     try
/*      */     {
/* 1060 */       return Integer.parseInt(str, 16);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/* 1065 */     return 0;
/*      */   }
/*      */ 
/*      */   private boolean isHexChar(char paramChar)
/*      */   {
/* 1073 */     return ((this.data.ch >= '0') && (this.data.ch <= '9')) || ((this.data.ch >= 'a') && (this.data.ch <= 'f')) || ((this.data.ch >= 'A') && (this.data.ch <= 'F'));
/*      */   }
/*      */ 
/*      */   private int get3DigitOctalNumber()
/*      */     throws IOException
/*      */   {
/* 1083 */     char c = this.data.ch;
/* 1084 */     String str = "" + this.data.ch;
/* 1085 */     readChar();
/* 1086 */     if ((this.data.ch >= '0') && (this.data.ch <= '7'))
/*      */     {
/* 1088 */       str = str + this.data.ch;
/* 1089 */       readChar();
/* 1090 */       if ((this.data.ch >= '0') && (this.data.ch <= '7'))
/*      */       {
/* 1092 */         str = str + this.data.ch;
/* 1093 */         if (c > '3')
/*      */         {
/* 1095 */           throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), c);
/* 1096 */         }readChar();
/*      */       }
/*      */     }
/* 1099 */     int i = 0;
/*      */     try
/*      */     {
/* 1102 */       i = Integer.parseInt(str, 8);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/* 1106 */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), str.charAt(0));
/*      */     }
/* 1108 */     return i;
/*      */   }
/*      */ 
/*      */   private Token getInteger()
/*      */     throws IOException
/*      */   {
/* 1116 */     String str = "" + this.data.ch;
/* 1117 */     readChar();
/* 1118 */     if (this.data.ch == '.')
/*      */     {
/* 1120 */       readChar();
/* 1121 */       return getFraction(str + '.');
/*      */     }
/* 1123 */     if ((this.data.ch == 'e') || (this.data.ch == 'E'))
/* 1124 */       return getExponent(str + 'E');
/* 1125 */     if ((this.data.ch >= '0') && (this.data.ch <= '9'))
/* 1126 */       while ((this.data.ch >= '0') && (this.data.ch <= '9'))
/*      */       {
/* 1128 */         str = str + this.data.ch;
/* 1129 */         readChar();
/* 1130 */         if (this.data.ch == '.')
/*      */         {
/* 1132 */           readChar();
/* 1133 */           return getFraction(str + '.');
/*      */         }
/*      */       }
/* 1136 */     return new Token(202, str);
/*      */   }
/*      */ 
/*      */   private Token replaceTrigraph()
/*      */     throws IOException
/*      */   {
/* 1144 */     readChar();
/* 1145 */     if (this.data.ch == '?')
/*      */     {
/* 1147 */       readChar();
/* 1148 */       if (this.data.ch == '=') {
/* 1149 */         this.data.ch = '#';
/* 1150 */       } else if (this.data.ch == '/') {
/* 1151 */         this.data.ch = '\\';
/* 1152 */       } else if (this.data.ch == '\'') {
/* 1153 */         this.data.ch = '^';
/* 1154 */       } else if (this.data.ch == '(') {
/* 1155 */         this.data.ch = '[';
/* 1156 */       } else if (this.data.ch == ')') {
/* 1157 */         this.data.ch = ']';
/* 1158 */       } else if (this.data.ch == '!') {
/* 1159 */         this.data.ch = '|';
/* 1160 */       } else if (this.data.ch == '<') {
/* 1161 */         this.data.ch = '{';
/* 1162 */       } else if (this.data.ch == '>') {
/* 1163 */         this.data.ch = '}';
/* 1164 */       } else if (this.data.ch == '-') {
/* 1165 */         this.data.ch = '~';
/*      */       }
/*      */       else {
/* 1168 */         unread(this.data.ch);
/* 1169 */         unread('?');
/* 1170 */         throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */       }
/* 1172 */       return getToken();
/*      */     }
/*      */ 
/* 1176 */     unread('?');
/* 1177 */     throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */   }
/*      */ 
/*      */   void skipWhiteSpace()
/*      */     throws IOException
/*      */   {
/* 1186 */     while (this.data.ch <= ' ')
/* 1187 */       readChar();
/*      */   }
/*      */ 
/*      */   private void skipBlockComment()
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1197 */       int i = 0;
/* 1198 */       readChar();
/* 1199 */       while (i == 0)
/*      */       {
/* 1201 */         while (this.data.ch != '*')
/* 1202 */           readChar();
/* 1203 */         readChar();
/* 1204 */         if (this.data.ch == '/')
/* 1205 */           i = 1;
/*      */       }
/*      */     }
/*      */     catch (EOFException localEOFException)
/*      */     {
/* 1210 */       ParseException.unclosedComment(this.data.filename);
/* 1211 */       throw localEOFException;
/*      */     }
/*      */   }
/*      */ 
/*      */   void skipLineComment()
/*      */     throws IOException
/*      */   {
/* 1220 */     while (this.data.ch != '\n')
/* 1221 */       readChar();
/*      */   }
/*      */ 
/*      */   private String getLineComment()
/*      */     throws IOException
/*      */   {
/* 1232 */     StringBuffer localStringBuffer = new StringBuffer("/");
/* 1233 */     while (this.data.ch != '\n')
/*      */     {
/* 1235 */       if (this.data.ch != '\r')
/* 1236 */         localStringBuffer.append(this.data.ch);
/* 1237 */       readChar();
/*      */     }
/* 1239 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private String getBlockComment()
/*      */     throws IOException
/*      */   {
/* 1247 */     StringBuffer localStringBuffer = new StringBuffer("/*");
/*      */     try
/*      */     {
/* 1250 */       int i = 0;
/* 1251 */       readChar();
/* 1252 */       localStringBuffer.append(this.data.ch);
/* 1253 */       while (i == 0)
/*      */       {
/* 1255 */         while (this.data.ch != '*')
/*      */         {
/* 1257 */           readChar();
/* 1258 */           localStringBuffer.append(this.data.ch);
/*      */         }
/* 1260 */         readChar();
/* 1261 */         localStringBuffer.append(this.data.ch);
/* 1262 */         if (this.data.ch == '/')
/* 1263 */           i = 1;
/*      */       }
/*      */     }
/*      */     catch (EOFException localEOFException)
/*      */     {
/* 1268 */       ParseException.unclosedComment(this.data.filename);
/* 1269 */       throw localEOFException;
/*      */     }
/* 1271 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   Token skipUntil(char paramChar)
/*      */     throws IOException
/*      */   {
/* 1279 */     while (this.data.ch != paramChar)
/*      */     {
/* 1281 */       if (this.data.ch == '/')
/*      */       {
/* 1283 */         readChar();
/* 1284 */         if (this.data.ch == '/')
/*      */         {
/* 1286 */           skipLineComment();
/*      */ 
/* 1290 */           if (paramChar == '\n') break;
/*      */         }
/* 1292 */         else if (this.data.ch == '*') {
/* 1293 */           skipBlockComment();
/*      */         }
/*      */       } else {
/* 1296 */         readChar();
/*      */       }
/*      */     }
/* 1298 */     return getToken();
/*      */   }
/*      */ 
/*      */   String getUntil(char paramChar)
/*      */     throws IOException
/*      */   {
/* 1307 */     return getUntil(paramChar, true, true, true);
/*      */   }
/*      */ 
/*      */   String getUntil(char paramChar, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException
/*      */   {
/* 1312 */     String str = "";
/* 1313 */     while (this.data.ch != paramChar)
/* 1314 */       str = appendToString(str, paramBoolean1, paramBoolean2, paramBoolean3);
/* 1315 */     return str;
/*      */   }
/*      */ 
/*      */   String getUntil(char paramChar1, char paramChar2)
/*      */     throws IOException
/*      */   {
/* 1323 */     String str = "";
/* 1324 */     while ((this.data.ch != paramChar1) && (this.data.ch != paramChar2))
/* 1325 */       str = appendToString(str, false, false, false);
/* 1326 */     return str;
/*      */   }
/*      */ 
/*      */   private String appendToString(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/* 1335 */     if ((paramBoolean3) && (this.data.ch == '/'))
/*      */     {
/* 1337 */       readChar();
/* 1338 */       if (this.data.ch == '/')
/* 1339 */         skipLineComment();
/* 1340 */       else if (this.data.ch == '*')
/* 1341 */         skipBlockComment();
/*      */       else {
/* 1343 */         paramString = paramString + '/';
/*      */       }
/*      */     }
/* 1346 */     else if (this.data.ch == '\\')
/*      */     {
/* 1348 */       readChar();
/* 1349 */       if (this.data.ch == '\n') {
/* 1350 */         readChar();
/* 1351 */       } else if (this.data.ch == '\r')
/*      */       {
/* 1353 */         readChar();
/* 1354 */         if (this.data.ch == '\n')
/* 1355 */           readChar();
/*      */       }
/*      */       else
/*      */       {
/* 1359 */         paramString = paramString + '\\' + this.data.ch;
/* 1360 */         readChar();
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1367 */       if ((paramBoolean2) && (this.data.ch == '"'))
/*      */       {
/* 1369 */         readChar();
/* 1370 */         paramString = paramString + '"';
/* 1371 */       }while (this.data.ch != '"') {
/* 1372 */         paramString = appendToString(paramString, true, false, paramBoolean3); continue;
/*      */ 
/* 1374 */         if ((paramBoolean1) && (paramBoolean2) && (this.data.ch == '('))
/*      */         {
/* 1376 */           readChar();
/* 1377 */           paramString = paramString + '(';
/* 1378 */         }while (this.data.ch != ')') {
/* 1379 */           paramString = appendToString(paramString, false, false, paramBoolean3); continue;
/*      */ 
/* 1381 */           if ((paramBoolean1) && (this.data.ch == '\''))
/*      */           {
/* 1383 */             readChar();
/* 1384 */             paramString = paramString + "'";
/* 1385 */             while (this.data.ch != '\'')
/* 1386 */               paramString = appendToString(paramString, false, true, paramBoolean3); 
/*      */           }
/*      */         }
/*      */       }
/* 1388 */       paramString = paramString + this.data.ch;
/* 1389 */       readChar();
/*      */     }
/* 1391 */     return paramString;
/*      */   }
/*      */ 
/*      */   String getStringToEOL()
/*      */     throws IOException
/*      */   {
/* 1399 */     String str = new String();
/* 1400 */     while (this.data.ch != '\n')
/*      */     {
/* 1402 */       if (this.data.ch == '\\')
/*      */       {
/* 1404 */         readChar();
/* 1405 */         if (this.data.ch == '\n') {
/* 1406 */           readChar();
/* 1407 */         } else if (this.data.ch == '\r')
/*      */         {
/* 1409 */           readChar();
/* 1410 */           if (this.data.ch == '\n')
/* 1411 */             readChar();
/*      */         }
/*      */         else
/*      */         {
/* 1415 */           str = str + this.data.ch;
/* 1416 */           readChar();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1421 */         str = str + this.data.ch;
/* 1422 */         readChar();
/*      */       }
/*      */     }
/* 1425 */     return str;
/*      */   }
/*      */ 
/*      */   String filename()
/*      */   {
/* 1433 */     return this.data.filename;
/*      */   }
/*      */ 
/*      */   IncludeEntry fileEntry()
/*      */   {
/* 1441 */     return this.data.fileEntry;
/*      */   }
/*      */ 
/*      */   int currentLineNumber()
/*      */   {
/* 1449 */     return this.data.line;
/*      */   }
/*      */ 
/*      */   int lastTokenLineNumber()
/*      */   {
/* 1457 */     return this.data.oldLine;
/*      */   }
/*      */ 
/*      */   String currentLine()
/*      */   {
/* 1467 */     this.BOL = (this.data.fileIndex - 1);
/*      */     try
/*      */     {
/* 1473 */       if ((this.data.fileBytes[(this.BOL - 1)] == '\r') && (this.data.fileBytes[this.BOL] == '\n'))
/* 1474 */         this.BOL -= 2;
/* 1475 */       else if (this.data.fileBytes[this.BOL] == '\n')
/* 1476 */         this.BOL -= 1;
/* 1477 */       while (this.data.fileBytes[this.BOL] != '\n')
/* 1478 */         this.BOL -= 1;
/*      */     }
/*      */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException1)
/*      */     {
/* 1482 */       this.BOL = -1;
/*      */     }
/* 1484 */     this.BOL += 1;
/* 1485 */     int i = this.data.fileIndex - 1;
/*      */     try
/*      */     {
/* 1488 */       while ((this.data.fileBytes[i] != '\n') && (this.data.fileBytes[i] != '\r'))
/* 1489 */         i++;
/*      */     }
/*      */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException2)
/*      */     {
/* 1493 */       i = this.data.fileBytes.length;
/*      */     }
/* 1495 */     if (this.BOL < i) {
/* 1496 */       return new String(this.data.fileBytes, this.BOL, i - this.BOL);
/*      */     }
/* 1498 */     return "";
/*      */   }
/*      */ 
/*      */   String lastTokenLine()
/*      */   {
/* 1506 */     int i = this.data.fileIndex;
/* 1507 */     this.data.fileIndex = this.data.oldIndex;
/* 1508 */     String str = currentLine();
/* 1509 */     this.data.fileIndex = i;
/* 1510 */     return str;
/*      */   }
/*      */ 
/*      */   int currentLinePosition()
/*      */   {
/* 1518 */     return this.data.fileIndex - this.BOL;
/*      */   }
/*      */ 
/*      */   int lastTokenLinePosition()
/*      */   {
/* 1526 */     return this.data.oldIndex - this.BOL;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Scanner
 * JD-Core Version:    0.6.2
 */