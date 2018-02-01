/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter.SerialFieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter.SerialMethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class SerializedFormWriterImpl extends SubWriterHolderWriter
/*     */   implements SerializedFormWriter
/*     */ {
/*     */   public SerializedFormWriterImpl(ConfigurationImpl paramConfigurationImpl)
/*     */     throws IOException
/*     */   {
/*  55 */     super(paramConfigurationImpl, DocPaths.SERIALIZED_FORM);
/*     */   }
/*     */ 
/*     */   public Content getHeader(String paramString)
/*     */   {
/*  65 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(paramString));
/*  66 */     addTop(localHtmlTree1);
/*  67 */     addNavLinks(true, localHtmlTree1);
/*  68 */     StringContent localStringContent = new StringContent(paramString);
/*  69 */     HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, localStringContent);
/*     */ 
/*  71 */     HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree2);
/*  72 */     localHtmlTree1.addContent(localHtmlTree3);
/*  73 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getSerializedSummariesHeader()
/*     */   {
/*  82 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/*  83 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/*  84 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getPackageSerializedHeader()
/*     */   {
/*  93 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/*  94 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/*  95 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getPackageHeader(String paramString)
/*     */   {
/* 105 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.PACKAGE_HEADING, true, this.packageLabel);
/*     */ 
/* 107 */     localHtmlTree.addContent(getSpace());
/* 108 */     localHtmlTree.addContent(paramString);
/* 109 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getClassSerializedHeader()
/*     */   {
/* 118 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 119 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 120 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getClassHeader(ClassDoc paramClassDoc)
/*     */   {
/* 133 */     StringContent localStringContent = (paramClassDoc.isPublic()) || (paramClassDoc.isProtected()) ? 
/* 131 */       getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.DEFAULT, paramClassDoc)
/* 132 */       .label(this.configuration
/* 132 */       .getClassName(paramClassDoc))) : 
/* 131 */       new StringContent(paramClassDoc
/* 133 */       .qualifiedName());
/* 134 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.blockList, getMarkerAnchor(paramClassDoc
/* 135 */       .qualifiedName()));
/*     */ 
/* 138 */     Object localObject = paramClassDoc
/* 137 */       .superclassType() != null ? 
/* 138 */       getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SERIALIZED_FORM, paramClassDoc
/* 140 */       .superclassType())) : null;
/*     */ 
/* 147 */     Content localContent = localObject == null ? this.configuration
/* 145 */       .getResource("doclet.Class_0_implements_serializable", localStringContent) : 
/* 145 */       this.configuration
/* 147 */       .getResource("doclet.Class_0_extends_implements_serializable", localStringContent, localObject);
/*     */ 
/* 150 */     localHtmlTree.addContent(HtmlTree.HEADING(HtmlConstants.SERIALIZED_MEMBER_HEADING, localContent));
/*     */ 
/* 152 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getSerialUIDInfoHeader()
/*     */   {
/* 161 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DL);
/* 162 */     localHtmlTree.addStyle(HtmlStyle.nameValue);
/* 163 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addSerialUIDInfo(String paramString1, String paramString2, Content paramContent)
/*     */   {
/* 176 */     StringContent localStringContent1 = new StringContent(paramString1);
/* 177 */     paramContent.addContent(HtmlTree.DT(localStringContent1));
/* 178 */     StringContent localStringContent2 = new StringContent(paramString2);
/* 179 */     paramContent.addContent(HtmlTree.DD(localStringContent2));
/*     */   }
/*     */ 
/*     */   public Content getClassContentHeader()
/*     */   {
/* 188 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 189 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 190 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getSerializedContent(Content paramContent)
/*     */   {
/* 200 */     HtmlTree localHtmlTree = HtmlTree.DIV(HtmlStyle.serializedFormContainer, paramContent);
/*     */ 
/* 202 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addFooter(Content paramContent)
/*     */   {
/* 211 */     addNavLinks(false, paramContent);
/* 212 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 219 */     printHtmlDocument(null, true, paramContent);
/*     */   }
/*     */ 
/*     */   public SerializedFormWriter.SerialFieldWriter getSerialFieldWriter(ClassDoc paramClassDoc)
/*     */   {
/* 228 */     return new HtmlSerialFieldWriter(this, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public SerializedFormWriter.SerialMethodWriter getSerialMethodWriter(ClassDoc paramClassDoc)
/*     */   {
/* 237 */     return new HtmlSerialMethodWriter(this, paramClassDoc);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.SerializedFormWriterImpl
 * JD-Core Version:    0.6.2
 */