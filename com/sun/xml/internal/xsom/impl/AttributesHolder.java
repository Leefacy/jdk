/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators.Adapter;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public abstract class AttributesHolder extends DeclarationImpl
/*     */ {
/*  62 */   protected final Map<UName, AttributeUseImpl> attributes = new LinkedHashMap();
/*     */ 
/*  67 */   protected final Set<UName> prohibitedAtts = new HashSet();
/*     */ 
/* 104 */   protected final Set<Ref.AttGroup> attGroups = new HashSet();
/*     */ 
/*     */   protected AttributesHolder(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous)
/*     */   {
/*  51 */     super(_parent, _annon, loc, _fa, _parent.getTargetNamespace(), _name, _anonymous);
/*     */   }
/*     */ 
/*     */   public abstract void setWildcard(WildcardImpl paramWildcardImpl);
/*     */ 
/*     */   public void addAttributeUse(UName name, AttributeUseImpl a)
/*     */   {
/*  64 */     this.attributes.put(name, a);
/*     */   }
/*     */ 
/*     */   public void addProhibitedAttribute(UName name)
/*     */   {
/*  69 */     this.prohibitedAtts.add(name);
/*     */   }
/*     */ 
/*     */   public Collection<XSAttributeUse> getAttributeUses()
/*     */   {
/*  78 */     List v = new ArrayList();
/*  79 */     v.addAll(this.attributes.values());
/*  80 */     for (XSAttGroupDecl agd : getAttGroups())
/*  81 */       v.addAll(agd.getAttributeUses());
/*  82 */     return v;
/*     */   }
/*     */   public Iterator<XSAttributeUse> iterateAttributeUses() {
/*  85 */     return getAttributeUses().iterator();
/*     */   }
/*     */ 
/*     */   public XSAttributeUse getDeclaredAttributeUse(String nsURI, String localName)
/*     */   {
/*  91 */     return (XSAttributeUse)this.attributes.get(new UName(nsURI, localName));
/*     */   }
/*     */ 
/*     */   public Iterator<AttributeUseImpl> iterateDeclaredAttributeUses() {
/*  95 */     return this.attributes.values().iterator();
/*     */   }
/*     */ 
/*     */   public Collection<AttributeUseImpl> getDeclaredAttributeUses() {
/*  99 */     return this.attributes.values();
/*     */   }
/*     */ 
/*     */   public void addAttGroup(Ref.AttGroup a)
/*     */   {
/* 106 */     this.attGroups.add(a);
/*     */   }
/*     */ 
/*     */   public Iterator<XSAttGroupDecl> iterateAttGroups()
/*     */   {
/* 111 */     return new Iterators.Adapter(this.attGroups.iterator()) {
/*     */       protected XSAttGroupDecl filter(Ref.AttGroup u) {
/* 113 */         return u.get();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Set<XSAttGroupDecl> getAttGroups() {
/* 119 */     return new AbstractSet() {
/*     */       public Iterator<XSAttGroupDecl> iterator() {
/* 121 */         return AttributesHolder.this.iterateAttGroups();
/*     */       }
/*     */ 
/*     */       public int size() {
/* 125 */         return AttributesHolder.this.attGroups.size();
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.AttributesHolder
 * JD-Core Version:    0.6.2
 */