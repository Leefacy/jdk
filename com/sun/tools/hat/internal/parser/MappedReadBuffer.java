/*     */ package com.sun.tools.hat.internal.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileChannel.MapMode;
/*     */ 
/*     */ class MappedReadBuffer
/*     */   implements ReadBuffer
/*     */ {
/*     */   private MappedByteBuffer buf;
/*     */ 
/*     */   MappedReadBuffer(MappedByteBuffer paramMappedByteBuffer)
/*     */   {
/*  49 */     this.buf = paramMappedByteBuffer;
/*     */   }
/*     */ 
/*     */   static ReadBuffer create(RandomAccessFile paramRandomAccessFile) throws IOException
/*     */   {
/*  54 */     FileChannel localFileChannel = paramRandomAccessFile.getChannel();
/*  55 */     long l = localFileChannel.size();
/*     */ 
/*  58 */     if ((canUseFileMap()) && (l <= 2147483647L)) {
/*     */       try
/*     */       {
/*  61 */         MappedByteBuffer localMappedByteBuffer = localFileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, l);
/*  62 */         localFileChannel.close();
/*  63 */         return new MappedReadBuffer(localMappedByteBuffer);
/*     */       } catch (IOException localIOException) {
/*  65 */         localIOException.printStackTrace();
/*  66 */         System.err.println("File mapping failed, will use direct read");
/*     */       }
/*     */     }
/*     */ 
/*  70 */     return new FileReadBuffer(paramRandomAccessFile);
/*     */   }
/*     */ 
/*     */   private static boolean canUseFileMap()
/*     */   {
/*  76 */     String str = System.getProperty("jhat.disableFileMap");
/*  77 */     return (str == null) || (str.equals("false"));
/*     */   }
/*     */ 
/*     */   private void seek(long paramLong) throws IOException {
/*  81 */     assert (paramLong <= 2147483647L) : "position overflow";
/*  82 */     this.buf.position((int)paramLong);
/*     */   }
/*     */ 
/*     */   public synchronized void get(long paramLong, byte[] paramArrayOfByte) throws IOException {
/*  86 */     seek(paramLong);
/*  87 */     this.buf.get(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public synchronized char getChar(long paramLong) throws IOException {
/*  91 */     seek(paramLong);
/*  92 */     return this.buf.getChar();
/*     */   }
/*     */ 
/*     */   public synchronized byte getByte(long paramLong) throws IOException {
/*  96 */     seek(paramLong);
/*  97 */     return this.buf.get();
/*     */   }
/*     */ 
/*     */   public synchronized short getShort(long paramLong) throws IOException {
/* 101 */     seek(paramLong);
/* 102 */     return this.buf.getShort();
/*     */   }
/*     */ 
/*     */   public synchronized int getInt(long paramLong) throws IOException {
/* 106 */     seek(paramLong);
/* 107 */     return this.buf.getInt();
/*     */   }
/*     */ 
/*     */   public synchronized long getLong(long paramLong) throws IOException {
/* 111 */     seek(paramLong);
/* 112 */     return this.buf.getLong();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.parser.MappedReadBuffer
 * JD-Core Version:    0.6.2
 */