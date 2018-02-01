/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfilePackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class ProfilePackageSummaryBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "PackageDoc";
/*     */   private final PackageDoc packageDoc;
/*     */   private final String profileName;
/*     */   private final int profileValue;
/*     */   private final ProfilePackageSummaryWriter profilePackageWriter;
/*     */   private Content contentTree;
/*     */ 
/*     */   private ProfilePackageSummaryBuilder(AbstractBuilder.Context paramContext, PackageDoc paramPackageDoc, ProfilePackageSummaryWriter paramProfilePackageSummaryWriter, Profile paramProfile)
/*     */   {
/*  88 */     super(paramContext);
/*  89 */     this.packageDoc = paramPackageDoc;
/*  90 */     this.profilePackageWriter = paramProfilePackageSummaryWriter;
/*  91 */     this.profileName = paramProfile.name;
/*  92 */     this.profileValue = paramProfile.value;
/*     */   }
/*     */ 
/*     */   public static ProfilePackageSummaryBuilder getInstance(AbstractBuilder.Context paramContext, PackageDoc paramPackageDoc, ProfilePackageSummaryWriter paramProfilePackageSummaryWriter, Profile paramProfile)
/*     */   {
/* 109 */     return new ProfilePackageSummaryBuilder(paramContext, paramPackageDoc, paramProfilePackageSummaryWriter, paramProfile);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 117 */     if (this.profilePackageWriter == null)
/*     */     {
/* 119 */       return;
/*     */     }
/* 121 */     build(this.layoutParser.parseXML("PackageDoc"), this.contentTree);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 128 */     return "PackageDoc";
/*     */   }
/*     */ 
/*     */   public void buildPackageDoc(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 138 */     paramContent = this.profilePackageWriter.getPackageHeader(
/* 139 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 140 */     buildChildren(paramXMLNode, paramContent);
/* 141 */     this.profilePackageWriter.addPackageFooter(paramContent);
/* 142 */     this.profilePackageWriter.printDocument(paramContent);
/* 143 */     this.profilePackageWriter.close();
/* 144 */     Util.copyDocFiles(this.configuration, this.packageDoc);
/*     */   }
/*     */ 
/*     */   public void buildContent(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 155 */     Content localContent = this.profilePackageWriter.getContentHeader();
/* 156 */     buildChildren(paramXMLNode, localContent);
/* 157 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 168 */     Content localContent = this.profilePackageWriter.getSummaryHeader();
/* 169 */     buildChildren(paramXMLNode, localContent);
/* 170 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildInterfaceSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 182 */     String str = this.configuration
/* 182 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 183 */       .getText("doclet.Interface_Summary"), 
/* 183 */       this.configuration
/* 184 */       .getText("doclet.interfaces"));
/*     */ 
/* 187 */     String[] arrayOfString = { this.configuration
/* 186 */       .getText("doclet.Interface"), 
/* 186 */       this.configuration
/* 187 */       .getText("doclet.Description") };
/*     */ 
/* 192 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 190 */       .isIncluded() ? this.packageDoc
/* 191 */       .interfaces() : this.configuration.classDocCatalog
/* 192 */       .interfaces(
/* 193 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 194 */     if (arrayOfClassDoc.length > 0)
/* 195 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 197 */         .getText("doclet.Interface_Summary"), 
/* 197 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildClassSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 211 */     String str = this.configuration
/* 211 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 212 */       .getText("doclet.Class_Summary"), 
/* 212 */       this.configuration
/* 213 */       .getText("doclet.classes"));
/*     */ 
/* 216 */     String[] arrayOfString = { this.configuration
/* 215 */       .getText("doclet.Class"), 
/* 215 */       this.configuration
/* 216 */       .getText("doclet.Description") };
/*     */ 
/* 221 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 219 */       .isIncluded() ? this.packageDoc
/* 220 */       .ordinaryClasses() : this.configuration.classDocCatalog
/* 221 */       .ordinaryClasses(
/* 222 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 223 */     if (arrayOfClassDoc.length > 0)
/* 224 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 226 */         .getText("doclet.Class_Summary"), 
/* 226 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildEnumSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 240 */     String str = this.configuration
/* 240 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 241 */       .getText("doclet.Enum_Summary"), 
/* 241 */       this.configuration
/* 242 */       .getText("doclet.enums"));
/*     */ 
/* 245 */     String[] arrayOfString = { this.configuration
/* 244 */       .getText("doclet.Enum"), 
/* 244 */       this.configuration
/* 245 */       .getText("doclet.Description") };
/*     */ 
/* 250 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 248 */       .isIncluded() ? this.packageDoc
/* 249 */       .enums() : this.configuration.classDocCatalog
/* 250 */       .enums(
/* 251 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 252 */     if (arrayOfClassDoc.length > 0)
/* 253 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 255 */         .getText("doclet.Enum_Summary"), 
/* 255 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildExceptionSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 269 */     String str = this.configuration
/* 269 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 270 */       .getText("doclet.Exception_Summary"), 
/* 270 */       this.configuration
/* 271 */       .getText("doclet.exceptions"));
/*     */ 
/* 274 */     String[] arrayOfString = { this.configuration
/* 273 */       .getText("doclet.Exception"), 
/* 273 */       this.configuration
/* 274 */       .getText("doclet.Description") };
/*     */ 
/* 279 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 277 */       .isIncluded() ? this.packageDoc
/* 278 */       .exceptions() : this.configuration.classDocCatalog
/* 279 */       .exceptions(
/* 280 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 281 */     if (arrayOfClassDoc.length > 0)
/* 282 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 284 */         .getText("doclet.Exception_Summary"), 
/* 284 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildErrorSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 298 */     String str = this.configuration
/* 298 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 299 */       .getText("doclet.Error_Summary"), 
/* 299 */       this.configuration
/* 300 */       .getText("doclet.errors"));
/*     */ 
/* 303 */     String[] arrayOfString = { this.configuration
/* 302 */       .getText("doclet.Error"), 
/* 302 */       this.configuration
/* 303 */       .getText("doclet.Description") };
/*     */ 
/* 308 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 306 */       .isIncluded() ? this.packageDoc
/* 307 */       .errors() : this.configuration.classDocCatalog
/* 308 */       .errors(
/* 309 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 310 */     if (arrayOfClassDoc.length > 0)
/* 311 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 313 */         .getText("doclet.Error_Summary"), 
/* 313 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 327 */     String str = this.configuration
/* 327 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 328 */       .getText("doclet.Annotation_Types_Summary"), 
/* 328 */       this.configuration
/* 329 */       .getText("doclet.annotationtypes"));
/*     */ 
/* 332 */     String[] arrayOfString = { this.configuration
/* 331 */       .getText("doclet.AnnotationType"), 
/* 331 */       this.configuration
/* 332 */       .getText("doclet.Description") };
/*     */ 
/* 337 */     ClassDoc[] arrayOfClassDoc = this.packageDoc
/* 335 */       .isIncluded() ? this.packageDoc
/* 336 */       .annotationTypes() : this.configuration.classDocCatalog
/* 337 */       .annotationTypes(
/* 338 */       Util.getPackageName(this.packageDoc));
/*     */ 
/* 339 */     if (arrayOfClassDoc.length > 0)
/* 340 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 342 */         .getText("doclet.Annotation_Types_Summary"), 
/* 342 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPackageDescription(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 356 */     if (this.configuration.nocomment) {
/* 357 */       return;
/*     */     }
/* 359 */     this.profilePackageWriter.addPackageDescription(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPackageTags(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 369 */     if (this.configuration.nocomment) {
/* 370 */       return;
/*     */     }
/* 372 */     this.profilePackageWriter.addPackageTags(paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.ProfilePackageSummaryBuilder
 * JD-Core Version:    0.6.2
 */