/*     */ package com.sun.tools.hat.internal.util;
/*     */ 
/*     */ public class ArraySorter
/*     */ {
/*     */   public static void sort(Object[] paramArrayOfObject, Comparer paramComparer)
/*     */   {
/*  59 */     quickSort(paramArrayOfObject, paramComparer, 0, paramArrayOfObject.length - 1);
/*     */   }
/*     */ 
/*     */   public static void sortArrayOfStrings(Object[] paramArrayOfObject)
/*     */   {
/*  67 */     sort(paramArrayOfObject, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  69 */         return ((String)paramAnonymousObject1).compareTo((String)paramAnonymousObject2);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static void swap(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*     */   {
/*  76 */     Object localObject = paramArrayOfObject[paramInt1];
/*  77 */     paramArrayOfObject[paramInt1] = paramArrayOfObject[paramInt2];
/*  78 */     paramArrayOfObject[paramInt2] = localObject;
/*     */   }
/*     */ 
/*     */   private static void quickSort(Object[] paramArrayOfObject, Comparer paramComparer, int paramInt1, int paramInt2)
/*     */   {
/*  90 */     if (paramInt2 <= paramInt1)
/*  91 */       return;
/*  92 */     int i = (paramInt1 + paramInt2) / 2;
/*  93 */     if (i != paramInt1)
/*  94 */       swap(paramArrayOfObject, i, paramInt1);
/*  95 */     Object localObject = paramArrayOfObject[paramInt1];
/*  96 */     int j = paramInt1 - 1;
/*  97 */     int k = paramInt1 + 1;
/*  98 */     int m = paramInt2;
/*     */ 
/* 105 */     while (k <= m) {
/* 106 */       int n = paramComparer.compare(paramArrayOfObject[k], localObject);
/* 107 */       if (n <= 0) {
/* 108 */         if (n < 0) {
/* 109 */           j = k;
/*     */         }
/* 111 */         k++;
/*     */       }
/*     */       else {
/*     */         int i1;
/*     */         while (true) {
/* 116 */           i1 = paramComparer.compare(paramArrayOfObject[m], localObject);
/* 117 */           if (i1 > 0) {
/* 118 */             m--;
/* 119 */             if (k > m) {
/* 120 */               break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 127 */         if (k <= m) {
/* 128 */           swap(paramArrayOfObject, k, m);
/* 129 */           if (i1 < 0) {
/* 130 */             j = k;
/*     */           }
/* 132 */           k++;
/* 133 */           m--;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 140 */     if (j > paramInt1)
/*     */     {
/* 142 */       swap(paramArrayOfObject, paramInt1, j);
/* 143 */       quickSort(paramArrayOfObject, paramComparer, paramInt1, j - 1);
/*     */     }
/* 145 */     quickSort(paramArrayOfObject, paramComparer, m + 1, paramInt2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.util.ArraySorter
 * JD-Core Version:    0.6.2
 */