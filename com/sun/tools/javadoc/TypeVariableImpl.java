/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Type.TypeVar;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ 
/*     */ public class TypeVariableImpl extends AbstractTypeImpl
/*     */   implements TypeVariable
/*     */ {
/*     */   TypeVariableImpl(DocEnv paramDocEnv, Type.TypeVar paramTypeVar)
/*     */   {
/*  57 */     super(paramDocEnv, paramTypeVar);
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type[] bounds()
/*     */   {
/*  64 */     return TypeMaker.getTypes(this.env, getBounds((Type.TypeVar)this.type, this.env));
/*     */   }
/*     */ 
/*     */   public ProgramElementDoc owner()
/*     */   {
/*  72 */     Symbol localSymbol = this.type.tsym.owner;
/*  73 */     if ((localSymbol.kind & 0x2) != 0) {
/*  74 */       return this.env.getClassDoc((Symbol.ClassSymbol)localSymbol);
/*     */     }
/*  76 */     Names localNames = localSymbol.name.table.names;
/*  77 */     if (localSymbol.name == localNames.init) {
/*  78 */       return this.env.getConstructorDoc((Symbol.MethodSymbol)localSymbol);
/*     */     }
/*  80 */     return this.env.getMethodDoc((Symbol.MethodSymbol)localSymbol);
/*     */   }
/*     */ 
/*     */   public ClassDoc asClassDoc()
/*     */   {
/*  89 */     return this.env.getClassDoc((Symbol.ClassSymbol)this.env.types.erasure(this.type).tsym);
/*     */   }
/*     */ 
/*     */   public TypeVariable asTypeVariable()
/*     */   {
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  99 */     return typeVarToString(this.env, (Type.TypeVar)this.type, true);
/*     */   }
/*     */ 
/*     */   static String typeVarToString(DocEnv paramDocEnv, Type.TypeVar paramTypeVar, boolean paramBoolean)
/*     */   {
/* 108 */     StringBuilder localStringBuilder = new StringBuilder(paramTypeVar.toString());
/* 109 */     List localList = getBounds(paramTypeVar, paramDocEnv);
/*     */     int i;
/* 110 */     if (localList.nonEmpty()) {
/* 111 */       i = 1;
/* 112 */       for (com.sun.tools.javac.code.Type localType : localList) {
/* 113 */         localStringBuilder.append(i != 0 ? " extends " : " & ");
/* 114 */         localStringBuilder.append(TypeMaker.getTypeString(paramDocEnv, localType, paramBoolean));
/* 115 */         i = 0;
/*     */       }
/*     */     }
/* 118 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static List<com.sun.tools.javac.code.Type> getBounds(Type.TypeVar paramTypeVar, DocEnv paramDocEnv)
/*     */   {
/* 125 */     com.sun.tools.javac.code.Type localType = paramTypeVar.getUpperBound();
/* 126 */     Name localName = localType.tsym.getQualifiedName();
/* 127 */     if ((localName == localName.table.names.java_lang_Object) && 
/* 128 */       (!localType
/* 128 */       .isAnnotated())) {
/* 129 */       return List.nil();
/*     */     }
/* 131 */     return paramDocEnv.types.getBounds(paramTypeVar);
/*     */   }
/*     */ 
/*     */   public AnnotationDesc[] annotations()
/*     */   {
/* 140 */     if (!this.type.isAnnotated()) {
/* 141 */       return new AnnotationDesc[0];
/*     */     }
/* 143 */     List localList = this.type.getAnnotationMirrors();
/* 144 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[localList.length()];
/* 145 */     int i = 0;
/* 146 */     for (Attribute.Compound localCompound : localList) {
/* 147 */       arrayOfAnnotationDesc[(i++)] = new AnnotationDescImpl(this.env, localCompound);
/*     */     }
/* 149 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.TypeVariableImpl
 * JD-Core Version:    0.6.2
 */