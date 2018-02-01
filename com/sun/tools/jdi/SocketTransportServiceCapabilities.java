/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.spi.TransportService.Capabilities;
/*     */ 
/*     */ class SocketTransportServiceCapabilities extends TransportService.Capabilities
/*     */ {
/*     */   public boolean supportsMultipleConnections()
/*     */   {
/* 521 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean supportsAttachTimeout() {
/* 525 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean supportsAcceptTimeout() {
/* 529 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean supportsHandshakeTimeout() {
/* 533 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SocketTransportServiceCapabilities
 * JD-Core Version:    0.6.2
 */