/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ final class JAnnotationStringValue extends JAnnotationValue
/*    */ {
/*    */   private final JExpression value;
/*    */ 
/*    */   JAnnotationStringValue(JExpression value)
/*    */   {
/* 44 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public void generate(JFormatter f) {
/* 48 */     f.g(this.value);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAnnotationStringValue
 * JD-Core Version:    0.6.2
 */