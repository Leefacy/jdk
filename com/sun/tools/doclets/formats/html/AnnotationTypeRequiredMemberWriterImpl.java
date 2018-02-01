/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class AnnotationTypeRequiredMemberWriterImpl extends AbstractMemberWriter
/*     */   implements AnnotationTypeRequiredMemberWriter, MemberSummaryWriter
/*     */ {
/*     */   public AnnotationTypeRequiredMemberWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, AnnotationTypeDoc paramAnnotationTypeDoc)
/*     */   {
/*  56 */     super(paramSubWriterHolderWriter, paramAnnotationTypeDoc);
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  64 */     paramContent.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_REQUIRED_MEMBER_SUMMARY);
/*     */ 
/*  66 */     Content localContent = this.writer.getMemberTreeHeader();
/*  67 */     this.writer.addSummaryHeader(this, paramClassDoc, localContent);
/*  68 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getMemberTreeHeader()
/*     */   {
/*  75 */     return this.writer.getMemberTreeHeader();
/*     */   }
/*     */ 
/*     */   public void addAnnotationDetailsMarker(Content paramContent)
/*     */   {
/*  82 */     paramContent.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_DETAILS);
/*     */   }
/*     */ 
/*     */   public void addAnnotationDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  90 */     if (!this.writer.printedAnnotationHeading) {
/*  91 */       paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ANNOTATION_TYPE_ELEMENT_DETAIL));
/*     */ 
/*  93 */       HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.annotationTypeDetailsLabel);
/*     */ 
/*  95 */       paramContent.addContent(localHtmlTree);
/*  96 */       this.writer.printedAnnotationHeading = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Content getAnnotationDocTreeHeader(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 105 */     paramContent.addContent(this.writer
/* 106 */       .getMarkerAnchor(paramMemberDoc
/* 106 */       .name() + ((ExecutableMemberDoc)paramMemberDoc)
/* 107 */       .signature()));
/* 108 */     Content localContent = this.writer.getMemberTreeHeader();
/* 109 */     HtmlTree localHtmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/* 110 */     localHtmlTree.addContent(paramMemberDoc.name());
/* 111 */     localContent.addContent(localHtmlTree);
/* 112 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getSignature(MemberDoc paramMemberDoc)
/*     */   {
/* 119 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.PRE);
/* 120 */     this.writer.addAnnotationInfo(paramMemberDoc, localHtmlTree);
/* 121 */     addModifiers(paramMemberDoc, localHtmlTree);
/*     */ 
/* 123 */     Content localContent = this.writer
/* 123 */       .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, 
/* 124 */       getType(paramMemberDoc)));
/*     */ 
/* 125 */     localHtmlTree.addContent(localContent);
/* 126 */     localHtmlTree.addContent(this.writer.getSpace());
/* 127 */     if (this.configuration.linksource) {
/* 128 */       StringContent localStringContent = new StringContent(paramMemberDoc.name());
/* 129 */       this.writer.addSrcLink(paramMemberDoc, localStringContent, localHtmlTree);
/*     */     } else {
/* 131 */       addName(paramMemberDoc.name(), localHtmlTree);
/*     */     }
/* 133 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addDeprecated(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 140 */     addDeprecatedInfo(paramMemberDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addComments(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 147 */     addComment(paramMemberDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addTags(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 154 */     this.writer.addTagsInfo(paramMemberDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getAnnotationDetails(Content paramContent)
/*     */   {
/* 161 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getAnnotationDoc(Content paramContent, boolean paramBoolean)
/*     */   {
/* 169 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 176 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void addSummaryLabel(Content paramContent)
/*     */   {
/* 183 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 184 */       .getResource("doclet.Annotation_Type_Required_Member_Summary"));
/*     */ 
/* 185 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public String getTableSummary()
/*     */   {
/* 192 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 193 */       .getText("doclet.Annotation_Type_Required_Member_Summary"), 
/* 193 */       this.configuration
/* 194 */       .getText("doclet.annotation_type_required_members"));
/*     */   }
/*     */ 
/*     */   public Content getCaption()
/*     */   {
/* 201 */     return this.configuration.getResource("doclet.Annotation_Type_Required_Members");
/*     */   }
/*     */ 
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 210 */     String[] arrayOfString = { this.writer
/* 209 */       .getModifierTypeHeader(), this.configuration
/* 210 */       .getText("doclet.0_and_1", this.configuration
/* 211 */       .getText("doclet.Annotation_Type_Required_Member"), 
/* 211 */       this.configuration
/* 212 */       .getText("doclet.Description")) };
/*     */ 
/* 214 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 221 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ANNOTATION_TYPE_REQUIRED_ELEMENT_SUMMARY));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 242 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 243 */       .getDocLink(paramKind, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 243 */       .name(), false));
/* 244 */     HtmlTree localHtmlTree2 = HtmlTree.CODE(localHtmlTree1);
/* 245 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 260 */     MemberDoc localMemberDoc = (MemberDoc)paramProgramElementDoc;
/* 261 */     addModifierAndType(localMemberDoc, getType(localMemberDoc), paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 268 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((MemberDoc)paramProgramElementDoc)
/* 269 */       .qualifiedName());
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 276 */     if (paramBoolean) {
/* 277 */       return this.writer.getHyperLink(SectionName.ANNOTATION_TYPE_REQUIRED_ELEMENT_SUMMARY, this.writer
/* 279 */         .getResource("doclet.navAnnotationTypeRequiredMember"));
/*     */     }
/*     */ 
/* 281 */     return this.writer.getResource("doclet.navAnnotationTypeRequiredMember");
/*     */   }
/*     */ 
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent)
/*     */   {
/* 289 */     if (paramBoolean) {
/* 290 */       paramContent.addContent(this.writer.getHyperLink(SectionName.ANNOTATION_TYPE_ELEMENT_DETAIL, this.writer
/* 292 */         .getResource("doclet.navAnnotationTypeMember")));
/*     */     }
/*     */     else
/*     */     {
/* 294 */       paramContent.addContent(this.writer.getResource("doclet.navAnnotationTypeMember"));
/*     */     }
/*     */   }
/*     */ 
/*     */   private Type getType(MemberDoc paramMemberDoc) {
/* 299 */     if ((paramMemberDoc instanceof FieldDoc)) {
/* 300 */       return ((FieldDoc)paramMemberDoc).type();
/*     */     }
/* 302 */     return ((MethodDoc)paramMemberDoc).returnType();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AnnotationTypeRequiredMemberWriterImpl
 * JD-Core Version:    0.6.2
 */