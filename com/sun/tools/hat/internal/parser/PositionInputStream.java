/*    */ package com.sun.tools.hat.internal.parser;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class PositionInputStream extends FilterInputStream
/*    */ {
/* 45 */   private long position = 0L;
/*    */ 
/*    */   public PositionInputStream(InputStream paramInputStream) {
/* 48 */     super(paramInputStream);
/*    */   }
/*    */ 
/*    */   public int read() throws IOException {
/* 52 */     int i = super.read();
/* 53 */     if (i != -1) this.position += 1L;
/* 54 */     return i;
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 58 */     int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/* 59 */     if (i != -1) this.position += i;
/* 60 */     return i;
/*    */   }
/*    */ 
/*    */   public long skip(long paramLong) throws IOException {
/* 64 */     long l = super.skip(paramLong);
/* 65 */     this.position += l;
/* 66 */     return l;
/*    */   }
/*    */ 
/*    */   public boolean markSupported() {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   public void mark(int paramInt) {
/* 74 */     throw new UnsupportedOperationException("mark");
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 78 */     throw new UnsupportedOperationException("reset");
/*    */   }
/*    */ 
/*    */   public long position() {
/* 82 */     return this.position;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.parser.PositionInputStream
 * JD-Core Version:    0.6.2
 */