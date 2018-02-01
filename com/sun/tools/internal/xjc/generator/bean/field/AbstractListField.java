/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldRef;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JOp;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class AbstractListField extends AbstractField
/*     */ {
/*     */   protected JFieldVar field;
/*     */   private JMethod internalGetter;
/*     */   protected final JPrimitiveType primitiveType;
/*  79 */   protected final JClass listT = this.codeModel.ref(List.class).narrow(this.exposedType.boxify());
/*     */   private final boolean eagerInstanciation;
/*     */ 
/*     */   protected AbstractListField(ClassOutlineImpl outline, CPropertyInfo prop, boolean eagerInstanciation)
/*     */   {
/*  95 */     super(outline, prop);
/*  96 */     this.eagerInstanciation = eagerInstanciation;
/*     */ 
/*  98 */     if ((this.implType instanceof JPrimitiveType))
/*     */     {
/* 100 */       assert (this.implType == this.exposedType);
/* 101 */       this.primitiveType = ((JPrimitiveType)this.implType);
/*     */     } else {
/* 103 */       this.primitiveType = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void generate()
/*     */   {
/* 110 */     this.field = this.outline.implClass.field(2, this.listT, this.prop.getName(false));
/* 111 */     if (this.eagerInstanciation) {
/* 112 */       this.field.init(newCoreList());
/*     */     }
/* 114 */     annotate(this.field);
/*     */ 
/* 117 */     generateAccessors();
/*     */   }
/*     */ 
/*     */   private void generateInternalGetter() {
/* 121 */     this.internalGetter = this.outline.implClass.method(2, this.listT, "_get" + this.prop.getName(true));
/* 122 */     if (!this.eagerInstanciation)
/*     */     {
/* 124 */       fixNullRef(this.internalGetter.body());
/*     */     }
/* 126 */     this.internalGetter.body()._return(this.field);
/*     */   }
/*     */ 
/*     */   protected final void fixNullRef(JBlock block)
/*     */   {
/* 136 */     block._if(this.field.eq(JExpr._null()))._then()
/* 137 */       .assign(this.field, 
/* 137 */       newCoreList());
/*     */   }
/*     */ 
/*     */   public JType getRawType() {
/* 141 */     return this.codeModel.ref(List.class).narrow(this.exposedType.boxify());
/*     */   }
/*     */ 
/*     */   private JExpression newCoreList() {
/* 145 */     return JExpr._new(getCoreListType());
/*     */   }
/*     */ 
/*     */   protected abstract JClass getCoreListType();
/*     */ 
/*     */   protected abstract void generateAccessors();
/*     */ 
/*     */   protected abstract class Accessor extends AbstractField.Accessor
/*     */   {
/*     */     protected final JFieldRef field;
/*     */ 
/*     */     protected Accessor(JExpression $target)
/*     */     {
/* 175 */       super($target);
/* 176 */       this.field = $target.ref(AbstractListField.this.field);
/*     */     }
/*     */ 
/*     */     protected final JExpression unbox(JExpression exp)
/*     */     {
/* 181 */       if (AbstractListField.this.primitiveType == null) return exp;
/* 182 */       return AbstractListField.this.primitiveType.unwrap(exp);
/*     */     }
/*     */     protected final JExpression box(JExpression exp) {
/* 185 */       if (AbstractListField.this.primitiveType == null) return exp;
/* 186 */       return AbstractListField.this.primitiveType.wrap(exp);
/*     */     }
/*     */ 
/*     */     protected final JExpression ref(boolean canBeNull)
/*     */     {
/* 206 */       if (canBeNull)
/* 207 */         return this.field;
/* 208 */       if (AbstractListField.this.internalGetter == null)
/* 209 */         AbstractListField.this.generateInternalGetter();
/* 210 */       return this.$target.invoke(AbstractListField.this.internalGetter);
/*     */     }
/*     */ 
/*     */     public JExpression count() {
/* 214 */       return JOp.cond(this.field.eq(JExpr._null()), JExpr.lit(0), this.field.invoke("size"));
/*     */     }
/*     */ 
/*     */     public void unsetValues(JBlock body) {
/* 218 */       body.assign(this.field, JExpr._null());
/*     */     }
/*     */     public JExpression hasSetValue() {
/* 221 */       return this.field.ne(JExpr._null()).cand(this.field.invoke("isEmpty").not());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.AbstractListField
 * JD-Core Version:    0.6.2
 */