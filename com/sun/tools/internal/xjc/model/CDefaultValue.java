/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JExpression;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ 
/*    */ public abstract class CDefaultValue
/*    */ {
/*    */   public abstract JExpression compute(Outline paramOutline);
/*    */ 
/*    */   public static CDefaultValue create(TypeUse typeUse, final XmlString defaultValue)
/*    */   {
/* 49 */     return new CDefaultValue() {
/*    */       public JExpression compute(Outline outline) {
/* 51 */         return this.val$typeUse.createConstant(outline, defaultValue);
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CDefaultValue
 * JD-Core Version:    0.6.2
 */