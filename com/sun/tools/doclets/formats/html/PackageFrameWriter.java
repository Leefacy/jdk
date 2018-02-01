/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PackageFrameWriter extends HtmlDocletWriter
/*     */ {
/*     */   private PackageDoc packageDoc;
/*     */   private Set<ClassDoc> documentedClasses;
/*     */ 
/*     */   public PackageFrameWriter(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc)
/*     */     throws IOException
/*     */   {
/*  76 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc).resolve(DocPaths.PACKAGE_FRAME));
/*  77 */     this.packageDoc = paramPackageDoc;
/*  78 */     if (paramConfigurationImpl.root.specifiedPackages().length == 0)
/*  79 */       this.documentedClasses = new HashSet(Arrays.asList(paramConfigurationImpl.root.classes()));
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc)
/*     */   {
/*     */     try
/*     */     {
/*  94 */       PackageFrameWriter localPackageFrameWriter = new PackageFrameWriter(paramConfigurationImpl, paramPackageDoc);
/*  95 */       String str = Util.getPackageName(paramPackageDoc);
/*  96 */       HtmlTree localHtmlTree1 = localPackageFrameWriter.getBody(false, localPackageFrameWriter.getWindowTitle(str));
/*  97 */       StringContent localStringContent = new StringContent(str);
/*  98 */       HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.bar, localPackageFrameWriter
/*  99 */         .getTargetPackageLink(paramPackageDoc, "classFrame", localStringContent));
/*     */ 
/* 100 */       localHtmlTree1.addContent(localHtmlTree2);
/* 101 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 102 */       localHtmlTree3.addStyle(HtmlStyle.indexContainer);
/* 103 */       localPackageFrameWriter.addClassListing(localHtmlTree3);
/* 104 */       localHtmlTree1.addContent(localHtmlTree3);
/* 105 */       localPackageFrameWriter.printHtmlDocument(paramConfigurationImpl.metakeywords
/* 106 */         .getMetaKeywords(paramPackageDoc), 
/* 106 */         false, localHtmlTree1);
/* 107 */       localPackageFrameWriter.close();
/*     */     } catch (IOException localIOException) {
/* 109 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 111 */         .toString(), DocPaths.PACKAGE_FRAME.getPath() });
/* 112 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addClassListing(Content paramContent)
/*     */   {
/* 124 */     ConfigurationImpl localConfigurationImpl = this.configuration;
/* 125 */     if (this.packageDoc.isIncluded()) {
/* 126 */       addClassKindListing(this.packageDoc.interfaces(), 
/* 127 */         getResource("doclet.Interfaces"), 
/* 127 */         paramContent);
/* 128 */       addClassKindListing(this.packageDoc.ordinaryClasses(), 
/* 129 */         getResource("doclet.Classes"), 
/* 129 */         paramContent);
/* 130 */       addClassKindListing(this.packageDoc.enums(), 
/* 131 */         getResource("doclet.Enums"), 
/* 131 */         paramContent);
/* 132 */       addClassKindListing(this.packageDoc.exceptions(), 
/* 133 */         getResource("doclet.Exceptions"), 
/* 133 */         paramContent);
/* 134 */       addClassKindListing(this.packageDoc.errors(), 
/* 135 */         getResource("doclet.Errors"), 
/* 135 */         paramContent);
/* 136 */       addClassKindListing(this.packageDoc.annotationTypes(), 
/* 137 */         getResource("doclet.AnnotationTypes"), 
/* 137 */         paramContent);
/*     */     } else {
/* 139 */       String str = Util.getPackageName(this.packageDoc);
/* 140 */       addClassKindListing(localConfigurationImpl.classDocCatalog.interfaces(str), 
/* 141 */         getResource("doclet.Interfaces"), 
/* 141 */         paramContent);
/* 142 */       addClassKindListing(localConfigurationImpl.classDocCatalog.ordinaryClasses(str), 
/* 143 */         getResource("doclet.Classes"), 
/* 143 */         paramContent);
/* 144 */       addClassKindListing(localConfigurationImpl.classDocCatalog.enums(str), 
/* 145 */         getResource("doclet.Enums"), 
/* 145 */         paramContent);
/* 146 */       addClassKindListing(localConfigurationImpl.classDocCatalog.exceptions(str), 
/* 147 */         getResource("doclet.Exceptions"), 
/* 147 */         paramContent);
/* 148 */       addClassKindListing(localConfigurationImpl.classDocCatalog.errors(str), 
/* 149 */         getResource("doclet.Errors"), 
/* 149 */         paramContent);
/* 150 */       addClassKindListing(localConfigurationImpl.classDocCatalog.annotationTypes(str), 
/* 151 */         getResource("doclet.AnnotationTypes"), 
/* 151 */         paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addClassKindListing(ClassDoc[] paramArrayOfClassDoc, Content paramContent1, Content paramContent2)
/*     */   {
/* 164 */     paramArrayOfClassDoc = Util.filterOutPrivateClasses(paramArrayOfClassDoc, this.configuration.javafx);
/* 165 */     if (paramArrayOfClassDoc.length > 0) {
/* 166 */       Arrays.sort(paramArrayOfClassDoc);
/* 167 */       int i = 0;
/* 168 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.UL);
/* 169 */       localHtmlTree1.setTitle(paramContent1);
/* 170 */       for (int j = 0; j < paramArrayOfClassDoc.length; j++)
/* 171 */         if ((this.documentedClasses == null) || 
/* 172 */           (this.documentedClasses
/* 172 */           .contains(paramArrayOfClassDoc[j])))
/*     */         {
/* 175 */           if ((Util.isCoreClass(paramArrayOfClassDoc[j])) && 
/* 176 */             (this.configuration
/* 176 */             .isGeneratedDoc(paramArrayOfClassDoc[j])))
/*     */           {
/* 179 */             if (i == 0) {
/* 180 */               localObject = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, paramContent1);
/*     */ 
/* 182 */               paramContent2.addContent((Content)localObject);
/* 183 */               i = 1;
/*     */             }
/* 185 */             Object localObject = new StringContent(paramArrayOfClassDoc[j].name());
/* 186 */             if (paramArrayOfClassDoc[j].isInterface()) localObject = HtmlTree.SPAN(HtmlStyle.interfaceName, (Content)localObject);
/* 187 */             Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE_FRAME, paramArrayOfClassDoc[j])
/* 188 */               .label((Content)localObject)
/* 188 */               .target("classFrame"));
/* 189 */             HtmlTree localHtmlTree2 = HtmlTree.LI(localContent);
/* 190 */             localHtmlTree1.addContent(localHtmlTree2);
/*     */           }
/*     */         }
/* 192 */       paramContent2.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PackageFrameWriter
 * JD-Core Version:    0.6.2
 */