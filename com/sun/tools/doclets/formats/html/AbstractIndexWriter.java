/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AbstractIndexWriter extends HtmlDocletWriter
/*     */ {
/*     */   protected IndexBuilder indexbuilder;
/*     */ 
/*     */   protected AbstractIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder)
/*     */     throws IOException
/*     */   {
/*  70 */     super(paramConfigurationImpl, paramDocPath);
/*  71 */     this.indexbuilder = paramIndexBuilder;
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkIndex()
/*     */   {
/*  80 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.indexLabel);
/*  81 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   protected void addContents(Character paramCharacter, List<? extends Doc> paramList, Content paramContent)
/*     */   {
/*  94 */     String str = paramCharacter.toString();
/*  95 */     paramContent.addContent(getMarkerAnchorForIndex(str));
/*  96 */     StringContent localStringContent = new StringContent(str);
/*  97 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, false, HtmlStyle.title, localStringContent);
/*     */ 
/*  99 */     paramContent.addContent(localHtmlTree1);
/* 100 */     int i = paramList.size();
/*     */ 
/* 102 */     if (i > 0) {
/* 103 */       HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DL);
/* 104 */       for (int j = 0; j < i; j++) {
/* 105 */         Doc localDoc = (Doc)paramList.get(j);
/* 106 */         if ((localDoc instanceof MemberDoc))
/* 107 */           addDescription((MemberDoc)localDoc, localHtmlTree2);
/* 108 */         else if ((localDoc instanceof ClassDoc))
/* 109 */           addDescription((ClassDoc)localDoc, localHtmlTree2);
/* 110 */         else if ((localDoc instanceof PackageDoc)) {
/* 111 */           addDescription((PackageDoc)localDoc, localHtmlTree2);
/*     */         }
/*     */       }
/* 114 */       paramContent.addContent(localHtmlTree2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addDescription(PackageDoc paramPackageDoc, Content paramContent)
/*     */   {
/* 125 */     Content localContent = getPackageLink(paramPackageDoc, new StringContent(Util.getPackageName(paramPackageDoc)));
/* 126 */     HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 127 */     localHtmlTree1.addContent(" - ");
/* 128 */     localHtmlTree1.addContent(getResource("doclet.package"));
/* 129 */     localHtmlTree1.addContent(" " + paramPackageDoc.name());
/* 130 */     paramContent.addContent(localHtmlTree1);
/* 131 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DD);
/* 132 */     addSummaryComment(paramPackageDoc, localHtmlTree2);
/* 133 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addDescription(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 143 */     Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.INDEX, paramClassDoc)
/* 144 */       .strong(true));
/*     */ 
/* 145 */     HtmlTree localHtmlTree1 = HtmlTree.DT(localContent);
/* 146 */     localHtmlTree1.addContent(" - ");
/* 147 */     addClassInfo(paramClassDoc, localHtmlTree1);
/* 148 */     paramContent.addContent(localHtmlTree1);
/* 149 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DD);
/* 150 */     addComment(paramClassDoc, localHtmlTree2);
/* 151 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addClassInfo(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 162 */     paramContent.addContent(getResource("doclet.in", 
/* 163 */       Util.getTypeName(this.configuration, paramClassDoc, false), 
/* 164 */       getPackageLink(paramClassDoc
/* 164 */       .containingPackage(), 
/* 165 */       Util.getPackageName(paramClassDoc
/* 165 */       .containingPackage()))));
/*     */   }
/*     */ 
/*     */   protected void addDescription(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 178 */     String str = (paramMemberDoc instanceof ExecutableMemberDoc) ? paramMemberDoc
/* 177 */       .name() + ((ExecutableMemberDoc)paramMemberDoc).flatSignature() : paramMemberDoc
/* 178 */       .name();
/* 179 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, 
/* 180 */       getDocLink(LinkInfoImpl.Kind.INDEX, paramMemberDoc, str));
/*     */ 
/* 181 */     HtmlTree localHtmlTree2 = HtmlTree.DT(localHtmlTree1);
/* 182 */     localHtmlTree2.addContent(" - ");
/* 183 */     addMemberDesc(paramMemberDoc, localHtmlTree2);
/* 184 */     paramContent.addContent(localHtmlTree2);
/* 185 */     HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DD);
/* 186 */     addComment(paramMemberDoc, localHtmlTree3);
/* 187 */     paramContent.addContent(localHtmlTree3);
/*     */   }
/*     */ 
/*     */   protected void addComment(ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 201 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 202 */     HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 203 */     localHtmlTree2.addStyle(HtmlStyle.block);
/* 204 */     if (Util.isDeprecated(paramProgramElementDoc)) {
/* 205 */       localHtmlTree2.addContent(localHtmlTree1);
/*     */       Tag[] arrayOfTag;
/* 206 */       if ((arrayOfTag = paramProgramElementDoc.tags("deprecated")).length > 0)
/* 207 */         addInlineDeprecatedComment(paramProgramElementDoc, arrayOfTag[0], localHtmlTree2);
/* 208 */       paramContent.addContent(localHtmlTree2);
/*     */     } else {
/* 210 */       ClassDoc localClassDoc = paramProgramElementDoc.containingClass();
/* 211 */       while (localClassDoc != null) {
/* 212 */         if (Util.isDeprecated(localClassDoc)) {
/* 213 */           localHtmlTree2.addContent(localHtmlTree1);
/* 214 */           paramContent.addContent(localHtmlTree2);
/* 215 */           break;
/*     */         }
/* 217 */         localClassDoc = localClassDoc.containingClass();
/*     */       }
/* 219 */       addSummaryComment(paramProgramElementDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addMemberDesc(MemberDoc paramMemberDoc, Content paramContent)
/*     */   {
/* 231 */     ClassDoc localClassDoc = paramMemberDoc.containingClass();
/* 232 */     String str = Util.getTypeName(this.configuration, localClassDoc, true) + " ";
/*     */ 
/* 234 */     if (paramMemberDoc.isField()) {
/* 235 */       if (paramMemberDoc.isStatic()) {
/* 236 */         paramContent.addContent(
/* 237 */           getResource("doclet.Static_variable_in", str));
/*     */       }
/*     */       else {
/* 239 */         paramContent.addContent(
/* 240 */           getResource("doclet.Variable_in", str));
/*     */       }
/*     */     }
/* 242 */     else if (paramMemberDoc.isConstructor()) {
/* 243 */       paramContent.addContent(
/* 244 */         getResource("doclet.Constructor_for", str));
/*     */     }
/* 245 */     else if (paramMemberDoc.isMethod()) {
/* 246 */       if (paramMemberDoc.isStatic()) {
/* 247 */         paramContent.addContent(
/* 248 */           getResource("doclet.Static_method_in", str));
/*     */       }
/*     */       else {
/* 250 */         paramContent.addContent(
/* 251 */           getResource("doclet.Method_in", str));
/*     */       }
/*     */     }
/*     */ 
/* 254 */     addPreQualifiedClassLink(LinkInfoImpl.Kind.INDEX, localClassDoc, false, paramContent);
/*     */   }
/*     */ 
/*     */   public Content getMarkerAnchorForIndex(String paramString)
/*     */   {
/* 265 */     return getMarkerAnchor(getNameForIndex(paramString), null);
/*     */   }
/*     */ 
/*     */   public String getNameForIndex(String paramString)
/*     */   {
/* 275 */     return "I:" + getName(paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AbstractIndexWriter
 * JD-Core Version:    0.6.2
 */