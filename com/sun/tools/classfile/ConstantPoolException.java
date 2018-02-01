/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ public class ConstantPoolException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -2324397349644754565L;
/*    */   public final int index;
/*    */ 
/*    */   ConstantPoolException(int paramInt)
/*    */   {
/* 38 */     this.index = paramInt;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.ConstantPoolException
 * JD-Core Version:    0.6.2
 */