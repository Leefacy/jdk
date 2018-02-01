/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlSchemaWriter;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyVisitor;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.PackageOutline;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class PackageOutlineImpl
/*     */   implements PackageOutline
/*     */ {
/*     */   private final Model _model;
/*     */   private final JPackage _package;
/*     */   private final ObjectFactoryGenerator objectFactoryGenerator;
/*  66 */   final Set<ClassOutlineImpl> classes = new HashSet();
/*  67 */   private final Set<ClassOutlineImpl> classesView = Collections.unmodifiableSet(this.classes);
/*     */   private String mostUsedNamespaceURI;
/*     */   private XmlNsForm elementFormDefault;
/*     */   private XmlNsForm attributeFormDefault;
/* 213 */   private HashMap<String, Integer> uriCountMap = new HashMap();
/*     */ 
/* 217 */   private HashMap<String, Integer> propUriCountMap = new HashMap();
/*     */ 
/*     */   public String getMostUsedNamespaceURI()
/*     */   {
/*  83 */     return this.mostUsedNamespaceURI;
/*     */   }
/*     */ 
/*     */   public XmlNsForm getAttributeFormDefault()
/*     */   {
/*  92 */     assert (this.attributeFormDefault != null);
/*  93 */     return this.attributeFormDefault;
/*     */   }
/*     */ 
/*     */   public XmlNsForm getElementFormDefault()
/*     */   {
/* 102 */     assert (this.elementFormDefault != null);
/* 103 */     return this.elementFormDefault;
/*     */   }
/*     */ 
/*     */   public JPackage _package() {
/* 107 */     return this._package;
/*     */   }
/*     */ 
/*     */   public ObjectFactoryGenerator objectFactoryGenerator() {
/* 111 */     return this.objectFactoryGenerator;
/*     */   }
/*     */ 
/*     */   public Set<ClassOutlineImpl> getClasses() {
/* 115 */     return this.classesView;
/*     */   }
/*     */ 
/*     */   public JDefinedClass objectFactory() {
/* 119 */     return this.objectFactoryGenerator.getObjectFactory();
/*     */   }
/*     */ 
/*     */   protected PackageOutlineImpl(BeanGenerator outline, Model model, JPackage _pkg) {
/* 123 */     this._model = model;
/* 124 */     this._package = _pkg;
/* 125 */     switch (2.$SwitchMap$com$sun$tools$internal$xjc$generator$bean$ImplStructureStrategy[model.strategy.ordinal()]) {
/*     */     case 1:
/* 127 */       this.objectFactoryGenerator = new PublicObjectFactoryGenerator(outline, model, _pkg);
/* 128 */       break;
/*     */     case 2:
/* 130 */       this.objectFactoryGenerator = new DualObjectFactoryGenerator(outline, model, _pkg);
/* 131 */       break;
/*     */     default:
/* 133 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void calcDefaultValues()
/*     */   {
/* 147 */     if (!this._model.isPackageLevelAnnotations()) {
/* 148 */       this.mostUsedNamespaceURI = "";
/* 149 */       this.elementFormDefault = XmlNsForm.UNQUALIFIED;
/* 150 */       return;
/*     */     }
/*     */ 
/* 154 */     CPropertyVisitor propVisitor = new CPropertyVisitor() {
/*     */       public Void onElement(CElementPropertyInfo p) {
/* 156 */         for (CTypeRef tr : p.getTypes()) {
/* 157 */           PackageOutlineImpl.this.countURI(PackageOutlineImpl.this.propUriCountMap, tr.getTagName());
/*     */         }
/* 159 */         return null;
/*     */       }
/*     */ 
/*     */       public Void onReference(CReferencePropertyInfo p) {
/* 163 */         for (CElement e : p.getElements()) {
/* 164 */           PackageOutlineImpl.this.countURI(PackageOutlineImpl.this.propUriCountMap, e.getElementName());
/*     */         }
/* 166 */         return null;
/*     */       }
/*     */ 
/*     */       public Void onAttribute(CAttributePropertyInfo p) {
/* 170 */         return null;
/*     */       }
/*     */ 
/*     */       public Void onValue(CValuePropertyInfo p) {
/* 174 */         return null;
/*     */       }
/*     */     };
/* 179 */     for (ClassOutlineImpl co : this.classes) {
/* 180 */       CClassInfo ci = co.target;
/* 181 */       countURI(this.uriCountMap, ci.getTypeName());
/* 182 */       countURI(this.uriCountMap, ci.getElementName());
/*     */ 
/* 184 */       for (CPropertyInfo p : ci.getProperties())
/* 185 */         p.accept(propVisitor);
/*     */     }
/* 187 */     this.mostUsedNamespaceURI = getMostUsedURI(this.uriCountMap);
/*     */ 
/* 189 */     this.elementFormDefault = getFormDefault();
/* 190 */     this.attributeFormDefault = XmlNsForm.UNQUALIFIED;
/*     */     try {
/* 192 */       XmlNsForm modelValue = this._model.getAttributeFormDefault(this.mostUsedNamespaceURI);
/* 193 */       this.attributeFormDefault = modelValue;
/*     */     }
/*     */     catch (Exception localException1)
/*     */     {
/*     */     }
/*     */ 
/* 200 */     if ((!this.mostUsedNamespaceURI.equals("")) || (this.elementFormDefault == XmlNsForm.QUALIFIED) || (this.attributeFormDefault == XmlNsForm.QUALIFIED)) {
/* 201 */       XmlSchemaWriter w = (XmlSchemaWriter)this._model.strategy.getPackage(this._package, Aspect.IMPLEMENTATION).annotate2(XmlSchemaWriter.class);
/* 202 */       if (!this.mostUsedNamespaceURI.equals(""))
/* 203 */         w.namespace(this.mostUsedNamespaceURI);
/* 204 */       if (this.elementFormDefault == XmlNsForm.QUALIFIED)
/* 205 */         w.elementFormDefault(this.elementFormDefault);
/* 206 */       if (this.attributeFormDefault == XmlNsForm.QUALIFIED)
/* 207 */         w.attributeFormDefault(this.attributeFormDefault);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void countURI(HashMap<String, Integer> map, QName qname)
/*     */   {
/* 226 */     if (qname == null) return;
/*     */ 
/* 228 */     String uri = qname.getNamespaceURI();
/*     */ 
/* 230 */     if (map.containsKey(uri))
/* 231 */       map.put(uri, Integer.valueOf(((Integer)map.get(uri)).intValue() + 1));
/*     */     else
/* 233 */       map.put(uri, Integer.valueOf(1));
/*     */   }
/*     */ 
/*     */   private String getMostUsedURI(HashMap<String, Integer> map)
/*     */   {
/* 248 */     String mostPopular = null;
/* 249 */     int count = 0;
/*     */ 
/* 251 */     for (Map.Entry e : map.entrySet()) {
/* 252 */       String uri = (String)e.getKey();
/* 253 */       int uriCount = ((Integer)e.getValue()).intValue();
/* 254 */       if (mostPopular == null) {
/* 255 */         mostPopular = uri;
/* 256 */         count = uriCount;
/*     */       }
/* 258 */       else if ((uriCount > count) || ((uriCount == count) && (mostPopular.equals("")))) {
/* 259 */         mostPopular = uri;
/* 260 */         count = uriCount;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 265 */     if (mostPopular == null) return "";
/* 266 */     return mostPopular;
/*     */   }
/*     */ 
/*     */   private XmlNsForm getFormDefault()
/*     */   {
/* 276 */     if (getMostUsedURI(this.propUriCountMap).equals("")) return XmlNsForm.UNQUALIFIED;
/* 277 */     return XmlNsForm.QUALIFIED;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.PackageOutlineImpl
 * JD-Core Version:    0.6.2
 */