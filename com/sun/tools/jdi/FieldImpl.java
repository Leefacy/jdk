/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class FieldImpl extends TypeComponentImpl
/*     */   implements Field, ValueContainer
/*     */ {
/*     */   FieldImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt)
/*     */   {
/*  38 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  43 */     if ((paramObject != null) && ((paramObject instanceof FieldImpl))) {
/*  44 */       FieldImpl localFieldImpl = (FieldImpl)paramObject;
/*     */ 
/*  47 */       return (declaringType().equals(localFieldImpl.declaringType())) && 
/*  46 */         (ref() == localFieldImpl.ref()) && 
/*  47 */         (super
/*  47 */         .equals(paramObject));
/*     */     }
/*     */ 
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  54 */     return (int)ref();
/*     */   }
/*     */ 
/*     */   public int compareTo(Field paramField) {
/*  58 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)declaringType();
/*  59 */     int i = localReferenceTypeImpl.compareTo(paramField.declaringType());
/*  60 */     if (i == 0)
/*     */     {
/*  62 */       i = localReferenceTypeImpl.indexOf(this) - localReferenceTypeImpl
/*  62 */         .indexOf(paramField);
/*     */     }
/*     */ 
/*  64 */     return i;
/*     */   }
/*     */ 
/*     */   public Type type() throws ClassNotLoadedException {
/*  68 */     return findType(signature());
/*     */   }
/*     */ 
/*     */   public Type findType(String paramString) throws ClassNotLoadedException {
/*  72 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)declaringType();
/*  73 */     return localReferenceTypeImpl.findType(paramString);
/*     */   }
/*     */ 
/*     */   public String typeName()
/*     */   {
/*  81 */     JNITypeParser localJNITypeParser = new JNITypeParser(signature());
/*  82 */     return localJNITypeParser.typeName();
/*     */   }
/*     */ 
/*     */   public boolean isTransient() {
/*  86 */     return isModifierSet(128);
/*     */   }
/*     */ 
/*     */   public boolean isVolatile() {
/*  90 */     return isModifierSet(64);
/*     */   }
/*     */ 
/*     */   public boolean isEnumConstant() {
/*  94 */     return isModifierSet(16384);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  98 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 100 */     localStringBuffer.append(declaringType().name());
/* 101 */     localStringBuffer.append('.');
/* 102 */     localStringBuffer.append(name());
/*     */ 
/* 104 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.FieldImpl
 * JD-Core Version:    0.6.2
 */