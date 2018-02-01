/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.Language;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIFactoryMethod;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public final class CClassInfo extends AbstractCElement
/*     */   implements ClassInfo<NType, NClass>, CClassInfoParent, CClass, NClass
/*     */ {
/*     */ 
/*     */   @XmlIDREF
/*     */   private CClass baseClass;
/*     */   private CClassInfo firstSubclass;
/*  84 */   private CClassInfo nextSibling = null;
/*     */   private final QName typeName;
/*     */ 
/*     */   @Nullable
/*     */   private String squeezedName;
/*     */ 
/*     */   @Nullable
/*     */   private final QName elementName;
/* 101 */   private boolean isOrdered = true;
/*     */ 
/* 103 */   private final List<CPropertyInfo> properties = new ArrayList();
/*     */   public String javadoc;
/*     */ 
/*     */   @XmlIDREF
/*     */   private final CClassInfoParent parent;
/*     */   public final String shortName;
/*     */ 
/*     */   @Nullable
/*     */   private String implClass;
/*     */   public final Model model;
/*     */   private boolean hasAttributeWildcard;
/* 249 */   private static final CClassInfoParent.Visitor<String> calcSqueezedName = new CClassInfoParent.Visitor() {
/*     */     public String onBean(CClassInfo bean) {
/* 251 */       return (String)bean.parent.accept(this) + bean.shortName;
/*     */     }
/*     */ 
/*     */     public String onElement(CElementInfo element) {
/* 255 */       return (String)element.parent.accept(this) + element.shortName();
/*     */     }
/*     */ 
/*     */     public String onPackage(JPackage pkg) {
/* 259 */       return "";
/*     */     }
/* 249 */   };
/*     */ 
/* 449 */   private Set<JClass> _implements = null;
/*     */ 
/* 459 */   private final List<Constructor> constructors = new ArrayList(1);
/*     */ 
/*     */   public CClassInfo(Model model, JPackage pkg, String shortName, Locator location, QName typeName, QName elementName, XSComponent source, CCustomizations customizations)
/*     */   {
/* 138 */     this(model, model.getPackage(pkg), shortName, location, typeName, elementName, source, customizations);
/*     */   }
/*     */ 
/*     */   public CClassInfo(Model model, CClassInfoParent p, String shortName, Locator location, QName typeName, QName elementName, XSComponent source, CCustomizations customizations) {
/* 142 */     super(model, source, location, customizations);
/* 143 */     this.model = model;
/* 144 */     this.parent = p;
/* 145 */     this.shortName = model.allocator.assignClassName(this.parent, shortName);
/* 146 */     this.typeName = typeName;
/* 147 */     this.elementName = elementName;
/*     */ 
/* 149 */     Language schemaLanguage = model.options.getSchemaLanguage();
/* 150 */     if ((schemaLanguage != null) && (
/* 151 */       (schemaLanguage
/* 151 */       .equals(Language.XMLSCHEMA)) || 
/* 151 */       (schemaLanguage.equals(Language.WSDL)))) {
/* 152 */       BIFactoryMethod factoryMethod = (BIFactoryMethod)((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).get(BIFactoryMethod.class);
/* 153 */       if (factoryMethod != null) {
/* 154 */         factoryMethod.markAsAcknowledged();
/* 155 */         this.squeezedName = factoryMethod.name;
/*     */       }
/*     */     }
/*     */ 
/* 159 */     model.add(this);
/*     */   }
/*     */ 
/*     */   public CClassInfo(Model model, JCodeModel cm, String fullName, Locator location, QName typeName, QName elementName, XSComponent source, CCustomizations customizations) {
/* 163 */     super(model, source, location, customizations);
/* 164 */     this.model = model;
/* 165 */     int idx = fullName.indexOf('.');
/* 166 */     if (idx < 0) {
/* 167 */       this.parent = model.getPackage(cm.rootPackage());
/* 168 */       this.shortName = model.allocator.assignClassName(this.parent, fullName);
/*     */     } else {
/* 170 */       this.parent = model.getPackage(cm._package(fullName.substring(0, idx)));
/* 171 */       this.shortName = model.allocator.assignClassName(this.parent, fullName.substring(idx + 1));
/*     */     }
/* 173 */     this.typeName = typeName;
/* 174 */     this.elementName = elementName;
/*     */ 
/* 176 */     model.add(this);
/*     */   }
/*     */ 
/*     */   public boolean hasAttributeWildcard() {
/* 180 */     return this.hasAttributeWildcard;
/*     */   }
/*     */ 
/*     */   public void hasAttributeWildcard(boolean hasAttributeWildcard) {
/* 184 */     this.hasAttributeWildcard = hasAttributeWildcard;
/*     */   }
/*     */ 
/*     */   public boolean hasSubClasses() {
/* 188 */     return this.firstSubclass != null;
/*     */   }
/*     */ 
/*     */   public boolean declaresAttributeWildcard()
/*     */   {
/* 196 */     return (this.hasAttributeWildcard) && (!inheritsAttributeWildcard());
/*     */   }
/*     */ 
/*     */   public boolean inheritsAttributeWildcard()
/*     */   {
/* 204 */     if (getRefBaseClass() != null) {
/* 205 */       CClassRef cref = (CClassRef)this.baseClass;
/* 206 */       if (cref.getSchemaComponent().getForeignAttributes().size() > 0)
/* 207 */         return true;
/*     */     }
/*     */     else {
/* 210 */       for (CClassInfo c = getBaseClass(); c != null; c = c.getBaseClass()) {
/* 211 */         if (c.hasAttributeWildcard)
/* 212 */           return true;
/*     */       }
/*     */     }
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */   public NClass getClazz()
/*     */   {
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   public CClassInfo getScope() {
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */   @XmlID
/*     */   public String getName() {
/* 229 */     return fullName();
/*     */   }
/*     */ 
/*     */   @XmlElement
/*     */   public String getSqueezedName()
/*     */   {
/* 245 */     if (this.squeezedName != null) return this.squeezedName;
/* 246 */     return (String)calcSqueezedName.onBean(this);
/*     */   }
/*     */ 
/*     */   public List<CPropertyInfo> getProperties()
/*     */   {
/* 267 */     return this.properties;
/*     */   }
/*     */ 
/*     */   public boolean hasValueProperty() {
/* 271 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public CPropertyInfo getProperty(String name)
/*     */   {
/* 279 */     for (CPropertyInfo p : this.properties)
/* 280 */       if (p.getName(false).equals(name))
/* 281 */         return p;
/* 282 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasProperties() {
/* 286 */     return !getProperties().isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean isElement() {
/* 290 */     return this.elementName != null;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public CNonElement getInfo()
/*     */   {
/* 298 */     return this;
/*     */   }
/*     */ 
/*     */   public Element<NType, NClass> asElement() {
/* 302 */     if (isElement()) {
/* 303 */       return this;
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isOrdered() {
/* 309 */     return this.isOrdered;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean isFinal()
/*     */   {
/* 317 */     return false;
/*     */   }
/*     */ 
/*     */   public void setOrdered(boolean value) {
/* 321 */     this.isOrdered = value;
/*     */   }
/*     */ 
/*     */   public QName getElementName() {
/* 325 */     return this.elementName;
/*     */   }
/*     */ 
/*     */   public QName getTypeName() {
/* 329 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public boolean isSimpleType() {
/* 333 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public String fullName()
/*     */   {
/* 340 */     String r = this.parent.fullName();
/* 341 */     if (r.length() == 0) return this.shortName;
/* 342 */     return r + '.' + this.shortName;
/*     */   }
/*     */ 
/*     */   public CClassInfoParent parent() {
/* 346 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setUserSpecifiedImplClass(String implClass) {
/* 350 */     assert (this.implClass == null);
/* 351 */     assert (implClass != null);
/* 352 */     this.implClass = implClass;
/*     */   }
/*     */ 
/*     */   public String getUserSpecifiedImplClass() {
/* 356 */     return this.implClass;
/*     */   }
/*     */ 
/*     */   public void addProperty(CPropertyInfo prop)
/*     */   {
/* 364 */     if (prop.ref().isEmpty())
/*     */     {
/* 367 */       return;
/* 368 */     }prop.setParent(this);
/* 369 */     this.properties.add(prop);
/*     */   }
/*     */ 
/*     */   public void setBaseClass(CClass base)
/*     */   {
/* 382 */     assert (this.baseClass == null);
/* 383 */     assert (base != null);
/* 384 */     this.baseClass = base;
/*     */ 
/* 386 */     assert (this.nextSibling == null);
/* 387 */     if ((base instanceof CClassInfo)) {
/* 388 */       CClassInfo realBase = (CClassInfo)base;
/* 389 */       this.nextSibling = realBase.firstSubclass;
/* 390 */       realBase.firstSubclass = this;
/*     */     }
/*     */   }
/*     */ 
/*     */   public CClassInfo getBaseClass()
/*     */   {
/* 400 */     if ((this.baseClass instanceof CClassInfo)) {
/* 401 */       return (CClassInfo)this.baseClass;
/*     */     }
/* 403 */     return null;
/*     */   }
/*     */ 
/*     */   public CClassRef getRefBaseClass()
/*     */   {
/* 408 */     if ((this.baseClass instanceof CClassRef)) {
/* 409 */       return (CClassRef)this.baseClass;
/*     */     }
/* 411 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator<CClassInfo> listSubclasses()
/*     */   {
/* 419 */     return new Iterator() {
/* 420 */       CClassInfo cur = CClassInfo.this.firstSubclass;
/*     */ 
/* 422 */       public boolean hasNext() { return this.cur != null; }
/*     */ 
/*     */       public CClassInfo next()
/*     */       {
/* 426 */         CClassInfo r = this.cur;
/* 427 */         this.cur = this.cur.nextSibling;
/* 428 */         return r;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 432 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public CClassInfo getSubstitutionHead() {
/* 438 */     CClassInfo c = getBaseClass();
/* 439 */     while ((c != null) && (!c.isElement()))
/* 440 */       c = c.getBaseClass();
/* 441 */     return c;
/*     */   }
/*     */ 
/*     */   public void _implements(JClass c)
/*     */   {
/* 452 */     if (this._implements == null)
/* 453 */       this._implements = new HashSet();
/* 454 */     this._implements.add(c);
/*     */   }
/*     */ 
/*     */   public void addConstructor(String[] fieldNames)
/*     */   {
/* 463 */     this.constructors.add(new Constructor(fieldNames));
/*     */   }
/*     */ 
/*     */   public Collection<? extends Constructor> getConstructors()
/*     */   {
/* 468 */     return this.constructors;
/*     */   }
/*     */ 
/*     */   public final <T> T accept(CClassInfoParent.Visitor<T> visitor) {
/* 472 */     return visitor.onBean(this);
/*     */   }
/*     */ 
/*     */   public JPackage getOwnerPackage() {
/* 476 */     return this.parent.getOwnerPackage();
/*     */   }
/*     */ 
/*     */   public final NClass getType() {
/* 480 */     return this;
/*     */   }
/*     */ 
/*     */   public final JClass toType(Outline o, Aspect aspect) {
/* 484 */     switch (3.$SwitchMap$com$sun$tools$internal$xjc$outline$Aspect[aspect.ordinal()]) {
/*     */     case 1:
/* 486 */       return o.getClazz(this).implRef;
/*     */     case 2:
/* 488 */       return o.getClazz(this).ref;
/*     */     }
/* 490 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public boolean isBoxedType()
/*     */   {
/* 495 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 499 */     return fullName();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CClassInfo
 * JD-Core Version:    0.6.2
 */