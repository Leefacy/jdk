/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public abstract class JavaLazyReadObject extends JavaHeapObject
/*     */ {
/*     */   private final long offset;
/*     */ 
/*     */   protected JavaLazyReadObject(long paramLong)
/*     */   {
/*  47 */     this.offset = paramLong;
/*     */   }
/*     */ 
/*     */   public final int getSize() {
/*  51 */     return getValueLength() + getClazz().getMinimumObjectSize();
/*     */   }
/*     */ 
/*     */   protected final long getOffset() {
/*  55 */     return this.offset;
/*     */   }
/*     */ 
/*     */   protected final int getValueLength()
/*     */   {
/*     */     try {
/*  61 */       return readValueLength();
/*     */     } catch (IOException localIOException) {
/*  63 */       System.err.println("lazy read failed at offset " + this.offset);
/*  64 */       localIOException.printStackTrace();
/*  65 */     }return 0;
/*     */   }
/*     */ 
/*     */   protected final byte[] getValue()
/*     */   {
/*     */     try
/*     */     {
/*  72 */       return readValue();
/*     */     } catch (IOException localIOException) {
/*  74 */       System.err.println("lazy read failed at offset " + this.offset);
/*  75 */       localIOException.printStackTrace();
/*  76 */     }return Snapshot.EMPTY_BYTE_ARRAY;
/*     */   }
/*     */ 
/*     */   public final long getId()
/*     */   {
/*     */     try
/*     */     {
/*  83 */       ReadBuffer localReadBuffer = getClazz().getReadBuffer();
/*  84 */       int i = getClazz().getIdentifierSize();
/*  85 */       if (i == 4) {
/*  86 */         return localReadBuffer.getInt(this.offset) & Snapshot.SMALL_ID_MASK;
/*     */       }
/*  88 */       return localReadBuffer.getLong(this.offset);
/*     */     }
/*     */     catch (IOException localIOException) {
/*  91 */       System.err.println("lazy read failed at offset " + this.offset);
/*  92 */       localIOException.printStackTrace();
/*  93 */     }return -1L;
/*     */   }
/*     */ 
/*     */   protected abstract int readValueLength() throws IOException;
/*     */ 
/*     */   protected abstract byte[] readValue() throws IOException;
/*     */ 
/*     */   protected static Number makeId(long paramLong)
/*     */   {
/* 102 */     if ((paramLong & (Snapshot.SMALL_ID_MASK ^ 0xFFFFFFFF)) == 0L) {
/* 103 */       return new Integer((int)paramLong);
/*     */     }
/* 105 */     return new Long(paramLong);
/*     */   }
/*     */ 
/*     */   protected static long getIdValue(Number paramNumber)
/*     */   {
/* 111 */     long l = paramNumber.longValue();
/* 112 */     if ((paramNumber instanceof Integer)) {
/* 113 */       l &= Snapshot.SMALL_ID_MASK;
/*     */     }
/* 115 */     return l;
/*     */   }
/*     */ 
/*     */   protected final long objectIdAt(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/* 120 */     int i = getClazz().getIdentifierSize();
/* 121 */     if (i == 4) {
/* 122 */       return intAt(paramInt, paramArrayOfByte) & Snapshot.SMALL_ID_MASK;
/*     */     }
/* 124 */     return longAt(paramInt, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   protected static byte byteAt(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/* 130 */     return paramArrayOfByte[paramInt];
/*     */   }
/*     */ 
/*     */   protected static boolean booleanAt(int paramInt, byte[] paramArrayOfByte) {
/* 134 */     return (paramArrayOfByte[paramInt] & 0xFF) != 0;
/*     */   }
/*     */ 
/*     */   protected static char charAt(int paramInt, byte[] paramArrayOfByte) {
/* 138 */     int i = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 139 */     int j = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 140 */     return (char)((i << 8) + j);
/*     */   }
/*     */ 
/*     */   protected static short shortAt(int paramInt, byte[] paramArrayOfByte) {
/* 144 */     int i = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 145 */     int j = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 146 */     return (short)((i << 8) + j);
/*     */   }
/*     */ 
/*     */   protected static int intAt(int paramInt, byte[] paramArrayOfByte) {
/* 150 */     int i = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 151 */     int j = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 152 */     int k = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 153 */     int m = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 154 */     return (i << 24) + (j << 16) + (k << 8) + m;
/*     */   }
/*     */ 
/*     */   protected static long longAt(int paramInt, byte[] paramArrayOfByte) {
/* 158 */     long l = 0L;
/* 159 */     for (int i = 0; i < 8; i++) {
/* 160 */       l <<= 8;
/* 161 */       int j = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 162 */       l |= j;
/*     */     }
/* 164 */     return l;
/*     */   }
/*     */ 
/*     */   protected static float floatAt(int paramInt, byte[] paramArrayOfByte) {
/* 168 */     int i = intAt(paramInt, paramArrayOfByte);
/* 169 */     return Float.intBitsToFloat(i);
/*     */   }
/*     */ 
/*     */   protected static double doubleAt(int paramInt, byte[] paramArrayOfByte) {
/* 173 */     long l = longAt(paramInt, paramArrayOfByte);
/* 174 */     return Double.longBitsToDouble(l);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaLazyReadObject
 * JD-Core Version:    0.6.2
 */