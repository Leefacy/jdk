/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ArrayReference;
/*     */ import com.sun.jdi.ArrayType;
/*     */ import com.sun.jdi.ClassLoaderReference;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.PrimitiveType;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ArrayTypeImpl extends ReferenceTypeImpl
/*     */   implements ArrayType
/*     */ {
/*     */   protected ArrayTypeImpl(VirtualMachine paramVirtualMachine, long paramLong)
/*     */   {
/*  40 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */ 
/*     */   public ArrayReference newInstance(int paramInt)
/*     */   {
/*     */     try {
/*  46 */       return (ArrayReference)JDWP.ArrayType.NewInstance.process(this.vm, this, paramInt).newArray;
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/*  48 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String componentSignature() {
/*  53 */     return signature().substring(1);
/*     */   }
/*     */ 
/*     */   public String componentTypeName() {
/*  57 */     JNITypeParser localJNITypeParser = new JNITypeParser(componentSignature());
/*  58 */     return localJNITypeParser.typeName();
/*     */   }
/*     */ 
/*     */   Type type() throws ClassNotLoadedException {
/*  62 */     return findType(componentSignature());
/*     */   }
/*     */ 
/*     */   void addVisibleMethods(Map<String, Method> paramMap, Set<InterfaceType> paramSet)
/*     */   {
/*     */   }
/*     */ 
/*     */   public List<Method> allMethods()
/*     */   {
/*  71 */     return new ArrayList(0);
/*     */   }
/*     */ 
/*     */   Type findComponentType(String paramString)
/*     */     throws ClassNotLoadedException
/*     */   {
/*  84 */     byte b = (byte)paramString.charAt(0);
/*  85 */     if (PacketStream.isObjectTag(b))
/*     */     {
/*  87 */       JNITypeParser localJNITypeParser = new JNITypeParser(componentSignature());
/*  88 */       List localList = this.vm.classesByName(localJNITypeParser.typeName());
/*  89 */       Iterator localIterator = localList.iterator();
/*  90 */       while (localIterator.hasNext()) {
/*  91 */         ReferenceType localReferenceType = (ReferenceType)localIterator.next();
/*  92 */         ClassLoaderReference localClassLoaderReference = localReferenceType.classLoader();
/*  93 */         if (localClassLoaderReference == null ? 
/*  94 */           classLoader() == null : localClassLoaderReference
/*  95 */           .equals(classLoader())) {
/*  96 */           return localReferenceType;
/*     */         }
/*     */       }
/*     */ 
/* 100 */       throw new ClassNotLoadedException(componentTypeName());
/*     */     }
/*     */ 
/* 103 */     return this.vm.primitiveTypeMirror(b);
/*     */   }
/*     */ 
/*     */   public Type componentType() throws ClassNotLoadedException
/*     */   {
/* 108 */     return findComponentType(componentSignature());
/*     */   }
/*     */ 
/*     */   static boolean isComponentAssignable(Type paramType1, Type paramType2) {
/* 112 */     if ((paramType2 instanceof PrimitiveType))
/*     */     {
/* 115 */       return paramType2.equals(paramType1);
/*     */     }
/* 117 */     if ((paramType1 instanceof PrimitiveType)) {
/* 118 */       return false;
/*     */     }
/*     */ 
/* 121 */     ReferenceTypeImpl localReferenceTypeImpl1 = (ReferenceTypeImpl)paramType2;
/* 122 */     ReferenceTypeImpl localReferenceTypeImpl2 = (ReferenceTypeImpl)paramType1;
/*     */ 
/* 125 */     return localReferenceTypeImpl1.isAssignableTo(localReferenceTypeImpl2);
/*     */   }
/*     */ 
/*     */   boolean isAssignableTo(ReferenceType paramReferenceType)
/*     */   {
/* 134 */     if ((paramReferenceType instanceof ArrayType))
/*     */       try {
/* 136 */         Type localType = ((ArrayType)paramReferenceType).componentType();
/* 137 */         return isComponentAssignable(localType, componentType());
/*     */       }
/*     */       catch (ClassNotLoadedException localClassNotLoadedException)
/*     */       {
/* 141 */         return false;
/*     */       }
/* 143 */     if ((paramReferenceType instanceof InterfaceType))
/*     */     {
/* 145 */       return paramReferenceType.name().equals("java.lang.Cloneable");
/*     */     }
/*     */ 
/* 148 */     return paramReferenceType.name().equals("java.lang.Object");
/*     */   }
/*     */ 
/*     */   List<ReferenceType> inheritedTypes()
/*     */   {
/* 153 */     return new ArrayList(0);
/*     */   }
/*     */ 
/*     */   void getModifiers() {
/* 157 */     if (this.modifiers != -1) {
/* 158 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 173 */       Type localType = componentType();
/* 174 */       if ((localType instanceof PrimitiveType)) {
/* 175 */         this.modifiers = 17;
/*     */       } else {
/* 177 */         ReferenceType localReferenceType = (ReferenceType)localType;
/* 178 */         this.modifiers = localReferenceType.modifiers();
/*     */       }
/*     */     } catch (ClassNotLoadedException localClassNotLoadedException) {
/* 181 */       localClassNotLoadedException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 186 */     return "array class " + name() + " (" + loaderString() + ")";
/*     */   }
/*     */ 
/*     */   public boolean isPrepared()
/*     */   {
/* 193 */     return true; } 
/* 194 */   public boolean isVerified() { return true; } 
/* 195 */   public boolean isInitialized() { return true; } 
/* 196 */   public boolean failedToInitialize() { return false; } 
/* 197 */   public boolean isAbstract() { return false; }
/*     */ 
/*     */ 
/*     */   public boolean isFinal()
/*     */   {
/* 202 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ArrayTypeImpl
 * JD-Core Version:    0.6.2
 */