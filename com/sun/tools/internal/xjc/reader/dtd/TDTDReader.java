/*     */ package com.sun.tools.internal.xjc.reader.dtd;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.AbortException;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CDefaultValue;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.internal.xjc.reader.ModelChecker;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIAttribute;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIInterface;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.dtdparser.DTDHandlerBase;
/*     */ import com.sun.xml.internal.dtdparser.DTDParser;
/*     */ import com.sun.xml.internal.dtdparser.InputEntity;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Stack;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class TDTDReader extends DTDHandlerBase
/*     */ {
/*     */   private final EntityResolver entityResolver;
/*     */   final BindInfo bindInfo;
/* 160 */   final Model model = (Model)Ring.get(Model.class);
/*     */   private final CodeModelClassFactory classFactory;
/*     */   private final ErrorReceiverFilter errorReceiver;
/* 169 */   private final Map<String, Element> elements = new HashMap();
/*     */ 
/* 361 */   private final Stack<ModelGroup> modelGroups = new Stack();
/*     */   private Locator locator;
/* 437 */   private static final Map<String, TypeUse> builtinConversions = Collections.unmodifiableMap(m);
/*     */ 
/*     */   public static Model parse(InputSource dtd, InputSource bindingInfo, ErrorReceiver errorReceiver, Options opts)
/*     */   {
/*     */     try
/*     */     {
/*  99 */       Ring old = Ring.begin();
/*     */       try {
/* 101 */         ErrorReceiverFilter ef = new ErrorReceiverFilter(errorReceiver);
/*     */ 
/* 103 */         JCodeModel cm = new JCodeModel();
/* 104 */         Model model = new Model(opts, cm, NameConverter.standard, opts.classNameAllocator, null);
/*     */ 
/* 106 */         Ring.add(cm);
/* 107 */         Ring.add(model);
/* 108 */         Ring.add(ErrorReceiver.class, ef);
/*     */ 
/* 110 */         TDTDReader reader = new TDTDReader(ef, opts, bindingInfo);
/*     */ 
/* 112 */         DTDParser parser = new DTDParser();
/* 113 */         parser.setDtdHandler(reader);
/* 114 */         if (opts.entityResolver != null)
/* 115 */           parser.setEntityResolver(opts.entityResolver);
/*     */         try
/*     */         {
/* 118 */           parser.parse(dtd);
/*     */         } catch (SAXParseException e) {
/* 120 */           return null;
/*     */         }
/*     */ 
/* 123 */         ((ModelChecker)Ring.get(ModelChecker.class)).check();
/*     */ 
/* 125 */         if (ef.hadError()) return null;
/* 126 */         return model;
/*     */       } finally {
/* 128 */         Ring.end(old);
/*     */       }
/*     */     } catch (IOException e) {
/* 131 */       errorReceiver.error(new SAXParseException2(e.getMessage(), null, e));
/* 132 */       return null;
/*     */     } catch (SAXException e) {
/* 134 */       errorReceiver.error(new SAXParseException2(e.getMessage(), null, e));
/* 135 */       return null;
/*     */     } catch (AbortException e) {
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   protected TDTDReader(ErrorReceiver errorReceiver, Options opts, InputSource _bindInfo) throws AbortException
/*     */   {
/* 143 */     this.entityResolver = opts.entityResolver;
/* 144 */     this.errorReceiver = new ErrorReceiverFilter(errorReceiver);
/* 145 */     this.bindInfo = new BindInfo(this.model, _bindInfo, this.errorReceiver);
/* 146 */     this.classFactory = new CodeModelClassFactory(errorReceiver);
/*     */   }
/*     */ 
/*     */   public void startDTD(InputEntity entity)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDTD()
/*     */     throws SAXException
/*     */   {
/* 180 */     for (Element e : this.elements.values()) {
/* 181 */       e.bind();
/*     */     }
/*     */ 
/* 184 */     if (this.errorReceiver.hadError()) {
/* 185 */       return;
/*     */     }
/* 187 */     processInterfaceDeclarations();
/*     */ 
/* 190 */     this.model.serialVersionUID = this.bindInfo.getSerialVersionUID();
/* 191 */     if (this.model.serialVersionUID != null)
/* 192 */       this.model.serializable = true;
/* 193 */     this.model.rootClass = this.bindInfo.getSuperClass();
/* 194 */     this.model.rootInterface = this.bindInfo.getSuperInterface();
/*     */ 
/* 202 */     processConstructorDeclarations();
/*     */   }
/*     */ 
/*     */   private void processInterfaceDeclarations()
/*     */   {
/* 209 */     Map fromName = new HashMap();
/*     */ 
/* 212 */     Map decls = new HashMap();
/*     */ 
/* 214 */     for (BIInterface decl : this.bindInfo.interfaces()) {
/* 215 */       final JDefinedClass intf = this.classFactory.createInterface(this.bindInfo
/* 216 */         .getTargetPackage(), decl.name(), copyLocator());
/* 217 */       decls.put(decl, intf);
/* 218 */       fromName.put(decl.name(), new InterfaceAcceptor() {
/*     */         public void implement(JClass c) {
/* 220 */           intf._implements(c);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 225 */     for (final CClassInfo ci : this.model.beans().values()) {
/* 226 */       fromName.put(ci.getName(), new InterfaceAcceptor() {
/*     */         public void implement(JClass c) {
/* 228 */           ci._implements(c);
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */ 
/* 235 */     for (Map.Entry e : decls.entrySet()) {
/* 236 */       BIInterface decl = (BIInterface)e.getKey();
/* 237 */       JClass c = (JClass)e.getValue();
/*     */ 
/* 239 */       for (String member : decl.members()) {
/* 240 */         InterfaceAcceptor acc = (InterfaceAcceptor)fromName.get(member);
/* 241 */         if (acc == null)
/*     */         {
/* 244 */           error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentInterfaceMember", new Object[] { member });
/*     */         }
/*     */         else
/*     */         {
/* 250 */           acc.implement(c);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   JPackage getTargetPackage()
/*     */   {
/* 263 */     return this.bindInfo.getTargetPackage();
/*     */   }
/*     */ 
/*     */   private void processConstructorDeclarations()
/*     */   {
/* 276 */     for (BIElement decl : this.bindInfo.elements()) {
/* 277 */       Element e = (Element)this.elements.get(decl.name());
/* 278 */       if (e == null) {
/* 279 */         error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentElementDeclaration", new Object[] { decl
/* 280 */           .name() });
/*     */       }
/* 284 */       else if (decl.isClass())
/*     */       {
/* 288 */         decl.declareConstructors(e.getClassInfo());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 293 */   public void attributeDecl(String elementName, String attributeName, String attributeType, String[] enumeration, short attributeUse, String defaultValue) throws SAXException { getOrCreateElement(elementName).attributes.add(
/* 294 */       createAttribute(elementName, attributeName, attributeType, enumeration, attributeUse, defaultValue));
/*     */   }
/*     */ 
/*     */   protected CPropertyInfo createAttribute(String elementName, String attributeName, String attributeType, String[] enums, short attributeUse, String defaultValue)
/*     */     throws SAXException
/*     */   {
/* 303 */     boolean required = attributeUse == 3;
/*     */ 
/* 306 */     BIElement edecl = this.bindInfo.element(elementName);
/* 307 */     BIAttribute decl = null;
/* 308 */     if (edecl != null) decl = edecl.attribute(attributeName);
/* 311 */     String propName;
/*     */     String propName;
/* 311 */     if (decl == null) propName = this.model.getNameConverter().toPropertyName(attributeName); else {
/* 312 */       propName = decl.getPropertyName();
/*     */     }
/* 314 */     QName qname = new QName("", attributeName);
/*     */     TypeUse use;
/*     */     TypeUse use;
/* 320 */     if ((decl != null) && (decl.getConversion() != null))
/* 321 */       use = decl.getConversion().getTransducer();
/*     */     else {
/* 323 */       use = (TypeUse)builtinConversions.get(attributeType);
/*     */     }
/*     */ 
/* 326 */     CPropertyInfo r = new CAttributePropertyInfo(propName, null, null, 
/* 326 */       copyLocator(), qname, use, null, required);
/*     */ 
/* 328 */     if (defaultValue != null) {
/* 329 */       r.defaultValue = CDefaultValue.create(use, new XmlString(defaultValue));
/*     */     }
/* 331 */     return r;
/*     */   }
/*     */ 
/*     */   Element getOrCreateElement(String elementName)
/*     */   {
/* 337 */     Element r = (Element)this.elements.get(elementName);
/* 338 */     if (r == null) {
/* 339 */       r = new Element(this, elementName);
/* 340 */       this.elements.put(elementName, r);
/*     */     }
/*     */ 
/* 343 */     return r;
/*     */   }
/*     */ 
/*     */   public void startContentModel(String elementName, short contentModelType) throws SAXException
/*     */   {
/* 348 */     assert (this.modelGroups.isEmpty());
/* 349 */     this.modelGroups.push(new ModelGroup());
/*     */   }
/*     */ 
/*     */   public void endContentModel(String elementName, short contentModelType) throws SAXException {
/* 353 */     assert (this.modelGroups.size() == 1);
/* 354 */     Term term = ((ModelGroup)this.modelGroups.pop()).wrapUp();
/*     */ 
/* 356 */     Element e = getOrCreateElement(elementName);
/* 357 */     e.define(contentModelType, term, copyLocator());
/*     */   }
/*     */ 
/*     */   public void startModelGroup()
/*     */     throws SAXException
/*     */   {
/* 364 */     this.modelGroups.push(new ModelGroup());
/*     */   }
/*     */ 
/*     */   public void endModelGroup(short occurence) throws SAXException {
/* 368 */     Term t = Occurence.wrap(((ModelGroup)this.modelGroups.pop()).wrapUp(), occurence);
/* 369 */     ((ModelGroup)this.modelGroups.peek()).addTerm(t);
/*     */   }
/*     */ 
/*     */   public void connector(short connectorType) throws SAXException {
/* 373 */     ((ModelGroup)this.modelGroups.peek()).setKind(connectorType);
/*     */   }
/*     */ 
/*     */   public void childElement(String elementName, short occurence)
/*     */     throws SAXException
/*     */   {
/* 380 */     Element child = getOrCreateElement(elementName);
/* 381 */     ((ModelGroup)this.modelGroups.peek()).addTerm(Occurence.wrap(child, occurence));
/* 382 */     child.isReferenced = true;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator loc)
/*     */   {
/* 398 */     this.locator = loc;
/*     */   }
/*     */ 
/*     */   private Locator copyLocator()
/*     */   {
/* 405 */     return new LocatorImpl(this.locator);
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/* 447 */     this.errorReceiver.error(e);
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 451 */     this.errorReceiver.fatalError(e);
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 455 */     this.errorReceiver.warning(e);
/*     */   }
/*     */ 
/*     */   protected final void error(Locator loc, String prop, Object[] args) {
/* 459 */     this.errorReceiver.error(loc, Messages.format(prop, args));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 425 */     Map m = new HashMap();
/*     */ 
/* 427 */     m.put("CDATA", CBuiltinLeafInfo.NORMALIZED_STRING);
/* 428 */     m.put("ENTITY", CBuiltinLeafInfo.TOKEN);
/* 429 */     m.put("ENTITIES", CBuiltinLeafInfo.STRING.makeCollection());
/* 430 */     m.put("NMTOKEN", CBuiltinLeafInfo.TOKEN);
/* 431 */     m.put("NMTOKENS", CBuiltinLeafInfo.STRING.makeCollection());
/* 432 */     m.put("ID", CBuiltinLeafInfo.ID);
/* 433 */     m.put("IDREF", CBuiltinLeafInfo.IDREF);
/* 434 */     m.put("IDREFS", TypeUseFactory.makeCollection(CBuiltinLeafInfo.IDREF));
/* 435 */     m.put("ENUMERATION", CBuiltinLeafInfo.TOKEN);
/*     */   }
/*     */ 
/*     */   private static abstract interface InterfaceAcceptor
/*     */   {
/*     */     public abstract void implement(JClass paramJClass);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.TDTDReader
 * JD-Core Version:    0.6.2
 */