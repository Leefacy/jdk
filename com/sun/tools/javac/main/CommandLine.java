/*    */ package com.sun.tools.javac.main;
/*    */ 
/*    */ import com.sun.tools.javac.util.List;
/*    */ import com.sun.tools.javac.util.ListBuffer;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.StreamTokenizer;
/*    */ 
/*    */ public class CommandLine
/*    */ {
/*    */   public static String[] parse(String[] paramArrayOfString)
/*    */     throws IOException
/*    */   {
/* 57 */     ListBuffer localListBuffer = new ListBuffer();
/* 58 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 59 */       String str = paramArrayOfString[i];
/* 60 */       if ((str.length() > 1) && (str.charAt(0) == '@')) {
/* 61 */         str = str.substring(1);
/* 62 */         if (str.charAt(0) == '@')
/* 63 */           localListBuffer.append(str);
/*    */         else
/* 65 */           loadCmdFile(str, localListBuffer);
/*    */       }
/*    */       else {
/* 68 */         localListBuffer.append(str);
/*    */       }
/*    */     }
/* 71 */     return (String[])localListBuffer.toList().toArray(new String[localListBuffer.length()]);
/*    */   }
/*    */ 
/*    */   private static void loadCmdFile(String paramString, ListBuffer<String> paramListBuffer)
/*    */     throws IOException
/*    */   {
/* 77 */     BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
/* 78 */     StreamTokenizer localStreamTokenizer = new StreamTokenizer(localBufferedReader);
/* 79 */     localStreamTokenizer.resetSyntax();
/* 80 */     localStreamTokenizer.wordChars(32, 255);
/* 81 */     localStreamTokenizer.whitespaceChars(0, 32);
/* 82 */     localStreamTokenizer.commentChar(35);
/* 83 */     localStreamTokenizer.quoteChar(34);
/* 84 */     localStreamTokenizer.quoteChar(39);
/* 85 */     while (localStreamTokenizer.nextToken() != -1) {
/* 86 */       paramListBuffer.append(localStreamTokenizer.sval);
/*    */     }
/* 88 */     localBufferedReader.close();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.main.CommandLine
 * JD-Core Version:    0.6.2
 */