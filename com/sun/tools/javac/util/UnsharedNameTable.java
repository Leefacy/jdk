/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ public class UnsharedNameTable extends Name.Table
/*     */ {
/*  54 */   private HashEntry[] hashes = null;
/*     */   private int hashMask;
/*     */   public int index;
/*     */ 
/*     */   public static Name.Table create(Names paramNames)
/*     */   {
/*  42 */     return new UnsharedNameTable(paramNames);
/*     */   }
/*     */ 
/*     */   public UnsharedNameTable(Names paramNames, int paramInt)
/*     */   {
/*  70 */     super(paramNames);
/*  71 */     this.hashMask = (paramInt - 1);
/*  72 */     this.hashes = new HashEntry[paramInt];
/*     */   }
/*     */ 
/*     */   public UnsharedNameTable(Names paramNames) {
/*  76 */     this(paramNames, 32768);
/*     */   }
/*     */ 
/*     */   public Name fromChars(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/*  82 */     byte[] arrayOfByte = new byte[paramInt2 * 3];
/*  83 */     int i = Convert.chars2utf(paramArrayOfChar, paramInt1, arrayOfByte, 0, paramInt2);
/*  84 */     return fromUtf(arrayOfByte, 0, i);
/*     */   }
/*     */ 
/*     */   public Name fromUtf(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  89 */     int i = hashValue(paramArrayOfByte, paramInt1, paramInt2) & this.hashMask;
/*     */ 
/*  91 */     HashEntry localHashEntry1 = this.hashes[i];
/*     */ 
/*  93 */     NameImpl localNameImpl = null;
/*     */ 
/*  95 */     Object localObject = null;
/*  96 */     HashEntry localHashEntry2 = localHashEntry1;
/*     */ 
/*  98 */     while ((localHashEntry1 != null) && 
/*  99 */       (localHashEntry1 != null))
/*     */     {
/* 103 */       localNameImpl = (NameImpl)localHashEntry1.get();
/*     */ 
/* 105 */       if (localNameImpl == null) {
/* 106 */         if (localHashEntry2 == localHashEntry1)
/*     */         {
/*     */           HashEntry tmp78_75 = localHashEntry1.next; localHashEntry2 = tmp78_75; this.hashes[i] = tmp78_75;
/*     */         }
/*     */         else {
/* 110 */           Assert.checkNonNull(localObject, "previousNonNullTableEntry cannot be null here.");
/* 111 */           ((HashEntry)localObject).next = localHashEntry1.next;
/*     */         }
/*     */       }
/*     */       else {
/* 115 */         if ((localNameImpl.getByteLength() == paramInt2) && (equals(localNameImpl.bytes, 0, paramArrayOfByte, paramInt1, paramInt2))) {
/* 116 */           return localNameImpl;
/*     */         }
/* 118 */         localObject = localHashEntry1;
/*     */       }
/*     */ 
/* 121 */       localHashEntry1 = localHashEntry1.next;
/*     */     }
/*     */ 
/* 124 */     byte[] arrayOfByte = new byte[paramInt2];
/* 125 */     System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/* 126 */     localNameImpl = new NameImpl(this, arrayOfByte, this.index++);
/*     */ 
/* 128 */     HashEntry localHashEntry3 = new HashEntry(localNameImpl);
/*     */ 
/* 130 */     if (localObject == null) {
/* 131 */       this.hashes[i] = localHashEntry3;
/*     */     }
/*     */     else {
/* 134 */       Assert.checkNull(((HashEntry)localObject).next, "previousNonNullTableEntry.next must be null.");
/* 135 */       ((HashEntry)localObject).next = localHashEntry3;
/*     */     }
/*     */ 
/* 138 */     return localNameImpl;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 143 */     this.hashes = null;
/*     */   }
/*     */ 
/*     */   static class HashEntry extends WeakReference<UnsharedNameTable.NameImpl>
/*     */   {
/*     */     HashEntry next;
/*     */ 
/*     */     HashEntry(UnsharedNameTable.NameImpl paramNameImpl)
/*     */     {
/*  48 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NameImpl extends Name
/*     */   {
/*     */     final byte[] bytes;
/*     */     final int index;
/*     */ 
/*     */     NameImpl(UnsharedNameTable paramUnsharedNameTable, byte[] paramArrayOfByte, int paramInt)
/*     */     {
/* 148 */       super();
/* 149 */       this.bytes = paramArrayOfByte;
/* 150 */       this.index = paramInt;
/*     */     }
/*     */ 
/*     */     public int getIndex()
/*     */     {
/* 158 */       return this.index;
/*     */     }
/*     */ 
/*     */     public int getByteLength()
/*     */     {
/* 163 */       return this.bytes.length;
/*     */     }
/*     */ 
/*     */     public byte getByteAt(int paramInt)
/*     */     {
/* 168 */       return this.bytes[paramInt];
/*     */     }
/*     */ 
/*     */     public byte[] getByteArray()
/*     */     {
/* 173 */       return this.bytes;
/*     */     }
/*     */ 
/*     */     public int getByteOffset()
/*     */     {
/* 178 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.UnsharedNameTable
 * JD-Core Version:    0.6.2
 */