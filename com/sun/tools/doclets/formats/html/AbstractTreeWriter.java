/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlAttr;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractTreeWriter extends HtmlDocletWriter
/*     */ {
/*     */   protected final ClassTree classtree;
/*     */   private static final String LI_CIRCLE = "circle";
/*     */ 
/*     */   protected AbstractTreeWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, ClassTree paramClassTree)
/*     */     throws IOException
/*     */   {
/*  71 */     super(paramConfigurationImpl, paramDocPath);
/*  72 */     this.classtree = paramClassTree;
/*     */   }
/*     */ 
/*     */   protected void addLevelInfo(ClassDoc paramClassDoc, List<ClassDoc> paramList, boolean paramBoolean, Content paramContent)
/*     */   {
/*  87 */     int i = paramList.size();
/*  88 */     if (i > 0) {
/*  89 */       HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.UL);
/*  90 */       for (int j = 0; j < i; j++) {
/*  91 */         ClassDoc localClassDoc = (ClassDoc)paramList.get(j);
/*  92 */         HtmlTree localHtmlTree2 = new HtmlTree(HtmlTag.LI);
/*  93 */         localHtmlTree2.addAttr(HtmlAttr.TYPE, "circle");
/*  94 */         addPartialInfo(localClassDoc, localHtmlTree2);
/*  95 */         addExtendsImplements(paramClassDoc, localClassDoc, localHtmlTree2);
/*  96 */         addLevelInfo(localClassDoc, this.classtree.subs(localClassDoc, paramBoolean), paramBoolean, localHtmlTree2);
/*     */ 
/*  98 */         localHtmlTree1.addContent(localHtmlTree2);
/*     */       }
/* 100 */       paramContent.addContent(localHtmlTree1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addTree(List<ClassDoc> paramList, String paramString, Content paramContent)
/*     */   {
/* 114 */     if (paramList.size() > 0) {
/* 115 */       ClassDoc localClassDoc = (ClassDoc)paramList.get(0);
/* 116 */       Content localContent = getResource(paramString);
/* 117 */       paramContent.addContent(HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, localContent));
/*     */ 
/* 119 */       addLevelInfo(!localClassDoc.isInterface() ? localClassDoc : null, paramList, paramList == this.classtree
/* 120 */         .baseEnums(), paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addExtendsImplements(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, Content paramContent)
/*     */   {
/* 134 */     ClassDoc[] arrayOfClassDoc = paramClassDoc2.interfaces();
/* 135 */     if (arrayOfClassDoc.length > (paramClassDoc2.isInterface() ? 1 : 0)) {
/* 136 */       Arrays.sort(arrayOfClassDoc);
/* 137 */       int i = 0;
/* 138 */       for (int j = 0; j < arrayOfClassDoc.length; j++) {
/* 139 */         if ((paramClassDoc1 != arrayOfClassDoc[j]) && (
/* 140 */           (arrayOfClassDoc[j].isPublic()) || 
/* 141 */           (Util.isLinkable(arrayOfClassDoc[j], this.configuration))))
/*     */         {
/* 144 */           if (i == 0) {
/* 145 */             if (paramClassDoc2.isInterface()) {
/* 146 */               paramContent.addContent(" (");
/* 147 */               paramContent.addContent(getResource("doclet.also"));
/* 148 */               paramContent.addContent(" extends ");
/*     */             } else {
/* 150 */               paramContent.addContent(" (implements ");
/*     */             }
/*     */           }
/* 153 */           else paramContent.addContent(", ");
/*     */ 
/* 155 */           addPreQualifiedClassLink(LinkInfoImpl.Kind.TREE, arrayOfClassDoc[j], paramContent);
/*     */ 
/* 157 */           i++;
/*     */         }
/*     */       }
/* 160 */       if (i > 0)
/* 161 */         paramContent.addContent(")");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addPartialInfo(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 173 */     addPreQualifiedStrongClassLink(LinkInfoImpl.Kind.TREE, paramClassDoc, paramContent);
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkTree()
/*     */   {
/* 182 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.treeLabel);
/* 183 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AbstractTreeWriter
 * JD-Core Version:    0.6.2
 */