/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ public class DOneOrMorePattern extends DUnaryPattern
/*    */ {
/*    */   public boolean isNullable()
/*    */   {
/* 53 */     return getChild().isNullable();
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 56 */     return visitor.onOneOrMore(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DOneOrMorePattern
 * JD-Core Version:    0.6.2
 */