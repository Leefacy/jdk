/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DOptionalPattern extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 56 */     return visitor.onOptional(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DOptionalPattern
 * JD-Core Version:    0.6.2
 */