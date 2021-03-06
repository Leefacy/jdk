/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ParseException;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   public static String getRequiredAttribute(Element element, String name)
/*     */   {
/*  49 */     String result = XmlUtil.getAttributeOrNull(element, name);
/*  50 */     if (result == null) {
/*  51 */       fail("parsing.missingRequiredAttribute", element
/*  53 */         .getTagName(), name);
/*     */     }
/*  55 */     return result;
/*     */   }
/*     */ 
/*     */   public static void verifyTag(Element element, String tag) {
/*  59 */     if (!element.getLocalName().equals(tag))
/*  60 */       fail("parsing.invalidTag", element.getTagName(), tag);
/*     */   }
/*     */ 
/*     */   public static void verifyTagNS(Element element, String tag, String nsURI) {
/*  64 */     if ((!element.getLocalName().equals(tag)) || (
/*  65 */       (element
/*  65 */       .getNamespaceURI() != null) && 
/*  66 */       (!element
/*  66 */       .getNamespaceURI().equals(nsURI))))
/*  67 */       fail("parsing.invalidTagNS", new Object[] { element
/*  70 */         .getTagName(), element
/*  71 */         .getNamespaceURI(), tag, nsURI });
/*     */   }
/*     */ 
/*     */   public static void verifyTagNS(Element element, QName name)
/*     */   {
/*  77 */     if (!isTagName(element, name))
/*  78 */       fail("parsing.invalidTagNS", new Object[] { element
/*  81 */         .getTagName(), element
/*  82 */         .getNamespaceURI(), name
/*  83 */         .getLocalPart(), name
/*  84 */         .getNamespaceURI() });
/*     */   }
/*     */ 
/*     */   public static boolean isTagName(Element element, QName name)
/*     */   {
/*  90 */     return (element.getLocalName().equals(name.getLocalPart())) && 
/*  89 */       (element
/*  89 */       .getNamespaceURI() != null) && 
/*  90 */       (element
/*  90 */       .getNamespaceURI().equals(name.getNamespaceURI()));
/*     */   }
/*     */ 
/*     */   public static void verifyTagNSRootElement(Element element, QName name)
/*     */   {
/*  95 */     if ((!element.getLocalName().equals(name.getLocalPart())) || (
/*  96 */       (element
/*  96 */       .getNamespaceURI() != null) && 
/*  97 */       (!element
/*  97 */       .getNamespaceURI().equals(name.getNamespaceURI()))))
/*  98 */       fail("parsing.incorrectRootElement", new Object[] { element
/* 101 */         .getTagName(), element
/* 102 */         .getNamespaceURI(), name
/* 103 */         .getLocalPart(), name
/* 104 */         .getNamespaceURI() });
/*     */   }
/*     */ 
/*     */   public static Element nextElementIgnoringCharacterContent(Iterator iter) {
/* 108 */     while (iter.hasNext()) {
/* 109 */       Node n = (Node)iter.next();
/* 110 */       if ((!(n instanceof Text)) && 
/* 112 */         (!(n instanceof Comment)))
/*     */       {
/* 114 */         if (!(n instanceof Element))
/* 115 */           fail("parsing.elementExpected");
/* 116 */         return (Element)n;
/*     */       }
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element nextElement(Iterator iter) {
/* 123 */     while (iter.hasNext()) {
/* 124 */       Node n = (Node)iter.next();
/* 125 */       if ((n instanceof Text)) {
/* 126 */         Text t = (Text)n;
/* 127 */         if (t.getData().trim().length() != 0)
/*     */         {
/* 129 */           fail("parsing.nonWhitespaceTextFound", t.getData().trim());
/*     */         }
/* 131 */       } else if (!(n instanceof Comment))
/*     */       {
/* 133 */         if (!(n instanceof Element))
/* 134 */           fail("parsing.elementExpected");
/* 135 */         return (Element)n;
/*     */       }
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public static String processSystemIdWithBase(String baseSystemId, String systemId)
/*     */   {
/*     */     try
/*     */     {
/* 145 */       URL base = null;
/*     */       try {
/* 147 */         base = new URL(baseSystemId);
/*     */       } catch (MalformedURLException e) {
/* 149 */         base = new File(baseSystemId).toURL();
/*     */       }
/*     */       try
/*     */       {
/* 153 */         URL url = new URL(base, systemId);
/* 154 */         return url.toString();
/*     */       } catch (MalformedURLException e) {
/* 156 */         fail("parsing.invalidURI", systemId);
/*     */       }
/*     */     }
/*     */     catch (MalformedURLException e) {
/* 160 */       fail("parsing.invalidURI", baseSystemId);
/*     */     }
/*     */ 
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */   public static void fail(String key) {
/* 167 */     throw new ParseException(key, new Object[0]);
/*     */   }
/*     */ 
/*     */   public static void fail(String key, String arg) {
/* 171 */     throw new ParseException(key, new Object[] { arg });
/*     */   }
/*     */ 
/*     */   public static void fail(String key, String arg1, String arg2) {
/* 175 */     throw new ParseException(key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   public static void fail(String key, Object[] args) {
/* 179 */     throw new ParseException(key, args);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.parser.Util
 * JD-Core Version:    0.6.2
 */