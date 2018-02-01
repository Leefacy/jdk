/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class ClassUseWriter extends SubWriterHolderWriter
/*     */ {
/*     */   final ClassDoc classdoc;
/*  58 */   Set<PackageDoc> pkgToPackageAnnotations = null;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToClassTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToClassAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodArgTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodReturnTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodParameterAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToFieldTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToFieldAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToSubclass;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToSubinterface;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToImplementingClass;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToField;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodReturn;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodArgs;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodThrows;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorParameterAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorArgs;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorArgTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorThrows;
/*     */   final SortedSet<PackageDoc> pkgSet;
/*     */   final MethodWriterImpl methodSubWriter;
/*     */   final ConstructorWriterImpl constrSubWriter;
/*     */   final FieldWriterImpl fieldSubWriter;
/*     */   final NestedClassWriterImpl classSubWriter;
/*     */   final String classUseTableSummary;
/*     */   final String subclassUseTableSummary;
/*     */   final String subinterfaceUseTableSummary;
/*     */   final String fieldUseTableSummary;
/*     */   final String methodUseTableSummary;
/*     */   final String constructorUseTableSummary;
/*     */ 
/*     */   public ClassUseWriter(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, DocPath paramDocPath, ClassDoc paramClassDoc)
/*     */     throws IOException
/*     */   {
/* 103 */     super(paramConfigurationImpl, paramDocPath);
/* 104 */     this.classdoc = paramClassDoc;
/* 105 */     if (paramClassUseMapper.classToPackageAnnotations.containsKey(paramClassDoc.qualifiedName()))
/* 106 */       this.pkgToPackageAnnotations = new TreeSet((Collection)paramClassUseMapper.classToPackageAnnotations.get(paramClassDoc.qualifiedName()));
/* 107 */     paramConfigurationImpl.currentcd = paramClassDoc;
/* 108 */     this.pkgSet = new TreeSet();
/* 109 */     this.pkgToClassTypeParameter = pkgDivide(paramClassUseMapper.classToClassTypeParam);
/* 110 */     this.pkgToClassAnnotations = pkgDivide(paramClassUseMapper.classToClassAnnotations);
/* 111 */     this.pkgToMethodTypeParameter = pkgDivide(paramClassUseMapper.classToExecMemberDocTypeParam);
/* 112 */     this.pkgToMethodArgTypeParameter = pkgDivide(paramClassUseMapper.classToExecMemberDocArgTypeParam);
/* 113 */     this.pkgToFieldTypeParameter = pkgDivide(paramClassUseMapper.classToFieldDocTypeParam);
/* 114 */     this.pkgToFieldAnnotations = pkgDivide(paramClassUseMapper.annotationToFieldDoc);
/* 115 */     this.pkgToMethodReturnTypeParameter = pkgDivide(paramClassUseMapper.classToExecMemberDocReturnTypeParam);
/* 116 */     this.pkgToMethodAnnotations = pkgDivide(paramClassUseMapper.classToExecMemberDocAnnotations);
/* 117 */     this.pkgToMethodParameterAnnotations = pkgDivide(paramClassUseMapper.classToExecMemberDocParamAnnotation);
/* 118 */     this.pkgToSubclass = pkgDivide(paramClassUseMapper.classToSubclass);
/* 119 */     this.pkgToSubinterface = pkgDivide(paramClassUseMapper.classToSubinterface);
/* 120 */     this.pkgToImplementingClass = pkgDivide(paramClassUseMapper.classToImplementingClass);
/* 121 */     this.pkgToField = pkgDivide(paramClassUseMapper.classToField);
/* 122 */     this.pkgToMethodReturn = pkgDivide(paramClassUseMapper.classToMethodReturn);
/* 123 */     this.pkgToMethodArgs = pkgDivide(paramClassUseMapper.classToMethodArgs);
/* 124 */     this.pkgToMethodThrows = pkgDivide(paramClassUseMapper.classToMethodThrows);
/* 125 */     this.pkgToConstructorAnnotations = pkgDivide(paramClassUseMapper.classToConstructorAnnotations);
/* 126 */     this.pkgToConstructorParameterAnnotations = pkgDivide(paramClassUseMapper.classToConstructorParamAnnotation);
/* 127 */     this.pkgToConstructorArgs = pkgDivide(paramClassUseMapper.classToConstructorArgs);
/* 128 */     this.pkgToConstructorArgTypeParameter = pkgDivide(paramClassUseMapper.classToConstructorDocArgTypeParam);
/* 129 */     this.pkgToConstructorThrows = pkgDivide(paramClassUseMapper.classToConstructorThrows);
/*     */ 
/* 131 */     if ((this.pkgSet.size() > 0) && 
/* 132 */       (paramClassUseMapper.classToPackage
/* 132 */       .containsKey(paramClassDoc
/* 132 */       .qualifiedName())) && 
/* 133 */       (!this.pkgSet
/* 133 */       .equals(paramClassUseMapper.classToPackage
/* 133 */       .get(paramClassDoc
/* 133 */       .qualifiedName())))) {
/* 134 */       paramConfigurationImpl.root.printWarning("Internal error: package sets don't match: " + this.pkgSet + " with: " + paramClassUseMapper.classToPackage
/* 135 */         .get(paramClassDoc
/* 135 */         .qualifiedName()));
/*     */     }
/* 137 */     this.methodSubWriter = new MethodWriterImpl(this);
/* 138 */     this.constrSubWriter = new ConstructorWriterImpl(this);
/* 139 */     this.fieldSubWriter = new FieldWriterImpl(this);
/* 140 */     this.classSubWriter = new NestedClassWriterImpl(this);
/* 141 */     this.classUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 142 */       .getText("doclet.classes"));
/*     */ 
/* 143 */     this.subclassUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 144 */       .getText("doclet.subclasses"));
/*     */ 
/* 145 */     this.subinterfaceUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 146 */       .getText("doclet.subinterfaces"));
/*     */ 
/* 147 */     this.fieldUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 148 */       .getText("doclet.fields"));
/*     */ 
/* 149 */     this.methodUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 150 */       .getText("doclet.methods"));
/*     */ 
/* 151 */     this.constructorUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 152 */       .getText("doclet.constructors"));
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassTree paramClassTree)
/*     */   {
/* 161 */     ClassUseMapper localClassUseMapper = new ClassUseMapper(paramConfigurationImpl.root, paramClassTree);
/* 162 */     ClassDoc[] arrayOfClassDoc = paramConfigurationImpl.root.classes();
/* 163 */     for (int i = 0; i < arrayOfClassDoc.length; i++)
/*     */     {
/* 168 */       if ((!paramConfigurationImpl.nodeprecated) || 
/* 169 */         (!Util.isDeprecated(arrayOfClassDoc[i]
/* 169 */         .containingPackage())))
/* 170 */         generate(paramConfigurationImpl, localClassUseMapper, arrayOfClassDoc[i]);
/*     */     }
/* 172 */     PackageDoc[] arrayOfPackageDoc = paramConfigurationImpl.packages;
/* 173 */     for (int j = 0; j < arrayOfPackageDoc.length; j++)
/*     */     {
/* 176 */       if ((!paramConfigurationImpl.nodeprecated) || (!Util.isDeprecated(arrayOfPackageDoc[j])))
/* 177 */         PackageUseWriter.generate(paramConfigurationImpl, localClassUseMapper, arrayOfPackageDoc[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Map<String, List<ProgramElementDoc>> pkgDivide(Map<String, ? extends List<? extends ProgramElementDoc>> paramMap) {
/* 182 */     HashMap localHashMap = new HashMap();
/* 183 */     List localList = (List)paramMap.get(this.classdoc.qualifiedName());
/* 184 */     if (localList != null) {
/* 185 */       Collections.sort(localList);
/* 186 */       Iterator localIterator = localList.iterator();
/* 187 */       while (localIterator.hasNext()) {
/* 188 */         ProgramElementDoc localProgramElementDoc = (ProgramElementDoc)localIterator.next();
/* 189 */         PackageDoc localPackageDoc = localProgramElementDoc.containingPackage();
/* 190 */         this.pkgSet.add(localPackageDoc);
/* 191 */         Object localObject = (List)localHashMap.get(localPackageDoc.name());
/* 192 */         if (localObject == null) {
/* 193 */           localObject = new ArrayList();
/* 194 */           localHashMap.put(localPackageDoc.name(), localObject);
/*     */         }
/* 196 */         ((List)localObject).add(localProgramElementDoc);
/*     */       }
/*     */     }
/* 199 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, ClassDoc paramClassDoc)
/*     */   {
/* 210 */     DocPath localDocPath = DocPath.forPackage(paramClassDoc)
/* 209 */       .resolve(DocPaths.CLASS_USE)
/* 210 */       .resolve(DocPath.forName(paramClassDoc));
/*     */     try
/*     */     {
/* 212 */       ClassUseWriter localClassUseWriter = new ClassUseWriter(paramConfigurationImpl, paramClassUseMapper, localDocPath, paramClassDoc);
/*     */ 
/* 215 */       localClassUseWriter.generateClassUseFile();
/* 216 */       localClassUseWriter.close();
/*     */     } catch (IOException localIOException) {
/* 218 */       paramConfigurationImpl.standardmessage
/* 219 */         .error("doclet.exception_encountered", new Object[] { localIOException
/* 220 */         .toString(), localDocPath.getPath() });
/* 221 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateClassUseFile()
/*     */     throws IOException
/*     */   {
/* 229 */     Content localContent = getClassUseHeader();
/* 230 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 231 */     localHtmlTree.addStyle(HtmlStyle.classUseContainer);
/* 232 */     if (this.pkgSet.size() > 0)
/* 233 */       addClassUse(localHtmlTree);
/*     */     else {
/* 235 */       localHtmlTree.addContent(getResource("doclet.ClassUse_No.usage.of.0", this.classdoc
/* 236 */         .qualifiedName()));
/*     */     }
/* 238 */     localContent.addContent(localHtmlTree);
/* 239 */     addNavLinks(false, localContent);
/* 240 */     addBottom(localContent);
/* 241 */     printHtmlDocument(null, true, localContent);
/*     */   }
/*     */ 
/*     */   protected void addClassUse(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 250 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/* 251 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/* 252 */     if (this.configuration.packages.length > 1) {
/* 253 */       addPackageList(localHtmlTree);
/* 254 */       addPackageAnnotationList(localHtmlTree);
/*     */     }
/* 256 */     addClassList(localHtmlTree);
/* 257 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addPackageList(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 266 */     HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, this.useTableSummary, 
/* 267 */       getTableCaption(this.configuration
/* 267 */       .getResource("doclet.ClassUse_Packages.that.use.0", 
/* 269 */       getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)))));
/*     */ 
/* 271 */     localHtmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 272 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 273 */     Iterator localIterator = this.pkgSet.iterator();
/* 274 */     for (int i = 0; localIterator.hasNext(); i++) {
/* 275 */       PackageDoc localPackageDoc = (PackageDoc)localIterator.next();
/* 276 */       HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.TR);
/* 277 */       if (i % 2 == 0)
/* 278 */         localHtmlTree4.addStyle(HtmlStyle.altColor);
/*     */       else {
/* 280 */         localHtmlTree4.addStyle(HtmlStyle.rowColor);
/*     */       }
/* 282 */       addPackageUse(localPackageDoc, localHtmlTree4);
/* 283 */       localHtmlTree2.addContent(localHtmlTree4);
/*     */     }
/* 285 */     localHtmlTree1.addContent(localHtmlTree2);
/* 286 */     HtmlTree localHtmlTree3 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 287 */     paramContent.addContent(localHtmlTree3);
/*     */   }
/*     */ 
/*     */   protected void addPackageAnnotationList(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 296 */     if ((!this.classdoc.isAnnotationType()) || (this.pkgToPackageAnnotations == null) || 
/* 298 */       (this.pkgToPackageAnnotations
/* 298 */       .isEmpty())) {
/* 299 */       return;
/*     */     }
/* 301 */     HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, this.useTableSummary, 
/* 302 */       getTableCaption(this.configuration
/* 302 */       .getResource("doclet.ClassUse_PackageAnnotation", 
/* 304 */       getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)))));
/*     */ 
/* 306 */     localHtmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 307 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 308 */     Iterator localIterator = this.pkgToPackageAnnotations.iterator();
/* 309 */     for (int i = 0; localIterator.hasNext(); i++) {
/* 310 */       PackageDoc localPackageDoc = (PackageDoc)localIterator.next();
/* 311 */       HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.TR);
/* 312 */       if (i % 2 == 0)
/* 313 */         localHtmlTree4.addStyle(HtmlStyle.altColor);
/*     */       else {
/* 315 */         localHtmlTree4.addStyle(HtmlStyle.rowColor);
/*     */       }
/* 317 */       HtmlTree localHtmlTree5 = HtmlTree.TD(HtmlStyle.colFirst, 
/* 318 */         getPackageLink(localPackageDoc, new StringContent(localPackageDoc
/* 318 */         .name())));
/* 319 */       localHtmlTree4.addContent(localHtmlTree5);
/* 320 */       HtmlTree localHtmlTree6 = new HtmlTree(HtmlTag.TD);
/* 321 */       localHtmlTree6.addStyle(HtmlStyle.colLast);
/* 322 */       addSummaryComment(localPackageDoc, localHtmlTree6);
/* 323 */       localHtmlTree4.addContent(localHtmlTree6);
/* 324 */       localHtmlTree2.addContent(localHtmlTree4);
/*     */     }
/* 326 */     localHtmlTree1.addContent(localHtmlTree2);
/* 327 */     HtmlTree localHtmlTree3 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 328 */     paramContent.addContent(localHtmlTree3);
/*     */   }
/*     */ 
/*     */   protected void addClassList(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 337 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.UL);
/* 338 */     localHtmlTree1.addStyle(HtmlStyle.blockList);
/* 339 */     for (Object localObject = this.pkgSet.iterator(); ((Iterator)localObject).hasNext(); ) {
/* 340 */       PackageDoc localPackageDoc = (PackageDoc)((Iterator)localObject).next();
/* 341 */       HtmlTree localHtmlTree2 = HtmlTree.LI(HtmlStyle.blockList, getMarkerAnchor(localPackageDoc.name()));
/* 342 */       Content localContent = getResource("doclet.ClassUse_Uses.of.0.in.1", 
/* 343 */         getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)), 
/* 345 */         getPackageLink(localPackageDoc, 
/* 345 */         Util.getPackageName(localPackageDoc)));
/*     */ 
/* 346 */       HtmlTree localHtmlTree3 = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, localContent);
/* 347 */       localHtmlTree2.addContent(localHtmlTree3);
/* 348 */       addClassUse(localPackageDoc, localHtmlTree2);
/* 349 */       localHtmlTree1.addContent(localHtmlTree2);
/*     */     }
/* 351 */     localObject = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 352 */     paramContent.addContent((Content)localObject);
/*     */   }
/*     */ 
/*     */   protected void addPackageUse(PackageDoc paramPackageDoc, Content paramContent)
/*     */     throws IOException
/*     */   {
/* 362 */     HtmlTree localHtmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, 
/* 363 */       getHyperLink(paramPackageDoc
/* 363 */       .name(), new StringContent(Util.getPackageName(paramPackageDoc))));
/* 364 */     paramContent.addContent(localHtmlTree1);
/* 365 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TD);
/* 366 */     localHtmlTree2.addStyle(HtmlStyle.colLast);
/* 367 */     addSummaryComment(paramPackageDoc, localHtmlTree2);
/* 368 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addClassUse(PackageDoc paramPackageDoc, Content paramContent)
/*     */     throws IOException
/*     */   {
/* 378 */     Content localContent1 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc));
/*     */ 
/* 380 */     Content localContent2 = getPackageLink(paramPackageDoc, Util.getPackageName(paramPackageDoc));
/* 381 */     this.classSubWriter.addUseInfo((List)this.pkgToClassAnnotations.get(paramPackageDoc.name()), this.configuration
/* 382 */       .getResource("doclet.ClassUse_Annotation", localContent1, localContent2), 
/* 382 */       this.classUseTableSummary, paramContent);
/*     */ 
/* 384 */     this.classSubWriter.addUseInfo((List)this.pkgToClassTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 385 */       .getResource("doclet.ClassUse_TypeParameter", localContent1, localContent2), 
/* 385 */       this.classUseTableSummary, paramContent);
/*     */ 
/* 387 */     this.classSubWriter.addUseInfo((List)this.pkgToSubclass.get(paramPackageDoc.name()), this.configuration
/* 388 */       .getResource("doclet.ClassUse_Subclass", localContent1, localContent2), 
/* 388 */       this.subclassUseTableSummary, paramContent);
/*     */ 
/* 390 */     this.classSubWriter.addUseInfo((List)this.pkgToSubinterface.get(paramPackageDoc.name()), this.configuration
/* 391 */       .getResource("doclet.ClassUse_Subinterface", localContent1, localContent2), 
/* 391 */       this.subinterfaceUseTableSummary, paramContent);
/*     */ 
/* 393 */     this.classSubWriter.addUseInfo((List)this.pkgToImplementingClass.get(paramPackageDoc.name()), this.configuration
/* 394 */       .getResource("doclet.ClassUse_ImplementingClass", localContent1, localContent2), 
/* 394 */       this.classUseTableSummary, paramContent);
/*     */ 
/* 396 */     this.fieldSubWriter.addUseInfo((List)this.pkgToField.get(paramPackageDoc.name()), this.configuration
/* 397 */       .getResource("doclet.ClassUse_Field", localContent1, localContent2), 
/* 397 */       this.fieldUseTableSummary, paramContent);
/*     */ 
/* 399 */     this.fieldSubWriter.addUseInfo((List)this.pkgToFieldAnnotations.get(paramPackageDoc.name()), this.configuration
/* 400 */       .getResource("doclet.ClassUse_FieldAnnotations", localContent1, localContent2), 
/* 400 */       this.fieldUseTableSummary, paramContent);
/*     */ 
/* 402 */     this.fieldSubWriter.addUseInfo((List)this.pkgToFieldTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 403 */       .getResource("doclet.ClassUse_FieldTypeParameter", localContent1, localContent2), 
/* 403 */       this.fieldUseTableSummary, paramContent);
/*     */ 
/* 405 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodAnnotations.get(paramPackageDoc.name()), this.configuration
/* 406 */       .getResource("doclet.ClassUse_MethodAnnotations", localContent1, localContent2), 
/* 406 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 408 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodParameterAnnotations.get(paramPackageDoc.name()), this.configuration
/* 409 */       .getResource("doclet.ClassUse_MethodParameterAnnotations", localContent1, localContent2), 
/* 409 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 411 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 412 */       .getResource("doclet.ClassUse_MethodTypeParameter", localContent1, localContent2), 
/* 412 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 414 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodReturn.get(paramPackageDoc.name()), this.configuration
/* 415 */       .getResource("doclet.ClassUse_MethodReturn", localContent1, localContent2), 
/* 415 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 417 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodReturnTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 418 */       .getResource("doclet.ClassUse_MethodReturnTypeParameter", localContent1, localContent2), 
/* 418 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 420 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodArgs.get(paramPackageDoc.name()), this.configuration
/* 421 */       .getResource("doclet.ClassUse_MethodArgs", localContent1, localContent2), 
/* 421 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 423 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodArgTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 424 */       .getResource("doclet.ClassUse_MethodArgsTypeParameters", localContent1, localContent2), 
/* 424 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 426 */     this.methodSubWriter.addUseInfo((List)this.pkgToMethodThrows.get(paramPackageDoc.name()), this.configuration
/* 427 */       .getResource("doclet.ClassUse_MethodThrows", localContent1, localContent2), 
/* 427 */       this.methodUseTableSummary, paramContent);
/*     */ 
/* 429 */     this.constrSubWriter.addUseInfo((List)this.pkgToConstructorAnnotations.get(paramPackageDoc.name()), this.configuration
/* 430 */       .getResource("doclet.ClassUse_ConstructorAnnotations", localContent1, localContent2), 
/* 430 */       this.constructorUseTableSummary, paramContent);
/*     */ 
/* 432 */     this.constrSubWriter.addUseInfo((List)this.pkgToConstructorParameterAnnotations.get(paramPackageDoc.name()), this.configuration
/* 433 */       .getResource("doclet.ClassUse_ConstructorParameterAnnotations", localContent1, localContent2), 
/* 433 */       this.constructorUseTableSummary, paramContent);
/*     */ 
/* 435 */     this.constrSubWriter.addUseInfo((List)this.pkgToConstructorArgs.get(paramPackageDoc.name()), this.configuration
/* 436 */       .getResource("doclet.ClassUse_ConstructorArgs", localContent1, localContent2), 
/* 436 */       this.constructorUseTableSummary, paramContent);
/*     */ 
/* 438 */     this.constrSubWriter.addUseInfo((List)this.pkgToConstructorArgTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 439 */       .getResource("doclet.ClassUse_ConstructorArgsTypeParameters", localContent1, localContent2), 
/* 439 */       this.constructorUseTableSummary, paramContent);
/*     */ 
/* 441 */     this.constrSubWriter.addUseInfo((List)this.pkgToConstructorThrows.get(paramPackageDoc.name()), this.configuration
/* 442 */       .getResource("doclet.ClassUse_ConstructorThrows", localContent1, localContent2), 
/* 442 */       this.constructorUseTableSummary, paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getClassUseHeader()
/*     */   {
/* 452 */     String str1 = this.configuration.getText(this.classdoc.isInterface() ? "doclet.Interface" : "doclet.Class");
/*     */ 
/* 454 */     String str2 = this.classdoc.qualifiedName();
/* 455 */     String str3 = this.configuration.getText("doclet.Window_ClassUse_Header", str1, str2);
/*     */ 
/* 457 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str3));
/* 458 */     addTop(localHtmlTree1);
/* 459 */     addNavLinks(true, localHtmlTree1);
/* 460 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 461 */     localContentBuilder.addContent(getResource("doclet.ClassUse_Title", str1));
/* 462 */     localContentBuilder.addContent(new HtmlTree(HtmlTag.BR));
/* 463 */     localContentBuilder.addContent(str2);
/* 464 */     HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.CLASS_PAGE_HEADING, true, HtmlStyle.title, localContentBuilder);
/*     */ 
/* 466 */     HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree2);
/* 467 */     localHtmlTree1.addContent(localHtmlTree3);
/* 468 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/* 478 */     Content localContent = getHyperLink(DocPath.parent
/* 478 */       .resolve(DocPaths.PACKAGE_SUMMARY), 
/* 478 */       this.packageLabel);
/* 479 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 480 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClass()
/*     */   {
/* 489 */     Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)
/* 491 */       .label(this.configuration
/* 491 */       .getText("doclet.Class")));
/*     */ 
/* 492 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 493 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClassUse()
/*     */   {
/* 502 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.useLabel);
/* 503 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 514 */     Content localContent = this.classdoc.containingPackage().isIncluded() ? 
/* 513 */       getHyperLink(DocPath.parent
/* 513 */       .resolve(DocPaths.PACKAGE_TREE), 
/* 513 */       this.treeLabel) : 
/* 514 */       getHyperLink(this.pathToRoot
/* 514 */       .resolve(DocPaths.OVERVIEW_TREE), 
/* 514 */       this.treeLabel);
/* 515 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 516 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ClassUseWriter
 * JD-Core Version:    0.6.2
 */