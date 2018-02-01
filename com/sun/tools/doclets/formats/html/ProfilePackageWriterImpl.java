/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfilePackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class ProfilePackageWriterImpl extends HtmlDocletWriter
/*     */   implements ProfilePackageSummaryWriter
/*     */ {
/*     */   protected PackageDoc prev;
/*     */   protected PackageDoc next;
/*     */   protected PackageDoc packageDoc;
/*     */   protected String profileName;
/*     */   protected int profileValue;
/*     */ 
/*     */   public ProfilePackageWriterImpl(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, Profile paramProfile)
/*     */     throws IOException
/*     */   {
/*  94 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc1).resolve(
/*  95 */       DocPaths.profilePackageSummary(paramProfile.name)));
/*     */ 
/*  96 */     this.prev = paramPackageDoc2;
/*  97 */     this.next = paramPackageDoc3;
/*  98 */     this.packageDoc = paramPackageDoc1;
/*  99 */     this.profileName = paramProfile.name;
/* 100 */     this.profileValue = paramProfile.value;
/*     */   }
/*     */ 
/*     */   public Content getPackageHeader(String paramString)
/*     */   {
/* 107 */     String str = this.packageDoc.name();
/* 108 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str));
/* 109 */     addTop(localHtmlTree1);
/* 110 */     addNavLinks(true, localHtmlTree1);
/* 111 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 112 */     localHtmlTree2.addStyle(HtmlStyle.header);
/* 113 */     StringContent localStringContent = new StringContent(this.profileName);
/* 114 */     HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.subTitle, localStringContent);
/* 115 */     localHtmlTree2.addContent(localHtmlTree3);
/* 116 */     HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.P);
/* 117 */     addAnnotationInfo(this.packageDoc, localHtmlTree4);
/* 118 */     localHtmlTree2.addContent(localHtmlTree4);
/* 119 */     HtmlTree localHtmlTree5 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, this.packageLabel);
/*     */ 
/* 121 */     localHtmlTree5.addContent(getSpace());
/* 122 */     RawHtml localRawHtml = new RawHtml(paramString);
/* 123 */     localHtmlTree5.addContent(localRawHtml);
/* 124 */     localHtmlTree2.addContent(localHtmlTree5);
/* 125 */     addDeprecationInfo(localHtmlTree2);
/* 126 */     if ((this.packageDoc.inlineTags().length > 0) && (!this.configuration.nocomment)) {
/* 127 */       HtmlTree localHtmlTree6 = new HtmlTree(HtmlTag.DIV);
/* 128 */       localHtmlTree6.addStyle(HtmlStyle.docSummary);
/* 129 */       addSummaryComment(this.packageDoc, localHtmlTree6);
/* 130 */       localHtmlTree2.addContent(localHtmlTree6);
/* 131 */       Content localContent1 = getSpace();
/* 132 */       Content localContent2 = getHyperLink(getDocLink(SectionName.PACKAGE_DESCRIPTION), this.descriptionLabel, "", "");
/*     */ 
/* 135 */       HtmlTree localHtmlTree7 = new HtmlTree(HtmlTag.P, new Content[] { this.seeLabel, localContent1, localContent2 });
/* 136 */       localHtmlTree2.addContent(localHtmlTree7);
/*     */     }
/* 138 */     localHtmlTree1.addContent(localHtmlTree2);
/* 139 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getContentHeader()
/*     */   {
/* 146 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 147 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 148 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addDeprecationInfo(Content paramContent)
/*     */   {
/* 157 */     Tag[] arrayOfTag1 = this.packageDoc.tags("deprecated");
/* 158 */     if (Util.isDeprecated(this.packageDoc)) {
/* 159 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 160 */       localHtmlTree1.addStyle(HtmlStyle.deprecatedContent);
/* 161 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 162 */       localHtmlTree1.addContent(localHtmlTree2);
/* 163 */       if (arrayOfTag1.length > 0) {
/* 164 */         Tag[] arrayOfTag2 = arrayOfTag1[0].inlineTags();
/* 165 */         if (arrayOfTag2.length > 0) {
/* 166 */           addInlineDeprecatedComment(this.packageDoc, arrayOfTag1[0], localHtmlTree1);
/*     */         }
/*     */       }
/* 169 */       paramContent.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent)
/*     */   {
/* 178 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/* 179 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 180 */     addClassesSummary(paramArrayOfClassDoc, paramString1, paramString2, paramArrayOfString, localHtmlTree, this.profileValue);
/*     */ 
/* 182 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public Content getSummaryHeader()
/*     */   {
/* 189 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 190 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 191 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addPackageDescription(Content paramContent)
/*     */   {
/* 198 */     if (this.packageDoc.inlineTags().length > 0) {
/* 199 */       paramContent.addContent(
/* 200 */         getMarkerAnchor(SectionName.PACKAGE_DESCRIPTION));
/*     */ 
/* 202 */       StringContent localStringContent = new StringContent(this.configuration
/* 202 */         .getText("doclet.Package_Description", this.packageDoc
/* 203 */         .name()));
/* 204 */       paramContent.addContent(HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, localStringContent));
/*     */ 
/* 206 */       addInlineComment(this.packageDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPackageTags(Content paramContent)
/*     */   {
/* 214 */     addTagsInfo(this.packageDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addPackageFooter(Content paramContent)
/*     */   {
/* 221 */     addNavLinks(false, paramContent);
/* 222 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 229 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.packageDoc), true, paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClassUse()
/*     */   {
/* 239 */     Content localContent = getHyperLink(DocPaths.PACKAGE_USE, this.useLabel, "", "");
/*     */ 
/* 241 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 242 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkPrevious()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 252 */     if (this.prev == null) {
/* 253 */       localHtmlTree = HtmlTree.LI(this.prevpackageLabel);
/*     */     } else {
/* 255 */       DocPath localDocPath = DocPath.relativePath(this.packageDoc, this.prev);
/* 256 */       localHtmlTree = HtmlTree.LI(getHyperLink(localDocPath.resolve(DocPaths.profilePackageSummary(this.profileName)), this.prevpackageLabel, "", ""));
/*     */     }
/*     */ 
/* 259 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkNext()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 269 */     if (this.next == null) {
/* 270 */       localHtmlTree = HtmlTree.LI(this.nextpackageLabel);
/*     */     } else {
/* 272 */       DocPath localDocPath = DocPath.relativePath(this.packageDoc, this.next);
/* 273 */       localHtmlTree = HtmlTree.LI(getHyperLink(localDocPath.resolve(DocPaths.profilePackageSummary(this.profileName)), this.nextpackageLabel, "", ""));
/*     */     }
/*     */ 
/* 276 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 286 */     Content localContent = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */ 
/* 288 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 289 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/* 298 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.packageLabel);
/* 299 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ProfilePackageWriterImpl
 * JD-Core Version:    0.6.2
 */