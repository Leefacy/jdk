/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.IsSetFieldRenderer;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet.Mode;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.util.XSFinder;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElementRef;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ @XmlRootElement(name="property")
/*     */ public final class BIProperty extends AbstractDeclarationImpl
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/* 106 */   private String name = null;
/*     */ 
/*     */   @XmlElement
/* 110 */   private String javadoc = null;
/*     */ 
/*     */   @XmlElement
/* 114 */   private BaseTypeBean baseType = null;
/*     */ 
/*     */   @XmlAttribute
/* 118 */   private boolean generateFailFastSetterMethod = false;
/*     */ 
/*     */   @XmlAttribute
/* 213 */   private CollectionTypeAttribute collectionType = null;
/*     */ 
/*     */   @XmlAttribute
/* 226 */   private OptionalPropertyMode optionalProperty = null;
/*     */ 
/*     */   @XmlAttribute
/* 241 */   private Boolean generateElementProperty = null;
/*     */ 
/*     */   @XmlAttribute(name="fixedAttributeAsConstantProperty")
/*     */   private Boolean isConstantProperty;
/* 596 */   private final XSFinder hasFixedValue = new XSFinder() {
/*     */     public Boolean attributeDecl(XSAttributeDecl decl) {
/* 598 */       return Boolean.valueOf(decl.getFixedValue() != null);
/*     */     }
/*     */ 
/*     */     public Boolean attributeUse(XSAttributeUse use) {
/* 602 */       return Boolean.valueOf(use.getFixedValue() != null);
/*     */     }
/*     */ 
/*     */     public Boolean schema(XSSchema s)
/*     */     {
/* 608 */       return Boolean.valueOf(true);
/*     */     }
/* 596 */   };
/*     */ 
/* 683 */   private static final XSFunction<XSComponent> defaultCustomizationFinder = new XSFunction()
/*     */   {
/*     */     public XSComponent attributeUse(XSAttributeUse use) {
/* 686 */       return use.getDecl();
/*     */     }
/*     */ 
/*     */     public XSComponent particle(XSParticle particle) {
/* 690 */       return particle.getTerm();
/*     */     }
/*     */ 
/*     */     public XSComponent schema(XSSchema schema)
/*     */     {
/* 695 */       return null;
/*     */     }
/*     */ 
/*     */     public XSComponent attributeDecl(XSAttributeDecl decl) {
/* 699 */       return decl.getOwnerSchema(); } 
/* 700 */     public XSComponent wildcard(XSWildcard wc) { return wc.getOwnerSchema(); } 
/* 701 */     public XSComponent modelGroupDecl(XSModelGroupDecl decl) { return decl.getOwnerSchema(); } 
/* 702 */     public XSComponent modelGroup(XSModelGroup group) { return group.getOwnerSchema(); } 
/* 703 */     public XSComponent elementDecl(XSElementDecl decl) { return decl.getOwnerSchema(); } 
/* 704 */     public XSComponent complexType(XSComplexType type) { return type.getOwnerSchema(); } 
/* 705 */     public XSComponent simpleType(XSSimpleType st) { return st.getOwnerSchema(); }
/*     */ 
/*     */     public XSComponent attGroupDecl(XSAttGroupDecl decl) {
/* 708 */       throw new IllegalStateException(); } 
/* 709 */     public XSComponent empty(XSContentType empty) { throw new IllegalStateException(); } 
/* 710 */     public XSComponent annotation(XSAnnotation xsAnnotation) { throw new IllegalStateException(); } 
/* 711 */     public XSComponent facet(XSFacet xsFacet) { throw new IllegalStateException(); } 
/* 712 */     public XSComponent notation(XSNotation xsNotation) { throw new IllegalStateException(); } 
/* 713 */     public XSComponent identityConstraint(XSIdentityConstraint x) { throw new IllegalStateException(); } 
/* 714 */     public XSComponent xpath(XSXPath xsxPath) { throw new IllegalStateException(); }
/*     */ 
/* 683 */   };
/*     */ 
/* 727 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "property");
/*     */ 
/*     */   public BIProperty(Locator loc, String _propName, String _javadoc, BaseTypeBean _baseType, CollectionTypeAttribute collectionType, Boolean isConst, OptionalPropertyMode optionalProperty, Boolean genElemProp)
/*     */   {
/* 126 */     super(loc);
/*     */ 
/* 128 */     this.name = _propName;
/* 129 */     this.javadoc = _javadoc;
/* 130 */     this.baseType = _baseType;
/* 131 */     this.collectionType = collectionType;
/* 132 */     this.isConstantProperty = isConst;
/* 133 */     this.optionalProperty = optionalProperty;
/* 134 */     this.generateElementProperty = genElemProp;
/*     */   }
/*     */ 
/*     */   protected BIProperty() {
/*     */   }
/*     */ 
/*     */   public Collection<BIDeclaration> getChildren() {
/* 141 */     BIConversion conv = getConv();
/* 142 */     if (conv == null) {
/* 143 */       return super.getChildren();
/*     */     }
/* 145 */     return Collections.singleton(conv);
/*     */   }
/*     */ 
/*     */   public void setParent(BindInfo parent) {
/* 149 */     super.setParent(parent);
/* 150 */     if ((this.baseType != null) && (this.baseType.conv != null))
/* 151 */       this.baseType.conv.setParent(parent);
/*     */   }
/*     */ 
/*     */   public String getPropertyName(boolean forConstant)
/*     */   {
/* 174 */     if (this.name != null) {
/* 175 */       BIGlobalBinding gb = getBuilder().getGlobalBinding();
/* 176 */       NameConverter nc = getBuilder().model.getNameConverter();
/*     */ 
/* 178 */       if ((gb.isJavaNamingConventionEnabled()) && (!forConstant))
/*     */       {
/* 180 */         return nc.toPropertyName(this.name);
/*     */       }
/* 182 */       return this.name;
/*     */     }
/* 184 */     BIProperty next = getDefault();
/* 185 */     if (next != null) return next.getPropertyName(forConstant);
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   public String getJavadoc()
/*     */   {
/* 196 */     return this.javadoc;
/*     */   }
/*     */ 
/*     */   public JType getBaseType()
/*     */   {
/* 201 */     if ((this.baseType != null) && (this.baseType.name != null)) {
/* 202 */       return TypeUtil.getType(getCodeModel(), this.baseType.name, 
/* 204 */         (ErrorReceiver)Ring.get(ErrorReceiver.class), 
/* 204 */         getLocation());
/*     */     }
/* 206 */     BIProperty next = getDefault();
/* 207 */     if (next != null) return next.getBaseType();
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */   CollectionTypeAttribute getCollectionType()
/*     */   {
/* 221 */     if (this.collectionType != null) return this.collectionType;
/* 222 */     return getDefault().getCollectionType();
/*     */   }
/*     */ 
/*     */   @XmlAttribute
/*     */   void setGenerateIsSetMethod(boolean b)
/*     */   {
/* 232 */     this.optionalProperty = (b ? OptionalPropertyMode.ISSET : OptionalPropertyMode.WRAPPER);
/*     */   }
/*     */ 
/*     */   public OptionalPropertyMode getOptionalPropertyMode() {
/* 236 */     if (this.optionalProperty != null) return this.optionalProperty;
/* 237 */     return getDefault().getOptionalPropertyMode();
/*     */   }
/*     */ 
/*     */   private Boolean generateElementProperty()
/*     */   {
/* 248 */     if (this.generateElementProperty != null) return this.generateElementProperty;
/* 249 */     BIProperty next = getDefault();
/* 250 */     if (next != null) return next.generateElementProperty();
/*     */ 
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isConstantProperty()
/*     */   {
/* 272 */     if (this.isConstantProperty != null) return this.isConstantProperty.booleanValue();
/*     */ 
/* 274 */     BIProperty next = getDefault();
/* 275 */     if (next != null) return next.isConstantProperty();
/*     */ 
/* 279 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   public CValuePropertyInfo createValueProperty(String defaultName, boolean forConstant, XSComponent source, TypeUse tu, QName typeName)
/*     */   {
/* 285 */     markAsAcknowledged();
/* 286 */     constantPropertyErrorCheck();
/*     */ 
/* 288 */     String name = getPropertyName(forConstant);
/* 289 */     if (name == null) {
/* 290 */       name = defaultName;
/* 291 */       if ((tu.isCollection()) && (getBuilder().getGlobalBinding().isSimpleMode())) {
/* 292 */         name = JJavaName.getPluralForm(name);
/*     */       }
/*     */     }
/* 295 */     CValuePropertyInfo prop = (CValuePropertyInfo)wrapUp(new CValuePropertyInfo(name, source, getCustomizations(source), source.getLocator(), tu, typeName), source);
/* 296 */     BIInlineBinaryData.handle(source, prop);
/* 297 */     return prop;
/*     */   }
/*     */ 
/*     */   public CAttributePropertyInfo createAttributeProperty(XSAttributeUse use, TypeUse tu)
/*     */   {
/* 304 */     boolean forConstant = (getCustomization(use)
/* 303 */       .isConstantProperty()) && 
/* 304 */       (use
/* 304 */       .getFixedValue() != null);
/*     */ 
/* 306 */     String name = getPropertyName(forConstant);
/* 307 */     if (name == null) {
/* 308 */       NameConverter conv = getBuilder().getNameConverter();
/* 309 */       if (forConstant)
/* 310 */         name = conv.toConstantName(use.getDecl().getName());
/*     */       else
/* 312 */         name = conv.toPropertyName(use.getDecl().getName());
/* 313 */       if ((tu.isCollection()) && (getBuilder().getGlobalBinding().isSimpleMode())) {
/* 314 */         name = JJavaName.getPluralForm(name);
/*     */       }
/*     */     }
/* 317 */     markAsAcknowledged();
/* 318 */     constantPropertyErrorCheck();
/*     */ 
/* 320 */     return (CAttributePropertyInfo)wrapUp(new CAttributePropertyInfo(name, use, getCustomizations(use), use.getLocator(), 
/* 321 */       BGMBuilder.getName(use
/* 321 */       .getDecl()), tu, 
/* 322 */       BGMBuilder.getName(use
/* 322 */       .getDecl().getType()), use.isRequired()), use);
/*     */   }
/*     */ 
/*     */   public CElementPropertyInfo createElementProperty(String defaultName, boolean forConstant, XSParticle source, RawTypeSet types)
/*     */   {
/* 338 */     if (!types.refs.isEmpty())
/*     */     {
/* 341 */       markAsAcknowledged();
/* 342 */     }constantPropertyErrorCheck();
/*     */ 
/* 344 */     String name = getPropertyName(forConstant);
/* 345 */     if (name == null) {
/* 346 */       name = defaultName;
/*     */     }
/* 348 */     CElementPropertyInfo prop = (CElementPropertyInfo)wrapUp(new CElementPropertyInfo(name, types
/* 350 */       .getCollectionMode(), types
/* 351 */       .id(), types
/* 352 */       .getExpectedMimeType(), source, 
/* 353 */       getCustomizations(source), 
/* 353 */       source
/* 354 */       .getLocator(), types.isRequired()), source);
/*     */ 
/* 357 */     types.addTo(prop);
/*     */ 
/* 359 */     BIInlineBinaryData.handle(source.getTerm(), prop);
/* 360 */     return prop;
/*     */   }
/*     */ 
/*     */   public CReferencePropertyInfo createDummyExtendedMixedReferenceProperty(String defaultName, XSComponent source, RawTypeSet types)
/*     */   {
/* 365 */     return createReferenceProperty(defaultName, false, source, types, true, true, false, true);
/*     */   }
/*     */ 
/*     */   public CReferencePropertyInfo createContentExtendedMixedReferenceProperty(String defaultName, XSComponent source, RawTypeSet types)
/*     */   {
/* 378 */     return createReferenceProperty(defaultName, false, source, types, true, false, true, true);
/*     */   }
/*     */ 
/*     */   public CReferencePropertyInfo createReferenceProperty(String defaultName, boolean forConstant, XSComponent source, RawTypeSet types, boolean isMixed, boolean dummy, boolean content, boolean isMixedExtended)
/*     */   {
/* 393 */     if (types == null) {
/* 394 */       content = true;
/*     */     }
/* 396 */     else if (!types.refs.isEmpty())
/*     */     {
/* 399 */       markAsAcknowledged();
/*     */     }
/* 401 */     constantPropertyErrorCheck();
/*     */ 
/* 403 */     String name = getPropertyName(forConstant);
/* 404 */     if (name == null) {
/* 405 */       name = defaultName;
/*     */     }
/* 407 */     CReferencePropertyInfo prop = (CReferencePropertyInfo)wrapUp(new CReferencePropertyInfo(name, types == null, 
/* 410 */       types == null ? false : types
/* 411 */       .isRequired(), isMixed, source, 
/* 414 */       getCustomizations(source), 
/* 414 */       source.getLocator(), dummy, content, isMixedExtended), source);
/*     */ 
/* 416 */     if (types != null) {
/* 417 */       types.addTo(prop);
/*     */     }
/*     */ 
/* 420 */     BIInlineBinaryData.handle(source, prop);
/* 421 */     return prop;
/*     */   }
/*     */ 
/*     */   public CPropertyInfo createElementOrReferenceProperty(String defaultName, boolean forConstant, XSParticle source, RawTypeSet types)
/*     */   {
/*     */     boolean generateRef;
/*     */     boolean generateRef;
/* 430 */     switch (3.$SwitchMap$com$sun$tools$internal$xjc$reader$RawTypeSet$Mode[types.canBeTypeRefs.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/* 434 */       Boolean b = generateElementProperty();
/*     */       boolean generateRef;
/* 435 */       if (b == null)
/* 436 */         generateRef = types.canBeTypeRefs == RawTypeSet.Mode.CAN_BE_TYPEREF;
/*     */       else
/* 438 */         generateRef = b.booleanValue();
/* 439 */       break;
/*     */     case 3:
/* 441 */       generateRef = true;
/* 442 */       break;
/*     */     default:
/* 444 */       throw new AssertionError();
/*     */     }
/*     */     boolean generateRef;
/* 447 */     if (generateRef) {
/* 448 */       return createReferenceProperty(defaultName, forConstant, source, types, false, false, false, false);
/*     */     }
/* 450 */     return createElementProperty(defaultName, forConstant, source, types);
/*     */   }
/*     */ 
/*     */   private <T extends CPropertyInfo> T wrapUp(T prop, XSComponent source)
/*     */   {
/* 458 */     prop.javadoc = concat(this.javadoc, 
/* 459 */       getBuilder().getBindInfo(source).getDocumentation());
/* 460 */     if (prop.javadoc == null) {
/* 461 */       prop.javadoc = "";
/*     */     }
/*     */ 
/* 465 */     OptionalPropertyMode opm = getOptionalPropertyMode();
/*     */     FieldRenderer r;
/*     */     FieldRenderer r;
/* 466 */     if (prop.isCollection()) {
/* 467 */       CollectionTypeAttribute ct = getCollectionType();
/* 468 */       r = ct.get(getBuilder().model);
/*     */     } else {
/* 470 */       FieldRendererFactory frf = getBuilder().fieldRendererFactory;
/*     */ 
/* 472 */       if (prop.isOptionalPrimitive())
/*     */       {
/*     */         FieldRenderer r;
/*     */         FieldRenderer r;
/*     */         FieldRenderer r;
/* 474 */         switch (3.$SwitchMap$com$sun$tools$internal$xjc$reader$xmlschema$bindinfo$OptionalPropertyMode[opm.ordinal()]) {
/*     */         case 1:
/* 476 */           r = frf.getRequiredUnboxed();
/* 477 */           break;
/*     */         case 2:
/* 480 */           r = frf.getSingle();
/* 481 */           break;
/*     */         case 3:
/* 483 */           r = frf.getSinglePrimitiveAccess();
/* 484 */           break;
/*     */         default:
/* 486 */           throw new Error();
/*     */         }
/*     */       } else {
/* 489 */         r = frf.getDefault();
/*     */       }
/*     */     }
/* 492 */     if (opm == OptionalPropertyMode.ISSET)
/*     */     {
/* 499 */       r = new IsSetFieldRenderer(r, (prop.isOptionalPrimitive()) || (prop.isCollection()), true);
/*     */     }
/*     */ 
/* 502 */     prop.realization = r;
/*     */ 
/* 504 */     JType bt = getBaseType();
/* 505 */     if (bt != null) {
/* 506 */       prop.baseType = bt;
/*     */     }
/* 508 */     return prop;
/*     */   }
/*     */ 
/*     */   private CCustomizations getCustomizations(XSComponent src) {
/* 512 */     return getBuilder().getBindInfo(src).toCustomizationList();
/*     */   }
/*     */ 
/*     */   private CCustomizations getCustomizations(XSComponent[] src) {
/* 516 */     CCustomizations c = null;
/* 517 */     for (XSComponent s : src) {
/* 518 */       CCustomizations r = getCustomizations(s);
/* 519 */       if (c == null) c = r; else
/* 520 */         c = CCustomizations.merge(c, r);
/*     */     }
/* 522 */     return c;
/*     */   }
/*     */ 
/*     */   private CCustomizations getCustomizations(XSAttributeUse src)
/*     */   {
/* 534 */     if (src.getDecl().isLocal()) {
/* 535 */       return getCustomizations(new XSComponent[] { src, src.getDecl() });
/*     */     }
/* 537 */     return getCustomizations(src);
/*     */   }
/*     */ 
/*     */   private CCustomizations getCustomizations(XSParticle src)
/*     */   {
/* 550 */     if (src.getTerm().isElementDecl()) {
/* 551 */       XSElementDecl xed = src.getTerm().asElementDecl();
/* 552 */       if (xed.isGlobal()) {
/* 553 */         return getCustomizations(src);
/*     */       }
/*     */     }
/* 556 */     return getCustomizations(new XSComponent[] { src, src.getTerm() });
/*     */   }
/*     */ 
/*     */   public void markAsAcknowledged()
/*     */   {
/* 562 */     if (isAcknowledged()) return;
/*     */ 
/* 565 */     super.markAsAcknowledged();
/*     */ 
/* 567 */     BIProperty def = getDefault();
/* 568 */     if (def != null) def.markAsAcknowledged(); 
/*     */   }
/*     */ 
/*     */   private void constantPropertyErrorCheck()
/*     */   {
/* 572 */     if ((this.isConstantProperty != null) && (getOwner() != null))
/*     */     {
/* 581 */       if (!this.hasFixedValue.find(getOwner())) {
/* 582 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(
/* 583 */           getLocation(), Messages.ERR_ILLEGAL_FIXEDATTR
/* 584 */           .format(new Object[0]));
/*     */ 
/* 587 */         this.isConstantProperty = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected BIProperty getDefault()
/*     */   {
/* 621 */     if (getOwner() == null) return null;
/* 622 */     BIProperty next = getDefault(getBuilder(), getOwner());
/* 623 */     if (next == this) return null;
/* 624 */     return next;
/*     */   }
/*     */ 
/*     */   private static BIProperty getDefault(BGMBuilder builder, XSComponent c) {
/* 628 */     while (c != null) {
/* 629 */       c = (XSComponent)c.apply(defaultCustomizationFinder);
/* 630 */       if (c != null) {
/* 631 */         BIProperty prop = (BIProperty)builder.getBindInfo(c).get(BIProperty.class);
/* 632 */         if (prop != null) return prop;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 637 */     return builder.getGlobalBinding().getDefaultProperty();
/*     */   }
/*     */ 
/*     */   public static BIProperty getCustomization(XSComponent c)
/*     */   {
/* 671 */     BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/* 674 */     if (c != null) {
/* 675 */       BIProperty prop = (BIProperty)builder.getBindInfo(c).get(BIProperty.class);
/* 676 */       if (prop != null) return prop;
/*     */ 
/*     */     }
/*     */ 
/* 680 */     return getDefault(builder, c);
/*     */   }
/*     */ 
/*     */   private static String concat(String s1, String s2)
/*     */   {
/* 719 */     if (s1 == null) return s2;
/* 720 */     if (s2 == null) return s1;
/* 721 */     return s1 + "\n\n" + s2;
/*     */   }
/*     */   public QName getName() {
/* 724 */     return NAME;
/*     */   }
/*     */ 
/*     */   public BIConversion getConv()
/*     */   {
/* 731 */     if (this.baseType != null) {
/* 732 */       return this.baseType.conv;
/*     */     }
/* 734 */     return null;
/*     */   }
/*     */ 
/*     */   private static final class BaseTypeBean
/*     */   {
/*     */ 
/*     */     @XmlElementRef
/*     */     BIConversion conv;
/*     */ 
/*     */     @XmlAttribute
/*     */     String name;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty
 * JD-Core Version:    0.6.2
 */