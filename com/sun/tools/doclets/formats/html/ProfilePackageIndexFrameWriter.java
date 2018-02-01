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
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ProfilePackageIndexFrameWriter extends AbstractProfileIndexWriter
/*     */ {
/*     */   public ProfilePackageIndexFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  58 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, String paramString)
/*     */   {
/*  69 */     DocPath localDocPath = DocPaths.profileFrame(paramString);
/*     */     try {
/*  71 */       ProfilePackageIndexFrameWriter localProfilePackageIndexFrameWriter = new ProfilePackageIndexFrameWriter(paramConfigurationImpl, localDocPath);
/*  72 */       localProfilePackageIndexFrameWriter.buildProfilePackagesIndexFile("doclet.Window_Overview", false, paramString);
/*  73 */       localProfilePackageIndexFrameWriter.close();
/*     */     } catch (IOException localIOException) {
/*  75 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  77 */         .toString(), localDocPath });
/*  78 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addProfilePackagesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3)
/*     */   {
/*  87 */     StringContent localStringContent = new StringContent(paramString3);
/*  88 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, 
/*  89 */       getTargetProfileLink("classFrame", localStringContent, paramString3));
/*     */ 
/*  90 */     localHtmlTree1.addContent(getSpace());
/*  91 */     localHtmlTree1.addContent(this.packagesLabel);
/*  92 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.indexContainer, localHtmlTree1);
/*  93 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.UL);
/*  94 */     localHtmlTree3.setTitle(this.packagesLabel);
/*  95 */     PackageDoc[] arrayOfPackageDoc = (PackageDoc[])this.configuration.profilePackages.get(paramString3);
/*  96 */     for (int i = 0; i < arrayOfPackageDoc.length; i++) {
/*  97 */       if ((!this.configuration.nodeprecated) || (!Util.isDeprecated(arrayOfPackageDoc[i]))) {
/*  98 */         localHtmlTree3.addContent(getPackage(arrayOfPackageDoc[i], paramString3));
/*     */       }
/*     */     }
/* 101 */     localHtmlTree2.addContent(localHtmlTree3);
/* 102 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected Content getPackage(PackageDoc paramPackageDoc, String paramString)
/*     */   {
/*     */     Object localObject;
/*     */     Content localContent;
/* 115 */     if (paramPackageDoc.name().length() > 0) {
/* 116 */       localObject = getPackageLabel(paramPackageDoc.name());
/* 117 */       localContent = getHyperLink(pathString(paramPackageDoc, 
/* 118 */         DocPaths.profilePackageFrame(paramString)), (Content)localObject, "", "packageFrame");
/*     */     }
/*     */     else
/*     */     {
/* 121 */       localObject = new StringContent("<unnamed package>");
/* 122 */       localContent = getHyperLink(DocPaths.PACKAGE_FRAME, (Content)localObject, "", "packageFrame");
/*     */     }
/*     */ 
/* 125 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 126 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarHeader(Content paramContent)
/*     */   {
/*     */     RawHtml localRawHtml;
/* 134 */     if (this.configuration.packagesheader.length() > 0)
/* 135 */       localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.packagesheader));
/*     */     else {
/* 137 */       localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.header));
/*     */     }
/* 139 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.bar, localRawHtml);
/*     */ 
/* 141 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addOverviewHeader(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addProfilesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addAllClassesLink(Content paramContent)
/*     */   {
/* 161 */     Content localContent = getHyperLink(DocPaths.ALLCLASSES_FRAME, this.allclassesLabel, "", "packageFrame");
/*     */ 
/* 163 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 164 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addAllPackagesLink(Content paramContent)
/*     */   {
/* 174 */     Content localContent = getHyperLink(DocPaths.OVERVIEW_FRAME, this.allpackagesLabel, "", "packageListFrame");
/*     */ 
/* 176 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 177 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addAllProfilesLink(Content paramContent)
/*     */   {
/* 187 */     Content localContent = getHyperLink(DocPaths.PROFILE_OVERVIEW_FRAME, this.allprofilesLabel, "", "packageListFrame");
/*     */ 
/* 189 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 190 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarFooter(Content paramContent)
/*     */   {
/* 197 */     HtmlTree localHtmlTree = HtmlTree.P(getSpace());
/* 198 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ProfilePackageIndexFrameWriter
 * JD-Core Version:    0.6.2
 */