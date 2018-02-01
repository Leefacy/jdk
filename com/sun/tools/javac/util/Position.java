/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.source.tree.LineMap;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ public class Position
/*     */ {
/*     */   public static final int NOPOS = -1;
/*     */   public static final int FIRSTPOS = 0;
/*     */   public static final int FIRSTLINE = 1;
/*     */   public static final int FIRSTCOLUMN = 1;
/*     */   public static final int LINESHIFT = 10;
/*     */   public static final int MAXCOLUMN = 1023;
/*     */   public static final int MAXLINE = 4194303;
/*     */   public static final int MAXPOS = 2147483647;
/*     */ 
/*     */   public static LineMap makeLineMap(char[] paramArrayOfChar, int paramInt, boolean paramBoolean)
/*     */   {
/*  75 */     LineMapImpl localLineMapImpl = paramBoolean ? new LineTabMapImpl(paramInt) : new LineMapImpl();
/*     */ 
/*  77 */     localLineMapImpl.build(paramArrayOfChar, paramInt);
/*  78 */     return localLineMapImpl;
/*     */   }
/*     */ 
/*     */   public static int encodePosition(int paramInt1, int paramInt2)
/*     */   {
/*  93 */     if (paramInt1 < 1)
/*  94 */       throw new IllegalArgumentException("line must be greater than 0");
/*  95 */     if (paramInt2 < 1) {
/*  96 */       throw new IllegalArgumentException("column must be greater than 0");
/*     */     }
/*  98 */     if ((paramInt1 > 4194303) || (paramInt2 > 1023)) {
/*  99 */       return -1;
/*     */     }
/* 101 */     return (paramInt1 << 10) + paramInt2;
/*     */   }
/*     */ 
/*     */   public static abstract interface LineMap extends LineMap
/*     */   {
/*     */     public abstract int getStartPosition(int paramInt);
/*     */ 
/*     */     public abstract int getPosition(int paramInt1, int paramInt2);
/*     */ 
/*     */     public abstract int getLineNumber(int paramInt);
/*     */ 
/*     */     public abstract int getColumnNumber(int paramInt);
/*     */   }
/*     */ 
/*     */   static class LineMapImpl
/*     */     implements Position.LineMap
/*     */   {
/*     */     protected int[] startPosition;
/* 190 */     private int lastPosition = 0;
/* 191 */     private int lastLine = 1;
/*     */ 
/*     */     protected void build(char[] paramArrayOfChar, int paramInt)
/*     */     {
/* 151 */       int i = 0;
/* 152 */       int j = 0;
/* 153 */       int[] arrayOfInt = new int[paramInt];
/* 154 */       while (j < paramInt) {
/* 155 */         arrayOfInt[(i++)] = j;
/*     */         do {
/* 157 */           int k = paramArrayOfChar[j];
/* 158 */           if ((k == 13) || (k == 10)) {
/* 159 */             if ((k == 13) && (j + 1 < paramInt) && (paramArrayOfChar[(j + 1)] == '\n')) {
/* 160 */               j += 2; break;
/*     */             }
/* 162 */             j++;
/* 163 */             break;
/*     */           }
/* 165 */           if (k == 9)
/* 166 */             setTabPosition(j);
/* 167 */           j++; } while (j < paramInt);
/*     */       }
/* 169 */       this.startPosition = new int[i];
/* 170 */       System.arraycopy(arrayOfInt, 0, this.startPosition, 0, i);
/*     */     }
/*     */ 
/*     */     public int getStartPosition(int paramInt) {
/* 174 */       return this.startPosition[(paramInt - 1)];
/*     */     }
/*     */ 
/*     */     public long getStartPosition(long paramLong) {
/* 178 */       return getStartPosition(longToInt(paramLong));
/*     */     }
/*     */ 
/*     */     public int getPosition(int paramInt1, int paramInt2) {
/* 182 */       return this.startPosition[(paramInt1 - 1)] + paramInt2 - 1;
/*     */     }
/*     */ 
/*     */     public long getPosition(long paramLong1, long paramLong2) {
/* 186 */       return getPosition(longToInt(paramLong1), longToInt(paramLong2));
/*     */     }
/*     */ 
/*     */     public int getLineNumber(int paramInt)
/*     */     {
/* 194 */       if (paramInt == this.lastPosition) {
/* 195 */         return this.lastLine;
/*     */       }
/* 197 */       this.lastPosition = paramInt;
/*     */ 
/* 199 */       int i = 0;
/* 200 */       int j = this.startPosition.length - 1;
/* 201 */       while (i <= j) {
/* 202 */         int k = i + j >> 1;
/* 203 */         int m = this.startPosition[k];
/*     */ 
/* 205 */         if (m < paramInt) {
/* 206 */           i = k + 1;
/* 207 */         } else if (m > paramInt) {
/* 208 */           j = k - 1;
/*     */         } else {
/* 210 */           this.lastLine = (k + 1);
/* 211 */           return this.lastLine;
/*     */         }
/*     */       }
/* 214 */       this.lastLine = i;
/* 215 */       return this.lastLine;
/*     */     }
/*     */ 
/*     */     public long getLineNumber(long paramLong) {
/* 219 */       return getLineNumber(longToInt(paramLong));
/*     */     }
/*     */ 
/*     */     public int getColumnNumber(int paramInt) {
/* 223 */       return paramInt - this.startPosition[(getLineNumber(paramInt) - 1)] + 1;
/*     */     }
/*     */ 
/*     */     public long getColumnNumber(long paramLong) {
/* 227 */       return getColumnNumber(longToInt(paramLong));
/*     */     }
/*     */ 
/*     */     private static int longToInt(long paramLong) {
/* 231 */       int i = (int)paramLong;
/* 232 */       if (i != paramLong)
/* 233 */         throw new IndexOutOfBoundsException();
/* 234 */       return i;
/*     */     }
/*     */ 
/*     */     protected void setTabPosition(int paramInt)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class LineTabMapImpl extends Position.LineMapImpl
/*     */   {
/*     */     private BitSet tabMap;
/*     */ 
/*     */     public LineTabMapImpl(int paramInt)
/*     */     {
/* 249 */       this.tabMap = new BitSet(paramInt);
/*     */     }
/*     */ 
/*     */     protected void setTabPosition(int paramInt) {
/* 253 */       this.tabMap.set(paramInt);
/*     */     }
/*     */ 
/*     */     public int getColumnNumber(int paramInt) {
/* 257 */       int i = this.startPosition[(getLineNumber(paramInt) - 1)];
/* 258 */       int j = 0;
/* 259 */       for (int k = i; k < paramInt; k++) {
/* 260 */         if (this.tabMap.get(k))
/* 261 */           j = j / 8 * 8 + 8;
/*     */         else
/* 263 */           j++;
/*     */       }
/* 265 */       return j + 1;
/*     */     }
/*     */ 
/*     */     public int getPosition(int paramInt1, int paramInt2) {
/* 269 */       int i = this.startPosition[(paramInt1 - 1)];
/* 270 */       paramInt2--;
/* 271 */       int j = 0;
/* 272 */       while (j < paramInt2) {
/* 273 */         i++;
/* 274 */         if (this.tabMap.get(i))
/* 275 */           j = j / 8 * 8 + 8;
/*     */         else
/* 277 */           j++;
/*     */       }
/* 279 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Position
 * JD-Core Version:    0.6.2
 */