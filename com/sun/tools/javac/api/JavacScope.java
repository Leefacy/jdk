/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.tree.Scope;
/*     */ import com.sun.tools.javac.code.Scope.StarImportScope;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ 
/*     */ public class JavacScope
/*     */   implements Scope
/*     */ {
/*     */   protected final Env<AttrContext> env;
/*     */ 
/*     */   JavacScope(Env<AttrContext> paramEnv)
/*     */   {
/*  53 */     paramEnv.getClass();
/*  54 */     this.env = paramEnv;
/*     */   }
/*     */ 
/*     */   public JavacScope getEnclosingScope() {
/*  58 */     if ((this.env.outer != null) && (this.env.outer != this.env)) {
/*  59 */       return new JavacScope(this.env.outer);
/*     */     }
/*     */ 
/*  62 */     return new JavacScope(this.env) {
/*     */       public boolean isStarImportScope() {
/*  64 */         return true;
/*     */       }
/*     */       public JavacScope getEnclosingScope() {
/*  67 */         return null;
/*     */       }
/*     */       public Iterable<? extends Element> getLocalElements() {
/*  70 */         return this.env.toplevel.starImportScope.getElements();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public TypeElement getEnclosingClass()
/*     */   {
/*  78 */     return (this.env.outer == null) || (this.env.outer == this.env) ? null : this.env.enclClass.sym;
/*     */   }
/*     */ 
/*     */   public ExecutableElement getEnclosingMethod() {
/*  82 */     return this.env.enclMethod == null ? null : this.env.enclMethod.sym;
/*     */   }
/*     */ 
/*     */   public Iterable<? extends Element> getLocalElements() {
/*  86 */     return ((AttrContext)this.env.info).getLocalElements();
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> getEnv() {
/*  90 */     return this.env;
/*     */   }
/*     */ 
/*     */   public boolean isStarImportScope() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  98 */     if ((paramObject instanceof JavacScope)) {
/*  99 */       JavacScope localJavacScope = (JavacScope)paramObject;
/*     */ 
/* 101 */       return (this.env.equals(localJavacScope.env)) && 
/* 101 */         (isStarImportScope() == localJavacScope.isStarImportScope());
/*     */     }
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 107 */     return this.env.hashCode() + (isStarImportScope() ? 1 : 0);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 111 */     return "JavacScope[env=" + this.env + ",starImport=" + isStarImportScope() + "]";
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.JavacScope
 * JD-Core Version:    0.6.2
 */