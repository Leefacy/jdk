/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.formats.html.markup.DocType;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlDocument;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javadoc.SourcePositionImpl;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ import javax.tools.FileObject;
/*     */ 
/*     */ public class SourceToHTMLConverter
/*     */ {
/*     */   private static final int NUM_BLANK_LINES = 60;
/*  61 */   private static final String NEW_LINE = DocletConstants.NL;
/*     */   private final ConfigurationImpl configuration;
/*     */   private final RootDoc rootDoc;
/*     */   private DocPath outputdir;
/*  73 */   private DocPath relativePath = DocPath.empty;
/*     */ 
/*     */   private SourceToHTMLConverter(ConfigurationImpl paramConfigurationImpl, RootDoc paramRootDoc, DocPath paramDocPath)
/*     */   {
/*  77 */     this.configuration = paramConfigurationImpl;
/*  78 */     this.rootDoc = paramRootDoc;
/*  79 */     this.outputdir = paramDocPath;
/*     */   }
/*     */ 
/*     */   public static void convertRoot(ConfigurationImpl paramConfigurationImpl, RootDoc paramRootDoc, DocPath paramDocPath)
/*     */   {
/*  91 */     new SourceToHTMLConverter(paramConfigurationImpl, paramRootDoc, paramDocPath).generate();
/*     */   }
/*     */ 
/*     */   void generate() {
/*  95 */     if ((this.rootDoc == null) || (this.outputdir == null)) {
/*  96 */       return;
/*     */     }
/*  98 */     PackageDoc[] arrayOfPackageDoc = this.rootDoc.specifiedPackages();
/*  99 */     for (int i = 0; i < arrayOfPackageDoc.length; i++)
/*     */     {
/* 102 */       if ((!this.configuration.nodeprecated) || (!Util.isDeprecated(arrayOfPackageDoc[i])))
/* 103 */         convertPackage(arrayOfPackageDoc[i], this.outputdir);
/*     */     }
/* 105 */     ClassDoc[] arrayOfClassDoc = this.rootDoc.specifiedClasses();
/* 106 */     for (int j = 0; j < arrayOfClassDoc.length; j++)
/*     */     {
/* 110 */       if ((!this.configuration.nodeprecated) || (
/* 111 */         (!Util.isDeprecated(arrayOfClassDoc[j])) && 
/* 111 */         (!Util.isDeprecated(arrayOfClassDoc[j].containingPackage()))))
/* 112 */         convertClass(arrayOfClassDoc[j], this.outputdir);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void convertPackage(PackageDoc paramPackageDoc, DocPath paramDocPath)
/*     */   {
/* 123 */     if (paramPackageDoc == null) {
/* 124 */       return;
/*     */     }
/* 126 */     ClassDoc[] arrayOfClassDoc = paramPackageDoc.allClasses();
/* 127 */     for (int i = 0; i < arrayOfClassDoc.length; i++)
/*     */     {
/* 132 */       if ((!this.configuration.nodeprecated) || (!Util.isDeprecated(arrayOfClassDoc[i])))
/* 133 */         convertClass(arrayOfClassDoc[i], paramDocPath);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void convertClass(ClassDoc paramClassDoc, DocPath paramDocPath)
/*     */   {
/* 144 */     if (paramClassDoc == null)
/* 145 */       return;
/*     */     try
/*     */     {
/* 148 */       SourcePosition localSourcePosition = paramClassDoc.position();
/* 149 */       if (localSourcePosition == null)
/*     */         return;
/*     */       Object localObject1;
/* 153 */       if ((localSourcePosition instanceof SourcePositionImpl)) {
/* 154 */         localObject2 = ((SourcePositionImpl)localSourcePosition).fileObject();
/* 155 */         if (localObject2 == null)
/* 156 */           return;
/* 157 */         localObject1 = ((FileObject)localObject2).openReader(true);
/*     */       } else {
/* 159 */         localObject2 = localSourcePosition.file();
/* 160 */         if (localObject2 == null)
/* 161 */           return;
/* 162 */         localObject1 = new FileReader((File)localObject2);
/*     */       }
/* 164 */       Object localObject2 = new LineNumberReader((Reader)localObject1);
/* 165 */       int i = 1;
/*     */ 
/* 167 */       this.relativePath = DocPaths.SOURCE_OUTPUT
/* 168 */         .resolve(DocPath.forPackage(paramClassDoc))
/* 169 */         .invert();
/* 170 */       Content localContent = getHeader();
/* 171 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.PRE);
/*     */       try
/*     */       {
/*     */         String str;
/* 173 */         while ((str = ((LineNumberReader)localObject2).readLine()) != null) {
/* 174 */           addLineNo(localHtmlTree1, i);
/* 175 */           addLine(localHtmlTree1, str, i);
/* 176 */           i++;
/*     */         }
/*     */       } finally {
/* 179 */         ((LineNumberReader)localObject2).close();
/*     */       }
/* 181 */       addBlankLines(localHtmlTree1);
/* 182 */       HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.sourceContainer, localHtmlTree1);
/* 183 */       localContent.addContent(localHtmlTree2);
/* 184 */       writeToFile(localContent, paramDocPath.resolve(DocPath.forClass(paramClassDoc)));
/*     */     } catch (IOException localIOException) {
/* 186 */       localIOException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeToFile(Content paramContent, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/* 197 */     DocType localDocType = DocType.TRANSITIONAL;
/* 198 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.HEAD);
/* 199 */     localHtmlTree1.addContent(HtmlTree.TITLE(new StringContent(this.configuration
/* 200 */       .getText("doclet.Window_Source_title"))));
/*     */ 
/* 201 */     localHtmlTree1.addContent(getStyleSheetProperties());
/* 202 */     HtmlTree localHtmlTree2 = HtmlTree.HTML(this.configuration.getLocale().getLanguage(), localHtmlTree1, paramContent);
/*     */ 
/* 204 */     HtmlDocument localHtmlDocument = new HtmlDocument(localDocType, localHtmlTree2);
/* 205 */     this.configuration.message.notice("doclet.Generating_0", new Object[] { paramDocPath.getPath() });
/* 206 */     DocFile localDocFile = DocFile.createFileForOutput(this.configuration, paramDocPath);
/* 207 */     Writer localWriter = localDocFile.openWriter();
/*     */     try {
/* 209 */       localHtmlDocument.write(localWriter, true);
/*     */     } finally {
/* 211 */       localWriter.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public HtmlTree getStyleSheetProperties()
/*     */   {
/* 222 */     String str = this.configuration.stylesheetfile;
/*     */     DocPath localDocPath;
/* 224 */     if (str.length() > 0) {
/* 225 */       localObject = DocFile.createFileForInput(this.configuration, str);
/* 226 */       localDocPath = DocPath.create(((DocFile)localObject).getName());
/*     */     } else {
/* 228 */       localDocPath = DocPaths.STYLESHEET;
/*     */     }
/* 230 */     Object localObject = this.relativePath.resolve(localDocPath);
/* 231 */     HtmlTree localHtmlTree = HtmlTree.LINK("stylesheet", "text/css", ((DocPath)localObject).getPath(), "Style");
/* 232 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   private static Content getHeader()
/*     */   {
/* 241 */     return new HtmlTree(HtmlTag.BODY);
/*     */   }
/*     */ 
/*     */   private static void addLineNo(Content paramContent, int paramInt)
/*     */   {
/* 251 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SPAN);
/* 252 */     localHtmlTree.addStyle(HtmlStyle.sourceLineNo);
/* 253 */     if (paramInt < 10)
/* 254 */       localHtmlTree.addContent("00" + Integer.toString(paramInt));
/* 255 */     else if (paramInt < 100)
/* 256 */       localHtmlTree.addContent("0" + Integer.toString(paramInt));
/*     */     else {
/* 258 */       localHtmlTree.addContent(Integer.toString(paramInt));
/*     */     }
/* 260 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   private void addLine(Content paramContent, String paramString, int paramInt)
/*     */   {
/* 271 */     if (paramString != null) {
/* 272 */       paramContent.addContent(Util.replaceTabs(this.configuration, paramString));
/* 273 */       HtmlTree localHtmlTree = HtmlTree.A_NAME("line." + Integer.toString(paramInt));
/* 274 */       paramContent.addContent(localHtmlTree);
/* 275 */       paramContent.addContent(NEW_LINE);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void addBlankLines(Content paramContent)
/*     */   {
/* 285 */     for (int i = 0; i < 60; i++)
/* 286 */       paramContent.addContent(NEW_LINE);
/*     */   }
/*     */ 
/*     */   public static String getAnchorName(Doc paramDoc)
/*     */   {
/* 297 */     return "line." + paramDoc.position().line();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.SourceToHTMLConverter
 * JD-Core Version:    0.6.2
 */