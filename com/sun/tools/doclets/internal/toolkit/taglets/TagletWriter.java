/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ 
/*     */ public abstract class TagletWriter
/*     */ {
/*     */   protected final boolean isFirstSentence;
/*     */ 
/*     */   protected TagletWriter(boolean paramBoolean)
/*     */   {
/*  52 */     this.isFirstSentence = paramBoolean;
/*     */   }
/*     */ 
/*     */   public abstract Content getOutputInstance();
/*     */ 
/*     */   protected abstract Content codeTagOutput(Tag paramTag);
/*     */ 
/*     */   protected abstract Content getDocRootOutput();
/*     */ 
/*     */   protected abstract Content deprecatedTagOutput(Doc paramDoc);
/*     */ 
/*     */   protected abstract Content literalTagOutput(Tag paramTag);
/*     */ 
/*     */   protected abstract MessageRetriever getMsgRetriever();
/*     */ 
/*     */   protected abstract Content getParamHeader(String paramString);
/*     */ 
/*     */   protected abstract Content paramTagOutput(ParamTag paramParamTag, String paramString);
/*     */ 
/*     */   protected abstract Content propertyTagOutput(Tag paramTag, String paramString);
/*     */ 
/*     */   protected abstract Content returnTagOutput(Tag paramTag);
/*     */ 
/*     */   protected abstract Content seeTagOutput(Doc paramDoc, SeeTag[] paramArrayOfSeeTag);
/*     */ 
/*     */   protected abstract Content simpleTagOutput(Tag[] paramArrayOfTag, String paramString);
/*     */ 
/*     */   protected abstract Content simpleTagOutput(Tag paramTag, String paramString);
/*     */ 
/*     */   protected abstract Content getThrowsHeader();
/*     */ 
/*     */   protected abstract Content throwsTagOutput(ThrowsTag paramThrowsTag);
/*     */ 
/*     */   protected abstract Content throwsTagOutput(Type paramType);
/*     */ 
/*     */   protected abstract Content valueTagOutput(FieldDoc paramFieldDoc, String paramString, boolean paramBoolean);
/*     */ 
/*     */   public static void genTagOuput(TagletManager paramTagletManager, Doc paramDoc, Taglet[] paramArrayOfTaglet, TagletWriter paramTagletWriter, Content paramContent)
/*     */   {
/* 204 */     paramTagletManager.checkTags(paramDoc, paramDoc.tags(), false);
/* 205 */     paramTagletManager.checkTags(paramDoc, paramDoc.inlineTags(), true);
/* 206 */     Content localContent = null;
/* 207 */     for (int i = 0; i < paramArrayOfTaglet.length; i++) {
/* 208 */       localContent = null;
/* 209 */       if ((!(paramDoc instanceof ClassDoc)) || (!(paramArrayOfTaglet[i] instanceof ParamTaglet)))
/*     */       {
/* 214 */         if (!(paramArrayOfTaglet[i] instanceof DeprecatedTaglet))
/*     */         {
/*     */           try
/*     */           {
/* 220 */             localContent = paramArrayOfTaglet[i].getTagletOutput(paramDoc, paramTagletWriter);
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException)
/*     */           {
/* 224 */             Tag[] arrayOfTag = paramDoc.tags(paramArrayOfTaglet[i].getName());
/* 225 */             if (arrayOfTag.length > 0) {
/* 226 */               localContent = paramArrayOfTaglet[i].getTagletOutput(arrayOfTag[0], paramTagletWriter);
/*     */             }
/*     */           }
/* 229 */           if (localContent != null) {
/* 230 */             paramTagletManager.seenCustomTag(paramArrayOfTaglet[i].getName());
/* 231 */             paramContent.addContent(localContent);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Content getInlineTagOuput(TagletManager paramTagletManager, Tag paramTag1, Tag paramTag2, TagletWriter paramTagletWriter)
/*     */   {
/* 247 */     Taglet[] arrayOfTaglet = paramTagletManager.getInlineCustomTaglets();
/*     */ 
/* 249 */     for (int i = 0; i < arrayOfTaglet.length; i++) {
/* 250 */       if (("@" + arrayOfTaglet[i].getName()).equals(paramTag2.name()))
/*     */       {
/* 253 */         paramTagletManager.seenCustomTag(arrayOfTaglet[i].getName());
/* 254 */         if (paramTag1 != null);
/* 254 */         Content localContent = arrayOfTaglet[i].getTagletOutput(arrayOfTaglet[i]
/* 256 */           .getName().equals("inheritDoc") ? paramTag1 : paramTag2, paramTagletWriter);
/*     */ 
/* 258 */         return localContent;
/*     */       }
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract Content commentTagsToOutput(Tag paramTag, Tag[] paramArrayOfTag);
/*     */ 
/*     */   public abstract Content commentTagsToOutput(Doc paramDoc, Tag[] paramArrayOfTag);
/*     */ 
/*     */   public abstract Content commentTagsToOutput(Tag paramTag, Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean);
/*     */ 
/*     */   public abstract Configuration configuration();
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter
 * JD-Core Version:    0.6.2
 */