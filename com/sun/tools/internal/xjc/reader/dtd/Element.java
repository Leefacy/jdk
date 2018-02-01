/*     */ package com.sun.tools.internal.xjc.reader.dtd;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo.CollectionMode;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ final class Element extends Term
/*     */   implements Comparable<Element>
/*     */ {
/*     */   final String name;
/*     */   private final TDTDReader owner;
/*     */   private short contentModelType;
/*     */   private Term contentModel;
/*     */   boolean isReferenced;
/*     */   private CClassInfo classInfo;
/*     */   private boolean classInfoComputed;
/*  98 */   final List<CPropertyInfo> attributes = new ArrayList();
/*     */ 
/* 103 */   private final List<Block> normalizedBlocks = new ArrayList();
/*     */   private boolean mustBeClass;
/*     */   private Locator locator;
/*     */ 
/*     */   public Element(TDTDReader owner, String name)
/*     */   {
/* 119 */     this.owner = owner;
/* 120 */     this.name = name;
/*     */   }
/*     */ 
/*     */   void normalize(List<Block> r, boolean optional) {
/* 124 */     Block o = new Block(optional, false);
/* 125 */     o.elements.add(this);
/* 126 */     r.add(o);
/*     */   }
/*     */ 
/*     */   void addAllElements(Block b) {
/* 130 */     b.elements.add(this);
/*     */   }
/*     */ 
/*     */   boolean isOptional() {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   boolean isRepeated() {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   void define(short contentModelType, Term contentModel, Locator locator)
/*     */   {
/* 146 */     assert (this.contentModel == null);
/* 147 */     this.contentModelType = contentModelType;
/* 148 */     this.contentModel = contentModel;
/* 149 */     this.locator = locator;
/* 150 */     contentModel.normalize(this.normalizedBlocks, false);
/*     */ 
/* 152 */     for (Block b : this.normalizedBlocks)
/* 153 */       if ((b.isRepeated) || (b.elements.size() > 1))
/* 154 */         for (Element e : b.elements)
/* 155 */           this.owner.getOrCreateElement(e.name).mustBeClass = true;
/*     */   }
/*     */ 
/*     */   private TypeUse getConversion()
/*     */   {
/* 166 */     assert (this.contentModel == Term.EMPTY);
/*     */ 
/* 168 */     BIElement e = this.owner.bindInfo.element(this.name);
/* 169 */     if (e != null) {
/* 170 */       BIConversion conv = e.getConversion();
/* 171 */       if (conv != null)
/* 172 */         return conv.getTransducer();
/*     */     }
/* 174 */     return CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */   CClassInfo getClassInfo()
/*     */   {
/* 181 */     if (!this.classInfoComputed) {
/* 182 */       this.classInfoComputed = true;
/* 183 */       this.classInfo = calcClass();
/*     */     }
/* 185 */     return this.classInfo;
/*     */   }
/*     */ 
/*     */   private CClassInfo calcClass() {
/* 189 */     BIElement e = this.owner.bindInfo.element(this.name);
/* 190 */     if (e == null) {
/* 191 */       if ((this.contentModelType != 2) || 
/* 192 */         (!this.attributes
/* 192 */         .isEmpty()) || (this.mustBeClass))
/*     */       {
/* 194 */         return createDefaultClass();
/* 195 */       }if (this.contentModel != Term.EMPTY) {
/* 196 */         throw new UnsupportedOperationException("mixed content model not supported");
/*     */       }
/*     */ 
/* 199 */       if (this.isReferenced) {
/* 200 */         return null;
/*     */       }
/*     */ 
/* 203 */       return createDefaultClass();
/*     */     }
/*     */ 
/* 206 */     return e.clazz;
/*     */   }
/*     */ 
/*     */   private CClassInfo createDefaultClass()
/*     */   {
/* 211 */     String className = this.owner.model.getNameConverter().toClassName(this.name);
/* 212 */     QName tagName = new QName("", this.name);
/*     */ 
/* 214 */     return new CClassInfo(this.owner.model, this.owner.getTargetPackage(), className, this.locator, null, tagName, null, null);
/*     */   }
/*     */ 
/*     */   void bind() {
/* 218 */     CClassInfo ci = getClassInfo();
/* 219 */     assert ((ci != null) || (this.attributes.isEmpty()));
/* 220 */     for (CPropertyInfo p : this.attributes) {
/* 221 */       ci.addProperty(p);
/*     */     }
/* 223 */     switch (this.contentModelType) {
/*     */     case 1:
/* 225 */       CReferencePropertyInfo rp = new CReferencePropertyInfo("Content", true, false, true, null, null, this.locator, false, false, false);
/* 226 */       rp.setWildcard(WildcardMode.SKIP);
/* 227 */       ci.addProperty(rp);
/* 228 */       return;
/*     */     case 3:
/* 230 */       break;
/*     */     case 2:
/* 232 */       if (this.contentModel != Term.EMPTY) {
/* 233 */         throw new UnsupportedOperationException("mixed content model unsupported yet");
/*     */       }
/* 235 */       if (ci != null)
/*     */       {
/* 237 */         CValuePropertyInfo p = new CValuePropertyInfo("value", null, null, this.locator, getConversion(), null);
/* 238 */         ci.addProperty(p);
/*     */       }
/* 240 */       return;
/*     */     case 0:
/* 243 */       assert (ci != null);
/* 244 */       return;
/*     */     }
/*     */ 
/* 248 */     Object n = new ArrayList();
/* 249 */     this.contentModel.normalize((List)n, false);
/*     */ 
/* 252 */     Set names = new HashSet();
/* 253 */     boolean collision = false;
/*     */ 
/* 256 */     for (Iterator localIterator2 = ((List)n).iterator(); localIterator2.hasNext(); ) { b = (Block)localIterator2.next();
/* 257 */       for (Element e : b.elements)
/* 258 */         if (!names.add(e.name)) {
/* 259 */           collision = true;
/* 260 */           break label334;
/*     */         }
/*     */     }
/*     */     Block b;
/*     */     label334: Object b;
/* 263 */     if (collision)
/*     */     {
/* 265 */       Block all = new Block(true, true);
/* 266 */       for (b = ((List)n).iterator(); b.hasNext(); ) { b = (Block)b.next();
/* 267 */         all.elements.addAll(((Block)b).elements); }
/* 268 */       ((List)n).clear();
/* 269 */       ((List)n).add(all);
/*     */     }
/*     */ 
/* 273 */     for (Block b : (List)n)
/*     */     {
/*     */       CElementPropertyInfo p;
/*     */       CElementPropertyInfo p;
/* 275 */       if ((b.isRepeated) || (b.elements.size() > 1))
/*     */       {
/* 277 */         StringBuilder name = new StringBuilder();
/* 278 */         for (b = b.elements.iterator(); ((Iterator)b).hasNext(); ) { Element e = (Element)((Iterator)b).next();
/* 279 */           if (name.length() > 0)
/* 280 */             name.append("Or");
/* 281 */           name.append(this.owner.model.getNameConverter().toPropertyName(e.name));
/*     */         }
/* 283 */         p = new CElementPropertyInfo(name.toString(), CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT, ID.NONE, null, null, null, this.locator, !b.isOptional);
/* 284 */         for (b = b.elements.iterator(); ((Iterator)b).hasNext(); ) { Element e = (Element)((Iterator)b).next();
/* 285 */           CClassInfo child = this.owner.getOrCreateElement(e.name).getClassInfo();
/* 286 */           assert (child != null);
/* 287 */           p.getTypes().add(new CTypeRef(child, new QName("", e.name), null, false, null)); }
/*     */       }
/*     */       else
/*     */       {
/* 291 */         String name = ((Element)b.elements.iterator().next()).name;
/* 292 */         String propName = this.owner.model.getNameConverter().toPropertyName(name);
/*     */ 
/* 295 */         Element ref = this.owner.getOrCreateElement(name);
/*     */         TypeUse refType;
/*     */         TypeUse refType;
/* 296 */         if (ref.getClassInfo() != null)
/* 297 */           refType = ref.getClassInfo();
/*     */         else {
/* 299 */           refType = ref.getConversion().getInfo();
/*     */         }
/*     */ 
/* 303 */         p = new CElementPropertyInfo(propName, refType
/* 303 */           .isCollection() ? CElementPropertyInfo.CollectionMode.REPEATED_VALUE : CElementPropertyInfo.CollectionMode.NOT_REPEATED, ID.NONE, null, null, null, this.locator, !b.isOptional);
/*     */ 
/* 305 */         p.getTypes().add(new CTypeRef(refType.getInfo(), new QName("", name), null, false, null));
/*     */       }
/* 307 */       ci.addProperty(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int compareTo(Element that) {
/* 312 */     return this.name.compareTo(that.name);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.Element
 * JD-Core Version:    0.6.2
 */