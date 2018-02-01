/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class StaticStringsHash
/*     */ {
/*  63 */   public String[] strings = null;
/*     */ 
/*  66 */   public int[] keys = null;
/*     */ 
/*  70 */   public int[][] buckets = (int[][])null;
/*     */ 
/*  73 */   public String method = null;
/*     */   private int length;
/*     */   private int[] tempKeys;
/*     */   private int[] bucketSizes;
/*     */   private int bucketCount;
/*     */   private int maxDepth;
/* 273 */   private int minStringLength = 2147483647;
/*     */   private int keyKind;
/*     */   private int charAt;
/*     */   private static final int LENGTH = 0;
/*     */   private static final int CHAR_AT = 1;
/*     */   private static final int HASH_CODE = 2;
/*     */   private static final int CHAR_AT_MAX_LINES = 50;
/*     */   private static final int CHAR_AT_MAX_CHARS = 1000;
/*     */ 
/*     */   public int getKey(String paramString)
/*     */   {
/*  81 */     switch (this.keyKind) { case 0:
/*  82 */       return paramString.length();
/*     */     case 1:
/*  83 */       return paramString.charAt(this.charAt);
/*     */     case 2:
/*  84 */       return paramString.hashCode();
/*     */     }
/*  86 */     throw new Error("Bad keyKind");
/*     */   }
/*     */ 
/*     */   public StaticStringsHash(String[] paramArrayOfString)
/*     */   {
/*  95 */     this.strings = paramArrayOfString;
/*  96 */     this.length = paramArrayOfString.length;
/*  97 */     this.tempKeys = new int[this.length];
/*  98 */     this.bucketSizes = new int[this.length];
/*  99 */     setMinStringLength();
/*     */ 
/* 105 */     int i = getKeys(0);
/* 106 */     int j = -1;
/* 107 */     int k = 0;
/*     */     int m;
/* 109 */     if (i > 1)
/*     */     {
/* 120 */       m = this.minStringLength;
/* 121 */       if ((this.length > 50) && (this.length * m > 1000))
/*     */       {
/* 123 */         m = this.length / 1000;
/*     */       }
/*     */ 
/* 126 */       this.charAt = 0;
/* 127 */       for (n = 0; n < m; n++) {
/* 128 */         i1 = getKeys(1);
/* 129 */         if (i1 < i) {
/* 130 */           i = i1;
/* 131 */           j = n;
/* 132 */           if (i == 1) {
/*     */             break;
/*     */           }
/*     */         }
/* 136 */         this.charAt += 1;
/*     */       }
/* 138 */       this.charAt = j;
/*     */ 
/* 141 */       if (i > 1)
/*     */       {
/* 153 */         n = getKeys(2);
/* 154 */         if (n < i - 3)
/*     */         {
/* 166 */           k = 1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 172 */       if (k == 0) {
/* 173 */         if (j >= 0)
/*     */         {
/* 177 */           getKeys(1);
/*     */         }
/*     */         else
/*     */         {
/* 183 */           getKeys(0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 190 */     this.keys = new int[this.bucketCount];
/* 191 */     System.arraycopy(this.tempKeys, 0, this.keys, 0, this.bucketCount);
/*     */     do
/*     */     {
/* 197 */       m = 0;
/* 198 */       for (n = 0; n < this.bucketCount - 1; n++) {
/* 199 */         if (this.keys[n] > this.keys[(n + 1)]) {
/* 200 */           i1 = this.keys[n];
/* 201 */           this.keys[n] = this.keys[(n + 1)];
/* 202 */           this.keys[(n + 1)] = i1;
/* 203 */           i1 = this.bucketSizes[n];
/* 204 */           this.bucketSizes[n] = this.bucketSizes[(n + 1)];
/* 205 */           this.bucketSizes[(n + 1)] = i1;
/* 206 */           m = 1;
/*     */         }
/*     */       }
/*     */     }
/* 210 */     while (m == 1);
/*     */ 
/* 216 */     int n = findUnusedKey();
/* 217 */     this.buckets = new int[this.bucketCount][];
/*     */     int i2;
/* 218 */     for (int i1 = 0; i1 < this.bucketCount; i1++) {
/* 219 */       this.buckets[i1] = new int[this.bucketSizes[i1]];
/* 220 */       for (i2 = 0; i2 < this.bucketSizes[i1]; i2++) {
/* 221 */         this.buckets[i1][i2] = n;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 227 */     for (i1 = 0; i1 < paramArrayOfString.length; i1++) {
/* 228 */       i2 = getKey(paramArrayOfString[i1]);
/* 229 */       for (int i3 = 0; i3 < this.bucketCount; i3++)
/* 230 */         if (this.keys[i3] == i2) {
/* 231 */           int i4 = 0;
/* 232 */           while (this.buckets[i3][i4] != n) {
/* 233 */             i4++;
/*     */           }
/* 235 */           this.buckets[i3][i4] = i1;
/* 236 */           break;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 246 */     StaticStringsHash localStaticStringsHash = new StaticStringsHash(paramArrayOfString);
/* 247 */     System.out.println();
/* 248 */     System.out.println("    public boolean contains(String key) {");
/* 249 */     System.out.println("        switch (key." + localStaticStringsHash.method + ") {");
/* 250 */     for (int i = 0; i < localStaticStringsHash.buckets.length; i++) {
/* 251 */       System.out.println("            case " + localStaticStringsHash.keys[i] + ": ");
/* 252 */       for (int j = 0; j < localStaticStringsHash.buckets[i].length; j++) {
/* 253 */         if (j > 0)
/* 254 */           System.out.print("                } else ");
/*     */         else {
/* 256 */           System.out.print("                ");
/*     */         }
/* 258 */         System.out.println("if (key.equals(\"" + localStaticStringsHash.strings[localStaticStringsHash.buckets[i][j]] + "\")) {");
/* 259 */         System.out.println("                    return true;");
/*     */       }
/* 261 */       System.out.println("                }");
/*     */     }
/* 263 */     System.out.println("        }");
/* 264 */     System.out.println("        return false;");
/* 265 */     System.out.println("    }");
/*     */   }
/*     */ 
/*     */   private void resetKeys(int paramInt)
/*     */   {
/* 294 */     this.keyKind = paramInt;
/* 295 */     switch (paramInt) { case 0:
/* 296 */       this.method = "length()"; break;
/*     */     case 1:
/* 297 */       this.method = ("charAt(" + this.charAt + ")"); break;
/*     */     case 2:
/* 298 */       this.method = "hashCode()";
/*     */     }
/* 300 */     this.maxDepth = 1;
/* 301 */     this.bucketCount = 0;
/* 302 */     for (int i = 0; i < this.length; i++) {
/* 303 */       this.tempKeys[i] = 0;
/* 304 */       this.bucketSizes[i] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setMinStringLength() {
/* 309 */     for (int i = 0; i < this.length; i++)
/* 310 */       if (this.strings[i].length() < this.minStringLength)
/* 311 */         this.minStringLength = this.strings[i].length();
/*     */   }
/*     */ 
/*     */   private int findUnusedKey()
/*     */   {
/* 317 */     int i = 0;
/* 318 */     int j = this.keys.length;
/*     */     while (true)
/*     */     {
/* 326 */       int k = 0;
/* 327 */       for (int m = 0; m < j; m++) {
/* 328 */         if (this.keys[m] == i) {
/* 329 */           k = 1;
/* 330 */           break;
/*     */         }
/*     */       }
/* 333 */       if (k == 0) break;
/* 334 */       i--;
/*     */     }
/*     */ 
/* 339 */     return i;
/*     */   }
/*     */ 
/*     */   private int getKeys(int paramInt) {
/* 343 */     resetKeys(paramInt);
/* 344 */     for (int i = 0; i < this.strings.length; i++) {
/* 345 */       addKey(getKey(this.strings[i]));
/*     */     }
/* 347 */     return this.maxDepth;
/*     */   }
/*     */ 
/*     */   private void addKey(int paramInt)
/*     */   {
/* 354 */     int i = 1;
/* 355 */     for (int j = 0; j < this.bucketCount; j++) {
/* 356 */       if (this.tempKeys[j] == paramInt) {
/* 357 */         i = 0;
/* 358 */         this.bucketSizes[j] += 1;
/* 359 */         if (this.bucketSizes[j] <= this.maxDepth) break;
/* 360 */         this.maxDepth = this.bucketSizes[j]; break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 366 */     if (i != 0) {
/* 367 */       this.tempKeys[this.bucketCount] = paramInt;
/* 368 */       this.bucketSizes[this.bucketCount] = 1;
/* 369 */       this.bucketCount += 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.StaticStringsHash
 * JD-Core Version:    0.6.2
 */