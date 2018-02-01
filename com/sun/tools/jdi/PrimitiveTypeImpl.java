/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveType;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ abstract class PrimitiveTypeImpl extends TypeImpl
/*    */   implements PrimitiveType
/*    */ {
/*    */   PrimitiveTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 33 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   abstract PrimitiveValue convert(PrimitiveValue paramPrimitiveValue)
/*    */     throws InvalidTypeException;
/*    */ 
/*    */   public String toString()
/*    */   {
/* 42 */     return name();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.PrimitiveTypeImpl
 * JD-Core Version:    0.6.2
 */