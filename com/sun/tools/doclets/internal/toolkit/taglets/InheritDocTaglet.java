/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ 
/*     */ public class InheritDocTaglet extends BaseInlineTaglet
/*     */ {
/*     */   public static final String INHERIT_DOC_INLINE_TAG = "{@inheritDoc}";
/*     */ 
/*     */   public InheritDocTaglet()
/*     */   {
/*  59 */     this.name = "inheritDoc";
/*     */   }
/*     */ 
/*     */   public boolean inField()
/*     */   {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inConstructor()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inOverview()
/*     */   {
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inPackage()
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inType()
/*     */   {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   private Content retrieveInheritedDocumentation(TagletWriter paramTagletWriter, ProgramElementDoc paramProgramElementDoc, Tag paramTag, boolean paramBoolean)
/*     */   {
/* 120 */     Content localContent = paramTagletWriter.getOutputInstance();
/*     */ 
/* 122 */     Configuration localConfiguration = paramTagletWriter.configuration();
/*     */ 
/* 124 */     Taglet localTaglet = paramTag == null ? null : localConfiguration.tagletManager
/* 124 */       .getTaglet(paramTag
/* 124 */       .name());
/* 125 */     if ((localTaglet != null) && (!(localTaglet instanceof InheritableTaglet)))
/*     */     {
/* 129 */       localObject = paramProgramElementDoc.name() + ((paramProgramElementDoc instanceof ExecutableMemberDoc) ? ((ExecutableMemberDoc)paramProgramElementDoc)
/* 129 */         .flatSignature() : "");
/*     */ 
/* 132 */       localConfiguration.message.warning(paramProgramElementDoc.position(), "doclet.noInheritedDoc", new Object[] { localObject });
/*     */     }
/*     */ 
/* 136 */     Object localObject = DocFinder.search(new DocFinder.Input(paramProgramElementDoc, (InheritableTaglet)localTaglet, paramTag, paramBoolean, true));
/*     */ 
/* 139 */     if (((DocFinder.Output)localObject).isValidInheritDocTag) {
/* 140 */       if (((DocFinder.Output)localObject).inlineTags.length > 0) {
/* 141 */         localContent = paramTagletWriter.commentTagsToOutput(((DocFinder.Output)localObject).holderTag, ((DocFinder.Output)localObject).holder, ((DocFinder.Output)localObject).inlineTags, paramBoolean);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 147 */       String str = paramProgramElementDoc.name() + ((paramProgramElementDoc instanceof ExecutableMemberDoc) ? ((ExecutableMemberDoc)paramProgramElementDoc)
/* 147 */         .flatSignature() : "");
/*     */ 
/* 149 */       localConfiguration.message.warning(paramProgramElementDoc.position(), "doclet.noInheritedDoc", new Object[] { str });
/*     */     }
/*     */ 
/* 152 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*     */   {
/* 164 */     if (!(paramTag.holder() instanceof ProgramElementDoc)) {
/* 165 */       return paramTagletWriter.getOutputInstance();
/*     */     }
/*     */ 
/* 169 */     return paramTag.name().equals("@inheritDoc") ? 
/* 168 */       retrieveInheritedDocumentation(paramTagletWriter, 
/* 168 */       (ProgramElementDoc)paramTag
/* 168 */       .holder(), null, paramTagletWriter.isFirstSentence) : 
/* 169 */       retrieveInheritedDocumentation(paramTagletWriter, 
/* 169 */       (ProgramElementDoc)paramTag
/* 169 */       .holder(), paramTag, paramTagletWriter.isFirstSentence);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.InheritDocTaglet
 * JD-Core Version:    0.6.2
 */