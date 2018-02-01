/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
/*     */ import com.sun.tools.internal.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.util.FlattenIterator;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public final class Model
/*     */   implements TypeInfoSet<NType, NClass, Void, Void>, CCustomizable
/*     */ {
/*  84 */   private final Map<NClass, CClassInfo> beans = new LinkedHashMap();
/*     */ 
/*  89 */   private final Map<NClass, CEnumLeafInfo> enums = new LinkedHashMap();
/*     */ 
/*  94 */   private final Map<NClass, Map<QName, CElementInfo>> elementMappings = new HashMap();
/*     */ 
/*  97 */   private final Iterable<? extends CElementInfo> allElements = new Iterable()
/*     */   {
/*     */     public Iterator<CElementInfo> iterator() {
/* 100 */       return new FlattenIterator(Model.this.elementMappings.values());
/*     */     }
/*  97 */   };
/*     */ 
/* 111 */   private final Map<QName, TypeUse> typeUses = new LinkedHashMap();
/*     */   private NameConverter nameConverter;
/*     */   CCustomizations customizations;
/* 128 */   private boolean packageLevelAnnotations = true;
/*     */   public final XSSchemaSet schemaComponent;
/* 139 */   private CCustomizations gloablCustomizations = new CCustomizations();
/*     */ 
/*     */   @XmlTransient
/*     */   public final JCodeModel codeModel;
/*     */   public final Options options;
/*     */ 
/*     */   @XmlAttribute
/*     */   public boolean serializable;
/*     */ 
/*     */   @XmlAttribute
/*     */   public Long serialVersionUID;
/*     */ 
/*     */   @XmlTransient
/*     */   public JClass rootClass;
/*     */ 
/*     */   @XmlTransient
/*     */   public JClass rootInterface;
/* 226 */   public ImplStructureStrategy strategy = ImplStructureStrategy.BEAN_ONLY;
/*     */   final ClassNameAllocatorWrapper allocator;
/*     */ 
/*     */   @XmlTransient
/*     */   public final SymbolSpace defaultSymbolSpace;
/* 248 */   private final Map<String, SymbolSpace> symbolSpaces = new HashMap();
/*     */ 
/* 474 */   private final Map<JPackage, CClassInfoParent.Package> cache = new HashMap();
/*     */ 
/* 489 */   static final Locator EMPTY_LOCATOR = l;
/*     */ 
/*     */   public Model(Options opts, JCodeModel cm, NameConverter nc, ClassNameAllocator allocator, XSSchemaSet schemaComponent)
/*     */   {
/* 149 */     this.options = opts;
/* 150 */     this.codeModel = cm;
/* 151 */     this.nameConverter = nc;
/* 152 */     this.defaultSymbolSpace = new SymbolSpace(this.codeModel);
/* 153 */     this.defaultSymbolSpace.setType(this.codeModel.ref(Object.class));
/*     */ 
/* 155 */     this.elementMappings.put(null, new HashMap());
/*     */ 
/* 157 */     if (opts.automaticNameConflictResolution)
/* 158 */       allocator = new AutoClassNameAllocator(allocator);
/* 159 */     this.allocator = new ClassNameAllocatorWrapper(allocator);
/* 160 */     this.schemaComponent = schemaComponent;
/* 161 */     this.gloablCustomizations.setParent(this, this);
/*     */   }
/*     */ 
/*     */   public void setNameConverter(NameConverter nameConverter) {
/* 165 */     assert (this.nameConverter == null);
/* 166 */     assert (nameConverter != null);
/* 167 */     this.nameConverter = nameConverter;
/*     */   }
/*     */ 
/*     */   public final NameConverter getNameConverter()
/*     */   {
/* 174 */     return this.nameConverter;
/*     */   }
/*     */ 
/*     */   public boolean isPackageLevelAnnotations() {
/* 178 */     return this.packageLevelAnnotations;
/*     */   }
/*     */ 
/*     */   public void setPackageLevelAnnotations(boolean packageLevelAnnotations) {
/* 182 */     this.packageLevelAnnotations = packageLevelAnnotations;
/*     */   }
/*     */ 
/*     */   public SymbolSpace getSymbolSpace(String name)
/*     */   {
/* 251 */     SymbolSpace ss = (SymbolSpace)this.symbolSpaces.get(name);
/* 252 */     if (ss == null)
/* 253 */       this.symbolSpaces.put(name, ss = new SymbolSpace(this.codeModel));
/* 254 */     return ss;
/*     */   }
/*     */ 
/*     */   public Outline generateCode(Options opt, ErrorReceiver receiver)
/*     */   {
/* 269 */     ErrorReceiverFilter ehf = new ErrorReceiverFilter(receiver);
/*     */ 
/* 275 */     Outline o = BeanGenerator.generate(this, ehf);
/*     */     try
/*     */     {
/* 278 */       for (Plugin ma : opt.activePlugins)
/* 279 */         ma.run(o, opt, ehf);
/*     */     }
/*     */     catch (SAXException e) {
/* 282 */       return null;
/*     */     }
/*     */ 
/* 288 */     Object check = new HashSet();
/* 289 */     for (CCustomizations c = this.customizations; c != null; c = c.next) {
/* 290 */       if (!((Set)check).add(c)) {
/* 291 */         throw new AssertionError();
/*     */       }
/* 293 */       for (CPluginCustomization p : c) {
/* 294 */         if (!p.isAcknowledged()) {
/* 295 */           ehf.error(p.locator, 
/* 297 */             Messages.format("UnusedCustomizationChecker.UnacknolwedgedCustomization", new Object[] { p.element
/* 299 */             .getNodeName() }));
/*     */ 
/* 301 */           ehf.error(c
/* 302 */             .getOwner().getLocator(), 
/* 303 */             Messages.format("UnusedCustomizationChecker.UnacknolwedgedCustomization.Relevant", new Object[0]));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 309 */     if (ehf.hadError())
/* 310 */       o = null;
/* 311 */     return o;
/*     */   }
/*     */ 
/*     */   public final Map<QName, CClassInfo> createTopLevelBindings()
/*     */   {
/* 331 */     Map r = new HashMap();
/* 332 */     for (CClassInfo b : beans().values()) {
/* 333 */       if (b.isElement())
/* 334 */         r.put(b.getElementName(), b);
/*     */     }
/* 336 */     return r;
/*     */   }
/*     */ 
/*     */   public Navigator<NType, NClass, Void, Void> getNavigator() {
/* 340 */     return NavigatorImpl.theInstance;
/*     */   }
/*     */ 
/*     */   public CNonElement getTypeInfo(NType type) {
/* 344 */     CBuiltinLeafInfo leaf = (CBuiltinLeafInfo)CBuiltinLeafInfo.LEAVES.get(type);
/* 345 */     if (leaf != null) return leaf;
/*     */ 
/* 347 */     return getClassInfo((NClass)getNavigator().asDecl(type));
/*     */   }
/*     */ 
/*     */   public CBuiltinLeafInfo getAnyTypeInfo() {
/* 351 */     return CBuiltinLeafInfo.ANYTYPE;
/*     */   }
/*     */ 
/*     */   public CNonElement getTypeInfo(Ref<NType, NClass> ref)
/*     */   {
/* 356 */     assert (!ref.valueList);
/* 357 */     return getTypeInfo((NType)ref.type);
/*     */   }
/*     */ 
/*     */   public Map<NClass, CClassInfo> beans() {
/* 361 */     return this.beans;
/*     */   }
/*     */ 
/*     */   public Map<NClass, CEnumLeafInfo> enums() {
/* 365 */     return this.enums;
/*     */   }
/*     */ 
/*     */   public Map<QName, TypeUse> typeUses() {
/* 369 */     return this.typeUses;
/*     */   }
/*     */ 
/*     */   public Map<NType, ? extends CArrayInfo> arrays()
/*     */   {
/* 376 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   public Map<NType, ? extends CBuiltinLeafInfo> builtins() {
/* 380 */     return CBuiltinLeafInfo.LEAVES;
/*     */   }
/*     */ 
/*     */   public CClassInfo getClassInfo(NClass t) {
/* 384 */     return (CClassInfo)this.beans.get(t);
/*     */   }
/*     */ 
/*     */   public CElementInfo getElementInfo(NClass scope, QName name) {
/* 388 */     Map m = (Map)this.elementMappings.get(scope);
/* 389 */     if (m != null) {
/* 390 */       CElementInfo r = (CElementInfo)m.get(name);
/* 391 */       if (r != null) return r;
/*     */     }
/* 393 */     return (CElementInfo)((Map)this.elementMappings.get(null)).get(name);
/*     */   }
/*     */ 
/*     */   public Map<QName, CElementInfo> getElementMappings(NClass scope) {
/* 397 */     return (Map)this.elementMappings.get(scope);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends CElementInfo> getAllElements() {
/* 401 */     return this.allElements;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public XSComponent getSchemaComponent()
/*     */   {
/* 409 */     return null;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Locator getLocator()
/*     */   {
/* 417 */     LocatorImpl r = new LocatorImpl();
/* 418 */     r.setLineNumber(-1);
/* 419 */     r.setColumnNumber(-1);
/* 420 */     return r;
/*     */   }
/*     */ 
/*     */   public CCustomizations getCustomizations()
/*     */   {
/* 427 */     return this.gloablCustomizations;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getXmlNs(String namespaceUri)
/*     */   {
/* 434 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   public Map<String, String> getSchemaLocations() {
/* 438 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   public XmlNsForm getElementFormDefault(String nsUri) {
/* 442 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public XmlNsForm getAttributeFormDefault(String nsUri) {
/* 446 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void dump(Result out)
/*     */   {
/* 451 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   void add(CEnumLeafInfo e) {
/* 455 */     this.enums.put(e.getClazz(), e);
/*     */   }
/*     */ 
/*     */   void add(CClassInfo ci) {
/* 459 */     this.beans.put(ci.getClazz(), ci);
/*     */   }
/*     */ 
/*     */   void add(CElementInfo ei) {
/* 463 */     NClass clazz = null;
/* 464 */     if (ei.getScope() != null) {
/* 465 */       clazz = ei.getScope().getClazz();
/*     */     }
/* 467 */     Map m = (Map)this.elementMappings.get(clazz);
/* 468 */     if (m == null)
/* 469 */       this.elementMappings.put(clazz, m = new HashMap());
/* 470 */     m.put(ei.getElementName(), ei);
/*     */   }
/*     */ 
/*     */   public CClassInfoParent.Package getPackage(JPackage pkg)
/*     */   {
/* 477 */     CClassInfoParent.Package r = (CClassInfoParent.Package)this.cache.get(pkg);
/* 478 */     if (r == null)
/* 479 */       this.cache.put(pkg, r = new CClassInfoParent.Package(pkg));
/* 480 */     return r;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 486 */     LocatorImpl l = new LocatorImpl();
/* 487 */     l.setColumnNumber(-1);
/* 488 */     l.setLineNumber(-1);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.Model
 * JD-Core Version:    0.6.2
 */