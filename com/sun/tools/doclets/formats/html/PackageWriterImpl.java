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
/*     */ import com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class PackageWriterImpl extends HtmlDocletWriter
/*     */   implements PackageSummaryWriter
/*     */ {
/*     */   protected PackageDoc prev;
/*     */   protected PackageDoc next;
/*     */   protected PackageDoc packageDoc;
/*     */ 
/*     */   public PackageWriterImpl(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3)
/*     */     throws IOException
/*     */   {
/*  83 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc1).resolve(DocPaths.PACKAGE_SUMMARY));
/*  84 */     this.prev = paramPackageDoc2;
/*  85 */     this.next = paramPackageDoc3;
/*  86 */     this.packageDoc = paramPackageDoc1;
/*     */   }
/*     */ 
/*     */   public Content getPackageHeader(String paramString)
/*     */   {
/*  93 */     String str = this.packageDoc.name();
/*  94 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str));
/*  95 */     addTop(localHtmlTree1);
/*  96 */     addNavLinks(true, localHtmlTree1);
/*  97 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/*  98 */     localHtmlTree2.addStyle(HtmlStyle.header);
/*  99 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.P);
/* 100 */     addAnnotationInfo(this.packageDoc, localHtmlTree3);
/* 101 */     localHtmlTree2.addContent(localHtmlTree3);
/* 102 */     HtmlTree localHtmlTree4 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, this.packageLabel);
/*     */ 
/* 104 */     localHtmlTree4.addContent(getSpace());
/* 105 */     StringContent localStringContent = new StringContent(paramString);
/* 106 */     localHtmlTree4.addContent(localStringContent);
/* 107 */     localHtmlTree2.addContent(localHtmlTree4);
/* 108 */     addDeprecationInfo(localHtmlTree2);
/* 109 */     if ((this.packageDoc.inlineTags().length > 0) && (!this.configuration.nocomment)) {
/* 110 */       HtmlTree localHtmlTree5 = new HtmlTree(HtmlTag.DIV);
/* 111 */       localHtmlTree5.addStyle(HtmlStyle.docSummary);
/* 112 */       addSummaryComment(this.packageDoc, localHtmlTree5);
/* 113 */       localHtmlTree2.addContent(localHtmlTree5);
/* 114 */       Content localContent1 = getSpace();
/* 115 */       Content localContent2 = getHyperLink(getDocLink(SectionName.PACKAGE_DESCRIPTION), this.descriptionLabel, "", "");
/*     */ 
/* 118 */       HtmlTree localHtmlTree6 = new HtmlTree(HtmlTag.P, new Content[] { this.seeLabel, localContent1, localContent2 });
/* 119 */       localHtmlTree2.addContent(localHtmlTree6);
/*     */     }
/* 121 */     localHtmlTree1.addContent(localHtmlTree2);
/* 122 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getContentHeader()
/*     */   {
/* 129 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 130 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 131 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addDeprecationInfo(Content paramContent)
/*     */   {
/* 140 */     Tag[] arrayOfTag1 = this.packageDoc.tags("deprecated");
/* 141 */     if (Util.isDeprecated(this.packageDoc)) {
/* 142 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.DIV);
/* 143 */       localHtmlTree1.addStyle(HtmlStyle.deprecatedContent);
/* 144 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 145 */       localHtmlTree1.addContent(localHtmlTree2);
/* 146 */       if (arrayOfTag1.length > 0) {
/* 147 */         Tag[] arrayOfTag2 = arrayOfTag1[0].inlineTags();
/* 148 */         if (arrayOfTag2.length > 0) {
/* 149 */           addInlineDeprecatedComment(this.packageDoc, arrayOfTag1[0], localHtmlTree1);
/*     */         }
/*     */       }
/* 152 */       paramContent.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Content getSummaryHeader()
/*     */   {
/* 160 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 161 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 162 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent)
/*     */   {
/* 170 */     if (paramArrayOfClassDoc.length > 0) {
/* 171 */       Arrays.sort(paramArrayOfClassDoc);
/* 172 */       Content localContent1 = getTableCaption(new RawHtml(paramString1));
/* 173 */       HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.typeSummary, 0, 3, 0, paramString2, localContent1);
/*     */ 
/* 175 */       localHtmlTree1.addContent(getSummaryTableHeader(paramArrayOfString, "col"));
/* 176 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 177 */       for (int i = 0; i < paramArrayOfClassDoc.length; i++)
/* 178 */         if ((Util.isCoreClass(paramArrayOfClassDoc[i])) && 
/* 179 */           (this.configuration
/* 179 */           .isGeneratedDoc(paramArrayOfClassDoc[i])))
/*     */         {
/* 182 */           Content localContent2 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE, paramArrayOfClassDoc[i]));
/*     */ 
/* 184 */           HtmlTree localHtmlTree4 = HtmlTree.TD(HtmlStyle.colFirst, localContent2);
/* 185 */           HtmlTree localHtmlTree5 = HtmlTree.TR(localHtmlTree4);
/* 186 */           if (i % 2 == 0)
/* 187 */             localHtmlTree5.addStyle(HtmlStyle.altColor);
/*     */           else
/* 189 */             localHtmlTree5.addStyle(HtmlStyle.rowColor);
/* 190 */           HtmlTree localHtmlTree6 = new HtmlTree(HtmlTag.TD);
/* 191 */           localHtmlTree6.addStyle(HtmlStyle.colLast);
/* 192 */           if (Util.isDeprecated(paramArrayOfClassDoc[i])) {
/* 193 */             localHtmlTree6.addContent(this.deprecatedLabel);
/* 194 */             if (paramArrayOfClassDoc[i].tags("deprecated").length > 0)
/* 195 */               addSummaryDeprecatedComment(paramArrayOfClassDoc[i], paramArrayOfClassDoc[i]
/* 196 */                 .tags("deprecated")[
/* 196 */                 0], localHtmlTree6);
/*     */           }
/*     */           else
/*     */           {
/* 200 */             addSummaryComment(paramArrayOfClassDoc[i], localHtmlTree6);
/* 201 */           }localHtmlTree5.addContent(localHtmlTree6);
/* 202 */           localHtmlTree2.addContent(localHtmlTree5);
/*     */         }
/* 204 */       localHtmlTree1.addContent(localHtmlTree2);
/* 205 */       HtmlTree localHtmlTree3 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 206 */       paramContent.addContent(localHtmlTree3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPackageDescription(Content paramContent)
/*     */   {
/* 214 */     if (this.packageDoc.inlineTags().length > 0) {
/* 215 */       paramContent.addContent(
/* 216 */         getMarkerAnchor(SectionName.PACKAGE_DESCRIPTION));
/*     */ 
/* 218 */       StringContent localStringContent = new StringContent(this.configuration
/* 218 */         .getText("doclet.Package_Description", this.packageDoc
/* 219 */         .name()));
/* 220 */       paramContent.addContent(HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, localStringContent));
/*     */ 
/* 222 */       addInlineComment(this.packageDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPackageTags(Content paramContent)
/*     */   {
/* 230 */     addTagsInfo(this.packageDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addPackageFooter(Content paramContent)
/*     */   {
/* 237 */     addNavLinks(false, paramContent);
/* 238 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 245 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.packageDoc), true, paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClassUse()
/*     */   {
/* 255 */     Content localContent = getHyperLink(DocPaths.PACKAGE_USE, this.useLabel, "", "");
/*     */ 
/* 257 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 258 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkPrevious()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 268 */     if (this.prev == null) {
/* 269 */       localHtmlTree = HtmlTree.LI(this.prevpackageLabel);
/*     */     } else {
/* 271 */       DocPath localDocPath = DocPath.relativePath(this.packageDoc, this.prev);
/* 272 */       localHtmlTree = HtmlTree.LI(getHyperLink(localDocPath.resolve(DocPaths.PACKAGE_SUMMARY), this.prevpackageLabel, "", ""));
/*     */     }
/*     */ 
/* 275 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkNext()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 285 */     if (this.next == null) {
/* 286 */       localHtmlTree = HtmlTree.LI(this.nextpackageLabel);
/*     */     } else {
/* 288 */       DocPath localDocPath = DocPath.relativePath(this.packageDoc, this.next);
/* 289 */       localHtmlTree = HtmlTree.LI(getHyperLink(localDocPath.resolve(DocPaths.PACKAGE_SUMMARY), this.nextpackageLabel, "", ""));
/*     */     }
/*     */ 
/* 292 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 302 */     Content localContent = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */ 
/* 304 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 305 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/* 314 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.packageLabel);
/* 315 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PackageWriterImpl
 * JD-Core Version:    0.6.2
 */