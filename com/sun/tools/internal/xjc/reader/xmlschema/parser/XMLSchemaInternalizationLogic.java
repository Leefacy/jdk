/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.AbstractReferenceFinderImpl;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.util.DOMUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class XMLSchemaInternalizationLogic
/*     */   implements InternalizationLogic
/*     */ {
/*     */   public XMLFilterImpl createExternalReferenceFinder(DOMForest parent)
/*     */   {
/*  66 */     return new ReferenceFinder(parent);
/*     */   }
/*     */ 
/*     */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) {
/*  70 */     return "http://www.w3.org/2001/XMLSchema".equals(target.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   public Element refineTarget(Element target)
/*     */   {
/*  75 */     Element annotation = DOMUtils.getFirstChildElement(target, "http://www.w3.org/2001/XMLSchema", "annotation");
/*  76 */     if (annotation == null)
/*     */     {
/*  78 */       annotation = insertXMLSchemaElement(target, "annotation");
/*     */     }
/*     */ 
/*  81 */     Element appinfo = DOMUtils.getFirstChildElement(annotation, "http://www.w3.org/2001/XMLSchema", "appinfo");
/*  82 */     if (appinfo == null)
/*     */     {
/*  84 */       appinfo = insertXMLSchemaElement(annotation, "appinfo");
/*     */     }
/*  86 */     return appinfo;
/*     */   }
/*     */ 
/*     */   private Element insertXMLSchemaElement(Element parent, String localName)
/*     */   {
/*  99 */     String qname = parent.getTagName();
/* 100 */     int idx = qname.indexOf(':');
/* 101 */     if (idx == -1) qname = localName; else {
/* 102 */       qname = qname.substring(0, idx + 1) + localName;
/*     */     }
/* 104 */     Element child = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", qname);
/*     */ 
/* 106 */     NodeList children = parent.getChildNodes();
/*     */ 
/* 108 */     if (children.getLength() == 0)
/* 109 */       parent.appendChild(child);
/*     */     else {
/* 111 */       parent.insertBefore(child, children.item(0));
/*     */     }
/* 113 */     return child;
/*     */   }
/*     */ 
/*     */   private static final class ReferenceFinder extends AbstractReferenceFinderImpl
/*     */   {
/*     */     ReferenceFinder(DOMForest parent)
/*     */     {
/*  53 */       super();
/*     */     }
/*     */ 
/*     */     protected String findExternalResource(String nsURI, String localName, Attributes atts) {
/*  57 */       if (("http://www.w3.org/2001/XMLSchema".equals(nsURI)) && (
/*  58 */         ("import"
/*  58 */         .equals(localName)) || 
/*  58 */         ("include".equals(localName)))) {
/*  59 */         return atts.getValue("schemaLocation");
/*     */       }
/*  61 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic
 * JD-Core Version:    0.6.2
 */