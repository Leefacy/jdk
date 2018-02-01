/*    */ package com.sun.xml.internal.rngom.xml.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ 
/*    */ public abstract class EncodingMap
/*    */ {
/* 51 */   private static final String[] aliases = { "UTF-8", "UTF8", "UTF-16", "Unicode", "UTF-16BE", "UnicodeBigUnmarked", "UTF-16LE", "UnicodeLittleUnmarked", "US-ASCII", "ASCII", "TIS-620", "TIS620" };
/*    */ 
/*    */   public static String getJavaName(String enc)
/*    */   {
/*    */     int i;
/*    */     try
/*    */     {
/* 62 */       "x".getBytes(enc);
/*    */     }
/*    */     catch (UnsupportedEncodingException e) {
/* 65 */       i = 0; } for (; i < aliases.length; i += 2) {
/* 66 */       if (enc.equalsIgnoreCase(aliases[i]))
/*    */         try {
/* 68 */           "x".getBytes(aliases[(i + 1)]);
/* 69 */           return aliases[(i + 1)];
/*    */         }
/*    */         catch (UnsupportedEncodingException localUnsupportedEncodingException1)
/*    */         {
/*    */         }
/*    */     }
/* 75 */     return enc;
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) {
/* 79 */     System.err.println(getJavaName(args[0]));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.xml.util.EncodingMap
 * JD-Core Version:    0.6.2
 */