/*    */ package com.sun.tools.internal.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.JBlock;
/*    */ 
/*    */ public class ExistingBlockReference
/*    */   implements BlockReference
/*    */ {
/*    */   private final JBlock block;
/*    */ 
/*    */   public ExistingBlockReference(JBlock _block)
/*    */   {
/* 39 */     this.block = _block;
/*    */   }
/*    */ 
/*    */   public JBlock get(boolean create) {
/* 43 */     return this.block;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.util.ExistingBlockReference
 * JD-Core Version:    0.6.2
 */