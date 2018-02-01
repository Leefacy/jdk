/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ProfilePackageFrameWriter extends HtmlDocletWriter
/*     */ {
/*     */   private PackageDoc packageDoc;
/*     */ 
/*     */   public ProfilePackageFrameWriter(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc, String paramString)
/*     */     throws IOException
/*     */   {
/*  71 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc).resolve(
/*  72 */       DocPaths.profilePackageFrame(paramString)));
/*     */ 
/*  73 */     this.packageDoc = paramPackageDoc;
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       String str1 = Profile.lookup(paramInt).name;
/*  89 */       ProfilePackageFrameWriter localProfilePackageFrameWriter = new ProfilePackageFrameWriter(paramConfigurationImpl, paramPackageDoc, str1);
/*     */ 
/*  91 */       StringBuilder localStringBuilder = new StringBuilder(str1);
/*  92 */       String str2 = " - ";
/*  93 */       localStringBuilder.append(str2);
/*  94 */       String str3 = Util.getPackageName(paramPackageDoc);
/*  95 */       localStringBuilder.append(str3);
/*  96 */       HtmlTree localHtmlTree1 = localProfilePackageFrameWriter.getBody(false, localProfilePackageFrameWriter
/*  97 */         .getWindowTitle(localStringBuilder
/*  97 */         .toString()));
/*  98 */       StringContent localStringContent1 = new StringContent(str1);
/*  99 */       StringContent localStringContent2 = new StringContent(str2);
/* 100 */       RawHtml localRawHtml = new RawHtml(str3);
/* 101 */       HtmlTree localHtmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.bar, localProfilePackageFrameWriter
/* 102 */         .getTargetProfileLink("classFrame", localStringContent1, str1));
/*     */ 
/* 103 */       localHtmlTree2.addContent(localStringContent2);
/* 104 */       localHtmlTree2.addContent(localProfilePackageFrameWriter.getTargetProfilePackageLink(paramPackageDoc, "classFrame", localRawHtml, str1));
/*     */ 
/* 106 */       localHtmlTree1.addContent(localHtmlTree2);
/* 107 */       HtmlTree localHtmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 108 */       localHtmlTree3.addStyle(HtmlStyle.indexContainer);
/* 109 */       localProfilePackageFrameWriter.addClassListing(localHtmlTree3, paramInt);
/* 110 */       localHtmlTree1.addContent(localHtmlTree3);
/* 111 */       localProfilePackageFrameWriter.printHtmlDocument(paramConfigurationImpl.metakeywords
/* 112 */         .getMetaKeywords(paramPackageDoc), 
/* 112 */         false, localHtmlTree1);
/* 113 */       localProfilePackageFrameWriter.close();
/*     */     } catch (IOException localIOException) {
/* 115 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/* 117 */         .toString(), DocPaths.PACKAGE_FRAME.getPath() });
/* 118 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addClassListing(Content paramContent, int paramInt)
/*     */   {
/* 131 */     if (this.packageDoc.isIncluded()) {
/* 132 */       addClassKindListing(this.packageDoc.interfaces(), 
/* 133 */         getResource("doclet.Interfaces"), 
/* 133 */         paramContent, paramInt);
/* 134 */       addClassKindListing(this.packageDoc.ordinaryClasses(), 
/* 135 */         getResource("doclet.Classes"), 
/* 135 */         paramContent, paramInt);
/* 136 */       addClassKindListing(this.packageDoc.enums(), 
/* 137 */         getResource("doclet.Enums"), 
/* 137 */         paramContent, paramInt);
/* 138 */       addClassKindListing(this.packageDoc.exceptions(), 
/* 139 */         getResource("doclet.Exceptions"), 
/* 139 */         paramContent, paramInt);
/* 140 */       addClassKindListing(this.packageDoc.errors(), 
/* 141 */         getResource("doclet.Errors"), 
/* 141 */         paramContent, paramInt);
/* 142 */       addClassKindListing(this.packageDoc.annotationTypes(), 
/* 143 */         getResource("doclet.AnnotationTypes"), 
/* 143 */         paramContent, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addClassKindListing(ClassDoc[] paramArrayOfClassDoc, Content paramContent1, Content paramContent2, int paramInt)
/*     */   {
/* 157 */     if (paramArrayOfClassDoc.length > 0) {
/* 158 */       Arrays.sort(paramArrayOfClassDoc);
/* 159 */       int i = 0;
/* 160 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.UL);
/* 161 */       localHtmlTree1.setTitle(paramContent1);
/* 162 */       for (int j = 0; j < paramArrayOfClassDoc.length; j++)
/* 163 */         if (isTypeInProfile(paramArrayOfClassDoc[j], paramInt))
/*     */         {
/* 166 */           if ((Util.isCoreClass(paramArrayOfClassDoc[j])) && 
/* 167 */             (this.configuration
/* 167 */             .isGeneratedDoc(paramArrayOfClassDoc[j])))
/*     */           {
/* 170 */             if (i == 0) {
/* 171 */               localObject = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, paramContent1);
/*     */ 
/* 173 */               paramContent2.addContent((Content)localObject);
/* 174 */               i = 1;
/*     */             }
/* 176 */             Object localObject = new StringContent(paramArrayOfClassDoc[j].name());
/* 177 */             if (paramArrayOfClassDoc[j].isInterface()) localObject = HtmlTree.SPAN(HtmlStyle.interfaceName, (Content)localObject);
/* 178 */             Content localContent = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE_FRAME, paramArrayOfClassDoc[j])
/* 179 */               .label((Content)localObject)
/* 179 */               .target("classFrame"));
/* 180 */             HtmlTree localHtmlTree2 = HtmlTree.LI(localContent);
/* 181 */             localHtmlTree1.addContent(localHtmlTree2);
/*     */           }
/*     */         }
/* 183 */       paramContent2.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ProfilePackageFrameWriter
 * JD-Core Version:    0.6.2
 */