/*     */ package com.sun.tools.hat.internal.util;
/*     */ 
/*     */ public class Misc
/*     */ {
/*  45 */   private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */   public static final String toHex(int paramInt)
/*     */   {
/*  49 */     char[] arrayOfChar = new char[8];
/*  50 */     int i = 0;
/*  51 */     for (int j = 28; j >= 0; j -= 4) {
/*  52 */       arrayOfChar[(i++)] = digits[(paramInt >> j & 0xF)];
/*     */     }
/*  54 */     return "0x" + new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public static final String toHex(long paramLong) {
/*  58 */     return "0x" + Long.toHexString(paramLong);
/*     */   }
/*     */ 
/*     */   public static final long parseHex(String paramString) {
/*  62 */     long l = 0L;
/*  63 */     if ((paramString.length() < 2) || (paramString.charAt(0) != '0') || 
/*  64 */       (paramString
/*  64 */       .charAt(1) != 
/*  64 */       'x')) {
/*  65 */       return -1L;
/*     */     }
/*  67 */     for (int i = 2; i < paramString.length(); i++) {
/*  68 */       l *= 16L;
/*  69 */       char c = paramString.charAt(i);
/*  70 */       if ((c >= '0') && (c <= '9'))
/*  71 */         l += c - '0';
/*  72 */       else if ((c >= 'a') && (c <= 'f'))
/*  73 */         l += c - 'a' + 10;
/*  74 */       else if ((c >= 'A') && (c <= 'F'))
/*  75 */         l += c - 'A' + 10;
/*     */       else {
/*  77 */         throw new NumberFormatException("" + c + " is not a valid hex digit");
/*     */       }
/*     */     }
/*     */ 
/*  81 */     return l;
/*     */   }
/*     */ 
/*     */   public static String encodeHtml(String paramString) {
/*  85 */     int i = paramString.length();
/*  86 */     StringBuffer localStringBuffer = new StringBuffer();
/*  87 */     for (int j = 0; j < i; j++) {
/*  88 */       int k = paramString.charAt(j);
/*  89 */       if (k == 60) {
/*  90 */         localStringBuffer.append("&lt;");
/*  91 */       } else if (k == 62) {
/*  92 */         localStringBuffer.append("&gt;");
/*  93 */       } else if (k == 34) {
/*  94 */         localStringBuffer.append("&quot;");
/*  95 */       } else if (k == 39) {
/*  96 */         localStringBuffer.append("&#039;");
/*  97 */       } else if (k == 38) {
/*  98 */         localStringBuffer.append("&amp;");
/*  99 */       } else if (k < 32) {
/* 100 */         localStringBuffer.append("&#" + Integer.toString(k) + ";");
/*     */       } else {
/* 102 */         int m = k & 0xFFFF;
/* 103 */         if (m > 127)
/* 104 */           localStringBuffer.append("&#" + Integer.toString(m) + ";");
/*     */         else {
/* 106 */           localStringBuffer.append(k);
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.util.Misc
 * JD-Core Version:    0.6.2
 */