/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class DGrammarPattern extends DPattern
/*     */   implements Iterable<DDefine>
/*     */ {
/*  58 */   private final Map<String, DDefine> patterns = new HashMap();
/*     */   DPattern start;
/*     */ 
/*     */   public DPattern getStart()
/*     */   {
/*  66 */     return this.start;
/*     */   }
/*     */ 
/*     */   public DDefine get(String name)
/*     */   {
/*  76 */     return (DDefine)this.patterns.get(name);
/*     */   }
/*     */ 
/*     */   DDefine getOrAdd(String name) {
/*  80 */     if (this.patterns.containsKey(name)) {
/*  81 */       return get(name);
/*     */     }
/*  83 */     DDefine d = new DDefine(name);
/*  84 */     this.patterns.put(name, d);
/*  85 */     return d;
/*     */   }
/*     */ 
/*     */   public Iterator<DDefine> iterator()
/*     */   {
/*  93 */     return this.patterns.values().iterator();
/*     */   }
/*     */ 
/*     */   public boolean isNullable() {
/*  97 */     return this.start.isNullable();
/*     */   }
/*     */ 
/*     */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 101 */     return visitor.onGrammar(this);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DGrammarPattern
 * JD-Core Version:    0.6.2
 */