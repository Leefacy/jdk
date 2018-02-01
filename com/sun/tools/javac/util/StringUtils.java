/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class StringUtils
/*    */ {
/*    */   public static String toLowerCase(String paramString)
/*    */   {
/* 45 */     return paramString.toLowerCase(Locale.US);
/*    */   }
/*    */ 
/*    */   public static String toUpperCase(String paramString)
/*    */   {
/* 52 */     return paramString.toUpperCase(Locale.US);
/*    */   }
/*    */ 
/*    */   public static int indexOfIgnoreCase(String paramString1, String paramString2)
/*    */   {
/* 59 */     return indexOfIgnoreCase(paramString1, paramString2, 0);
/*    */   }
/*    */ 
/*    */   public static int indexOfIgnoreCase(String paramString1, String paramString2, int paramInt)
/*    */   {
/* 66 */     Matcher localMatcher = Pattern.compile(Pattern.quote(paramString2), 2).matcher(paramString1);
/* 67 */     return localMatcher.find(paramInt) ? localMatcher.start() : -1;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.StringUtils
 * JD-Core Version:    0.6.2
 */