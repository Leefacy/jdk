/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.FloatType;
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class FloatTypeImpl extends PrimitiveTypeImpl
/*    */   implements FloatType
/*    */ {
/*    */   FloatTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature()
/*    */   {
/* 37 */     return String.valueOf('F');
/*    */   }
/*    */ 
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedFloatValue());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.FloatTypeImpl
 * JD-Core Version:    0.6.2
 */