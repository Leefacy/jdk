/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaInt extends JavaValue
/*    */ {
/*    */   int value;
/*    */ 
/*    */   public JavaInt(int paramInt)
/*    */   {
/* 47 */     this.value = paramInt;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return "" + this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaInt
 * JD-Core Version:    0.6.2
 */