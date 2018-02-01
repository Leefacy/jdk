/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ class SunSDK
/*    */ {
/*    */   static String home()
/*    */   {
/* 40 */     File localFile1 = new File(System.getProperty("java.home"));
/* 41 */     File localFile2 = new File(localFile1.getParent());
/*    */ 
/* 44 */     String str = "bin" + File.separator + 
/* 44 */       System.mapLibraryName("jdwp");
/*    */ 
/* 45 */     File localFile3 = new File(localFile2, str);
/* 46 */     return localFile3.exists() ? localFile2.getAbsolutePath() : null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SunSDK
 * JD-Core Version:    0.6.2
 */