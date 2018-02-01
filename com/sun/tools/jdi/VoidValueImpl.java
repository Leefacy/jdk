/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.Type;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import com.sun.jdi.VoidValue;
/*    */ 
/*    */ public class VoidValueImpl extends ValueImpl
/*    */   implements VoidValue
/*    */ {
/*    */   VoidValueImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 33 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject) {
/* 37 */     return (paramObject != null) && ((paramObject instanceof VoidValue)) && (super.equals(paramObject));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 44 */     return 47245;
/*    */   }
/*    */ 
/*    */   public Type type() {
/* 48 */     return this.vm.theVoidType();
/*    */   }
/*    */ 
/*    */   ValueImpl prepareForAssignmentTo(ValueContainer paramValueContainer) throws InvalidTypeException
/*    */   {
/* 53 */     if ("void".equals(paramValueContainer.typeName())) {
/* 54 */       return this;
/*    */     }
/* 56 */     throw new InvalidTypeException();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 60 */     return "<void value>";
/*    */   }
/*    */ 
/*    */   byte typeValueKey() {
/* 64 */     return 86;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VoidValueImpl
 * JD-Core Version:    0.6.2
 */