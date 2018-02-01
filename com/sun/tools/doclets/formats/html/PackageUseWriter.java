/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class PackageUseWriter extends SubWriterHolderWriter
/*     */ {
/*     */   final PackageDoc pkgdoc;
/*  50 */   final SortedMap<String, Set<ClassDoc>> usingPackageToUsedClasses = new TreeMap();
/*     */ 
/*     */   public PackageUseWriter(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, DocPath paramDocPath, PackageDoc paramPackageDoc)
/*     */     throws IOException
/*     */   {
/*  62 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc).resolve(paramDocPath));
/*  63 */     this.pkgdoc = paramPackageDoc;
/*     */ 
/*  68 */     ClassDoc[] arrayOfClassDoc = paramPackageDoc.allClasses();
/*     */     ClassDoc localClassDoc1;
/*     */     Iterator localIterator;
/*  69 */     for (int i = 0; i < arrayOfClassDoc.length; i++) {
/*  70 */       localClassDoc1 = arrayOfClassDoc[i];
/*  71 */       Set localSet = (Set)paramClassUseMapper.classToClass.get(localClassDoc1.qualifiedName());
/*  72 */       if (localSet != null)
/*  73 */         for (localIterator = localSet.iterator(); localIterator.hasNext(); ) {
/*  74 */           ClassDoc localClassDoc2 = (ClassDoc)localIterator.next();
/*  75 */           PackageDoc localPackageDoc = localClassDoc2.containingPackage();
/*     */ 
/*  77 */           Object localObject = (Set)this.usingPackageToUsedClasses
/*  77 */             .get(localPackageDoc
/*  77 */             .name());
/*  78 */           if (localObject == null) {
/*  79 */             localObject = new TreeSet();
/*  80 */             this.usingPackageToUsedClasses.put(Util.getPackageName(localPackageDoc), localObject);
/*     */           }
/*     */ 
/*  83 */           ((Set)localObject).add(localClassDoc1);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, PackageDoc paramPackageDoc)
/*     */   {
/*  99 */     DocPath localDocPath = DocPaths.PACKAGE_USE;
/*     */     try {
/* 101 */       PackageUseWriter localPackageUseWriter = new PackageUseWriter(paramConfigurationImpl, paramClassUseMapper, localDocPath, paramPackageDoc);
/*     */ 
/* 103 */       localPackageUseWriter.generatePackageUseFile();
/* 104 */       localPackageUseWriter.close();
/*     */     } catch (IOException localIOException) {
/* 106 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 108 */         .toString(), localDocPath });
/* 109 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generatePackageUseFile()
/*     */     throws IOException
/*     */   {
/* 118 */     Content localContent = getPackageUseHeader();
/* 119 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 120 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 121 */     if (this.usingPackageToUsedClasses.isEmpty())
/* 122 */       localHtmlTree.addContent(getResource("doclet.ClassUse_No.usage.of.0", this.pkgdoc
/* 123 */         .name()));
/*     */     else {
/* 125 */       addPackageUse(localHtmlTree);
/*     */     }
/* 127 */     localContent.addContent(localHtmlTree);
/* 128 */     addNavLinks(false, localContent);
/* 129 */     addBottom(localContent);
/* 130 */     printHtmlDocument(null, true, localContent);
/*     */   }
/*     */ 
/*     */   protected void addPackageUse(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 139 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 140 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 141 */     if (this.configuration.packages.length > 1) {
/* 142 */       addPackageList(localHtmlTree);
/*     */     }
/* 144 */     addClassList(localHtmlTree);
/* 145 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addPackageList(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 154 */     HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, this.useTableSummary, 
/* 155 */       getTableCaption(this.configuration
/* 155 */       .getResource("doclet.ClassUse_Packages.that.use.0", 
/* 157 */       getPackageLink(this.pkgdoc, 
/* 157 */       Util.getPackageName(this.pkgdoc)))));
/*     */ 
/* 158 */     localHtmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 159 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 160 */     Iterator localIterator = this.usingPackageToUsedClasses.keySet().iterator();
/* 161 */     for (int i = 0; localIterator.hasNext(); i++) {
/* 162 */       PackageDoc localPackageDoc = this.configuration.root.packageNamed((String)localIterator.next());
/* 163 */       HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.TR);
/* 164 */       if (i % 2 == 0)
/* 165 */         localHtmlTree4.addStyle(HtmlStyle.altColor);
/*     */       else {
/* 167 */         localHtmlTree4.addStyle(HtmlStyle.rowColor);
/*     */       }
/* 169 */       addPackageUse(localPackageDoc, localHtmlTree4);
/* 170 */       localHtmlTree2.addContent(localHtmlTree4);
/*     */     }
/* 172 */     localHtmlTree1.addContent(localHtmlTree2);
/* 173 */     HtmlTree localHtmlTree3 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 174 */     paramContent.addContent(localHtmlTree3);
/*     */   }
/*     */ 
/*     */   protected void addClassList(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 184 */     String[] arrayOfString = { this.configuration
/* 184 */       .getText("doclet.0_and_1", this.configuration
/* 185 */       .getText("doclet.Class"), 
/* 185 */       this.configuration
/* 186 */       .getText("doclet.Description")) };
/*     */ 
/* 188 */     Iterator localIterator1 = this.usingPackageToUsedClasses.keySet().iterator();
/* 189 */     while (localIterator1.hasNext()) {
/* 190 */       String str1 = (String)localIterator1.next();
/* 191 */       PackageDoc localPackageDoc = this.configuration.root.packageNamed(str1);
/* 192 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.LI);
/* 193 */       localHtmlTree1.addStyle(HtmlStyle.blockList);
/* 194 */       if (localPackageDoc != null) {
/* 195 */         localHtmlTree1.addContent(getMarkerAnchor(localPackageDoc.name()));
/*     */       }
/* 197 */       String str2 = this.configuration.getText("doclet.Use_Table_Summary", this.configuration
/* 198 */         .getText("doclet.classes"));
/*     */ 
/* 199 */       HtmlTree localHtmlTree2 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, str2, 
/* 200 */         getTableCaption(this.configuration
/* 200 */         .getResource("doclet.ClassUse_Classes.in.0.used.by.1", 
/* 202 */         getPackageLink(this.pkgdoc, 
/* 202 */         Util.getPackageName(this.pkgdoc)), 
/* 203 */         getPackageLink(localPackageDoc, 
/* 203 */         Util.getPackageName(localPackageDoc)))));
/*     */ 
/* 204 */       localHtmlTree2.addContent(getSummaryTableHeader(arrayOfString, "col"));
/* 205 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.TBODY);
/*     */ 
/* 207 */       Iterator localIterator2 = ((Set)this.usingPackageToUsedClasses
/* 207 */         .get(str1))
/* 207 */         .iterator();
/* 208 */       for (int i = 0; localIterator2.hasNext(); i++) {
/* 209 */         HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.TR);
/* 210 */         if (i % 2 == 0)
/* 211 */           localHtmlTree4.addStyle(HtmlStyle.altColor);
/*     */         else {
/* 213 */           localHtmlTree4.addStyle(HtmlStyle.rowColor);
/*     */         }
/* 215 */         addClassRow((ClassDoc)localIterator2.next(), str1, localHtmlTree4);
/* 216 */         localHtmlTree3.addContent(localHtmlTree4);
/*     */       }
/* 218 */       localHtmlTree2.addContent(localHtmlTree3);
/* 219 */       localHtmlTree1.addContent(localHtmlTree2);
/* 220 */       paramContent.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addClassRow(ClassDoc paramClassDoc, String paramString, Content paramContent)
/*     */   {
/* 233 */     DocPath localDocPath = pathString(paramClassDoc, DocPaths.CLASS_USE
/* 234 */       .resolve(DocPath.forName(paramClassDoc)));
/*     */ 
/* 235 */     HtmlTree localHtmlTree = HtmlTree.TD(HtmlStyle.colOne, 
/* 236 */       getHyperLink(localDocPath
/* 236 */       .fragment(paramString), 
/* 236 */       new StringContent(paramClassDoc.name())));
/* 237 */     addIndexComment(paramClassDoc, localHtmlTree);
/* 238 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addPackageUse(PackageDoc paramPackageDoc, Content paramContent)
/*     */     throws IOException
/*     */   {
/* 248 */     HtmlTree localHtmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, 
/* 249 */       getHyperLink(Util.getPackageName(paramPackageDoc), 
/* 249 */       new StringContent(
/* 250 */       Util.getPackageName(paramPackageDoc))));
/*     */ 
/* 251 */     paramContent.addContent(localHtmlTree1);
/* 252 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TD);
/* 253 */     localHtmlTree2.addStyle(HtmlStyle.colLast);
/* 254 */     if ((paramPackageDoc != null) && (paramPackageDoc.name().length() != 0))
/* 255 */       addSummaryComment(paramPackageDoc, localHtmlTree2);
/*     */     else {
/* 257 */       localHtmlTree2.addContent(getSpace());
/*     */     }
/* 259 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected Content getPackageUseHeader()
/*     */   {
/* 268 */     String str1 = this.configuration.getText("doclet.Package");
/* 269 */     String str2 = this.pkgdoc.name();
/* 270 */     String str3 = this.configuration.getText("doclet.Window_ClassUse_Header", str1, str2);
/*     */ 
/* 272 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str3));
/* 273 */     addTop(localHtmlTree1);
/* 274 */     addNavLinks(true, localHtmlTree1);
/* 275 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 276 */     localContentBuilder.addContent(getResource("doclet.ClassUse_Title", str1));
/* 277 */     localContentBuilder.addContent(new HtmlTree(HtmlTag.BR));
/* 278 */     localContentBuilder.addContent(str2);
/* 279 */     HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, localContentBuilder);
/*     */ 
/* 281 */     HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree2);
/* 282 */     localHtmlTree1.addContent(localHtmlTree3);
/* 283 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/* 292 */     Content localContent = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */ 
/* 294 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 295 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClassUse()
/*     */   {
/* 304 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.useLabel);
/* 305 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 314 */     Content localContent = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel);
/*     */ 
/* 316 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 317 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PackageUseWriter
 * JD-Core Version:    0.6.2
 */