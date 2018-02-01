/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.internal.xsom.impl.parser.state.Schema;
/*     */ import com.sun.xml.internal.xsom.impl.util.Uri;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParser;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class NGCCRuntimeEx extends NGCCRuntime
/*     */   implements PatcherManager
/*     */ {
/*     */   public final ParserContext parser;
/*     */   public SchemaImpl currentSchema;
/*  69 */   public int finalDefault = 0;
/*     */ 
/*  71 */   public int blockDefault = 0;
/*     */ 
/*  77 */   public boolean elementFormDefault = false;
/*     */ 
/*  83 */   public boolean attributeFormDefault = false;
/*     */ 
/*  91 */   public boolean chameleonMode = false;
/*     */   private String documentSystemId;
/* 104 */   private final Stack<String> elementNames = new Stack();
/*     */   private final NGCCRuntimeEx referer;
/*     */   public SchemaDocumentImpl document;
/* 416 */   private Context currentContext = null;
/*     */   public static final String XMLSchemaNSURI = "http://www.w3.org/2001/XMLSchema";
/*     */ 
/*     */   NGCCRuntimeEx(ParserContext _parser)
/*     */   {
/* 119 */     this(_parser, false, null);
/*     */   }
/*     */ 
/*     */   private NGCCRuntimeEx(ParserContext _parser, boolean chameleonMode, NGCCRuntimeEx referer) {
/* 123 */     this.parser = _parser;
/* 124 */     this.chameleonMode = chameleonMode;
/* 125 */     this.referer = referer;
/*     */ 
/* 128 */     this.currentContext = new Context("", "", null);
/* 129 */     this.currentContext = new Context("xml", "http://www.w3.org/XML/1998/namespace", this.currentContext);
/*     */   }
/*     */ 
/*     */   public void checkDoubleDefError(XSDeclaration c) throws SAXException {
/* 133 */     if ((c == null) || (ignorableDuplicateComponent(c))) return;
/*     */ 
/* 135 */     reportError(Messages.format("DoubleDefinition", new Object[] { c.getName() }));
/* 136 */     reportError(Messages.format("DoubleDefinition.Original", new Object[0]), c.getLocator());
/*     */   }
/*     */ 
/*     */   public static boolean ignorableDuplicateComponent(XSDeclaration c) {
/* 140 */     if (c.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/* 141 */       if ((c instanceof XSSimpleType))
/*     */       {
/* 143 */         return true;
/* 144 */       }if ((c.isGlobal()) && (c.getName().equals("anyType")))
/* 145 */         return true;
/*     */     }
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   public void addPatcher(Patch patcher)
/*     */   {
/* 154 */     this.parser.patcherManager.addPatcher(patcher);
/*     */   }
/*     */   public void addErrorChecker(Patch patcher) {
/* 157 */     this.parser.patcherManager.addErrorChecker(patcher);
/*     */   }
/*     */   public void reportError(String msg, Locator loc) throws SAXException {
/* 160 */     this.parser.patcherManager.reportError(msg, loc);
/*     */   }
/*     */   public void reportError(String msg) throws SAXException {
/* 163 */     reportError(msg, getLocator());
/*     */   }
/*     */ 
/*     */   private InputSource resolveRelativeURL(String namespaceURI, String relativeUri)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 182 */       String baseUri = getLocator().getSystemId();
/* 183 */       if (baseUri == null)
/*     */       {
/* 186 */         baseUri = this.documentSystemId;
/*     */       }
/* 188 */       EntityResolver er = this.parser.getEntityResolver();
/* 189 */       String systemId = null;
/*     */ 
/* 191 */       if (relativeUri != null) {
/* 192 */         systemId = Uri.resolve(baseUri, relativeUri);
/*     */       }
/* 194 */       if (er != null) {
/* 195 */         InputSource is = er.resolveEntity(namespaceURI, systemId);
/* 196 */         if (is == null)
/*     */           try {
/* 198 */             String normalizedSystemId = URI.create(systemId).normalize().toASCIIString();
/* 199 */             is = er.resolveEntity(namespaceURI, normalizedSystemId);
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/*     */           }
/* 204 */         if (is != null) {
/* 205 */           return is;
/*     */         }
/*     */       }
/*     */ 
/* 209 */       if (systemId != null) {
/* 210 */         return new InputSource(systemId);
/*     */       }
/* 212 */       return null;
/*     */     } catch (IOException e) {
/* 214 */       SAXParseException se = new SAXParseException(e.getMessage(), getLocator(), e);
/* 215 */       this.parser.errorHandler.error(se);
/* 216 */     }return null;
/*     */   }
/*     */ 
/*     */   public void includeSchema(String schemaLocation)
/*     */     throws SAXException
/*     */   {
/* 222 */     NGCCRuntimeEx runtime = new NGCCRuntimeEx(this.parser, this.chameleonMode, this);
/* 223 */     runtime.currentSchema = this.currentSchema;
/* 224 */     runtime.blockDefault = this.blockDefault;
/* 225 */     runtime.finalDefault = this.finalDefault;
/*     */ 
/* 227 */     if (schemaLocation == null)
/*     */     {
/* 229 */       SAXParseException e = new SAXParseException(
/* 229 */         Messages.format("MissingSchemaLocation", new Object[0]), 
/* 229 */         getLocator());
/* 230 */       this.parser.errorHandler.fatalError(e);
/* 231 */       throw e;
/*     */     }
/*     */ 
/* 234 */     runtime.parseEntity(resolveRelativeURL(null, schemaLocation), true, this.currentSchema
/* 235 */       .getTargetNamespace(), getLocator());
/*     */   }
/*     */ 
/*     */   public void importSchema(String ns, String schemaLocation) throws SAXException
/*     */   {
/* 240 */     NGCCRuntimeEx newRuntime = new NGCCRuntimeEx(this.parser, false, this);
/* 241 */     InputSource source = resolveRelativeURL(ns, schemaLocation);
/* 242 */     if (source != null)
/* 243 */       newRuntime.parseEntity(source, false, ns, getLocator());
/*     */   }
/*     */ 
/*     */   public boolean hasAlreadyBeenRead()
/*     */   {
/* 282 */     if ((this.documentSystemId != null) && 
/* 283 */       (this.documentSystemId.startsWith("file:///")))
/*     */     {
/* 289 */       this.documentSystemId = ("file:/" + this.documentSystemId.substring(8));
/*     */     }
/*     */ 
/* 296 */     assert (this.document == null);
/* 297 */     this.document = new SchemaDocumentImpl(this.currentSchema, this.documentSystemId);
/*     */ 
/* 299 */     SchemaDocumentImpl existing = (SchemaDocumentImpl)this.parser.parsedDocuments.get(this.document);
/* 300 */     if (existing == null)
/* 301 */       this.parser.parsedDocuments.put(this.document, this.document);
/*     */     else {
/* 303 */       this.document = existing;
/*     */     }
/*     */ 
/* 306 */     assert (this.document != null);
/*     */ 
/* 308 */     if (this.referer != null) {
/* 309 */       assert (this.referer.document != null) : ("referer " + this.referer.documentSystemId + " has docIdentity==null");
/* 310 */       this.referer.document.references.add(this.document);
/* 311 */       this.document.referers.add(this.referer.document);
/*     */     }
/*     */ 
/* 314 */     return existing != null;
/*     */   }
/*     */ 
/*     */   public void parseEntity(InputSource source, boolean includeMode, String expectedNamespace, Locator importLocation)
/*     */     throws SAXException
/*     */   {
/* 327 */     this.documentSystemId = source.getSystemId();
/*     */     try {
/* 329 */       Schema s = new Schema(this, includeMode, expectedNamespace);
/* 330 */       setRootHandler(s);
/*     */       try {
/* 332 */         this.parser.parser.parse(source, this, getErrorHandler(), this.parser.getEntityResolver());
/*     */       } catch (IOException fnfe) {
/* 334 */         SAXParseException se = new SAXParseException(fnfe.toString(), importLocation, fnfe);
/* 335 */         this.parser.errorHandler.warning(se);
/*     */       }
/*     */     } catch (SAXException e) {
/* 338 */       this.parser.setErrorFlag();
/* 339 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public AnnotationParser createAnnotationParser()
/*     */   {
/* 347 */     if (this.parser.getAnnotationParserFactory() == null) {
/* 348 */       return DefaultAnnotationParser.theInstance;
/*     */     }
/* 350 */     return this.parser.getAnnotationParserFactory().create();
/*     */   }
/*     */ 
/*     */   public String getAnnotationContextElementName()
/*     */   {
/* 358 */     return (String)this.elementNames.get(this.elementNames.size() - 2);
/*     */   }
/*     */ 
/*     */   public Locator copyLocator()
/*     */   {
/* 363 */     return new LocatorImpl(getLocator());
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler() {
/* 367 */     return this.parser.errorHandler;
/*     */   }
/*     */ 
/*     */   public void onEnterElementConsumed(String uri, String localName, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 373 */     super.onEnterElementConsumed(uri, localName, qname, atts);
/* 374 */     this.elementNames.push(localName);
/*     */   }
/*     */ 
/*     */   public void onLeaveElementConsumed(String uri, String localName, String qname) throws SAXException
/*     */   {
/* 379 */     super.onLeaveElementConsumed(uri, localName, qname);
/* 380 */     this.elementNames.pop();
/*     */   }
/*     */ 
/*     */   public ValidationContext createValidationContext()
/*     */   {
/* 420 */     return this.currentContext;
/*     */   }
/*     */ 
/*     */   public XmlString createXmlString(String value) {
/* 424 */     if (value == null) return null;
/* 425 */     return new XmlString(value, createValidationContext());
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException
/*     */   {
/* 430 */     super.startPrefixMapping(prefix, uri);
/* 431 */     this.currentContext = new Context(prefix, uri, this.currentContext);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 435 */     super.endPrefixMapping(prefix);
/* 436 */     this.currentContext = this.currentContext.previous;
/*     */   }
/*     */ 
/*     */   public UName parseUName(String qname)
/*     */     throws SAXException
/*     */   {
/* 452 */     int idx = qname.indexOf(':');
/* 453 */     if (idx < 0) {
/* 454 */       String uri = resolveNamespacePrefix("");
/*     */ 
/* 457 */       if ((uri.equals("")) && (this.chameleonMode)) {
/* 458 */         uri = this.currentSchema.getTargetNamespace();
/*     */       }
/*     */ 
/* 461 */       return new UName(uri, qname, qname);
/*     */     }
/* 463 */     String prefix = qname.substring(0, idx);
/* 464 */     String uri = this.currentContext.resolveNamespacePrefix(prefix);
/* 465 */     if (uri == null)
/*     */     {
/* 467 */       reportError(Messages.format("UndefinedPrefix", new Object[] { prefix }));
/*     */ 
/* 469 */       uri = "undefined";
/*     */     }
/* 471 */     return new UName(uri, qname.substring(idx + 1), qname);
/*     */   }
/*     */ 
/*     */   public boolean parseBoolean(String v)
/*     */   {
/* 476 */     if (v == null) return false;
/* 477 */     v = v.trim();
/* 478 */     return (v.equals("true")) || (v.equals("1"));
/*     */   }
/*     */ 
/*     */   protected void unexpectedX(String token)
/*     */     throws SAXException
/*     */   {
/* 489 */     SAXParseException e = new SAXParseException(MessageFormat.format("Unexpected {0} appears at line {1} column {2}", new Object[] { token, 
/* 487 */       Integer.valueOf(getLocator().getLineNumber()), 
/* 488 */       Integer.valueOf(getLocator().getColumnNumber()) }), 
/* 489 */       getLocator());
/*     */ 
/* 491 */     this.parser.errorHandler.fatalError(e);
/* 492 */     throw e;
/*     */   }
/*     */ 
/*     */   public ForeignAttributesImpl parseForeignAttributes(ForeignAttributesImpl next) {
/* 496 */     ForeignAttributesImpl impl = new ForeignAttributesImpl(createValidationContext(), copyLocator(), next);
/*     */ 
/* 498 */     Attributes atts = getCurrentAttributes();
/* 499 */     for (int i = 0; i < atts.getLength(); i++) {
/* 500 */       if (atts.getURI(i).length() > 0) {
/* 501 */         impl.addAttribute(atts
/* 502 */           .getURI(i), 
/* 502 */           atts
/* 503 */           .getLocalName(i), 
/* 503 */           atts
/* 504 */           .getQName(i), 
/* 504 */           atts
/* 505 */           .getType(i), 
/* 505 */           atts
/* 506 */           .getValue(i));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 511 */     return impl;
/*     */   }
/*     */ 
/*     */   private static class Context
/*     */     implements ValidationContext
/*     */   {
/*     */     private final String prefix;
/*     */     private final String uri;
/*     */     private final Context previous;
/*     */ 
/*     */     Context(String _prefix, String _uri, Context _context)
/*     */     {
/* 395 */       this.previous = _context;
/* 396 */       this.prefix = _prefix;
/* 397 */       this.uri = _uri;
/*     */     }
/*     */ 
/*     */     public String resolveNamespacePrefix(String p) {
/* 401 */       if (p.equals(this.prefix)) return this.uri;
/* 402 */       if (this.previous == null) return null;
/* 403 */       return this.previous.resolveNamespacePrefix(p);
/*     */     }
/*     */ 
/*     */     public String getBaseUri()
/*     */     {
/* 411 */       return null; } 
/* 412 */     public boolean isNotation(String arg0) { return false; } 
/* 413 */     public boolean isUnparsedEntity(String arg0) { return false; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx
 * JD-Core Version:    0.6.2
 */