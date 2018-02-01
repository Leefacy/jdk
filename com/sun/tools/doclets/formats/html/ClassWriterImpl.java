/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.MemberSummaryBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.ParamTaglet;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import com.sun.tools.javadoc.RootDocImpl;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ClassWriterImpl extends SubWriterHolderWriter
/*     */   implements ClassWriter
/*     */ {
/*     */   protected final ClassDoc classDoc;
/*     */   protected final ClassTree classtree;
/*     */   protected final ClassDoc prev;
/*     */   protected final ClassDoc next;
/*     */ 
/*     */   public ClassWriterImpl(ConfigurationImpl paramConfigurationImpl, ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, ClassDoc paramClassDoc3, ClassTree paramClassTree)
/*     */     throws IOException
/*     */   {
/*  79 */     super(paramConfigurationImpl, DocPath.forClass(paramClassDoc1));
/*  80 */     this.classDoc = paramClassDoc1;
/*  81 */     paramConfigurationImpl.currentcd = paramClassDoc1;
/*  82 */     this.classtree = paramClassTree;
/*  83 */     this.prev = paramClassDoc2;
/*  84 */     this.next = paramClassDoc3;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkPackage()
/*     */   {
/*  93 */     Content localContent = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */ 
/*  95 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/*  96 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClass()
/*     */   {
/* 105 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.classLabel);
/* 106 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkClassUse()
/*     */   {
/* 115 */     Content localContent = getHyperLink(DocPaths.CLASS_USE.resolve(this.filename), this.useLabel);
/* 116 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 117 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkPrevious()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 127 */     if (this.prev != null) {
/* 128 */       Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.prev)
/* 130 */         .label(this.prevclassLabel)
/* 130 */         .strong(true));
/* 131 */       localHtmlTree = HtmlTree.LI(localContent);
/*     */     }
/*     */     else {
/* 134 */       localHtmlTree = HtmlTree.LI(this.prevclassLabel);
/* 135 */     }return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getNavLinkNext()
/*     */   {
/*     */     HtmlTree localHtmlTree;
/* 145 */     if (this.next != null) {
/* 146 */       Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.next)
/* 148 */         .label(this.nextclassLabel)
/* 148 */         .strong(true));
/* 149 */       localHtmlTree = HtmlTree.LI(localContent);
/*     */     }
/*     */     else {
/* 152 */       localHtmlTree = HtmlTree.LI(this.nextclassLabel);
/* 153 */     }return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getHeader(String paramString)
/*     */   {
/* 161 */     String str1 = this.classDoc.containingPackage() != null ? this.classDoc
/* 161 */       .containingPackage().name() : "";
/* 162 */     String str2 = this.classDoc.name();
/* 163 */     HtmlTree localHtmlTree1 = getBody(true, getWindowTitle(str2));
/* 164 */     addTop(localHtmlTree1);
/* 165 */     addNavLinks(true, localHtmlTree1);
/* 166 */     localHtmlTree1.addContent(HtmlConstants.START_OF_CLASS_DATA);
/* 167 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 168 */     localHtmlTree2.addStyle(HtmlStyle.header);
/* 169 */     if (this.configuration.showProfiles) {
/* 170 */       localObject1 = "";
/* 171 */       int i = this.configuration.profiles.getProfile(getTypeNameForProfile(this.classDoc));
/* 172 */       if (i > 0) {
/* 173 */         localObject3 = new StringContent();
/* 174 */         for (int j = i; j < this.configuration.profiles.getProfileCount(); j++) {
/* 175 */           ((Content)localObject3).addContent((String)localObject1);
/* 176 */           ((Content)localObject3).addContent(Profile.lookup(j).name);
/* 177 */           localObject1 = ", ";
/*     */         }
/* 179 */         HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.subTitle, (Content)localObject3);
/* 180 */         localHtmlTree2.addContent(localHtmlTree3);
/*     */       }
/*     */     }
/* 183 */     if (str1.length() > 0) {
/* 184 */       localObject1 = new StringContent(str1);
/* 185 */       localObject2 = HtmlTree.DIV(HtmlStyle.subTitle, (Content)localObject1);
/* 186 */       localHtmlTree2.addContent((Content)localObject2);
/*     */     }
/* 188 */     Object localObject1 = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_HEADER, this.classDoc);
/*     */ 
/* 191 */     ((LinkInfoImpl)localObject1).linkToSelf = false;
/* 192 */     Object localObject2 = new StringContent(paramString);
/* 193 */     Object localObject3 = HtmlTree.HEADING(HtmlConstants.CLASS_PAGE_HEADING, true, HtmlStyle.title, (Content)localObject2);
/*     */ 
/* 195 */     ((Content)localObject3).addContent(getTypeParameterLinks((LinkInfoImpl)localObject1));
/* 196 */     localHtmlTree2.addContent((Content)localObject3);
/* 197 */     localHtmlTree1.addContent(localHtmlTree2);
/* 198 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public Content getClassContentHeader()
/*     */   {
/* 205 */     return getContentHeader();
/*     */   }
/*     */ 
/*     */   public void addFooter(Content paramContent)
/*     */   {
/* 212 */     paramContent.addContent(HtmlConstants.END_OF_CLASS_DATA);
/* 213 */     addNavLinks(false, paramContent);
/* 214 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */   public void printDocument(Content paramContent)
/*     */     throws IOException
/*     */   {
/* 221 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.classDoc), true, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getClassInfoTreeHeader()
/*     */   {
/* 229 */     return getMemberTreeHeader();
/*     */   }
/*     */ 
/*     */   public Content getClassInfo(Content paramContent)
/*     */   {
/* 236 */     return getMemberTree(HtmlStyle.description, paramContent);
/*     */   }
/*     */ 
/*     */   public void addClassSignature(String paramString, Content paramContent)
/*     */   {
/* 243 */     boolean bool = this.classDoc.isInterface();
/* 244 */     paramContent.addContent(new HtmlTree(HtmlTag.BR));
/* 245 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.PRE);
/* 246 */     addAnnotationInfo(this.classDoc, localHtmlTree);
/* 247 */     localHtmlTree.addContent(paramString);
/* 248 */     LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE, this.classDoc);
/*     */ 
/* 251 */     localLinkInfoImpl.linkToSelf = false;
/* 252 */     StringContent localStringContent = new StringContent(this.classDoc.name());
/* 253 */     Content localContent1 = getTypeParameterLinks(localLinkInfoImpl);
/* 254 */     if (this.configuration.linksource) {
/* 255 */       addSrcLink(this.classDoc, localStringContent, localHtmlTree);
/* 256 */       localHtmlTree.addContent(localContent1);
/*     */     } else {
/* 258 */       localObject = HtmlTree.SPAN(HtmlStyle.typeNameLabel, localStringContent);
/* 259 */       ((Content)localObject).addContent(localContent1);
/* 260 */       localHtmlTree.addContent((Content)localObject);
/*     */     }
/* 262 */     if (!bool) {
/* 263 */       localObject = Util.getFirstVisibleSuperClass(this.classDoc, this.configuration);
/*     */ 
/* 265 */       if (localObject != null) {
/* 266 */         localHtmlTree.addContent(DocletConstants.NL);
/* 267 */         localHtmlTree.addContent("extends ");
/* 268 */         Content localContent2 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE_PARENT_NAME, (Type)localObject));
/*     */ 
/* 271 */         localHtmlTree.addContent(localContent2);
/*     */       }
/*     */     }
/* 274 */     Object localObject = this.classDoc.interfaceTypes();
/* 275 */     if ((localObject != null) && (localObject.length > 0)) {
/* 276 */       int i = 0;
/* 277 */       for (int j = 0; j < localObject.length; j++) {
/* 278 */         ClassDoc localClassDoc = localObject[j].asClassDoc();
/* 279 */         if ((localClassDoc.isPublic()) || 
/* 280 */           (Util.isLinkable(localClassDoc, this.configuration)))
/*     */         {
/* 283 */           if (i == 0) {
/* 284 */             localHtmlTree.addContent(DocletConstants.NL);
/* 285 */             localHtmlTree.addContent(bool ? "extends " : "implements ");
/*     */           } else {
/* 287 */             localHtmlTree.addContent(", ");
/*     */           }
/* 289 */           Content localContent3 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE_PARENT_NAME, localObject[j]));
/*     */ 
/* 292 */           localHtmlTree.addContent(localContent3);
/* 293 */           i++;
/*     */         }
/*     */       }
/*     */     }
/* 296 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public void addClassDescription(Content paramContent)
/*     */   {
/* 303 */     if (!this.configuration.nocomment)
/*     */     {
/* 305 */       if (this.classDoc.inlineTags().length > 0)
/* 306 */         addInlineComment(this.classDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addClassTagInfo(Content paramContent)
/*     */   {
/* 315 */     if (!this.configuration.nocomment)
/*     */     {
/* 317 */       addTagsInfo(this.classDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Content getClassInheritenceTree(Type paramType)
/*     */   {
/* 329 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.UL);
/* 330 */     localHtmlTree1.addStyle(HtmlStyle.inheritance);
/* 331 */     Object localObject = null;
/*     */     Type localType;
/*     */     do
/*     */     {
/* 333 */       localType = Util.getFirstVisibleSuperClass((paramType instanceof ClassDoc) ? (ClassDoc)paramType : paramType
/* 334 */         .asClassDoc(), this.configuration);
/*     */ 
/* 336 */       if (localType != null) {
/* 337 */         HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.UL);
/* 338 */         localHtmlTree2.addStyle(HtmlStyle.inheritance);
/* 339 */         localHtmlTree2.addContent(getTreeForClassHelper(paramType));
/* 340 */         if (localObject != null)
/* 341 */           localHtmlTree2.addContent((Content)localObject);
/* 342 */         HtmlTree localHtmlTree3 = HtmlTree.LI(localHtmlTree2);
/* 343 */         localObject = localHtmlTree3;
/* 344 */         paramType = localType;
/*     */       }
/*     */       else {
/* 347 */         localHtmlTree1.addContent(getTreeForClassHelper(paramType));
/*     */       }
/*     */     }
/* 349 */     while (localType != null);
/* 350 */     if (localObject != null)
/* 351 */       localHtmlTree1.addContent((Content)localObject);
/* 352 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   private Content getTreeForClassHelper(Type paramType)
/*     */   {
/* 362 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/*     */     Content localContent;
/* 363 */     if (paramType.equals(this.classDoc)) {
/* 364 */       localContent = getTypeParameterLinks(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.TREE, this.classDoc));
/*     */ 
/* 367 */       if (this.configuration.shouldExcludeQualifier(this.classDoc
/* 368 */         .containingPackage().name())) {
/* 369 */         localHtmlTree.addContent(paramType.asClassDoc().name());
/* 370 */         localHtmlTree.addContent(localContent);
/*     */       } else {
/* 372 */         localHtmlTree.addContent(paramType.asClassDoc().qualifiedName());
/* 373 */         localHtmlTree.addContent(localContent);
/*     */       }
/*     */     } else {
/* 376 */       localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_TREE_PARENT, paramType)
/* 378 */         .label(this.configuration
/* 378 */         .getClassName(paramType
/* 378 */         .asClassDoc())));
/* 379 */       localHtmlTree.addContent(localContent);
/*     */     }
/* 381 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addClassTree(Content paramContent)
/*     */   {
/* 388 */     if (!this.classDoc.isClass()) {
/* 389 */       return;
/*     */     }
/* 391 */     paramContent.addContent(getClassInheritenceTree(this.classDoc));
/*     */   }
/*     */ 
/*     */   public void addTypeParamInfo(Content paramContent)
/*     */   {
/* 398 */     if (this.classDoc.typeParamTags().length > 0) {
/* 399 */       Content localContent = new ParamTaglet().getTagletOutput(this.classDoc, 
/* 400 */         getTagletWriterInstance(false));
/*     */ 
/* 401 */       HtmlTree localHtmlTree = HtmlTree.DL(localContent);
/* 402 */       paramContent.addContent(localHtmlTree);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addSubClassInfo(Content paramContent)
/*     */   {
/* 410 */     if (this.classDoc.isClass()) {
/* 411 */       if ((this.classDoc.qualifiedName().equals("java.lang.Object")) || 
/* 412 */         (this.classDoc
/* 412 */         .qualifiedName().equals("org.omg.CORBA.Object"))) {
/* 413 */         return;
/*     */       }
/* 415 */       List localList = this.classtree.subs(this.classDoc, false);
/* 416 */       if (localList.size() > 0) {
/* 417 */         Content localContent = getResource("doclet.Subclasses");
/*     */ 
/* 419 */         HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 420 */         HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 421 */         localHtmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.SUBCLASSES, localList));
/*     */ 
/* 423 */         paramContent.addContent(localHtmlTree2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addSubInterfacesInfo(Content paramContent)
/*     */   {
/* 432 */     if (this.classDoc.isInterface()) {
/* 433 */       List localList = this.classtree.allSubs(this.classDoc, false);
/* 434 */       if (localList.size() > 0) {
/* 435 */         Content localContent = getResource("doclet.Subinterfaces");
/*     */ 
/* 437 */         HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 438 */         HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 439 */         localHtmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.SUBINTERFACES, localList));
/*     */ 
/* 441 */         paramContent.addContent(localHtmlTree2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addInterfaceUsageInfo(Content paramContent)
/*     */   {
/* 450 */     if (!this.classDoc.isInterface()) {
/* 451 */       return;
/*     */     }
/* 453 */     if ((this.classDoc.qualifiedName().equals("java.lang.Cloneable")) || 
/* 454 */       (this.classDoc
/* 454 */       .qualifiedName().equals("java.io.Serializable"))) {
/* 455 */       return;
/*     */     }
/* 457 */     List localList = this.classtree.implementingclasses(this.classDoc);
/* 458 */     if (localList.size() > 0) {
/* 459 */       Content localContent = getResource("doclet.Implementing_Classes");
/*     */ 
/* 461 */       HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 462 */       HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 463 */       localHtmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.IMPLEMENTED_CLASSES, localList));
/*     */ 
/* 465 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addImplementedInterfacesInfo(Content paramContent)
/*     */   {
/* 475 */     List localList = Util.getAllInterfaces(this.classDoc, this.configuration);
/* 476 */     if ((this.classDoc.isClass()) && (localList.size() > 0)) {
/* 477 */       Content localContent = getResource("doclet.All_Implemented_Interfaces");
/*     */ 
/* 479 */       HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 480 */       HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 481 */       localHtmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.IMPLEMENTED_INTERFACES, localList));
/*     */ 
/* 483 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addSuperInterfacesInfo(Content paramContent)
/*     */   {
/* 493 */     List localList = Util.getAllInterfaces(this.classDoc, this.configuration);
/* 494 */     if ((this.classDoc.isInterface()) && (localList.size() > 0)) {
/* 495 */       Content localContent = getResource("doclet.All_Superinterfaces");
/*     */ 
/* 497 */       HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 498 */       HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 499 */       localHtmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.SUPER_INTERFACES, localList));
/*     */ 
/* 501 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNestedClassInfo(Content paramContent)
/*     */   {
/* 509 */     ClassDoc localClassDoc = this.classDoc.containingClass();
/* 510 */     if (localClassDoc != null)
/*     */     {
/*     */       Content localContent;
/* 512 */       if (localClassDoc.isInterface()) {
/* 513 */         localContent = getResource("doclet.Enclosing_Interface");
/*     */       }
/*     */       else {
/* 516 */         localContent = getResource("doclet.Enclosing_Class");
/*     */       }
/*     */ 
/* 519 */       HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 520 */       HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 521 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DD);
/* 522 */       localHtmlTree3.addContent(getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, localClassDoc)));
/*     */ 
/* 524 */       localHtmlTree2.addContent(localHtmlTree3);
/* 525 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addFunctionalInterfaceInfo(Content paramContent)
/*     */   {
/* 533 */     if (isFunctionalInterface()) {
/* 534 */       HtmlTree localHtmlTree1 = HtmlTree.DT(getResource("doclet.Functional_Interface"));
/* 535 */       HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/* 536 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DD);
/* 537 */       localHtmlTree3.addContent(getResource("doclet.Functional_Interface_Message"));
/* 538 */       localHtmlTree2.addContent(localHtmlTree3);
/* 539 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isFunctionalInterface() {
/* 544 */     if ((this.configuration.root instanceof RootDocImpl)) {
/* 545 */       RootDocImpl localRootDocImpl = (RootDocImpl)this.configuration.root;
/* 546 */       AnnotationDesc[] arrayOfAnnotationDesc1 = this.classDoc.annotations();
/* 547 */       for (AnnotationDesc localAnnotationDesc : arrayOfAnnotationDesc1) {
/* 548 */         if (localRootDocImpl.isFunctionalInterface(localAnnotationDesc)) {
/* 549 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 553 */     return false;
/*     */   }
/*     */ 
/*     */   public void addClassDeprecationInfo(Content paramContent)
/*     */   {
/* 560 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.HR);
/* 561 */     paramContent.addContent(localHtmlTree1);
/* 562 */     Tag[] arrayOfTag1 = this.classDoc.tags("deprecated");
/* 563 */     if (Util.isDeprecated(this.classDoc)) {
/* 564 */       HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 565 */       HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.block, localHtmlTree2);
/* 566 */       if (arrayOfTag1.length > 0) {
/* 567 */         Tag[] arrayOfTag2 = arrayOfTag1[0].inlineTags();
/* 568 */         if (arrayOfTag2.length > 0) {
/* 569 */           localHtmlTree3.addContent(getSpace());
/* 570 */           addInlineDeprecatedComment(this.classDoc, arrayOfTag1[0], localHtmlTree3);
/*     */         }
/*     */       }
/* 573 */       paramContent.addContent(localHtmlTree3);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Content getClassLinks(LinkInfoImpl.Kind paramKind, List<?> paramList)
/*     */   {
/* 585 */     Object[] arrayOfObject = paramList.toArray();
/* 586 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DD);
/* 587 */     for (int i = 0; i < paramList.size(); i++)
/*     */     {
/*     */       Object localObject;
/* 588 */       if (i > 0) {
/* 589 */         localObject = new StringContent(", ");
/* 590 */         localHtmlTree.addContent((Content)localObject);
/*     */       }
/* 592 */       if ((arrayOfObject[i] instanceof ClassDoc)) {
/* 593 */         localObject = getLink(new LinkInfoImpl(this.configuration, paramKind, (ClassDoc)arrayOfObject[i]));
/*     */ 
/* 595 */         localHtmlTree.addContent((Content)localObject);
/*     */       } else {
/* 597 */         localObject = getLink(new LinkInfoImpl(this.configuration, paramKind, (Type)arrayOfObject[i]));
/*     */ 
/* 599 */         localHtmlTree.addContent((Content)localObject);
/*     */       }
/*     */     }
/* 602 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 609 */     Content localContent = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */ 
/* 611 */     HtmlTree localHtmlTree = HtmlTree.LI(localContent);
/* 612 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addSummaryDetailLinks(Content paramContent)
/*     */   {
/*     */     try
/*     */     {
/* 622 */       HtmlTree localHtmlTree = HtmlTree.DIV(getNavSummaryLinks());
/* 623 */       localHtmlTree.addContent(getNavDetailLinks());
/* 624 */       paramContent.addContent(localHtmlTree);
/*     */     } catch (Exception localException) {
/* 626 */       localException.printStackTrace();
/* 627 */       throw new DocletAbortException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLinks()
/*     */     throws Exception
/*     */   {
/* 637 */     HtmlTree localHtmlTree1 = HtmlTree.LI(this.summaryLabel);
/* 638 */     localHtmlTree1.addContent(getSpace());
/* 639 */     HtmlTree localHtmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, localHtmlTree1);
/*     */ 
/* 641 */     MemberSummaryBuilder localMemberSummaryBuilder = (MemberSummaryBuilder)this.configuration
/* 641 */       .getBuilderFactory().getMemberSummaryBuilder(this);
/* 642 */     String[] arrayOfString = { "doclet.navNested", "doclet.navEnum", "doclet.navField", "doclet.navConstructor", "doclet.navMethod" };
/*     */ 
/* 646 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 647 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.LI);
/* 648 */       if ((i != 1) || (this.classDoc.isEnum()))
/*     */       {
/* 651 */         if ((i != 3) || (!this.classDoc.isEnum()))
/*     */         {
/* 656 */           AbstractMemberWriter localAbstractMemberWriter = (AbstractMemberWriter)localMemberSummaryBuilder
/* 656 */             .getMemberSummaryWriter(i);
/*     */ 
/* 657 */           if (localAbstractMemberWriter == null)
/* 658 */             localHtmlTree3.addContent(getResource(arrayOfString[i]));
/*     */           else {
/* 660 */             localAbstractMemberWriter.addNavSummaryLink(localMemberSummaryBuilder
/* 661 */               .members(i), 
/* 661 */               localMemberSummaryBuilder
/* 662 */               .getVisibleMemberMap(i), 
/* 662 */               localHtmlTree3);
/*     */           }
/* 664 */           if (i < arrayOfString.length - 1) {
/* 665 */             addNavGap(localHtmlTree3);
/*     */           }
/* 667 */           localHtmlTree2.addContent(localHtmlTree3);
/*     */         }
/*     */       }
/*     */     }
/* 669 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   protected Content getNavDetailLinks()
/*     */     throws Exception
/*     */   {
/* 678 */     HtmlTree localHtmlTree1 = HtmlTree.LI(this.detailLabel);
/* 679 */     localHtmlTree1.addContent(getSpace());
/* 680 */     HtmlTree localHtmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, localHtmlTree1);
/*     */ 
/* 682 */     MemberSummaryBuilder localMemberSummaryBuilder = (MemberSummaryBuilder)this.configuration
/* 682 */       .getBuilderFactory().getMemberSummaryBuilder(this);
/* 683 */     String[] arrayOfString = { "doclet.navNested", "doclet.navEnum", "doclet.navField", "doclet.navConstructor", "doclet.navMethod" };
/*     */ 
/* 687 */     for (int i = 1; i < arrayOfString.length; i++) {
/* 688 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.LI);
/*     */ 
/* 691 */       AbstractMemberWriter localAbstractMemberWriter = (AbstractMemberWriter)localMemberSummaryBuilder
/* 691 */         .getMemberSummaryWriter(i);
/*     */ 
/* 692 */       if ((i != 1) || (this.classDoc.isEnum()))
/*     */       {
/* 695 */         if ((i != 3) || (!this.classDoc.isEnum()))
/*     */         {
/* 698 */           if (localAbstractMemberWriter == null)
/* 699 */             localHtmlTree3.addContent(getResource(arrayOfString[i]));
/*     */           else {
/* 701 */             localAbstractMemberWriter.addNavDetailLink(localMemberSummaryBuilder.members(i), localHtmlTree3);
/*     */           }
/* 703 */           if (i < arrayOfString.length - 1) {
/* 704 */             addNavGap(localHtmlTree3);
/*     */           }
/* 706 */           localHtmlTree2.addContent(localHtmlTree3);
/*     */         }
/*     */       }
/*     */     }
/* 708 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   protected void addNavGap(Content paramContent)
/*     */   {
/* 717 */     paramContent.addContent(getSpace());
/* 718 */     paramContent.addContent("|");
/* 719 */     paramContent.addContent(getSpace());
/*     */   }
/*     */ 
/*     */   public ClassDoc getClassDoc()
/*     */   {
/* 728 */     return this.classDoc;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ClassWriterImpl
 * JD-Core Version:    0.6.2
 */