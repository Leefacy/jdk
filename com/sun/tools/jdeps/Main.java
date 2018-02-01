/*    */ package com.sun.tools.jdeps;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString)
/*    */     throws Exception
/*    */   {
/* 47 */     JdepsTask localJdepsTask = new JdepsTask();
/* 48 */     int i = localJdepsTask.run(paramArrayOfString);
/* 49 */     System.exit(i);
/*    */   }
/*    */ 
/*    */   public static int run(String[] paramArrayOfString, PrintWriter paramPrintWriter)
/*    */   {
/* 61 */     JdepsTask localJdepsTask = new JdepsTask();
/* 62 */     localJdepsTask.setLog(paramPrintWriter);
/* 63 */     return localJdepsTask.run(paramArrayOfString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdeps.Main
 * JD-Core Version:    0.6.2
 */