/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CDefaultValue;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ 
/*     */ final class ConstField extends AbstractField
/*     */ {
/*     */   private final JFieldVar $ref;
/*     */ 
/*     */   ConstField(ClassOutlineImpl outline, CPropertyInfo prop)
/*     */   {
/*  59 */     super(outline, prop);
/*     */ 
/*  62 */     assert (!prop.isCollection());
/*     */ 
/*  64 */     JPrimitiveType ptype = this.implType.boxify().getPrimitiveType();
/*     */ 
/*  67 */     JExpression defaultValue = null;
/*  68 */     if (prop.defaultValue != null) {
/*  69 */       defaultValue = prop.defaultValue.compute(outline.parent());
/*     */     }
/*  71 */     this.$ref = outline.ref.field(25, ptype != null ? ptype : this.implType, prop
/*  72 */       .getName(true), 
/*  72 */       defaultValue);
/*  73 */     this.$ref.javadoc().append(prop.javadoc);
/*     */ 
/*  75 */     annotate(this.$ref);
/*     */   }
/*     */ 
/*     */   public JType getRawType()
/*     */   {
/*  80 */     return this.exposedType;
/*     */   }
/*     */ 
/*     */   public FieldAccessor create(JExpression target)
/*     */   {
/*  85 */     return new Accessor(target);
/*     */   }
/*     */ 
/*     */   private class Accessor extends AbstractField.Accessor
/*     */   {
/*     */     Accessor(JExpression $target) {
/*  91 */       super($target);
/*     */     }
/*     */ 
/*     */     public void unsetValues(JBlock body) {
/*     */     }
/*     */ 
/*     */     public JExpression hasSetValue() {
/*  98 */       return null;
/*     */     }
/*     */ 
/*     */     public void toRawValue(JBlock block, JVar $var)
/*     */     {
/* 103 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 107 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.ConstField
 * JD-Core Version:    0.6.2
 */