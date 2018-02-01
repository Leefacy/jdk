/*     */ package com.sun.tools.javac.model;
/*     */ 
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class FilteredMemberList extends AbstractList<Symbol>
/*     */ {
/*     */   private final Scope scope;
/*     */ 
/*     */   public FilteredMemberList(Scope paramScope)
/*     */   {
/*  51 */     this.scope = paramScope;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  55 */     int i = 0;
/*  56 */     for (Scope.Entry localEntry = this.scope.elems; localEntry != null; localEntry = localEntry.sibling) {
/*  57 */       if (!unwanted(localEntry.sym))
/*  58 */         i++;
/*     */     }
/*  60 */     return i;
/*     */   }
/*     */ 
/*     */   public Symbol get(int paramInt) {
/*  64 */     for (Scope.Entry localEntry = this.scope.elems; localEntry != null; localEntry = localEntry.sibling) {
/*  65 */       if ((!unwanted(localEntry.sym)) && (paramInt-- == 0))
/*  66 */         return localEntry.sym;
/*     */     }
/*  68 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */ 
/*     */   public Iterator<Symbol> iterator()
/*     */   {
/*  73 */     return new Iterator()
/*     */     {
/*  76 */       private Scope.Entry nextEntry = FilteredMemberList.this.scope.elems;
/*     */ 
/*  78 */       private boolean hasNextForSure = false;
/*     */ 
/*     */       public boolean hasNext() {
/*  81 */         if (this.hasNextForSure) {
/*  82 */           return true;
/*     */         }
/*  84 */         while ((this.nextEntry != null) && (FilteredMemberList.unwanted(this.nextEntry.sym))) {
/*  85 */           this.nextEntry = this.nextEntry.sibling;
/*     */         }
/*  87 */         this.hasNextForSure = (this.nextEntry != null);
/*  88 */         return this.hasNextForSure;
/*     */       }
/*     */ 
/*     */       public Symbol next() {
/*  92 */         if (hasNext()) {
/*  93 */           Symbol localSymbol = this.nextEntry.sym;
/*  94 */           this.nextEntry = this.nextEntry.sibling;
/*  95 */           this.hasNextForSure = false;
/*  96 */           return localSymbol;
/*     */         }
/*  98 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public void remove()
/*     */       {
/* 103 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static boolean unwanted(Symbol paramSymbol)
/*     */   {
/* 113 */     return (paramSymbol == null) || ((paramSymbol.flags() & 0x1000) != 0L);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.model.FilteredMemberList
 * JD-Core Version:    0.6.2
 */