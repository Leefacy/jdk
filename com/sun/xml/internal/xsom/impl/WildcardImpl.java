/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSWildcard.Any;
/*     */ import com.sun.xml.internal.xsom.XSWildcard.Other;
/*     */ import com.sun.xml.internal.xsom.XSWildcard.Union;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSWildcardFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSWildcardVisitor;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public abstract class WildcardImpl extends ComponentImpl
/*     */   implements XSWildcard, Ref.Term
/*     */ {
/*     */   private final int mode;
/*     */ 
/*     */   protected WildcardImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, int _mode)
/*     */   {
/*  52 */     super(owner, _annon, _loc, _fa);
/*  53 */     this.mode = _mode;
/*     */   }
/*     */ 
/*     */   public int getMode() {
/*  57 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public WildcardImpl union(SchemaDocumentImpl owner, WildcardImpl rhs) {
/*  61 */     if (((this instanceof Any)) || ((rhs instanceof Any))) {
/*  62 */       return new Any(owner, null, null, null, this.mode);
/*     */     }
/*  64 */     if (((this instanceof Finite)) && ((rhs instanceof Finite))) {
/*  65 */       Set values = new HashSet();
/*  66 */       values.addAll(((Finite)this).names);
/*  67 */       values.addAll(((Finite)rhs).names);
/*  68 */       return new Finite(owner, null, null, null, values, this.mode);
/*     */     }
/*     */ 
/*  71 */     if (((this instanceof Other)) && ((rhs instanceof Other))) {
/*  72 */       if (((Other)this).otherNamespace.equals(((Other)rhs).otherNamespace))
/*     */       {
/*  74 */         return new Other(owner, null, null, null, ((Other)this).otherNamespace, this.mode);
/*     */       }
/*     */ 
/*  77 */       return new Other(owner, null, null, null, "", this.mode);
/*     */     }
/*     */     Finite f;
/*     */     Other o;
/*     */     Finite f;
/*  83 */     if ((this instanceof Other)) {
/*  84 */       Other o = (Other)this; f = (Finite)rhs;
/*     */     } else {
/*  86 */       o = (Other)rhs; f = (Finite)this;
/*     */     }
/*     */ 
/*  89 */     if (f.names.contains(o.otherNamespace)) {
/*  90 */       return new Any(owner, null, null, null, this.mode);
/*     */     }
/*  92 */     return new Other(owner, null, null, null, o.otherNamespace, this.mode);
/*     */   }
/*     */ 
/*     */   public final void visit(XSVisitor visitor)
/*     */   {
/* 168 */     visitor.wildcard(this);
/*     */   }
/*     */   public final void visit(XSTermVisitor visitor) {
/* 171 */     visitor.wildcard(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 174 */     return function.wildcard(this);
/*     */   }
/*     */ 
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 178 */     return function.wildcard(this, param);
/*     */   }
/*     */ 
/*     */   public Object apply(XSFunction function) {
/* 182 */     return function.wildcard(this);
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/* 186 */     return true; } 
/* 187 */   public boolean isModelGroupDecl() { return false; } 
/* 188 */   public boolean isModelGroup() { return false; } 
/* 189 */   public boolean isElementDecl() { return false; } 
/*     */   public XSWildcard asWildcard() {
/* 191 */     return this; } 
/* 192 */   public XSModelGroupDecl asModelGroupDecl() { return null; } 
/* 193 */   public XSModelGroup asModelGroup() { return null; } 
/* 194 */   public XSElementDecl asElementDecl() { return null; }
/*     */ 
/*     */   public XSTerm getTerm()
/*     */   {
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */   public static final class Any extends WildcardImpl
/*     */     implements XSWildcard.Any
/*     */   {
/*     */     public Any(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, int _mode)
/*     */     {
/*  99 */       super(_annon, _loc, _fa, _mode);
/*     */     }
/*     */ 
/*     */     public boolean acceptsNamespace(String namespaceURI) {
/* 103 */       return true;
/*     */     }
/*     */     public void visit(XSWildcardVisitor visitor) {
/* 106 */       visitor.any(this);
/*     */     }
/*     */     public Object apply(XSWildcardFunction function) {
/* 109 */       return function.any(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Finite extends WildcardImpl
/*     */     implements XSWildcard.Union
/*     */   {
/*     */     private final Set<String> names;
/*     */     private final Set<String> namesView;
/*     */ 
/*     */     public Finite(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, Set<String> ns, int _mode)
/*     */     {
/* 139 */       super(_annon, _loc, _fa, _mode);
/* 140 */       this.names = ns;
/* 141 */       this.namesView = Collections.unmodifiableSet(this.names);
/*     */     }
/*     */ 
/*     */     public Iterator<String> iterateNamespaces()
/*     */     {
/* 148 */       return this.names.iterator();
/*     */     }
/*     */ 
/*     */     public Collection<String> getNamespaces() {
/* 152 */       return this.namesView;
/*     */     }
/*     */ 
/*     */     public boolean acceptsNamespace(String namespaceURI) {
/* 156 */       return this.names.contains(namespaceURI);
/*     */     }
/*     */ 
/*     */     public void visit(XSWildcardVisitor visitor) {
/* 160 */       visitor.union(this);
/*     */     }
/*     */     public Object apply(XSWildcardFunction function) {
/* 163 */       return function.union(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Other extends WildcardImpl
/*     */     implements XSWildcard.Other
/*     */   {
/*     */     private final String otherNamespace;
/*     */ 
/*     */     public Other(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String otherNamespace, int _mode)
/*     */     {
/* 116 */       super(_annon, _loc, _fa, _mode);
/* 117 */       this.otherNamespace = otherNamespace;
/*     */     }
/*     */ 
/*     */     public String getOtherNamespace()
/*     */     {
/* 122 */       return this.otherNamespace;
/*     */     }
/*     */     public boolean acceptsNamespace(String namespaceURI) {
/* 125 */       return !namespaceURI.equals(this.otherNamespace);
/*     */     }
/*     */ 
/*     */     public void visit(XSWildcardVisitor visitor) {
/* 129 */       visitor.other(this);
/*     */     }
/*     */     public Object apply(XSWildcardFunction function) {
/* 132 */       return function.other(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.WildcardImpl
 * JD-Core Version:    0.6.2
 */