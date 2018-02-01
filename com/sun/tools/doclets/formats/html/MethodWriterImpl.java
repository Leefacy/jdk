/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.MethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ImplementedMethods;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class MethodWriterImpl extends AbstractExecutableMemberWriter
/*     */   implements MethodWriter, MemberSummaryWriter
/*     */ {
/*     */   public MethodWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  59 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public MethodWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter)
/*     */   {
/*  68 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  76 */     paramContent.addContent(HtmlConstants.START_OF_METHOD_SUMMARY);
/*  77 */     Content localContent = this.writer.getMemberTreeHeader();
/*  78 */     this.writer.addSummaryHeader(this, paramClassDoc, localContent);
/*  79 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getMethodDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  87 */     paramContent.addContent(HtmlConstants.START_OF_METHOD_DETAILS);
/*  88 */     Content localContent = this.writer.getMemberTreeHeader();
/*  89 */     localContent.addContent(this.writer.getMarkerAnchor(SectionName.METHOD_DETAIL));
/*     */ 
/*  91 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.methodDetailsLabel);
/*     */ 
/*  93 */     localContent.addContent(localHtmlTree);
/*  94 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getMethodDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/*     */     String str;
/* 103 */     if ((str = getErasureAnchor(paramMethodDoc)) != null) {
/* 104 */       paramContent.addContent(this.writer.getMarkerAnchor(str));
/*     */     }
/* 106 */     paramContent.addContent(this.writer
/* 107 */       .getMarkerAnchor(this.writer
/* 107 */       .getAnchor(paramMethodDoc)));
/*     */ 
/* 108 */     Content localContent = this.writer.getMemberTreeHeader();
/* 109 */     HtmlTree localHtmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/* 110 */     localHtmlTree.addContent(paramMethodDoc.name());
/* 111 */     localContent.addContent(localHtmlTree);
/* 112 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getSignature(MethodDoc paramMethodDoc)
/*     */   {
/* 122 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.PRE);
/* 123 */     this.writer.addAnnotationInfo(paramMethodDoc, localHtmlTree);
/* 124 */     addModifiers(paramMethodDoc, localHtmlTree);
/* 125 */     addTypeParameters(paramMethodDoc, localHtmlTree);
/* 126 */     addReturnType(paramMethodDoc, localHtmlTree);
/* 127 */     if (this.configuration.linksource) {
/* 128 */       StringContent localStringContent = new StringContent(paramMethodDoc.name());
/* 129 */       this.writer.addSrcLink(paramMethodDoc, localStringContent, localHtmlTree);
/*     */     } else {
/* 131 */       addName(paramMethodDoc.name(), localHtmlTree);
/*     */     }
/* 133 */     int i = localHtmlTree.charCount();
/* 134 */     addParameters(paramMethodDoc, localHtmlTree, i);
/* 135 */     addExceptions(paramMethodDoc, localHtmlTree, i);
/* 136 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addDeprecated(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 143 */     addDeprecatedInfo(paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addComments(Type paramType, MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 150 */     ClassDoc localClassDoc = paramType.asClassDoc();
/* 151 */     if (paramMethodDoc.inlineTags().length > 0)
/* 152 */       if ((paramType.asClassDoc().equals(this.classdoc)) || (
/* 153 */         (!localClassDoc
/* 153 */         .isPublic()) && 
/* 154 */         (!Util.isLinkable(localClassDoc, this.configuration))))
/*     */       {
/* 155 */         this.writer.addInlineComment(paramMethodDoc, paramContent);
/*     */       }
/*     */       else {
/* 158 */         Content localContent = this.writer
/* 158 */           .getDocLink(LinkInfoImpl.Kind.METHOD_DOC_COPY, paramType
/* 159 */           .asClassDoc(), paramMethodDoc, paramType
/* 160 */           .asClassDoc().isIncluded() ? paramType
/* 161 */           .typeName() : paramType.qualifiedTypeName(), false);
/*     */ 
/* 163 */         HtmlTree localHtmlTree1 = HtmlTree.CODE(localContent);
/* 164 */         HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.descfrmTypeLabel, paramType.asClassDoc().isClass() ? this.writer.descfrmClassLabel : this.writer.descfrmInterfaceLabel);
/*     */ 
/* 166 */         localHtmlTree2.addContent(this.writer.getSpace());
/* 167 */         localHtmlTree2.addContent(localHtmlTree1);
/* 168 */         paramContent.addContent(HtmlTree.DIV(HtmlStyle.block, localHtmlTree2));
/* 169 */         this.writer.addInlineComment(paramMethodDoc, paramContent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addTags(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 178 */     this.writer.addTagsInfo(paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getMethodDetails(Content paramContent)
/*     */   {
/* 185 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getMethodDoc(Content paramContent, boolean paramBoolean)
/*     */   {
/* 193 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 200 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public int getMemberKind() {
/* 204 */     return 4;
/*     */   }
/*     */ 
/*     */   public void addSummaryLabel(Content paramContent)
/*     */   {
/* 211 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 212 */       .getResource("doclet.Method_Summary"));
/*     */ 
/* 213 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public String getTableSummary()
/*     */   {
/* 220 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 221 */       .getText("doclet.Method_Summary"), 
/* 221 */       this.configuration
/* 222 */       .getText("doclet.methods"));
/*     */   }
/*     */ 
/*     */   public Content getCaption()
/*     */   {
/* 229 */     return this.configuration.getResource("doclet.Methods");
/*     */   }
/*     */ 
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 238 */     String[] arrayOfString = { this.writer
/* 237 */       .getModifierTypeHeader(), this.configuration
/* 238 */       .getText("doclet.0_and_1", this.configuration
/* 239 */       .getText("doclet.Method"), 
/* 239 */       this.configuration
/* 240 */       .getText("doclet.Description")) };
/*     */ 
/* 242 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 249 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.METHOD_SUMMARY));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 257 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.METHODS_INHERITANCE, this.configuration
/* 258 */       .getClassName(paramClassDoc)));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 265 */     Content localContent = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/* 269 */     StringContent localStringContent = new StringContent(paramClassDoc.isClass() ? this.configuration
/* 268 */       .getText("doclet.Methods_Inherited_From_Class") : 
/* 268 */       this.configuration
/* 269 */       .getText("doclet.Methods_Inherited_From_Interface"));
/*     */ 
/* 270 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, localStringContent);
/*     */ 
/* 272 */     localHtmlTree.addContent(this.writer.getSpace());
/* 273 */     localHtmlTree.addContent(localContent);
/* 274 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 281 */     MethodDoc localMethodDoc = (MethodDoc)paramProgramElementDoc;
/* 282 */     addModifierAndType(localMethodDoc, localMethodDoc.returnType(), paramContent);
/*     */   }
/*     */ 
/*     */   protected static void addOverridden(HtmlDocletWriter paramHtmlDocletWriter, Type paramType, MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 290 */     if (paramHtmlDocletWriter.configuration.nocomment) {
/* 291 */       return;
/*     */     }
/* 293 */     ClassDoc localClassDoc = paramType.asClassDoc();
/* 294 */     if ((!localClassDoc.isPublic()) && 
/* 295 */       (!Util.isLinkable(localClassDoc, paramHtmlDocletWriter.configuration)))
/*     */     {
/* 297 */       return;
/*     */     }
/* 299 */     if ((paramType.asClassDoc().isIncluded()) && (!paramMethodDoc.isIncluded()))
/*     */     {
/* 302 */       return;
/*     */     }
/* 304 */     Content localContent1 = paramHtmlDocletWriter.overridesLabel;
/* 305 */     LinkInfoImpl.Kind localKind = LinkInfoImpl.Kind.METHOD_OVERRIDES;
/*     */ 
/* 307 */     if (paramMethodDoc != null) {
/* 308 */       if ((paramType.asClassDoc().isAbstract()) && (paramMethodDoc.isAbstract()))
/*     */       {
/* 311 */         localContent1 = paramHtmlDocletWriter.specifiedByLabel;
/* 312 */         localKind = LinkInfoImpl.Kind.METHOD_SPECIFIED_BY;
/*     */       }
/* 314 */       HtmlTree localHtmlTree1 = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.overrideSpecifyLabel, localContent1));
/* 315 */       paramContent.addContent(localHtmlTree1);
/*     */ 
/* 317 */       Content localContent2 = paramHtmlDocletWriter
/* 317 */         .getLink(new LinkInfoImpl(paramHtmlDocletWriter.configuration, localKind, paramType));
/*     */ 
/* 318 */       HtmlTree localHtmlTree2 = HtmlTree.CODE(localContent2);
/* 319 */       String str = paramMethodDoc.name();
/* 320 */       Content localContent3 = paramHtmlDocletWriter.getLink(new LinkInfoImpl(paramHtmlDocletWriter.configuration, LinkInfoImpl.Kind.MEMBER, paramType
/* 322 */         .asClassDoc())
/* 323 */         .where(paramHtmlDocletWriter
/* 323 */         .getName(paramHtmlDocletWriter
/* 323 */         .getAnchor(paramMethodDoc)))
/* 323 */         .label(str));
/* 324 */       HtmlTree localHtmlTree3 = HtmlTree.CODE(localContent3);
/* 325 */       HtmlTree localHtmlTree4 = HtmlTree.DD(localHtmlTree3);
/* 326 */       localHtmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 327 */       localHtmlTree4.addContent(paramHtmlDocletWriter.getResource("doclet.in_class"));
/* 328 */       localHtmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 329 */       localHtmlTree4.addContent(localHtmlTree2);
/* 330 */       paramContent.addContent(localHtmlTree4);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static void addImplementsInfo(HtmlDocletWriter paramHtmlDocletWriter, MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 339 */     if (paramHtmlDocletWriter.configuration.nocomment) {
/* 340 */       return;
/*     */     }
/* 342 */     ImplementedMethods localImplementedMethods = new ImplementedMethods(paramMethodDoc, paramHtmlDocletWriter.configuration);
/*     */ 
/* 344 */     MethodDoc[] arrayOfMethodDoc = localImplementedMethods.build();
/* 345 */     for (int i = 0; i < arrayOfMethodDoc.length; i++) {
/* 346 */       MethodDoc localMethodDoc = arrayOfMethodDoc[i];
/* 347 */       Type localType = localImplementedMethods.getMethodHolder(localMethodDoc);
/* 348 */       Content localContent1 = paramHtmlDocletWriter.getLink(new LinkInfoImpl(paramHtmlDocletWriter.configuration, LinkInfoImpl.Kind.METHOD_SPECIFIED_BY, localType));
/*     */ 
/* 350 */       HtmlTree localHtmlTree1 = HtmlTree.CODE(localContent1);
/* 351 */       HtmlTree localHtmlTree2 = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.overrideSpecifyLabel, paramHtmlDocletWriter.specifiedByLabel));
/* 352 */       paramContent.addContent(localHtmlTree2);
/* 353 */       Content localContent2 = paramHtmlDocletWriter.getDocLink(LinkInfoImpl.Kind.MEMBER, localMethodDoc, localMethodDoc
/* 355 */         .name(), false);
/* 356 */       HtmlTree localHtmlTree3 = HtmlTree.CODE(localContent2);
/* 357 */       HtmlTree localHtmlTree4 = HtmlTree.DD(localHtmlTree3);
/* 358 */       localHtmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 359 */       localHtmlTree4.addContent(paramHtmlDocletWriter.getResource("doclet.in_interface"));
/* 360 */       localHtmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 361 */       localHtmlTree4.addContent(localHtmlTree1);
/* 362 */       paramContent.addContent(localHtmlTree4);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addReturnType(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 373 */     Type localType = paramMethodDoc.returnType();
/* 374 */     if (localType != null) {
/* 375 */       Content localContent = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.RETURN_TYPE, localType));
/*     */ 
/* 377 */       paramContent.addContent(localContent);
/* 378 */       paramContent.addContent(this.writer.getSpace());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 386 */     if (paramBoolean) {
/* 387 */       if (paramClassDoc == null) {
/* 388 */         return this.writer.getHyperLink(SectionName.METHOD_SUMMARY, this.writer
/* 390 */           .getResource("doclet.navMethod"));
/*     */       }
/*     */ 
/* 392 */       return this.writer.getHyperLink(SectionName.METHODS_INHERITANCE, this.configuration
/* 394 */         .getClassName(paramClassDoc), 
/* 394 */         this.writer.getResource("doclet.navMethod"));
/*     */     }
/*     */ 
/* 397 */     return this.writer.getResource("doclet.navMethod");
/*     */   }
/*     */ 
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent)
/*     */   {
/* 405 */     if (paramBoolean) {
/* 406 */       paramContent.addContent(this.writer.getHyperLink(SectionName.METHOD_DETAIL, this.writer
/* 407 */         .getResource("doclet.navMethod")));
/*     */     }
/*     */     else
/* 409 */       paramContent.addContent(this.writer.getResource("doclet.navMethod"));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.MethodWriterImpl
 * JD-Core Version:    0.6.2
 */