/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.TransportTimeoutException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import com.sun.jdi.connect.spi.TransportService.Capabilities;
/*     */ import com.sun.jdi.connect.spi.TransportService.ListenKey;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class SocketTransportService extends TransportService
/*     */ {
/*  42 */   private ResourceBundle messages = null;
/*     */ 
/*     */   void handshake(Socket paramSocket, long paramLong)
/*     */     throws IOException
/*     */   {
/* 120 */     paramSocket.setSoTimeout((int)paramLong);
/*     */ 
/* 122 */     byte[] arrayOfByte1 = "JDWP-Handshake".getBytes("UTF-8");
/* 123 */     paramSocket.getOutputStream().write(arrayOfByte1);
/*     */ 
/* 125 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
/* 126 */     int i = 0;
/* 127 */     while (i < arrayOfByte1.length)
/*     */     {
/*     */       try {
/* 130 */         j = paramSocket.getInputStream().read(arrayOfByte2, i, arrayOfByte1.length - i);
/*     */       } catch (SocketTimeoutException localSocketTimeoutException) {
/* 132 */         throw new IOException("handshake timeout");
/*     */       }
/* 134 */       if (j < 0) {
/* 135 */         paramSocket.close();
/* 136 */         throw new IOException("handshake failed - connection prematurally closed");
/*     */       }
/* 138 */       i += j;
/*     */     }
/* 140 */     for (int j = 0; j < arrayOfByte1.length; j++) {
/* 141 */       if (arrayOfByte2[j] != arrayOfByte1[j]) {
/* 142 */         throw new IOException("handshake failed - unrecognized message from target VM");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 147 */     paramSocket.setSoTimeout(0);
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 160 */     return "Socket";
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/* 167 */     synchronized (this) {
/* 168 */       if (this.messages == null) {
/* 169 */         this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */       }
/*     */     }
/* 172 */     return this.messages.getString("socket_transportservice.description");
/*     */   }
/*     */ 
/*     */   public TransportService.Capabilities capabilities()
/*     */   {
/* 179 */     return new SocketTransportServiceCapabilities();
/*     */   }
/*     */ 
/*     */   public Connection attach(String paramString, long paramLong1, long paramLong2)
/*     */     throws IOException
/*     */   {
/* 190 */     if (paramString == null) {
/* 191 */       throw new NullPointerException("address is null");
/*     */     }
/* 193 */     if ((paramLong1 < 0L) || (paramLong2 < 0L)) {
/* 194 */       throw new IllegalArgumentException("timeout is negative");
/*     */     }
/*     */ 
/* 197 */     int i = paramString.indexOf(':');
/*     */     String str1;
/*     */     String str2;
/* 200 */     if (i < 0) {
/* 201 */       str1 = InetAddress.getLocalHost().getHostName();
/* 202 */       str2 = paramString;
/*     */     } else {
/* 204 */       str1 = paramString.substring(0, i);
/* 205 */       str2 = paramString.substring(i + 1);
/*     */     }
/*     */     int j;
/*     */     try
/*     */     {
/* 210 */       j = Integer.decode(str2).intValue();
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 212 */       throw new IllegalArgumentException("unable to parse port number in address");
/*     */     }
/*     */ 
/* 219 */     InetSocketAddress localInetSocketAddress = new InetSocketAddress(str1, j);
/* 220 */     Socket localSocket = new Socket();
/*     */     try {
/* 222 */       localSocket.connect(localInetSocketAddress, (int)paramLong1);
/*     */     } catch (SocketTimeoutException localSocketTimeoutException) {
/*     */       try {
/* 225 */         localSocket.close(); } catch (IOException localIOException2) {
/*     */       }
/* 227 */       throw new TransportTimeoutException("timed out trying to establish connection");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 232 */       handshake(localSocket, paramLong2);
/*     */     } catch (IOException localIOException1) {
/*     */       try {
/* 235 */         localSocket.close(); } catch (IOException localIOException3) {
/*     */       }
/* 237 */       throw localIOException1;
/*     */     }
/*     */ 
/* 240 */     return new SocketConnection(localSocket);
/*     */   }
/*     */ 
/*     */   TransportService.ListenKey startListening(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/*     */     InetSocketAddress localInetSocketAddress;
/* 249 */     if (paramString == null)
/* 250 */       localInetSocketAddress = new InetSocketAddress(paramInt);
/*     */     else {
/* 252 */       localInetSocketAddress = new InetSocketAddress(paramString, paramInt);
/*     */     }
/* 254 */     ServerSocket localServerSocket = new ServerSocket();
/* 255 */     localServerSocket.bind(localInetSocketAddress);
/* 256 */     return new SocketListenKey(localServerSocket);
/*     */   }
/*     */ 
/*     */   public TransportService.ListenKey startListening(String paramString)
/*     */     throws IOException
/*     */   {
/* 264 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 265 */       paramString = "0";
/*     */     }
/*     */ 
/* 268 */     int i = paramString.indexOf(':');
/* 269 */     String str = null;
/* 270 */     if (i >= 0) {
/* 271 */       str = paramString.substring(0, i);
/* 272 */       paramString = paramString.substring(i + 1);
/*     */     }
/*     */     int j;
/*     */     try
/*     */     {
/* 277 */       j = Integer.decode(paramString).intValue();
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 279 */       throw new IllegalArgumentException("unable to parse port number in address");
/*     */     }
/*     */ 
/* 283 */     return startListening(str, j);
/*     */   }
/*     */ 
/*     */   public TransportService.ListenKey startListening()
/*     */     throws IOException
/*     */   {
/* 290 */     return startListening(null, 0);
/*     */   }
/*     */ 
/*     */   public void stopListening(TransportService.ListenKey paramListenKey)
/*     */     throws IOException
/*     */   {
/* 297 */     if (!(paramListenKey instanceof SocketListenKey)) {
/* 298 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */ 
/* 301 */     synchronized (paramListenKey) {
/* 302 */       ServerSocket localServerSocket = ((SocketListenKey)paramListenKey).socket();
/*     */ 
/* 306 */       if (localServerSocket.isClosed()) {
/* 307 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/* 309 */       localServerSocket.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Connection accept(TransportService.ListenKey paramListenKey, long paramLong1, long paramLong2)
/*     */     throws IOException
/*     */   {
/* 317 */     if ((paramLong1 < 0L) || (paramLong2 < 0L)) {
/* 318 */       throw new IllegalArgumentException("timeout is negative");
/*     */     }
/* 320 */     if (!(paramListenKey instanceof SocketListenKey))
/* 321 */       throw new IllegalArgumentException("Invalid listener");
/*     */     ServerSocket localServerSocket;
/* 327 */     synchronized (paramListenKey) {
/* 328 */       localServerSocket = ((SocketListenKey)paramListenKey).socket();
/* 329 */       if (localServerSocket.isClosed()) {
/* 330 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 345 */     localServerSocket.setSoTimeout((int)paramLong1);
/*     */     try
/*     */     {
/* 348 */       ??? = localServerSocket.accept();
/*     */     } catch (SocketTimeoutException localSocketTimeoutException) {
/* 350 */       throw new TransportTimeoutException("timeout waiting for connection");
/*     */     }
/*     */ 
/* 354 */     handshake((Socket)???, paramLong2);
/*     */ 
/* 356 */     return new SocketConnection((Socket)???);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 360 */     return name();
/*     */   }
/*     */ 
/*     */   static class SocketListenKey extends TransportService.ListenKey
/*     */   {
/*     */     ServerSocket ss;
/*     */ 
/*     */     SocketListenKey(ServerSocket paramServerSocket)
/*     */     {
/*  52 */       this.ss = paramServerSocket;
/*     */     }
/*     */ 
/*     */     ServerSocket socket() {
/*  56 */       return this.ss;
/*     */     }
/*     */ 
/*     */     public String address()
/*     */     {
/*  64 */       InetAddress localInetAddress = this.ss.getInetAddress();
/*     */ 
/*  72 */       if (localInetAddress.isAnyLocalAddress()) {
/*     */         try {
/*  74 */           localInetAddress = InetAddress.getLocalHost();
/*     */         } catch (UnknownHostException localUnknownHostException1) {
/*  76 */           localObject2 = new byte[] { 127, 0, 0, 1 };
/*     */           try {
/*  78 */             localInetAddress = InetAddress.getByAddress("127.0.0.1", (byte[])localObject2);
/*     */           } catch (UnknownHostException localUnknownHostException2) {
/*  80 */             throw new InternalError("unable to get local hostname");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  92 */       Object localObject2 = localInetAddress.getHostName();
/*  93 */       String str = localInetAddress.getHostAddress();
/*     */       Object localObject1;
/*  94 */       if (((String)localObject2).equals(str)) {
/*  95 */         if ((localInetAddress instanceof Inet6Address))
/*  96 */           localObject1 = "[" + str + "]";
/*     */         else
/*  98 */           localObject1 = str;
/*     */       }
/*     */       else {
/* 101 */         localObject1 = localObject2;
/*     */       }
/*     */ 
/* 108 */       return (String)localObject1 + ":" + this.ss.getLocalPort();
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 112 */       return address();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SocketTransportService
 * JD-Core Version:    0.6.2
 */