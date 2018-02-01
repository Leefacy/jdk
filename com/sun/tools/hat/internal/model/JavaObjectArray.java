/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class JavaObjectArray extends JavaLazyReadObject
/*     */ {
/*     */   private Object clazz;
/*     */ 
/*     */   public JavaObjectArray(long paramLong1, long paramLong2)
/*     */   {
/*  46 */     super(paramLong2);
/*  47 */     this.clazz = makeId(paramLong1);
/*     */   }
/*     */ 
/*     */   public JavaClass getClazz() {
/*  51 */     return (JavaClass)this.clazz;
/*     */   }
/*     */ 
/*     */   public void resolve(Snapshot paramSnapshot) {
/*  55 */     if ((this.clazz instanceof JavaClass)) {
/*  56 */       return;
/*     */     }
/*  58 */     long l = getIdValue((Number)this.clazz);
/*     */     JavaHeapObject localJavaHeapObject;
/*  59 */     if (paramSnapshot.isNewStyleArrayClass())
/*     */     {
/*  61 */       localJavaHeapObject = paramSnapshot.findThing(l);
/*  62 */       if ((localJavaHeapObject instanceof JavaClass)) {
/*  63 */         this.clazz = ((JavaClass)localJavaHeapObject);
/*     */       }
/*     */     }
/*  66 */     if (!(this.clazz instanceof JavaClass)) {
/*  67 */       localJavaHeapObject = paramSnapshot.findThing(l);
/*  68 */       if ((localJavaHeapObject != null) && ((localJavaHeapObject instanceof JavaClass))) {
/*  69 */         JavaClass localJavaClass = (JavaClass)localJavaHeapObject;
/*  70 */         String str = localJavaClass.getName();
/*  71 */         if (!str.startsWith("[")) {
/*  72 */           str = "L" + localJavaClass.getName() + ";";
/*     */         }
/*  74 */         this.clazz = paramSnapshot.getArrayClass(str);
/*     */       }
/*     */     }
/*     */ 
/*  78 */     if (!(this.clazz instanceof JavaClass)) {
/*  79 */       this.clazz = paramSnapshot.getOtherArrayType();
/*     */     }
/*  81 */     ((JavaClass)this.clazz).addInstance(this);
/*  82 */     super.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */   public JavaThing[] getValues() {
/*  86 */     return getElements();
/*     */   }
/*     */ 
/*     */   public JavaThing[] getElements() {
/*  90 */     Snapshot localSnapshot = getClazz().getSnapshot();
/*  91 */     byte[] arrayOfByte = getValue();
/*  92 */     int i = localSnapshot.getIdentifierSize();
/*  93 */     int j = arrayOfByte.length / i;
/*  94 */     JavaThing[] arrayOfJavaThing = new JavaThing[j];
/*  95 */     int k = 0;
/*  96 */     for (int m = 0; m < arrayOfJavaThing.length; m++) {
/*  97 */       long l = objectIdAt(k, arrayOfByte);
/*  98 */       k += i;
/*  99 */       arrayOfJavaThing[m] = localSnapshot.findThing(l);
/*     */     }
/* 101 */     return arrayOfJavaThing;
/*     */   }
/*     */ 
/*     */   public int compareTo(JavaThing paramJavaThing) {
/* 105 */     if ((paramJavaThing instanceof JavaObjectArray)) {
/* 106 */       return 0;
/*     */     }
/* 108 */     return super.compareTo(paramJavaThing);
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 112 */     return getValueLength() / getClazz().getIdentifierSize();
/*     */   }
/*     */ 
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 116 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/* 117 */     JavaThing[] arrayOfJavaThing = getElements();
/* 118 */     for (int i = 0; i < arrayOfJavaThing.length; i++)
/* 119 */       if ((arrayOfJavaThing[i] != null) && ((arrayOfJavaThing[i] instanceof JavaHeapObject)))
/* 120 */         paramJavaHeapObjectVisitor.visit((JavaHeapObject)arrayOfJavaThing[i]);
/*     */   }
/*     */ 
/*     */   public String describeReferenceTo(JavaThing paramJavaThing, Snapshot paramSnapshot)
/*     */   {
/* 130 */     JavaThing[] arrayOfJavaThing = getElements();
/* 131 */     for (int i = 0; i < arrayOfJavaThing.length; i++) {
/* 132 */       if (arrayOfJavaThing[i] == paramJavaThing) {
/* 133 */         return "Element " + i + " of " + this;
/*     */       }
/*     */     }
/* 136 */     return super.describeReferenceTo(paramJavaThing, paramSnapshot);
/*     */   }
/*     */ 
/*     */   protected final int readValueLength()
/*     */     throws IOException
/*     */   {
/* 150 */     JavaClass localJavaClass = getClazz();
/* 151 */     ReadBuffer localReadBuffer = localJavaClass.getReadBuffer();
/* 152 */     int i = localJavaClass.getIdentifierSize();
/* 153 */     long l = getOffset() + i + 4L;
/* 154 */     int j = localReadBuffer.getInt(l);
/* 155 */     return j * localJavaClass.getIdentifierSize();
/*     */   }
/*     */ 
/*     */   protected final byte[] readValue() throws IOException {
/* 159 */     JavaClass localJavaClass = getClazz();
/* 160 */     ReadBuffer localReadBuffer = localJavaClass.getReadBuffer();
/* 161 */     int i = localJavaClass.getIdentifierSize();
/* 162 */     long l = getOffset() + i + 4L;
/* 163 */     int j = localReadBuffer.getInt(l);
/* 164 */     if (j == 0) {
/* 165 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     }
/* 167 */     byte[] arrayOfByte = new byte[j * i];
/* 168 */     localReadBuffer.get(l + 4L + i, arrayOfByte);
/* 169 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaObjectArray
 * JD-Core Version:    0.6.2
 */