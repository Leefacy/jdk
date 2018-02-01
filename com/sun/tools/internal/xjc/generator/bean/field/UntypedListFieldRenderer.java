/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ 
/*    */ public final class UntypedListFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private JClass coreList;
/*    */   private boolean dummy;
/*    */   private boolean content;
/*    */ 
/*    */   protected UntypedListFieldRenderer(JClass coreList)
/*    */   {
/* 46 */     this(coreList, false, false);
/*    */   }
/*    */ 
/*    */   protected UntypedListFieldRenderer(JClass coreList, boolean dummy, boolean content) {
/* 50 */     this.coreList = coreList;
/* 51 */     this.dummy = dummy;
/* 52 */     this.content = content;
/*    */   }
/*    */ 
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop) {
/* 56 */     if (this.dummy) {
/* 57 */       return new DummyListField(context, prop, this.coreList);
/*    */     }
/* 59 */     if (this.content) {
/* 60 */       return new ContentListField(context, prop, this.coreList);
/*    */     }
/* 62 */     return new UntypedListField(context, prop, this.coreList);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.UntypedListFieldRenderer
 * JD-Core Version:    0.6.2
 */