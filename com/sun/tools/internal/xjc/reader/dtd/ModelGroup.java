/*     */ package com.sun.tools.internal.xjc.reader.dtd;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ final class ModelGroup extends Term
/*     */ {
/*     */   Kind kind;
/*  44 */   private final List<Term> terms = new ArrayList();
/*     */ 
/*     */   void normalize(List<Block> r, boolean optional) {
/*  47 */     switch (1.$SwitchMap$com$sun$tools$internal$xjc$reader$dtd$ModelGroup$Kind[this.kind.ordinal()]) {
/*     */     case 1:
/*  49 */       for (Term t : this.terms)
/*  50 */         t.normalize(r, optional);
/*  51 */       return;
/*     */     case 2:
/*  53 */       Block b = new Block((isOptional()) || (optional), isRepeated());
/*  54 */       addAllElements(b);
/*  55 */       r.add(b);
/*  56 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   void addAllElements(Block b) {
/*  61 */     for (Term t : this.terms)
/*  62 */       t.addAllElements(b);
/*     */   }
/*     */ 
/*     */   boolean isOptional() {
/*  66 */     switch (1.$SwitchMap$com$sun$tools$internal$xjc$reader$dtd$ModelGroup$Kind[this.kind.ordinal()]) {
/*     */     case 1:
/*  68 */       for (Term t : this.terms)
/*  69 */         if (!t.isOptional())
/*  70 */           return false;
/*  71 */       return true;
/*     */     case 2:
/*  73 */       for (Term t : this.terms)
/*  74 */         if (t.isOptional())
/*  75 */           return true;
/*  76 */       return false;
/*     */     }
/*  78 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   boolean isRepeated()
/*     */   {
/*  83 */     switch (1.$SwitchMap$com$sun$tools$internal$xjc$reader$dtd$ModelGroup$Kind[this.kind.ordinal()]) {
/*     */     case 1:
/*  85 */       return true;
/*     */     case 2:
/*  87 */       for (Term t : this.terms)
/*  88 */         if (t.isRepeated())
/*  89 */           return true;
/*  90 */       return false;
/*     */     }
/*  92 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   void setKind(short connectorType)
/*     */   {
/*     */     Kind k;
/*     */     Kind k;
/*  98 */     switch (connectorType) {
/*     */     case 1:
/* 100 */       k = Kind.SEQUENCE;
/* 101 */       break;
/*     */     case 0:
/* 103 */       k = Kind.CHOICE;
/* 104 */       break;
/*     */     default:
/* 106 */       throw new IllegalArgumentException();
/*     */     }
/*     */     Kind k;
/* 109 */     assert ((this.kind == null) || (k == this.kind));
/* 110 */     this.kind = k;
/*     */   }
/*     */ 
/*     */   void addTerm(Term t) {
/* 114 */     if ((t instanceof ModelGroup)) {
/* 115 */       ModelGroup mg = (ModelGroup)t;
/* 116 */       if (mg.kind == this.kind) {
/* 117 */         this.terms.addAll(mg.terms);
/* 118 */         return;
/*     */       }
/*     */     }
/* 121 */     this.terms.add(t);
/*     */   }
/*     */ 
/*     */   Term wrapUp()
/*     */   {
/* 126 */     switch (this.terms.size()) {
/*     */     case 0:
/* 128 */       return EMPTY;
/*     */     case 1:
/* 130 */       assert (this.kind == null);
/* 131 */       return (Term)this.terms.get(0);
/*     */     }
/* 133 */     assert (this.kind != null);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   static enum Kind
/*     */   {
/*  39 */     CHOICE, SEQUENCE;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.ModelGroup
 * JD-Core Version:    0.6.2
 */