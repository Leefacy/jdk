/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class LayoutParser extends DefaultHandler
/*     */ {
/*     */   private Map<String, XMLNode> xmlElementsMap;
/*     */   private XMLNode currentNode;
/*     */   private final Configuration configuration;
/*     */   private String currentRoot;
/*     */   private boolean isParsing;
/*     */ 
/*     */   private LayoutParser(Configuration paramConfiguration)
/*     */   {
/*  63 */     this.xmlElementsMap = new HashMap();
/*  64 */     this.configuration = paramConfiguration;
/*     */   }
/*     */ 
/*     */   public static LayoutParser getInstance(Configuration paramConfiguration)
/*     */   {
/*  74 */     return new LayoutParser(paramConfiguration);
/*     */   }
/*     */ 
/*     */   public XMLNode parseXML(String paramString)
/*     */   {
/*  83 */     if (this.xmlElementsMap.containsKey(paramString))
/*  84 */       return (XMLNode)this.xmlElementsMap.get(paramString);
/*     */     try
/*     */     {
/*  87 */       this.currentRoot = paramString;
/*  88 */       this.isParsing = false;
/*  89 */       SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
/*  90 */       SAXParser localSAXParser = localSAXParserFactory.newSAXParser();
/*  91 */       InputStream localInputStream = this.configuration.getBuilderXML();
/*  92 */       localSAXParser.parse(localInputStream, this);
/*  93 */       return (XMLNode)this.xmlElementsMap.get(paramString);
/*     */     } catch (Throwable localThrowable) {
/*  95 */       localThrowable.printStackTrace();
/*  96 */       throw new DocletAbortException(localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
/*     */     throws SAXException
/*     */   {
/* 107 */     if ((this.isParsing) || (paramString3.equals(this.currentRoot))) {
/* 108 */       this.isParsing = true;
/* 109 */       this.currentNode = new XMLNode(this.currentNode, paramString3);
/* 110 */       for (int i = 0; i < paramAttributes.getLength(); i++)
/* 111 */         this.currentNode.attrs.put(paramAttributes.getLocalName(i), paramAttributes.getValue(i));
/* 112 */       if (paramString3.equals(this.currentRoot))
/* 113 */         this.xmlElementsMap.put(paramString3, this.currentNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String paramString1, String paramString2, String paramString3)
/*     */     throws SAXException
/*     */   {
/* 123 */     if (!this.isParsing) {
/* 124 */       return;
/*     */     }
/* 126 */     this.currentNode = this.currentNode.parent;
/* 127 */     this.isParsing = (!paramString3.equals(this.currentRoot));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.LayoutParser
 * JD-Core Version:    0.6.2
 */