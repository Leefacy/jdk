/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ final class DOMBuilder extends SAX2DOMEx
/*    */ {
/*    */   private Locator locator;
/*    */ 
/*    */   public DOMBuilder(DocumentBuilderFactory f)
/*    */     throws ParserConfigurationException
/*    */   {
/* 43 */     super(f);
/*    */   }
/*    */ 
/*    */   public void setDocumentLocator(Locator locator)
/*    */   {
/* 48 */     super.setDocumentLocator(locator);
/* 49 */     this.locator = locator;
/*    */   }
/*    */ 
/*    */   public void startElement(String namespace, String localName, String qName, Attributes attrs)
/*    */   {
/* 54 */     super.startElement(namespace, localName, qName, attrs);
/* 55 */     DOMLocator.setLocationInfo(getCurrentElement(), this.locator);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.DOMBuilder
 * JD-Core Version:    0.6.2
 */