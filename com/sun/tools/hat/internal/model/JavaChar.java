/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaChar extends JavaValue
/*    */ {
/*    */   char value;
/*    */ 
/*    */   public JavaChar(char paramChar)
/*    */   {
/* 47 */     this.value = paramChar;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return "" + this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaChar
 * JD-Core Version:    0.6.2
 */