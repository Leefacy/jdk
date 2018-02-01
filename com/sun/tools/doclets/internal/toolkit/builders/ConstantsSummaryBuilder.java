/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ConstantsSummaryBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "ConstantSummary";
/*     */   public static final int MAX_CONSTANT_VALUE_INDEX_LENGTH = 2;
/*     */   protected final ConstantsSummaryWriter writer;
/*     */   protected final Set<ClassDoc> classDocsWithConstFields;
/*     */   protected Set<String> printedPackageHeaders;
/*     */   private PackageDoc currentPackage;
/*     */   private ClassDoc currentClass;
/*     */   private Content contentTree;
/*     */ 
/*     */   private ConstantsSummaryBuilder(AbstractBuilder.Context paramContext, ConstantsSummaryWriter paramConstantsSummaryWriter)
/*     */   {
/*  98 */     super(paramContext);
/*  99 */     this.writer = paramConstantsSummaryWriter;
/* 100 */     this.classDocsWithConstFields = new HashSet();
/*     */   }
/*     */ 
/*     */   public static ConstantsSummaryBuilder getInstance(AbstractBuilder.Context paramContext, ConstantsSummaryWriter paramConstantsSummaryWriter)
/*     */   {
/* 111 */     return new ConstantsSummaryBuilder(paramContext, paramConstantsSummaryWriter);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 118 */     if (this.writer == null)
/*     */     {
/* 120 */       return;
/*     */     }
/* 122 */     build(this.layoutParser.parseXML("ConstantSummary"), this.contentTree);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 129 */     return "ConstantSummary";
/*     */   }
/*     */ 
/*     */   public void buildConstantSummary(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 139 */     paramContent = this.writer.getHeader();
/* 140 */     buildChildren(paramXMLNode, paramContent);
/* 141 */     this.writer.addFooter(paramContent);
/* 142 */     this.writer.printDocument(paramContent);
/* 143 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void buildContents(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 153 */     Content localContent = this.writer.getContentsHeader();
/* 154 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 155 */     this.printedPackageHeaders = new HashSet();
/* 156 */     for (int i = 0; i < arrayOfPackageDoc.length; i++) {
/* 157 */       if ((hasConstantField(arrayOfPackageDoc[i])) && (!hasPrintedPackageIndex(arrayOfPackageDoc[i].name()))) {
/* 158 */         this.writer.addLinkToPackageContent(arrayOfPackageDoc[i], 
/* 159 */           parsePackageName(arrayOfPackageDoc[i]
/* 159 */           .name()), this.printedPackageHeaders, localContent);
/*     */       }
/*     */     }
/*     */ 
/* 163 */     paramContent.addContent(this.writer.getContentsList(localContent));
/*     */   }
/*     */ 
/*     */   public void buildConstantSummaries(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 173 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 174 */     this.printedPackageHeaders = new HashSet();
/* 175 */     Content localContent = this.writer.getConstantSummaries();
/* 176 */     for (int i = 0; i < arrayOfPackageDoc.length; i++) {
/* 177 */       if (hasConstantField(arrayOfPackageDoc[i])) {
/* 178 */         this.currentPackage = arrayOfPackageDoc[i];
/*     */ 
/* 180 */         buildChildren(paramXMLNode, localContent);
/*     */       }
/*     */     }
/* 183 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildPackageHeader(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 193 */     String str = parsePackageName(this.currentPackage.name());
/* 194 */     if (!this.printedPackageHeaders.contains(str)) {
/* 195 */       this.writer.addPackageName(this.currentPackage, 
/* 196 */         parsePackageName(this.currentPackage
/* 196 */         .name()), paramContent);
/* 197 */       this.printedPackageHeaders.add(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildClassConstantSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 210 */     ClassDoc[] arrayOfClassDoc = this.currentPackage.name().length() > 0 ? this.currentPackage
/* 209 */       .allClasses() : this.configuration.classDocCatalog
/* 210 */       .allClasses("<Unnamed>");
/*     */ 
/* 212 */     Arrays.sort(arrayOfClassDoc);
/* 213 */     Content localContent = this.writer.getClassConstantHeader();
/* 214 */     for (int i = 0; i < arrayOfClassDoc.length; i++)
/* 215 */       if ((this.classDocsWithConstFields.contains(arrayOfClassDoc[i])) && 
/* 216 */         (arrayOfClassDoc[i]
/* 216 */         .isIncluded()))
/*     */       {
/* 219 */         this.currentClass = arrayOfClassDoc[i];
/*     */ 
/* 221 */         buildChildren(paramXMLNode, localContent);
/*     */       }
/* 223 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildConstantMembers(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 234 */     new ConstantFieldBuilder(this.currentClass).buildMembersSummary(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   private boolean hasConstantField(PackageDoc paramPackageDoc)
/*     */   {
/*     */     ClassDoc[] arrayOfClassDoc;
/* 245 */     if (paramPackageDoc.name().length() > 0)
/* 246 */       arrayOfClassDoc = paramPackageDoc.allClasses();
/*     */     else {
/* 248 */       arrayOfClassDoc = this.configuration.classDocCatalog.allClasses("<Unnamed>");
/*     */     }
/*     */ 
/* 251 */     boolean bool = false;
/* 252 */     for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 253 */       if ((arrayOfClassDoc[i].isIncluded()) && (hasConstantField(arrayOfClassDoc[i]))) {
/* 254 */         bool = true;
/*     */       }
/*     */     }
/* 257 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean hasConstantField(ClassDoc paramClassDoc)
/*     */   {
/* 267 */     VisibleMemberMap localVisibleMemberMap = new VisibleMemberMap(paramClassDoc, 2, this.configuration);
/*     */ 
/* 269 */     List localList = localVisibleMemberMap.getLeafClassMembers(this.configuration);
/* 270 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 271 */       FieldDoc localFieldDoc = (FieldDoc)localIterator.next();
/* 272 */       if (localFieldDoc.constantValueExpression() != null) {
/* 273 */         this.classDocsWithConstFields.add(paramClassDoc);
/* 274 */         return true;
/*     */       }
/*     */     }
/* 277 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean hasPrintedPackageIndex(String paramString)
/*     */   {
/* 287 */     String[] arrayOfString = (String[])this.printedPackageHeaders.toArray(new String[0]);
/* 288 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 289 */       if (paramString.startsWith(arrayOfString[i])) {
/* 290 */         return true;
/*     */       }
/*     */     }
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */   private String parsePackageName(String paramString)
/*     */   {
/* 377 */     int i = -1;
/* 378 */     for (int j = 0; j < 2; j++) {
/* 379 */       i = paramString.indexOf(".", i + 1);
/*     */     }
/* 381 */     if (i != -1) {
/* 382 */       paramString = paramString.substring(0, i);
/*     */     }
/* 384 */     return paramString;
/*     */   }
/*     */ 
/*     */   private class ConstantFieldBuilder
/*     */   {
/* 307 */     protected VisibleMemberMap visibleMemberMapFields = null;
/*     */ 
/* 312 */     protected VisibleMemberMap visibleMemberMapEnumConst = null;
/*     */     protected ClassDoc classdoc;
/*     */ 
/*     */     public ConstantFieldBuilder(ClassDoc arg2)
/*     */     {
/*     */       ClassDoc localClassDoc;
/* 324 */       this.classdoc = localClassDoc;
/* 325 */       this.visibleMemberMapFields = new VisibleMemberMap(localClassDoc, 2, ConstantsSummaryBuilder.this.configuration);
/*     */ 
/* 327 */       this.visibleMemberMapEnumConst = new VisibleMemberMap(localClassDoc, 1, ConstantsSummaryBuilder.this.configuration);
/*     */     }
/*     */ 
/*     */     protected void buildMembersSummary(XMLNode paramXMLNode, Content paramContent)
/*     */     {
/* 339 */       ArrayList localArrayList = new ArrayList(members());
/* 340 */       if (localArrayList.size() > 0) {
/* 341 */         Collections.sort(localArrayList);
/* 342 */         ConstantsSummaryBuilder.this.writer.addConstantMembers(this.classdoc, localArrayList, paramContent);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected List<FieldDoc> members()
/*     */     {
/* 351 */       List localList = this.visibleMemberMapFields.getLeafClassMembers(ConstantsSummaryBuilder.this.configuration);
/* 352 */       localList.addAll(this.visibleMemberMapEnumConst.getLeafClassMembers(ConstantsSummaryBuilder.this.configuration));
/*     */       Iterator localIterator;
/* 355 */       if (localList != null)
/* 356 */         localIterator = localList.iterator();
/*     */       else {
/* 358 */         return null;
/*     */       }
/* 360 */       LinkedList localLinkedList = new LinkedList();
/*     */ 
/* 362 */       while (localIterator.hasNext()) {
/* 363 */         FieldDoc localFieldDoc = (FieldDoc)localIterator.next();
/* 364 */         if (localFieldDoc.constantValue() != null) {
/* 365 */           localLinkedList.add(localFieldDoc);
/*     */         }
/*     */       }
/* 368 */       return localLinkedList;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.ConstantsSummaryBuilder
 * JD-Core Version:    0.6.2
 */