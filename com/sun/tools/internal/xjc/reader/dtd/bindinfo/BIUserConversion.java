/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class BIUserConversion
/*     */   implements BIConversion
/*     */ {
/*     */   private final BindInfo owner;
/*     */   private final Element e;
/*     */ 
/*     */   BIUserConversion(BindInfo bi, Element _e)
/*     */   {
/*  69 */     this.owner = bi;
/*  70 */     this.e = _e;
/*     */   }
/*     */ 
/*     */   private static void add(Map<String, BIConversion> m, BIConversion c) {
/*  74 */     m.put(c.name(), c);
/*     */   }
/*     */ 
/*     */   static void addBuiltinConversions(BindInfo bi, Map<String, BIConversion> m)
/*     */   {
/*  79 */     add(m, new BIUserConversion(bi, parse("<conversion name='boolean' type='java.lang.Boolean' parse='getBoolean' />")));
/*  80 */     add(m, new BIUserConversion(bi, parse("<conversion name='byte' type='java.lang.Byte' parse='parseByte' />")));
/*  81 */     add(m, new BIUserConversion(bi, parse("<conversion name='short' type='java.lang.Short' parse='parseShort' />")));
/*  82 */     add(m, new BIUserConversion(bi, parse("<conversion name='int' type='java.lang.Integer' parse='parseInt' />")));
/*  83 */     add(m, new BIUserConversion(bi, parse("<conversion name='long' type='java.lang.Long' parse='parseLong' />")));
/*  84 */     add(m, new BIUserConversion(bi, parse("<conversion name='float' type='java.lang.Float' parse='parseFloat' />")));
/*  85 */     add(m, new BIUserConversion(bi, parse("<conversion name='double' type='java.lang.Double' parse='parseDouble' />")));
/*     */   }
/*     */ 
/*     */   private static Element parse(String text)
/*     */   {
/*     */     try {
/*  91 */       DocumentBuilderFactory dbf = XmlFactory.createDocumentBuilderFactory(false);
/*  92 */       InputSource is = new InputSource(new StringReader(text));
/*  93 */       return dbf.newDocumentBuilder().parse(is).getDocumentElement();
/*     */     } catch (SAXException x) {
/*  95 */       throw new Error(x);
/*     */     } catch (IOException x) {
/*  97 */       throw new Error(x);
/*     */     } catch (ParserConfigurationException x) {
/*  99 */       throw new Error(x);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Locator getSourceLocation()
/*     */   {
/* 114 */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */ 
/*     */   public String name() {
/* 118 */     return DOMUtil.getAttribute(this.e, "name");
/*     */   }
/*     */ 
/*     */   public TypeUse getTransducer()
/*     */   {
/* 123 */     String ws = DOMUtil.getAttribute(this.e, "whitespace");
/* 124 */     if (ws == null) ws = "collapse";
/*     */ 
/* 126 */     String type = DOMUtil.getAttribute(this.e, "type");
/* 127 */     if (type == null) type = name();
/* 128 */     JType t = null;
/*     */ 
/* 130 */     int idx = type.lastIndexOf('.');
/* 131 */     if (idx < 0) {
/*     */       try
/*     */       {
/* 134 */         t = JPrimitiveType.parse(this.owner.codeModel, type);
/*     */       }
/*     */       catch (IllegalArgumentException ex) {
/* 137 */         type = this.owner.getTargetPackage().name() + '.' + type;
/*     */       }
/*     */     }
/* 140 */     if (t == null) {
/*     */       try
/*     */       {
/* 143 */         JDefinedClass cls = this.owner.codeModel._class(type);
/* 144 */         cls.hide();
/* 145 */         t = cls;
/*     */       } catch (JClassAlreadyExistsException ex) {
/* 147 */         t = ex.getExistingClass();
/*     */       }
/*     */     }
/*     */ 
/* 151 */     String parse = DOMUtil.getAttribute(this.e, "parse");
/* 152 */     if (parse == null) parse = "new";
/*     */ 
/* 154 */     String print = DOMUtil.getAttribute(this.e, "print");
/* 155 */     if (print == null) print = "toString";
/*     */ 
/* 157 */     JDefinedClass adapter = generateAdapter(this.owner.codeModel, parse, print, t.boxify());
/*     */ 
/* 160 */     return TypeUseFactory.adapt(CBuiltinLeafInfo.STRING, new CAdapter(adapter));
/*     */   }
/*     */ 
/*     */   private JDefinedClass generateAdapter(JCodeModel cm, String parseMethod, String printMethod, JClass inMemoryType)
/*     */   {
/* 165 */     JDefinedClass adapter = null;
/*     */ 
/* 167 */     int id = 1;
/* 168 */     while (adapter == null) {
/*     */       try {
/* 170 */         JPackage pkg = this.owner.getTargetPackage();
/* 171 */         adapter = pkg._class("Adapter" + id);
/*     */       }
/*     */       catch (JClassAlreadyExistsException ex)
/*     */       {
/* 176 */         id++;
/*     */       }
/*     */     }
/*     */ 
/* 180 */     adapter._extends(cm.ref(XmlAdapter.class).narrow(String.class).narrow(inMemoryType));
/*     */ 
/* 182 */     JMethod unmarshal = adapter.method(1, inMemoryType, "unmarshal");
/* 183 */     JVar $value = unmarshal.param(String.class, "value");
/*     */     JExpression inv;
/*     */     JExpression inv;
/* 187 */     if (parseMethod.equals("new"))
/*     */     {
/* 192 */       inv = JExpr._new(inMemoryType).arg($value);
/*     */     } else {
/* 194 */       int idx = parseMethod.lastIndexOf('.');
/*     */       JExpression inv;
/* 195 */       if (idx < 0)
/*     */       {
/* 201 */         inv = inMemoryType.staticInvoke(parseMethod).arg($value);
/*     */       }
/* 203 */       else inv = JExpr.direct(parseMethod + "(value)");
/*     */     }
/*     */ 
/* 206 */     unmarshal.body()._return(inv);
/*     */ 
/* 209 */     JMethod marshal = adapter.method(1, String.class, "marshal");
/* 210 */     $value = marshal.param(inMemoryType, "value");
/*     */ 
/* 212 */     int idx = printMethod.lastIndexOf('.');
/* 213 */     if (idx < 0)
/*     */     {
/* 218 */       inv = $value.invoke(printMethod);
/*     */     }
/*     */     else {
/* 221 */       inv = JExpr.direct(printMethod + "(value)");
/*     */     }
/* 223 */     marshal.body()._return(inv);
/*     */ 
/* 225 */     return adapter;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIUserConversion
 * JD-Core Version:    0.6.2
 */