/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import com.sun.xml.internal.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class ElementDecl extends DeclarationImpl
/*     */   implements XSElementDecl, Ref.Term
/*     */ {
/*     */   private XmlString defaultValue;
/*     */   private XmlString fixedValue;
/*     */   private boolean nillable;
/*     */   private boolean _abstract;
/*     */   private Ref.Type type;
/*     */   private Ref.Element substHead;
/*     */   private int substDisallowed;
/*     */   private int substExcluded;
/*     */   private final List<XSIdentityConstraint> idConstraints;
/*     */   private Boolean form;
/* 133 */   private Set<XSElementDecl> substitutables = null;
/*     */ 
/* 136 */   private Set<XSElementDecl> substitutablesView = null;
/*     */ 
/*     */   public ElementDecl(PatcherManager reader, SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa, String _tns, String _name, boolean _anonymous, XmlString _defv, XmlString _fixedv, boolean _nillable, boolean _abstract, Boolean _form, Ref.Type _type, Ref.Element _substHead, int _substDisallowed, int _substExcluded, List<IdentityConstraintImpl> idConstraints)
/*     */   {
/*  62 */     super(owner, _annon, _loc, fa, _tns, _name, _anonymous);
/*     */ 
/*  64 */     this.defaultValue = _defv;
/*  65 */     this.fixedValue = _fixedv;
/*  66 */     this.nillable = _nillable;
/*  67 */     this._abstract = _abstract;
/*  68 */     this.form = _form;
/*  69 */     this.type = _type;
/*  70 */     this.substHead = _substHead;
/*  71 */     this.substDisallowed = _substDisallowed;
/*  72 */     this.substExcluded = _substExcluded;
/*  73 */     this.idConstraints = Collections.unmodifiableList(idConstraints);
/*     */ 
/*  75 */     for (IdentityConstraintImpl idc : idConstraints) {
/*  76 */       idc.setParent(this);
/*     */     }
/*  78 */     if (this.type == null)
/*  79 */       throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public XmlString getDefaultValue() {
/*  83 */     return this.defaultValue;
/*     */   }
/*     */   public XmlString getFixedValue() {
/*  86 */     return this.fixedValue;
/*     */   }
/*     */   public boolean isNillable() {
/*  89 */     return this.nillable;
/*     */   }
/*     */   public boolean isAbstract() {
/*  92 */     return this._abstract;
/*     */   }
/*     */   public XSType getType() {
/*  95 */     return this.type.getType();
/*     */   }
/*     */ 
/*     */   public XSElementDecl getSubstAffiliation() {
/*  99 */     if (this.substHead == null) return null;
/* 100 */     return this.substHead.get();
/*     */   }
/*     */ 
/*     */   public boolean isSubstitutionDisallowed(int method)
/*     */   {
/* 105 */     return (this.substDisallowed & method) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isSubstitutionExcluded(int method)
/*     */   {
/* 110 */     return (this.substExcluded & method) != 0;
/*     */   }
/*     */ 
/*     */   public List<XSIdentityConstraint> getIdentityConstraints()
/*     */   {
/* 115 */     return this.idConstraints;
/*     */   }
/*     */ 
/*     */   public Boolean getForm()
/*     */   {
/* 120 */     return this.form;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public XSElementDecl[] listSubstitutables()
/*     */   {
/* 128 */     Set s = getSubstitutables();
/* 129 */     return (XSElementDecl[])s.toArray(new Ref.Term[s.size()]);
/*     */   }
/*     */ 
/*     */   public Set<? extends XSElementDecl> getSubstitutables()
/*     */   {
/* 139 */     if (this.substitutables == null)
/*     */     {
/* 142 */       this.substitutables = (this.substitutablesView = Collections.singleton(this));
/*     */     }
/* 144 */     return this.substitutablesView;
/*     */   }
/*     */ 
/*     */   protected void addSubstitutable(ElementDecl decl) {
/* 148 */     if (this.substitutables == null) {
/* 149 */       this.substitutables = new HashSet();
/* 150 */       this.substitutables.add(this);
/* 151 */       this.substitutablesView = Collections.unmodifiableSet(this.substitutables);
/*     */     }
/* 153 */     this.substitutables.add(decl);
/*     */   }
/*     */ 
/*     */   public void updateSubstitutabilityMap()
/*     */   {
/* 158 */     ElementDecl parent = this;
/* 159 */     XSType type = getType();
/*     */ 
/* 161 */     boolean rused = false;
/* 162 */     boolean eused = false;
/*     */ 
/* 164 */     while ((parent = (ElementDecl)parent.getSubstAffiliation()) != null)
/*     */     {
/* 166 */       if (!parent.isSubstitutionDisallowed(4))
/*     */       {
/* 169 */         boolean rd = parent.isSubstitutionDisallowed(2);
/* 170 */         boolean ed = parent.isSubstitutionDisallowed(1);
/*     */ 
/* 172 */         if (((!rd) || (!rused)) && ((!ed) || (!eused)))
/*     */         {
/* 174 */           XSType parentType = parent.getType();
/* 175 */           while (type != parentType) {
/* 176 */             if (type.getDerivationMethod() == 2) rused = true; else {
/* 177 */               eused = true;
/*     */             }
/* 179 */             type = type.getBaseType();
/* 180 */             if (type != null)
/*     */             {
/* 183 */               if (type.isComplexType()) {
/* 184 */                 rd |= type.asComplexType().isSubstitutionProhibited(2);
/* 185 */                 ed |= type.asComplexType().isSubstitutionProhibited(1);
/*     */               }
/* 187 */               if (getRoot().getAnyType().equals(type)) break;
/*     */             }
/*     */           }
/* 190 */           if (((!rd) || (!rused)) && ((!ed) || (!eused)))
/*     */           {
/* 193 */             parent.addSubstitutable(this); }  } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 198 */   public boolean canBeSubstitutedBy(XSElementDecl e) { return getSubstitutables().contains(e); }
/*     */ 
/*     */   public boolean isWildcard() {
/* 201 */     return false; } 
/* 202 */   public boolean isModelGroupDecl() { return false; } 
/* 203 */   public boolean isModelGroup() { return false; } 
/* 204 */   public boolean isElementDecl() { return true; } 
/*     */   public XSWildcard asWildcard() {
/* 206 */     return null; } 
/* 207 */   public XSModelGroupDecl asModelGroupDecl() { return null; } 
/* 208 */   public XSModelGroup asModelGroup() { return null; } 
/* 209 */   public XSElementDecl asElementDecl() { return this; }
/*     */ 
/*     */ 
/*     */   public void visit(XSVisitor visitor)
/*     */   {
/* 215 */     visitor.elementDecl(this);
/*     */   }
/*     */   public void visit(XSTermVisitor visitor) {
/* 218 */     visitor.elementDecl(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 221 */     return function.elementDecl(this);
/*     */   }
/*     */ 
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 225 */     return function.elementDecl(this, param);
/*     */   }
/*     */ 
/*     */   public Object apply(XSFunction function) {
/* 229 */     return function.elementDecl(this);
/*     */   }
/*     */ 
/*     */   public XSTerm getTerm()
/*     */   {
/* 234 */     return this;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.ElementDecl
 * JD-Core Version:    0.6.2
 */