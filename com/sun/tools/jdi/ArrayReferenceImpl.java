/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ArrayReference;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ArrayReferenceImpl extends ObjectReferenceImpl
/*     */   implements ArrayReference
/*     */ {
/*  38 */   int length = -1;
/*     */ 
/*     */   ArrayReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  41 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */ 
/*     */   protected ClassTypeImpl invokableReferenceType(Method paramMethod)
/*     */   {
/*  49 */     return (ClassTypeImpl)paramMethod.declaringType();
/*     */   }
/*     */ 
/*     */   ArrayTypeImpl arrayType() {
/*  53 */     return (ArrayTypeImpl)type();
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  61 */     if (this.length == -1) {
/*     */       try {
/*  63 */         this.length = 
/*  64 */           JDWP.ArrayReference.Length.process(this.vm, this).arrayLength;
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/*  66 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */     }
/*  69 */     return this.length;
/*     */   }
/*     */ 
/*     */   public Value getValue(int paramInt) {
/*  73 */     List localList = getValues(paramInt, 1);
/*  74 */     return (Value)localList.get(0);
/*     */   }
/*     */ 
/*     */   public List<Value> getValues() {
/*  78 */     return getValues(0, -1);
/*     */   }
/*     */ 
/*     */   private void validateArrayAccess(int paramInt1, int paramInt2)
/*     */   {
/*  89 */     if ((paramInt1 < 0) || (paramInt1 > length())) {
/*  90 */       throw new IndexOutOfBoundsException("Invalid array index: " + paramInt1);
/*     */     }
/*     */ 
/*  93 */     if (paramInt2 < 0) {
/*  94 */       throw new IndexOutOfBoundsException("Invalid array range length: " + paramInt2);
/*     */     }
/*     */ 
/*  97 */     if (paramInt1 + paramInt2 > length())
/*  98 */       throw new IndexOutOfBoundsException("Invalid array range: " + paramInt1 + " to " + (paramInt1 + paramInt2 - 1));
/*     */   }
/*     */ 
/*     */   private static <T> T cast(Object paramObject)
/*     */   {
/* 106 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public List<Value> getValues(int paramInt1, int paramInt2) {
/* 110 */     if (paramInt2 == -1) {
/* 111 */       paramInt2 = length() - paramInt1;
/*     */     }
/* 113 */     validateArrayAccess(paramInt1, paramInt2);
/* 114 */     if (paramInt2 == 0) {
/* 115 */       return new ArrayList();
/*     */     }
/*     */     List localList;
/*     */     try
/*     */     {
/* 120 */       localList = (List)cast(JDWP.ArrayReference.GetValues.process(this.vm, this, paramInt1, paramInt2).values);
/*     */     } catch (JDWPException localJDWPException) {
/* 122 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 125 */     return localList;
/*     */   }
/*     */ 
/*     */   public void setValue(int paramInt, Value paramValue)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 131 */     ArrayList localArrayList = new ArrayList(1);
/* 132 */     localArrayList.add(paramValue);
/* 133 */     setValues(paramInt, localArrayList, 0, 1);
/*     */   }
/*     */ 
/*     */   public void setValues(List<? extends Value> paramList)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 139 */     setValues(0, paramList, 0, -1);
/*     */   }
/*     */ 
/*     */   public void setValues(int paramInt1, List<? extends Value> paramList, int paramInt2, int paramInt3)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 147 */     if (paramInt3 == -1)
/*     */     {
/* 150 */       paramInt3 = Math.min(length() - paramInt1, paramList
/* 151 */         .size() - paramInt2);
/*     */     }
/* 153 */     validateMirrorsOrNulls(paramList);
/* 154 */     validateArrayAccess(paramInt1, paramInt3);
/*     */ 
/* 156 */     if ((paramInt2 < 0) || (paramInt2 > paramList.size())) {
/* 157 */       throw new IndexOutOfBoundsException("Invalid source index: " + paramInt2);
/*     */     }
/*     */ 
/* 160 */     if (paramInt2 + paramInt3 > paramList.size()) {
/* 161 */       throw new IndexOutOfBoundsException("Invalid source range: " + paramInt2 + " to " + (paramInt2 + paramInt3 - 1));
/*     */     }
/*     */ 
/* 167 */     int i = 0;
/* 168 */     ValueImpl[] arrayOfValueImpl = new ValueImpl[paramInt3];
/*     */ 
/* 170 */     for (int j = 0; j < paramInt3; j++) {
/* 171 */       ValueImpl localValueImpl = (ValueImpl)paramList.get(paramInt2 + j);
/*     */       try
/*     */       {
/* 175 */         arrayOfValueImpl[j] = 
/* 176 */           ValueImpl.prepareForAssignment(localValueImpl, new Component());
/*     */ 
/* 178 */         i = 1;
/*     */       }
/*     */       catch (ClassNotLoadedException localClassNotLoadedException)
/*     */       {
/* 190 */         if (localValueImpl != null) {
/* 191 */           throw localClassNotLoadedException;
/*     */         }
/*     */       }
/*     */     }
/* 195 */     if (i != 0)
/*     */       try
/*     */       {
/* 198 */         JDWP.ArrayReference.SetValues.process(this.vm, this, paramInt1, arrayOfValueImpl);
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 200 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 207 */     return "instance of " + arrayType().componentTypeName() + "[" + 
/* 207 */       length() + "] (id=" + uniqueID() + ")";
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 211 */     return 91;
/*     */   }
/*     */ 
/*     */   void validateAssignment(ValueContainer paramValueContainer) throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/*     */     try {
/* 217 */       super.validateAssignment(paramValueContainer);
/*     */     }
/*     */     catch (ClassNotLoadedException localClassNotLoadedException)
/*     */     {
/* 228 */       boolean bool = false;
/*     */ 
/* 230 */       JNITypeParser localJNITypeParser1 = new JNITypeParser(paramValueContainer
/* 230 */         .signature());
/*     */ 
/* 232 */       JNITypeParser localJNITypeParser2 = new JNITypeParser(
/* 232 */         arrayType().signature());
/* 233 */       int i = localJNITypeParser1.dimensionCount();
/* 234 */       if (i <= localJNITypeParser2.dimensionCount())
/*     */       {
/* 242 */         String str1 = localJNITypeParser1
/* 242 */           .componentSignature(i);
/*     */ 
/* 244 */         Type localType1 = paramValueContainer
/* 244 */           .findType(str1);
/*     */ 
/* 246 */         String str2 = localJNITypeParser2
/* 246 */           .componentSignature(i);
/*     */ 
/* 248 */         Type localType2 = arrayType().findComponentType(str2);
/* 249 */         bool = ArrayTypeImpl.isComponentAssignable(localType1, localType2);
/*     */       }
/*     */ 
/* 253 */       if (!bool)
/*     */       {
/* 257 */         throw new InvalidTypeException("Cannot assign " + 
/* 255 */           arrayType().name() + " to " + paramValueContainer
/* 257 */           .typeName());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class Component
/*     */     implements ValueContainer
/*     */   {
/*     */     Component()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Type type() throws ClassNotLoadedException
/*     */     {
/* 271 */       return ArrayReferenceImpl.this.arrayType().componentType();
/*     */     }
/*     */     public String typeName() {
/* 274 */       return ArrayReferenceImpl.this.arrayType().componentTypeName();
/*     */     }
/*     */     public String signature() {
/* 277 */       return ArrayReferenceImpl.this.arrayType().componentSignature();
/*     */     }
/*     */     public Type findType(String paramString) throws ClassNotLoadedException {
/* 280 */       return ArrayReferenceImpl.this.arrayType().findComponentType(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ArrayReferenceImpl
 * JD-Core Version:    0.6.2
 */