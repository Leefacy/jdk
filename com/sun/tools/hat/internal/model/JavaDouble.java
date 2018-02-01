/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaDouble extends JavaValue
/*    */ {
/*    */   double value;
/*    */ 
/*    */   public JavaDouble(double paramDouble)
/*    */   {
/* 47 */     this.value = paramDouble;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return Double.toString(this.value);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaDouble
 * JD-Core Version:    0.6.2
 */