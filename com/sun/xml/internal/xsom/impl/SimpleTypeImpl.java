/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public abstract class SimpleTypeImpl extends DeclarationImpl
/*     */   implements XSSimpleType, ContentTypeImpl, Ref.SimpleType
/*     */ {
/*     */   private Ref.SimpleType baseType;
/*  80 */   private short redefiningCount = 0;
/*     */ 
/*  82 */   private SimpleTypeImpl redefinedBy = null;
/*     */   private final Set<XSVariety> finalSet;
/*     */ 
/*     */   SimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _baseType)
/*     */   {
/*  59 */     super(_parent, _annon, _loc, _fa, _parent.getTargetNamespace(), _name, _anonymous);
/*     */ 
/*  61 */     this.baseType = _baseType;
/*  62 */     this.finalSet = finalSet;
/*     */   }
/*     */ 
/*     */   public XSType[] listSubstitutables()
/*     */   {
/*  68 */     return Util.listSubstitutables(this);
/*     */   }
/*     */ 
/*     */   public void redefine(SimpleTypeImpl st) {
/*  72 */     this.baseType = st;
/*  73 */     st.redefinedBy = this;
/*  74 */     this.redefiningCount = ((short)(st.redefiningCount + 1));
/*     */   }
/*     */ 
/*     */   public XSSimpleType getRedefinedBy()
/*     */   {
/*  85 */     return this.redefinedBy;
/*     */   }
/*     */ 
/*     */   public int getRedefinedCount() {
/*  89 */     int i = 0;
/*  90 */     for (SimpleTypeImpl st = this.redefinedBy; st != null; st = st.redefinedBy)
/*  91 */       i++;
/*  92 */     return i;
/*     */   }
/*     */   public XSType getBaseType() {
/*  95 */     return this.baseType.getType(); } 
/*  96 */   public XSSimpleType getSimpleBaseType() { return this.baseType.getType(); } 
/*  97 */   public boolean isPrimitive() { return false; }
/*     */ 
/*     */   public XSListSimpleType getBaseListType() {
/* 100 */     return getSimpleBaseType().getBaseListType();
/*     */   }
/*     */ 
/*     */   public XSUnionSimpleType getBaseUnionType() {
/* 104 */     return getSimpleBaseType().getBaseUnionType();
/*     */   }
/*     */ 
/*     */   public boolean isFinal(XSVariety v)
/*     */   {
/* 110 */     return this.finalSet.contains(v);
/*     */   }
/*     */ 
/*     */   public final int getDerivationMethod() {
/* 114 */     return 2;
/*     */   }
/*     */   public final XSSimpleType asSimpleType() {
/* 117 */     return this; } 
/* 118 */   public final XSComplexType asComplexType() { return null; }
/*     */ 
/*     */   public boolean isDerivedFrom(XSType t) {
/* 121 */     XSType x = this;
/*     */     while (true) {
/* 123 */       if (t == x)
/* 124 */         return true;
/* 125 */       XSType s = x.getBaseType();
/* 126 */       if (s == x)
/* 127 */         return false;
/* 128 */       x = s;
/*     */     }
/*     */   }
/*     */ 
/* 132 */   public final boolean isSimpleType() { return true; } 
/* 133 */   public final boolean isComplexType() { return false; } 
/* 134 */   public final XSParticle asParticle() { return null; } 
/* 135 */   public final XSContentType asEmpty() { return null; }
/*     */ 
/*     */   public boolean isRestriction() {
/* 138 */     return false; } 
/* 139 */   public boolean isList() { return false; } 
/* 140 */   public boolean isUnion() { return false; } 
/* 141 */   public XSRestrictionSimpleType asRestriction() { return null; } 
/* 142 */   public XSListSimpleType asList() { return null; } 
/* 143 */   public XSUnionSimpleType asUnion() { return null; }
/*     */ 
/*     */ 
/*     */   public final void visit(XSVisitor visitor)
/*     */   {
/* 149 */     visitor.simpleType(this);
/*     */   }
/*     */   public final void visit(XSContentTypeVisitor visitor) {
/* 152 */     visitor.simpleType(this);
/*     */   }
/*     */   public final Object apply(XSFunction function) {
/* 155 */     return function.simpleType(this);
/*     */   }
/*     */   public final Object apply(XSContentTypeFunction function) {
/* 158 */     return function.simpleType(this);
/*     */   }
/*     */ 
/*     */   public XSContentType getContentType() {
/* 162 */     return this;
/*     */   }
/* 164 */   public XSSimpleType getType() { return this; }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.SimpleTypeImpl
 * JD-Core Version:    0.6.2
 */