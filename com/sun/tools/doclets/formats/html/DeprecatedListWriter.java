/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DeprecatedAPIListBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class DeprecatedListWriter extends SubWriterHolderWriter
/*     */ {
/*  49 */   private static final String[] ANCHORS = { "package", "interface", "class", "enum", "exception", "error", "annotation.type", "field", "method", "constructor", "enum.constant", "annotation.type.member" };
/*     */ 
/*  55 */   private static final String[] HEADING_KEYS = { "doclet.Deprecated_Packages", "doclet.Deprecated_Interfaces", "doclet.Deprecated_Classes", "doclet.Deprecated_Enums", "doclet.Deprecated_Exceptions", "doclet.Deprecated_Errors", "doclet.Deprecated_Annotation_Types", "doclet.Deprecated_Fields", "doclet.Deprecated_Methods", "doclet.Deprecated_Constructors", "doclet.Deprecated_Enum_Constants", "doclet.Deprecated_Annotation_Type_Members" };
/*     */ 
/*  66 */   private static final String[] SUMMARY_KEYS = { "doclet.deprecated_packages", "doclet.deprecated_interfaces", "doclet.deprecated_classes", "doclet.deprecated_enums", "doclet.deprecated_exceptions", "doclet.deprecated_errors", "doclet.deprecated_annotation_types", "doclet.deprecated_fields", "doclet.deprecated_methods", "doclet.deprecated_constructors", "doclet.deprecated_enum_constants", "doclet.deprecated_annotation_type_members" };
/*     */ 
/*  77 */   private static final String[] HEADER_KEYS = { "doclet.Package", "doclet.Interface", "doclet.Class", "doclet.Enum", "doclet.Exceptions", "doclet.Errors", "doclet.AnnotationType", "doclet.Field", "doclet.Method", "doclet.Constructor", "doclet.Enum_Constant", "doclet.Annotation_Type_Member" };
/*     */   private AbstractMemberWriter[] writers;
/*     */   private ConfigurationImpl configuration;
/*     */ 
/*     */   public DeprecatedListWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  99 */     super(paramConfigurationImpl, paramDocPath);
/* 100 */     this.configuration = paramConfigurationImpl;
/* 101 */     NestedClassWriterImpl localNestedClassWriterImpl = new NestedClassWriterImpl(this);
/* 102 */     this.writers = new AbstractMemberWriter[] { localNestedClassWriterImpl, localNestedClassWriterImpl, localNestedClassWriterImpl, localNestedClassWriterImpl, localNestedClassWriterImpl, localNestedClassWriterImpl, new FieldWriterImpl(this), new MethodWriterImpl(this), new ConstructorWriterImpl(this), new EnumConstantWriterImpl(this), new AnnotationTypeOptionalMemberWriterImpl(this, null) };
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl)
/*     */   {
/* 119 */     DocPath localDocPath = DocPaths.DEPRECATED_LIST;
/*     */     try {
/* 121 */       DeprecatedListWriter localDeprecatedListWriter = new DeprecatedListWriter(paramConfigurationImpl, localDocPath);
/*     */ 
/* 123 */       localDeprecatedListWriter.generateDeprecatedListFile(new DeprecatedAPIListBuilder(paramConfigurationImpl));
/*     */ 
/* 125 */       localDeprecatedListWriter.close();
/*     */     } catch (IOException localIOException) {
/* 127 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 129 */         .toString(), localDocPath });
/* 130 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateDeprecatedListFile(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder)
/*     */     throws IOException
/*     */   {
/* 141 */     Content localContent = getHeader();
/* 142 */     localContent.addContent(getContentsList(paramDeprecatedAPIListBuilder));
/*     */ 
/* 144 */     String[] arrayOfString = new String[1];
/* 145 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV);
/* 146 */     localHtmlTree.addStyle(HtmlStyle.contentContainer);
/* 147 */     for (int i = 0; i < 12; i++) {
/* 148 */       if (paramDeprecatedAPIListBuilder.hasDocumentation(i)) {
/* 149 */         addAnchor(paramDeprecatedAPIListBuilder, i, localHtmlTree);
/*     */ 
/* 151 */         String str = this.configuration
/* 151 */           .getText("doclet.Member_Table_Summary", this.configuration
/* 152 */           .getText(HEADING_KEYS[i]), 
/* 152 */           this.configuration
/* 153 */           .getText(SUMMARY_KEYS[i]));
/*     */ 
/* 154 */         arrayOfString[0] = this.configuration.getText("doclet.0_and_1", this.configuration
/* 155 */           .getText(HEADER_KEYS[i]), 
/* 155 */           this.configuration
/* 156 */           .getText("doclet.Description"));
/*     */ 
/* 159 */         if (i == 0) {
/* 160 */           addPackageDeprecatedAPI(paramDeprecatedAPIListBuilder.getList(i), HEADING_KEYS[i], str, arrayOfString, localHtmlTree);
/*     */         }
/*     */         else {
/* 163 */           this.writers[(i - 1)].addDeprecatedAPI(paramDeprecatedAPIListBuilder.getList(i), HEADING_KEYS[i], str, arrayOfString, localHtmlTree);
/*     */         }
/*     */       }
/*     */     }
/* 167 */     localContent.addContent(localHtmlTree);
/* 168 */     addNavLinks(false, localContent);
/* 169 */     addBottom(localContent);
/* 170 */     printHtmlDocument(null, true, localContent);
/*     */   }
/*     */ 
/*     */   private void addIndexLink(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder, int paramInt, Content paramContent)
/*     */   {
/* 182 */     if (paramDeprecatedAPIListBuilder.hasDocumentation(paramInt)) {
/* 183 */       HtmlTree localHtmlTree = HtmlTree.LI(getHyperLink(ANCHORS[paramInt], 
/* 184 */         getResource(HEADING_KEYS[paramInt])));
/*     */ 
/* 185 */       paramContent.addContent(localHtmlTree);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Content getContentsList(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder)
/*     */   {
/* 196 */     Content localContent1 = getResource("doclet.Deprecated_API");
/* 197 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, localContent1);
/*     */ 
/* 199 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 200 */     Content localContent2 = getResource("doclet.Contents");
/* 201 */     localHtmlTree2.addContent(HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, localContent2));
/*     */ 
/* 203 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.UL);
/* 204 */     for (int i = 0; i < 12; i++) {
/* 205 */       addIndexLink(paramDeprecatedAPIListBuilder, i, localHtmlTree3);
/*     */     }
/* 207 */     localHtmlTree2.addContent(localHtmlTree3);
/* 208 */     return localHtmlTree2;
/*     */   }
/*     */ 
/*     */   private void addAnchor(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder, int paramInt, Content paramContent)
/*     */   {
/* 219 */     if (paramDeprecatedAPIListBuilder.hasDocumentation(paramInt))
/* 220 */       paramContent.addContent(getMarkerAnchor(ANCHORS[paramInt]));
/*     */   }
/*     */ 
/*     */   public Content getHeader()
/*     */   {
/* 230 */     String str = this.configuration.getText("doclet.Window_Deprecated_List");
/* 231 */     HtmlTree localHtmlTree = getBody(true, getWindowTitle(str));
/* 232 */     addTop(localHtmlTree);
/* 233 */     addNavLinks(true, localHtmlTree);
/* 234 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkDeprecated()
/*     */   {
/* 243 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.deprecatedLabel);
/* 244 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.DeprecatedListWriter
 * JD-Core Version:    0.6.2
 */