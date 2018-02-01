/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class NGCCRuntime
/*     */   implements ContentHandler, NGCCEventSource
/*     */ {
/*     */   private Locator locator;
/* 123 */   private final Stack attStack = new Stack();
/*     */   private AttributesImpl currentAtts;
/* 140 */   private StringBuffer text = new StringBuffer();
/*     */   private NGCCEventReceiver currentHandler;
/*     */   static final String IMPOSSIBLE = "";
/* 429 */   private ContentHandler redirect = null;
/*     */ 
/* 435 */   private int redirectionDepth = 0;
/*     */ 
/* 478 */   private final ArrayList namespaces = new ArrayList();
/*     */ 
/* 498 */   private int nsEffectivePtr = 0;
/*     */ 
/* 503 */   private final Stack nsEffectiveStack = new Stack();
/*     */ 
/* 537 */   private int indent = 0;
/* 538 */   private boolean needIndent = true;
/*     */ 
/*     */   public NGCCRuntime()
/*     */   {
/*  60 */     reset();
/*     */   }
/*     */ 
/*     */   public void setRootHandler(NGCCHandler rootHandler)
/*     */   {
/*  80 */     if (this.currentHandler != null)
/*  81 */       throw new IllegalStateException();
/*  82 */     this.currentHandler = rootHandler;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  93 */     this.attStack.clear();
/*  94 */     this.currentAtts = null;
/*  95 */     this.currentHandler = null;
/*  96 */     this.indent = 0;
/*  97 */     this.locator = null;
/*  98 */     this.namespaces.clear();
/*  99 */     this.needIndent = true;
/* 100 */     this.redirect = null;
/* 101 */     this.redirectionDepth = 0;
/* 102 */     this.text = new StringBuffer();
/*     */ 
/* 105 */     this.attStack.push(new AttributesImpl());
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator _loc)
/*     */   {
/* 111 */     this.locator = _loc;
/*     */   }
/*     */ 
/*     */   public Locator getLocator()
/*     */   {
/* 119 */     return this.locator;
/*     */   }
/*     */ 
/*     */   public Attributes getCurrentAttributes()
/*     */   {
/* 136 */     return this.currentAtts;
/*     */   }
/*     */ 
/*     */   public int replace(NGCCEventReceiver o, NGCCEventReceiver n)
/*     */   {
/* 149 */     if (o != this.currentHandler)
/* 150 */       throw new IllegalStateException();
/* 151 */     this.currentHandler = n;
/*     */ 
/* 153 */     return 0;
/*     */   }
/*     */ 
/*     */   private void processPendingText(boolean ignorable)
/*     */     throws SAXException
/*     */   {
/* 219 */     if ((!ignorable) || (this.text.toString().trim().length() != 0))
/*     */     {
/* 222 */       this.currentHandler.text(this.text.toString());
/*     */     }
/*     */ 
/* 225 */     if (this.text.length() > 1024) this.text = new StringBuffer(); else
/* 226 */       this.text.setLength(0);
/*     */   }
/*     */ 
/*     */   public void processList(String str) throws SAXException {
/* 230 */     StringTokenizer t = new StringTokenizer(str, " \t\r\n");
/* 231 */     while (t.hasMoreTokens())
/* 232 */       this.currentHandler.text(t.nextToken());
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localname, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 238 */     if (this.redirect != null) {
/* 239 */       this.redirect.startElement(uri, localname, qname, atts);
/* 240 */       this.redirectionDepth += 1;
/*     */     } else {
/* 242 */       processPendingText(true);
/*     */ 
/* 244 */       this.currentHandler.enterElement(uri, localname, qname, atts);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onEnterElementConsumed(String uri, String localName, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 265 */     this.attStack.push(this.currentAtts = new AttributesImpl(atts));
/* 266 */     this.nsEffectiveStack.push(new Integer(this.nsEffectivePtr));
/* 267 */     this.nsEffectivePtr = this.namespaces.size();
/*     */   }
/*     */ 
/*     */   public void onLeaveElementConsumed(String uri, String localName, String qname) throws SAXException {
/* 271 */     this.attStack.pop();
/* 272 */     if (this.attStack.isEmpty())
/* 273 */       this.currentAtts = null;
/*     */     else
/* 275 */       this.currentAtts = ((AttributesImpl)this.attStack.peek());
/* 276 */     this.nsEffectivePtr = ((Integer)this.nsEffectiveStack.pop()).intValue();
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localname, String qname)
/*     */     throws SAXException
/*     */   {
/* 282 */     if (this.redirect != null) {
/* 283 */       this.redirect.endElement(uri, localname, qname);
/* 284 */       this.redirectionDepth -= 1;
/*     */ 
/* 286 */       if (this.redirectionDepth != 0) {
/* 287 */         return;
/*     */       }
/*     */ 
/* 290 */       for (int i = 0; i < this.namespaces.size(); i += 2)
/* 291 */         this.redirect.endPrefixMapping((String)this.namespaces.get(i));
/* 292 */       this.redirect.endDocument();
/*     */ 
/* 294 */       this.redirect = null;
/*     */     }
/*     */ 
/* 298 */     processPendingText(false);
/*     */ 
/* 300 */     this.currentHandler.leaveElement(uri, localname, qname);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 305 */     if (this.redirect != null)
/* 306 */       this.redirect.characters(ch, start, length);
/*     */     else
/* 308 */       this.text.append(ch, start, length); 
/*     */   }
/*     */ 
/* 311 */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { if (this.redirect != null)
/* 312 */       this.redirect.ignorableWhitespace(ch, start, length);
/*     */     else
/* 314 */       this.text.append(ch, start, length); }
/*     */ 
/*     */   public int getAttributeIndex(String uri, String localname)
/*     */   {
/* 318 */     return this.currentAtts.getIndex(uri, localname);
/*     */   }
/*     */   public void consumeAttribute(int index) throws SAXException {
/* 321 */     String uri = this.currentAtts.getURI(index);
/* 322 */     String local = this.currentAtts.getLocalName(index);
/* 323 */     String qname = this.currentAtts.getQName(index);
/* 324 */     String value = this.currentAtts.getValue(index);
/* 325 */     this.currentAtts.removeAttribute(index);
/*     */ 
/* 327 */     this.currentHandler.enterAttribute(uri, local, qname);
/* 328 */     this.currentHandler.text(value);
/* 329 */     this.currentHandler.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException
/*     */   {
/* 334 */     if (this.redirect != null) {
/* 335 */       this.redirect.startPrefixMapping(prefix, uri);
/*     */     } else {
/* 337 */       this.namespaces.add(prefix);
/* 338 */       this.namespaces.add(uri);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 343 */     if (this.redirect != null) {
/* 344 */       this.redirect.endPrefixMapping(prefix);
/*     */     } else {
/* 346 */       this.namespaces.remove(this.namespaces.size() - 1);
/* 347 */       this.namespaces.remove(this.namespaces.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name) throws SAXException {
/* 352 */     if (this.redirect != null)
/* 353 */       this.redirect.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 357 */     if (this.redirect != null)
/* 358 */       this.redirect.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 379 */     this.currentHandler.leaveElement("", "", "");
/*     */ 
/* 381 */     reset();
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 397 */     this.currentHandler.enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 403 */     this.currentHandler.enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 409 */     this.currentHandler.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 415 */     this.currentHandler.leaveElement(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 419 */     this.currentHandler.text(value);
/*     */   }
/*     */ 
/*     */   public void redirectSubtree(ContentHandler child, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 453 */     this.redirect = child;
/* 454 */     this.redirect.setDocumentLocator(this.locator);
/* 455 */     this.redirect.startDocument();
/*     */ 
/* 460 */     for (int i = 0; i < this.namespaces.size(); i += 2) {
/* 461 */       this.redirect.startPrefixMapping(
/* 462 */         (String)this.namespaces
/* 462 */         .get(i), 
/* 463 */         (String)this.namespaces
/* 463 */         .get(i + 1));
/*     */     }
/*     */ 
/* 466 */     this.redirect.startElement(uri, local, qname, this.currentAtts);
/* 467 */     this.redirectionDepth = 1;
/*     */   }
/*     */ 
/*     */   public String resolveNamespacePrefix(String prefix)
/*     */   {
/* 506 */     for (int i = this.nsEffectivePtr - 2; i >= 0; i -= 2) {
/* 507 */       if (this.namespaces.get(i).equals(prefix)) {
/* 508 */         return (String)this.namespaces.get(i + 1);
/*     */       }
/*     */     }
/* 511 */     if (prefix.equals("")) return "";
/* 512 */     if (prefix.equals("xml"))
/* 513 */       return "http://www.w3.org/XML/1998/namespace";
/* 514 */     return null;
/*     */   }
/*     */ 
/*     */   protected void unexpectedX(String token)
/*     */     throws SAXException
/*     */   {
/* 526 */     throw new SAXParseException(MessageFormat.format("Unexpected {0} appears at line {1} column {2}", new Object[] { token, new Integer(
/* 524 */       getLocator().getLineNumber()), new Integer(
/* 525 */       getLocator().getColumnNumber()) }), 
/* 526 */       getLocator());
/*     */   }
/*     */ 
/*     */   private void printIndent()
/*     */   {
/* 540 */     for (int i = 0; i < this.indent; i++)
/* 541 */       System.out.print("  "); 
/*     */   }
/*     */ 
/* 544 */   public void trace(String s) { if (this.needIndent) {
/* 545 */       this.needIndent = false;
/* 546 */       printIndent();
/*     */     }
/* 548 */     System.out.print(s); }
/*     */ 
/*     */   public void traceln(String s) {
/* 551 */     trace(s);
/* 552 */     trace("\n");
/* 553 */     this.needIndent = true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.NGCCRuntime
 * JD-Core Version:    0.6.2
 */