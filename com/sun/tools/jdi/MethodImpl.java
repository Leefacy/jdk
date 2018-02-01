/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.ArrayReference;
/*     */ import com.sun.jdi.ArrayType;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class MethodImpl extends TypeComponentImpl
/*     */   implements Method
/*     */ {
/*     */   private JNITypeParser signatureParser;
/* 251 */   ReturnContainer retValContainer = null;
/*     */ 
/*     */   abstract int argSlotCount()
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   abstract List<Location> allLineLocations(SDE.Stratum paramStratum, String paramString)
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   abstract List<Location> locationsOfLine(SDE.Stratum paramStratum, String paramString, int paramInt)
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   MethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt)
/*     */   {
/*  53 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */ 
/*  55 */     this.signatureParser = new JNITypeParser(paramString2);
/*     */   }
/*     */ 
/*     */   static MethodImpl createMethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt)
/*     */   {
/*  65 */     if ((paramInt & 0x500) != 0)
/*     */     {
/*  67 */       return new NonConcreteMethodImpl(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */     }
/*     */ 
/*  72 */     return new ConcreteMethodImpl(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  80 */     if ((paramObject != null) && ((paramObject instanceof MethodImpl))) {
/*  81 */       MethodImpl localMethodImpl = (MethodImpl)paramObject;
/*     */ 
/*  84 */       return (declaringType().equals(localMethodImpl.declaringType())) && 
/*  83 */         (ref() == localMethodImpl.ref()) && 
/*  84 */         (super
/*  84 */         .equals(paramObject));
/*     */     }
/*     */ 
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  91 */     return (int)ref();
/*     */   }
/*     */ 
/*     */   public final List<Location> allLineLocations() throws AbsentInformationException
/*     */   {
/*  96 */     return allLineLocations(this.vm.getDefaultStratum(), null);
/*     */   }
/*     */ 
/*     */   public List<Location> allLineLocations(String paramString1, String paramString2)
/*     */     throws AbsentInformationException
/*     */   {
/* 102 */     return allLineLocations(this.declaringType.stratum(paramString1), paramString2);
/*     */   }
/*     */ 
/*     */   public final List<Location> locationsOfLine(int paramInt)
/*     */     throws AbsentInformationException
/*     */   {
/* 108 */     return locationsOfLine(this.vm.getDefaultStratum(), null, paramInt);
/*     */   }
/*     */ 
/*     */   public List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt)
/*     */     throws AbsentInformationException
/*     */   {
/* 116 */     return locationsOfLine(this.declaringType.stratum(paramString1), paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   LineInfo codeIndexToLineInfo(SDE.Stratum paramStratum, long paramLong)
/*     */   {
/* 122 */     if (paramStratum.isJava()) {
/* 123 */       return new BaseLineInfo(-1, this.declaringType);
/*     */     }
/* 125 */     return new StratumLineInfo(paramStratum.id(), -1, null, null);
/*     */   }
/*     */ 
/*     */   public String returnTypeName()
/*     */   {
/* 135 */     return this.signatureParser.typeName();
/*     */   }
/*     */ 
/*     */   private String returnSignature() {
/* 139 */     return this.signatureParser.signature();
/*     */   }
/*     */ 
/*     */   public Type returnType() throws ClassNotLoadedException {
/* 143 */     return findType(returnSignature());
/*     */   }
/*     */ 
/*     */   public Type findType(String paramString) throws ClassNotLoadedException {
/* 147 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)declaringType();
/* 148 */     return localReferenceTypeImpl.findType(paramString);
/*     */   }
/*     */ 
/*     */   public List<String> argumentTypeNames() {
/* 152 */     return this.signatureParser.argumentTypeNames();
/*     */   }
/*     */ 
/*     */   public List<String> argumentSignatures() {
/* 156 */     return this.signatureParser.argumentSignatures();
/*     */   }
/*     */ 
/*     */   Type argumentType(int paramInt) throws ClassNotLoadedException {
/* 160 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)declaringType();
/* 161 */     String str = (String)argumentSignatures().get(paramInt);
/* 162 */     return localReferenceTypeImpl.findType(str);
/*     */   }
/*     */ 
/*     */   public List<Type> argumentTypes() throws ClassNotLoadedException {
/* 166 */     int i = argumentSignatures().size();
/* 167 */     ArrayList localArrayList = new ArrayList(i);
/* 168 */     for (int j = 0; j < i; j++) {
/* 169 */       Type localType = argumentType(j);
/* 170 */       localArrayList.add(localType);
/*     */     }
/*     */ 
/* 173 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public int compareTo(Method paramMethod) {
/* 177 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)declaringType();
/* 178 */     int i = localReferenceTypeImpl.compareTo(paramMethod.declaringType());
/* 179 */     if (i == 0)
/*     */     {
/* 181 */       i = localReferenceTypeImpl.indexOf(this) - localReferenceTypeImpl
/* 181 */         .indexOf(paramMethod);
/*     */     }
/*     */ 
/* 183 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean isAbstract() {
/* 187 */     return isModifierSet(1024);
/*     */   }
/*     */ 
/*     */   public boolean isDefault()
/*     */   {
/* 194 */     return (!isModifierSet(1024)) && 
/* 192 */       (!isModifierSet(8)) && 
/* 193 */       (!isModifierSet(2)) && 
/* 194 */       ((declaringType() instanceof InterfaceType));
/*     */   }
/*     */ 
/*     */   public boolean isSynchronized() {
/* 198 */     return isModifierSet(32);
/*     */   }
/*     */ 
/*     */   public boolean isNative() {
/* 202 */     return isModifierSet(256);
/*     */   }
/*     */ 
/*     */   public boolean isVarArgs() {
/* 206 */     return isModifierSet(128);
/*     */   }
/*     */ 
/*     */   public boolean isBridge() {
/* 210 */     return isModifierSet(64);
/*     */   }
/*     */ 
/*     */   public boolean isConstructor() {
/* 214 */     return name().equals("<init>");
/*     */   }
/*     */ 
/*     */   public boolean isStaticInitializer() {
/* 218 */     return name().equals("<clinit>");
/*     */   }
/*     */ 
/*     */   public boolean isObsolete() {
/*     */     try {
/* 223 */       return JDWP.Method.IsObsolete.process(this.vm, this.declaringType, this.ref).isObsolete;
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 226 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   ReturnContainer getReturnValueContainer()
/*     */   {
/* 253 */     if (this.retValContainer == null) {
/* 254 */       this.retValContainer = new ReturnContainer();
/*     */     }
/* 256 */     return this.retValContainer;
/*     */   }
/*     */ 
/*     */   void handleVarArgs(List<Value> paramList)
/*     */     throws ClassNotLoadedException, InvalidTypeException
/*     */   {
/* 301 */     List localList = argumentTypes();
/* 302 */     ArrayType localArrayType = (ArrayType)localList.get(localList.size() - 1);
/* 303 */     Type localType1 = localArrayType.componentType();
/* 304 */     int i = paramList.size();
/* 305 */     int j = localList.size();
/* 306 */     if (i < j - 1)
/*     */     {
/* 308 */       return;
/*     */     }
/* 310 */     if (i == j - 1)
/*     */     {
/* 313 */       localObject = localArrayType.newInstance(0);
/* 314 */       paramList.add(localObject);
/* 315 */       return;
/*     */     }
/* 317 */     Object localObject = (Value)paramList.get(j - 1);
/* 318 */     if (localObject == null) {
/* 319 */       return;
/*     */     }
/* 321 */     Type localType2 = ((Value)localObject).type();
/* 322 */     if (((localType2 instanceof ArrayTypeImpl)) && 
/* 323 */       (i == j) && 
/* 324 */       (((ArrayTypeImpl)localType2)
/* 324 */       .isAssignableTo(localArrayType)))
/*     */     {
/* 329 */       return;
/*     */     }
/*     */ 
/* 338 */     int k = i - j + 1;
/* 339 */     ArrayReference localArrayReference = localArrayType.newInstance(k);
/*     */ 
/* 346 */     localArrayReference.setValues(0, paramList, j - 1, k);
/* 347 */     paramList.set(j - 1, localArrayReference);
/*     */ 
/* 352 */     for (int m = j; m < i; m++)
/* 353 */       paramList.remove(j);
/*     */   }
/*     */ 
/*     */   List<Value> validateAndPrepareArgumentsForInvoke(List<? extends Value> paramList)
/*     */     throws ClassNotLoadedException, InvalidTypeException
/*     */   {
/* 364 */     ArrayList localArrayList = new ArrayList(paramList);
/* 365 */     if (isVarArgs()) {
/* 366 */       handleVarArgs(localArrayList);
/*     */     }
/*     */ 
/* 369 */     int i = localArrayList.size();
/*     */ 
/* 371 */     JNITypeParser localJNITypeParser = new JNITypeParser(signature());
/* 372 */     List localList = localJNITypeParser.argumentSignatures();
/*     */ 
/* 374 */     if (localList.size() != i)
/*     */     {
/* 377 */       throw new IllegalArgumentException("Invalid argument count: expected " + localList
/* 376 */         .size() + ", received " + localArrayList
/* 377 */         .size());
/*     */     }
/*     */ 
/* 380 */     for (int j = 0; j < i; j++) {
/* 381 */       Object localObject = (Value)localArrayList.get(j);
/* 382 */       localObject = ValueImpl.prepareForAssignment((Value)localObject, new ArgumentContainer(j));
/*     */ 
/* 384 */       localArrayList.set(j, localObject);
/*     */     }
/* 386 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 390 */     StringBuffer localStringBuffer = new StringBuffer();
/* 391 */     localStringBuffer.append(declaringType().name());
/* 392 */     localStringBuffer.append(".");
/* 393 */     localStringBuffer.append(name());
/* 394 */     localStringBuffer.append("(");
/* 395 */     int i = 1;
/* 396 */     for (String str : argumentTypeNames()) {
/* 397 */       if (i == 0) {
/* 398 */         localStringBuffer.append(", ");
/*     */       }
/* 400 */       localStringBuffer.append(str);
/* 401 */       i = 0;
/*     */     }
/* 403 */     localStringBuffer.append(")");
/* 404 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   class ArgumentContainer
/*     */     implements ValueContainer
/*     */   {
/*     */     int index;
/*     */ 
/*     */     ArgumentContainer(int arg2)
/*     */     {
/*     */       int i;
/* 267 */       this.index = i;
/*     */     }
/*     */     public Type type() throws ClassNotLoadedException {
/* 270 */       return MethodImpl.this.argumentType(this.index);
/*     */     }
/*     */     public String typeName() {
/* 273 */       return (String)MethodImpl.this.argumentTypeNames().get(this.index);
/*     */     }
/*     */     public String signature() {
/* 276 */       return (String)MethodImpl.this.argumentSignatures().get(this.index);
/*     */     }
/*     */     public Type findType(String paramString) throws ClassNotLoadedException {
/* 279 */       return MethodImpl.this.findType(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   class ReturnContainer
/*     */     implements ValueContainer
/*     */   {
/*     */     ReturnContainer()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Type type()
/*     */       throws ClassNotLoadedException
/*     */     {
/* 239 */       return MethodImpl.this.returnType();
/*     */     }
/*     */     public String typeName() {
/* 242 */       return MethodImpl.this.returnTypeName();
/*     */     }
/*     */     public String signature() {
/* 245 */       return MethodImpl.this.returnSignature();
/*     */     }
/*     */     public Type findType(String paramString) throws ClassNotLoadedException {
/* 248 */       return MethodImpl.this.findType(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.MethodImpl
 * JD-Core Version:    0.6.2
 */