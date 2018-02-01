/*     */ package com.sun.xml.internal.rngom.nc;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class NameClass
/*     */   implements ParsedNameClass, Serializable
/*     */ {
/*     */   static final int SPECIFICITY_NONE = -1;
/*     */   static final int SPECIFICITY_ANY_NAME = 0;
/*     */   static final int SPECIFICITY_NS_NAME = 1;
/*     */   static final int SPECIFICITY_NAME = 2;
/* 114 */   public static final NameClass ANY = new AnyNameClass();
/*     */ 
/* 131 */   public static final NameClass NULL = new NullNameClass();
/*     */ 
/*     */   public abstract boolean contains(QName paramQName);
/*     */ 
/*     */   public abstract int containsSpecificity(QName paramQName);
/*     */ 
/*     */   public abstract <V> V accept(NameClassVisitor<V> paramNameClassVisitor);
/*     */ 
/*     */   public abstract boolean isOpen();
/*     */ 
/*     */   public Set<QName> listNames()
/*     */   {
/*  93 */     final Set names = new HashSet();
/*  94 */     accept(new NameClassWalker()
/*     */     {
/*     */       public Void visitName(QName name) {
/*  97 */         names.add(name);
/*  98 */         return null;
/*     */       }
/*     */     });
/* 101 */     return names;
/*     */   }
/*     */ 
/*     */   public final boolean hasOverlapWith(NameClass nc2)
/*     */   {
/* 109 */     return OverlapDetector.overlap(this, nc2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.NameClass
 * JD-Core Version:    0.6.2
 */