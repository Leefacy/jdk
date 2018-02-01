/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public abstract class DContainerPattern extends DPattern
/*    */   implements Iterable<DPattern>
/*    */ {
/*    */   private DPattern head;
/*    */   private DPattern tail;
/*    */ 
/*    */   public DPattern firstChild()
/*    */   {
/* 61 */     return this.head;
/*    */   }
/*    */ 
/*    */   public DPattern lastChild() {
/* 65 */     return this.tail;
/*    */   }
/*    */ 
/*    */   public int countChildren() {
/* 69 */     int i = 0;
/* 70 */     for (DPattern p = firstChild(); p != null; p = p.next)
/* 71 */       i++;
/* 72 */     return i;
/*    */   }
/*    */ 
/*    */   public Iterator<DPattern> iterator() {
/* 76 */     return new Iterator() {
/* 77 */       DPattern next = DContainerPattern.this.head;
/*    */ 
/* 79 */       public boolean hasNext() { return this.next != null; }
/*    */ 
/*    */       public DPattern next()
/*    */       {
/* 83 */         DPattern r = this.next;
/* 84 */         this.next = this.next.next;
/* 85 */         return r;
/*    */       }
/*    */ 
/*    */       public void remove() {
/* 89 */         throw new UnsupportedOperationException();
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   void add(DPattern child) {
/* 95 */     if (this.tail == null) {
/* 96 */       child.prev = (child.next = null);
/* 97 */       this.head = (this.tail = child);
/*    */     } else {
/* 99 */       child.prev = this.tail;
/* 100 */       this.tail.next = child;
/* 101 */       child.next = null;
/* 102 */       this.tail = child;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DContainerPattern
 * JD-Core Version:    0.6.2
 */