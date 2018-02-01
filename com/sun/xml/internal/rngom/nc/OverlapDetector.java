/*     */ package com.sun.xml.internal.rngom.nc;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ class OverlapDetector
/*     */   implements NameClassVisitor<Void>
/*     */ {
/*     */   private NameClass nc1;
/*     */   private NameClass nc2;
/*  53 */   private boolean overlaps = false;
/*     */   static final String IMPOSSIBLE = "";
/*     */ 
/*     */   private OverlapDetector(NameClass nc1, NameClass nc2)
/*     */   {
/*  58 */     this.nc1 = nc1;
/*  59 */     this.nc2 = nc2;
/*  60 */     nc1.accept(this);
/*  61 */     nc2.accept(this);
/*     */   }
/*     */ 
/*     */   private void probe(QName name) {
/*  65 */     if ((this.nc1.contains(name)) && (this.nc2.contains(name)))
/*  66 */       this.overlaps = true;
/*     */   }
/*     */ 
/*     */   public Void visitChoice(NameClass nc1, NameClass nc2) {
/*  70 */     nc1.accept(this);
/*  71 */     nc2.accept(this);
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitNsName(String ns) {
/*  76 */     probe(new QName(ns, ""));
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitNsNameExcept(String ns, NameClass ex) {
/*  81 */     probe(new QName(ns, ""));
/*  82 */     ex.accept(this);
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitAnyName() {
/*  87 */     probe(new QName("", ""));
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitAnyNameExcept(NameClass ex) {
/*  92 */     probe(new QName("", ""));
/*  93 */     ex.accept(this);
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitName(QName name) {
/*  98 */     probe(name);
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitNull() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   static boolean overlap(NameClass nc1, NameClass nc2) {
/* 107 */     if ((nc2 instanceof SimpleNameClass)) {
/* 108 */       SimpleNameClass snc = (SimpleNameClass)nc2;
/* 109 */       return nc1.contains(snc.name);
/*     */     }
/* 111 */     if ((nc1 instanceof SimpleNameClass)) {
/* 112 */       SimpleNameClass snc = (SimpleNameClass)nc1;
/* 113 */       return nc2.contains(snc.name);
/*     */     }
/* 115 */     return new OverlapDetector(nc1, nc2).overlaps;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.OverlapDetector
 * JD-Core Version:    0.6.2
 */