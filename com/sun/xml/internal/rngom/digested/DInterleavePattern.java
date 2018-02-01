/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DInterleavePattern extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     for (DPattern p = firstChild(); p != null; p = p.next)
/* 54 */       if (!p.isNullable())
/* 55 */         return false;
/* 56 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 59 */     return visitor.onInterleave(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DInterleavePattern
 * JD-Core Version:    0.6.2
 */