/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ public class Convert
/*     */ {
/*     */   public static int string2int(String paramString, int paramInt)
/*     */     throws NumberFormatException
/*     */   {
/*  60 */     if (paramInt == 10) {
/*  61 */       return Integer.parseInt(paramString, paramInt);
/*     */     }
/*  63 */     char[] arrayOfChar = paramString.toCharArray();
/*  64 */     int i = 2147483647 / (paramInt / 2);
/*  65 */     int j = 0;
/*  66 */     for (int k = 0; k < arrayOfChar.length; k++) {
/*  67 */       int m = Character.digit(arrayOfChar[k], paramInt);
/*  68 */       if ((j < 0) || (j > i) || (j * paramInt > 2147483647 - m))
/*     */       {
/*  71 */         throw new NumberFormatException();
/*  72 */       }j = j * paramInt + m;
/*     */     }
/*  74 */     return j;
/*     */   }
/*     */ 
/*     */   public static long string2long(String paramString, int paramInt)
/*     */     throws NumberFormatException
/*     */   {
/*  82 */     if (paramInt == 10) {
/*  83 */       return Long.parseLong(paramString, paramInt);
/*     */     }
/*  85 */     char[] arrayOfChar = paramString.toCharArray();
/*  86 */     long l1 = 9223372036854775807L / (paramInt / 2);
/*  87 */     long l2 = 0L;
/*  88 */     for (int i = 0; i < arrayOfChar.length; i++) {
/*  89 */       int j = Character.digit(arrayOfChar[i], paramInt);
/*  90 */       if ((l2 < 0L) || (l2 > l1) || (l2 * paramInt > 9223372036854775807L - j))
/*     */       {
/*  93 */         throw new NumberFormatException();
/*  94 */       }l2 = l2 * paramInt + j;
/*     */     }
/*  96 */     return l2;
/*     */   }
/*     */ 
/*     */   public static int utf2chars(byte[] paramArrayOfByte, int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
/*     */   {
/* 116 */     int i = paramInt1;
/* 117 */     int j = paramInt2;
/* 118 */     int k = paramInt1 + paramInt3;
/* 119 */     while (i < k) {
/* 120 */       int m = paramArrayOfByte[(i++)] & 0xFF;
/* 121 */       if (m >= 224) {
/* 122 */         m = (m & 0xF) << 12;
/* 123 */         m |= (paramArrayOfByte[(i++)] & 0x3F) << 6;
/* 124 */         m |= paramArrayOfByte[(i++)] & 0x3F;
/* 125 */       } else if (m >= 192) {
/* 126 */         m = (m & 0x1F) << 6;
/* 127 */         m |= paramArrayOfByte[(i++)] & 0x3F;
/*     */       }
/* 129 */       paramArrayOfChar[(j++)] = ((char)m);
/*     */     }
/* 131 */     return j;
/*     */   }
/*     */ 
/*     */   public static char[] utf2chars(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 140 */     char[] arrayOfChar1 = new char[paramInt2];
/* 141 */     int i = utf2chars(paramArrayOfByte, paramInt1, arrayOfChar1, 0, paramInt2);
/* 142 */     char[] arrayOfChar2 = new char[i];
/* 143 */     System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, i);
/* 144 */     return arrayOfChar2;
/*     */   }
/*     */ 
/*     */   public static char[] utf2chars(byte[] paramArrayOfByte)
/*     */   {
/* 152 */     return utf2chars(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static String utf2string(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 161 */     char[] arrayOfChar = new char[paramInt2];
/* 162 */     int i = utf2chars(paramArrayOfByte, paramInt1, arrayOfChar, 0, paramInt2);
/* 163 */     return new String(arrayOfChar, 0, i);
/*     */   }
/*     */ 
/*     */   public static String utf2string(byte[] paramArrayOfByte)
/*     */   {
/* 171 */     return utf2string(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static int chars2utf(char[] paramArrayOfChar, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 188 */     int i = paramInt2;
/* 189 */     int j = paramInt1 + paramInt3;
/* 190 */     for (int k = paramInt1; k < j; k++) {
/* 191 */       int m = paramArrayOfChar[k];
/* 192 */       if ((1 <= m) && (m <= 127)) {
/* 193 */         paramArrayOfByte[(i++)] = ((byte)m);
/* 194 */       } else if (m <= 2047) {
/* 195 */         paramArrayOfByte[(i++)] = ((byte)(0xC0 | m >> 6));
/* 196 */         paramArrayOfByte[(i++)] = ((byte)(0x80 | m & 0x3F));
/*     */       } else {
/* 198 */         paramArrayOfByte[(i++)] = ((byte)(0xE0 | m >> 12));
/* 199 */         paramArrayOfByte[(i++)] = ((byte)(0x80 | m >> 6 & 0x3F));
/* 200 */         paramArrayOfByte[(i++)] = ((byte)(0x80 | m & 0x3F));
/*     */       }
/*     */     }
/* 203 */     return i;
/*     */   }
/*     */ 
/*     */   public static byte[] chars2utf(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 212 */     byte[] arrayOfByte1 = new byte[paramInt2 * 3];
/* 213 */     int i = chars2utf(paramArrayOfChar, paramInt1, arrayOfByte1, 0, paramInt2);
/* 214 */     byte[] arrayOfByte2 = new byte[i];
/* 215 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
/* 216 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public static byte[] chars2utf(char[] paramArrayOfChar)
/*     */   {
/* 224 */     return chars2utf(paramArrayOfChar, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public static byte[] string2utf(String paramString)
/*     */   {
/* 230 */     return chars2utf(paramString.toCharArray());
/*     */   }
/*     */ 
/*     */   public static String quote(String paramString)
/*     */   {
/* 238 */     StringBuilder localStringBuilder = new StringBuilder();
/* 239 */     for (int i = 0; i < paramString.length(); i++) {
/* 240 */       localStringBuilder.append(quote(paramString.charAt(i)));
/*     */     }
/* 242 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static String quote(char paramChar)
/*     */   {
/* 250 */     switch (paramChar) { case '\b':
/* 251 */       return "\\b";
/*     */     case '\f':
/* 252 */       return "\\f";
/*     */     case '\n':
/* 253 */       return "\\n";
/*     */     case '\r':
/* 254 */       return "\\r";
/*     */     case '\t':
/* 255 */       return "\\t";
/*     */     case '\'':
/* 256 */       return "\\'";
/*     */     case '"':
/* 257 */       return "\\\"";
/*     */     case '\\':
/* 258 */       return "\\\\";
/*     */     }
/*     */ 
/* 262 */     return isPrintableAscii(paramChar) ? 
/* 261 */       String.valueOf(paramChar) : 
/* 262 */       String.format("\\u%04x", new Object[] { 
/* 262 */       Integer.valueOf(paramChar) });
/*     */   }
/*     */ 
/*     */   private static boolean isPrintableAscii(char paramChar)
/*     */   {
/* 270 */     return (paramChar >= ' ') && (paramChar <= '~');
/*     */   }
/*     */ 
/*     */   public static String escapeUnicode(String paramString)
/*     */   {
/* 276 */     int i = paramString.length();
/* 277 */     int j = 0;
/* 278 */     while (j < i) {
/* 279 */       char c = paramString.charAt(j);
/* 280 */       if (c > 'ÿ') {
/* 281 */         StringBuilder localStringBuilder = new StringBuilder();
/* 282 */         localStringBuilder.append(paramString.substring(0, j));
/* 283 */         while (j < i) {
/* 284 */           c = paramString.charAt(j);
/* 285 */           if (c > 'ÿ') {
/* 286 */             localStringBuilder.append("\\u");
/* 287 */             localStringBuilder.append(Character.forDigit((c >> '\f') % 16, 16));
/* 288 */             localStringBuilder.append(Character.forDigit((c >> '\b') % 16, 16));
/* 289 */             localStringBuilder.append(Character.forDigit((c >> '\004') % 16, 16));
/* 290 */             localStringBuilder.append(Character.forDigit(c % '\020', 16));
/*     */           } else {
/* 292 */             localStringBuilder.append(c);
/*     */           }
/* 294 */           j++;
/*     */         }
/* 296 */         paramString = localStringBuilder.toString();
/*     */       } else {
/* 298 */         j++;
/*     */       }
/*     */     }
/* 301 */     return paramString;
/*     */   }
/*     */ 
/*     */   public static Name shortName(Name paramName)
/*     */   {
/* 309 */     return paramName.subName(paramName
/* 310 */       .lastIndexOf((byte)46) + 
/* 310 */       1, paramName.getByteLength());
/*     */   }
/*     */ 
/*     */   public static String shortName(String paramString) {
/* 314 */     return paramString.substring(paramString.lastIndexOf('.') + 1);
/*     */   }
/*     */ 
/*     */   public static Name packagePart(Name paramName)
/*     */   {
/* 321 */     return paramName.subName(0, paramName.lastIndexOf((byte)46));
/*     */   }
/*     */ 
/*     */   public static String packagePart(String paramString) {
/* 325 */     int i = paramString.lastIndexOf('.');
/* 326 */     return i < 0 ? "" : paramString.substring(0, i);
/*     */   }
/*     */ 
/*     */   public static List<Name> enclosingCandidates(Name paramName) {
/* 330 */     List localList = List.nil();
/*     */     int i;
/* 332 */     while ((i = paramName.lastIndexOf((byte)36)) > 0) {
/* 333 */       paramName = paramName.subName(0, i);
/* 334 */       localList = localList.prepend(paramName);
/*     */     }
/* 336 */     return localList;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Convert
 * JD-Core Version:    0.6.2
 */