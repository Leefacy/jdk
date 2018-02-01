/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class Env<A>
/*     */   implements Iterable<Env<A>>
/*     */ {
/*     */   public Env<A> next;
/*     */   public Env<A> outer;
/*     */   public JCTree tree;
/*     */   public JCTree.JCCompilationUnit toplevel;
/*     */   public JCTree.JCClassDecl enclClass;
/*     */   public JCTree.JCMethodDecl enclMethod;
/*     */   public A info;
/*  76 */   public boolean baseClause = false;
/*     */ 
/*     */   public Env(JCTree paramJCTree, A paramA)
/*     */   {
/*  82 */     this.next = null;
/*  83 */     this.outer = null;
/*  84 */     this.tree = paramJCTree;
/*  85 */     this.toplevel = null;
/*  86 */     this.enclClass = null;
/*  87 */     this.enclMethod = null;
/*  88 */     this.info = paramA;
/*     */   }
/*     */ 
/*     */   public Env<A> dup(JCTree paramJCTree, A paramA)
/*     */   {
/*  95 */     return dupto(new Env(paramJCTree, paramA));
/*     */   }
/*     */ 
/*     */   public Env<A> dupto(Env<A> paramEnv)
/*     */   {
/* 102 */     paramEnv.next = this;
/* 103 */     paramEnv.outer = this.outer;
/* 104 */     paramEnv.toplevel = this.toplevel;
/* 105 */     paramEnv.enclClass = this.enclClass;
/* 106 */     paramEnv.enclMethod = this.enclMethod;
/* 107 */     return paramEnv;
/*     */   }
/*     */ 
/*     */   public Env<A> dup(JCTree paramJCTree)
/*     */   {
/* 114 */     return dup(paramJCTree, this.info);
/*     */   }
/*     */ 
/*     */   public Env<A> enclosing(JCTree.Tag paramTag)
/*     */   {
/* 120 */     Env localEnv = this;
/* 121 */     while ((localEnv != null) && (!localEnv.tree.hasTag(paramTag))) localEnv = localEnv.next;
/* 122 */     return localEnv;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     StringBuilder localStringBuilder = new StringBuilder();
/* 128 */     localStringBuilder.append("Env[").append(this.info);
/*     */ 
/* 133 */     if (this.outer != null)
/* 134 */       localStringBuilder.append(",outer=").append(this.outer);
/* 135 */     localStringBuilder.append("]");
/* 136 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public Iterator<Env<A>> iterator() {
/* 140 */     return new Iterator() {
/* 141 */       Env<A> next = Env.this;
/*     */ 
/* 143 */       public boolean hasNext() { return this.next.outer != null; }
/*     */ 
/*     */       public Env<A> next() {
/* 146 */         if (hasNext()) {
/* 147 */           Env localEnv = this.next;
/* 148 */           this.next = localEnv.outer;
/* 149 */           return localEnv;
/*     */         }
/* 151 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 155 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Env
 * JD-Core Version:    0.6.2
 */