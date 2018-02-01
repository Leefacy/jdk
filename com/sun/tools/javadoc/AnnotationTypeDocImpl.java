/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ public class AnnotationTypeDocImpl extends ClassDocImpl
/*     */   implements AnnotationTypeDoc
/*     */ {
/*     */   public AnnotationTypeDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/*  52 */     this(paramDocEnv, paramClassSymbol, null);
/*     */   }
/*     */ 
/*     */   public AnnotationTypeDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, TreePath paramTreePath) {
/*  56 */     super(paramDocEnv, paramClassSymbol, paramTreePath);
/*     */   }
/*     */ 
/*     */   public boolean isAnnotationType()
/*     */   {
/*  64 */     return !isInterface();
/*     */   }
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/*  73 */     return this.env.legacyDoclet;
/*     */   }
/*     */ 
/*     */   public MethodDoc[] methods(boolean paramBoolean)
/*     */   {
/*  83 */     return this.env.legacyDoclet ? 
/*  83 */       (MethodDoc[])elements() : new MethodDoc[0];
/*     */   }
/*     */ 
/*     */   public AnnotationTypeElementDoc[] elements()
/*     */   {
/*  93 */     List localList = List.nil();
/*  94 */     for (Scope.Entry localEntry = this.tsym.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/*  95 */       if ((localEntry.sym != null) && (localEntry.sym.kind == 16)) {
/*  96 */         Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localEntry.sym;
/*  97 */         localList = localList.prepend(this.env.getAnnotationTypeElementDoc(localMethodSymbol));
/*     */       }
/*     */     }
/*     */ 
/* 101 */     return (AnnotationTypeElementDoc[])localList
/* 101 */       .toArray(new AnnotationTypeElementDoc[localList
/* 101 */       .length()]);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.AnnotationTypeDocImpl
 * JD-Core Version:    0.6.2
 */