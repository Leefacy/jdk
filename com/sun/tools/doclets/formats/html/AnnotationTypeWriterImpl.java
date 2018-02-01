/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.MemberSummaryBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class AnnotationTypeWriterImpl extends SubWriterHolderWriter
/*     */   implements AnnotationTypeWriter
/*     */ {
/*     */   protected AnnotationTypeDoc annotationType;
/*     */   protected Type prev;
/*     */   protected Type next;
/*     */ 
/*     */   public AnnotationTypeWriterImpl(ConfigurationImpl paramConfigurationImpl, AnnotationTypeDoc paramAnnotationTypeDoc, Type paramType1, Type paramType2)
/*     */     throws Exception
/*     */   {
/*  71 */     super(paramConfigurationImpl, DocPath.forClass(paramAnnotationTypeDoc));
/*  72 */     this.annotationType = paramAnnotationTypeDoc;
/*  73 */     paramConfigurationImpl.currentcd = paramAnnotationTypeDoc.asClassDoc();
/*  74 */     this.prev = paramType1;
/*  75 */     this.next = paramType2;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/*  84 */     Content localContent = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */ 
/*  86 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  87 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClass()
/*     */   {
/*  96 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.classLabel);
/*  97 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClassUse()
/*     */   {
/* 106 */     Content localContent = getHyperLink(DocPaths.CLASS_USE.resolve(this.filename), this.useLabel);
/* 107 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 108 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkPrevious()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 118 */     if (this.prev != null) {
/* 119 */       Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.prev
/* 120 */         .asClassDoc())
/* 121 */         .label(this.prevclassLabel)
/* 121 */         .strong(true));
/* 122 */       localHtmlTree = HtmlTree.LI(localContent);
/*     */     }
/*     */     else {
/* 125 */       localHtmlTree = HtmlTree.LI(this.prevclassLabel);
/* 126 */     }return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkNext()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 136 */     if (this.next != null) {
/* 137 */       Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.next
/* 138 */         .asClassDoc())
/* 139 */         .label(this.nextclassLabel)
/* 139 */         .strong(true));
/* 140 */       localHtmlTree = HtmlTree.LI(localContent);
/*     */     }
/*     */     else {
/* 143 */       localHtmlTree = HtmlTree.LI(this.nextclassLabel);
/* 144 */     }return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getHeader(String paramString)
/*     */   {
/* 152 */     String str1 = this.annotationType.containingPackage() != null ? this.annotationType
/* 152 */       .containingPackage().name() : "";
/* 153 */     String str2 = this.annotationType.name();
/* 154 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str2));
/* 155 */     addTop(localHtmlTree1);
/* 156 */     addNavLinks(true, localHtmlTree1);
/* 157 */     localHtmlTree1.addContent(HtmlConstants.START_OF_CLASS_DATA);
/* 158 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 159 */     localHtmlTree2.addStyle(HtmlStyle.header);
/* 160 */     if (str1.length() > 0) {
/* 161 */       localObject1 = new StringContent(str1);
/* 162 */       localObject2 = HtmlTree.DIV(HtmlStyle.subTitle, (Content)localObject1);
/* 163 */       localHtmlTree2.addContent((Content)localObject2);
/*     */     }
/* 165 */     Object localObject1 = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_HEADER, this.annotationType);
/*     */ 
/* 167 */     Object localObject2 = new StringContent(paramString);
/* 168 */     HtmlTree localHtmlTree3 = HtmlTree.HEADING(HtmlConstants.CLASS_PAGE_HEADING, true, HtmlStyle.title, (Content)localObject2);
/*     */ 
/* 170 */     localHtmlTree3.addContent(getTypeParameterLinks((LinkInfoImpl)localObject1));
/* 171 */     localHtmlTree2.addContent(localHtmlTree3);
/* 172 */     localHtmlTree1.addContent(localHtmlTree2);
/* 173 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getAnnotationContentHeader()
/*     */   {
/* 180 */     return getContentHeader();
/*     */   }
/*     */ 
/*     */   public void addFooter(Content paramContent)
/*     */   {
/* 187 */     paramContent.addContent(HtmlConstants.END_OF_CLASS_DATA);
/* 188 */     addNavLinks(false, paramContent);
/* 189 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 196 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.annotationType), true, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getAnnotationInfoTreeHeader()
/*     */   {
/* 204 */     return getMemberTreeHeader();
/*     */   }
/*     */ 
/*     */   public Content getAnnotationInfo(Content paramContent)
/*     */   {
/* 211 */     return getMemberTree(HtmlStyle.description, paramContent);
/*     */   }
/*     */ 
/*     */   public void addAnnotationTypeSignature(String paramString, Content paramContent)
/*     */   {
/* 218 */     paramContent.addContent(new HtmlTree(HtmlTag.BR));
/* 219 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.PRE);
/* 220 */     addAnnotationInfo(this.annotationType, localHtmlTree1);
/* 221 */     localHtmlTree1.addContent(paramString);
/* 222 */     LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE, this.annotationType);
/*     */ 
/* 224 */     StringContent localStringContent = new StringContent(this.annotationType.name());
/* 225 */     Content localContent = getTypeParameterLinks(localLinkInfoImpl);
/* 226 */     if (this.configuration.linksource) {
/* 227 */       addSrcLink(this.annotationType, localStringContent, localHtmlTree1);
/* 228 */       localHtmlTree1.addContent(localContent);
/*     */     } else {
/* 230 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.memberNameLabel, localStringContent);
/* 231 */       localHtmlTree2.addContent(localContent);
/* 232 */       localHtmlTree1.addContent(localHtmlTree2);
/*     */     }
/* 234 */     paramContent.addContent(localHtmlTree1);
/*     */   }
/*     */ 
/*     */   public void addAnnotationTypeDescription(Content paramContent)
/*     */   {
/* 241 */     if ((!this.configuration.nocomment) && 
/* 242 */       (this.annotationType.inlineTags().length > 0))
/* 243 */       addInlineComment(this.annotationType, paramContent);
/*     */   }
/*     */ 
/*     */   public void addAnnotationTypeTagInfo(Content paramContent)
/*     */   {
/* 252 */     if (!this.configuration.nocomment)
/* 253 */       addTagsInfo(this.annotationType, paramContent);
/*     */   }
/*     */ 
/*     */   public void addAnnotationTypeDeprecationInfo(Content paramContent)
/*     */   {
/* 261 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.HR);
/* 262 */     paramContent.addContent(localHtmlTree1);
/* 263 */     Tag[] arrayOfTag1 = this.annotationType.tags("deprecated");
/* 264 */     if (Util.isDeprecated(this.annotationType)) {
/* 265 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 266 */       HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.block, localHtmlTree2);
/* 267 */       if (arrayOfTag1.length > 0) {
/* 268 */         Tag[] arrayOfTag2 = arrayOfTag1[0].inlineTags();
/* 269 */         if (arrayOfTag2.length > 0) {
/* 270 */           localHtmlTree3.addContent(getSpace());
/* 271 */           addInlineDeprecatedComment(this.annotationType, arrayOfTag1[0], localHtmlTree3);
/*     */         }
/*     */       }
/* 274 */       paramContent.addContent(localHtmlTree3);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 282 */     Content localContent = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */ 
/* 284 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 285 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addSummaryDetailLinks(Content paramContent)
/*     */   {
/*     */     try
/*     */     {
/* 295 */       HtmlTree localHtmlTree = HtmlTree.DIV(getNavSummaryLinks());
/* 296 */       localHtmlTree.addContent(getNavDetailLinks());
/* 297 */       paramContent.addContent(localHtmlTree);
/*     */     } catch (Exception localException) {
/* 299 */       localException.printStackTrace();
/* 300 */       throw new DocletAbortException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLinks()
/*     */     throws Exception
/*     */   {
/* 310 */     HtmlTree localHtmlTree1 = HtmlTree.LI(this.summaryLabel);
/* 311 */     localHtmlTree1.addContent(getSpace());
/* 312 */     HtmlTree localHtmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, localHtmlTree1);
/*     */ 
/* 314 */     MemberSummaryBuilder localMemberSummaryBuilder = (MemberSummaryBuilder)this.configuration
/* 314 */       .getBuilderFactory().getMemberSummaryBuilder(this);
/* 315 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.LI);
/* 316 */     addNavSummaryLink(localMemberSummaryBuilder, "doclet.navField", 5, localHtmlTree3);
/*     */ 
/* 319 */     addNavGap(localHtmlTree3);
/* 320 */     localHtmlTree2.addContent(localHtmlTree3);
/* 321 */     HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.LI);
/* 322 */     addNavSummaryLink(localMemberSummaryBuilder, "doclet.navAnnotationTypeRequiredMember", 7, localHtmlTree4);
/*     */ 
/* 325 */     addNavGap(localHtmlTree4);
/* 326 */     localHtmlTree2.addContent(localHtmlTree4);
/* 327 */     HtmlTree localHtmlTree5 = new HtmlTree(HtmlTag.LI);
/* 328 */     addNavSummaryLink(localMemberSummaryBuilder, "doclet.navAnnotationTypeOptionalMember", 6, localHtmlTree5);
/*     */ 
/* 331 */     localHtmlTree2.addContent(localHtmlTree5);
/* 332 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   protected void addNavSummaryLink(MemberSummaryBuilder paramMemberSummaryBuilder, String paramString, int paramInt, Content paramContent)
/*     */   {
/* 346 */     AbstractMemberWriter localAbstractMemberWriter = (AbstractMemberWriter)paramMemberSummaryBuilder
/* 346 */       .getMemberSummaryWriter(paramInt);
/*     */ 
/* 347 */     if (localAbstractMemberWriter == null)
/* 348 */       paramContent.addContent(getResource(paramString));
/*     */     else
/* 350 */       paramContent.addContent(localAbstractMemberWriter.getNavSummaryLink(null, 
/* 351 */         !paramMemberSummaryBuilder
/* 351 */         .getVisibleMemberMap(paramInt)
/* 351 */         .noVisibleMembers()));
/*     */   }
/*     */ 
/*     */   protected Content getNavDetailLinks()
/*     */     throws Exception
/*     */   {
/* 361 */     HtmlTree localHtmlTree1 = HtmlTree.LI(this.detailLabel);
/* 362 */     localHtmlTree1.addContent(getSpace());
/* 363 */     HtmlTree localHtmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, localHtmlTree1);
/*     */ 
/* 365 */     MemberSummaryBuilder localMemberSummaryBuilder = (MemberSummaryBuilder)this.configuration
/* 365 */       .getBuilderFactory().getMemberSummaryBuilder(this);
/*     */ 
/* 368 */     AbstractMemberWriter localAbstractMemberWriter1 = (AbstractMemberWriter)localMemberSummaryBuilder
/* 368 */       .getMemberSummaryWriter(5);
/*     */ 
/* 371 */     AbstractMemberWriter localAbstractMemberWriter2 = (AbstractMemberWriter)localMemberSummaryBuilder
/* 371 */       .getMemberSummaryWriter(6);
/*     */ 
/* 374 */     AbstractMemberWriter localAbstractMemberWriter3 = (AbstractMemberWriter)localMemberSummaryBuilder
/* 374 */       .getMemberSummaryWriter(7);
/*     */ 
/* 375 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.LI);
/* 376 */     if (localAbstractMemberWriter1 != null)
/* 377 */       localAbstractMemberWriter1.addNavDetailLink(this.annotationType.fields().length > 0, localHtmlTree3);
/*     */     else {
/* 379 */       localHtmlTree3.addContent(getResource("doclet.navField"));
/*     */     }
/* 381 */     addNavGap(localHtmlTree3);
/* 382 */     localHtmlTree2.addContent(localHtmlTree3);
/*     */     HtmlTree localHtmlTree4;
/* 383 */     if (localAbstractMemberWriter2 != null) {
/* 384 */       localHtmlTree4 = new HtmlTree(HtmlTag.LI);
/* 385 */       localAbstractMemberWriter2.addNavDetailLink(this.annotationType.elements().length > 0, localHtmlTree4);
/* 386 */       localHtmlTree2.addContent(localHtmlTree4);
/* 387 */     } else if (localAbstractMemberWriter3 != null) {
/* 388 */       localHtmlTree4 = new HtmlTree(HtmlTag.LI);
/* 389 */       localAbstractMemberWriter3.addNavDetailLink(this.annotationType.elements().length > 0, localHtmlTree4);
/* 390 */       localHtmlTree2.addContent(localHtmlTree4);
/*     */     } else {
/* 392 */       localHtmlTree4 = HtmlTree.LI(getResource("doclet.navAnnotationTypeMember"));
/* 393 */       localHtmlTree2.addContent(localHtmlTree4);
/*     */     }
/* 395 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   protected void addNavGap(Content paramContent)
/*     */   {
/* 404 */     paramContent.addContent(getSpace());
/* 405 */     paramContent.addContent("|");
/* 406 */     paramContent.addContent(getSpace());
/*     */   }
/*     */ 
/*     */   public AnnotationTypeDoc getAnnotationTypeDoc()
/*     */   {
/* 413 */     return this.annotationType;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AnnotationTypeWriterImpl
 * JD-Core Version:    0.6.2
 */