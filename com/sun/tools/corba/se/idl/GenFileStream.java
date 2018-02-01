/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.CharArrayWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class GenFileStream extends PrintWriter
/*    */ {
/*    */   private CharArrayWriter charArrayWriter;
/*    */   private static CharArrayWriter tmpCharArrayWriter;
/*    */   private String name;
/*    */ 
/*    */   public GenFileStream(String paramString)
/*    */   {
/* 58 */     super(GenFileStream.tmpCharArrayWriter = new CharArrayWriter());
/* 59 */     this.charArrayWriter = tmpCharArrayWriter;
/* 60 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 65 */     File localFile = new File(this.name);
/*    */     try
/*    */     {
/* 68 */       if (checkError()) {
/* 69 */         throw new IOException();
/*    */       }
/*    */ 
/* 74 */       FileWriter localFileWriter = new FileWriter(localFile);
/* 75 */       localFileWriter.write(this.charArrayWriter.toCharArray());
/* 76 */       localFileWriter.close();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 80 */       String[] arrayOfString = { this.name, localIOException.toString() };
/* 81 */       System.err.println(Util.getMessage("GenFileStream.1", arrayOfString));
/*    */     }
/* 83 */     super.close();
/*    */   }
/*    */ 
/*    */   public String name()
/*    */   {
/* 88 */     return this.name;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.GenFileStream
 * JD-Core Version:    0.6.2
 */