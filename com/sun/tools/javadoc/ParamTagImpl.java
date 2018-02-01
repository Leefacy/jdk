/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ class ParamTagImpl extends TagImpl
/*     */   implements ParamTag
/*     */ {
/*  46 */   private static final Pattern typeParamRE = Pattern.compile("<([^<>]+)>");
/*     */   private final String parameterName;
/*     */   private final String parameterComment;
/*     */   private final boolean isTypeParameter;
/*     */   private Tag[] inlineTags;
/*     */ 
/*     */   ParamTagImpl(DocImpl paramDocImpl, String paramString1, String paramString2)
/*     */   {
/*  58 */     super(paramDocImpl, paramString1, paramString2);
/*  59 */     String[] arrayOfString = divideAtWhite();
/*     */ 
/*  61 */     Matcher localMatcher = typeParamRE.matcher(arrayOfString[0]);
/*  62 */     this.isTypeParameter = localMatcher.matches();
/*  63 */     this.parameterName = (this.isTypeParameter ? localMatcher.group(1) : arrayOfString[0]);
/*  64 */     this.parameterComment = arrayOfString[1];
/*     */   }
/*     */ 
/*     */   public String parameterName()
/*     */   {
/*  71 */     return this.parameterName;
/*     */   }
/*     */ 
/*     */   public String parameterComment()
/*     */   {
/*  78 */     return this.parameterComment;
/*     */   }
/*     */ 
/*     */   public String kind()
/*     */   {
/*  86 */     return "@param";
/*     */   }
/*     */ 
/*     */   public boolean isTypeParameter()
/*     */   {
/*  93 */     return this.isTypeParameter;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return this.name + ":" + this.text;
/*     */   }
/*     */ 
/*     */   public Tag[] inlineTags()
/*     */   {
/* 114 */     if (this.inlineTags == null) {
/* 115 */       this.inlineTags = Comment.getInlineTags(this.holder, this.parameterComment);
/*     */     }
/* 117 */     return this.inlineTags;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ParamTagImpl
 * JD-Core Version:    0.6.2
 */