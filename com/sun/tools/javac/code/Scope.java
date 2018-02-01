/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Filter;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class Scope
/*     */ {
/*     */   private int shared;
/*     */   public Scope next;
/*     */   public Symbol owner;
/*     */   Entry[] table;
/*     */   int hashMask;
/*     */   public Entry elems;
/*  74 */   int nelems = 0;
/*     */ 
/*  78 */   List<ScopeListener> listeners = List.nil();
/*     */ 
/*  83 */   private static final Entry sentinel = new Entry(null, null, null, null);
/*     */   private static final int INITIAL_SIZE = 16;
/*  91 */   public static final Scope emptyScope = new Scope(null, null, new Entry[0]);
/*     */ 
/* 308 */   static final Filter<Symbol> noFilter = new Filter() {
/*     */     public boolean accepts(Symbol paramAnonymousSymbol) {
/* 310 */       return true;
/*     */     }
/* 308 */   };
/*     */ 
/*     */   private Scope(Scope paramScope, Symbol paramSymbol, Entry[] paramArrayOfEntry)
/*     */   {
/*  97 */     this.next = paramScope;
/*  98 */     Assert.check((emptyScope == null) || (paramSymbol != null));
/*  99 */     this.owner = paramSymbol;
/* 100 */     this.table = paramArrayOfEntry;
/* 101 */     this.hashMask = (paramArrayOfEntry.length - 1);
/*     */   }
/*     */ 
/*     */   private Scope(Scope paramScope, Symbol paramSymbol, Entry[] paramArrayOfEntry, int paramInt)
/*     */   {
/* 106 */     this(paramScope, paramSymbol, paramArrayOfEntry);
/* 107 */     this.nelems = paramInt;
/*     */   }
/*     */ 
/*     */   public Scope(Symbol paramSymbol)
/*     */   {
/* 114 */     this(null, paramSymbol, new Entry[16]);
/*     */   }
/*     */ 
/*     */   public Scope dup()
/*     */   {
/* 123 */     return dup(this.owner);
/*     */   }
/*     */ 
/*     */   public Scope dup(Symbol paramSymbol)
/*     */   {
/* 132 */     Scope localScope = new Scope(this, paramSymbol, this.table, this.nelems);
/* 133 */     this.shared += 1;
/*     */ 
/* 136 */     return localScope;
/*     */   }
/*     */ 
/*     */   public Scope dupUnshared()
/*     */   {
/* 144 */     return new Scope(this, this.owner, (Entry[])this.table.clone(), this.nelems);
/*     */   }
/*     */ 
/*     */   public Scope leave()
/*     */   {
/* 151 */     Assert.check(this.shared == 0);
/* 152 */     if (this.table != this.next.table) return this.next;
/* 153 */     while (this.elems != null) {
/* 154 */       int i = getIndex(this.elems.sym.name);
/* 155 */       Entry localEntry = this.table[i];
/* 156 */       Assert.check(localEntry == this.elems, this.elems.sym);
/* 157 */       this.table[i] = this.elems.shadowed;
/* 158 */       this.elems = this.elems.sibling;
/*     */     }
/* 160 */     Assert.check(this.next.shared > 0);
/* 161 */     this.next.shared -= 1;
/* 162 */     this.next.nelems = this.nelems;
/*     */ 
/* 165 */     return this.next;
/*     */   }
/*     */ 
/*     */   private void dble()
/*     */   {
/* 171 */     Assert.check(this.shared == 0);
/* 172 */     Entry[] arrayOfEntry1 = this.table;
/* 173 */     Entry[] arrayOfEntry2 = new Entry[arrayOfEntry1.length * 2];
/* 174 */     for (Scope localScope = this; localScope != null; localScope = localScope.next) {
/* 175 */       if (localScope.table == arrayOfEntry1) {
/* 176 */         Assert.check((localScope == this) || (localScope.shared != 0));
/* 177 */         localScope.table = arrayOfEntry2;
/* 178 */         localScope.hashMask = (arrayOfEntry2.length - 1);
/*     */       }
/*     */     }
/* 181 */     int i = 0;
/* 182 */     int j = arrayOfEntry1.length;
/*     */     while (true) { j--; if (j < 0) break;
/* 183 */       Entry localEntry = arrayOfEntry1[j];
/* 184 */       if ((localEntry != null) && (localEntry != sentinel)) {
/* 185 */         this.table[getIndex(localEntry.sym.name)] = localEntry;
/* 186 */         i++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 191 */     this.nelems = i;
/*     */   }
/*     */ 
/*     */   public void enter(Symbol paramSymbol)
/*     */   {
/* 197 */     Assert.check(this.shared == 0);
/* 198 */     enter(paramSymbol, this);
/*     */   }
/*     */ 
/*     */   public void enter(Symbol paramSymbol, Scope paramScope) {
/* 202 */     enter(paramSymbol, paramScope, paramScope, false);
/*     */   }
/*     */ 
/*     */   public void enter(Symbol paramSymbol, Scope paramScope1, Scope paramScope2, boolean paramBoolean)
/*     */   {
/* 211 */     Assert.check(this.shared == 0);
/* 212 */     if (this.nelems * 3 >= this.hashMask * 2)
/* 213 */       dble();
/* 214 */     int i = getIndex(paramSymbol.name);
/* 215 */     Entry localEntry1 = this.table[i];
/* 216 */     if (localEntry1 == null) {
/* 217 */       localEntry1 = sentinel;
/* 218 */       this.nelems += 1;
/*     */     }
/* 220 */     Entry localEntry2 = makeEntry(paramSymbol, localEntry1, this.elems, paramScope1, paramScope2, paramBoolean);
/* 221 */     this.table[i] = localEntry2;
/* 222 */     this.elems = localEntry2;
/*     */ 
/* 225 */     for (List localList = this.listeners; localList.nonEmpty(); localList = localList.tail)
/* 226 */       ((ScopeListener)localList.head).symbolAdded(paramSymbol, this);
/*     */   }
/*     */ 
/*     */   Entry makeEntry(Symbol paramSymbol, Entry paramEntry1, Entry paramEntry2, Scope paramScope1, Scope paramScope2, boolean paramBoolean)
/*     */   {
/* 231 */     return new Entry(paramSymbol, paramEntry1, paramEntry2, paramScope1);
/*     */   }
/*     */ 
/*     */   public void addScopeListener(ScopeListener paramScopeListener)
/*     */   {
/* 241 */     this.listeners = this.listeners.prepend(paramScopeListener);
/*     */   }
/*     */ 
/*     */   public void remove(final Symbol paramSymbol)
/*     */   {
/* 247 */     Assert.check(this.shared == 0);
/* 248 */     Entry localEntry1 = lookup(paramSymbol.name, new Filter()
/*     */     {
/*     */       public boolean accepts(Symbol paramAnonymousSymbol) {
/* 251 */         return paramAnonymousSymbol == paramSymbol;
/*     */       }
/*     */     });
/* 254 */     if (localEntry1.scope == null) return;
/*     */ 
/* 257 */     int i = getIndex(paramSymbol.name);
/* 258 */     Entry localEntry2 = this.table[i];
/* 259 */     if (localEntry2 == localEntry1)
/* 260 */       this.table[i] = localEntry1.shadowed;
/*     */     else while (true) {
/* 262 */         if (localEntry2.shadowed == localEntry1) {
/* 263 */           localEntry2.shadowed = localEntry1.shadowed;
/* 264 */           break;
/*     */         }
/* 266 */         localEntry2 = localEntry2.shadowed;
/*     */       }
/*     */ 
/*     */ 
/* 270 */     localEntry2 = this.elems;
/* 271 */     if (localEntry2 == localEntry1)
/* 272 */       this.elems = localEntry1.sibling;
/*     */     else while (true) {
/* 274 */         if (localEntry2.sibling == localEntry1) {
/* 275 */           localEntry2.sibling = localEntry1.sibling;
/* 276 */           break;
/*     */         }
/* 278 */         localEntry2 = localEntry2.sibling;
/*     */       }
/*     */ 
/*     */ 
/* 282 */     for (List localList = this.listeners; localList.nonEmpty(); localList = localList.tail)
/* 283 */       ((ScopeListener)localList.head).symbolRemoved(paramSymbol, this);
/*     */   }
/*     */ 
/*     */   public void enterIfAbsent(Symbol paramSymbol)
/*     */   {
/* 290 */     Assert.check(this.shared == 0);
/* 291 */     Entry localEntry = lookup(paramSymbol.name);
/* 292 */     while ((localEntry.scope == this) && (localEntry.sym.kind != paramSymbol.kind)) localEntry = localEntry.next();
/* 293 */     if (localEntry.scope != this) enter(paramSymbol);
/*     */   }
/*     */ 
/*     */   public boolean includes(Symbol paramSymbol)
/*     */   {
/* 300 */     for (Entry localEntry = lookup(paramSymbol.name); 
/* 301 */       localEntry.scope == this; 
/* 302 */       localEntry = localEntry.next()) {
/* 303 */       if (localEntry.sym == paramSymbol) return true;
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */   public Entry lookup(Name paramName)
/*     */   {
/* 321 */     return lookup(paramName, noFilter);
/*     */   }
/*     */ 
/*     */   public Entry lookup(Name paramName, Filter<Symbol> paramFilter) {
/* 325 */     Entry localEntry = this.table[getIndex(paramName)];
/* 326 */     if ((localEntry == null) || (localEntry == sentinel))
/* 327 */       return sentinel;
/* 328 */     while ((localEntry.scope != null) && ((localEntry.sym.name != paramName) || (!paramFilter.accepts(localEntry.sym))))
/* 329 */       localEntry = localEntry.shadowed;
/* 330 */     return localEntry;
/*     */   }
/*     */ 
/*     */   int getIndex(Name paramName)
/*     */   {
/* 348 */     int i = paramName.hashCode();
/* 349 */     int j = i & this.hashMask;
/*     */ 
/* 352 */     int k = this.hashMask - (i + (i >> 16) << 1);
/* 353 */     int m = -1;
/*     */     while (true) {
/* 355 */       Entry localEntry = this.table[j];
/* 356 */       if (localEntry == null)
/* 357 */         return m >= 0 ? m : j;
/* 358 */       if (localEntry == sentinel)
/*     */       {
/* 361 */         if (m < 0)
/* 362 */           m = j;
/* 363 */       } else if (localEntry.sym.name == paramName)
/* 364 */         return j;
/* 365 */       j = j + k & this.hashMask;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean anyMatch(Filter<Symbol> paramFilter) {
/* 370 */     return getElements(paramFilter).iterator().hasNext();
/*     */   }
/*     */ 
/*     */   public Iterable<Symbol> getElements() {
/* 374 */     return getElements(noFilter);
/*     */   }
/*     */ 
/*     */   public Iterable<Symbol> getElements(final Filter<Symbol> paramFilter) {
/* 378 */     return new Iterable() {
/*     */       public Iterator<Symbol> iterator() {
/* 380 */         return new Iterator()
/*     */         {
/*     */           private Scope currScope;
/*     */           private Scope.Entry currEntry;
/*     */ 
/*     */           public boolean hasNext()
/*     */           {
/* 388 */             return this.currEntry != null;
/*     */           }
/*     */ 
/*     */           public Symbol next() {
/* 392 */             Symbol localSymbol = this.currEntry == null ? null : this.currEntry.sym;
/* 393 */             if (this.currEntry != null) {
/* 394 */               this.currEntry = this.currEntry.sibling;
/*     */             }
/* 396 */             update();
/* 397 */             return localSymbol;
/*     */           }
/*     */ 
/*     */           public void remove() {
/* 401 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           private void update() {
/* 405 */             skipToNextMatchingEntry();
/* 406 */             while ((this.currEntry == null) && (this.currScope.next != null)) {
/* 407 */               this.currScope = this.currScope.next;
/* 408 */               this.currEntry = this.currScope.elems;
/* 409 */               skipToNextMatchingEntry();
/*     */             }
/*     */           }
/*     */ 
/*     */           void skipToNextMatchingEntry() {
/* 414 */             while ((this.currEntry != null) && (!Scope.3.this.val$sf.accepts(this.currEntry.sym)))
/* 415 */               this.currEntry = this.currEntry.sibling;
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Iterable<Symbol> getElementsByName(Name paramName)
/*     */   {
/* 424 */     return getElementsByName(paramName, noFilter);
/*     */   }
/*     */ 
/*     */   public Iterable<Symbol> getElementsByName(final Name paramName, final Filter<Symbol> paramFilter) {
/* 428 */     return new Iterable() {
/*     */       public Iterator<Symbol> iterator() {
/* 430 */         return new Iterator() {
/* 431 */           Scope.Entry currentEntry = Scope.this.lookup(Scope.4.this.val$name, Scope.4.this.val$sf);
/*     */ 
/*     */           public boolean hasNext() {
/* 434 */             return this.currentEntry.scope != null;
/*     */           }
/*     */           public Symbol next() {
/* 437 */             Scope.Entry localEntry = this.currentEntry;
/* 438 */             this.currentEntry = this.currentEntry.next(Scope.4.this.val$sf);
/* 439 */             return localEntry.sym;
/*     */           }
/*     */           public void remove() {
/* 442 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 450 */     StringBuilder localStringBuilder = new StringBuilder();
/* 451 */     localStringBuilder.append("Scope[");
/* 452 */     for (Scope localScope = this; localScope != null; localScope = localScope.next) {
/* 453 */       if (localScope != this) localStringBuilder.append(" | ");
/* 454 */       for (Entry localEntry = localScope.elems; localEntry != null; localEntry = localEntry.sibling) {
/* 455 */         if (localEntry != localScope.elems) localStringBuilder.append(", ");
/* 456 */         localStringBuilder.append(localEntry.sym);
/*     */       }
/*     */     }
/* 459 */     localStringBuilder.append("]");
/* 460 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static class CompoundScope extends Scope
/*     */     implements Scope.ScopeListener
/*     */   {
/* 607 */     public static final Scope.Entry[] emptyTable = new Scope.Entry[0];
/*     */ 
/* 609 */     private List<Scope> subScopes = List.nil();
/* 610 */     private int mark = 0;
/*     */ 
/*     */     public CompoundScope(Symbol paramSymbol) {
/* 613 */       super(paramSymbol, emptyTable, null);
/*     */     }
/*     */ 
/*     */     public void addSubScope(Scope paramScope) {
/* 617 */       if (paramScope != null) {
/* 618 */         this.subScopes = this.subScopes.prepend(paramScope);
/* 619 */         paramScope.addScopeListener(this);
/* 620 */         this.mark += 1;
/* 621 */         for (Scope.ScopeListener localScopeListener : this.listeners)
/* 622 */           localScopeListener.symbolAdded(null, this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void symbolAdded(Symbol paramSymbol, Scope paramScope)
/*     */     {
/* 628 */       this.mark += 1;
/* 629 */       for (Scope.ScopeListener localScopeListener : this.listeners)
/* 630 */         localScopeListener.symbolAdded(paramSymbol, paramScope);
/*     */     }
/*     */ 
/*     */     public void symbolRemoved(Symbol paramSymbol, Scope paramScope)
/*     */     {
/* 635 */       this.mark += 1;
/* 636 */       for (Scope.ScopeListener localScopeListener : this.listeners)
/* 637 */         localScopeListener.symbolRemoved(paramSymbol, paramScope);
/*     */     }
/*     */ 
/*     */     public int getMark()
/*     */     {
/* 642 */       return this.mark;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 647 */       StringBuilder localStringBuilder = new StringBuilder();
/* 648 */       localStringBuilder.append("CompoundScope{");
/* 649 */       String str = "";
/* 650 */       for (Scope localScope : this.subScopes) {
/* 651 */         localStringBuilder.append(str);
/* 652 */         localStringBuilder.append(localScope);
/* 653 */         str = ",";
/*     */       }
/* 655 */       localStringBuilder.append("}");
/* 656 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public Iterable<Symbol> getElements(final Filter<Symbol> paramFilter)
/*     */     {
/* 661 */       return new Iterable() { public Iterator<Symbol> iterator() { // Byte code:
/*     */           //   0: new 23	com/sun/tools/javac/code/Scope$CompoundScope$1$1
/*     */           //   3: dup
/*     */           //   4: aload_0
/*     */           //   5: aload_0
/*     */           //   6: getfield 41	com/sun/tools/javac/code/Scope$CompoundScope$1:this$0	Lcom/sun/tools/javac/code/Scope$CompoundScope;
/*     */           //   9: invokestatic 43	com/sun/tools/javac/code/Scope$CompoundScope:access$200	(Lcom/sun/tools/javac/code/Scope$CompoundScope;)Lcom/sun/tools/javac/util/List;
/*     */           //   12: invokespecial 44	com/sun/tools/javac/code/Scope$CompoundScope$1$1:<init>	(Lcom/sun/tools/javac/code/Scope$CompoundScope$1;Lcom/sun/tools/javac/util/List;)V
/*     */           //   15: areturn }  } ; } 
/* 674 */     public Iterable<Symbol> getElementsByName(final Name paramName, final Filter<Symbol> paramFilter) { return new Iterable()
/*     */       {
/*     */         public Iterator<Symbol> iterator()
/*     */         {
/*     */           // Byte code:
/*     */           //   0: new 24	com/sun/tools/javac/code/Scope$CompoundScope$2$1
/*     */           //   3: dup
/*     */           //   4: aload_0
/*     */           //   5: aload_0
/*     */           //   6: getfield 44	com/sun/tools/javac/code/Scope$CompoundScope$2:this$0	Lcom/sun/tools/javac/code/Scope$CompoundScope;
/*     */           //   9: invokestatic 47	com/sun/tools/javac/code/Scope$CompoundScope:access$200	(Lcom/sun/tools/javac/code/Scope$CompoundScope;)Lcom/sun/tools/javac/util/List;
/*     */           //   12: invokespecial 48	com/sun/tools/javac/code/Scope$CompoundScope$2$1:<init>	(Lcom/sun/tools/javac/code/Scope$CompoundScope$2;Lcom/sun/tools/javac/util/List;)V
/*     */           //   15: areturn
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public Scope.Entry lookup(Name paramName, Filter<Symbol> paramFilter)
/*     */     {
/* 725 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public Scope dup(Symbol paramSymbol)
/*     */     {
/* 730 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public void enter(Symbol paramSymbol, Scope paramScope1, Scope paramScope2, boolean paramBoolean)
/*     */     {
/* 735 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public void remove(Symbol paramSymbol)
/*     */     {
/* 740 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     abstract class CompoundScopeIterator
/*     */       implements Iterator<Symbol>
/*     */     {
/*     */       private Iterator<Symbol> currentIterator;
/*     */       private List<Scope> scopesToScan;
/*     */ 
/*     */       public CompoundScopeIterator()
/*     */       {
/*     */         Object localObject;
/* 691 */         this.scopesToScan = localObject;
/* 692 */         update();
/*     */       }
/*     */ 
/*     */       abstract Iterator<Symbol> nextIterator(Scope paramScope);
/*     */ 
/*     */       public boolean hasNext() {
/* 698 */         return this.currentIterator != null;
/*     */       }
/*     */ 
/*     */       public Symbol next() {
/* 702 */         Symbol localSymbol = (Symbol)this.currentIterator.next();
/* 703 */         if (!this.currentIterator.hasNext()) {
/* 704 */           update();
/*     */         }
/* 706 */         return localSymbol;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 710 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       private void update() {
/* 714 */         while (this.scopesToScan.nonEmpty()) {
/* 715 */           this.currentIterator = nextIterator((Scope)this.scopesToScan.head);
/* 716 */           this.scopesToScan = this.scopesToScan.tail;
/* 717 */           if (this.currentIterator.hasNext()) return;
/*     */         }
/* 719 */         this.currentIterator = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DelegatedScope extends Scope
/*     */   {
/*     */     Scope delegatee;
/* 570 */     public static final Scope.Entry[] emptyTable = new Scope.Entry[0];
/*     */ 
/*     */     public DelegatedScope(Scope paramScope) {
/* 573 */       super(paramScope.owner, emptyTable, null);
/* 574 */       this.delegatee = paramScope;
/*     */     }
/*     */     public Scope dup() {
/* 577 */       return new DelegatedScope(this.next);
/*     */     }
/*     */     public Scope dupUnshared() {
/* 580 */       return new DelegatedScope(this.next);
/*     */     }
/*     */     public Scope leave() {
/* 583 */       return this.next;
/*     */     }
/*     */     public void enter(Symbol paramSymbol) {
/*     */     }
/*     */ 
/*     */     public void enter(Symbol paramSymbol, Scope paramScope) {
/*     */     }
/*     */ 
/*     */     public void remove(Symbol paramSymbol) {
/* 592 */       throw new AssertionError(paramSymbol);
/*     */     }
/*     */     public Scope.Entry lookup(Name paramName) {
/* 595 */       return this.delegatee.lookup(paramName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     public Symbol sym;
/*     */     private Entry shadowed;
/*     */     public Entry sibling;
/*     */     public Scope scope;
/*     */ 
/*     */     public Entry(Symbol paramSymbol, Entry paramEntry1, Entry paramEntry2, Scope paramScope)
/*     */     {
/* 488 */       this.sym = paramSymbol;
/* 489 */       this.shadowed = paramEntry1;
/* 490 */       this.sibling = paramEntry2;
/* 491 */       this.scope = paramScope;
/*     */     }
/*     */ 
/*     */     public Entry next()
/*     */     {
/* 498 */       return this.shadowed;
/*     */     }
/*     */ 
/*     */     public Entry next(Filter<Symbol> paramFilter) {
/* 502 */       if ((this.shadowed.sym == null) || (paramFilter.accepts(this.shadowed.sym))) return this.shadowed;
/* 503 */       return this.shadowed.next(paramFilter);
/*     */     }
/*     */ 
/*     */     public boolean isStaticallyImported() {
/* 507 */       return false;
/*     */     }
/*     */ 
/*     */     public Scope getOrigin()
/*     */     {
/* 517 */       return this.scope;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ErrorScope extends Scope
/*     */   {
/*     */     ErrorScope(Scope paramScope, Symbol paramSymbol, Scope.Entry[] paramArrayOfEntry)
/*     */     {
/* 747 */       super(paramSymbol, paramArrayOfEntry, null);
/*     */     }
/*     */     public ErrorScope(Symbol paramSymbol) {
/* 750 */       super();
/*     */     }
/*     */     public Scope dup() {
/* 753 */       return new ErrorScope(this, this.owner, this.table);
/*     */     }
/*     */     public Scope dupUnshared() {
/* 756 */       return new ErrorScope(this, this.owner, (Scope.Entry[])this.table.clone());
/*     */     }
/*     */     public Scope.Entry lookup(Name paramName) {
/* 759 */       Scope.Entry localEntry = super.lookup(paramName);
/* 760 */       if (localEntry.scope == null) {
/* 761 */         return new Scope.Entry(this.owner, null, null, null);
/*     */       }
/* 763 */       return localEntry;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ImportScope extends Scope
/*     */   {
/*     */     public ImportScope(Symbol paramSymbol)
/*     */     {
/* 524 */       super();
/*     */     }
/*     */ 
/*     */     Scope.Entry makeEntry(Symbol paramSymbol, Scope.Entry paramEntry1, Scope.Entry paramEntry2, Scope paramScope1, final Scope paramScope2, final boolean paramBoolean)
/*     */     {
/* 530 */       return new Scope.Entry(paramSymbol, paramEntry1, paramEntry2, paramScope1)
/*     */       {
/*     */         public Scope getOrigin() {
/* 533 */           return paramScope2;
/*     */         }
/*     */ 
/*     */         public boolean isStaticallyImported()
/*     */         {
/* 538 */           return paramBoolean;
/*     */         } } ;
/*     */     }
/*     */   }
/*     */   public static abstract interface ScopeListener {
/*     */     public abstract void symbolAdded(Symbol paramSymbol, Scope paramScope);
/*     */ 
/*     */     public abstract void symbolRemoved(Symbol paramSymbol, Scope paramScope);
/*     */   }
/* 547 */   public static class StarImportScope extends Scope.ImportScope implements Scope.ScopeListener { public StarImportScope(Symbol paramSymbol) { super(); }
/*     */ 
/*     */     public void importAll(Scope paramScope)
/*     */     {
/* 551 */       for (Scope.Entry localEntry = paramScope.elems; localEntry != null; localEntry = localEntry.sibling) {
/* 552 */         if ((localEntry.sym.kind == 2) && (!includes(localEntry.sym))) {
/* 553 */           enter(localEntry.sym, paramScope);
/*     */         }
/*     */       }
/* 556 */       paramScope.addScopeListener(this);
/*     */     }
/*     */ 
/*     */     public void symbolRemoved(Symbol paramSymbol, Scope paramScope) {
/* 560 */       remove(paramSymbol);
/*     */     }
/*     */ 
/*     */     public void symbolAdded(Symbol paramSymbol, Scope paramScope)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Scope
 * JD-Core Version:    0.6.2
 */