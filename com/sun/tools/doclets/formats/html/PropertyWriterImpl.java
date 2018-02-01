/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.PropertyWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PropertyWriterImpl extends AbstractMemberWriter
/*     */   implements PropertyWriter, MemberSummaryWriter
/*     */ {
/*     */   public PropertyWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  52 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  60 */     paramContent.addContent(HtmlConstants.START_OF_PROPERTY_SUMMARY);
/*  61 */     Content localContent = this.writer.getMemberTreeHeader();
/*  62 */     this.writer.addSummaryHeader(this, paramClassDoc, localContent);
/*  63 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getPropertyDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  71 */     paramContent.addContent(HtmlConstants.START_OF_PROPERTY_DETAILS);
/*  72 */     Content localContent = this.writer.getMemberTreeHeader();
/*  73 */     localContent.addContent(this.writer.getMarkerAnchor(SectionName.PROPERTY_DETAIL));
/*     */ 
/*  75 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.propertyDetailsLabel);
/*     */ 
/*  77 */     localContent.addContent(localHtmlTree);
/*  78 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getPropertyDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/*  86 */     paramContent.addContent(this.writer
/*  87 */       .getMarkerAnchor(paramMethodDoc
/*  87 */       .name()));
/*  88 */     Content localContent = this.writer.getMemberTreeHeader();
/*  89 */     HtmlTree localHtmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/*  90 */     localHtmlTree.addContent(paramMethodDoc.name().substring(0, paramMethodDoc.name().lastIndexOf("Property")));
/*  91 */     localContent.addContent(localHtmlTree);
/*  92 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getSignature(MethodDoc paramMethodDoc)
/*     */   {
/*  99 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.PRE);
/* 100 */     this.writer.addAnnotationInfo(paramMethodDoc, localHtmlTree);
/* 101 */     addModifiers(paramMethodDoc, localHtmlTree);
/* 102 */     Content localContent = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramMethodDoc
/* 104 */       .returnType()));
/* 105 */     localHtmlTree.addContent(localContent);
/* 106 */     localHtmlTree.addContent(" ");
/* 107 */     if (this.configuration.linksource) {
/* 108 */       StringContent localStringContent = new StringContent(paramMethodDoc.name());
/* 109 */       this.writer.addSrcLink(paramMethodDoc, localStringContent, localHtmlTree);
/*     */     } else {
/* 111 */       addName(paramMethodDoc.name(), localHtmlTree);
/*     */     }
/* 113 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void addDeprecated(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addComments(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 126 */     ClassDoc localClassDoc = paramMethodDoc.containingClass();
/* 127 */     if (paramMethodDoc.inlineTags().length > 0)
/* 128 */       if ((localClassDoc.equals(this.classdoc)) || (
/* 129 */         (!localClassDoc
/* 129 */         .isPublic()) && (!Util.isLinkable(localClassDoc, this.configuration)))) {
/* 130 */         this.writer.addInlineComment(paramMethodDoc, paramContent);
/*     */       }
/*     */       else {
/* 133 */         Content localContent = this.writer
/* 133 */           .getDocLink(LinkInfoImpl.Kind.PROPERTY_DOC_COPY, localClassDoc, paramMethodDoc, localClassDoc
/* 135 */           .isIncluded() ? localClassDoc
/* 136 */           .typeName() : localClassDoc.qualifiedTypeName(), false);
/*     */ 
/* 138 */         HtmlTree localHtmlTree1 = HtmlTree.CODE(localContent);
/* 139 */         HtmlTree localHtmlTree2 = HtmlTree.SPAN(HtmlStyle.descfrmTypeLabel, localClassDoc.isClass() ? this.writer.descfrmClassLabel : this.writer.descfrmInterfaceLabel);
/*     */ 
/* 141 */         localHtmlTree2.addContent(this.writer.getSpace());
/* 142 */         localHtmlTree2.addContent(localHtmlTree1);
/* 143 */         paramContent.addContent(HtmlTree.DIV(HtmlStyle.block, localHtmlTree2));
/* 144 */         this.writer.addInlineComment(paramMethodDoc, paramContent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addTags(MethodDoc paramMethodDoc, Content paramContent)
/*     */   {
/* 153 */     this.writer.addTagsInfo(paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getPropertyDetails(Content paramContent)
/*     */   {
/* 160 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getPropertyDoc(Content paramContent, boolean paramBoolean)
/*     */   {
/* 168 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 175 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public int getMemberKind() {
/* 179 */     return 8;
/*     */   }
/*     */ 
/*     */   public void addSummaryLabel(Content paramContent)
/*     */   {
/* 186 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 187 */       .getResource("doclet.Property_Summary"));
/*     */ 
/* 188 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public String getTableSummary()
/*     */   {
/* 195 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 196 */       .getText("doclet.Property_Summary"), 
/* 196 */       this.configuration
/* 197 */       .getText("doclet.properties"));
/*     */   }
/*     */ 
/*     */   public Content getCaption()
/*     */   {
/* 204 */     return this.configuration.getResource("doclet.Properties");
/*     */   }
/*     */ 
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 213 */     String[] arrayOfString = { this.configuration
/* 212 */       .getText("doclet.Type"), 
/* 212 */       this.configuration
/* 213 */       .getText("doclet.0_and_1", this.configuration
/* 214 */       .getText("doclet.Property"), 
/* 214 */       this.configuration
/* 215 */       .getText("doclet.Description")) };
/*     */ 
/* 217 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 224 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.PROPERTY_SUMMARY));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 232 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.PROPERTIES_INHERITANCE, this.configuration
/* 234 */       .getClassName(paramClassDoc)));
/*     */   }
/*     */ 
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 241 */     Content localContent = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/* 245 */     StringContent localStringContent = new StringContent(paramClassDoc.isClass() ? this.configuration
/* 244 */       .getText("doclet.Properties_Inherited_From_Class") : 
/* 244 */       this.configuration
/* 245 */       .getText("doclet.Properties_Inherited_From_Interface"));
/*     */ 
/* 246 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, localStringContent);
/*     */ 
/* 248 */     localHtmlTree.addContent(this.writer.getSpace());
/* 249 */     localHtmlTree.addContent(localContent);
/* 250 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 258 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 259 */       .getDocLink(paramKind, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 261 */       .name().substring(0, paramProgramElementDoc.name().lastIndexOf("Property")), false, true));
/*     */ 
/* 265 */     HtmlTree localHtmlTree2 = HtmlTree.CODE(localHtmlTree1);
/* 266 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 274 */     paramContent.addContent(this.writer
/* 275 */       .getDocLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, (MemberDoc)paramProgramElementDoc, 
/* 276 */       (paramProgramElementDoc
/* 276 */       .name().lastIndexOf("Property") != -1) && (this.configuration.javafx) ? paramProgramElementDoc
/* 277 */       .name().substring(0, paramProgramElementDoc.name().length() - "Property".length()) : paramProgramElementDoc
/* 278 */       .name(), false, true));
/*     */   }
/*     */ 
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 286 */     MethodDoc localMethodDoc = (MethodDoc)paramProgramElementDoc;
/* 287 */     addModifierAndType(localMethodDoc, localMethodDoc.returnType(), paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 294 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((MethodDoc)paramProgramElementDoc)
/* 295 */       .qualifiedName());
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 302 */     if (paramBoolean) {
/* 303 */       if (paramClassDoc == null) {
/* 304 */         return this.writer.getHyperLink(SectionName.PROPERTY_SUMMARY, this.writer
/* 306 */           .getResource("doclet.navProperty"));
/*     */       }
/*     */ 
/* 308 */       return this.writer.getHyperLink(SectionName.PROPERTIES_INHERITANCE, this.configuration
/* 310 */         .getClassName(paramClassDoc), 
/* 310 */         this.writer.getResource("doclet.navProperty"));
/*     */     }
/*     */ 
/* 313 */     return this.writer.getResource("doclet.navProperty");
/*     */   }
/*     */ 
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent)
/*     */   {
/* 321 */     if (paramBoolean) {
/* 322 */       paramContent.addContent(this.writer.getHyperLink(SectionName.PROPERTY_DETAIL, this.writer
/* 324 */         .getResource("doclet.navProperty")));
/*     */     }
/*     */     else
/*     */     {
/* 326 */       paramContent.addContent(this.writer.getResource("doclet.navProperty"));
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.PropertyWriterImpl
 * JD-Core Version:    0.6.2
 */