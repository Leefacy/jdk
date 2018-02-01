/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BindingComponent;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class ComplexTypeFieldBuilder extends BindingComponent
/*     */ {
/*  51 */   private final CTBuilder[] complexTypeBuilders = { new MultiWildcardComplexTypeBuilder(), new MixedExtendedComplexTypeBuilder(), new MixedComplexTypeBuilder(), new FreshComplexTypeBuilder(), new ExtendedComplexTypeBuilder(), new RestrictedComplexTypeBuilder(), new STDerivedComplexTypeBuilder() };
/*     */ 
/*  62 */   private final Map<XSComplexType, ComplexTypeBindingMode> complexTypeBindingModes = new HashMap();
/*     */ 
/*     */   public void build(XSComplexType type)
/*     */   {
/*  69 */     for (CTBuilder ctb : this.complexTypeBuilders) {
/*  70 */       if (ctb.isApplicable(type)) {
/*  71 */         ctb.build(type);
/*  72 */         return;
/*     */       }
/*     */     }
/*  75 */     if (!$assertionsDisabled) throw new AssertionError();
/*     */   }
/*     */ 
/*     */   public void recordBindingMode(XSComplexType type, ComplexTypeBindingMode flag)
/*     */   {
/*  93 */     Object o = this.complexTypeBindingModes.put(type, flag);
/*  94 */     assert (o == null);
/*     */   }
/*     */ 
/*     */   protected ComplexTypeBindingMode getBindingMode(XSComplexType type)
/*     */   {
/* 102 */     ComplexTypeBindingMode r = (ComplexTypeBindingMode)this.complexTypeBindingModes.get(type);
/* 103 */     assert (r != null);
/* 104 */     return r;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder
 * JD-Core Version:    0.6.2
 */