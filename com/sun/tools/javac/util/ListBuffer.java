/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class ListBuffer<A> extends AbstractQueue<A>
/*     */ {
/*     */   private List<A> elems;
/*     */   private List<A> last;
/*     */   private int count;
/*     */   private boolean shared;
/*     */ 
/*     */   public static <T> ListBuffer<T> of(T paramT)
/*     */   {
/*  44 */     ListBuffer localListBuffer = new ListBuffer();
/*  45 */     localListBuffer.add(paramT);
/*  46 */     return localListBuffer;
/*     */   }
/*     */ 
/*     */   public ListBuffer()
/*     */   {
/*  69 */     clear();
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  73 */     this.elems = List.nil();
/*  74 */     this.last = null;
/*  75 */     this.count = 0;
/*  76 */     this.shared = false;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  82 */     return this.count;
/*     */   }
/*     */   public int size() {
/*  85 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  91 */     return this.count == 0;
/*     */   }
/*     */ 
/*     */   public boolean nonEmpty()
/*     */   {
/*  97 */     return this.count != 0;
/*     */   }
/*     */ 
/*     */   private void copy()
/*     */   {
/* 103 */     if (this.elems.nonEmpty()) {
/* 104 */       List localList = this.elems;
/*     */ 
/* 106 */       this.elems = (this.last = List.of(localList.head));
/*     */ 
/* 108 */       while ((localList = localList.tail).nonEmpty()) {
/* 109 */         this.last.tail = List.of(localList.head);
/* 110 */         this.last = this.last.tail;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ListBuffer<A> prepend(A paramA)
/*     */   {
/* 118 */     this.elems = this.elems.prepend(paramA);
/* 119 */     if (this.last == null) this.last = this.elems;
/* 120 */     this.count += 1;
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */   public ListBuffer<A> append(A paramA)
/*     */   {
/* 127 */     paramA.getClass();
/* 128 */     if (this.shared) copy();
/* 129 */     List localList = List.of(paramA);
/* 130 */     if (this.last != null) {
/* 131 */       this.last.tail = localList;
/* 132 */       this.last = localList;
/*     */     } else {
/* 134 */       this.elems = (this.last = localList);
/*     */     }
/* 136 */     this.count += 1;
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   public ListBuffer<A> appendList(List<A> paramList)
/*     */   {
/* 143 */     while (paramList.nonEmpty()) {
/* 144 */       append(paramList.head);
/* 145 */       paramList = paramList.tail;
/*     */     }
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */   public ListBuffer<A> appendList(ListBuffer<A> paramListBuffer)
/*     */   {
/* 153 */     return appendList(paramListBuffer.toList());
/*     */   }
/*     */ 
/*     */   public ListBuffer<A> appendArray(A[] paramArrayOfA)
/*     */   {
/* 159 */     for (int i = 0; i < paramArrayOfA.length; i++) {
/* 160 */       append(paramArrayOfA[i]);
/*     */     }
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   public List<A> toList()
/*     */   {
/* 168 */     this.shared = true;
/* 169 */     return this.elems;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 175 */     return this.elems.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 181 */     return this.elems.toArray(paramArrayOfT);
/*     */   }
/*     */   public Object[] toArray() {
/* 184 */     return toArray(new Object[size()]);
/*     */   }
/*     */ 
/*     */   public A first()
/*     */   {
/* 190 */     return this.elems.head;
/*     */   }
/*     */ 
/*     */   public A next()
/*     */   {
/* 196 */     Object localObject = this.elems.head;
/* 197 */     if (!this.elems.isEmpty()) {
/* 198 */       this.elems = this.elems.tail;
/* 199 */       if (this.elems.isEmpty()) this.last = null;
/* 200 */       this.count -= 1;
/*     */     }
/* 202 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Iterator<A> iterator()
/*     */   {
/* 208 */     return new Iterator() {
/* 209 */       List<A> elems = ListBuffer.this.elems;
/*     */ 
/* 211 */       public boolean hasNext() { return !this.elems.isEmpty(); }
/*     */ 
/*     */       public A next() {
/* 214 */         if (this.elems.isEmpty())
/* 215 */           throw new NoSuchElementException();
/* 216 */         Object localObject = this.elems.head;
/* 217 */         this.elems = this.elems.tail;
/* 218 */         return localObject;
/*     */       }
/*     */       public void remove() {
/* 221 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean add(A paramA) {
/* 227 */     append(paramA);
/* 228 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject) {
/* 232 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> paramCollection) {
/* 236 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 237 */       if (!contains(localObject))
/* 238 */         return false;
/*     */     }
/* 240 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends A> paramCollection) {
/* 244 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 245 */       append(localObject); }
/* 246 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection) {
/* 250 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> paramCollection) {
/* 254 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean offer(A paramA) {
/* 258 */     append(paramA);
/* 259 */     return true;
/*     */   }
/*     */ 
/*     */   public A poll() {
/* 263 */     return next();
/*     */   }
/*     */ 
/*     */   public A peek() {
/* 267 */     return first();
/*     */   }
/*     */ 
/*     */   public A last() {
/* 271 */     return this.last != null ? this.last.head : null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.ListBuffer
 * JD-Core Version:    0.6.2
 */