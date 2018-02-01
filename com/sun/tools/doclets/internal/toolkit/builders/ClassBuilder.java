/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ClassBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "ClassDoc";
/*     */   private final ClassDoc classDoc;
/*     */   private final ClassWriter writer;
/*     */   private final boolean isInterface;
/*     */   private final boolean isEnum;
/*     */   private Content contentTree;
/*     */ 
/*     */   private ClassBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, ClassWriter paramClassWriter)
/*     */   {
/*  88 */     super(paramContext);
/*  89 */     this.classDoc = paramClassDoc;
/*  90 */     this.writer = paramClassWriter;
/*  91 */     if (paramClassDoc.isInterface()) {
/*  92 */       this.isInterface = true;
/*  93 */       this.isEnum = false;
/*  94 */     } else if (paramClassDoc.isEnum()) {
/*  95 */       this.isInterface = false;
/*  96 */       this.isEnum = true;
/*  97 */       Util.setEnumDocumentation(this.configuration, paramClassDoc);
/*     */     } else {
/*  99 */       this.isInterface = false;
/* 100 */       this.isEnum = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ClassBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, ClassWriter paramClassWriter)
/*     */   {
/* 113 */     return new ClassBuilder(paramContext, paramClassDoc, paramClassWriter);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 120 */     build(this.layoutParser.parseXML("ClassDoc"), this.contentTree);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 127 */     return "ClassDoc";
/*     */   }
/*     */ 
/*     */   public void buildClassDoc(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/*     */     String str;
/* 138 */     if (this.isInterface)
/* 139 */       str = "doclet.Interface";
/* 140 */     else if (this.isEnum)
/* 141 */       str = "doclet.Enum";
/*     */     else {
/* 143 */       str = "doclet.Class";
/*     */     }
/* 145 */     paramContent = this.writer.getHeader(this.configuration.getText(str) + " " + this.classDoc
/* 146 */       .name());
/* 147 */     Content localContent = this.writer.getClassContentHeader();
/* 148 */     buildChildren(paramXMLNode, localContent);
/* 149 */     paramContent.addContent(localContent);
/* 150 */     this.writer.addFooter(paramContent);
/* 151 */     this.writer.printDocument(paramContent);
/* 152 */     this.writer.close();
/* 153 */     copyDocFiles();
/*     */   }
/*     */ 
/*     */   public void buildClassTree(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 163 */     this.writer.addClassTree(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildClassInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 173 */     Content localContent = this.writer.getClassInfoTreeHeader();
/* 174 */     buildChildren(paramXMLNode, localContent);
/* 175 */     paramContent.addContent(this.writer.getClassInfo(localContent));
/*     */   }
/*     */ 
/*     */   public void buildTypeParamInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 185 */     this.writer.addTypeParamInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildSuperInterfacesInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 195 */     this.writer.addSuperInterfacesInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildImplementedInterfacesInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 205 */     this.writer.addImplementedInterfacesInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildSubClassInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 215 */     this.writer.addSubClassInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildSubInterfacesInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 225 */     this.writer.addSubInterfacesInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildInterfaceUsageInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 235 */     this.writer.addInterfaceUsageInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildFunctionalInterfaceInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 245 */     this.writer.addFunctionalInterfaceInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 255 */     this.writer.addClassDeprecationInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildNestedClassInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 265 */     this.writer.addNestedClassInfo(paramContent);
/*     */   }
/*     */ 
/*     */   private void copyDocFiles()
/*     */   {
/* 272 */     PackageDoc localPackageDoc = this.classDoc.containingPackage();
/* 273 */     if ((this.configuration.packages == null) || 
/* 274 */       (Arrays.binarySearch(this.configuration.packages, localPackageDoc) < 0))
/*     */     {
/* 276 */       if (!this.containingPackagesSeen
/* 276 */         .contains(localPackageDoc
/* 276 */         .name()))
/*     */       {
/* 280 */         Util.copyDocFiles(this.configuration, localPackageDoc);
/* 281 */         this.containingPackagesSeen.add(localPackageDoc.name());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildClassSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 292 */     StringBuilder localStringBuilder = new StringBuilder(this.classDoc.modifiers());
/* 293 */     localStringBuilder.append(localStringBuilder.length() == 0 ? "" : " ");
/* 294 */     if (this.isEnum) {
/* 295 */       localStringBuilder.append("enum ");
/*     */       int i;
/* 297 */       if ((i = localStringBuilder.indexOf("abstract")) >= 0) {
/* 298 */         localStringBuilder.delete(i, i + "abstract".length());
/*     */ 
/* 300 */         localStringBuilder = new StringBuilder(
/* 300 */           Util.replaceText(localStringBuilder
/* 300 */           .toString(), "  ", " "));
/*     */       }
/* 302 */       if ((i = localStringBuilder.indexOf("final")) >= 0) {
/* 303 */         localStringBuilder.delete(i, i + "final".length());
/*     */ 
/* 305 */         localStringBuilder = new StringBuilder(
/* 305 */           Util.replaceText(localStringBuilder
/* 305 */           .toString(), "  ", " "));
/*     */       }
/*     */ 
/*     */     }
/* 309 */     else if (!this.isInterface) {
/* 310 */       localStringBuilder.append("class ");
/*     */     }
/* 312 */     this.writer.addClassSignature(localStringBuilder.toString(), paramContent);
/*     */   }
/*     */ 
/*     */   public void buildClassDescription(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 322 */     this.writer.addClassDescription(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildClassTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 332 */     this.writer.addClassTagInfo(paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMemberSummary(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 342 */     Content localContent = this.writer.getMemberTreeHeader();
/* 343 */     this.configuration.getBuilderFactory()
/* 344 */       .getMemberSummaryBuilder(this.writer)
/* 344 */       .buildChildren(paramXMLNode, localContent);
/* 345 */     paramContent.addContent(this.writer.getMemberSummaryTree(localContent));
/*     */   }
/*     */ 
/*     */   public void buildMemberDetails(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 355 */     Content localContent = this.writer.getMemberTreeHeader();
/* 356 */     buildChildren(paramXMLNode, localContent);
/* 357 */     paramContent.addContent(this.writer.getMemberDetailsTree(localContent));
/*     */   }
/*     */ 
/*     */   public void buildEnumConstantsDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 368 */     this.configuration.getBuilderFactory()
/* 369 */       .getEnumConstantsBuilder(this.writer)
/* 369 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildFieldDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 380 */     this.configuration.getBuilderFactory()
/* 381 */       .getFieldBuilder(this.writer)
/* 381 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPropertyDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 391 */     this.configuration.getBuilderFactory()
/* 392 */       .getPropertyBuilder(this.writer)
/* 392 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildConstructorDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 403 */     this.configuration.getBuilderFactory()
/* 404 */       .getConstructorBuilder(this.writer)
/* 404 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMethodDetails(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 415 */     this.configuration.getBuilderFactory()
/* 416 */       .getMethodBuilder(this.writer)
/* 416 */       .buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.ClassBuilder
 * JD-Core Version:    0.6.2
 */