/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Group;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PackageIndexWriter extends AbstractPackageIndexWriter
/*     */ {
/*     */   private RootDoc root;
/*     */   private Map<String, List<PackageDoc>> groupPackageMap;
/*     */   private List<String> groupList;
/*     */ 
/*     */   public PackageIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  79 */     super(paramConfigurationImpl, paramDocPath);
/*  80 */     this.root = paramConfigurationImpl.root;
/*  81 */     this.groupPackageMap = paramConfigurationImpl.group.groupPackages(this.packages);
/*  82 */     this.groupList = paramConfigurationImpl.group.getGroupList();
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl)
/*     */   {
/*  92 */     DocPath localDocPath = DocPaths.OVERVIEW_SUMMARY;
/*     */     try {
/*  94 */       PackageIndexWriter localPackageIndexWriter = new PackageIndexWriter(paramConfigurationImpl, localDocPath);
/*  95 */       localPackageIndexWriter.buildPackageIndexFile("doclet.Window_Overview_Summary", true);
/*  96 */       localPackageIndexWriter.close();
/*     */     } catch (IOException localIOException) {
/*  98 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 100 */         .toString(), localDocPath });
/* 101 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addIndex(Content paramContent)
/*     */   {
/* 112 */     for (int i = 0; i < this.groupList.size(); i++) {
/* 113 */       String str = (String)this.groupList.get(i);
/* 114 */       List localList = (List)this.groupPackageMap.get(str);
/* 115 */       if ((localList != null) && (localList.size() > 0))
/* 116 */         addIndexContents((PackageDoc[])localList.toArray(new PackageDoc[localList.size()]), str, this.configuration
/* 117 */           .getText("doclet.Member_Table_Summary", str, this.configuration
/* 118 */           .getText("doclet.packages")), 
/* 117 */           paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addProfilesList(Content paramContent1, Content paramContent2)
/*     */   {
/* 127 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlTag.H2, paramContent1);
/* 128 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(localHtmlTree1);
/* 129 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.UL);
/*     */ 
/* 131 */     for (int i = 1; i < this.configuration.profiles.getProfileCount(); i++) {
/* 132 */       String str = Profile.lookup(i).name;
/*     */ 
/* 135 */       if (this.configuration.shouldDocumentProfile(str)) {
/* 136 */         Content localContent = getTargetProfileLink("classFrame", new StringContent(str), str);
/*     */ 
/* 138 */         HtmlTree localHtmlTree5 = HtmlTree.LI(localContent);
/* 139 */         localHtmlTree3.addContent(localHtmlTree5);
/*     */       }
/*     */     }
/* 142 */     localHtmlTree2.addContent(localHtmlTree3);
/* 143 */     HtmlTree localHtmlTree4 = HtmlTree.DIV(HtmlStyle.contentContainer, localHtmlTree2);
/* 144 */     paramContent2.addContent(localHtmlTree4);
/*     */   }
/*     */ 
/*     */   protected void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, String paramString1, String paramString2, Content paramContent)
/*     */   {
/* 152 */     HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.overviewSummary, 0, 3, 0, paramString2, 
/* 153 */       getTableCaption(new RawHtml(paramString1)));
/*     */ 
/* 154 */     localHtmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 155 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 156 */     addPackagesList(paramArrayOfPackageDoc, localHtmlTree2);
/* 157 */     localHtmlTree1.addContent(localHtmlTree2);
/* 158 */     HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.contentContainer, localHtmlTree1);
/* 159 */     paramContent.addContent(localHtmlTree3);
/*     */   }
/*     */ 
/*     */   protected void addPackagesList(PackageDoc[] paramArrayOfPackageDoc, Content paramContent)
/*     */   {
/* 169 */     for (int i = 0; i < paramArrayOfPackageDoc.length; i++)
/* 170 */       if ((paramArrayOfPackageDoc[i] != null) && (paramArrayOfPackageDoc[i].name().length() > 0) && (
/* 171 */         (!this.configuration.nodeprecated) || (!Util.isDeprecated(paramArrayOfPackageDoc[i]))))
/*     */       {
/* 173 */         Content localContent = getPackageLink(paramArrayOfPackageDoc[i], 
/* 174 */           getPackageName(paramArrayOfPackageDoc[i]));
/*     */ 
/* 175 */         HtmlTree localHtmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, localContent);
/* 176 */         HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TD);
/* 177 */         localHtmlTree2.addStyle(HtmlStyle.colLast);
/* 178 */         addSummaryComment(paramArrayOfPackageDoc[i], localHtmlTree2);
/* 179 */         HtmlTree localHtmlTree3 = HtmlTree.TR(localHtmlTree1);
/* 180 */         localHtmlTree3.addContent(localHtmlTree2);
/* 181 */         if (i % 2 == 0)
/* 182 */           localHtmlTree3.addStyle(HtmlStyle.altColor);
/*     */         else
/* 184 */           localHtmlTree3.addStyle(HtmlStyle.rowColor);
/* 185 */         paramContent.addContent(localHtmlTree3);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void addOverviewHeader(Content paramContent)
/*     */   {
/* 198 */     if (this.root.inlineTags().length > 0) {
/* 199 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 200 */       localHtmlTree1.addStyle(HtmlStyle.subTitle);
/* 201 */       addSummaryComment(this.root, localHtmlTree1);
/* 202 */       HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 203 */       Content localContent1 = this.seeLabel;
/* 204 */       localContent1.addContent(" ");
/* 205 */       HtmlTree localHtmlTree3 = HtmlTree.P(localContent1);
/* 206 */       Content localContent2 = getHyperLink(getDocLink(SectionName.OVERVIEW_DESCRIPTION), this.descriptionLabel, "", "");
/*     */ 
/* 209 */       localHtmlTree3.addContent(localContent2);
/* 210 */       localHtmlTree2.addContent(localHtmlTree3);
/* 211 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addOverviewComment(Content paramContent)
/*     */   {
/* 223 */     if (this.root.inlineTags().length > 0) {
/* 224 */       paramContent.addContent(
/* 225 */         getMarkerAnchor(SectionName.OVERVIEW_DESCRIPTION));
/*     */ 
/* 226 */       addInlineComment(this.root, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addOverview(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 237 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 238 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 239 */     addOverviewComment(localHtmlTree);
/* 240 */     addTagsInfo(this.root, localHtmlTree);
/* 241 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarHeader(Content paramContent)
/*     */   {
/* 252 */     addTop(paramContent);
/* 253 */     addNavLinks(true, paramContent);
/* 254 */     addConfigurationTitle(paramContent);
/*     */   }
/*     */ 
/*     */   protected void addNavigationBarFooter(Content paramContent)
/*     */   {
/* 264 */     addNavLinks(false, paramContent);
/* 265 */     addBottom(paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PackageIndexWriter
 * JD-Core Version:    0.6.2
 */