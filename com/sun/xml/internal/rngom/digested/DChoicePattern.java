/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DChoicePattern extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 55 */     for (DPattern p = firstChild(); p != null; p = p.next)
/* 56 */       if (p.isNullable())
/* 57 */         return true;
/* 58 */     return false;
/*    */   }
/*    */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 61 */     return visitor.onChoice(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DChoicePattern
 * JD-Core Version:    0.6.2
 */