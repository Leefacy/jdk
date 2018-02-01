/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter.SerialFieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public class HtmlSerialFieldWriter extends FieldWriterImpl
/*     */   implements SerializedFormWriter.SerialFieldWriter
/*     */ {
/*  50 */   ProgramElementDoc[] members = null;
/*     */ 
/*     */   public HtmlSerialFieldWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  54 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public List<FieldDoc> members(ClassDoc paramClassDoc) {
/*  58 */     return Arrays.asList(paramClassDoc.serializableFields());
/*     */   }
/*     */ 
/*     */   public Content getSerializableFieldsHeader()
/*     */   {
/*  67 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/*  68 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/*  69 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getFieldsContentHeader(boolean paramBoolean)
/*     */   {
/*  79 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/*  80 */     if (paramBoolean)
/*  81 */       localHtmlTree.addStyle(HtmlStyle.blockListLast);
/*     */     else
/*  83 */       localHtmlTree.addStyle(HtmlStyle.blockList);
/*  84 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getSerializableFields(String paramString, Content paramContent)
/*     */   {
/*  96 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.LI);
/*  97 */     localHtmlTree1.addStyle(HtmlStyle.blockList);
/*  98 */     if (paramContent.isValid()) {
/*  99 */       StringContent localStringContent = new StringContent(paramString);
/* 100 */       HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.SERIALIZED_MEMBER_HEADING, localStringContent);
/*     */ 
/* 102 */       localHtmlTree1.addContent(localHtmlTree2);
/* 103 */       localHtmlTree1.addContent(paramContent);
/*     */     }
/* 105 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public void addMemberHeader(ClassDoc paramClassDoc, String paramString1, String paramString2, String paramString3, Content paramContent)
/*     */   {
/* 119 */     RawHtml localRawHtml = new RawHtml(paramString3);
/* 120 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.MEMBER_HEADING, localRawHtml);
/* 121 */     paramContent.addContent(localHtmlTree1);
/* 122 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.PRE);
/* 123 */     if (paramClassDoc == null) {
/* 124 */       localHtmlTree2.addContent(paramString1);
/*     */     } else {
/* 126 */       Content localContent = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SERIAL_MEMBER, paramClassDoc));
/*     */ 
/* 128 */       localHtmlTree2.addContent(localContent);
/*     */     }
/* 130 */     localHtmlTree2.addContent(paramString2 + " ");
/* 131 */     localHtmlTree2.addContent(paramString3);
/* 132 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   public void addMemberDeprecatedInfo(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/* 142 */     addDeprecatedInfo(paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addMemberDescription(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/* 152 */     if (paramFieldDoc.inlineTags().length > 0) {
/* 153 */       this.writer.addInlineComment(paramFieldDoc, paramContent);
/*     */     }
/* 155 */     Tag[] arrayOfTag = paramFieldDoc.tags("serial");
/* 156 */     if (arrayOfTag.length > 0)
/* 157 */       this.writer.addInlineComment(paramFieldDoc, arrayOfTag[0], paramContent);
/*     */   }
/*     */ 
/*     */   public void addMemberDescription(SerialFieldTag paramSerialFieldTag, Content paramContent)
/*     */   {
/* 168 */     String str = paramSerialFieldTag.description().trim();
/* 169 */     if (!str.isEmpty()) {
/* 170 */       RawHtml localRawHtml = new RawHtml(str);
/* 171 */       HtmlTree localHtmlTree = HtmlTree.DIV(HtmlStyle.block, localRawHtml);
/* 172 */       paramContent.addContent(localHtmlTree);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addMemberTags(FieldDoc paramFieldDoc, Content paramContent)
/*     */   {
/* 183 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 184 */     TagletWriter.genTagOuput(this.configuration.tagletManager, paramFieldDoc, this.configuration.tagletManager
/* 185 */       .getCustomTaglets(paramFieldDoc), 
/* 185 */       this.writer
/* 186 */       .getTagletWriterInstance(false), 
/* 186 */       localContentBuilder);
/* 187 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DL);
/* 188 */     localHtmlTree.addContent(localContentBuilder);
/* 189 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public boolean shouldPrintOverview(FieldDoc paramFieldDoc)
/*     */   {
/* 201 */     if ((!this.configuration.nocomment) && (
/* 202 */       (!paramFieldDoc.commentText().isEmpty()) || 
/* 203 */       (this.writer
/* 203 */       .hasSerializationOverviewTags(paramFieldDoc))))
/*     */     {
/* 204 */       return true;
/*     */     }
/* 206 */     if (paramFieldDoc.tags("deprecated").length > 0)
/* 207 */       return true;
/* 208 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.HtmlSerialFieldWriter
 * JD-Core Version:    0.6.2
 */