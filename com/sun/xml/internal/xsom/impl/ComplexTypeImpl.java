/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators.Filter;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators.Union;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class ComplexTypeImpl extends AttributesHolder
/*     */   implements XSComplexType, Ref.ComplexType
/*     */ {
/*     */   private int derivationMethod;
/*     */   private Ref.Type baseType;
/* 114 */   private short redefiningCount = 0;
/*     */ 
/* 116 */   private ComplexTypeImpl redefinedBy = null;
/*     */   private XSElementDecl scope;
/*     */   private final boolean _abstract;
/*     */   private WildcardImpl localAttWildcard;
/*     */   private final int finalValue;
/*     */   private final int blockValue;
/*     */   private Ref.ContentType contentType;
/*     */   private XSContentType explicitContent;
/*     */   private final boolean mixed;
/*     */ 
/*     */   public ComplexTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, boolean _abstract, int _derivationMethod, Ref.Type _base, int _final, int _block, boolean _mixed)
/*     */   {
/*  62 */     super(_parent, _annon, _loc, _fa, _name, _anonymous);
/*     */ 
/*  64 */     if (_base == null) {
/*  65 */       throw new IllegalArgumentException();
/*     */     }
/*  67 */     this._abstract = _abstract;
/*  68 */     this.derivationMethod = _derivationMethod;
/*  69 */     this.baseType = _base;
/*  70 */     this.finalValue = _final;
/*  71 */     this.blockValue = _block;
/*  72 */     this.mixed = _mixed;
/*     */   }
/*     */   public XSComplexType asComplexType() {
/*  75 */     return this;
/*     */   }
/*     */   public boolean isDerivedFrom(XSType t) {
/*  78 */     XSType x = this;
/*     */     while (true) {
/*  80 */       if (t == x)
/*  81 */         return true;
/*  82 */       XSType s = x.getBaseType();
/*  83 */       if (s == x)
/*  84 */         return false;
/*  85 */       x = s;
/*     */     }
/*     */   }
/*     */ 
/*  89 */   public XSSimpleType asSimpleType() { return null; } 
/*  90 */   public final boolean isSimpleType() { return false; } 
/*  91 */   public final boolean isComplexType() { return true; }
/*     */ 
/*     */   public int getDerivationMethod() {
/*  94 */     return this.derivationMethod;
/*     */   }
/*     */   public XSType getBaseType() {
/*  97 */     return this.baseType.getType();
/*     */   }
/*     */ 
/*     */   public void redefine(ComplexTypeImpl ct)
/*     */   {
/* 103 */     if ((this.baseType instanceof DelayedRef))
/* 104 */       ((DelayedRef)this.baseType).redefine(ct);
/*     */     else
/* 106 */       this.baseType = ct;
/* 107 */     ct.redefinedBy = this;
/* 108 */     this.redefiningCount = ((short)(ct.redefiningCount + 1));
/*     */   }
/*     */ 
/*     */   public XSComplexType getRedefinedBy()
/*     */   {
/* 119 */     return this.redefinedBy;
/*     */   }
/*     */ 
/*     */   public int getRedefinedCount() {
/* 123 */     int i = 0;
/* 124 */     for (ComplexTypeImpl ct = this.redefinedBy; ct != null; ct = ct.redefinedBy)
/* 125 */       i++;
/* 126 */     return i;
/*     */   }
/*     */ 
/*     */   public XSElementDecl getScope()
/*     */   {
/* 131 */     return this.scope; } 
/* 132 */   public void setScope(XSElementDecl _scope) { this.scope = _scope; }
/*     */ 
/*     */   public boolean isAbstract() {
/* 135 */     return this._abstract;
/*     */   }
/*     */ 
/*     */   public void setWildcard(WildcardImpl wc)
/*     */   {
/* 142 */     this.localAttWildcard = wc;
/*     */   }
/*     */   public XSWildcard getAttributeWildcard() {
/* 145 */     WildcardImpl complete = this.localAttWildcard;
/*     */ 
/* 147 */     Iterator itr = iterateAttGroups();
/* 148 */     while (itr.hasNext()) {
/* 149 */       WildcardImpl w = (WildcardImpl)((XSAttGroupDecl)itr.next()).getAttributeWildcard();
/*     */ 
/* 151 */       if (w != null)
/*     */       {
/* 153 */         if (complete == null) {
/* 154 */           complete = w;
/*     */         }
/*     */         else
/*     */         {
/* 158 */           complete = complete.union(this.ownerDocument, w);
/*     */         }
/*     */       }
/*     */     }
/* 161 */     if (getDerivationMethod() == 2) return complete;
/*     */ 
/* 163 */     WildcardImpl base = null;
/* 164 */     XSType baseType = getBaseType();
/* 165 */     if (baseType.asComplexType() != null) {
/* 166 */       base = (WildcardImpl)baseType.asComplexType().getAttributeWildcard();
/*     */     }
/* 168 */     if (complete == null) return base;
/* 169 */     if (base == null) return complete;
/*     */ 
/* 171 */     return complete.union(this.ownerDocument, base);
/*     */   }
/*     */ 
/*     */   public boolean isFinal(int derivationMethod)
/*     */   {
/* 176 */     return (this.finalValue & derivationMethod) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isSubstitutionProhibited(int method)
/*     */   {
/* 181 */     return (this.blockValue & method) != 0;
/*     */   }
/*     */ 
/*     */   public void setContentType(Ref.ContentType v)
/*     */   {
/* 186 */     this.contentType = v; } 
/* 187 */   public XSContentType getContentType() { return this.contentType.getContentType(); }
/*     */ 
/*     */   public void setExplicitContent(XSContentType v)
/*     */   {
/* 191 */     this.explicitContent = v;
/*     */   }
/* 193 */   public XSContentType getExplicitContent() { return this.explicitContent; }
/*     */ 
/*     */   public boolean isMixed() {
/* 196 */     return this.mixed;
/*     */   }
/*     */ 
/*     */   public XSAttributeUse getAttributeUse(String nsURI, String localName)
/*     */   {
/* 202 */     UName name = new UName(nsURI, localName);
/*     */ 
/* 204 */     if (this.prohibitedAtts.contains(name)) return null;
/*     */ 
/* 206 */     XSAttributeUse o = (XSAttributeUse)this.attributes.get(name);
/*     */ 
/* 209 */     if (o == null) {
/* 210 */       Iterator itr = iterateAttGroups();
/* 211 */       while ((itr.hasNext()) && (o == null)) {
/* 212 */         o = ((XSAttGroupDecl)itr.next()).getAttributeUse(nsURI, localName);
/*     */       }
/*     */     }
/* 215 */     if (o == null) {
/* 216 */       XSType base = getBaseType();
/* 217 */       if (base.asComplexType() != null) {
/* 218 */         o = base.asComplexType().getAttributeUse(nsURI, localName);
/*     */       }
/*     */     }
/* 221 */     return o;
/*     */   }
/*     */ 
/*     */   public Iterator<XSAttributeUse> iterateAttributeUses()
/*     */   {
/* 226 */     XSComplexType baseType = getBaseType().asComplexType();
/*     */ 
/* 228 */     if (baseType == null) return super.iterateAttributeUses();
/*     */ 
/* 238 */     return new Iterators.Union(new Iterators.Filter(baseType
/* 231 */       .iterateAttributeUses()) {
/*     */       protected boolean matches(XSAttributeUse value) {
/* 233 */         XSAttributeDecl u = value.getDecl();
/* 234 */         UName n = new UName(u.getTargetNamespace(), u.getName());
/* 235 */         return !ComplexTypeImpl.this.prohibitedAtts.contains(n);
/*     */       }
/*     */     }
/*     */     , super
/* 238 */       .iterateAttributeUses());
/*     */   }
/*     */ 
/*     */   public Collection<XSAttributeUse> getAttributeUses() {
/* 242 */     XSComplexType baseType = getBaseType().asComplexType();
/*     */ 
/* 244 */     if (baseType == null) return super.getAttributeUses();
/*     */ 
/* 247 */     Map uses = new HashMap();
/* 248 */     for (XSAttributeUse a : baseType.getAttributeUses()) {
/* 249 */       uses.put(new UName(a.getDecl()), a);
/*     */     }
/* 251 */     uses.keySet().removeAll(this.prohibitedAtts);
/*     */ 
/* 253 */     for (XSAttributeUse a : super.getAttributeUses()) {
/* 254 */       uses.put(new UName(a.getDecl()), a);
/*     */     }
/* 256 */     return uses.values();
/*     */   }
/*     */ 
/*     */   public XSType[] listSubstitutables()
/*     */   {
/* 261 */     return Util.listSubstitutables(this);
/*     */   }
/*     */ 
/*     */   public void visit(XSVisitor visitor) {
/* 265 */     visitor.complexType(this);
/*     */   }
/*     */   public <T> T apply(XSFunction<T> function) {
/* 268 */     return function.complexType(this);
/*     */   }
/*     */ 
/*     */   public XSComplexType getType() {
/* 272 */     return this;
/*     */   }
/*     */   public List<XSComplexType> getSubtypes() {
/* 275 */     ArrayList subtypeList = new ArrayList();
/* 276 */     Iterator cTypes = getRoot().iterateComplexTypes();
/* 277 */     while (cTypes.hasNext()) {
/* 278 */       XSComplexType cType = (Ref.ComplexType)cTypes.next();
/* 279 */       XSType base = cType.getBaseType();
/* 280 */       if ((base != null) && (base.equals(this))) {
/* 281 */         subtypeList.add(cType);
/*     */       }
/*     */     }
/* 284 */     return subtypeList;
/*     */   }
/*     */ 
/*     */   public List<XSElementDecl> getElementDecls() {
/* 288 */     ArrayList declList = new ArrayList();
/* 289 */     XSSchemaSet schemaSet = getRoot();
/* 290 */     for (XSSchema sch : schemaSet.getSchemas()) {
/* 291 */       for (XSElementDecl decl : sch.getElementDecls().values()) {
/* 292 */         if (decl.getType().equals(this)) {
/* 293 */           declList.add(decl);
/*     */         }
/*     */       }
/*     */     }
/* 297 */     return declList;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.ComplexTypeImpl
 * JD-Core Version:    0.6.2
 */