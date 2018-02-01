/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.spi.ClosedConnectionException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class SharedMemoryConnection extends Connection
/*     */ {
/*     */   private long id;
/*  36 */   private Object receiveLock = new Object();
/*  37 */   private Object sendLock = new Object();
/*  38 */   private Object closeLock = new Object();
/*  39 */   private boolean closed = false;
/*     */ 
/*     */   private native byte receiveByte0(long paramLong) throws IOException;
/*     */ 
/*     */   private native void sendByte0(long paramLong, byte paramByte) throws IOException;
/*     */ 
/*     */   private native void close0(long paramLong);
/*     */ 
/*     */   private native byte[] receivePacket0(long paramLong) throws IOException;
/*     */ 
/*     */   private native void sendPacket0(long paramLong, byte[] paramArrayOfByte) throws IOException;
/*     */ 
/*  49 */   void handshake(long paramLong) throws IOException { byte[] arrayOfByte = "JDWP-Handshake".getBytes("UTF-8");
/*     */ 
/*  51 */     for (int i = 0; i < arrayOfByte.length; i++) {
/*  52 */       sendByte0(this.id, arrayOfByte[i]);
/*     */     }
/*  54 */     for (i = 0; i < arrayOfByte.length; i++) {
/*  55 */       int j = receiveByte0(this.id);
/*  56 */       if (j != arrayOfByte[i])
/*  57 */         throw new IOException("handshake failed - unrecognized message from target VM");
/*     */     }
/*     */   }
/*     */ 
/*     */   SharedMemoryConnection(long paramLong)
/*     */     throws IOException
/*     */   {
/*  64 */     this.id = paramLong;
/*     */   }
/*     */ 
/*     */   public void close() {
/*  68 */     synchronized (this.closeLock) {
/*  69 */       if (!this.closed) {
/*  70 */         close0(this.id);
/*  71 */         this.closed = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isOpen() {
/*  77 */     synchronized (this.closeLock) {
/*  78 */       return !this.closed;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] readPacket() throws IOException {
/*  83 */     if (!isOpen()) {
/*  84 */       throw new ClosedConnectionException("Connection closed");
/*     */     }
/*     */     byte[] arrayOfByte;
/*     */     try
/*     */     {
/*  89 */       synchronized (this.receiveLock) {
/*  90 */         arrayOfByte = receivePacket0(this.id);
/*     */       }
/*     */     } catch (IOException localIOException) {
/*  93 */       if (!isOpen()) {
/*  94 */         throw new ClosedConnectionException("Connection closed");
/*     */       }
/*  96 */       throw localIOException;
/*     */     }
/*     */ 
/*  99 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public void writePacket(byte[] paramArrayOfByte) throws IOException {
/* 103 */     if (!isOpen()) {
/* 104 */       throw new ClosedConnectionException("Connection closed");
/*     */     }
/*     */ 
/* 110 */     if (paramArrayOfByte.length < 11) {
/* 111 */       throw new IllegalArgumentException("packet is insufficient size");
/*     */     }
/* 113 */     int i = paramArrayOfByte[0] & 0xFF;
/* 114 */     int j = paramArrayOfByte[1] & 0xFF;
/* 115 */     int k = paramArrayOfByte[2] & 0xFF;
/* 116 */     int m = paramArrayOfByte[3] & 0xFF;
/* 117 */     int n = i << 24 | j << 16 | k << 8 | m << 0;
/* 118 */     if (n < 11) {
/* 119 */       throw new IllegalArgumentException("packet is insufficient size");
/*     */     }
/*     */ 
/* 125 */     if (n > paramArrayOfByte.length) {
/* 126 */       throw new IllegalArgumentException("length mis-match");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 131 */       synchronized (this.sendLock) {
/* 132 */         sendPacket0(this.id, paramArrayOfByte);
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 135 */       if (!isOpen()) {
/* 136 */         throw new ClosedConnectionException("Connection closed");
/*     */       }
/* 138 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SharedMemoryConnection
 * JD-Core Version:    0.6.2
 */