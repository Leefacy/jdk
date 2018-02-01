/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.SerializedFormBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ 
/*     */ public class TagletWriterImpl extends TagletWriter
/*     */ {
/*     */   private final HtmlDocletWriter htmlWriter;
/*     */   private final ConfigurationImpl configuration;
/*     */ 
/*     */   public TagletWriterImpl(HtmlDocletWriter paramHtmlDocletWriter, boolean paramBoolean)
/*     */   {
/*  58 */     super(paramBoolean);
/*  59 */     this.htmlWriter = paramHtmlDocletWriter;
/*  60 */     this.configuration = paramHtmlDocletWriter.configuration;
/*     */   }
/*     */ 
/*     */   public Content getOutputInstance()
/*     */   {
/*  67 */     return new ContentBuilder();
/*     */   }
/*     */ 
/*     */   protected Content codeTagOutput(Tag paramTag)
/*     */   {
/*  74 */     HtmlTree localHtmlTree = HtmlTree.CODE(new StringContent(Util.normalizeNewlines(paramTag.text())));
/*  75 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getDocRootOutput()
/*     */   {
/*     */     String str;
/*  83 */     if (this.htmlWriter.pathToRoot.isEmpty())
/*  84 */       str = ".";
/*     */     else
/*  86 */       str = this.htmlWriter.pathToRoot.getPath();
/*  87 */     return new StringContent(str);
/*     */   }
/*     */ 
/*     */   public Content deprecatedTagOutput(Doc paramDoc)
/*     */   {
/*  94 */     ContentBuilder localContentBuilder = new ContentBuilder();
/*  95 */     Tag[] arrayOfTag = paramDoc.tags("deprecated");
/*     */     Object localObject;
/*  96 */     if ((paramDoc instanceof ClassDoc)) {
/*  97 */       if (Util.isDeprecated((ProgramElementDoc)paramDoc)) {
/*  98 */         localContentBuilder.addContent(HtmlTree.SPAN(HtmlStyle.deprecatedLabel, new StringContent(this.configuration
/*  99 */           .getText("doclet.Deprecated"))));
/*     */ 
/* 100 */         localContentBuilder.addContent(RawHtml.nbsp);
/* 101 */         if (arrayOfTag.length > 0) {
/* 102 */           localObject = arrayOfTag[0].inlineTags();
/* 103 */           if (localObject.length > 0)
/* 104 */             localContentBuilder.addContent(commentTagsToOutput(null, paramDoc, arrayOfTag[0]
/* 105 */               .inlineTags(), false));
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 111 */       localObject = (MemberDoc)paramDoc;
/* 112 */       if (Util.isDeprecated((ProgramElementDoc)paramDoc)) {
/* 113 */         localContentBuilder.addContent(HtmlTree.SPAN(HtmlStyle.deprecatedLabel, new StringContent(this.configuration
/* 114 */           .getText("doclet.Deprecated"))));
/*     */ 
/* 115 */         localContentBuilder.addContent(RawHtml.nbsp);
/* 116 */         if (arrayOfTag.length > 0) {
/* 117 */           Content localContent = commentTagsToOutput(null, paramDoc, arrayOfTag[0]
/* 118 */             .inlineTags(), false);
/* 119 */           if (!localContent.isEmpty())
/* 120 */             localContentBuilder.addContent(HtmlTree.SPAN(HtmlStyle.deprecationComment, localContent));
/*     */         }
/*     */       }
/* 123 */       else if (Util.isDeprecated(((MemberDoc)localObject).containingClass())) {
/* 124 */         localContentBuilder.addContent(HtmlTree.SPAN(HtmlStyle.deprecatedLabel, new StringContent(this.configuration
/* 125 */           .getText("doclet.Deprecated"))));
/*     */ 
/* 126 */         localContentBuilder.addContent(RawHtml.nbsp);
/*     */       }
/*     */     }
/*     */ 
/* 130 */     return localContentBuilder;
/*     */   }
/*     */ 
/*     */   protected Content literalTagOutput(Tag paramTag)
/*     */   {
/* 137 */     StringContent localStringContent = new StringContent(Util.normalizeNewlines(paramTag.text()));
/* 138 */     return localStringContent;
/*     */   }
/*     */ 
/*     */   public MessageRetriever getMsgRetriever()
/*     */   {
/* 145 */     return this.configuration.message;
/*     */   }
/*     */ 
/*     */   public Content getParamHeader(String paramString)
/*     */   {
/* 152 */     HtmlTree localHtmlTree = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.paramLabel, new StringContent(paramString)));
/*     */ 
/* 154 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content paramTagOutput(ParamTag paramParamTag, String paramString)
/*     */   {
/* 161 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 162 */     localContentBuilder.addContent(HtmlTree.CODE(new RawHtml(paramString)));
/* 163 */     localContentBuilder.addContent(" - ");
/* 164 */     localContentBuilder.addContent(this.htmlWriter.commentTagsToContent(paramParamTag, null, paramParamTag.inlineTags(), false));
/* 165 */     HtmlTree localHtmlTree = HtmlTree.DD(localContentBuilder);
/* 166 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content propertyTagOutput(Tag paramTag, String paramString)
/*     */   {
/* 173 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 174 */     localContentBuilder.addContent(new RawHtml(paramString));
/* 175 */     localContentBuilder.addContent(" ");
/* 176 */     localContentBuilder.addContent(HtmlTree.CODE(new RawHtml(paramTag.text())));
/* 177 */     localContentBuilder.addContent(".");
/* 178 */     HtmlTree localHtmlTree = HtmlTree.P(localContentBuilder);
/* 179 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content returnTagOutput(Tag paramTag)
/*     */   {
/* 186 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 187 */     localContentBuilder.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.returnLabel, new StringContent(this.configuration
/* 188 */       .getText("doclet.Returns")))));
/*     */ 
/* 189 */     localContentBuilder.addContent(HtmlTree.DD(this.htmlWriter.commentTagsToContent(paramTag, null, paramTag
/* 190 */       .inlineTags(), false)));
/* 191 */     return localContentBuilder;
/*     */   }
/*     */ 
/*     */   public Content seeTagOutput(Doc paramDoc, SeeTag[] paramArrayOfSeeTag)
/*     */   {
/* 198 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 199 */     if (paramArrayOfSeeTag.length > 0)
/* 200 */       for (int i = 0; i < paramArrayOfSeeTag.length; i++) {
/* 201 */         appendSeparatorIfNotEmpty(localContentBuilder);
/* 202 */         localContentBuilder.addContent(this.htmlWriter.seeTagToContent(paramArrayOfSeeTag[i]));
/*     */       }
/*     */     Object localObject2;
/* 205 */     if ((paramDoc.isField()) && (((FieldDoc)paramDoc).constantValue() != null) && ((this.htmlWriter instanceof ClassWriterImpl)))
/*     */     {
/* 208 */       appendSeparatorIfNotEmpty(localContentBuilder);
/*     */ 
/* 210 */       localObject1 = this.htmlWriter.pathToRoot
/* 210 */         .resolve(DocPaths.CONSTANT_VALUES);
/*     */ 
/* 212 */       localObject2 = ((ClassWriterImpl)this.htmlWriter)
/* 212 */         .getClassDoc().qualifiedName() + "." + ((FieldDoc)paramDoc).name();
/* 213 */       DocLink localDocLink = ((DocPath)localObject1).fragment((String)localObject2);
/* 214 */       localContentBuilder.addContent(this.htmlWriter.getHyperLink(localDocLink, new StringContent(this.configuration
/* 215 */         .getText("doclet.Constants_Summary"))));
/*     */     }
/*     */ 
/* 217 */     if ((paramDoc.isClass()) && (((ClassDoc)paramDoc).isSerializable()))
/*     */     {
/* 219 */       if ((SerializedFormBuilder.serialInclude(paramDoc)) && 
/* 220 */         (SerializedFormBuilder.serialInclude(((ClassDoc)paramDoc)
/* 220 */         .containingPackage()))) {
/* 221 */         appendSeparatorIfNotEmpty(localContentBuilder);
/* 222 */         localObject1 = this.htmlWriter.pathToRoot.resolve(DocPaths.SERIALIZED_FORM);
/* 223 */         localObject2 = ((DocPath)localObject1).fragment(((ClassDoc)paramDoc).qualifiedName());
/* 224 */         localContentBuilder.addContent(this.htmlWriter.getHyperLink((DocLink)localObject2, new StringContent(this.configuration
/* 225 */           .getText("doclet.Serialized_Form"))));
/*     */       }
/*     */     }
/*     */ 
/* 228 */     if (localContentBuilder.isEmpty()) {
/* 229 */       return localContentBuilder;
/*     */     }
/* 231 */     Object localObject1 = new ContentBuilder();
/* 232 */     ((ContentBuilder)localObject1).addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.seeLabel, new StringContent(this.configuration
/* 233 */       .getText("doclet.See_Also")))));
/*     */ 
/* 234 */     ((ContentBuilder)localObject1).addContent(HtmlTree.DD(localContentBuilder));
/* 235 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void appendSeparatorIfNotEmpty(ContentBuilder paramContentBuilder)
/*     */   {
/* 240 */     if (!paramContentBuilder.isEmpty()) {
/* 241 */       paramContentBuilder.addContent(", ");
/* 242 */       paramContentBuilder.addContent(DocletConstants.NL);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Content simpleTagOutput(Tag[] paramArrayOfTag, String paramString)
/*     */   {
/* 250 */     ContentBuilder localContentBuilder1 = new ContentBuilder();
/* 251 */     localContentBuilder1.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.simpleTagLabel, new RawHtml(paramString))));
/* 252 */     ContentBuilder localContentBuilder2 = new ContentBuilder();
/* 253 */     for (int i = 0; i < paramArrayOfTag.length; i++) {
/* 254 */       if (i > 0) {
/* 255 */         localContentBuilder2.addContent(", ");
/*     */       }
/* 257 */       localContentBuilder2.addContent(this.htmlWriter.commentTagsToContent(paramArrayOfTag[i], null, paramArrayOfTag[i]
/* 258 */         .inlineTags(), false));
/*     */     }
/* 260 */     localContentBuilder1.addContent(HtmlTree.DD(localContentBuilder2));
/* 261 */     return localContentBuilder1;
/*     */   }
/*     */ 
/*     */   public Content simpleTagOutput(Tag paramTag, String paramString)
/*     */   {
/* 268 */     ContentBuilder localContentBuilder = new ContentBuilder();
/* 269 */     localContentBuilder.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.simpleTagLabel, new RawHtml(paramString))));
/* 270 */     Content localContent = this.htmlWriter.commentTagsToContent(paramTag, null, paramTag
/* 271 */       .inlineTags(), false);
/* 272 */     localContentBuilder.addContent(HtmlTree.DD(localContent));
/* 273 */     return localContentBuilder;
/*     */   }
/*     */ 
/*     */   public Content getThrowsHeader()
/*     */   {
/* 280 */     HtmlTree localHtmlTree = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.throwsLabel, new StringContent(this.configuration
/* 281 */       .getText("doclet.Throws"))));
/*     */ 
/* 282 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content throwsTagOutput(ThrowsTag paramThrowsTag)
/*     */   {
/* 289 */     ContentBuilder localContentBuilder = new ContentBuilder();
/*     */ 
/* 292 */     Content localContent1 = paramThrowsTag.exceptionType() == null ? new RawHtml(paramThrowsTag
/* 291 */       .exceptionName()) : this.htmlWriter
/* 292 */       .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramThrowsTag
/* 293 */       .exceptionType()));
/* 294 */     localContentBuilder.addContent(HtmlTree.CODE(localContent1));
/* 295 */     Content localContent2 = this.htmlWriter.commentTagsToContent(paramThrowsTag, null, paramThrowsTag
/* 296 */       .inlineTags(), false);
/* 297 */     if ((localContent2 != null) && (!localContent2.isEmpty())) {
/* 298 */       localContentBuilder.addContent(" - ");
/* 299 */       localContentBuilder.addContent(localContent2);
/*     */     }
/* 301 */     HtmlTree localHtmlTree = HtmlTree.DD(localContentBuilder);
/* 302 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content throwsTagOutput(Type paramType)
/*     */   {
/* 309 */     HtmlTree localHtmlTree = HtmlTree.DD(HtmlTree.CODE(this.htmlWriter.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramType))));
/*     */ 
/* 311 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content valueTagOutput(FieldDoc paramFieldDoc, String paramString, boolean paramBoolean)
/*     */   {
/* 320 */     return paramBoolean ? this.htmlWriter
/* 320 */       .getDocLink(LinkInfoImpl.Kind.VALUE_TAG, paramFieldDoc, paramString, false) : 
/* 320 */       new RawHtml(paramString);
/*     */   }
/*     */ 
/*     */   public Content commentTagsToOutput(Tag paramTag, Tag[] paramArrayOfTag)
/*     */   {
/* 328 */     return commentTagsToOutput(paramTag, null, paramArrayOfTag, false);
/*     */   }
/*     */ 
/*     */   public Content commentTagsToOutput(Doc paramDoc, Tag[] paramArrayOfTag)
/*     */   {
/* 335 */     return commentTagsToOutput(null, paramDoc, paramArrayOfTag, false);
/*     */   }
/*     */ 
/*     */   public Content commentTagsToOutput(Tag paramTag, Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean)
/*     */   {
/* 343 */     return this.htmlWriter.commentTagsToContent(paramTag, paramDoc, paramArrayOfTag, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Configuration configuration()
/*     */   {
/* 351 */     return this.configuration;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.TagletWriterImpl
 * JD-Core Version:    0.6.2
 */