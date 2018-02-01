/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ public class AnnotatedTypeImpl extends AbstractTypeImpl
/*     */   implements AnnotatedType
/*     */ {
/*     */   AnnotatedTypeImpl(DocEnv paramDocEnv, com.sun.tools.javac.code.Type paramType)
/*     */   {
/*  44 */     super(paramDocEnv, paramType);
/*     */   }
/*     */ 
/*     */   public AnnotationDesc[] annotations()
/*     */   {
/*  53 */     List localList = this.type.getAnnotationMirrors();
/*  54 */     if ((localList == null) || 
/*  55 */       (localList
/*  55 */       .isEmpty())) {
/*  56 */       return new AnnotationDesc[0];
/*     */     }
/*  58 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[localList.length()];
/*  59 */     int i = 0;
/*  60 */     for (Attribute.Compound localCompound : localList) {
/*  61 */       arrayOfAnnotationDesc[(i++)] = new AnnotationDescImpl(this.env, localCompound);
/*     */     }
/*  63 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type underlyingType()
/*     */   {
/*  68 */     return TypeMaker.getType(this.env, this.type.unannotatedType(), true, false);
/*     */   }
/*     */ 
/*     */   public AnnotatedType asAnnotatedType()
/*     */   {
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  78 */     return typeName();
/*     */   }
/*     */ 
/*     */   public String typeName()
/*     */   {
/*  83 */     return underlyingType().typeName();
/*     */   }
/*     */ 
/*     */   public String qualifiedTypeName()
/*     */   {
/*  88 */     return underlyingType().qualifiedTypeName();
/*     */   }
/*     */ 
/*     */   public String simpleTypeName()
/*     */   {
/*  93 */     return underlyingType().simpleTypeName();
/*     */   }
/*     */ 
/*     */   public String dimension()
/*     */   {
/*  98 */     return underlyingType().dimension();
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 103 */     return underlyingType().isPrimitive();
/*     */   }
/*     */ 
/*     */   public ClassDoc asClassDoc()
/*     */   {
/* 108 */     return underlyingType().asClassDoc();
/*     */   }
/*     */ 
/*     */   public TypeVariable asTypeVariable()
/*     */   {
/* 113 */     return underlyingType().asTypeVariable();
/*     */   }
/*     */ 
/*     */   public WildcardType asWildcardType()
/*     */   {
/* 118 */     return underlyingType().asWildcardType();
/*     */   }
/*     */ 
/*     */   public ParameterizedType asParameterizedType()
/*     */   {
/* 123 */     return underlyingType().asParameterizedType();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.AnnotatedTypeImpl
 * JD-Core Version:    0.6.2
 */