/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ public class Scanner
/*      */   implements Constants
/*      */ {
/*      */   public static final long OFFSETINC = 1L;
/*      */   public static final long LINEINC = 4294967296L;
/*      */   public static final int EOF = -1;
/*      */   public Environment env;
/*      */   protected ScannerInputReader in;
/*   96 */   public boolean scanComments = false;
/*      */   public int token;
/*      */   public long pos;
/*      */   public long prevPos;
/*      */   protected int ch;
/*      */   public char charValue;
/*      */   public int intValue;
/*      */   public long longValue;
/*      */   public float floatValue;
/*      */   public double doubleValue;
/*      */   public String stringValue;
/*      */   public Identifier idValue;
/*      */   public int radix;
/*      */   public String docComment;
/*      */   private int count;
/*  139 */   private char[] buffer = new char[1024];
/*      */ 
/*  141 */   private void growBuffer() { char[] arrayOfChar = new char[this.buffer.length * 2];
/*  142 */     System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.buffer.length);
/*  143 */     this.buffer = arrayOfChar;
/*      */   }
/*      */ 
/*      */   private void putc(int paramInt)
/*      */   {
/*  150 */     if (this.count == this.buffer.length) {
/*  151 */       growBuffer();
/*      */     }
/*  153 */     this.buffer[(this.count++)] = ((char)paramInt);
/*      */   }
/*      */ 
/*      */   private String bufferString() {
/*  157 */     return new String(this.buffer, 0, this.count);
/*      */   }
/*      */ 
/*      */   public Scanner(Environment paramEnvironment, InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  164 */     this.env = paramEnvironment;
/*  165 */     useInputStream(paramInputStream);
/*      */   }
/*      */ 
/*      */   protected void useInputStream(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  174 */       this.in = new ScannerInputReader(this.env, paramInputStream);
/*      */     } catch (Exception localException) {
/*  176 */       this.env.setCharacterEncoding(null);
/*  177 */       this.in = new ScannerInputReader(this.env, paramInputStream);
/*      */     }
/*      */ 
/*  180 */     this.ch = this.in.read();
/*  181 */     this.prevPos = this.in.pos;
/*      */ 
/*  183 */     scan();
/*      */   }
/*      */ 
/*      */   protected Scanner(Environment paramEnvironment)
/*      */   {
/*  190 */     this.env = paramEnvironment;
/*      */   }
/*      */ 
/*      */   private static void defineKeyword(int paramInt)
/*      */   {
/*  198 */     Identifier.lookup(opNames[paramInt]).setType(paramInt);
/*      */   }
/*      */ 
/*      */   private void skipComment()
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/*  276 */       switch (this.ch) {
/*      */       case -1:
/*  278 */         this.env.error(this.pos, "eof.in.comment");
/*  279 */         return;
/*      */       case 42:
/*  282 */         if ((this.ch = this.in.read()) != 47) continue;
/*  283 */         this.ch = this.in.read();
/*  284 */         return;
/*      */       }
/*      */ 
/*  289 */       this.ch = this.in.read();
/*      */     }
/*      */   }
/*      */ 
/*      */   private String scanDocComment()
/*      */     throws IOException
/*      */   {
/*  324 */     ScannerInputReader localScannerInputReader = this.in;
/*      */ 
/*  327 */     char[] arrayOfChar = this.buffer;
/*  328 */     int j = 0;
/*      */     int i;
/*  339 */     while ((i = localScannerInputReader.read()) == 42);
/*  343 */     if (i == 47)
/*      */     {
/*  345 */       this.ch = localScannerInputReader.read();
/*  346 */       return "";
/*      */     }
/*      */ 
/*  350 */     if (i == 10) {
/*  351 */       i = localScannerInputReader.read();
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  365 */       switch (i)
/*      */       {
/*      */       case 9:
/*      */       case 32:
/*  373 */         i = localScannerInputReader.read();
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*  393 */       case 31: }  } if (i == 42)
/*      */     {
/*      */       do
/*  396 */         i = localScannerInputReader.read();
/*  397 */       while (i == 42);
/*      */ 
/*  400 */       if (i == 47)
/*      */       {
/*  403 */         this.ch = localScannerInputReader.read();
/*  404 */         break label541;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  412 */       switch (i)
/*      */       {
/*      */       case -1:
/*  416 */         this.env.error(this.pos, "eof.in.comment");
/*  417 */         this.ch = -1;
/*  418 */         break;
/*      */       case 42:
/*  423 */         i = localScannerInputReader.read();
/*  424 */         if (i == 47)
/*      */         {
/*  427 */           this.ch = localScannerInputReader.read();
/*  428 */           break label541;
/*      */         }
/*      */ 
/*  432 */         if (j == arrayOfChar.length) {
/*  433 */           growBuffer();
/*  434 */           arrayOfChar = this.buffer;
/*      */         }
/*  436 */         arrayOfChar[(j++)] = '*';
/*  437 */         break;
/*      */       case 10:
/*  443 */         if (j == arrayOfChar.length) {
/*  444 */           growBuffer();
/*  445 */           arrayOfChar = this.buffer;
/*      */         }
/*  447 */         arrayOfChar[(j++)] = '\n';
/*  448 */         i = localScannerInputReader.read();
/*  449 */         break;
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*      */       default:
/*  462 */         if (j == arrayOfChar.length) {
/*  463 */           growBuffer();
/*  464 */           arrayOfChar = this.buffer;
/*      */         }
/*  466 */         arrayOfChar[(j++)] = ((char)i);
/*  467 */         i = localScannerInputReader.read();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  478 */     label541: if (j > 0) {
/*  479 */       int k = j - 1;
/*      */ 
/*  481 */       while (k > -1)
/*  482 */         switch (arrayOfChar[k]) {
/*      */         case '\t':
/*      */         case ' ':
/*      */         case '*':
/*  486 */           k--;
/*  487 */           break;
/*      */         case '\000':
/*      */         case '\001':
/*      */         case '\002':
/*      */         case '\003':
/*      */         case '\004':
/*      */         case '\005':
/*      */         case '\006':
/*      */         case '\007':
/*      */         case '\b':
/*      */         case '\n':
/*      */         case '\013':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case '\016':
/*      */         case '\017':
/*      */         case '\020':
/*      */         case '\021':
/*      */         case '\022':
/*      */         case '\023':
/*      */         case '\024':
/*      */         case '\025':
/*      */         case '\026':
/*      */         case '\027':
/*      */         case '\030':
/*      */         case '\031':
/*      */         case '\032':
/*      */         case '\033':
/*      */         case '\034':
/*      */         case '\035':
/*      */         case '\036':
/*      */         case '\037':
/*      */         case '!':
/*      */         case '"':
/*      */         case '#':
/*      */         case '$':
/*      */         case '%':
/*      */         case '&':
/*      */         case '\'':
/*      */         case '(':
/*  501 */         case ')': }  j = k + 1;
/*      */ 
/*  504 */       return new String(arrayOfChar, 0, j);
/*      */     }
/*  506 */     return "";
/*      */   }
/*      */ 
/*      */   private void scanNumber()
/*      */     throws IOException
/*      */   {
/*  515 */     int i = 0;
/*  516 */     int j = 0;
/*  517 */     int k = 0;
/*  518 */     this.radix = (this.ch == 48 ? 8 : 10);
/*  519 */     long l = this.ch - 48;
/*  520 */     this.count = 0;
/*  521 */     putc(this.ch);
/*      */     while (true)
/*      */     {
/*  524 */       switch (this.ch = this.in.read()) {
/*      */       case 46:
/*  526 */         if (this.radix == 16)
/*      */           break label719;
/*  528 */         scanReal();
/*  529 */         return;
/*      */       case 56:
/*      */       case 57:
/*  534 */         i = 1;
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*  537 */         k = 1;
/*  538 */         putc(this.ch);
/*  539 */         if (this.radix == 10) {
/*  540 */           j = (j != 0) || (l * 10L / 10L != l) ? 1 : 0;
/*  541 */           l = l * 10L + (this.ch - 48);
/*  542 */           j = (j != 0) || (l - 1L < -1L) ? 1 : 0;
/*  543 */         } else if (this.radix == 8) {
/*  544 */           j = (j != 0) || (l >>> 61 != 0L) ? 1 : 0;
/*  545 */           l = (l << 3) + (this.ch - 48);
/*      */         } else {
/*  547 */           j = (j != 0) || (l >>> 60 != 0L) ? 1 : 0;
/*  548 */           l = (l << 4) + (this.ch - 48);
/*      */         }
/*  550 */         break;
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*  553 */         if (this.radix != 16) {
/*  554 */           scanReal();
/*  555 */           return; } case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 97:
/*      */       case 98:
/*      */       case 99:
/*  559 */         k = 1;
/*  560 */         putc(this.ch);
/*  561 */         if (this.radix != 16)
/*      */           break label719;
/*  563 */         j = (j != 0) || (l >>> 60 != 0L) ? 1 : 0;
/*      */ 
/*  565 */         l = (l << 4) + 10L + 
/*  565 */           Character.toLowerCase((char)this.ch) - 
/*  565 */           97L;
/*  566 */         break;
/*      */       case 76:
/*      */       case 108:
/*  569 */         this.ch = this.in.read();
/*  570 */         this.longValue = l;
/*  571 */         this.token = 66;
/*  572 */         break;
/*      */       case 88:
/*      */       case 120:
/*  577 */         if ((this.count != 1) || (this.radix != 8)) break label719;
/*  578 */         this.radix = 16;
/*  579 */         k = 0;
/*      */       case 47:
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*      */       case 63:
/*      */       case 64:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 89:
/*      */       case 90:
/*      */       case 91:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*      */       case 95:
/*      */       case 96:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*  587 */       case 119: }  } this.intValue = ((int)l);
/*  588 */     this.token = 65;
/*      */ 
/*  598 */     label719: if ((Character.isJavaLetterOrDigit((char)this.ch)) || (this.ch == 46)) {
/*  599 */       this.env.error(this.in.pos, "invalid.number");
/*      */       do this.ch = this.in.read();
/*  601 */       while ((Character.isJavaLetterOrDigit((char)this.ch)) || (this.ch == 46));
/*  602 */       this.intValue = 0;
/*  603 */       this.token = 65;
/*  604 */     } else if ((this.radix == 8) && (i != 0))
/*      */     {
/*  606 */       this.intValue = 0;
/*  607 */       this.token = 65;
/*  608 */       this.env.error(this.pos, "invalid.octal.number");
/*  609 */     } else if ((this.radix == 16) && (k == 0))
/*      */     {
/*  611 */       this.intValue = 0;
/*  612 */       this.token = 65;
/*  613 */       this.env.error(this.pos, "invalid.hex.number");
/*      */     }
/*  615 */     else if (this.token == 65)
/*      */     {
/*  618 */       j = (j != 0) || ((l & 0x0) != 0L) || ((this.radix == 10) && (l > 2147483648L)) ? 1 : 0;
/*      */ 
/*  622 */       if (j != 0) {
/*  623 */         this.intValue = 0;
/*      */ 
/*  627 */         switch (this.radix) {
/*      */         case 8:
/*  629 */           this.env.error(this.pos, "overflow.int.oct");
/*  630 */           break;
/*      */         case 10:
/*  632 */           this.env.error(this.pos, "overflow.int.dec");
/*  633 */           break;
/*      */         case 16:
/*  635 */           this.env.error(this.pos, "overflow.int.hex");
/*  636 */           break;
/*      */         default:
/*  638 */           throw new CompilerError("invalid radix");
/*      */         }
/*      */       }
/*      */     }
/*  642 */     else if (j != 0) {
/*  643 */       this.longValue = 0L;
/*      */ 
/*  647 */       switch (this.radix) {
/*      */       case 8:
/*  649 */         this.env.error(this.pos, "overflow.long.oct");
/*  650 */         break;
/*      */       case 10:
/*  652 */         this.env.error(this.pos, "overflow.long.dec");
/*  653 */         break;
/*      */       case 16:
/*  655 */         this.env.error(this.pos, "overflow.long.hex");
/*  656 */         break;
/*      */       default:
/*  658 */         throw new CompilerError("invalid radix");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void scanReal()
/*      */     throws IOException
/*      */   {
/*  672 */     int i = 0;
/*  673 */     int j = 0;
/*      */ 
/*  675 */     if (this.ch == 46) {
/*  676 */       putc(this.ch);
/*  677 */       this.ch = this.in.read();
/*      */     }
/*      */     int k;
/*  681 */     for (; ; this.ch = this.in.read()) {
/*  682 */       switch (this.ch) { case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/*  685 */         putc(this.ch);
/*  686 */         break;
/*      */       case 69:
/*      */       case 101:
/*  689 */         if (i != 0)
/*      */           break label405;
/*  691 */         putc(this.ch);
/*  692 */         i = 1;
/*  693 */         break;
/*      */       case 43:
/*      */       case 45:
/*  696 */         k = this.buffer[(this.count - 1)];
/*  697 */         if ((k != 101) && (k != 69))
/*      */           break label405;
/*  699 */         putc(this.ch);
/*  700 */         break;
/*      */       case 70:
/*      */       case 102:
/*  703 */         this.ch = this.in.read();
/*  704 */         j = 1;
/*  705 */         break;
/*      */       case 68:
/*      */       case 100:
/*  708 */         this.ch = this.in.read();
/*      */       case 44:
/*      */       case 46:
/*      */       case 47:
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*      */       case 63:
/*      */       case 64:
/*      */       case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       case 90:
/*      */       case 91:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*      */       case 95:
/*      */       case 96:
/*      */       case 97:
/*      */       case 98:
/*      */       case 99:
/*      */       default:
/*  711 */         break label405;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  717 */     label405: if ((Character.isJavaLetterOrDigit((char)this.ch)) || (this.ch == 46)) {
/*  718 */       this.env.error(this.in.pos, "invalid.number");
/*      */       do this.ch = this.in.read();
/*  720 */       while ((Character.isJavaLetterOrDigit((char)this.ch)) || (this.ch == 46));
/*  721 */       this.doubleValue = 0.0D;
/*  722 */       this.token = 68;
/*      */     } else {
/*  724 */       this.token = (j != 0 ? 67 : 68);
/*      */       try {
/*  726 */         k = this.buffer[(this.count - 1)];
/*  727 */         if ((k == 101) || (k == 69) || (k == 43) || (k == 45))
/*      */         {
/*  729 */           this.env.error(this.in.pos - 1L, "float.format");
/*      */         }
/*      */         else
/*      */         {
/*      */           String str;
/*  730 */           if (j != 0) {
/*  731 */             str = bufferString();
/*  732 */             this.floatValue = Float.valueOf(str).floatValue();
/*  733 */             if (Float.isInfinite(this.floatValue))
/*  734 */               this.env.error(this.pos, "overflow.float");
/*  735 */             else if ((this.floatValue == 0.0F) && (!looksLikeZero(str)))
/*  736 */               this.env.error(this.pos, "underflow.float");
/*      */           }
/*      */           else {
/*  739 */             str = bufferString();
/*  740 */             this.doubleValue = Double.valueOf(str).doubleValue();
/*  741 */             if (Double.isInfinite(this.doubleValue))
/*  742 */               this.env.error(this.pos, "overflow.double");
/*  743 */             else if ((this.doubleValue == 0.0D) && (!looksLikeZero(str)))
/*  744 */               this.env.error(this.pos, "underflow.double");
/*      */           }
/*      */         }
/*      */       } catch (NumberFormatException localNumberFormatException) {
/*  748 */         this.env.error(this.pos, "float.format");
/*  749 */         this.doubleValue = 0.0D;
/*  750 */         this.floatValue = 0.0F;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean looksLikeZero(String paramString)
/*      */   {
/*  759 */     int i = paramString.length();
/*  760 */     for (int j = 0; j < i; j++) {
/*  761 */       switch (paramString.charAt(j)) { case '\000':
/*      */       case '.':
/*  763 */         break;
/*      */       case '1':
/*      */       case '2':
/*      */       case '3':
/*      */       case '4':
/*      */       case '5':
/*      */       case '6':
/*      */       case '7':
/*      */       case '8':
/*      */       case '9':
/*  766 */         return false;
/*      */       case 'E':
/*      */       case 'F':
/*      */       case 'e':
/*      */       case 'f':
/*  768 */         return true;
/*      */       }
/*      */     }
/*  771 */     return true;
/*      */   }
/*      */ 
/*      */   private int scanEscapeChar()
/*      */     throws IOException
/*      */   {
/*  780 */     long l = this.in.pos;
/*      */ 
/*  782 */     switch (this.ch = this.in.read()) { case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*  785 */       int i = this.ch - 48;
/*  786 */       for (int j = 2; j > 0; j--) {
/*  787 */         switch (this.ch = this.in.read()) { case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 54:
/*      */         case 55:
/*  790 */           i = (i << 3) + this.ch - 48;
/*  791 */           break;
/*      */         default:
/*  794 */           if (i > 255) {
/*  795 */             this.env.error(l, "invalid.escape.char");
/*      */           }
/*  797 */           return i;
/*      */         }
/*      */       }
/*  800 */       this.ch = this.in.read();
/*  801 */       if (i > 255) {
/*  802 */         this.env.error(l, "invalid.escape.char");
/*      */       }
/*  804 */       return i;
/*      */     case 114:
/*  807 */       this.ch = this.in.read(); return 13;
/*      */     case 110:
/*  808 */       this.ch = this.in.read(); return 10;
/*      */     case 102:
/*  809 */       this.ch = this.in.read(); return 12;
/*      */     case 98:
/*  810 */       this.ch = this.in.read(); return 8;
/*      */     case 116:
/*  811 */       this.ch = this.in.read(); return 9;
/*      */     case 92:
/*  812 */       this.ch = this.in.read(); return 92;
/*      */     case 34:
/*  813 */       this.ch = this.in.read(); return 34;
/*      */     case 39:
/*  814 */       this.ch = this.in.read(); return 39;
/*      */     }
/*      */ 
/*  817 */     this.env.error(l, "invalid.escape.char");
/*  818 */     this.ch = this.in.read();
/*  819 */     return -1;
/*      */   }
/*      */ 
/*      */   private void scanString()
/*      */     throws IOException
/*      */   {
/*  827 */     this.token = 69;
/*  828 */     this.count = 0;
/*  829 */     this.ch = this.in.read();
/*      */     while (true)
/*      */     {
/*  833 */       switch (this.ch) {
/*      */       case -1:
/*  835 */         this.env.error(this.pos, "eof.in.string");
/*  836 */         this.stringValue = bufferString();
/*  837 */         return;
/*      */       case 10:
/*      */       case 13:
/*  841 */         this.ch = this.in.read();
/*  842 */         this.env.error(this.pos, "newline.in.string");
/*  843 */         this.stringValue = bufferString();
/*  844 */         return;
/*      */       case 34:
/*  847 */         this.ch = this.in.read();
/*  848 */         this.stringValue = bufferString();
/*  849 */         return;
/*      */       case 92:
/*  852 */         int i = scanEscapeChar();
/*  853 */         if (i >= 0)
/*  854 */           putc((char)i); break;
/*      */       default:
/*  860 */         putc(this.ch);
/*  861 */         this.ch = this.in.read();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void scanCharacter()
/*      */     throws IOException
/*      */   {
/*  872 */     this.token = 63;
/*      */ 
/*  874 */     switch (this.ch = this.in.read()) {
/*      */     case 92:
/*  876 */       int i = scanEscapeChar();
/*  877 */       this.charValue = ((char)(i >= 0 ? i : 0));
/*  878 */       break;
/*      */     case 39:
/*  886 */       this.charValue = '\000';
/*  887 */       this.env.error(this.pos, "invalid.char.constant");
/*  888 */       this.ch = this.in.read();
/*  889 */       while (this.ch == 39) {
/*  890 */         this.ch = this.in.read();
/*      */       }
/*  892 */       return;
/*      */     case 10:
/*      */     case 13:
/*  896 */       this.charValue = '\000';
/*  897 */       this.env.error(this.pos, "invalid.char.constant");
/*  898 */       return;
/*      */     default:
/*  901 */       this.charValue = ((char)this.ch);
/*  902 */       this.ch = this.in.read();
/*      */     }
/*      */ 
/*  906 */     if (this.ch == 39) {
/*  907 */       this.ch = this.in.read();
/*      */     } else {
/*  909 */       this.env.error(this.pos, "invalid.char.constant");
/*      */       while (true) {
/*  911 */         switch (this.ch) {
/*      */         case 39:
/*  913 */           this.ch = this.in.read();
/*  914 */           return;
/*      */         case -1:
/*      */         case 10:
/*      */         case 59:
/*  918 */           return;
/*      */         }
/*  920 */         this.ch = this.in.read();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void scanIdentifier()
/*      */     throws IOException
/*      */   {
/*  931 */     this.count = 0;
/*      */     do
/*      */       while (true) {
/*  934 */         putc(this.ch);
/*  935 */         switch (this.ch = this.in.read()) { case 36:
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 57:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 72:
/*      */         case 73:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/*      */         case 79:
/*      */         case 80:
/*      */         case 81:
/*      */         case 82:
/*      */         case 83:
/*      */         case 84:
/*      */         case 85:
/*      */         case 86:
/*      */         case 87:
/*      */         case 88:
/*      */         case 89:
/*      */         case 90:
/*      */         case 95:
/*      */         case 97:
/*      */         case 98:
/*      */         case 99:
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*      */         case 108:
/*      */         case 109:
/*      */         case 110:
/*      */         case 111:
/*      */         case 112:
/*      */         case 113:
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/*      */         case 120:
/*      */         case 121:
/*      */         case 122:
/*      */         case 37:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 46:
/*      */         case 47:
/*      */         case 58:
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*  954 */         case 96: }  }  while (Character.isJavaLetterOrDigit((char)this.ch));
/*  955 */     this.idValue = Identifier.lookup(bufferString());
/*  956 */     this.token = this.idValue.getType();
/*      */   }
/*      */ 
/*      */   public long getEndPos()
/*      */   {
/*  968 */     return this.in.pos;
/*      */   }
/*      */ 
/*      */   public IdentifierToken getIdToken()
/*      */   {
/*  976 */     return this.token != 60 ? null : new IdentifierToken(this.pos, this.idValue);
/*      */   }
/*      */ 
/*      */   public long scan()
/*      */     throws IOException
/*      */   {
/*  984 */     return xscan();
/*      */   }
/*      */ 
/*      */   protected long xscan() throws IOException {
/*  988 */     ScannerInputReader localScannerInputReader = this.in;
/*  989 */     long l = this.pos;
/*  990 */     this.prevPos = localScannerInputReader.pos;
/*  991 */     this.docComment = null;
/*      */     while (true) {
/*  993 */       this.pos = localScannerInputReader.pos;
/*      */ 
/*  995 */       switch (this.ch) {
/*      */       case -1:
/*  997 */         this.token = -1;
/*  998 */         return l;
/*      */       case 10:
/* 1001 */         if (this.scanComments) {
/* 1002 */           this.ch = 32;
/*      */ 
/* 1006 */           this.token = 146;
/* 1007 */           return l;
/*      */         }
/*      */       case 9:
/*      */       case 12:
/*      */       case 32:
/* 1012 */         this.ch = localScannerInputReader.read();
/* 1013 */         break;
/*      */       case 47:
/* 1016 */         switch (this.ch = localScannerInputReader.read())
/*      */         {
/*      */         case 47:
/* 1019 */           while (((this.ch = localScannerInputReader.read()) != -1) && (this.ch != 10));
/* 1020 */           if (this.scanComments) {
/* 1021 */             this.token = 146;
/* 1022 */             return l;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 42:
/* 1027 */           this.ch = localScannerInputReader.read();
/* 1028 */           if (this.ch == 42)
/* 1029 */             this.docComment = scanDocComment();
/*      */           else {
/* 1031 */             skipComment();
/*      */           }
/* 1033 */           if (this.scanComments) {
/* 1034 */             return l;
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 61:
/* 1039 */           this.ch = localScannerInputReader.read();
/* 1040 */           this.token = 3;
/* 1041 */           return l;
/*      */         default:
/* 1044 */           this.token = 31;
/* 1045 */           return l;
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 34:
/* 1050 */         scanString();
/* 1051 */         return l;
/*      */       case 39:
/* 1054 */         scanCharacter();
/* 1055 */         return l;
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/* 1059 */         scanNumber();
/* 1060 */         return l;
/*      */       case 46:
/* 1063 */         switch (this.ch = localScannerInputReader.read()) { case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 57:
/* 1066 */           this.count = 0;
/* 1067 */           putc(46);
/* 1068 */           scanReal();
/* 1069 */           break;
/*      */         default:
/* 1071 */           this.token = 46;
/*      */         }
/* 1073 */         return l;
/*      */       case 123:
/* 1076 */         this.ch = localScannerInputReader.read();
/* 1077 */         this.token = 138;
/* 1078 */         return l;
/*      */       case 125:
/* 1081 */         this.ch = localScannerInputReader.read();
/* 1082 */         this.token = 139;
/* 1083 */         return l;
/*      */       case 40:
/* 1086 */         this.ch = localScannerInputReader.read();
/* 1087 */         this.token = 140;
/* 1088 */         return l;
/*      */       case 41:
/* 1091 */         this.ch = localScannerInputReader.read();
/* 1092 */         this.token = 141;
/* 1093 */         return l;
/*      */       case 91:
/* 1096 */         this.ch = localScannerInputReader.read();
/* 1097 */         this.token = 142;
/* 1098 */         return l;
/*      */       case 93:
/* 1101 */         this.ch = localScannerInputReader.read();
/* 1102 */         this.token = 143;
/* 1103 */         return l;
/*      */       case 44:
/* 1106 */         this.ch = localScannerInputReader.read();
/* 1107 */         this.token = 0;
/* 1108 */         return l;
/*      */       case 59:
/* 1111 */         this.ch = localScannerInputReader.read();
/* 1112 */         this.token = 135;
/* 1113 */         return l;
/*      */       case 63:
/* 1116 */         this.ch = localScannerInputReader.read();
/* 1117 */         this.token = 137;
/* 1118 */         return l;
/*      */       case 126:
/* 1121 */         this.ch = localScannerInputReader.read();
/* 1122 */         this.token = 38;
/* 1123 */         return l;
/*      */       case 58:
/* 1126 */         this.ch = localScannerInputReader.read();
/* 1127 */         this.token = 136;
/* 1128 */         return l;
/*      */       case 45:
/* 1131 */         switch (this.ch = localScannerInputReader.read()) {
/*      */         case 45:
/* 1133 */           this.ch = localScannerInputReader.read();
/* 1134 */           this.token = 51;
/* 1135 */           return l;
/*      */         case 61:
/* 1138 */           this.ch = localScannerInputReader.read();
/* 1139 */           this.token = 6;
/* 1140 */           return l;
/*      */         }
/* 1142 */         this.token = 30;
/* 1143 */         return l;
/*      */       case 43:
/* 1146 */         switch (this.ch = localScannerInputReader.read()) {
/*      */         case 43:
/* 1148 */           this.ch = localScannerInputReader.read();
/* 1149 */           this.token = 50;
/* 1150 */           return l;
/*      */         case 61:
/* 1153 */           this.ch = localScannerInputReader.read();
/* 1154 */           this.token = 5;
/* 1155 */           return l;
/*      */         }
/* 1157 */         this.token = 29;
/* 1158 */         return l;
/*      */       case 60:
/* 1161 */         switch (this.ch = localScannerInputReader.read()) {
/*      */         case 60:
/* 1163 */           if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1164 */             this.ch = localScannerInputReader.read();
/* 1165 */             this.token = 7;
/* 1166 */             return l;
/*      */           }
/* 1168 */           this.token = 26;
/* 1169 */           return l;
/*      */         case 61:
/* 1172 */           this.ch = localScannerInputReader.read();
/* 1173 */           this.token = 23;
/* 1174 */           return l;
/*      */         }
/* 1176 */         this.token = 24;
/* 1177 */         return l;
/*      */       case 62:
/* 1180 */         switch (this.ch = localScannerInputReader.read()) {
/*      */         case 62:
/* 1182 */           switch (this.ch = localScannerInputReader.read()) {
/*      */           case 61:
/* 1184 */             this.ch = localScannerInputReader.read();
/* 1185 */             this.token = 8;
/* 1186 */             return l;
/*      */           case 62:
/* 1189 */             if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1190 */               this.ch = localScannerInputReader.read();
/* 1191 */               this.token = 9;
/* 1192 */               return l;
/*      */             }
/* 1194 */             this.token = 28;
/* 1195 */             return l;
/*      */           }
/* 1197 */           this.token = 27;
/* 1198 */           return l;
/*      */         case 61:
/* 1201 */           this.ch = localScannerInputReader.read();
/* 1202 */           this.token = 21;
/* 1203 */           return l;
/*      */         }
/* 1205 */         this.token = 22;
/* 1206 */         return l;
/*      */       case 124:
/* 1209 */         switch (this.ch = localScannerInputReader.read()) {
/*      */         case 124:
/* 1211 */           this.ch = localScannerInputReader.read();
/* 1212 */           this.token = 14;
/* 1213 */           return l;
/*      */         case 61:
/* 1216 */           this.ch = localScannerInputReader.read();
/* 1217 */           this.token = 11;
/* 1218 */           return l;
/*      */         }
/* 1220 */         this.token = 16;
/* 1221 */         return l;
/*      */       case 38:
/* 1224 */         switch (this.ch = localScannerInputReader.read()) {
/*      */         case 38:
/* 1226 */           this.ch = localScannerInputReader.read();
/* 1227 */           this.token = 15;
/* 1228 */           return l;
/*      */         case 61:
/* 1231 */           this.ch = localScannerInputReader.read();
/* 1232 */           this.token = 10;
/* 1233 */           return l;
/*      */         }
/* 1235 */         this.token = 18;
/* 1236 */         return l;
/*      */       case 61:
/* 1239 */         if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1240 */           this.ch = localScannerInputReader.read();
/* 1241 */           this.token = 20;
/* 1242 */           return l;
/*      */         }
/* 1244 */         this.token = 1;
/* 1245 */         return l;
/*      */       case 37:
/* 1248 */         if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1249 */           this.ch = localScannerInputReader.read();
/* 1250 */           this.token = 4;
/* 1251 */           return l;
/*      */         }
/* 1253 */         this.token = 32;
/* 1254 */         return l;
/*      */       case 94:
/* 1257 */         if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1258 */           this.ch = localScannerInputReader.read();
/* 1259 */           this.token = 12;
/* 1260 */           return l;
/*      */         }
/* 1262 */         this.token = 17;
/* 1263 */         return l;
/*      */       case 33:
/* 1266 */         if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1267 */           this.ch = localScannerInputReader.read();
/* 1268 */           this.token = 19;
/* 1269 */           return l;
/*      */         }
/* 1271 */         this.token = 37;
/* 1272 */         return l;
/*      */       case 42:
/* 1275 */         if ((this.ch = localScannerInputReader.read()) == 61) {
/* 1276 */           this.ch = localScannerInputReader.read();
/* 1277 */           this.token = 2;
/* 1278 */           return l;
/*      */         }
/* 1280 */         this.token = 33;
/* 1281 */         return l;
/*      */       case 36:
/*      */       case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       case 90:
/*      */       case 95:
/*      */       case 97:
/*      */       case 98:
/*      */       case 99:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/* 1294 */         scanIdentifier();
/* 1295 */         return l;
/*      */       case 26:
/* 1299 */         if ((this.ch = localScannerInputReader.read()) == -1) {
/* 1300 */           this.token = -1;
/* 1301 */           return l;
/*      */         }
/* 1303 */         this.env.error(this.pos, "funny.char");
/* 1304 */         this.ch = localScannerInputReader.read();
/* 1305 */         break;
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 11:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*      */       case 35:
/*      */       case 64:
/*      */       case 92:
/*      */       case 96:
/*      */       default:
/* 1309 */         if (Character.isJavaLetter((char)this.ch)) {
/* 1310 */           scanIdentifier();
/* 1311 */           return l;
/*      */         }
/* 1313 */         this.env.error(this.pos, "funny.char");
/* 1314 */         this.ch = localScannerInputReader.read();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void match(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1325 */     int i = 1;
/*      */     do {
/*      */       do {
/*      */         while (true) { scan();
/* 1329 */           if (this.token != paramInt1) break;
/* 1330 */           i++; }
/* 1331 */         if (this.token != paramInt2) break;
/* 1332 */         i--; } while (i != 0);
/* 1333 */       return;
/*      */     }
/* 1335 */     while (this.token != -1);
/* 1336 */     this.env.error(this.pos, "unbalanced.paren");
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  206 */     defineKeyword(92);
/*  207 */     defineKeyword(90);
/*  208 */     defineKeyword(91);
/*  209 */     defineKeyword(93);
/*  210 */     defineKeyword(94);
/*  211 */     defineKeyword(95);
/*  212 */     defineKeyword(96);
/*  213 */     defineKeyword(97);
/*  214 */     defineKeyword(98);
/*  215 */     defineKeyword(99);
/*  216 */     defineKeyword(100);
/*  217 */     defineKeyword(101);
/*  218 */     defineKeyword(102);
/*  219 */     defineKeyword(103);
/*  220 */     defineKeyword(104);
/*      */ 
/*  223 */     defineKeyword(70);
/*  224 */     defineKeyword(71);
/*  225 */     defineKeyword(72);
/*  226 */     defineKeyword(73);
/*  227 */     defineKeyword(74);
/*  228 */     defineKeyword(75);
/*  229 */     defineKeyword(76);
/*  230 */     defineKeyword(77);
/*  231 */     defineKeyword(78);
/*      */ 
/*  234 */     defineKeyword(25);
/*  235 */     defineKeyword(80);
/*  236 */     defineKeyword(81);
/*  237 */     defineKeyword(49);
/*  238 */     defineKeyword(82);
/*  239 */     defineKeyword(83);
/*  240 */     defineKeyword(84);
/*      */ 
/*  243 */     defineKeyword(110);
/*  244 */     defineKeyword(111);
/*  245 */     defineKeyword(112);
/*  246 */     defineKeyword(113);
/*  247 */     defineKeyword(114);
/*  248 */     defineKeyword(115);
/*  249 */     defineKeyword(144);
/*      */ 
/*  252 */     defineKeyword(120);
/*  253 */     defineKeyword(121);
/*  254 */     defineKeyword(122);
/*  255 */     defineKeyword(124);
/*  256 */     defineKeyword(125);
/*  257 */     defineKeyword(126);
/*  258 */     defineKeyword(127);
/*  259 */     defineKeyword(130);
/*  260 */     defineKeyword(129);
/*  261 */     defineKeyword(128);
/*  262 */     defineKeyword(131);
/*      */ 
/*  265 */     defineKeyword(123);
/*  266 */     defineKeyword(58);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Scanner
 * JD-Core Version:    0.6.2
 */