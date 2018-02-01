/*      */ package com.sun.tools.doclets.formats.html;
/*      */ 
/*      */ import com.sun.javadoc.AnnotationDesc;
/*      */ import com.sun.javadoc.AnnotationDesc.ElementValuePair;
/*      */ import com.sun.javadoc.AnnotationTypeDoc;
/*      */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*      */ import com.sun.javadoc.AnnotationValue;
/*      */ import com.sun.javadoc.ClassDoc;
/*      */ import com.sun.javadoc.Doc;
/*      */ import com.sun.javadoc.ExecutableMemberDoc;
/*      */ import com.sun.javadoc.FieldDoc;
/*      */ import com.sun.javadoc.MemberDoc;
/*      */ import com.sun.javadoc.MethodDoc;
/*      */ import com.sun.javadoc.PackageDoc;
/*      */ import com.sun.javadoc.Parameter;
/*      */ import com.sun.javadoc.ProgramElementDoc;
/*      */ import com.sun.javadoc.RootDoc;
/*      */ import com.sun.javadoc.SeeTag;
/*      */ import com.sun.javadoc.Tag;
/*      */ import com.sun.javadoc.Type;
/*      */ import com.sun.tools.doclets.formats.html.markup.Comment;
/*      */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*      */ import com.sun.tools.doclets.formats.html.markup.DocType;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlAttr;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlDocWriter;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlDocument;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlTag.BlockType;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*      */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*      */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*      */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*      */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*      */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*      */ import com.sun.tools.doclets.internal.toolkit.Content;
/*      */ import com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter;
/*      */ import com.sun.tools.doclets.internal.toolkit.taglets.DocRootTaglet;
/*      */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*      */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.Extern;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.ImplementedMethods;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*      */ import com.sun.tools.javac.sym.Profiles;
/*      */ import com.sun.tools.javac.util.StringUtils;
/*      */ import java.io.IOException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ public class HtmlDocletWriter extends HtmlDocWriter
/*      */ {
/*      */   public final DocPath pathToRoot;
/*      */   public final DocPath path;
/*      */   public final DocPath filename;
/*      */   public final ConfigurationImpl configuration;
/*   88 */   protected boolean printedAnnotationHeading = false;
/*      */ 
/*   93 */   protected boolean printedAnnotationFieldHeading = false;
/*      */ 
/*   98 */   private boolean isAnnotationDocumented = false;
/*      */ 
/*  103 */   private boolean isContainerDocumented = false;
/*      */ 
/*  174 */   private static final Pattern docrootPattern = Pattern.compile(Pattern.quote("{@docroot}"), 
/*  174 */     2);
/*      */ 
/* 1739 */   static final Set<String> blockTags = new HashSet();
/*      */ 
/*      */   public HtmlDocletWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*      */     throws IOException
/*      */   {
/*  112 */     super(paramConfigurationImpl, paramDocPath);
/*  113 */     this.configuration = paramConfigurationImpl;
/*  114 */     this.path = paramDocPath;
/*  115 */     this.pathToRoot = paramDocPath.parent().invert();
/*  116 */     this.filename = paramDocPath.basename();
/*      */   }
/*      */ 
/*      */   public String replaceDocRootDir(String paramString)
/*      */   {
/*  140 */     int i = paramString.indexOf("{@");
/*  141 */     if (i < 0) {
/*  142 */       return paramString;
/*      */     }
/*  144 */     Matcher localMatcher = docrootPattern.matcher(paramString);
/*  145 */     if (!localMatcher.find()) {
/*  146 */       return paramString;
/*      */     }
/*  148 */     StringBuilder localStringBuilder = new StringBuilder();
/*  149 */     int j = 0;
/*      */     do {
/*  151 */       int k = localMatcher.start();
/*      */ 
/*  153 */       localStringBuilder.append(paramString.substring(j, k));
/*  154 */       j = localMatcher.end();
/*  155 */       if ((this.configuration.docrootparent.length() > 0) && (paramString.startsWith("/..", j)))
/*      */       {
/*  157 */         localStringBuilder.append(this.configuration.docrootparent);
/*  158 */         j += 3;
/*      */       }
/*      */       else {
/*  161 */         localStringBuilder.append(this.pathToRoot.isEmpty() ? "." : this.pathToRoot.getPath());
/*      */       }
/*      */ 
/*  164 */       if ((j < paramString.length()) && (paramString.charAt(j) != '/'))
/*  165 */         localStringBuilder.append('/');
/*      */     }
/*  167 */     while (localMatcher.find());
/*  168 */     localStringBuilder.append(paramString.substring(j));
/*  169 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public Content getAllClassesLinkScript(String paramString)
/*      */   {
/*  183 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.SCRIPT);
/*  184 */     localHtmlTree1.addAttr(HtmlAttr.TYPE, "text/javascript");
/*  185 */     String str = "<!--" + DocletConstants.NL + "  allClassesLink = document.getElementById(\"" + paramString + "\");" + DocletConstants.NL + "  if(window==top) {" + DocletConstants.NL + "    allClassesLink.style.display = \"block\";" + DocletConstants.NL + "  }" + DocletConstants.NL + "  else {" + DocletConstants.NL + "    allClassesLink.style.display = \"none\";" + DocletConstants.NL + "  }" + DocletConstants.NL + "  //-->" + DocletConstants.NL;
/*      */ 
/*  194 */     RawHtml localRawHtml = new RawHtml(str);
/*  195 */     localHtmlTree1.addContent(localRawHtml);
/*  196 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(localHtmlTree1);
/*  197 */     return localHtmlTree2;
/*      */   }
/*      */ 
/*      */   private void addMethodInfo(MethodDoc paramMethodDoc, Content paramContent)
/*      */   {
/*  207 */     ClassDoc[] arrayOfClassDoc = paramMethodDoc.containingClass().interfaces();
/*  208 */     MethodDoc localMethodDoc = paramMethodDoc.overriddenMethod();
/*      */ 
/*  212 */     if (((arrayOfClassDoc.length > 0) && 
/*  213 */       (new ImplementedMethods(paramMethodDoc, this.configuration)
/*  213 */       .build().length > 0)) || (localMethodDoc != null))
/*      */     {
/*  215 */       MethodWriterImpl.addImplementsInfo(this, paramMethodDoc, paramContent);
/*  216 */       if (localMethodDoc != null)
/*  217 */         MethodWriterImpl.addOverridden(this, paramMethodDoc
/*  218 */           .overriddenType(), localMethodDoc, paramContent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addTagsInfo(Doc paramDoc, Content paramContent)
/*      */   {
/*  230 */     if (this.configuration.nocomment) {
/*  231 */       return;
/*      */     }
/*  233 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DL);
/*  234 */     if ((paramDoc instanceof MethodDoc)) {
/*  235 */       addMethodInfo((MethodDoc)paramDoc, localHtmlTree);
/*      */     }
/*  237 */     ContentBuilder localContentBuilder = new ContentBuilder();
/*  238 */     TagletWriter.genTagOuput(this.configuration.tagletManager, paramDoc, this.configuration.tagletManager
/*  239 */       .getCustomTaglets(paramDoc), 
/*  240 */       getTagletWriterInstance(false), 
/*  240 */       localContentBuilder);
/*  241 */     localHtmlTree.addContent(localContentBuilder);
/*  242 */     paramContent.addContent(localHtmlTree);
/*      */   }
/*      */ 
/*      */   protected boolean hasSerializationOverviewTags(FieldDoc paramFieldDoc)
/*      */   {
/*  253 */     ContentBuilder localContentBuilder = new ContentBuilder();
/*  254 */     TagletWriter.genTagOuput(this.configuration.tagletManager, paramFieldDoc, this.configuration.tagletManager
/*  255 */       .getCustomTaglets(paramFieldDoc), 
/*  256 */       getTagletWriterInstance(false), 
/*  256 */       localContentBuilder);
/*  257 */     return !localContentBuilder.isEmpty();
/*      */   }
/*      */ 
/*      */   public TagletWriter getTagletWriterInstance(boolean paramBoolean)
/*      */   {
/*  266 */     return new TagletWriterImpl(this, paramBoolean);
/*      */   }
/*      */ 
/*      */   public Content getTargetPackageLink(PackageDoc paramPackageDoc, String paramString, Content paramContent)
/*      */   {
/*  279 */     return getHyperLink(pathString(paramPackageDoc, DocPaths.PACKAGE_SUMMARY), paramContent, "", paramString);
/*      */   }
/*      */ 
/*      */   public Content getTargetProfilePackageLink(PackageDoc paramPackageDoc, String paramString1, Content paramContent, String paramString2)
/*      */   {
/*  293 */     return getHyperLink(pathString(paramPackageDoc, DocPaths.profilePackageSummary(paramString2)), paramContent, "", paramString1);
/*      */   }
/*      */ 
/*      */   public Content getTargetProfileLink(String paramString1, Content paramContent, String paramString2)
/*      */   {
/*  307 */     return getHyperLink(this.pathToRoot.resolve(
/*  308 */       DocPaths.profileSummary(paramString2)), paramContent, "", paramString1);
/*      */   }
/*      */ 
/*      */   public String getTypeNameForProfile(ClassDoc paramClassDoc)
/*      */   {
/*  319 */     StringBuilder localStringBuilder = new StringBuilder(paramClassDoc
/*  319 */       .containingPackage().name().replace(".", "/"));
/*  320 */     localStringBuilder.append("/")
/*  321 */       .append(paramClassDoc
/*  321 */       .name().replace(".", "$"));
/*  322 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public boolean isTypeInProfile(ClassDoc paramClassDoc, int paramInt)
/*      */   {
/*  333 */     return this.configuration.profiles.getProfile(getTypeNameForProfile(paramClassDoc)) <= paramInt;
/*      */   }
/*      */ 
/*      */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent, int paramInt)
/*      */   {
/*  339 */     if (paramArrayOfClassDoc.length > 0) {
/*  340 */       Arrays.sort(paramArrayOfClassDoc);
/*  341 */       Content localContent1 = getTableCaption(new RawHtml(paramString1));
/*  342 */       HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.typeSummary, 0, 3, 0, paramString2, localContent1);
/*      */ 
/*  344 */       localHtmlTree1.addContent(getSummaryTableHeader(paramArrayOfString, "col"));
/*  345 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/*  346 */       for (int i = 0; i < paramArrayOfClassDoc.length; i++)
/*  347 */         if (isTypeInProfile(paramArrayOfClassDoc[i], paramInt))
/*      */         {
/*  350 */           if ((Util.isCoreClass(paramArrayOfClassDoc[i])) && 
/*  351 */             (this.configuration
/*  351 */             .isGeneratedDoc(paramArrayOfClassDoc[i])))
/*      */           {
/*  354 */             Content localContent2 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE, paramArrayOfClassDoc[i]));
/*      */ 
/*  356 */             HtmlTree localHtmlTree3 = HtmlTree.TD(HtmlStyle.colFirst, localContent2);
/*  357 */             HtmlTree localHtmlTree4 = HtmlTree.TR(localHtmlTree3);
/*  358 */             if (i % 2 == 0)
/*  359 */               localHtmlTree4.addStyle(HtmlStyle.altColor);
/*      */             else
/*  361 */               localHtmlTree4.addStyle(HtmlStyle.rowColor);
/*  362 */             HtmlTree localHtmlTree5 = new HtmlTree(HtmlTag.TD);
/*  363 */             localHtmlTree5.addStyle(HtmlStyle.colLast);
/*  364 */             if (Util.isDeprecated(paramArrayOfClassDoc[i])) {
/*  365 */               localHtmlTree5.addContent(this.deprecatedLabel);
/*  366 */               if (paramArrayOfClassDoc[i].tags("deprecated").length > 0)
/*  367 */                 addSummaryDeprecatedComment(paramArrayOfClassDoc[i], paramArrayOfClassDoc[i]
/*  368 */                   .tags("deprecated")[
/*  368 */                   0], localHtmlTree5);
/*      */             }
/*      */             else
/*      */             {
/*  372 */               addSummaryComment(paramArrayOfClassDoc[i], localHtmlTree5);
/*  373 */             }localHtmlTree4.addContent(localHtmlTree5);
/*  374 */             localHtmlTree2.addContent(localHtmlTree4);
/*      */           }
/*      */         }
/*  376 */       localHtmlTree1.addContent(localHtmlTree2);
/*  377 */       paramContent.addContent(localHtmlTree1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printHtmlDocument(String[] paramArrayOfString, boolean paramBoolean, Content paramContent)
/*      */     throws IOException
/*      */   {
/*  393 */     DocType localDocType = DocType.TRANSITIONAL;
/*  394 */     Comment localComment = new Comment(this.configuration.getText("doclet.New_Page"));
/*  395 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.HEAD);
/*  396 */     localHtmlTree1.addContent(getGeneratedBy(!this.configuration.notimestamp));
/*      */     Object localObject1;
/*  397 */     if (this.configuration.charset.length() > 0) {
/*  398 */       localObject1 = HtmlTree.META("Content-Type", "text/html", this.configuration.charset);
/*      */ 
/*  400 */       localHtmlTree1.addContent((Content)localObject1);
/*      */     }
/*  402 */     localHtmlTree1.addContent(getTitle());
/*  403 */     if (!this.configuration.notimestamp) {
/*  404 */       localObject1 = new SimpleDateFormat("yyyy-MM-dd");
/*  405 */       localObject2 = HtmlTree.META("date", ((SimpleDateFormat)localObject1).format(new Date()));
/*  406 */       localHtmlTree1.addContent((Content)localObject2);
/*      */     }
/*  408 */     if (paramArrayOfString != null) {
/*  409 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/*  410 */         localObject2 = HtmlTree.META("keywords", paramArrayOfString[i]);
/*  411 */         localHtmlTree1.addContent((Content)localObject2);
/*      */       }
/*      */     }
/*  414 */     localHtmlTree1.addContent(getStyleSheetProperties());
/*  415 */     localHtmlTree1.addContent(getScriptProperties());
/*  416 */     HtmlTree localHtmlTree2 = HtmlTree.HTML(this.configuration.getLocale().getLanguage(), localHtmlTree1, paramContent);
/*      */ 
/*  418 */     Object localObject2 = new HtmlDocument(localDocType, localComment, localHtmlTree2);
/*      */ 
/*  420 */     write((Content)localObject2);
/*      */   }
/*      */ 
/*      */   public String getWindowTitle(String paramString)
/*      */   {
/*  430 */     if (this.configuration.windowtitle.length() > 0) {
/*  431 */       paramString = paramString + " (" + this.configuration.windowtitle + ")";
/*      */     }
/*  433 */     return paramString;
/*      */   }
/*      */ 
/*      */   public Content getUserHeaderFooter(boolean paramBoolean)
/*      */   {
/*      */     String str;
/*  444 */     if (paramBoolean) {
/*  445 */       str = replaceDocRootDir(this.configuration.header);
/*      */     }
/*  447 */     else if (this.configuration.footer.length() != 0)
/*  448 */       str = replaceDocRootDir(this.configuration.footer);
/*      */     else {
/*  450 */       str = replaceDocRootDir(this.configuration.header);
/*      */     }
/*      */ 
/*  453 */     RawHtml localRawHtml = new RawHtml(str);
/*  454 */     return localRawHtml;
/*      */   }
/*      */ 
/*      */   public void addTop(Content paramContent)
/*      */   {
/*  463 */     RawHtml localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.top));
/*  464 */     paramContent.addContent(localRawHtml);
/*      */   }
/*      */ 
/*      */   public void addBottom(Content paramContent)
/*      */   {
/*  473 */     RawHtml localRawHtml = new RawHtml(replaceDocRootDir(this.configuration.bottom));
/*  474 */     HtmlTree localHtmlTree1 = HtmlTree.SMALL(localRawHtml);
/*  475 */     HtmlTree localHtmlTree2 = HtmlTree.P(HtmlStyle.legalCopy, localHtmlTree1);
/*  476 */     paramContent.addContent(localHtmlTree2);
/*      */   }
/*      */ 
/*      */   protected void addNavLinks(boolean paramBoolean, Content paramContent)
/*      */   {
/*  486 */     if (!this.configuration.nonavbar) {
/*  487 */       String str = "allclasses_";
/*  488 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.DIV);
/*  489 */       Content localContent = this.configuration.getResource("doclet.Skip_navigation_links");
/*  490 */       if (paramBoolean) {
/*  491 */         paramContent.addContent(HtmlConstants.START_OF_TOP_NAVBAR);
/*  492 */         localHtmlTree1.addStyle(HtmlStyle.topNav);
/*  493 */         str = str + "navbar_top";
/*  494 */         localObject = getMarkerAnchor(SectionName.NAVBAR_TOP);
/*      */ 
/*  496 */         localHtmlTree1.addContent((Content)localObject);
/*  497 */         localHtmlTree2 = HtmlTree.DIV(HtmlStyle.skipNav, getHyperLink(
/*  498 */           getDocLink(SectionName.SKIP_NAVBAR_TOP), 
/*  498 */           localContent, localContent
/*  499 */           .toString(), ""));
/*  500 */         localHtmlTree1.addContent(localHtmlTree2);
/*      */       } else {
/*  502 */         paramContent.addContent(HtmlConstants.START_OF_BOTTOM_NAVBAR);
/*  503 */         localHtmlTree1.addStyle(HtmlStyle.bottomNav);
/*  504 */         str = str + "navbar_bottom";
/*  505 */         localObject = getMarkerAnchor(SectionName.NAVBAR_BOTTOM);
/*  506 */         localHtmlTree1.addContent((Content)localObject);
/*  507 */         localHtmlTree2 = HtmlTree.DIV(HtmlStyle.skipNav, getHyperLink(
/*  508 */           getDocLink(SectionName.SKIP_NAVBAR_BOTTOM), 
/*  508 */           localContent, localContent
/*  509 */           .toString(), ""));
/*  510 */         localHtmlTree1.addContent(localHtmlTree2);
/*      */       }
/*  512 */       if (paramBoolean)
/*  513 */         localHtmlTree1.addContent(getMarkerAnchor(SectionName.NAVBAR_TOP_FIRSTROW));
/*      */       else {
/*  515 */         localHtmlTree1.addContent(getMarkerAnchor(SectionName.NAVBAR_BOTTOM_FIRSTROW));
/*      */       }
/*  517 */       Object localObject = new HtmlTree(HtmlTag.UL);
/*  518 */       ((HtmlTree)localObject).addStyle(HtmlStyle.navList);
/*  519 */       ((HtmlTree)localObject).addAttr(HtmlAttr.TITLE, this.configuration
/*  520 */         .getText("doclet.Navigation"));
/*      */ 
/*  521 */       if (this.configuration.createoverview) {
/*  522 */         ((HtmlTree)localObject).addContent(getNavLinkContents());
/*      */       }
/*  524 */       if (this.configuration.packages.length == 1)
/*  525 */         ((HtmlTree)localObject).addContent(getNavLinkPackage(this.configuration.packages[0]));
/*  526 */       else if (this.configuration.packages.length > 1) {
/*  527 */         ((HtmlTree)localObject).addContent(getNavLinkPackage());
/*      */       }
/*  529 */       ((HtmlTree)localObject).addContent(getNavLinkClass());
/*  530 */       if (this.configuration.classuse) {
/*  531 */         ((HtmlTree)localObject).addContent(getNavLinkClassUse());
/*      */       }
/*  533 */       if (this.configuration.createtree) {
/*  534 */         ((HtmlTree)localObject).addContent(getNavLinkTree());
/*      */       }
/*  536 */       if ((!this.configuration.nodeprecated) && (!this.configuration.nodeprecatedlist))
/*      */       {
/*  538 */         ((HtmlTree)localObject).addContent(getNavLinkDeprecated());
/*      */       }
/*  540 */       if (this.configuration.createindex) {
/*  541 */         ((HtmlTree)localObject).addContent(getNavLinkIndex());
/*      */       }
/*  543 */       if (!this.configuration.nohelp) {
/*  544 */         ((HtmlTree)localObject).addContent(getNavLinkHelp());
/*      */       }
/*  546 */       localHtmlTree1.addContent((Content)localObject);
/*  547 */       HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.aboutLanguage, getUserHeaderFooter(paramBoolean));
/*  548 */       localHtmlTree1.addContent(localHtmlTree2);
/*  549 */       paramContent.addContent(localHtmlTree1);
/*  550 */       HtmlTree localHtmlTree3 = HtmlTree.UL(HtmlStyle.navList, getNavLinkPrevious());
/*  551 */       localHtmlTree3.addContent(getNavLinkNext());
/*  552 */       HtmlTree localHtmlTree4 = HtmlTree.DIV(HtmlStyle.subNav, localHtmlTree3);
/*  553 */       HtmlTree localHtmlTree5 = HtmlTree.UL(HtmlStyle.navList, getNavShowLists());
/*  554 */       localHtmlTree5.addContent(getNavHideLists(this.filename));
/*  555 */       localHtmlTree4.addContent(localHtmlTree5);
/*  556 */       HtmlTree localHtmlTree6 = HtmlTree.UL(HtmlStyle.navList, getNavLinkClassIndex());
/*  557 */       localHtmlTree6.addAttr(HtmlAttr.ID, str.toString());
/*  558 */       localHtmlTree4.addContent(localHtmlTree6);
/*  559 */       localHtmlTree4.addContent(getAllClassesLinkScript(str.toString()));
/*  560 */       addSummaryDetailLinks(localHtmlTree4);
/*  561 */       if (paramBoolean) {
/*  562 */         localHtmlTree4.addContent(getMarkerAnchor(SectionName.SKIP_NAVBAR_TOP));
/*  563 */         paramContent.addContent(localHtmlTree4);
/*  564 */         paramContent.addContent(HtmlConstants.END_OF_TOP_NAVBAR);
/*      */       } else {
/*  566 */         localHtmlTree4.addContent(getMarkerAnchor(SectionName.SKIP_NAVBAR_BOTTOM));
/*  567 */         paramContent.addContent(localHtmlTree4);
/*  568 */         paramContent.addContent(HtmlConstants.END_OF_BOTTOM_NAVBAR);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkNext()
/*      */   {
/*  580 */     return getNavLinkNext(null);
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkPrevious()
/*      */   {
/*  590 */     return getNavLinkPrevious(null);
/*      */   }
/*      */ 
/*      */   protected void addSummaryDetailLinks(Content paramContent)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkContents()
/*      */   {
/*  605 */     Content localContent = getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_SUMMARY), this.overviewLabel, "", "");
/*      */ 
/*  607 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  608 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkPackage(PackageDoc paramPackageDoc)
/*      */   {
/*  618 */     Content localContent = getPackageLink(paramPackageDoc, this.packageLabel);
/*      */ 
/*  620 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  621 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkPackage()
/*      */   {
/*  630 */     HtmlTree localHtmlTree = HtmlTree.LI(this.packageLabel);
/*  631 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkClassUse()
/*      */   {
/*  640 */     HtmlTree localHtmlTree = HtmlTree.LI(this.useLabel);
/*  641 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   public Content getNavLinkPrevious(DocPath paramDocPath)
/*      */   {
/*      */     HtmlTree localHtmlTree;
/*  652 */     if (paramDocPath != null) {
/*  653 */       localHtmlTree = HtmlTree.LI(getHyperLink(paramDocPath, this.prevLabel, "", ""));
/*      */     }
/*      */     else
/*  656 */       localHtmlTree = HtmlTree.LI(this.prevLabel);
/*  657 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   public Content getNavLinkNext(DocPath paramDocPath)
/*      */   {
/*      */     HtmlTree localHtmlTree;
/*  669 */     if (paramDocPath != null) {
/*  670 */       localHtmlTree = HtmlTree.LI(getHyperLink(paramDocPath, this.nextLabel, "", ""));
/*      */     }
/*      */     else
/*  673 */       localHtmlTree = HtmlTree.LI(this.nextLabel);
/*  674 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavShowLists(DocPath paramDocPath)
/*      */   {
/*  684 */     DocLink localDocLink = new DocLink(paramDocPath, this.path.getPath(), null);
/*  685 */     Content localContent = getHyperLink(localDocLink, this.framesLabel, "", "_top");
/*  686 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  687 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavShowLists()
/*      */   {
/*  696 */     return getNavShowLists(this.pathToRoot.resolve(DocPaths.INDEX));
/*      */   }
/*      */ 
/*      */   protected Content getNavHideLists(DocPath paramDocPath)
/*      */   {
/*  706 */     Content localContent = getHyperLink(paramDocPath, this.noframesLabel, "", "_top");
/*  707 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  708 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkTree()
/*      */   {
/*  721 */     PackageDoc[] arrayOfPackageDoc = this.configuration.root.specifiedPackages();
/*      */     Content localContent;
/*  722 */     if ((arrayOfPackageDoc.length == 1) && (this.configuration.root.specifiedClasses().length == 0)) {
/*  723 */       localContent = getHyperLink(pathString(arrayOfPackageDoc[0], DocPaths.PACKAGE_TREE), this.treeLabel, "", "");
/*      */     }
/*      */     else
/*      */     {
/*  727 */       localContent = getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_TREE), this.treeLabel, "", "");
/*      */     }
/*      */ 
/*  730 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  731 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkMainTree(String paramString)
/*      */   {
/*  741 */     Content localContent = getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_TREE), new StringContent(paramString));
/*      */ 
/*  743 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  744 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkClass()
/*      */   {
/*  753 */     HtmlTree localHtmlTree = HtmlTree.LI(this.classLabel);
/*  754 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkDeprecated()
/*      */   {
/*  763 */     Content localContent = getHyperLink(this.pathToRoot.resolve(DocPaths.DEPRECATED_LIST), this.deprecatedLabel, "", "");
/*      */ 
/*  765 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  766 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkClassIndex()
/*      */   {
/*  777 */     Content localContent = getHyperLink(this.pathToRoot.resolve(DocPaths.ALLCLASSES_NOFRAME), this.allclassesLabel, "", "");
/*      */ 
/*  780 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  781 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkIndex()
/*      */   {
/*  790 */     Content localContent = getHyperLink(this.pathToRoot.resolve(this.configuration.splitindex ? DocPaths.INDEX_FILES
/*  792 */       .resolve(DocPaths.indexN(1)) : 
/*  792 */       DocPaths.INDEX_ALL), this.indexLabel, "", "");
/*      */ 
/*  795 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  796 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   protected Content getNavLinkHelp()
/*      */   {
/*  807 */     String str = this.configuration.helpfile;
/*      */     DocPath localDocPath;
/*  809 */     if (str.isEmpty()) {
/*  810 */       localDocPath = DocPaths.HELP_DOC;
/*      */     } else {
/*  812 */       localObject = DocFile.createFileForInput(this.configuration, str);
/*  813 */       localDocPath = DocPath.create(((DocFile)localObject).getName());
/*      */     }
/*  815 */     Object localObject = getHyperLink(this.pathToRoot.resolve(localDocPath), this.helpLabel, "", "");
/*      */ 
/*  817 */     HtmlTree localHtmlTree = HtmlTree.LI((Content)localObject);
/*  818 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   public Content getSummaryTableHeader(String[] paramArrayOfString, String paramString)
/*      */   {
/*  829 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.TR);
/*  830 */     int i = paramArrayOfString.length;
/*      */     StringContent localStringContent;
/*  832 */     if (i == 1) {
/*  833 */       localStringContent = new StringContent(paramArrayOfString[0]);
/*  834 */       localHtmlTree.addContent(HtmlTree.TH(HtmlStyle.colOne, paramString, localStringContent));
/*  835 */       return localHtmlTree;
/*      */     }
/*  837 */     for (int j = 0; j < i; j++) {
/*  838 */       localStringContent = new StringContent(paramArrayOfString[j]);
/*  839 */       if (j == 0)
/*  840 */         localHtmlTree.addContent(HtmlTree.TH(HtmlStyle.colFirst, paramString, localStringContent));
/*  841 */       else if (j == i - 1)
/*  842 */         localHtmlTree.addContent(HtmlTree.TH(HtmlStyle.colLast, paramString, localStringContent));
/*      */       else
/*  844 */         localHtmlTree.addContent(HtmlTree.TH(paramString, localStringContent));
/*      */     }
/*  846 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   public Content getTableCaption(Content paramContent)
/*      */   {
/*  856 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(paramContent);
/*  857 */     Content localContent = getSpace();
/*  858 */     HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.tabEnd, localContent);
/*  859 */     HtmlTree localHtmlTree3 = HtmlTree.CAPTION(localHtmlTree1);
/*  860 */     localHtmlTree3.addContent(localHtmlTree2);
/*  861 */     return localHtmlTree3;
/*      */   }
/*      */ 
/*      */   public Content getMarkerAnchor(String paramString)
/*      */   {
/*  871 */     return getMarkerAnchor(getName(paramString), null);
/*      */   }
/*      */ 
/*      */   public Content getMarkerAnchor(SectionName paramSectionName)
/*      */   {
/*  881 */     return getMarkerAnchor(paramSectionName.getName(), null);
/*      */   }
/*      */ 
/*      */   public Content getMarkerAnchor(SectionName paramSectionName, String paramString)
/*      */   {
/*  892 */     return getMarkerAnchor(paramSectionName.getName() + getName(paramString), null);
/*      */   }
/*      */ 
/*      */   public Content getMarkerAnchor(String paramString, Content paramContent)
/*      */   {
/*  903 */     if (paramContent == null)
/*  904 */       paramContent = new Comment(" ");
/*  905 */     HtmlTree localHtmlTree = HtmlTree.A_NAME(paramString, paramContent);
/*  906 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   public Content getPackageName(PackageDoc paramPackageDoc)
/*      */   {
/*  918 */     return (paramPackageDoc == null) || (paramPackageDoc.name().length() == 0) ? this.defaultPackageLabel : 
/*  918 */       getPackageLabel(paramPackageDoc
/*  918 */       .name());
/*      */   }
/*      */ 
/*      */   public Content getPackageLabel(String paramString)
/*      */   {
/*  928 */     return new StringContent(paramString);
/*      */   }
/*      */ 
/*      */   protected void addPackageDeprecatedAPI(List<Doc> paramList, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent)
/*      */   {
/*  942 */     if (paramList.size() > 0) {
/*  943 */       HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.deprecatedSummary, 0, 3, 0, paramString2, 
/*  944 */         getTableCaption(this.configuration
/*  944 */         .getResource(paramString1)));
/*      */ 
/*  945 */       localHtmlTree1.addContent(getSummaryTableHeader(paramArrayOfString, "col"));
/*  946 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/*  947 */       for (int i = 0; i < paramList.size(); i++) {
/*  948 */         localObject = (PackageDoc)paramList.get(i);
/*  949 */         HtmlTree localHtmlTree4 = HtmlTree.TD(HtmlStyle.colOne, 
/*  950 */           getPackageLink((PackageDoc)localObject, 
/*  950 */           getPackageName((PackageDoc)localObject)));
/*      */ 
/*  951 */         if (((PackageDoc)localObject).tags("deprecated").length > 0) {
/*  952 */           addInlineDeprecatedComment((Doc)localObject, localObject.tags("deprecated")[0], localHtmlTree4);
/*      */         }
/*  954 */         HtmlTree localHtmlTree5 = HtmlTree.TR(localHtmlTree4);
/*  955 */         if (i % 2 == 0)
/*  956 */           localHtmlTree5.addStyle(HtmlStyle.altColor);
/*      */         else {
/*  958 */           localHtmlTree5.addStyle(HtmlStyle.rowColor);
/*      */         }
/*  960 */         localHtmlTree2.addContent(localHtmlTree5);
/*      */       }
/*  962 */       localHtmlTree1.addContent(localHtmlTree2);
/*  963 */       HtmlTree localHtmlTree3 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/*  964 */       Object localObject = HtmlTree.UL(HtmlStyle.blockList, localHtmlTree3);
/*  965 */       paramContent.addContent((Content)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DocPath pathString(ClassDoc paramClassDoc, DocPath paramDocPath)
/*      */   {
/*  976 */     return pathString(paramClassDoc.containingPackage(), paramDocPath);
/*      */   }
/*      */ 
/*      */   protected DocPath pathString(PackageDoc paramPackageDoc, DocPath paramDocPath)
/*      */   {
/*  989 */     return this.pathToRoot.resolve(DocPath.forPackage(paramPackageDoc).resolve(paramDocPath));
/*      */   }
/*      */ 
/*      */   public Content getPackageLink(PackageDoc paramPackageDoc, String paramString)
/*      */   {
/* 1000 */     return getPackageLink(paramPackageDoc, new StringContent(paramString));
/*      */   }
/*      */ 
/*      */   public Content getPackageLink(PackageDoc paramPackageDoc, Content paramContent)
/*      */   {
/* 1011 */     int i = (paramPackageDoc != null) && (paramPackageDoc.isIncluded()) ? 1 : 0;
/* 1012 */     if (i == 0) {
/* 1013 */       localObject = this.configuration.packages;
/* 1014 */       for (int j = 0; j < localObject.length; j++) {
/* 1015 */         if (localObject[j].equals(paramPackageDoc)) {
/* 1016 */           i = 1;
/* 1017 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1021 */     if ((i != 0) || (paramPackageDoc == null)) {
/* 1022 */       return getHyperLink(pathString(paramPackageDoc, DocPaths.PACKAGE_SUMMARY), paramContent);
/*      */     }
/*      */ 
/* 1025 */     Object localObject = getCrossPackageLink(Util.getPackageName(paramPackageDoc));
/* 1026 */     if (localObject != null) {
/* 1027 */       return getHyperLink((DocLink)localObject, paramContent);
/*      */     }
/* 1029 */     return paramContent;
/*      */   }
/*      */ 
/*      */   public Content italicsClassName(ClassDoc paramClassDoc, boolean paramBoolean)
/*      */   {
/* 1035 */     StringContent localStringContent = new StringContent(paramBoolean ? paramClassDoc.qualifiedName() : paramClassDoc.name());
/* 1036 */     return paramClassDoc.isInterface() ? HtmlTree.SPAN(HtmlStyle.interfaceName, localStringContent) : localStringContent;
/*      */   }
/*      */ 
/*      */   public void addSrcLink(ProgramElementDoc paramProgramElementDoc, Content paramContent1, Content paramContent2)
/*      */   {
/* 1047 */     if (paramProgramElementDoc == null) {
/* 1048 */       return;
/*      */     }
/* 1050 */     ClassDoc localClassDoc = paramProgramElementDoc.containingClass();
/* 1051 */     if (localClassDoc == null)
/*      */     {
/* 1053 */       localClassDoc = (ClassDoc)paramProgramElementDoc;
/*      */     }
/*      */ 
/* 1057 */     DocPath localDocPath = this.pathToRoot
/* 1056 */       .resolve(DocPaths.SOURCE_OUTPUT)
/* 1057 */       .resolve(DocPath.forClass(localClassDoc));
/*      */ 
/* 1058 */     Content localContent = getHyperLink(localDocPath.fragment(SourceToHTMLConverter.getAnchorName(paramProgramElementDoc)), paramContent1, "", "");
/* 1059 */     paramContent2.addContent(localContent);
/*      */   }
/*      */ 
/*      */   public Content getLink(LinkInfoImpl paramLinkInfoImpl)
/*      */   {
/* 1070 */     LinkFactoryImpl localLinkFactoryImpl = new LinkFactoryImpl(this);
/* 1071 */     return localLinkFactoryImpl.getLink(paramLinkInfoImpl);
/*      */   }
/*      */ 
/*      */   public Content getTypeParameterLinks(LinkInfoImpl paramLinkInfoImpl)
/*      */   {
/* 1081 */     LinkFactoryImpl localLinkFactoryImpl = new LinkFactoryImpl(this);
/* 1082 */     return localLinkFactoryImpl.getTypeParameterLinks(paramLinkInfoImpl, false);
/*      */   }
/*      */ 
/*      */   public Content getCrossClassLink(String paramString1, String paramString2, Content paramContent, boolean paramBoolean1, String paramString3, boolean paramBoolean2)
/*      */   {
/* 1102 */     String str1 = "";
/* 1103 */     String str2 = paramString1 == null ? "" : paramString1;
/*      */     int i;
/* 1105 */     while ((i = str2.lastIndexOf('.')) != -1)
/*      */     {
/* 1107 */       str1 = str2.substring(i + 1, str2.length()) + (str1
/* 1107 */         .length() > 0 ? "." + str1 : "");
/* 1108 */       Object localObject = new StringContent(str1);
/* 1109 */       if (paramBoolean2)
/* 1110 */         localObject = HtmlTree.CODE((Content)localObject);
/* 1111 */       str2 = str2.substring(0, i);
/* 1112 */       if (getCrossPackageLink(str2) != null)
/*      */       {
/* 1118 */         DocLink localDocLink = this.configuration.extern.getExternalLink(str2, this.pathToRoot, str1 + ".html", paramString2);
/*      */ 
/* 1120 */         return getHyperLink(localDocLink, (paramContent == null) || 
/* 1121 */           (paramContent
/* 1121 */           .isEmpty()) ? localObject : paramContent, paramBoolean1, paramString3, this.configuration
/* 1123 */           .getText("doclet.Href_Class_Or_Interface_Title", str2), 
/* 1123 */           "");
/*      */       }
/*      */     }
/*      */ 
/* 1127 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isClassLinkable(ClassDoc paramClassDoc) {
/* 1131 */     if (paramClassDoc.isIncluded()) {
/* 1132 */       return this.configuration.isGeneratedDoc(paramClassDoc);
/*      */     }
/* 1134 */     return this.configuration.extern.isExternal(paramClassDoc);
/*      */   }
/*      */ 
/*      */   public DocLink getCrossPackageLink(String paramString) {
/* 1138 */     return this.configuration.extern.getExternalLink(paramString, this.pathToRoot, DocPaths.PACKAGE_SUMMARY
/* 1139 */       .getPath());
/*      */   }
/*      */ 
/*      */   public Content getQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc)
/*      */   {
/* 1150 */     return getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1151 */       .label(this.configuration
/* 1151 */       .getClassName(paramClassDoc)));
/*      */   }
/*      */ 
/*      */   public void addPreQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, Content paramContent)
/*      */   {
/* 1162 */     addPreQualifiedClassLink(paramKind, paramClassDoc, false, paramContent);
/*      */   }
/*      */ 
/*      */   public Content getPreQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, boolean paramBoolean)
/*      */   {
/* 1176 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 1177 */     PackageDoc localPackageDoc = paramClassDoc.containingPackage();
/* 1178 */     if ((localPackageDoc != null) && (!this.configuration.shouldExcludeQualifier(localPackageDoc.name()))) {
/* 1179 */       localContentBuilder.addContent(getPkgName(paramClassDoc));
/*      */     }
/* 1181 */     localContentBuilder.addContent(getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1182 */       .label(paramClassDoc
/* 1182 */       .name()).strong(paramBoolean)));
/* 1183 */     return localContentBuilder;
/*      */   }
/*      */ 
/*      */   public void addPreQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, boolean paramBoolean, Content paramContent)
/*      */   {
/* 1198 */     PackageDoc localPackageDoc = paramClassDoc.containingPackage();
/* 1199 */     if ((localPackageDoc != null) && (!this.configuration.shouldExcludeQualifier(localPackageDoc.name()))) {
/* 1200 */       paramContent.addContent(getPkgName(paramClassDoc));
/*      */     }
/* 1202 */     paramContent.addContent(getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1203 */       .label(paramClassDoc
/* 1203 */       .name()).strong(paramBoolean)));
/*      */   }
/*      */ 
/*      */   public void addPreQualifiedStrongClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, Content paramContent)
/*      */   {
/* 1215 */     addPreQualifiedClassLink(paramKind, paramClassDoc, true, paramContent);
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, MemberDoc paramMemberDoc, String paramString)
/*      */   {
/* 1227 */     return getDocLink(paramKind, paramMemberDoc.containingClass(), paramMemberDoc, new StringContent(paramString));
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, MemberDoc paramMemberDoc, String paramString, boolean paramBoolean)
/*      */   {
/* 1242 */     return getDocLink(paramKind, paramMemberDoc.containingClass(), paramMemberDoc, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, String paramString, boolean paramBoolean)
/*      */   {
/* 1259 */     return getDocLink(paramKind, paramClassDoc, paramMemberDoc, paramString, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, Content paramContent, boolean paramBoolean) {
/* 1263 */     return getDocLink(paramKind, paramClassDoc, paramMemberDoc, paramContent, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1281 */     return getDocLink(paramKind, paramClassDoc, paramMemberDoc, new StringContent(check(paramString)), paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   String check(String paramString) {
/* 1285 */     if (paramString.matches(".*[&<>].*")) throw new IllegalArgumentException(paramString);
/* 1286 */     return paramString;
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, Content paramContent, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1291 */     if ((!paramMemberDoc.isIncluded()) && 
/* 1292 */       (!Util.isLinkable(paramClassDoc, this.configuration)))
/*      */     {
/* 1293 */       return paramContent;
/* 1294 */     }if ((paramMemberDoc instanceof ExecutableMemberDoc)) {
/* 1295 */       ExecutableMemberDoc localExecutableMemberDoc = (ExecutableMemberDoc)paramMemberDoc;
/* 1296 */       return getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1297 */         .label(paramContent)
/* 1297 */         .where(getName(getAnchor(localExecutableMemberDoc, paramBoolean2))).strong(paramBoolean1));
/* 1298 */     }if ((paramMemberDoc instanceof MemberDoc)) {
/* 1299 */       return getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1300 */         .label(paramContent)
/* 1300 */         .where(getName(paramMemberDoc.name())).strong(paramBoolean1));
/*      */     }
/* 1302 */     return paramContent;
/*      */   }
/*      */ 
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, Content paramContent)
/*      */   {
/* 1319 */     if ((!paramMemberDoc.isIncluded()) && 
/* 1320 */       (!Util.isLinkable(paramClassDoc, this.configuration)))
/*      */     {
/* 1321 */       return paramContent;
/* 1322 */     }if ((paramMemberDoc instanceof ExecutableMemberDoc)) {
/* 1323 */       ExecutableMemberDoc localExecutableMemberDoc = (ExecutableMemberDoc)paramMemberDoc;
/* 1324 */       return getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1325 */         .label(paramContent)
/* 1325 */         .where(getName(getAnchor(localExecutableMemberDoc))));
/* 1326 */     }if ((paramMemberDoc instanceof MemberDoc)) {
/* 1327 */       return getLink(new LinkInfoImpl(this.configuration, paramKind, paramClassDoc)
/* 1328 */         .label(paramContent)
/* 1328 */         .where(getName(paramMemberDoc.name())));
/*      */     }
/* 1330 */     return paramContent;
/*      */   }
/*      */ 
/*      */   public String getAnchor(ExecutableMemberDoc paramExecutableMemberDoc)
/*      */   {
/* 1335 */     return getAnchor(paramExecutableMemberDoc, false);
/*      */   }
/*      */ 
/*      */   public String getAnchor(ExecutableMemberDoc paramExecutableMemberDoc, boolean paramBoolean) {
/* 1339 */     if (paramBoolean) {
/* 1340 */       return paramExecutableMemberDoc.name();
/*      */     }
/* 1342 */     StringBuilder localStringBuilder1 = new StringBuilder(paramExecutableMemberDoc.signature());
/* 1343 */     StringBuilder localStringBuilder2 = new StringBuilder();
/* 1344 */     int i = 0;
/* 1345 */     for (int j = 0; j < localStringBuilder1.length(); j++) {
/* 1346 */       char c = localStringBuilder1.charAt(j);
/* 1347 */       if (c == '<')
/* 1348 */         i++;
/* 1349 */       else if (c == '>')
/* 1350 */         i--;
/* 1351 */       else if (i == 0) {
/* 1352 */         localStringBuilder2.append(c);
/*      */       }
/*      */     }
/* 1355 */     return paramExecutableMemberDoc.name() + localStringBuilder2.toString();
/*      */   }
/*      */ 
/*      */   public Content seeTagToContent(SeeTag paramSeeTag) {
/* 1359 */     String str1 = paramSeeTag.name();
/* 1360 */     if ((!str1.startsWith("@link")) && (!str1.equals("@see"))) {
/* 1361 */       return new ContentBuilder();
/*      */     }
/*      */ 
/* 1364 */     String str2 = replaceDocRootDir(Util.normalizeNewlines(paramSeeTag.text()));
/*      */ 
/* 1367 */     if ((str2.startsWith("<")) || (str2.startsWith("\""))) {
/* 1368 */       return new RawHtml(str2);
/*      */     }
/*      */ 
/* 1371 */     boolean bool = str1.equalsIgnoreCase("@linkplain");
/* 1372 */     Content localContent1 = plainOrCode(bool, new RawHtml(paramSeeTag.label()));
/*      */ 
/* 1375 */     Content localContent2 = plainOrCode(bool, new RawHtml(str2));
/*      */ 
/* 1377 */     ClassDoc localClassDoc = paramSeeTag.referencedClass();
/* 1378 */     String str3 = paramSeeTag.referencedClassName();
/* 1379 */     MemberDoc localMemberDoc = paramSeeTag.referencedMember();
/* 1380 */     String str4 = paramSeeTag.referencedMemberName();
/*      */ 
/* 1382 */     if (localClassDoc == null)
/*      */     {
/* 1384 */       localObject = paramSeeTag.referencedPackage();
/* 1385 */       if ((localObject != null) && (((PackageDoc)localObject).isIncluded()))
/*      */       {
/* 1387 */         if (localContent1.isEmpty())
/* 1388 */           localContent1 = plainOrCode(bool, new StringContent(((PackageDoc)localObject).name()));
/* 1389 */         return getPackageLink((PackageDoc)localObject, localContent1);
/*      */       }
/*      */ 
/* 1393 */       DocLink localDocLink = getCrossPackageLink(str3);
/* 1394 */       if (localDocLink != null)
/*      */       {
/* 1396 */         return getHyperLink(localDocLink, localContent1
/* 1397 */           .isEmpty() ? localContent2 : localContent1);
/*      */       }
/*      */       Content localContent3;
/* 1398 */       if ((localContent3 = getCrossClassLink(str3, str4, localContent1, false, "", !bool)) != null)
/*      */       {
/* 1401 */         return localContent3;
/*      */       }
/*      */ 
/* 1404 */       this.configuration.getDocletSpecificMsg().warning(paramSeeTag.position(), "doclet.see.class_or_package_not_found", new Object[] { str1, str2 });
/*      */ 
/* 1406 */       return localContent1.isEmpty() ? localContent2 : localContent1;
/*      */     }
/*      */ 
/* 1409 */     if (str4 == null)
/*      */     {
/* 1411 */       if (localContent1.isEmpty()) {
/* 1412 */         localContent1 = plainOrCode(bool, new StringContent(localClassDoc.name()));
/*      */       }
/* 1414 */       return getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.DEFAULT, localClassDoc)
/* 1415 */         .label(localContent1));
/*      */     }
/* 1416 */     if (localMemberDoc == null)
/*      */     {
/* 1419 */       return localContent1.isEmpty() ? localContent2 : localContent1;
/*      */     }
/*      */ 
/* 1423 */     Object localObject = localMemberDoc.containingClass();
/* 1424 */     if ((paramSeeTag.text().trim().startsWith("#")) && 
/* 1425 */       (!((ClassDoc)localObject)
/* 1425 */       .isPublic()) && 
/* 1426 */       (!Util.isLinkable((ClassDoc)localObject, this.configuration)))
/*      */     {
/* 1431 */       if ((this instanceof ClassWriterImpl))
/* 1432 */         localObject = ((ClassWriterImpl)this).getClassDoc();
/* 1433 */       else if (!((ClassDoc)localObject).isPublic())
/* 1434 */         this.configuration.getDocletSpecificMsg().warning(paramSeeTag
/* 1435 */           .position(), "doclet.see.class_or_package_not_accessible", new Object[] { str1, ((ClassDoc)localObject)
/* 1436 */           .qualifiedName() });
/*      */       else {
/* 1438 */         this.configuration.getDocletSpecificMsg().warning(paramSeeTag
/* 1439 */           .position(), "doclet.see.class_or_package_not_found", new Object[] { str1, str2 });
/*      */       }
/*      */     }
/*      */ 
/* 1443 */     if (this.configuration.currentcd != localObject) {
/* 1444 */       str4 = ((ClassDoc)localObject).name() + "." + str4;
/*      */     }
/* 1446 */     if (((localMemberDoc instanceof ExecutableMemberDoc)) && 
/* 1447 */       (str4.indexOf('(') < 0)) {
/* 1448 */       str4 = str4 + ((ExecutableMemberDoc)localMemberDoc).signature();
/*      */     }
/*      */ 
/* 1452 */     localContent2 = plainOrCode(bool, new StringContent(str4));
/*      */ 
/* 1454 */     return getDocLink(LinkInfoImpl.Kind.SEE_TAG, (ClassDoc)localObject, localMemberDoc, localContent1
/* 1455 */       .isEmpty() ? localContent2 : localContent1, false);
/*      */   }
/*      */ 
/*      */   private Content plainOrCode(boolean paramBoolean, Content paramContent)
/*      */   {
/* 1460 */     return (paramBoolean) || (paramContent.isEmpty()) ? paramContent : HtmlTree.CODE(paramContent);
/*      */   }
/*      */ 
/*      */   public void addInlineComment(Doc paramDoc, Tag paramTag, Content paramContent)
/*      */   {
/* 1471 */     addCommentTags(paramDoc, paramTag, paramTag.inlineTags(), false, false, paramContent);
/*      */   }
/*      */ 
/*      */   public void addInlineDeprecatedComment(Doc paramDoc, Tag paramTag, Content paramContent)
/*      */   {
/* 1482 */     addCommentTags(paramDoc, paramTag.inlineTags(), true, false, paramContent);
/*      */   }
/*      */ 
/*      */   public void addSummaryComment(Doc paramDoc, Content paramContent)
/*      */   {
/* 1492 */     addSummaryComment(paramDoc, paramDoc.firstSentenceTags(), paramContent);
/*      */   }
/*      */ 
/*      */   public void addSummaryComment(Doc paramDoc, Tag[] paramArrayOfTag, Content paramContent)
/*      */   {
/* 1503 */     addCommentTags(paramDoc, paramArrayOfTag, false, true, paramContent);
/*      */   }
/*      */ 
/*      */   public void addSummaryDeprecatedComment(Doc paramDoc, Tag paramTag, Content paramContent) {
/* 1507 */     addCommentTags(paramDoc, paramTag.firstSentenceTags(), true, true, paramContent);
/*      */   }
/*      */ 
/*      */   public void addInlineComment(Doc paramDoc, Content paramContent)
/*      */   {
/* 1517 */     addCommentTags(paramDoc, paramDoc.inlineTags(), false, false, paramContent);
/*      */   }
/*      */ 
/*      */   private void addCommentTags(Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean1, boolean paramBoolean2, Content paramContent)
/*      */   {
/* 1531 */     addCommentTags(paramDoc, null, paramArrayOfTag, paramBoolean1, paramBoolean2, paramContent);
/*      */   }
/*      */ 
/*      */   private void addCommentTags(Doc paramDoc, Tag paramTag, Tag[] paramArrayOfTag, boolean paramBoolean1, boolean paramBoolean2, Content paramContent)
/*      */   {
/* 1546 */     if (this.configuration.nocomment) {
/* 1547 */       return;
/*      */     }
/*      */ 
/* 1550 */     Content localContent = commentTagsToContent(null, paramDoc, paramArrayOfTag, paramBoolean2);
/*      */     HtmlTree localHtmlTree1;
/* 1551 */     if (paramBoolean1) {
/* 1552 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecationComment, localContent);
/* 1553 */       localHtmlTree1 = HtmlTree.DIV(HtmlStyle.block, localHtmlTree2);
/* 1554 */       paramContent.addContent(localHtmlTree1);
/*      */     }
/*      */     else {
/* 1557 */       localHtmlTree1 = HtmlTree.DIV(HtmlStyle.block, localContent);
/* 1558 */       paramContent.addContent(localHtmlTree1);
/*      */     }
/* 1560 */     if (paramArrayOfTag.length == 0)
/* 1561 */       paramContent.addContent(getSpace());
/*      */   }
/*      */ 
/*      */   public Content commentTagsToContent(Tag paramTag, Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean)
/*      */   {
/* 1579 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 1580 */     int i = 0;
/*      */ 
/* 1582 */     this.configuration.tagletManager.checkTags(paramDoc, paramArrayOfTag, true);
/* 1583 */     for (int j = 0; j < paramArrayOfTag.length; j++) {
/* 1584 */       Tag localTag = paramArrayOfTag[j];
/* 1585 */       String str1 = localTag.name();
/* 1586 */       if ((localTag instanceof SeeTag)) {
/* 1587 */         localContentBuilder.addContent(seeTagToContent((SeeTag)localTag));
/* 1588 */       } else if (!str1.equals("Text")) {
/* 1589 */         boolean bool = localContentBuilder.isEmpty();
/*      */         Object localObject;
/* 1591 */         if ((this.configuration.docrootparent.length() > 0) && 
/* 1592 */           (localTag
/* 1592 */           .name().equals("@docRoot")) && 
/* 1593 */           (paramArrayOfTag[(j + 1)]
/* 1593 */           .text().startsWith("/..")))
/*      */         {
/* 1596 */           i = 1;
/*      */ 
/* 1598 */           localObject = new StringContent(this.configuration.docrootparent);
/*      */         } else {
/* 1600 */           localObject = TagletWriter.getInlineTagOuput(this.configuration.tagletManager, paramTag, localTag, 
/* 1602 */             getTagletWriterInstance(paramBoolean));
/*      */         }
/*      */ 
/* 1604 */         if (localObject != null)
/* 1605 */           localContentBuilder.addContent((Content)localObject);
/* 1606 */         if ((bool) && (paramBoolean) && (localTag.name().equals("@inheritDoc")) && (!localContentBuilder.isEmpty())) {
/* 1607 */           break;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1612 */         String str2 = localTag.text();
/*      */ 
/* 1614 */         if (i != 0) {
/* 1615 */           str2 = str2.replaceFirst("/..", "");
/* 1616 */           i = 0;
/*      */         }
/*      */ 
/* 1620 */         str2 = redirectRelativeLinks(localTag.holder(), str2);
/*      */ 
/* 1627 */         str2 = replaceDocRootDir(str2);
/* 1628 */         if (paramBoolean) {
/* 1629 */           str2 = removeNonInlineHtmlTags(str2);
/*      */         }
/* 1631 */         str2 = Util.replaceTabs(this.configuration, str2);
/* 1632 */         str2 = Util.normalizeNewlines(str2);
/* 1633 */         localContentBuilder.addContent(new RawHtml(str2));
/*      */       }
/*      */     }
/* 1636 */     return localContentBuilder;
/*      */   }
/*      */ 
/*      */   private boolean shouldNotRedirectRelativeLinks()
/*      */   {
/* 1645 */     return ((this instanceof AnnotationTypeWriter)) || ((this instanceof ClassWriter)) || ((this instanceof PackageSummaryWriter));
/*      */   }
/*      */ 
/*      */   private String redirectRelativeLinks(Doc paramDoc, String paramString)
/*      */   {
/* 1674 */     if ((paramDoc == null) || (shouldNotRedirectRelativeLinks()))
/* 1675 */       return paramString;
/*      */     DocPath localDocPath;
/* 1679 */     if ((paramDoc instanceof ClassDoc))
/* 1680 */       localDocPath = DocPath.forPackage(((ClassDoc)paramDoc).containingPackage());
/* 1681 */     else if ((paramDoc instanceof MemberDoc))
/* 1682 */       localDocPath = DocPath.forPackage(((MemberDoc)paramDoc).containingPackage());
/* 1683 */     else if ((paramDoc instanceof PackageDoc))
/* 1684 */       localDocPath = DocPath.forPackage((PackageDoc)paramDoc);
/*      */     else {
/* 1686 */       return paramString;
/*      */     }
/*      */ 
/* 1690 */     int j = StringUtils.indexOfIgnoreCase(paramString, "<a");
/* 1691 */     if (j >= 0) {
/* 1692 */       StringBuilder localStringBuilder = new StringBuilder(paramString);
/*      */ 
/* 1694 */       while (j >= 0)
/* 1695 */         if ((localStringBuilder.length() > j + 2) && (!Character.isWhitespace(localStringBuilder.charAt(j + 2)))) {
/* 1696 */           j = StringUtils.indexOfIgnoreCase(localStringBuilder.toString(), "<a", j + 1);
/*      */         }
/*      */         else
/*      */         {
/* 1700 */           j = localStringBuilder.indexOf("=", j) + 1;
/* 1701 */           int i = localStringBuilder.indexOf(">", j + 1);
/* 1702 */           if (j == 0)
/*      */           {
/* 1704 */             this.configuration.root.printWarning(paramDoc
/* 1705 */               .position(), this.configuration
/* 1706 */               .getText("doclet.malformed_html_link_tag", paramString));
/*      */ 
/* 1707 */             break;
/*      */           }
/* 1709 */           if (i == -1)
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/* 1714 */           if (localStringBuilder.substring(j, i).indexOf("\"") != -1) {
/* 1715 */             j = localStringBuilder.indexOf("\"", j) + 1;
/* 1716 */             i = localStringBuilder.indexOf("\"", j + 1);
/* 1717 */             if ((j == 0) || (i == -1))
/*      */             {
/*      */               break;
/*      */             }
/*      */           }
/* 1722 */           String str1 = localStringBuilder.substring(j, i);
/* 1723 */           String str2 = StringUtils.toLowerCase(str1);
/* 1724 */           if ((!str2.startsWith("mailto:")) && 
/* 1725 */             (!str2
/* 1725 */             .startsWith("http:")) && 
/* 1726 */             (!str2
/* 1726 */             .startsWith("https:")) && 
/* 1727 */             (!str2
/* 1727 */             .startsWith("file:")))
/*      */           {
/* 1729 */             str1 = "{@" + new DocRootTaglet().getName() + "}/" + localDocPath
/* 1729 */               .resolve(str1)
/* 1729 */               .getPath();
/* 1730 */             localStringBuilder.replace(j, i, str1);
/*      */           }
/* 1732 */           j = StringUtils.indexOfIgnoreCase(localStringBuilder.toString(), "<a", j + 1);
/*      */         }
/* 1734 */       return localStringBuilder.toString();
/*      */     }
/* 1736 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String removeNonInlineHtmlTags(String paramString)
/*      */   {
/* 1748 */     int i = paramString.length();
/*      */ 
/* 1750 */     int j = 0;
/* 1751 */     int k = paramString.indexOf('<');
/* 1752 */     if (k < 0) {
/* 1753 */       return paramString;
/*      */     }
/*      */ 
/* 1756 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1757 */     while (k != -1) {
/* 1758 */       int m = k + 1;
/* 1759 */       if (m == i)
/*      */         break;
/* 1761 */       char c = paramString.charAt(m);
/* 1762 */       if (c == '/') {
/* 1763 */         m++; if (m == i)
/*      */           break;
/* 1765 */         c = paramString.charAt(m);
/*      */       }
/* 1767 */       int n = m;
/* 1768 */       while (isHtmlTagLetterOrDigit(c)) {
/* 1769 */         m++; if (m == i)
/*      */           break label173;
/* 1771 */         c = paramString.charAt(m);
/*      */       }
/* 1773 */       if ((c == '>') && (blockTags.contains(StringUtils.toLowerCase(paramString.substring(n, m))))) {
/* 1774 */         localStringBuilder.append(paramString, j, k);
/* 1775 */         j = m + 1;
/*      */       }
/* 1777 */       k = paramString.indexOf('<', m);
/*      */     }
/* 1779 */     label173: localStringBuilder.append(paramString.substring(j));
/*      */ 
/* 1781 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static boolean isHtmlTagLetterOrDigit(char paramChar) {
/* 1785 */     return (('a' <= paramChar) && (paramChar <= 'z')) || (('A' <= paramChar) && (paramChar <= 'Z')) || (('1' <= paramChar) && (paramChar <= '6'));
/*      */   }
/*      */ 
/*      */   public HtmlTree getStyleSheetProperties()
/*      */   {
/* 1796 */     String str = this.configuration.stylesheetfile;
/*      */     DocPath localDocPath;
/* 1798 */     if (str.isEmpty()) {
/* 1799 */       localDocPath = DocPaths.STYLESHEET;
/*      */     } else {
/* 1801 */       localObject = DocFile.createFileForInput(this.configuration, str);
/* 1802 */       localDocPath = DocPath.create(((DocFile)localObject).getName());
/*      */     }
/* 1804 */     Object localObject = HtmlTree.LINK("stylesheet", "text/css", this.pathToRoot
/* 1805 */       .resolve(localDocPath)
/* 1805 */       .getPath(), "Style");
/*      */ 
/* 1807 */     return localObject;
/*      */   }
/*      */ 
/*      */   public HtmlTree getScriptProperties()
/*      */   {
/* 1816 */     HtmlTree localHtmlTree = HtmlTree.SCRIPT("text/javascript", this.pathToRoot
/* 1817 */       .resolve(DocPaths.JAVASCRIPT)
/* 1817 */       .getPath());
/* 1818 */     return localHtmlTree;
/*      */   }
/*      */ 
/*      */   public boolean isCoreClass(ClassDoc paramClassDoc)
/*      */   {
/* 1827 */     return (paramClassDoc.containingClass() == null) || (paramClassDoc.isStatic());
/*      */   }
/*      */ 
/*      */   public void addAnnotationInfo(PackageDoc paramPackageDoc, Content paramContent)
/*      */   {
/* 1838 */     addAnnotationInfo(paramPackageDoc, paramPackageDoc.annotations(), paramContent);
/*      */   }
/*      */ 
/*      */   public void addReceiverAnnotationInfo(ExecutableMemberDoc paramExecutableMemberDoc, AnnotationDesc[] paramArrayOfAnnotationDesc, Content paramContent)
/*      */   {
/* 1851 */     addAnnotationInfo(0, paramExecutableMemberDoc, paramArrayOfAnnotationDesc, false, paramContent);
/*      */   }
/*      */ 
/*      */   public void addAnnotationInfo(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*      */   {
/* 1861 */     addAnnotationInfo(paramProgramElementDoc, paramProgramElementDoc.annotations(), paramContent);
/*      */   }
/*      */ 
/*      */   public boolean addAnnotationInfo(int paramInt, Doc paramDoc, Parameter paramParameter, Content paramContent)
/*      */   {
/* 1874 */     return addAnnotationInfo(paramInt, paramDoc, paramParameter.annotations(), false, paramContent);
/*      */   }
/*      */ 
/*      */   private void addAnnotationInfo(Doc paramDoc, AnnotationDesc[] paramArrayOfAnnotationDesc, Content paramContent)
/*      */   {
/* 1887 */     addAnnotationInfo(0, paramDoc, paramArrayOfAnnotationDesc, true, paramContent);
/*      */   }
/*      */ 
/*      */   private boolean addAnnotationInfo(int paramInt, Doc paramDoc, AnnotationDesc[] paramArrayOfAnnotationDesc, boolean paramBoolean, Content paramContent)
/*      */   {
/* 1901 */     List localList = getAnnotations(paramInt, paramArrayOfAnnotationDesc, paramBoolean);
/* 1902 */     String str = "";
/* 1903 */     if (localList.isEmpty()) {
/* 1904 */       return false;
/*      */     }
/* 1906 */     for (Content localContent : localList) {
/* 1907 */       paramContent.addContent(str);
/* 1908 */       paramContent.addContent(localContent);
/* 1909 */       str = " ";
/*      */     }
/* 1911 */     return true;
/*      */   }
/*      */ 
/*      */   private List<Content> getAnnotations(int paramInt, AnnotationDesc[] paramArrayOfAnnotationDesc, boolean paramBoolean)
/*      */   {
/* 1925 */     return getAnnotations(paramInt, paramArrayOfAnnotationDesc, paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public List<Content> getAnnotations(int paramInt, AnnotationDesc[] paramArrayOfAnnotationDesc, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1945 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1947 */     for (int i = 0; i < paramArrayOfAnnotationDesc.length; i++) {
/* 1948 */       AnnotationTypeDoc localAnnotationTypeDoc = paramArrayOfAnnotationDesc[i].annotationType();
/*      */ 
/* 1954 */       if ((Util.isDocumentedAnnotation(localAnnotationTypeDoc)) || (this.isAnnotationDocumented) || (this.isContainerDocumented))
/*      */       {
/* 1963 */         ContentBuilder localContentBuilder = new ContentBuilder();
/* 1964 */         this.isAnnotationDocumented = false;
/* 1965 */         LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.ANNOTATION, localAnnotationTypeDoc);
/*      */ 
/* 1967 */         AnnotationDesc.ElementValuePair[] arrayOfElementValuePair = paramArrayOfAnnotationDesc[i].elementValues();
/*      */         Object localObject1;
/*      */         Object localObject2;
/*      */         Object localObject3;
/*      */         Object localObject4;
/* 1969 */         if (paramArrayOfAnnotationDesc[i].isSynthesized()) {
/* 1970 */           for (int j = 0; j < arrayOfElementValuePair.length; j++) {
/* 1971 */             localObject1 = arrayOfElementValuePair[j].value();
/* 1972 */             localObject2 = new ArrayList();
/* 1973 */             if ((((AnnotationValue)localObject1).value() instanceof AnnotationValue[]))
/*      */             {
/* 1975 */               localObject3 = (AnnotationValue[])((AnnotationValue)localObject1)
/* 1975 */                 .value();
/* 1976 */               ((List)localObject2).addAll(Arrays.asList((Object[])localObject3));
/*      */             } else {
/* 1978 */               ((List)localObject2).add(localObject1);
/*      */             }
/* 1980 */             localObject3 = "";
/* 1981 */             for (localObject4 = ((List)localObject2).iterator(); ((Iterator)localObject4).hasNext(); ) { AnnotationValue localAnnotationValue = (AnnotationValue)((Iterator)localObject4).next();
/* 1982 */               localContentBuilder.addContent((String)localObject3);
/* 1983 */               localContentBuilder.addContent(annotationValueToContent(localAnnotationValue));
/* 1984 */               localObject3 = " ";
/*      */             }
/*      */           }
/*      */         }
/* 1988 */         else if (isAnnotationArray(arrayOfElementValuePair))
/*      */         {
/* 1992 */           if ((arrayOfElementValuePair.length == 1) && (this.isAnnotationDocumented))
/*      */           {
/* 1994 */             AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])arrayOfElementValuePair[0]
/* 1994 */               .value().value();
/* 1995 */             localObject1 = new ArrayList();
/* 1996 */             ((List)localObject1).addAll(Arrays.asList(arrayOfAnnotationValue));
/* 1997 */             localObject2 = "";
/* 1998 */             for (localObject3 = ((List)localObject1).iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (AnnotationValue)((Iterator)localObject3).next();
/* 1999 */               localContentBuilder.addContent((String)localObject2);
/* 2000 */               localContentBuilder.addContent(annotationValueToContent((AnnotationValue)localObject4));
/* 2001 */               localObject2 = " ";
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 2007 */             addAnnotations(localAnnotationTypeDoc, localLinkInfoImpl, localContentBuilder, arrayOfElementValuePair, paramInt, false);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2012 */           addAnnotations(localAnnotationTypeDoc, localLinkInfoImpl, localContentBuilder, arrayOfElementValuePair, paramInt, paramBoolean1);
/*      */         }
/*      */ 
/* 2015 */         localContentBuilder.addContent(paramBoolean1 ? DocletConstants.NL : "");
/* 2016 */         localArrayList.add(localContentBuilder);
/*      */       }
/*      */     }
/* 2018 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private void addAnnotations(AnnotationTypeDoc paramAnnotationTypeDoc, LinkInfoImpl paramLinkInfoImpl, ContentBuilder paramContentBuilder, AnnotationDesc.ElementValuePair[] paramArrayOfElementValuePair, int paramInt, boolean paramBoolean)
/*      */   {
/* 2034 */     paramLinkInfoImpl.label = new StringContent("@" + paramAnnotationTypeDoc.name());
/* 2035 */     paramContentBuilder.addContent(getLink(paramLinkInfoImpl));
/* 2036 */     if (paramArrayOfElementValuePair.length > 0) {
/* 2037 */       paramContentBuilder.addContent("(");
/* 2038 */       for (int i = 0; i < paramArrayOfElementValuePair.length; i++) {
/* 2039 */         if (i > 0) {
/* 2040 */           paramContentBuilder.addContent(",");
/* 2041 */           if (paramBoolean) {
/* 2042 */             paramContentBuilder.addContent(DocletConstants.NL);
/* 2043 */             int j = paramAnnotationTypeDoc.name().length() + 2;
/* 2044 */             for (int k = 0; k < j + paramInt; k++) {
/* 2045 */               paramContentBuilder.addContent(" ");
/*      */             }
/*      */           }
/*      */         }
/* 2049 */         paramContentBuilder.addContent(getDocLink(LinkInfoImpl.Kind.ANNOTATION, paramArrayOfElementValuePair[i]
/* 2050 */           .element(), paramArrayOfElementValuePair[i].element().name(), false));
/* 2051 */         paramContentBuilder.addContent("=");
/* 2052 */         AnnotationValue localAnnotationValue1 = paramArrayOfElementValuePair[i].value();
/* 2053 */         ArrayList localArrayList = new ArrayList();
/* 2054 */         if ((localAnnotationValue1.value() instanceof AnnotationValue[]))
/*      */         {
/* 2056 */           localObject = (AnnotationValue[])localAnnotationValue1
/* 2056 */             .value();
/* 2057 */           localArrayList.addAll(Arrays.asList((Object[])localObject));
/*      */         } else {
/* 2059 */           localArrayList.add(localAnnotationValue1);
/*      */         }
/* 2061 */         paramContentBuilder.addContent(localArrayList.size() == 1 ? "" : "{");
/* 2062 */         Object localObject = "";
/* 2063 */         for (AnnotationValue localAnnotationValue2 : localArrayList) {
/* 2064 */           paramContentBuilder.addContent((String)localObject);
/* 2065 */           paramContentBuilder.addContent(annotationValueToContent(localAnnotationValue2));
/* 2066 */           localObject = ",";
/*      */         }
/* 2068 */         paramContentBuilder.addContent(localArrayList.size() == 1 ? "" : "}");
/* 2069 */         this.isContainerDocumented = false;
/*      */       }
/* 2071 */       paramContentBuilder.addContent(")");
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isAnnotationArray(AnnotationDesc.ElementValuePair[] paramArrayOfElementValuePair)
/*      */   {
/* 2085 */     for (int i = 0; i < paramArrayOfElementValuePair.length; i++) {
/* 2086 */       AnnotationValue localAnnotationValue = paramArrayOfElementValuePair[i].value();
/* 2087 */       if ((localAnnotationValue.value() instanceof AnnotationValue[]))
/*      */       {
/* 2089 */         AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])localAnnotationValue
/* 2089 */           .value();
/* 2090 */         if ((arrayOfAnnotationValue.length > 1) && 
/* 2091 */           ((arrayOfAnnotationValue[0].value() instanceof AnnotationDesc)))
/*      */         {
/* 2093 */           AnnotationTypeDoc localAnnotationTypeDoc = ((AnnotationDesc)arrayOfAnnotationValue[0]
/* 2093 */             .value()).annotationType();
/* 2094 */           this.isContainerDocumented = true;
/* 2095 */           if (Util.isDocumentedAnnotation(localAnnotationTypeDoc)) {
/* 2096 */             this.isAnnotationDocumented = true;
/*      */           }
/* 2098 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2103 */     return false;
/*      */   }
/*      */ 
/*      */   private Content annotationValueToContent(AnnotationValue paramAnnotationValue)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2107 */     if ((paramAnnotationValue.value() instanceof Type)) {
/* 2108 */       localObject1 = (Type)paramAnnotationValue.value();
/* 2109 */       if (((Type)localObject1).asClassDoc() != null) {
/* 2110 */         localObject2 = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.ANNOTATION, (Type)localObject1);
/*      */ 
/* 2112 */         ((LinkInfoImpl)localObject2).label = new StringContent((((Type)localObject1).asClassDoc().isIncluded() ? ((Type)localObject1)
/* 2113 */           .typeName() : ((Type)localObject1)
/* 2114 */           .qualifiedTypeName()) + ((Type)localObject1).dimension() + ".class");
/* 2115 */         return getLink((LinkInfoImpl)localObject2);
/*      */       }
/* 2117 */       return new StringContent(((Type)localObject1).typeName() + ((Type)localObject1).dimension() + ".class");
/*      */     }
/* 2119 */     if ((paramAnnotationValue.value() instanceof AnnotationDesc)) {
/* 2120 */       localObject1 = getAnnotations(0, new AnnotationDesc[] { 
/* 2121 */         (AnnotationDesc)paramAnnotationValue
/* 2121 */         .value() }, false);
/*      */ 
/* 2123 */       localObject2 = new ContentBuilder();
/* 2124 */       for (Content localContent : (List)localObject1) {
/* 2125 */         ((ContentBuilder)localObject2).addContent(localContent);
/*      */       }
/* 2127 */       return localObject2;
/* 2128 */     }if ((paramAnnotationValue.value() instanceof MemberDoc)) {
/* 2129 */       return getDocLink(LinkInfoImpl.Kind.ANNOTATION, 
/* 2130 */         (MemberDoc)paramAnnotationValue
/* 2130 */         .value(), 
/* 2131 */         ((MemberDoc)paramAnnotationValue
/* 2131 */         .value()).name(), false);
/*      */     }
/* 2133 */     return new StringContent(paramAnnotationValue.toString());
/*      */   }
/*      */ 
/*      */   public Configuration configuration()
/*      */   {
/* 2143 */     return this.configuration;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1741 */     for (HtmlTag localHtmlTag : HtmlTag.values())
/* 1742 */       if (localHtmlTag.blockType == HtmlTag.BlockType.BLOCK)
/* 1743 */         blockTags.add(localHtmlTag.value);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.HtmlDocletWriter
 * JD-Core Version:    0.6.2
 */