/*     */ package com.sun.tools.internal.xjc.api;
/*     */ 
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ public final class Reference
/*     */ {
/*     */   public final TypeMirror type;
/*     */   public final Element annotations;
/*     */ 
/*     */   public Reference(ExecutableElement method)
/*     */   {
/*  66 */     this(method.getReturnType(), method);
/*     */   }
/*     */ 
/*     */   public Reference(VariableElement param)
/*     */   {
/*  74 */     this(param.asType(), param);
/*     */   }
/*     */ 
/*     */   public Reference(TypeElement type, ProcessingEnvironment env)
/*     */   {
/*  81 */     this(env.getTypeUtils().getDeclaredType(type, new TypeMirror[0]), type);
/*     */   }
/*     */ 
/*     */   public Reference(TypeMirror type, Element annotations)
/*     */   {
/*  88 */     if ((type == null) || (annotations == null))
/*  89 */       throw new IllegalArgumentException();
/*  90 */     this.type = type;
/*  91 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o) {
/*  95 */     if (this == o) return true;
/*  96 */     if (!(o instanceof Reference)) return false;
/*     */ 
/*  98 */     Reference that = (Reference)o;
/*     */ 
/* 100 */     return (this.annotations.equals(that.annotations)) && (this.type.equals(that.type));
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 104 */     return 29 * this.type.hashCode() + this.annotations.hashCode();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.Reference
 * JD-Core Version:    0.6.2
 */