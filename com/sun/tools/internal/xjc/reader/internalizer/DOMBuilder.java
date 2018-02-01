/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ class DOMBuilder extends SAX2DOMEx
/*     */ {
/*     */   private final LocatorTable locatorTable;
/*     */   private final Set outerMostBindings;
/*     */   private Locator locator;
/*     */ 
/*     */   public DOMBuilder(Document dom, LocatorTable ltable, Set outerMostBindings)
/*     */   {
/*  59 */     super(dom);
/*  60 */     this.locatorTable = ltable;
/*  61 */     this.outerMostBindings = outerMostBindings;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*  72 */     this.locator = locator;
/*  73 */     super.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */   {
/*  78 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */ 
/*  80 */     Element e = getCurrentElement();
/*  81 */     this.locatorTable.storeStartLocation(e, this.locator);
/*     */ 
/*  84 */     if (("http://java.sun.com/xml/ns/jaxb".equals(e.getNamespaceURI())) && 
/*  85 */       ("bindings"
/*  85 */       .equals(e
/*  85 */       .getLocalName())))
/*     */     {
/*  90 */       Node p = e.getParentNode();
/*  91 */       if (((p instanceof Document)) || (((p instanceof Element)) && 
/*  92 */         (!e
/*  92 */         .getNamespaceURI().equals(p.getNamespaceURI()))))
/*  93 */         this.outerMostBindings.add(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */   {
/*  99 */     this.locatorTable.storeEndLocation(getCurrentElement(), this.locator);
/* 100 */     super.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.internalizer.DOMBuilder
 * JD-Core Version:    0.6.2
 */