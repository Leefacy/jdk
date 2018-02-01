/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class JavaObject extends JavaLazyReadObject
/*     */ {
/*     */   private Object clazz;
/*     */ 
/*     */   public JavaObject(long paramLong1, long paramLong2)
/*     */   {
/*  54 */     super(paramLong2);
/*  55 */     this.clazz = makeId(paramLong1);
/*     */   }
/*     */ 
/*     */   public void resolve(Snapshot paramSnapshot) {
/*  59 */     if ((this.clazz instanceof JavaClass)) {
/*  60 */       return;
/*     */     }
/*  62 */     if ((this.clazz instanceof Number)) {
/*  63 */       long l1 = getIdValue((Number)this.clazz);
/*  64 */       this.clazz = paramSnapshot.findThing(l1);
/*  65 */       if (!(this.clazz instanceof JavaClass)) { warn("Class " + Long.toHexString(l1) + " not found, " + "adding fake class!");
/*     */ 
/*  69 */         ReadBuffer localReadBuffer = paramSnapshot.getReadBuffer();
/*  70 */         int j = paramSnapshot.getIdentifierSize();
/*  71 */         long l2 = getOffset() + 2 * j + 4L;
/*     */         int i;
/*     */         try { i = localReadBuffer.getInt(l2);
/*     */         } catch (IOException localIOException) {
/*  75 */           throw new RuntimeException(localIOException);
/*     */         }
/*  77 */         this.clazz = paramSnapshot.addFakeInstanceClass(l1, i); }
/*     */     }
/*     */     else {
/*  80 */       throw new InternalError("should not reach here");
/*     */     }
/*     */ 
/*  83 */     JavaClass localJavaClass = (JavaClass)this.clazz;
/*  84 */     localJavaClass.resolve(paramSnapshot);
/*     */ 
/*  89 */     parseFields(getValue(), true);
/*     */ 
/*  91 */     localJavaClass.addInstance(this);
/*  92 */     super.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */   public boolean isSameTypeAs(JavaThing paramJavaThing)
/*     */   {
/* 100 */     if (!(paramJavaThing instanceof JavaObject)) {
/* 101 */       return false;
/*     */     }
/* 103 */     JavaObject localJavaObject = (JavaObject)paramJavaThing;
/* 104 */     return getClazz().equals(localJavaObject.getClazz());
/*     */   }
/*     */ 
/*     */   public JavaClass getClazz()
/*     */   {
/* 111 */     return (JavaClass)this.clazz;
/*     */   }
/*     */ 
/*     */   public JavaThing[] getFields()
/*     */   {
/* 117 */     return parseFields(getValue(), false);
/*     */   }
/*     */ 
/*     */   public JavaThing getField(String paramString)
/*     */   {
/* 122 */     JavaThing[] arrayOfJavaThing = getFields();
/* 123 */     JavaField[] arrayOfJavaField = getClazz().getFieldsForInstance();
/* 124 */     for (int i = 0; i < arrayOfJavaField.length; i++) {
/* 125 */       if (arrayOfJavaField[i].getName().equals(paramString)) {
/* 126 */         return arrayOfJavaThing[i];
/*     */       }
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public int compareTo(JavaThing paramJavaThing) {
/* 133 */     if ((paramJavaThing instanceof JavaObject)) {
/* 134 */       JavaObject localJavaObject = (JavaObject)paramJavaThing;
/* 135 */       return getClazz().getName().compareTo(localJavaObject.getClazz().getName());
/*     */     }
/* 137 */     return super.compareTo(paramJavaThing);
/*     */   }
/*     */ 
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 141 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/* 142 */     JavaThing[] arrayOfJavaThing = getFields();
/* 143 */     for (int i = 0; i < arrayOfJavaThing.length; i++)
/* 144 */       if ((arrayOfJavaThing[i] != null) && (
/* 145 */         (!paramJavaHeapObjectVisitor.mightExclude()) || 
/* 146 */         (!paramJavaHeapObjectVisitor
/* 146 */         .exclude(getClazz().getClassForField(i), 
/* 147 */         getClazz().getFieldForInstance(i)))))
/*     */       {
/* 150 */         if ((arrayOfJavaThing[i] instanceof JavaHeapObject))
/* 151 */           paramJavaHeapObjectVisitor.visit((JavaHeapObject)arrayOfJavaThing[i]);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean refersOnlyWeaklyTo(Snapshot paramSnapshot, JavaThing paramJavaThing)
/*     */   {
/* 158 */     if (paramSnapshot.getWeakReferenceClass() != null) {
/* 159 */       int i = paramSnapshot.getReferentFieldIndex();
/* 160 */       if (paramSnapshot.getWeakReferenceClass().isAssignableFrom(getClazz()))
/*     */       {
/* 164 */         JavaThing[] arrayOfJavaThing = getFields();
/* 165 */         for (int j = 0; j < arrayOfJavaThing.length; j++) {
/* 166 */           if ((j != i) && (arrayOfJavaThing[j] == paramJavaThing)) {
/* 167 */             return false;
/*     */           }
/*     */         }
/* 170 */         return true;
/*     */       }
/*     */     }
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   public String describeReferenceTo(JavaThing paramJavaThing, Snapshot paramSnapshot)
/*     */   {
/* 181 */     JavaThing[] arrayOfJavaThing = getFields();
/* 182 */     for (int i = 0; i < arrayOfJavaThing.length; i++) {
/* 183 */       if (arrayOfJavaThing[i] == paramJavaThing) {
/* 184 */         JavaField localJavaField = getClazz().getFieldForInstance(i);
/* 185 */         return "field " + localJavaField.getName();
/*     */       }
/*     */     }
/* 188 */     return super.describeReferenceTo(paramJavaThing, paramSnapshot);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 192 */     if (getClazz().isString()) {
/* 193 */       JavaThing localJavaThing = getField("value");
/* 194 */       if ((localJavaThing instanceof JavaValueArray)) {
/* 195 */         return ((JavaValueArray)localJavaThing).valueString();
/*     */       }
/* 197 */       return "null";
/*     */     }
/*     */ 
/* 200 */     return super.toString();
/*     */   }
/*     */ 
/*     */   protected final int readValueLength()
/*     */     throws IOException
/*     */   {
/* 216 */     JavaClass localJavaClass = getClazz();
/* 217 */     int i = localJavaClass.getIdentifierSize();
/* 218 */     long l = getOffset() + 2 * i + 4L;
/* 219 */     return localJavaClass.getReadBuffer().getInt(l);
/*     */   }
/*     */ 
/*     */   protected final byte[] readValue() throws IOException {
/* 223 */     JavaClass localJavaClass = getClazz();
/* 224 */     int i = localJavaClass.getIdentifierSize();
/* 225 */     ReadBuffer localReadBuffer = localJavaClass.getReadBuffer();
/* 226 */     long l = getOffset() + 2 * i + 4L;
/* 227 */     int j = localReadBuffer.getInt(l);
/* 228 */     if (j == 0) {
/* 229 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     }
/* 231 */     byte[] arrayOfByte = new byte[j];
/* 232 */     localReadBuffer.get(l + 4L, arrayOfByte);
/* 233 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private JavaThing[] parseFields(byte[] paramArrayOfByte, boolean paramBoolean)
/*     */   {
/* 238 */     JavaClass localJavaClass1 = getClazz();
/* 239 */     int i = localJavaClass1.getNumFieldsForInstance();
/* 240 */     JavaField[] arrayOfJavaField = localJavaClass1.getFields();
/* 241 */     JavaThing[] arrayOfJavaThing = new JavaThing[i];
/* 242 */     Snapshot localSnapshot = localJavaClass1.getSnapshot();
/* 243 */     int j = localSnapshot.getIdentifierSize();
/* 244 */     int k = 0;
/*     */ 
/* 255 */     i -= arrayOfJavaField.length;
/* 256 */     JavaClass localJavaClass2 = localJavaClass1;
/* 257 */     int m = 0;
/* 258 */     for (int n = 0; n < arrayOfJavaThing.length; k++) {
/* 259 */       while (k >= arrayOfJavaField.length) {
/* 260 */         localJavaClass2 = localJavaClass2.getSuperclass();
/* 261 */         arrayOfJavaField = localJavaClass2.getFields();
/* 262 */         k = 0;
/* 263 */         i -= arrayOfJavaField.length;
/*     */       }
/* 265 */       JavaField localJavaField = arrayOfJavaField[k];
/* 266 */       char c = localJavaField.getSignature().charAt(0);
/*     */       byte b;
/*     */       int i1;
/* 267 */       switch (c) {
/*     */       case 'L':
/*     */       case '[':
/* 270 */         long l1 = objectIdAt(m, paramArrayOfByte);
/* 271 */         m += j;
/* 272 */         JavaObjectRef localJavaObjectRef = new JavaObjectRef(l1);
/* 273 */         arrayOfJavaThing[(i + k)] = localJavaObjectRef.dereference(localSnapshot, localJavaField, paramBoolean);
/* 274 */         break;
/*     */       case 'Z':
/* 277 */         b = byteAt(m, paramArrayOfByte);
/* 278 */         m++;
/* 279 */         arrayOfJavaThing[(i + k)] = new JavaBoolean(b != 0);
/* 280 */         break;
/*     */       case 'B':
/* 283 */         b = byteAt(m, paramArrayOfByte);
/* 284 */         m++;
/* 285 */         arrayOfJavaThing[(i + k)] = new JavaByte(b);
/* 286 */         break;
/*     */       case 'S':
/* 289 */         i1 = shortAt(m, paramArrayOfByte);
/* 290 */         m += 2;
/* 291 */         arrayOfJavaThing[(i + k)] = new JavaShort(i1);
/* 292 */         break;
/*     */       case 'C':
/* 295 */         i1 = charAt(m, paramArrayOfByte);
/* 296 */         m += 2;
/* 297 */         arrayOfJavaThing[(i + k)] = new JavaChar(i1);
/* 298 */         break;
/*     */       case 'I':
/* 301 */         int i2 = intAt(m, paramArrayOfByte);
/* 302 */         m += 4;
/* 303 */         arrayOfJavaThing[(i + k)] = new JavaInt(i2);
/* 304 */         break;
/*     */       case 'J':
/* 307 */         long l2 = longAt(m, paramArrayOfByte);
/* 308 */         m += 8;
/* 309 */         arrayOfJavaThing[(i + k)] = new JavaLong(l2);
/* 310 */         break;
/*     */       case 'F':
/* 313 */         float f = floatAt(m, paramArrayOfByte);
/* 314 */         m += 4;
/* 315 */         arrayOfJavaThing[(i + k)] = new JavaFloat(f);
/* 316 */         break;
/*     */       case 'D':
/* 319 */         double d = doubleAt(m, paramArrayOfByte);
/* 320 */         m += 8;
/* 321 */         arrayOfJavaThing[(i + k)] = new JavaDouble(d);
/* 322 */         break;
/*     */       case 'E':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'K':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       default:
/* 325 */         throw new RuntimeException("invalid signature: " + c);
/*     */       }
/* 258 */       n++;
/*     */     }
/*     */ 
/* 328 */     return arrayOfJavaThing;
/*     */   }
/*     */ 
/*     */   private void warn(String paramString) {
/* 332 */     System.out.println("WARNING: " + paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaObject
 * JD-Core Version:    0.6.2
 */