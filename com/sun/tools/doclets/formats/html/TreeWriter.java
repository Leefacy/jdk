/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class TreeWriter extends AbstractTreeWriter
/*     */ {
/*     */   private PackageDoc[] packages;
/*     */   private boolean classesonly;
/*     */ 
/*     */   public TreeWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, ClassTree paramClassTree)
/*     */     throws IOException
/*     */   {
/*  72 */     super(paramConfigurationImpl, paramDocPath, paramClassTree);
/*  73 */     this.packages = paramConfigurationImpl.packages;
/*  74 */     this.classesonly = (this.packages.length == 0);
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassTree paramClassTree)
/*     */   {
/*  87 */     DocPath localDocPath = DocPaths.OVERVIEW_TREE;
/*     */     try {
/*  89 */       TreeWriter localTreeWriter = new TreeWriter(paramConfigurationImpl, localDocPath, paramClassTree);
/*  90 */       localTreeWriter.generateTreeFile();
/*  91 */       localTreeWriter.close();
/*     */     } catch (IOException localIOException) {
/*  93 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  95 */         .toString(), localDocPath });
/*  96 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void generateTreeFile()
/*     */     throws IOException
/*     */   {
/* 104 */     Content localContent1 = getTreeHeader();
/* 105 */     Content localContent2 = getResource("doclet.Hierarchy_For_All_Packages");
/* 106 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, false, HtmlStyle.title, localContent2);
/*     */ 
/* 108 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 109 */     addPackageTreeLinks(localHtmlTree2);
/* 110 */     localContent1.addContent(localHtmlTree2);
/* 111 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 112 */     localHtmlTree3.addStyle(HtmlStyle.contentContainer);
/* 113 */     addTree(this.classtree.baseclasses(), "doclet.Class_Hierarchy", localHtmlTree3);
/* 114 */     addTree(this.classtree.baseinterfaces(), "doclet.Interface_Hierarchy", localHtmlTree3);
/* 115 */     addTree(this.classtree.baseAnnotationTypes(), "doclet.Annotation_Type_Hierarchy", localHtmlTree3);
/* 116 */     addTree(this.classtree.baseEnums(), "doclet.Enum_Hierarchy", localHtmlTree3);
/* 117 */     localContent1.addContent(localHtmlTree3);
/* 118 */     addNavLinks(false, localContent1);
/* 119 */     addBottom(localContent1);
/* 120 */     printHtmlDocument(null, true, localContent1);
/*     */   }
/*     */ 
/*     */   protected void addPackageTreeLinks(Content paramContent)
/*     */   {
/* 130 */     if ((this.packages.length == 1) && (this.packages[0].name().length() == 0)) {
/* 131 */       return;
/*     */     }
/* 133 */     if (!this.classesonly) {
/* 134 */       HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.packageHierarchyLabel, 
/* 135 */         getResource("doclet.Package_Hierarchies"));
/*     */ 
/* 136 */       paramContent.addContent(localHtmlTree1);
/* 137 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.UL);
/* 138 */       localHtmlTree2.addStyle(HtmlStyle.horizontal);
/* 139 */       for (int i = 0; i < this.packages.length; i++)
/*     */       {
/* 143 */         if ((this.packages[i].name().length() != 0) && ((!this.configuration.nodeprecated) || 
/* 144 */           (!Util.isDeprecated(this.packages[i]))))
/*     */         {
/* 147 */           DocPath localDocPath = pathString(this.packages[i], DocPaths.PACKAGE_TREE);
/* 148 */           HtmlTree localHtmlTree3 = HtmlTree.LI(getHyperLink(localDocPath, new StringContent(this.packages[i]
/* 149 */             .name())));
/* 150 */           if (i < this.packages.length - 1) {
/* 151 */             localHtmlTree3.addContent(", ");
/*     */           }
/* 153 */           localHtmlTree2.addContent(localHtmlTree3);
/*     */         }
/*     */       }
/* 155 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getTreeHeader()
/*     */   {
/* 165 */     String str = this.configuration.getText("doclet.Window_Class_Hierarchy");
/* 166 */     HtmlTree localHtmlTree = getBody(true, getWindowTitle(str));
/* 167 */     addTop(localHtmlTree);
/* 168 */     addNavLinks(true, localHtmlTree);
/* 169 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.TreeWriter
 * JD-Core Version:    0.6.2
 */