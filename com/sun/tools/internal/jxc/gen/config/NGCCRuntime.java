/*     */ package com.sun.tools.internal.jxc.gen.config;
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
/* 125 */   private final Stack attStack = new Stack();
/*     */   private AttributesImpl currentAtts;
/* 142 */   private StringBuffer text = new StringBuffer();
/*     */   private NGCCEventReceiver currentHandler;
/*     */   static final String IMPOSSIBLE = "";
/* 431 */   private ContentHandler redirect = null;
/*     */ 
/* 437 */   private int redirectionDepth = 0;
/*     */ 
/* 480 */   private final ArrayList namespaces = new ArrayList();
/*     */ 
/* 500 */   private int nsEffectivePtr = 0;
/*     */ 
/* 505 */   private final Stack nsEffectiveStack = new Stack();
/*     */ 
/* 539 */   private int indent = 0;
/* 540 */   private boolean needIndent = true;
/*     */ 
/*     */   public NGCCRuntime()
/*     */   {
/*  62 */     reset();
/*     */   }
/*     */ 
/*     */   public void setRootHandler(NGCCHandler rootHandler)
/*     */   {
/*  82 */     if (this.currentHandler != null)
/*  83 */       throw new IllegalStateException();
/*  84 */     this.currentHandler = rootHandler;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  95 */     this.attStack.clear();
/*  96 */     this.currentAtts = null;
/*  97 */     this.currentHandler = null;
/*  98 */     this.indent = 0;
/*  99 */     this.locator = null;
/* 100 */     this.namespaces.clear();
/* 101 */     this.needIndent = true;
/* 102 */     this.redirect = null;
/* 103 */     this.redirectionDepth = 0;
/* 104 */     this.text = new StringBuffer();
/*     */ 
/* 107 */     this.attStack.push(new AttributesImpl());
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator _loc)
/*     */   {
/* 113 */     this.locator = _loc;
/*     */   }
/*     */ 
/*     */   public Locator getLocator()
/*     */   {
/* 121 */     return this.locator;
/*     */   }
/*     */ 
/*     */   public Attributes getCurrentAttributes()
/*     */   {
/* 138 */     return this.currentAtts;
/*     */   }
/*     */ 
/*     */   public int replace(NGCCEventReceiver o, NGCCEventReceiver n)
/*     */   {
/* 151 */     if (o != this.currentHandler)
/* 152 */       throw new IllegalStateException();
/* 153 */     this.currentHandler = n;
/*     */ 
/* 155 */     return 0;
/*     */   }
/*     */ 
/*     */   private void processPendingText(boolean ignorable)
/*     */     throws SAXException
/*     */   {
/* 221 */     if ((!ignorable) || (this.text.toString().trim().length() != 0))
/*     */     {
/* 224 */       this.currentHandler.text(this.text.toString());
/*     */     }
/*     */ 
/* 227 */     if (this.text.length() > 1024) this.text = new StringBuffer(); else
/* 228 */       this.text.setLength(0);
/*     */   }
/*     */ 
/*     */   public void processList(String str) throws SAXException {
/* 232 */     StringTokenizer t = new StringTokenizer(str, " \t\r\n");
/* 233 */     while (t.hasMoreTokens())
/* 234 */       this.currentHandler.text(t.nextToken());
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localname, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 240 */     if (this.redirect != null) {
/* 241 */       this.redirect.startElement(uri, localname, qname, atts);
/* 242 */       this.redirectionDepth += 1;
/*     */     } else {
/* 244 */       processPendingText(true);
/*     */ 
/* 246 */       this.currentHandler.enterElement(uri, localname, qname, atts);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onEnterElementConsumed(String uri, String localName, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 267 */     this.attStack.push(this.currentAtts = new AttributesImpl(atts));
/* 268 */     this.nsEffectiveStack.push(new Integer(this.nsEffectivePtr));
/* 269 */     this.nsEffectivePtr = this.namespaces.size();
/*     */   }
/*     */ 
/*     */   public void onLeaveElementConsumed(String uri, String localName, String qname) throws SAXException {
/* 273 */     this.attStack.pop();
/* 274 */     if (this.attStack.isEmpty())
/* 275 */       this.currentAtts = null;
/*     */     else
/* 277 */       this.currentAtts = ((AttributesImpl)this.attStack.peek());
/* 278 */     this.nsEffectivePtr = ((Integer)this.nsEffectiveStack.pop()).intValue();
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localname, String qname)
/*     */     throws SAXException
/*     */   {
/* 284 */     if (this.redirect != null) {
/* 285 */       this.redirect.endElement(uri, localname, qname);
/* 286 */       this.redirectionDepth -= 1;
/*     */ 
/* 288 */       if (this.redirectionDepth != 0) {
/* 289 */         return;
/*     */       }
/*     */ 
/* 292 */       for (int i = 0; i < this.namespaces.size(); i += 2)
/* 293 */         this.redirect.endPrefixMapping((String)this.namespaces.get(i));
/* 294 */       this.redirect.endDocument();
/*     */ 
/* 296 */       this.redirect = null;
/*     */     }
/*     */ 
/* 300 */     processPendingText(false);
/*     */ 
/* 302 */     this.currentHandler.leaveElement(uri, localname, qname);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 307 */     if (this.redirect != null)
/* 308 */       this.redirect.characters(ch, start, length);
/*     */     else
/* 310 */       this.text.append(ch, start, length); 
/*     */   }
/*     */ 
/* 313 */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { if (this.redirect != null)
/* 314 */       this.redirect.ignorableWhitespace(ch, start, length);
/*     */     else
/* 316 */       this.text.append(ch, start, length); }
/*     */ 
/*     */   public int getAttributeIndex(String uri, String localname)
/*     */   {
/* 320 */     return this.currentAtts.getIndex(uri, localname);
/*     */   }
/*     */   public void consumeAttribute(int index) throws SAXException {
/* 323 */     String uri = this.currentAtts.getURI(index);
/* 324 */     String local = this.currentAtts.getLocalName(index);
/* 325 */     String qname = this.currentAtts.getQName(index);
/* 326 */     String value = this.currentAtts.getValue(index);
/* 327 */     this.currentAtts.removeAttribute(index);
/*     */ 
/* 329 */     this.currentHandler.enterAttribute(uri, local, qname);
/* 330 */     this.currentHandler.text(value);
/* 331 */     this.currentHandler.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException
/*     */   {
/* 336 */     if (this.redirect != null) {
/* 337 */       this.redirect.startPrefixMapping(prefix, uri);
/*     */     } else {
/* 339 */       this.namespaces.add(prefix);
/* 340 */       this.namespaces.add(uri);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 345 */     if (this.redirect != null) {
/* 346 */       this.redirect.endPrefixMapping(prefix);
/*     */     } else {
/* 348 */       this.namespaces.remove(this.namespaces.size() - 1);
/* 349 */       this.namespaces.remove(this.namespaces.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name) throws SAXException {
/* 354 */     if (this.redirect != null)
/* 355 */       this.redirect.skippedEntity(name);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 359 */     if (this.redirect != null)
/* 360 */       this.redirect.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 381 */     this.currentHandler.leaveElement("", "", "");
/*     */ 
/* 383 */     reset();
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 399 */     this.currentHandler.enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 405 */     this.currentHandler.enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 411 */     this.currentHandler.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 417 */     this.currentHandler.leaveElement(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 421 */     this.currentHandler.text(value);
/*     */   }
/*     */ 
/*     */   public void redirectSubtree(ContentHandler child, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 455 */     this.redirect = child;
/* 456 */     this.redirect.setDocumentLocator(this.locator);
/* 457 */     this.redirect.startDocument();
/*     */ 
/* 462 */     for (int i = 0; i < this.namespaces.size(); i += 2) {
/* 463 */       this.redirect.startPrefixMapping(
/* 464 */         (String)this.namespaces
/* 464 */         .get(i), 
/* 465 */         (String)this.namespaces
/* 465 */         .get(i + 1));
/*     */     }
/*     */ 
/* 468 */     this.redirect.startElement(uri, local, qname, this.currentAtts);
/* 469 */     this.redirectionDepth = 1;
/*     */   }
/*     */ 
/*     */   public String resolveNamespacePrefix(String prefix)
/*     */   {
/* 508 */     for (int i = this.nsEffectivePtr - 2; i >= 0; i -= 2) {
/* 509 */       if (this.namespaces.get(i).equals(prefix)) {
/* 510 */         return (String)this.namespaces.get(i + 1);
/*     */       }
/*     */     }
/* 513 */     if (prefix.equals("")) return "";
/* 514 */     if (prefix.equals("xml"))
/* 515 */       return "http://www.w3.org/XML/1998/namespace";
/* 516 */     return null;
/*     */   }
/*     */ 
/*     */   protected void unexpectedX(String token)
/*     */     throws SAXException
/*     */   {
/* 528 */     throw new SAXParseException(MessageFormat.format("Unexpected {0} appears at line {1} column {2}", new Object[] { token, new Integer(
/* 526 */       getLocator().getLineNumber()), new Integer(
/* 527 */       getLocator().getColumnNumber()) }), 
/* 528 */       getLocator());
/*     */   }
/*     */ 
/*     */   private void printIndent()
/*     */   {
/* 542 */     for (int i = 0; i < this.indent; i++)
/* 543 */       System.out.print("  "); 
/*     */   }
/*     */ 
/* 546 */   public void trace(String s) { if (this.needIndent) {
/* 547 */       this.needIndent = false;
/* 548 */       printIndent();
/*     */     }
/* 550 */     System.out.print(s); }
/*     */ 
/*     */   public void traceln(String s) {
/* 553 */     trace(s);
/* 554 */     trace("\n");
/* 555 */     this.needIndent = true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.gen.config.NGCCRuntime
 * JD-Core Version:    0.6.2
 */