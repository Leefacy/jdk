/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class AnnotationTypeOptionalMemberWriterImpl extends AnnotationTypeRequiredMemberWriterImpl
/*     */   implements AnnotationTypeOptionalMemberWriter, MemberSummaryWriter
/*     */ {
/*     */   public AnnotationTypeOptionalMemberWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, AnnotationTypeDoc paramAnnotationTypeDoc)
/*     */   {
/*  57 */     super(paramSubWriterHolderWriter, paramAnnotationTypeDoc);
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  65 */     paramContent.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_OPTIONAL_MEMBER_SUMMARY);
/*     */ 
/*  67 */     Content localContent = this.writer.getMemberTreeHeader();
/*  68 */     this.writer.addSummaryHeader(this, paramClassDoc, localContent);
/*  69 */     return localContent;
/*     */   }
/*     */ 
/*     */   public void addDefaultValueInfo(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/*  76 */     if (((AnnotationTypeElementDoc)paramMemberDoc).defaultValue() != null) {
/*  77 */       HtmlTree localHtmlTree1 = HtmlTree.DT(this.writer.getResource("doclet.Default"));
/*  78 */       HtmlTree localHtmlTree2 = HtmlTree.DL(localHtmlTree1);
/*  79 */       HtmlTree localHtmlTree3 = HtmlTree.DD(new StringContent(((AnnotationTypeElementDoc)paramMemberDoc)
/*  80 */         .defaultValue().toString()));
/*  81 */       localHtmlTree2.addContent(localHtmlTree3);
/*  82 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  90 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void addSummaryLabel(Content paramContent)
/*     */   {
/*  97 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/*  98 */       .getResource("doclet.Annotation_Type_Optional_Member_Summary"));
/*     */ 
/*  99 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public String getTableSummary()
/*     */   {
/* 106 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 107 */       .getText("doclet.Annotation_Type_Optional_Member_Summary"), 
/* 107 */       this.configuration
/* 108 */       .getText("doclet.annotation_type_optional_members"));
/*     */   }
/*     */ 
/*     */   public Content getCaption()
/*     */   {
/* 115 */     return this.configuration.getResource("doclet.Annotation_Type_Optional_Members");
/*     */   }
/*     */ 
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 124 */     String[] arrayOfString = { this.writer
/* 123 */       .getModifierTypeHeader(), this.configuration
/* 124 */       .getText("doclet.0_and_1", this.configuration
/* 125 */       .getText("doclet.Annotation_Type_Optional_Member"), 
/* 125 */       this.configuration
/* 126 */       .getText("doclet.Description")) };
/*     */ 
/* 128 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 135 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY));
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 143 */     if (paramBoolean) {
/* 144 */       return this.writer.getHyperLink(SectionName.ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY, this.writer
/* 146 */         .getResource("doclet.navAnnotationTypeOptionalMember"));
/*     */     }
/*     */ 
/* 148 */     return this.writer.getResource("doclet.navAnnotationTypeOptionalMember");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AnnotationTypeOptionalMemberWriterImpl
 * JD-Core Version:    0.6.2
 */