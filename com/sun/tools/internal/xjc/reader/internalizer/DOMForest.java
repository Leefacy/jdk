/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.XMLStreamReaderToContentHandler;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.marshaller.DataWriter;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import com.sun.xml.internal.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public final class DOMForest
/*     */ {
/*  87 */   private final Map<String, Document> core = new HashMap();
/*     */ 
/*  98 */   private final Set<String> rootDocuments = new HashSet();
/*     */ 
/* 101 */   public final LocatorTable locatorTable = new LocatorTable();
/*     */ 
/* 104 */   public final Set<Element> outerMostBindings = new HashSet();
/*     */ 
/* 107 */   private EntityResolver entityResolver = null;
/*     */ 
/* 110 */   private ErrorReceiver errorReceiver = null;
/*     */   protected final InternalizationLogic logic;
/*     */   private final SAXParserFactory parserFactory;
/*     */   private final DocumentBuilder documentBuilder;
/*     */   private final Options options;
/*     */ 
/*     */   public DOMForest(SAXParserFactory parserFactory, DocumentBuilder documentBuilder, InternalizationLogic logic)
/*     */   {
/* 124 */     this.parserFactory = parserFactory;
/* 125 */     this.documentBuilder = documentBuilder;
/* 126 */     this.logic = logic;
/* 127 */     this.options = null;
/*     */   }
/*     */ 
/*     */   public DOMForest(InternalizationLogic logic, Options opt)
/*     */   {
/* 132 */     if (opt == null) throw new AssertionError("Options object null");
/* 133 */     this.options = opt;
/*     */     try
/*     */     {
/* 136 */       DocumentBuilderFactory dbf = XmlFactory.createDocumentBuilderFactory(opt.disableXmlSecurity);
/* 137 */       this.documentBuilder = dbf.newDocumentBuilder();
/* 138 */       this.parserFactory = XmlFactory.createParserFactory(opt.disableXmlSecurity);
/*     */     } catch (ParserConfigurationException e) {
/* 140 */       throw new AssertionError(e);
/*     */     }
/*     */ 
/* 143 */     this.logic = logic;
/*     */   }
/*     */ 
/*     */   public Document get(String systemId)
/*     */   {
/* 151 */     Document doc = (Document)this.core.get(systemId);
/*     */ 
/* 153 */     if ((doc == null) && (systemId.startsWith("file:/")) && (!systemId.startsWith("file://")))
/*     */     {
/* 160 */       doc = (Document)this.core.get("file://" + systemId.substring(5));
/*     */     }
/*     */     String systemPath;
/* 163 */     if ((doc == null) && (systemId.startsWith("file:")))
/*     */     {
/* 166 */       systemPath = getPath(systemId);
/* 167 */       for (String key : this.core.keySet()) {
/* 168 */         if ((key.startsWith("file:")) && (getPath(key).equalsIgnoreCase(systemPath))) {
/* 169 */           doc = (Document)this.core.get(key);
/* 170 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 175 */     return doc;
/*     */   }
/*     */ 
/*     */   private String getPath(String key)
/*     */   {
/* 182 */     key = key.substring(5);
/* 183 */     while ((key.length() > 0) && (key.charAt(0) == '/')) {
/* 184 */       key = key.substring(1);
/*     */     }
/* 186 */     return key;
/*     */   }
/*     */ 
/*     */   public Set<String> getRootDocuments()
/*     */   {
/* 193 */     return Collections.unmodifiableSet(this.rootDocuments);
/*     */   }
/*     */ 
/*     */   public Document getOneDocument()
/*     */   {
/* 200 */     for (Document dom : this.core.values()) {
/* 201 */       if (!dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb")) {
/* 202 */         return dom;
/*     */       }
/*     */     }
/* 205 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   public boolean checkSchemaCorrectness(ErrorReceiver errorHandler)
/*     */   {
/*     */     try
/*     */     {
/* 221 */       boolean disableXmlSecurity = false;
/* 222 */       if (this.options != null) {
/* 223 */         disableXmlSecurity = this.options.disableXmlSecurity;
/*     */       }
/* 225 */       SchemaFactory sf = XmlFactory.createSchemaFactory("http://www.w3.org/2001/XMLSchema", disableXmlSecurity);
/* 226 */       ErrorReceiverFilter filter = new ErrorReceiverFilter(errorHandler);
/* 227 */       sf.setErrorHandler(filter);
/* 228 */       Set roots = getRootDocuments();
/* 229 */       Source[] sources = new Source[roots.size()];
/* 230 */       int i = 0;
/* 231 */       for (String root : roots) {
/* 232 */         sources[(i++)] = new DOMSource(get(root), root);
/*     */       }
/* 234 */       sf.newSchema(sources);
/* 235 */       return !filter.hadError();
/*     */     } catch (SAXException e) {
/*     */     }
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   public String getSystemId(Document dom)
/*     */   {
/* 248 */     for (Map.Entry e : this.core.entrySet()) {
/* 249 */       if (e.getValue() == dom)
/* 250 */         return (String)e.getKey();
/*     */     }
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */   public Document parse(InputSource source, boolean root) throws SAXException {
/* 256 */     if (source.getSystemId() == null) {
/* 257 */       throw new IllegalArgumentException();
/*     */     }
/* 259 */     return parse(source.getSystemId(), source, root);
/*     */   }
/*     */ 
/*     */   public Document parse(String systemId, boolean root)
/*     */     throws SAXException, IOException
/*     */   {
/* 271 */     systemId = Options.normalizeSystemId(systemId);
/*     */ 
/* 273 */     if (this.core.containsKey(systemId))
/*     */     {
/* 275 */       return (Document)this.core.get(systemId);
/*     */     }
/* 277 */     InputSource is = null;
/*     */ 
/* 280 */     if (this.entityResolver != null)
/* 281 */       is = this.entityResolver.resolveEntity(null, systemId);
/* 282 */     if (is == null) {
/* 283 */       is = new InputSource(systemId);
/*     */     }
/*     */ 
/* 286 */     return parse(systemId, is, root);
/*     */   }
/*     */ 
/*     */   private ContentHandler getParserHandler(Document dom)
/*     */   {
/* 300 */     ContentHandler handler = new DOMBuilder(dom, this.locatorTable, this.outerMostBindings);
/* 301 */     handler = new WhitespaceStripper(handler, this.errorReceiver, this.entityResolver);
/* 302 */     handler = new VersionChecker(handler, this.errorReceiver, this.entityResolver);
/*     */ 
/* 306 */     XMLFilterImpl f = this.logic.createExternalReferenceFinder(this);
/* 307 */     f.setContentHandler(handler);
/*     */ 
/* 309 */     if (this.errorReceiver != null)
/* 310 */       f.setErrorHandler(this.errorReceiver);
/* 311 */     if (this.entityResolver != null) {
/* 312 */       f.setEntityResolver(this.entityResolver);
/*     */     }
/* 314 */     return f;
/*     */   }
/*     */ 
/*     */   public Handler getParserHandler(String systemId, boolean root)
/*     */   {
/* 335 */     final Document dom = this.documentBuilder.newDocument();
/* 336 */     this.core.put(systemId, dom);
/* 337 */     if (root) {
/* 338 */       this.rootDocuments.add(systemId);
/*     */     }
/* 340 */     ContentHandler handler = getParserHandler(dom);
/*     */ 
/* 345 */     HandlerImpl x = new HandlerImpl(dom) {
/*     */       public Document getDocument() {
/* 347 */         return dom;
/*     */       }
/*     */     };
/* 350 */     x.setContentHandler(handler);
/*     */ 
/* 352 */     return x;
/*     */   }
/*     */ 
/*     */   public Document parse(String systemId, InputSource inputSource, boolean root)
/*     */     throws SAXException
/*     */   {
/* 362 */     Document dom = this.documentBuilder.newDocument();
/*     */ 
/* 364 */     systemId = Options.normalizeSystemId(systemId);
/*     */ 
/* 368 */     this.core.put(systemId, dom);
/* 369 */     if (root)
/* 370 */       this.rootDocuments.add(systemId);
/*     */     try
/*     */     {
/* 373 */       XMLReader reader = this.parserFactory.newSAXParser().getXMLReader();
/* 374 */       reader.setContentHandler(getParserHandler(dom));
/* 375 */       if (this.errorReceiver != null)
/* 376 */         reader.setErrorHandler(this.errorReceiver);
/* 377 */       if (this.entityResolver != null)
/* 378 */         reader.setEntityResolver(this.entityResolver);
/* 379 */       reader.parse(inputSource);
/*     */     }
/*     */     catch (ParserConfigurationException e) {
/* 382 */       this.errorReceiver.error(e.getMessage(), e);
/* 383 */       this.core.remove(systemId);
/* 384 */       this.rootDocuments.remove(systemId);
/* 385 */       return null;
/*     */     } catch (IOException e) {
/* 387 */       this.errorReceiver.error(Messages.format("DOMFOREST_INPUTSOURCE_IOEXCEPTION", new Object[] { systemId, e.toString() }), e);
/* 388 */       this.core.remove(systemId);
/* 389 */       this.rootDocuments.remove(systemId);
/* 390 */       return null;
/*     */     }
/*     */ 
/* 393 */     return dom;
/*     */   }
/*     */ 
/*     */   public Document parse(String systemId, XMLStreamReader parser, boolean root) throws XMLStreamException {
/* 397 */     Document dom = this.documentBuilder.newDocument();
/*     */ 
/* 399 */     systemId = Options.normalizeSystemId(systemId);
/*     */ 
/* 401 */     if (root) {
/* 402 */       this.rootDocuments.add(systemId);
/*     */     }
/* 404 */     if (systemId == null)
/* 405 */       throw new IllegalArgumentException("system id cannot be null");
/* 406 */     this.core.put(systemId, dom);
/*     */ 
/* 408 */     new XMLStreamReaderToContentHandler(parser, getParserHandler(dom), false, false).bridge();
/*     */ 
/* 410 */     return dom;
/*     */   }
/*     */ 
/*     */   public SCDBasedBindingSet transform(boolean enableSCD)
/*     */   {
/* 424 */     return Internalizer.transform(this, enableSCD, this.options.disableXmlSecurity);
/*     */   }
/*     */ 
/*     */   public void weakSchemaCorrectnessCheck(SchemaFactory sf)
/*     */   {
/* 447 */     List sources = new ArrayList();
/* 448 */     for (String systemId : getRootDocuments()) {
/* 449 */       Document dom = get(systemId);
/* 450 */       if (!dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb"))
/*     */       {
/* 453 */         SAXSource ss = createSAXSource(systemId);
/*     */         try {
/* 455 */           ss.getXMLReader().setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/*     */         } catch (SAXException e) {
/* 457 */           throw new AssertionError(e);
/*     */         }
/* 459 */         sources.add(ss);
/*     */       }
/*     */     }
/*     */     try {
/* 463 */       XmlFactory.allowExternalAccess(sf, "file,http", this.options.disableXmlSecurity).newSchema((Source[])sources.toArray(new SAXSource[0]));
/*     */     }
/*     */     catch (SAXException localSAXException3)
/*     */     {
/*     */     }
/*     */     catch (RuntimeException re)
/*     */     {
/*     */       try {
/* 471 */         sf.getErrorHandler().warning(new SAXParseException(
/* 472 */           Messages.format("ERR_GENERAL_SCHEMA_CORRECTNESS_ERROR", new Object[] { re
/* 473 */           .getMessage() }), null, null, -1, -1, re));
/*     */       }
/*     */       catch (SAXException localSAXException2)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public SAXSource createSAXSource(String systemId)
/*     */   {
/* 486 */     ContentHandlerNamespacePrefixAdapter reader = new ContentHandlerNamespacePrefixAdapter(new XMLFilterImpl()
/*     */     {
/*     */       public void parse(InputSource input)
/*     */         throws SAXException, IOException
/*     */       {
/* 491 */         DOMForest.this.createParser().parse(input, this, this, this);
/*     */       }
/*     */ 
/*     */       public void parse(String systemId) throws SAXException, IOException
/*     */       {
/* 496 */         parse(new InputSource(systemId));
/*     */       }
/*     */     });
/* 500 */     return new SAXSource(reader, new InputSource(systemId));
/*     */   }
/*     */ 
/*     */   public XMLParser createParser()
/*     */   {
/* 511 */     return new DOMForestParser(this, new JAXPParser(XmlFactory.createParserFactory(this.options.disableXmlSecurity)));
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver() {
/* 515 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver entityResolver) {
/* 519 */     this.entityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   public ErrorReceiver getErrorHandler() {
/* 523 */     return this.errorReceiver;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorReceiver errorHandler) {
/* 527 */     this.errorReceiver = errorHandler;
/*     */   }
/*     */ 
/*     */   public Document[] listDocuments()
/*     */   {
/* 534 */     return (Document[])this.core.values().toArray(new Document[this.core.size()]);
/*     */   }
/*     */ 
/*     */   public String[] listSystemIDs()
/*     */   {
/* 541 */     return (String[])this.core.keySet().toArray(new String[this.core.keySet().size()]);
/*     */   }
/*     */ 
/*     */   public void dump(OutputStream out)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 553 */       boolean disableXmlSecurity = false;
/* 554 */       if (this.options != null) {
/* 555 */         disableXmlSecurity = this.options.disableXmlSecurity;
/*     */       }
/* 557 */       TransformerFactory tf = XmlFactory.createTransformerFactory(disableXmlSecurity);
/* 558 */       it = tf.newTransformer();
/*     */ 
/* 560 */       for (Map.Entry e : this.core.entrySet()) {
/* 561 */         out.write(("---<< " + (String)e.getKey() + '\n').getBytes());
/*     */ 
/* 563 */         DataWriter dw = new DataWriter(new OutputStreamWriter(out), null);
/* 564 */         dw.setIndentStep("  ");
/* 565 */         it.transform(new DOMSource((Node)e.getValue()), new SAXResult(dw));
/*     */ 
/* 568 */         out.write("\n\n\n".getBytes());
/*     */       }
/*     */     }
/*     */     catch (TransformerException e)
/*     */     {
/*     */       Transformer it;
/* 571 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Handler extends ContentHandler
/*     */   {
/*     */     public abstract Document getDocument();
/*     */   }
/*     */ 
/*     */   private static abstract class HandlerImpl extends XMLFilterImpl
/*     */     implements DOMForest.Handler
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.internalizer.DOMForest
 * JD-Core Version:    0.6.2
 */