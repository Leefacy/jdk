/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class ReferenceChain
/*    */ {
/*    */   JavaHeapObject obj;
/*    */   ReferenceChain next;
/*    */ 
/*    */   public ReferenceChain(JavaHeapObject paramJavaHeapObject, ReferenceChain paramReferenceChain)
/*    */   {
/* 47 */     this.obj = paramJavaHeapObject;
/* 48 */     this.next = paramReferenceChain;
/*    */   }
/*    */ 
/*    */   public JavaHeapObject getObj() {
/* 52 */     return this.obj;
/*    */   }
/*    */ 
/*    */   public ReferenceChain getNext() {
/* 56 */     return this.next;
/*    */   }
/*    */ 
/*    */   public int getDepth() {
/* 60 */     int i = 1;
/* 61 */     ReferenceChain localReferenceChain = this.next;
/* 62 */     while (localReferenceChain != null) {
/* 63 */       i++;
/* 64 */       localReferenceChain = localReferenceChain.next;
/*    */     }
/* 66 */     return i;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.ReferenceChain
 * JD-Core Version:    0.6.2
 */