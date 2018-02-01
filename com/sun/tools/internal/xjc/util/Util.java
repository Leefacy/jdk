/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public final class Util
/*    */ {
/*    */   public static String getSystemProperty(String name)
/*    */   {
/*    */     try
/*    */     {
/* 47 */       return System.getProperty(name); } catch (SecurityException e) {
/*    */     }
/* 49 */     return null;
/*    */   }
/*    */ 
/*    */   public static boolean equals(Locator lhs, Locator rhs)
/*    */   {
/* 60 */     return (lhs.getLineNumber() == rhs.getLineNumber()) && 
/* 58 */       (lhs
/* 58 */       .getColumnNumber() == rhs.getColumnNumber()) && 
/* 59 */       (equals(lhs
/* 59 */       .getSystemId(), rhs.getSystemId())) && 
/* 60 */       (equals(lhs
/* 60 */       .getPublicId(), rhs.getPublicId()));
/*    */   }
/*    */ 
/*    */   private static boolean equals(String lhs, String rhs) {
/* 64 */     if ((lhs == null) && (rhs == null)) return true;
/* 65 */     if ((lhs == null) || (rhs == null)) return false;
/* 66 */     return lhs.equals(rhs);
/*    */   }
/*    */ 
/*    */   public static String getSystemProperty(Class clazz, String name)
/*    */   {
/* 74 */     return getSystemProperty(clazz.getName() + '.' + name);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.util.Util
 * JD-Core Version:    0.6.2
 */