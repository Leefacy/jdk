/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ abstract interface ElementSet extends Iterable<Element>
/*    */ {
/* 42 */   public static final ElementSet EMPTY_SET = new ElementSet()
/*    */   {
/*    */     public void addNext(Element element) {
/*    */     }
/*    */ 
/*    */     public boolean contains(ElementSet element) {
/* 48 */       return this == element;
/*    */     }
/*    */ 
/*    */     public Iterator<Element> iterator() {
/* 52 */       return Collections.emptySet().iterator();
/*    */     }
/* 42 */   };
/*    */ 
/*    */   public abstract void addNext(Element paramElement);
/*    */ 
/*    */   public abstract boolean contains(ElementSet paramElementSet);
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.gbind.ElementSet
 * JD-Core Version:    0.6.2
 */