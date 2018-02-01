/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class SplitIndexWriter extends AbstractIndexWriter
/*     */ {
/*     */   protected int prev;
/*     */   protected int next;
/*     */ 
/*     */   public SplitIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  71 */     super(paramConfigurationImpl, paramDocPath, paramIndexBuilder);
/*  72 */     this.prev = paramInt1;
/*  73 */     this.next = paramInt2;
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, IndexBuilder paramIndexBuilder)
/*     */   {
/*  86 */     DocPath localDocPath1 = DocPath.empty;
/*  87 */     DocPath localDocPath2 = DocPaths.INDEX_FILES;
/*     */     try {
/*  89 */       for (int i = 0; i < paramIndexBuilder.elements().length; i++) {
/*  90 */         int j = i + 1;
/*  91 */         int k = j == 1 ? -1 : i;
/*  92 */         int m = j == paramIndexBuilder.elements().length ? -1 : j + 1;
/*  93 */         localDocPath1 = DocPaths.indexN(j);
/*     */ 
/*  95 */         SplitIndexWriter localSplitIndexWriter = new SplitIndexWriter(paramConfigurationImpl, localDocPath2
/*  95 */           .resolve(localDocPath1), 
/*  95 */           paramIndexBuilder, k, m);
/*     */ 
/*  97 */         localSplitIndexWriter.generateIndexFile(
/*  98 */           (Character)paramIndexBuilder
/*  98 */           .elements()[i]);
/*  99 */         localSplitIndexWriter.close();
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 102 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 104 */         .toString(), localDocPath1.getPath() });
/* 105 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateIndexFile(Character paramCharacter)
/*     */     throws IOException
/*     */   {
/* 117 */     String str = this.configuration.getText("doclet.Window_Split_Index", paramCharacter
/* 118 */       .toString());
/* 119 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str));
/* 120 */     addTop(localHtmlTree1);
/* 121 */     addNavLinks(true, localHtmlTree1);
/* 122 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 123 */     localHtmlTree2.addStyle(HtmlStyle.contentContainer);
/* 124 */     addLinksForIndexes(localHtmlTree2);
/* 125 */     addContents(paramCharacter, this.indexbuilder.getMemberList(paramCharacter), localHtmlTree2);
/* 126 */     addLinksForIndexes(localHtmlTree2);
/* 127 */     localHtmlTree1.addContent(localHtmlTree2);
/* 128 */     addNavLinks(false, localHtmlTree1);
/* 129 */     addBottom(localHtmlTree1);
/* 130 */     printHtmlDocument(null, true, localHtmlTree1);
/*     */   }
/*     */ 
/*     */   protected void addLinksForIndexes(Content paramContent)
/*     */   {
/* 139 */     Object[] arrayOfObject = this.indexbuilder.elements();
/* 140 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 141 */       int j = i + 1;
/* 142 */       paramContent.addContent(getHyperLink(DocPaths.indexN(j), new StringContent(arrayOfObject[i]
/* 143 */         .toString())));
/* 144 */       paramContent.addContent(getSpace());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Content getNavLinkPrevious()
/*     */   {
/* 154 */     Content localContent1 = getResource("doclet.Prev_Letter");
/* 155 */     if (this.prev == -1) {
/* 156 */       return HtmlTree.LI(localContent1);
/*     */     }
/*     */ 
/* 159 */     Content localContent2 = getHyperLink(DocPaths.indexN(this.prev), localContent1);
/*     */ 
/* 161 */     return HtmlTree.LI(localContent2);
/*     */   }
/*     */ 
/*     */   public Content getNavLinkNext()
/*     */   {
/* 171 */     Content localContent1 = getResource("doclet.Next_Letter");
/* 172 */     if (this.next == -1) {
/* 173 */       return HtmlTree.LI(localContent1);
/*     */     }
/*     */ 
/* 176 */     Content localContent2 = getHyperLink(DocPaths.indexN(this.next), localContent1);
/*     */ 
/* 178 */     return HtmlTree.LI(localContent2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.SplitIndexWriter
 * JD-Core Version:    0.6.2
 */