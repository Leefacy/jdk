/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ 
/*     */ public class SharedNameTable extends Name.Table
/*     */ {
/*  42 */   private static List<SoftReference<SharedNameTable>> freelist = List.nil();
/*     */   private NameImpl[] hashes;
/*     */   public byte[] bytes;
/*     */   private int hashMask;
/*  73 */   private int nc = 0;
/*     */ 
/*     */   public static synchronized SharedNameTable create(Names paramNames)
/*     */   {
/*  45 */     while (freelist.nonEmpty()) {
/*  46 */       SharedNameTable localSharedNameTable = (SharedNameTable)((SoftReference)freelist.head).get();
/*  47 */       freelist = freelist.tail;
/*  48 */       if (localSharedNameTable != null) {
/*  49 */         return localSharedNameTable;
/*     */       }
/*     */     }
/*  52 */     return new SharedNameTable(paramNames);
/*     */   }
/*     */ 
/*     */   private static synchronized void dispose(SharedNameTable paramSharedNameTable) {
/*  56 */     freelist = freelist.prepend(new SoftReference(paramSharedNameTable));
/*     */   }
/*     */ 
/*     */   public SharedNameTable(Names paramNames, int paramInt1, int paramInt2)
/*     */   {
/*  82 */     super(paramNames);
/*  83 */     this.hashMask = (paramInt1 - 1);
/*  84 */     this.hashes = new NameImpl[paramInt1];
/*  85 */     this.bytes = new byte[paramInt2];
/*     */   }
/*     */ 
/*     */   public SharedNameTable(Names paramNames)
/*     */   {
/*  90 */     this(paramNames, 32768, 131072);
/*     */   }
/*     */ 
/*     */   public Name fromChars(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/*  95 */     int i = this.nc;
/*  96 */     byte[] arrayOfByte = this.bytes = ArrayUtils.ensureCapacity(this.bytes, i + paramInt2 * 3);
/*  97 */     int j = Convert.chars2utf(paramArrayOfChar, paramInt1, arrayOfByte, i, paramInt2) - i;
/*  98 */     int k = hashValue(arrayOfByte, i, j) & this.hashMask;
/*  99 */     NameImpl localNameImpl = this.hashes[k];
/* 100 */     while ((localNameImpl != null) && (
/* 101 */       (localNameImpl
/* 101 */       .getByteLength() != j) || 
/* 102 */       (!equals(arrayOfByte, localNameImpl.index, arrayOfByte, i, j))))
/*     */     {
/* 103 */       localNameImpl = localNameImpl.next;
/*     */     }
/* 105 */     if (localNameImpl == null) {
/* 106 */       localNameImpl = new NameImpl(this);
/* 107 */       localNameImpl.index = i;
/* 108 */       localNameImpl.length = j;
/* 109 */       localNameImpl.next = this.hashes[k];
/* 110 */       this.hashes[k] = localNameImpl;
/* 111 */       this.nc = (i + j);
/* 112 */       if (j == 0) {
/* 113 */         this.nc += 1;
/*     */       }
/*     */     }
/* 116 */     return localNameImpl;
/*     */   }
/*     */ 
/*     */   public Name fromUtf(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 121 */     int i = hashValue(paramArrayOfByte, paramInt1, paramInt2) & this.hashMask;
/* 122 */     NameImpl localNameImpl = this.hashes[i];
/* 123 */     byte[] arrayOfByte = this.bytes;
/* 124 */     while ((localNameImpl != null) && (
/* 125 */       (localNameImpl
/* 125 */       .getByteLength() != paramInt2) || (!equals(arrayOfByte, localNameImpl.index, paramArrayOfByte, paramInt1, paramInt2)))) {
/* 126 */       localNameImpl = localNameImpl.next;
/*     */     }
/* 128 */     if (localNameImpl == null) {
/* 129 */       int j = this.nc;
/* 130 */       arrayOfByte = this.bytes = ArrayUtils.ensureCapacity(arrayOfByte, j + paramInt2);
/* 131 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, j, paramInt2);
/* 132 */       localNameImpl = new NameImpl(this);
/* 133 */       localNameImpl.index = j;
/* 134 */       localNameImpl.length = paramInt2;
/* 135 */       localNameImpl.next = this.hashes[i];
/* 136 */       this.hashes[i] = localNameImpl;
/* 137 */       this.nc = (j + paramInt2);
/* 138 */       if (paramInt2 == 0) {
/* 139 */         this.nc += 1;
/*     */       }
/*     */     }
/* 142 */     return localNameImpl;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 147 */     dispose(this);
/*     */   }
/*     */ 
/*     */   static class NameImpl extends Name
/*     */   {
/*     */     NameImpl next;
/*     */     int index;
/*     */     int length;
/*     */ 
/*     */     NameImpl(SharedNameTable paramSharedNameTable)
/*     */     {
/* 165 */       super();
/*     */     }
/*     */ 
/*     */     public int getIndex()
/*     */     {
/* 170 */       return this.index;
/*     */     }
/*     */ 
/*     */     public int getByteLength()
/*     */     {
/* 175 */       return this.length;
/*     */     }
/*     */ 
/*     */     public byte getByteAt(int paramInt)
/*     */     {
/* 180 */       return getByteArray()[(this.index + paramInt)];
/*     */     }
/*     */ 
/*     */     public byte[] getByteArray()
/*     */     {
/* 185 */       return ((SharedNameTable)this.table).bytes;
/*     */     }
/*     */ 
/*     */     public int getByteOffset()
/*     */     {
/* 190 */       return this.index;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 196 */       return this.index;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 202 */       if ((paramObject instanceof Name))
/*     */       {
/* 204 */         return (this.table == ((Name)paramObject).table) && 
/* 204 */           (this.index == ((Name)paramObject)
/* 204 */           .getIndex());
/* 205 */       }return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.SharedNameTable
 * JD-Core Version:    0.6.2
 */