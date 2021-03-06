/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ public class XmlChars
/*     */ {
/*     */   public static boolean isChar(int ucs4char)
/*     */   {
/*  65 */     return ((ucs4char >= 32) && (ucs4char <= 55295)) || (ucs4char == 10) || (ucs4char == 9) || (ucs4char == 13) || ((ucs4char >= 57344) && (ucs4char <= 65533)) || ((ucs4char >= 65536) && (ucs4char <= 1114111));
/*     */   }
/*     */ 
/*     */   public static boolean isNameChar(char c)
/*     */   {
/*  83 */     if (isLetter2(c))
/*  84 */       return true;
/*  85 */     if (c == '>')
/*  86 */       return false;
/*  87 */     if ((c == '.') || (c == '-') || (c == '_') || (c == ':') || 
/*  88 */       (isExtender(c)))
/*     */     {
/*  89 */       return true;
/*     */     }
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isNCNameChar(char c)
/*     */   {
/* 107 */     return (c != ':') && (isNameChar(c));
/*     */   }
/*     */ 
/*     */   public static boolean isSpace(char c)
/*     */   {
/* 115 */     return (c == ' ') || (c == '\t') || (c == '\n') || (c == '\r');
/*     */   }
/*     */ 
/*     */   public static boolean isLetter(char c)
/*     */   {
/* 170 */     if ((c >= 'a') && (c <= 'z'))
/* 171 */       return true;
/* 172 */     if (c == '/')
/* 173 */       return false;
/* 174 */     if ((c >= 'A') && (c <= 'Z')) {
/* 175 */       return true;
/*     */     }
/*     */ 
/* 181 */     switch (Character.getType(c))
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 5:
/*     */     case 10:
/* 191 */       return (!isCompatibilityChar(c)) && ((c < '⃝') || (c > '⃠'));
/*     */     case 4:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/* 197 */     }return ((c >= 'ʻ') && (c <= 'ˁ')) || (c == 'ՙ') || (c == 'ۥ') || (c == 'ۦ');
/*     */   }
/*     */ 
/*     */   private static boolean isCompatibilityChar(char c)
/*     */   {
/* 213 */     switch (c >> '\b' & 0xFF)
/*     */     {
/*     */     case 0:
/* 216 */       return (c == 'ª') || (c == 'µ') || (c == 'º');
/*     */     case 1:
/* 220 */       return ((c >= 'Ĳ') && (c <= 'ĳ')) || ((c >= 'Ŀ') && (c <= 'ŀ')) || (c == 'ŉ') || (c == 'ſ') || ((c >= 'Ǆ') && (c <= 'ǌ')) || ((c >= 'Ǳ') && (c <= 'ǳ'));
/*     */     case 2:
/* 229 */       return ((c >= 'ʰ') && (c <= 'ʸ')) || ((c >= 'ˠ') && (c <= 'ˤ'));
/*     */     case 3:
/* 233 */       return c == 'ͺ';
/*     */     case 5:
/* 236 */       return c == 'և';
/*     */     case 14:
/* 239 */       return (c >= 'ໜ') && (c <= 'ໝ');
/*     */     case 17:
/* 243 */       return (c == 'ᄁ') || (c == 'ᄄ') || (c == 'ᄈ') || (c == 'ᄊ') || (c == 'ᄍ') || ((c >= 'ᄓ') && (c <= 'ᄻ')) || (c == 'ᄽ') || (c == 'ᄿ') || ((c >= 'ᅁ') && (c <= 'ᅋ')) || (c == 'ᅍ') || (c == 'ᅏ') || ((c >= 'ᅑ') && (c <= 'ᅓ')) || ((c >= 'ᅖ') && (c <= 'ᅘ')) || (c == 'ᅢ') || (c == 'ᅤ') || (c == 'ᅦ') || (c == 'ᅨ') || ((c >= 'ᅪ') && (c <= 'ᅬ')) || ((c >= 'ᅯ') && (c <= 'ᅱ')) || (c == 'ᅴ') || ((c >= 'ᅶ') && (c <= 'ᆝ')) || ((c >= 'ᆟ') && (c <= 'ᆢ')) || ((c >= 'ᆩ') && (c <= 'ᆪ')) || ((c >= 'ᆬ') && (c <= 'ᆭ')) || ((c >= 'ᆰ') && (c <= 'ᆶ')) || (c == 'ᆹ') || (c == 'ᆻ') || ((c >= 'ᇃ') && (c <= 'ᇪ')) || ((c >= 'ᇬ') && (c <= 'ᇯ')) || ((c >= 'ᇱ') && (c <= 'ᇸ'));
/*     */     case 32:
/* 276 */       return c == 'ⁿ';
/*     */     case 33:
/* 279 */       return (c == 'ℂ') || (c == 'ℇ') || ((c >= 'ℊ') && (c <= 'ℓ')) || (c == 'ℕ') || ((c >= '℘') && (c <= 'ℝ')) || (c == 'ℤ') || (c == 'ℨ') || ((c >= 'ℬ') && (c <= 'ℭ')) || ((c >= 'ℯ') && (c <= 'ℸ')) || ((c >= 'Ⅰ') && (c <= 'ⅿ'));
/*     */     case 48:
/* 297 */       return (c >= '゛') && (c <= '゜');
/*     */     case 49:
/* 301 */       return (c >= 'ㄱ') && (c <= 'ㆎ');
/*     */     case 249:
/*     */     case 250:
/*     */     case 251:
/*     */     case 252:
/*     */     case 253:
/*     */     case 254:
/*     */     case 255:
/* 311 */       return true;
/*     */     }
/*     */ 
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isLetter2(char c)
/*     */   {
/* 329 */     if ((c >= 'a') && (c <= 'z'))
/* 330 */       return true;
/* 331 */     if (c == '>')
/* 332 */       return false;
/* 333 */     if ((c >= 'A') && (c <= 'Z')) {
/* 334 */       return true;
/*     */     }
/*     */ 
/* 340 */     switch (Character.getType(c))
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 357 */       return (!isCompatibilityChar(c)) && ((c < '⃝') || (c > '⃠'));
/*     */     }
/*     */ 
/* 363 */     return c == '·';
/*     */   }
/*     */ 
/*     */   private static boolean isDigit(char c)
/*     */   {
/* 374 */     return (Character.isDigit(c)) && ((c < 65296) || (c > 65305));
/*     */   }
/*     */ 
/*     */   private static boolean isExtender(char c)
/*     */   {
/* 380 */     return (c == '·') || (c == 'ː') || (c == 'ˑ') || (c == '·') || (c == 'ـ') || (c == 'ๆ') || (c == 'ໆ') || (c == '々') || ((c >= '〱') && (c <= '〵')) || ((c >= 'ゝ') && (c <= 'ゞ')) || ((c >= 'ー') && (c <= 'ヾ'));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.dtdparser.XmlChars
 * JD-Core Version:    0.6.2
 */