/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.ExtensionBindingChecker;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.TDTDReader;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForestScanner;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.SCDBasedBindingSet;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.VersionChecker;
/*     */ import com.sun.tools.internal.xjc.reader.relaxng.RELAXNGCompiler;
/*     */ import com.sun.tools.internal.xjc.reader.relaxng.RELAXNGInternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.AnnotationParserFactoryImpl;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.CustomizationContextChecker;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.IncorrectNamespaceURIChecker;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.SchemaConstraintChecker;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.util.CheckingSchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DSchemaBuilderImpl;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import com.sun.xml.internal.rngom.parse.compact.CompactParseable;
/*     */ import com.sun.xml.internal.rngom.parse.xml.SAXParseable;
/*     */ import com.sun.xml.internal.rngom.xml.sax.XMLReaderCreator;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import com.sun.xml.internal.xsom.parser.XSOMParser;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLFilter;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public final class ModelLoader
/*     */ {
/*     */   private final Options opt;
/*     */   private final ErrorReceiverFilter errorReceiver;
/*     */   private final JCodeModel codeModel;
/*     */   private SCDBasedBindingSet scdBasedBindingSet;
/*     */ 
/*     */   public static Model load(Options opt, JCodeModel codeModel, ErrorReceiver er)
/*     */   {
/* 104 */     return new ModelLoader(opt, codeModel, er).load();
/*     */   }
/*     */ 
/*     */   public ModelLoader(Options _opt, JCodeModel _codeModel, ErrorReceiver er)
/*     */   {
/* 109 */     this.opt = _opt;
/* 110 */     this.codeModel = _codeModel;
/* 111 */     this.errorReceiver = new ErrorReceiverFilter(er);
/*     */   }
/*     */ 
/*     */   private Model load()
/*     */   {
/* 118 */     if (!sanityCheck())
/* 119 */       return null;
/*     */     try
/*     */     {
/*     */       Model grammar;
/*     */       Model grammar;
/*     */       Model grammar;
/*     */       Model grammar;
/*     */       Model grammar;
/* 123 */       switch (4.$SwitchMap$com$sun$tools$internal$xjc$Language[this.opt.getSchemaLanguage().ordinal()])
/*     */       {
/*     */       case 1:
/* 126 */         InputSource bindFile = null;
/* 127 */         if (this.opt.getBindFiles().length > 0) {
/* 128 */           bindFile = this.opt.getBindFiles()[0];
/*     */         }
/* 130 */         if (bindFile == null)
/*     */         {
/* 132 */           bindFile = new InputSource(new StringReader("<?xml version='1.0'?><xml-java-binding-schema><options package='" + (this.opt.defaultPackage == null ? "generated" : this.opt.defaultPackage) + "'/></xml-java-binding-schema>"));
/*     */         }
/*     */ 
/* 140 */         checkTooManySchemaErrors();
/* 141 */         grammar = loadDTD(this.opt.getGrammars()[0], bindFile);
/* 142 */         break;
/*     */       case 2:
/* 145 */         checkTooManySchemaErrors();
/* 146 */         grammar = loadRELAXNG();
/* 147 */         break;
/*     */       case 3:
/* 150 */         checkTooManySchemaErrors();
/* 151 */         grammar = loadRELAXNGCompact();
/* 152 */         break;
/*     */       case 4:
/* 155 */         grammar = annotateXMLSchema(loadWSDL());
/* 156 */         break;
/*     */       case 5:
/* 159 */         grammar = annotateXMLSchema(loadXMLSchema());
/* 160 */         break;
/*     */       default:
/* 163 */         throw new AssertionError();
/*     */       }
/*     */       Model grammar;
/* 166 */       if (this.errorReceiver.hadError())
/* 167 */         grammar = null;
/*     */       else {
/* 169 */         grammar.setPackageLevelAnnotations(this.opt.packageLevelAnnotations);
/*     */       }
/*     */ 
/* 172 */       return grammar;
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/* 178 */       if (this.opt.verbose)
/*     */       {
/* 182 */         if (e.getException() != null)
/* 183 */           e.getException().printStackTrace();
/*     */         else
/* 185 */           e.printStackTrace();
/*     */       }
/* 187 */       return null;
/*     */     } catch (AbortException e) {
/*     */     }
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean sanityCheck()
/*     */   {
/* 201 */     if (this.opt.getSchemaLanguage() == Language.XMLSCHEMA) {
/* 202 */       Language guess = this.opt.guessSchemaLanguage();
/*     */ 
/* 204 */       String[] msg = null;
/* 205 */       switch (4.$SwitchMap$com$sun$tools$internal$xjc$Language[guess.ordinal()]) {
/*     */       case 1:
/* 207 */         msg = new String[] { "DTD", "-dtd" };
/* 208 */         break;
/*     */       case 2:
/* 210 */         msg = new String[] { "RELAX NG", "-relaxng" };
/* 211 */         break;
/*     */       case 3:
/* 213 */         msg = new String[] { "RELAX NG compact syntax", "-relaxng-compact" };
/* 214 */         break;
/*     */       case 4:
/* 216 */         msg = new String[] { "WSDL", "-wsdl" };
/*     */       }
/*     */ 
/* 219 */       if (msg != null) {
/* 220 */         this.errorReceiver.warning(null, 
/* 221 */           Messages.format("Driver.ExperimentalLanguageWarning", new Object[] { msg[0], msg[1] }));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */   private void checkTooManySchemaErrors()
/*     */   {
/* 268 */     if (this.opt.getGrammars().length != 1)
/* 269 */       this.errorReceiver.error(null, Messages.format("ModelLoader.TooManySchema", new Object[0]));
/*     */   }
/*     */ 
/*     */   private Model loadDTD(InputSource source, InputSource bindFile)
/*     */   {
/* 283 */     return TDTDReader.parse(source, bindFile, this.errorReceiver, this.opt);
/*     */   }
/*     */ 
/*     */   public DOMForest buildDOMForest(InternalizationLogic logic)
/*     */     throws SAXException
/*     */   {
/* 300 */     DOMForest forest = new DOMForest(logic, this.opt);
/*     */ 
/* 302 */     forest.setErrorHandler(this.errorReceiver);
/* 303 */     if (this.opt.entityResolver != null) {
/* 304 */       forest.setEntityResolver(this.opt.entityResolver);
/*     */     }
/*     */ 
/* 307 */     for (InputSource value : this.opt.getGrammars()) {
/* 308 */       this.errorReceiver.pollAbort();
/* 309 */       forest.parse(value, true);
/*     */     }
/*     */ 
/* 313 */     for (InputSource value : this.opt.getBindFiles()) {
/* 314 */       this.errorReceiver.pollAbort();
/* 315 */       Document dom = forest.parse(value, true);
/* 316 */       if (dom != null) {
/* 317 */         Element root = dom.getDocumentElement();
/*     */ 
/* 320 */         if ((!fixNull(root.getNamespaceURI()).equals("http://java.sun.com/xml/ns/jaxb")) || 
/* 321 */           (!root
/* 321 */           .getLocalName().equals("bindings"))) {
/* 322 */           this.errorReceiver.error(new SAXParseException(Messages.format("Driver.NotABindingFile", new Object[] { root
/* 323 */             .getNamespaceURI(), root
/* 324 */             .getLocalName() }), null, value
/* 326 */             .getSystemId(), -1, -1));
/*     */         }
/*     */       }
/*     */     }
/* 330 */     this.scdBasedBindingSet = forest.transform(this.opt.isExtensionMode());
/*     */ 
/* 332 */     return forest;
/*     */   }
/*     */ 
/*     */   private String fixNull(String s) {
/* 336 */     if (s == null) return "";
/* 337 */     return s;
/*     */   }
/*     */ 
/*     */   public XSSchemaSet loadXMLSchema()
/*     */     throws SAXException
/*     */   {
/* 345 */     if ((this.opt.strictCheck) && (!SchemaConstraintChecker.check(this.opt.getGrammars(), this.errorReceiver, this.opt.entityResolver, this.opt.disableXmlSecurity)))
/*     */     {
/* 347 */       return null;
/*     */     }
/*     */ 
/* 350 */     if (this.opt.getBindFiles().length == 0)
/*     */     {
/*     */       try
/*     */       {
/* 354 */         return createXSOMSpeculative();
/*     */       }
/*     */       catch (SpeculationFailure localSpeculationFailure)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 362 */     DOMForest forest = buildDOMForest(new XMLSchemaInternalizationLogic());
/* 363 */     return createXSOM(forest, this.scdBasedBindingSet);
/*     */   }
/*     */ 
/*     */   private XSSchemaSet loadWSDL()
/*     */     throws SAXException
/*     */   {
/* 376 */     DOMForest forest = buildDOMForest(new XMLSchemaInternalizationLogic());
/*     */ 
/* 378 */     DOMForestScanner scanner = new DOMForestScanner(forest);
/*     */ 
/* 380 */     XSOMParser xsomParser = createXSOMParser(forest);
/*     */ 
/* 383 */     for (InputSource grammar : this.opt.getGrammars()) {
/* 384 */       Document wsdlDom = forest.get(grammar.getSystemId());
/* 385 */       if (wsdlDom == null) {
/* 386 */         String systemId = Options.normalizeSystemId(grammar.getSystemId());
/* 387 */         if (forest.get(systemId) != null) {
/* 388 */           grammar.setSystemId(systemId);
/* 389 */           wsdlDom = forest.get(grammar.getSystemId());
/*     */         }
/*     */       }
/*     */ 
/* 393 */       NodeList schemas = wsdlDom.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 394 */       for (int i = 0; i < schemas.getLength(); i++)
/* 395 */         scanner.scan((Element)schemas.item(i), xsomParser.getParserHandler());
/*     */     }
/* 397 */     return xsomParser.getResult();
/*     */   }
/*     */ 
/*     */   public Model annotateXMLSchema(XSSchemaSet xs)
/*     */   {
/* 408 */     if (xs == null)
/* 409 */       return null;
/* 410 */     return BGMBuilder.build(xs, this.codeModel, this.errorReceiver, this.opt);
/*     */   }
/*     */ 
/*     */   public XSOMParser createXSOMParser(XMLParser parser)
/*     */   {
/* 422 */     XSOMParser reader = new XSOMParser(new XMLSchemaParser(parser, null));
/* 423 */     reader.setAnnotationParser(new AnnotationParserFactoryImpl(this.opt));
/* 424 */     reader.setErrorHandler(this.errorReceiver);
/* 425 */     reader.setEntityResolver(this.opt.entityResolver);
/* 426 */     return reader;
/*     */   }
/*     */ 
/*     */   public XSOMParser createXSOMParser(final DOMForest forest) {
/* 430 */     XSOMParser p = createXSOMParser(forest.createParser());
/* 431 */     p.setEntityResolver(new EntityResolver()
/*     */     {
/*     */       public InputSource resolveEntity(String publicId, String systemId)
/*     */         throws SAXException, IOException
/*     */       {
/* 439 */         if ((systemId != null) && (forest.get(systemId) != null))
/* 440 */           return new InputSource(systemId);
/* 441 */         if (ModelLoader.this.opt.entityResolver != null) {
/* 442 */           return ModelLoader.this.opt.entityResolver.resolveEntity(publicId, systemId);
/*     */         }
/* 444 */         return null;
/*     */       }
/*     */     });
/* 447 */     return p;
/*     */   }
/*     */ 
/*     */   private XSSchemaSet createXSOMSpeculative()
/*     */     throws SAXException, ModelLoader.SpeculationFailure
/*     */   {
/* 473 */     XMLParser parser = new XMLParser() {
/* 474 */       private final JAXPParser base = new JAXPParser(XmlFactory.createParserFactory(ModelLoader.this.opt.disableXmlSecurity));
/*     */ 
/*     */       public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver)
/*     */         throws SAXException, IOException
/*     */       {
/* 479 */         handler = wrapBy(new ModelLoader.SpeculationChecker(null), handler);
/* 480 */         handler = wrapBy(new VersionChecker(null, ModelLoader.this.errorReceiver, entityResolver), handler);
/*     */ 
/* 482 */         this.base.parse(source, handler, errorHandler, entityResolver);
/*     */       }
/*     */ 
/*     */       private ContentHandler wrapBy(XMLFilterImpl filter, ContentHandler handler)
/*     */       {
/* 490 */         filter.setContentHandler(handler);
/* 491 */         return filter;
/*     */       }
/*     */     };
/* 495 */     XSOMParser reader = createXSOMParser(parser);
/*     */ 
/* 498 */     for (InputSource value : this.opt.getGrammars()) {
/* 499 */       reader.parse(value);
/*     */     }
/* 501 */     return reader.getResult();
/*     */   }
/*     */ 
/*     */   public XSSchemaSet createXSOM(DOMForest forest, SCDBasedBindingSet scdBasedBindingSet)
/*     */     throws SAXException
/*     */   {
/* 512 */     XSOMParser reader = createXSOMParser(forest);
/*     */ 
/* 515 */     for (String systemId : forest.getRootDocuments()) {
/* 516 */       this.errorReceiver.pollAbort();
/* 517 */       Document dom = forest.get(systemId);
/* 518 */       if (!dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb")) {
/* 519 */         reader.parse(systemId);
/*     */       }
/*     */     }
/*     */ 
/* 523 */     XSSchemaSet result = reader.getResult();
/*     */ 
/* 525 */     if (result != null) {
/* 526 */       scdBasedBindingSet.apply(result, this.errorReceiver);
/*     */     }
/* 528 */     return result;
/*     */   }
/*     */ 
/*     */   private Model loadRELAXNG()
/*     */     throws SAXException
/*     */   {
/* 537 */     final DOMForest forest = buildDOMForest(new RELAXNGInternalizationLogic());
/*     */ 
/* 542 */     XMLReaderCreator xrc = new XMLReaderCreator()
/*     */     {
/*     */       public XMLReader createXMLReader()
/*     */       {
/* 547 */         XMLFilter buffer = new XMLFilterImpl()
/*     */         {
/*     */           public void parse(InputSource source) throws IOException, SAXException {
/* 550 */             ModelLoader.3.this.val$forest.createParser().parse(source, this, this, this);
/*     */           }
/*     */         };
/* 554 */         XMLFilter f = new ExtensionBindingChecker("http://relaxng.org/ns/structure/1.0", ModelLoader.this.opt, ModelLoader.this.errorReceiver);
/* 555 */         f.setParent(buffer);
/*     */ 
/* 557 */         f.setEntityResolver(ModelLoader.this.opt.entityResolver);
/*     */ 
/* 559 */         return f;
/*     */       }
/*     */     };
/* 563 */     Parseable p = new SAXParseable(this.opt.getGrammars()[0], this.errorReceiver, xrc);
/*     */ 
/* 565 */     return loadRELAXNG(p);
/*     */   }
/*     */ 
/*     */   private Model loadRELAXNGCompact()
/*     */   {
/* 573 */     if (this.opt.getBindFiles().length > 0) {
/* 574 */       this.errorReceiver.error(new SAXParseException(
/* 575 */         Messages.format("ModelLoader.BindingFileNotSupportedForRNC", new Object[0]), 
/* 575 */         null));
/*     */     }
/*     */ 
/* 578 */     Parseable p = new CompactParseable(this.opt.getGrammars()[0], this.errorReceiver);
/*     */ 
/* 580 */     return loadRELAXNG(p);
/*     */   }
/*     */ 
/*     */   private Model loadRELAXNG(Parseable p)
/*     */   {
/* 588 */     SchemaBuilder sb = new CheckingSchemaBuilder(new DSchemaBuilderImpl(), this.errorReceiver);
/*     */     try
/*     */     {
/* 591 */       DPattern out = (DPattern)p.parse(sb);
/* 592 */       return RELAXNGCompiler.build(out, this.codeModel, this.opt);
/*     */     } catch (IllegalSchemaException e) {
/* 594 */       this.errorReceiver.error(e.getMessage(), e);
/* 595 */     }return null;
/*     */   }
/*     */ 
/*     */   private static final class SpeculationChecker extends XMLFilterImpl
/*     */   {
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       throws SAXException
/*     */     {
/* 456 */       if ((localName.equals("bindings")) && (uri.equals("http://java.sun.com/xml/ns/jaxb")))
/* 457 */         throw new ModelLoader.SpeculationFailure(null);
/* 458 */       super.startElement(uri, localName, qName, attributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class SpeculationFailure extends Error
/*     */   {
/*     */   }
/*     */ 
/*     */   private class XMLSchemaParser
/*     */     implements XMLParser
/*     */   {
/*     */     private final XMLParser baseParser;
/*     */ 
/*     */     private XMLSchemaParser(XMLParser baseParser)
/*     */     {
/* 243 */       this.baseParser = baseParser;
/*     */     }
/*     */ 
/*     */     public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver)
/*     */       throws SAXException, IOException
/*     */     {
/* 249 */       handler = wrapBy(new ExtensionBindingChecker("http://www.w3.org/2001/XMLSchema", ModelLoader.this.opt, ModelLoader.this.errorReceiver), handler);
/* 250 */       handler = wrapBy(new IncorrectNamespaceURIChecker(ModelLoader.this.errorReceiver), handler);
/* 251 */       handler = wrapBy(new CustomizationContextChecker(ModelLoader.this.errorReceiver), handler);
/*     */ 
/* 254 */       this.baseParser.parse(source, handler, errorHandler, entityResolver);
/*     */     }
/*     */ 
/*     */     private ContentHandler wrapBy(XMLFilterImpl filter, ContentHandler handler)
/*     */     {
/* 262 */       filter.setContentHandler(handler);
/* 263 */       return filter;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.ModelLoader
 * JD-Core Version:    0.6.2
 */