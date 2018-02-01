/*     */ package com.sun.tools.internal.xjc.addon.episode;
/*     */ 
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.episode.Bindings;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.episode.Package;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.episode.SchemaBindings;
/*     */ import com.sun.xml.internal.txw2.TXW;
/*     */ import com.sun.xml.internal.txw2.output.StreamSerializer;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class PluginImpl extends Plugin
/*     */ {
/*     */   private File episodeFile;
/* 203 */   private static final XSFunction<String> SCD = new XSFunction() {
/*     */     private String name(XSDeclaration decl) {
/* 205 */       if (decl.getTargetNamespace().equals("")) {
/* 206 */         return decl.getName();
/*     */       }
/* 208 */       return "tns:" + decl.getName();
/*     */     }
/*     */ 
/*     */     public String complexType(XSComplexType type) {
/* 212 */       return "~" + name(type);
/*     */     }
/*     */ 
/*     */     public String simpleType(XSSimpleType simpleType) {
/* 216 */       return "~" + name(simpleType);
/*     */     }
/*     */ 
/*     */     public String elementDecl(XSElementDecl decl) {
/* 220 */       return name(decl);
/*     */     }
/*     */ 
/*     */     public String annotation(XSAnnotation ann)
/*     */     {
/* 225 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String attGroupDecl(XSAttGroupDecl decl) {
/* 229 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String attributeDecl(XSAttributeDecl decl) {
/* 233 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String attributeUse(XSAttributeUse use) {
/* 237 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String schema(XSSchema schema) {
/* 241 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String facet(XSFacet facet) {
/* 245 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String notation(XSNotation notation) {
/* 249 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String identityConstraint(XSIdentityConstraint decl) {
/* 253 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String xpath(XSXPath xpath) {
/* 257 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String particle(XSParticle particle) {
/* 261 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String empty(XSContentType empty) {
/* 265 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String wildcard(XSWildcard wc) {
/* 269 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String modelGroupDecl(XSModelGroupDecl decl) {
/* 273 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String modelGroup(XSModelGroup group) {
/* 277 */       throw new UnsupportedOperationException();
/*     */     }
/* 203 */   };
/*     */ 
/*     */   public String getOptionName()
/*     */   {
/*  86 */     return "episode";
/*     */   }
/*     */ 
/*     */   public String getUsage() {
/*  90 */     return "  -episode <FILE>    :  generate the episode file for separate compilation";
/*     */   }
/*     */ 
/*     */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/*  94 */     if (args[i].equals("-episode")) {
/*  95 */       this.episodeFile = new File(opt.requireArgument("-episode", args, ++i));
/*  96 */       return 2;
/*     */     }
/*  98 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       Map perSchema = new HashMap();
/* 110 */       boolean hasComponentInNoNamespace = false;
/*     */ 
/* 113 */       List outlines = new ArrayList();
/*     */ 
/* 115 */       for (ClassOutline co : model.getClasses()) {
/* 116 */         XSComponent sc = co.target.getSchemaComponent();
/* 117 */         String fullName = co.implClass.fullName();
/* 118 */         String packageName = co.implClass.getPackage().name();
/* 119 */         OutlineAdaptor adaptor = new OutlineAdaptor(sc, PluginImpl.OutlineAdaptor.OutlineType.CLASS, fullName, packageName);
/*     */ 
/* 121 */         outlines.add(adaptor);
/*     */       }
/*     */ 
/* 124 */       for (EnumOutline eo : model.getEnums()) {
/* 125 */         XSComponent sc = eo.target.getSchemaComponent();
/* 126 */         String fullName = eo.clazz.fullName();
/* 127 */         String packageName = eo.clazz.getPackage().name();
/* 128 */         OutlineAdaptor adaptor = new OutlineAdaptor(sc, PluginImpl.OutlineAdaptor.OutlineType.ENUM, fullName, packageName);
/*     */ 
/* 130 */         outlines.add(adaptor);
/*     */       }
/*     */ 
/* 133 */       for (OutlineAdaptor oa : outlines) {
/* 134 */         sc = oa.schemaComponent;
/*     */ 
/* 136 */         if ((sc != null) && 
/* 137 */           ((sc instanceof XSDeclaration)))
/*     */         {
/* 139 */           XSDeclaration decl = (XSDeclaration)sc;
/* 140 */           if (!decl.isLocal())
/*     */           {
/* 143 */             PerSchemaOutlineAdaptors list = (PerSchemaOutlineAdaptors)perSchema.get(decl.getOwnerSchema());
/* 144 */             if (list == null) {
/* 145 */               list = new PerSchemaOutlineAdaptors(null);
/* 146 */               perSchema.put(decl.getOwnerSchema(), list);
/*     */             }
/*     */ 
/* 149 */             list.add(oa);
/*     */ 
/* 151 */             if (decl.getTargetNamespace().equals(""))
/* 152 */               hasComponentInNoNamespace = true;
/*     */           }
/*     */         }
/*     */       }
/*     */       XSComponent sc;
/* 155 */       Object os = new FileOutputStream(this.episodeFile);
/* 156 */       Bindings bindings = (Bindings)TXW.create(Bindings.class, new StreamSerializer((OutputStream)os, "UTF-8"));
/* 157 */       if (hasComponentInNoNamespace)
/* 158 */         bindings._namespace("http://java.sun.com/xml/ns/jaxb", "jaxb");
/*     */       else
/* 160 */         bindings._namespace("http://java.sun.com/xml/ns/jaxb", "");
/* 161 */       bindings.version("2.1");
/* 162 */       bindings._comment("\n\n" + opt.getPrologComment() + "\n  ");
/*     */ 
/* 165 */       for (Map.Entry e : perSchema.entrySet()) {
/* 166 */         PerSchemaOutlineAdaptors ps = (PerSchemaOutlineAdaptors)e.getValue();
/* 167 */         Bindings group = bindings.bindings();
/* 168 */         String tns = ((XSSchema)e.getKey()).getTargetNamespace();
/* 169 */         if (!tns.equals("")) {
/* 170 */           group._namespace(tns, "tns");
/*     */         }
/* 172 */         group.scd("x-schema::" + (tns.equals("") ? "" : "tns"));
/* 173 */         SchemaBindings schemaBindings = group.schemaBindings();
/* 174 */         schemaBindings.map(false);
/*     */         String packageName;
/* 175 */         if (ps.packageNames.size() == 1)
/*     */         {
/* 177 */           packageName = (String)ps.packageNames.iterator().next();
/* 178 */           if ((packageName != null) && (packageName.length() > 0)) {
/* 179 */             schemaBindings._package().name(packageName);
/*     */           }
/*     */         }
/*     */ 
/* 183 */         for (OutlineAdaptor oa : ps.outlineAdaptors) {
/* 184 */           Bindings child = group.bindings();
/* 185 */           oa.buildBindings(child);
/*     */         }
/* 187 */         group.commit(true);
/*     */       }
/*     */ 
/* 190 */       bindings.commit();
/*     */ 
/* 192 */       return true;
/*     */     } catch (IOException e) {
/* 194 */       errorHandler.error(new SAXParseException("Failed to write to " + this.episodeFile, null, e));
/* 195 */     }return false;
/*     */   }
/*     */ 
/*     */   private static final class OutlineAdaptor
/*     */   {
/*     */     private final XSComponent schemaComponent;
/*     */     private final OutlineType outlineType;
/*     */     private final String implName;
/*     */     private final String packageName;
/*     */ 
/*     */     public OutlineAdaptor(XSComponent schemaComponent, OutlineType outlineType, String implName, String packageName)
/*     */     {
/* 316 */       this.schemaComponent = schemaComponent;
/* 317 */       this.outlineType = outlineType;
/* 318 */       this.implName = implName;
/* 319 */       this.packageName = packageName;
/*     */     }
/*     */ 
/*     */     private void buildBindings(Bindings bindings) {
/* 323 */       bindings.scd((String)this.schemaComponent.apply(PluginImpl.SCD));
/* 324 */       this.outlineType.bindingsBuilder.build(this, bindings);
/*     */     }
/*     */ 
/*     */     private static enum OutlineType
/*     */     {
/* 285 */       CLASS(new BindingsBuilder() {
/*     */         public void build(PluginImpl.OutlineAdaptor adaptor, Bindings bindings) {
/* 287 */           bindings.klass().ref(adaptor.implName);
/*     */         }
/* 285 */       }), 
/*     */ 
/* 291 */       ENUM(new BindingsBuilder() {
/*     */         public void build(PluginImpl.OutlineAdaptor adaptor, Bindings bindings) {
/* 293 */           bindings.typesafeEnumClass().ref(adaptor.implName);
/*     */         }
/* 291 */       });
/*     */ 
/*     */       private final BindingsBuilder bindingsBuilder;
/*     */ 
/*     */       private OutlineType(BindingsBuilder bindingsBuilder)
/*     */       {
/* 300 */         this.bindingsBuilder = bindingsBuilder;
/*     */       }
/*     */ 
/*     */       private static abstract interface BindingsBuilder
/*     */       {
/*     */         public abstract void build(PluginImpl.OutlineAdaptor paramOutlineAdaptor, Bindings paramBindings);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class PerSchemaOutlineAdaptors
/*     */   {
/* 330 */     private final List<PluginImpl.OutlineAdaptor> outlineAdaptors = new ArrayList();
/*     */ 
/* 332 */     private final Set<String> packageNames = new HashSet();
/*     */ 
/*     */     private void add(PluginImpl.OutlineAdaptor outlineAdaptor)
/*     */     {
/* 336 */       this.outlineAdaptors.add(outlineAdaptor);
/* 337 */       this.packageNames.add(outlineAdaptor.packageName);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.addon.episode.PluginImpl
 * JD-Core Version:    0.6.2
 */