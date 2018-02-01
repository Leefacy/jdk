/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ThrowsTaglet extends BaseExecutableMemberTaglet
/*     */   implements InheritableTaglet
/*     */ {
/*     */   public ThrowsTaglet()
/*     */   {
/*  49 */     this.name = "throws";
/*     */   }
/*     */ 
/*     */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput)
/*     */   {
/*     */     ClassDoc localClassDoc;
/*  57 */     if (paramInput.tagId == null) {
/*  58 */       localObject = (ThrowsTag)paramInput.tag;
/*  59 */       localClassDoc = ((ThrowsTag)localObject).exception();
/*  60 */       paramInput.tagId = (localClassDoc == null ? ((ThrowsTag)localObject)
/*  61 */         .exceptionName() : ((ThrowsTag)localObject)
/*  62 */         .exception().qualifiedName());
/*     */     } else {
/*  64 */       localClassDoc = paramInput.element.containingClass().findClass(paramInput.tagId);
/*     */     }
/*     */ 
/*  67 */     Object localObject = ((MethodDoc)paramInput.element).throwsTags();
/*  68 */     for (int i = 0; i < localObject.length; i++)
/*  69 */       if ((paramInput.tagId.equals(localObject[i].exceptionName())) || (
/*  70 */         (localObject[i]
/*  70 */         .exception() != null) && 
/*  71 */         (paramInput.tagId
/*  71 */         .equals(localObject[i]
/*  71 */         .exception().qualifiedName())))) {
/*  72 */         paramOutput.holder = paramInput.element;
/*  73 */         paramOutput.holderTag = localObject[i];
/*  74 */         paramOutput.inlineTags = (paramInput.isFirstSentence ? localObject[i]
/*  75 */           .firstSentenceTags() : localObject[i].inlineTags());
/*  76 */         paramOutput.tagList.add(localObject[i]);
/*  77 */       } else if ((localClassDoc != null) && (localObject[i].exception() != null) && 
/*  78 */         (localObject[i]
/*  78 */         .exception().subclassOf(localClassDoc))) {
/*  79 */         paramOutput.tagList.add(localObject[i]);
/*     */       }
/*     */   }
/*     */ 
/*     */   private Content linkToUndocumentedDeclaredExceptions(Type[] paramArrayOfType, Set<String> paramSet, TagletWriter paramTagletWriter)
/*     */   {
/*  90 */     Content localContent = paramTagletWriter.getOutputInstance();
/*     */ 
/*  92 */     for (int i = 0; i < paramArrayOfType.length; i++) {
/*  93 */       if ((paramArrayOfType[i].asClassDoc() != null) && 
/*  94 */         (!paramSet
/*  94 */         .contains(paramArrayOfType[i]
/*  95 */         .asClassDoc().name())))
/*  96 */         if (!paramSet
/*  96 */           .contains(paramArrayOfType[i]
/*  97 */           .asClassDoc().qualifiedName())) {
/*  98 */           if (paramSet.size() == 0) {
/*  99 */             localContent.addContent(paramTagletWriter.getThrowsHeader());
/*     */           }
/* 101 */           localContent.addContent(paramTagletWriter.throwsTagOutput(paramArrayOfType[i]));
/* 102 */           paramSet.add(paramArrayOfType[i].asClassDoc().name());
/*     */         }
/*     */     }
/* 105 */     return localContent;
/*     */   }
/*     */ 
/*     */   private Content inheritThrowsDocumentation(Doc paramDoc, Type[] paramArrayOfType, Set<String> paramSet, TagletWriter paramTagletWriter)
/*     */   {
/* 115 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 116 */     if ((paramDoc instanceof MethodDoc)) {
/* 117 */       LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 118 */       for (int i = 0; i < paramArrayOfType.length; i++)
/*     */       {
/* 120 */         DocFinder.Output localOutput = DocFinder.search(new DocFinder.Input((MethodDoc)paramDoc, this, paramArrayOfType[i]
/* 121 */           .typeName()));
/* 122 */         if (localOutput.tagList.size() == 0) {
/* 123 */           localOutput = DocFinder.search(new DocFinder.Input((MethodDoc)paramDoc, this, paramArrayOfType[i]
/* 125 */             .qualifiedTypeName()));
/*     */         }
/* 127 */         localLinkedHashSet.addAll(localOutput.tagList);
/*     */       }
/* 129 */       localContent.addContent(throwsTagsOutput(
/* 130 */         (ThrowsTag[])localLinkedHashSet
/* 130 */         .toArray(new ThrowsTag[0]), 
/* 130 */         paramTagletWriter, paramSet, false));
/*     */     }
/*     */ 
/* 133 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*     */   {
/* 140 */     ExecutableMemberDoc localExecutableMemberDoc = (ExecutableMemberDoc)paramDoc;
/* 141 */     ThrowsTag[] arrayOfThrowsTag = localExecutableMemberDoc.throwsTags();
/* 142 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 143 */     HashSet localHashSet = new HashSet();
/* 144 */     if (arrayOfThrowsTag.length > 0) {
/* 145 */       localContent.addContent(throwsTagsOutput(localExecutableMemberDoc
/* 146 */         .throwsTags(), paramTagletWriter, localHashSet, true));
/*     */     }
/* 148 */     localContent.addContent(inheritThrowsDocumentation(paramDoc, localExecutableMemberDoc
/* 149 */       .thrownExceptionTypes(), localHashSet, paramTagletWriter));
/* 150 */     localContent.addContent(linkToUndocumentedDeclaredExceptions(localExecutableMemberDoc
/* 151 */       .thrownExceptionTypes(), localHashSet, paramTagletWriter));
/* 152 */     return localContent;
/*     */   }
/*     */ 
/*     */   protected Content throwsTagsOutput(ThrowsTag[] paramArrayOfThrowsTag, TagletWriter paramTagletWriter, Set<String> paramSet, boolean paramBoolean)
/*     */   {
/* 168 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 169 */     if (paramArrayOfThrowsTag.length > 0)
/* 170 */       for (int i = 0; i < paramArrayOfThrowsTag.length; i++) {
/* 171 */         ThrowsTag localThrowsTag = paramArrayOfThrowsTag[i];
/* 172 */         ClassDoc localClassDoc = localThrowsTag.exception();
/* 173 */         if ((paramBoolean) || ((!paramSet.contains(localThrowsTag.exceptionName())) && ((localClassDoc == null) || 
/* 174 */           (!paramSet
/* 174 */           .contains(localClassDoc
/* 174 */           .qualifiedName())))))
/*     */         {
/* 177 */           if (paramSet.size() == 0) {
/* 178 */             localContent.addContent(paramTagletWriter.getThrowsHeader());
/*     */           }
/* 180 */           localContent.addContent(paramTagletWriter.throwsTagOutput(localThrowsTag));
/* 181 */           paramSet.add(localClassDoc != null ? localClassDoc
/* 182 */             .qualifiedName() : localThrowsTag.exceptionName());
/*     */         }
/*     */       }
/* 185 */     return localContent;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.ThrowsTaglet
 * JD-Core Version:    0.6.2
 */