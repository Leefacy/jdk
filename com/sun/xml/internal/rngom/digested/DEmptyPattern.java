/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DEmptyPattern extends DPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 56 */     return visitor.onEmpty(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DEmptyPattern
 * JD-Core Version:    0.6.2
 */