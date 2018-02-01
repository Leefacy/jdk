/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.FieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class FieldWriterImpl extends AbstractMemberWriter
/*     */   implements FieldWriter, MemberSummaryWriter
/*     */ {
/*     */   public FieldWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  52 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public FieldWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  56 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  64 */     paramContent.addContent(HtmlConstants.START_OF_FIELD_SUMMARY);
/*  65 */     Content localContent = this.writer.getMemberTreeHeader();
/*  66 */     this.writer.addSummaryHeader(this, paramClassDoc, localContent);
/*  67 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getFieldDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  75 */     paramContent.addContent(HtmlConstants.START_OF_FIELD_DETAILS);
/*  76 */     Content localContent = this.writer.getMemberTreeHeader();
/*  77 */     localContent.addContent(this.writer.getMarkerAnchor(SectionName.FIELD_DETAIL));
/*     */ 
/*  79 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.fieldDetailsLabel);
/*     */ 
/*  81 */     localContent.addContent(localHtmlTree);
/*  82 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getFieldDocTreeHeader(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/*  90 */     paramContent.addContent(this.writer
/*  91 */       .getMarkerAnchor(paramFieldDoc
/*  91 */       .name()));
/*  92 */     Content localContent = this.writer.getMemberTreeHeader();
/*  93 */     HtmlTree localHtmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/*  94 */     localHtmlTree.addContent(paramFieldDoc.name());
/*  95 */     localContent.addContent(localHtmlTree);
/*  96 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getSignature(FieldDoc paramFieldDoc)
/*     */   {
/* 103 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.PRE);
/* 104 */     this.writer.addAnnotationInfo(paramFieldDoc, localHtmlTree);
/* 105 */     addModifiers(paramFieldDoc, localHtmlTree);
/* 106 */     Content localContent = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramFieldDoc
/* 107 */       .type()));
/* 108 */     localHtmlTree.addContent(localContent);
/* 109 */     localHtmlTree.addContent(" ");
/* 110 */     if (this.configuration.linksource) {
/* 111 */       StringContent localStringContent = new StringContent(paramFieldDoc.name());
/* 112 */       this.writer.addSrcLink(paramFieldDoc, localStringContent, localHtmlTree);
/*     */     } else {
/* 114 */       addName(paramFieldDoc.name(), localHtmlTree);
/*     */     }
/* 116 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addDeprecated(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/* 123 */     addDeprecatedInfo(paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addComments(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/* 130 */     ClassDoc localClassDoc = paramFieldDoc.containingClass();
/* 131 */     if (paramFieldDoc.inlineTags().length > 0)
/* 132 */       if ((localClassDoc.equals(this.classdoc)) || (
/* 133 */         (!localClassDoc
/* 133 */         .isPublic()) && (!Util.isLinkable(localClassDoc, this.configuration)))) {
/* 134 */         this.writer.addInlineComment(paramFieldDoc, paramContent);
/*     */       }
/*     */       else {
/* 137 */         Content localContent = this.writer
/* 137 */           .getDocLink(LinkInfoImpl.Kind.FIELD_DOC_COPY, localClassDoc, paramFieldDoc, localClassDoc
/* 139 */           .isIncluded() ? localClassDoc
/* 140 */           .typeName() : localClassDoc.qualifiedTypeName(), false);
/*     */ 
/* 142 */         HtmlTree localHtmlTree1 = HtmlTree.CODE(localContent);
/* 143 */         HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.descfrmTypeLabel, localClassDoc.isClass() ? this.writer.descfrmClassLabel : this.writer.descfrmInterfaceLabel);
/*     */ 
/* 145 */         localHtmlTree2.addContent(this.writer.getSpace());
/* 146 */         localHtmlTree2.addContent(localHtmlTree1);
/* 147 */         paramContent.addContent(HtmlTree.DIV(HtmlStyle.block, localHtmlTree2));
/* 148 */         this.writer.addInlineComment(paramFieldDoc, paramContent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addTags(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/* 157 */     this.writer.addTagsInfo(paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getFieldDetails(Content paramContent)
/*     */   {
/* 164 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getFieldDoc(Content paramContent, boolean paramBoolean)
/*     */   {
/* 172 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 179 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public int getMemberKind() {
/* 183 */     return 2;
/*     */   }
/*     */ 
/*     */   public void addSummaryLabel(Content paramContent)
/*     */   {
/* 190 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 191 */       .getResource("doclet.Field_Summary"));
/*     */ 
/* 192 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public String getTableSummary()
/*     */   {
/* 199 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 200 */       .getText("doclet.Field_Summary"), 
/* 200 */       this.configuration
/* 201 */       .getText("doclet.fields"));
/*     */   }
/*     */ 
/*     */   public Content getCaption()
/*     */   {
/* 208 */     return this.configuration.getResource("doclet.Fields");
/*     */   }
/*     */ 
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 217 */     String[] arrayOfString = { this.writer
/* 216 */       .getModifierTypeHeader(), this.configuration
/* 217 */       .getText("doclet.0_and_1", this.configuration
/* 218 */       .getText("doclet.Field"), 
/* 218 */       this.configuration
/* 219 */       .getText("doclet.Description")) };
/*     */ 
/* 221 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 228 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.FIELD_SUMMARY));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 236 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.FIELDS_INHERITANCE, this.configuration
/* 237 */       .getClassName(paramClassDoc)));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 244 */     Content localContent = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/* 248 */     StringContent localStringContent = new StringContent(paramClassDoc.isClass() ? this.configuration
/* 247 */       .getText("doclet.Fields_Inherited_From_Class") : 
/* 247 */       this.configuration
/* 248 */       .getText("doclet.Fields_Inherited_From_Interface"));
/*     */ 
/* 249 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, localStringContent);
/*     */ 
/* 251 */     localHtmlTree.addContent(this.writer.getSpace());
/* 252 */     localHtmlTree.addContent(localContent);
/* 253 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 261 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 262 */       .getDocLink(paramKind, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 262 */       .name(), false));
/* 263 */     HtmlTree localHtmlTree2 = HtmlTree.CODE(localHtmlTree1);
/* 264 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 272 */     paramContent.addContent(this.writer
/* 273 */       .getDocLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 274 */       .name(), false));
/*     */   }
/*     */ 
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 281 */     FieldDoc localFieldDoc = (FieldDoc)paramProgramElementDoc;
/* 282 */     addModifierAndType(localFieldDoc, localFieldDoc.type(), paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 289 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((FieldDoc)paramProgramElementDoc)
/* 290 */       .qualifiedName());
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 297 */     if (paramBoolean) {
/* 298 */       if (paramClassDoc == null) {
/* 299 */         return this.writer.getHyperLink(SectionName.FIELD_SUMMARY, this.writer
/* 301 */           .getResource("doclet.navField"));
/*     */       }
/*     */ 
/* 303 */       return this.writer.getHyperLink(SectionName.FIELDS_INHERITANCE, this.configuration
/* 305 */         .getClassName(paramClassDoc), 
/* 305 */         this.writer.getResource("doclet.navField"));
/*     */     }
/*     */ 
/* 308 */     return this.writer.getResource("doclet.navField");
/*     */   }
/*     */ 
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent)
/*     */   {
/* 316 */     if (paramBoolean) {
/* 317 */       paramContent.addContent(this.writer.getHyperLink(SectionName.FIELD_DETAIL, this.writer
/* 319 */         .getResource("doclet.navField")));
/*     */     }
/*     */     else
/*     */     {
/* 321 */       paramContent.addContent(this.writer.getResource("doclet.navField"));
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.FieldWriterImpl
 * JD-Core Version:    0.6.2
 */