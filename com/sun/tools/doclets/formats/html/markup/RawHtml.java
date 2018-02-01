/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public class RawHtml extends Content
/*     */ {
/*     */   private String rawHtmlContent;
/*  48 */   public static final Content nbsp = new RawHtml("&nbsp;");
/*     */ 
/*     */   public RawHtml(String paramString)
/*     */   {
/*  56 */     this.rawHtmlContent = ((String)nullCheck(paramString));
/*     */   }
/*     */ 
/*     */   public void addContent(Content paramContent)
/*     */   {
/*  68 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */   public void addContent(String paramString)
/*     */   {
/*  80 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  87 */     return this.rawHtmlContent.isEmpty();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     return this.rawHtmlContent;
/*     */   }
/*     */ 
/*     */   public int charCount()
/*     */   {
/* 102 */     return charCount(this.rawHtmlContent);
/*     */   }
/*     */ 
/*     */   static int charCount(String paramString) {
/* 106 */     State localState = State.TEXT;
/* 107 */     int i = 0;
/* 108 */     for (int j = 0; j < paramString.length(); j++) {
/* 109 */       char c = paramString.charAt(j);
/* 110 */       switch (1.$SwitchMap$com$sun$tools$doclets$formats$html$markup$RawHtml$State[localState.ordinal()]) {
/*     */       case 1:
/* 112 */         switch (c) {
/*     */         case '<':
/* 114 */           localState = State.TAG;
/* 115 */           break;
/*     */         case '&':
/* 117 */           localState = State.ENTITY;
/* 118 */           i++;
/* 119 */           break;
/*     */         default:
/* 121 */           i++;
/*     */         }
/* 123 */         break;
/*     */       case 2:
/* 126 */         if (!Character.isLetterOrDigit(c))
/* 127 */           localState = State.TEXT; break;
/*     */       case 3:
/* 131 */         switch (c) {
/*     */         case '"':
/* 133 */           localState = State.STRING;
/* 134 */           break;
/*     */         case '>':
/* 136 */           localState = State.TEXT;
/*     */         }
/*     */ 
/* 139 */         break;
/*     */       case 4:
/* 142 */         switch (c) {
/*     */         case '"':
/* 144 */           localState = State.TAG;
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 149 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 157 */     paramWriter.write(this.rawHtmlContent);
/* 158 */     return this.rawHtmlContent.endsWith(DocletConstants.NL);
/*     */   }
/*     */ 
/*     */   private static enum State
/*     */   {
/*  98 */     TEXT, ENTITY, TAG, STRING;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.RawHtml
 * JD-Core Version:    0.6.2
 */