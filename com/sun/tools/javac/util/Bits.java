/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class Bits
/*     */ {
/*     */   private static final int wordlen = 32;
/*     */   private static final int wordshift = 5;
/*     */   private static final int wordmask = 31;
/*  91 */   public int[] bits = null;
/*     */ 
/*  93 */   private static final int[] unassignedBits = new int[0];
/*     */   protected BitsState currentState;
/*     */ 
/*     */   public Bits()
/*     */   {
/* 100 */     this(false);
/*     */   }
/*     */ 
/*     */   public Bits(Bits paramBits) {
/* 104 */     this(paramBits.dup().bits, BitsState.getState(paramBits.bits, false));
/*     */   }
/*     */ 
/*     */   public Bits(boolean paramBoolean) {
/* 108 */     this(unassignedBits, BitsState.getState(unassignedBits, paramBoolean));
/*     */   }
/*     */ 
/*     */   protected Bits(int[] paramArrayOfInt, BitsState paramBitsState)
/*     */   {
/* 114 */     this.bits = paramArrayOfInt;
/* 115 */     this.currentState = paramBitsState;
/* 116 */     switch (1.$SwitchMap$com$sun$tools$javac$util$Bits$BitsState[paramBitsState.ordinal()]) {
/*     */     case 1:
/* 118 */       this.bits = null;
/* 119 */       break;
/*     */     case 2:
/* 121 */       Assert.check(paramArrayOfInt != unassignedBits);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void sizeTo(int paramInt)
/*     */   {
/* 127 */     if (this.bits.length < paramInt)
/* 128 */       this.bits = Arrays.copyOf(this.bits, paramInt);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 135 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 136 */     for (int i = 0; i < this.bits.length; i++) {
/* 137 */       this.bits[i] = 0;
/*     */     }
/* 139 */     this.currentState = BitsState.NORMAL;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 143 */     internalReset();
/*     */   }
/*     */ 
/*     */   protected void internalReset() {
/* 147 */     this.bits = null;
/* 148 */     this.currentState = BitsState.UNKNOWN;
/*     */   }
/*     */ 
/*     */   public boolean isReset() {
/* 152 */     return this.currentState == BitsState.UNKNOWN;
/*     */   }
/*     */ 
/*     */   public Bits assign(Bits paramBits) {
/* 156 */     this.bits = paramBits.dup().bits;
/* 157 */     this.currentState = BitsState.NORMAL;
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   public Bits dup()
/*     */   {
/* 164 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 165 */     Bits localBits = new Bits();
/* 166 */     localBits.bits = dupBits();
/* 167 */     this.currentState = BitsState.NORMAL;
/* 168 */     return localBits;
/*     */   }
/*     */ 
/*     */   protected int[] dupBits()
/*     */   {
/*     */     int[] arrayOfInt;
/* 173 */     if (this.currentState != BitsState.NORMAL) {
/* 174 */       arrayOfInt = this.bits;
/*     */     } else {
/* 176 */       arrayOfInt = new int[this.bits.length];
/* 177 */       System.arraycopy(this.bits, 0, arrayOfInt, 0, this.bits.length);
/*     */     }
/* 179 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void incl(int paramInt)
/*     */   {
/* 185 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 186 */     Assert.check(paramInt >= 0, "Value of x " + paramInt);
/* 187 */     sizeTo((paramInt >>> 5) + 1);
/* 188 */     this.bits[(paramInt >>> 5)] |= 1 << (paramInt & 0x1F);
/*     */ 
/* 190 */     this.currentState = BitsState.NORMAL;
/*     */   }
/*     */ 
/*     */   public void inclRange(int paramInt1, int paramInt2)
/*     */   {
/* 197 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 198 */     sizeTo((paramInt2 >>> 5) + 1);
/* 199 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 200 */       this.bits[(i >>> 5)] |= 1 << (i & 0x1F);
/*     */     }
/*     */ 
/* 203 */     this.currentState = BitsState.NORMAL;
/*     */   }
/*     */ 
/*     */   public void excludeFrom(int paramInt)
/*     */   {
/* 209 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 210 */     Bits localBits = new Bits();
/* 211 */     localBits.sizeTo(this.bits.length);
/* 212 */     localBits.inclRange(0, paramInt);
/* 213 */     internalAndSet(localBits);
/* 214 */     this.currentState = BitsState.NORMAL;
/*     */   }
/*     */ 
/*     */   public void excl(int paramInt)
/*     */   {
/* 220 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 221 */     Assert.check(paramInt >= 0);
/* 222 */     sizeTo((paramInt >>> 5) + 1);
/* 223 */     this.bits[(paramInt >>> 5)] &= (1 << (paramInt & 0x1F) ^ 0xFFFFFFFF);
/*     */ 
/* 225 */     this.currentState = BitsState.NORMAL;
/*     */   }
/*     */ 
/*     */   public boolean isMember(int paramInt)
/*     */   {
/* 231 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 232 */     return (0 <= paramInt) && (paramInt < this.bits.length << 5) && ((this.bits[(paramInt >>> 5)] & 1 << (paramInt & 0x1F)) != 0);
/*     */   }
/*     */ 
/*     */   public Bits andSet(Bits paramBits)
/*     */   {
/* 240 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 241 */     internalAndSet(paramBits);
/* 242 */     this.currentState = BitsState.NORMAL;
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */   protected void internalAndSet(Bits paramBits) {
/* 247 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 248 */     sizeTo(paramBits.bits.length);
/* 249 */     for (int i = 0; i < paramBits.bits.length; i++)
/* 250 */       this.bits[i] &= paramBits.bits[i];
/*     */   }
/*     */ 
/*     */   public Bits orSet(Bits paramBits)
/*     */   {
/* 257 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 258 */     sizeTo(paramBits.bits.length);
/* 259 */     for (int i = 0; i < paramBits.bits.length; i++) {
/* 260 */       this.bits[i] |= paramBits.bits[i];
/*     */     }
/* 262 */     this.currentState = BitsState.NORMAL;
/* 263 */     return this;
/*     */   }
/*     */ 
/*     */   public Bits diffSet(Bits paramBits)
/*     */   {
/* 269 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 270 */     for (int i = 0; i < this.bits.length; i++) {
/* 271 */       if (i < paramBits.bits.length) {
/* 272 */         this.bits[i] &= (paramBits.bits[i] ^ 0xFFFFFFFF);
/*     */       }
/*     */     }
/* 275 */     this.currentState = BitsState.NORMAL;
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   public Bits xorSet(Bits paramBits)
/*     */   {
/* 282 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 283 */     sizeTo(paramBits.bits.length);
/* 284 */     for (int i = 0; i < paramBits.bits.length; i++) {
/* 285 */       this.bits[i] ^= paramBits.bits[i];
/*     */     }
/* 287 */     this.currentState = BitsState.NORMAL;
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   private static int trailingZeroBits(int paramInt)
/*     */   {
/* 295 */     Assert.check(true);
/* 296 */     if (paramInt == 0) {
/* 297 */       return 32;
/*     */     }
/* 299 */     int i = 1;
/* 300 */     if ((paramInt & 0xFFFF) == 0) { i += 16; paramInt >>>= 16; }
/* 301 */     if ((paramInt & 0xFF) == 0) { i += 8; paramInt >>>= 8; }
/* 302 */     if ((paramInt & 0xF) == 0) { i += 4; paramInt >>>= 4; }
/* 303 */     if ((paramInt & 0x3) == 0) { i += 2; paramInt >>>= 2; }
/* 304 */     return i - (paramInt & 0x1);
/*     */   }
/*     */ 
/*     */   public int nextBit(int paramInt)
/*     */   {
/* 315 */     Assert.check(this.currentState != BitsState.UNKNOWN);
/* 316 */     int i = paramInt >>> 5;
/* 317 */     if (i >= this.bits.length) {
/* 318 */       return -1;
/*     */     }
/* 320 */     int j = this.bits[i] & ((1 << (paramInt & 0x1F)) - 1 ^ 0xFFFFFFFF);
/*     */     while (true) {
/* 322 */       if (j != 0) {
/* 323 */         return (i << 5) + trailingZeroBits(j);
/*     */       }
/* 325 */       i++;
/* 326 */       if (i >= this.bits.length) {
/* 327 */         return -1;
/*     */       }
/* 329 */       j = this.bits[i];
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 337 */     if ((this.bits != null) && (this.bits.length > 0)) {
/* 338 */       char[] arrayOfChar = new char[this.bits.length * 32];
/* 339 */       for (int i = 0; i < this.bits.length * 32; i++) {
/* 340 */         arrayOfChar[i] = (isMember(i) ? 49 : '0');
/*     */       }
/* 342 */       return new String(arrayOfChar);
/*     */     }
/* 344 */     return "[]";
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 350 */     Random localRandom = new Random();
/* 351 */     Bits localBits = new Bits();
/* 352 */     for (int i = 0; i < 125; i++)
/*     */     {
/*     */       do
/* 355 */         j = localRandom.nextInt(250);
/* 356 */       while (localBits.isMember(j));
/* 357 */       System.out.println("adding " + j);
/* 358 */       localBits.incl(j);
/*     */     }
/* 360 */     i = 0;
/* 361 */     for (int j = localBits.nextBit(0); j >= 0; j = localBits.nextBit(j + 1)) {
/* 362 */       System.out.println("found " + j);
/* 363 */       i++;
/*     */     }
/* 365 */     if (i != 125)
/* 366 */       throw new Error();
/*     */   }
/*     */ 
/*     */   protected static enum BitsState
/*     */   {
/*  60 */     UNKNOWN, 
/*     */ 
/*  65 */     UNINIT, 
/*     */ 
/*  71 */     NORMAL;
/*     */ 
/*     */     static BitsState getState(int[] paramArrayOfInt, boolean paramBoolean) {
/*  74 */       if (paramBoolean) {
/*  75 */         return UNKNOWN;
/*     */       }
/*  77 */       if (paramArrayOfInt != Bits.unassignedBits) {
/*  78 */         return NORMAL;
/*     */       }
/*  80 */       return UNINIT;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Bits
 * JD-Core Version:    0.6.2
 */