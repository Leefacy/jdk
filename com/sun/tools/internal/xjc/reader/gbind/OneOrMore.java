/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ public final class OneOrMore extends Expression
/*    */ {
/*    */   private final Expression child;
/*    */ 
/*    */   public OneOrMore(Expression child)
/*    */   {
/* 40 */     this.child = child;
/*    */   }
/*    */ 
/*    */   ElementSet lastSet() {
/* 44 */     return this.child.lastSet();
/*    */   }
/*    */ 
/*    */   boolean isNullable() {
/* 48 */     return this.child.isNullable();
/*    */   }
/*    */ 
/*    */   void buildDAG(ElementSet incoming) {
/* 52 */     this.child.buildDAG(ElementSets.union(incoming, this.child.lastSet()));
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 56 */     return this.child.toString() + '+';
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.gbind.OneOrMore
 * JD-Core Version:    0.6.2
 */