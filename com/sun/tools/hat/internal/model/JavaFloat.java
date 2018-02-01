/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaFloat extends JavaValue
/*    */ {
/*    */   float value;
/*    */ 
/*    */   public JavaFloat(float paramFloat)
/*    */   {
/* 47 */     this.value = paramFloat;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return Float.toString(this.value);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaFloat
 * JD-Core Version:    0.6.2
 */