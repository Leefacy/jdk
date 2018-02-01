/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class Type
/*     */ {
/*     */   public boolean isObject()
/*     */   {
/*  46 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract <R, D> R accept(Visitor<R, D> paramVisitor, D paramD);
/*     */ 
/*     */   protected static void append(StringBuilder paramStringBuilder, String paramString1, List<? extends Type> paramList, String paramString2) {
/*  52 */     paramStringBuilder.append(paramString1);
/*  53 */     String str = "";
/*  54 */     for (Type localType : paramList) {
/*  55 */       paramStringBuilder.append(str);
/*  56 */       paramStringBuilder.append(localType);
/*  57 */       str = ", ";
/*     */     }
/*  59 */     paramStringBuilder.append(paramString2);
/*     */   }
/*     */ 
/*     */   protected static void appendIfNotEmpty(StringBuilder paramStringBuilder, String paramString1, List<? extends Type> paramList, String paramString2) {
/*  63 */     if ((paramList != null) && (paramList.size() > 0))
/*  64 */       append(paramStringBuilder, paramString1, paramList, paramString2);
/*     */   }
/*     */ 
/*     */   public static class ArrayType extends Type
/*     */   {
/*     */     public final Type elemType;
/*     */ 
/*     */     public ArrayType(Type paramType)
/*     */     {
/* 127 */       this.elemType = paramType;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/* 131 */       return paramVisitor.visitArrayType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 136 */       return this.elemType + "[]";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ClassSigType extends Type
/*     */   {
/*     */     public final List<Type.TypeParamType> typeParamTypes;
/*     */     public final Type superclassType;
/*     */     public final List<Type> superinterfaceTypes;
/*     */ 
/*     */     public ClassSigType(List<Type.TypeParamType> paramList, Type paramType, List<Type> paramList1)
/*     */     {
/* 198 */       this.typeParamTypes = paramList;
/* 199 */       this.superclassType = paramType;
/* 200 */       this.superinterfaceTypes = paramList1;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/* 204 */       return paramVisitor.visitClassSigType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 209 */       StringBuilder localStringBuilder = new StringBuilder();
/* 210 */       appendIfNotEmpty(localStringBuilder, "<", this.typeParamTypes, ">");
/* 211 */       if (this.superclassType != null) {
/* 212 */         localStringBuilder.append(" extends ");
/* 213 */         localStringBuilder.append(this.superclassType);
/*     */       }
/* 215 */       appendIfNotEmpty(localStringBuilder, " implements ", this.superinterfaceTypes, "");
/* 216 */       return localStringBuilder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ClassType extends Type
/*     */   {
/*     */     public final ClassType outerType;
/*     */     public final String name;
/*     */     public final List<Type> typeArgs;
/*     */ 
/*     */     public ClassType(ClassType paramClassType, String paramString, List<Type> paramList)
/*     */     {
/* 242 */       this.outerType = paramClassType;
/* 243 */       this.name = paramString;
/* 244 */       this.typeArgs = paramList;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/* 248 */       return paramVisitor.visitClassType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getBinaryName() {
/* 252 */       if (this.outerType == null) {
/* 253 */         return this.name;
/*     */       }
/* 255 */       return this.outerType.getBinaryName() + "$" + this.name;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 260 */       StringBuilder localStringBuilder = new StringBuilder();
/* 261 */       if (this.outerType != null) {
/* 262 */         localStringBuilder.append(this.outerType);
/* 263 */         localStringBuilder.append(".");
/*     */       }
/* 265 */       localStringBuilder.append(this.name);
/* 266 */       appendIfNotEmpty(localStringBuilder, "<", this.typeArgs, ">");
/* 267 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public boolean isObject()
/*     */     {
/* 274 */       return (this.outerType == null) && 
/* 273 */         (this.name
/* 273 */         .equals("java/lang/Object")) && (
/* 273 */         (this.typeArgs == null) || 
/* 274 */         (this.typeArgs
/* 274 */         .isEmpty()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MethodType extends Type
/*     */   {
/*     */     public final List<? extends Type.TypeParamType> typeParamTypes;
/*     */     public final List<? extends Type> paramTypes;
/*     */     public final Type returnType;
/*     */     public final List<? extends Type> throwsTypes;
/*     */ 
/*     */     public MethodType(List<? extends Type> paramList, Type paramType)
/*     */     {
/* 153 */       this(null, paramList, paramType, null);
/*     */     }
/*     */ 
/*     */     public MethodType(List<? extends Type.TypeParamType> paramList, List<? extends Type> paramList1, Type paramType, List<? extends Type> paramList2)
/*     */     {
/* 160 */       this.typeParamTypes = paramList;
/* 161 */       this.paramTypes = paramList1;
/* 162 */       this.returnType = paramType;
/* 163 */       this.throwsTypes = paramList2;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/* 167 */       return paramVisitor.visitMethodType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 172 */       StringBuilder localStringBuilder = new StringBuilder();
/* 173 */       appendIfNotEmpty(localStringBuilder, "<", this.typeParamTypes, "> ");
/* 174 */       localStringBuilder.append(this.returnType);
/* 175 */       append(localStringBuilder, " (", this.paramTypes, ")");
/* 176 */       appendIfNotEmpty(localStringBuilder, " throws ", this.throwsTypes, "");
/* 177 */       return localStringBuilder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SimpleType extends Type
/*     */   {
/* 106 */     private static final Set<String> primitiveTypes = new HashSet(Arrays.asList(new String[] { "boolean", "byte", "char", "double", "float", "int", "long", "short", "void" }));
/*     */     public final String name;
/*     */ 
/*     */     public SimpleType(String paramString)
/*     */     {
/*  95 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/*  99 */       return paramVisitor.visitSimpleType(this, paramD);
/*     */     }
/*     */ 
/*     */     public boolean isPrimitiveType() {
/* 103 */       return primitiveTypes.contains(this.name);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 111 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TypeParamType extends Type
/*     */   {
/*     */     public final String name;
/*     */     public final Type classBound;
/*     */     public final List<Type> interfaceBounds;
/*     */ 
/*     */     public TypeParamType(String paramString, Type paramType, List<Type> paramList)
/*     */     {
/* 299 */       this.name = paramString;
/* 300 */       this.classBound = paramType;
/* 301 */       this.interfaceBounds = paramList;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/* 305 */       return paramVisitor.visitTypeParamType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 310 */       StringBuilder localStringBuilder = new StringBuilder();
/* 311 */       localStringBuilder.append(this.name);
/* 312 */       String str = " extends ";
/* 313 */       if (this.classBound != null) {
/* 314 */         localStringBuilder.append(str);
/* 315 */         localStringBuilder.append(this.classBound);
/* 316 */         str = " & ";
/*     */       }
/* 318 */       if (this.interfaceBounds != null) {
/* 319 */         for (Type localType : this.interfaceBounds) {
/* 320 */           localStringBuilder.append(str);
/* 321 */           localStringBuilder.append(localType);
/* 322 */           str = " & ";
/*     */         }
/*     */       }
/* 325 */       return localStringBuilder.toString();
/*     */     }
/*     */   }
/*     */   public static abstract interface Visitor<R, P> {
/*     */     public abstract R visitSimpleType(Type.SimpleType paramSimpleType, P paramP);
/*     */ 
/*     */     public abstract R visitArrayType(Type.ArrayType paramArrayType, P paramP);
/*     */ 
/*     */     public abstract R visitMethodType(Type.MethodType paramMethodType, P paramP);
/*     */ 
/*     */     public abstract R visitClassSigType(Type.ClassSigType paramClassSigType, P paramP);
/*     */ 
/*     */     public abstract R visitClassType(Type.ClassType paramClassType, P paramP);
/*     */ 
/*     */     public abstract R visitTypeParamType(Type.TypeParamType paramTypeParamType, P paramP);
/*     */ 
/*     */     public abstract R visitWildcardType(Type.WildcardType paramWildcardType, P paramP);
/*     */   }
/*     */ 
/*     */   public static class WildcardType extends Type {
/*     */     public final Kind kind;
/*     */     public final Type boundType;
/*     */ 
/* 349 */     public WildcardType() { this(Kind.UNBOUNDED, null); }
/*     */ 
/*     */     public WildcardType(Kind paramKind, Type paramType) {
/* 352 */       this.kind = paramKind;
/* 353 */       this.boundType = paramType;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(Type.Visitor<R, D> paramVisitor, D paramD) {
/* 357 */       return paramVisitor.visitWildcardType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 362 */       switch (Type.1.$SwitchMap$com$sun$tools$classfile$Type$WildcardType$Kind[this.kind.ordinal()]) {
/*     */       case 1:
/* 364 */         return "?";
/*     */       case 2:
/* 366 */         return "? extends " + this.boundType;
/*     */       case 3:
/* 368 */         return "? super " + this.boundType;
/*     */       }
/* 370 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     public static enum Kind
/*     */     {
/* 347 */       UNBOUNDED, EXTENDS, SUPER;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Type
 * JD-Core Version:    0.6.2
 */