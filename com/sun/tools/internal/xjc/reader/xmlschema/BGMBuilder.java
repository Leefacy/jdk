/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.fmt.JTextFile;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent.Package;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.ModelChecker;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDom;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BISerializable;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.util.XSFinder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class BGMBuilder extends BindingComponent
/*     */ {
/*     */   public final boolean inExtensionMode;
/*     */   public final String defaultPackage1;
/*     */   public final String defaultPackage2;
/* 132 */   private final BindGreen green = (BindGreen)Ring.get(BindGreen.class);
/* 133 */   private final BindPurple purple = (BindPurple)Ring.get(BindPurple.class);
/*     */ 
/* 135 */   public final Model model = (Model)Ring.get(Model.class);
/*     */   public final FieldRendererFactory fieldRendererFactory;
/*     */   private RefererFinder refFinder;
/*     */   private List<Plugin> activePlugins;
/*     */   private BIGlobalBinding globalBinding;
/*     */   private ParticleBinder particleBinder;
/* 389 */   private final BindInfo emptyBindInfo = new BindInfo();
/*     */ 
/* 432 */   private final Map<XSComponent, BindInfo> externalBindInfos = new HashMap();
/*     */ 
/* 462 */   private final XSFinder toPurple = new XSFinder()
/*     */   {
/*     */     public Boolean attributeUse(XSAttributeUse use)
/*     */     {
/* 466 */       return Boolean.valueOf(true);
/*     */     }
/*     */ 
/*     */     public Boolean simpleType(XSSimpleType xsSimpleType)
/*     */     {
/* 472 */       return Boolean.valueOf(true);
/*     */     }
/*     */ 
/*     */     public Boolean wildcard(XSWildcard xsWildcard)
/*     */     {
/* 479 */       return Boolean.valueOf(true);
/*     */     }
/* 462 */   };
/*     */   private Transformer identityTransformer;
/*     */ 
/*     */   public static Model build(XSSchemaSet _schemas, JCodeModel codeModel, ErrorReceiver _errorReceiver, Options opts)
/*     */   {
/*  91 */     Ring old = Ring.begin();
/*     */     try {
/*  93 */       ErrorReceiverFilter ef = new ErrorReceiverFilter(_errorReceiver);
/*     */ 
/*  95 */       Ring.add(XSSchemaSet.class, _schemas);
/*  96 */       Ring.add(codeModel);
/*  97 */       Model model = new Model(opts, codeModel, null, opts.classNameAllocator, _schemas);
/*  98 */       Ring.add(model);
/*  99 */       Ring.add(ErrorReceiver.class, ef);
/* 100 */       Ring.add(CodeModelClassFactory.class, new CodeModelClassFactory(ef));
/*     */ 
/* 103 */       BGMBuilder builder = new BGMBuilder(opts.defaultPackage, opts.defaultPackage2, opts
/* 103 */         .isExtensionMode(), opts.getFieldRendererFactory(), opts.activePlugins);
/* 104 */       builder._build();
/*     */       Model localModel1;
/* 106 */       if (ef.hadError()) return null;
/* 107 */       return model;
/*     */     } finally {
/* 109 */       Ring.end(old);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected BGMBuilder(String defaultPackage1, String defaultPackage2, boolean _inExtensionMode, FieldRendererFactory fieldRendererFactory, List<Plugin> activePlugins)
/*     */   {
/* 151 */     this.inExtensionMode = _inExtensionMode;
/* 152 */     this.defaultPackage1 = defaultPackage1;
/* 153 */     this.defaultPackage2 = defaultPackage2;
/* 154 */     this.fieldRendererFactory = fieldRendererFactory;
/* 155 */     this.activePlugins = activePlugins;
/* 156 */     promoteGlobalBindings();
/*     */   }
/*     */ 
/*     */   private void _build()
/*     */   {
/* 161 */     buildContents();
/* 162 */     getClassSelector().executeTasks();
/*     */ 
/* 166 */     ((UnusedCustomizationChecker)Ring.get(UnusedCustomizationChecker.class)).run();
/*     */ 
/* 168 */     ((ModelChecker)Ring.get(ModelChecker.class)).check();
/*     */ 
/* 170 */     for (Plugin ma : this.activePlugins)
/* 171 */       ma.postProcessModel(this.model, (ErrorHandler)Ring.get(ErrorReceiver.class));
/*     */   }
/*     */ 
/*     */   private void promoteGlobalBindings()
/*     */   {
/* 179 */     XSSchemaSet schemas = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/*     */ 
/* 181 */     for (XSSchema s : schemas.getSchemas()) {
/* 182 */       BindInfo bi = getBindInfo(s);
/*     */ 
/* 185 */       this.model.getCustomizations().addAll(bi.toCustomizationList());
/*     */ 
/* 187 */       BIGlobalBinding gb = (BIGlobalBinding)bi.get(BIGlobalBinding.class);
/* 188 */       if (gb != null)
/*     */       {
/* 191 */         gb.markAsAcknowledged();
/*     */ 
/* 193 */         if (this.globalBinding == null) {
/* 194 */           this.globalBinding = gb;
/*     */         }
/* 196 */         else if (!this.globalBinding.isEqual(gb))
/*     */         {
/* 200 */           getErrorReporter().error(gb.getLocation(), "ERR_MULTIPLE_GLOBAL_BINDINGS", new Object[0]);
/*     */ 
/* 202 */           getErrorReporter().error(this.globalBinding.getLocation(), "ERR_MULTIPLE_GLOBAL_BINDINGS_OTHER", new Object[0]);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 208 */     if (this.globalBinding == null)
/*     */     {
/* 211 */       this.globalBinding = new BIGlobalBinding();
/* 212 */       BindInfo big = new BindInfo();
/* 213 */       big.addDecl(this.globalBinding);
/* 214 */       big.setOwner(this, null);
/*     */     }
/*     */ 
/* 218 */     this.model.strategy = this.globalBinding.getCodeGenerationStrategy();
/* 219 */     this.model.rootClass = this.globalBinding.getSuperClass();
/* 220 */     this.model.rootInterface = this.globalBinding.getSuperInterface();
/*     */ 
/* 222 */     this.particleBinder = (this.globalBinding.isSimpleMode() ? new ExpressionParticleBinder() : new DefaultParticleBinder());
/*     */ 
/* 225 */     BISerializable serial = this.globalBinding.getSerializable();
/* 226 */     if (serial != null) {
/* 227 */       this.model.serializable = true;
/* 228 */       this.model.serialVersionUID = serial.uid;
/*     */     }
/*     */ 
/* 232 */     if (this.globalBinding.nameConverter != null) {
/* 233 */       this.model.setNameConverter(this.globalBinding.nameConverter);
/*     */     }
/*     */ 
/* 236 */     this.globalBinding.dispatchGlobalConversions(schemas);
/*     */ 
/* 238 */     this.globalBinding.errorCheck();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public BIGlobalBinding getGlobalBinding()
/*     */   {
/* 252 */     return this.globalBinding;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public ParticleBinder getParticleBinder()
/*     */   {
/* 260 */     return this.particleBinder;
/*     */   }
/*     */ 
/*     */   public NameConverter getNameConverter()
/*     */   {
/* 274 */     return this.model.getNameConverter();
/*     */   }
/*     */ 
/*     */   private void buildContents() {
/* 278 */     ClassSelector cs = getClassSelector();
/* 279 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*     */ 
/* 281 */     for (XSSchema s : ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getSchemas()) {
/* 282 */       BISchemaBinding sb = (BISchemaBinding)getBindInfo(s).get(BISchemaBinding.class);
/*     */ 
/* 284 */       if ((sb != null) && (!sb.map)) {
/* 285 */         sb.markAsAcknowledged();
/*     */       }
/*     */       else
/*     */       {
/* 289 */         getClassSelector().pushClassScope(new CClassInfoParent.Package(
/* 290 */           getClassSelector().getPackage(s.getTargetNamespace())));
/*     */ 
/* 292 */         checkMultipleSchemaBindings(s);
/* 293 */         processPackageJavadoc(s);
/* 294 */         populate(s.getAttGroupDecls(), s);
/* 295 */         populate(s.getAttributeDecls(), s);
/* 296 */         populate(s.getElementDecls(), s);
/* 297 */         populate(s.getModelGroupDecls(), s);
/*     */ 
/* 300 */         for (XSType t : s.getTypes().values()) {
/* 301 */           stb.refererStack.push(t);
/* 302 */           this.model.typeUses().put(getName(t), cs.bindToType(t, s));
/* 303 */           stb.refererStack.pop();
/*     */         }
/*     */ 
/* 306 */         getClassSelector().popClassScope();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkMultipleSchemaBindings(XSSchema schema) {
/* 312 */     ArrayList locations = new ArrayList();
/*     */ 
/* 314 */     BindInfo bi = getBindInfo(schema);
/* 315 */     for (BIDeclaration bid : bi) {
/* 316 */       if (bid.getName() == BISchemaBinding.NAME)
/* 317 */         locations.add(bid.getLocation());
/*     */     }
/* 319 */     if (locations.size() <= 1) return;
/*     */ 
/* 322 */     getErrorReporter().error((Locator)locations.get(0), "BGMBuilder.MultipleSchemaBindings", new Object[] { schema
/* 324 */       .getTargetNamespace() });
/* 325 */     for (int i = 1; i < locations.size(); i++)
/* 326 */       getErrorReporter().error((Locator)locations.get(i), "BGMBuilder.MultipleSchemaBindings.Location", new Object[0]);
/*     */   }
/*     */ 
/*     */   private void populate(Map<String, ? extends XSComponent> col, XSSchema schema)
/*     */   {
/* 335 */     ClassSelector cs = getClassSelector();
/* 336 */     for (XSComponent sc : col.values())
/* 337 */       cs.bindToType(sc, schema);
/*     */   }
/*     */ 
/*     */   private void processPackageJavadoc(XSSchema s)
/*     */   {
/* 346 */     BISchemaBinding cust = (BISchemaBinding)getBindInfo(s).get(BISchemaBinding.class);
/* 347 */     if (cust == null) return;
/*     */ 
/* 349 */     cust.markAsAcknowledged();
/* 350 */     if (cust.getJavadoc() == null) return;
/*     */ 
/* 353 */     JTextFile html = new JTextFile("package.html");
/* 354 */     html.setContents(cust.getJavadoc());
/* 355 */     getClassSelector().getPackage(s.getTargetNamespace()).addResourceFile(html);
/*     */   }
/*     */ 
/*     */   public BindInfo getOrCreateBindInfo(XSComponent schemaComponent)
/*     */   {
/* 373 */     BindInfo bi = _getBindInfoReadOnly(schemaComponent);
/* 374 */     if (bi != null) return bi;
/*     */ 
/* 379 */     bi = new BindInfo();
/* 380 */     bi.setOwner(this, schemaComponent);
/* 381 */     this.externalBindInfos.put(schemaComponent, bi);
/* 382 */     return bi;
/*     */   }
/*     */ 
/*     */   public BindInfo getBindInfo(XSComponent schemaComponent)
/*     */   {
/* 400 */     BindInfo bi = _getBindInfoReadOnly(schemaComponent);
/* 401 */     if (bi != null) return bi;
/* 402 */     return this.emptyBindInfo;
/*     */   }
/*     */ 
/*     */   private BindInfo _getBindInfoReadOnly(XSComponent schemaComponent)
/*     */   {
/* 413 */     BindInfo bi = (BindInfo)this.externalBindInfos.get(schemaComponent);
/* 414 */     if (bi != null) return bi;
/*     */ 
/* 416 */     XSAnnotation annon = schemaComponent.getAnnotation();
/* 417 */     if (annon != null) {
/* 418 */       bi = (BindInfo)annon.getAnnotation();
/* 419 */       if (bi != null) {
/* 420 */         if (bi.getOwner() == null)
/* 421 */           bi.setOwner(this, schemaComponent);
/* 422 */         return bi;
/*     */       }
/*     */     }
/*     */ 
/* 426 */     return null;
/*     */   }
/*     */ 
/*     */   protected final BIDom getLocalDomCustomization(XSParticle p)
/*     */   {
/* 438 */     if (p == null) {
/* 439 */       return null;
/*     */     }
/* 441 */     BIDom dom = (BIDom)getBindInfo(p).get(BIDom.class);
/* 442 */     if (dom != null) return dom;
/*     */ 
/* 445 */     dom = (BIDom)getBindInfo(p.getTerm()).get(BIDom.class);
/* 446 */     if (dom != null) return dom;
/*     */ 
/* 448 */     XSTerm t = p.getTerm();
/*     */ 
/* 450 */     if (t.isElementDecl()) {
/* 451 */       return (BIDom)getBindInfo(t.asElementDecl().getType()).get(BIDom.class);
/*     */     }
/* 453 */     if (t.isModelGroupDecl()) {
/* 454 */       return (BIDom)getBindInfo(t.asModelGroupDecl().getModelGroup()).get(BIDom.class);
/*     */     }
/* 456 */     return null;
/*     */   }
/*     */ 
/*     */   public void ying(XSComponent sc, @Nullable XSComponent referer)
/*     */   {
/* 489 */     if ((((Boolean)sc.apply(this.toPurple)).booleanValue() == true) || (getClassSelector().bindToType(sc, referer) != null))
/* 490 */       sc.visit(this.purple);
/*     */     else
/* 492 */       sc.visit(this.green);
/*     */   }
/*     */ 
/*     */   public Transformer getIdentityTransformer()
/*     */   {
/*     */     try
/*     */     {
/* 502 */       if (this.identityTransformer == null) {
/* 503 */         TransformerFactory tf = XmlFactory.createTransformerFactory(this.model.options.disableXmlSecurity);
/* 504 */         this.identityTransformer = tf.newTransformer();
/*     */       }
/* 506 */       return this.identityTransformer;
/*     */     } catch (TransformerConfigurationException e) {
/* 508 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<XSComponent> getReferer(XSType c)
/*     */   {
/* 516 */     if (this.refFinder == null) {
/* 517 */       this.refFinder = new RefererFinder();
/* 518 */       this.refFinder.schemaSet((XSSchemaSet)Ring.get(XSSchemaSet.class));
/*     */     }
/* 520 */     return this.refFinder.getReferer(c);
/*     */   }
/*     */ 
/*     */   public static QName getName(XSDeclaration decl)
/*     */   {
/* 529 */     String local = decl.getName();
/* 530 */     if (local == null) return null;
/* 531 */     return new QName(decl.getTargetNamespace(), local);
/*     */   }
/*     */ 
/*     */   public String deriveName(String name, XSComponent comp)
/*     */   {
/* 548 */     XSSchema owner = comp.getOwnerSchema();
/*     */ 
/* 550 */     name = getNameConverter().toClassName(name);
/*     */ 
/* 552 */     if (owner != null) {
/* 553 */       BISchemaBinding sb = (BISchemaBinding)getBindInfo(owner).get(BISchemaBinding.class);
/*     */ 
/* 555 */       if (sb != null) name = sb.mangleClassName(name, comp);
/*     */     }
/*     */ 
/* 558 */     return name;
/*     */   }
/*     */ 
/*     */   public boolean isGenerateMixedExtensions() {
/* 562 */     if (this.globalBinding != null) {
/* 563 */       return this.globalBinding.isGenerateMixedExtensions();
/*     */     }
/* 565 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder
 * JD-Core Version:    0.6.2
 */