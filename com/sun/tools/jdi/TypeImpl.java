/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.Type;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ public abstract class TypeImpl extends MirrorImpl
/*    */   implements Type
/*    */ {
/* 32 */   private String myName = null;
/*    */ 
/*    */   TypeImpl(VirtualMachine paramVirtualMachine)
/*    */   {
/* 36 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   public abstract String signature();
/*    */ 
/*    */   public String name() {
/* 42 */     if (this.myName == null) {
/* 43 */       JNITypeParser localJNITypeParser = new JNITypeParser(signature());
/* 44 */       this.myName = localJNITypeParser.typeName();
/*    */     }
/* 46 */     return this.myName;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject) {
/* 50 */     if ((paramObject != null) && ((paramObject instanceof Type))) {
/* 51 */       Type localType = (Type)paramObject;
/*    */ 
/* 53 */       return (signature().equals(localType.signature())) && 
/* 53 */         (super
/* 53 */         .equals(paramObject));
/*    */     }
/*    */ 
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 60 */     return signature().hashCode();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.TypeImpl
 * JD-Core Version:    0.6.2
 */