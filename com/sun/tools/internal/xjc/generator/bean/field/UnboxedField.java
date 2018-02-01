/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ 
/*     */ public class UnboxedField extends AbstractFieldWithVar
/*     */ {
/*     */   private final JPrimitiveType ptype;
/*     */ 
/*     */   protected UnboxedField(ClassOutlineImpl outline, CPropertyInfo prop)
/*     */   {
/*  57 */     super(outline, prop);
/*     */ 
/*  59 */     assert (this.implType == this.exposedType);
/*     */ 
/*  61 */     this.ptype = ((JPrimitiveType)this.implType);
/*  62 */     assert (this.ptype != null);
/*     */ 
/*  64 */     createField();
/*     */ 
/*  70 */     MethodWriter writer = outline.createMethodWriter();
/*  71 */     NameConverter nc = outline.parent().getModel().getNameConverter();
/*     */ 
/*  79 */     JMethod $get = writer.declareMethod(this.ptype, getGetterMethod());
/*  80 */     String javadoc = prop.javadoc;
/*  81 */     if (javadoc.length() == 0)
/*  82 */       javadoc = Messages.DEFAULT_GETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) });
/*  83 */     writer.javadoc().append(javadoc);
/*     */ 
/*  85 */     $get.body()._return(ref());
/*     */ 
/*  92 */     JMethod $set = writer.declareMethod(this.codeModel.VOID, "set" + prop.getName(true));
/*  93 */     JVar $value = writer.addParameter(this.ptype, "value");
/*  94 */     JBlock body = $set.body();
/*  95 */     body.assign(JExpr._this().ref(ref()), $value);
/*     */ 
/*  97 */     writer.javadoc().append(Messages.DEFAULT_SETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }));
/*     */   }
/*     */ 
/*     */   protected JType getType(Aspect aspect)
/*     */   {
/* 102 */     return super.getType(aspect).boxify().getPrimitiveType();
/*     */   }
/*     */ 
/*     */   protected JType getFieldType() {
/* 106 */     return this.ptype;
/*     */   }
/*     */ 
/*     */   public FieldAccessor create(JExpression targetObject)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 100	com/sun/tools/internal/xjc/generator/bean/field/UnboxedField$1
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: invokespecial 216	com/sun/tools/internal/xjc/generator/bean/field/UnboxedField$1:<init>	(Lcom/sun/tools/internal/xjc/generator/bean/field/UnboxedField;Lcom/sun/codemodel/internal/JExpression;)V
/*     */     //   9: areturn
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.UnboxedField
 * JD-Core Version:    0.6.2
 */