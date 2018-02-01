/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class IncorrectNamespaceURIChecker extends XMLFilterImpl
/*     */ {
/*     */   private ErrorHandler errorHandler;
/*  74 */   private Locator locator = null;
/*     */ 
/*  77 */   private boolean isJAXBPrefixUsed = false;
/*     */ 
/*  79 */   private boolean isCustomizationUsed = false;
/*     */ 
/*     */   public IncorrectNamespaceURIChecker(ErrorHandler handler)
/*     */   {
/*  69 */     this.errorHandler = handler;
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*  83 */     if ((this.isJAXBPrefixUsed) && (!this.isCustomizationUsed))
/*     */     {
/*  85 */       SAXParseException e = new SAXParseException(
/*  85 */         Messages.format("IncorrectNamespaceURIChecker.WarnIncorrectURI", new Object[] { "http://java.sun.com/xml/ns/jaxb" }), 
/*  85 */         this.locator);
/*     */ 
/*  87 */       this.errorHandler.warning(e);
/*     */     }
/*     */ 
/*  90 */     super.endDocument();
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException
/*     */   {
/*  95 */     if ("http://www.w3.org/XML/1998/namespace".equals(uri)) return;
/*  96 */     if (prefix.equals("jaxb"))
/*  97 */       this.isJAXBPrefixUsed = true;
/*  98 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/*  99 */       this.isCustomizationUsed = true;
/*     */     }
/* 101 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException
/*     */   {
/* 106 */     if ("xml".equals(prefix)) return;
/* 107 */     super.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 113 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */ 
/* 120 */     if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb"))
/* 121 */       this.isCustomizationUsed = true;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 126 */     super.setDocumentLocator(locator);
/* 127 */     this.locator = locator;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.parser.IncorrectNamespaceURIChecker
 * JD-Core Version:    0.6.2
 */