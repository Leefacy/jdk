/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ class ParameterImpl
/*     */   implements Parameter
/*     */ {
/*     */   private final DocEnv env;
/*     */   private final Symbol.VarSymbol sym;
/*     */   private final Type type;
/*     */ 
/*     */   ParameterImpl(DocEnv paramDocEnv, Symbol.VarSymbol paramVarSymbol)
/*     */   {
/*  56 */     this.env = paramDocEnv;
/*  57 */     this.sym = paramVarSymbol;
/*  58 */     this.type = TypeMaker.getType(paramDocEnv, paramVarSymbol.type, false);
/*     */   }
/*     */ 
/*     */   public Type type()
/*     */   {
/*  65 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/*  73 */     return this.sym.toString();
/*     */   }
/*     */ 
/*     */   public String typeName()
/*     */   {
/*  83 */     return ((this.type instanceof ClassDoc)) || ((this.type instanceof TypeVariable)) ? this.type
/*  82 */       .typeName() : this.type
/*  83 */       .toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  94 */     return typeName() + " " + this.sym;
/*     */   }
/*     */ 
/*     */   public AnnotationDesc[] annotations()
/*     */   {
/* 102 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[this.sym.getRawAttributes().length()];
/* 103 */     int i = 0;
/* 104 */     for (Attribute.Compound localCompound : this.sym.getRawAttributes()) {
/* 105 */       arrayOfAnnotationDesc[(i++)] = new AnnotationDescImpl(this.env, localCompound);
/*     */     }
/* 107 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ParameterImpl
 * JD-Core Version:    0.6.2
 */