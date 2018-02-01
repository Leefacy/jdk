/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldRef;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ 
/*     */ abstract class AbstractFieldWithVar extends AbstractField
/*     */ {
/*     */   private JFieldVar field;
/*     */ 
/*     */   AbstractFieldWithVar(ClassOutlineImpl outline, CPropertyInfo prop)
/*     */   {
/*  57 */     super(outline, prop);
/*     */   }
/*     */ 
/*     */   protected final void createField() {
/*  61 */     this.field = this.outline.implClass.field(2, 
/*  62 */       getFieldType(), this.prop.getName(false));
/*     */ 
/*  64 */     annotate(this.field);
/*     */   }
/*     */ 
/*     */   protected String getGetterMethod()
/*     */   {
/*  75 */     if (getOptions().enableIntrospection)
/*     */     {
/*  78 */       return ((getFieldType().isPrimitive()) && 
/*  77 */         (getFieldType().boxify().getPrimitiveType() == this.codeModel.BOOLEAN) ? "is" : "get") + this.prop
/*  78 */         .getName(true);
/*     */     }
/*     */ 
/*  80 */     return (getFieldType().boxify().getPrimitiveType() == this.codeModel.BOOLEAN ? "is" : "get") + this.prop.getName(true);
/*     */   }
/*     */ 
/*     */   protected abstract JType getFieldType();
/*     */ 
/*     */   protected JFieldVar ref()
/*     */   {
/*  89 */     return this.field;
/*     */   }
/*     */   public final JType getRawType() {
/*  92 */     return this.exposedType;
/*     */   }
/*     */   protected abstract class Accessor extends AbstractField.Accessor {
/*     */     protected final JFieldRef $ref;
/*     */ 
/*     */     protected Accessor(JExpression $target) {
/*  98 */       super($target);
/*  99 */       this.$ref = $target.ref(AbstractFieldWithVar.this.ref());
/*     */     }
/*     */ 
/*     */     public final void toRawValue(JBlock block, JVar $var)
/*     */     {
/* 108 */       if (AbstractFieldWithVar.this.getOptions().enableIntrospection)
/* 109 */         block.assign($var, this.$target.invoke(AbstractFieldWithVar.this.getGetterMethod()));
/*     */       else
/* 111 */         block.assign($var, this.$target.invoke(AbstractFieldWithVar.this.getGetterMethod()));
/*     */     }
/*     */ 
/*     */     public final void fromRawValue(JBlock block, String uniqueName, JExpression $var)
/*     */     {
/* 116 */       block.invoke(this.$target, "set" + AbstractFieldWithVar.this.prop.getName(true)).arg($var);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.AbstractFieldWithVar
 * JD-Core Version:    0.6.2
 */