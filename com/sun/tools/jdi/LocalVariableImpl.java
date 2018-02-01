/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class LocalVariableImpl extends MirrorImpl
/*     */   implements LocalVariable, ValueContainer
/*     */ {
/*     */   private final Method method;
/*     */   private final int slot;
/*     */   private final Location scopeStart;
/*     */   private final Location scopeEnd;
/*     */   private final String name;
/*     */   private final String signature;
/*  38 */   private String genericSignature = null;
/*     */ 
/*     */   LocalVariableImpl(VirtualMachine paramVirtualMachine, Method paramMethod, int paramInt, Location paramLocation1, Location paramLocation2, String paramString1, String paramString2, String paramString3)
/*     */   {
/*  44 */     super(paramVirtualMachine);
/*  45 */     this.method = paramMethod;
/*  46 */     this.slot = paramInt;
/*  47 */     this.scopeStart = paramLocation1;
/*  48 */     this.scopeEnd = paramLocation2;
/*  49 */     this.name = paramString1;
/*  50 */     this.signature = paramString2;
/*  51 */     if ((paramString3 != null) && (paramString3.length() > 0)) {
/*  52 */       this.genericSignature = paramString3;
/*     */     }
/*     */     else
/*  55 */       this.genericSignature = null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  60 */     if ((paramObject != null) && ((paramObject instanceof LocalVariableImpl))) {
/*  61 */       LocalVariableImpl localLocalVariableImpl = (LocalVariableImpl)paramObject;
/*  62 */       if ((slot() == localLocalVariableImpl.slot()) && (this.scopeStart != null));
/*  65 */       return (this.scopeStart
/*  64 */         .equals(localLocalVariableImpl.scopeStart)) && 
/*  65 */         (super
/*  65 */         .equals(paramObject));
/*     */     }
/*     */ 
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  75 */     return (this.scopeStart.hashCode() << 4) + slot();
/*     */   }
/*     */ 
/*     */   public int compareTo(LocalVariable paramLocalVariable) {
/*  79 */     LocalVariableImpl localLocalVariableImpl = (LocalVariableImpl)paramLocalVariable;
/*     */ 
/*  81 */     int i = this.scopeStart.compareTo(localLocalVariableImpl.scopeStart);
/*  82 */     if (i == 0) {
/*  83 */       i = slot() - localLocalVariableImpl.slot();
/*     */     }
/*  85 */     return i;
/*     */   }
/*     */ 
/*     */   public String name() {
/*  89 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String typeName()
/*     */   {
/*  97 */     JNITypeParser localJNITypeParser = new JNITypeParser(this.signature);
/*  98 */     return localJNITypeParser.typeName();
/*     */   }
/*     */ 
/*     */   public Type type() throws ClassNotLoadedException {
/* 102 */     return findType(signature());
/*     */   }
/*     */ 
/*     */   public Type findType(String paramString) throws ClassNotLoadedException {
/* 106 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)this.method.declaringType();
/* 107 */     return localReferenceTypeImpl.findType(paramString);
/*     */   }
/*     */ 
/*     */   public String signature() {
/* 111 */     return this.signature;
/*     */   }
/*     */ 
/*     */   public String genericSignature() {
/* 115 */     return this.genericSignature;
/*     */   }
/*     */ 
/*     */   public boolean isVisible(StackFrame paramStackFrame) {
/* 119 */     validateMirror(paramStackFrame);
/* 120 */     Method localMethod = paramStackFrame.location().method();
/*     */ 
/* 122 */     if (!localMethod.equals(this.method)) {
/* 123 */       throw new IllegalArgumentException("frame method different than variable's method");
/*     */     }
/*     */ 
/* 130 */     if (localMethod.isNative()) {
/* 131 */       return false;
/*     */     }
/*     */ 
/* 135 */     return (this.scopeStart.compareTo(paramStackFrame.location()) <= 0) && 
/* 135 */       (this.scopeEnd
/* 135 */       .compareTo(paramStackFrame
/* 135 */       .location()) >= 0);
/*     */   }
/*     */ 
/*     */   public boolean isArgument() {
/*     */     try {
/* 140 */       MethodImpl localMethodImpl = (MethodImpl)this.scopeStart.method();
/* 141 */       return this.slot < localMethodImpl.argSlotCount();
/*     */     } catch (AbsentInformationException localAbsentInformationException) {
/*     */     }
/* 144 */     throw new InternalException();
/*     */   }
/*     */ 
/*     */   int slot()
/*     */   {
/* 149 */     return this.slot;
/*     */   }
/*     */ 
/*     */   boolean hides(LocalVariable paramLocalVariable)
/*     */   {
/* 165 */     LocalVariableImpl localLocalVariableImpl = (LocalVariableImpl)paramLocalVariable;
/* 166 */     if ((!this.method.equals(localLocalVariableImpl.method)) || 
/* 167 */       (!this.name
/* 167 */       .equals(localLocalVariableImpl.name)))
/*     */     {
/* 168 */       return false;
/*     */     }
/* 170 */     return this.scopeStart.compareTo(localLocalVariableImpl.scopeStart) > 0;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 176 */     return name() + " in " + this.method.toString() + "@" + this.scopeStart
/* 176 */       .toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.LocalVariableImpl
 * JD-Core Version:    0.6.2
 */