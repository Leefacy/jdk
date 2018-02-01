/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ class WhitespaceStripper extends XMLFilterImpl
/*     */ {
/*  50 */   private int state = 0;
/*     */ 
/*  52 */   private char[] buf = new char[1024];
/*  53 */   private int bufLen = 0;
/*     */   private static final int AFTER_START_ELEMENT = 1;
/*     */   private static final int AFTER_END_ELEMENT = 2;
/*     */ 
/*     */   public WhitespaceStripper(XMLReader reader)
/*     */   {
/*  59 */     setParent(reader);
/*     */   }
/*     */ 
/*     */   public WhitespaceStripper(ContentHandler handler, ErrorHandler eh, EntityResolver er) {
/*  63 */     setContentHandler(handler);
/*  64 */     if (eh != null) setErrorHandler(eh);
/*  65 */     if (er != null) setEntityResolver(er); 
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/*  69 */     switch (this.state)
/*     */     {
/*     */     case 1:
/*  75 */       if (this.bufLen + length > this.buf.length)
/*     */       {
/*  77 */         char[] newBuf = new char[Math.max(this.bufLen + length, this.buf.length * 2)];
/*  78 */         System.arraycopy(this.buf, 0, newBuf, 0, this.bufLen);
/*  79 */         this.buf = newBuf;
/*     */       }
/*  81 */       System.arraycopy(ch, start, this.buf, this.bufLen, length);
/*  82 */       this.bufLen += length;
/*  83 */       break;
/*     */     case 2:
/*  86 */       int len = start + length;
/*  87 */       for (int i = start; i < len; i++)
/*  88 */         if (!WhiteSpaceProcessor.isWhiteSpace(ch[i])) {
/*  89 */           super.characters(ch, start, length);
/*  90 */           return;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*  98 */     processPendingText();
/*  99 */     super.startElement(uri, localName, qName, atts);
/* 100 */     this.state = 1;
/* 101 */     this.bufLen = 0;
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 105 */     processPendingText();
/* 106 */     super.endElement(uri, localName, qName);
/* 107 */     this.state = 2;
/*     */   }
/*     */ 
/*     */   private void processPendingText()
/*     */     throws SAXException
/*     */   {
/* 115 */     if (this.state == 1)
/* 116 */       for (int i = this.bufLen - 1; i >= 0; i--)
/* 117 */         if (!WhiteSpaceProcessor.isWhiteSpace(this.buf[i])) {
/* 118 */           super.characters(this.buf, 0, this.bufLen);
/* 119 */           return;
/*     */         }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.internalizer.WhitespaceStripper
 * JD-Core Version:    0.6.2
 */