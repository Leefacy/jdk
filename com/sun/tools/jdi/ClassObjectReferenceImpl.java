/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ClassObjectReference;
/*    */ import com.sun.jdi.ReferenceType;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class ClassObjectReferenceImpl extends ObjectReferenceImpl
/*    */   implements ClassObjectReference
/*    */ {
/*    */   private ReferenceType reflectedType;
/*    */ 
/*    */   ClassObjectReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong)
/*    */   {
/* 36 */     super(paramVirtualMachine, paramLong);
/*    */   }
/*    */ 
/*    */   public ReferenceType reflectedType() {
/* 40 */     if (this.reflectedType == null) {
/*    */       try
/*    */       {
/* 43 */         JDWP.ClassObjectReference.ReflectedType localReflectedType = JDWP.ClassObjectReference.ReflectedType.process(this.vm, this);
/*    */ 
/* 44 */         this.reflectedType = this.vm.referenceType(localReflectedType.typeID, localReflectedType.refTypeTag);
/*    */       }
/*    */       catch (JDWPException localJDWPException)
/*    */       {
/* 48 */         throw localJDWPException.toJDIException();
/*    */       }
/*    */     }
/* 51 */     return this.reflectedType;
/*    */   }
/*    */ 
/*    */   byte typeValueKey() {
/* 55 */     return 99;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 60 */     return "instance of " + referenceType().name() + "(reflected class=" + 
/* 60 */       reflectedType().name() + ", " + "id=" + uniqueID() + ")";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ClassObjectReferenceImpl
 * JD-Core Version:    0.6.2
 */