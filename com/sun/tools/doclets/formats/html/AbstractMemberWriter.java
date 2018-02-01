/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlAttr;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.DeprecatedTaglet;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MethodTypes;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractMemberWriter
/*     */ {
/*     */   protected final ConfigurationImpl configuration;
/*     */   protected final SubWriterHolderWriter writer;
/*     */   protected final ClassDoc classdoc;
/*  55 */   protected Map<String, Integer> typeMap = new LinkedHashMap();
/*  56 */   protected Set<MethodTypes> methodTypes = EnumSet.noneOf(MethodTypes.class);
/*  57 */   private int methodTypesOr = 0;
/*     */   public final boolean nodepr;
/*  60 */   protected boolean printedSummaryHeader = false;
/*     */ 
/*     */   public AbstractMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  63 */     this.configuration = paramSubWriterHolderWriter.configuration;
/*  64 */     this.writer = paramSubWriterHolderWriter;
/*  65 */     this.nodepr = this.configuration.nodeprecated;
/*  66 */     this.classdoc = paramClassDoc;
/*     */   }
/*     */ 
/*     */   public AbstractMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  70 */     this(paramSubWriterHolderWriter, null);
/*     */   }
/*     */ 
/*     */   public abstract void addSummaryLabel(Content paramContent);
/*     */ 
/*     */   public abstract String getTableSummary();
/*     */ 
/*     */   public abstract Content getCaption();
/*     */ 
/*     */   public abstract String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc);
/*     */ 
/*     */   public abstract void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent);
/*     */ 
/*     */   public abstract void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent);
/*     */ 
/*     */   public abstract void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent);
/*     */ 
/*     */   protected abstract void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent);
/*     */ 
/*     */   protected void addSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 146 */     addSummaryLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, paramProgramElementDoc, paramContent);
/*     */   }
/*     */ 
/*     */   protected abstract void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent);
/*     */ 
/*     */   protected abstract void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent);
/*     */ 
/*     */   protected abstract Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc);
/*     */ 
/*     */   protected abstract Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean);
/*     */ 
/*     */   protected abstract void addNavDetailLink(boolean paramBoolean, Content paramContent);
/*     */ 
/*     */   protected void addName(String paramString, Content paramContent)
/*     */   {
/* 202 */     paramContent.addContent(paramString);
/*     */   }
/*     */ 
/*     */   protected String modifierString(MemberDoc paramMemberDoc)
/*     */   {
/* 213 */     int i = paramMemberDoc.modifierSpecifier();
/* 214 */     int j = 288;
/* 215 */     return Modifier.toString(i & (j ^ 0xFFFFFFFF));
/*     */   }
/*     */ 
/*     */   protected String typeString(MemberDoc paramMemberDoc) {
/* 219 */     String str = "";
/* 220 */     if ((paramMemberDoc instanceof MethodDoc))
/* 221 */       str = ((MethodDoc)paramMemberDoc).returnType().toString();
/* 222 */     else if ((paramMemberDoc instanceof FieldDoc)) {
/* 223 */       str = ((FieldDoc)paramMemberDoc).type().toString();
/*     */     }
/* 225 */     return str;
/*     */   }
/*     */ 
/*     */   protected void addModifiers(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 235 */     String str = modifierString(paramMemberDoc);
/*     */ 
/* 238 */     if (((paramMemberDoc.isField()) || (paramMemberDoc.isMethod())) && ((this.writer instanceof ClassWriterImpl)))
/*     */     {
/* 240 */       if (((ClassWriterImpl)this.writer)
/* 240 */         .getClassDoc().isInterface())
/*     */       {
/* 248 */         str = (paramMemberDoc.isMethod()) && (((MethodDoc)paramMemberDoc).isDefault()) ? 
/* 247 */           Util.replaceText(str, "public", "default")
/* 247 */           .trim() : 
/* 248 */           Util.replaceText(str, "public", "")
/* 248 */           .trim();
/*     */       }
/*     */     }
/* 250 */     if (str.length() > 0) {
/* 251 */       paramContent.addContent(str);
/* 252 */       paramContent.addContent(this.writer.getSpace());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String makeSpace(int paramInt) {
/* 257 */     if (paramInt <= 0) {
/* 258 */       return "";
/*     */     }
/* 260 */     StringBuilder localStringBuilder = new StringBuilder(paramInt);
/* 261 */     for (int i = 0; i < paramInt; i++) {
/* 262 */       localStringBuilder.append(' ');
/*     */     }
/* 264 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected void addModifierAndType(ProgramElementDoc paramProgramElementDoc, Type paramType, Content paramContent)
/*     */   {
/* 276 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.CODE);
/* 277 */     addModifier(paramProgramElementDoc, localHtmlTree);
/* 278 */     if (paramType == null) {
/* 279 */       if (paramProgramElementDoc.isClass())
/* 280 */         localHtmlTree.addContent("class");
/*     */       else {
/* 282 */         localHtmlTree.addContent("interface");
/*     */       }
/* 284 */       localHtmlTree.addContent(this.writer.getSpace());
/*     */     }
/* 286 */     else if (((paramProgramElementDoc instanceof ExecutableMemberDoc)) && 
/* 287 */       (((ExecutableMemberDoc)paramProgramElementDoc)
/* 287 */       .typeParameters().length > 0)) {
/* 288 */       Content localContent = ((AbstractExecutableMemberWriter)this).getTypeParameters((ExecutableMemberDoc)paramProgramElementDoc);
/*     */ 
/* 290 */       localHtmlTree.addContent(localContent);
/*     */ 
/* 292 */       if (localContent.charCount() > 10)
/* 293 */         localHtmlTree.addContent(new HtmlTree(HtmlTag.BR));
/*     */       else {
/* 295 */         localHtmlTree.addContent(this.writer.getSpace());
/*     */       }
/* 297 */       localHtmlTree.addContent(this.writer
/* 298 */         .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SUMMARY_RETURN_TYPE, paramType)));
/*     */     }
/*     */     else
/*     */     {
/* 301 */       localHtmlTree.addContent(this.writer
/* 302 */         .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SUMMARY_RETURN_TYPE, paramType)));
/*     */     }
/*     */ 
/* 307 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   private void addModifier(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 317 */     if (paramProgramElementDoc.isProtected()) {
/* 318 */       paramContent.addContent("protected ");
/* 319 */     } else if (paramProgramElementDoc.isPrivate()) {
/* 320 */       paramContent.addContent("private ");
/* 321 */     } else if (!paramProgramElementDoc.isPublic()) {
/* 322 */       paramContent.addContent(this.configuration.getText("doclet.Package_private"));
/* 323 */       paramContent.addContent(" ");
/*     */     }
/* 325 */     if (paramProgramElementDoc.isMethod()) {
/* 326 */       if ((!paramProgramElementDoc.containingClass().isInterface()) && 
/* 327 */         (((MethodDoc)paramProgramElementDoc)
/* 327 */         .isAbstract())) {
/* 328 */         paramContent.addContent("abstract ");
/*     */       }
/*     */ 
/* 335 */       if (((MethodDoc)paramProgramElementDoc).isDefault()) {
/* 336 */         paramContent.addContent("default ");
/*     */       }
/*     */     }
/* 339 */     if (paramProgramElementDoc.isStatic())
/* 340 */       paramContent.addContent("static ");
/*     */   }
/*     */ 
/*     */   protected void addDeprecatedInfo(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 351 */     Content localContent1 = new DeprecatedTaglet().getTagletOutput(paramProgramElementDoc, this.writer
/* 352 */       .getTagletWriterInstance(false));
/*     */ 
/* 353 */     if (!localContent1.isEmpty()) {
/* 354 */       Content localContent2 = localContent1;
/* 355 */       HtmlTree localHtmlTree = HtmlTree.DIV(HtmlStyle.block, localContent2);
/* 356 */       paramContent.addContent(localHtmlTree);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addComment(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 367 */     if (paramProgramElementDoc.inlineTags().length > 0)
/* 368 */       this.writer.addInlineComment(paramProgramElementDoc, paramContent);
/*     */   }
/*     */ 
/*     */   protected String name(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 373 */     return paramProgramElementDoc.name();
/*     */   }
/*     */ 
/*     */   protected Content getHead(MemberDoc paramMemberDoc)
/*     */   {
/* 383 */     StringContent localStringContent = new StringContent(paramMemberDoc.name());
/* 384 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.MEMBER_HEADING, localStringContent);
/* 385 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected boolean isInherited(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 397 */     if ((paramProgramElementDoc.isPrivate()) || ((paramProgramElementDoc.isPackagePrivate()) && 
/* 398 */       (!paramProgramElementDoc
/* 398 */       .containingPackage().equals(this.classdoc.containingPackage())))) {
/* 399 */       return false;
/*     */     }
/* 401 */     return true;
/*     */   }
/*     */ 
/*     */   protected void addDeprecatedAPI(List<Doc> paramList, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent)
/*     */   {
/* 415 */     if (paramList.size() > 0) {
/* 416 */       HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.deprecatedSummary, 0, 3, 0, paramString2, this.writer
/* 417 */         .getTableCaption(this.configuration
/* 417 */         .getResource(paramString1)));
/*     */ 
/* 418 */       localHtmlTree1.addContent(this.writer.getSummaryTableHeader(paramArrayOfString, "col"));
/* 419 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 420 */       for (int i = 0; i < paramList.size(); i++) {
/* 421 */         localObject = (ProgramElementDoc)paramList.get(i);
/* 422 */         HtmlTree localHtmlTree4 = HtmlTree.TD(HtmlStyle.colOne, getDeprecatedLink((ProgramElementDoc)localObject));
/* 423 */         if (((ProgramElementDoc)localObject).tags("deprecated").length > 0)
/* 424 */           this.writer.addInlineDeprecatedComment((Doc)localObject, localObject
/* 425 */             .tags("deprecated")[
/* 425 */             0], localHtmlTree4);
/* 426 */         HtmlTree localHtmlTree5 = HtmlTree.TR(localHtmlTree4);
/* 427 */         if (i % 2 == 0)
/* 428 */           localHtmlTree5.addStyle(HtmlStyle.altColor);
/*     */         else
/* 430 */           localHtmlTree5.addStyle(HtmlStyle.rowColor);
/* 431 */         localHtmlTree2.addContent(localHtmlTree5);
/*     */       }
/* 433 */       localHtmlTree1.addContent(localHtmlTree2);
/* 434 */       HtmlTree localHtmlTree3 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/* 435 */       Object localObject = HtmlTree.UL(HtmlStyle.blockList, localHtmlTree3);
/* 436 */       paramContent.addContent((Content)localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addUseInfo(List<? extends ProgramElementDoc> paramList, Content paramContent1, String paramString, Content paramContent2)
/*     */   {
/* 450 */     if (paramList == null) {
/* 451 */       return;
/*     */     }
/* 453 */     List<? extends ProgramElementDoc> localList = paramList;
/* 454 */     int i = 0;
/* 455 */     if (localList.size() > 0) {
/* 456 */       HtmlTree localHtmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, paramString, this.writer
/* 457 */         .getTableCaption(paramContent1));
/*     */ 
/* 458 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 459 */       Iterator localIterator = localList.iterator();
/* 460 */       for (int j = 0; localIterator.hasNext(); j++) {
/* 461 */         ProgramElementDoc localProgramElementDoc = (ProgramElementDoc)localIterator.next();
/* 462 */         ClassDoc localClassDoc = localProgramElementDoc.containingClass();
/* 463 */         if (i == 0) {
/* 464 */           localHtmlTree1.addContent(this.writer.getSummaryTableHeader(
/* 465 */             getSummaryTableHeader(localProgramElementDoc), 
/* 465 */             "col"));
/* 466 */           i = 1;
/*     */         }
/* 468 */         HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.TR);
/* 469 */         if (j % 2 == 0)
/* 470 */           localHtmlTree3.addStyle(HtmlStyle.altColor);
/*     */         else {
/* 472 */           localHtmlTree3.addStyle(HtmlStyle.rowColor);
/*     */         }
/* 474 */         HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.TD);
/* 475 */         localHtmlTree4.addStyle(HtmlStyle.colFirst);
/* 476 */         this.writer.addSummaryType(this, localProgramElementDoc, localHtmlTree4);
/* 477 */         localHtmlTree3.addContent(localHtmlTree4);
/* 478 */         HtmlTree localHtmlTree5 = new HtmlTree(HtmlTag.TD);
/* 479 */         localHtmlTree5.addStyle(HtmlStyle.colLast);
/* 480 */         if ((localClassDoc != null) && (!(localProgramElementDoc instanceof ConstructorDoc)) && (!(localProgramElementDoc instanceof ClassDoc)))
/*     */         {
/* 482 */           HtmlTree localHtmlTree6 = new HtmlTree(HtmlTag.SPAN);
/* 483 */           localHtmlTree6.addStyle(HtmlStyle.typeNameLabel);
/* 484 */           localHtmlTree6.addContent(localClassDoc.name() + ".");
/* 485 */           localHtmlTree5.addContent(localHtmlTree6);
/*     */         }
/* 487 */         addSummaryLink((localProgramElementDoc instanceof ClassDoc) ? LinkInfoImpl.Kind.CLASS_USE : LinkInfoImpl.Kind.MEMBER, localClassDoc, localProgramElementDoc, localHtmlTree5);
/*     */ 
/* 490 */         this.writer.addSummaryLinkComment(this, localProgramElementDoc, localHtmlTree5);
/* 491 */         localHtmlTree3.addContent(localHtmlTree5);
/* 492 */         localHtmlTree2.addContent(localHtmlTree3);
/*     */       }
/* 494 */       localHtmlTree1.addContent(localHtmlTree2);
/* 495 */       paramContent2.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addNavDetailLink(List<?> paramList, Content paramContent)
/*     */   {
/* 506 */     addNavDetailLink(paramList.size() > 0, paramContent);
/*     */   }
/*     */ 
/*     */   protected void addNavSummaryLink(List<?> paramList, VisibleMemberMap paramVisibleMemberMap, Content paramContent)
/*     */   {
/* 518 */     if (paramList.size() > 0) {
/* 519 */       paramContent.addContent(getNavSummaryLink(null, true));
/* 520 */       return;
/*     */     }
/* 522 */     ClassDoc localClassDoc = this.classdoc.superclass();
/* 523 */     while (localClassDoc != null) {
/* 524 */       List localList = paramVisibleMemberMap.getMembersFor(localClassDoc);
/* 525 */       if (localList.size() > 0) {
/* 526 */         paramContent.addContent(getNavSummaryLink(localClassDoc, true));
/* 527 */         return;
/*     */       }
/* 529 */       localClassDoc = localClassDoc.superclass();
/*     */     }
/* 531 */     paramContent.addContent(getNavSummaryLink(null, false));
/*     */   }
/*     */ 
/*     */   protected void serialWarning(SourcePosition paramSourcePosition, String paramString1, String paramString2, String paramString3) {
/* 535 */     if (this.configuration.serialwarn)
/* 536 */       this.configuration.getDocletSpecificMsg().warning(paramSourcePosition, paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */ 
/*     */   public ProgramElementDoc[] eligibleMembers(ProgramElementDoc[] paramArrayOfProgramElementDoc)
/*     */   {
/* 541 */     return this.nodepr ? Util.excludeDeprecatedMembers(paramArrayOfProgramElementDoc) : paramArrayOfProgramElementDoc;
/*     */   }
/*     */ 
/*     */   public void addMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Tag[] paramArrayOfTag, List<Content> paramList, int paramInt)
/*     */   {
/* 555 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.TD);
/* 556 */     localHtmlTree1.addStyle(HtmlStyle.colFirst);
/* 557 */     this.writer.addSummaryType(this, paramProgramElementDoc, localHtmlTree1);
/* 558 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.TD);
/* 559 */     setSummaryColumnStyle(localHtmlTree2);
/* 560 */     addSummaryLink(paramClassDoc, paramProgramElementDoc, localHtmlTree2);
/* 561 */     this.writer.addSummaryLinkComment(this, paramProgramElementDoc, paramArrayOfTag, localHtmlTree2);
/* 562 */     HtmlTree localHtmlTree3 = HtmlTree.TR(localHtmlTree1);
/* 563 */     localHtmlTree3.addContent(localHtmlTree2);
/* 564 */     if (((paramProgramElementDoc instanceof MethodDoc)) && (!paramProgramElementDoc.isAnnotationTypeElement()))
/*     */     {
/* 566 */       int i = paramProgramElementDoc.isStatic() ? MethodTypes.STATIC.value() : MethodTypes.INSTANCE
/* 566 */         .value();
/* 567 */       if (paramProgramElementDoc.containingClass().isInterface())
/*     */       {
/* 570 */         i = ((MethodDoc)paramProgramElementDoc).isAbstract() ? i | MethodTypes.ABSTRACT
/* 569 */           .value() : i | MethodTypes.DEFAULT
/* 570 */           .value();
/*     */       }
/*     */       else
/*     */       {
/* 574 */         i = ((MethodDoc)paramProgramElementDoc).isAbstract() ? i | MethodTypes.ABSTRACT
/* 573 */           .value() : i | MethodTypes.CONCRETE
/* 574 */           .value();
/*     */       }
/* 576 */       if ((Util.isDeprecated(paramProgramElementDoc)) || (Util.isDeprecated(this.classdoc))) {
/* 577 */         i |= MethodTypes.DEPRECATED.value();
/*     */       }
/* 579 */       this.methodTypesOr |= i;
/* 580 */       String str = "i" + paramInt;
/* 581 */       this.typeMap.put(str, Integer.valueOf(i));
/* 582 */       localHtmlTree3.addAttr(HtmlAttr.ID, str);
/*     */     }
/* 584 */     if (paramInt % 2 == 0)
/* 585 */       localHtmlTree3.addStyle(HtmlStyle.altColor);
/*     */     else
/* 587 */       localHtmlTree3.addStyle(HtmlStyle.rowColor);
/* 588 */     paramList.add(localHtmlTree3);
/*     */   }
/*     */ 
/*     */   public boolean showTabs()
/*     */   {
/* 599 */     for (MethodTypes localMethodTypes : EnumSet.allOf(MethodTypes.class)) {
/* 600 */       int i = localMethodTypes.value();
/* 601 */       if ((i & this.methodTypesOr) == i) {
/* 602 */         this.methodTypes.add(localMethodTypes);
/*     */       }
/*     */     }
/* 605 */     boolean bool = this.methodTypes.size() > 1;
/* 606 */     if (bool) {
/* 607 */       this.methodTypes.add(MethodTypes.ALL);
/*     */     }
/* 609 */     return bool;
/*     */   }
/*     */ 
/*     */   public void setSummaryColumnStyle(HtmlTree paramHtmlTree)
/*     */   {
/* 618 */     paramHtmlTree.addStyle(HtmlStyle.colLast);
/*     */   }
/*     */ 
/*     */   public void addInheritedMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, boolean paramBoolean1, boolean paramBoolean2, Content paramContent)
/*     */   {
/* 633 */     this.writer.addInheritedMemberSummary(this, paramClassDoc, paramProgramElementDoc, paramBoolean1, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getInheritedSummaryHeader(ClassDoc paramClassDoc)
/*     */   {
/* 644 */     Content localContent = this.writer.getMemberTreeHeader();
/* 645 */     this.writer.addInheritedSummaryHeader(this, paramClassDoc, localContent);
/* 646 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getInheritedSummaryLinksTree()
/*     */   {
/* 655 */     return new HtmlTree(HtmlTag.CODE);
/*     */   }
/*     */ 
/*     */   public Content getSummaryTableTree(ClassDoc paramClassDoc, List<Content> paramList)
/*     */   {
/* 666 */     return this.writer.getSummaryTableTree(this, paramClassDoc, paramList, showTabs());
/*     */   }
/*     */ 
/*     */   public Content getMemberTree(Content paramContent)
/*     */   {
/* 676 */     return this.writer.getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getMemberTree(Content paramContent, boolean paramBoolean)
/*     */   {
/* 687 */     if (paramBoolean) {
/* 688 */       return HtmlTree.UL(HtmlStyle.blockListLast, paramContent);
/*     */     }
/* 690 */     return HtmlTree.UL(HtmlStyle.blockList, paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AbstractMemberWriter
 * JD-Core Version:    0.6.2
 */