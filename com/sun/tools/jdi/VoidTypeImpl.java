/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import com.sun.jdi.VoidType;
/*    */ 
/*    */ public class VoidTypeImpl extends TypeImpl
/*    */   implements VoidType
/*    */ {
/*    */   VoidTypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public String signature() {
/* 36 */     return String.valueOf('V');
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 40 */     return name();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VoidTypeImpl
 * JD-Core Version:    0.6.2
 */