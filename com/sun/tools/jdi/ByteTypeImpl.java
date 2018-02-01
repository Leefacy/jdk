/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ByteType;
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class ByteTypeImpl extends PrimitiveTypeImpl
/*    */   implements ByteType
/*    */ {
/*    */   ByteTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature()
/*    */   {
/* 37 */     return String.valueOf('B');
/*    */   }
/*    */ 
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedByteValue());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ByteTypeImpl
 * JD-Core Version:    0.6.2
 */