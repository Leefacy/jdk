/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.LongType;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class LongTypeImpl extends PrimitiveTypeImpl
/*    */   implements LongType
/*    */ {
/*    */   LongTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature()
/*    */   {
/* 37 */     return String.valueOf('J');
/*    */   }
/*    */ 
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedLongValue());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.LongTypeImpl
 * JD-Core Version:    0.6.2
 */