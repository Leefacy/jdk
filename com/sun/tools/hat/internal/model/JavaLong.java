/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaLong extends JavaValue
/*    */ {
/*    */   long value;
/*    */ 
/*    */   public JavaLong(long paramLong)
/*    */   {
/* 47 */     this.value = paramLong;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return Long.toString(this.value);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaLong
 * JD-Core Version:    0.6.2
 */