/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import com.sun.jdi.connect.spi.TransportService.Capabilities;
/*     */ import com.sun.jdi.connect.spi.TransportService.ListenKey;
/*     */ import java.io.IOException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ class SharedMemoryTransportService extends TransportService
/*     */ {
/*  37 */   private ResourceBundle messages = null;
/*     */ 
/*     */   SharedMemoryTransportService()
/*     */   {
/*  69 */     System.loadLibrary("dt_shmem");
/*  70 */     initialize();
/*     */   }
/*     */ 
/*     */   public String name() {
/*  74 */     return "SharedMemory";
/*     */   }
/*     */ 
/*     */   public String defaultAddress() {
/*  78 */     return "javadebug";
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  85 */     synchronized (this) {
/*  86 */       if (this.messages == null) {
/*  87 */         this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */       }
/*     */     }
/*  90 */     return this.messages.getString("memory_transportservice.description");
/*     */   }
/*     */ 
/*     */   public TransportService.Capabilities capabilities() {
/*  94 */     return new SharedMemoryTransportServiceCapabilities(); } 
/*     */   private native void initialize();
/*     */ 
/*     */   private native long startListening0(String paramString) throws IOException;
/*     */ 
/*     */   private native long attach0(String paramString, long paramLong) throws IOException;
/*     */ 
/*     */   private native void stopListening0(long paramLong) throws IOException;
/*     */ 
/*     */   private native long accept0(long paramLong1, long paramLong2) throws IOException;
/*     */ 
/*     */   private native String name(long paramLong) throws IOException;
/*     */ 
/* 105 */   public Connection attach(String paramString, long paramLong1, long paramLong2) throws IOException { if (paramString == null) {
/* 106 */       throw new NullPointerException("address is null");
/*     */     }
/* 108 */     long l = attach0(paramString, paramLong1);
/* 109 */     SharedMemoryConnection localSharedMemoryConnection = new SharedMemoryConnection(l);
/* 110 */     localSharedMemoryConnection.handshake(paramLong2);
/* 111 */     return localSharedMemoryConnection; }
/*     */ 
/*     */   public TransportService.ListenKey startListening(String paramString) throws IOException
/*     */   {
/* 115 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 116 */       paramString = defaultAddress();
/*     */     }
/* 118 */     long l = startListening0(paramString);
/* 119 */     return new SharedMemoryListenKey(l, name(l));
/*     */   }
/*     */ 
/*     */   public TransportService.ListenKey startListening() throws IOException {
/* 123 */     return startListening(null);
/*     */   }
/*     */ 
/*     */   public void stopListening(TransportService.ListenKey paramListenKey) throws IOException {
/* 127 */     if (!(paramListenKey instanceof SharedMemoryListenKey)) {
/* 128 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */ 
/* 132 */     SharedMemoryListenKey localSharedMemoryListenKey = (SharedMemoryListenKey)paramListenKey;
/*     */     long l;
/* 133 */     synchronized (localSharedMemoryListenKey) {
/* 134 */       l = localSharedMemoryListenKey.id();
/* 135 */       if (l == 0L) {
/* 136 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/*     */ 
/* 140 */       localSharedMemoryListenKey.setId(0L);
/*     */     }
/* 142 */     stopListening0(l);
/*     */   }
/*     */ 
/*     */   public Connection accept(TransportService.ListenKey paramListenKey, long paramLong1, long paramLong2) throws IOException {
/* 146 */     if (!(paramListenKey instanceof SharedMemoryListenKey)) {
/* 147 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */ 
/* 151 */     SharedMemoryListenKey localSharedMemoryListenKey = (SharedMemoryListenKey)paramListenKey;
/*     */     long l1;
/* 152 */     synchronized (localSharedMemoryListenKey) {
/* 153 */       l1 = localSharedMemoryListenKey.id();
/* 154 */       if (l1 == 0L) {
/* 155 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 164 */     long l2 = accept0(l1, paramLong1);
/* 165 */     SharedMemoryConnection localSharedMemoryConnection = new SharedMemoryConnection(l2);
/* 166 */     localSharedMemoryConnection.handshake(paramLong2);
/* 167 */     return localSharedMemoryConnection;
/*     */   }
/*     */ 
/*     */   static class SharedMemoryListenKey extends TransportService.ListenKey
/*     */   {
/*     */     long id;
/*     */     String name;
/*     */ 
/*     */     SharedMemoryListenKey(long paramLong, String paramString)
/*     */     {
/*  47 */       this.id = paramLong;
/*  48 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     long id() {
/*  52 */       return this.id;
/*     */     }
/*     */ 
/*     */     void setId(long paramLong) {
/*  56 */       this.id = paramLong;
/*     */     }
/*     */ 
/*     */     public String address() {
/*  60 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String toString() {
/*  64 */       return address();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SharedMemoryTransportService
 * JD-Core Version:    0.6.2
 */