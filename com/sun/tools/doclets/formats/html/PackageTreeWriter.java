/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PackageTreeWriter extends AbstractTreeWriter
/*     */ {
/*     */   protected PackageDoc packagedoc;
/*     */   protected PackageDoc prev;
/*     */   protected PackageDoc next;
/*     */ 
/*     */   public PackageTreeWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3)
/*     */     throws IOException
/*     */   {
/*  74 */     super(paramConfigurationImpl, paramDocPath, new ClassTree(paramConfigurationImpl.classDocCatalog
/*  76 */       .allClasses(paramPackageDoc1), 
/*  76 */       paramConfigurationImpl));
/*     */ 
/*  78 */     this.packagedoc = paramPackageDoc1;
/*  79 */     this.prev = paramPackageDoc2;
/*  80 */     this.next = paramPackageDoc3;
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, boolean paramBoolean)
/*     */   {
/*  98 */     DocPath localDocPath = DocPath.forPackage(paramPackageDoc1).resolve(DocPaths.PACKAGE_TREE);
/*     */     try {
/* 100 */       PackageTreeWriter localPackageTreeWriter = new PackageTreeWriter(paramConfigurationImpl, localDocPath, paramPackageDoc1, paramPackageDoc2, paramPackageDoc3);
/*     */ 
/* 102 */       localPackageTreeWriter.generatePackageTreeFile();
/* 103 */       localPackageTreeWriter.close();
/*     */     } catch (IOException localIOException) {
/* 105 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 107 */         .toString(), localDocPath.getPath() });
/* 108 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generatePackageTreeFile()
/*     */     throws IOException
/*     */   {
/* 116 */     Content localContent1 = getPackageTreeHeader();
/* 117 */     Content localContent2 = getResource("doclet.Hierarchy_For_Package", 
/* 118 */       Util.getPackageName(this.packagedoc));
/*     */ 
/* 119 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, false, HtmlStyle.title, localContent2);
/*     */ 
/* 121 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 122 */     if (this.configuration.packages.length > 1) {
/* 123 */       addLinkToMainTree(localHtmlTree2);
/*     */     }
/* 125 */     localContent1.addContent(localHtmlTree2);
/* 126 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 127 */     localHtmlTree3.addStyle(HtmlStyle.contentContainer);
/* 128 */     addTree(this.classtree.baseclasses(), "doclet.Class_Hierarchy", localHtmlTree3);
/* 129 */     addTree(this.classtree.baseinterfaces(), "doclet.Interface_Hierarchy", localHtmlTree3);
/* 130 */     addTree(this.classtree.baseAnnotationTypes(), "doclet.Annotation_Type_Hierarchy", localHtmlTree3);
/* 131 */     addTree(this.classtree.baseEnums(), "doclet.Enum_Hierarchy", localHtmlTree3);
/* 132 */     localContent1.addContent(localHtmlTree3);
/* 133 */     addNavLinks(false, localContent1);
/* 134 */     addBottom(localContent1);
/* 135 */     printHtmlDocument(null, true, localContent1);
/*     */   }
/*     */ 
/*     */   protected Content getPackageTreeHeader()
/*     */   {
/* 145 */     String str = this.packagedoc.name() + " " + this.configuration
/* 145 */       .getText("doclet.Window_Class_Hierarchy");
/*     */ 
/* 146 */     HtmlTree localHtmlTree = getBody(true, getWindowTitle(str));
/* 147 */     addTop(localHtmlTree);
/* 148 */     addNavLinks(true, localHtmlTree);
/* 149 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addLinkToMainTree(Content paramContent)
/*     */   {
/* 158 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.packageHierarchyLabel, 
/* 159 */       getResource("doclet.Package_Hierarchies"));
/*     */ 
/* 160 */     paramContent.addContent(localHtmlTree1);
/* 161 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.UL);
/* 162 */     localHtmlTree2.addStyle(HtmlStyle.horizontal);
/* 163 */     localHtmlTree2.addContent(getNavLinkMainTree(this.configuration.getText("doclet.All_Packages")));
/* 164 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPrevious()
/*     */   {
/* 173 */     if (this.prev == null) {
/* 174 */       return getNavLinkPrevious(null);
/*     */     }
/* 176 */     DocPath localDocPath = DocPath.relativePath(this.packagedoc, this.prev);
/* 177 */     return getNavLinkPrevious(localDocPath.resolve(DocPaths.PACKAGE_TREE));
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkNext()
/*     */   {
/* 187 */     if (this.next == null) {
/* 188 */       return getNavLinkNext(null);
/*     */     }
/* 190 */     DocPath localDocPath = DocPath.relativePath(this.packagedoc, this.next);
/* 191 */     return getNavLinkNext(localDocPath.resolve(DocPaths.PACKAGE_TREE));
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/* 201 */     Content localContent = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */ 
/* 203 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 204 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PackageTreeWriter
 * JD-Core Version:    0.6.2
 */