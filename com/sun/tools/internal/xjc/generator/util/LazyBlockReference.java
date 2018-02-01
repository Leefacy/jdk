/*    */ package com.sun.tools.internal.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.JBlock;
/*    */ 
/*    */ public abstract class LazyBlockReference
/*    */   implements BlockReference
/*    */ {
/* 38 */   private JBlock block = null;
/*    */ 
/*    */   protected abstract JBlock create();
/*    */ 
/*    */   public JBlock get(boolean create)
/*    */   {
/* 47 */     if (!create) return this.block;
/* 48 */     if (this.block == null)
/* 49 */       this.block = create();
/* 50 */     return this.block;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.util.LazyBlockReference
 * JD-Core Version:    0.6.2
 */