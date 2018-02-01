/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter.SerialMethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*     */ 
/*     */ public class HtmlSerialMethodWriter extends MethodWriterImpl
/*     */   implements SerializedFormWriter.SerialMethodWriter
/*     */ {
/*     */   public HtmlSerialMethodWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  50 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public Content getSerializableMethodsHeader()
/*     */   {
/*  59 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL);
/*  60 */     localHtmlTree.addStyle(HtmlStyle.blockList);
/*  61 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getMethodsContentHeader(boolean paramBoolean)
/*     */   {
/*  71 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI);
/*  72 */     if (paramBoolean)
/*  73 */       localHtmlTree.addStyle(HtmlStyle.blockListLast);
/*     */     else
/*  75 */       localHtmlTree.addStyle(HtmlStyle.blockList);
/*  76 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getSerializableMethods(String paramString, Content paramContent)
/*     */   {
/*  88 */     StringContent localStringContent = new StringContent(paramString);
/*  89 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.SERIALIZED_MEMBER_HEADING, localStringContent);
/*     */ 
/*  91 */     HtmlTree localHtmlTree2 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree1);
/*  92 */     localHtmlTree2.addContent(paramContent);
/*  93 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   public Content getNoCustomizationMsg(String paramString)
/*     */   {
/* 103 */     StringContent localStringContent = new StringContent(paramString);
/* 104 */     return localStringContent;
/*     */   }
/*     */ 
/*     */   public void addMemberHeader(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 114 */     paramContent.addContent(getHead(paramMethodDoc));
/* 115 */     paramContent.addContent(getSignature(paramMethodDoc));
/*     */   }
/*     */ 
/*     */   public void addDeprecatedMemberInfo(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 125 */     addDeprecatedInfo(paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addMemberDescription(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 135 */     addComment(paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addMemberTags(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 145 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 146 */     TagletManager localTagletManager = this.configuration.tagletManager;
/*     */ 
/* 148 */     TagletWriter.genTagOuput(localTagletManager, paramMethodDoc, localTagletManager
/* 149 */       .getSerializedFormTaglets(), this.writer
/* 150 */       .getTagletWriterInstance(false), 
/* 150 */       localContentBuilder);
/* 151 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DL);
/* 152 */     localHtmlTree.addContent(localContentBuilder);
/* 153 */     paramContent.addContent(localHtmlTree);
/* 154 */     MethodDoc localMethodDoc = paramMethodDoc;
/* 155 */     if ((localMethodDoc.name().compareTo("writeExternal") == 0) && 
/* 156 */       (localMethodDoc
/* 156 */       .tags("serialData").length == 0))
/*     */     {
/* 157 */       serialWarning(paramMethodDoc.position(), "doclet.MissingSerialDataTag", localMethodDoc
/* 158 */         .containingClass().qualifiedName(), localMethodDoc.name());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.HtmlSerialMethodWriter
 * JD-Core Version:    0.6.2
 */