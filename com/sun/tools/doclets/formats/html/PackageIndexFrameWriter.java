/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PackageIndexFrameWriter extends AbstractPackageIndexWriter
/*     */ {
/*     */   public PackageIndexFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  56 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl)
/*     */   {
/*  65 */     DocPath localDocPath = DocPaths.OVERVIEW_FRAME;
/*     */     try {
/*  67 */       PackageIndexFrameWriter localPackageIndexFrameWriter = new PackageIndexFrameWriter(paramConfigurationImpl, localDocPath);
/*  68 */       localPackageIndexFrameWriter.buildPackageIndexFile("doclet.Window_Overview", false);
/*  69 */       localPackageIndexFrameWriter.close();
/*     */     } catch (IOException localIOException) {
/*  71 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  73 */         .toString(), localDocPath });
/*  74 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent)
/*     */   {
/*  83 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, this.packagesLabel);
/*     */ 
/*  85 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.indexContainer, localHtmlTree1);
/*  86 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.UL);
/*  87 */     localHtmlTree3.setTitle(this.packagesLabel);
/*  88 */     for (int i = 0; i < paramArrayOfPackageDoc.length; i++)
/*     */     {
/*  91 */       if ((paramArrayOfPackageDoc[i] != null) && ((!this.configuration.nodeprecated) || 
/*  92 */         (!Util.isDeprecated(paramArrayOfPackageDoc[i]))))
/*     */       {
/*  93 */         localHtmlTree3.addContent(getPackage(paramArrayOfPackageDoc[i]));
/*     */       }
/*     */     }
/*  96 */     localHtmlTree2.addContent(localHtmlTree3);
/*  97 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected Content getPackage(PackageDoc paramPackageDoc)
/*     */   {
/*     */     Object localObject;
/*     */     Content localContent;
/* 109 */     if (paramPackageDoc.name().length() > 0) {
/* 110 */       localObject = getPackageLabel(paramPackageDoc.name());
/* 111 */       localContent = getHyperLink(pathString(paramPackageDoc, DocPaths.PACKAGE_FRAME), (Content)localObject, "", "packageFrame");
/*     */     }
/*     */     else
/*     */     {
/* 115 */       localObject = new StringContent("<unnamed package>");
/* 116 */       localContent = getHyperLink(DocPaths.PACKAGE_FRAME, (Content)localObject, "", "packageFrame");
/*     */     }
/*     */ 
/* 119 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 120 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarHeader(Content paramContent)
/*     */   {
/*     */     RawHtml localRawHtml;
/* 128 */     if (this.configuration.packagesheader.length() > 0)
/* 129 */       localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.packagesheader));
/*     */     else {
/* 131 */       localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.header));
/*     */     }
/* 133 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.bar, localRawHtml);
/*     */ 
/* 135 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addOverviewHeader(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addAllClassesLink(Content paramContent)
/*     */   {
/* 151 */     Content localContent = getHyperLink(DocPaths.ALLCLASSES_FRAME, this.allclassesLabel, "", "packageFrame");
/*     */ 
/* 153 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 154 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addAllProfilesLink(Content paramContent)
/*     */   {
/* 164 */     Content localContent = getHyperLink(DocPaths.PROFILE_OVERVIEW_FRAME, this.allprofilesLabel, "", "packageListFrame");
/*     */ 
/* 166 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 167 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarFooter(Content paramContent)
/*     */   {
/* 174 */     HtmlTree localHtmlTree = HtmlTree.P(getSpace());
/* 175 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PackageIndexFrameWriter
 * JD-Core Version:    0.6.2
 */