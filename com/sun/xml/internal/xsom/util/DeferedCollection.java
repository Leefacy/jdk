/*     */ package com.sun.xml.internal.xsom.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class DeferedCollection<T>
/*     */   implements Collection<T>
/*     */ {
/*     */   private final Iterator<T> result;
/*  49 */   private final List<T> archive = new ArrayList();
/*     */ 
/*     */   public DeferedCollection(Iterator<T> result) {
/*  52 */     this.result = result;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  56 */     if (this.archive.isEmpty())
/*  57 */       fetch();
/*  58 */     return this.archive.isEmpty();
/*     */   }
/*     */ 
/*     */   public int size() {
/*  62 */     fetchAll();
/*  63 */     return this.archive.size();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object o) {
/*  67 */     if (this.archive.contains(o))
/*  68 */       return true;
/*  69 */     while (this.result.hasNext()) {
/*  70 */       Object value = this.result.next();
/*  71 */       this.archive.add(value);
/*  72 */       if (value.equals(o))
/*  73 */         return true;
/*     */     }
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> c) {
/*  79 */     for (Iterator localIterator = c.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
/*  80 */       if (!contains(o))
/*  81 */         return false;
/*     */     }
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   public Iterator<T> iterator() {
/*  87 */     return new Iterator() {
/*  88 */       int idx = 0;
/*     */ 
/*  90 */       public boolean hasNext() { if (this.idx < DeferedCollection.this.archive.size())
/*  91 */           return true;
/*  92 */         return DeferedCollection.this.result.hasNext(); }
/*     */ 
/*     */       public T next()
/*     */       {
/*  96 */         if (this.idx == DeferedCollection.this.archive.size())
/*  97 */           DeferedCollection.this.fetch();
/*  98 */         if (this.idx == DeferedCollection.this.archive.size())
/*  99 */           throw new NoSuchElementException();
/* 100 */         return DeferedCollection.this.archive.get(this.idx++);
/*     */       }
/*     */ 
/*     */       public void remove()
/*     */       {
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Object[] toArray() {
/* 110 */     fetchAll();
/* 111 */     return this.archive.toArray();
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] a) {
/* 115 */     fetchAll();
/* 116 */     return this.archive.toArray(a);
/*     */   }
/*     */ 
/*     */   private void fetchAll()
/*     */   {
/* 122 */     while (this.result.hasNext())
/* 123 */       this.archive.add(this.result.next());
/*     */   }
/*     */ 
/*     */   private void fetch()
/*     */   {
/* 130 */     if (this.result.hasNext())
/* 131 */       this.archive.add(this.result.next());
/*     */   }
/*     */ 
/*     */   public boolean add(T o)
/*     */   {
/* 136 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean remove(Object o) {
/* 140 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends T> c) {
/* 144 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> c) {
/* 148 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> c) {
/* 152 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 156 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.util.DeferedCollection
 * JD-Core Version:    0.6.2
 */