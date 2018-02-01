/*    */ package com.sun.tools.extcheck;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public final class Main
/*    */ {
/*    */   public static final String INSUFFICIENT = "Insufficient number of arguments";
/*    */   public static final String MISSING = "Missing <jar file> argument";
/*    */   public static final String DOES_NOT_EXIST = "Jarfile does not exist: ";
/*    */   public static final String EXTRA = "Extra command line argument: ";
/*    */ 
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/*    */     try
/*    */     {
/* 48 */       realMain(paramArrayOfString);
/*    */     } catch (Exception localException) {
/* 50 */       System.err.println(localException.getMessage());
/* 51 */       System.exit(-1);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void realMain(String[] paramArrayOfString) throws Exception {
/* 56 */     if (paramArrayOfString.length < 1) {
/* 57 */       usage("Insufficient number of arguments");
/*    */     }
/* 59 */     int i = 0;
/* 60 */     boolean bool1 = false;
/* 61 */     if (paramArrayOfString[i].equals("-verbose")) {
/* 62 */       bool1 = true;
/* 63 */       i++;
/* 64 */       if (i >= paramArrayOfString.length) {
/* 65 */         usage("Missing <jar file> argument");
/*    */       }
/*    */     }
/* 68 */     String str = paramArrayOfString[i];
/* 69 */     i++;
/* 70 */     File localFile = new File(str);
/* 71 */     if (!localFile.exists()) {
/* 72 */       usage("Jarfile does not exist: " + str);
/*    */     }
/* 74 */     if (i < paramArrayOfString.length) {
/* 75 */       usage("Extra command line argument: " + paramArrayOfString[i]);
/*    */     }
/* 77 */     ExtCheck localExtCheck = ExtCheck.create(localFile, bool1);
/* 78 */     boolean bool2 = localExtCheck.checkInstalledAgainstTarget();
/* 79 */     if (bool2)
/* 80 */       System.exit(0);
/*    */     else
/* 82 */       System.exit(1);
/*    */   }
/*    */ 
/*    */   private static void usage(String paramString) throws Exception
/*    */   {
/* 87 */     throw new Exception(paramString + "\nUsage: extcheck [-verbose] <jar file>");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.extcheck.Main
 * JD-Core Version:    0.6.2
 */