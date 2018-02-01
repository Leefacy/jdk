/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.LocatorTable;
/*     */ import com.sun.xml.internal.bind.marshaller.DataWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class DOMForest
/*     */ {
/*  71 */   protected final Set<String> rootDocuments = new HashSet();
/*     */ 
/*  76 */   protected final Set<String> externalReferences = new HashSet();
/*     */ 
/*  81 */   protected final Map<String, Document> core = new HashMap();
/*     */   protected final ErrorReceiver errorReceiver;
/*     */   private final DocumentBuilder documentBuilder;
/*     */   private final SAXParserFactory parserFactory;
/*  90 */   protected final List<Element> inlinedSchemaElements = new ArrayList();
/*     */ 
/*  96 */   public final LocatorTable locatorTable = new LocatorTable();
/*     */   protected final EntityResolver entityResolver;
/* 102 */   public final Set<Element> outerMostBindings = new HashSet();
/*     */   protected final InternalizationLogic logic;
/*     */   protected final WsimportOptions options;
/* 172 */   protected Map<String, String> resolvedCache = new HashMap();
/*     */ 
/*     */   public DOMForest(InternalizationLogic logic, @NotNull EntityResolver entityResolver, WsimportOptions options, ErrorReceiver errReceiver)
/*     */   {
/* 111 */     this.options = options;
/* 112 */     this.entityResolver = entityResolver;
/* 113 */     this.errorReceiver = errReceiver;
/* 114 */     this.logic = logic;
/*     */     try
/*     */     {
/* 117 */       boolean secureProcessingEnabled = (options == null) || (!options.disableXmlSecurity);
/* 118 */       DocumentBuilderFactory dbf = XmlUtil.newDocumentBuilderFactory(secureProcessingEnabled);
/* 119 */       dbf.setNamespaceAware(true);
/* 120 */       this.documentBuilder = dbf.newDocumentBuilder();
/*     */ 
/* 122 */       this.parserFactory = XmlUtil.newSAXParserFactory(secureProcessingEnabled);
/* 123 */       this.parserFactory.setNamespaceAware(true);
/*     */     } catch (ParserConfigurationException e) {
/* 125 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<Element> getInlinedSchemaElement() {
/* 130 */     return this.inlinedSchemaElements;
/*     */   }
/*     */   @NotNull
/*     */   public Document parse(InputSource source, boolean root) throws SAXException, IOException {
/* 134 */     if (source.getSystemId() == null)
/* 135 */       throw new IllegalArgumentException();
/* 136 */     return parse(source.getSystemId(), source, root);
/*     */   }
/*     */ 
/*     */   public Document parse(String systemId, boolean root)
/*     */     throws SAXException, IOException
/*     */   {
/* 148 */     systemId = normalizeSystemId(systemId);
/*     */ 
/* 150 */     InputSource is = null;
/*     */ 
/* 153 */     is = this.entityResolver.resolveEntity(null, systemId);
/* 154 */     if (is == null) {
/* 155 */       is = new InputSource(systemId);
/*     */     } else {
/* 157 */       this.resolvedCache.put(systemId, is.getSystemId());
/* 158 */       systemId = is.getSystemId();
/*     */     }
/*     */ 
/* 161 */     if (this.core.containsKey(systemId))
/*     */     {
/* 163 */       return (Document)this.core.get(systemId);
/*     */     }
/*     */ 
/* 166 */     if (!root) {
/* 167 */       addExternalReferences(systemId);
/*     */     }
/*     */ 
/* 170 */     return parse(systemId, is, root);
/*     */   }
/*     */ 
/*     */   public Map<String, String> getReferencedEntityMap()
/*     */   {
/* 175 */     return this.resolvedCache;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private Document parse(String systemId, InputSource inputSource, boolean root)
/*     */     throws SAXException, IOException
/*     */   {
/* 183 */     Document dom = this.documentBuilder.newDocument();
/*     */ 
/* 185 */     systemId = normalizeSystemId(systemId);
/*     */ 
/* 189 */     this.core.put(systemId, dom);
/*     */ 
/* 191 */     dom.setDocumentURI(systemId);
/* 192 */     if (root)
/* 193 */       this.rootDocuments.add(systemId);
/*     */     try
/*     */     {
/* 196 */       XMLReader reader = createReader(dom);
/*     */ 
/* 198 */       InputStream is = null;
/* 199 */       if (inputSource.getByteStream() == null) {
/* 200 */         inputSource = this.entityResolver.resolveEntity(null, systemId);
/*     */       }
/* 202 */       reader.parse(inputSource);
/* 203 */       Element doc = dom.getDocumentElement();
/* 204 */       if (doc == null) {
/* 205 */         return null;
/*     */       }
/* 207 */       NodeList schemas = doc.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 208 */       for (int i = 0; i < schemas.getLength(); i++)
/* 209 */         this.inlinedSchemaElements.add((Element)schemas.item(i));
/*     */     }
/*     */     catch (ParserConfigurationException e) {
/* 212 */       this.errorReceiver.error(e);
/* 213 */       throw new SAXException(e.getMessage());
/*     */     }
/* 215 */     this.resolvedCache.put(systemId, dom.getDocumentURI());
/* 216 */     return dom;
/*     */   }
/*     */ 
/*     */   public void addExternalReferences(String ref) {
/* 220 */     if (!this.externalReferences.contains(ref))
/* 221 */       this.externalReferences.add(ref);
/*     */   }
/*     */ 
/*     */   public Set<String> getExternalReferences()
/*     */   {
/* 226 */     return this.externalReferences;
/*     */   }
/*     */ 
/*     */   private XMLReader createReader(Document dom)
/*     */     throws SAXException, ParserConfigurationException
/*     */   {
/* 245 */     XMLReader reader = this.parserFactory.newSAXParser().getXMLReader();
/* 246 */     DOMBuilder dombuilder = new DOMBuilder(dom, this.locatorTable, this.outerMostBindings);
/*     */     try {
/* 248 */       reader.setProperty("http://xml.org/sax/properties/lexical-handler", dombuilder);
/*     */     } catch (SAXException e) {
/* 250 */       this.errorReceiver.debug(e.getMessage());
/*     */     }
/*     */ 
/* 253 */     ContentHandler handler = new WhitespaceStripper(dombuilder, this.errorReceiver, this.entityResolver);
/* 254 */     handler = new VersionChecker(handler, this.errorReceiver, this.entityResolver);
/*     */ 
/* 258 */     XMLFilterImpl f = this.logic.createExternalReferenceFinder(this);
/* 259 */     f.setContentHandler(handler);
/* 260 */     if (this.errorReceiver != null)
/* 261 */       f.setErrorHandler(this.errorReceiver);
/* 262 */     f.setEntityResolver(this.entityResolver);
/*     */ 
/* 264 */     reader.setContentHandler(f);
/* 265 */     if (this.errorReceiver != null)
/* 266 */       reader.setErrorHandler(this.errorReceiver);
/* 267 */     reader.setEntityResolver(this.entityResolver);
/* 268 */     return reader;
/*     */   }
/*     */ 
/*     */   private String normalizeSystemId(String systemId) {
/*     */     try {
/* 273 */       systemId = new URI(systemId).normalize().toString();
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException) {
/*     */     }
/* 277 */     return systemId;
/*     */   }
/*     */ 
/*     */   boolean isExtensionMode() {
/* 281 */     return this.options.isExtensionMode();
/*     */   }
/*     */ 
/*     */   public Document get(String systemId)
/*     */   {
/* 290 */     Document doc = (Document)this.core.get(systemId);
/*     */ 
/* 292 */     if ((doc == null) && (systemId.startsWith("file:/")) && (!systemId.startsWith("file://")))
/*     */     {
/* 299 */       doc = (Document)this.core.get("file://" + systemId.substring(5));
/*     */     }
/*     */     String systemPath;
/* 302 */     if ((doc == null) && (systemId.startsWith("file:")))
/*     */     {
/* 305 */       systemPath = getPath(systemId);
/* 306 */       for (String key : this.core.keySet()) {
/* 307 */         if ((key.startsWith("file:")) && (getPath(key).equalsIgnoreCase(systemPath))) {
/* 308 */           doc = (Document)this.core.get(key);
/* 309 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 314 */     return doc;
/*     */   }
/*     */ 
/*     */   private String getPath(String key)
/*     */   {
/* 321 */     key = key.substring(5);
/* 322 */     while ((key.length() > 0) && (key.charAt(0) == '/'))
/* 323 */       key = key.substring(1);
/* 324 */     return key;
/*     */   }
/*     */ 
/*     */   public String[] listSystemIDs()
/*     */   {
/* 331 */     return (String[])this.core.keySet().toArray(new String[this.core.keySet().size()]);
/*     */   }
/*     */ 
/*     */   public String getSystemId(Document dom)
/*     */   {
/* 340 */     for (Map.Entry e : this.core.entrySet()) {
/* 341 */       if (e.getValue() == dom)
/* 342 */         return (String)e.getKey();
/*     */     }
/* 344 */     return null;
/*     */   }
/*     */ 
/*     */   public String getFirstRootDocument()
/*     */   {
/* 351 */     if (this.rootDocuments.isEmpty()) return null;
/* 352 */     return (String)this.rootDocuments.iterator().next();
/*     */   }
/*     */ 
/*     */   public Set<String> getRootDocuments() {
/* 356 */     return this.rootDocuments;
/*     */   }
/*     */ 
/*     */   public void dump(OutputStream out)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 368 */       boolean secureProcessingEnabled = (this.options == null) || (!this.options.disableXmlSecurity);
/* 369 */       TransformerFactory tf = XmlUtil.newTransformerFactory(secureProcessingEnabled);
/* 370 */       it = tf.newTransformer();
/*     */ 
/* 372 */       for (Map.Entry e : this.core.entrySet()) {
/* 373 */         out.write(("---<< " + (String)e.getKey() + '\n').getBytes());
/*     */ 
/* 375 */         DataWriter dw = new DataWriter(new OutputStreamWriter(out), null);
/* 376 */         dw.setIndentStep("  ");
/* 377 */         it.transform(new DOMSource((Node)e.getValue()), new SAXResult(dw));
/*     */ 
/* 380 */         out.write("\n\n\n".getBytes());
/*     */       }
/*     */     }
/*     */     catch (TransformerException e)
/*     */     {
/*     */       Transformer it;
/* 383 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Handler extends ContentHandler
/*     */   {
/*     */     public abstract Document getDocument();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.parser.DOMForest
 * JD-Core Version:    0.6.2
 */