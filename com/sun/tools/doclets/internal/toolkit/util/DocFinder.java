/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.InheritableTaglet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DocFinder
/*     */ {
/*     */   public static Output search(Input paramInput)
/*     */   {
/* 188 */     Output localOutput = new Output();
/* 189 */     if (!paramInput.isInheritDocTag)
/*     */     {
/* 192 */       if (paramInput.taglet == null)
/*     */       {
/* 194 */         localOutput.inlineTags = (paramInput.isFirstSentence ? paramInput.element
/* 195 */           .firstSentenceTags() : paramInput.element
/* 196 */           .inlineTags());
/* 197 */         localOutput.holder = paramInput.element;
/*     */       } else {
/* 199 */         paramInput.taglet.inherit(paramInput, localOutput);
/*     */       }
/*     */     }
/* 202 */     if ((localOutput.inlineTags != null) && (localOutput.inlineTags.length > 0)) {
/* 203 */       return localOutput;
/*     */     }
/* 205 */     localOutput.isValidInheritDocTag = false;
/* 206 */     Input localInput = paramInput.copy();
/* 207 */     localInput.isInheritDocTag = false;
/*     */     Object localObject;
/* 208 */     if ((paramInput.element instanceof MethodDoc)) {
/* 209 */       localObject = ((MethodDoc)paramInput.element).overriddenMethod();
/* 210 */       if (localObject != null) {
/* 211 */         localInput.element = ((ProgramElementDoc)localObject);
/* 212 */         localOutput = search(localInput);
/* 213 */         localOutput.isValidInheritDocTag = true;
/* 214 */         if (localOutput.inlineTags.length > 0) {
/* 215 */           return localOutput;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 222 */       MethodDoc[] arrayOfMethodDoc = new ImplementedMethods((MethodDoc)paramInput.element, null)
/* 222 */         .build(false);
/*     */ 
/* 223 */       for (int i = 0; i < arrayOfMethodDoc.length; i++) {
/* 224 */         localInput.element = arrayOfMethodDoc[i];
/* 225 */         localOutput = search(localInput);
/* 226 */         localOutput.isValidInheritDocTag = true;
/* 227 */         if (localOutput.inlineTags.length > 0)
/* 228 */           return localOutput;
/*     */       }
/*     */     }
/* 231 */     else if ((paramInput.element instanceof ClassDoc)) {
/* 232 */       localObject = ((ClassDoc)paramInput.element).superclass();
/* 233 */       if (localObject != null) {
/* 234 */         localInput.element = ((ProgramElementDoc)localObject);
/* 235 */         localOutput = search(localInput);
/* 236 */         localOutput.isValidInheritDocTag = true;
/* 237 */         if (localOutput.inlineTags.length > 0) {
/* 238 */           return localOutput;
/*     */         }
/*     */       }
/*     */     }
/* 242 */     return localOutput;
/*     */   }
/*     */ 
/*     */   public static class Input
/*     */   {
/*     */     public ProgramElementDoc element;
/*  58 */     public InheritableTaglet taglet = null;
/*     */ 
/*  63 */     public String tagId = null;
/*     */ 
/*  69 */     public Tag tag = null;
/*     */ 
/*  74 */     public boolean isFirstSentence = false;
/*     */ 
/*  79 */     public boolean isInheritDocTag = false;
/*     */ 
/*  85 */     public boolean isTypeVariableParamTag = false;
/*     */ 
/*     */     public Input(ProgramElementDoc paramProgramElementDoc, InheritableTaglet paramInheritableTaglet, Tag paramTag, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/*  89 */       this(paramProgramElementDoc);
/*  90 */       this.taglet = paramInheritableTaglet;
/*  91 */       this.tag = paramTag;
/*  92 */       this.isFirstSentence = paramBoolean1;
/*  93 */       this.isInheritDocTag = paramBoolean2;
/*     */     }
/*     */ 
/*     */     public Input(ProgramElementDoc paramProgramElementDoc, InheritableTaglet paramInheritableTaglet, String paramString) {
/*  97 */       this(paramProgramElementDoc);
/*  98 */       this.taglet = paramInheritableTaglet;
/*  99 */       this.tagId = paramString;
/*     */     }
/*     */ 
/*     */     public Input(ProgramElementDoc paramProgramElementDoc, InheritableTaglet paramInheritableTaglet, String paramString, boolean paramBoolean)
/*     */     {
/* 104 */       this(paramProgramElementDoc);
/* 105 */       this.taglet = paramInheritableTaglet;
/* 106 */       this.tagId = paramString;
/* 107 */       this.isTypeVariableParamTag = paramBoolean;
/*     */     }
/*     */ 
/*     */     public Input(ProgramElementDoc paramProgramElementDoc, InheritableTaglet paramInheritableTaglet) {
/* 111 */       this(paramProgramElementDoc);
/* 112 */       this.taglet = paramInheritableTaglet;
/*     */     }
/*     */ 
/*     */     public Input(ProgramElementDoc paramProgramElementDoc) {
/* 116 */       if (paramProgramElementDoc == null)
/* 117 */         throw new NullPointerException();
/* 118 */       this.element = paramProgramElementDoc;
/*     */     }
/*     */ 
/*     */     public Input(ProgramElementDoc paramProgramElementDoc, boolean paramBoolean) {
/* 122 */       this(paramProgramElementDoc);
/* 123 */       this.isFirstSentence = paramBoolean;
/*     */     }
/*     */ 
/*     */     public Input copy() {
/* 127 */       Input localInput = new Input(this.element);
/* 128 */       localInput.taglet = this.taglet;
/* 129 */       localInput.tagId = this.tagId;
/* 130 */       localInput.tag = this.tag;
/* 131 */       localInput.isFirstSentence = this.isFirstSentence;
/* 132 */       localInput.isInheritDocTag = this.isInheritDocTag;
/* 133 */       localInput.isTypeVariableParamTag = this.isTypeVariableParamTag;
/* 134 */       if (localInput.element == null)
/* 135 */         throw new NullPointerException();
/* 136 */       return localInput;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Output
/*     */   {
/*     */     public Tag holderTag;
/*     */     public Doc holder;
/* 159 */     public Tag[] inlineTags = new Tag[0];
/*     */ 
/* 164 */     public boolean isValidInheritDocTag = true;
/*     */ 
/* 174 */     public List<Tag> tagList = new ArrayList();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.DocFinder
 * JD-Core Version:    0.6.2
 */