/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.StringReference;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public class StringReferenceImpl extends ObjectReferenceImpl
/*    */   implements StringReference
/*    */ {
/*    */   private String value;
/*    */ 
/*    */   StringReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong)
/*    */   {
/* 36 */     super(paramVirtualMachine, paramLong);
/*    */   }
/*    */ 
/*    */   public String value() {
/* 40 */     if (this.value == null)
/*    */     {
/*    */       try
/*    */       {
/* 44 */         this.value = 
/* 45 */           JDWP.StringReference.Value.process(this.vm, this).stringValue;
/*    */       }
/*    */       catch (JDWPException localJDWPException) {
/* 47 */         throw localJDWPException.toJDIException();
/*    */       }
/*    */     }
/* 50 */     return this.value;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 54 */     return "\"" + value() + "\"";
/*    */   }
/*    */ 
/*    */   byte typeValueKey() {
/* 58 */     return 115;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.StringReferenceImpl
 * JD-Core Version:    0.6.2
 */