/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PackageSummaryBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "PackageDoc";
/*     */   private final PackageDoc packageDoc;
/*     */   private final PackageSummaryWriter packageWriter;
/*     */   private Content contentTree;
/*     */ 
/*     */   private PackageSummaryBuilder(AbstractBuilder.Context paramContext, PackageDoc paramPackageDoc, PackageSummaryWriter paramPackageSummaryWriter)
/*     */   {
/*  78 */     super(paramContext);
/*  79 */     this.packageDoc = paramPackageDoc;
/*  80 */     this.packageWriter = paramPackageSummaryWriter;
/*     */   }
/*     */ 
/*     */   public static PackageSummaryBuilder getInstance(AbstractBuilder.Context paramContext, PackageDoc paramPackageDoc, PackageSummaryWriter paramPackageSummaryWriter)
/*     */   {
/*  95 */     return new PackageSummaryBuilder(paramContext, paramPackageDoc, paramPackageSummaryWriter);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 102 */     if (this.packageWriter == null)
/*     */     {
/* 104 */       return;
/*     */     }
/* 106 */     build(this.layoutParser.parseXML("PackageDoc"), this.contentTree);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 113 */     return "PackageDoc";
/*     */   }
/*     */ 
/*     */   public void buildPackageDoc(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 123 */     paramContent = this.packageWriter.getPackageHeader(Util.getPackageName(this.packageDoc));
/* 124 */     buildChildren(paramXMLNode, paramContent);
/* 125 */     this.packageWriter.addPackageFooter(paramContent);
/* 126 */     this.packageWriter.printDocument(paramContent);
/* 127 */     this.packageWriter.close();
/* 128 */     Util.copyDocFiles(this.configuration, this.packageDoc);
/*     */   }
/*     */ 
/*     */   public void buildContent(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 139 */     Content localContent = this.packageWriter.getContentHeader();
/* 140 */     buildChildren(paramXMLNode, localContent);
/* 141 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 152 */     Content localContent = this.packageWriter.getSummaryHeader();
/* 153 */     buildChildren(paramXMLNode, localContent);
/* 154 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildInterfaceSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 166 */     String str = this.configuration
/* 166 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 167 */       .getText("doclet.Interface_Summary"), 
/* 167 */       this.configuration
/* 168 */       .getText("doclet.interfaces"));
/*     */ 
/* 171 */     String[] arrayOfString = { this.configuration
/* 170 */       .getText("doclet.Interface"), 
/* 170 */       this.configuration
/* 171 */       .getText("doclet.Description") };
/*     */ 
/* 176 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 174 */       .isIncluded() ? this.packageDoc
/* 175 */       .interfaces() : this.configuration.classDocCatalog
/* 176 */       .interfaces(
/* 177 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 178 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 179 */     if (arrayOfClassDoc.length > 0)
/* 180 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 182 */         .getText("doclet.Interface_Summary"), 
/* 182 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildClassSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 196 */     String str = this.configuration
/* 196 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 197 */       .getText("doclet.Class_Summary"), 
/* 197 */       this.configuration
/* 198 */       .getText("doclet.classes"));
/*     */ 
/* 201 */     String[] arrayOfString = { this.configuration
/* 200 */       .getText("doclet.Class"), 
/* 200 */       this.configuration
/* 201 */       .getText("doclet.Description") };
/*     */ 
/* 206 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 204 */       .isIncluded() ? this.packageDoc
/* 205 */       .ordinaryClasses() : this.configuration.classDocCatalog
/* 206 */       .ordinaryClasses(
/* 207 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 208 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 209 */     if (arrayOfClassDoc.length > 0)
/* 210 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 212 */         .getText("doclet.Class_Summary"), 
/* 212 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildEnumSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 226 */     String str = this.configuration
/* 226 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 227 */       .getText("doclet.Enum_Summary"), 
/* 227 */       this.configuration
/* 228 */       .getText("doclet.enums"));
/*     */ 
/* 231 */     String[] arrayOfString = { this.configuration
/* 230 */       .getText("doclet.Enum"), 
/* 230 */       this.configuration
/* 231 */       .getText("doclet.Description") };
/*     */ 
/* 236 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 234 */       .isIncluded() ? this.packageDoc
/* 235 */       .enums() : this.configuration.classDocCatalog
/* 236 */       .enums(
/* 237 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 238 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 239 */     if (arrayOfClassDoc.length > 0)
/* 240 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 242 */         .getText("doclet.Enum_Summary"), 
/* 242 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildExceptionSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 256 */     String str = this.configuration
/* 256 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 257 */       .getText("doclet.Exception_Summary"), 
/* 257 */       this.configuration
/* 258 */       .getText("doclet.exceptions"));
/*     */ 
/* 261 */     String[] arrayOfString = { this.configuration
/* 260 */       .getText("doclet.Exception"), 
/* 260 */       this.configuration
/* 261 */       .getText("doclet.Description") };
/*     */ 
/* 266 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 264 */       .isIncluded() ? this.packageDoc
/* 265 */       .exceptions() : this.configuration.classDocCatalog
/* 266 */       .exceptions(
/* 267 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 268 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 269 */     if (arrayOfClassDoc.length > 0)
/* 270 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 272 */         .getText("doclet.Exception_Summary"), 
/* 272 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildErrorSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 286 */     String str = this.configuration
/* 286 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 287 */       .getText("doclet.Error_Summary"), 
/* 287 */       this.configuration
/* 288 */       .getText("doclet.errors"));
/*     */ 
/* 291 */     String[] arrayOfString = { this.configuration
/* 290 */       .getText("doclet.Error"), 
/* 290 */       this.configuration
/* 291 */       .getText("doclet.Description") };
/*     */ 
/* 296 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 294 */       .isIncluded() ? this.packageDoc
/* 295 */       .errors() : this.configuration.classDocCatalog
/* 296 */       .errors(
/* 297 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 298 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 299 */     if (arrayOfClassDoc.length > 0)
/* 300 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 302 */         .getText("doclet.Error_Summary"), 
/* 302 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 316 */     String str = this.configuration
/* 316 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 317 */       .getText("doclet.Annotation_Types_Summary"), 
/* 317 */       this.configuration
/* 318 */       .getText("doclet.annotationtypes"));
/*     */ 
/* 321 */     String[] arrayOfString = { this.configuration
/* 320 */       .getText("doclet.AnnotationType"), 
/* 320 */       this.configuration
/* 321 */       .getText("doclet.Description") };
/*     */ 
/* 326 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 324 */       .isIncluded() ? this.packageDoc
/* 325 */       .annotationTypes() : this.configuration.classDocCatalog
/* 326 */       .annotationTypes(
/* 327 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 328 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 329 */     if (arrayOfClassDoc.length > 0)
/* 330 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 332 */         .getText("doclet.Annotation_Types_Summary"), 
/* 332 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPackageDescription(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 346 */     if (this.configuration.nocomment) {
/* 347 */       return;
/*     */     }
/* 349 */     this.packageWriter.addPackageDescription(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPackageTags(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 359 */     if (this.configuration.nocomment) {
/* 360 */       return;
/*     */     }
/* 362 */     this.packageWriter.addPackageTags(paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.PackageSummaryBuilder
 * JD-Core Version:    0.6.2
 */