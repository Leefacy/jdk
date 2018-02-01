/*     */ package sun.tools.native2ascii;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ 
/*     */ class A2NFilter extends FilterReader
/*     */ {
/*  45 */   private char[] trailChars = null;
/*     */ 
/*     */   public A2NFilter(Reader paramReader) {
/*  48 */     super(paramReader);
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException {
/*  52 */     int i = 0;
/*  53 */     int j = 0;
/*     */ 
/*  55 */     char[] arrayOfChar = new char[paramInt2];
/*  56 */     int k = 0;
/*  57 */     int m = 0;
/*     */ 
/*  60 */     if (this.trailChars != null) {
/*  61 */       for (n = 0; n < this.trailChars.length; n++)
/*  62 */         arrayOfChar[n] = this.trailChars[n];
/*  63 */       i = this.trailChars.length;
/*  64 */       this.trailChars = null;
/*     */     }
/*     */ 
/*  67 */     int n = this.in.read(arrayOfChar, i, paramInt2 - i);
/*  68 */     if (n < 0) {
/*  69 */       m = 1;
/*  70 */       if (i == 0)
/*  71 */         return -1;
/*     */     } else {
/*  73 */       i += n;
/*     */     }
/*     */ 
/*  76 */     for (int i1 = 0; i1 < i; ) {
/*  77 */       int i2 = arrayOfChar[(i1++)];
/*     */ 
/*  79 */       if ((i2 != 92) || ((m != 0) && (i <= 5)))
/*     */       {
/*  87 */         paramArrayOfChar[(j++)] = i2;
/*     */       }
/*     */       else
/*     */       {
/*  91 */         int i3 = i - i1;
/*     */         int i4;
/*  92 */         if (i3 < 5)
/*     */         {
/*  95 */           this.trailChars = new char[1 + i3];
/*  96 */           this.trailChars[0] = i2;
/*  97 */           for (i4 = 0; i4 < i3; i4++)
/*  98 */             this.trailChars[(1 + i4)] = arrayOfChar[(i1 + i4)];
/*  99 */           break;
/*     */         }
/*     */ 
/* 103 */         i2 = arrayOfChar[(i1++)];
/* 104 */         if (i2 != 117)
/*     */         {
/* 106 */           paramArrayOfChar[(j++)] = '\\';
/* 107 */           paramArrayOfChar[(j++)] = i2;
/*     */         }
/*     */         else
/*     */         {
/* 112 */           i4 = 0;
/* 113 */           int i5 = 1;
/*     */           try {
/* 115 */             i4 = (char)Integer.parseInt(new String(arrayOfChar, i1, 4), 16);
/*     */           } catch (NumberFormatException localNumberFormatException) {
/* 117 */             i5 = 0;
/*     */           }
/* 119 */           if ((i5 != 0) && (Main.canConvert(i4)))
/*     */           {
/* 121 */             paramArrayOfChar[(j++)] = i4;
/* 122 */             i1 += 4;
/*     */           }
/*     */           else {
/* 125 */             paramArrayOfChar[(j++)] = '\\';
/* 126 */             paramArrayOfChar[(j++)] = 'u';
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 132 */     return j;
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/* 136 */     char[] arrayOfChar = new char[1];
/*     */ 
/* 138 */     if (read(arrayOfChar, 0, 1) == -1) {
/* 139 */       return -1;
/*     */     }
/* 141 */     return arrayOfChar[0];
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.native2ascii.A2NFilter
 * JD-Core Version:    0.6.2
 */