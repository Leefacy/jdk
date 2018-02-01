/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ public abstract class Name
/*     */   implements javax.lang.model.element.Name
/*     */ {
/*     */   public final Table table;
/*     */ 
/*     */   protected Name(Table paramTable)
/*     */   {
/*  42 */     this.table = paramTable;
/*     */   }
/*     */ 
/*     */   public boolean contentEquals(CharSequence paramCharSequence)
/*     */   {
/*  49 */     return toString().equals(paramCharSequence.toString());
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  56 */     return toString().length();
/*     */   }
/*     */ 
/*     */   public char charAt(int paramInt)
/*     */   {
/*  63 */     return toString().charAt(paramInt);
/*     */   }
/*     */ 
/*     */   public CharSequence subSequence(int paramInt1, int paramInt2)
/*     */   {
/*  70 */     return toString().subSequence(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Name append(Name paramName)
/*     */   {
/*  76 */     int i = getByteLength();
/*  77 */     byte[] arrayOfByte = new byte[i + paramName.getByteLength()];
/*  78 */     getBytes(arrayOfByte, 0);
/*  79 */     paramName.getBytes(arrayOfByte, i);
/*  80 */     return this.table.fromUtf(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public Name append(char paramChar, Name paramName)
/*     */   {
/*  87 */     int i = getByteLength();
/*  88 */     byte[] arrayOfByte = new byte[i + 1 + paramName.getByteLength()];
/*  89 */     getBytes(arrayOfByte, 0);
/*  90 */     arrayOfByte[i] = ((byte)paramChar);
/*  91 */     paramName.getBytes(arrayOfByte, i + 1);
/*  92 */     return this.table.fromUtf(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int compareTo(Name paramName)
/*     */   {
/*  98 */     return paramName.getIndex() - getIndex();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 104 */     return getByteLength() == 0;
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(byte paramByte)
/*     */   {
/* 110 */     byte[] arrayOfByte = getByteArray();
/* 111 */     int i = getByteOffset();
/* 112 */     int j = getByteLength() - 1;
/* 113 */     while ((j >= 0) && (arrayOfByte[(i + j)] != paramByte)) j--;
/* 114 */     return j;
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Name paramName)
/*     */   {
/* 120 */     byte[] arrayOfByte1 = getByteArray();
/* 121 */     int i = getByteOffset();
/* 122 */     int j = getByteLength();
/* 123 */     byte[] arrayOfByte2 = paramName.getByteArray();
/* 124 */     int k = paramName.getByteOffset();
/* 125 */     int m = paramName.getByteLength();
/*     */ 
/* 127 */     int n = 0;
/* 128 */     while ((n < m) && (n < j) && (arrayOfByte1[(i + n)] == arrayOfByte2[(k + n)]))
/*     */     {
/* 131 */       n++;
/* 132 */     }return n == m;
/*     */   }
/*     */ 
/*     */   public Name subName(int paramInt1, int paramInt2)
/*     */   {
/* 139 */     if (paramInt2 < paramInt1) paramInt2 = paramInt1;
/* 140 */     return this.table.fromUtf(getByteArray(), getByteOffset() + paramInt1, paramInt2 - paramInt1);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 147 */     return Convert.utf2string(getByteArray(), getByteOffset(), getByteLength());
/*     */   }
/*     */ 
/*     */   public byte[] toUtf()
/*     */   {
/* 153 */     byte[] arrayOfByte = new byte[getByteLength()];
/* 154 */     getBytes(arrayOfByte, 0);
/* 155 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public abstract int getIndex();
/*     */ 
/*     */   public abstract int getByteLength();
/*     */ 
/*     */   public abstract byte getByteAt(int paramInt);
/*     */ 
/*     */   public void getBytes(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 174 */     System.arraycopy(getByteArray(), getByteOffset(), paramArrayOfByte, paramInt, getByteLength());
/*     */   }
/*     */ 
/*     */   public abstract byte[] getByteArray();
/*     */ 
/*     */   public abstract int getByteOffset();
/*     */ 
/*     */   public static abstract class Table
/*     */   {
/*     */     public final Names names;
/*     */ 
/*     */     Table(Names paramNames)
/*     */     {
/* 194 */       this.names = paramNames;
/*     */     }
/*     */ 
/*     */     public abstract Name fromChars(char[] paramArrayOfChar, int paramInt1, int paramInt2);
/*     */ 
/*     */     public Name fromString(String paramString)
/*     */     {
/* 204 */       char[] arrayOfChar = paramString.toCharArray();
/* 205 */       return fromChars(arrayOfChar, 0, arrayOfChar.length);
/*     */     }
/*     */ 
/*     */     public Name fromUtf(byte[] paramArrayOfByte)
/*     */     {
/* 212 */       return fromUtf(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public abstract Name fromUtf(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */     public abstract void dispose();
/*     */ 
/*     */     protected static int hashValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/* 227 */       int i = 0;
/* 228 */       int j = paramInt1;
/*     */ 
/* 230 */       for (int k = 0; k < paramInt2; k++) {
/* 231 */         i = (i << 5) - i + paramArrayOfByte[(j++)];
/*     */       }
/* 233 */       return i;
/*     */     }
/*     */ 
/*     */     protected static boolean equals(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*     */     {
/* 240 */       int i = 0;
/* 241 */       while ((i < paramInt3) && (paramArrayOfByte1[(paramInt1 + i)] == paramArrayOfByte2[(paramInt2 + i)])) {
/* 242 */         i++;
/*     */       }
/* 244 */       return i == paramInt3;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Name
 * JD-Core Version:    0.6.2
 */