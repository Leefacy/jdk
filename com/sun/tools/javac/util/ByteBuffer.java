/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class ByteBuffer
/*     */ {
/*     */   public byte[] elems;
/*     */   public int length;
/*     */ 
/*     */   public ByteBuffer()
/*     */   {
/*  52 */     this(64);
/*     */   }
/*     */ 
/*     */   public ByteBuffer(int paramInt)
/*     */   {
/*  59 */     this.elems = new byte[paramInt];
/*  60 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   public void appendByte(int paramInt)
/*     */   {
/*  66 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length);
/*  67 */     this.elems[(this.length++)] = ((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public void appendBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  74 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length + paramInt2);
/*  75 */     System.arraycopy(paramArrayOfByte, paramInt1, this.elems, this.length, paramInt2);
/*  76 */     this.length += paramInt2;
/*     */   }
/*     */ 
/*     */   public void appendBytes(byte[] paramArrayOfByte)
/*     */   {
/*  82 */     appendBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void appendChar(int paramInt)
/*     */   {
/*  88 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length + 1);
/*  89 */     this.elems[this.length] = ((byte)(paramInt >> 8 & 0xFF));
/*  90 */     this.elems[(this.length + 1)] = ((byte)(paramInt & 0xFF));
/*  91 */     this.length += 2;
/*     */   }
/*     */ 
/*     */   public void appendInt(int paramInt)
/*     */   {
/*  97 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length + 3);
/*  98 */     this.elems[this.length] = ((byte)(paramInt >> 24 & 0xFF));
/*  99 */     this.elems[(this.length + 1)] = ((byte)(paramInt >> 16 & 0xFF));
/* 100 */     this.elems[(this.length + 2)] = ((byte)(paramInt >> 8 & 0xFF));
/* 101 */     this.elems[(this.length + 3)] = ((byte)(paramInt & 0xFF));
/* 102 */     this.length += 4;
/*     */   }
/*     */ 
/*     */   public void appendLong(long paramLong)
/*     */   {
/* 108 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(8);
/* 109 */     DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*     */     try {
/* 111 */       localDataOutputStream.writeLong(paramLong);
/* 112 */       appendBytes(localByteArrayOutputStream.toByteArray(), 0, 8);
/*     */     } catch (IOException localIOException) {
/* 114 */       throw new AssertionError("write");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void appendFloat(float paramFloat)
/*     */   {
/* 121 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4);
/* 122 */     DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*     */     try {
/* 124 */       localDataOutputStream.writeFloat(paramFloat);
/* 125 */       appendBytes(localByteArrayOutputStream.toByteArray(), 0, 4);
/*     */     } catch (IOException localIOException) {
/* 127 */       throw new AssertionError("write");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void appendDouble(double paramDouble)
/*     */   {
/* 134 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(8);
/* 135 */     DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*     */     try {
/* 137 */       localDataOutputStream.writeDouble(paramDouble);
/* 138 */       appendBytes(localByteArrayOutputStream.toByteArray(), 0, 8);
/*     */     } catch (IOException localIOException) {
/* 140 */       throw new AssertionError("write");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void appendName(Name paramName)
/*     */   {
/* 147 */     appendBytes(paramName.getByteArray(), paramName.getByteOffset(), paramName.getByteLength());
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 153 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   public Name toName(Names paramNames)
/*     */   {
/* 159 */     return paramNames.fromUtf(this.elems, 0, this.length);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.ByteBuffer
 * JD-Core Version:    0.6.2
 */