/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveValue;
/*    */ import com.sun.jdi.ShortType;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class ShortTypeImpl extends PrimitiveTypeImpl
/*    */   implements ShortType
/*    */ {
/*    */   ShortTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature()
/*    */   {
/* 37 */     return String.valueOf('S');
/*    */   }
/*    */ 
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedShortValue());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ShortTypeImpl
 * JD-Core Version:    0.6.2
 */