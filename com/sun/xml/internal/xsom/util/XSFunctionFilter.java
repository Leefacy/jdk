/*     */ package com.sun.xml.internal.xsom.util;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ 
/*     */ public class XSFunctionFilter<T>
/*     */   implements XSFunction<T>
/*     */ {
/*     */   protected XSFunction<T> core;
/*     */ 
/*     */   public XSFunctionFilter(XSFunction<T> _core)
/*     */   {
/*  64 */     this.core = _core;
/*     */   }
/*     */   public XSFunctionFilter() {
/*     */   }
/*     */ 
/*     */   public T annotation(XSAnnotation ann) {
/*  70 */     return this.core.annotation(ann);
/*     */   }
/*     */ 
/*     */   public T attGroupDecl(XSAttGroupDecl decl) {
/*  74 */     return this.core.attGroupDecl(decl);
/*     */   }
/*     */ 
/*     */   public T attributeDecl(XSAttributeDecl decl) {
/*  78 */     return this.core.attributeDecl(decl);
/*     */   }
/*     */ 
/*     */   public T attributeUse(XSAttributeUse use) {
/*  82 */     return this.core.attributeUse(use);
/*     */   }
/*     */ 
/*     */   public T complexType(XSComplexType type) {
/*  86 */     return this.core.complexType(type);
/*     */   }
/*     */ 
/*     */   public T schema(XSSchema schema) {
/*  90 */     return this.core.schema(schema);
/*     */   }
/*     */ 
/*     */   public T facet(XSFacet facet) {
/*  94 */     return this.core.facet(facet);
/*     */   }
/*     */ 
/*     */   public T notation(XSNotation notation) {
/*  98 */     return this.core.notation(notation);
/*     */   }
/*     */ 
/*     */   public T simpleType(XSSimpleType simpleType) {
/* 102 */     return this.core.simpleType(simpleType);
/*     */   }
/*     */ 
/*     */   public T particle(XSParticle particle) {
/* 106 */     return this.core.particle(particle);
/*     */   }
/*     */ 
/*     */   public T empty(XSContentType empty) {
/* 110 */     return this.core.empty(empty);
/*     */   }
/*     */ 
/*     */   public T wildcard(XSWildcard wc) {
/* 114 */     return this.core.wildcard(wc);
/*     */   }
/*     */ 
/*     */   public T modelGroupDecl(XSModelGroupDecl decl) {
/* 118 */     return this.core.modelGroupDecl(decl);
/*     */   }
/*     */ 
/*     */   public T modelGroup(XSModelGroup group) {
/* 122 */     return this.core.modelGroup(group);
/*     */   }
/*     */ 
/*     */   public T elementDecl(XSElementDecl decl) {
/* 126 */     return this.core.elementDecl(decl);
/*     */   }
/*     */ 
/*     */   public T identityConstraint(XSIdentityConstraint decl) {
/* 130 */     return this.core.identityConstraint(decl);
/*     */   }
/*     */ 
/*     */   public T xpath(XSXPath xpath) {
/* 134 */     return this.core.xpath(xpath);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.util.XSFunctionFilter
 * JD-Core Version:    0.6.2
 */