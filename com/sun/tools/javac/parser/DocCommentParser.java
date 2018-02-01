/*      */ package com.sun.tools.javac.parser;
/*      */ 
/*      */ import com.sun.source.doctree.AttributeTree.ValueKind;
/*      */ import com.sun.source.doctree.DocTree.Kind;
/*      */ import com.sun.tools.javac.tree.DCTree;
/*      */ import com.sun.tools.javac.tree.DCTree.DCDocComment;
/*      */ import com.sun.tools.javac.tree.DCTree.DCEndElement;
/*      */ import com.sun.tools.javac.tree.DCTree.DCEndPosTree;
/*      */ import com.sun.tools.javac.tree.DCTree.DCErroneous;
/*      */ import com.sun.tools.javac.tree.DCTree.DCIdentifier;
/*      */ import com.sun.tools.javac.tree.DCTree.DCReference;
/*      */ import com.sun.tools.javac.tree.DCTree.DCStartElement;
/*      */ import com.sun.tools.javac.tree.DCTree.DCText;
/*      */ import com.sun.tools.javac.tree.DCTree.DCUnknownInlineTag;
/*      */ import com.sun.tools.javac.tree.DocTreeMaker;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DeferredDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.StringUtils;
/*      */ import java.text.BreakIterator;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class DocCommentParser
/*      */ {
/*      */   final ParserFactory fac;
/*      */   final DiagnosticSource diagSource;
/*      */   final Tokens.Comment comment;
/*      */   final DocTreeMaker m;
/*      */   final Names names;
/*      */   BreakIterator sentenceBreaker;
/*      */   protected char[] buf;
/*      */   protected int bp;
/*      */   protected int buflen;
/*      */   protected char ch;
/*   97 */   int textStart = -1;
/*   98 */   int lastNonWhite = -1;
/*   99 */   boolean newline = true;
/*      */   Map<Name, TagParser> tagParsers;
/*  993 */   Set<String> htmlBlockTags = new HashSet(Arrays.asList(new String[] { "h1", "h2", "h3", "h4", "h5", "h6", "p", "pre" }));
/*      */ 
/*      */   DocCommentParser(ParserFactory paramParserFactory, DiagnosticSource paramDiagnosticSource, Tokens.Comment paramComment)
/*      */   {
/*  104 */     this.fac = paramParserFactory;
/*  105 */     this.diagSource = paramDiagnosticSource;
/*  106 */     this.comment = paramComment;
/*  107 */     this.names = paramParserFactory.names;
/*  108 */     this.m = paramParserFactory.docTreeMaker;
/*      */ 
/*  110 */     Locale localLocale = paramParserFactory.locale == null ? Locale.getDefault() : paramParserFactory.locale;
/*      */ 
/*  112 */     Options localOptions = paramParserFactory.options;
/*  113 */     boolean bool = localOptions.isSet("breakIterator");
/*  114 */     if ((bool) || (!localLocale.getLanguage().equals(Locale.ENGLISH.getLanguage()))) {
/*  115 */       this.sentenceBreaker = BreakIterator.getSentenceInstance(localLocale);
/*      */     }
/*  117 */     initTagParsers();
/*      */   }
/*      */ 
/*      */   DCTree.DCDocComment parse() {
/*  121 */     String str1 = this.comment.getText();
/*  122 */     this.buf = new char[str1.length() + 1];
/*  123 */     str1.getChars(0, str1.length(), this.buf, 0);
/*  124 */     this.buf[(this.buf.length - 1)] = '\032';
/*  125 */     this.buflen = (this.buf.length - 1);
/*  126 */     this.bp = -1;
/*  127 */     nextChar();
/*      */ 
/*  129 */     List localList1 = blockContent();
/*  130 */     List localList2 = blockTags();
/*      */ 
/*  133 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/*  135 */     for (; localList1.nonEmpty(); localList1 = localList1.tail) {
/*  136 */       localDCTree = (DCTree)localList1.head;
/*  137 */       switch (20.$SwitchMap$com$sun$source$doctree$DocTree$Kind[localDCTree.getKind().ordinal()]) {
/*      */       case 1:
/*  139 */         String str2 = ((DCTree.DCText)localDCTree).getBody();
/*  140 */         int j = getSentenceBreak(str2);
/*      */         int k;
/*  141 */         if (j > 0) {
/*  142 */           k = j;
/*  143 */           while ((k > 0) && (isWhitespace(str2.charAt(k - 1))))
/*  144 */             k--;
/*  145 */           localListBuffer.add(this.m.at(localDCTree.pos).Text(str2.substring(0, k)));
/*  146 */           int n = j;
/*  147 */           while ((n < str2.length()) && (isWhitespace(str2.charAt(n))))
/*  148 */             n++;
/*  149 */           localList1 = localList1.tail;
/*  150 */           if (n < str2.length())
/*  151 */             localList1 = localList1.prepend(this.m.at(localDCTree.pos + n).Text(str2.substring(n)));
/*      */         }
/*  153 */         else if ((localList1.tail.nonEmpty()) && 
/*  154 */           (isSentenceBreak((DCTree)localList1.tail.head))) {
/*  155 */           k = str2.length() - 1;
/*  156 */           while ((k > 0) && (isWhitespace(str2.charAt(k))))
/*  157 */             k--;
/*  158 */           localListBuffer.add(this.m.at(localDCTree.pos).Text(str2.substring(0, k + 1)));
/*  159 */           localList1 = localList1.tail;
/*  160 */         }break;
/*      */       case 2:
/*      */       case 3:
/*  167 */         if (isSentenceBreak(localDCTree)) {
/*      */           break label437;
/*      */         }
/*      */       }
/*  171 */       localListBuffer.add(localDCTree);
/*      */     }
/*      */ 
/*  175 */     label437: DCTree localDCTree = (DCTree)getFirst(new List[] { localListBuffer.toList(), localList1, localList2 });
/*  176 */     int i = localDCTree == null ? -1 : localDCTree.pos;
/*      */ 
/*  178 */     DCTree.DCDocComment localDCDocComment = this.m.at(i).DocComment(this.comment, localListBuffer.toList(), localList1, localList2);
/*  179 */     return localDCDocComment;
/*      */   }
/*      */ 
/*      */   void nextChar() {
/*  183 */     this.ch = this.buf[this.buflen];
/*  184 */     switch (this.ch) { case '\n':
/*      */     case '\f':
/*      */     case '\r':
/*  186 */       this.newline = true;
/*      */     case '\013':
/*      */     }
/*      */   }
/*      */ 
/*      */   protected List<DCTree> blockContent()
/*      */   {
/*  197 */     ListBuffer localListBuffer = new ListBuffer();
/*  198 */     this.textStart = -1;
/*      */ 
/*  201 */     while (this.bp < this.buflen) {
/*  202 */       switch (this.ch) { case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*  204 */         this.newline = true;
/*      */       case '\t':
/*      */       case ' ':
/*  208 */         nextChar();
/*  209 */         break;
/*      */       case '&':
/*  212 */         entity(localListBuffer);
/*  213 */         break;
/*      */       case '<':
/*  216 */         this.newline = false;
/*  217 */         addPendingText(localListBuffer, this.bp - 1);
/*  218 */         localListBuffer.add(html());
/*  219 */         if (this.textStart == -1) {
/*  220 */           this.textStart = this.bp;
/*  221 */           this.lastNonWhite = -1; } break;
/*      */       case '>':
/*  226 */         this.newline = false;
/*  227 */         addPendingText(localListBuffer, this.bp - 1);
/*  228 */         localListBuffer.add(this.m.at(this.bp).Erroneous(newString(this.bp, this.bp + 1), this.diagSource, "dc.bad.gt", new Object[0]));
/*  229 */         nextChar();
/*  230 */         if (this.textStart == -1) {
/*  231 */           this.textStart = this.bp;
/*  232 */           this.lastNonWhite = -1; } break;
/*      */       case '{':
/*  237 */         inlineTag(localListBuffer);
/*  238 */         break;
/*      */       case '@':
/*  241 */         if (this.newline)
/*  242 */           addPendingText(localListBuffer, this.lastNonWhite);
/*  243 */         break;
/*      */       default:
/*  248 */         this.newline = false;
/*  249 */         if (this.textStart == -1)
/*  250 */           this.textStart = this.bp;
/*  251 */         this.lastNonWhite = this.bp;
/*  252 */         nextChar();
/*      */       }
/*      */     }
/*      */ 
/*  256 */     if (this.lastNonWhite != -1) {
/*  257 */       addPendingText(localListBuffer, this.lastNonWhite);
/*      */     }
/*  259 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   protected List<DCTree> blockTags()
/*      */   {
/*  268 */     ListBuffer localListBuffer = new ListBuffer();
/*  269 */     while (this.ch == '@')
/*  270 */       localListBuffer.add(blockTag());
/*  271 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   protected DCTree blockTag()
/*      */   {
/*  280 */     int i = this.bp;
/*      */     try {
/*  282 */       nextChar();
/*  283 */       if (isIdentifierStart(this.ch)) {
/*  284 */         Name localName = readTagName();
/*  285 */         TagParser localTagParser = (TagParser)this.tagParsers.get(localName);
/*  286 */         if (localTagParser == null) {
/*  287 */           List localList = blockContent();
/*  288 */           return this.m.at(i).UnknownBlockTag(localName, localList);
/*      */         }
/*  290 */         switch (localTagParser.getKind()) {
/*      */         case BLOCK:
/*  292 */           return localTagParser.parse(i);
/*      */         case INLINE:
/*  294 */           return erroneous("dc.bad.inline.tag", i);
/*      */         }
/*      */       }
/*      */ 
/*  298 */       blockContent();
/*      */ 
/*  300 */       return erroneous("dc.no.tag.name", i);
/*      */     } catch (ParseException localParseException) {
/*  302 */       blockContent();
/*  303 */       return erroneous(localParseException.getMessage(), i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void inlineTag(ListBuffer<DCTree> paramListBuffer) {
/*  308 */     this.newline = false;
/*  309 */     nextChar();
/*  310 */     if (this.ch == '@') {
/*  311 */       addPendingText(paramListBuffer, this.bp - 2);
/*  312 */       paramListBuffer.add(inlineTag());
/*  313 */       this.textStart = this.bp;
/*  314 */       this.lastNonWhite = -1;
/*      */     } else {
/*  316 */       if (this.textStart == -1)
/*  317 */         this.textStart = (this.bp - 1);
/*  318 */       this.lastNonWhite = this.bp;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DCTree inlineTag()
/*      */   {
/*  329 */     int i = this.bp - 1;
/*      */     try {
/*  331 */       nextChar();
/*  332 */       if (isIdentifierStart(this.ch)) {
/*  333 */         Name localName = readTagName();
/*  334 */         skipWhitespace();
/*      */ 
/*  336 */         TagParser localTagParser = (TagParser)this.tagParsers.get(localName);
/*      */         Object localObject;
/*  337 */         if (localTagParser == null) {
/*  338 */           localObject = inlineText();
/*  339 */           if (localObject != null) {
/*  340 */             nextChar();
/*  341 */             return this.m.at(i).UnknownInlineTag(localName, List.of(localObject)).setEndPos(this.bp);
/*      */           }
/*  343 */         } else if (localTagParser.getKind() == DocCommentParser.TagParser.Kind.INLINE) {
/*  344 */           localObject = (DCTree.DCEndPosTree)localTagParser.parse(i);
/*  345 */           if (localObject != null)
/*  346 */             return ((DCTree.DCEndPosTree)localObject).setEndPos(this.bp);
/*      */         }
/*      */         else {
/*  349 */           inlineText();
/*  350 */           nextChar();
/*      */         }
/*      */       }
/*  353 */       return erroneous("dc.no.tag.name", i);
/*      */     } catch (ParseException localParseException) {
/*  355 */       return erroneous(localParseException.getMessage(), i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DCTree inlineText()
/*      */     throws DocCommentParser.ParseException
/*      */   {
/*  365 */     skipWhitespace();
/*  366 */     int i = this.bp;
/*  367 */     int j = 1;
/*      */ 
/*  370 */     while (this.bp < this.buflen) {
/*  371 */       switch (this.ch) { case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*  373 */         this.newline = true;
/*  374 */         break;
/*      */       case '\t':
/*      */       case ' ':
/*  377 */         break;
/*      */       case '{':
/*  380 */         this.newline = false;
/*  381 */         this.lastNonWhite = this.bp;
/*  382 */         j++;
/*  383 */         break;
/*      */       case '}':
/*  386 */         j--; if (j == 0) {
/*  387 */           return this.m.at(i).Text(newString(i, this.bp));
/*      */         }
/*  389 */         this.newline = false;
/*  390 */         this.lastNonWhite = this.bp;
/*  391 */         break;
/*      */       case '@':
/*  394 */         if (this.newline)
/*      */           break label220;
/*  396 */         this.newline = false;
/*  397 */         this.lastNonWhite = this.bp;
/*  398 */         break;
/*      */       default:
/*  401 */         this.newline = false;
/*  402 */         this.lastNonWhite = this.bp;
/*      */       }
/*      */ 
/*  405 */       nextChar();
/*      */     }
/*  407 */     label220: throw new ParseException("dc.unterminated.inline.tag");
/*      */   }
/*      */ 
/*      */   protected DCTree.DCReference reference(boolean paramBoolean)
/*      */     throws DocCommentParser.ParseException
/*      */   {
/*  419 */     int i = this.bp;
/*  420 */     int j = 0;
/*      */ 
/*  425 */     while (this.bp < this.buflen) {
/*  426 */       switch (this.ch) { case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*  428 */         this.newline = true;
/*      */       case '\t':
/*      */       case ' ':
/*  432 */         if (j != 0) break;
/*  433 */         break;
/*      */       case '(':
/*      */       case '<':
/*  438 */         this.newline = false;
/*  439 */         j++;
/*  440 */         break;
/*      */       case ')':
/*      */       case '>':
/*  444 */         this.newline = false;
/*  445 */         j--;
/*  446 */         break;
/*      */       case '}':
/*  449 */         if (this.bp == i)
/*  450 */           return null;
/*  451 */         this.newline = false;
/*  452 */         break;
/*      */       case '@':
/*  455 */         if (this.newline)
/*      */         {
/*      */           break label194;
/*      */         }
/*      */       default:
/*  460 */         this.newline = false;
/*      */       }
/*      */ 
/*  463 */       nextChar();
/*      */     }
/*      */ 
/*  466 */     label194: if (j != 0) {
/*  467 */       throw new ParseException("dc.unterminated.signature");
/*      */     }
/*  469 */     String str = newString(i, this.bp);
/*      */ 
/*  476 */     Log.DeferredDiagnosticHandler localDeferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.fac.log);
/*      */     JCTree localJCTree;
/*      */     Name localName;
/*      */     List localList;
/*      */     try {
/*  480 */       int k = str.indexOf("#");
/*  481 */       int n = str.indexOf("(", k + 1);
/*  482 */       if (k == -1) {
/*  483 */         if (n == -1) {
/*  484 */           localJCTree = parseType(str);
/*  485 */           localName = null;
/*      */         } else {
/*  487 */           localJCTree = null;
/*  488 */           localName = parseMember(str.substring(0, n));
/*      */         }
/*      */       } else {
/*  491 */         localJCTree = k == 0 ? null : parseType(str.substring(0, k));
/*  492 */         if (n == -1)
/*  493 */           localName = parseMember(str.substring(k + 1));
/*      */         else {
/*  495 */           localName = parseMember(str.substring(k + 1, n));
/*      */         }
/*      */       }
/*  498 */       if (n < 0) {
/*  499 */         localList = null;
/*      */       } else {
/*  501 */         int i1 = str.indexOf(")", n);
/*  502 */         if (i1 != str.length() - 1)
/*  503 */           throw new ParseException("dc.ref.bad.parens");
/*  504 */         localList = parseParams(str.substring(n + 1, i1));
/*      */       }
/*      */ 
/*  507 */       if (!localDeferredDiagnosticHandler.getDiagnostics().isEmpty())
/*  508 */         throw new ParseException("dc.ref.syntax.error");
/*      */     }
/*      */     finally {
/*  511 */       this.fac.log.popDiagnosticHandler(localDeferredDiagnosticHandler);
/*      */     }
/*      */ 
/*  514 */     return (DCTree.DCReference)this.m.at(i).Reference(str, localJCTree, localName, localList).setEndPos(this.bp);
/*      */   }
/*      */ 
/*      */   JCTree parseType(String paramString) throws DocCommentParser.ParseException {
/*  518 */     JavacParser localJavacParser = this.fac.newParser(paramString, false, false, false);
/*  519 */     JCTree.JCExpression localJCExpression = localJavacParser.parseType();
/*  520 */     if (localJavacParser.token().kind != Tokens.TokenKind.EOF)
/*  521 */       throw new ParseException("dc.ref.unexpected.input");
/*  522 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   Name parseMember(String paramString) throws DocCommentParser.ParseException {
/*  526 */     JavacParser localJavacParser = this.fac.newParser(paramString, false, false, false);
/*  527 */     Name localName = localJavacParser.ident();
/*  528 */     if (localJavacParser.token().kind != Tokens.TokenKind.EOF)
/*  529 */       throw new ParseException("dc.ref.unexpected.input");
/*  530 */     return localName;
/*      */   }
/*      */ 
/*      */   List<JCTree> parseParams(String paramString) throws DocCommentParser.ParseException {
/*  534 */     if (paramString.trim().isEmpty()) {
/*  535 */       return List.nil();
/*      */     }
/*  537 */     JavacParser localJavacParser = this.fac.newParser(paramString.replace("...", "[]"), false, false, false);
/*  538 */     ListBuffer localListBuffer = new ListBuffer();
/*  539 */     localListBuffer.add(localJavacParser.parseType());
/*      */ 
/*  541 */     if (localJavacParser.token().kind == Tokens.TokenKind.IDENTIFIER) {
/*  542 */       localJavacParser.nextToken();
/*      */     }
/*  544 */     while (localJavacParser.token().kind == Tokens.TokenKind.COMMA) {
/*  545 */       localJavacParser.nextToken();
/*  546 */       localListBuffer.add(localJavacParser.parseType());
/*      */ 
/*  548 */       if (localJavacParser.token().kind == Tokens.TokenKind.IDENTIFIER) {
/*  549 */         localJavacParser.nextToken();
/*      */       }
/*      */     }
/*  552 */     if (localJavacParser.token().kind != Tokens.TokenKind.EOF) {
/*  553 */       throw new ParseException("dc.ref.unexpected.input");
/*      */     }
/*  555 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   protected DCTree.DCIdentifier identifier()
/*      */     throws DocCommentParser.ParseException
/*      */   {
/*  565 */     skipWhitespace();
/*  566 */     int i = this.bp;
/*      */ 
/*  568 */     if (isJavaIdentifierStart(this.ch)) {
/*  569 */       Name localName = readJavaIdentifier();
/*  570 */       return this.m.at(i).Identifier(localName);
/*      */     }
/*      */ 
/*  573 */     throw new ParseException("dc.identifier.expected");
/*      */   }
/*      */ 
/*      */   protected DCTree.DCText quotedString()
/*      */   {
/*  582 */     int i = this.bp;
/*  583 */     nextChar();
/*      */ 
/*  586 */     while (this.bp < this.buflen) {
/*  587 */       switch (this.ch) { case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*  589 */         this.newline = true;
/*  590 */         break;
/*      */       case '\t':
/*      */       case ' ':
/*  593 */         break;
/*      */       case '"':
/*  596 */         nextChar();
/*      */ 
/*  598 */         return this.m.at(i).Text(newString(i, this.bp));
/*      */       case '@':
/*  601 */         if (this.newline) {
/*      */           break label145;
/*      */         }
/*      */       }
/*  605 */       nextChar();
/*      */     }
/*  607 */     label145: return null;
/*      */   }
/*      */ 
/*      */   protected List<DCTree> inlineContent()
/*      */   {
/*  617 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/*  619 */     skipWhitespace();
/*  620 */     int i = this.bp;
/*  621 */     int j = 1;
/*  622 */     this.textStart = -1;
/*      */ 
/*  625 */     while (this.bp < this.buflen)
/*      */     {
/*  627 */       switch (this.ch) { case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*  629 */         this.newline = true;
/*      */       case '\t':
/*      */       case ' ':
/*  633 */         nextChar();
/*  634 */         break;
/*      */       case '&':
/*  637 */         entity(localListBuffer);
/*  638 */         break;
/*      */       case '<':
/*  641 */         this.newline = false;
/*  642 */         addPendingText(localListBuffer, this.bp - 1);
/*  643 */         localListBuffer.add(html());
/*  644 */         break;
/*      */       case '{':
/*  647 */         this.newline = false;
/*  648 */         j++;
/*  649 */         nextChar();
/*  650 */         break;
/*      */       case '}':
/*  653 */         this.newline = false;
/*  654 */         j--; if (j == 0) {
/*  655 */           addPendingText(localListBuffer, this.bp - 1);
/*  656 */           nextChar();
/*  657 */           return localListBuffer.toList();
/*      */         }
/*  659 */         nextChar();
/*  660 */         break;
/*      */       case '@':
/*  663 */         if (this.newline)
/*      */         {
/*      */           break label263;
/*      */         }
/*      */       default:
/*  668 */         if (this.textStart == -1)
/*  669 */           this.textStart = this.bp;
/*  670 */         nextChar();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  675 */     label263: return List.of(erroneous("dc.unterminated.inline.tag", i));
/*      */   }
/*      */ 
/*      */   protected void entity(ListBuffer<DCTree> paramListBuffer) {
/*  679 */     this.newline = false;
/*  680 */     addPendingText(paramListBuffer, this.bp - 1);
/*  681 */     paramListBuffer.add(entity());
/*  682 */     if (this.textStart == -1) {
/*  683 */       this.textStart = this.bp;
/*  684 */       this.lastNonWhite = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DCTree entity()
/*      */   {
/*  693 */     int i = this.bp;
/*  694 */     nextChar();
/*  695 */     Name localName = null;
/*  696 */     int j = 0;
/*  697 */     if (this.ch == '#') {
/*  698 */       int k = this.bp;
/*  699 */       nextChar();
/*  700 */       if (isDecimalDigit(this.ch)) {
/*  701 */         nextChar();
/*  702 */         while (isDecimalDigit(this.ch))
/*  703 */           nextChar();
/*  704 */         localName = this.names.fromChars(this.buf, k, this.bp - k);
/*  705 */       } else if ((this.ch == 'x') || (this.ch == 'X')) {
/*  706 */         nextChar();
/*  707 */         if (isHexDigit(this.ch)) {
/*  708 */           nextChar();
/*  709 */           while (isHexDigit(this.ch))
/*  710 */             nextChar();
/*  711 */           localName = this.names.fromChars(this.buf, k, this.bp - k);
/*      */         }
/*      */       }
/*  714 */     } else if (isIdentifierStart(this.ch)) {
/*  715 */       localName = readIdentifier();
/*      */     }
/*      */ 
/*  718 */     if (localName == null) {
/*  719 */       return erroneous("dc.bad.entity", i);
/*      */     }
/*  721 */     if (this.ch != ';')
/*  722 */       return erroneous("dc.missing.semicolon", i);
/*  723 */     nextChar();
/*  724 */     return this.m.at(i).Entity(localName);
/*      */   }
/*      */ 
/*      */   protected DCTree html()
/*      */   {
/*  733 */     int i = this.bp;
/*  734 */     nextChar();
/*      */     Name localName;
/*  735 */     if (isIdentifierStart(this.ch)) {
/*  736 */       localName = readIdentifier();
/*  737 */       List localList = htmlAttrs();
/*  738 */       if (localList != null) {
/*  739 */         boolean bool = false;
/*  740 */         if (this.ch == '/') {
/*  741 */           nextChar();
/*  742 */           bool = true;
/*      */         }
/*  744 */         if (this.ch == '>') {
/*  745 */           nextChar();
/*  746 */           return this.m.at(i).StartElement(localName, localList, bool).setEndPos(this.bp);
/*      */         }
/*      */       }
/*  749 */     } else if (this.ch == '/') {
/*  750 */       nextChar();
/*  751 */       if (isIdentifierStart(this.ch)) {
/*  752 */         localName = readIdentifier();
/*  753 */         skipWhitespace();
/*  754 */         if (this.ch == '>') {
/*  755 */           nextChar();
/*  756 */           return this.m.at(i).EndElement(localName);
/*      */         }
/*      */       }
/*  759 */     } else if (this.ch == '!') {
/*  760 */       nextChar();
/*  761 */       if (this.ch == '-') {
/*  762 */         nextChar();
/*  763 */         if (this.ch == '-') {
/*  764 */           nextChar();
/*  765 */           while (this.bp < this.buflen) {
/*  766 */             int j = 0;
/*  767 */             while (this.ch == '-') {
/*  768 */               j++;
/*  769 */               nextChar();
/*      */             }
/*      */ 
/*  773 */             if ((j >= 2) && (this.ch == '>')) {
/*  774 */               nextChar();
/*  775 */               return this.m.at(i).Comment(newString(i, this.bp));
/*      */             }
/*      */ 
/*  778 */             nextChar();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  784 */     this.bp = (i + 1);
/*  785 */     this.ch = this.buf[this.bp];
/*  786 */     return erroneous("dc.malformed.html", i);
/*      */   }
/*      */ 
/*      */   protected List<DCTree> htmlAttrs()
/*      */   {
/*  795 */     ListBuffer localListBuffer = new ListBuffer();
/*  796 */     skipWhitespace();
/*      */ 
/*  799 */     while (isIdentifierStart(this.ch)) {
/*  800 */       int i = this.bp;
/*  801 */       Name localName = readIdentifier();
/*  802 */       skipWhitespace();
/*  803 */       List localList = null;
/*  804 */       AttributeTree.ValueKind localValueKind = AttributeTree.ValueKind.EMPTY;
/*  805 */       if (this.ch == '=') {
/*  806 */         localObject = new ListBuffer();
/*  807 */         nextChar();
/*  808 */         skipWhitespace();
/*  809 */         if ((this.ch == '\'') || (this.ch == '"')) {
/*  810 */           localValueKind = this.ch == '\'' ? AttributeTree.ValueKind.SINGLE : AttributeTree.ValueKind.DOUBLE;
/*  811 */           int j = this.ch;
/*  812 */           nextChar();
/*  813 */           this.textStart = this.bp;
/*  814 */           while ((this.bp < this.buflen) && (this.ch != j)) {
/*  815 */             if ((this.newline) && (this.ch == '@')) {
/*  816 */               localListBuffer.add(erroneous("dc.unterminated.string", i));
/*      */ 
/*  821 */               break label301;
/*      */             }
/*  823 */             attrValueChar((ListBuffer)localObject);
/*      */           }
/*  825 */           addPendingText((ListBuffer)localObject, this.bp - 1);
/*  826 */           nextChar();
/*      */         } else {
/*  828 */           localValueKind = AttributeTree.ValueKind.UNQUOTED;
/*  829 */           this.textStart = this.bp;
/*  830 */           while ((this.bp < this.buflen) && (!isUnquotedAttrValueTerminator(this.ch))) {
/*  831 */             attrValueChar((ListBuffer)localObject);
/*      */           }
/*  833 */           addPendingText((ListBuffer)localObject, this.bp - 1);
/*      */         }
/*  835 */         skipWhitespace();
/*  836 */         localList = ((ListBuffer)localObject).toList();
/*      */       }
/*  838 */       Object localObject = this.m.at(i).Attribute(localName, localValueKind, localList);
/*  839 */       localListBuffer.add(localObject);
/*      */     }
/*      */ 
/*  842 */     label301: return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   protected void attrValueChar(ListBuffer<DCTree> paramListBuffer) {
/*  846 */     switch (this.ch) {
/*      */     case '&':
/*  848 */       entity(paramListBuffer);
/*  849 */       break;
/*      */     case '{':
/*  852 */       inlineTag(paramListBuffer);
/*  853 */       break;
/*      */     default:
/*  856 */       nextChar();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addPendingText(ListBuffer<DCTree> paramListBuffer, int paramInt) {
/*  861 */     if (this.textStart != -1) {
/*  862 */       if (this.textStart <= paramInt) {
/*  863 */         paramListBuffer.add(this.m.at(this.textStart).Text(newString(this.textStart, paramInt + 1)));
/*      */       }
/*  865 */       this.textStart = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DCTree.DCErroneous erroneous(String paramString, int paramInt) {
/*  870 */     int i = this.bp - 1;
/*      */ 
/*  872 */     while (i > paramInt) {
/*  873 */       switch (this.buf[i]) { case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*  875 */         this.newline = true;
/*  876 */         break;
/*      */       case '\t':
/*      */       case ' ':
/*  878 */         break;
/*      */       default:
/*  880 */         break;
/*      */       }
/*  882 */       i--;
/*      */     }
/*  884 */     this.textStart = -1;
/*  885 */     return this.m.at(paramInt).Erroneous(newString(paramInt, i + 1), this.diagSource, paramString, new Object[0]);
/*      */   }
/*      */ 
/*      */   <T> T getFirst(List<T>[] paramArrayOfList)
/*      */   {
/*  890 */     for (List<T> localList : paramArrayOfList) {
/*  891 */       if (localList.nonEmpty())
/*  892 */         return localList.head;
/*      */     }
/*  894 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean isIdentifierStart(char paramChar) {
/*  898 */     return Character.isUnicodeIdentifierStart(paramChar);
/*      */   }
/*      */ 
/*      */   protected Name readIdentifier() {
/*  902 */     int i = this.bp;
/*  903 */     nextChar();
/*  904 */     while ((this.bp < this.buflen) && (Character.isUnicodeIdentifierPart(this.ch)))
/*  905 */       nextChar();
/*  906 */     return this.names.fromChars(this.buf, i, this.bp - i);
/*      */   }
/*      */ 
/*      */   protected Name readTagName() {
/*  910 */     int i = this.bp;
/*  911 */     nextChar();
/*  912 */     while ((this.bp < this.buflen) && ((Character.isUnicodeIdentifierPart(this.ch)) || (this.ch == '.')))
/*  913 */       nextChar();
/*  914 */     return this.names.fromChars(this.buf, i, this.bp - i);
/*      */   }
/*      */ 
/*      */   protected boolean isJavaIdentifierStart(char paramChar) {
/*  918 */     return Character.isJavaIdentifierStart(paramChar);
/*      */   }
/*      */ 
/*      */   protected Name readJavaIdentifier() {
/*  922 */     int i = this.bp;
/*  923 */     nextChar();
/*  924 */     while ((this.bp < this.buflen) && (Character.isJavaIdentifierPart(this.ch)))
/*  925 */       nextChar();
/*  926 */     return this.names.fromChars(this.buf, i, this.bp - i);
/*      */   }
/*      */ 
/*      */   protected boolean isDecimalDigit(char paramChar) {
/*  930 */     return ('0' <= paramChar) && (paramChar <= '9');
/*      */   }
/*      */ 
/*      */   protected boolean isHexDigit(char paramChar) {
/*  934 */     return (('0' <= paramChar) && (paramChar <= '9')) || (('a' <= paramChar) && (paramChar <= 'f')) || (('A' <= paramChar) && (paramChar <= 'F'));
/*      */   }
/*      */ 
/*      */   protected boolean isUnquotedAttrValueTerminator(char paramChar)
/*      */   {
/*  940 */     switch (paramChar) { case '\t':
/*      */     case '\n':
/*      */     case '\f':
/*      */     case '\r':
/*      */     case ' ':
/*      */     case '"':
/*      */     case '\'':
/*      */     case '<':
/*      */     case '=':
/*      */     case '>':
/*      */     case '`':
/*  945 */       return true;
/*      */     }
/*  947 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isWhitespace(char paramChar)
/*      */   {
/*  952 */     return Character.isWhitespace(paramChar);
/*      */   }
/*      */ 
/*      */   protected void skipWhitespace() {
/*  956 */     while (isWhitespace(this.ch))
/*  957 */       nextChar();
/*      */   }
/*      */ 
/*      */   protected int getSentenceBreak(String paramString) {
/*  961 */     if (this.sentenceBreaker != null) {
/*  962 */       this.sentenceBreaker.setText(paramString);
/*  963 */       i = this.sentenceBreaker.next();
/*  964 */       return i == paramString.length() ? -1 : i;
/*      */     }
/*      */ 
/*  968 */     int i = 0;
/*  969 */     for (int j = 0; j < paramString.length(); j++) {
/*  970 */       switch (paramString.charAt(j)) {
/*      */       case '.':
/*  972 */         i = 1;
/*  973 */         break;
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*      */       case ' ':
/*  980 */         if (i != 0) {
/*  981 */           return j;
/*      */         }
/*      */         break;
/*      */       default:
/*  985 */         i = 0;
/*      */       }
/*      */     }
/*      */ 
/*  989 */     return -1;
/*      */   }
/*      */ 
/*      */   protected boolean isSentenceBreak(Name paramName)
/*      */   {
/*  997 */     return this.htmlBlockTags.contains(StringUtils.toLowerCase(paramName.toString()));
/*      */   }
/*      */ 
/*      */   protected boolean isSentenceBreak(DCTree paramDCTree) {
/* 1001 */     switch (paramDCTree.getKind()) {
/*      */     case START_ELEMENT:
/* 1003 */       return isSentenceBreak(((DCTree.DCStartElement)paramDCTree).getName());
/*      */     case END_ELEMENT:
/* 1006 */       return isSentenceBreak(((DCTree.DCEndElement)paramDCTree).getName());
/*      */     }
/* 1008 */     return false;
/*      */   }
/*      */ 
/*      */   String newString(int paramInt1, int paramInt2)
/*      */   {
/* 1016 */     return new String(this.buf, paramInt1, paramInt2 - paramInt1);
/*      */   }
/*      */ 
/*      */   private void initTagParsers()
/*      */   {
/* 1045 */     TagParser[] arrayOfTagParser1 = { new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.AUTHOR)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1049 */         List localList = DocCommentParser.this.blockContent();
/* 1050 */         return DocCommentParser.this.m.at(paramAnonymousInt).Author(localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.CODE)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1057 */         DCTree localDCTree = DocCommentParser.this.inlineText();
/* 1058 */         DocCommentParser.this.nextChar();
/* 1059 */         return DocCommentParser.this.m.at(paramAnonymousInt).Code((DCTree.DCText)localDCTree);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.DEPRECATED)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1066 */         List localList = DocCommentParser.this.blockContent();
/* 1067 */         return DocCommentParser.this.m.at(paramAnonymousInt).Deprecated(localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.DOC_ROOT)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1074 */         if (DocCommentParser.this.ch == '}') {
/* 1075 */           DocCommentParser.this.nextChar();
/* 1076 */           return DocCommentParser.this.m.at(paramAnonymousInt).DocRoot();
/*      */         }
/* 1078 */         DocCommentParser.this.inlineText();
/* 1079 */         DocCommentParser.this.nextChar();
/* 1080 */         throw new DocCommentParser.ParseException("dc.unexpected.content");
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.EXCEPTION)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1087 */         DocCommentParser.this.skipWhitespace();
/* 1088 */         DCTree.DCReference localDCReference = DocCommentParser.this.reference(false);
/* 1089 */         List localList = DocCommentParser.this.blockContent();
/* 1090 */         return DocCommentParser.this.m.at(paramAnonymousInt).Exception(localDCReference, localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.INHERIT_DOC)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1097 */         if (DocCommentParser.this.ch == '}') {
/* 1098 */           DocCommentParser.this.nextChar();
/* 1099 */           return DocCommentParser.this.m.at(paramAnonymousInt).InheritDoc();
/*      */         }
/* 1101 */         DocCommentParser.this.inlineText();
/* 1102 */         DocCommentParser.this.nextChar();
/* 1103 */         throw new DocCommentParser.ParseException("dc.unexpected.content");
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.LINK)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1110 */         DCTree.DCReference localDCReference = DocCommentParser.this.reference(true);
/* 1111 */         List localList = DocCommentParser.this.inlineContent();
/* 1112 */         return DocCommentParser.this.m.at(paramAnonymousInt).Link(localDCReference, localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.LINK_PLAIN)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1119 */         DCTree.DCReference localDCReference = DocCommentParser.this.reference(true);
/* 1120 */         List localList = DocCommentParser.this.inlineContent();
/* 1121 */         return DocCommentParser.this.m.at(paramAnonymousInt).LinkPlain(localDCReference, localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.LITERAL)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1128 */         DCTree localDCTree = DocCommentParser.this.inlineText();
/* 1129 */         DocCommentParser.this.nextChar();
/* 1130 */         return DocCommentParser.this.m.at(paramAnonymousInt).Literal((DCTree.DCText)localDCTree);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.PARAM)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1137 */         DocCommentParser.this.skipWhitespace();
/*      */ 
/* 1139 */         boolean bool = false;
/* 1140 */         if (DocCommentParser.this.ch == '<') {
/* 1141 */           bool = true;
/* 1142 */           DocCommentParser.this.nextChar();
/*      */         }
/*      */ 
/* 1145 */         DCTree.DCIdentifier localDCIdentifier = DocCommentParser.this.identifier();
/*      */ 
/* 1147 */         if (bool) {
/* 1148 */           if (DocCommentParser.this.ch != '>')
/* 1149 */             throw new DocCommentParser.ParseException("dc.gt.expected");
/* 1150 */           DocCommentParser.this.nextChar();
/*      */         }
/*      */ 
/* 1153 */         DocCommentParser.this.skipWhitespace();
/* 1154 */         List localList = DocCommentParser.this.blockContent();
/* 1155 */         return DocCommentParser.this.m.at(paramAnonymousInt).Param(bool, localDCIdentifier, localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.RETURN)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1162 */         List localList = DocCommentParser.this.blockContent();
/* 1163 */         return DocCommentParser.this.m.at(paramAnonymousInt).Return(localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.SEE)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1170 */         DocCommentParser.this.skipWhitespace();
/* 1171 */         switch (DocCommentParser.this.ch) {
/*      */         case '"':
/* 1173 */           DCTree.DCText localDCText = DocCommentParser.this.quotedString();
/* 1174 */           if (localDCText != null) {
/* 1175 */             DocCommentParser.this.skipWhitespace();
/* 1176 */             if ((DocCommentParser.this.ch == '@') || ((DocCommentParser.this.ch == '\032') && (DocCommentParser.this.bp == DocCommentParser.this.buf.length - 1)))
/*      */             {
/* 1178 */               return DocCommentParser.this.m.at(paramAnonymousInt).See(List.of(localDCText));
/*      */             }
/*      */           }
/*      */ 
/*      */           break;
/*      */         case '<':
/* 1184 */           List localList1 = DocCommentParser.this.blockContent();
/* 1185 */           if (localList1 != null) {
/* 1186 */             return DocCommentParser.this.m.at(paramAnonymousInt).See(localList1);
/*      */           }
/*      */           break;
/*      */         case '@':
/* 1190 */           if (DocCommentParser.this.newline) {
/* 1191 */             throw new DocCommentParser.ParseException("dc.no.content");
/*      */           }
/*      */           break;
/*      */         case '\032':
/* 1195 */           if (DocCommentParser.this.bp == DocCommentParser.this.buf.length - 1) {
/* 1196 */             throw new DocCommentParser.ParseException("dc.no.content");
/*      */           }
/*      */           break;
/*      */         default:
/* 1200 */           if ((DocCommentParser.this.isJavaIdentifierStart(DocCommentParser.this.ch)) || (DocCommentParser.this.ch == '#')) {
/* 1201 */             DCTree.DCReference localDCReference = DocCommentParser.this.reference(true);
/* 1202 */             List localList2 = DocCommentParser.this.blockContent();
/* 1203 */             return DocCommentParser.this.m.at(paramAnonymousInt).See(localList2.prepend(localDCReference));
/*      */           }break;
/*      */         }
/* 1206 */         throw new DocCommentParser.ParseException("dc.unexpected.content");
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.SERIAL_DATA)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1213 */         List localList = DocCommentParser.this.blockContent();
/* 1214 */         return DocCommentParser.this.m.at(paramAnonymousInt).SerialData(localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.SERIAL_FIELD)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1221 */         DocCommentParser.this.skipWhitespace();
/* 1222 */         DCTree.DCIdentifier localDCIdentifier = DocCommentParser.this.identifier();
/* 1223 */         DocCommentParser.this.skipWhitespace();
/* 1224 */         DCTree.DCReference localDCReference = DocCommentParser.this.reference(false);
/* 1225 */         List localList = null;
/* 1226 */         if (DocCommentParser.this.isWhitespace(DocCommentParser.this.ch)) {
/* 1227 */           DocCommentParser.this.skipWhitespace();
/* 1228 */           localList = DocCommentParser.this.blockContent();
/*      */         }
/* 1230 */         return DocCommentParser.this.m.at(paramAnonymousInt).SerialField(localDCIdentifier, localDCReference, localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.SERIAL)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1237 */         List localList = DocCommentParser.this.blockContent();
/* 1238 */         return DocCommentParser.this.m.at(paramAnonymousInt).Serial(localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.SINCE)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1245 */         List localList = DocCommentParser.this.blockContent();
/* 1246 */         return DocCommentParser.this.m.at(paramAnonymousInt).Since(localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.THROWS)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1253 */         DocCommentParser.this.skipWhitespace();
/* 1254 */         DCTree.DCReference localDCReference = DocCommentParser.this.reference(false);
/* 1255 */         List localList = DocCommentParser.this.blockContent();
/* 1256 */         return DocCommentParser.this.m.at(paramAnonymousInt).Throws(localDCReference, localList);
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.INLINE, DocTree.Kind.VALUE)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */         throws DocCommentParser.ParseException
/*      */       {
/* 1263 */         DCTree.DCReference localDCReference = DocCommentParser.this.reference(true);
/* 1264 */         DocCommentParser.this.skipWhitespace();
/* 1265 */         if (DocCommentParser.this.ch == '}') {
/* 1266 */           DocCommentParser.this.nextChar();
/* 1267 */           return DocCommentParser.this.m.at(paramAnonymousInt).Value(localDCReference);
/*      */         }
/* 1269 */         DocCommentParser.this.nextChar();
/* 1270 */         throw new DocCommentParser.ParseException("dc.unexpected.content");
/*      */       }
/*      */     }
/*      */     , new TagParser(DocCommentParser.TagParser.Kind.BLOCK, DocTree.Kind.VERSION)
/*      */     {
/*      */       public DCTree parse(int paramAnonymousInt)
/*      */       {
/* 1277 */         List localList = DocCommentParser.this.blockContent();
/* 1278 */         return DocCommentParser.this.m.at(paramAnonymousInt).Version(localList);
/*      */       }
/*      */     }
/*      */      };
/* 1283 */     this.tagParsers = new HashMap();
/* 1284 */     for (TagParser localTagParser : arrayOfTagParser1)
/* 1285 */       this.tagParsers.put(this.names.fromString(localTagParser.getTreeKind().tagName), localTagParser);
/*      */   }
/*      */ 
/*      */   static class ParseException extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     ParseException(String paramString)
/*      */     {
/*   74 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class TagParser
/*      */   {
/*      */     Kind kind;
/*      */     DocTree.Kind treeKind;
/*      */ 
/*      */     TagParser(Kind paramKind, DocTree.Kind paramKind1)
/*      */     {
/* 1026 */       this.kind = paramKind;
/* 1027 */       this.treeKind = paramKind1;
/*      */     }
/*      */ 
/*      */     Kind getKind() {
/* 1031 */       return this.kind;
/*      */     }
/*      */ 
/*      */     DocTree.Kind getTreeKind() {
/* 1035 */       return this.treeKind;
/*      */     }
/*      */ 
/*      */     abstract DCTree parse(int paramInt)
/*      */       throws DocCommentParser.ParseException;
/*      */ 
/*      */     static enum Kind
/*      */     {
/* 1020 */       INLINE, BLOCK;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.DocCommentParser
 * JD-Core Version:    0.6.2
 */