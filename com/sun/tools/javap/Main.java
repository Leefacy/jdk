/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/* 45 */     JavapTask localJavapTask = new JavapTask();
/* 46 */     int i = localJavapTask.run(paramArrayOfString);
/* 47 */     System.exit(i);
/*    */   }
/*    */ 
/*    */   public static int run(String[] paramArrayOfString, PrintWriter paramPrintWriter)
/*    */   {
/* 57 */     JavapTask localJavapTask = new JavapTask();
/* 58 */     localJavapTask.setLog(paramPrintWriter);
/* 59 */     return localJavapTask.run(paramArrayOfString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.Main
 * JD-Core Version:    0.6.2
 */