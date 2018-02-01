/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent.Package;
/*     */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public final class BIEnumeration
/*     */   implements BIConversion
/*     */ {
/*     */   private final Element e;
/*     */   private final TypeUse xducer;
/*     */ 
/*     */   private BIEnumeration(Element _e, TypeUse _xducer)
/*     */   {
/*  48 */     this.e = _e;
/*  49 */     this.xducer = _xducer;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/*  57 */     return DOMUtil.getAttribute(this.e, "name");
/*     */   }
/*     */   public TypeUse getTransducer() {
/*  60 */     return this.xducer;
/*     */   }
/*     */ 
/*     */   static BIEnumeration create(Element dom, BindInfo parent)
/*     */   {
/*  78 */     return new BIEnumeration(dom, new CEnumLeafInfo(parent.model, null, new CClassInfoParent.Package(parent
/*  73 */       .getTargetPackage()), 
/*  74 */       DOMUtil.getAttribute(dom, "name"), 
/*  74 */       CBuiltinLeafInfo.STRING, 
/*  76 */       buildMemberList(parent.model, dom), 
/*  76 */       null, null, 
/*  78 */       DOMLocator.getLocationInfo(dom)));
/*     */   }
/*     */ 
/*     */   static BIEnumeration create(Element dom, BIElement parent)
/*     */   {
/*  94 */     return new BIEnumeration(dom, new CEnumLeafInfo(parent.parent.model, null, parent.clazz, 
/*  90 */       DOMUtil.getAttribute(dom, "name"), 
/*  90 */       CBuiltinLeafInfo.STRING, 
/*  92 */       buildMemberList(parent.parent.model, dom), 
/*  92 */       null, null, 
/*  94 */       DOMLocator.getLocationInfo(dom)));
/*     */   }
/*     */ 
/*     */   private static List<CEnumConstant> buildMemberList(Model model, Element dom)
/*     */   {
/*  98 */     List r = new ArrayList();
/*     */ 
/* 100 */     String members = DOMUtil.getAttribute(dom, "members");
/* 101 */     if (members == null) members = "";
/*     */ 
/* 103 */     StringTokenizer tokens = new StringTokenizer(members);
/* 104 */     while (tokens.hasMoreTokens()) {
/* 105 */       String token = tokens.nextToken();
/* 106 */       r.add(new CEnumConstant(model.getNameConverter().toConstantName(token), null, token, null, null, null));
/*     */     }
/*     */ 
/* 110 */     return r;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIEnumeration
 * JD-Core Version:    0.6.2
 */