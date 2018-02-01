/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public class StringContent extends Content
/*     */ {
/*     */   private StringBuilder stringContent;
/*     */ 
/*     */   public StringContent()
/*     */   {
/*  52 */     this.stringContent = new StringBuilder();
/*     */   }
/*     */ 
/*     */   public StringContent(String paramString)
/*     */   {
/*  61 */     this.stringContent = new StringBuilder();
/*  62 */     appendChars(paramString);
/*     */   }
/*     */ 
/*     */   public void addContent(Content paramContent)
/*     */   {
/*  75 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */   public void addContent(String paramString)
/*     */   {
/*  86 */     appendChars(paramString);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  94 */     return this.stringContent.length() == 0;
/*     */   }
/*     */ 
/*     */   public int charCount()
/*     */   {
/*  99 */     return RawHtml.charCount(this.stringContent.toString());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 107 */     return this.stringContent.toString();
/*     */   }
/*     */ 
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 115 */     String str = this.stringContent.toString();
/* 116 */     paramWriter.write(str);
/* 117 */     return str.endsWith(DocletConstants.NL);
/*     */   }
/*     */ 
/*     */   private void appendChars(String paramString) {
/* 121 */     for (int i = 0; i < paramString.length(); i++) {
/* 122 */       char c = paramString.charAt(i);
/* 123 */       switch (c) { case '<':
/* 124 */         this.stringContent.append("&lt;"); break;
/*     */       case '>':
/* 125 */         this.stringContent.append("&gt;"); break;
/*     */       case '&':
/* 126 */         this.stringContent.append("&amp;"); break;
/*     */       default:
/* 127 */         this.stringContent.append(c);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.StringContent
 * JD-Core Version:    0.6.2
 */