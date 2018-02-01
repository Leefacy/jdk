/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class HackJavaValue extends JavaValue
/*    */ {
/*    */   private String value;
/*    */   private int size;
/*    */ 
/*    */   public HackJavaValue(String paramString, int paramInt)
/*    */   {
/* 53 */     this.value = paramString;
/* 54 */     this.size = paramInt;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 58 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int getSize() {
/* 62 */     return this.size;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.HackJavaValue
 * JD-Core Version:    0.6.2
 */