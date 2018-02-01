/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ 
/*     */ class ThreadGroupIterator
/*     */   implements Iterator<ThreadGroupReference>
/*     */ {
/*  48 */   private final Stack<Iterator<ThreadGroupReference>> stack = new Stack();
/*     */ 
/*     */   ThreadGroupIterator(List<ThreadGroupReference> paramList) {
/*  51 */     push(paramList);
/*     */   }
/*     */ 
/*     */   ThreadGroupIterator(ThreadGroupReference paramThreadGroupReference) {
/*  55 */     ArrayList localArrayList = new ArrayList();
/*  56 */     localArrayList.add(paramThreadGroupReference);
/*  57 */     push(localArrayList);
/*     */   }
/*     */ 
/*     */   ThreadGroupIterator() {
/*  61 */     this(Env.vm().topLevelThreadGroups());
/*     */   }
/*     */ 
/*     */   private Iterator<ThreadGroupReference> top() {
/*  65 */     return (Iterator)this.stack.peek();
/*     */   }
/*     */ 
/*     */   private void push(List<ThreadGroupReference> paramList)
/*     */   {
/*  75 */     this.stack.push(paramList.iterator());
/*  76 */     while ((!this.stack.isEmpty()) && (!top().hasNext()))
/*  77 */       this.stack.pop();
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  83 */     return !this.stack.isEmpty();
/*     */   }
/*     */ 
/*     */   public ThreadGroupReference next()
/*     */   {
/*  88 */     return nextThreadGroup();
/*     */   }
/*     */ 
/*     */   public ThreadGroupReference nextThreadGroup() {
/*  92 */     ThreadGroupReference localThreadGroupReference = (ThreadGroupReference)top().next();
/*  93 */     push(localThreadGroupReference.threadGroups());
/*  94 */     return localThreadGroupReference;
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   static ThreadGroupReference find(String paramString) {
/* 103 */     ThreadGroupIterator localThreadGroupIterator = new ThreadGroupIterator();
/* 104 */     while (localThreadGroupIterator.hasNext()) {
/* 105 */       ThreadGroupReference localThreadGroupReference = localThreadGroupIterator.nextThreadGroup();
/* 106 */       if (localThreadGroupReference.name().equals(paramString)) {
/* 107 */         return localThreadGroupReference;
/*     */       }
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.ThreadGroupIterator
 * JD-Core Version:    0.6.2
 */