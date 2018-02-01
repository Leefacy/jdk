/*    */ package com.sun.tools.example.debug.tty;
/*    */ 
/*    */ import com.sun.jdi.ThreadGroupReference;
/*    */ import com.sun.jdi.ThreadReference;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ class ThreadIterator
/*    */   implements Iterator<ThreadReference>
/*    */ {
/* 43 */   Iterator<ThreadReference> it = null;
/*    */   ThreadGroupIterator tgi;
/*    */ 
/*    */   ThreadIterator(ThreadGroupReference paramThreadGroupReference)
/*    */   {
/* 47 */     this.tgi = new ThreadGroupIterator(paramThreadGroupReference);
/*    */   }
/*    */ 
/*    */   ThreadIterator(List<ThreadGroupReference> paramList) {
/* 51 */     this.tgi = new ThreadGroupIterator(paramList);
/*    */   }
/*    */ 
/*    */   ThreadIterator() {
/* 55 */     this.tgi = new ThreadGroupIterator();
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 60 */     while ((this.it == null) || (!this.it.hasNext())) {
/* 61 */       if (!this.tgi.hasNext()) {
/* 62 */         return false;
/*    */       }
/* 64 */       this.it = this.tgi.nextThreadGroup().threads().iterator();
/*    */     }
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   public ThreadReference next()
/*    */   {
/* 71 */     return (ThreadReference)this.it.next();
/*    */   }
/*    */ 
/*    */   public ThreadReference nextThread() {
/* 75 */     return next();
/*    */   }
/*    */ 
/*    */   public void remove()
/*    */   {
/* 80 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.ThreadIterator
 * JD-Core Version:    0.6.2
 */