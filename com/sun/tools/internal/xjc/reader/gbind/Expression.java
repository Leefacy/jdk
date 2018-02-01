/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ public abstract class Expression
/*    */ {
/* 55 */   public static final Expression EPSILON = new Expression() {
/*    */     ElementSet lastSet() {
/* 57 */       return ElementSet.EMPTY_SET;
/*    */     }
/*    */ 
/*    */     boolean isNullable() {
/* 61 */       return true;
/*    */     }
/*    */ 
/*    */     void buildDAG(ElementSet incoming)
/*    */     {
/*    */     }
/*    */ 
/*    */     public String toString() {
/* 69 */       return "-";
/*    */     }
/* 55 */   };
/*    */ 
/*    */   abstract ElementSet lastSet();
/*    */ 
/*    */   abstract boolean isNullable();
/*    */ 
/*    */   abstract void buildDAG(ElementSet paramElementSet);
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.gbind.Expression
 * JD-Core Version:    0.6.2
 */