/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ 
/*    */ public class IsSetFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRenderer core;
/*    */   private final boolean generateUnSetMethod;
/*    */   private final boolean generateIsSetMethod;
/*    */ 
/*    */   public IsSetFieldRenderer(FieldRenderer core, boolean generateUnSetMethod, boolean generateIsSetMethod)
/*    */   {
/* 53 */     this.core = core;
/* 54 */     this.generateUnSetMethod = generateUnSetMethod;
/* 55 */     this.generateIsSetMethod = generateIsSetMethod;
/*    */   }
/*    */ 
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop)
/*    */   {
/* 60 */     return new IsSetField(context, prop, this.core
/* 60 */       .generate(context, prop), 
/* 60 */       this.generateUnSetMethod, this.generateIsSetMethod);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.IsSetFieldRenderer
 * JD-Core Version:    0.6.2
 */