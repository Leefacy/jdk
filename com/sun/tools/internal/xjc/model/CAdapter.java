/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.tools.internal.xjc.model.nav.EagerNClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ 
/*     */ public final class CAdapter extends Adapter<NType, NClass>
/*     */ {
/*     */   private JClass adapterClass1;
/*     */   private Class<? extends XmlAdapter> adapterClass2;
/*     */ 
/*     */   public CAdapter(Class<? extends XmlAdapter> adapter, boolean copy)
/*     */   {
/*  67 */     super(getRef(adapter, copy), NavigatorImpl.theInstance);
/*  68 */     this.adapterClass1 = null;
/*  69 */     this.adapterClass2 = adapter;
/*     */   }
/*     */ 
/*     */   static NClass getRef(final Class<? extends XmlAdapter> adapter, boolean copy) {
/*  73 */     if (copy)
/*     */     {
/*  76 */       return new EagerNClass(adapter)
/*     */       {
/*     */         public JClass toType(Outline o, Aspect aspect) {
/*  79 */           return o.addRuntime(adapter);
/*     */         }
/*     */ 
/*     */         public String fullName() {
/*  83 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */     }
/*  87 */     return NavigatorImpl.theInstance.ref(adapter);
/*     */   }
/*     */ 
/*     */   public CAdapter(JClass adapter)
/*     */   {
/*  92 */     super(NavigatorImpl.theInstance.ref(adapter), NavigatorImpl.theInstance);
/*  93 */     this.adapterClass1 = adapter;
/*  94 */     this.adapterClass2 = null;
/*     */   }
/*     */ 
/*     */   public JClass getAdapterClass(Outline o) {
/*  98 */     if (this.adapterClass1 == null)
/*  99 */       this.adapterClass1 = o.getCodeModel().ref(this.adapterClass2);
/* 100 */     return ((NClass)this.adapterType).toType(o, Aspect.EXPOSED);
/*     */   }
/*     */ 
/*     */   public boolean isWhitespaceAdapter()
/*     */   {
/* 108 */     return (this.adapterClass2 == CollapsedStringAdapter.class) || (this.adapterClass2 == NormalizedStringAdapter.class);
/*     */   }
/*     */ 
/*     */   public Class<? extends XmlAdapter> getAdapterIfKnown()
/*     */   {
/* 117 */     return this.adapterClass2;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CAdapter
 * JD-Core Version:    0.6.2
 */