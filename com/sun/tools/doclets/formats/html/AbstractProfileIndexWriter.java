/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class AbstractProfileIndexWriter extends HtmlDocletWriter
/*     */ {
/*     */   protected Profiles profiles;
/*     */ 
/*     */   public AbstractProfileIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  62 */     super(paramConfigurationImpl, paramDocPath);
/*  63 */     this.profiles = paramConfigurationImpl.profiles;
/*     */   }
/*     */ 
/*     */   protected abstract void addNavigationBarHeader(Content paramContent);
/*     */ 
/*     */   protected abstract void addNavigationBarFooter(Content paramContent);
/*     */ 
/*     */   protected abstract void addOverviewHeader(Content paramContent);
/*     */ 
/*     */   protected abstract void addProfilesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent);
/*     */ 
/*     */   protected abstract void addProfilePackagesList(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3);
/*     */ 
/*     */   protected void buildProfileIndexFile(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 119 */     String str = this.configuration.getText(paramString);
/* 120 */     HtmlTree localHtmlTree = getBody(paramBoolean, getWindowTitle(str));
/* 121 */     addNavigationBarHeader(localHtmlTree);
/* 122 */     addOverviewHeader(localHtmlTree);
/* 123 */     addIndex(localHtmlTree);
/* 124 */     addOverview(localHtmlTree);
/* 125 */     addNavigationBarFooter(localHtmlTree);
/* 126 */     printHtmlDocument(this.configuration.metakeywords.getOverviewMetaKeywords(paramString, this.configuration.doctitle), paramBoolean, localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void buildProfilePackagesIndexFile(String paramString1, boolean paramBoolean, String paramString2)
/*     */     throws IOException
/*     */   {
/* 141 */     String str = this.configuration.getText(paramString1);
/* 142 */     HtmlTree localHtmlTree = getBody(paramBoolean, getWindowTitle(str));
/* 143 */     addNavigationBarHeader(localHtmlTree);
/* 144 */     addOverviewHeader(localHtmlTree);
/* 145 */     addProfilePackagesIndex(localHtmlTree, paramString2);
/* 146 */     addOverview(localHtmlTree);
/* 147 */     addNavigationBarFooter(localHtmlTree);
/* 148 */     printHtmlDocument(this.configuration.metakeywords.getOverviewMetaKeywords(paramString1, this.configuration.doctitle), paramBoolean, localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addOverview(Content paramContent)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addIndex(Content paramContent)
/*     */   {
/* 166 */     addIndexContents(this.profiles, "doclet.Profile_Summary", this.configuration
/* 167 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 168 */       .getText("doclet.Profile_Summary"), 
/* 168 */       this.configuration
/* 169 */       .getText("doclet.profiles")), paramContent);
/*     */   }
/*     */ 
/*     */   protected void addProfilePackagesIndex(Content paramContent, String paramString)
/*     */   {
/* 179 */     addProfilePackagesIndexContents(this.profiles, "doclet.Profile_Summary", this.configuration
/* 180 */       .getText("doclet.Member_Table_Summary", this.configuration
/* 181 */       .getText("doclet.Profile_Summary"), 
/* 181 */       this.configuration
/* 182 */       .getText("doclet.profiles")), paramContent, paramString);
/*     */   }
/*     */ 
/*     */   protected void addIndexContents(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent)
/*     */   {
/* 196 */     if (paramProfiles.getProfileCount() > 0) {
/* 197 */       HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 198 */       localHtmlTree.addStyle(HtmlStyle.indexHeader);
/* 199 */       addAllClassesLink(localHtmlTree);
/* 200 */       addAllPackagesLink(localHtmlTree);
/* 201 */       paramContent.addContent(localHtmlTree);
/* 202 */       addProfilesList(paramProfiles, paramString1, paramString2, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addProfilePackagesIndexContents(Profiles paramProfiles, String paramString1, String paramString2, Content paramContent, String paramString3)
/*     */   {
/* 218 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 219 */     localHtmlTree.addStyle(HtmlStyle.indexHeader);
/* 220 */     addAllClassesLink(localHtmlTree);
/* 221 */     addAllPackagesLink(localHtmlTree);
/* 222 */     addAllProfilesLink(localHtmlTree);
/* 223 */     paramContent.addContent(localHtmlTree);
/* 224 */     addProfilePackagesList(paramProfiles, paramString1, paramString2, paramContent, paramString3);
/*     */   }
/*     */ 
/*     */   protected void addConfigurationTitle(Content paramContent)
/*     */   {
/* 233 */     if (this.configuration.doctitle.length() > 0) {
/* 234 */       RawHtml localRawHtml = new RawHtml(this.configuration.doctitle);
/* 235 */       HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.title, localRawHtml);
/*     */ 
/* 237 */       HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 238 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkContents()
/*     */   {
/* 249 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.overviewLabel);
/* 250 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addAllClassesLink(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addAllPackagesLink(Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addAllProfilesLink(Content paramContent)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AbstractProfileIndexWriter
 * JD-Core Version:    0.6.2
 */