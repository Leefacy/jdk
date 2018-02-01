/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.CharType;
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class CharTypeImpl extends PrimitiveTypeImpl
/*    */   implements CharType
/*    */ {
/*    */   CharTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature()
/*    */   {
/* 37 */     return String.valueOf('C');
/*    */   }
/*    */ 
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedCharValue());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.CharTypeImpl
 * JD-Core Version:    0.6.2
 */