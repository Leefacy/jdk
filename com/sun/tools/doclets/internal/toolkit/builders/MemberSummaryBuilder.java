/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class MemberSummaryBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   public static final String NAME = "MemberSummary";
/*     */   private final VisibleMemberMap[] visibleMemberMaps;
/*     */   private MemberSummaryWriter[] memberSummaryWriters;
/*     */   private final ClassDoc classDoc;
/*     */ 
/*     */   private MemberSummaryBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc)
/*     */   {
/*  77 */     super(paramContext);
/*  78 */     this.classDoc = paramClassDoc;
/*  79 */     this.visibleMemberMaps = new VisibleMemberMap[9];
/*     */ 
/*  81 */     for (int i = 0; i < 9; i++)
/*  82 */       this.visibleMemberMaps[i] = new VisibleMemberMap(paramClassDoc, i, this.configuration);
/*     */   }
/*     */ 
/*     */   public static MemberSummaryBuilder getInstance(ClassWriter paramClassWriter, AbstractBuilder.Context paramContext)
/*     */     throws Exception
/*     */   {
/* 101 */     MemberSummaryBuilder localMemberSummaryBuilder = new MemberSummaryBuilder(paramContext, paramClassWriter
/* 101 */       .getClassDoc());
/* 102 */     localMemberSummaryBuilder.memberSummaryWriters = new MemberSummaryWriter[9];
/*     */ 
/* 104 */     WriterFactory localWriterFactory = paramContext.configuration.getWriterFactory();
/* 105 */     for (int i = 0; i < 9; i++) {
/* 106 */       localMemberSummaryBuilder.memberSummaryWriters[i] = 
/* 107 */         (localMemberSummaryBuilder.visibleMemberMaps[i]
/* 107 */         .noVisibleMembers() ? null : localWriterFactory
/* 109 */         .getMemberSummaryWriter(paramClassWriter, i));
/*     */     }
/*     */ 
/* 111 */     return localMemberSummaryBuilder;
/*     */   }
/*     */ 
/*     */   public static MemberSummaryBuilder getInstance(AnnotationTypeWriter paramAnnotationTypeWriter, AbstractBuilder.Context paramContext)
/*     */     throws Exception
/*     */   {
/* 125 */     MemberSummaryBuilder localMemberSummaryBuilder = new MemberSummaryBuilder(paramContext, paramAnnotationTypeWriter
/* 125 */       .getAnnotationTypeDoc());
/* 126 */     localMemberSummaryBuilder.memberSummaryWriters = new MemberSummaryWriter[9];
/*     */ 
/* 128 */     WriterFactory localWriterFactory = paramContext.configuration.getWriterFactory();
/* 129 */     for (int i = 0; i < 9; i++) {
/* 130 */       localMemberSummaryBuilder.memberSummaryWriters[i] = 
/* 131 */         (localMemberSummaryBuilder.visibleMemberMaps[i]
/* 131 */         .noVisibleMembers() ? null : localWriterFactory
/* 133 */         .getMemberSummaryWriter(paramAnnotationTypeWriter, i));
/*     */     }
/*     */ 
/* 136 */     return localMemberSummaryBuilder;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 143 */     return "MemberSummary";
/*     */   }
/*     */ 
/*     */   public VisibleMemberMap getVisibleMemberMap(int paramInt)
/*     */   {
/* 155 */     return this.visibleMemberMaps[paramInt];
/*     */   }
/*     */ 
/*     */   public MemberSummaryWriter getMemberSummaryWriter(int paramInt)
/*     */   {
/* 167 */     return this.memberSummaryWriters[paramInt];
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> members(int paramInt)
/*     */   {
/* 180 */     return this.visibleMemberMaps[paramInt].getLeafClassMembers(this.configuration);
/*     */   }
/*     */ 
/*     */   public boolean hasMembersToDocument()
/*     */   {
/* 189 */     if ((this.classDoc instanceof AnnotationTypeDoc)) {
/* 190 */       return ((AnnotationTypeDoc)this.classDoc).elements().length > 0;
/*     */     }
/* 192 */     for (int i = 0; i < 9; i++) {
/* 193 */       VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[i];
/* 194 */       if (!localVisibleMemberMap.noVisibleMembers()) {
/* 195 */         return true;
/*     */       }
/*     */     }
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   public void buildEnumConstantsSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 208 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[1];
/*     */ 
/* 210 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[1];
/*     */ 
/* 212 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, false, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeFieldsSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 222 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[5];
/*     */ 
/* 224 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[5];
/*     */ 
/* 226 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, false, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeOptionalMemberSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 236 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[6];
/*     */ 
/* 238 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[6];
/*     */ 
/* 240 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, false, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeRequiredMemberSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 250 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[7];
/*     */ 
/* 252 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[7];
/*     */ 
/* 254 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, false, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildFieldsSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 264 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[2];
/*     */ 
/* 266 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[2];
/*     */ 
/* 268 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, true, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPropertiesSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 275 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[8];
/*     */ 
/* 277 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[8];
/*     */ 
/* 279 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, true, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildNestedClassesSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 289 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[0];
/*     */ 
/* 291 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[0];
/*     */ 
/* 293 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, true, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMethodsSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 303 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[4];
/*     */ 
/* 305 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[4];
/*     */ 
/* 307 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, true, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildConstructorsSummary(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 317 */     MemberSummaryWriter localMemberSummaryWriter = this.memberSummaryWriters[3];
/*     */ 
/* 319 */     VisibleMemberMap localVisibleMemberMap = this.visibleMemberMaps[3];
/*     */ 
/* 321 */     addSummary(localMemberSummaryWriter, localVisibleMemberMap, false, paramContent);
/*     */   }
/*     */ 
/*     */   private void buildSummary(MemberSummaryWriter paramMemberSummaryWriter, VisibleMemberMap paramVisibleMemberMap, LinkedList<Content> paramLinkedList)
/*     */   {
/* 333 */     ArrayList localArrayList = new ArrayList(paramVisibleMemberMap.getLeafClassMembers(this.configuration));
/*     */ 
/* 335 */     if (localArrayList.size() > 0) {
/* 336 */       Collections.sort(localArrayList);
/* 337 */       LinkedList localLinkedList = new LinkedList();
/* 338 */       for (int i = 0; i < localArrayList.size(); i++) {
/* 339 */         ProgramElementDoc localProgramElementDoc1 = (ProgramElementDoc)localArrayList.get(i);
/*     */ 
/* 341 */         ProgramElementDoc localProgramElementDoc2 = paramVisibleMemberMap
/* 341 */           .getPropertyMemberDoc(localProgramElementDoc1);
/*     */ 
/* 342 */         if (localProgramElementDoc2 != null) {
/* 343 */           processProperty(paramVisibleMemberMap, localProgramElementDoc1, localProgramElementDoc2);
/*     */         }
/* 345 */         Tag[] arrayOfTag = localProgramElementDoc1.firstSentenceTags();
/* 346 */         if (((localProgramElementDoc1 instanceof MethodDoc)) && (arrayOfTag.length == 0))
/*     */         {
/* 350 */           DocFinder.Output localOutput = DocFinder.search(new DocFinder.Input((MethodDoc)localProgramElementDoc1));
/*     */ 
/* 351 */           if ((localOutput.holder != null) && 
/* 352 */             (localOutput.holder
/* 352 */             .firstSentenceTags().length > 0)) {
/* 353 */             arrayOfTag = localOutput.holder.firstSentenceTags();
/*     */           }
/*     */         }
/* 356 */         paramMemberSummaryWriter.addMemberSummary(this.classDoc, localProgramElementDoc1, arrayOfTag, localLinkedList, i);
/*     */       }
/*     */ 
/* 359 */       paramLinkedList.add(paramMemberSummaryWriter.getSummaryTableTree(this.classDoc, localLinkedList));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processProperty(VisibleMemberMap paramVisibleMemberMap, ProgramElementDoc paramProgramElementDoc1, ProgramElementDoc paramProgramElementDoc2)
/*     */   {
/* 378 */     StringBuilder localStringBuilder = new StringBuilder();
/* 379 */     boolean bool1 = isSetter(paramProgramElementDoc1);
/* 380 */     boolean bool2 = isGetter(paramProgramElementDoc1);
/* 381 */     if ((bool2) || (bool1))
/*     */     {
/* 383 */       if (bool1) {
/* 384 */         localStringBuilder.append(
/* 385 */           MessageFormat.format(this.configuration
/* 386 */           .getText("doclet.PropertySetterWithName"), 
/* 386 */           new Object[] { 
/* 387 */           Util.propertyNameFromMethodName(this.configuration, paramProgramElementDoc1
/* 387 */           .name()) }));
/*     */       }
/* 389 */       if (bool2) {
/* 390 */         localStringBuilder.append(
/* 391 */           MessageFormat.format(this.configuration
/* 392 */           .getText("doclet.PropertyGetterWithName"), 
/* 392 */           new Object[] { 
/* 393 */           Util.propertyNameFromMethodName(this.configuration, paramProgramElementDoc1
/* 393 */           .name()) }));
/*     */       }
/* 395 */       if ((paramProgramElementDoc2.commentText() != null) && 
/* 396 */         (!paramProgramElementDoc2
/* 396 */         .commentText().isEmpty())) {
/* 397 */         localStringBuilder.append(" \n @propertyDescription ");
/*     */       }
/*     */     }
/* 400 */     localStringBuilder.append(paramProgramElementDoc2.commentText());
/*     */ 
/* 403 */     LinkedList localLinkedList = new LinkedList();
/* 404 */     String[] arrayOfString = { "@defaultValue", "@since" };
/* 405 */     for (String str2 : arrayOfString) {
/* 406 */       Tag[] arrayOfTag = paramProgramElementDoc2.tags(str2);
/* 407 */       if (arrayOfTag != null) {
/* 408 */         localLinkedList.addAll(Arrays.asList(arrayOfTag));
/*     */       }
/*     */     }
/* 411 */     for (??? = localLinkedList.iterator(); ((Iterator)???).hasNext(); ) { localObject2 = (Tag)((Iterator)???).next();
/* 412 */       localStringBuilder.append("\n")
/* 413 */         .append(((Tag)localObject2)
/* 413 */         .name())
/* 414 */         .append(" ")
/* 415 */         .append(((Tag)localObject2)
/* 415 */         .text());
/*     */     }
/*     */     Object localObject2;
/* 419 */     if ((!bool2) && (!bool1)) {
/* 420 */       ??? = (MethodDoc)paramVisibleMemberMap.getGetterForProperty(paramProgramElementDoc1);
/* 421 */       localObject2 = (MethodDoc)paramVisibleMemberMap.getSetterForProperty(paramProgramElementDoc1);
/*     */ 
/* 423 */       if ((null != ???) && 
/* 424 */         (localStringBuilder
/* 424 */         .indexOf("@see #" + ((MethodDoc)???)
/* 424 */         .name()) == -1)) {
/* 425 */         localStringBuilder.append("\n @see #")
/* 426 */           .append(((MethodDoc)???)
/* 426 */           .name())
/* 427 */           .append("() ");
/*     */       }
/*     */ 
/* 430 */       if ((null != localObject2) && 
/* 431 */         (localStringBuilder
/* 431 */         .indexOf("@see #" + ((MethodDoc)localObject2)
/* 431 */         .name()) == -1)) {
/* 432 */         String str1 = localObject2.parameters()[0].typeName();
/*     */ 
/* 434 */         str1 = str1.split("<")[0];
/* 435 */         if (str1.contains(".")) {
/* 436 */           str1 = str1.substring(str1.lastIndexOf(".") + 1);
/*     */         }
/* 438 */         localStringBuilder.append("\n @see #").append(((MethodDoc)localObject2).name());
/*     */ 
/* 440 */         if (localObject2.parameters()[0].type().asTypeVariable() == null) {
/* 441 */           localStringBuilder.append("(").append(str1).append(")");
/*     */         }
/* 443 */         localStringBuilder.append(" \n");
/*     */       }
/*     */     }
/* 446 */     paramProgramElementDoc1.setRawCommentText(localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   private boolean isGetter(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 455 */     String str = paramProgramElementDoc.name();
/* 456 */     return (str.startsWith("get")) || (str.startsWith("is"));
/*     */   }
/*     */ 
/*     */   private boolean isSetter(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 466 */     return paramProgramElementDoc.name().startsWith("set");
/*     */   }
/*     */ 
/*     */   private void buildInheritedSummary(MemberSummaryWriter paramMemberSummaryWriter, VisibleMemberMap paramVisibleMemberMap, LinkedList<Content> paramLinkedList)
/*     */   {
/* 478 */     Iterator localIterator = paramVisibleMemberMap.getVisibleClassesList().iterator();
/* 479 */     while (localIterator.hasNext()) {
/* 480 */       ClassDoc localClassDoc = (ClassDoc)localIterator.next();
/* 481 */       if (((localClassDoc.isPublic()) || 
/* 482 */         (Util.isLinkable(localClassDoc, this.configuration))) && 
/* 485 */         (localClassDoc != this.classDoc))
/*     */       {
/* 488 */         List localList = paramVisibleMemberMap.getMembersFor(localClassDoc);
/* 489 */         if (localList.size() > 0) {
/* 490 */           Collections.sort(localList);
/* 491 */           Content localContent1 = paramMemberSummaryWriter.getInheritedSummaryHeader(localClassDoc);
/* 492 */           Content localContent2 = paramMemberSummaryWriter.getInheritedSummaryLinksTree();
/* 493 */           for (int i = 0; i < localList.size(); i++) {
/* 494 */             paramMemberSummaryWriter.addInheritedMemberSummary(
/* 495 */               (localClassDoc
/* 495 */               .isPackagePrivate()) && 
/* 496 */               (!Util.isLinkable(localClassDoc, this.configuration)) ? 
/* 496 */               this.classDoc : localClassDoc, 
/* 498 */               (ProgramElementDoc)localList
/* 498 */               .get(i), 
/* 498 */               i == 0, i == localList
/* 500 */               .size() - 1, localContent2);
/*     */           }
/* 502 */           localContent1.addContent(localContent2);
/* 503 */           paramLinkedList.add(paramMemberSummaryWriter.getMemberTree(localContent1));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addSummary(MemberSummaryWriter paramMemberSummaryWriter, VisibleMemberMap paramVisibleMemberMap, boolean paramBoolean, Content paramContent)
/*     */   {
/* 519 */     LinkedList localLinkedList = new LinkedList();
/* 520 */     buildSummary(paramMemberSummaryWriter, paramVisibleMemberMap, localLinkedList);
/* 521 */     if (paramBoolean)
/* 522 */       buildInheritedSummary(paramMemberSummaryWriter, paramVisibleMemberMap, localLinkedList);
/* 523 */     if (!localLinkedList.isEmpty()) {
/* 524 */       Content localContent = paramMemberSummaryWriter.getMemberSummaryHeader(this.classDoc, paramContent);
/*     */ 
/* 526 */       for (int i = 0; i < localLinkedList.size(); i++) {
/* 527 */         localContent.addContent((Content)localLinkedList.get(i));
/*     */       }
/* 529 */       paramContent.addContent(paramMemberSummaryWriter.getMemberTree(localContent));
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.MemberSummaryBuilder
 * JD-Core Version:    0.6.2
 */