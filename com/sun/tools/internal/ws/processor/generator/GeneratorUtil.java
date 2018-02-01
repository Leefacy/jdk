/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.Options;
/*    */ 
/*    */ public class GeneratorUtil
/*    */ {
/*    */   public static boolean classExists(Options options, String className)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       getLoadableClassName(className, options.getClassLoader());
/* 43 */       return true; } catch (ClassNotFoundException ce) {
/*    */     }
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   private static String getLoadableClassName(String className, ClassLoader classLoader)
/*    */     throws ClassNotFoundException
/*    */   {
/*    */     try
/*    */     {
/* 55 */       Class.forName(className, true, classLoader);
/*    */     } catch (ClassNotFoundException e) {
/* 57 */       int idx = className.lastIndexOf(GeneratorConstants.DOTC.getValue());
/* 58 */       if (idx > -1) {
/* 59 */         String tmp = className.substring(0, idx) + GeneratorConstants.SIG_INNERCLASS.getValue();
/* 60 */         tmp = tmp + className.substring(idx + 1);
/* 61 */         return getLoadableClassName(tmp, classLoader);
/*    */       }
/* 63 */       throw e;
/*    */     }
/* 65 */     return className;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.generator.GeneratorUtil
 * JD-Core Version:    0.6.2
 */