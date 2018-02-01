/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public final class BIElement
/*     */ {
/*     */   final BindInfo parent;
/*     */   private final Element e;
/*     */   public final CClassInfo clazz;
/* 138 */   private final List<BIContent> contents = new ArrayList();
/*     */ 
/* 141 */   private final Map<String, BIConversion> conversions = new HashMap();
/*     */   private BIContent rest;
/* 151 */   private final Map<String, BIAttribute> attributes = new HashMap();
/*     */ 
/* 154 */   private final List<BIConstructor> constructors = new ArrayList();
/*     */   private final String className;
/*     */ 
/*     */   BIElement(BindInfo bi, Element _e)
/*     */   {
/*  54 */     this.parent = bi;
/*  55 */     this.e = _e;
/*     */ 
/*  58 */     Element c = DOMUtil.getElement(this.e, "content");
/*  59 */     if (c != null) {
/*  60 */       if (DOMUtil.getAttribute(c, "property") != null)
/*     */       {
/*  62 */         this.rest = BIContent.create(c, this);
/*     */       }
/*     */       else {
/*  65 */         for (Element p : DOMUtil.getChildElements(c)) {
/*  66 */           if (p.getLocalName().equals("rest"))
/*  67 */             this.rest = BIContent.create(p, this);
/*     */           else {
/*  69 */             this.contents.add(BIContent.create(p, this));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  76 */     for (Element atr : DOMUtil.getChildElements(this.e, "attribute")) {
/*  77 */       BIAttribute a = new BIAttribute(this, atr);
/*  78 */       this.attributes.put(a.name(), a);
/*     */     }
/*     */     String className;
/*  81 */     if (isClass())
/*     */     {
/*  83 */       className = DOMUtil.getAttribute(this.e, "class");
/*  84 */       if (className == null)
/*     */       {
/*  86 */         className = NameConverter.standard.toClassName(name());
/*  87 */       }this.className = className;
/*     */     }
/*     */     else {
/*  90 */       this.className = null;
/*     */     }
/*     */ 
/*  94 */     for (Element conv : DOMUtil.getChildElements(this.e, "conversion")) {
/*  95 */       BIConversion c = new BIUserConversion(bi, conv);
/*  96 */       this.conversions.put(c.name(), c);
/*     */     }
/*  98 */     for (Element en : DOMUtil.getChildElements(this.e, "enumeration")) {
/*  99 */       BIConversion c = BIEnumeration.create(en, this);
/* 100 */       this.conversions.put(c.name(), c);
/*     */     }
/*     */ 
/* 104 */     for (Element c : DOMUtil.getChildElements(this.e, "constructor")) {
/* 105 */       this.constructors.add(new BIConstructor(c));
/*     */     }
/*     */ 
/* 108 */     String name = name();
/* 109 */     QName tagName = new QName("", name);
/*     */ 
/* 111 */     this.clazz = new CClassInfo(this.parent.model, this.parent.getTargetPackage(), this.className, getLocation(), null, tagName, null, null);
/*     */   }
/*     */ 
/*     */   public Locator getLocation()
/*     */   {
/* 118 */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 166 */     return DOMUtil.getAttribute(this.e, "name");
/*     */   }
/*     */ 
/*     */   public boolean isClass()
/*     */   {
/* 173 */     return "class".equals(this.e.getAttribute("type"));
/*     */   }
/*     */ 
/*     */   public boolean isRoot()
/*     */   {
/* 180 */     return "true".equals(this.e.getAttribute("root"));
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 191 */     return this.className;
/*     */   }
/*     */ 
/*     */   public void declareConstructors(CClassInfo src)
/*     */   {
/* 205 */     for (BIConstructor c : this.constructors)
/* 206 */       c.createDeclaration(src);
/*     */   }
/*     */ 
/*     */   public BIConversion getConversion()
/*     */   {
/* 221 */     String cnv = DOMUtil.getAttribute(this.e, "convert");
/* 222 */     if (cnv == null) return null;
/*     */ 
/* 224 */     return conversion(cnv);
/*     */   }
/*     */ 
/*     */   public BIConversion conversion(String name)
/*     */   {
/* 237 */     BIConversion r = (BIConversion)this.conversions.get(name);
/* 238 */     if (r != null) return r;
/*     */ 
/* 241 */     return this.parent.conversion(name);
/*     */   }
/*     */ 
/*     */   public List<BIContent> getContents()
/*     */   {
/* 249 */     return this.contents;
/*     */   }
/*     */ 
/*     */   public BIAttribute attribute(String name)
/*     */   {
/* 259 */     return (BIAttribute)this.attributes.get(name);
/*     */   }
/*     */ 
/*     */   public BIContent getRest()
/*     */   {
/* 267 */     return this.rest;
/*     */   }
/*     */ 
/*     */   public Locator getSourceLocation() {
/* 271 */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIElement
 * JD-Core Version:    0.6.2
 */