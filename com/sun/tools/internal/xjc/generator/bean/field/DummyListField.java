/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class DummyListField extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */ 
/*     */   protected DummyListField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList)
/*     */   {
/*  96 */     super(context, prop, !coreList.fullName().equals("java.util.ArrayList"));
/*  97 */     this.coreList = coreList.narrow(this.exposedType.boxify());
/*  98 */     generate();
/*     */   }
/*     */ 
/*     */   protected void annotate(JAnnotatable field)
/*     */   {
/* 106 */     super.annotate(field);
/*     */ 
/* 108 */     if ((this.prop instanceof CReferencePropertyInfo)) {
/* 109 */       CReferencePropertyInfo pref = (CReferencePropertyInfo)this.prop;
/* 110 */       if (pref.isDummy())
/* 111 */         annotateDummy(field);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void annotateDummy(JAnnotatable field)
/*     */   {
/* 118 */     field.annotate(OverrideAnnotationOf.class);
/*     */   }
/*     */ 
/*     */   protected final JClass getCoreListType() {
/* 122 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   public void generateAccessors() {
/*     */   }
/*     */ 
/*     */   public Accessor create(JExpression targetObject) {
/* 129 */     return new Accessor(targetObject);
/*     */   }
/*     */ 
/*     */   class Accessor extends AbstractListField.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 134 */       super($target);
/*     */     }
/*     */ 
/*     */     public void toRawValue(JBlock block, JVar $var)
/*     */     {
/* 141 */       block.assign($var, JExpr._new(DummyListField.this.codeModel.ref(ArrayList.class).narrow(DummyListField.this.exposedType.boxify())).arg(this.$target
/* 142 */         .invoke(DummyListField.this.$get)));
/*     */     }
/*     */ 
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var)
/*     */     {
/* 149 */       JVar $list = block.decl(DummyListField.this.listT, uniqueName + 'l', this.$target.invoke(DummyListField.this.$get));
/* 150 */       block.invoke($list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.DummyListField
 * JD-Core Version:    0.6.2
 */