/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DTextPattern extends DPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 56 */     return visitor.onText(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DTextPattern
 * JD-Core Version:    0.6.2
 */