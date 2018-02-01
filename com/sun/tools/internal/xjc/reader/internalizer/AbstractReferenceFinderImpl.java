/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public abstract class AbstractReferenceFinderImpl extends XMLFilterImpl
/*     */ {
/*     */   protected final DOMForest parent;
/*     */   private Locator locator;
/*     */ 
/*     */   protected AbstractReferenceFinderImpl(DOMForest _parent)
/*     */   {
/*  55 */     this.parent = _parent;
/*     */   }
/*     */ 
/*     */   protected abstract String findExternalResource(String paramString1, String paramString2, Attributes paramAttributes);
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*  71 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */ 
/*  73 */     String relativeRef = findExternalResource(namespaceURI, localName, atts);
/*  74 */     if (relativeRef == null) {
/*  75 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  79 */       String lsi = this.locator.getSystemId();
/*     */ 
/*  81 */       URI relRefURI = new URI(relativeRef);
/*     */       String ref;
/*     */       String ref;
/*  82 */       if (relRefURI.isAbsolute()) {
/*  83 */         ref = relativeRef;
/*     */       }
/*     */       else
/*     */       {
/*     */         String ref;
/*  85 */         if (lsi.startsWith("jar:")) {
/*  86 */           int bangIdx = lsi.indexOf('!');
/*     */           String ref;
/*  87 */           if (bangIdx > 0)
/*     */           {
/*  89 */             ref = lsi.substring(0, bangIdx + 1) + new URI(lsi
/*  89 */               .substring(bangIdx + 1))
/*  89 */               .resolve(new URI(relativeRef)).toString();
/*     */           }
/*  91 */           else ref = relativeRef; 
/*     */         }
/*     */         else
/*     */         {
/*  94 */           ref = new URI(lsi).resolve(new URI(relativeRef)).toString();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 100 */       if (this.parent != null)
/* 101 */         this.parent.parse(ref, false);
/*     */     }
/*     */     catch (URISyntaxException e) {
/* 104 */       String msg = e.getMessage();
/* 105 */       if (new File(relativeRef).exists()) {
/* 106 */         msg = Messages.format("ERR_FILENAME_IS_NOT_URI", new Object[0]) + ' ' + msg;
/*     */       }
/*     */ 
/* 110 */       SAXParseException spe = new SAXParseException2(
/* 110 */         Messages.format("AbstractReferenceFinderImpl.UnableToParse", new Object[] { relativeRef, msg }), 
/* 110 */         this.locator, e);
/*     */ 
/* 113 */       fatalError(spe);
/* 114 */       throw spe;
/*     */     }
/*     */     catch (IOException e) {
/* 117 */       SAXParseException spe = new SAXParseException2(
/* 117 */         Messages.format("AbstractReferenceFinderImpl.UnableToParse", new Object[] { relativeRef, e
/* 117 */         .getMessage() }), this.locator, e);
/*     */ 
/* 120 */       fatalError(spe);
/* 121 */       throw spe;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 129 */     super.setDocumentLocator(locator);
/* 130 */     this.locator = locator;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.internalizer.AbstractReferenceFinderImpl
 * JD-Core Version:    0.6.2
 */