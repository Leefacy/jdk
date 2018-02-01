/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ public class ConstructorDocImpl extends ExecutableMemberDocImpl
/*     */   implements ConstructorDoc
/*     */ {
/*     */   public ConstructorDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/*  54 */     super(paramDocEnv, paramMethodSymbol);
/*     */   }
/*     */ 
/*     */   public ConstructorDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath)
/*     */   {
/*  61 */     super(paramDocEnv, paramMethodSymbol, paramTreePath);
/*     */   }
/*     */ 
/*     */   public boolean isConstructor()
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/*  79 */     Symbol.ClassSymbol localClassSymbol = this.sym.enclClass();
/*  80 */     return localClassSymbol.name.toString();
/*     */   }
/*     */ 
/*     */   public String qualifiedName()
/*     */   {
/*  89 */     return this.sym.enclClass().getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return typeParametersString() + qualifiedName() + signature();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ConstructorDocImpl
 * JD-Core Version:    0.6.2
 */