/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ThreadReference;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import java.util.EventObject;
/*    */ 
/*    */ class VMAction extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = -1701944679310296090L;
/*    */   static final int VM_SUSPENDED = 1;
/*    */   static final int VM_NOT_SUSPENDED = 2;
/*    */   int id;
/*    */   ThreadReference resumingThread;
/*    */ 
/*    */   VMAction(VirtualMachine paramVirtualMachine, int paramInt)
/*    */   {
/* 46 */     this(paramVirtualMachine, null, paramInt);
/*    */   }
/*    */ 
/*    */   VMAction(VirtualMachine paramVirtualMachine, ThreadReference paramThreadReference, int paramInt)
/*    */   {
/* 52 */     super(paramVirtualMachine);
/* 53 */     this.id = paramInt;
/* 54 */     this.resumingThread = paramThreadReference;
/*    */   }
/*    */   VirtualMachine vm() {
/* 57 */     return (VirtualMachine)getSource();
/*    */   }
/*    */   int id() {
/* 60 */     return this.id;
/*    */   }
/*    */ 
/*    */   ThreadReference resumingThread() {
/* 64 */     return this.resumingThread;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VMAction
 * JD-Core Version:    0.6.2
 */