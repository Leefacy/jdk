/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ public abstract class BasePropertyTaglet extends BaseTaglet
/*     */ {
/*     */   abstract String getText(TagletWriter paramTagletWriter);
/*     */ 
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*     */   {
/*  64 */     return paramTagletWriter.propertyTagOutput(paramTag, getText(paramTagletWriter));
/*     */   }
/*     */ 
/*     */   public boolean inConstructor()
/*     */   {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inOverview()
/*     */   {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inPackage()
/*     */   {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inType()
/*     */   {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isInlineTag()
/*     */   {
/* 108 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.BasePropertyTaglet
 * JD-Core Version:    0.6.2
 */