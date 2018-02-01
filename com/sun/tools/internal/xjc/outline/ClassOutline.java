/*     */ package com.sun.tools.internal.xjc.outline;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class ClassOutline
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   public final CClassInfo target;
/*     */ 
/*     */   @NotNull
/*     */   public final JDefinedClass ref;
/*     */ 
/*     */   @NotNull
/*     */   public final JDefinedClass implClass;
/*     */ 
/*     */   @NotNull
/*     */   public final JClass implRef;
/*     */ 
/*     */   @NotNull
/*     */   public abstract Outline parent();
/*     */ 
/*     */   @NotNull
/*     */   public PackageOutline _package()
/*     */   {
/*  58 */     return parent().getPackageContext(this.ref._package());
/*     */   }
/*     */ 
/*     */   protected ClassOutline(CClassInfo _target, JDefinedClass exposedClass, JClass implRef, JDefinedClass _implClass)
/*     */   {
/*  97 */     this.target = _target;
/*  98 */     this.ref = exposedClass;
/*  99 */     this.implRef = implRef;
/* 100 */     this.implClass = _implClass;
/*     */   }
/*     */ 
/*     */   public final FieldOutline[] getDeclaredFields()
/*     */   {
/* 108 */     List props = this.target.getProperties();
/* 109 */     FieldOutline[] fr = new FieldOutline[props.size()];
/* 110 */     for (int i = 0; i < fr.length; i++)
/* 111 */       fr[i] = parent().getField((CPropertyInfo)props.get(i));
/* 112 */     return fr;
/*     */   }
/*     */ 
/*     */   public final ClassOutline getSuperClass()
/*     */   {
/* 121 */     CClassInfo s = this.target.getBaseClass();
/* 122 */     if (s == null) return null;
/* 123 */     return parent().getClazz(s);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.ClassOutline
 * JD-Core Version:    0.6.2
 */