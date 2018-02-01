/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Type.WildcardType;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ public class WildcardTypeImpl extends AbstractTypeImpl
/*     */   implements WildcardType
/*     */ {
/*     */   WildcardTypeImpl(DocEnv paramDocEnv, Type.WildcardType paramWildcardType)
/*     */   {
/*  50 */     super(paramDocEnv, paramWildcardType);
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type[] extendsBounds()
/*     */   {
/*  59 */     return TypeMaker.getTypes(this.env, getExtendsBounds((Type.WildcardType)this.type));
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type[] superBounds()
/*     */   {
/*  68 */     return TypeMaker.getTypes(this.env, getSuperBounds((Type.WildcardType)this.type));
/*     */   }
/*     */ 
/*     */   public ClassDoc asClassDoc()
/*     */   {
/*  76 */     return this.env.getClassDoc((Symbol.ClassSymbol)this.env.types.erasure(this.type).tsym);
/*     */   }
/*     */ 
/*     */   public WildcardType asWildcardType()
/*     */   {
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   public String typeName() {
/*  85 */     return "?";
/*     */   }
/*  87 */   public String qualifiedTypeName() { return "?"; } 
/*     */   public String simpleTypeName() {
/*  89 */     return "?";
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  93 */     return wildcardTypeToString(this.env, (Type.WildcardType)this.type, true);
/*     */   }
/*     */ 
/*     */   static String wildcardTypeToString(DocEnv paramDocEnv, Type.WildcardType paramWildcardType, boolean paramBoolean)
/*     */   {
/* 104 */     if (paramDocEnv.legacyDoclet) {
/* 105 */       return TypeMaker.getTypeName(paramDocEnv.types.erasure(paramWildcardType), paramBoolean);
/*     */     }
/* 107 */     StringBuilder localStringBuilder = new StringBuilder("?");
/* 108 */     List localList = getExtendsBounds(paramWildcardType);
/* 109 */     if (localList.nonEmpty()) {
/* 110 */       localStringBuilder.append(" extends ");
/*     */     } else {
/* 112 */       localList = getSuperBounds(paramWildcardType);
/* 113 */       if (localList.nonEmpty()) {
/* 114 */         localStringBuilder.append(" super ");
/*     */       }
/*     */     }
/* 117 */     int i = 1;
/* 118 */     for (com.sun.tools.javac.code.Type localType : localList) {
/* 119 */       if (i == 0) {
/* 120 */         localStringBuilder.append(" & ");
/*     */       }
/* 122 */       localStringBuilder.append(TypeMaker.getTypeString(paramDocEnv, localType, paramBoolean));
/* 123 */       i = 0;
/*     */     }
/* 125 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static List<com.sun.tools.javac.code.Type> getExtendsBounds(Type.WildcardType paramWildcardType)
/*     */   {
/* 131 */     return paramWildcardType.isSuperBound() ? 
/* 130 */       List.nil() : 
/* 131 */       List.of(paramWildcardType.type);
/*     */   }
/*     */ 
/*     */   private static List<com.sun.tools.javac.code.Type> getSuperBounds(Type.WildcardType paramWildcardType)
/*     */   {
/* 137 */     return paramWildcardType.isExtendsBound() ? 
/* 136 */       List.nil() : 
/* 137 */       List.of(paramWildcardType.type);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.WildcardTypeImpl
 * JD-Core Version:    0.6.2
 */