/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JEnumConstant;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldRef;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JForEach;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.codemodel.internal.fmt.JStaticJavaFile;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.xjc.AbortException;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAnyAttributeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlEnumValueWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlEnumWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlJavaTypeAdapterWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlMimeTypeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlRootElementWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlSeeAlsoWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlTypeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent.Visitor;
/*     */ import com.sun.tools.internal.xjc.model.CClassRef;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.EnumConstantOutline;
/*     */ import com.sun.tools.internal.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.outline.PackageOutline;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapterMarker;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class BeanGenerator
/*     */   implements Outline
/*     */ {
/*     */   private final CodeModelClassFactory codeModelClassFactory;
/*     */   private final ErrorReceiver errorReceiver;
/* 116 */   private final Map<JPackage, PackageOutlineImpl> packageContexts = new LinkedHashMap();
/*     */ 
/* 118 */   private final Map<CClassInfo, ClassOutlineImpl> classes = new LinkedHashMap();
/*     */ 
/* 120 */   private final Map<CEnumLeafInfo, EnumOutline> enums = new LinkedHashMap();
/*     */ 
/* 124 */   private final Map<Class, JClass> generatedRuntime = new LinkedHashMap();
/*     */   private final Model model;
/*     */   private final JCodeModel codeModel;
/* 131 */   private final Map<CPropertyInfo, FieldOutline> fields = new LinkedHashMap();
/*     */ 
/* 135 */   final Map<CElementInfo, ElementOutlineImpl> elements = new LinkedHashMap();
/*     */ 
/* 337 */   private final CClassInfoParent.Visitor<JClassContainer> exposedContainerBuilder = new CClassInfoParent.Visitor()
/*     */   {
/*     */     public JClassContainer onBean(CClassInfo bean)
/*     */     {
/* 341 */       return BeanGenerator.this.getClazz(bean).ref;
/*     */     }
/*     */ 
/*     */     public JClassContainer onElement(CElementInfo element)
/*     */     {
/* 346 */       return BeanGenerator.this.getElement(element).implClass;
/*     */     }
/*     */ 
/*     */     public JClassContainer onPackage(JPackage pkg) {
/* 350 */       return BeanGenerator.this.model.strategy.getPackage(pkg, Aspect.EXPOSED);
/*     */     }
/* 337 */   };
/*     */ 
/* 353 */   private final CClassInfoParent.Visitor<JClassContainer> implContainerBuilder = new CClassInfoParent.Visitor()
/*     */   {
/*     */     public JClassContainer onBean(CClassInfo bean)
/*     */     {
/* 357 */       return BeanGenerator.this.getClazz(bean).implClass;
/*     */     }
/*     */ 
/*     */     public JClassContainer onElement(CElementInfo element) {
/* 361 */       return BeanGenerator.this.getElement(element).implClass;
/*     */     }
/*     */ 
/*     */     public JClassContainer onPackage(JPackage pkg) {
/* 365 */       return BeanGenerator.this.model.strategy.getPackage(pkg, Aspect.IMPLEMENTATION);
/*     */     }
/* 353 */   };
/*     */ 
/*     */   public static Outline generate(Model model, ErrorReceiver _errorReceiver)
/*     */   {
/*     */     try
/*     */     {
/* 154 */       return new BeanGenerator(model, _errorReceiver); } catch (AbortException e) {
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   private BeanGenerator(Model _model, ErrorReceiver _errorReceiver)
/*     */   {
/* 162 */     this.model = _model;
/* 163 */     this.codeModel = this.model.codeModel;
/* 164 */     this.errorReceiver = _errorReceiver;
/* 165 */     this.codeModelClassFactory = new CodeModelClassFactory(this.errorReceiver);
/*     */ 
/* 168 */     for (Iterator localIterator = this.model.enums().values().iterator(); localIterator.hasNext(); ) { p = (CEnumLeafInfo)localIterator.next();
/* 169 */       this.enums.put(p, generateEnumDef(p));
/*     */     }
/*     */ 
/* 172 */     JPackage[] packages = getUsedPackages(Aspect.EXPOSED);
/*     */ 
/* 175 */     for (JPackage pkg : packages) {
/* 176 */       getPackageContext(pkg);
/*     */     }
/*     */ 
/* 181 */     for (CClassInfo bean : this.model.beans().values()) {
/* 182 */       getClazz(bean);
/*     */     }
/*     */ 
/* 186 */     for (CEnumLeafInfo p = this.packageContexts.values().iterator(); p.hasNext(); ) { p = (PackageOutlineImpl)p.next();
/* 187 */       ((PackageOutlineImpl)p).calcDefaultValues();
/*     */     }
/*     */ 
/* 190 */     JClass OBJECT = this.codeModel.ref(Object.class);
/*     */ 
/* 194 */     for (Object p = getClasses().iterator(); ((Iterator)p).hasNext(); ) { ClassOutlineImpl cc = (ClassOutlineImpl)((Iterator)p).next();
/*     */ 
/* 197 */       CClassInfo superClass = cc.target.getBaseClass();
/* 198 */       if (superClass != null)
/*     */       {
/* 200 */         this.model.strategy._extends(cc, getClazz(superClass));
/*     */       } else {
/* 202 */         CClassRef refSuperClass = cc.target.getRefBaseClass();
/* 203 */         if (refSuperClass != null) {
/* 204 */           cc.implClass._extends(refSuperClass.toType(this, Aspect.EXPOSED));
/*     */         }
/*     */         else {
/* 207 */           if ((this.model.rootClass != null) && (cc.implClass._extends().equals(OBJECT))) {
/* 208 */             cc.implClass._extends(this.model.rootClass);
/*     */           }
/* 210 */           if (this.model.rootInterface != null) {
/* 211 */             cc.ref._implements(this.model.rootInterface);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 222 */       if (this.model.serializable) {
/* 223 */         cc.implClass._implements(Serializable.class);
/* 224 */         if (this.model.serialVersionUID != null) {
/* 225 */           cc.implClass.field(28, this.codeModel.LONG, "serialVersionUID", 
/* 229 */             JExpr.lit(this.model.serialVersionUID
/* 229 */             .longValue()));
/*     */         }
/*     */       }
/*     */ 
/* 233 */       CClassInfoParent base = cc.target.parent();
/* 234 */       if ((base != null) && ((base instanceof CClassInfo))) {
/* 235 */         String pkg = base.getOwnerPackage().name();
/* 236 */         String shortName = base.fullName().substring(base.fullName().indexOf(pkg) + pkg.length() + 1);
/* 237 */         if (cc.target.shortName.equals(shortName)) {
/* 238 */           getErrorReceiver().error(cc.target.getLocator(), Messages.ERR_KEYNAME_COLLISION.format(new Object[] { shortName }));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 245 */     for (p = getClasses().iterator(); ((Iterator)p).hasNext(); ) { ClassOutlineImpl co = (ClassOutlineImpl)((Iterator)p).next();
/* 246 */       generateClassBody(co);
/*     */     }
/*     */ 
/* 249 */     for (p = this.enums.values().iterator(); ((Iterator)p).hasNext(); ) { EnumOutline eo = (EnumOutline)((Iterator)p).next();
/* 250 */       generateEnumBody(eo);
/*     */     }
/*     */ 
/* 254 */     for (p = this.model.getAllElements().iterator(); ((Iterator)p).hasNext(); ) { CElementInfo ei = (CElementInfo)((Iterator)p).next();
/* 255 */       getPackageContext(ei._package()).objectFactoryGenerator().populate(ei);
/*     */     }
/*     */ 
/* 258 */     if (this.model.options.debugMode)
/* 259 */       generateClassList();
/*     */   }
/*     */ 
/*     */   private void generateClassList()
/*     */   {
/*     */     try
/*     */     {
/* 273 */       JDefinedClass jc = this.codeModel.rootPackage()._class("JAXBDebug");
/* 274 */       JMethod m = jc.method(17, JAXBContext.class, "createContext");
/* 275 */       JVar $classLoader = m.param(ClassLoader.class, "classLoader");
/* 276 */       m._throws(JAXBException.class);
/* 277 */       JInvocation inv = this.codeModel.ref(JAXBContext.class).staticInvoke("newInstance");
/* 278 */       m.body()._return(inv);
/*     */       StringBuilder buf;
/* 280 */       switch (this.model.strategy) {
/*     */       case INTF_AND_IMPL:
/* 282 */         buf = new StringBuilder();
/* 283 */         for (PackageOutlineImpl po : this.packageContexts.values()) {
/* 284 */           if (buf.length() > 0) {
/* 285 */             buf.append(':');
/*     */           }
/* 287 */           buf.append(po._package().name());
/*     */         }
/* 289 */         inv.arg(buf.toString()).arg($classLoader);
/* 290 */         break;
/*     */       case BEAN_ONLY:
/* 293 */         for (ClassOutlineImpl cc : getClasses()) {
/* 294 */           inv.arg(cc.implRef.dotclass());
/*     */         }
/* 296 */         for (PackageOutlineImpl po : this.packageContexts.values()) {
/* 297 */           inv.arg(po.objectFactory().dotclass());
/*     */         }
/* 299 */         break;
/*     */       default:
/* 301 */         throw new IllegalStateException();
/*     */       }
/*     */     } catch (JClassAlreadyExistsException e) {
/* 304 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Model getModel()
/*     */   {
/* 311 */     return this.model;
/*     */   }
/*     */ 
/*     */   public JCodeModel getCodeModel() {
/* 315 */     return this.codeModel;
/*     */   }
/*     */ 
/*     */   public JClassContainer getContainer(CClassInfoParent parent, Aspect aspect)
/*     */   {
/*     */     CClassInfoParent.Visitor v;
/*     */     CClassInfoParent.Visitor v;
/* 320 */     switch (aspect) {
/*     */     case EXPOSED:
/* 322 */       v = this.exposedContainerBuilder;
/* 323 */       break;
/*     */     case IMPLEMENTATION:
/* 325 */       v = this.implContainerBuilder;
/* 326 */       break;
/*     */     default:
/* 328 */       if (!$assertionsDisabled) throw new AssertionError();
/* 329 */       throw new IllegalStateException();
/*     */     }
/*     */     CClassInfoParent.Visitor v;
/* 331 */     return (JClassContainer)parent.accept(v);
/*     */   }
/*     */ 
/*     */   public final JType resolve(CTypeRef ref, Aspect a) {
/* 335 */     return ((NType)ref.getTarget().getType()).toType(this, a);
/*     */   }
/*     */ 
/*     */   public final JPackage[] getUsedPackages(Aspect aspect)
/*     */   {
/* 383 */     Set s = new TreeSet();
/*     */ 
/* 385 */     for (CClassInfo bean : this.model.beans().values()) {
/* 386 */       JClassContainer cont = getContainer(bean.parent(), aspect);
/* 387 */       if (cont.isPackage()) {
/* 388 */         s.add((JPackage)cont);
/*     */       }
/*     */     }
/*     */ 
/* 392 */     for (CElementInfo e : this.model.getElementMappings(null).values())
/*     */     {
/* 397 */       s.add(e._package());
/*     */     }
/*     */ 
/* 400 */     return (JPackage[])s.toArray(new JPackage[s.size()]);
/*     */   }
/*     */ 
/*     */   public ErrorReceiver getErrorReceiver() {
/* 404 */     return this.errorReceiver;
/*     */   }
/*     */ 
/*     */   public CodeModelClassFactory getClassFactory() {
/* 408 */     return this.codeModelClassFactory;
/*     */   }
/*     */ 
/*     */   public PackageOutlineImpl getPackageContext(JPackage p) {
/* 412 */     PackageOutlineImpl r = (PackageOutlineImpl)this.packageContexts.get(p);
/* 413 */     if (r == null) {
/* 414 */       r = new PackageOutlineImpl(this, this.model, p);
/* 415 */       this.packageContexts.put(p, r);
/*     */     }
/* 417 */     return r;
/*     */   }
/*     */ 
/*     */   private ClassOutlineImpl generateClassDef(CClassInfo bean)
/*     */   {
/* 425 */     ImplStructureStrategy.Result r = this.model.strategy.createClasses(this, bean);
/*     */     JClass implRef;
/*     */     JClass implRef;
/* 428 */     if (bean.getUserSpecifiedImplClass() != null)
/*     */     {
/*     */       JDefinedClass usr;
/*     */       try {
/* 432 */         JDefinedClass usr = this.codeModel._class(bean.getUserSpecifiedImplClass());
/*     */ 
/* 434 */         usr.hide();
/*     */       }
/*     */       catch (JClassAlreadyExistsException e) {
/* 437 */         usr = e.getExistingClass();
/*     */       }
/* 439 */       usr._extends(r.implementation);
/* 440 */       implRef = usr;
/*     */     } else {
/* 442 */       implRef = r.implementation;
/*     */     }
/*     */ 
/* 445 */     return new ClassOutlineImpl(this, bean, r.exposed, r.implementation, implRef);
/*     */   }
/*     */ 
/*     */   public Collection<ClassOutlineImpl> getClasses()
/*     */   {
/* 450 */     assert (this.model.beans().size() == this.classes.size());
/* 451 */     return this.classes.values();
/*     */   }
/*     */ 
/*     */   public ClassOutlineImpl getClazz(CClassInfo bean) {
/* 455 */     ClassOutlineImpl r = (ClassOutlineImpl)this.classes.get(bean);
/* 456 */     if (r == null) {
/* 457 */       this.classes.put(bean, r = generateClassDef(bean));
/*     */     }
/* 459 */     return r;
/*     */   }
/*     */ 
/*     */   public ElementOutlineImpl getElement(CElementInfo ei) {
/* 463 */     ElementOutlineImpl def = (ElementOutlineImpl)this.elements.get(ei);
/* 464 */     if ((def == null) && (ei.hasClass()))
/*     */     {
/* 466 */       def = new ElementOutlineImpl(this, ei);
/*     */     }
/* 468 */     return def;
/*     */   }
/*     */ 
/*     */   public EnumOutline getEnum(CEnumLeafInfo eli) {
/* 472 */     return (EnumOutline)this.enums.get(eli);
/*     */   }
/*     */ 
/*     */   public Collection<EnumOutline> getEnums() {
/* 476 */     return this.enums.values();
/*     */   }
/*     */ 
/*     */   public Iterable<? extends PackageOutline> getAllPackageContexts() {
/* 480 */     return this.packageContexts.values();
/*     */   }
/*     */ 
/*     */   public FieldOutline getField(CPropertyInfo prop) {
/* 484 */     return (FieldOutline)this.fields.get(prop);
/*     */   }
/*     */ 
/*     */   private void generateClassBody(ClassOutlineImpl cc)
/*     */   {
/* 492 */     CClassInfo target = cc.target;
/*     */ 
/* 495 */     String mostUsedNamespaceURI = cc._package().getMostUsedNamespaceURI();
/*     */ 
/* 499 */     XmlTypeWriter xtw = (XmlTypeWriter)cc.implClass.annotate2(XmlTypeWriter.class);
/* 500 */     writeTypeName(cc.target.getTypeName(), xtw, mostUsedNamespaceURI);
/*     */ 
/* 502 */     if (this.model.options.target.isLaterThan(SpecVersion.V2_1))
/*     */     {
/* 504 */       Iterator subclasses = cc.target.listSubclasses();
/* 505 */       if (subclasses.hasNext()) {
/* 506 */         XmlSeeAlsoWriter saw = (XmlSeeAlsoWriter)cc.implClass.annotate2(XmlSeeAlsoWriter.class);
/* 507 */         while (subclasses.hasNext()) {
/* 508 */           CClassInfo s = (CClassInfo)subclasses.next();
/* 509 */           saw.value(getClazz(s).implRef);
/*     */         }
/*     */       }
/*     */     }
/*     */     String namespaceURI;
/* 514 */     if (target.isElement()) {
/* 515 */       namespaceURI = target.getElementName().getNamespaceURI();
/* 516 */       String localPart = target.getElementName().getLocalPart();
/*     */ 
/* 520 */       XmlRootElementWriter xrew = (XmlRootElementWriter)cc.implClass.annotate2(XmlRootElementWriter.class);
/* 521 */       xrew.name(localPart);
/* 522 */       if (!namespaceURI.equals(mostUsedNamespaceURI))
/*     */       {
/* 524 */         xrew.namespace(namespaceURI);
/*     */       }
/*     */     }
/*     */ 
/* 528 */     if (target.isOrdered()) {
/* 529 */       for (CPropertyInfo p : target.getProperties()) {
/* 530 */         if ((!(p instanceof CAttributePropertyInfo)) && (
/* 531 */           (!(p instanceof CReferencePropertyInfo)) || 
/* 532 */           (!((CReferencePropertyInfo)p)
/* 532 */           .isDummy()))) {
/* 533 */           xtw.propOrder(p.getName(false));
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 539 */       xtw.getAnnotationUse().paramArray("propOrder");
/*     */     }
/*     */ 
/* 542 */     for (CPropertyInfo prop : target.getProperties()) {
/* 543 */       generateFieldDecl(cc, prop);
/*     */     }
/*     */ 
/* 546 */     if (target.declaresAttributeWildcard()) {
/* 547 */       generateAttributeWildcard(cc);
/*     */     }
/*     */ 
/* 551 */     cc.ref.javadoc().append(target.javadoc);
/*     */ 
/* 553 */     cc._package().objectFactoryGenerator().populate(cc);
/*     */   }
/*     */ 
/*     */   private void writeTypeName(QName typeName, XmlTypeWriter xtw, String mostUsedNamespaceURI) {
/* 557 */     if (typeName == null) {
/* 558 */       xtw.name("");
/*     */     } else {
/* 560 */       xtw.name(typeName.getLocalPart());
/* 561 */       String typeNameURI = typeName.getNamespaceURI();
/* 562 */       if (!typeNameURI.equals(mostUsedNamespaceURI))
/*     */       {
/* 564 */         xtw.namespace(typeNameURI);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void generateAttributeWildcard(ClassOutlineImpl cc)
/*     */   {
/* 573 */     String FIELD_NAME = "otherAttributes";
/* 574 */     String METHOD_SEED = this.model.getNameConverter().toClassName(FIELD_NAME);
/*     */ 
/* 576 */     JClass mapType = this.codeModel.ref(Map.class).narrow(new Class[] { QName.class, String.class });
/* 577 */     JClass mapImpl = this.codeModel.ref(HashMap.class).narrow(new Class[] { QName.class, String.class });
/*     */ 
/* 581 */     JFieldVar $ref = cc.implClass.field(4, mapType, FIELD_NAME, 
/* 582 */       JExpr._new(mapImpl));
/*     */ 
/* 583 */     $ref.annotate2(XmlAnyAttributeWriter.class);
/*     */ 
/* 585 */     MethodWriter writer = cc.createMethodWriter();
/*     */ 
/* 587 */     JMethod $get = writer.declareMethod(mapType, "get" + METHOD_SEED);
/* 588 */     $get.javadoc().append("Gets a map that contains attributes that aren't bound to any typed property on this class.\n\n<p>\nthe map is keyed by the name of the attribute and \nthe value is the string value of the attribute.\n\nthe map returned by this method is live, and you can add new attribute\nby updating the map directly. Because of this design, there's no setter.\n");
/*     */ 
/* 596 */     $get.javadoc().addReturn().append("always non-null");
/*     */ 
/* 598 */     $get.body()._return($ref);
/*     */   }
/*     */ 
/*     */   private EnumOutline generateEnumDef(CEnumLeafInfo e)
/*     */   {
/* 608 */     JDefinedClass type = getClassFactory().createClass(
/* 609 */       getContainer(e.parent, Aspect.EXPOSED), 
/* 609 */       e.shortName, e.getLocator(), ClassType.ENUM);
/* 610 */     type.javadoc().append(e.javadoc);
/*     */ 
/* 612 */     return new EnumOutline(e, type)
/*     */     {
/*     */       @NotNull
/*     */       public Outline parent()
/*     */       {
/* 618 */         return BeanGenerator.this;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private void generateEnumBody(EnumOutline eo) {
/* 624 */     JDefinedClass type = eo.clazz;
/* 625 */     CEnumLeafInfo e = eo.target;
/*     */ 
/* 627 */     XmlTypeWriter xtw = (XmlTypeWriter)type.annotate2(XmlTypeWriter.class);
/* 628 */     writeTypeName(e.getTypeName(), xtw, eo
/* 629 */       ._package().getMostUsedNamespaceURI());
/*     */ 
/* 631 */     JCodeModel cModel = this.model.codeModel;
/*     */ 
/* 634 */     JType baseExposedType = e.base.toType(this, Aspect.EXPOSED).unboxify();
/* 635 */     JType baseImplType = e.base.toType(this, Aspect.IMPLEMENTATION).unboxify();
/*     */ 
/* 638 */     XmlEnumWriter xew = (XmlEnumWriter)type.annotate2(XmlEnumWriter.class);
/* 639 */     xew.value(baseExposedType);
/*     */ 
/* 642 */     boolean needsValue = e.needsValueField();
/*     */ 
/* 648 */     Set enumFieldNames = new HashSet();
/*     */ 
/* 650 */     for (CEnumConstant mem : e.members) {
/* 651 */       String constName = mem.getName();
/*     */ 
/* 653 */       if (!JJavaName.isJavaIdentifier(constName))
/*     */       {
/* 655 */         getErrorReceiver().error(e.getLocator(), Messages.ERR_UNUSABLE_NAME
/* 656 */           .format(new Object[] { mem
/* 656 */           .getLexicalValue(), constName }));
/*     */       }
/*     */ 
/* 659 */       if (!enumFieldNames.add(constName)) {
/* 660 */         getErrorReceiver().error(e.getLocator(), Messages.ERR_NAME_COLLISION.format(new Object[] { constName }));
/*     */       }
/*     */ 
/* 666 */       JEnumConstant constRef = type.enumConstant(constName);
/* 667 */       if (needsValue) {
/* 668 */         constRef.arg(e.base.createConstant(this, new XmlString(mem.getLexicalValue())));
/*     */       }
/*     */ 
/* 671 */       if (!mem.getLexicalValue().equals(constName)) {
/* 672 */         ((XmlEnumValueWriter)constRef.annotate2(XmlEnumValueWriter.class)).value(mem.getLexicalValue());
/*     */       }
/*     */ 
/* 676 */       if (mem.javadoc != null) {
/* 677 */         constRef.javadoc().append(mem.javadoc);
/*     */       }
/*     */ 
/* 680 */       eo.constants.add(new EnumConstantOutline(mem, constRef)
/*     */       {
/*     */       });
/*     */     }
/*     */ 
/* 685 */     if (needsValue)
/*     */     {
/* 688 */       JFieldVar $value = type.field(12, baseExposedType, "value");
/*     */ 
/* 692 */       type.method(1, baseExposedType, "value").body()._return($value);
/*     */ 
/* 699 */       JMethod m = type.constructor(0);
/* 700 */       m.body().assign($value, m.param(baseImplType, "v"));
/*     */ 
/* 712 */       JMethod m = type.method(17, type, "fromValue");
/* 713 */       JVar $v = m.param(baseExposedType, "v");
/* 714 */       JForEach fe = m.body().forEach(type, "c", type.staticInvoke("values"));
/*     */       JExpression eq;
/*     */       JExpression eq;
/* 716 */       if (baseExposedType.isPrimitive())
/* 717 */         eq = fe.var().ref($value).eq($v);
/*     */       else {
/* 719 */         eq = fe.var().ref($value).invoke("equals").arg($v);
/*     */       }
/*     */ 
/* 722 */       fe.body()._if(eq)._then()._return(fe.var());
/*     */ 
/* 724 */       JInvocation ex = JExpr._new(cModel.ref(IllegalArgumentException.class));
/*     */       JExpression strForm;
/*     */       JExpression strForm;
/* 727 */       if (baseExposedType.isPrimitive()) {
/* 728 */         strForm = cModel.ref(String.class).staticInvoke("valueOf").arg($v);
/*     */       }
/*     */       else
/*     */       {
/*     */         JExpression strForm;
/* 729 */         if (baseExposedType == cModel.ref(String.class))
/* 730 */           strForm = $v;
/*     */         else
/* 732 */           strForm = $v.invoke("toString");
/*     */       }
/* 734 */       m.body()._throw(ex.arg(strForm));
/*     */     }
/*     */     else
/*     */     {
/* 739 */       type.method(1, String.class, "value").body()._return(JExpr.invoke("name"));
/*     */ 
/* 743 */       JMethod m = type.method(17, type, "fromValue");
/* 744 */       m.body()._return(JExpr.invoke("valueOf").arg(m.param(String.class, "v")));
/*     */     }
/*     */   }
/*     */ 
/*     */   private FieldOutline generateFieldDecl(ClassOutlineImpl cc, CPropertyInfo prop)
/*     */   {
/* 756 */     FieldRenderer fr = prop.realization;
/* 757 */     if (fr == null)
/*     */     {
/* 759 */       fr = this.model.options.getFieldRendererFactory().getDefault();
/*     */     }
/*     */ 
/* 762 */     FieldOutline field = fr.generate(cc, prop);
/* 763 */     this.fields.put(prop, field);
/*     */ 
/* 765 */     return field;
/*     */   }
/*     */ 
/*     */   public final void generateAdapterIfNecessary(CPropertyInfo prop, JAnnotatable field)
/*     */   {
/* 774 */     CAdapter adapter = prop.getAdapter();
/* 775 */     if (adapter != null) {
/* 776 */       if (adapter.getAdapterIfKnown() == SwaRefAdapterMarker.class) {
/* 777 */         field.annotate(XmlAttachmentRef.class);
/*     */       }
/*     */       else
/*     */       {
/* 781 */         XmlJavaTypeAdapterWriter xjtw = (XmlJavaTypeAdapterWriter)field.annotate2(XmlJavaTypeAdapterWriter.class);
/* 782 */         xjtw.value(((NClass)adapter.adapterType).toType(this, Aspect.EXPOSED));
/*     */       }
/*     */     }
/*     */ 
/* 786 */     switch (prop.id()) {
/*     */     case ID:
/* 788 */       field.annotate(XmlID.class);
/* 789 */       break;
/*     */     case IDREF:
/* 791 */       field.annotate(XmlIDREF.class);
/*     */     }
/*     */ 
/* 795 */     if (prop.getExpectedMimeType() != null)
/* 796 */       ((XmlMimeTypeWriter)field.annotate2(XmlMimeTypeWriter.class)).value(prop.getExpectedMimeType().toString());
/*     */   }
/*     */ 
/*     */   public final JClass addRuntime(Class clazz)
/*     */   {
/* 801 */     JClass g = (JClass)this.generatedRuntime.get(clazz);
/* 802 */     if (g == null)
/*     */     {
/* 804 */       JPackage implPkg = getUsedPackages(Aspect.IMPLEMENTATION)[0].subPackage("runtime");
/* 805 */       g = generateStaticClass(clazz, implPkg);
/* 806 */       this.generatedRuntime.put(clazz, g);
/*     */     }
/* 808 */     return g;
/*     */   }
/*     */ 
/*     */   public JClass generateStaticClass(Class src, JPackage out) {
/* 812 */     String shortName = getShortName(src.getName());
/*     */ 
/* 818 */     URL res = src.getResource(shortName + ".java");
/* 819 */     if (res == null) {
/* 820 */       res = src.getResource(shortName + ".java_");
/*     */     }
/* 822 */     if (res == null) {
/* 823 */       throw new InternalError("Unable to load source code of " + src.getName() + " as a resource");
/*     */     }
/*     */ 
/* 826 */     JStaticJavaFile sjf = new JStaticJavaFile(out, shortName, res, null);
/* 827 */     out.addResourceFile(sjf);
/* 828 */     return sjf.getJClass();
/*     */   }
/*     */ 
/*     */   private String getShortName(String name) {
/* 832 */     return name.substring(name.lastIndexOf('.') + 1);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.BeanGenerator
 * JD-Core Version:    0.6.2
 */