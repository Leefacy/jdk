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
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfileSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class ProfileWriterImpl extends HtmlDocletWriter
/*     */   implements ProfileSummaryWriter
/*     */ {
/*     */   protected Profile prevProfile;
/*     */   protected Profile nextProfile;
/*     */   protected Profile profile;
/*     */ 
/*     */   public ProfileWriterImpl(ConfigurationImpl paramConfigurationImpl, Profile paramProfile1, Profile paramProfile2, Profile paramProfile3)
/*     */     throws IOException
/*     */   {
/*  80 */     super(paramConfigurationImpl, DocPaths.profileSummary(paramProfile1.name));
/*  81 */     this.prevProfile = paramProfile2;
/*  82 */     this.nextProfile = paramProfile3;
/*  83 */     this.profile = paramProfile1;
/*     */   }
/*     */ 
/*     */   public Content getProfileHeader(String paramString)
/*     */   {
/*  90 */     String str = this.profile.name;
/*  91 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str));
/*  92 */     addTop(localHtmlTree1);
/*  93 */     addNavLinks(true, localHtmlTree1);
/*  94 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/*  95 */     localHtmlTree2.addStyle(HtmlStyle.header);
/*  96 */     HtmlTree localHtmlTree3 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, this.profileLabel);
/*     */ 
/*  98 */     localHtmlTree3.addContent(getSpace());
/*  99 */     RawHtml localRawHtml = new RawHtml(paramString);
/* 100 */     localHtmlTree3.addContent(localRawHtml);
/* 101 */     localHtmlTree2.addContent(localHtmlTree3);
/* 102 */     localHtmlTree1.addContent(localHtmlTree2);
/* 103 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getContentHeader()
/*     */   {
/* 110 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 111 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 112 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getSummaryHeader()
/*     */   {
/* 119 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/* 120 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 121 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getSummaryTree(Content paramContent)
/*     */   {
/* 128 */     HtmlTree localHtmlTree1 = HtmlTree.UL(HtmlStyle.blockList, paramContent);
/* 129 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.summary, localHtmlTree1);
/* 130 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   public Content getPackageSummaryHeader(PackageDoc paramPackageDoc)
/*     */   {
/* 137 */     Content localContent = getTargetProfilePackageLink(paramPackageDoc, "classFrame", new StringContent(paramPackageDoc
/* 138 */       .name()), this.profile.name);
/* 139 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlTag.H3, localContent);
/* 140 */     HtmlTree localHtmlTree2 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 141 */     addPackageDeprecationInfo(localHtmlTree2, paramPackageDoc);
/* 142 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   public Content getPackageSummaryTree(Content paramContent)
/*     */   {
/* 149 */     HtmlTree localHtmlTree = HtmlTree.UL(HtmlStyle.blockList, paramContent);
/* 150 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent)
/*     */   {
/* 158 */     addClassesSummary(paramArrayOfClassDoc, paramString1, paramString2, paramArrayOfString, paramContent, this.profile.value);
/*     */   }
/*     */ 
/*     */   public void addProfileFooter(Content paramContent)
/*     */   {
/* 166 */     addNavLinks(false, paramContent);
/* 167 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 174 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.profile), true, paramContent);
/*     */   }
/*     */ 
/*     */   public void addPackageDeprecationInfo(Content paramContent, PackageDoc paramPackageDoc)
/*     */   {
/* 186 */     if (Util.isDeprecated(paramPackageDoc)) {
/* 187 */       Tag[] arrayOfTag1 = paramPackageDoc.tags("deprecated");
/* 188 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 189 */       localHtmlTree1.addStyle(HtmlStyle.deprecatedContent);
/* 190 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 191 */       localHtmlTree1.addContent(localHtmlTree2);
/* 192 */       if (arrayOfTag1.length > 0) {
/* 193 */         Tag[] arrayOfTag2 = arrayOfTag1[0].inlineTags();
/* 194 */         if (arrayOfTag2.length > 0) {
/* 195 */           addInlineDeprecatedComment(paramPackageDoc, arrayOfTag1[0], localHtmlTree1);
/*     */         }
/*     */       }
/* 198 */       paramContent.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Content getNavLinkPrevious()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 209 */     if (this.prevProfile == null)
/* 210 */       localHtmlTree = HtmlTree.LI(this.prevprofileLabel);
/*     */     else {
/* 212 */       localHtmlTree = HtmlTree.LI(getHyperLink(this.pathToRoot.resolve(DocPaths.profileSummary(this.prevProfile.name)), this.prevprofileLabel, "", ""));
/*     */     }
/*     */ 
/* 215 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkNext()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 225 */     if (this.nextProfile == null)
/* 226 */       localHtmlTree = HtmlTree.LI(this.nextprofileLabel);
/*     */     else {
/* 228 */       localHtmlTree = HtmlTree.LI(getHyperLink(this.pathToRoot.resolve(DocPaths.profileSummary(this.nextProfile.name)), this.nextprofileLabel, "", ""));
/*     */     }
/*     */ 
/* 231 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ProfileWriterImpl
 * JD-Core Version:    0.6.2
 */