/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class NoExtendedContentField extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */ 
/*     */   protected NoExtendedContentField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList)
/*     */   {
/* 100 */     super(context, prop, false);
/* 101 */     this.coreList = coreList;
/* 102 */     generate();
/*     */   }
/*     */ 
/*     */   protected final JClass getCoreListType() {
/* 106 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   public void generateAccessors()
/*     */   {
/* 111 */     MethodWriter writer = this.outline.createMethodWriter();
/* 112 */     Accessor acc = create(JExpr._this());
/*     */ 
/* 118 */     this.$get = writer.declareMethod(this.listT, "get" + this.prop.getName(true));
/* 119 */     writer.javadoc().append(this.prop.javadoc);
/* 120 */     JBlock block = this.$get.body();
/* 121 */     fixNullRef(block);
/* 122 */     block._return(acc.ref(true));
/*     */ 
/* 124 */     String pname = NameConverter.standard.toVariableName(this.prop.getName(true));
/* 125 */     writer.javadoc().append("Gets the value of the " + pname + " property.\n\n" + "<p>\n" + "This accessor method returns a reference to the live list,\n" + "not a snapshot. Therefore any modification you make to the\n" + "returned list will be present inside the JAXB object.\n" + "This is why there is not a <CODE>set</CODE> method for the " + pname + " property.\n" + "\n" + "<p>\n" + "For example, to add a new item, do as follows:\n" + "<pre>\n" + "   get" + this.prop
/* 136 */       .getName(true) + 
/* 136 */       "().add(newItem);\n" + "</pre>\n" + "\n\n");
/*     */ 
/* 141 */     writer.javadoc().append("<p>\nObjects of the following type(s) are allowed in the list\n")
/* 144 */       .append(listPossibleTypes(this.prop));
/*     */   }
/*     */ 
/*     */   public Accessor create(JExpression targetObject)
/*     */   {
/* 148 */     return new Accessor(targetObject);
/*     */   }
/*     */ 
/*     */   protected JType getType(Aspect aspect)
/*     */   {
/* 175 */     if (Aspect.IMPLEMENTATION.equals(aspect)) {
/* 176 */       return super.getType(aspect);
/*     */     }
/*     */ 
/* 179 */     if ((this.prop instanceof CReferencePropertyInfo)) {
/* 180 */       Set elements = ((CReferencePropertyInfo)this.prop).getElements();
/* 181 */       if ((elements != null) && (elements.size() > 0)) {
/* 182 */         return this.codeModel.ref(Serializable.class);
/*     */       }
/*     */     }
/*     */ 
/* 186 */     return this.codeModel.ref(String.class);
/*     */   }
/*     */ 
/*     */   class Accessor extends AbstractListField.Accessor
/*     */   {
/*     */     protected Accessor(JExpression $target)
/*     */     {
/* 153 */       super($target);
/*     */     }
/*     */ 
/*     */     public void toRawValue(JBlock block, JVar $var)
/*     */     {
/* 160 */       block.assign($var, JExpr._new(NoExtendedContentField.this.codeModel.ref(ArrayList.class).narrow(NoExtendedContentField.this.getType(Aspect.EXPOSED).boxify())).arg(this.$target
/* 161 */         .invoke(NoExtendedContentField.this.$get)));
/*     */     }
/*     */ 
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var)
/*     */     {
/* 168 */       JVar $list = block.decl(NoExtendedContentField.this.listT, uniqueName + 'l', this.$target.invoke(NoExtendedContentField.this.$get));
/* 169 */       block.invoke($list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.NoExtendedContentField
 * JD-Core Version:    0.6.2
 */