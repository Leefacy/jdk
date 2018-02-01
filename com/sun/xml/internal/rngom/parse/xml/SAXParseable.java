/*     */ package com.sun.xml.internal.rngom.parse.xml;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import com.sun.xml.internal.rngom.xml.sax.JAXPXMLReaderCreator;
/*     */ import com.sun.xml.internal.rngom.xml.sax.XMLReaderCreator;
/*     */ import java.io.IOException;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class SAXParseable
/*     */   implements Parseable
/*     */ {
/*     */   private final InputSource in;
/*     */   final XMLReaderCreator xrc;
/*     */   final ErrorHandler eh;
/*     */ 
/*     */   public SAXParseable(InputSource in, ErrorHandler eh, XMLReaderCreator xrc)
/*     */   {
/*  76 */     this.xrc = xrc;
/*  77 */     this.eh = eh;
/*  78 */     this.in = in;
/*     */   }
/*     */ 
/*     */   public SAXParseable(InputSource in, ErrorHandler eh) {
/*  82 */     this(in, eh, new JAXPXMLReaderCreator());
/*     */   }
/*     */ 
/*     */   public ParsedPattern parse(SchemaBuilder schemaBuilder) throws BuildException, IllegalSchemaException {
/*     */     try {
/*  87 */       XMLReader xr = this.xrc.createXMLReader();
/*  88 */       SchemaParser sp = new SchemaParser(this, xr, this.eh, schemaBuilder, null, null, "");
/*  89 */       xr.parse(this.in);
/*  90 */       ParsedPattern p = sp.getParsedPattern();
/*  91 */       return schemaBuilder.expandPattern(p);
/*     */     }
/*     */     catch (SAXException e) {
/*  94 */       throw toBuildException(e);
/*     */     }
/*     */     catch (IOException e) {
/*  97 */       throw new BuildException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ParsedPattern parseInclude(String uri, SchemaBuilder schemaBuilder, IncludedGrammar g, String inheritedNs) throws BuildException, IllegalSchemaException
/*     */   {
/*     */     try {
/* 104 */       XMLReader xr = this.xrc.createXMLReader();
/* 105 */       SchemaParser sp = new SchemaParser(this, xr, this.eh, schemaBuilder, g, g, inheritedNs);
/* 106 */       xr.parse(makeInputSource(xr, uri));
/* 107 */       return sp.getParsedPattern();
/*     */     }
/*     */     catch (SAXException e) {
/* 110 */       throw toBuildException(e);
/*     */     }
/*     */     catch (IOException e) {
/* 113 */       throw new BuildException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ParsedPattern parseExternal(String uri, SchemaBuilder schemaBuilder, Scope s, String inheritedNs) throws BuildException, IllegalSchemaException
/*     */   {
/*     */     try {
/* 120 */       XMLReader xr = this.xrc.createXMLReader();
/* 121 */       SchemaParser sp = new SchemaParser(this, xr, this.eh, schemaBuilder, null, s, inheritedNs);
/* 122 */       xr.parse(makeInputSource(xr, uri));
/* 123 */       return sp.getParsedPattern();
/*     */     }
/*     */     catch (SAXException e) {
/* 126 */       throw toBuildException(e);
/*     */     }
/*     */     catch (IOException e) {
/* 129 */       throw new BuildException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static InputSource makeInputSource(XMLReader xr, String systemId) throws IOException, SAXException {
/* 134 */     EntityResolver er = xr.getEntityResolver();
/* 135 */     if (er != null) {
/* 136 */       InputSource inputSource = er.resolveEntity(null, systemId);
/* 137 */       if (inputSource != null)
/* 138 */         return inputSource;
/*     */     }
/* 140 */     return new InputSource(systemId);
/*     */   }
/*     */ 
/*     */   static BuildException toBuildException(SAXException e) {
/* 144 */     Exception inner = e.getException();
/* 145 */     if ((inner instanceof BuildException))
/* 146 */       throw ((BuildException)inner);
/* 147 */     throw new BuildException(e);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.xml.SAXParseable
 * JD-Core Version:    0.6.2
 */