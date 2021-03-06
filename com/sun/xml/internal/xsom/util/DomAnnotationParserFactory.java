/*     */ package com.sun.xml.internal.xsom.util;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParser;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParserFactory;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.sax.SAXTransformerFactory;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ 
/*     */ public class DomAnnotationParserFactory
/*     */   implements AnnotationParserFactory
/*     */ {
/*  69 */   private static final ContextClassloaderLocal<SAXTransformerFactory> stf = new ContextClassloaderLocal()
/*     */   {
/*     */     protected SAXTransformerFactory initialValue() throws Exception {
/*  72 */       return (SAXTransformerFactory)SAXTransformerFactory.newInstance();
/*     */     }
/*  69 */   };
/*     */ 
/*     */   public AnnotationParser create()
/*     */   {
/*  62 */     return new AnnotationParserImpl();
/*     */   }
/*     */ 
/*     */   public AnnotationParser create(boolean disableSecureProcessing) {
/*  66 */     return new AnnotationParserImpl(disableSecureProcessing);
/*     */   }
/*     */ 
/*     */   private static class AnnotationParserImpl extends AnnotationParser
/*     */   {
/*     */     private final TransformerHandler transformer;
/*     */     private DOMResult result;
/*     */ 
/*     */     AnnotationParserImpl()
/*     */     {
/*  85 */       this(false);
/*     */     }
/*     */ 
/*     */     AnnotationParserImpl(boolean disableSecureProcessing) {
/*     */       try {
/*  90 */         SAXTransformerFactory factory = (SAXTransformerFactory)DomAnnotationParserFactory.stf.get();
/*  91 */         factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", disableSecureProcessing);
/*  92 */         this.transformer = factory.newTransformerHandler();
/*     */       } catch (TransformerConfigurationException e) {
/*  94 */         throw new Error(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public ContentHandler getContentHandler(AnnotationContext context, String parentElementName, ErrorHandler errorHandler, EntityResolver entityResolver) {
/*  99 */       this.result = new DOMResult();
/* 100 */       this.transformer.setResult(this.result);
/* 101 */       return this.transformer;
/*     */     }
/*     */ 
/*     */     public Object getResult(Object existing) {
/* 105 */       Document dom = (Document)this.result.getNode();
/* 106 */       Element e = dom.getDocumentElement();
/* 107 */       if ((existing instanceof Element))
/*     */       {
/* 109 */         Element prev = (Element)existing;
/* 110 */         Node anchor = e.getFirstChild();
/* 111 */         while (prev.getFirstChild() != null) {
/* 112 */           Node move = prev.getFirstChild();
/* 113 */           e.insertBefore(e.getOwnerDocument().adoptNode(move), anchor);
/*     */         }
/*     */       }
/* 116 */       return e;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.util.DomAnnotationParserFactory
 * JD-Core Version:    0.6.2
 */