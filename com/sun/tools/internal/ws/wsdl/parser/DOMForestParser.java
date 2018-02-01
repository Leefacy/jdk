/*    */ package com.sun.tools.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*    */ import java.io.IOException;
/*    */ import org.w3c.dom.Document;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class DOMForestParser
/*    */   implements XMLParser
/*    */ {
/*    */   private final DOMForest forest;
/*    */   private final DOMForestScanner scanner;
/*    */   private final XMLParser fallbackParser;
/*    */ 
/*    */   public DOMForestParser(DOMForest forest, XMLParser fallbackParser)
/*    */   {
/* 66 */     this.forest = forest;
/* 67 */     this.scanner = new DOMForestScanner(forest);
/* 68 */     this.fallbackParser = fallbackParser;
/*    */   }
/*    */ 
/*    */   public void parse(InputSource source, ContentHandler handler, EntityResolver entityResolver, ErrorHandler errHandler)
/*    */     throws SAXException, IOException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver)
/*    */     throws SAXException, IOException
/*    */   {
/* 79 */     String systemId = source.getSystemId();
/* 80 */     Document dom = this.forest.get(systemId);
/*    */ 
/* 82 */     if (dom == null)
/*    */     {
/* 88 */       this.fallbackParser.parse(source, handler, errorHandler, entityResolver);
/* 89 */       return;
/*    */     }
/*    */ 
/* 92 */     this.scanner.scan(dom, handler);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.parser.DOMForestParser
 * JD-Core Version:    0.6.2
 */