/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AllClassesFrameWriter extends HtmlDocletWriter
/*     */ {
/*     */   protected IndexBuilder indexbuilder;
/*  61 */   final HtmlTree BR = new HtmlTree(HtmlTag.BR);
/*     */ 
/*     */   public AllClassesFrameWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder)
/*     */     throws IOException
/*     */   {
/*  75 */     super(paramConfigurationImpl, paramDocPath);
/*  76 */     this.indexbuilder = paramIndexBuilder;
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, IndexBuilder paramIndexBuilder)
/*     */   {
/*  90 */     DocPath localDocPath = DocPaths.ALLCLASSES_FRAME;
/*     */     try {
/*  92 */       AllClassesFrameWriter localAllClassesFrameWriter = new AllClassesFrameWriter(paramConfigurationImpl, localDocPath, paramIndexBuilder);
/*     */ 
/*  94 */       localAllClassesFrameWriter.buildAllClassesFile(true);
/*  95 */       localAllClassesFrameWriter.close();
/*  96 */       localDocPath = DocPaths.ALLCLASSES_NOFRAME;
/*  97 */       localAllClassesFrameWriter = new AllClassesFrameWriter(paramConfigurationImpl, localDocPath, paramIndexBuilder);
/*     */ 
/*  99 */       localAllClassesFrameWriter.buildAllClassesFile(false);
/* 100 */       localAllClassesFrameWriter.close();
/*     */     } catch (IOException localIOException) {
/* 102 */       paramConfigurationImpl.standardmessage
/* 103 */         .error("doclet.exception_encountered", new Object[] { localIOException
/* 104 */         .toString(), localDocPath });
/* 105 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void buildAllClassesFile(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 114 */     String str = this.configuration.getText("doclet.All_Classes");
/* 115 */     HtmlTree localHtmlTree1 = getBody(false, getWindowTitle(str));
/* 116 */     HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.bar, this.allclassesLabel);
/*     */ 
/* 118 */     localHtmlTree1.addContent(localHtmlTree2);
/* 119 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.UL);
/*     */ 
/* 121 */     addAllClasses(localHtmlTree3, paramBoolean);
/* 122 */     HtmlTree localHtmlTree4 = HtmlTree.DIV(HtmlStyle.indexContainer, localHtmlTree3);
/* 123 */     localHtmlTree1.addContent(localHtmlTree4);
/* 124 */     printHtmlDocument(null, false, localHtmlTree1);
/*     */   }
/*     */ 
/*     */   protected void addAllClasses(Content paramContent, boolean paramBoolean)
/*     */   {
/* 135 */     for (int i = 0; i < this.indexbuilder.elements().length; i++) {
/* 136 */       Character localCharacter = (Character)this.indexbuilder.elements()[i];
/* 137 */       addContents(this.indexbuilder.getMemberList(localCharacter), paramBoolean, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addContents(List<Doc> paramList, boolean paramBoolean, Content paramContent)
/*     */   {
/* 154 */     for (int i = 0; i < paramList.size(); i++) {
/* 155 */       ClassDoc localClassDoc = (ClassDoc)paramList.get(i);
/* 156 */       if (Util.isCoreClass(localClassDoc))
/*     */       {
/* 159 */         Content localContent1 = italicsClassName(localClassDoc, false);
/*     */         Content localContent2;
/* 161 */         if (paramBoolean)
/* 162 */           localContent2 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.ALL_CLASSES_FRAME, localClassDoc)
/* 163 */             .label(localContent1)
/* 163 */             .target("classFrame"));
/*     */         else {
/* 165 */           localContent2 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.DEFAULT, localClassDoc).label(localContent1));
/*     */         }
/* 167 */         HtmlTree localHtmlTree = HtmlTree.LI(localContent2);
/* 168 */         paramContent.addContent(localHtmlTree);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AllClassesFrameWriter
 * JD-Core Version:    0.6.2
 */