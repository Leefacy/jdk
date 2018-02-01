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
/*     */ public class SingleIndexWriter extends AbstractIndexWriter
/*     */ {
/*     */   public SingleIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder)
/*     */     throws IOException
/*     */   {
/*  60 */     super(paramConfigurationImpl, paramDocPath, paramIndexBuilder);
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, IndexBuilder paramIndexBuilder)
/*     */   {
/*  72 */     DocPath localDocPath = DocPaths.INDEX_ALL;
/*     */     try {
/*  74 */       SingleIndexWriter localSingleIndexWriter = new SingleIndexWriter(paramConfigurationImpl, localDocPath, paramIndexBuilder);
/*     */ 
/*  76 */       localSingleIndexWriter.generateIndexFile();
/*  77 */       localSingleIndexWriter.close();
/*     */     } catch (IOException localIOException) {
/*  79 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  81 */         .toString(), localDocPath });
/*  82 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateIndexFile()
/*     */     throws IOException
/*     */   {
/*  91 */     String str = this.configuration.getText("doclet.Window_Single_Index");
/*  92 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str));
/*  93 */     addTop(localHtmlTree1);
/*  94 */     addNavLinks(true, localHtmlTree1);
/*  95 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/*  96 */     localHtmlTree2.addStyle(HtmlStyle.contentContainer);
/*  97 */     addLinksForIndexes(localHtmlTree2);
/*  98 */     for (int i = 0; i < this.indexbuilder.elements().length; i++) {
/*  99 */       Character localCharacter = (Character)this.indexbuilder.elements()[i];
/* 100 */       addContents(localCharacter, this.indexbuilder.getMemberList(localCharacter), localHtmlTree2);
/*     */     }
/* 102 */     addLinksForIndexes(localHtmlTree2);
/* 103 */     localHtmlTree1.addContent(localHtmlTree2);
/* 104 */     addNavLinks(false, localHtmlTree1);
/* 105 */     addBottom(localHtmlTree1);
/* 106 */     printHtmlDocument(null, true, localHtmlTree1);
/*     */   }
/*     */ 
/*     */   protected void addLinksForIndexes(Content paramContent)
/*     */   {
/* 115 */     for (int i = 0; i < this.indexbuilder.elements().length; i++) {
/* 116 */       String str = this.indexbuilder.elements()[i].toString();
/* 117 */       paramContent.addContent(
/* 118 */         getHyperLink(getNameForIndex(str), 
/* 118 */         new StringContent(str)));
/*     */ 
/* 120 */       paramContent.addContent(getSpace());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.SingleIndexWriter
 * JD-Core Version:    0.6.2
 */