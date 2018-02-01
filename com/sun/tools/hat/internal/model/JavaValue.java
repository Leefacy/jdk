/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public abstract class JavaValue extends JavaThing
/*    */ {
/*    */   public boolean isHeapAllocated()
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   public abstract String toString();
/*    */ 
/*    */   public int getSize()
/*    */   {
/* 58 */     return 0;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaValue
 * JD-Core Version:    0.6.2
 */