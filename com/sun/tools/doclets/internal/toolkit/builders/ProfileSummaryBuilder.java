/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfileSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ProfileSummaryBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "ProfileDoc";
/*     */   private final Profile profile;
/*     */   private final ProfileSummaryWriter profileWriter;
/*     */   private Content contentTree;
/*     */   private PackageDoc pkg;
/*     */ 
/*     */   private ProfileSummaryBuilder(AbstractBuilder.Context paramContext, Profile paramProfile, ProfileSummaryWriter paramProfileSummaryWriter)
/*     */   {
/*  81 */     super(paramContext);
/*  82 */     this.profile = paramProfile;
/*  83 */     this.profileWriter = paramProfileSummaryWriter;
/*     */   }
/*     */ 
/*     */   public static ProfileSummaryBuilder getInstance(AbstractBuilder.Context paramContext, Profile paramProfile, ProfileSummaryWriter paramProfileSummaryWriter)
/*     */   {
/*  98 */     return new ProfileSummaryBuilder(paramContext, paramProfile, paramProfileSummaryWriter);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 105 */     if (this.profileWriter == null)
/*     */     {
/* 107 */       return;
/*     */     }
/* 109 */     build(this.layoutParser.parseXML("ProfileDoc"), this.contentTree);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 116 */     return "ProfileDoc";
/*     */   }
/*     */ 
/*     */   public void buildProfileDoc(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 126 */     paramContent = this.profileWriter.getProfileHeader(this.profile.name);
/* 127 */     buildChildren(paramXMLNode, paramContent);
/* 128 */     this.profileWriter.addProfileFooter(paramContent);
/* 129 */     this.profileWriter.printDocument(paramContent);
/* 130 */     this.profileWriter.close();
/* 131 */     Util.copyDocFiles(this.configuration, DocPaths.profileSummary(this.profile.name));
/*     */   }
/*     */ 
/*     */   public void buildContent(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 142 */     Content localContent = this.profileWriter.getContentHeader();
/* 143 */     buildChildren(paramXMLNode, localContent);
/* 144 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 155 */     Content localContent = this.profileWriter.getSummaryHeader();
/* 156 */     buildChildren(paramXMLNode, localContent);
/* 157 */     paramContent.addContent(this.profileWriter.getSummaryTree(localContent));
/*     */   }
/*     */ 
/*     */   public void buildPackageSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 168 */     PackageDoc[] arrayOfPackageDoc = (PackageDoc[])this.configuration.profilePackages.get(this.profile.name);
/* 169 */     for (int i = 0; i < arrayOfPackageDoc.length; i++) {
/* 170 */       this.pkg = arrayOfPackageDoc[i];
/* 171 */       Content localContent = this.profileWriter.getPackageSummaryHeader(this.pkg);
/* 172 */       buildChildren(paramXMLNode, localContent);
/* 173 */       paramContent.addContent(this.profileWriter.getPackageSummaryTree(localContent));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildInterfaceSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 187 */     String str = this.configuration
/* 187 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 188 */       .getText("doclet.Interface_Summary"), 
/* 188 */       this.configuration
/* 189 */       .getText("doclet.interfaces"));
/*     */ 
/* 192 */     String[] arrayOfString = { this.configuration
/* 191 */       .getText("doclet.Interface"), 
/* 191 */       this.configuration
/* 192 */       .getText("doclet.Description") };
/*     */ 
/* 194 */     ClassDoc[] arrayOfClassDoc = this.pkg.interfaces();
/* 195 */     if (arrayOfClassDoc.length > 0)
/* 196 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 198 */         .getText("doclet.Interface_Summary"), 
/* 198 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildClassSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 212 */     String str = this.configuration
/* 212 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 213 */       .getText("doclet.Class_Summary"), 
/* 213 */       this.configuration
/* 214 */       .getText("doclet.classes"));
/*     */ 
/* 217 */     String[] arrayOfString = { this.configuration
/* 216 */       .getText("doclet.Class"), 
/* 216 */       this.configuration
/* 217 */       .getText("doclet.Description") };
/*     */ 
/* 219 */     ClassDoc[] arrayOfClassDoc = this.pkg.ordinaryClasses();
/* 220 */     if (arrayOfClassDoc.length > 0)
/* 221 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 223 */         .getText("doclet.Class_Summary"), 
/* 223 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildEnumSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 237 */     String str = this.configuration
/* 237 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 238 */       .getText("doclet.Enum_Summary"), 
/* 238 */       this.configuration
/* 239 */       .getText("doclet.enums"));
/*     */ 
/* 242 */     String[] arrayOfString = { this.configuration
/* 241 */       .getText("doclet.Enum"), 
/* 241 */       this.configuration
/* 242 */       .getText("doclet.Description") };
/*     */ 
/* 244 */     ClassDoc[] arrayOfClassDoc = this.pkg.enums();
/* 245 */     if (arrayOfClassDoc.length > 0)
/* 246 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 248 */         .getText("doclet.Enum_Summary"), 
/* 248 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildExceptionSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 262 */     String str = this.configuration
/* 262 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 263 */       .getText("doclet.Exception_Summary"), 
/* 263 */       this.configuration
/* 264 */       .getText("doclet.exceptions"));
/*     */ 
/* 267 */     String[] arrayOfString = { this.configuration
/* 266 */       .getText("doclet.Exception"), 
/* 266 */       this.configuration
/* 267 */       .getText("doclet.Description") };
/*     */ 
/* 269 */     ClassDoc[] arrayOfClassDoc = this.pkg.exceptions();
/* 270 */     if (arrayOfClassDoc.length > 0)
/* 271 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 273 */         .getText("doclet.Exception_Summary"), 
/* 273 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildErrorSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 287 */     String str = this.configuration
/* 287 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 288 */       .getText("doclet.Error_Summary"), 
/* 288 */       this.configuration
/* 289 */       .getText("doclet.errors"));
/*     */ 
/* 292 */     String[] arrayOfString = { this.configuration
/* 291 */       .getText("doclet.Error"), 
/* 291 */       this.configuration
/* 292 */       .getText("doclet.Description") };
/*     */ 
/* 294 */     ClassDoc[] arrayOfClassDoc = this.pkg.errors();
/* 295 */     if (arrayOfClassDoc.length > 0)
/* 296 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/* 298 */         .getText("doclet.Error_Summary"), 
/* 298 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 312 */     String str = this.configuration
/* 312 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 313 */       .getText("doclet.Annotation_Types_Summary"), 
/* 313 */       this.configuration
/* 314 */       .getText("doclet.annotationtypes"));
/*     */ 
/* 317 */     String[] arrayOfString = { this.configuration
/* 316 */       .getText("doclet.AnnotationType"), 
/* 316 */       this.configuration
/* 317 */       .getText("doclet.Description") };
/*     */ 
/* 319 */     AnnotationTypeDoc[] arrayOfAnnotationTypeDoc = this.pkg.annotationTypes();
/* 320 */     if (arrayOfAnnotationTypeDoc.length > 0)
/* 321 */       this.profileWriter.addClassesSummary(arrayOfAnnotationTypeDoc, this.configuration
/* 323 */         .getText("doclet.Annotation_Types_Summary"), 
/* 323 */         str, arrayOfString, paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.ProfileSummaryBuilder
 * JD-Core Version:    0.6.2
 */