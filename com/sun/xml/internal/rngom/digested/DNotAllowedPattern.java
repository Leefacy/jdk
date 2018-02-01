/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DNotAllowedPattern extends DPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     return false;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 56 */     return visitor.onNotAllowed(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DNotAllowedPattern
 * JD-Core Version:    0.6.2
 */