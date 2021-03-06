/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public final class CCustomizations extends ArrayList<CPluginCustomization>
/*     */ {
/*     */   CCustomizations next;
/*     */   private CCustomizable owner;
/* 123 */   public static final CCustomizations EMPTY = new CCustomizations();
/*     */ 
/*     */   public CCustomizations()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CCustomizations(Collection<? extends CPluginCustomization> cPluginCustomizations)
/*     */   {
/*  64 */     super(cPluginCustomizations);
/*     */   }
/*     */ 
/*     */   void setParent(Model model, CCustomizable owner) {
/*  68 */     if (this.owner != null) return;
/*     */ 
/*  74 */     this.next = model.customizations;
/*  75 */     model.customizations = this;
/*  76 */     assert (owner != null);
/*  77 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   public CCustomizable getOwner()
/*     */   {
/*  86 */     assert (this.owner != null);
/*  87 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public CPluginCustomization find(String nsUri)
/*     */   {
/*  95 */     for (CPluginCustomization p : this) {
/*  96 */       if (fixNull(p.element.getNamespaceURI()).equals(nsUri))
/*  97 */         return p;
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public CPluginCustomization find(String nsUri, String localName)
/*     */   {
/* 107 */     for (CPluginCustomization p : this) {
/* 108 */       if ((fixNull(p.element.getNamespaceURI()).equals(nsUri)) && 
/* 109 */         (fixNull(p.element
/* 109 */         .getLocalName()).equals(localName)))
/* 110 */         return p;
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   private String fixNull(String s) {
/* 116 */     if (s == null) return "";
/* 117 */     return s;
/*     */   }
/*     */ 
/*     */   public static CCustomizations merge(CCustomizations lhs, CCustomizations rhs)
/*     */   {
/* 129 */     if ((lhs == null) || (lhs.isEmpty())) return rhs;
/* 130 */     if ((rhs == null) || (rhs.isEmpty())) return lhs;
/*     */ 
/* 132 */     CCustomizations r = new CCustomizations(lhs);
/* 133 */     r.addAll(rhs);
/* 134 */     return r;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o) {
/* 138 */     return this == o;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 142 */     return System.identityHashCode(this);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CCustomizations
 * JD-Core Version:    0.6.2
 */