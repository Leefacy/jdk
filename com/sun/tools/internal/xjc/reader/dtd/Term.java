/*    */ package com.sun.tools.internal.xjc.reader.dtd;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ abstract class Term
/*    */ {
/* 47 */   static final Term EMPTY = new Term() {
/*    */     void normalize(List<Block> r, boolean optional) {
/*    */     }
/*    */ 
/*    */     void addAllElements(Block b) {
/*    */     }
/*    */ 
/*    */     boolean isOptional() {
/* 55 */       return false;
/*    */     }
/*    */ 
/*    */     boolean isRepeated() {
/* 59 */       return false;
/*    */     }
/* 47 */   };
/*    */ 
/*    */   abstract void normalize(List<Block> paramList, boolean paramBoolean);
/*    */ 
/*    */   abstract void addAllElements(Block paramBlock);
/*    */ 
/*    */   abstract boolean isOptional();
/*    */ 
/*    */   abstract boolean isRepeated();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.Term
 * JD-Core Version:    0.6.2
 */