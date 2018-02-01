/*     */ package com.sun.tools.internal.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo.CollectionMode;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet.Mode;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.rngom.digested.DAttributePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DChoicePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DMixedPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOneOrMorePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOptionalPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*     */ import com.sun.xml.internal.rngom.digested.DZeroOrMorePattern;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ final class ContentModelBinder extends DPatternWalker
/*     */ {
/*     */   private final RELAXNGCompiler compiler;
/*     */   private final CClassInfo clazz;
/*  59 */   private boolean insideOptional = false;
/*  60 */   private int iota = 1;
/*     */ 
/*     */   public ContentModelBinder(RELAXNGCompiler compiler, CClassInfo clazz) {
/*  63 */     this.compiler = compiler;
/*  64 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */   public Void onMixed(DMixedPattern p) {
/*  68 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Void onChoice(DChoicePattern p) {
/*  72 */     boolean old = this.insideOptional;
/*  73 */     this.insideOptional = true;
/*  74 */     super.onChoice(p);
/*  75 */     this.insideOptional = old;
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   public Void onOptional(DOptionalPattern p) {
/*  80 */     boolean old = this.insideOptional;
/*  81 */     this.insideOptional = true;
/*  82 */     super.onOptional(p);
/*  83 */     this.insideOptional = old;
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/*  88 */     return onRepeated(p, true);
/*     */   }
/*     */ 
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/*  92 */     return onRepeated(p, this.insideOptional);
/*     */   }
/*     */ 
/*     */   private Void onRepeated(DPattern p, boolean optional)
/*     */   {
/*  97 */     RawTypeSet rts = RawTypeSetBuilder.build(this.compiler, p, optional ? Multiplicity.STAR : Multiplicity.PLUS);
/*  98 */     if (rts.canBeTypeRefs == RawTypeSet.Mode.SHOULD_BE_TYPEREF)
/*     */     {
/* 100 */       CElementPropertyInfo prop = new CElementPropertyInfo(
/* 100 */         calcName(p), 
/* 100 */         CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT, ID.NONE, null, null, null, p.getLocation(), !optional);
/* 101 */       rts.addTo(prop);
/* 102 */       this.clazz.addProperty(prop);
/*     */     }
/*     */     else {
/* 105 */       CReferencePropertyInfo prop = new CReferencePropertyInfo(
/* 105 */         calcName(p), 
/* 105 */         true, !optional, false, null, null, p.getLocation(), false, false, false);
/* 106 */       rts.addTo(prop);
/* 107 */       this.clazz.addProperty(prop);
/*     */     }
/*     */ 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public Void onAttribute(DAttributePattern p)
/*     */   {
/* 115 */     QName name = (QName)p.getName().listNames().iterator().next();
/*     */ 
/* 119 */     CAttributePropertyInfo ap = new CAttributePropertyInfo(
/* 118 */       calcName(p), 
/* 118 */       null, null, p.getLocation(), name, 
/* 119 */       (TypeUse)p
/* 119 */       .getChild().accept(this.compiler.typeUseBinder), null, !this.insideOptional);
/*     */ 
/* 121 */     this.clazz.addProperty(ap);
/*     */ 
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   private String calcName(DPattern p)
/*     */   {
/* 128 */     return "field" + this.iota++;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.relaxng.ContentModelBinder
 * JD-Core Version:    0.6.2
 */