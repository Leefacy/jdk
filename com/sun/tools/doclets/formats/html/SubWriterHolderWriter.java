/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MethodTypes;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class SubWriterHolderWriter extends HtmlDocletWriter
/*     */ {
/*     */   public SubWriterHolderWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  60 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */   public void addSummaryHeader(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  72 */     paramAbstractMemberWriter.addSummaryAnchor(paramClassDoc, paramContent);
/*  73 */     paramAbstractMemberWriter.addSummaryLabel(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getSummaryTableTree(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, List<Content> paramList, boolean paramBoolean)
/*     */   {
/*     */     Content localContent;
/*  88 */     if (paramBoolean) {
/*  89 */       localContent = getTableCaption(paramAbstractMemberWriter.methodTypes);
/*  90 */       generateMethodTypesScript(paramAbstractMemberWriter.typeMap, paramAbstractMemberWriter.methodTypes);
/*     */     }
/*     */     else {
/*  93 */       localContent = getTableCaption(paramAbstractMemberWriter.getCaption());
/*     */     }
/*  95 */     HtmlTree localHtmlTree = HtmlTree.TABLE(HtmlStyle.memberSummary, 0, 3, 0, paramAbstractMemberWriter
/*  96 */       .getTableSummary(), localContent);
/*  97 */     localHtmlTree.addContent(getSummaryTableHeader(paramAbstractMemberWriter.getSummaryTableHeader(paramClassDoc), "col"));
/*  98 */     for (int i = 0; i < paramList.size(); i++) {
/*  99 */       localHtmlTree.addContent((Content)paramList.get(i));
/*     */     }
/* 101 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getTableCaption(Set<MethodTypes> paramSet)
/*     */   {
/* 111 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.CAPTION);
/* 112 */     for (MethodTypes localMethodTypes : paramSet)
/*     */     {
/*     */       HtmlTree localHtmlTree2;
/*     */       HtmlTree localHtmlTree3;
/* 115 */       if (localMethodTypes.isDefaultTab()) {
/* 116 */         localHtmlTree2 = HtmlTree.SPAN(this.configuration.getResource(localMethodTypes.resourceKey()));
/* 117 */         localHtmlTree3 = HtmlTree.SPAN(localMethodTypes.tabId(), HtmlStyle.activeTableTab, localHtmlTree2);
/*     */       }
/*     */       else {
/* 120 */         localHtmlTree2 = HtmlTree.SPAN(getMethodTypeLinks(localMethodTypes));
/* 121 */         localHtmlTree3 = HtmlTree.SPAN(localMethodTypes.tabId(), HtmlStyle.tableTab, localHtmlTree2);
/*     */       }
/*     */ 
/* 124 */       HtmlTree localHtmlTree4 = HtmlTree.SPAN(HtmlStyle.tabEnd, getSpace());
/* 125 */       localHtmlTree3.addContent(localHtmlTree4);
/* 126 */       localHtmlTree1.addContent(localHtmlTree3);
/*     */     }
/* 128 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getMethodTypeLinks(MethodTypes paramMethodTypes)
/*     */   {
/* 138 */     String str = "javascript:show(" + paramMethodTypes.value() + ");";
/* 139 */     HtmlTree localHtmlTree = HtmlTree.A(str, this.configuration.getResource(paramMethodTypes.resourceKey()));
/* 140 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryHeader(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 152 */     paramAbstractMemberWriter.addInheritedSummaryAnchor(paramClassDoc, paramContent);
/* 153 */     paramAbstractMemberWriter.addInheritedSummaryLabel(paramClassDoc, paramContent);
/*     */   }
/*     */ 
/*     */   protected void addIndexComment(Doc paramDoc, Content paramContent)
/*     */   {
/* 163 */     addIndexComment(paramDoc, paramDoc.firstSentenceTags(), paramContent);
/*     */   }
/*     */ 
/*     */   protected void addIndexComment(Doc paramDoc, Tag[] paramArrayOfTag, Content paramContent)
/*     */   {
/* 175 */     Tag[] arrayOfTag = paramDoc.tags("deprecated");
/*     */     HtmlTree localHtmlTree1;
/* 177 */     if (Util.isDeprecated((ProgramElementDoc)paramDoc)) {
/* 178 */       localObject = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 179 */       localHtmlTree1 = HtmlTree.DIV(HtmlStyle.block, (Content)localObject);
/* 180 */       localHtmlTree1.addContent(getSpace());
/* 181 */       if (arrayOfTag.length > 0) {
/* 182 */         addInlineDeprecatedComment(paramDoc, arrayOfTag[0], localHtmlTree1);
/*     */       }
/* 184 */       paramContent.addContent(localHtmlTree1);
/* 185 */       return;
/*     */     }
/* 187 */     Object localObject = ((ProgramElementDoc)paramDoc).containingClass();
/* 188 */     if ((localObject != null) && (Util.isDeprecated((Doc)localObject))) {
/* 189 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 190 */       localHtmlTree1 = HtmlTree.DIV(HtmlStyle.block, localHtmlTree2);
/* 191 */       localHtmlTree1.addContent(getSpace());
/* 192 */       paramContent.addContent(localHtmlTree1);
/*     */     }
/*     */ 
/* 195 */     addSummaryComment(paramDoc, paramArrayOfTag, paramContent);
/*     */   }
/*     */ 
/*     */   public void addSummaryType(AbstractMemberWriter paramAbstractMemberWriter, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 207 */     paramAbstractMemberWriter.addSummaryType(paramProgramElementDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addSummaryLinkComment(AbstractMemberWriter paramAbstractMemberWriter, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 219 */     addSummaryLinkComment(paramAbstractMemberWriter, paramProgramElementDoc, paramProgramElementDoc.firstSentenceTags(), paramContent);
/*     */   }
/*     */ 
/*     */   public void addSummaryLinkComment(AbstractMemberWriter paramAbstractMemberWriter, ProgramElementDoc paramProgramElementDoc, Tag[] paramArrayOfTag, Content paramContent)
/*     */   {
/* 232 */     addIndexComment(paramProgramElementDoc, paramArrayOfTag, paramContent);
/*     */   }
/*     */ 
/*     */   public void addInheritedMemberSummary(AbstractMemberWriter paramAbstractMemberWriter, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, boolean paramBoolean, Content paramContent)
/*     */   {
/* 246 */     if (!paramBoolean) {
/* 247 */       paramContent.addContent(", ");
/*     */     }
/* 249 */     paramAbstractMemberWriter.addInheritedSummaryLink(paramClassDoc, paramProgramElementDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getContentHeader()
/*     */   {
/* 258 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 259 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 260 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getMemberTreeHeader()
/*     */   {
/* 269 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/* 270 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 271 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getMemberTree(Content paramContent)
/*     */   {
/* 281 */     HtmlTree localHtmlTree = HtmlTree.UL(HtmlStyle.blockList, paramContent);
/* 282 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryTree(Content paramContent)
/*     */   {
/* 292 */     return getMemberTree(HtmlStyle.summary, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getMemberDetailsTree(Content paramContent)
/*     */   {
/* 302 */     return getMemberTree(HtmlStyle.details, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getMemberTree(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 312 */     HtmlTree localHtmlTree = HtmlTree.DIV(paramHtmlStyle, getMemberTree(paramContent));
/* 313 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.SubWriterHolderWriter
 * JD-Core Version:    0.6.2
 */