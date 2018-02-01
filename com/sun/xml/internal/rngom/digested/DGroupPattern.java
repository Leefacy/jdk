/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DGroupPattern extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     for (DPattern p = firstChild(); p != null; p = p.next)
/* 54 */       if (!p.isNullable())
/* 55 */         return false;
/* 56 */     return true;
/*    */   }
/*    */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 59 */     return visitor.onGroup(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DGroupPattern
 * JD-Core Version:    0.6.2
 */