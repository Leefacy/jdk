/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.java.Constants;
/*     */ 
/*     */ public final class Vset
/*     */   implements Constants
/*     */ {
/*     */   long vset;
/*     */   long uset;
/*     */   long[] x;
/*  58 */   static final long[] emptyX = new long[0];
/*  59 */   static final long[] fullX = new long[0];
/*     */   static final int VBITS = 64;
/*  78 */   static final Vset DEAD_END = new Vset(-1L, -1L, fullX);
/*     */ 
/*     */   public Vset()
/*     */   {
/*  84 */     this.x = emptyX;
/*     */   }
/*     */ 
/*     */   private Vset(long paramLong1, long paramLong2, long[] paramArrayOfLong) {
/*  88 */     this.vset = paramLong1;
/*  89 */     this.uset = paramLong2;
/*  90 */     this.x = paramArrayOfLong;
/*     */   }
/*     */ 
/*     */   public Vset copy()
/*     */   {
/*  98 */     if (this == DEAD_END) {
/*  99 */       return this;
/*     */     }
/* 101 */     Vset localVset = new Vset(this.vset, this.uset, this.x);
/* 102 */     if (this.x.length > 0) {
/* 103 */       localVset.growX(this.x.length);
/*     */     }
/* 105 */     return localVset;
/*     */   }
/*     */ 
/*     */   private void growX(int paramInt) {
/* 109 */     long[] arrayOfLong1 = new long[paramInt];
/* 110 */     long[] arrayOfLong2 = this.x;
/* 111 */     for (int i = 0; i < arrayOfLong2.length; i++) {
/* 112 */       arrayOfLong1[i] = arrayOfLong2[i];
/*     */     }
/* 114 */     this.x = arrayOfLong1;
/*     */   }
/*     */ 
/*     */   public boolean isDeadEnd()
/*     */   {
/* 128 */     return this == DEAD_END;
/*     */   }
/*     */ 
/*     */   public boolean isReallyDeadEnd()
/*     */   {
/* 141 */     return this.x == fullX;
/*     */   }
/*     */ 
/*     */   public Vset clearDeadEnd()
/*     */   {
/* 155 */     if (this == DEAD_END) {
/* 156 */       return new Vset(-1L, -1L, fullX);
/*     */     }
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean testVar(int paramInt)
/*     */   {
/* 165 */     long l = 1L << paramInt;
/* 166 */     if (paramInt >= 64) {
/* 167 */       int i = (paramInt / 64 - 1) * 2;
/* 168 */       if (i >= this.x.length) {
/* 169 */         return this.x == fullX;
/*     */       }
/* 171 */       return (this.x[i] & l) != 0L;
/*     */     }
/* 173 */     return (this.vset & l) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean testVarUnassigned(int paramInt)
/*     */   {
/* 183 */     long l = 1L << paramInt;
/* 184 */     if (paramInt >= 64)
/*     */     {
/* 186 */       int i = (paramInt / 64 - 1) * 2 + 1;
/* 187 */       if (i >= this.x.length) {
/* 188 */         return this.x == fullX;
/*     */       }
/* 190 */       return (this.x[i] & l) != 0L;
/*     */     }
/* 192 */     return (this.uset & l) != 0L;
/*     */   }
/*     */ 
/*     */   public Vset addVar(int paramInt)
/*     */   {
/* 201 */     if (this.x == fullX) {
/* 202 */       return this;
/*     */     }
/*     */ 
/* 207 */     long l = 1L << paramInt;
/* 208 */     if (paramInt >= 64) {
/* 209 */       int i = (paramInt / 64 - 1) * 2;
/* 210 */       if (i >= this.x.length) {
/* 211 */         growX(i + 1);
/*     */       }
/* 213 */       this.x[i] |= l;
/* 214 */       if (i + 1 < this.x.length)
/* 215 */         this.x[(i + 1)] &= (l ^ 0xFFFFFFFF);
/*     */     }
/*     */     else {
/* 218 */       this.vset |= l;
/* 219 */       this.uset &= (l ^ 0xFFFFFFFF);
/*     */     }
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset addVarUnassigned(int paramInt)
/*     */   {
/* 229 */     if (this.x == fullX) {
/* 230 */       return this;
/*     */     }
/*     */ 
/* 235 */     long l = 1L << paramInt;
/* 236 */     if (paramInt >= 64)
/*     */     {
/* 238 */       int i = (paramInt / 64 - 1) * 2 + 1;
/* 239 */       if (i >= this.x.length) {
/* 240 */         growX(i + 1);
/*     */       }
/* 242 */       this.x[i] |= l;
/* 243 */       this.x[(i - 1)] &= (l ^ 0xFFFFFFFF);
/*     */     } else {
/* 245 */       this.uset |= l;
/* 246 */       this.vset &= (l ^ 0xFFFFFFFF);
/*     */     }
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset clearVar(int paramInt)
/*     */   {
/* 257 */     if (this.x == fullX) {
/* 258 */       return this;
/*     */     }
/* 260 */     long l = 1L << paramInt;
/* 261 */     if (paramInt >= 64) {
/* 262 */       int i = (paramInt / 64 - 1) * 2;
/* 263 */       if (i >= this.x.length) {
/* 264 */         return this;
/*     */       }
/* 266 */       this.x[i] &= (l ^ 0xFFFFFFFF);
/* 267 */       if (i + 1 < this.x.length)
/* 268 */         this.x[(i + 1)] &= (l ^ 0xFFFFFFFF);
/*     */     }
/*     */     else {
/* 271 */       this.vset &= (l ^ 0xFFFFFFFF);
/* 272 */       this.uset &= (l ^ 0xFFFFFFFF);
/*     */     }
/* 274 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset join(Vset paramVset)
/*     */   {
/* 288 */     if (this == DEAD_END) {
/* 289 */       return paramVset.copy();
/*     */     }
/* 291 */     if (paramVset == DEAD_END) {
/* 292 */       return this;
/*     */     }
/* 294 */     if (this.x == fullX) {
/* 295 */       return paramVset.copy();
/*     */     }
/* 297 */     if (paramVset.x == fullX) {
/* 298 */       return this;
/*     */     }
/*     */ 
/* 304 */     this.vset &= paramVset.vset;
/* 305 */     this.uset &= paramVset.uset;
/*     */ 
/* 307 */     if (paramVset.x == emptyX) {
/* 308 */       this.x = emptyX;
/*     */     }
/*     */     else {
/* 311 */       long[] arrayOfLong = paramVset.x;
/* 312 */       int i = this.x.length;
/* 313 */       int j = arrayOfLong.length < i ? arrayOfLong.length : i;
/* 314 */       for (int k = 0; k < j; k++) {
/* 315 */         this.x[k] &= arrayOfLong[k];
/*     */       }
/*     */ 
/* 320 */       for (k = j; k < i; k++) {
/* 321 */         this.x[k] = 0L;
/*     */       }
/*     */     }
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset addDAandJoinDU(Vset paramVset)
/*     */   {
/* 340 */     if (this == DEAD_END) {
/* 341 */       return this;
/*     */     }
/* 343 */     if (paramVset == DEAD_END) {
/* 344 */       return paramVset;
/*     */     }
/* 346 */     if (this.x == fullX) {
/* 347 */       return this;
/*     */     }
/* 349 */     if (paramVset.x == fullX) {
/* 350 */       return paramVset.copy();
/*     */     }
/*     */ 
/* 356 */     this.vset |= paramVset.vset;
/* 357 */     this.uset = (this.uset & paramVset.uset & (paramVset.vset ^ 0xFFFFFFFF));
/*     */ 
/* 359 */     int i = this.x.length;
/* 360 */     long[] arrayOfLong = paramVset.x;
/* 361 */     int j = arrayOfLong.length;
/*     */ 
/* 363 */     if (arrayOfLong != emptyX)
/*     */     {
/* 365 */       if (j > i) {
/* 366 */         growX(j);
/*     */       }
/* 368 */       k = 0;
/* 369 */       while (k < j) {
/* 370 */         this.x[k] |= arrayOfLong[k];
/* 371 */         k++;
/* 372 */         if (k == j) break;
/* 373 */         this.x[k] = (this.x[k] & arrayOfLong[k] & (arrayOfLong[(k - 1)] ^ 0xFFFFFFFF));
/* 374 */         k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 382 */     for (int k = j | 0x1; k < i; k += 2) {
/* 383 */       this.x[k] = 0L;
/*     */     }
/* 385 */     return this;
/*     */   }
/*     */ 
/*     */   public static Vset firstDAandSecondDU(Vset paramVset1, Vset paramVset2)
/*     */   {
/* 404 */     if (paramVset1.x == fullX) {
/* 405 */       return paramVset1.copy();
/*     */     }
/*     */ 
/* 408 */     long[] arrayOfLong1 = paramVset1.x;
/* 409 */     int i = arrayOfLong1.length;
/* 410 */     long[] arrayOfLong2 = paramVset2.x;
/* 411 */     int j = arrayOfLong2.length;
/* 412 */     int k = i > j ? i : j;
/* 413 */     long[] arrayOfLong3 = emptyX;
/*     */ 
/* 415 */     if (k > 0) {
/* 416 */       arrayOfLong3 = new long[k];
/* 417 */       for (int m = 0; m < i; m += 2) {
/* 418 */         arrayOfLong3[m] = arrayOfLong1[m];
/*     */       }
/* 420 */       for (m = 1; m < j; m += 2) {
/* 421 */         arrayOfLong3[m] = arrayOfLong2[m];
/*     */       }
/*     */     }
/*     */ 
/* 425 */     return new Vset(paramVset1.vset, paramVset2.uset, arrayOfLong3);
/*     */   }
/*     */ 
/*     */   public Vset removeAdditionalVars(int paramInt)
/*     */   {
/* 436 */     if (this.x == fullX) {
/* 437 */       return this;
/*     */     }
/* 439 */     long l = 1L << paramInt;
/* 440 */     if (paramInt >= 64) {
/* 441 */       int i = (paramInt / 64 - 1) * 2;
/* 442 */       if (i < this.x.length) {
/* 443 */         this.x[i] &= l - 1L;
/* 444 */         i++; if (i < this.x.length)
/* 445 */           this.x[i] &= l - 1L;
/*     */         while (true) {
/* 447 */           i++; if (i >= this.x.length) break;
/* 448 */           this.x[i] = 0L;
/*     */         }
/*     */       }
/*     */     } else {
/* 452 */       if (this.x.length > 0) {
/* 453 */         this.x = emptyX;
/*     */       }
/* 455 */       this.vset &= l - 1L;
/* 456 */       this.uset &= l - 1L;
/*     */     }
/* 458 */     return this;
/*     */   }
/*     */ 
/*     */   public int varLimit()
/*     */   {
/*     */     int i;
/* 468 */     for (int j = this.x.length / 2 * 2; j >= 0; j -= 2)
/* 469 */       if (j != this.x.length) {
/* 470 */         l = this.x[j];
/* 471 */         if (j + 1 < this.x.length) {
/* 472 */           l |= this.x[(j + 1)];
/*     */         }
/* 474 */         if (l != 0L) {
/* 475 */           i = (j / 2 + 1) * 64;
/* 476 */           break label111;
/*     */         }
/*     */       }
/* 479 */     long l = this.vset;
/* 480 */     l |= this.uset;
/* 481 */     if (l != 0L) {
/* 482 */       i = 0;
/*     */     }
/*     */     else {
/* 485 */       return 0;
/*     */     }
/*     */ 
/* 488 */     label111: while (l != 0L) {
/* 489 */       i++;
/* 490 */       l >>>= 1;
/*     */     }
/* 492 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 496 */     if (this == DEAD_END)
/* 497 */       return "{DEAD_END}";
/* 498 */     StringBuffer localStringBuffer = new StringBuffer("{");
/* 499 */     int i = 64 * (1 + (this.x.length + 1) / 2);
/* 500 */     for (int j = 0; j < i; j++) {
/* 501 */       if (!testVarUnassigned(j)) {
/* 502 */         if (localStringBuffer.length() > 1) {
/* 503 */           localStringBuffer.append(' ');
/*     */         }
/* 505 */         localStringBuffer.append(j);
/* 506 */         if (!testVar(j)) {
/* 507 */           localStringBuffer.append('?');
/*     */         }
/*     */       }
/*     */     }
/* 511 */     if (this.x == fullX) {
/* 512 */       localStringBuffer.append("...DEAD_END");
/*     */     }
/* 514 */     localStringBuffer.append('}');
/* 515 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.Vset
 * JD-Core Version:    0.6.2
 */