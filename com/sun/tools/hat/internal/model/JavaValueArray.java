/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class JavaValueArray extends JavaLazyReadObject
/*     */   implements ArrayTypeCodes
/*     */ {
/*     */   private JavaClass clazz;
/*     */   private int data;
/*     */   private static final int SIGNATURE_MASK = 255;
/*     */   private static final int LENGTH_DIVIDER_MASK = 65280;
/*     */   private static final int LENGTH_DIVIDER_SHIFT = 8;
/*     */ 
/*     */   private static String arrayTypeName(byte paramByte)
/*     */   {
/*  47 */     switch (paramByte) {
/*     */     case 66:
/*  49 */       return "byte[]";
/*     */     case 90:
/*  51 */       return "boolean[]";
/*     */     case 67:
/*  53 */       return "char[]";
/*     */     case 83:
/*  55 */       return "short[]";
/*     */     case 73:
/*  57 */       return "int[]";
/*     */     case 70:
/*  59 */       return "float[]";
/*     */     case 74:
/*  61 */       return "long[]";
/*     */     case 68:
/*  63 */       return "double[]";
/*     */     case 69:
/*     */     case 71:
/*     */     case 72:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/*     */     case 78:
/*     */     case 79:
/*     */     case 80:
/*     */     case 81:
/*     */     case 82:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 87:
/*     */     case 88:
/*  65 */     case 89: } throw new RuntimeException("invalid array element sig: " + paramByte);
/*     */   }
/*     */ 
/*     */   private static int elementSize(byte paramByte)
/*     */   {
/*  70 */     switch (paramByte) {
/*     */     case 4:
/*     */     case 8:
/*  73 */       return 1;
/*     */     case 5:
/*     */     case 9:
/*  76 */       return 2;
/*     */     case 6:
/*     */     case 10:
/*  79 */       return 4;
/*     */     case 7:
/*     */     case 11:
/*  82 */       return 8;
/*     */     }
/*  84 */     throw new RuntimeException("invalid array element type: " + paramByte);
/*     */   }
/*     */ 
/*     */   protected final int readValueLength()
/*     */     throws IOException
/*     */   {
/*  99 */     JavaClass localJavaClass = getClazz();
/* 100 */     ReadBuffer localReadBuffer = localJavaClass.getReadBuffer();
/* 101 */     int i = localJavaClass.getIdentifierSize();
/* 102 */     long l = getOffset() + i + 4L;
/*     */ 
/* 104 */     int j = localReadBuffer.getInt(l);
/*     */ 
/* 106 */     byte b = localReadBuffer.getByte(l + 4L);
/* 107 */     return j * elementSize(b);
/*     */   }
/*     */ 
/*     */   protected final byte[] readValue() throws IOException {
/* 111 */     JavaClass localJavaClass = getClazz();
/* 112 */     ReadBuffer localReadBuffer = localJavaClass.getReadBuffer();
/* 113 */     int i = localJavaClass.getIdentifierSize();
/* 114 */     long l = getOffset() + i + 4L;
/*     */ 
/* 116 */     int j = localReadBuffer.getInt(l);
/*     */ 
/* 118 */     byte b = localReadBuffer.getByte(l + 4L);
/* 119 */     if (j == 0) {
/* 120 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     }
/* 122 */     j *= elementSize(b);
/* 123 */     byte[] arrayOfByte = new byte[j];
/* 124 */     localReadBuffer.get(l + 5L, arrayOfByte);
/* 125 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public JavaValueArray(byte paramByte, long paramLong)
/*     */   {
/* 148 */     super(paramLong);
/* 149 */     this.data = (paramByte & 0xFF);
/*     */   }
/*     */ 
/*     */   public JavaClass getClazz() {
/* 153 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 157 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/*     */   }
/*     */ 
/*     */   public void resolve(Snapshot paramSnapshot) {
/* 161 */     if ((this.clazz instanceof JavaClass)) {
/* 162 */       return;
/*     */     }
/* 164 */     byte b = getElementType();
/* 165 */     this.clazz = paramSnapshot.findClass(arrayTypeName(b));
/* 166 */     if (this.clazz == null) {
/* 167 */       this.clazz = paramSnapshot.getArrayClass("" + (char)b);
/*     */     }
/* 169 */     getClazz().addInstance(this);
/* 170 */     super.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 174 */     int i = (this.data & 0xFF00) >>> 8;
/* 175 */     if (i == 0) {
/* 176 */       int j = getElementType();
/* 177 */       switch (j) {
/*     */       case 66:
/*     */       case 90:
/* 180 */         i = 1;
/* 181 */         break;
/*     */       case 67:
/*     */       case 83:
/* 184 */         i = 2;
/* 185 */         break;
/*     */       case 70:
/*     */       case 73:
/* 188 */         i = 4;
/* 189 */         break;
/*     */       case 68:
/*     */       case 74:
/* 192 */         i = 8;
/* 193 */         break;
/*     */       case 69:
/*     */       case 71:
/*     */       case 72:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*     */       case 88:
/*     */       case 89:
/*     */       default:
/* 195 */         throw new RuntimeException("unknown primitive type: " + j);
/*     */       }
/*     */ 
/* 198 */       this.data |= i << 8;
/*     */     }
/* 200 */     return getValueLength() / i;
/*     */   }
/*     */ 
/*     */   public Object getElements() {
/* 204 */     int i = getLength();
/* 205 */     int j = getElementType();
/* 206 */     byte[] arrayOfByte = getValue();
/* 207 */     int k = 0;
/*     */     Object localObject;
/*     */     int m;
/* 208 */     switch (j) {
/*     */     case 90:
/* 210 */       localObject = new boolean[i];
/* 211 */       for (m = 0; m < i; m++) {
/* 212 */         localObject[m] = booleanAt(k, arrayOfByte);
/* 213 */         k++;
/*     */       }
/* 215 */       return localObject;
/*     */     case 66:
/* 218 */       localObject = new byte[i];
/* 219 */       for (m = 0; m < i; m++) {
/* 220 */         localObject[m] = byteAt(k, arrayOfByte);
/* 221 */         k++;
/*     */       }
/* 223 */       return localObject;
/*     */     case 67:
/* 226 */       localObject = new char[i];
/* 227 */       for (m = 0; m < i; m++) {
/* 228 */         localObject[m] = charAt(k, arrayOfByte);
/* 229 */         k += 2;
/*     */       }
/* 231 */       return localObject;
/*     */     case 83:
/* 234 */       localObject = new short[i];
/* 235 */       for (m = 0; m < i; m++) {
/* 236 */         localObject[m] = shortAt(k, arrayOfByte);
/* 237 */         k += 2;
/*     */       }
/* 239 */       return localObject;
/*     */     case 73:
/* 242 */       localObject = new int[i];
/* 243 */       for (m = 0; m < i; m++) {
/* 244 */         localObject[m] = intAt(k, arrayOfByte);
/* 245 */         k += 4;
/*     */       }
/* 247 */       return localObject;
/*     */     case 74:
/* 250 */       localObject = new long[i];
/* 251 */       for (m = 0; m < i; m++) {
/* 252 */         localObject[m] = longAt(k, arrayOfByte);
/* 253 */         k += 8;
/*     */       }
/* 255 */       return localObject;
/*     */     case 70:
/* 258 */       localObject = new float[i];
/* 259 */       for (m = 0; m < i; m++) {
/* 260 */         localObject[m] = floatAt(k, arrayOfByte);
/* 261 */         k += 4;
/*     */       }
/* 263 */       return localObject;
/*     */     case 68:
/* 266 */       localObject = new double[i];
/* 267 */       for (m = 0; m < i; m++) {
/* 268 */         localObject[m] = doubleAt(k, arrayOfByte);
/* 269 */         k += 8;
/*     */       }
/* 271 */       return localObject;
/*     */     case 69:
/*     */     case 71:
/*     */     case 72:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/*     */     case 78:
/*     */     case 79:
/*     */     case 80:
/*     */     case 81:
/*     */     case 82:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 87:
/*     */     case 88:
/* 274 */     case 89: } throw new RuntimeException("unknown primitive type?");
/*     */   }
/*     */ 
/*     */   public byte getElementType()
/*     */   {
/* 280 */     return (byte)(this.data & 0xFF);
/*     */   }
/*     */ 
/*     */   private void checkIndex(int paramInt) {
/* 284 */     if ((paramInt < 0) || (paramInt >= getLength()))
/* 285 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*     */   }
/*     */ 
/*     */   private void requireType(char paramChar)
/*     */   {
/* 290 */     if (getElementType() != paramChar)
/* 291 */       throw new RuntimeException("not of type : " + paramChar);
/*     */   }
/*     */ 
/*     */   public boolean getBooleanAt(int paramInt)
/*     */   {
/* 296 */     checkIndex(paramInt);
/* 297 */     requireType('Z');
/* 298 */     return booleanAt(paramInt, getValue());
/*     */   }
/*     */ 
/*     */   public byte getByteAt(int paramInt) {
/* 302 */     checkIndex(paramInt);
/* 303 */     requireType('B');
/* 304 */     return byteAt(paramInt, getValue());
/*     */   }
/*     */ 
/*     */   public char getCharAt(int paramInt) {
/* 308 */     checkIndex(paramInt);
/* 309 */     requireType('C');
/* 310 */     return charAt(paramInt << 1, getValue());
/*     */   }
/*     */ 
/*     */   public short getShortAt(int paramInt) {
/* 314 */     checkIndex(paramInt);
/* 315 */     requireType('S');
/* 316 */     return shortAt(paramInt << 1, getValue());
/*     */   }
/*     */ 
/*     */   public int getIntAt(int paramInt) {
/* 320 */     checkIndex(paramInt);
/* 321 */     requireType('I');
/* 322 */     return intAt(paramInt << 2, getValue());
/*     */   }
/*     */ 
/*     */   public long getLongAt(int paramInt) {
/* 326 */     checkIndex(paramInt);
/* 327 */     requireType('J');
/* 328 */     return longAt(paramInt << 3, getValue());
/*     */   }
/*     */ 
/*     */   public float getFloatAt(int paramInt) {
/* 332 */     checkIndex(paramInt);
/* 333 */     requireType('F');
/* 334 */     return floatAt(paramInt << 2, getValue());
/*     */   }
/*     */ 
/*     */   public double getDoubleAt(int paramInt) {
/* 338 */     checkIndex(paramInt);
/* 339 */     requireType('D');
/* 340 */     return doubleAt(paramInt << 3, getValue());
/*     */   }
/*     */ 
/*     */   public String valueString() {
/* 344 */     return valueString(true);
/*     */   }
/*     */ 
/*     */   public String valueString(boolean paramBoolean)
/*     */   {
/* 350 */     byte[] arrayOfByte = getValue();
/* 351 */     int i = arrayOfByte.length;
/* 352 */     int j = getElementType();
/*     */     StringBuffer localStringBuffer;
/*     */     int k;
/*     */     int m;
/* 353 */     if (j == 67) {
/* 354 */       localStringBuffer = new StringBuffer();
/* 355 */       for (k = 0; k < arrayOfByte.length; ) {
/* 356 */         m = charAt(k, arrayOfByte);
/* 357 */         localStringBuffer.append(m);
/* 358 */         k += 2;
/*     */       }
/*     */     } else {
/* 361 */       k = 8;
/* 362 */       if (paramBoolean) {
/* 363 */         k = 1000;
/*     */       }
/* 365 */       localStringBuffer = new StringBuffer("{");
/* 366 */       m = 0;
/* 367 */       for (int n = 0; n < arrayOfByte.length; ) {
/* 368 */         if (m > 0) {
/* 369 */           localStringBuffer.append(", ");
/*     */         }
/* 371 */         if (m >= k) {
/* 372 */           localStringBuffer.append("... ");
/*     */         }
/*     */         else {
/* 375 */           m++;
/*     */           int i1;
/* 376 */           switch (j) {
/*     */           case 90:
/* 378 */             boolean bool = booleanAt(n, arrayOfByte);
/* 379 */             if (bool)
/* 380 */               localStringBuffer.append("true");
/*     */             else {
/* 382 */               localStringBuffer.append("false");
/*     */             }
/* 384 */             n++;
/* 385 */             break;
/*     */           case 66:
/* 388 */             i1 = 0xFF & byteAt(n, arrayOfByte);
/* 389 */             localStringBuffer.append("0x" + Integer.toString(i1, 16));
/* 390 */             n++;
/* 391 */             break;
/*     */           case 83:
/* 394 */             i1 = shortAt(n, arrayOfByte);
/* 395 */             n += 2;
/* 396 */             localStringBuffer.append("" + i1);
/* 397 */             break;
/*     */           case 73:
/* 400 */             i1 = intAt(n, arrayOfByte);
/* 401 */             n += 4;
/* 402 */             localStringBuffer.append("" + i1);
/* 403 */             break;
/*     */           case 74:
/* 406 */             long l = longAt(n, arrayOfByte);
/* 407 */             localStringBuffer.append("" + l);
/* 408 */             n += 8;
/* 409 */             break;
/*     */           case 70:
/* 412 */             float f = floatAt(n, arrayOfByte);
/* 413 */             localStringBuffer.append("" + f);
/* 414 */             n += 4;
/* 415 */             break;
/*     */           case 68:
/* 418 */             double d = doubleAt(n, arrayOfByte);
/* 419 */             localStringBuffer.append("" + d);
/* 420 */             n += 8;
/* 421 */             break;
/*     */           case 67:
/*     */           case 69:
/*     */           case 71:
/*     */           case 72:
/*     */           case 75:
/*     */           case 76:
/*     */           case 77:
/*     */           case 78:
/*     */           case 79:
/*     */           case 80:
/*     */           case 81:
/*     */           case 82:
/*     */           case 84:
/*     */           case 85:
/*     */           case 86:
/*     */           case 87:
/*     */           case 88:
/*     */           case 89:
/*     */           default:
/* 424 */             throw new RuntimeException("unknown primitive type?");
/*     */           }
/*     */         }
/*     */       }
/* 428 */       localStringBuffer.append("}");
/*     */     }
/* 430 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaValueArray
 * JD-Core Version:    0.6.2
 */