/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.SectionName;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public abstract class HtmlDocWriter extends HtmlWriter
/*     */ {
/*     */   public static final String CONTENT_TYPE = "text/html";
/*     */ 
/*     */   public HtmlDocWriter(Configuration paramConfiguration, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  67 */     super(paramConfiguration, paramDocPath);
/*  68 */     paramConfiguration.message.notice("doclet.Generating_0", new Object[] { 
/*  69 */       DocFile.createFileForOutput(paramConfiguration, paramDocPath)
/*  69 */       .getPath() });
/*     */   }
/*     */ 
/*     */   public abstract Configuration configuration();
/*     */ 
/*     */   public Content getHyperLink(DocPath paramDocPath, String paramString)
/*     */   {
/*  78 */     return getHyperLink(paramDocPath, new StringContent(paramString), false, "", "", "");
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(String paramString, Content paramContent)
/*     */   {
/*  91 */     return getHyperLink(getDocLink(paramString), paramContent, "", "");
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(SectionName paramSectionName, Content paramContent)
/*     */   {
/* 103 */     return getHyperLink(getDocLink(paramSectionName), paramContent, "", "");
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(SectionName paramSectionName, String paramString, Content paramContent)
/*     */   {
/* 118 */     return getHyperLink(getDocLink(paramSectionName, paramString), paramContent, "", "");
/*     */   }
/*     */ 
/*     */   public DocLink getDocLink(String paramString)
/*     */   {
/* 128 */     return DocLink.fragment(getName(paramString));
/*     */   }
/*     */ 
/*     */   public DocLink getDocLink(SectionName paramSectionName)
/*     */   {
/* 138 */     return DocLink.fragment(paramSectionName.getName());
/*     */   }
/*     */ 
/*     */   public DocLink getDocLink(SectionName paramSectionName, String paramString)
/*     */   {
/* 151 */     return DocLink.fragment(paramSectionName.getName() + getName(paramString));
/*     */   }
/*     */ 
/*     */   public String getName(String paramString)
/*     */   {
/* 161 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 173 */     for (int i = 0; i < paramString.length(); i++) {
/* 174 */       char c = paramString.charAt(i);
/* 175 */       switch (c) {
/*     */       case '(':
/*     */       case ')':
/*     */       case ',':
/*     */       case '<':
/*     */       case '>':
/* 181 */         localStringBuilder.append('-');
/* 182 */         break;
/*     */       case ' ':
/*     */       case '[':
/* 185 */         break;
/*     */       case ']':
/* 187 */         localStringBuilder.append(":A");
/* 188 */         break;
/*     */       case '$':
/* 194 */         if (i == 0)
/* 195 */           localStringBuilder.append("Z:Z");
/* 196 */         localStringBuilder.append(":D");
/* 197 */         break;
/*     */       case '_':
/* 201 */         if (i == 0)
/* 202 */           localStringBuilder.append("Z:Z");
/* 203 */         localStringBuilder.append(c);
/* 204 */         break;
/*     */       default:
/* 206 */         localStringBuilder.append(c);
/*     */       }
/*     */     }
/* 209 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(DocPath paramDocPath, Content paramContent)
/*     */   {
/* 220 */     return getHyperLink(paramDocPath, paramContent, "", "");
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(DocLink paramDocLink, Content paramContent) {
/* 224 */     return getHyperLink(paramDocLink, paramContent, "", "");
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(DocPath paramDocPath, Content paramContent, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 230 */     return getHyperLink(new DocLink(paramDocPath), paramContent, paramBoolean, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(DocLink paramDocLink, Content paramContent, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 237 */     Object localObject = paramContent;
/* 238 */     if (paramBoolean) {
/* 239 */       localObject = HtmlTree.SPAN(HtmlStyle.typeNameLink, (Content)localObject);
/*     */     }
/* 241 */     if ((paramString1 != null) && (paramString1.length() != 0)) {
/* 242 */       localHtmlTree = new HtmlTree(HtmlTag.FONT, new Content[] { localObject });
/* 243 */       localHtmlTree.addAttr(HtmlAttr.CLASS, paramString1);
/* 244 */       localObject = localHtmlTree;
/*     */     }
/* 246 */     HtmlTree localHtmlTree = HtmlTree.A(paramDocLink.toString(), (Content)localObject);
/* 247 */     if ((paramString2 != null) && (paramString2.length() != 0)) {
/* 248 */       localHtmlTree.addAttr(HtmlAttr.TITLE, paramString2);
/*     */     }
/* 250 */     if ((paramString3 != null) && (paramString3.length() != 0)) {
/* 251 */       localHtmlTree.addAttr(HtmlAttr.TARGET, paramString3);
/*     */     }
/* 253 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(DocPath paramDocPath, Content paramContent, String paramString1, String paramString2)
/*     */   {
/* 267 */     return getHyperLink(new DocLink(paramDocPath), paramContent, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public Content getHyperLink(DocLink paramDocLink, Content paramContent, String paramString1, String paramString2)
/*     */   {
/* 272 */     HtmlTree localHtmlTree = HtmlTree.A(paramDocLink.toString(), paramContent);
/* 273 */     if ((paramString1 != null) && (paramString1.length() != 0)) {
/* 274 */       localHtmlTree.addAttr(HtmlAttr.TITLE, paramString1);
/*     */     }
/* 276 */     if ((paramString2 != null) && (paramString2.length() != 0)) {
/* 277 */       localHtmlTree.addAttr(HtmlAttr.TARGET, paramString2);
/*     */     }
/* 279 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public String getPkgName(ClassDoc paramClassDoc)
/*     */   {
/* 288 */     String str = paramClassDoc.containingPackage().name();
/* 289 */     if (str.length() > 0) {
/* 290 */       str = str + ".";
/* 291 */       return str;
/*     */     }
/* 293 */     return "";
/*     */   }
/*     */ 
/*     */   public boolean getMemberDetailsListPrinted() {
/* 297 */     return this.memberDetailsListPrinted;
/*     */   }
/*     */ 
/*     */   public void printFramesetDocument(String paramString, boolean paramBoolean, Content paramContent)
/*     */     throws IOException
/*     */   {
/* 310 */     DocType localDocType = DocType.FRAMESET;
/* 311 */     Comment localComment = new Comment(this.configuration.getText("doclet.New_Page"));
/* 312 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.HEAD);
/* 313 */     localHtmlTree1.addContent(getGeneratedBy(!paramBoolean));
/* 314 */     if (this.configuration.charset.length() > 0) {
/* 315 */       localHtmlTree2 = HtmlTree.META("Content-Type", "text/html", this.configuration.charset);
/*     */ 
/* 317 */       localHtmlTree1.addContent(localHtmlTree2);
/*     */     }
/* 319 */     HtmlTree localHtmlTree2 = HtmlTree.TITLE(new StringContent(paramString));
/* 320 */     localHtmlTree1.addContent(localHtmlTree2);
/* 321 */     localHtmlTree1.addContent(getFramesetJavaScript());
/* 322 */     HtmlTree localHtmlTree3 = HtmlTree.HTML(this.configuration.getLocale().getLanguage(), localHtmlTree1, paramContent);
/*     */ 
/* 324 */     HtmlDocument localHtmlDocument = new HtmlDocument(localDocType, localComment, localHtmlTree3);
/*     */ 
/* 326 */     write(localHtmlDocument);
/*     */   }
/*     */ 
/*     */   protected Comment getGeneratedBy(boolean paramBoolean) {
/* 330 */     String str = "Generated by javadoc";
/* 331 */     if (paramBoolean) {
/* 332 */       GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
/* 333 */       Date localDate = localGregorianCalendar.getTime();
/* 334 */       str = str + " (" + this.configuration.getDocletSpecificBuildDate() + ") on " + localDate;
/*     */     }
/* 336 */     return new Comment(str);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.HtmlDocWriter
 * JD-Core Version:    0.6.2
 */