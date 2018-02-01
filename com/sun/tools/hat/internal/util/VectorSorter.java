/*     */ package com.sun.tools.hat.internal.util;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class VectorSorter
/*     */ {
/*     */   public static void sort(Vector<Object> paramVector, Comparer paramComparer)
/*     */   {
/*  60 */     quickSort(paramVector, paramComparer, 0, paramVector.size() - 1);
/*     */   }
/*     */ 
/*     */   public static void sortVectorOfStrings(Vector<Object> paramVector)
/*     */   {
/*  68 */     sort(paramVector, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  70 */         return ((String)paramAnonymousObject1).compareTo((String)paramAnonymousObject2);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static void swap(Vector<Object> paramVector, int paramInt1, int paramInt2)
/*     */   {
/*  77 */     Object localObject = paramVector.elementAt(paramInt1);
/*  78 */     paramVector.setElementAt(paramVector.elementAt(paramInt2), paramInt1);
/*  79 */     paramVector.setElementAt(localObject, paramInt2);
/*     */   }
/*     */ 
/*     */   private static void quickSort(Vector<Object> paramVector, Comparer paramComparer, int paramInt1, int paramInt2)
/*     */   {
/*  91 */     if (paramInt2 <= paramInt1)
/*  92 */       return;
/*  93 */     int i = (paramInt1 + paramInt2) / 2;
/*  94 */     if (i != paramInt1)
/*  95 */       swap(paramVector, i, paramInt1);
/*  96 */     Object localObject = paramVector.elementAt(paramInt1);
/*     */ 
/*  98 */     int j = paramInt1 - 1;
/*  99 */     int k = paramInt1 + 1;
/* 100 */     int m = paramInt2;
/*     */ 
/* 107 */     while (k <= m) {
/* 108 */       int n = paramComparer.compare(paramVector.elementAt(k), localObject);
/* 109 */       if (n <= 0) {
/* 110 */         if (n < 0) {
/* 111 */           j = k;
/*     */         }
/* 113 */         k++;
/*     */       } else {
/*     */         int i1;
/*     */         while (true) {
/* 117 */           i1 = paramComparer.compare(paramVector.elementAt(m), localObject);
/*     */ 
/* 119 */           if (i1 > 0) {
/* 120 */             m--;
/* 121 */             if (k > m) {
/* 122 */               break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 129 */         if (k <= m) {
/* 130 */           swap(paramVector, k, m);
/* 131 */           if (i1 < 0) {
/* 132 */             j = k;
/*     */           }
/* 134 */           k++;
/* 135 */           m--;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (j > paramInt1)
/*     */     {
/* 143 */       swap(paramVector, paramInt1, j);
/* 144 */       quickSort(paramVector, paramComparer, paramInt1, j - 1);
/*     */     }
/* 146 */     quickSort(paramVector, paramComparer, m + 1, paramInt2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.util.VectorSorter
 * JD-Core Version:    0.6.2
 */