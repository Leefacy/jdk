/*    */ package com.sun.tools.internal.ws.util;
/*    */ 
/*    */ public final class ClassNameInfo
/*    */ {
/*    */   public static String getName(String className)
/*    */   {
/* 35 */     String qual = getQualifier(className);
/* 36 */     int len = className.length();
/* 37 */     int closingBracket = className.indexOf('>');
/* 38 */     if (closingBracket > 0) {
/* 39 */       len = closingBracket;
/*    */     }
/* 41 */     return qual != null ? className
/* 41 */       .substring(qual
/* 41 */       .length() + 1, len) : className;
/*    */   }
/*    */ 
/*    */   public static String getGenericClass(String className)
/*    */   {
/* 53 */     int index = className.indexOf('<');
/* 54 */     if (index < 0)
/* 55 */       return className;
/* 56 */     return index > 0 ? className.substring(0, index) : className;
/*    */   }
/*    */ 
/*    */   public static String getQualifier(String className)
/*    */   {
/* 61 */     int idot = className.indexOf(' ');
/* 62 */     if (idot <= 0)
/* 63 */       idot = className.length();
/*    */     else
/* 65 */       idot--;
/* 66 */     int index = className.lastIndexOf('.', idot - 1);
/* 67 */     return index < 0 ? null : className.substring(0, index);
/*    */   }
/*    */ 
/*    */   public static String replaceInnerClassSym(String name) {
/* 71 */     return name.replace('$', '_');
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.util.ClassNameInfo
 * JD-Core Version:    0.6.2
 */