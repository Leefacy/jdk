/*    */ package sun.tools.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.StreamTokenizer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class CommandLine
/*    */ {
/*    */   public static String[] parse(String[] paramArrayOfString)
/*    */     throws IOException
/*    */   {
/* 57 */     ArrayList localArrayList = new ArrayList(paramArrayOfString.length);
/* 58 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 59 */       String str = paramArrayOfString[i];
/* 60 */       if ((str.length() > 1) && (str.charAt(0) == '@')) {
/* 61 */         str = str.substring(1);
/* 62 */         if (str.charAt(0) == '@')
/* 63 */           localArrayList.add(str);
/*    */         else
/* 65 */           loadCmdFile(str, localArrayList);
/*    */       }
/*    */       else {
/* 68 */         localArrayList.add(str);
/*    */       }
/*    */     }
/* 71 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*    */   }
/*    */ 
/*    */   private static void loadCmdFile(String paramString, List paramList)
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
/* 86 */       paramList.add(localStreamTokenizer.sval);
/*    */     }
/* 88 */     localBufferedReader.close();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.util.CommandLine
 * JD-Core Version:    0.6.2
 */