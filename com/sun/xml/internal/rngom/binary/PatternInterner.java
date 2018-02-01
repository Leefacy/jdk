/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ final class PatternInterner
/*     */ {
/*     */   private static final int INIT_SIZE = 256;
/*     */   private static final float LOAD_FACTOR = 0.3F;
/*     */   private Pattern[] table;
/*     */   private int used;
/*     */   private int usedLimit;
/*     */ 
/*     */   PatternInterner()
/*     */   {
/*  56 */     this.table = null;
/*  57 */     this.used = 0;
/*  58 */     this.usedLimit = 0;
/*     */   }
/*     */ 
/*     */   PatternInterner(PatternInterner parent) {
/*  62 */     this.table = parent.table;
/*  63 */     if (this.table != null)
/*  64 */       this.table = ((Pattern[])this.table.clone());
/*  65 */     this.used = parent.used;
/*  66 */     this.usedLimit = parent.usedLimit;
/*     */   }
/*     */ 
/*     */   Pattern intern(Pattern p)
/*     */   {
/*     */     int h;
/*     */     int h;
/*  73 */     if (this.table == null) {
/*  74 */       this.table = new Pattern[256];
/*  75 */       this.usedLimit = 76;
/*  76 */       h = firstIndex(p);
/*     */     } else {
/*  78 */       for (h = firstIndex(p); this.table[h] != null; h = nextIndex(h)) {
/*  79 */         if (p.samePattern(this.table[h]))
/*  80 */           return this.table[h];
/*     */       }
/*     */     }
/*  83 */     if (this.used >= this.usedLimit)
/*     */     {
/*  85 */       Pattern[] oldTable = this.table;
/*  86 */       this.table = new Pattern[this.table.length << 1];
/*  87 */       for (int i = oldTable.length; i > 0; ) {
/*  88 */         i--;
/*  89 */         if (oldTable[i] != null)
/*     */         {
/*  91 */           int j = firstIndex(oldTable[i]);
/*  92 */           while (this.table[j] != null)
/*  93 */             j = nextIndex(j);
/*  94 */           this.table[j] = oldTable[i];
/*     */         }
/*     */       }
/*  97 */       for (h = firstIndex(p); this.table[h] != null; h = nextIndex(h));
/*  98 */       this.usedLimit = ((int)(this.table.length * 0.3F));
/*     */     }
/* 100 */     this.used += 1;
/* 101 */     this.table[h] = p;
/* 102 */     return p;
/*     */   }
/*     */ 
/*     */   private int firstIndex(Pattern p) {
/* 106 */     return p.patternHashCode() & this.table.length - 1;
/*     */   }
/*     */ 
/*     */   private int nextIndex(int i) {
/* 110 */     return i == 0 ? this.table.length - 1 : i - 1;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.PatternInterner
 * JD-Core Version:    0.6.2
 */