/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ public class LegacyTaglet
/*     */   implements Taglet
/*     */ {
/*     */   private com.sun.tools.doclets.Taglet legacyTaglet;
/*     */ 
/*     */   public LegacyTaglet(com.sun.tools.doclets.Taglet paramTaglet)
/*     */   {
/*  55 */     this.legacyTaglet = paramTaglet;
/*     */   }
/*     */ 
/*     */   public boolean inField()
/*     */   {
/*  62 */     return (this.legacyTaglet.isInlineTag()) || (this.legacyTaglet.inField());
/*     */   }
/*     */ 
/*     */   public boolean inConstructor()
/*     */   {
/*  69 */     return (this.legacyTaglet.isInlineTag()) || (this.legacyTaglet.inConstructor());
/*     */   }
/*     */ 
/*     */   public boolean inMethod()
/*     */   {
/*  76 */     return (this.legacyTaglet.isInlineTag()) || (this.legacyTaglet.inMethod());
/*     */   }
/*     */ 
/*     */   public boolean inOverview()
/*     */   {
/*  83 */     return (this.legacyTaglet.isInlineTag()) || (this.legacyTaglet.inOverview());
/*     */   }
/*     */ 
/*     */   public boolean inPackage()
/*     */   {
/*  90 */     return (this.legacyTaglet.isInlineTag()) || (this.legacyTaglet.inPackage());
/*     */   }
/*     */ 
/*     */   public boolean inType()
/*     */   {
/*  97 */     return (this.legacyTaglet.isInlineTag()) || (this.legacyTaglet.inType());
/*     */   }
/*     */ 
/*     */   public boolean isInlineTag()
/*     */   {
/* 107 */     return this.legacyTaglet.isInlineTag();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 114 */     return this.legacyTaglet.getName();
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*     */     throws IllegalArgumentException
/*     */   {
/* 122 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 123 */     localContent.addContent(new RawHtml(this.legacyTaglet.toString(paramTag)));
/* 124 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*     */     throws IllegalArgumentException
/*     */   {
/* 132 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 133 */     Tag[] arrayOfTag = paramDoc.tags(getName());
/* 134 */     if (arrayOfTag.length > 0) {
/* 135 */       String str = this.legacyTaglet.toString(arrayOfTag);
/* 136 */       if (str != null) {
/* 137 */         localContent.addContent(new RawHtml(str));
/*     */       }
/*     */     }
/* 140 */     return localContent;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.LegacyTaglet
 * JD-Core Version:    0.6.2
 */