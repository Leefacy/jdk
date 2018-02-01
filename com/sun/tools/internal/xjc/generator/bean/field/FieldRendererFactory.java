/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ 
/*    */ public class FieldRendererFactory
/*    */ {
/* 81 */   private final FieldRenderer DEFAULT = new DefaultFieldRenderer(this);
/*    */ 
/* 84 */   private static final FieldRenderer ARRAY = new GenericFieldRenderer(ArrayField.class);
/*    */ 
/* 87 */   private static final FieldRenderer REQUIRED_UNBOXED = new GenericFieldRenderer(UnboxedField.class);
/*    */ 
/* 90 */   private static final FieldRenderer SINGLE = new GenericFieldRenderer(SingleField.class);
/*    */ 
/* 93 */   private static final FieldRenderer SINGLE_PRIMITIVE_ACCESS = new GenericFieldRenderer(SinglePrimitiveAccessField.class);
/*    */ 
/*    */   public FieldRenderer getDefault()
/*    */   {
/* 54 */     return this.DEFAULT;
/*    */   }
/*    */   public FieldRenderer getArray() {
/* 57 */     return ARRAY;
/*    */   }
/*    */   public FieldRenderer getRequiredUnboxed() {
/* 60 */     return REQUIRED_UNBOXED;
/*    */   }
/*    */   public FieldRenderer getSingle() {
/* 63 */     return SINGLE;
/*    */   }
/*    */   public FieldRenderer getSinglePrimitiveAccess() {
/* 66 */     return SINGLE_PRIMITIVE_ACCESS;
/*    */   }
/*    */   public FieldRenderer getList(JClass coreList) {
/* 69 */     return new UntypedListFieldRenderer(coreList);
/*    */   }
/*    */   public FieldRenderer getContentList(JClass coreList) {
/* 72 */     return new UntypedListFieldRenderer(coreList, false, true);
/*    */   }
/*    */   public FieldRenderer getDummyList(JClass coreList) {
/* 75 */     return new UntypedListFieldRenderer(coreList, true, false);
/*    */   }
/*    */   public FieldRenderer getConst(FieldRenderer fallback) {
/* 78 */     return new ConstFieldRenderer(fallback);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory
 * JD-Core Version:    0.6.2
 */