/*    */ package com.sun.tools.internal.xjc.reader.dtd;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ final class Block
/*    */ {
/*    */   final boolean isOptional;
/*    */   final boolean isRepeated;
/* 44 */   final Set<Element> elements = new LinkedHashSet();
/*    */ 
/*    */   Block(boolean optional, boolean repeated) {
/* 47 */     this.isOptional = optional;
/* 48 */     this.isRepeated = repeated;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.Block
 * JD-Core Version:    0.6.2
 */