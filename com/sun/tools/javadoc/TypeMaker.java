/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.Type.TypeVar;
/*     */ import com.sun.tools.javac.code.Type.WildcardType;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ public class TypeMaker
/*     */ {
/*     */   public static com.sun.javadoc.Type getType(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType)
/*     */   {
/*  48 */     return getType(paramDocEnv, paramType, true);
/*     */   }
/*     */ 
/*     */   public static com.sun.javadoc.Type getType(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType, boolean paramBoolean)
/*     */   {
/*  57 */     return getType(paramDocEnv, paramType, paramBoolean, true);
/*     */   }
/*     */ 
/*     */   public static com.sun.javadoc.Type getType(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*     */     try {
/*  63 */       return getTypeImpl(paramDocEnv, paramType, paramBoolean1, paramBoolean2);
/*     */     }
/*     */     catch (Symbol.CompletionFailure localCompletionFailure)
/*     */     {
/*     */     }
/*     */ 
/*  69 */     return getType(paramDocEnv, paramType, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   private static com.sun.javadoc.Type getTypeImpl(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  76 */     if (paramDocEnv.legacyDoclet) {
/*  77 */       paramType = paramDocEnv.types.erasure(paramType);
/*     */     }
/*     */ 
/*  80 */     if ((paramBoolean2) && (paramType.isAnnotated())) {
/*  81 */       return new AnnotatedTypeImpl(paramDocEnv, paramType);
/*     */     }
/*     */ 
/*  84 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*     */     case 1:
/*  86 */       if (ClassDocImpl.isGeneric((Symbol.ClassSymbol)paramType.tsym)) {
/*  87 */         return paramDocEnv.getParameterizedType((Type.ClassType)paramType);
/*     */       }
/*  89 */       return paramDocEnv.getClassDoc((Symbol.ClassSymbol)paramType.tsym);
/*     */     case 2:
/*  92 */       Type.WildcardType localWildcardType = (Type.WildcardType)paramType;
/*  93 */       return new WildcardTypeImpl(paramDocEnv, localWildcardType);
/*     */     case 3:
/*  94 */       return new TypeVariableImpl(paramDocEnv, (Type.TypeVar)paramType);
/*     */     case 4:
/*  95 */       return new ArrayTypeImpl(paramDocEnv, paramType);
/*     */     case 5:
/*  96 */       return PrimitiveType.byteType;
/*     */     case 6:
/*  97 */       return PrimitiveType.charType;
/*     */     case 7:
/*  98 */       return PrimitiveType.shortType;
/*     */     case 8:
/*  99 */       return PrimitiveType.intType;
/*     */     case 9:
/* 100 */       return PrimitiveType.longType;
/*     */     case 10:
/* 101 */       return PrimitiveType.floatType;
/*     */     case 11:
/* 102 */       return PrimitiveType.doubleType;
/*     */     case 12:
/* 103 */       return PrimitiveType.booleanType;
/*     */     case 13:
/* 104 */       return PrimitiveType.voidType;
/*     */     case 14:
/* 106 */       if (paramBoolean1)
/* 107 */         return paramDocEnv.getClassDoc((Symbol.ClassSymbol)paramType.tsym);
/*     */       break;
/*     */     }
/* 110 */     return new PrimitiveType(paramType.tsym.getQualifiedName().toString());
/*     */   }
/*     */ 
/*     */   public static com.sun.javadoc.Type[] getTypes(DocEnv paramDocEnv, List<com.sun.tools.javac.code.Type> paramList)
/*     */   {
/* 118 */     return getTypes(paramDocEnv, paramList, new com.sun.javadoc.Type[paramList.length()]);
/*     */   }
/*     */ 
/*     */   public static com.sun.javadoc.Type[] getTypes(DocEnv paramDocEnv, List<com.sun.tools.javac.code.Type> paramList, com.sun.javadoc.Type[] paramArrayOfType)
/*     */   {
/* 126 */     int i = 0;
/* 127 */     for (com.sun.tools.javac.code.Type localType : paramList) {
/* 128 */       paramArrayOfType[(i++)] = getType(paramDocEnv, localType);
/*     */     }
/* 130 */     return paramArrayOfType;
/*     */   }
/*     */ 
/*     */   public static String getTypeName(com.sun.tools.javac.code.Type paramType, boolean paramBoolean) {
/* 134 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*     */     case 4:
/* 136 */       StringBuilder localStringBuilder = new StringBuilder();
/* 137 */       while (paramType.hasTag(TypeTag.ARRAY)) {
/* 138 */         localStringBuilder.append("[]");
/* 139 */         paramType = ((Type.ArrayType)paramType).elemtype;
/*     */       }
/* 141 */       localStringBuilder.insert(0, getTypeName(paramType, paramBoolean));
/* 142 */       return localStringBuilder.toString();
/*     */     case 1:
/* 144 */       return ClassDocImpl.getClassName((Symbol.ClassSymbol)paramType.tsym, paramBoolean);
/*     */     }
/* 146 */     return paramType.tsym.getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   static String getTypeString(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType, boolean paramBoolean)
/*     */   {
/* 157 */     if (paramType.isAnnotated()) {
/* 158 */       paramType = paramType.unannotatedType();
/*     */     }
/* 160 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*     */     case 4:
/* 162 */       StringBuilder localStringBuilder = new StringBuilder();
/* 163 */       while (paramType.hasTag(TypeTag.ARRAY)) {
/* 164 */         localStringBuilder.append("[]");
/* 165 */         paramType = paramDocEnv.types.elemtype(paramType);
/*     */       }
/* 167 */       localStringBuilder.insert(0, getTypeString(paramDocEnv, paramType, paramBoolean));
/* 168 */       return localStringBuilder.toString();
/*     */     case 1:
/* 171 */       return ParameterizedTypeImpl.parameterizedTypeToString(paramDocEnv, (Type.ClassType)paramType, paramBoolean);
/*     */     case 2:
/* 173 */       Type.WildcardType localWildcardType = (Type.WildcardType)paramType;
/* 174 */       return WildcardTypeImpl.wildcardTypeToString(paramDocEnv, localWildcardType, paramBoolean);
/*     */     case 3:
/* 176 */     }return paramType.tsym.getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   static String typeParametersString(DocEnv paramDocEnv, Symbol paramSymbol, boolean paramBoolean)
/*     */   {
/* 187 */     if ((paramDocEnv.legacyDoclet) || (paramSymbol.type.getTypeArguments().isEmpty())) {
/* 188 */       return "";
/*     */     }
/* 190 */     StringBuilder localStringBuilder = new StringBuilder();
/* 191 */     for (com.sun.tools.javac.code.Type localType : paramSymbol.type.getTypeArguments()) {
/* 192 */       localStringBuilder.append(localStringBuilder.length() == 0 ? "<" : ", ");
/* 193 */       localStringBuilder.append(TypeVariableImpl.typeVarToString(paramDocEnv, (Type.TypeVar)localType, paramBoolean));
/*     */     }
/* 195 */     localStringBuilder.append(">");
/* 196 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static String typeArgumentsString(DocEnv paramDocEnv, Type.ClassType paramClassType, boolean paramBoolean)
/*     */   {
/* 205 */     if ((paramDocEnv.legacyDoclet) || (paramClassType.getTypeArguments().isEmpty())) {
/* 206 */       return "";
/*     */     }
/* 208 */     StringBuilder localStringBuilder = new StringBuilder();
/* 209 */     for (com.sun.tools.javac.code.Type localType : paramClassType.getTypeArguments()) {
/* 210 */       localStringBuilder.append(localStringBuilder.length() == 0 ? "<" : ", ");
/* 211 */       localStringBuilder.append(getTypeString(paramDocEnv, localType, paramBoolean));
/*     */     }
/* 213 */     localStringBuilder.append(">");
/* 214 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static class ArrayTypeImpl
/*     */     implements com.sun.javadoc.Type
/*     */   {
/*     */     com.sun.tools.javac.code.Type arrayType;
/*     */     DocEnv env;
/* 229 */     private com.sun.javadoc.Type skipArraysCache = null;
/*     */ 
/*     */     ArrayTypeImpl(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType)
/*     */     {
/* 225 */       this.env = paramDocEnv;
/* 226 */       this.arrayType = paramType;
/*     */     }
/*     */ 
/*     */     public com.sun.javadoc.Type getElementType()
/*     */     {
/* 232 */       return TypeMaker.getType(this.env, this.env.types.elemtype(this.arrayType));
/*     */     }
/*     */ 
/*     */     private com.sun.javadoc.Type skipArrays() {
/* 236 */       if (this.skipArraysCache == null)
/*     */       {
/* 238 */         for (com.sun.tools.javac.code.Type localType = this.arrayType; localType.hasTag(TypeTag.ARRAY); localType = this.env.types.elemtype(localType));
/* 239 */         this.skipArraysCache = TypeMaker.getType(this.env, localType);
/*     */       }
/* 241 */       return this.skipArraysCache;
/*     */     }
/*     */ 
/*     */     public String dimension()
/*     */     {
/* 250 */       StringBuilder localStringBuilder = new StringBuilder();
/* 251 */       for (com.sun.tools.javac.code.Type localType = this.arrayType; localType.hasTag(TypeTag.ARRAY); localType = this.env.types.elemtype(localType)) {
/* 252 */         localStringBuilder.append("[]");
/*     */       }
/* 254 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public String typeName()
/*     */     {
/* 263 */       return skipArrays().typeName();
/*     */     }
/*     */ 
/*     */     public String qualifiedTypeName()
/*     */     {
/* 273 */       return skipArrays().qualifiedTypeName();
/*     */     }
/*     */ 
/*     */     public String simpleTypeName()
/*     */     {
/* 280 */       return skipArrays().simpleTypeName();
/*     */     }
/*     */ 
/*     */     public ClassDoc asClassDoc()
/*     */     {
/* 290 */       return skipArrays().asClassDoc();
/*     */     }
/*     */ 
/*     */     public ParameterizedType asParameterizedType()
/*     */     {
/* 298 */       return skipArrays().asParameterizedType();
/*     */     }
/*     */ 
/*     */     public TypeVariable asTypeVariable()
/*     */     {
/* 306 */       return skipArrays().asTypeVariable();
/*     */     }
/*     */ 
/*     */     public WildcardType asWildcardType()
/*     */     {
/* 313 */       return null;
/*     */     }
/*     */ 
/*     */     public AnnotatedType asAnnotatedType()
/*     */     {
/* 320 */       return null;
/*     */     }
/*     */ 
/*     */     public AnnotationTypeDoc asAnnotationTypeDoc()
/*     */     {
/* 328 */       return skipArrays().asAnnotationTypeDoc();
/*     */     }
/*     */ 
/*     */     public boolean isPrimitive()
/*     */     {
/* 335 */       return skipArrays().isPrimitive();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 350 */       return qualifiedTypeName() + dimension();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.TypeMaker
 * JD-Core Version:    0.6.2
 */