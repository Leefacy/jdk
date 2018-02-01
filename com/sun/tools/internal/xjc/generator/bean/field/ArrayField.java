/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldRef;
/*     */ import com.sun.codemodel.internal.JForLoop;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JOp;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import java.util.List;
/*     */ 
/*     */ final class ArrayField extends AbstractListField
/*     */ {
/*     */   private JMethod $setAll;
/*     */   private JMethod $getAll;
/*     */ 
/*     */   ArrayField(ClassOutlineImpl context, CPropertyInfo prop)
/*     */   {
/*  91 */     super(context, prop, false);
/*  92 */     generateArray();
/*     */   }
/*     */ 
/*     */   protected final void generateArray() {
/*  96 */     this.field = this.outline.implClass.field(2, getCoreListType(), this.prop.getName(false));
/*  97 */     annotate(this.field);
/*     */ 
/* 100 */     generateAccessors();
/*     */   }
/*     */ 
/*     */   public void generateAccessors()
/*     */   {
/* 105 */     MethodWriter writer = this.outline.createMethodWriter();
/* 106 */     Accessor acc = create(JExpr._this());
/*     */ 
/* 115 */     this.$getAll = writer.declareMethod(this.exposedType.array(), "get" + this.prop.getName(true));
/* 116 */     writer.javadoc().append(this.prop.javadoc);
/* 117 */     JBlock body = this.$getAll.body();
/*     */ 
/* 119 */     body._if(acc.ref(true).eq(JExpr._null()))._then()
/* 120 */       ._return(JExpr.newArray(this.exposedType, 0));
/*     */ 
/* 121 */     JVar var = body.decl(this.exposedType.array(), "retVal", JExpr.newArray(this.implType, acc.ref(true).ref("length")));
/* 122 */     body.add(this.codeModel.ref(System.class).staticInvoke("arraycopy")
/* 123 */       .arg(acc
/* 123 */       .ref(true))
/* 123 */       .arg(JExpr.lit(0))
/* 124 */       .arg(var)
/* 125 */       .arg(JExpr.lit(0))
/* 125 */       .arg(acc.ref(true).ref("length")));
/* 126 */     body._return(JExpr.direct("retVal"));
/*     */ 
/* 128 */     List returnTypes = listPossibleTypes(this.prop);
/* 129 */     writer.javadoc().addReturn().append("array of\n").append(returnTypes);
/*     */ 
/* 136 */     JMethod $get = writer.declareMethod(this.exposedType, "get" + this.prop.getName(true));
/* 137 */     JVar $idx = writer.addParameter(this.codeModel.INT, "idx");
/*     */ 
/* 139 */     $get.body()._if(acc.ref(true).eq(JExpr._null()))._then()
/* 140 */       ._throw(JExpr._new(this.codeModel
/* 140 */       .ref(IndexOutOfBoundsException.class)));
/*     */ 
/* 142 */     writer.javadoc().append(this.prop.javadoc);
/* 143 */     $get.body()._return(acc.ref(true).component($idx));
/*     */ 
/* 145 */     writer.javadoc().addReturn().append("one of\n").append(returnTypes);
/*     */ 
/* 151 */     JMethod $getLength = writer.declareMethod(this.codeModel.INT, "get" + this.prop.getName(true) + "Length");
/* 152 */     $getLength.body()._if(acc.ref(true).eq(JExpr._null()))._then()
/* 153 */       ._return(JExpr.lit(0));
/*     */ 
/* 154 */     $getLength.body()._return(acc.ref(true).ref("length"));
/*     */ 
/* 161 */     this.$setAll = writer.declareMethod(this.codeModel.VOID, "set" + this.prop
/* 163 */       .getName(true));
/*     */ 
/* 165 */     writer.javadoc().append(this.prop.javadoc);
/* 166 */     JVar $value = writer.addParameter(this.exposedType.array(), "values");
/* 167 */     JVar $len = this.$setAll.body().decl(this.codeModel.INT, "len", $value.ref("length"));
/*     */ 
/* 169 */     this.$setAll.body().assign(
/* 170 */       (JAssignmentTarget)acc
/* 170 */       .ref(true), 
/* 171 */       castToImplTypeArray(JExpr.newArray(this.codeModel
/* 172 */       .ref(this.exposedType
/* 172 */       .erasure().fullName()), $len)));
/*     */ 
/* 175 */     JForLoop _for = this.$setAll.body()._for();
/* 176 */     JVar $i = _for.init(this.codeModel.INT, "i", JExpr.lit(0));
/* 177 */     _for.test(JOp.lt($i, $len));
/* 178 */     _for.update($i.incr());
/* 179 */     _for.body().assign(acc.ref(true).component($i), castToImplType(acc.box($value.component($i))));
/*     */ 
/* 181 */     writer.javadoc().addParam($value)
/* 182 */       .append("allowed objects are\n")
/* 183 */       .append(returnTypes);
/*     */ 
/* 187 */     JMethod $set = writer.declareMethod(this.exposedType, "set" + this.prop
/* 189 */       .getName(true));
/*     */ 
/* 190 */     $idx = writer.addParameter(this.codeModel.INT, "idx");
/* 191 */     $value = writer.addParameter(this.exposedType, "value");
/*     */ 
/* 193 */     writer.javadoc().append(this.prop.javadoc);
/*     */ 
/* 195 */     body = $set.body();
/* 196 */     body._return(JExpr.assign(acc.ref(true).component($idx), 
/* 197 */       castToImplType(acc
/* 197 */       .box($value))));
/*     */ 
/* 199 */     writer.javadoc().addParam($value)
/* 200 */       .append("allowed object is\n")
/* 201 */       .append(returnTypes);
/*     */   }
/*     */ 
/*     */   public JType getRawType()
/*     */   {
/* 207 */     return this.exposedType.array();
/*     */   }
/*     */ 
/*     */   protected JClass getCoreListType() {
/* 211 */     return this.exposedType.array();
/*     */   }
/*     */ 
/*     */   public Accessor create(JExpression targetObject) {
/* 215 */     return new Accessor(targetObject);
/*     */   }
/*     */ 
/*     */   protected final JExpression castToImplTypeArray(JExpression exp)
/*     */   {
/* 222 */     return JExpr.cast(this.implType.array(), exp);
/*     */   }
/*     */ 
/*     */   class Accessor extends AbstractListField.Accessor
/*     */   {
/*     */     protected Accessor(JExpression $target)
/*     */     {
/*  68 */       super($target);
/*     */     }
/*     */ 
/*     */     public void toRawValue(JBlock block, JVar $var) {
/*  72 */       block.assign($var, this.$target.invoke(ArrayField.this.$getAll));
/*     */     }
/*     */ 
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/*  76 */       block.invoke(this.$target, ArrayField.this.$setAll).arg($var);
/*     */     }
/*     */ 
/*     */     public JExpression hasSetValue()
/*     */     {
/*  81 */       return this.field.ne(JExpr._null()).cand(this.field.ref("length").gt(JExpr.lit(0)));
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.ArrayField
 * JD-Core Version:    0.6.2
 */