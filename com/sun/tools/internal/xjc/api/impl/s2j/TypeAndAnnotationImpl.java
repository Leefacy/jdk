/*     */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlJavaTypeAdapterWriter;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapterMarker;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ 
/*     */ final class TypeAndAnnotationImpl
/*     */   implements TypeAndAnnotation
/*     */ {
/*     */   private final TypeUse typeUse;
/*     */   private final Outline outline;
/*     */ 
/*     */   public TypeAndAnnotationImpl(Outline outline, TypeUse typeUse)
/*     */   {
/*  53 */     this.typeUse = typeUse;
/*  54 */     this.outline = outline;
/*     */   }
/*     */ 
/*     */   public JType getTypeClass() {
/*  58 */     CAdapter a = this.typeUse.getAdapterUse();
/*     */     NType nt;
/*     */     NType nt;
/*  60 */     if (a != null)
/*  61 */       nt = (NType)a.customType;
/*     */     else {
/*  63 */       nt = (NType)this.typeUse.getInfo().getType();
/*     */     }
/*  65 */     JType jt = nt.toType(this.outline, Aspect.EXPOSED);
/*     */ 
/*  67 */     JPrimitiveType prim = jt.boxify().getPrimitiveType();
/*  68 */     if ((!this.typeUse.isCollection()) && (prim != null)) {
/*  69 */       jt = prim;
/*     */     }
/*  71 */     if (this.typeUse.isCollection()) {
/*  72 */       jt = jt.array();
/*     */     }
/*  74 */     return jt;
/*     */   }
/*     */ 
/*     */   public void annotate(JAnnotatable programElement) {
/*  78 */     if ((this.typeUse.getAdapterUse() == null) && (!this.typeUse.isCollection())) {
/*  79 */       return;
/*     */     }
/*  81 */     CAdapter adapterUse = this.typeUse.getAdapterUse();
/*  82 */     if (adapterUse != null)
/*     */     {
/*  84 */       if (adapterUse.getAdapterIfKnown() == SwaRefAdapterMarker.class) {
/*  85 */         programElement.annotate(XmlAttachmentRef.class);
/*     */       }
/*     */       else
/*     */       {
/*  89 */         ((XmlJavaTypeAdapterWriter)programElement.annotate2(XmlJavaTypeAdapterWriter.class)).value(((NClass)adapterUse.adapterType)
/*  90 */           .toType(this.outline, Aspect.EXPOSED));
/*     */       }
/*     */     }
/*     */ 
/*  93 */     if (this.typeUse.isCollection())
/*  94 */       programElement.annotate(XmlList.class);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  98 */     StringBuilder builder = new StringBuilder();
/*     */ 
/* 100 */     builder.append(getTypeClass());
/* 101 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o) {
/* 105 */     if (!(o instanceof TypeAndAnnotationImpl)) return false;
/* 106 */     TypeAndAnnotationImpl that = (TypeAndAnnotationImpl)o;
/* 107 */     return this.typeUse == that.typeUse;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 111 */     return this.typeUse.hashCode();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.impl.s2j.TypeAndAnnotationImpl
 * JD-Core Version:    0.6.2
 */