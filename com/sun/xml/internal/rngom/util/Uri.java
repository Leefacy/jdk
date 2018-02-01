/*     */ package com.sun.xml.internal.rngom.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class Uri
/*     */ {
/*  53 */   private static String utf8 = "UTF-8";
/*     */   private static final String HEX_DIGITS = "0123456789abcdef";
/*     */   private static final String excluded = "<>\"{}|\\^`";
/*     */ 
/*     */   public static boolean isValid(String s)
/*     */   {
/*  56 */     return (isValidPercent(s)) && (isValidFragment(s)) && (isValidScheme(s));
/*     */   }
/*     */ 
/*     */   public static String escapeDisallowedChars(String s)
/*     */   {
/*  62 */     StringBuffer buf = null;
/*  63 */     int len = s.length();
/*  64 */     int done = 0;
/*     */     while (true) {
/*  66 */       int i = done;
/*     */       while (true) {
/*  68 */         if (i == len) {
/*  69 */           if (done != 0) break;
/*  70 */           return s;
/*     */         }
/*     */ 
/*  73 */         if (isExcluded(s.charAt(i)))
/*     */           break;
/*  75 */         i++;
/*     */       }
/*  77 */       if (buf == null)
/*  78 */         buf = new StringBuffer();
/*  79 */       if (i > done) {
/*  80 */         buf.append(s.substring(done, i));
/*  81 */         done = i;
/*     */       }
/*  83 */       if (i == len)
/*     */         break;
/*  85 */       for (i++; (i < len) && (isExcluded(s.charAt(i))); i++);
/*  87 */       String tem = s.substring(done, i);
/*     */       try
/*     */       {
/*  90 */         bytes = tem.getBytes(utf8);
/*     */       }
/*     */       catch (UnsupportedEncodingException e)
/*     */       {
/*     */         byte[] bytes;
/*  93 */         utf8 = "UTF8";
/*     */         try {
/*  95 */           bytes = tem.getBytes(utf8);
/*     */         }
/*     */         catch (UnsupportedEncodingException e2)
/*     */         {
/*     */           byte[] bytes;
/*  99 */           return s;
/*     */         }
/*     */       }
/*     */       byte[] bytes;
/* 102 */       for (int j = 0; j < bytes.length; j++) {
/* 103 */         buf.append('%');
/* 104 */         buf.append("0123456789abcdef".charAt((bytes[j] & 0xFF) >> 4));
/* 105 */         buf.append("0123456789abcdef".charAt(bytes[j] & 0xF));
/*     */       }
/* 107 */       done = i;
/*     */     }
/* 109 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isExcluded(char c)
/*     */   {
/* 115 */     return (c <= ' ') || (c >= '') || ("<>\"{}|\\^`".indexOf(c) >= 0);
/*     */   }
/*     */ 
/*     */   private static boolean isAlpha(char c) {
/* 119 */     return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'));
/*     */   }
/*     */ 
/*     */   private static boolean isHexDigit(char c) {
/* 123 */     return (('a' <= c) && (c <= 'f')) || (('A' <= c) && (c <= 'F')) || (isDigit(c));
/*     */   }
/*     */ 
/*     */   private static boolean isDigit(char c) {
/* 127 */     return ('0' <= c) && (c <= '9');
/*     */   }
/*     */ 
/*     */   private static boolean isSchemeChar(char c) {
/* 131 */     return (isAlpha(c)) || (isDigit(c)) || (c == '+') || (c == '-') || (c == '.');
/*     */   }
/*     */ 
/*     */   private static boolean isValidPercent(String s) {
/* 135 */     int len = s.length();
/* 136 */     for (int i = 0; i < len; i++)
/* 137 */       if (s.charAt(i) == '%') {
/* 138 */         if (i + 2 >= len)
/* 139 */           return false;
/* 140 */         if ((!isHexDigit(s.charAt(i + 1))) || 
/* 141 */           (!isHexDigit(s
/* 141 */           .charAt(i + 2))))
/*     */         {
/* 142 */           return false;
/*     */         }
/*     */       }
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean isValidFragment(String s) {
/* 148 */     int i = s.indexOf('#');
/* 149 */     return (i < 0) || (s.indexOf('#', i + 1) < 0);
/*     */   }
/*     */ 
/*     */   private static boolean isValidScheme(String s) {
/* 153 */     if (!isAbsolute(s))
/* 154 */       return true;
/* 155 */     int i = s.indexOf(':');
/* 156 */     if ((i == 0) || 
/* 157 */       (i + 1 == s
/* 157 */       .length()) || 
/* 158 */       (!isAlpha(s
/* 158 */       .charAt(0))))
/*     */     {
/* 159 */       return false;
/*     */     }do { i--; if (i <= 0) break; }
/* 161 */     while (isSchemeChar(s.charAt(i)));
/* 162 */     return false;
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   public static String resolve(String baseUri, String uriReference) {
/* 167 */     if ((!isAbsolute(uriReference)) && (baseUri != null) && (isAbsolute(baseUri)))
/*     */       try {
/* 169 */         return new URL(new URL(baseUri), uriReference).toString();
/*     */       }
/*     */       catch (MalformedURLException localMalformedURLException) {
/*     */       }
/* 173 */     return uriReference;
/*     */   }
/*     */ 
/*     */   public static boolean hasFragmentId(String uri) {
/* 177 */     return uri.indexOf('#') >= 0;
/*     */   }
/*     */ 
/*     */   public static boolean isAbsolute(String uri) {
/* 181 */     int i = uri.indexOf(':');
/* 182 */     if (i < 0)
/* 183 */       return false; while (true) {
/* 184 */       i--; if (i < 0) break;
/* 185 */       switch (uri.charAt(i)) {
/*     */       case '#':
/*     */       case '/':
/*     */       case '?':
/* 189 */         return false;
/*     */       }
/*     */     }
/* 192 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.util.Uri
 * JD-Core Version:    0.6.2
 */