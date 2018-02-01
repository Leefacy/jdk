/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AnnotationTypeOptionalMemberBuilder extends AnnotationTypeRequiredMemberBuilder
/*     */ {
/*     */   private AnnotationTypeOptionalMemberBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, AnnotationTypeOptionalMemberWriter paramAnnotationTypeOptionalMemberWriter)
/*     */   {
/*  58 */     super(paramContext, paramClassDoc, paramAnnotationTypeOptionalMemberWriter, 6);
/*     */   }
/*     */ 
/*     */   public static AnnotationTypeOptionalMemberBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, AnnotationTypeOptionalMemberWriter paramAnnotationTypeOptionalMemberWriter)
/*     */   {
/*  73 */     return new AnnotationTypeOptionalMemberBuilder(paramContext, paramClassDoc, paramAnnotationTypeOptionalMemberWriter);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  82 */     return "AnnotationTypeOptionalMemberDetails";
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeOptionalMember(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/*  92 */     buildAnnotationTypeMember(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildDefaultValueInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 102 */     ((AnnotationTypeOptionalMemberWriter)this.writer).addDefaultValueInfo(
/* 103 */       (MemberDoc)this.members
/* 103 */       .get(this.currentMemberIndex), 
/* 103 */       paramContent);
/*     */   }
/*     */ 
/*     */   public AnnotationTypeRequiredMemberWriter getWriter()
/*     */   {
/* 112 */     return this.writer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.AnnotationTypeOptionalMemberBuilder
 * JD-Core Version:    0.6.2
 */