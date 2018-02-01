/*      */ package com.sun.tools.example.debug.expr;
/*      */ 
/*      */ import java.io.IOException;
/*      */ 
/*      */ public class ExpressionParserTokenManager
/*      */   implements ExpressionParserConstants
/*      */ {
/*  937 */   static final long[] jjbitVec0 = { -2L, -1L, -1L, -1L };
/*      */ 
/*  940 */   static final long[] jjbitVec2 = { 0L, 0L, -1L, -1L };
/*      */ 
/*  943 */   static final long[] jjbitVec3 = { 2301339413881290750L, -16384L, 4294967295L, 432345564227567616L };
/*      */ 
/*  946 */   static final long[] jjbitVec4 = { 0L, 0L, 0L, -36028797027352577L };
/*      */ 
/*  949 */   static final long[] jjbitVec5 = { 0L, -1L, -1L, -1L };
/*      */ 
/*  952 */   static final long[] jjbitVec6 = { -1L, -1L, 65535L, 0L };
/*      */ 
/*  955 */   static final long[] jjbitVec7 = { -1L, -1L, 0L, 0L };
/*      */ 
/*  958 */   static final long[] jjbitVec8 = { 70368744177663L, 0L, 0L, 0L };
/*      */ 
/* 1547 */   static final int[] jjnextStates = { 30, 31, 36, 37, 40, 41, 8, 49, 60, 61, 19, 20, 22, 10, 12, 45, 47, 2, 50, 51, 53, 4, 5, 8, 19, 20, 24, 22, 32, 33, 8, 40, 41, 8, 56, 57, 59, 63, 64, 66, 6, 7, 13, 14, 16, 21, 23, 25, 34, 35, 38, 39, 42, 43 };
/*      */ 
/* 1587 */   public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while", null, null, null, null, null, null, null, null, null, null, null, "(", ")", "{", "}", "[", "]", ";", ",", ".", "=", ">", "<", "!", "~", "?", ":", "==", "<=", ">=", "!=", "||", "&&", "++", "--", "+", "-", "*", "/", "&", "|", "^", "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=", ">>=", ">>>=" };
/*      */ 
/* 1609 */   public static final String[] lexStateNames = { "DEFAULT" };
/*      */ 
/* 1612 */   static final long[] jjtoToken = { -8070450532247929343L, 4503599627370446L };
/*      */ 
/* 1615 */   static final long[] jjtoSkip = { 510L, 0L };
/*      */ 
/* 1618 */   static final long[] jjtoSpecial = { 448L, 0L };
/*      */   private ASCII_UCodeESC_CharStream input_stream;
/* 1622 */   private final int[] jjrounds = new int[67];
/* 1623 */   private final int[] jjstateSet = new int[''];
/*      */   protected char curChar;
/* 1679 */   int curLexState = 0;
/* 1680 */   int defaultLexState = 0;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */ 
/*      */   private final int jjStopStringLiteralDfa_0(int paramInt, long paramLong1, long paramLong2)
/*      */   {
/*   42 */     switch (paramInt)
/*      */     {
/*      */     case 0:
/*   45 */       if ((paramLong2 & 0x4000) != 0L) {
/*   46 */         return 4;
/*      */       }
/*   48 */       if ((paramLong1 & 0xFFFFFE00) != 0L)
/*      */       {
/*   50 */         this.jjmatchedKind = 67;
/*   51 */         return 28;
/*      */       }
/*   53 */       if ((paramLong2 & 0x0) != 0L) {
/*   54 */         return 49;
/*      */       }
/*   56 */       return -1;
/*      */     case 1:
/*   58 */       if ((paramLong1 & 0xBFCFFE00) != 0L)
/*      */       {
/*   60 */         if (this.jjmatchedPos != 1)
/*      */         {
/*   62 */           this.jjmatchedKind = 67;
/*   63 */           this.jjmatchedPos = 1;
/*      */         }
/*   65 */         return 28;
/*      */       }
/*   67 */       if ((paramLong1 & 0x40300000) != 0L) {
/*   68 */         return 28;
/*      */       }
/*   70 */       return -1;
/*      */     case 2:
/*   72 */       if ((paramLong1 & 0xAFEFFE00) != 0L)
/*      */       {
/*   74 */         if (this.jjmatchedPos != 2)
/*      */         {
/*   76 */           this.jjmatchedKind = 67;
/*   77 */           this.jjmatchedPos = 2;
/*      */         }
/*   79 */         return 28;
/*      */       }
/*   81 */       if ((paramLong1 & 0x10000000) != 0L) {
/*   82 */         return 28;
/*      */       }
/*   84 */       return -1;
/*      */     case 3:
/*   86 */       if ((paramLong1 & 0x8FAF4E00) != 0L)
/*      */       {
/*   88 */         this.jjmatchedKind = 67;
/*   89 */         this.jjmatchedPos = 3;
/*   90 */         return 28;
/*      */       }
/*   92 */       if ((paramLong1 & 0x2040B000) != 0L) {
/*   93 */         return 28;
/*      */       }
/*   95 */       return -1;
/*      */     case 4:
/*   97 */       if ((paramLong1 & 0x80AC0600) != 0L)
/*      */       {
/*   99 */         if (this.jjmatchedPos != 4)
/*      */         {
/*  101 */           this.jjmatchedKind = 67;
/*  102 */           this.jjmatchedPos = 4;
/*      */         }
/*  104 */         return 28;
/*      */       }
/*  106 */       if ((paramLong1 & 0xF034800) != 0L) {
/*  107 */         return 28;
/*      */       }
/*  109 */       return -1;
/*      */     case 5:
/*  111 */       if ((paramLong1 & 0x848C0600) != 0L)
/*      */       {
/*  113 */         this.jjmatchedKind = 67;
/*  114 */         this.jjmatchedPos = 5;
/*  115 */         return 28;
/*      */       }
/*  117 */       if ((paramLong1 & 0x200000) != 0L) {
/*  118 */         return 28;
/*      */       }
/*  120 */       return -1;
/*      */     case 6:
/*  122 */       if ((paramLong1 & 0x80040200) != 0L)
/*      */       {
/*  124 */         this.jjmatchedKind = 67;
/*  125 */         this.jjmatchedPos = 6;
/*  126 */         return 28;
/*      */       }
/*  128 */       if ((paramLong1 & 0x4880400) != 0L) {
/*  129 */         return 28;
/*      */       }
/*  131 */       return -1;
/*      */     case 7:
/*  133 */       if ((paramLong1 & 0x80000000) != 0L)
/*      */       {
/*  135 */         this.jjmatchedKind = 67;
/*  136 */         this.jjmatchedPos = 7;
/*  137 */         return 28;
/*      */       }
/*  139 */       if ((paramLong1 & 0x40200) != 0L) {
/*  140 */         return 28;
/*      */       }
/*  142 */       return -1;
/*      */     case 8:
/*  144 */       if ((paramLong1 & 0x80000000) != 0L)
/*      */       {
/*  146 */         this.jjmatchedKind = 67;
/*  147 */         this.jjmatchedPos = 8;
/*  148 */         return 28;
/*      */       }
/*  150 */       if ((paramLong1 & 0x0) != 0L) {
/*  151 */         return 28;
/*      */       }
/*  153 */       return -1;
/*      */     case 9:
/*  155 */       if ((paramLong1 & 0x0) != 0L)
/*      */       {
/*  157 */         this.jjmatchedKind = 67;
/*  158 */         this.jjmatchedPos = 9;
/*  159 */         return 28;
/*      */       }
/*  161 */       if ((paramLong1 & 0x80000000) != 0L) {
/*  162 */         return 28;
/*      */       }
/*  164 */       return -1;
/*      */     case 10:
/*  166 */       if ((paramLong1 & 0x0) != 0L)
/*      */       {
/*  168 */         this.jjmatchedKind = 67;
/*  169 */         this.jjmatchedPos = 10;
/*  170 */         return 28;
/*      */       }
/*  172 */       return -1;
/*      */     }
/*  174 */     return -1;
/*      */   }
/*      */ 
/*      */   private final int jjStartNfa_0(int paramInt, long paramLong1, long paramLong2)
/*      */   {
/*  179 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(paramInt, paramLong1, paramLong2), paramInt + 1);
/*      */   }
/*      */ 
/*      */   private final int jjStopAtPos(int paramInt1, int paramInt2) {
/*  183 */     this.jjmatchedKind = paramInt2;
/*  184 */     this.jjmatchedPos = paramInt1;
/*  185 */     return paramInt1 + 1;
/*      */   }
/*      */ 
/*      */   private final int jjStartNfaWithStates_0(int paramInt1, int paramInt2, int paramInt3) {
/*  189 */     this.jjmatchedKind = paramInt2;
/*  190 */     this.jjmatchedPos = paramInt1;
/*      */     try { this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {
/*  192 */       return paramInt1 + 1;
/*  193 */     }return jjMoveNfa_0(paramInt3, paramInt1 + 1);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  197 */     switch (this.curChar)
/*      */     {
/*      */     case '!':
/*  200 */       this.jjmatchedKind = 82;
/*  201 */       return jjMoveStringLiteralDfa1_0(0L, 33554432L);
/*      */     case '%':
/*  203 */       this.jjmatchedKind = 101;
/*  204 */       return jjMoveStringLiteralDfa1_0(0L, 281474976710656L);
/*      */     case '&':
/*  206 */       this.jjmatchedKind = 98;
/*  207 */       return jjMoveStringLiteralDfa1_0(0L, 35184506306560L);
/*      */     case '(':
/*  209 */       return jjStopAtPos(0, 70);
/*      */     case ')':
/*  211 */       return jjStopAtPos(0, 71);
/*      */     case '*':
/*  213 */       this.jjmatchedKind = 96;
/*  214 */       return jjMoveStringLiteralDfa1_0(0L, 8796093022208L);
/*      */     case '+':
/*  216 */       this.jjmatchedKind = 94;
/*  217 */       return jjMoveStringLiteralDfa1_0(0L, 2199291691008L);
/*      */     case ',':
/*  219 */       return jjStopAtPos(0, 77);
/*      */     case '-':
/*  221 */       this.jjmatchedKind = 95;
/*  222 */       return jjMoveStringLiteralDfa1_0(0L, 4398583382016L);
/*      */     case '.':
/*  224 */       return jjStartNfaWithStates_0(0, 78, 4);
/*      */     case '/':
/*  226 */       this.jjmatchedKind = 97;
/*  227 */       return jjMoveStringLiteralDfa1_0(0L, 17592186044416L);
/*      */     case ':':
/*  229 */       return jjStopAtPos(0, 85);
/*      */     case ';':
/*  231 */       return jjStopAtPos(0, 76);
/*      */     case '<':
/*  233 */       this.jjmatchedKind = 81;
/*  234 */       return jjMoveStringLiteralDfa1_0(0L, 563224839716864L);
/*      */     case '=':
/*  236 */       this.jjmatchedKind = 79;
/*  237 */       return jjMoveStringLiteralDfa1_0(0L, 4194304L);
/*      */     case '>':
/*  239 */       this.jjmatchedKind = 80;
/*  240 */       return jjMoveStringLiteralDfa1_0(0L, 3379349004746752L);
/*      */     case '?':
/*  242 */       return jjStopAtPos(0, 84);
/*      */     case '[':
/*  244 */       return jjStopAtPos(0, 74);
/*      */     case ']':
/*  246 */       return jjStopAtPos(0, 75);
/*      */     case '^':
/*  248 */       this.jjmatchedKind = 100;
/*  249 */       return jjMoveStringLiteralDfa1_0(0L, 140737488355328L);
/*      */     case 'a':
/*  251 */       return jjMoveStringLiteralDfa1_0(512L, 0L);
/*      */     case 'b':
/*  253 */       return jjMoveStringLiteralDfa1_0(7168L, 0L);
/*      */     case 'c':
/*  255 */       return jjMoveStringLiteralDfa1_0(516096L, 0L);
/*      */     case 'd':
/*  257 */       return jjMoveStringLiteralDfa1_0(3670016L, 0L);
/*      */     case 'e':
/*  259 */       return jjMoveStringLiteralDfa1_0(12582912L, 0L);
/*      */     case 'f':
/*  261 */       return jjMoveStringLiteralDfa1_0(520093696L, 0L);
/*      */     case 'g':
/*  263 */       return jjMoveStringLiteralDfa1_0(536870912L, 0L);
/*      */     case 'i':
/*  265 */       return jjMoveStringLiteralDfa1_0(67645734912L, 0L);
/*      */     case 'l':
/*  267 */       return jjMoveStringLiteralDfa1_0(68719476736L, 0L);
/*      */     case 'n':
/*  269 */       return jjMoveStringLiteralDfa1_0(962072674304L, 0L);
/*      */     case 'p':
/*  271 */       return jjMoveStringLiteralDfa1_0(16492674416640L, 0L);
/*      */     case 'r':
/*  273 */       return jjMoveStringLiteralDfa1_0(17592186044416L, 0L);
/*      */     case 's':
/*  275 */       return jjMoveStringLiteralDfa1_0(1090715534753792L, 0L);
/*      */     case 't':
/*  277 */       return jjMoveStringLiteralDfa1_0(70931694131085312L, 0L);
/*      */     case 'v':
/*  279 */       return jjMoveStringLiteralDfa1_0(216172782113783808L, 0L);
/*      */     case 'w':
/*  281 */       return jjMoveStringLiteralDfa1_0(288230376151711744L, 0L);
/*      */     case '{':
/*  283 */       return jjStopAtPos(0, 72);
/*      */     case '|':
/*  285 */       this.jjmatchedKind = 99;
/*  286 */       return jjMoveStringLiteralDfa1_0(0L, 70368811286528L);
/*      */     case '}':
/*  288 */       return jjStopAtPos(0, 73);
/*      */     case '~':
/*  290 */       return jjStopAtPos(0, 83);
/*      */     case '"':
/*      */     case '#':
/*      */     case '$':
/*      */     case '\'':
/*      */     case '0':
/*      */     case '1':
/*      */     case '2':
/*      */     case '3':
/*      */     case '4':
/*      */     case '5':
/*      */     case '6':
/*      */     case '7':
/*      */     case '8':
/*      */     case '9':
/*      */     case '@':
/*      */     case 'A':
/*      */     case 'B':
/*      */     case 'C':
/*      */     case 'D':
/*      */     case 'E':
/*      */     case 'F':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'I':
/*      */     case 'J':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     case 'Z':
/*      */     case '\\':
/*      */     case '_':
/*      */     case '`':
/*      */     case 'h':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'm':
/*      */     case 'o':
/*      */     case 'q':
/*      */     case 'u':
/*      */     case 'x':
/*      */     case 'y':
/*  292 */     case 'z': } return jjMoveNfa_0(0, 0);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa1_0(long paramLong1, long paramLong2) {
/*      */     try {
/*  297 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  299 */       jjStopStringLiteralDfa_0(0, paramLong1, paramLong2);
/*  300 */       return 1;
/*      */     }
/*  302 */     switch (this.curChar)
/*      */     {
/*      */     case '&':
/*  305 */       if ((paramLong2 & 0x8000000) != 0L) {
/*  306 */         return jjStopAtPos(1, 91);
/*      */       }
/*      */       break;
/*      */     case '+':
/*  310 */       if ((paramLong2 & 0x10000000) != 0L) {
/*  311 */         return jjStopAtPos(1, 92);
/*      */       }
/*      */       break;
/*      */     case '-':
/*  315 */       if ((paramLong2 & 0x20000000) != 0L) {
/*  316 */         return jjStopAtPos(1, 93);
/*      */       }
/*      */       break;
/*      */     case '<':
/*  320 */       if ((paramLong2 & 0x0) != 0L)
/*      */       {
/*  322 */         this.jjmatchedKind = 102;
/*  323 */         this.jjmatchedPos = 1;
/*      */       }
/*  325 */       return jjMoveStringLiteralDfa2_0(paramLong1, 0L, paramLong2, 562949953421312L);
/*      */     case '=':
/*  327 */       if ((paramLong2 & 0x400000) != 0L)
/*  328 */         return jjStopAtPos(1, 86);
/*  329 */       if ((paramLong2 & 0x800000) != 0L)
/*  330 */         return jjStopAtPos(1, 87);
/*  331 */       if ((paramLong2 & 0x1000000) != 0L)
/*  332 */         return jjStopAtPos(1, 88);
/*  333 */       if ((paramLong2 & 0x2000000) != 0L)
/*  334 */         return jjStopAtPos(1, 89);
/*  335 */       if ((paramLong2 & 0x0) != 0L)
/*  336 */         return jjStopAtPos(1, 105);
/*  337 */       if ((paramLong2 & 0x0) != 0L)
/*  338 */         return jjStopAtPos(1, 106);
/*  339 */       if ((paramLong2 & 0x0) != 0L)
/*  340 */         return jjStopAtPos(1, 107);
/*  341 */       if ((paramLong2 & 0x0) != 0L)
/*  342 */         return jjStopAtPos(1, 108);
/*  343 */       if ((paramLong2 & 0x0) != 0L)
/*  344 */         return jjStopAtPos(1, 109);
/*  345 */       if ((paramLong2 & 0x0) != 0L)
/*  346 */         return jjStopAtPos(1, 110);
/*  347 */       if ((paramLong2 & 0x0) != 0L)
/*  348 */         return jjStopAtPos(1, 111);
/*  349 */       if ((paramLong2 & 0x0) != 0L) {
/*  350 */         return jjStopAtPos(1, 112);
/*      */       }
/*      */       break;
/*      */     case '>':
/*  354 */       if ((paramLong2 & 0x0) != 0L)
/*      */       {
/*  356 */         this.jjmatchedKind = 103;
/*  357 */         this.jjmatchedPos = 1;
/*      */       }
/*  359 */       return jjMoveStringLiteralDfa2_0(paramLong1, 0L, paramLong2, 3378799232155648L);
/*      */     case 'a':
/*  361 */       return jjMoveStringLiteralDfa2_0(paramLong1, 1236967383040L, paramLong2, 0L);
/*      */     case 'b':
/*  363 */       return jjMoveStringLiteralDfa2_0(paramLong1, 512L, paramLong2, 0L);
/*      */     case 'e':
/*  365 */       return jjMoveStringLiteralDfa2_0(paramLong1, 17867064475648L, paramLong2, 0L);
/*      */     case 'f':
/*  367 */       if ((paramLong1 & 0x40000000) != 0L) {
/*  368 */         return jjStartNfaWithStates_0(1, 30, 28);
/*      */       }
/*      */       break;
/*      */     case 'h':
/*  372 */       return jjMoveStringLiteralDfa2_0(paramLong1, 296146859871731712L, paramLong2, 0L);
/*      */     case 'i':
/*  374 */       return jjMoveStringLiteralDfa2_0(paramLong1, 100663296L, paramLong2, 0L);
/*      */     case 'l':
/*  376 */       return jjMoveStringLiteralDfa2_0(paramLong1, 138477568L, paramLong2, 0L);
/*      */     case 'm':
/*  378 */       return jjMoveStringLiteralDfa2_0(paramLong1, 6442450944L, paramLong2, 0L);
/*      */     case 'n':
/*  380 */       return jjMoveStringLiteralDfa2_0(paramLong1, 60129542144L, paramLong2, 0L);
/*      */     case 'o':
/*  382 */       if ((paramLong1 & 0x100000) != 0L)
/*      */       {
/*  384 */         this.jjmatchedKind = 20;
/*  385 */         this.jjmatchedPos = 1;
/*      */       }
/*  387 */       return jjMoveStringLiteralDfa2_0(paramLong1, 216172851641058304L, paramLong2, 0L);
/*      */     case 'r':
/*  389 */       return jjMoveStringLiteralDfa2_0(paramLong1, 63056991852955648L, paramLong2, 0L);
/*      */     case 't':
/*  391 */       return jjMoveStringLiteralDfa2_0(paramLong1, 70368744177664L, paramLong2, 0L);
/*      */     case 'u':
/*  393 */       return jjMoveStringLiteralDfa2_0(paramLong1, 150083337191424L, paramLong2, 0L);
/*      */     case 'w':
/*  395 */       return jjMoveStringLiteralDfa2_0(paramLong1, 281474976710656L, paramLong2, 0L);
/*      */     case 'x':
/*  397 */       return jjMoveStringLiteralDfa2_0(paramLong1, 8388608L, paramLong2, 0L);
/*      */     case 'y':
/*  399 */       return jjMoveStringLiteralDfa2_0(paramLong1, 562949953425408L, paramLong2, 0L);
/*      */     case '|':
/*  401 */       if ((paramLong2 & 0x4000000) != 0L)
/*  402 */         return jjStopAtPos(1, 90); break;
/*      */     case '\'':
/*      */     case '(':
/*      */     case ')':
/*      */     case '*':
/*      */     case ',':
/*      */     case '.':
/*      */     case '/':
/*      */     case '0':
/*      */     case '1':
/*      */     case '2':
/*      */     case '3':
/*      */     case '4':
/*      */     case '5':
/*      */     case '6':
/*      */     case '7':
/*      */     case '8':
/*      */     case '9':
/*      */     case ':':
/*      */     case ';':
/*      */     case '?':
/*      */     case '@':
/*      */     case 'A':
/*      */     case 'B':
/*      */     case 'C':
/*      */     case 'D':
/*      */     case 'E':
/*      */     case 'F':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'I':
/*      */     case 'J':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     case 'Z':
/*      */     case '[':
/*      */     case '\\':
/*      */     case ']':
/*      */     case '^':
/*      */     case '_':
/*      */     case '`':
/*      */     case 'c':
/*      */     case 'd':
/*      */     case 'g':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'p':
/*      */     case 'q':
/*      */     case 's':
/*      */     case 'v':
/*      */     case 'z':
/*  408 */     case '{': } return jjStartNfa_0(0, paramLong1, paramLong2);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa2_0(long paramLong1, long paramLong2, long paramLong3, long paramLong4) {
/*  412 */     if ((paramLong2 &= paramLong1 | paramLong4 &= paramLong3) == 0L)
/*  413 */       return jjStartNfa_0(0, paramLong1, paramLong3);
/*      */     try {
/*  415 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  417 */       jjStopStringLiteralDfa_0(1, paramLong2, paramLong4);
/*  418 */       return 2;
/*      */     }
/*  420 */     switch (this.curChar)
/*      */     {
/*      */     case '=':
/*  423 */       if ((paramLong4 & 0x0) != 0L)
/*  424 */         return jjStopAtPos(2, 113);
/*  425 */       if ((paramLong4 & 0x0) != 0L) {
/*  426 */         return jjStopAtPos(2, 114);
/*      */       }
/*      */       break;
/*      */     case '>':
/*  430 */       if ((paramLong4 & 0x0) != 0L)
/*      */       {
/*  432 */         this.jjmatchedKind = 104;
/*  433 */         this.jjmatchedPos = 2;
/*      */       }
/*  435 */       return jjMoveStringLiteralDfa3_0(paramLong2, 0L, paramLong4, 2251799813685248L);
/*      */     case 'a':
/*  437 */       return jjMoveStringLiteralDfa3_0(paramLong2, 9077567999016960L, paramLong4, 0L);
/*      */     case 'b':
/*  439 */       return jjMoveStringLiteralDfa3_0(paramLong2, 8796093022208L, paramLong4, 0L);
/*      */     case 'c':
/*  441 */       return jjMoveStringLiteralDfa3_0(paramLong2, 1099511627776L, paramLong4, 0L);
/*      */     case 'e':
/*  443 */       return jjMoveStringLiteralDfa3_0(paramLong2, 2048L, paramLong4, 0L);
/*      */     case 'f':
/*  445 */       return jjMoveStringLiteralDfa3_0(paramLong2, 524288L, paramLong4, 0L);
/*      */     case 'i':
/*  447 */       return jjMoveStringLiteralDfa3_0(paramLong2, 361697544096448512L, paramLong4, 0L);
/*      */     case 'l':
/*  449 */       return jjMoveStringLiteralDfa3_0(paramLong2, 144115737848446976L, paramLong4, 0L);
/*      */     case 'n':
/*  451 */       return jjMoveStringLiteralDfa3_0(paramLong2, 563018773954560L, paramLong4, 0L);
/*      */     case 'o':
/*  453 */       return jjMoveStringLiteralDfa3_0(paramLong2, 39582552818688L, paramLong4, 0L);
/*      */     case 'p':
/*  455 */       return jjMoveStringLiteralDfa3_0(paramLong2, 140743930806272L, paramLong4, 0L);
/*      */     case 'r':
/*  457 */       if ((paramLong2 & 0x10000000) != 0L) {
/*  458 */         return jjStartNfaWithStates_0(2, 28, 28);
/*      */       }
/*  460 */       return jjMoveStringLiteralDfa3_0(paramLong2, 6755399441055744L, paramLong4, 0L);
/*      */     case 's':
/*  462 */       return jjMoveStringLiteralDfa3_0(paramLong2, 8594137600L, paramLong4, 0L);
/*      */     case 't':
/*  464 */       if ((paramLong2 & 0x0) != 0L)
/*      */       {
/*  466 */         this.jjmatchedKind = 34;
/*  467 */         this.jjmatchedPos = 2;
/*      */       }
/*  469 */       return jjMoveStringLiteralDfa3_0(paramLong2, 17764530016256L, paramLong4, 0L);
/*      */     case 'u':
/*  471 */       return jjMoveStringLiteralDfa3_0(paramLong2, 18014398511579136L, paramLong4, 0L);
/*      */     case 'w':
/*  473 */       if ((paramLong2 & 0x0) != 0L) {
/*  474 */         return jjStartNfaWithStates_0(2, 38, 28);
/*      */       }
/*      */       break;
/*      */     case 'y':
/*  478 */       if ((paramLong2 & 0x0) != 0L)
/*  479 */         return jjStartNfaWithStates_0(2, 55, 28); break;
/*      */     case '?':
/*      */     case '@':
/*      */     case 'A':
/*      */     case 'B':
/*      */     case 'C':
/*      */     case 'D':
/*      */     case 'E':
/*      */     case 'F':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'I':
/*      */     case 'J':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     case 'Z':
/*      */     case '[':
/*      */     case '\\':
/*      */     case ']':
/*      */     case '^':
/*      */     case '_':
/*      */     case '`':
/*      */     case 'd':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'm':
/*      */     case 'q':
/*      */     case 'v':
/*  485 */     case 'x': } return jjStartNfa_0(1, paramLong2, paramLong4);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa3_0(long paramLong1, long paramLong2, long paramLong3, long paramLong4) {
/*  489 */     if ((paramLong2 &= paramLong1 | paramLong4 &= paramLong3) == 0L)
/*  490 */       return jjStartNfa_0(1, paramLong1, paramLong3);
/*      */     try {
/*  492 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  494 */       jjStopStringLiteralDfa_0(2, paramLong2, paramLong4);
/*  495 */       return 3;
/*      */     }
/*  497 */     switch (this.curChar)
/*      */     {
/*      */     case '=':
/*  500 */       if ((paramLong4 & 0x0) != 0L) {
/*  501 */         return jjStopAtPos(3, 115);
/*      */       }
/*      */       break;
/*      */     case 'a':
/*  505 */       return jjMoveStringLiteralDfa4_0(paramLong2, 144115188311263232L, paramLong4, 0L);
/*      */     case 'b':
/*  507 */       return jjMoveStringLiteralDfa4_0(paramLong2, 2097152L, paramLong4, 0L);
/*      */     case 'c':
/*  509 */       return jjMoveStringLiteralDfa4_0(paramLong2, 562949953437696L, paramLong4, 0L);
/*      */     case 'd':
/*  511 */       if ((paramLong2 & 0x0) != 0L) {
/*  512 */         return jjStartNfaWithStates_0(3, 56, 28);
/*      */       }
/*      */       break;
/*      */     case 'e':
/*  516 */       if ((paramLong2 & 0x1000) != 0L)
/*  517 */         return jjStartNfaWithStates_0(3, 12, 28);
/*  518 */       if ((paramLong2 & 0x2000) != 0L)
/*  519 */         return jjStartNfaWithStates_0(3, 13, 28);
/*  520 */       if ((paramLong2 & 0x400000) != 0L)
/*  521 */         return jjStartNfaWithStates_0(3, 22, 28);
/*  522 */       if ((paramLong2 & 0x0) != 0L) {
/*  523 */         return jjStartNfaWithStates_0(3, 54, 28);
/*      */       }
/*  525 */       return jjMoveStringLiteralDfa4_0(paramLong2, 140771856482304L, paramLong4, 0L);
/*      */     case 'g':
/*  527 */       if ((paramLong2 & 0x0) != 0L) {
/*  528 */         return jjStartNfaWithStates_0(3, 36, 28);
/*      */       }
/*      */       break;
/*      */     case 'i':
/*  532 */       return jjMoveStringLiteralDfa4_0(paramLong2, 137438953472L, paramLong4, 0L);
/*      */     case 'k':
/*  534 */       return jjMoveStringLiteralDfa4_0(paramLong2, 1099511627776L, paramLong4, 0L);
/*      */     case 'l':
/*  536 */       if ((paramLong2 & 0x0) != 0L) {
/*  537 */         return jjStartNfaWithStates_0(3, 39, 28);
/*      */       }
/*  539 */       return jjMoveStringLiteralDfa4_0(paramLong2, 288239174392218624L, paramLong4, 0L);
/*      */     case 'n':
/*  541 */       return jjMoveStringLiteralDfa4_0(paramLong2, 9007199254740992L, paramLong4, 0L);
/*      */     case 'o':
/*  543 */       if ((paramLong2 & 0x20000000) != 0L) {
/*  544 */         return jjStartNfaWithStates_0(3, 29, 28);
/*      */       }
/*  546 */       return jjMoveStringLiteralDfa4_0(paramLong2, 6755403736023040L, paramLong4, 0L);
/*      */     case 'r':
/*  548 */       if ((paramLong2 & 0x8000) != 0L) {
/*  549 */         return jjStartNfaWithStates_0(3, 15, 28);
/*      */       }
/*  551 */       return jjMoveStringLiteralDfa4_0(paramLong2, 35184372088832L, paramLong4, 0L);
/*      */     case 's':
/*  553 */       if ((paramLong2 & 0x0) != 0L) {
/*  554 */         return jjStartNfaWithStates_0(3, 50, 28);
/*      */       }
/*  556 */       return jjMoveStringLiteralDfa4_0(paramLong2, 16973824L, paramLong4, 0L);
/*      */     case 't':
/*  558 */       return jjMoveStringLiteralDfa4_0(paramLong2, 356250357596672L, paramLong4, 0L);
/*      */     case 'u':
/*  560 */       return jjMoveStringLiteralDfa4_0(paramLong2, 17592186044416L, paramLong4, 0L);
/*      */     case 'v':
/*  562 */       return jjMoveStringLiteralDfa4_0(paramLong2, 2199023255552L, paramLong4, 0L);
/*      */     case '>':
/*      */     case '?':
/*      */     case '@':
/*      */     case 'A':
/*      */     case 'B':
/*      */     case 'C':
/*      */     case 'D':
/*      */     case 'E':
/*      */     case 'F':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'I':
/*      */     case 'J':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'S':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     case 'Z':
/*      */     case '[':
/*      */     case '\\':
/*      */     case ']':
/*      */     case '^':
/*      */     case '_':
/*      */     case '`':
/*      */     case 'f':
/*      */     case 'h':
/*      */     case 'j':
/*      */     case 'm':
/*      */     case 'p':
/*  566 */     case 'q': } return jjStartNfa_0(2, paramLong2, paramLong4);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa4_0(long paramLong1, long paramLong2, long paramLong3, long paramLong4) {
/*  570 */     if ((paramLong2 &= paramLong1 | paramLong4 &= paramLong3) == 0L)
/*  571 */       return jjStartNfa_0(2, paramLong1, paramLong3);
/*      */     try {
/*  573 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  575 */       jjStopStringLiteralDfa_0(3, paramLong2, 0L);
/*  576 */       return 4;
/*      */     }
/*  578 */     switch (this.curChar)
/*      */     {
/*      */     case 'a':
/*  581 */       return jjMoveStringLiteralDfa5_0(paramLong2, 3307124817920L);
/*      */     case 'c':
/*  583 */       return jjMoveStringLiteralDfa5_0(paramLong2, 281474976710656L);
/*      */     case 'e':
/*  585 */       if ((paramLong2 & 0x1000000) != 0L)
/*  586 */         return jjStartNfaWithStates_0(4, 24, 28);
/*  587 */       if ((paramLong2 & 0x0) != 0L) {
/*  588 */         return jjStartNfaWithStates_0(4, 58, 28);
/*      */       }
/*  590 */       return jjMoveStringLiteralDfa5_0(paramLong2, 4400193995776L);
/*      */     case 'h':
/*  592 */       if ((paramLong2 & 0x4000) != 0L) {
/*  593 */         return jjStartNfaWithStates_0(4, 14, 28);
/*      */       }
/*  595 */       return jjMoveStringLiteralDfa5_0(paramLong2, 562949953421312L);
/*      */     case 'i':
/*  597 */       return jjMoveStringLiteralDfa5_0(paramLong2, 79164837462016L);
/*      */     case 'k':
/*  599 */       if ((paramLong2 & 0x800) != 0L) {
/*  600 */         return jjStartNfaWithStates_0(4, 11, 28);
/*      */       }
/*      */       break;
/*      */     case 'l':
/*  604 */       if ((paramLong2 & 0x2000000) != 0L)
/*      */       {
/*  606 */         this.jjmatchedKind = 25;
/*  607 */         this.jjmatchedPos = 4;
/*      */       }
/*  609 */       return jjMoveStringLiteralDfa5_0(paramLong2, 69206016L);
/*      */     case 'n':
/*  611 */       return jjMoveStringLiteralDfa5_0(paramLong2, 8388608L);
/*      */     case 'r':
/*  613 */       if ((paramLong2 & 0x0) != 0L) {
/*  614 */         return jjStartNfaWithStates_0(4, 47, 28);
/*      */       }
/*  616 */       return jjMoveStringLiteralDfa5_0(paramLong2, 17630840750592L);
/*      */     case 's':
/*  618 */       if ((paramLong2 & 0x10000) != 0L) {
/*  619 */         return jjStartNfaWithStates_0(4, 16, 28);
/*      */       }
/*  621 */       return jjMoveStringLiteralDfa5_0(paramLong2, 9007199254740992L);
/*      */     case 't':
/*  623 */       if ((paramLong2 & 0x20000) != 0L)
/*  624 */         return jjStartNfaWithStates_0(4, 17, 28);
/*  625 */       if ((paramLong2 & 0x8000000) != 0L)
/*  626 */         return jjStartNfaWithStates_0(4, 27, 28);
/*  627 */       if ((paramLong2 & 0x0) != 0L) {
/*  628 */         return jjStartNfaWithStates_0(4, 45, 28);
/*      */       }
/*  630 */       return jjMoveStringLiteralDfa5_0(paramLong2, 144115188075855872L);
/*      */     case 'u':
/*  632 */       return jjMoveStringLiteralDfa5_0(paramLong2, 524288L);
/*      */     case 'v':
/*  634 */       return jjMoveStringLiteralDfa5_0(paramLong2, 137438953472L);
/*      */     case 'w':
/*  636 */       if ((paramLong2 & 0x0) != 0L)
/*      */       {
/*  638 */         this.jjmatchedKind = 51;
/*  639 */         this.jjmatchedPos = 4;
/*      */       }
/*  641 */       return jjMoveStringLiteralDfa5_0(paramLong2, 4503599627370496L);
/*      */     case 'b':
/*      */     case 'd':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'j':
/*      */     case 'm':
/*      */     case 'o':
/*      */     case 'p':
/*  645 */     case 'q': } return jjStartNfa_0(3, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa5_0(long paramLong1, long paramLong2) {
/*  649 */     if ((paramLong2 &= paramLong1) == 0L)
/*  650 */       return jjStartNfa_0(3, paramLong1, 0L);
/*      */     try {
/*  652 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  654 */       jjStopStringLiteralDfa_0(4, paramLong2, 0L);
/*  655 */       return 5;
/*      */     }
/*  657 */     switch (this.curChar)
/*      */     {
/*      */     case 'a':
/*  660 */       return jjMoveStringLiteralDfa6_0(paramLong2, 1536L);
/*      */     case 'c':
/*  662 */       if ((paramLong2 & 0x0) != 0L)
/*  663 */         return jjStartNfaWithStates_0(5, 43, 28);
/*  664 */       if ((paramLong2 & 0x0) != 0L) {
/*  665 */         return jjStartNfaWithStates_0(5, 46, 28);
/*      */       }
/*  667 */       return jjMoveStringLiteralDfa6_0(paramLong2, 4398046511104L);
/*      */     case 'd':
/*  669 */       return jjMoveStringLiteralDfa6_0(paramLong2, 8388608L);
/*      */     case 'e':
/*  671 */       if ((paramLong2 & 0x200000) != 0L)
/*  672 */         return jjStartNfaWithStates_0(5, 21, 28);
/*  673 */       if ((paramLong2 & 0x0) != 0L) {
/*  674 */         return jjStartNfaWithStates_0(5, 37, 28);
/*      */       }
/*      */       break;
/*      */     case 'f':
/*  678 */       return jjMoveStringLiteralDfa6_0(paramLong2, 34359738368L);
/*      */     case 'g':
/*  680 */       return jjMoveStringLiteralDfa6_0(paramLong2, 1099511627776L);
/*      */     case 'h':
/*  682 */       if ((paramLong2 & 0x0) != 0L) {
/*  683 */         return jjStartNfaWithStates_0(5, 48, 28);
/*      */       }
/*      */       break;
/*      */     case 'i':
/*  687 */       return jjMoveStringLiteralDfa6_0(paramLong2, 153122387330596864L);
/*      */     case 'l':
/*  689 */       return jjMoveStringLiteralDfa6_0(paramLong2, 67633152L);
/*      */     case 'm':
/*  691 */       return jjMoveStringLiteralDfa6_0(paramLong2, 2147483648L);
/*      */     case 'n':
/*  693 */       if ((paramLong2 & 0x0) != 0L) {
/*  694 */         return jjStartNfaWithStates_0(5, 44, 28);
/*      */       }
/*  696 */       return jjMoveStringLiteralDfa6_0(paramLong2, 8590196736L);
/*      */     case 'r':
/*  698 */       return jjMoveStringLiteralDfa6_0(paramLong2, 562949953421312L);
/*      */     case 's':
/*  700 */       if ((paramLong2 & 0x0) != 0L) {
/*  701 */         return jjStartNfaWithStates_0(5, 52, 28);
/*      */       }
/*      */       break;
/*      */     case 't':
/*  705 */       if ((paramLong2 & 0x0) != 0L) {
/*  706 */         return jjStartNfaWithStates_0(5, 32, 28);
/*      */       }
/*  708 */       return jjMoveStringLiteralDfa6_0(paramLong2, 2199023255552L);
/*      */     case 'b':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'o':
/*      */     case 'p':
/*  712 */     case 'q': } return jjStartNfa_0(4, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa6_0(long paramLong1, long paramLong2) {
/*  716 */     if ((paramLong2 &= paramLong1) == 0L)
/*  717 */       return jjStartNfa_0(4, paramLong1, 0L);
/*      */     try {
/*  719 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  721 */       jjStopStringLiteralDfa_0(5, paramLong2, 0L);
/*  722 */       return 6;
/*      */     }
/*  724 */     switch (this.curChar)
/*      */     {
/*      */     case 'a':
/*  727 */       return jjMoveStringLiteralDfa7_0(paramLong2, 34359738368L);
/*      */     case 'c':
/*  729 */       return jjMoveStringLiteralDfa7_0(paramLong2, 8589935104L);
/*      */     case 'e':
/*  731 */       if ((paramLong2 & 0x0) != 0L)
/*  732 */         return jjStartNfaWithStates_0(6, 40, 28);
/*  733 */       if ((paramLong2 & 0x0) != 0L) {
/*  734 */         return jjStartNfaWithStates_0(6, 41, 28);
/*      */       }
/*  736 */       return jjMoveStringLiteralDfa7_0(paramLong2, 9007201402224640L);
/*      */     case 'l':
/*  738 */       return jjMoveStringLiteralDfa7_0(paramLong2, 144115188075855872L);
/*      */     case 'n':
/*  740 */       if ((paramLong2 & 0x400) != 0L) {
/*  741 */         return jjStartNfaWithStates_0(6, 10, 28);
/*      */       }
/*      */       break;
/*      */     case 'o':
/*  745 */       return jjMoveStringLiteralDfa7_0(paramLong2, 562949953421312L);
/*      */     case 's':
/*  747 */       if ((paramLong2 & 0x800000) != 0L) {
/*  748 */         return jjStartNfaWithStates_0(6, 23, 28);
/*      */       }
/*      */       break;
/*      */     case 't':
/*  752 */       if ((paramLong2 & 0x80000) != 0L) {
/*  753 */         return jjStartNfaWithStates_0(6, 19, 28);
/*      */       }
/*  755 */       return jjMoveStringLiteralDfa7_0(paramLong2, 4398046511104L);
/*      */     case 'u':
/*  757 */       return jjMoveStringLiteralDfa7_0(paramLong2, 262144L);
/*      */     case 'y':
/*  759 */       if ((paramLong2 & 0x4000000) != 0L)
/*  760 */         return jjStartNfaWithStates_0(6, 26, 28); break;
/*      */     case 'b':
/*      */     case 'd':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'm':
/*      */     case 'p':
/*      */     case 'q':
/*      */     case 'r':
/*      */     case 'v':
/*      */     case 'w':
/*  766 */     case 'x': } return jjStartNfa_0(5, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa7_0(long paramLong1, long paramLong2) {
/*  770 */     if ((paramLong2 &= paramLong1) == 0L)
/*  771 */       return jjStartNfa_0(5, paramLong1, 0L);
/*      */     try {
/*  773 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  775 */       jjStopStringLiteralDfa_0(6, paramLong2, 0L);
/*  776 */       return 7;
/*      */     }
/*  778 */     switch (this.curChar)
/*      */     {
/*      */     case 'c':
/*  781 */       return jjMoveStringLiteralDfa8_0(paramLong2, 34359738368L);
/*      */     case 'e':
/*  783 */       if ((paramLong2 & 0x40000) != 0L)
/*  784 */         return jjStartNfaWithStates_0(7, 18, 28);
/*  785 */       if ((paramLong2 & 0x0) != 0L) {
/*  786 */         return jjStartNfaWithStates_0(7, 57, 28);
/*      */       }
/*  788 */       return jjMoveStringLiteralDfa8_0(paramLong2, 4406636445696L);
/*      */     case 'n':
/*  790 */       return jjMoveStringLiteralDfa8_0(paramLong2, 9570151355645952L);
/*      */     case 't':
/*  792 */       if ((paramLong2 & 0x200) != 0L) {
/*  793 */         return jjStartNfaWithStates_0(7, 9, 28);
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  799 */     return jjStartNfa_0(6, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa8_0(long paramLong1, long paramLong2) {
/*  803 */     if ((paramLong2 &= paramLong1) == 0L)
/*  804 */       return jjStartNfa_0(6, paramLong1, 0L);
/*      */     try {
/*  806 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  808 */       jjStopStringLiteralDfa_0(7, paramLong2, 0L);
/*  809 */       return 8;
/*      */     }
/*  811 */     switch (this.curChar)
/*      */     {
/*      */     case 'd':
/*  814 */       if ((paramLong2 & 0x0) != 0L) {
/*  815 */         return jjStartNfaWithStates_0(8, 42, 28);
/*      */       }
/*      */       break;
/*      */     case 'e':
/*  819 */       if ((paramLong2 & 0x0) != 0L) {
/*  820 */         return jjStartNfaWithStates_0(8, 35, 28);
/*      */       }
/*      */       break;
/*      */     case 'i':
/*  824 */       return jjMoveStringLiteralDfa9_0(paramLong2, 562949953421312L);
/*      */     case 'o':
/*  826 */       return jjMoveStringLiteralDfa9_0(paramLong2, 8589934592L);
/*      */     case 't':
/*  828 */       if ((paramLong2 & 0x0) != 0L) {
/*  829 */         return jjStartNfaWithStates_0(8, 53, 28);
/*      */       }
/*  831 */       return jjMoveStringLiteralDfa9_0(paramLong2, 2147483648L);
/*      */     }
/*      */ 
/*  835 */     return jjStartNfa_0(7, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa9_0(long paramLong1, long paramLong2) {
/*  839 */     if ((paramLong2 &= paramLong1) == 0L)
/*  840 */       return jjStartNfa_0(7, paramLong1, 0L);
/*      */     try {
/*  842 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  844 */       jjStopStringLiteralDfa_0(8, paramLong2, 0L);
/*  845 */       return 9;
/*      */     }
/*  847 */     switch (this.curChar)
/*      */     {
/*      */     case 'f':
/*  850 */       if ((paramLong2 & 0x0) != 0L) {
/*  851 */         return jjStartNfaWithStates_0(9, 33, 28);
/*      */       }
/*      */       break;
/*      */     case 's':
/*  855 */       if ((paramLong2 & 0x80000000) != 0L) {
/*  856 */         return jjStartNfaWithStates_0(9, 31, 28);
/*      */       }
/*      */       break;
/*      */     case 'z':
/*  860 */       return jjMoveStringLiteralDfa10_0(paramLong2, 562949953421312L);
/*      */     }
/*      */ 
/*  864 */     return jjStartNfa_0(8, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa10_0(long paramLong1, long paramLong2) {
/*  868 */     if ((paramLong2 &= paramLong1) == 0L)
/*  869 */       return jjStartNfa_0(8, paramLong1, 0L);
/*      */     try {
/*  871 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  873 */       jjStopStringLiteralDfa_0(9, paramLong2, 0L);
/*  874 */       return 10;
/*      */     }
/*  876 */     switch (this.curChar)
/*      */     {
/*      */     case 'e':
/*  879 */       return jjMoveStringLiteralDfa11_0(paramLong2, 562949953421312L);
/*      */     }
/*      */ 
/*  883 */     return jjStartNfa_0(9, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final int jjMoveStringLiteralDfa11_0(long paramLong1, long paramLong2) {
/*  887 */     if ((paramLong2 &= paramLong1) == 0L)
/*  888 */       return jjStartNfa_0(9, paramLong1, 0L);
/*      */     try {
/*  890 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  892 */       jjStopStringLiteralDfa_0(10, paramLong2, 0L);
/*  893 */       return 11;
/*      */     }
/*  895 */     switch (this.curChar)
/*      */     {
/*      */     case 'd':
/*  898 */       if ((paramLong2 & 0x0) != 0L) {
/*  899 */         return jjStartNfaWithStates_0(11, 49, 28);
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  905 */     return jjStartNfa_0(10, paramLong2, 0L);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAdd(int paramInt) {
/*  909 */     if (this.jjrounds[paramInt] != this.jjround)
/*      */     {
/*  911 */       this.jjstateSet[(this.jjnewStateCnt++)] = paramInt;
/*  912 */       this.jjrounds[paramInt] = this.jjround;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void jjAddStates(int paramInt1, int paramInt2) {
/*      */     do
/*  918 */       this.jjstateSet[(this.jjnewStateCnt++)] = jjnextStates[paramInt1];
/*  919 */     while (paramInt1++ != paramInt2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAddTwoStates(int paramInt1, int paramInt2) {
/*  923 */     jjCheckNAdd(paramInt1);
/*  924 */     jjCheckNAdd(paramInt2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAddStates(int paramInt1, int paramInt2) {
/*      */     do
/*  929 */       jjCheckNAdd(jjnextStates[paramInt1]);
/*  930 */     while (paramInt1++ != paramInt2);
/*      */   }
/*      */ 
/*      */   private final void jjCheckNAddStates(int paramInt) {
/*  934 */     jjCheckNAdd(jjnextStates[paramInt]);
/*  935 */     jjCheckNAdd(jjnextStates[(paramInt + 1)]);
/*      */   }
/*      */ 
/*      */   private final int jjMoveNfa_0(int paramInt1, int paramInt2)
/*      */   {
/*  963 */     int i = 0;
/*  964 */     this.jjnewStateCnt = 67;
/*  965 */     int j = 1;
/*  966 */     this.jjstateSet[0] = paramInt1;
/*  967 */     int k = 2147483647;
/*      */     while (true)
/*      */     {
/*  970 */       if (++this.jjround == 2147483647)
/*  971 */         ReInitRounds();
/*      */       long l1;
/*  973 */       if (this.curChar < '@')
/*      */       {
/*  975 */         l1 = 1L << this.curChar;
/*      */         do
/*      */         {
/*  979 */           switch (this.jjstateSet[(--j)])
/*      */           {
/*      */           case 0:
/*  982 */             if ((0x0 & l1) != 0L) {
/*  983 */               jjCheckNAddStates(0, 6);
/*  984 */             } else if (this.curChar == '/') {
/*  985 */               jjAddStates(7, 9);
/*  986 */             } else if (this.curChar == '$')
/*      */             {
/*  988 */               if (k > 67) {
/*  989 */                 k = 67;
/*      */               }
/*  991 */               jjCheckNAdd(28);
/*      */             }
/*  993 */             else if (this.curChar == '"') {
/*  994 */               jjCheckNAddStates(10, 12);
/*  995 */             } else if (this.curChar == '\'') {
/*  996 */               jjAddStates(13, 14);
/*  997 */             } else if (this.curChar == '.') {
/*  998 */               jjCheckNAdd(4);
/*      */             }
/* 1000 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1002 */               if (k > 59) {
/* 1003 */                 k = 59;
/*      */               }
/* 1005 */               jjCheckNAddTwoStates(1, 2);
/*      */             }
/* 1007 */             else if (this.curChar == '0')
/*      */             {
/* 1009 */               if (k > 59) {
/* 1010 */                 k = 59;
/*      */               }
/* 1012 */               jjCheckNAddStates(15, 17); } break;
/*      */           case 49:
/* 1016 */             if (this.curChar == '*')
/* 1017 */               jjCheckNAddTwoStates(62, 63);
/* 1018 */             else if (this.curChar == '/') {
/* 1019 */               jjCheckNAddStates(18, 20);
/*      */             }
/* 1021 */             if (this.curChar == '*')
/* 1022 */               this.jjstateSet[(this.jjnewStateCnt++)] = 54; break;
/*      */           case 1:
/* 1026 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1029 */               if (k > 59) {
/* 1030 */                 k = 59;
/*      */               }
/* 1032 */               jjCheckNAddTwoStates(1, 2);
/* 1033 */             }break;
/*      */           case 3:
/* 1035 */             if (this.curChar == '.')
/* 1036 */               jjCheckNAdd(4); break;
/*      */           case 4:
/* 1040 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1043 */               if (k > 63) {
/* 1044 */                 k = 63;
/*      */               }
/* 1046 */               jjCheckNAddStates(21, 23);
/* 1047 */             }break;
/*      */           case 6:
/* 1049 */             if ((0x0 & l1) != 0L)
/* 1050 */               jjCheckNAdd(7); break;
/*      */           case 7:
/* 1054 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1057 */               if (k > 63) {
/* 1058 */                 k = 63;
/*      */               }
/* 1060 */               jjCheckNAddTwoStates(7, 8);
/* 1061 */             }break;
/*      */           case 9:
/* 1063 */             if (this.curChar == '\'')
/* 1064 */               jjAddStates(13, 14); break;
/*      */           case 10:
/* 1068 */             if ((0xFFFFDBFF & l1) != 0L)
/* 1069 */               jjCheckNAdd(11); break;
/*      */           case 11:
/* 1073 */             if ((this.curChar == '\'') && (k > 65))
/* 1074 */               k = 65; break;
/*      */           case 13:
/* 1078 */             if ((0x0 & l1) != 0L)
/* 1079 */               jjCheckNAdd(11); break;
/*      */           case 14:
/* 1083 */             if ((0x0 & l1) != 0L)
/* 1084 */               jjCheckNAddTwoStates(15, 11); break;
/*      */           case 15:
/* 1088 */             if ((0x0 & l1) != 0L)
/* 1089 */               jjCheckNAdd(11); break;
/*      */           case 16:
/* 1093 */             if ((0x0 & l1) != 0L)
/* 1094 */               this.jjstateSet[(this.jjnewStateCnt++)] = 17; break;
/*      */           case 17:
/* 1098 */             if ((0x0 & l1) != 0L)
/* 1099 */               jjCheckNAdd(15); break;
/*      */           case 18:
/* 1103 */             if (this.curChar == '"')
/* 1104 */               jjCheckNAddStates(10, 12); break;
/*      */           case 19:
/* 1108 */             if ((0xFFFFDBFF & l1) != 0L)
/* 1109 */               jjCheckNAddStates(10, 12); break;
/*      */           case 21:
/* 1113 */             if ((0x0 & l1) != 0L)
/* 1114 */               jjCheckNAddStates(10, 12); break;
/*      */           case 22:
/* 1118 */             if ((this.curChar == '"') && (k > 66))
/* 1119 */               k = 66; break;
/*      */           case 23:
/* 1123 */             if ((0x0 & l1) != 0L)
/* 1124 */               jjCheckNAddStates(24, 27); break;
/*      */           case 24:
/* 1128 */             if ((0x0 & l1) != 0L)
/* 1129 */               jjCheckNAddStates(10, 12); break;
/*      */           case 25:
/* 1133 */             if ((0x0 & l1) != 0L)
/* 1134 */               this.jjstateSet[(this.jjnewStateCnt++)] = 26; break;
/*      */           case 26:
/* 1138 */             if ((0x0 & l1) != 0L)
/* 1139 */               jjCheckNAdd(24); break;
/*      */           case 27:
/* 1143 */             if (this.curChar == '$')
/*      */             {
/* 1146 */               if (k > 67) {
/* 1147 */                 k = 67;
/*      */               }
/* 1149 */               jjCheckNAdd(28);
/* 1150 */             }break;
/*      */           case 28:
/* 1152 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1155 */               if (k > 67) {
/* 1156 */                 k = 67;
/*      */               }
/* 1158 */               jjCheckNAdd(28);
/* 1159 */             }break;
/*      */           case 29:
/* 1161 */             if ((0x0 & l1) != 0L)
/* 1162 */               jjCheckNAddStates(0, 6); break;
/*      */           case 30:
/* 1166 */             if ((0x0 & l1) != 0L)
/* 1167 */               jjCheckNAddTwoStates(30, 31); break;
/*      */           case 31:
/* 1171 */             if (this.curChar == '.')
/*      */             {
/* 1174 */               if (k > 63) {
/* 1175 */                 k = 63;
/*      */               }
/* 1177 */               jjCheckNAddStates(28, 30);
/* 1178 */             }break;
/*      */           case 32:
/* 1180 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1183 */               if (k > 63) {
/* 1184 */                 k = 63;
/*      */               }
/* 1186 */               jjCheckNAddStates(28, 30);
/* 1187 */             }break;
/*      */           case 34:
/* 1189 */             if ((0x0 & l1) != 0L)
/* 1190 */               jjCheckNAdd(35); break;
/*      */           case 35:
/* 1194 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1197 */               if (k > 63) {
/* 1198 */                 k = 63;
/*      */               }
/* 1200 */               jjCheckNAddTwoStates(35, 8);
/* 1201 */             }break;
/*      */           case 36:
/* 1203 */             if ((0x0 & l1) != 0L)
/* 1204 */               jjCheckNAddTwoStates(36, 37); break;
/*      */           case 38:
/* 1208 */             if ((0x0 & l1) != 0L)
/* 1209 */               jjCheckNAdd(39); break;
/*      */           case 39:
/* 1213 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1216 */               if (k > 63) {
/* 1217 */                 k = 63;
/*      */               }
/* 1219 */               jjCheckNAddTwoStates(39, 8);
/* 1220 */             }break;
/*      */           case 40:
/* 1222 */             if ((0x0 & l1) != 0L)
/* 1223 */               jjCheckNAddStates(31, 33); break;
/*      */           case 42:
/* 1227 */             if ((0x0 & l1) != 0L)
/* 1228 */               jjCheckNAdd(43); break;
/*      */           case 43:
/* 1232 */             if ((0x0 & l1) != 0L)
/* 1233 */               jjCheckNAddTwoStates(43, 8); break;
/*      */           case 44:
/* 1237 */             if (this.curChar == '0')
/*      */             {
/* 1240 */               if (k > 59) {
/* 1241 */                 k = 59;
/*      */               }
/* 1243 */               jjCheckNAddStates(15, 17);
/* 1244 */             }break;
/*      */           case 46:
/* 1246 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1249 */               if (k > 59) {
/* 1250 */                 k = 59;
/*      */               }
/* 1252 */               jjCheckNAddTwoStates(46, 2);
/* 1253 */             }break;
/*      */           case 47:
/* 1255 */             if ((0x0 & l1) != 0L)
/*      */             {
/* 1258 */               if (k > 59) {
/* 1259 */                 k = 59;
/*      */               }
/* 1261 */               jjCheckNAddTwoStates(47, 2);
/* 1262 */             }break;
/*      */           case 48:
/* 1264 */             if (this.curChar == '/')
/* 1265 */               jjAddStates(7, 9); break;
/*      */           case 50:
/* 1269 */             if ((0xFFFFDBFF & l1) != 0L)
/* 1270 */               jjCheckNAddStates(18, 20); break;
/*      */           case 51:
/* 1274 */             if (((0x2400 & l1) != 0L) && (k > 6))
/* 1275 */               k = 6; break;
/*      */           case 52:
/* 1279 */             if ((this.curChar == '\n') && (k > 6))
/* 1280 */               k = 6; break;
/*      */           case 53:
/* 1284 */             if (this.curChar == '\r')
/* 1285 */               this.jjstateSet[(this.jjnewStateCnt++)] = 52; break;
/*      */           case 54:
/* 1289 */             if (this.curChar == '*')
/* 1290 */               jjCheckNAddTwoStates(55, 56); break;
/*      */           case 55:
/* 1294 */             if ((0xFFFFFFFF & l1) != 0L)
/* 1295 */               jjCheckNAddTwoStates(55, 56); break;
/*      */           case 56:
/* 1299 */             if (this.curChar == '*')
/* 1300 */               jjCheckNAddStates(34, 36); break;
/*      */           case 57:
/* 1304 */             if ((0xFFFFFFFF & l1) != 0L)
/* 1305 */               jjCheckNAddTwoStates(58, 56); break;
/*      */           case 58:
/* 1309 */             if ((0xFFFFFFFF & l1) != 0L)
/* 1310 */               jjCheckNAddTwoStates(58, 56); break;
/*      */           case 59:
/* 1314 */             if ((this.curChar == '/') && (k > 7))
/* 1315 */               k = 7; break;
/*      */           case 60:
/* 1319 */             if (this.curChar == '*')
/* 1320 */               this.jjstateSet[(this.jjnewStateCnt++)] = 54; break;
/*      */           case 61:
/* 1324 */             if (this.curChar == '*')
/* 1325 */               jjCheckNAddTwoStates(62, 63); break;
/*      */           case 62:
/* 1329 */             if ((0xFFFFFFFF & l1) != 0L)
/* 1330 */               jjCheckNAddTwoStates(62, 63); break;
/*      */           case 63:
/* 1334 */             if (this.curChar == '*')
/* 1335 */               jjCheckNAddStates(37, 39); break;
/*      */           case 64:
/* 1339 */             if ((0xFFFFFFFF & l1) != 0L)
/* 1340 */               jjCheckNAddTwoStates(65, 63); break;
/*      */           case 65:
/* 1344 */             if ((0xFFFFFFFF & l1) != 0L)
/* 1345 */               jjCheckNAddTwoStates(65, 63); break;
/*      */           case 66:
/* 1349 */             if ((this.curChar == '/') && (k > 8))
/* 1350 */               k = 8; break;
/*      */           case 2:
/*      */           case 5:
/*      */           case 8:
/*      */           case 12:
/*      */           case 20:
/*      */           case 33:
/*      */           case 37:
/*      */           case 41:
/* 1355 */           case 45: }  } while (j != i);
/*      */       }
/* 1357 */       else if (this.curChar < '')
/*      */       {
/* 1359 */         l1 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1363 */           switch (this.jjstateSet[(--j)])
/*      */           {
/*      */           case 0:
/*      */           case 28:
/* 1367 */             if ((0x87FFFFFE & l1) != 0L)
/*      */             {
/* 1370 */               if (k > 67) {
/* 1371 */                 k = 67;
/*      */               }
/* 1373 */               jjCheckNAdd(28);
/* 1374 */             }break;
/*      */           case 2:
/* 1376 */             if (((0x1000 & l1) != 0L) && (k > 59))
/* 1377 */               k = 59; break;
/*      */           case 5:
/* 1381 */             if ((0x20 & l1) != 0L)
/* 1382 */               jjAddStates(40, 41); break;
/*      */           case 8:
/* 1386 */             if (((0x50 & l1) != 0L) && (k > 63))
/* 1387 */               k = 63; break;
/*      */           case 10:
/* 1391 */             if ((0xEFFFFFFF & l1) != 0L)
/* 1392 */               jjCheckNAdd(11); break;
/*      */           case 12:
/* 1396 */             if (this.curChar == '\\')
/* 1397 */               jjAddStates(42, 44); break;
/*      */           case 13:
/* 1401 */             if ((0x10000000 & l1) != 0L)
/* 1402 */               jjCheckNAdd(11); break;
/*      */           case 19:
/* 1406 */             if ((0xEFFFFFFF & l1) != 0L)
/* 1407 */               jjCheckNAddStates(10, 12); break;
/*      */           case 20:
/* 1411 */             if (this.curChar == '\\')
/* 1412 */               jjAddStates(45, 47); break;
/*      */           case 21:
/* 1416 */             if ((0x10000000 & l1) != 0L)
/* 1417 */               jjCheckNAddStates(10, 12); break;
/*      */           case 33:
/* 1421 */             if ((0x20 & l1) != 0L)
/* 1422 */               jjAddStates(48, 49); break;
/*      */           case 37:
/* 1426 */             if ((0x20 & l1) != 0L)
/* 1427 */               jjAddStates(50, 51); break;
/*      */           case 41:
/* 1431 */             if ((0x20 & l1) != 0L)
/* 1432 */               jjAddStates(52, 53); break;
/*      */           case 45:
/* 1436 */             if ((0x1000000 & l1) != 0L)
/* 1437 */               jjCheckNAdd(46); break;
/*      */           case 46:
/* 1441 */             if ((0x7E & l1) != 0L)
/*      */             {
/* 1444 */               if (k > 59) {
/* 1445 */                 k = 59;
/*      */               }
/* 1447 */               jjCheckNAddTwoStates(46, 2);
/* 1448 */             }break;
/*      */           case 50:
/* 1450 */             jjAddStates(18, 20);
/* 1451 */             break;
/*      */           case 55:
/* 1453 */             jjCheckNAddTwoStates(55, 56);
/* 1454 */             break;
/*      */           case 57:
/*      */           case 58:
/* 1457 */             jjCheckNAddTwoStates(58, 56);
/* 1458 */             break;
/*      */           case 62:
/* 1460 */             jjCheckNAddTwoStates(62, 63);
/* 1461 */             break;
/*      */           case 64:
/*      */           case 65:
/* 1464 */             jjCheckNAddTwoStates(65, 63);
/*      */           case 1:
/*      */           case 3:
/*      */           case 4:
/*      */           case 6:
/*      */           case 7:
/*      */           case 9:
/*      */           case 11:
/*      */           case 14:
/*      */           case 15:
/*      */           case 16:
/*      */           case 17:
/*      */           case 18:
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/*      */           case 26:
/*      */           case 27:
/*      */           case 29:
/*      */           case 30:
/*      */           case 31:
/*      */           case 32:
/*      */           case 34:
/*      */           case 35:
/*      */           case 36:
/*      */           case 38:
/*      */           case 39:
/*      */           case 40:
/*      */           case 42:
/*      */           case 43:
/*      */           case 44:
/*      */           case 47:
/*      */           case 48:
/*      */           case 49:
/*      */           case 51:
/*      */           case 52:
/*      */           case 53:
/*      */           case 54:
/*      */           case 56:
/*      */           case 59:
/*      */           case 60:
/*      */           case 61:
/* 1468 */           case 63: }  } while (j != i);
/*      */       }
/*      */       else
/*      */       {
/* 1472 */         int m = this.curChar >> '\b';
/* 1473 */         int n = m >> 6;
/* 1474 */         long l2 = 1L << (m & 0x3F);
/* 1475 */         int i1 = (this.curChar & 0xFF) >> '\006';
/* 1476 */         long l3 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1480 */           switch (this.jjstateSet[(--j)])
/*      */           {
/*      */           case 0:
/*      */           case 28:
/* 1484 */             if (jjCanMove_1(m, n, i1, l2, l3))
/*      */             {
/* 1487 */               if (k > 67) {
/* 1488 */                 k = 67;
/*      */               }
/* 1490 */               jjCheckNAdd(28);
/* 1491 */             }break;
/*      */           case 10:
/* 1493 */             if (jjCanMove_0(m, n, i1, l2, l3))
/* 1494 */               this.jjstateSet[(this.jjnewStateCnt++)] = 11; break;
/*      */           case 19:
/* 1498 */             if (jjCanMove_0(m, n, i1, l2, l3))
/* 1499 */               jjAddStates(10, 12); break;
/*      */           case 50:
/* 1503 */             if (jjCanMove_0(m, n, i1, l2, l3))
/* 1504 */               jjAddStates(18, 20); break;
/*      */           case 55:
/* 1508 */             if (jjCanMove_0(m, n, i1, l2, l3))
/* 1509 */               jjCheckNAddTwoStates(55, 56); break;
/*      */           case 57:
/*      */           case 58:
/* 1514 */             if (jjCanMove_0(m, n, i1, l2, l3))
/* 1515 */               jjCheckNAddTwoStates(58, 56); break;
/*      */           case 62:
/* 1519 */             if (jjCanMove_0(m, n, i1, l2, l3))
/* 1520 */               jjCheckNAddTwoStates(62, 63); break;
/*      */           case 64:
/*      */           case 65:
/* 1525 */             if (jjCanMove_0(m, n, i1, l2, l3)) {
/* 1526 */               jjCheckNAddTwoStates(65, 63);
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/* 1531 */         while (j != i);
/*      */       }
/* 1533 */       if (k != 2147483647)
/*      */       {
/* 1535 */         this.jjmatchedKind = k;
/* 1536 */         this.jjmatchedPos = paramInt2;
/* 1537 */         k = 2147483647;
/*      */       }
/* 1539 */       paramInt2++;
/* 1540 */       if ((j = this.jjnewStateCnt) == (i = 67 - (this.jjnewStateCnt = i)))
/* 1541 */         return paramInt2;
/*      */       try {
/* 1543 */         this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {  }
/* 1544 */     }return paramInt2;
/*      */   }
/*      */ 
/*      */   private static final boolean jjCanMove_0(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2)
/*      */   {
/* 1555 */     switch (paramInt1)
/*      */     {
/*      */     case 0:
/* 1558 */       return (jjbitVec2[paramInt3] & paramLong2) != 0L;
/*      */     }
/* 1560 */     if ((jjbitVec0[paramInt2] & paramLong1) != 0L) {
/* 1561 */       return true;
/*      */     }
/* 1563 */     return false;
/*      */   }
/*      */ 
/*      */   private static final boolean jjCanMove_1(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2)
/*      */   {
/* 1568 */     switch (paramInt1)
/*      */     {
/*      */     case 0:
/* 1571 */       return (jjbitVec4[paramInt3] & paramLong2) != 0L;
/*      */     case 48:
/* 1573 */       return (jjbitVec5[paramInt3] & paramLong2) != 0L;
/*      */     case 49:
/* 1575 */       return (jjbitVec6[paramInt3] & paramLong2) != 0L;
/*      */     case 51:
/* 1577 */       return (jjbitVec7[paramInt3] & paramLong2) != 0L;
/*      */     case 61:
/* 1579 */       return (jjbitVec8[paramInt3] & paramLong2) != 0L;
/*      */     }
/* 1581 */     if ((jjbitVec3[paramInt2] & paramLong1) != 0L) {
/* 1582 */       return true;
/*      */     }
/* 1584 */     return false;
/*      */   }
/*      */ 
/*      */   public ExpressionParserTokenManager(ASCII_UCodeESC_CharStream paramASCII_UCodeESC_CharStream)
/*      */   {
/* 1630 */     this.input_stream = paramASCII_UCodeESC_CharStream;
/*      */   }
/*      */ 
/*      */   public ExpressionParserTokenManager(ASCII_UCodeESC_CharStream paramASCII_UCodeESC_CharStream, int paramInt) {
/* 1634 */     this(paramASCII_UCodeESC_CharStream);
/* 1635 */     SwitchTo(paramInt);
/*      */   }
/*      */ 
/*      */   public void ReInit(ASCII_UCodeESC_CharStream paramASCII_UCodeESC_CharStream) {
/* 1639 */     this.jjmatchedPos = (this.jjnewStateCnt = 0);
/* 1640 */     this.curLexState = this.defaultLexState;
/* 1641 */     this.input_stream = paramASCII_UCodeESC_CharStream;
/* 1642 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   private final void ReInitRounds()
/*      */   {
/* 1647 */     this.jjround = -2147483647;
/* 1648 */     for (int i = 67; i-- > 0; )
/* 1649 */       this.jjrounds[i] = -2147483648;
/*      */   }
/*      */ 
/*      */   public void ReInit(ASCII_UCodeESC_CharStream paramASCII_UCodeESC_CharStream, int paramInt)
/*      */   {
/* 1654 */     ReInit(paramASCII_UCodeESC_CharStream);
/* 1655 */     SwitchTo(paramInt);
/*      */   }
/*      */ 
/*      */   public void SwitchTo(int paramInt) {
/* 1659 */     if ((paramInt >= 1) || (paramInt < 0)) {
/* 1660 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + paramInt + ". State unchanged.", 2);
/*      */     }
/* 1662 */     this.curLexState = paramInt;
/*      */   }
/*      */ 
/*      */   private final Token jjFillToken()
/*      */   {
/* 1668 */     Token localToken = Token.newToken(this.jjmatchedKind);
/* 1669 */     localToken.kind = this.jjmatchedKind;
/* 1670 */     String str = jjstrLiteralImages[this.jjmatchedKind];
/* 1671 */     localToken.image = (str == null ? this.input_stream.GetImage() : str);
/* 1672 */     localToken.beginLine = this.input_stream.getBeginLine();
/* 1673 */     localToken.beginColumn = this.input_stream.getBeginColumn();
/* 1674 */     localToken.endLine = this.input_stream.getEndLine();
/* 1675 */     localToken.endColumn = this.input_stream.getEndColumn();
/* 1676 */     return localToken;
/*      */   }
/*      */ 
/*      */   public final Token getNextToken()
/*      */   {
/* 1688 */     Object localObject = null;
/*      */ 
/* 1690 */     int i = 0;
/*      */     while (true)
/*      */     {
/*      */       Token localToken;
/*      */       try
/*      */       {
/* 1697 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/*      */       catch (IOException localIOException1)
/*      */       {
/* 1701 */         this.jjmatchedKind = 0;
/* 1702 */         localToken = jjFillToken();
/* 1703 */         localToken.specialToken = localObject;
/* 1704 */         return localToken;
/*      */       }
/*      */       try
/*      */       {
/* 1708 */         while ((this.curChar <= ' ') && ((0x3600 & 1L << this.curChar) != 0L))
/* 1709 */           this.curChar = this.input_stream.BeginToken();
/*      */       } catch (IOException localIOException2) {
/*      */       }
/* 1712 */       continue;
/* 1713 */       this.jjmatchedKind = 2147483647;
/* 1714 */       this.jjmatchedPos = 0;
/* 1715 */       i = jjMoveStringLiteralDfa0_0();
/* 1716 */       if (this.jjmatchedKind == 2147483647)
/*      */         break;
/* 1718 */       if (this.jjmatchedPos + 1 < i) {
/* 1719 */         this.input_stream.backup(i - this.jjmatchedPos - 1);
/*      */       }
/* 1721 */       if ((jjtoToken[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) != 0L)
/*      */       {
/* 1723 */         localToken = jjFillToken();
/* 1724 */         localToken.specialToken = localObject;
/* 1725 */         return localToken;
/*      */       }
/*      */ 
/* 1729 */       if ((jjtoSpecial[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) != 0L)
/*      */       {
/* 1731 */         localToken = jjFillToken();
/* 1732 */         if (localObject == null) {
/* 1733 */           localObject = localToken;
/*      */         }
/*      */         else {
/* 1736 */           localToken.specialToken = localObject;
/* 1737 */           localObject = localObject.next = localToken;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1743 */     int j = this.input_stream.getEndLine();
/* 1744 */     int k = this.input_stream.getEndColumn();
/* 1745 */     String str = null;
/* 1746 */     boolean bool = false;
/*      */     try { this.input_stream.readChar(); this.input_stream.backup(1);
/*      */     } catch (IOException localIOException3) {
/* 1749 */       bool = true;
/* 1750 */       str = i <= 1 ? "" : this.input_stream.GetImage();
/* 1751 */       if ((this.curChar == '\n') || (this.curChar == '\r')) {
/* 1752 */         j++;
/* 1753 */         k = 0;
/*      */       } else {
/* 1755 */         k++;
/*      */       }
/*      */     }
/* 1758 */     if (!bool) {
/* 1759 */       this.input_stream.backup(1);
/* 1760 */       str = i <= 1 ? "" : this.input_stream.GetImage();
/*      */     }
/* 1762 */     throw new TokenMgrError(bool, this.curLexState, j, k, str, this.curChar, 0);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.expr.ExpressionParserTokenManager
 * JD-Core Version:    0.6.2
 */