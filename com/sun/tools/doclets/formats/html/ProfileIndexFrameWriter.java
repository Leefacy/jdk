/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
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
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class ProfileIndexFrameWriter extends AbstractProfileIndexWriter
/*     */ {
/*     */   public ProfileIndexFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  58 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl)
/*     */   {
/*  68 */     DocPath localDocPath = DocPaths.PROFILE_OVERVIEW_FRAME;
/*     */     try {
/*  70 */       ProfileIndexFrameWriter localProfileIndexFrameWriter = new ProfileIndexFrameWriter(paramConfigurationImpl, localDocPath);
/*  71 */       localProfileIndexFrameWriter.buildProfileIndexFile("doclet.Window_Overview", false);
/*  72 */       localProfileIndexFrameWriter.close();
/*     */     } catch (IOException localIOException) {
/*  74 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  76 */         .toString(), localDocPath });
/*  77 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addProfilesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent)
/*     */   {
/*  86 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.PROFILE_HEADING, true, this.profilesLabel);
/*     */ 
/*  88 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.indexContainer, localHtmlTree1);
/*  89 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.UL);
/*  90 */     localHtmlTree3.setTitle(this.profilesLabel);
/*     */ 
/*  92 */     for (int i = 1; i < paramProfiles.getProfileCount(); i++) {
/*  93 */       String str = Profile.lookup(i).name;
/*     */ 
/*  96 */       if (this.configuration.shouldDocumentProfile(str))
/*  97 */         localHtmlTree3.addContent(getProfile(str));
/*     */     }
/*  99 */     localHtmlTree2.addContent(localHtmlTree3);
/* 100 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected Content getProfile(String paramString)
/*     */   {
/* 112 */     StringContent localStringContent = new StringContent(paramString);
/* 113 */     Content localContent = getHyperLink(DocPaths.profileFrame(paramString), localStringContent, "", "packageListFrame");
/*     */ 
/* 115 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 116 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarHeader(Content paramContent)
/*     */   {
/*     */     RawHtml localRawHtml;
/* 124 */     if (this.configuration.packagesheader.length() > 0)
/* 125 */       localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.packagesheader));
/*     */     else {
/* 127 */       localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.header));
/*     */     }
/* 129 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.bar, localRawHtml);
/*     */ 
/* 131 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addOverviewHeader(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addAllClassesLink(Content paramContent)
/*     */   {
/* 147 */     Content localContent = getHyperLink(DocPaths.ALLCLASSES_FRAME, this.allclassesLabel, "", "packageFrame");
/*     */ 
/* 149 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 150 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addAllPackagesLink(Content paramContent)
/*     */   {
/* 160 */     Content localContent = getHyperLink(DocPaths.OVERVIEW_FRAME, this.allpackagesLabel, "", "packageListFrame");
/*     */ 
/* 162 */     HtmlTree localHtmlTree = HtmlTree.SPAN(localContent);
/* 163 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarFooter(Content paramContent)
/*     */   {
/* 170 */     HtmlTree localHtmlTree = HtmlTree.P(getSpace());
/* 171 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addProfilePackagesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ProfileIndexFrameWriter
 * JD-Core Version:    0.6.2
 */