/*    */ package com.sun.tools.javac;
/*    */ 
/*    */ import com.sun.tools.javac.main.Main.Result;
/*    */ import java.io.PrintWriter;
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString)
/*    */     throws Exception
/*    */   {
/* 42 */     System.exit(compile(paramArrayOfString));
/*    */   }
/*    */ 
/*    */   public static int compile(String[] paramArrayOfString)
/*    */   {
/* 54 */     com.sun.tools.javac.main.Main localMain = new com.sun.tools.javac.main.Main("javac");
/*    */ 
/* 56 */     return localMain.compile(paramArrayOfString).exitCode;
/*    */   }
/*    */ 
/*    */   public static int compile(String[] paramArrayOfString, PrintWriter paramPrintWriter)
/*    */   {
/* 72 */     com.sun.tools.javac.main.Main localMain = new com.sun.tools.javac.main.Main("javac", paramPrintWriter);
/*    */ 
/* 74 */     return localMain.compile(paramArrayOfString).exitCode;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.Main
 * JD-Core Version:    0.6.2
 */