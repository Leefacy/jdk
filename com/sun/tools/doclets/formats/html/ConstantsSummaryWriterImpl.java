/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class ConstantsSummaryWriterImpl extends HtmlDocletWriter
/*     */   implements ConstantsSummaryWriter
/*     */ {
/*     */   ConfigurationImpl configuration;
/*     */   private ClassDoc currentClassDoc;
/*     */   private final String constantsTableSummary;
/*     */   private final String[] constantsTableHeader;
/*     */ 
/*     */   public ConstantsSummaryWriterImpl(ConfigurationImpl paramConfigurationImpl)
/*     */     throws IOException
/*     */   {
/*  72 */     super(paramConfigurationImpl, DocPaths.CONSTANT_VALUES);
/*  73 */     this.configuration = paramConfigurationImpl;
/*  74 */     this.constantsTableSummary = paramConfigurationImpl.getText("doclet.Constants_Table_Summary", paramConfigurationImpl
/*  75 */       .getText("doclet.Constants_Summary"));
/*     */ 
/*  76 */     this.constantsTableHeader = new String[] { 
/*  77 */       getModifierTypeHeader(), paramConfigurationImpl
/*  78 */       .getText("doclet.ConstantField"), 
/*  78 */       paramConfigurationImpl
/*  79 */       .getText("doclet.Value") };
/*     */   }
/*     */ 
/*     */   public Content getHeader()
/*     */   {
/*  87 */     String str = this.configuration.getText("doclet.Constants_Summary");
/*  88 */     HtmlTree localHtmlTree = getBody(true, getWindowTitle(str));
/*  89 */     addTop(localHtmlTree);
/*  90 */     addNavLinks(true, localHtmlTree);
/*  91 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getContentsHeader()
/*     */   {
/*  98 */     return new HtmlTree(HtmlTag.UL);
/*     */   }
/*     */ 
/*     */   public void addLinkToPackageContent(PackageDoc paramPackageDoc, String paramString, Set<String> paramSet, Content paramContent)
/*     */   {
/* 106 */     String str = paramPackageDoc.name();
/*     */     Content localContent1;
/* 109 */     if (str.length() == 0) {
/* 110 */       localContent1 = getHyperLink(getDocLink(SectionName.UNNAMED_PACKAGE_ANCHOR), this.defaultPackageLabel, "", "");
/*     */     }
/*     */     else
/*     */     {
/* 114 */       Content localContent2 = getPackageLabel(paramString);
/* 115 */       localContent2.addContent(".*");
/* 116 */       localContent1 = getHyperLink(DocLink.fragment(paramString), localContent2, "", "");
/*     */ 
/* 118 */       paramSet.add(paramString);
/*     */     }
/* 120 */     paramContent.addContent(HtmlTree.LI(localContent1));
/*     */   }
/*     */ 
/*     */   public Content getContentsList(Content paramContent)
/*     */   {
/* 127 */     Content localContent1 = getResource("doclet.Constants_Summary");
/*     */ 
/* 129 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, localContent1);
/*     */ 
/* 131 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 132 */     Content localContent2 = getResource("doclet.Contents");
/*     */ 
/* 134 */     localHtmlTree2.addContent(HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, localContent2));
/*     */ 
/* 136 */     localHtmlTree2.addContent(paramContent);
/* 137 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   public Content getConstantSummaries()
/*     */   {
/* 144 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 145 */     localHtmlTree.addStyle(HtmlStyle.constantValuesContainer);
/* 146 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addPackageName(PackageDoc paramPackageDoc, String paramString, Content paramContent)
/*     */   {
/*     */     Content localContent;
/* 155 */     if (paramString.length() == 0) {
/* 156 */       paramContent.addContent(getMarkerAnchor(SectionName.UNNAMED_PACKAGE_ANCHOR));
/*     */ 
/* 158 */       localContent = this.defaultPackageLabel;
/*     */     } else {
/* 160 */       paramContent.addContent(getMarkerAnchor(paramString));
/*     */ 
/* 162 */       localContent = getPackageLabel(paramString);
/*     */     }
/* 164 */     StringContent localStringContent = new StringContent(".*");
/* 165 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, localContent);
/*     */ 
/* 167 */     localHtmlTree.addContent(localStringContent);
/* 168 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public Content getClassConstantHeader()
/*     */   {
/* 175 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 176 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 177 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getConstantMembersHeader(ClassDoc paramClassDoc)
/*     */   {
/* 191 */     StringContent localStringContent = (paramClassDoc.isPublic()) || (paramClassDoc.isProtected()) ? 
/* 189 */       getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CONSTANT_SUMMARY, paramClassDoc)) : 
/* 189 */       new StringContent(paramClassDoc
/* 191 */       .qualifiedName());
/* 192 */     String str = paramClassDoc.containingPackage().name();
/* 193 */     if (str.length() > 0) {
/* 194 */       ContentBuilder localContentBuilder = new ContentBuilder();
/* 195 */       localContentBuilder.addContent(str);
/* 196 */       localContentBuilder.addContent(".");
/* 197 */       localContentBuilder.addContent(localStringContent);
/* 198 */       return getClassName(localContentBuilder);
/*     */     }
/* 200 */     return getClassName(localStringContent);
/*     */   }
/*     */ 
/*     */   protected Content getClassName(Content paramContent)
/*     */   {
/* 211 */     HtmlTree localHtmlTree = HtmlTree.TABLE(HtmlStyle.constantsSummary, 0, 3, 0, this.constantsTableSummary, 
/* 212 */       getTableCaption(paramContent));
/*     */ 
/* 213 */     localHtmlTree.addContent(getSummaryTableHeader(this.constantsTableHeader, "col"));
/* 214 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addConstantMembers(ClassDoc paramClassDoc, List<FieldDoc> paramList, Content paramContent)
/*     */   {
/* 222 */     this.currentClassDoc = paramClassDoc;
/* 223 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.TBODY);
/* 224 */     for (int i = 0; i < paramList.size(); i++) {
/* 225 */       localHtmlTree2 = new HtmlTree(HtmlTag.TR);
/* 226 */       if (i % 2 == 0)
/* 227 */         localHtmlTree2.addStyle(HtmlStyle.altColor);
/*     */       else
/* 229 */         localHtmlTree2.addStyle(HtmlStyle.rowColor);
/* 230 */       addConstantMember((FieldDoc)paramList.get(i), localHtmlTree2);
/* 231 */       localHtmlTree1.addContent(localHtmlTree2);
/*     */     }
/* 233 */     Content localContent = getConstantMembersHeader(paramClassDoc);
/* 234 */     localContent.addContent(localHtmlTree1);
/* 235 */     HtmlTree localHtmlTree2 = HtmlTree.LI(HtmlStyle.blockList, localContent);
/* 236 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   private void addConstantMember(FieldDoc paramFieldDoc, HtmlTree paramHtmlTree)
/*     */   {
/* 246 */     paramHtmlTree.addContent(getTypeColumn(paramFieldDoc));
/* 247 */     paramHtmlTree.addContent(getNameColumn(paramFieldDoc));
/* 248 */     paramHtmlTree.addContent(getValue(paramFieldDoc));
/*     */   }
/*     */ 
/*     */   private Content getTypeColumn(FieldDoc paramFieldDoc)
/*     */   {
/* 258 */     Content localContent = getMarkerAnchor(this.currentClassDoc.qualifiedName() + "." + paramFieldDoc
/* 259 */       .name());
/* 260 */     HtmlTree localHtmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, localContent);
/* 261 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.CODE);
/* 262 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramFieldDoc.modifiers());
/* 263 */     while (localStringTokenizer.hasMoreTokens()) {
/* 264 */       localObject = new StringContent(localStringTokenizer.nextToken());
/* 265 */       localHtmlTree2.addContent((Content)localObject);
/* 266 */       localHtmlTree2.addContent(getSpace());
/*     */     }
/* 268 */     Object localObject = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CONSTANT_SUMMARY, paramFieldDoc
/* 269 */       .type()));
/* 270 */     localHtmlTree2.addContent((Content)localObject);
/* 271 */     localHtmlTree1.addContent(localHtmlTree2);
/* 272 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   private Content getNameColumn(FieldDoc paramFieldDoc)
/*     */   {
/* 282 */     Content localContent = getDocLink(LinkInfoImpl.Kind.CONSTANT_SUMMARY, paramFieldDoc, paramFieldDoc
/* 283 */       .name(), false);
/* 284 */     HtmlTree localHtmlTree = HtmlTree.CODE(localContent);
/* 285 */     return HtmlTree.TD(localHtmlTree);
/*     */   }
/*     */ 
/*     */   private Content getValue(FieldDoc paramFieldDoc)
/*     */   {
/* 295 */     StringContent localStringContent = new StringContent(paramFieldDoc.constantValueExpression());
/* 296 */     HtmlTree localHtmlTree = HtmlTree.CODE(localStringContent);
/* 297 */     return HtmlTree.TD(HtmlStyle.colLast, localHtmlTree);
/*     */   }
/*     */ 
/*     */   public void addFooter(Content paramContent)
/*     */   {
/* 304 */     addNavLinks(false, paramContent);
/* 305 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 312 */     printHtmlDocument(null, true, paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ConstantsSummaryWriterImpl
 * JD-Core Version:    0.6.2
 */