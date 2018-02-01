/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.impl.ElementDecl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaSetImpl;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import com.sun.xml.internal.xsom.parser.XSOMParser;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class ParserContext
/*     */ {
/*  60 */   public final SchemaSetImpl schemaSet = new SchemaSetImpl();
/*     */   private final XSOMParser owner;
/*     */   final XMLParser parser;
/*  67 */   private final Vector<Patch> patchers = new Vector();
/*  68 */   private final Vector<Patch> errorCheckers = new Vector();
/*     */ 
/*  77 */   public final Map<SchemaDocumentImpl, SchemaDocumentImpl> parsedDocuments = new HashMap();
/*     */ 
/* 145 */   private boolean hadError = false;
/*     */ 
/* 154 */   final PatcherManager patcherManager = new PatcherManager() {
/*     */     public void addPatcher(Patch patch) {
/* 156 */       ParserContext.this.patchers.add(patch);
/*     */     }
/*     */     public void addErrorChecker(Patch patch) {
/* 159 */       ParserContext.this.errorCheckers.add(patch);
/*     */     }
/*     */ 
/*     */     public void reportError(String msg, Locator src) throws SAXException {
/* 163 */       ParserContext.this.setErrorFlag();
/*     */ 
/* 165 */       SAXParseException e = new SAXParseException(msg, src);
/* 166 */       if (ParserContext.this.errorHandler == null) {
/* 167 */         throw e;
/*     */       }
/* 169 */       ParserContext.this.errorHandler.error(e);
/*     */     }
/* 154 */   };
/*     */ 
/* 177 */   final ErrorHandler errorHandler = new ErrorHandler() {
/*     */     private ErrorHandler getErrorHandler() {
/* 179 */       if (ParserContext.this.owner.getErrorHandler() == null) {
/* 180 */         return ParserContext.this.noopHandler;
/*     */       }
/* 182 */       return ParserContext.this.owner.getErrorHandler();
/*     */     }
/*     */ 
/*     */     public void warning(SAXParseException e) throws SAXException {
/* 186 */       getErrorHandler().warning(e);
/*     */     }
/*     */ 
/*     */     public void error(SAXParseException e) throws SAXException {
/* 190 */       ParserContext.this.setErrorFlag();
/* 191 */       getErrorHandler().error(e);
/*     */     }
/*     */ 
/*     */     public void fatalError(SAXParseException e) throws SAXException {
/* 195 */       ParserContext.this.setErrorFlag();
/* 196 */       getErrorHandler().fatalError(e);
/*     */     }
/* 177 */   };
/*     */ 
/* 203 */   final ErrorHandler noopHandler = new ErrorHandler() {
/*     */     public void warning(SAXParseException e) {
/*     */     }
/*     */     public void error(SAXParseException e) {
/*     */     }
/*     */     public void fatalError(SAXParseException e) {
/* 209 */       ParserContext.this.setErrorFlag();
/*     */     }
/* 203 */   };
/*     */ 
/*     */   public ParserContext(XSOMParser owner, XMLParser parser)
/*     */   {
/*  81 */     this.owner = owner;
/*  82 */     this.parser = parser;
/*     */     try
/*     */     {
/*  85 */       parse(new InputSource(ParserContext.class.getResource("datatypes.xsd").toExternalForm()));
/*     */ 
/*  88 */       SchemaImpl xs = (SchemaImpl)this.schemaSet
/*  88 */         .getSchema("http://www.w3.org/2001/XMLSchema");
/*     */ 
/*  89 */       xs.addSimpleType(this.schemaSet.anySimpleType, true);
/*  90 */       xs.addComplexType(this.schemaSet.anyType, true);
/*     */     }
/*     */     catch (SAXException e) {
/*  93 */       if (e.getException() != null)
/*  94 */         e.getException().printStackTrace();
/*     */       else
/*  96 */         e.printStackTrace();
/*  97 */       throw new InternalError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver() {
/* 102 */     return this.owner.getEntityResolver();
/*     */   }
/*     */ 
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/* 106 */     return this.owner.getAnnotationParserFactory();
/*     */   }
/*     */ 
/*     */   public void parse(InputSource source)
/*     */     throws SAXException
/*     */   {
/* 113 */     newNGCCRuntime().parseEntity(source, false, null, null);
/*     */   }
/*     */ 
/*     */   public XSSchemaSet getResult()
/*     */     throws SAXException
/*     */   {
/* 119 */     for (Iterator localIterator1 = this.patchers.iterator(); localIterator1.hasNext(); ) { patcher = (Patch)localIterator1.next();
/* 120 */       patcher.run();
/*     */     }
/*     */     Patch patcher;
/* 121 */     this.patchers.clear();
/*     */ 
/* 124 */     Iterator itr = this.schemaSet.iterateElementDecls();
/* 125 */     while (itr.hasNext()) {
/* 126 */       ((ElementDecl)itr.next()).updateSubstitutabilityMap();
/*     */     }
/*     */ 
/* 129 */     for (Patch patcher : this.errorCheckers)
/* 130 */       patcher.run();
/* 131 */     this.errorCheckers.clear();
/*     */ 
/* 134 */     if (this.hadError) return null;
/* 135 */     return this.schemaSet;
/*     */   }
/*     */ 
/*     */   public NGCCRuntimeEx newNGCCRuntime() {
/* 139 */     return new NGCCRuntimeEx(this);
/*     */   }
/*     */ 
/*     */   void setErrorFlag()
/*     */   {
/* 148 */     this.hadError = true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.ParserContext
 * JD-Core Version:    0.6.2
 */