/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstructorWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ConstructorWriterImpl extends AbstractExecutableMemberWriter
/*     */   implements ConstructorWriter, MemberSummaryWriter
/*     */ {
/*  51 */   private boolean foundNonPubConstructor = false;
/*     */ 
/*     */   public ConstructorWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  61 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*  62 */     VisibleMemberMap localVisibleMemberMap = new VisibleMemberMap(paramClassDoc, 3, this.configuration);
/*     */ 
/*  64 */     ArrayList localArrayList = new ArrayList(localVisibleMemberMap.getMembersFor(paramClassDoc));
/*  65 */     for (int i = 0; i < localArrayList.size(); i++)
/*  66 */       if ((((ProgramElementDoc)localArrayList.get(i)).isProtected()) || 
/*  67 */         (((ProgramElementDoc)localArrayList
/*  67 */         .get(i))
/*  67 */         .isPrivate()))
/*  68 */         setFoundNonPubConstructor(true);
/*     */   }
/*     */ 
/*     */   public ConstructorWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter)
/*     */   {
/*  79 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  87 */     paramContent.addContent(HtmlConstants.START_OF_CONSTRUCTOR_SUMMARY);
/*  88 */     Content localContent = this.writer.getMemberTreeHeader();
/*  89 */     this.writer.addSummaryHeader(this, paramClassDoc, localContent);
/*  90 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getConstructorDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/*  98 */     paramContent.addContent(HtmlConstants.START_OF_CONSTRUCTOR_DETAILS);
/*  99 */     Content localContent = this.writer.getMemberTreeHeader();
/* 100 */     localContent.addContent(this.writer.getMarkerAnchor(SectionName.CONSTRUCTOR_DETAIL));
/*     */ 
/* 102 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.constructorDetailsLabel);
/*     */ 
/* 104 */     localContent.addContent(localHtmlTree);
/* 105 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getConstructorDocTreeHeader(ConstructorDoc paramConstructorDoc, Content paramContent)
/*     */   {
/*     */     String str;
/* 114 */     if ((str = getErasureAnchor(paramConstructorDoc)) != null) {
/* 115 */       paramContent.addContent(this.writer.getMarkerAnchor(str));
/*     */     }
/* 117 */     paramContent.addContent(this.writer
/* 118 */       .getMarkerAnchor(this.writer
/* 118 */       .getAnchor(paramConstructorDoc)));
/*     */ 
/* 119 */     Content localContent = this.writer.getMemberTreeHeader();
/* 120 */     HtmlTree localHtmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/* 121 */     localHtmlTree.addContent(paramConstructorDoc.name());
/* 122 */     localContent.addContent(localHtmlTree);
/* 123 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getSignature(ConstructorDoc paramConstructorDoc)
/*     */   {
/* 130 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.PRE);
/* 131 */     this.writer.addAnnotationInfo(paramConstructorDoc, localHtmlTree);
/* 132 */     addModifiers(paramConstructorDoc, localHtmlTree);
/* 133 */     if (this.configuration.linksource) {
/* 134 */       StringContent localStringContent = new StringContent(paramConstructorDoc.name());
/* 135 */       this.writer.addSrcLink(paramConstructorDoc, localStringContent, localHtmlTree);
/*     */     } else {
/* 137 */       addName(paramConstructorDoc.name(), localHtmlTree);
/*     */     }
/* 139 */     int i = localHtmlTree.charCount();
/* 140 */     addParameters(paramConstructorDoc, localHtmlTree, i);
/* 141 */     addExceptions(paramConstructorDoc, localHtmlTree, i);
/* 142 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public void setSummaryColumnStyle(HtmlTree paramHtmlTree)
/*     */   {
/* 150 */     if (this.foundNonPubConstructor)
/* 151 */       paramHtmlTree.addStyle(HtmlStyle.colLast);
/*     */     else
/* 153 */       paramHtmlTree.addStyle(HtmlStyle.colOne);
/*     */   }
/*     */ 
/*     */   public void addDeprecated(ConstructorDoc paramConstructorDoc, Content paramContent)
/*     */   {
/* 160 */     addDeprecatedInfo(paramConstructorDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addComments(ConstructorDoc paramConstructorDoc, Content paramContent)
/*     */   {
/* 167 */     addComment(paramConstructorDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public void addTags(ConstructorDoc paramConstructorDoc, Content paramContent)
/*     */   {
/* 174 */     this.writer.addTagsInfo(paramConstructorDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getConstructorDetails(Content paramContent)
/*     */   {
/* 181 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */   public Content getConstructorDoc(Content paramContent, boolean paramBoolean)
/*     */   {
/* 189 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 196 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void setFoundNonPubConstructor(boolean paramBoolean)
/*     */   {
/* 205 */     this.foundNonPubConstructor = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void addSummaryLabel(Content paramContent)
/*     */   {
/* 212 */     HtmlTree localHtmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 213 */       .getResource("doclet.Constructor_Summary"));
/*     */ 
/* 214 */     paramContent.addContent(localHtmlTree);
/*     */   }
/*     */ 
/*     */   public String getTableSummary()
/*     */   {
/* 221 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 222 */       .getText("doclet.Constructor_Summary"), 
/* 222 */       this.configuration
/* 223 */       .getText("doclet.constructors"));
/*     */   }
/*     */ 
/*     */   public Content getCaption()
/*     */   {
/* 230 */     return this.configuration.getResource("doclet.Constructors");
/*     */   }
/*     */ 
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/*     */     String[] arrayOfString;
/* 238 */     if (this.foundNonPubConstructor)
/*     */     {
/* 241 */       arrayOfString = new String[] { this.configuration
/* 240 */         .getText("doclet.Modifier"), 
/* 240 */         this.configuration
/* 241 */         .getText("doclet.0_and_1", this.configuration
/* 242 */         .getText("doclet.Constructor"), 
/* 242 */         this.configuration
/* 243 */         .getText("doclet.Description")) };
/*     */     }
/*     */     else
/*     */     {
/* 248 */       arrayOfString = new String[] { this.configuration
/* 248 */         .getText("doclet.0_and_1", this.configuration
/* 249 */         .getText("doclet.Constructor"), 
/* 249 */         this.configuration
/* 250 */         .getText("doclet.Description")) };
/*     */     }
/*     */ 
/* 253 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 260 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.CONSTRUCTOR_SUMMARY));
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
/*     */   public int getMemberKind()
/*     */   {
/* 277 */     return 3;
/*     */   }
/*     */ 
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 284 */     if (paramBoolean) {
/* 285 */       return this.writer.getHyperLink(SectionName.CONSTRUCTOR_SUMMARY, this.writer
/* 286 */         .getResource("doclet.navConstructor"));
/*     */     }
/*     */ 
/* 288 */     return this.writer.getResource("doclet.navConstructor");
/*     */   }
/*     */ 
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent)
/*     */   {
/* 296 */     if (paramBoolean) {
/* 297 */       paramContent.addContent(this.writer.getHyperLink(SectionName.CONSTRUCTOR_DETAIL, this.writer
/* 299 */         .getResource("doclet.navConstructor")));
/*     */     }
/*     */     else
/*     */     {
/* 301 */       paramContent.addContent(this.writer.getResource("doclet.navConstructor"));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 309 */     if (this.foundNonPubConstructor) {
/* 310 */       HtmlTree localHtmlTree = new HtmlTree(HtmlTag.CODE);
/* 311 */       if (paramProgramElementDoc.isProtected())
/* 312 */         localHtmlTree.addContent("protected ");
/* 313 */       else if (paramProgramElementDoc.isPrivate())
/* 314 */         localHtmlTree.addContent("private ");
/* 315 */       else if (paramProgramElementDoc.isPublic())
/* 316 */         localHtmlTree.addContent(this.writer.getSpace());
/*     */       else {
/* 318 */         localHtmlTree.addContent(this.configuration
/* 319 */           .getText("doclet.Package_private"));
/*     */       }
/*     */ 
/* 321 */       paramContent.addContent(localHtmlTree);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ConstructorWriterImpl
 * JD-Core Version:    0.6.2
 */