/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DListPattern extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     return getChild().isNullable();
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 56 */     return visitor.onList(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DListPattern
 * JD-Core Version:    0.6.2
 */