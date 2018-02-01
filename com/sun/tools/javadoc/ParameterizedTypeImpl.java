/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ public class ParameterizedTypeImpl extends AbstractTypeImpl
/*     */   implements ParameterizedType
/*     */ {
/*     */   ParameterizedTypeImpl(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType)
/*     */   {
/*  53 */     super(paramDocEnv, paramType);
/*     */   }
/*     */ 
/*     */   public ClassDoc asClassDoc()
/*     */   {
/*  61 */     return this.env.getClassDoc((Symbol.ClassSymbol)this.type.tsym);
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type[] typeArguments()
/*     */   {
/*  68 */     return TypeMaker.getTypes(this.env, this.type.getTypeArguments());
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type superclassType()
/*     */   {
/*  76 */     if (asClassDoc().isInterface()) {
/*  77 */       return null;
/*     */     }
/*  79 */     com.sun.tools.javac.code.Type localType = this.env.types.supertype(this.type);
/*  80 */     return TypeMaker.getType(this.env, localType != this.type ? localType : this.env.syms.objectType);
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type[] interfaceTypes()
/*     */   {
/*  90 */     return TypeMaker.getTypes(this.env, this.env.types.interfaces(this.type));
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type containingType()
/*     */   {
/*  98 */     if (this.type.getEnclosingType().hasTag(TypeTag.CLASS))
/*     */     {
/* 100 */       return TypeMaker.getType(this.env, this.type.getEnclosingType());
/*     */     }
/* 102 */     Symbol.ClassSymbol localClassSymbol = this.type.tsym.owner.enclClass();
/* 103 */     if (localClassSymbol != null)
/*     */     {
/* 107 */       return this.env.getClassDoc(localClassSymbol);
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */   public String typeName()
/*     */   {
/* 118 */     return TypeMaker.getTypeName(this.type, false);
/*     */   }
/*     */ 
/*     */   public ParameterizedType asParameterizedType()
/*     */   {
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     return parameterizedTypeToString(this.env, (Type.ClassType)this.type, true);
/*     */   }
/*     */ 
/*     */   static String parameterizedTypeToString(DocEnv paramDocEnv, Type.ClassType paramClassType, boolean paramBoolean)
/*     */   {
/* 133 */     if (paramDocEnv.legacyDoclet) {
/* 134 */       return TypeMaker.getTypeName(paramClassType, paramBoolean);
/*     */     }
/* 136 */     StringBuilder localStringBuilder = new StringBuilder();
/* 137 */     if (!paramClassType.getEnclosingType().hasTag(TypeTag.CLASS)) {
/* 138 */       localStringBuilder.append(TypeMaker.getTypeName(paramClassType, paramBoolean));
/*     */     } else {
/* 140 */       Type.ClassType localClassType = (Type.ClassType)paramClassType.getEnclosingType();
/* 141 */       localStringBuilder.append(parameterizedTypeToString(paramDocEnv, localClassType, paramBoolean))
/* 142 */         .append('.')
/* 143 */         .append(paramClassType.tsym.name
/* 143 */         .toString());
/*     */     }
/* 145 */     localStringBuilder.append(TypeMaker.typeArgumentsString(paramDocEnv, paramClassType, paramBoolean));
/* 146 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ParameterizedTypeImpl
 * JD-Core Version:    0.6.2
 */