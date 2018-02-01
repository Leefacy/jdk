/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class HtmlDocument extends Content
/*     */ {
/*  47 */   private List<Content> docContent = Collections.emptyList();
/*     */ 
/*     */   public HtmlDocument(Content paramContent1, Content paramContent2, Content paramContent3)
/*     */   {
/*  57 */     this.docContent = new ArrayList();
/*  58 */     addContent((Content)nullCheck(paramContent1));
/*  59 */     addContent((Content)nullCheck(paramContent2));
/*  60 */     addContent((Content)nullCheck(paramContent3));
/*     */   }
/*     */ 
/*     */   public HtmlDocument(Content paramContent1, Content paramContent2)
/*     */   {
/*  70 */     this.docContent = new ArrayList();
/*  71 */     addContent((Content)nullCheck(paramContent1));
/*  72 */     addContent((Content)nullCheck(paramContent2));
/*     */   }
/*     */ 
/*     */   public final void addContent(Content paramContent)
/*     */   {
/*  81 */     if (paramContent.isValid())
/*  82 */       this.docContent.add(paramContent);
/*     */   }
/*     */ 
/*     */   public void addContent(String paramString)
/*     */   {
/*  94 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 101 */     return this.docContent.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 108 */     for (Content localContent : this.docContent)
/* 109 */       paramBoolean = localContent.write(paramWriter, paramBoolean);
/* 110 */     return paramBoolean;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.HtmlDocument
 * JD-Core Version:    0.6.2
 */