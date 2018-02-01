/*    */ package com.sun.xml.internal.rngom.xml.sax;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXNotRecognizedException;
/*    */ import org.xml.sax.SAXNotSupportedException;
/*    */ import org.xml.sax.XMLReader;
/*    */ 
/*    */ public class JAXPXMLReaderCreator
/*    */   implements XMLReaderCreator
/*    */ {
/*    */   private final SAXParserFactory spf;
/*    */ 
/*    */   public JAXPXMLReaderCreator(SAXParserFactory spf)
/*    */   {
/* 71 */     this.spf = spf;
/*    */   }
/*    */ 
/*    */   public JAXPXMLReaderCreator()
/*    */   {
/* 79 */     this.spf = SAXParserFactory.newInstance();
/*    */     try {
/* 81 */       this.spf.setNamespaceAware(true);
/* 82 */       this.spf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*    */     } catch (ParserConfigurationException ex) {
/* 84 */       Logger.getLogger(JAXPXMLReaderCreator.class.getName()).log(Level.SEVERE, null, ex);
/*    */     } catch (SAXNotRecognizedException ex) {
/* 86 */       Logger.getLogger(JAXPXMLReaderCreator.class.getName()).log(Level.SEVERE, null, ex);
/*    */     } catch (SAXNotSupportedException ex) {
/* 88 */       Logger.getLogger(JAXPXMLReaderCreator.class.getName()).log(Level.SEVERE, null, ex);
/*    */     }
/*    */   }
/*    */ 
/*    */   public XMLReader createXMLReader()
/*    */     throws SAXException
/*    */   {
/*    */     try
/*    */     {
/* 97 */       return this.spf.newSAXParser().getXMLReader();
/*    */     } catch (ParserConfigurationException e) {
/* 99 */       throw new SAXException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.xml.sax.JAXPXMLReaderCreator
 * JD-Core Version:    0.6.2
 */