/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class InputEntity
/*     */ {
/*     */   private int start;
/*     */   private int finish;
/*     */   private char[] buf;
/*  60 */   private int lineNumber = 1;
/*  61 */   private boolean returnedFirstHalf = false;
/*  62 */   private boolean maybeInCRLF = false;
/*     */   private String name;
/*     */   private InputEntity next;
/*     */   private InputSource input;
/*     */   private Reader reader;
/*     */   private boolean isClosed;
/*     */   private DTDEventListener errHandler;
/*     */   private Locale locale;
/*     */   private StringBuffer rememberedText;
/*     */   private int startRemember;
/*     */   private boolean isPE;
/*     */   private static final int BUFSIZ = 8193;
/*  91 */   private static final char[] newline = { '\n' };
/*     */ 
/*     */   public static InputEntity getInputEntity(DTDEventListener h, Locale l) {
/*  94 */     InputEntity retval = new InputEntity();
/*  95 */     retval.errHandler = h;
/*  96 */     retval.locale = l;
/*  97 */     return retval;
/*     */   }
/*     */ 
/*     */   public boolean isInternal()
/*     */   {
/* 111 */     return this.reader == null;
/*     */   }
/*     */ 
/*     */   public boolean isDocument()
/*     */   {
/* 118 */     return this.next == null;
/*     */   }
/*     */ 
/*     */   public boolean isParameterEntity()
/*     */   {
/* 126 */     return this.isPE;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 133 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void init(InputSource in, String name, InputEntity stack, boolean isPE)
/*     */     throws IOException, SAXException
/*     */   {
/* 143 */     this.input = in;
/* 144 */     this.isPE = isPE;
/* 145 */     this.reader = in.getCharacterStream();
/*     */ 
/* 147 */     if (this.reader == null) {
/* 148 */       InputStream bytes = in.getByteStream();
/*     */ 
/* 150 */       if (bytes == null)
/* 151 */         this.reader = XmlReader.createReader(new URL(in.getSystemId())
/* 152 */           .openStream());
/* 153 */       else if (in.getEncoding() != null)
/* 154 */         this.reader = XmlReader.createReader(in.getByteStream(), in
/* 155 */           .getEncoding());
/*     */       else
/* 157 */         this.reader = XmlReader.createReader(in.getByteStream());
/*     */     }
/* 159 */     this.next = stack;
/* 160 */     this.buf = new char[8193];
/* 161 */     this.name = name;
/* 162 */     checkRecursion(stack);
/*     */   }
/*     */ 
/*     */   public void init(char[] b, String name, InputEntity stack, boolean isPE)
/*     */     throws SAXException
/*     */   {
/* 171 */     this.next = stack;
/* 172 */     this.buf = b;
/* 173 */     this.finish = b.length;
/* 174 */     this.name = name;
/* 175 */     this.isPE = isPE;
/* 176 */     checkRecursion(stack);
/*     */   }
/*     */ 
/*     */   private void checkRecursion(InputEntity stack)
/*     */     throws SAXException
/*     */   {
/* 182 */     if (stack == null)
/* 183 */       return;
/* 184 */     for (stack = stack.next; stack != null; stack = stack.next)
/* 185 */       if ((stack.name != null) && (stack.name.equals(this.name)))
/* 186 */         fatal("P-069", new Object[] { this.name });
/*     */   }
/*     */ 
/*     */   public InputEntity pop()
/*     */     throws IOException
/*     */   {
/* 193 */     close();
/* 194 */     return this.next;
/*     */   }
/*     */ 
/*     */   public boolean isEOF()
/*     */     throws IOException, SAXException
/*     */   {
/* 204 */     if (this.start >= this.finish) {
/* 205 */       fillbuf();
/* 206 */       return this.start >= this.finish;
/*     */     }
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 217 */     if (this.reader == null)
/* 218 */       return null;
/* 219 */     if ((this.reader instanceof XmlReader)) {
/* 220 */       return ((XmlReader)this.reader).getEncoding();
/*     */     }
/*     */ 
/* 224 */     if ((this.reader instanceof InputStreamReader))
/* 225 */       return ((InputStreamReader)this.reader).getEncoding();
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public char getNameChar()
/*     */     throws IOException, SAXException
/*     */   {
/* 237 */     if (this.finish <= this.start)
/* 238 */       fillbuf();
/* 239 */     if (this.finish > this.start) {
/* 240 */       char c = this.buf[(this.start++)];
/* 241 */       if (XmlChars.isNameChar(c))
/* 242 */         return c;
/* 243 */       this.start -= 1;
/*     */     }
/* 245 */     return '\000';
/*     */   }
/*     */ 
/*     */   public char getc()
/*     */     throws IOException, SAXException
/*     */   {
/* 255 */     if (this.finish <= this.start)
/* 256 */       fillbuf();
/* 257 */     if (this.finish > this.start) {
/* 258 */       char c = this.buf[(this.start++)];
/*     */ 
/* 264 */       if (this.returnedFirstHalf) {
/* 265 */         if ((c >= 56320) && (c <= 57343)) {
/* 266 */           this.returnedFirstHalf = false;
/* 267 */           return c;
/*     */         }
/* 269 */         fatal("P-070", new Object[] { Integer.toHexString(c) });
/*     */       }
/* 271 */       if (((c >= ' ') && (c <= 55295)) || (c == '\t') || ((c >= 57344) && (c <= 65533)))
/*     */       {
/* 275 */         return c;
/*     */       }
/*     */ 
/* 281 */       if ((c == '\r') && (!isInternal())) {
/* 282 */         this.maybeInCRLF = true;
/* 283 */         c = getc();
/* 284 */         if (c != '\n')
/* 285 */           ungetc();
/* 286 */         this.maybeInCRLF = false;
/*     */ 
/* 288 */         this.lineNumber += 1;
/* 289 */         return '\n';
/*     */       }
/* 291 */       if ((c == '\n') || (c == '\r')) {
/* 292 */         if ((!isInternal()) && (!this.maybeInCRLF))
/* 293 */           this.lineNumber += 1;
/* 294 */         return c;
/*     */       }
/*     */ 
/* 298 */       if ((c >= 55296) && (c < 56320)) {
/* 299 */         this.returnedFirstHalf = true;
/* 300 */         return c;
/*     */       }
/*     */ 
/* 303 */       fatal("P-071", new Object[] { Integer.toHexString(c) });
/*     */     }
/* 305 */     throw new EndOfInputException();
/*     */   }
/*     */ 
/*     */   public boolean peekc(char c)
/*     */     throws IOException, SAXException
/*     */   {
/* 314 */     if (this.finish <= this.start)
/* 315 */       fillbuf();
/* 316 */     if (this.finish > this.start) {
/* 317 */       if (this.buf[this.start] == c) {
/* 318 */         this.start += 1;
/* 319 */         return true;
/*     */       }
/* 321 */       return false;
/*     */     }
/* 323 */     return false;
/*     */   }
/*     */ 
/*     */   public void ungetc()
/*     */   {
/* 332 */     if (this.start == 0)
/* 333 */       throw new InternalError("ungetc");
/* 334 */     this.start -= 1;
/*     */ 
/* 336 */     if ((this.buf[this.start] == '\n') || (this.buf[this.start] == '\r')) {
/* 337 */       if (!isInternal())
/* 338 */         this.lineNumber -= 1;
/* 339 */     } else if (this.returnedFirstHalf)
/* 340 */       this.returnedFirstHalf = false;
/*     */   }
/*     */ 
/*     */   public boolean maybeWhitespace()
/*     */     throws IOException, SAXException
/*     */   {
/* 351 */     boolean isSpace = false;
/* 352 */     boolean sawCR = false;
/*     */     while (true)
/*     */     {
/* 356 */       if (this.finish <= this.start)
/* 357 */         fillbuf();
/* 358 */       if (this.finish <= this.start) {
/* 359 */         return isSpace;
/*     */       }
/* 361 */       char c = this.buf[(this.start++)];
/* 362 */       if ((c != ' ') && (c != '\t') && (c != '\n') && (c != '\r')) break;
/* 363 */       isSpace = true;
/*     */ 
/* 368 */       if (((c == '\n') || (c == '\r')) && (!isInternal())) {
/* 369 */         if ((c != '\n') || (!sawCR)) {
/* 370 */           this.lineNumber += 1;
/* 371 */           sawCR = false;
/*     */         }
/* 373 */         if (c == '\r')
/* 374 */           sawCR = true;
/*     */       }
/*     */     }
/* 377 */     this.start -= 1;
/* 378 */     return isSpace;
/*     */   }
/*     */ 
/*     */   public boolean parsedContent(DTDEventListener docHandler)
/*     */     throws IOException, SAXException
/*     */   {
/*     */     int last;
/* 407 */     int first = last = this.start; for (boolean sawContent = false; ; last++)
/*     */     {
/* 410 */       if (last >= this.finish) {
/* 411 */         if (last > first)
/*     */         {
/* 413 */           docHandler.characters(this.buf, first, last - first);
/* 414 */           sawContent = true;
/* 415 */           this.start = last;
/*     */         }
/* 417 */         if (isEOF())
/* 418 */           return sawContent;
/* 419 */         first = this.start;
/* 420 */         last = first - 1;
/*     */       }
/*     */       else
/*     */       {
/* 424 */         char c = this.buf[last];
/*     */ 
/* 435 */         if (((c <= ']') || (c > 55295)) && ((c >= '&') || (c < ' ')) && ((c <= '<') || (c >= ']')) && ((c <= '&') || (c >= '<')) && (c != '\t') && ((c < 57344) || (c > 65533)))
/*     */         {
/* 445 */           if ((c == '<') || (c == '&'))
/*     */           {
/*     */             break;
/*     */           }
/* 449 */           if (c == '\n') {
/* 450 */             if (!isInternal()) {
/* 451 */               this.lineNumber += 1;
/*     */             }
/*     */ 
/*     */           }
/* 458 */           else if (c == '\r') {
/* 459 */             if (!isInternal())
/*     */             {
/* 462 */               docHandler.characters(this.buf, first, last - first);
/* 463 */               docHandler.characters(newline, 0, 1);
/* 464 */               sawContent = true;
/* 465 */               this.lineNumber += 1;
/* 466 */               if ((this.finish > last + 1) && 
/* 467 */                 (this.buf[(last + 1)] == '\n')) {
/* 468 */                 last++;
/*     */               }
/*     */ 
/* 472 */               first = this.start = last + 1;
/*     */             }
/*     */ 
/*     */           }
/* 477 */           else if (c == ']') {
/* 478 */             switch (this.finish - last)
/*     */             {
/*     */             case 2:
/* 482 */               if (this.buf[(last + 1)] != ']')
/*     */               {
/*     */                 continue;
/*     */               }
/*     */             case 1:
/* 487 */               if ((this.reader == null) || (this.isClosed))
/*     */                 continue;
/* 489 */               if (last == first)
/* 490 */                 throw new InternalError("fillbuf");
/* 491 */               last--;
/* 492 */               if (last > first)
/*     */               {
/* 494 */                 docHandler.characters(this.buf, first, last - first);
/* 495 */                 sawContent = true;
/* 496 */                 this.start = last;
/*     */               }
/* 498 */               fillbuf();
/* 499 */               first = last = this.start;
/* 500 */               break;
/*     */             default:
/* 505 */               if ((this.buf[(last + 1)] != ']') || (this.buf[(last + 2)] != '>')) continue;
/* 506 */               fatal("P-072", null); break;
/*     */             }
/*     */ 
/*     */           }
/* 512 */           else if ((c >= 55296) && (c <= 57343)) {
/* 513 */             if (last + 1 >= this.finish) {
/* 514 */               if (last > first)
/*     */               {
/* 516 */                 docHandler.characters(this.buf, first, last - first);
/* 517 */                 sawContent = true;
/* 518 */                 this.start = (last + 1);
/*     */               }
/* 520 */               if (isEOF()) {
/* 521 */                 fatal("P-081", new Object[] { 
/* 522 */                   Integer.toHexString(c) });
/*     */               }
/*     */ 
/* 524 */               first = this.start;
/* 525 */               last = first;
/*     */             }
/* 528 */             else if (checkSurrogatePair(last)) {
/* 529 */               last++;
/*     */             } else {
/* 531 */               last--;
/*     */ 
/* 533 */               break;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 538 */             fatal("P-071", new Object[] { Integer.toHexString(c) });
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     char c;
/* 540 */     if (last == first) {
/* 541 */       return sawContent;
/*     */     }
/* 543 */     docHandler.characters(this.buf, first, last - first);
/* 544 */     this.start = last;
/* 545 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean unparsedContent(DTDEventListener docHandler, boolean ignorableWhitespace, String whitespaceInvalidMessage)
/*     */     throws IOException, SAXException
/*     */   {
/* 577 */     if (!peek("![CDATA[", null))
/* 578 */       return false;
/* 579 */     docHandler.startCDATA();
/*     */     while (true)
/*     */     {
/* 585 */       boolean done = false;
/*     */ 
/* 590 */       boolean white = ignorableWhitespace;
/*     */ 
/* 592 */       for (int last = this.start; last < this.finish; last++) {
/* 593 */         char c = this.buf[last];
/*     */ 
/* 598 */         if (!XmlChars.isChar(c)) {
/* 599 */           white = false;
/* 600 */           if ((c >= 55296) && (c <= 57343)) {
/* 601 */             if (checkSurrogatePair(last)) {
/* 602 */               last++;
/*     */             }
/*     */             else {
/* 605 */               last--;
/* 606 */               break;
/*     */             }
/*     */           }
/* 609 */           else fatal("P-071", new Object[] { 
/* 610 */               Integer.toHexString(this.buf[last]) });
/*     */ 
/*     */         }
/* 612 */         else if (c == '\n') {
/* 613 */           if (!isInternal()) {
/* 614 */             this.lineNumber += 1;
/*     */           }
/*     */         }
/* 617 */         else if (c == '\r')
/*     */         {
/* 619 */           if (!isInternal())
/*     */           {
/* 622 */             if (white) {
/* 623 */               if (whitespaceInvalidMessage != null) {
/* 624 */                 this.errHandler.error(new SAXParseException(DTDParser.messages.getMessage(this.locale, whitespaceInvalidMessage), null));
/*     */               }
/* 626 */               docHandler.ignorableWhitespace(this.buf, this.start, last - this.start);
/*     */ 
/* 628 */               docHandler.ignorableWhitespace(newline, 0, 1);
/*     */             }
/*     */             else {
/* 631 */               docHandler.characters(this.buf, this.start, last - this.start);
/* 632 */               docHandler.characters(newline, 0, 1);
/*     */             }
/* 634 */             this.lineNumber += 1;
/* 635 */             if ((this.finish > last + 1) && 
/* 636 */               (this.buf[(last + 1)] == '\n')) {
/* 637 */               last++;
/*     */             }
/*     */ 
/* 641 */             this.start = (last + 1);
/*     */           }
/*     */         }
/* 644 */         else if (c != ']') {
/* 645 */           if ((c != ' ') && (c != '\t'))
/* 646 */             white = false;
/*     */         }
/*     */         else {
/* 649 */           if (last + 2 >= this.finish) break;
/* 650 */           if ((this.buf[(last + 1)] == ']') && (this.buf[(last + 2)] == '>')) {
/* 651 */             done = true;
/* 652 */             break;
/*     */           }
/* 654 */           white = false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 661 */       if (white) {
/* 662 */         if (whitespaceInvalidMessage != null) {
/* 663 */           this.errHandler.error(new SAXParseException(DTDParser.messages.getMessage(this.locale, whitespaceInvalidMessage), null));
/*     */         }
/* 665 */         docHandler.ignorableWhitespace(this.buf, this.start, last - this.start);
/*     */       }
/*     */       else {
/* 668 */         docHandler.characters(this.buf, this.start, last - this.start);
/*     */       }
/* 670 */       if (done) {
/* 671 */         this.start = (last + 3);
/* 672 */         break;
/*     */       }
/* 674 */       this.start = last;
/* 675 */       if (isEOF())
/* 676 */         fatal("P-073", null);
/*     */     }
/* 678 */     docHandler.endCDATA();
/* 679 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean checkSurrogatePair(int offset)
/*     */     throws SAXException
/*     */   {
/* 686 */     if (offset + 1 >= this.finish) {
/* 687 */       return false;
/*     */     }
/* 689 */     char c1 = this.buf[(offset++)];
/* 690 */     char c2 = this.buf[offset];
/*     */ 
/* 692 */     if ((c1 >= 55296) && (c1 < 56320) && (c2 >= 56320) && (c2 <= 57343))
/* 693 */       return true;
/* 694 */     fatal("P-074", new Object[] { 
/* 695 */       Integer.toHexString(c1 & 0xFFFF), 
/* 696 */       Integer.toHexString(c2 & 0xFFFF) });
/*     */ 
/* 698 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean ignorableWhitespace(DTDEventListener handler)
/*     */     throws IOException, SAXException
/*     */   {
/* 712 */     boolean isSpace = false;
/*     */ 
/* 716 */     int first = this.start;
/*     */     while (true) { if (this.finish <= this.start) {
/* 718 */         if (isSpace)
/* 719 */           handler.ignorableWhitespace(this.buf, first, this.start - first);
/* 720 */         fillbuf();
/* 721 */         first = this.start;
/*     */       }
/* 723 */       if (this.finish <= this.start) {
/* 724 */         return isSpace;
/*     */       }
/* 726 */       char c = this.buf[(this.start++)];
/* 727 */       switch (c) {
/*     */       case '\n':
/* 729 */         if (!isInternal()) {
/* 730 */           this.lineNumber += 1;
/*     */         }
/*     */ 
/*     */       case '\t':
/*     */       case ' ':
/* 735 */         isSpace = true;
/* 736 */         break;
/*     */       case '\r':
/* 739 */         isSpace = true;
/* 740 */         if (!isInternal())
/* 741 */           this.lineNumber += 1;
/* 742 */         handler.ignorableWhitespace(this.buf, first, this.start - 1 - first);
/*     */ 
/* 744 */         handler.ignorableWhitespace(newline, 0, 1);
/* 745 */         if ((this.start < this.finish) && (this.buf[this.start] == '\n'))
/* 746 */           this.start += 1;
/* 747 */         first = this.start;
/*     */       }
/*     */     }
/*     */ 
/* 751 */     ungetc();
/* 752 */     if (isSpace)
/* 753 */       handler.ignorableWhitespace(this.buf, first, this.start - first);
/* 754 */     return isSpace;
/*     */   }
/*     */ 
/*     */   public boolean peek(String next, char[] chars)
/*     */     throws IOException, SAXException
/*     */   {
/*     */     int len;
/*     */     int len;
/* 772 */     if (chars != null)
/* 773 */       len = chars.length;
/*     */     else {
/* 775 */       len = next.length();
/*     */     }
/*     */ 
/* 780 */     if ((this.finish <= this.start) || (this.finish - this.start < len)) {
/* 781 */       fillbuf();
/*     */     }
/*     */ 
/* 784 */     if (this.finish <= this.start) {
/* 785 */       return false;
/*     */     }
/*     */ 
/* 788 */     if (chars != null) {
/* 789 */       for (int i = 0; (i < len) && (this.start + i < this.finish); i++) {
/* 790 */         if (this.buf[(this.start + i)] != chars[i])
/* 791 */           return false;
/*     */       }
/*     */     }
/* 794 */     for (int i = 0; (i < len) && (this.start + i < this.finish); i++) {
/* 795 */       if (this.buf[(this.start + i)] != next.charAt(i)) {
/* 796 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 802 */     if (i < len) {
/* 803 */       if ((this.reader == null) || (this.isClosed)) {
/* 804 */         return false;
/*     */       }
/*     */ 
/* 813 */       if (len > this.buf.length) {
/* 814 */         fatal("P-077", new Object[] { new Integer(this.buf.length) });
/*     */       }
/* 816 */       fillbuf();
/* 817 */       return peek(next, chars);
/*     */     }
/*     */ 
/* 820 */     this.start += len;
/* 821 */     return true;
/*     */   }
/*     */ 
/*     */   public void startRemembering()
/*     */   {
/* 833 */     if (this.startRemember != 0)
/* 834 */       throw new InternalError();
/* 835 */     this.startRemember = this.start;
/*     */   }
/*     */ 
/*     */   public String rememberText()
/*     */   {
/*     */     String retval;
/*     */     String retval;
/* 844 */     if (this.rememberedText != null) {
/* 845 */       this.rememberedText.append(this.buf, this.startRemember, this.start - this.startRemember);
/*     */ 
/* 847 */       retval = this.rememberedText.toString();
/*     */     } else {
/* 849 */       retval = new String(this.buf, this.startRemember, this.start - this.startRemember);
/*     */     }
/*     */ 
/* 852 */     this.startRemember = 0;
/* 853 */     this.rememberedText = null;
/* 854 */     return retval;
/*     */   }
/*     */ 
/*     */   private InputEntity getTopEntity()
/*     */   {
/* 859 */     InputEntity current = this;
/*     */ 
/* 863 */     while ((current != null) && (current.input == null))
/* 864 */       current = current.next;
/* 865 */     return current == null ? this : current;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 873 */     InputEntity where = getTopEntity();
/* 874 */     if (where == this)
/* 875 */       return this.input.getPublicId();
/* 876 */     return where.getPublicId();
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 884 */     InputEntity where = getTopEntity();
/* 885 */     if (where == this)
/* 886 */       return this.input.getSystemId();
/* 887 */     return where.getSystemId();
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 895 */     InputEntity where = getTopEntity();
/* 896 */     if (where == this)
/* 897 */       return this.lineNumber;
/* 898 */     return where.getLineNumber();
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 906 */     return -1;
/*     */   }
/*     */ 
/*     */   private void fillbuf()
/*     */     throws IOException, SAXException
/*     */   {
/* 926 */     if ((this.reader == null) || (this.isClosed)) {
/* 927 */       return;
/*     */     }
/*     */ 
/* 930 */     if (this.startRemember != 0) {
/* 931 */       if (this.rememberedText == null)
/* 932 */         this.rememberedText = new StringBuffer(this.buf.length);
/* 933 */       this.rememberedText.append(this.buf, this.startRemember, this.start - this.startRemember);
/*     */     }
/*     */ 
/* 937 */     boolean extra = (this.finish > 0) && (this.start > 0);
/*     */ 
/* 940 */     if (extra)
/* 941 */       this.start -= 1;
/* 942 */     int len = this.finish - this.start;
/*     */ 
/* 944 */     System.arraycopy(this.buf, this.start, this.buf, 0, len);
/* 945 */     this.start = 0;
/* 946 */     this.finish = len;
/*     */     try
/*     */     {
/* 949 */       len = this.buf.length - len;
/* 950 */       len = this.reader.read(this.buf, this.finish, len);
/*     */     } catch (UnsupportedEncodingException e) {
/* 952 */       fatal("P-075", new Object[] { e.getMessage() });
/*     */     } catch (CharConversionException e) {
/* 954 */       fatal("P-076", new Object[] { e.getMessage() });
/*     */     }
/* 956 */     if (len >= 0)
/* 957 */       this.finish += len;
/*     */     else
/* 959 */       close();
/* 960 */     if (extra) {
/* 961 */       this.start += 1;
/*     */     }
/* 963 */     if (this.startRemember != 0)
/*     */     {
/* 965 */       this.startRemember = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 971 */       if ((this.reader != null) && (!this.isClosed))
/* 972 */         this.reader.close();
/* 973 */       this.isClosed = true;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fatal(String messageId, Object[] params)
/*     */     throws SAXException
/*     */   {
/* 983 */     SAXParseException x = new SAXParseException(DTDParser.messages.getMessage(this.locale, messageId, params), null);
/*     */ 
/* 986 */     close();
/* 987 */     this.errHandler.fatalError(x);
/* 988 */     throw x;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.dtdparser.InputEntity
 * JD-Core Version:    0.6.2
 */