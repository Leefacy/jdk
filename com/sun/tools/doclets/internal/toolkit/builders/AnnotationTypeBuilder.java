/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class AnnotationTypeBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "AnnotationTypeDoc";
/*     */   private final AnnotationTypeDoc annotationTypeDoc;
/*     */   private final AnnotationTypeWriter writer;
/*     */   private Content contentTree;
/*     */ 
/*     */   private AnnotationTypeBuilder(AbstractBuilder.Context paramContext, AnnotationTypeDoc paramAnnotationTypeDoc, AnnotationTypeWriter paramAnnotationTypeWriter)
/*     */   {
/*  79 */     super(paramContext);
/*  80 */     this.annotationTypeDoc = paramAnnotationTypeDoc;
/*  81 */     this.writer = paramAnnotationTypeWriter;
/*     */   }
/*     */ 
/*     */   public static AnnotationTypeBuilder getInstance(AbstractBuilder.Context paramContext, AnnotationTypeDoc paramAnnotationTypeDoc, AnnotationTypeWriter paramAnnotationTypeWriter)
/*     */     throws Exception
/*     */   {
/*  95 */     return new AnnotationTypeBuilder(paramContext, paramAnnotationTypeDoc, paramAnnotationTypeWriter);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 102 */     build(this.layoutParser.parseXML("AnnotationTypeDoc"), this.contentTree);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 109 */     return "AnnotationTypeDoc";
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeDoc(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 119 */     paramContent = this.writer.getHeader(this.configuration.getText("doclet.AnnotationType") + " " + this.annotationTypeDoc
/* 120 */       .name());
/* 121 */     Content localContent = this.writer.getAnnotationContentHeader();
/* 122 */     buildChildren(paramXMLNode, localContent);
/* 123 */     paramContent.addContent(localContent);
/* 124 */     this.writer.addFooter(paramContent);
/* 125 */     this.writer.printDocument(paramContent);
/* 126 */     this.writer.close();
/* 127 */     copyDocFiles();
/*     */   }
/*     */ 
/*     */   private void copyDocFiles()
/*     */   {
/* 134 */     PackageDoc localPackageDoc = this.annotationTypeDoc.containingPackage();
/* 135 */     if ((this.configuration.packages == null) || 
/* 136 */       (Arrays.binarySearch(this.configuration.packages, localPackageDoc) < 0))
/*     */     {
/* 138 */       if (!this.containingPackagesSeen
/* 138 */         .contains(localPackageDoc
/* 138 */         .name()))
/*     */       {
/* 142 */         Util.copyDocFiles(this.configuration, localPackageDoc);
/* 143 */         this.containingPackagesSeen.add(localPackageDoc.name());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 154 */     Content localContent = this.writer.getAnnotationInfoTreeHeader();
/* 155 */     buildChildren(paramXMLNode, localContent);
/* 156 */     paramContent.addContent(this.writer.getAnnotationInfo(localContent));
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 166 */     this.writer.addAnnotationTypeDeprecationInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 177 */     StringBuilder localStringBuilder = new StringBuilder(this.annotationTypeDoc
/* 177 */       .modifiers() + " ");
/* 178 */     this.writer.addAnnotationTypeSignature(Util.replaceText(localStringBuilder
/* 179 */       .toString(), "interface", "@interface"), paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeDescription(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 189 */     this.writer.addAnnotationTypeDescription(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 199 */     this.writer.addAnnotationTypeTagInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMemberSummary(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 210 */     Content localContent = this.writer.getMemberTreeHeader();
/* 211 */     this.configuration.getBuilderFactory()
/* 212 */       .getMemberSummaryBuilder(this.writer)
/* 212 */       .buildChildren(paramXMLNode, localContent);
/* 213 */     paramContent.addContent(this.writer.getMemberSummaryTree(localContent));
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeMemberDetails(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 223 */     Content localContent = this.writer.getMemberTreeHeader();
/* 224 */     buildChildren(paramXMLNode, localContent);
/* 225 */     if (localContent.isValid())
/* 226 */       paramContent.addContent(this.writer.getMemberDetailsTree(localContent));
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeFieldDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 238 */     this.configuration.getBuilderFactory()
/* 239 */       .getAnnotationTypeFieldsBuilder(this.writer)
/* 239 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeOptionalMemberDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 250 */     this.configuration.getBuilderFactory()
/* 251 */       .getAnnotationTypeOptionalMemberBuilder(this.writer)
/* 251 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeRequiredMemberDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 262 */     this.configuration.getBuilderFactory()
/* 263 */       .getAnnotationTypeRequiredMemberBuilder(this.writer)
/* 263 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.AnnotationTypeBuilder
 * JD-Core Version:    0.6.2
 */