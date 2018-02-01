/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AnnotationTypeRequiredMemberBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   protected ClassDoc classDoc;
/*     */   protected VisibleMemberMap visibleMemberMap;
/*     */   protected AnnotationTypeRequiredMemberWriter writer;
/*     */   protected List<ProgramElementDoc> members;
/*     */   protected int currentMemberIndex;
/*     */ 
/*     */   protected AnnotationTypeRequiredMemberBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, AnnotationTypeRequiredMemberWriter paramAnnotationTypeRequiredMemberWriter, int paramInt)
/*     */   {
/*  85 */     super(paramContext);
/*  86 */     this.classDoc = paramClassDoc;
/*  87 */     this.writer = paramAnnotationTypeRequiredMemberWriter;
/*  88 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, paramInt, this.configuration);
/*     */ 
/*  90 */     this.members = new ArrayList(this.visibleMemberMap
/*  91 */       .getMembersFor(paramClassDoc));
/*     */ 
/*  92 */     if (this.configuration.getMemberComparator() != null)
/*  93 */       Collections.sort(this.members, this.configuration.getMemberComparator());
/*     */   }
/*     */ 
/*     */   public static AnnotationTypeRequiredMemberBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, AnnotationTypeRequiredMemberWriter paramAnnotationTypeRequiredMemberWriter)
/*     */   {
/* 108 */     return new AnnotationTypeRequiredMemberBuilder(paramContext, paramClassDoc, paramAnnotationTypeRequiredMemberWriter, 7);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 117 */     return "AnnotationTypeRequiredMemberDetails";
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> members(ClassDoc paramClassDoc)
/*     */   {
/* 129 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */ 
/*     */   public VisibleMemberMap getVisibleMemberMap()
/*     */   {
/* 138 */     return this.visibleMemberMap;
/*     */   }
/*     */ 
/*     */   public boolean hasMembersToDocument()
/*     */   {
/* 145 */     return this.members.size() > 0;
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeRequiredMember(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 155 */     buildAnnotationTypeMember(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeMember(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 165 */     if (this.writer == null) {
/* 166 */       return;
/*     */     }
/* 168 */     int i = this.members.size();
/* 169 */     if (i > 0) {
/* 170 */       this.writer.addAnnotationDetailsMarker(paramContent);
/* 171 */       for (this.currentMemberIndex = 0; this.currentMemberIndex < i; 
/* 172 */         this.currentMemberIndex += 1) {
/* 173 */         Content localContent1 = this.writer.getMemberTreeHeader();
/* 174 */         this.writer.addAnnotationDetailsTreeHeader(this.classDoc, localContent1);
/* 175 */         Content localContent2 = this.writer.getAnnotationDocTreeHeader(
/* 176 */           (MemberDoc)this.members
/* 176 */           .get(this.currentMemberIndex), 
/* 176 */           localContent1);
/* 177 */         buildChildren(paramXMLNode, localContent2);
/* 178 */         localContent1.addContent(this.writer.getAnnotationDoc(localContent2, this.currentMemberIndex == i - 1));
/*     */ 
/* 180 */         paramContent.addContent(this.writer.getAnnotationDetails(localContent1));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 192 */     paramContent.addContent(this.writer
/* 193 */       .getSignature((MemberDoc)this.members
/* 193 */       .get(this.currentMemberIndex)));
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 203 */     this.writer.addDeprecated((MemberDoc)this.members.get(this.currentMemberIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMemberComments(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 215 */     if (!this.configuration.nocomment)
/* 216 */       this.writer.addComments((MemberDoc)this.members.get(this.currentMemberIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 228 */     this.writer.addTags((MemberDoc)this.members.get(this.currentMemberIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public AnnotationTypeRequiredMemberWriter getWriter()
/*     */   {
/* 239 */     return this.writer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.AnnotationTypeRequiredMemberBuilder
 * JD-Core Version:    0.6.2
 */