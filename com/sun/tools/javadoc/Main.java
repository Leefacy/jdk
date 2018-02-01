/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  54 */     System.exit(execute(paramArrayOfString));
/*     */   }
/*     */ 
/*     */   public static int execute(String[] paramArrayOfString)
/*     */   {
/*  63 */     Start localStart = new Start();
/*  64 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(ClassLoader paramClassLoader, String[] paramArrayOfString)
/*     */   {
/*  78 */     Start localStart = new Start(paramClassLoader);
/*  79 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(String paramString, String[] paramArrayOfString)
/*     */   {
/*  89 */     Start localStart = new Start(paramString);
/*  90 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(String paramString, ClassLoader paramClassLoader, String[] paramArrayOfString)
/*     */   {
/* 105 */     Start localStart = new Start(paramString, paramClassLoader);
/* 106 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(String paramString1, String paramString2, String[] paramArrayOfString)
/*     */   {
/* 119 */     Start localStart = new Start(paramString1, paramString2);
/* 120 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(String paramString1, String paramString2, ClassLoader paramClassLoader, String[] paramArrayOfString)
/*     */   {
/* 139 */     Start localStart = new Start(paramString1, paramString2, paramClassLoader);
/* 140 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2, String[] paramArrayOfString)
/*     */   {
/* 159 */     Start localStart = new Start(paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3, paramString2);
/*     */ 
/* 162 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public static int execute(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2, ClassLoader paramClassLoader, String[] paramArrayOfString)
/*     */   {
/* 187 */     Start localStart = new Start(paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3, paramString2, paramClassLoader);
/*     */ 
/* 191 */     return localStart.begin(paramArrayOfString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.Main
 * JD-Core Version:    0.6.2
 */