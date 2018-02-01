/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaBoolean extends JavaValue
/*    */ {
/*    */   boolean value;
/*    */ 
/*    */   public JavaBoolean(boolean paramBoolean)
/*    */   {
/* 47 */     this.value = paramBoolean;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return "" + this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaBoolean
 * JD-Core Version:    0.6.2
 */