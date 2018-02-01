/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup.Compositor;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ final class DefaultParticleBinder extends ParticleBinder
/*     */ {
/*     */   public void build(XSParticle p, Collection<XSParticle> forcedProps)
/*     */   {
/*  58 */     Checker checker = checkCollision(p, forcedProps);
/*     */ 
/*  60 */     if (checker.hasNameCollision())
/*     */     {
/*  65 */       CReferencePropertyInfo prop = new CReferencePropertyInfo(
/*  62 */         getCurrentBean().getBaseClass() == null ? "Content" : "Rest", true, false, false, p, this.builder
/*  64 */         .getBindInfo(p)
/*  64 */         .toCustomizationList(), p
/*  65 */         .getLocator(), false, false, false);
/*  66 */       RawTypeSetBuilder.build(p, false).addTo(prop);
/*  67 */       prop.javadoc = Messages.format("DefaultParticleBinder.FallbackJavadoc", new Object[] { checker
/*  68 */         .getCollisionInfo().toString() });
/*     */ 
/*  70 */       getCurrentBean().addProperty(prop);
/*     */     } else {
/*  72 */       new Builder(checker.markedParticles).particle(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean checkFallback(XSParticle p)
/*     */   {
/*  78 */     return checkCollision(p, Collections.emptyList()).hasNameCollision();
/*     */   }
/*     */ 
/*     */   private Checker checkCollision(XSParticle p, Collection<XSParticle> forcedProps)
/*     */   {
/*  83 */     Checker checker = new Checker(forcedProps);
/*     */ 
/*  85 */     CClassInfo superClass = getCurrentBean().getBaseClass();
/*     */ 
/*  87 */     if (superClass != null)
/*  88 */       checker.readSuperClass(superClass);
/*  89 */     checker.particle(p);
/*     */ 
/*  91 */     return checker;
/*     */   }
/*     */ 
/*     */   private final class Builder
/*     */     implements XSTermVisitor
/*     */   {
/*     */     private final Map<XSParticle, String> markedParticles;
/*     */     private boolean insideOptionalParticle;
/*     */ 
/*     */     Builder()
/*     */     {
/* 323 */       this.markedParticles = markedParticles;
/*     */     }
/*     */ 
/*     */     private boolean marked(XSParticle p)
/*     */     {
/* 341 */       return this.markedParticles.containsKey(p);
/*     */     }
/*     */ 
/*     */     private String getLabel(XSParticle p) {
/* 345 */       return (String)this.markedParticles.get(p);
/*     */     }
/*     */ 
/*     */     public void particle(XSParticle p) {
/* 349 */       XSTerm t = p.getTerm();
/*     */ 
/* 351 */       if (marked(p)) {
/* 352 */         BIProperty cust = BIProperty.getCustomization(p);
/* 353 */         CPropertyInfo prop = cust.createElementOrReferenceProperty(
/* 354 */           getLabel(p), 
/* 354 */           false, p, RawTypeSetBuilder.build(p, this.insideOptionalParticle));
/* 355 */         DefaultParticleBinder.this.getCurrentBean().addProperty(prop);
/*     */       }
/*     */       else {
/* 358 */         assert (!p.isRepeated());
/*     */ 
/* 360 */         boolean oldIOP = this.insideOptionalParticle;
/* 361 */         this.insideOptionalParticle |= BigInteger.ZERO.equals(p.getMinOccurs());
/*     */ 
/* 363 */         t.visit(this);
/* 364 */         this.insideOptionalParticle = oldIOP;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void elementDecl(XSElementDecl e)
/*     */     {
/* 370 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/*     */ 
/*     */     public void wildcard(XSWildcard wc)
/*     */     {
/* 375 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/*     */ 
/*     */     public void modelGroupDecl(XSModelGroupDecl decl)
/*     */     {
/* 379 */       modelGroup(decl.getModelGroup());
/*     */     }
/*     */ 
/*     */     public void modelGroup(XSModelGroup mg) {
/* 383 */       boolean oldIOP = this.insideOptionalParticle;
/* 384 */       this.insideOptionalParticle |= mg.getCompositor() == XSModelGroup.CHOICE;
/*     */ 
/* 386 */       for (XSParticle p : mg.getChildren()) {
/* 387 */         particle(p);
/*     */       }
/* 389 */       this.insideOptionalParticle = oldIOP;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class Checker
/*     */     implements XSTermVisitor
/*     */   {
/* 124 */     private CollisionInfo collisionInfo = null;
/*     */ 
/* 127 */     private final NameCollisionChecker cchecker = new NameCollisionChecker(null);
/*     */     private final Collection<XSParticle> forcedProps;
/*     */     private XSParticle outerParticle;
/* 220 */     public final Map<XSParticle, String> markedParticles = new HashMap();
/*     */ 
/* 293 */     private final Map<XSParticle, String> labelCache = new Hashtable();
/*     */ 
/*     */     Checker()
/*     */     {
/* 110 */       this.forcedProps = forcedProps;
/*     */     }
/*     */ 
/*     */     boolean hasNameCollision() {
/* 114 */       return this.collisionInfo != null;
/*     */     }
/*     */ 
/*     */     CollisionInfo getCollisionInfo() {
/* 118 */       return this.collisionInfo;
/*     */     }
/*     */ 
/*     */     public void particle(XSParticle p)
/*     */     {
/* 136 */       if ((DefaultParticleBinder.this.getLocalPropCustomization(p) != null) || 
/* 137 */         (DefaultParticleBinder.this.builder
/* 137 */         .getLocalDomCustomization(p) != null))
/*     */       {
/* 140 */         check(p);
/* 141 */         mark(p);
/* 142 */         return;
/*     */       }
/*     */ 
/* 145 */       XSTerm t = p.getTerm();
/*     */ 
/* 147 */       if ((p.isRepeated()) && ((t.isModelGroup()) || (t.isModelGroupDecl())))
/*     */       {
/* 149 */         mark(p);
/* 150 */         return;
/*     */       }
/*     */ 
/* 153 */       if (this.forcedProps.contains(p))
/*     */       {
/* 155 */         mark(p);
/* 156 */         return;
/*     */       }
/*     */ 
/* 159 */       this.outerParticle = p;
/* 160 */       t.visit(this);
/*     */     }
/*     */ 
/*     */     public void elementDecl(XSElementDecl decl)
/*     */     {
/* 170 */       check(this.outerParticle);
/* 171 */       mark(this.outerParticle);
/*     */     }
/*     */ 
/*     */     public void modelGroup(XSModelGroup mg)
/*     */     {
/* 176 */       if ((mg.getCompositor() == XSModelGroup.Compositor.CHOICE) && (DefaultParticleBinder.this.builder.getGlobalBinding().isChoiceContentPropertyEnabled())) {
/* 177 */         mark(this.outerParticle);
/* 178 */         return;
/*     */       }
/*     */ 
/* 181 */       for (XSParticle child : mg.getChildren())
/* 182 */         particle(child);
/*     */     }
/*     */ 
/*     */     public void modelGroupDecl(XSModelGroupDecl decl) {
/* 186 */       modelGroup(decl.getModelGroup());
/*     */     }
/*     */ 
/*     */     public void wildcard(XSWildcard wc) {
/* 190 */       mark(this.outerParticle);
/*     */     }
/*     */ 
/*     */     void readSuperClass(CClassInfo ci) {
/* 194 */       this.cchecker.readSuperClass(ci);
/*     */     }
/*     */ 
/*     */     private void check(XSParticle p)
/*     */     {
/* 204 */       if (this.collisionInfo == null)
/* 205 */         this.collisionInfo = this.cchecker.check(p);
/*     */     }
/*     */ 
/*     */     private void mark(XSParticle p)
/*     */     {
/* 212 */       this.markedParticles.put(p, computeLabel(p));
/*     */     }
/*     */ 
/*     */     private String computeLabel(XSParticle p)
/*     */     {
/* 300 */       String label = (String)this.labelCache.get(p);
/* 301 */       if (label == null)
/* 302 */         this.labelCache.put(p, label = DefaultParticleBinder.this.computeLabel(p));
/* 303 */       return label;
/*     */     }
/*     */ 
/*     */     private final class NameCollisionChecker
/*     */     {
/* 260 */       private final List<XSParticle> particles = new ArrayList();
/*     */ 
/* 265 */       private final Map<String, CPropertyInfo> occupiedLabels = new HashMap();
/*     */ 
/*     */       private NameCollisionChecker()
/*     */       {
/*     */       }
/*     */ 
/*     */       CollisionInfo check(XSParticle p)
/*     */       {
/* 242 */         String label = DefaultParticleBinder.Checker.this.computeLabel(p);
/* 243 */         if (this.occupiedLabels.containsKey(label))
/*     */         {
/* 246 */           return new CollisionInfo(label, p.getLocator(), 
/* 246 */             ((CPropertyInfo)this.occupiedLabels
/* 246 */             .get(label)).locator);
/*     */         }
/*     */ 
/* 249 */         for (XSParticle jp : this.particles) {
/* 250 */           if (!check(p, jp))
/*     */           {
/* 252 */             return new CollisionInfo(label, p.getLocator(), jp.getLocator());
/*     */           }
/*     */         }
/* 255 */         this.particles.add(p);
/* 256 */         return null;
/*     */       }
/*     */ 
/*     */       private boolean check(XSParticle p1, XSParticle p2)
/*     */       {
/* 273 */         return !DefaultParticleBinder.Checker.this.computeLabel(p1).equals(DefaultParticleBinder.Checker.this.computeLabel(p2));
/*     */       }
/*     */ 
/*     */       void readSuperClass(CClassInfo base)
/*     */       {
/* 281 */         for (; base != null; base = base.getBaseClass())
/* 282 */           for (CPropertyInfo p : base.getProperties())
/* 283 */             this.occupiedLabels.put(p.getName(true), p);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.DefaultParticleBinder
 * JD-Core Version:    0.6.2
 */