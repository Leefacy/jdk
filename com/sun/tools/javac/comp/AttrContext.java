/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ public class AttrContext
/*     */ {
/*  43 */   Scope scope = null;
/*     */ 
/*  47 */   int staticLevel = 0;
/*     */ 
/*  51 */   boolean isSelfCall = false;
/*     */ 
/*  55 */   boolean selectSuper = false;
/*     */ 
/*  59 */   boolean isSerializable = false;
/*     */ 
/*  63 */   Resolve.MethodResolutionPhase pendingResolutionPhase = null;
/*     */   Lint lint;
/*  72 */   Symbol enclVar = null;
/*     */ 
/*  77 */   Attr.ResultInfo returnResult = null;
/*     */ 
/*  81 */   Type defaultSuperCallSite = null;
/*     */ 
/*     */   AttrContext dup(Scope paramScope)
/*     */   {
/*  86 */     AttrContext localAttrContext = new AttrContext();
/*  87 */     localAttrContext.scope = paramScope;
/*  88 */     localAttrContext.staticLevel = this.staticLevel;
/*  89 */     localAttrContext.isSelfCall = this.isSelfCall;
/*  90 */     localAttrContext.selectSuper = this.selectSuper;
/*  91 */     localAttrContext.pendingResolutionPhase = this.pendingResolutionPhase;
/*  92 */     localAttrContext.lint = this.lint;
/*  93 */     localAttrContext.enclVar = this.enclVar;
/*  94 */     localAttrContext.returnResult = this.returnResult;
/*  95 */     localAttrContext.defaultSuperCallSite = this.defaultSuperCallSite;
/*  96 */     localAttrContext.isSerializable = this.isSerializable;
/*  97 */     return localAttrContext;
/*     */   }
/*     */ 
/*     */   AttrContext dup()
/*     */   {
/* 103 */     return dup(this.scope);
/*     */   }
/*     */ 
/*     */   public Iterable<Symbol> getLocalElements() {
/* 107 */     if (this.scope == null)
/* 108 */       return List.nil();
/* 109 */     return this.scope.getElements();
/*     */   }
/*     */ 
/*     */   boolean lastResolveVarargs()
/*     */   {
/* 114 */     return (this.pendingResolutionPhase != null) && 
/* 114 */       (this.pendingResolutionPhase
/* 114 */       .isVarargsRequired());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 119 */     return "AttrContext[" + this.scope.toString() + "]";
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.AttrContext
 * JD-Core Version:    0.6.2
 */