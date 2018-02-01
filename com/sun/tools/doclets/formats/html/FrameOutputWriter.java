/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class FrameOutputWriter extends HtmlDocletWriter
/*     */ {
/*     */   int noOfPackages;
/*  58 */   private final String SCROLL_YES = "yes";
/*     */ 
/*     */   public FrameOutputWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  67 */     super(paramConfigurationImpl, paramDocPath);
/*  68 */     this.noOfPackages = paramConfigurationImpl.packages.length;
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl)
/*     */   {
/*  80 */     DocPath localDocPath = DocPath.empty;
/*     */     try {
/*  82 */       localDocPath = DocPaths.INDEX;
/*  83 */       FrameOutputWriter localFrameOutputWriter = new FrameOutputWriter(paramConfigurationImpl, localDocPath);
/*  84 */       localFrameOutputWriter.generateFrameFile();
/*  85 */       localFrameOutputWriter.close();
/*     */     } catch (IOException localIOException) {
/*  87 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  89 */         .toString(), localDocPath });
/*  90 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateFrameFile()
/*     */     throws IOException
/*     */   {
/*  99 */     Content localContent = getFrameDetails();
/* 100 */     if (this.configuration.windowtitle.length() > 0) {
/* 101 */       printFramesetDocument(this.configuration.windowtitle, this.configuration.notimestamp, localContent);
/*     */     }
/*     */     else
/* 104 */       printFramesetDocument(this.configuration.getText("doclet.Generated_Docs_Untitled"), this.configuration.notimestamp, localContent);
/*     */   }
/*     */ 
/*     */   protected void addFrameWarning(Content paramContent)
/*     */   {
/* 116 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.NOFRAMES);
/* 117 */     HtmlTree localHtmlTree2 = HtmlTree.NOSCRIPT(
/* 118 */       HtmlTree.DIV(getResource("doclet.No_Script_Message")));
/*     */ 
/* 119 */     localHtmlTree1.addContent(localHtmlTree2);
/* 120 */     HtmlTree localHtmlTree3 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 121 */       getResource("doclet.Frame_Alert"));
/*     */ 
/* 122 */     localHtmlTree1.addContent(localHtmlTree3);
/* 123 */     HtmlTree localHtmlTree4 = HtmlTree.P(getResource("doclet.Frame_Warning_Message", 
/* 124 */       getHyperLink(this.configuration.topFile, this.configuration
/* 125 */       .getText("doclet.Non_Frame_Version"))));
/*     */ 
/* 126 */     localHtmlTree1.addContent(localHtmlTree4);
/* 127 */     paramContent.addContent(localHtmlTree1);
/*     */   }
/*     */ 
/*     */   protected Content getFrameDetails()
/*     */   {
/* 136 */     HtmlTree localHtmlTree1 = HtmlTree.FRAMESET("20%,80%", null, "Documentation frame", "top.loadFrames()");
/*     */ 
/* 138 */     if (this.noOfPackages <= 1) {
/* 139 */       addAllClassesFrameTag(localHtmlTree1);
/* 140 */     } else if (this.noOfPackages > 1) {
/* 141 */       HtmlTree localHtmlTree2 = HtmlTree.FRAMESET(null, "30%,70%", "Left frames", "top.loadFrames()");
/*     */ 
/* 143 */       addAllPackagesFrameTag(localHtmlTree2);
/* 144 */       addAllClassesFrameTag(localHtmlTree2);
/* 145 */       localHtmlTree1.addContent(localHtmlTree2);
/*     */     }
/* 147 */     addClassFrameTag(localHtmlTree1);
/* 148 */     addFrameWarning(localHtmlTree1);
/* 149 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   private void addAllPackagesFrameTag(Content paramContent)
/*     */   {
/* 158 */     HtmlTree localHtmlTree = HtmlTree.FRAME(DocPaths.OVERVIEW_FRAME.getPath(), "packageListFrame", this.configuration
/* 159 */       .getText("doclet.All_Packages"));
/*     */ 
/* 160 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   private void addAllClassesFrameTag(Content paramContent)
/*     */   {
/* 169 */     HtmlTree localHtmlTree = HtmlTree.FRAME(DocPaths.ALLCLASSES_FRAME.getPath(), "packageFrame", this.configuration
/* 170 */       .getText("doclet.All_classes_and_interfaces"));
/*     */ 
/* 171 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   private void addClassFrameTag(Content paramContent)
/*     */   {
/* 180 */     HtmlTree localHtmlTree = HtmlTree.FRAME(this.configuration.topFile.getPath(), "classFrame", this.configuration
/* 181 */       .getText("doclet.Package_class_and_interface_descriptions"), 
/* 181 */       "yes");
/*     */ 
/* 183 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.FrameOutputWriter
 * JD-Core Version:    0.6.2
 */