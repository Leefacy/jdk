/*    */ package com.sun.tools.internal.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.internal.JEnumConstant;
/*    */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*    */ 
/*    */ public abstract class EnumConstantOutline
/*    */ {
/*    */   public final CEnumConstant target;
/*    */   public final JEnumConstant constRef;
/*    */ 
/*    */   protected EnumConstantOutline(CEnumConstant target, JEnumConstant constRef)
/*    */   {
/* 55 */     this.target = target;
/* 56 */     this.constRef = constRef;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.EnumConstantOutline
 * JD-Core Version:    0.6.2
 */