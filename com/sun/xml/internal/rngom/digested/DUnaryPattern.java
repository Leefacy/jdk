/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public abstract class DUnaryPattern extends DPattern
/*    */ {
/*    */   private DPattern child;
/*    */ 
/*    */   public DPattern getChild()
/*    */   {
/* 55 */     return this.child;
/*    */   }
/*    */ 
/*    */   public void setChild(DPattern child) {
/* 59 */     this.child = child;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DUnaryPattern
 * JD-Core Version:    0.6.2
 */