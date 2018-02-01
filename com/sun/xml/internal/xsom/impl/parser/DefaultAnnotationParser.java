/*    */ package com.sun.xml.internal.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*    */ import com.sun.xml.internal.xsom.parser.AnnotationParser;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ class DefaultAnnotationParser extends AnnotationParser
/*    */ {
/* 47 */   public static final AnnotationParser theInstance = new DefaultAnnotationParser();
/*    */ 
/*    */   public ContentHandler getContentHandler(AnnotationContext contest, String elementName, ErrorHandler errorHandler, EntityResolver entityResolver)
/*    */   {
/* 52 */     return new DefaultHandler();
/*    */   }
/*    */ 
/*    */   public Object getResult(Object existing) {
/* 56 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.DefaultAnnotationParser
 * JD-Core Version:    0.6.2
 */