/*      */ package com.sun.tools.javac.code;
/*      */ 
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.type.ArrayType;
/*      */ import javax.lang.model.type.DeclaredType;
/*      */ import javax.lang.model.type.ErrorType;
/*      */ import javax.lang.model.type.ExecutableType;
/*      */ import javax.lang.model.type.IntersectionType;
/*      */ import javax.lang.model.type.NoType;
/*      */ import javax.lang.model.type.NullType;
/*      */ import javax.lang.model.type.PrimitiveType;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ import javax.lang.model.type.TypeMirror;
/*      */ import javax.lang.model.type.TypeVariable;
/*      */ import javax.lang.model.type.TypeVisitor;
/*      */ import javax.lang.model.type.UnionType;
/*      */ import javax.lang.model.type.WildcardType;
/*      */ 
/*      */ public abstract class Type extends AnnoConstruct
/*      */   implements TypeMirror
/*      */ {
/*   75 */   public static final JCNoType noType = new JCNoType()
/*      */   {
/*      */     public String toString() {
/*   78 */       return "none";
/*      */     }
/*   75 */   };
/*      */ 
/*   83 */   public static final JCNoType recoveryType = new JCNoType()
/*      */   {
/*      */     public String toString() {
/*   86 */       return "recovery";
/*      */     }
/*   83 */   };
/*      */ 
/*   91 */   public static final JCNoType stuckType = new JCNoType()
/*      */   {
/*      */     public String toString() {
/*   94 */       return "stuck";
/*      */     }
/*   91 */   };
/*      */ 
/*  101 */   public static boolean moreInfo = false;
/*      */   public Symbol.TypeSymbol tsym;
/*      */ 
/*      */   public boolean hasTag(TypeTag paramTypeTag)
/*      */   {
/*  112 */     return paramTypeTag == getTag();
/*      */   }
/*      */ 
/*      */   public abstract TypeTag getTag();
/*      */ 
/*      */   public boolean isNumeric()
/*      */   {
/*  122 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isPrimitive() {
/*  126 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isPrimitiveOrVoid() {
/*  130 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isReference() {
/*  134 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isNullOrReference() {
/*  138 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isPartial() {
/*  142 */     return false;
/*      */   }
/*      */ 
/*      */   public Object constValue()
/*      */   {
/*  152 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isFalse()
/*      */   {
/*  158 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isTrue()
/*      */   {
/*  164 */     return false;
/*      */   }
/*      */ 
/*      */   public Type getModelType()
/*      */   {
/*  173 */     return this;
/*      */   }
/*      */ 
/*      */   public static com.sun.tools.javac.util.List<Type> getModelTypes(com.sun.tools.javac.util.List<Type> paramList) {
/*  177 */     ListBuffer localListBuffer = new ListBuffer();
/*  178 */     for (Type localType : paramList)
/*  179 */       localListBuffer.append(localType.getModelType());
/*  180 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   public Type getOriginalType()
/*      */   {
/*  186 */     return this;
/*      */   }
/*      */   public <R, S> R accept(Visitor<R, S> paramVisitor, S paramS) {
/*  189 */     return paramVisitor.visitType(this, paramS);
/*      */   }
/*      */ 
/*      */   public Type(Symbol.TypeSymbol paramTypeSymbol)
/*      */   {
/*  194 */     this.tsym = paramTypeSymbol;
/*      */   }
/*      */ 
/*      */   public Type map(Mapping paramMapping)
/*      */   {
/*  213 */     return this;
/*      */   }
/*      */ 
/*      */   public static com.sun.tools.javac.util.List<Type> map(com.sun.tools.javac.util.List<Type> paramList, Mapping paramMapping)
/*      */   {
/*  219 */     if (paramList.nonEmpty()) {
/*  220 */       com.sun.tools.javac.util.List localList = map(paramList.tail, paramMapping);
/*  221 */       Type localType = paramMapping.apply((Type)paramList.head);
/*  222 */       if ((localList != paramList.tail) || (localType != paramList.head))
/*  223 */         return localList.prepend(localType);
/*      */     }
/*  225 */     return paramList;
/*      */   }
/*      */ 
/*      */   public Type constType(Object paramObject)
/*      */   {
/*  232 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public Type baseType()
/*      */   {
/*  240 */     return this;
/*      */   }
/*      */ 
/*      */   public Type annotatedType(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList) {
/*  244 */     return new AnnotatedType(paramList, this);
/*      */   }
/*      */ 
/*      */   public boolean isAnnotated() {
/*  248 */     return false;
/*      */   }
/*      */ 
/*      */   public Type unannotatedType()
/*      */   {
/*  256 */     return this;
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.TypeCompound> getAnnotationMirrors()
/*      */   {
/*  261 */     return com.sun.tools.javac.util.List.nil();
/*      */   }
/*      */ 
/*      */   public <A extends Annotation> A getAnnotation(Class<A> paramClass)
/*      */   {
/*  267 */     return null;
/*      */   }
/*      */ 
/*      */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> paramClass)
/*      */   {
/*  274 */     Annotation[] arrayOfAnnotation = (Annotation[])Array.newInstance(paramClass, 0);
/*  275 */     return arrayOfAnnotation;
/*      */   }
/*      */ 
/*      */   public static com.sun.tools.javac.util.List<Type> baseTypes(com.sun.tools.javac.util.List<Type> paramList)
/*      */   {
/*  281 */     if (paramList.nonEmpty()) {
/*  282 */       Type localType = ((Type)paramList.head).baseType();
/*  283 */       com.sun.tools.javac.util.List localList = baseTypes(paramList.tail);
/*  284 */       if ((localType != paramList.head) || (localList != paramList.tail))
/*  285 */         return localList.prepend(localType);
/*      */     }
/*  287 */     return paramList;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  295 */     String str = (this.tsym == null) || (this.tsym.name == null) ? "<none>" : this.tsym.name
/*  295 */       .toString();
/*  296 */     if ((moreInfo) && (hasTag(TypeTag.TYPEVAR))) {
/*  297 */       str = str + hashCode();
/*      */     }
/*  299 */     return str;
/*      */   }
/*      */ 
/*      */   public static String toString(com.sun.tools.javac.util.List<Type> paramList)
/*      */   {
/*  308 */     if (paramList.isEmpty()) {
/*  309 */       return "";
/*      */     }
/*  311 */     StringBuilder localStringBuilder = new StringBuilder();
/*  312 */     localStringBuilder.append(((Type)paramList.head).toString());
/*  313 */     for (com.sun.tools.javac.util.List localList = paramList.tail; localList.nonEmpty(); localList = localList.tail)
/*  314 */       localStringBuilder.append(",").append(((Type)localList.head).toString());
/*  315 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String stringValue()
/*      */   {
/*  323 */     Object localObject = Assert.checkNonNull(constValue());
/*  324 */     return localObject.toString();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  334 */     return super.equals(paramObject);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  339 */     return super.hashCode();
/*      */   }
/*      */ 
/*      */   public String argtypes(boolean paramBoolean) {
/*  343 */     com.sun.tools.javac.util.List localList = getParameterTypes();
/*  344 */     if (!paramBoolean) return localList.toString();
/*  345 */     StringBuilder localStringBuilder = new StringBuilder();
/*  346 */     while (localList.tail.nonEmpty()) {
/*  347 */       localStringBuilder.append(localList.head);
/*  348 */       localList = localList.tail;
/*  349 */       localStringBuilder.append(',');
/*      */     }
/*  351 */     if (((Type)localList.head).unannotatedType().hasTag(TypeTag.ARRAY)) {
/*  352 */       localStringBuilder.append(((ArrayType)((Type)localList.head).unannotatedType()).elemtype);
/*  353 */       if (((Type)localList.head).getAnnotationMirrors().nonEmpty()) {
/*  354 */         localStringBuilder.append(((Type)localList.head).getAnnotationMirrors());
/*      */       }
/*  356 */       localStringBuilder.append("...");
/*      */     } else {
/*  358 */       localStringBuilder.append(localList.head);
/*      */     }
/*  360 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Type> getTypeArguments()
/*      */   {
/*  365 */     return com.sun.tools.javac.util.List.nil(); } 
/*  366 */   public Type getEnclosingType() { return null; } 
/*  367 */   public com.sun.tools.javac.util.List<Type> getParameterTypes() { return com.sun.tools.javac.util.List.nil(); } 
/*  368 */   public Type getReturnType() { return null; } 
/*  369 */   public Type getReceiverType() { return null; } 
/*  370 */   public com.sun.tools.javac.util.List<Type> getThrownTypes() { return com.sun.tools.javac.util.List.nil(); } 
/*  371 */   public Type getUpperBound() { return null; } 
/*  372 */   public Type getLowerBound() { return null; }
/*      */ 
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Type> allparams()
/*      */   {
/*  381 */     return com.sun.tools.javac.util.List.nil();
/*      */   }
/*      */ 
/*      */   public boolean isErroneous()
/*      */   {
/*  386 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isErroneous(com.sun.tools.javac.util.List<Type> paramList) {
/*  390 */     for (Object localObject = paramList; ((com.sun.tools.javac.util.List)localObject).nonEmpty(); localObject = ((com.sun.tools.javac.util.List)localObject).tail)
/*  391 */       if (((Type)((com.sun.tools.javac.util.List)localObject).head).isErroneous()) return true;
/*  392 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isParameterized()
/*      */   {
/*  401 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isRaw()
/*      */   {
/*  412 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isCompound() {
/*  416 */     if (this.tsym.completer == null);
/*  421 */     return (this.tsym
/*  421 */       .flags() & 0x1000000) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isIntersection() {
/*  425 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isUnion() {
/*  429 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isInterface() {
/*  433 */     return (this.tsym.flags() & 0x200) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isFinal() {
/*  437 */     return (this.tsym.flags() & 0x10) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean contains(Type paramType)
/*      */   {
/*  444 */     return paramType == this;
/*      */   }
/*      */ 
/*      */   public static boolean contains(com.sun.tools.javac.util.List<Type> paramList, Type paramType) {
/*  448 */     for (Object localObject = paramList; 
/*  449 */       ((com.sun.tools.javac.util.List)localObject).tail != null; 
/*  450 */       localObject = ((com.sun.tools.javac.util.List)localObject).tail)
/*  451 */       if (((Type)((com.sun.tools.javac.util.List)localObject).head).contains(paramType)) return true;
/*  452 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean containsAny(com.sun.tools.javac.util.List<Type> paramList)
/*      */   {
/*  458 */     for (Type localType : paramList)
/*  459 */       if (contains(localType)) return true;
/*  460 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean containsAny(com.sun.tools.javac.util.List<Type> paramList1, com.sun.tools.javac.util.List<Type> paramList2) {
/*  464 */     for (Type localType : paramList1)
/*  465 */       if (localType.containsAny(paramList2)) return true;
/*  466 */     return false;
/*      */   }
/*      */ 
/*      */   public static com.sun.tools.javac.util.List<Type> filter(com.sun.tools.javac.util.List<Type> paramList, Filter<Type> paramFilter) {
/*  470 */     ListBuffer localListBuffer = new ListBuffer();
/*  471 */     for (Type localType : paramList) {
/*  472 */       if (paramFilter.accepts(localType)) {
/*  473 */         localListBuffer.append(localType);
/*      */       }
/*      */     }
/*  476 */     return localListBuffer.toList();
/*      */   }
/*      */   public boolean isSuperBound() {
/*  479 */     return false; } 
/*  480 */   public boolean isExtendsBound() { return false; } 
/*  481 */   public boolean isUnbound() { return false; } 
/*  482 */   public Type withTypeVar(Type paramType) { return this; }
/*      */ 
/*      */   public MethodType asMethodType()
/*      */   {
/*  486 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public void complete() {
/*      */   }
/*      */ 
/*      */   public Symbol.TypeSymbol asElement() {
/*  493 */     return this.tsym;
/*      */   }
/*      */ 
/*      */   public TypeKind getKind()
/*      */   {
/*  498 */     return TypeKind.OTHER;
/*      */   }
/*      */ 
/*      */   public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */   {
/*  503 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public static class AnnotatedType extends Type
/*      */     implements ArrayType, DeclaredType, PrimitiveType, TypeVariable, WildcardType
/*      */   {
/*      */     private com.sun.tools.javac.util.List<Attribute.TypeCompound> typeAnnotations;
/*      */     private Type underlyingType;
/*      */ 
/*      */     protected AnnotatedType(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList, Type paramType)
/*      */     {
/* 1876 */       super();
/* 1877 */       this.typeAnnotations = paramList;
/* 1878 */       this.underlyingType = paramType;
/* 1879 */       Assert.check((paramList != null) && (paramList.nonEmpty()), "Can't create AnnotatedType without annotations: " + paramType);
/*      */ 
/* 1881 */       Assert.check(!paramType.isAnnotated(), "Can't annotate already annotated type: " + paramType + "; adding: " + paramList);
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1888 */       return this.underlyingType.getTag();
/*      */     }
/*      */ 
/*      */     public boolean isAnnotated()
/*      */     {
/* 1893 */       return true;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Attribute.TypeCompound> getAnnotationMirrors()
/*      */     {
/* 1898 */       return this.typeAnnotations;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/* 1904 */       return this.underlyingType.getKind();
/*      */     }
/*      */ 
/*      */     public Type unannotatedType()
/*      */     {
/* 1909 */       return this.underlyingType;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1914 */       return paramVisitor.visitAnnotatedType(this, paramS);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/* 1919 */       return this.underlyingType.accept(paramTypeVisitor, paramP);
/*      */     }
/*      */ 
/*      */     public Type map(Type.Mapping paramMapping)
/*      */     {
/* 1924 */       this.underlyingType.map(paramMapping);
/* 1925 */       return this;
/*      */     }
/*      */ 
/*      */     public Type constType(Object paramObject) {
/* 1929 */       return this.underlyingType.constType(paramObject);
/*      */     }
/* 1931 */     public Type getEnclosingType() { return this.underlyingType.getEnclosingType(); }
/*      */ 
/*      */     public Type getReturnType() {
/* 1934 */       return this.underlyingType.getReturnType();
/*      */     }
/* 1936 */     public com.sun.tools.javac.util.List<Type> getTypeArguments() { return this.underlyingType.getTypeArguments(); } 
/*      */     public com.sun.tools.javac.util.List<Type> getParameterTypes() {
/* 1938 */       return this.underlyingType.getParameterTypes();
/*      */     }
/* 1940 */     public Type getReceiverType() { return this.underlyingType.getReceiverType(); } 
/*      */     public com.sun.tools.javac.util.List<Type> getThrownTypes() {
/* 1942 */       return this.underlyingType.getThrownTypes();
/*      */     }
/* 1944 */     public Type getUpperBound() { return this.underlyingType.getUpperBound(); } 
/*      */     public Type getLowerBound() {
/* 1946 */       return this.underlyingType.getLowerBound();
/*      */     }
/*      */     public boolean isErroneous() {
/* 1949 */       return this.underlyingType.isErroneous();
/*      */     }
/* 1951 */     public boolean isCompound() { return this.underlyingType.isCompound(); } 
/*      */     public boolean isInterface() {
/* 1953 */       return this.underlyingType.isInterface();
/*      */     }
/* 1955 */     public com.sun.tools.javac.util.List<Type> allparams() { return this.underlyingType.allparams(); } 
/*      */     public boolean isPrimitive() {
/* 1957 */       return this.underlyingType.isPrimitive();
/*      */     }
/* 1959 */     public boolean isPrimitiveOrVoid() { return this.underlyingType.isPrimitiveOrVoid(); } 
/*      */     public boolean isNumeric() {
/* 1961 */       return this.underlyingType.isNumeric();
/*      */     }
/* 1963 */     public boolean isReference() { return this.underlyingType.isReference(); } 
/*      */     public boolean isNullOrReference() {
/* 1965 */       return this.underlyingType.isNullOrReference();
/*      */     }
/* 1967 */     public boolean isPartial() { return this.underlyingType.isPartial(); } 
/*      */     public boolean isParameterized() {
/* 1969 */       return this.underlyingType.isParameterized();
/*      */     }
/* 1971 */     public boolean isRaw() { return this.underlyingType.isRaw(); } 
/*      */     public boolean isFinal() {
/* 1973 */       return this.underlyingType.isFinal();
/*      */     }
/* 1975 */     public boolean isSuperBound() { return this.underlyingType.isSuperBound(); } 
/*      */     public boolean isExtendsBound() {
/* 1977 */       return this.underlyingType.isExtendsBound();
/*      */     }
/* 1979 */     public boolean isUnbound() { return this.underlyingType.isUnbound(); }
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1987 */       if ((this.typeAnnotations != null) && 
/* 1988 */         (!this.typeAnnotations
/* 1988 */         .isEmpty())) {
/* 1989 */         return "(" + this.typeAnnotations.toString() + " :: " + this.underlyingType.toString() + ")";
/*      */       }
/* 1991 */       return "({} :: " + this.underlyingType.toString() + ")";
/*      */     }
/*      */ 
/*      */     public boolean contains(Type paramType)
/*      */     {
/* 1996 */       return this.underlyingType.contains(paramType);
/*      */     }
/*      */ 
/*      */     public Type withTypeVar(Type paramType)
/*      */     {
/* 2002 */       this.underlyingType = this.underlyingType.withTypeVar(paramType);
/* 2003 */       return this;
/*      */     }
/*      */ 
/*      */     public Symbol.TypeSymbol asElement()
/*      */     {
/* 2008 */       return this.underlyingType.asElement();
/*      */     }
/*      */ 
/*      */     public Type.MethodType asMethodType() {
/* 2012 */       return this.underlyingType.asMethodType();
/*      */     }
/*      */     public void complete() {
/* 2015 */       this.underlyingType.complete();
/*      */     }
/*      */     public TypeMirror getComponentType() {
/* 2018 */       return ((Type.ArrayType)this.underlyingType).getComponentType();
/*      */     }
/*      */ 
/*      */     public Type makeVarargs() {
/* 2022 */       return ((Type.ArrayType)this.underlyingType).makeVarargs().annotatedType(this.typeAnnotations);
/*      */     }
/*      */ 
/*      */     public TypeMirror getExtendsBound() {
/* 2026 */       return ((Type.WildcardType)this.underlyingType).getExtendsBound();
/*      */     }
/* 2028 */     public TypeMirror getSuperBound() { return ((Type.WildcardType)this.underlyingType).getSuperBound(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   public static class ArrayType extends Type
/*      */     implements ArrayType
/*      */   {
/*      */     public Type elemtype;
/*      */ 
/*      */     public ArrayType(Type paramType, Symbol.TypeSymbol paramTypeSymbol)
/*      */     {
/* 1047 */       super();
/* 1048 */       this.elemtype = paramType;
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1053 */       return TypeTag.ARRAY;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS) {
/* 1057 */       return paramVisitor.visitArrayType(this, paramS);
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1061 */       return this.elemtype + "[]";
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 1065 */       if (this != paramObject) if (!(paramObject instanceof ArrayType)) {
/*      */           break label33;
/*      */         }
/* 1068 */       label33: return this.elemtype
/* 1068 */         .equals(((ArrayType)paramObject).elemtype);
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1072 */       return (TypeTag.ARRAY.ordinal() << 5) + this.elemtype.hashCode();
/*      */     }
/*      */ 
/*      */     public boolean isVarargs() {
/* 1076 */       return false;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<Type> allparams() {
/* 1079 */       return this.elemtype.allparams();
/*      */     }
/*      */     public boolean isErroneous() {
/* 1082 */       return this.elemtype.isErroneous();
/*      */     }
/*      */ 
/*      */     public boolean isParameterized() {
/* 1086 */       return this.elemtype.isParameterized();
/*      */     }
/*      */ 
/*      */     public boolean isReference()
/*      */     {
/* 1091 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isNullOrReference()
/*      */     {
/* 1096 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isRaw() {
/* 1100 */       return this.elemtype.isRaw();
/*      */     }
/*      */ 
/*      */     public ArrayType makeVarargs() {
/* 1104 */       return new ArrayType(this.elemtype, this.tsym)
/*      */       {
/*      */         public boolean isVarargs() {
/* 1107 */           return true;
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public Type map(Type.Mapping paramMapping) {
/* 1113 */       Type localType = paramMapping.apply(this.elemtype);
/* 1114 */       if (localType == this.elemtype) return this;
/* 1115 */       return new ArrayType(localType, this.tsym);
/*      */     }
/*      */ 
/*      */     public boolean contains(Type paramType) {
/* 1119 */       return (paramType == this) || (this.elemtype.contains(paramType));
/*      */     }
/*      */ 
/*      */     public void complete() {
/* 1123 */       this.elemtype.complete();
/*      */     }
/*      */ 
/*      */     public Type getComponentType() {
/* 1127 */       return this.elemtype;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind() {
/* 1131 */       return TypeKind.ARRAY;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1135 */       return paramTypeVisitor.visitArray(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BottomType extends Type
/*      */     implements NullType
/*      */   {
/*      */     public BottomType()
/*      */     {
/* 1747 */       super();
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1752 */       return TypeTag.BOT;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/* 1757 */       return TypeKind.NULL;
/*      */     }
/*      */ 
/*      */     public boolean isCompound() {
/* 1761 */       return false;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1765 */       return paramTypeVisitor.visitNull(this, paramP);
/*      */     }
/*      */ 
/*      */     public Type constType(Object paramObject)
/*      */     {
/* 1770 */       return this;
/*      */     }
/*      */ 
/*      */     public String stringValue()
/*      */     {
/* 1775 */       return "null";
/*      */     }
/*      */ 
/*      */     public boolean isNullOrReference()
/*      */     {
/* 1780 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CapturedType extends Type.TypeVar
/*      */   {
/*      */     public Type.WildcardType wildcard;
/*      */ 
/*      */     public CapturedType(Name paramName, Symbol paramSymbol, Type paramType1, Type paramType2, Type.WildcardType paramWildcardType)
/*      */     {
/* 1354 */       super(paramSymbol, paramType2);
/* 1355 */       this.lower = ((Type)Assert.checkNonNull(paramType2));
/* 1356 */       this.bound = paramType1;
/* 1357 */       this.wildcard = paramWildcardType;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1362 */       return paramVisitor.visitCapturedType(this, paramS);
/*      */     }
/*      */ 
/*      */     public boolean isCaptured()
/*      */     {
/* 1367 */       return true;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1373 */       return "capture#" + 
/* 1373 */         (hashCode() & 0xFFFFFFFF) % 997L + " of " + this.wildcard;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CapturedUndetVar extends Type.UndetVar
/*      */   {
/*      */     public CapturedUndetVar(Type.CapturedType paramCapturedType, Types paramTypes)
/*      */     {
/* 1667 */       super(paramTypes);
/* 1668 */       if (!paramCapturedType.lower.hasTag(TypeTag.BOT))
/* 1669 */         this.bounds.put(Type.UndetVar.InferenceBound.LOWER, com.sun.tools.javac.util.List.of(paramCapturedType.lower));
/*      */     }
/*      */ 
/*      */     public void addBound(Type.UndetVar.InferenceBound paramInferenceBound, Type paramType, Types paramTypes, boolean paramBoolean)
/*      */     {
/* 1675 */       if (paramBoolean)
/*      */       {
/* 1677 */         super.addBound(paramInferenceBound, paramType, paramTypes, paramBoolean);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isCaptured()
/*      */     {
/* 1683 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ClassType extends Type
/*      */     implements DeclaredType
/*      */   {
/*      */     private Type outer_field;
/*      */     public com.sun.tools.javac.util.List<Type> typarams_field;
/*      */     public com.sun.tools.javac.util.List<Type> allparams_field;
/*      */     public Type supertype_field;
/*      */     public com.sun.tools.javac.util.List<Type> interfaces_field;
/*      */     public com.sun.tools.javac.util.List<Type> all_interfaces_field;
/*  903 */     int rank_field = -1;
/*      */ 
/*      */     public ClassType(Type paramType, com.sun.tools.javac.util.List<Type> paramList, Symbol.TypeSymbol paramTypeSymbol)
/*      */     {
/*  762 */       super();
/*  763 */       this.outer_field = paramType;
/*  764 */       this.typarams_field = paramList;
/*  765 */       this.allparams_field = null;
/*  766 */       this.supertype_field = null;
/*  767 */       this.interfaces_field = null;
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/*  781 */       return TypeTag.CLASS;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/*  786 */       return paramVisitor.visitClassType(this, paramS);
/*      */     }
/*      */ 
/*      */     public Type constType(Object paramObject) {
/*  790 */       final Object localObject = paramObject;
/*  791 */       return new ClassType(getEnclosingType(), this.typarams_field, this.tsym)
/*      */       {
/*      */         public Object constValue() {
/*  794 */           return localObject;
/*      */         }
/*      */ 
/*      */         public Type baseType() {
/*  798 */           return this.tsym.type;
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  806 */       StringBuilder localStringBuilder = new StringBuilder();
/*  807 */       if ((getEnclosingType().hasTag(TypeTag.CLASS)) && (this.tsym.owner.kind == 2)) {
/*  808 */         localStringBuilder.append(getEnclosingType().toString());
/*  809 */         localStringBuilder.append(".");
/*  810 */         localStringBuilder.append(className(this.tsym, false));
/*      */       } else {
/*  812 */         localStringBuilder.append(className(this.tsym, true));
/*      */       }
/*  814 */       if (getTypeArguments().nonEmpty()) {
/*  815 */         localStringBuilder.append('<');
/*  816 */         localStringBuilder.append(getTypeArguments().toString());
/*  817 */         localStringBuilder.append(">");
/*      */       }
/*  819 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     private String className(Symbol paramSymbol, boolean paramBoolean)
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  823 */       if ((paramSymbol.name.isEmpty()) && ((paramSymbol.flags() & 0x1000000) != 0L)) {
/*  824 */         localObject1 = new StringBuilder(this.supertype_field.toString());
/*  825 */         for (localObject2 = this.interfaces_field; ((com.sun.tools.javac.util.List)localObject2).nonEmpty(); localObject2 = ((com.sun.tools.javac.util.List)localObject2).tail) {
/*  826 */           ((StringBuilder)localObject1).append("&");
/*  827 */           ((StringBuilder)localObject1).append(((Type)((com.sun.tools.javac.util.List)localObject2).head).toString());
/*      */         }
/*  829 */         return ((StringBuilder)localObject1).toString();
/*  830 */       }if (paramSymbol.name.isEmpty())
/*      */       {
/*  832 */         localObject2 = (ClassType)this.tsym.type.unannotatedType();
/*  833 */         if (localObject2 == null)
/*  834 */           localObject1 = Log.getLocalizedString("anonymous.class", new Object[] { null });
/*  835 */         else if ((((ClassType)localObject2).interfaces_field != null) && (((ClassType)localObject2).interfaces_field.nonEmpty())) {
/*  836 */           localObject1 = Log.getLocalizedString("anonymous.class", new Object[] { ((ClassType)localObject2).interfaces_field.head });
/*      */         }
/*      */         else {
/*  839 */           localObject1 = Log.getLocalizedString("anonymous.class", new Object[] { ((ClassType)localObject2).supertype_field });
/*      */         }
/*      */ 
/*  842 */         if (moreInfo)
/*  843 */           localObject1 = (String)localObject1 + String.valueOf(paramSymbol.hashCode());
/*  844 */         return localObject1;
/*  845 */       }if (paramBoolean) {
/*  846 */         return paramSymbol.getQualifiedName().toString();
/*      */       }
/*  848 */       return paramSymbol.name.toString();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getTypeArguments()
/*      */     {
/*  853 */       if (this.typarams_field == null) {
/*  854 */         complete();
/*  855 */         if (this.typarams_field == null)
/*  856 */           this.typarams_field = com.sun.tools.javac.util.List.nil();
/*      */       }
/*  858 */       return this.typarams_field;
/*      */     }
/*      */ 
/*      */     public boolean hasErasedSupertypes() {
/*  862 */       return isRaw();
/*      */     }
/*      */ 
/*      */     public Type getEnclosingType() {
/*  866 */       return this.outer_field;
/*      */     }
/*      */ 
/*      */     public void setEnclosingType(Type paramType) {
/*  870 */       this.outer_field = paramType;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> allparams() {
/*  874 */       if (this.allparams_field == null) {
/*  875 */         this.allparams_field = getTypeArguments().prependList(getEnclosingType().allparams());
/*      */       }
/*  877 */       return this.allparams_field;
/*      */     }
/*      */ 
/*      */     public boolean isErroneous()
/*      */     {
/*  884 */       return (getEnclosingType().isErroneous()) || 
/*  883 */         (isErroneous(getTypeArguments())) || (
/*  884 */         (this != this.tsym.type
/*  884 */         .unannotatedType()) && (this.tsym.type.isErroneous()));
/*      */     }
/*      */ 
/*      */     public boolean isParameterized() {
/*  888 */       return allparams().tail != null;
/*      */     }
/*      */ 
/*      */     public boolean isReference()
/*      */     {
/*  894 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isNullOrReference()
/*      */     {
/*  899 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isRaw()
/*      */     {
/*  911 */       if (this != this.tsym.type);
/*  914 */       return (this.tsym.type
/*  913 */         .allparams().nonEmpty()) && 
/*  914 */         (allparams().isEmpty());
/*      */     }
/*      */ 
/*      */     public Type map(Type.Mapping paramMapping) {
/*  918 */       Type localType1 = getEnclosingType();
/*  919 */       Type localType2 = paramMapping.apply(localType1);
/*  920 */       com.sun.tools.javac.util.List localList1 = getTypeArguments();
/*  921 */       com.sun.tools.javac.util.List localList2 = map(localList1, paramMapping);
/*  922 */       if ((localType2 == localType1) && (localList2 == localList1)) return this;
/*  923 */       return new ClassType(localType2, localList2, this.tsym);
/*      */     }
/*      */ 
/*      */     public boolean contains(Type paramType)
/*      */     {
/*  932 */       return (paramType == this) || 
/*  929 */         ((isParameterized()) && (
/*  930 */         (getEnclosingType().contains(paramType)) || (contains(getTypeArguments(), paramType)))) || (
/*  931 */         (isCompound()) && (
/*  932 */         (this.supertype_field
/*  932 */         .contains(paramType)) || 
/*  932 */         (contains(this.interfaces_field, paramType))));
/*      */     }
/*      */ 
/*      */     public void complete() {
/*  936 */       if (this.tsym.completer != null) this.tsym.complete(); 
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/*  940 */       return TypeKind.DECLARED;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/*  944 */       return paramTypeVisitor.visitDeclared(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class DelegatedType extends Type
/*      */   {
/*      */     public Type qtype;
/*      */     public TypeTag tag;
/*      */ 
/*      */     public DelegatedType(TypeTag paramTypeTag, Type paramType)
/*      */     {
/* 1383 */       super();
/* 1384 */       this.tag = paramTypeTag;
/* 1385 */       this.qtype = paramType;
/*      */     }
/* 1387 */     public TypeTag getTag() { return this.tag; } 
/* 1388 */     public String toString() { return this.qtype.toString(); } 
/* 1389 */     public com.sun.tools.javac.util.List<Type> getTypeArguments() { return this.qtype.getTypeArguments(); } 
/* 1390 */     public Type getEnclosingType() { return this.qtype.getEnclosingType(); } 
/* 1391 */     public com.sun.tools.javac.util.List<Type> getParameterTypes() { return this.qtype.getParameterTypes(); } 
/* 1392 */     public Type getReturnType() { return this.qtype.getReturnType(); } 
/* 1393 */     public Type getReceiverType() { return this.qtype.getReceiverType(); } 
/* 1394 */     public com.sun.tools.javac.util.List<Type> getThrownTypes() { return this.qtype.getThrownTypes(); } 
/* 1395 */     public com.sun.tools.javac.util.List<Type> allparams() { return this.qtype.allparams(); } 
/* 1396 */     public Type getUpperBound() { return this.qtype.getUpperBound(); } 
/* 1397 */     public boolean isErroneous() { return this.qtype.isErroneous(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   public static class ErasedClassType extends Type.ClassType
/*      */   {
/*      */     public ErasedClassType(Type paramType, Symbol.TypeSymbol paramTypeSymbol)
/*      */     {
/*  950 */       super(com.sun.tools.javac.util.List.nil(), paramTypeSymbol);
/*      */     }
/*      */ 
/*      */     public boolean hasErasedSupertypes()
/*      */     {
/*  955 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ErrorType extends Type.ClassType
/*      */     implements ErrorType
/*      */   {
/* 1788 */     private Type originalType = null;
/*      */ 
/*      */     public ErrorType(Type paramType, Symbol.TypeSymbol paramTypeSymbol) {
/* 1791 */       super(com.sun.tools.javac.util.List.nil(), null);
/* 1792 */       this.tsym = paramTypeSymbol;
/* 1793 */       this.originalType = (paramType == null ? noType : paramType);
/*      */     }
/*      */ 
/*      */     public ErrorType(Symbol.ClassSymbol paramClassSymbol, Type paramType) {
/* 1797 */       this(paramType, paramClassSymbol);
/* 1798 */       paramClassSymbol.type = this;
/* 1799 */       paramClassSymbol.kind = 63;
/* 1800 */       paramClassSymbol.members_field = new Scope.ErrorScope(paramClassSymbol);
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1805 */       return TypeTag.ERROR;
/*      */     }
/*      */ 
/*      */     public boolean isPartial()
/*      */     {
/* 1810 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isReference()
/*      */     {
/* 1815 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isNullOrReference()
/*      */     {
/* 1820 */       return true;
/*      */     }
/*      */ 
/*      */     public ErrorType(Name paramName, Symbol.TypeSymbol paramTypeSymbol, Type paramType) {
/* 1824 */       this(new Symbol.ClassSymbol(1073741833L, paramName, null, paramTypeSymbol), paramType);
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1829 */       return paramVisitor.visitErrorType(this, paramS);
/*      */     }
/*      */     public Type constType(Object paramObject) {
/* 1832 */       return this; } 
/* 1833 */     public Type getEnclosingType() { return this; } 
/* 1834 */     public Type getReturnType() { return this; } 
/* 1835 */     public Type asSub(Symbol paramSymbol) { return this; } 
/* 1836 */     public Type map(Type.Mapping paramMapping) { return this; } 
/*      */     public boolean isGenType(Type paramType) {
/* 1838 */       return true; } 
/* 1839 */     public boolean isErroneous() { return true; } 
/* 1840 */     public boolean isCompound() { return false; } 
/* 1841 */     public boolean isInterface() { return false; } 
/*      */     public com.sun.tools.javac.util.List<Type> allparams() {
/* 1843 */       return com.sun.tools.javac.util.List.nil(); } 
/* 1844 */     public com.sun.tools.javac.util.List<Type> getTypeArguments() { return com.sun.tools.javac.util.List.nil(); }
/*      */ 
/*      */     public TypeKind getKind() {
/* 1847 */       return TypeKind.ERROR;
/*      */     }
/*      */ 
/*      */     public Type getOriginalType() {
/* 1851 */       return this.originalType;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1855 */       return paramTypeVisitor.visitError(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ForAll extends Type.DelegatedType
/*      */     implements ExecutableType
/*      */   {
/*      */     public com.sun.tools.javac.util.List<Type> tvars;
/*      */ 
/*      */     public ForAll(com.sun.tools.javac.util.List<Type> paramList, Type paramType)
/*      */     {
/* 1409 */       super((Type.MethodType)paramType);
/* 1410 */       this.tvars = paramList;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1415 */       return paramVisitor.visitForAll(this, paramS);
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1419 */       return "<" + this.tvars + ">" + this.qtype;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<Type> getTypeArguments() {
/* 1422 */       return this.tvars;
/*      */     }
/*      */     public boolean isErroneous() {
/* 1425 */       return this.qtype.isErroneous();
/*      */     }
/*      */ 
/*      */     public Type map(Type.Mapping paramMapping) {
/* 1429 */       return paramMapping.apply(this.qtype);
/*      */     }
/*      */ 
/*      */     public boolean contains(Type paramType) {
/* 1433 */       return this.qtype.contains(paramType);
/*      */     }
/*      */ 
/*      */     public Type.MethodType asMethodType() {
/* 1437 */       return (Type.MethodType)this.qtype;
/*      */     }
/*      */ 
/*      */     public void complete() {
/* 1441 */       for (com.sun.tools.javac.util.List localList = this.tvars; localList.nonEmpty(); localList = localList.tail) {
/* 1442 */         ((Type.TypeVar)localList.head).bound.complete();
/*      */       }
/* 1444 */       this.qtype.complete();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type.TypeVar> getTypeVariables() {
/* 1448 */       return com.sun.tools.javac.util.List.convert(Type.TypeVar.class, getTypeArguments());
/*      */     }
/*      */ 
/*      */     public TypeKind getKind() {
/* 1452 */       return TypeKind.EXECUTABLE;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1456 */       return paramTypeVisitor.visitExecutable(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class IntersectionClassType extends Type.ClassType
/*      */     implements IntersectionType
/*      */   {
/*      */     public boolean allInterfaces;
/*      */ 
/*      */     public IntersectionClassType(com.sun.tools.javac.util.List<Type> paramList, Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean)
/*      */     {
/* 1002 */       super(com.sun.tools.javac.util.List.nil(), paramClassSymbol);
/* 1003 */       this.allInterfaces = paramBoolean;
/* 1004 */       Assert.check((paramClassSymbol.flags() & 0x1000000) != 0L);
/* 1005 */       this.supertype_field = ((Type)paramList.head);
/* 1006 */       this.interfaces_field = paramList.tail;
/* 1007 */       Assert.check((this.supertype_field.tsym.completer != null) || 
/* 1008 */         (!this.supertype_field
/* 1008 */         .isInterface()), this.supertype_field);
/*      */     }
/*      */ 
/*      */     public java.util.List<? extends TypeMirror> getBounds() {
/* 1012 */       return Collections.unmodifiableList(getExplicitComponents());
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getComponents() {
/* 1016 */       return this.interfaces_field.prepend(this.supertype_field);
/*      */     }
/*      */ 
/*      */     public boolean isIntersection()
/*      */     {
/* 1021 */       return true;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getExplicitComponents()
/*      */     {
/* 1027 */       return this.allInterfaces ? this.interfaces_field : 
/* 1027 */         getComponents();
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/* 1032 */       return TypeKind.INTERSECTION;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/* 1037 */       return paramTypeVisitor.visitIntersection(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCNoType extends Type
/*      */     implements NoType
/*      */   {
/*      */     public JCNoType()
/*      */     {
/* 1691 */       super();
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1696 */       return TypeTag.NONE;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/* 1701 */       return TypeKind.NONE;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/* 1706 */       return paramTypeVisitor.visitNoType(this, paramP);
/*      */     }
/*      */ 
/*      */     public boolean isCompound() {
/* 1710 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCPrimitiveType extends Type
/*      */     implements PrimitiveType
/*      */   {
/*      */     TypeTag tag;
/*      */ 
/*      */     public JCPrimitiveType(TypeTag paramTypeTag, Symbol.TypeSymbol paramTypeSymbol)
/*      */     {
/*  512 */       super();
/*  513 */       this.tag = paramTypeTag;
/*  514 */       Assert.check(paramTypeTag.isPrimitive);
/*      */     }
/*      */ 
/*      */     public boolean isNumeric()
/*      */     {
/*  519 */       return this.tag != TypeTag.BOOLEAN;
/*      */     }
/*      */ 
/*      */     public boolean isPrimitive()
/*      */     {
/*  524 */       return true;
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/*  529 */       return this.tag;
/*      */     }
/*      */ 
/*      */     public boolean isPrimitiveOrVoid()
/*      */     {
/*  534 */       return true;
/*      */     }
/*      */ 
/*      */     public Type constType(Object paramObject)
/*      */     {
/*  542 */       final Object localObject = paramObject;
/*  543 */       return new JCPrimitiveType(this.tag, this.tsym)
/*      */       {
/*      */         public Object constValue() {
/*  546 */           return localObject;
/*      */         }
/*      */ 
/*      */         public Type baseType() {
/*  550 */           return this.tsym.type;
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public String stringValue()
/*      */     {
/*  560 */       Object localObject = Assert.checkNonNull(constValue());
/*  561 */       if (this.tag == TypeTag.BOOLEAN) {
/*  562 */         return ((Integer)localObject).intValue() == 0 ? "false" : "true";
/*      */       }
/*  564 */       if (this.tag == TypeTag.CHAR) {
/*  565 */         return String.valueOf((char)((Integer)localObject).intValue());
/*      */       }
/*      */ 
/*  568 */       return localObject.toString();
/*      */     }
/*      */ 
/*      */     public boolean isFalse()
/*      */     {
/*  576 */       if (this.tag == TypeTag.BOOLEAN);
/*  579 */       return (constValue() != null) && 
/*  579 */         (((Integer)constValue()).intValue() == 0);
/*      */     }
/*      */ 
/*      */     public boolean isTrue()
/*      */     {
/*  586 */       if (this.tag == TypeTag.BOOLEAN);
/*  589 */       return (constValue() != null) && 
/*  589 */         (((Integer)constValue()).intValue() != 0);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/*  594 */       return paramTypeVisitor.visitPrimitive(this, paramP);
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/*  599 */       switch (Type.4.$SwitchMap$com$sun$tools$javac$code$TypeTag[this.tag.ordinal()]) { case 1:
/*  600 */         return TypeKind.BYTE;
/*      */       case 2:
/*  601 */         return TypeKind.CHAR;
/*      */       case 3:
/*  602 */         return TypeKind.SHORT;
/*      */       case 4:
/*  603 */         return TypeKind.INT;
/*      */       case 5:
/*  604 */         return TypeKind.LONG;
/*      */       case 6:
/*  605 */         return TypeKind.FLOAT;
/*      */       case 7:
/*  606 */         return TypeKind.DOUBLE;
/*      */       case 8:
/*  607 */         return TypeKind.BOOLEAN;
/*      */       }
/*  609 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCVoidType extends Type
/*      */     implements NoType
/*      */   {
/*      */     public JCVoidType()
/*      */     {
/* 1718 */       super();
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1723 */       return TypeTag.VOID;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/* 1728 */       return TypeKind.VOID;
/*      */     }
/*      */ 
/*      */     public boolean isCompound() {
/* 1732 */       return false;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1736 */       return paramTypeVisitor.visitNoType(this, paramP);
/*      */     }
/*      */ 
/*      */     public boolean isPrimitiveOrVoid()
/*      */     {
/* 1741 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Mapping
/*      */   {
/*      */     private String name;
/*      */ 
/*      */     public Mapping(String paramString)
/*      */     {
/*  202 */       this.name = paramString;
/*      */     }
/*      */     public abstract Type apply(Type paramType);
/*      */ 
/*  206 */     public String toString() { return this.name; }
/*      */ 
/*      */   }
/*      */ 
/*      */   public static class MethodType extends Type
/*      */     implements ExecutableType
/*      */   {
/*      */     public com.sun.tools.javac.util.List<Type> argtypes;
/*      */     public Type restype;
/*      */     public com.sun.tools.javac.util.List<Type> thrown;
/*      */     public Type recvtype;
/*      */ 
/*      */     public MethodType(com.sun.tools.javac.util.List<Type> paramList1, Type paramType, com.sun.tools.javac.util.List<Type> paramList2, Symbol.TypeSymbol paramTypeSymbol)
/*      */     {
/* 1153 */       super();
/* 1154 */       this.argtypes = paramList1;
/* 1155 */       this.restype = paramType;
/* 1156 */       this.thrown = paramList2;
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1161 */       return TypeTag.METHOD;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS) {
/* 1165 */       return paramVisitor.visitMethodType(this, paramS);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1174 */       return "(" + this.argtypes + ")" + this.restype;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<Type> getParameterTypes() {
/* 1177 */       return this.argtypes; } 
/* 1178 */     public Type getReturnType() { return this.restype; } 
/* 1179 */     public Type getReceiverType() { return this.recvtype; } 
/* 1180 */     public com.sun.tools.javac.util.List<Type> getThrownTypes() { return this.thrown; }
/*      */ 
/*      */ 
/*      */     public boolean isErroneous()
/*      */     {
/* 1185 */       return (isErroneous(this.argtypes)) || 
/* 1184 */         ((this.restype != null) && 
/* 1185 */         (this.restype
/* 1185 */         .isErroneous()));
/*      */     }
/*      */ 
/*      */     public Type map(Type.Mapping paramMapping) {
/* 1189 */       com.sun.tools.javac.util.List localList1 = map(this.argtypes, paramMapping);
/* 1190 */       Type localType = paramMapping.apply(this.restype);
/* 1191 */       com.sun.tools.javac.util.List localList2 = map(this.thrown, paramMapping);
/* 1192 */       if ((localList1 == this.argtypes) && (localType == this.restype) && (localList2 == this.thrown))
/*      */       {
/* 1194 */         return this;
/* 1195 */       }return new MethodType(localList1, localType, localList2, this.tsym);
/*      */     }
/*      */ 
/*      */     public boolean contains(Type paramType) {
/* 1199 */       return (paramType == this) || (contains(this.argtypes, paramType)) || (this.restype.contains(paramType)) || (contains(this.thrown, paramType));
/*      */     }
/*      */     public MethodType asMethodType() {
/* 1202 */       return this;
/*      */     }
/*      */     public void complete() {
/* 1205 */       for (com.sun.tools.javac.util.List localList = this.argtypes; localList.nonEmpty(); localList = localList.tail)
/* 1206 */         ((Type)localList.head).complete();
/* 1207 */       this.restype.complete();
/* 1208 */       this.recvtype.complete();
/* 1209 */       for (localList = this.thrown; localList.nonEmpty(); localList = localList.tail)
/* 1210 */         ((Type)localList.head).complete();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type.TypeVar> getTypeVariables() {
/* 1214 */       return com.sun.tools.javac.util.List.nil();
/*      */     }
/*      */ 
/*      */     public Symbol.TypeSymbol asElement() {
/* 1218 */       return null;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind() {
/* 1222 */       return TypeKind.EXECUTABLE;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1226 */       return paramTypeVisitor.visitExecutable(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PackageType extends Type implements NoType
/*      */   {
/*      */     PackageType(Symbol.TypeSymbol paramTypeSymbol) {
/* 1233 */       super();
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1238 */       return TypeTag.PACKAGE;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1243 */       return paramVisitor.visitPackageType(this, paramS);
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1247 */       return this.tsym.getQualifiedName().toString();
/*      */     }
/*      */ 
/*      */     public TypeKind getKind() {
/* 1251 */       return TypeKind.PACKAGE;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/* 1255 */       return paramTypeVisitor.visitNoType(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TypeVar extends Type
/*      */     implements TypeVariable
/*      */   {
/* 1271 */     public Type bound = null;
/*      */     public Type lower;
/* 1310 */     int rank_field = -1;
/*      */ 
/*      */     public TypeVar(Name paramName, Symbol paramSymbol, Type paramType)
/*      */     {
/* 1281 */       super();
/* 1282 */       this.tsym = new Symbol.TypeVariableSymbol(0L, paramName, this, paramSymbol);
/* 1283 */       this.lower = paramType;
/*      */     }
/*      */ 
/*      */     public TypeVar(Symbol.TypeSymbol paramTypeSymbol, Type paramType1, Type paramType2) {
/* 1287 */       super();
/* 1288 */       this.bound = paramType1;
/* 1289 */       this.lower = paramType2;
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 1294 */       return TypeTag.TYPEVAR;
/*      */     }
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1299 */       return paramVisitor.visitTypeVar(this, paramS);
/*      */     }
/*      */ 
/*      */     public Type getUpperBound()
/*      */     {
/* 1304 */       if (((this.bound == null) || (this.bound.hasTag(TypeTag.NONE))) && (this != this.tsym.type)) {
/* 1305 */         this.bound = this.tsym.type.getUpperBound();
/*      */       }
/* 1307 */       return this.bound;
/*      */     }
/*      */ 
/*      */     public Type getLowerBound()
/*      */     {
/* 1314 */       return this.lower;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind() {
/* 1318 */       return TypeKind.TYPEVAR;
/*      */     }
/*      */ 
/*      */     public boolean isCaptured() {
/* 1322 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean isReference()
/*      */     {
/* 1327 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isNullOrReference()
/*      */     {
/* 1332 */       return true;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/* 1337 */       return paramTypeVisitor.visitTypeVariable(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class UndetVar extends Type.DelegatedType
/*      */   {
/*      */     protected Map<InferenceBound, com.sun.tools.javac.util.List<Type>> bounds;
/* 1500 */     public Type inst = null;
/*      */     public int declaredCount;
/* 1506 */     public UndetVarListener listener = null;
/*      */ 
/* 1593 */     Type.Mapping toTypeVarMap = new Type.Mapping("toTypeVarMap")
/*      */     {
/*      */       public Type apply(Type paramAnonymousType) {
/* 1596 */         if (paramAnonymousType.hasTag(TypeTag.UNDETVAR)) {
/* 1597 */           Type.UndetVar localUndetVar = (Type.UndetVar)paramAnonymousType;
/* 1598 */           return localUndetVar.inst != null ? localUndetVar.inst : localUndetVar.qtype;
/*      */         }
/* 1600 */         return paramAnonymousType.map(this);
/*      */       }
/* 1593 */     };
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/* 1510 */       return paramVisitor.visitUndetVar(this, paramS);
/*      */     }
/*      */ 
/*      */     public UndetVar(Type.TypeVar paramTypeVar, Types paramTypes) {
/* 1514 */       super(paramTypeVar);
/* 1515 */       this.bounds = new EnumMap(InferenceBound.class);
/* 1516 */       com.sun.tools.javac.util.List localList = paramTypes.getBounds(paramTypeVar);
/* 1517 */       this.declaredCount = localList.length();
/* 1518 */       this.bounds.put(InferenceBound.UPPER, localList);
/* 1519 */       this.bounds.put(InferenceBound.LOWER, com.sun.tools.javac.util.List.nil());
/* 1520 */       this.bounds.put(InferenceBound.EQ, com.sun.tools.javac.util.List.nil());
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1524 */       return this.inst == null ? this.qtype + "?" : this.inst.toString();
/*      */     }
/*      */ 
/*      */     public String debugString() {
/* 1528 */       String str = "inference var = " + this.qtype + "\n";
/* 1529 */       if (this.inst != null) {
/* 1530 */         str = str + "inst = " + this.inst + '\n';
/*      */       }
/* 1532 */       for (InferenceBound localInferenceBound : InferenceBound.values()) {
/* 1533 */         com.sun.tools.javac.util.List localList = (com.sun.tools.javac.util.List)this.bounds.get(localInferenceBound);
/* 1534 */         if (localList.size() > 0) {
/* 1535 */           str = str + localInferenceBound + " = " + localList + '\n';
/*      */         }
/*      */       }
/* 1538 */       return str;
/*      */     }
/*      */ 
/*      */     public boolean isPartial()
/*      */     {
/* 1543 */       return true;
/*      */     }
/*      */ 
/*      */     public Type baseType()
/*      */     {
/* 1548 */       return this.inst == null ? this : this.inst.baseType();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getBounds(InferenceBound[] paramArrayOfInferenceBound)
/*      */     {
/* 1553 */       ListBuffer localListBuffer = new ListBuffer();
/* 1554 */       for (InferenceBound localInferenceBound : paramArrayOfInferenceBound) {
/* 1555 */         localListBuffer.appendList((com.sun.tools.javac.util.List)this.bounds.get(localInferenceBound));
/*      */       }
/* 1557 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getDeclaredBounds()
/*      */     {
/* 1562 */       ListBuffer localListBuffer = new ListBuffer();
/* 1563 */       int i = 0;
/* 1564 */       for (Type localType : getBounds(new InferenceBound[] { InferenceBound.UPPER })) {
/* 1565 */         if (i++ == this.declaredCount) break;
/* 1566 */         localListBuffer.append(localType);
/*      */       }
/* 1568 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     public void setBounds(InferenceBound paramInferenceBound, com.sun.tools.javac.util.List<Type> paramList)
/*      */     {
/* 1573 */       this.bounds.put(paramInferenceBound, paramList);
/*      */     }
/*      */ 
/*      */     public final void addBound(InferenceBound paramInferenceBound, Type paramType, Types paramTypes)
/*      */     {
/* 1578 */       addBound(paramInferenceBound, paramType, paramTypes, false);
/*      */     }
/*      */ 
/*      */     protected void addBound(InferenceBound paramInferenceBound, Type paramType, Types paramTypes, boolean paramBoolean) {
/* 1582 */       Type localType1 = this.toTypeVarMap.apply(paramType).baseType();
/* 1583 */       com.sun.tools.javac.util.List localList = (com.sun.tools.javac.util.List)this.bounds.get(paramInferenceBound);
/* 1584 */       for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); 
/* 1587 */         return) { Type localType2 = (Type)localIterator.next();
/*      */ 
/* 1587 */         if ((!paramTypes.isSameType(localType2, localType1, true)) && (paramType != this.qtype)); } this.bounds.put(paramInferenceBound, localList.prepend(localType1));
/* 1590 */       notifyChange(EnumSet.of(paramInferenceBound));
/*      */     }
/*      */ 
/*      */     public void substBounds(com.sun.tools.javac.util.List<Type> paramList1, com.sun.tools.javac.util.List<Type> paramList2, Types paramTypes)
/*      */     {
/* 1607 */       com.sun.tools.javac.util.List localList1 = paramList1.diff(paramList2);
/*      */ 
/* 1609 */       if (localList1.isEmpty()) return;
/* 1610 */       final EnumSet localEnumSet = EnumSet.noneOf(InferenceBound.class);
/* 1611 */       UndetVarListener localUndetVarListener = this.listener;
/*      */       try
/*      */       {
/* 1614 */         this.listener = new UndetVarListener() {
/*      */           public void varChanged(Type.UndetVar paramAnonymousUndetVar, Set<Type.UndetVar.InferenceBound> paramAnonymousSet) {
/* 1616 */             localEnumSet.addAll(paramAnonymousSet);
/*      */           }
/*      */         };
/* 1619 */         for (Map.Entry localEntry : this.bounds.entrySet()) {
/* 1620 */           localInferenceBound = (InferenceBound)localEntry.getKey();
/* 1621 */           com.sun.tools.javac.util.List localList2 = (com.sun.tools.javac.util.List)localEntry.getValue();
/* 1622 */           ListBuffer localListBuffer1 = new ListBuffer();
/* 1623 */           ListBuffer localListBuffer2 = new ListBuffer();
/*      */ 
/* 1625 */           for (localIterator2 = localList2.iterator(); localIterator2.hasNext(); ) { localType = (Type)localIterator2.next();
/* 1626 */             if (!localType.containsAny(localList1))
/* 1627 */               localListBuffer1.append(localType);
/*      */             else {
/* 1629 */               localListBuffer2.append(localType);
/*      */             }
/*      */           }
/*      */ 
/* 1633 */           this.bounds.put(localInferenceBound, localListBuffer1.toList());
/*      */ 
/* 1635 */           for (localIterator2 = localListBuffer2.iterator(); localIterator2.hasNext(); ) { localType = (Type)localIterator2.next();
/* 1636 */             addBound(localInferenceBound, paramTypes.subst(localType, paramList1, paramList2), paramTypes, true);
/*      */           }
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/*      */         InferenceBound localInferenceBound;
/*      */         Iterator localIterator2;
/*      */         Type localType;
/* 1640 */         this.listener = localUndetVarListener;
/* 1641 */         if (!localEnumSet.isEmpty())
/* 1642 */           notifyChange(localEnumSet);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void notifyChange(EnumSet<InferenceBound> paramEnumSet)
/*      */     {
/* 1648 */       if (this.listener != null)
/* 1649 */         this.listener.varChanged(this, paramEnumSet);
/*      */     }
/*      */ 
/*      */     public boolean isCaptured()
/*      */     {
/* 1654 */       return false;
/*      */     }
/*      */ 
/*      */     public static abstract enum InferenceBound
/*      */     {
/* 1481 */       UPPER, 
/*      */ 
/* 1485 */       LOWER, 
/*      */ 
/* 1489 */       EQ;
/*      */ 
/*      */       public abstract InferenceBound complement();
/*      */     }
/*      */ 
/*      */     public static abstract interface UndetVarListener
/*      */     {
/*      */       public abstract void varChanged(Type.UndetVar paramUndetVar, Set<Type.UndetVar.InferenceBound> paramSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class UnionClassType extends Type.ClassType
/*      */     implements UnionType
/*      */   {
/*      */     final com.sun.tools.javac.util.List<? extends Type> alternatives_field;
/*      */ 
/*      */     public UnionClassType(Type.ClassType paramClassType, com.sun.tools.javac.util.List<? extends Type> paramList)
/*      */     {
/*  964 */       super(paramClassType.typarams_field, paramClassType.tsym);
/*  965 */       this.allparams_field = paramClassType.allparams_field;
/*  966 */       this.supertype_field = paramClassType.supertype_field;
/*  967 */       this.interfaces_field = paramClassType.interfaces_field;
/*  968 */       this.all_interfaces_field = paramClassType.interfaces_field;
/*  969 */       this.alternatives_field = paramList;
/*      */     }
/*      */ 
/*      */     public Type getLub() {
/*  973 */       return this.tsym.type;
/*      */     }
/*      */ 
/*      */     public java.util.List<? extends TypeMirror> getAlternatives() {
/*  977 */       return Collections.unmodifiableList(this.alternatives_field);
/*      */     }
/*      */ 
/*      */     public boolean isUnion()
/*      */     {
/*  982 */       return true;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind()
/*      */     {
/*  987 */       return TypeKind.UNION;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/*  992 */       return paramTypeVisitor.visitUnion(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class UnknownType extends Type
/*      */   {
/*      */     public UnknownType()
/*      */     {
/* 2034 */       super();
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/* 2039 */       return TypeTag.UNKNOWN;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP)
/*      */     {
/* 2044 */       return paramTypeVisitor.visitUnknown(this, paramP);
/*      */     }
/*      */ 
/*      */     public boolean isPartial()
/*      */     {
/* 2049 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface Visitor<R, S>
/*      */   {
/*      */     public abstract R visitClassType(Type.ClassType paramClassType, S paramS);
/*      */ 
/*      */     public abstract R visitWildcardType(Type.WildcardType paramWildcardType, S paramS);
/*      */ 
/*      */     public abstract R visitArrayType(Type.ArrayType paramArrayType, S paramS);
/*      */ 
/*      */     public abstract R visitMethodType(Type.MethodType paramMethodType, S paramS);
/*      */ 
/*      */     public abstract R visitPackageType(Type.PackageType paramPackageType, S paramS);
/*      */ 
/*      */     public abstract R visitTypeVar(Type.TypeVar paramTypeVar, S paramS);
/*      */ 
/*      */     public abstract R visitCapturedType(Type.CapturedType paramCapturedType, S paramS);
/*      */ 
/*      */     public abstract R visitForAll(Type.ForAll paramForAll, S paramS);
/*      */ 
/*      */     public abstract R visitUndetVar(Type.UndetVar paramUndetVar, S paramS);
/*      */ 
/*      */     public abstract R visitErrorType(Type.ErrorType paramErrorType, S paramS);
/*      */ 
/*      */     public abstract R visitAnnotatedType(Type.AnnotatedType paramAnnotatedType, S paramS);
/*      */ 
/*      */     public abstract R visitType(Type paramType, S paramS);
/*      */   }
/*      */ 
/*      */   public static class WildcardType extends Type
/*      */     implements WildcardType
/*      */   {
/*      */     public Type type;
/*      */     public BoundKind kind;
/*      */     public Type.TypeVar bound;
/*  681 */     boolean isPrintingBound = false;
/*      */ 
/*      */     public <R, S> R accept(Type.Visitor<R, S> paramVisitor, S paramS)
/*      */     {
/*  623 */       return paramVisitor.visitWildcardType(this, paramS);
/*      */     }
/*      */ 
/*      */     public WildcardType(Type paramType, BoundKind paramBoundKind, Symbol.TypeSymbol paramTypeSymbol) {
/*  627 */       super();
/*  628 */       this.type = ((Type)Assert.checkNonNull(paramType));
/*  629 */       this.kind = paramBoundKind;
/*      */     }
/*      */     public WildcardType(WildcardType paramWildcardType, Type.TypeVar paramTypeVar) {
/*  632 */       this(paramWildcardType.type, paramWildcardType.kind, paramWildcardType.tsym, paramTypeVar);
/*      */     }
/*      */ 
/*      */     public WildcardType(Type paramType, BoundKind paramBoundKind, Symbol.TypeSymbol paramTypeSymbol, Type.TypeVar paramTypeVar) {
/*  636 */       this(paramType, paramBoundKind, paramTypeSymbol);
/*  637 */       this.bound = paramTypeVar;
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/*  642 */       return TypeTag.WILDCARD;
/*      */     }
/*      */ 
/*      */     public boolean contains(Type paramType)
/*      */     {
/*  647 */       return (this.kind != BoundKind.UNBOUND) && (this.type.contains(paramType));
/*      */     }
/*      */ 
/*      */     public boolean isSuperBound() {
/*  651 */       return (this.kind == BoundKind.SUPER) || (this.kind == BoundKind.UNBOUND);
/*      */     }
/*      */ 
/*      */     public boolean isExtendsBound() {
/*  655 */       return (this.kind == BoundKind.EXTENDS) || (this.kind == BoundKind.UNBOUND);
/*      */     }
/*      */ 
/*      */     public boolean isUnbound() {
/*  659 */       return this.kind == BoundKind.UNBOUND;
/*      */     }
/*      */ 
/*      */     public boolean isReference()
/*      */     {
/*  664 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean isNullOrReference()
/*      */     {
/*  669 */       return true;
/*      */     }
/*      */ 
/*      */     public Type withTypeVar(Type paramType)
/*      */     {
/*  675 */       if (this.bound == paramType)
/*  676 */         return this;
/*  677 */       this.bound = ((Type.TypeVar)paramType);
/*  678 */       return this;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  683 */       StringBuilder localStringBuilder = new StringBuilder();
/*  684 */       localStringBuilder.append(this.kind.toString());
/*  685 */       if (this.kind != BoundKind.UNBOUND)
/*  686 */         localStringBuilder.append(this.type);
/*  687 */       if ((moreInfo) && (this.bound != null) && (!this.isPrintingBound))
/*      */         try {
/*  689 */           this.isPrintingBound = true;
/*  690 */           localStringBuilder.append("{:").append(this.bound.bound).append(":}");
/*      */ 
/*  692 */           this.isPrintingBound = false; } finally { this.isPrintingBound = false; }
/*      */ 
/*  694 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     public Type map(Type.Mapping paramMapping)
/*      */     {
/*  699 */       Type localType = this.type;
/*  700 */       if (localType != null)
/*  701 */         localType = paramMapping.apply(localType);
/*  702 */       if (localType == this.type) {
/*  703 */         return this;
/*      */       }
/*  705 */       return new WildcardType(localType, this.kind, this.tsym, this.bound);
/*      */     }
/*      */ 
/*      */     public Type getExtendsBound() {
/*  709 */       if (this.kind == BoundKind.EXTENDS) {
/*  710 */         return this.type;
/*      */       }
/*  712 */       return null;
/*      */     }
/*      */ 
/*      */     public Type getSuperBound() {
/*  716 */       if (this.kind == BoundKind.SUPER) {
/*  717 */         return this.type;
/*      */       }
/*  719 */       return null;
/*      */     }
/*      */ 
/*      */     public TypeKind getKind() {
/*  723 */       return TypeKind.WILDCARD;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/*  727 */       return paramTypeVisitor.visitWildcard(this, paramP);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Type
 * JD-Core Version:    0.6.2
 */