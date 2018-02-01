/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.AbortException;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.SchemaCache;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CPluginCustomization;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.tools.internal.xjc.util.ForkContentHandler;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class BindInfo
/*     */ {
/*     */   protected final ErrorReceiver errorReceiver;
/*     */   final Model model;
/*     */   private final String defaultPackage;
/*     */   final JCodeModel codeModel;
/*     */   final CodeModelClassFactory classFactory;
/*     */   private final Element dom;
/* 136 */   private final Map<String, BIConversion> conversions = new HashMap();
/*     */ 
/* 139 */   private final Map<String, BIElement> elements = new HashMap();
/*     */ 
/* 142 */   private final Map<String, BIInterface> interfaces = new HashMap();
/*     */   private static final String XJC_NS = "http://java.sun.com/xml/ns/jaxb/xjc";
/* 292 */   private static SchemaCache bindingFileSchema = new SchemaCache(BindInfo.class.getResource("bindingfile.xsd"));
/*     */ 
/*     */   public BindInfo(Model model, InputSource source, ErrorReceiver _errorReceiver)
/*     */     throws AbortException
/*     */   {
/*  81 */     this(model, parse(model, source, _errorReceiver), _errorReceiver);
/*     */   }
/*     */ 
/*     */   public BindInfo(Model model, Document _dom, ErrorReceiver _errorReceiver) {
/*  85 */     this.model = model;
/*  86 */     this.dom = _dom.getDocumentElement();
/*  87 */     this.codeModel = model.codeModel;
/*  88 */     this.errorReceiver = _errorReceiver;
/*  89 */     this.classFactory = new CodeModelClassFactory(_errorReceiver);
/*     */ 
/*  92 */     this.defaultPackage = model.options.defaultPackage;
/*     */ 
/*  95 */     model.getCustomizations().addAll(getGlobalCustomizations());
/*     */ 
/*  98 */     for (Element ele : DOMUtil.getChildElements(this.dom, "element")) {
/*  99 */       BIElement e = new BIElement(this, ele);
/* 100 */       this.elements.put(e.name(), e);
/*     */     }
/*     */ 
/* 104 */     BIUserConversion.addBuiltinConversions(this, this.conversions);
/*     */ 
/* 107 */     for (Element cnv : DOMUtil.getChildElements(this.dom, "conversion")) {
/* 108 */       BIConversion c = new BIUserConversion(this, cnv);
/* 109 */       this.conversions.put(c.name(), c);
/*     */     }
/* 111 */     for (Element en : DOMUtil.getChildElements(this.dom, "enumeration")) {
/* 112 */       BIConversion c = BIEnumeration.create(en, this);
/* 113 */       this.conversions.put(c.name(), c);
/*     */     }
/*     */ 
/* 119 */     for (Element itf : DOMUtil.getChildElements(this.dom, "interface")) {
/* 120 */       BIInterface c = new BIInterface(itf);
/* 121 */       this.interfaces.put(c.name(), c);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Long getSerialVersionUID()
/*     */   {
/* 155 */     Element serial = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "serializable");
/* 156 */     if (serial == null) return null;
/*     */ 
/* 158 */     String v = DOMUtil.getAttribute(serial, "uid");
/* 159 */     if (v == null) v = "1";
/* 160 */     return new Long(v);
/*     */   }
/*     */ 
/*     */   public JClass getSuperClass()
/*     */   {
/* 165 */     Element sc = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "superClass");
/* 166 */     if (sc == null) return null;
/*     */ 
/*     */     JDefinedClass c;
/*     */     try
/*     */     {
/* 171 */       String v = DOMUtil.getAttribute(sc, "name");
/* 172 */       if (v == null) return null;
/* 173 */       JDefinedClass c = this.codeModel._class(v);
/* 174 */       c.hide();
/*     */     } catch (JClassAlreadyExistsException e) {
/* 176 */       c = e.getExistingClass();
/*     */     }
/*     */ 
/* 179 */     return c;
/*     */   }
/*     */ 
/*     */   public JClass getSuperInterface()
/*     */   {
/* 184 */     Element sc = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "superInterface");
/* 185 */     if (sc == null) return null;
/*     */ 
/* 187 */     String name = DOMUtil.getAttribute(sc, "name");
/* 188 */     if (name == null) return null;
/*     */ 
/*     */     JDefinedClass c;
/*     */     try
/*     */     {
/* 193 */       JDefinedClass c = this.codeModel._class(name, ClassType.INTERFACE);
/* 194 */       c.hide();
/*     */     } catch (JClassAlreadyExistsException e) {
/* 196 */       c = e.getExistingClass();
/*     */     }
/*     */ 
/* 199 */     return c;
/*     */   }
/*     */ 
/*     */   public JPackage getTargetPackage()
/*     */   {
/* 206 */     if (this.model.options.defaultPackage != null)
/*     */     {
/* 208 */       return this.codeModel._package(this.model.options.defaultPackage);
/*     */     }
/*     */     String p;
/*     */     String p;
/* 211 */     if (this.defaultPackage != null)
/* 212 */       p = this.defaultPackage;
/*     */     else
/* 214 */       p = getOption("package", "");
/* 215 */     return this.codeModel._package(p);
/*     */   }
/*     */ 
/*     */   public BIConversion conversion(String name)
/*     */   {
/* 225 */     BIConversion r = (BIConversion)this.conversions.get(name);
/* 226 */     if (r == null)
/* 227 */       throw new AssertionError("undefined conversion name: this should be checked by the validator before we read it");
/* 228 */     return r;
/*     */   }
/*     */ 
/*     */   public BIElement element(String name)
/*     */   {
/* 239 */     return (BIElement)this.elements.get(name);
/*     */   }
/*     */ 
/*     */   public Collection<BIElement> elements() {
/* 243 */     return this.elements.values();
/*     */   }
/*     */ 
/*     */   public Collection<BIInterface> interfaces()
/*     */   {
/* 248 */     return this.interfaces.values();
/*     */   }
/*     */ 
/*     */   private CCustomizations getGlobalCustomizations()
/*     */   {
/* 255 */     CCustomizations r = null;
/* 256 */     for (Element e : DOMUtil.getChildElements(this.dom)) {
/* 257 */       if (this.model.options.pluginURIs.contains(e.getNamespaceURI()))
/*     */       {
/* 259 */         if (r == null)
/* 260 */           r = new CCustomizations();
/* 261 */         r.add(new CPluginCustomization(e, DOMLocator.getLocationInfo(e)));
/*     */       }
/*     */     }
/* 264 */     if (r == null) r = CCustomizations.EMPTY;
/* 265 */     return new CCustomizations(r);
/*     */   }
/*     */ 
/*     */   private String getOption(String attName, String defaultValue)
/*     */   {
/* 280 */     Element opt = DOMUtil.getElement(this.dom, "options");
/* 281 */     if (opt != null) {
/* 282 */       String s = DOMUtil.getAttribute(opt, attName);
/* 283 */       if (s != null)
/* 284 */         return s;
/*     */     }
/* 286 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   private static Document parse(Model model, InputSource is, ErrorReceiver receiver)
/*     */     throws AbortException
/*     */   {
/*     */     try
/*     */     {
/* 300 */       ValidatorHandler validator = bindingFileSchema.newValidator();
/*     */ 
/* 306 */       SAXParserFactory pf = XmlFactory.createParserFactory(model.options.disableXmlSecurity);
/* 307 */       DocumentBuilderFactory domFactory = XmlFactory.createDocumentBuilderFactory(model.options.disableXmlSecurity);
/* 308 */       DOMBuilder builder = new DOMBuilder(domFactory);
/*     */ 
/* 310 */       ErrorReceiverFilter controller = new ErrorReceiverFilter(receiver);
/* 311 */       validator.setErrorHandler(controller);
/* 312 */       XMLReader reader = pf.newSAXParser().getXMLReader();
/* 313 */       reader.setErrorHandler(controller);
/*     */ 
/* 315 */       DTDExtensionBindingChecker checker = new DTDExtensionBindingChecker("", model.options, controller);
/* 316 */       checker.setContentHandler(validator);
/*     */ 
/* 318 */       reader.setContentHandler(new ForkContentHandler(checker, builder));
/*     */ 
/* 320 */       reader.parse(is);
/*     */ 
/* 322 */       if (controller.hadError()) throw new AbortException();
/* 323 */       return (Document)builder.getDOM();
/*     */     } catch (IOException e) {
/* 325 */       receiver.error(new SAXParseException2(e.getMessage(), null, e));
/*     */     } catch (SAXException e) {
/* 327 */       receiver.error(new SAXParseException2(e.getMessage(), null, e));
/*     */     } catch (ParserConfigurationException e) {
/* 329 */       receiver.error(new SAXParseException2(e.getMessage(), null, e));
/*     */     }
/*     */ 
/* 332 */     throw new AbortException();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.BindInfo
 * JD-Core Version:    0.6.2
 */