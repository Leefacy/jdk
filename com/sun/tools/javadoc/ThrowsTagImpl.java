/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ 
/*     */ class ThrowsTagImpl extends TagImpl
/*     */   implements ThrowsTag
/*     */ {
/*     */   private final String exceptionName;
/*     */   private final String exceptionComment;
/*     */   private Tag[] inlineTags;
/*     */ 
/*     */   ThrowsTagImpl(DocImpl paramDocImpl, String paramString1, String paramString2)
/*     */   {
/*  57 */     super(paramDocImpl, paramString1, paramString2);
/*  58 */     String[] arrayOfString = divideAtWhite();
/*  59 */     this.exceptionName = arrayOfString[0];
/*  60 */     this.exceptionComment = arrayOfString[1];
/*     */   }
/*     */ 
/*     */   public String exceptionName()
/*     */   {
/*  67 */     return this.exceptionName;
/*     */   }
/*     */ 
/*     */   public String exceptionComment()
/*     */   {
/*  74 */     return this.exceptionComment;
/*     */   }
/*     */ 
/*     */   public ClassDoc exception()
/*     */   {
/*     */     ClassDocImpl localClassDocImpl1;
/*  82 */     if (!(this.holder instanceof ExecutableMemberDoc)) {
/*  83 */       localClassDocImpl1 = null;
/*     */     } else {
/*  85 */       ExecutableMemberDocImpl localExecutableMemberDocImpl = (ExecutableMemberDocImpl)this.holder;
/*  86 */       ClassDocImpl localClassDocImpl2 = (ClassDocImpl)localExecutableMemberDocImpl.containingClass();
/*  87 */       localClassDocImpl1 = (ClassDocImpl)localClassDocImpl2.findClass(this.exceptionName);
/*     */     }
/*  89 */     return localClassDocImpl1;
/*     */   }
/*     */ 
/*     */   public Type exceptionType()
/*     */   {
/*  98 */     return exception();
/*     */   }
/*     */ 
/*     */   public String kind()
/*     */   {
/* 108 */     return "@throws";
/*     */   }
/*     */ 
/*     */   public Tag[] inlineTags()
/*     */   {
/* 121 */     if (this.inlineTags == null) {
/* 122 */       this.inlineTags = Comment.getInlineTags(this.holder, exceptionComment());
/*     */     }
/* 124 */     return this.inlineTags;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ThrowsTagImpl
 * JD-Core Version:    0.6.2
 */