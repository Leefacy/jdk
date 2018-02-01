/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Attribute.Array;
/*     */ import com.sun.tools.javac.code.Attribute.Class;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Attribute.Constant;
/*     */ import com.sun.tools.javac.code.Attribute.Enum;
/*     */ import com.sun.tools.javac.code.Attribute.Error;
/*     */ import com.sun.tools.javac.code.Attribute.Visitor;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ 
/*     */ public class AnnotationValueImpl
/*     */   implements AnnotationValue
/*     */ {
/*     */   private final DocEnv env;
/*     */   private final Attribute attr;
/*     */ 
/*     */   AnnotationValueImpl(DocEnv paramDocEnv, Attribute paramAttribute)
/*     */   {
/*  53 */     this.env = paramDocEnv;
/*  54 */     this.attr = paramAttribute;
/*     */   }
/*     */ 
/*     */   public Object value()
/*     */   {
/*  69 */     ValueVisitor localValueVisitor = new ValueVisitor(null);
/*  70 */     this.attr.accept(localValueVisitor);
/*  71 */     return localValueVisitor.value;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 121 */     ToStringVisitor localToStringVisitor = new ToStringVisitor(null);
/* 122 */     this.attr.accept(localToStringVisitor);
/* 123 */     return localToStringVisitor.toString();
/*     */   }
/*     */ 
/*     */   private class ToStringVisitor implements Attribute.Visitor {
/* 127 */     private final StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     private ToStringVisitor() {
/*     */     }
/* 131 */     public String toString() { return this.sb.toString(); }
/*     */ 
/*     */     public void visitConstant(Attribute.Constant paramConstant)
/*     */     {
/* 135 */       if (paramConstant.type.hasTag(TypeTag.BOOLEAN))
/*     */       {
/* 137 */         this.sb.append(((Integer)paramConstant.value).intValue() != 0);
/*     */       }
/* 139 */       else this.sb.append(FieldDocImpl.constantValueExpression(paramConstant.value));
/*     */     }
/*     */ 
/*     */     public void visitClass(Attribute.Class paramClass)
/*     */     {
/* 144 */       this.sb.append(paramClass);
/*     */     }
/*     */ 
/*     */     public void visitEnum(Attribute.Enum paramEnum) {
/* 148 */       this.sb.append(paramEnum);
/*     */     }
/*     */ 
/*     */     public void visitCompound(Attribute.Compound paramCompound) {
/* 152 */       this.sb.append(new AnnotationDescImpl(AnnotationValueImpl.this.env, paramCompound));
/*     */     }
/*     */ 
/*     */     public void visitArray(Attribute.Array paramArray)
/*     */     {
/* 157 */       if (paramArray.values.length != 1) this.sb.append('{');
/*     */ 
/* 159 */       int i = 1;
/* 160 */       for (Attribute localAttribute : paramArray.values) {
/* 161 */         if (i != 0)
/* 162 */           i = 0;
/*     */         else {
/* 164 */           this.sb.append(", ");
/*     */         }
/* 166 */         localAttribute.accept(this);
/*     */       }
/*     */ 
/* 169 */       if (paramArray.values.length != 1) this.sb.append('}'); 
/*     */     }
/*     */ 
/*     */     public void visitError(Attribute.Error paramError)
/*     */     {
/* 173 */       this.sb.append("<error>");
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ValueVisitor
/*     */     implements Attribute.Visitor
/*     */   {
/*     */     public Object value;
/*     */ 
/*     */     private ValueVisitor()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitConstant(Attribute.Constant paramConstant)
/*     */     {
/*  78 */       if (paramConstant.type.hasTag(TypeTag.BOOLEAN))
/*     */       {
/*  80 */         this.value = Boolean.valueOf(((Integer)paramConstant.value)
/*  81 */           .intValue() != 0);
/*     */       }
/*  83 */       else this.value = paramConstant.value;
/*     */     }
/*     */ 
/*     */     public void visitClass(Attribute.Class paramClass)
/*     */     {
/*  88 */       this.value = TypeMaker.getType(AnnotationValueImpl.this.env, AnnotationValueImpl.this.env.types
/*  89 */         .erasure(paramClass.classType));
/*     */     }
/*     */ 
/*     */     public void visitEnum(Attribute.Enum paramEnum) {
/*  93 */       this.value = AnnotationValueImpl.this.env.getFieldDoc(paramEnum.value);
/*     */     }
/*     */ 
/*     */     public void visitCompound(Attribute.Compound paramCompound) {
/*  97 */       this.value = new AnnotationDescImpl(AnnotationValueImpl.this.env, paramCompound);
/*     */     }
/*     */ 
/*     */     public void visitArray(Attribute.Array paramArray) {
/* 101 */       AnnotationValue[] arrayOfAnnotationValue = new AnnotationValue[paramArray.values.length];
/* 102 */       for (int i = 0; i < arrayOfAnnotationValue.length; i++) {
/* 103 */         arrayOfAnnotationValue[i] = new AnnotationValueImpl(AnnotationValueImpl.this.env, paramArray.values[i]);
/*     */       }
/* 105 */       this.value = arrayOfAnnotationValue;
/*     */     }
/*     */ 
/*     */     public void visitError(Attribute.Error paramError) {
/* 109 */       this.value = "<error>";
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.AnnotationValueImpl
 * JD-Core Version:    0.6.2
 */