/*    */ package com.sun.jdi;
/*    */ 
/*    */ import com.sun.tools.jdi.VirtualMachineManagerImpl;
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class Bootstrap
/*    */ {
/*    */   public static synchronized VirtualMachineManager virtualMachineManager()
/*    */   {
/* 55 */     return VirtualMachineManagerImpl.virtualMachineManager();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Bootstrap
 * JD-Core Version:    0.6.2
 */