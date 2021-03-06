/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class ConnectedComponent
/*    */   implements Iterable<Element>
/*    */ {
/* 42 */   private final List<Element> elements = new ArrayList();
/*    */   boolean isRequired;
/*    */ 
/*    */   public final boolean isCollection()
/*    */   {
/* 54 */     assert (!this.elements.isEmpty());
/*    */ 
/* 58 */     if (this.elements.size() > 1) {
/* 59 */       return true;
/*    */     }
/*    */ 
/* 62 */     Element n = (Element)this.elements.get(0);
/* 63 */     return n.hasSelfLoop();
/*    */   }
/*    */ 
/*    */   public final boolean isRequired()
/*    */   {
/* 75 */     return this.isRequired;
/*    */   }
/*    */ 
/*    */   void add(Element e) {
/* 79 */     assert (!this.elements.contains(e));
/* 80 */     this.elements.add(e);
/*    */   }
/*    */ 
/*    */   public Iterator<Element> iterator() {
/* 84 */     return this.elements.iterator();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 91 */     String s = this.elements.toString();
/* 92 */     if (isRequired())
/* 93 */       s = s + '!';
/* 94 */     if (isCollection())
/* 95 */       s = s + '*';
/* 96 */     return s;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.gbind.ConnectedComponent
 * JD-Core Version:    0.6.2
 */