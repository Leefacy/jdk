/*     */ package com.sun.tools.internal.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent.Package;
/*     */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.rngom.digested.DChoicePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DDefine;
/*     */ import com.sun.xml.internal.rngom.digested.DElementPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*     */ import com.sun.xml.internal.rngom.digested.DRefPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DValuePattern;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class RELAXNGCompiler
/*     */ {
/*     */   final DPattern grammar;
/*     */   final Set<DDefine> defs;
/*     */   final Options opts;
/*     */   final Model model;
/*     */   final JPackage pkg;
/*  83 */   final Map<String, DatatypeLib> datatypes = new HashMap();
/*     */ 
/*  95 */   final Map<DPattern, CTypeInfo[]> classes = new HashMap();
/*     */ 
/* 102 */   final Map<CClassInfo, DPattern> bindQueue = new HashMap();
/*     */ 
/* 104 */   final TypeUseBinder typeUseBinder = new TypeUseBinder(this);
/*     */ 
/*     */   public static Model build(DPattern grammar, JCodeModel codeModel, Options opts) {
/* 107 */     RELAXNGCompiler compiler = new RELAXNGCompiler(grammar, codeModel, opts);
/* 108 */     compiler.compile();
/* 109 */     return compiler.model;
/*     */   }
/*     */ 
/*     */   public RELAXNGCompiler(DPattern grammar, JCodeModel codeModel, Options opts) {
/* 113 */     this.grammar = grammar;
/* 114 */     this.opts = opts;
/* 115 */     this.model = new Model(opts, codeModel, NameConverter.smart, opts.classNameAllocator, null);
/*     */ 
/* 117 */     this.datatypes.put("", DatatypeLib.BUILTIN);
/* 118 */     this.datatypes.put("http://www.w3.org/2001/XMLSchema-datatypes", DatatypeLib.XMLSCHEMA);
/*     */ 
/* 121 */     DefineFinder deff = new DefineFinder();
/* 122 */     grammar.accept(deff);
/* 123 */     this.defs = deff.defs;
/*     */ 
/* 125 */     if (opts.defaultPackage2 != null) {
/* 126 */       this.pkg = codeModel._package(opts.defaultPackage2);
/*     */     }
/* 128 */     else if (opts.defaultPackage != null)
/* 129 */       this.pkg = codeModel._package(opts.defaultPackage);
/*     */     else
/* 131 */       this.pkg = codeModel.rootPackage();
/*     */   }
/*     */ 
/*     */   private void compile()
/*     */   {
/* 136 */     promoteElementDefsToClasses();
/* 137 */     promoteTypeSafeEnums();
/*     */ 
/* 140 */     promoteTypePatternsToClasses();
/*     */ 
/* 142 */     for (Map.Entry e : this.bindQueue.entrySet())
/* 143 */       bindContentModel((CClassInfo)e.getKey(), (DPattern)e.getValue());
/*     */   }
/*     */ 
/*     */   private void bindContentModel(CClassInfo clazz, DPattern pattern)
/*     */   {
/* 150 */     pattern.accept(new ContentModelBinder(this, clazz));
/*     */   }
/*     */ 
/*     */   private void promoteTypeSafeEnums()
/*     */   {
/* 157 */     List members = new ArrayList();
/*     */ 
/* 160 */     for (DDefine def : this.defs) {
/* 161 */       DPattern p = def.getPattern();
/* 162 */       if ((p instanceof DChoicePattern)) {
/* 163 */         DChoicePattern cp = (DChoicePattern)p;
/*     */ 
/* 165 */         members.clear();
/*     */ 
/* 169 */         DValuePattern vp = null;
/*     */ 
/* 171 */         Iterator localIterator2 = cp.iterator();
/*     */         while (true) { if (!localIterator2.hasNext()) break label205; DPattern child = (DPattern)localIterator2.next();
/* 172 */           if (!(child instanceof DValuePattern)) break;
/* 173 */           DValuePattern c = (DValuePattern)child;
/* 174 */           if (vp == null)
/* 175 */             vp = c;
/*     */           else {
/* 177 */             if ((!vp.getDatatypeLibrary().equals(c.getDatatypeLibrary())) || 
/* 178 */               (!vp
/* 178 */               .getType().equals(c.getType()))) {
/*     */               break;
/*     */             }
/*     */           }
/* 182 */           members.add(new CEnumConstant(this.model
/* 183 */             .getNameConverter().toConstantName(c.getValue()), null, c
/* 184 */             .getValue(), null, null, c.getLocation()));
/*     */         }
/*     */ 
/* 190 */         if (!members.isEmpty())
/*     */         {
/* 193 */           Object base = CBuiltinLeafInfo.STRING;
/*     */ 
/* 195 */           DatatypeLib lib = (DatatypeLib)this.datatypes.get(vp.getNs());
/* 196 */           if (lib != null) {
/* 197 */             TypeUse use = lib.get(vp.getType());
/* 198 */             if ((use instanceof CNonElement)) {
/* 199 */               base = (CNonElement)use;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 205 */           CEnumLeafInfo xducer = new CEnumLeafInfo(this.model, null, new CClassInfoParent.Package(this.pkg), def
/* 203 */             .getName(), (CNonElement)base, new ArrayList(members), null, null, cp
/* 205 */             .getLocation());
/*     */ 
/* 207 */           this.classes.put(cp, new CTypeInfo[] { xducer });
/*     */         }
/*     */       }
/*     */     }
/* 210 */     label205:
/*     */   }
/*     */ 
/*     */   private void promoteElementDefsToClasses()
/*     */   {
/* 215 */     for (DDefine def : this.defs) {
/* 216 */       DPattern p = def.getPattern();
/* 217 */       if ((p instanceof DElementPattern)) {
/* 218 */         DElementPattern ep = (DElementPattern)p;
/*     */ 
/* 220 */         mapToClass(ep);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 225 */     this.grammar.accept(new DPatternWalker() {
/*     */       public Void onRef(DRefPattern p) {
/* 227 */         return null;
/*     */       }
/*     */ 
/*     */       public Void onElement(DElementPattern p) {
/* 231 */         RELAXNGCompiler.this.mapToClass(p);
/* 232 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void mapToClass(DElementPattern p) {
/* 238 */     NameClass nc = p.getName();
/* 239 */     if (nc.isOpen()) {
/* 240 */       return;
/*     */     }
/* 242 */     Set names = nc.listNames();
/*     */ 
/* 244 */     CClassInfo[] types = new CClassInfo[names.size()];
/* 245 */     int i = 0;
/* 246 */     for (QName n : names)
/*     */     {
/* 248 */       String name = this.model.getNameConverter().toClassName(n.getLocalPart());
/*     */ 
/* 250 */       this.bindQueue.put(types[(i++)] = 
/* 251 */          = new CClassInfo(this.model, this.pkg, name, p
/* 251 */         .getLocation(), null, n, null, null), p
/* 252 */         .getChild());
/*     */     }
/*     */ 
/* 255 */     this.classes.put(p, types);
/*     */   }
/*     */ 
/*     */   private void promoteTypePatternsToClasses()
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.relaxng.RELAXNGCompiler
 * JD-Core Version:    0.6.2
 */