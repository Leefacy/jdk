/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CDefaultValue;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ 
/*    */ final class ConstFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRenderer fallback;
/*    */ 
/*    */   protected ConstFieldRenderer(FieldRenderer fallback)
/*    */   {
/* 47 */     this.fallback = fallback;
/*    */   }
/*    */ 
/*    */   public FieldOutline generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 51 */     if (prop.defaultValue.compute(outline.parent()) == null) {
/* 52 */       return this.fallback.generate(outline, prop);
/*    */     }
/* 54 */     return new ConstField(outline, prop);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.ConstFieldRenderer
 * JD-Core Version:    0.6.2
 */