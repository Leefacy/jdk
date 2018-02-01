/*    */ package com.sun.tools.javah;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/* 45 */     JavahTask localJavahTask = new JavahTask();
/* 46 */     int i = localJavahTask.run(paramArrayOfString);
/* 47 */     System.exit(i);
/*    */   }
/*    */ 
/*    */   public static int run(String[] paramArrayOfString, PrintWriter paramPrintWriter)
/*    */   {
/* 57 */     JavahTask localJavahTask = new JavahTask();
/* 58 */     localJavahTask.setLog(paramPrintWriter);
/* 59 */     return localJavahTask.run(paramArrayOfString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.Main
 * JD-Core Version:    0.6.2
 */