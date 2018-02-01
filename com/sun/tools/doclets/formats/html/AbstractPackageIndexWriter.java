/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class AbstractPackageIndexWriter extends HtmlDocletWriter
/*     */ {
/*     */   protected PackageDoc[] packages;
/*     */ 
/*     */   public AbstractPackageIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  64 */     super(paramConfigurationImpl, paramDocPath);
/*  65 */     this.packages = paramConfigurationImpl.packages;
/*     */   }
/*     */ 
/*     */   protected abstract void addNavigationBarHeader(Content paramContent);
/*     */ 
/*     */   protected abstract void addNavigationBarFooter(Content paramContent);
/*     */ 
/*     */   protected abstract void addOverviewHeader(Content paramContent);
/*     */ 
/*     */   protected abstract void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent);
/*     */ 
/*     */   protected void buildPackageIndexFile(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 109 */     String str = this.configuration.getText(paramString);
/* 110 */     HtmlTree localHtmlTree = getBody(paramBoolean, getWindowTitle(str));
/* 111 */     addNavigationBarHeader(localHtmlTree);
/* 112 */     addOverviewHeader(localHtmlTree);
/* 113 */     addIndex(localHtmlTree);
/* 114 */     addOverview(localHtmlTree);
/* 115 */     addNavigationBarFooter(localHtmlTree);
/* 116 */     printHtmlDocument(this.configuration.metakeywords.getOverviewMetaKeywords(paramString, this.configuration.doctitle), paramBoolean, localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addOverview(Content paramContent)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addIndex(Content paramContent)
/*     */   {
/* 134 */     addIndexContents(this.packages, "doclet.Package_Summary", this.configuration
/* 135 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 136 */       .getText("doclet.Package_Summary"), 
/* 136 */       this.configuration
/* 137 */       .getText("doclet.packages")), paramContent);
/*     */   }
/*     */ 
/*     */   protected void addIndexContents(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent)
/*     */   {
/* 151 */     if (paramArrayOfPackageDoc.length > 0) {
/* 152 */       Arrays.sort(paramArrayOfPackageDoc);
/* 153 */       HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 154 */       localHtmlTree.addStyle(HtmlStyle.indexHeader);
/* 155 */       addAllClassesLink(localHtmlTree);
/* 156 */       if (this.configuration.showProfiles) {
/* 157 */         addAllProfilesLink(localHtmlTree);
/*     */       }
/* 159 */       paramContent.addContent(localHtmlTree);
/* 160 */       if ((this.configuration.showProfiles) && (this.configuration.profilePackages.size() > 0)) {
/* 161 */         Content localContent = this.configuration.getResource("doclet.Profiles");
/* 162 */         addProfilesList(localContent, paramContent);
/*     */       }
/* 164 */       addPackagesList(paramArrayOfPackageDoc, paramString1, paramString2, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addConfigurationTitle(Content paramContent)
/*     */   {
/* 174 */     if (this.configuration.doctitle.length() > 0) {
/* 175 */       RawHtml localRawHtml = new RawHtml(this.configuration.doctitle);
/* 176 */       HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.title, localRawHtml);
/*     */ 
/* 178 */       HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 179 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkContents()
/*     */   {
/* 190 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.overviewLabel);
/* 191 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addAllClassesLink(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addAllProfilesLink(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addProfilesList(Content paramContent1, Content paramContent2)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AbstractPackageIndexWriter
 * JD-Core Version:    0.6.2
 */