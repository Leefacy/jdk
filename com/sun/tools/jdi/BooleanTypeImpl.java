/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.BooleanType;
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class BooleanTypeImpl extends PrimitiveTypeImpl
/*    */   implements BooleanType
/*    */ {
/*    */   BooleanTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature() {
/* 36 */     return String.valueOf('Z');
/*    */   }
/*    */ 
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 40 */     return this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedBooleanValue());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.BooleanTypeImpl
 * JD-Core Version:    0.6.2
 */