/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaByte extends JavaValue
/*    */ {
/*    */   byte value;
/*    */ 
/*    */   public JavaByte(byte paramByte)
/*    */   {
/* 47 */     this.value = paramByte;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 51 */     return "0x" + Integer.toString(this.value & 0xFF, 16);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaByte
 * JD-Core Version:    0.6.2
 */