/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.spi.TransportService.Capabilities;
/*     */ 
/*     */ class SharedMemoryTransportServiceCapabilities extends TransportService.Capabilities
/*     */ {
/*     */   public boolean supportsMultipleConnections()
/*     */   {
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean supportsAttachTimeout() {
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean supportsAcceptTimeout() {
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean supportsHandshakeTimeout() {
/* 187 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SharedMemoryTransportServiceCapabilities
 * JD-Core Version:    0.6.2
 */