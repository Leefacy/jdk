/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup.Compositor;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class ModelGroupImpl extends ComponentImpl
/*     */   implements XSModelGroup, Ref.Term
/*     */ {
/*     */   private final ParticleImpl[] children;
/*     */   private final XSModelGroup.Compositor compositor;
/*     */ 
/*     */   public ModelGroupImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, XSModelGroup.Compositor _compositor, ParticleImpl[] _children)
/*     */   {
/*  50 */     super(owner, _annon, _loc, _fa);
/*  51 */     this.compositor = _compositor;
/*  52 */     this.children = _children;
/*     */ 
/*  54 */     if (this.compositor == null)
/*  55 */       throw new IllegalArgumentException();
/*  56 */     for (int i = this.children.length - 1; i >= 0; i--)
/*  57 */       if (this.children[i] == null)
/*  58 */         throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public ParticleImpl getChild(int idx) {
/*  62 */     return this.children[idx]; } 
/*  63 */   public int getSize() { return this.children.length; } 
/*     */   public ParticleImpl[] getChildren() {
/*  65 */     return this.children;
/*     */   }
/*     */ 
/*     */   public XSModelGroup.Compositor getCompositor() {
/*  69 */     return this.compositor;
/*     */   }
/*     */ 
/*     */   public void redefine(ModelGroupDeclImpl oldMG) {
/*  73 */     for (ParticleImpl p : this.children)
/*  74 */       p.redefine(oldMG);
/*     */   }
/*     */ 
/*     */   public Iterator<XSParticle> iterator() {
/*  78 */     return Arrays.asList((XSParticle[])this.children).iterator();
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/*  82 */     return false; } 
/*  83 */   public boolean isModelGroupDecl() { return false; } 
/*  84 */   public boolean isModelGroup() { return true; } 
/*  85 */   public boolean isElementDecl() { return false; } 
/*     */   public XSWildcard asWildcard() {
/*  87 */     return null; } 
/*  88 */   public XSModelGroupDecl asModelGroupDecl() { return null; } 
/*  89 */   public XSModelGroup asModelGroup() { return this; } 
/*  90 */   public XSElementDecl asElementDecl() { return null; }
/*     */ 
/*     */ 
/*     */   public void visit(XSVisitor visitor)
/*     */   {
/*  95 */     visitor.modelGroup(this);
/*     */   }
/*     */   public void visit(XSTermVisitor visitor) {
/*  98 */     visitor.modelGroup(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 101 */     return function.modelGroup(this);
/*     */   }
/*     */ 
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 105 */     return function.modelGroup(this, param);
/*     */   }
/*     */ 
/*     */   public Object apply(XSFunction function) {
/* 109 */     return function.modelGroup(this);
/*     */   }
/*     */ 
/*     */   public XSTerm getTerm() {
/* 113 */     return this;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.ModelGroupImpl
 * JD-Core Version:    0.6.2
 */