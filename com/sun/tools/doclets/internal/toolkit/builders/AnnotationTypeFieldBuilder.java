/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeFieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AnnotationTypeFieldBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   protected ClassDoc classDoc;
/*     */   protected VisibleMemberMap visibleMemberMap;
/*     */   protected AnnotationTypeFieldWriter writer;
/*     */   protected List<ProgramElementDoc> members;
/*     */   protected int currentMemberIndex;
/*     */ 
/*     */   protected AnnotationTypeFieldBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, AnnotationTypeFieldWriter paramAnnotationTypeFieldWriter, int paramInt)
/*     */   {
/*  85 */     super(paramContext);
/*  86 */     this.classDoc = paramClassDoc;
/*  87 */     this.writer = paramAnnotationTypeFieldWriter;
/*  88 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, paramInt, this.configuration);
/*     */ 
/*  90 */     this.members = new ArrayList(this.visibleMemberMap
/*  91 */       .getMembersFor(paramClassDoc));
/*     */ 
/*  92 */     if (this.configuration.getMemberComparator() != null)
/*  93 */       Collections.sort(this.members, this.configuration.getMemberComparator());
/*     */   }
/*     */ 
/*     */   public static AnnotationTypeFieldBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, AnnotationTypeFieldWriter paramAnnotationTypeFieldWriter)
/*     */   {
/* 108 */     return new AnnotationTypeFieldBuilder(paramContext, paramClassDoc, paramAnnotationTypeFieldWriter, 5);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 116 */     return "AnnotationTypeFieldDetails";
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> members(ClassDoc paramClassDoc)
/*     */   {
/* 128 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */ 
/*     */   public VisibleMemberMap getVisibleMemberMap()
/*     */   {
/* 137 */     return this.visibleMemberMap;
/*     */   }
/*     */ 
/*     */   public boolean hasMembersToDocument()
/*     */   {
/* 144 */     return this.members.size() > 0;
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeField(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 154 */     buildAnnotationTypeMember(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildAnnotationTypeMember(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 164 */     if (this.writer == null) {
/* 165 */       return;
/*     */     }
/* 167 */     int i = this.members.size();
/* 168 */     if (i > 0) {
/* 169 */       this.writer.addAnnotationFieldDetailsMarker(paramContent);
/* 170 */       for (this.currentMemberIndex = 0; this.currentMemberIndex < i; 
/* 171 */         this.currentMemberIndex += 1) {
/* 172 */         Content localContent1 = this.writer.getMemberTreeHeader();
/* 173 */         this.writer.addAnnotationDetailsTreeHeader(this.classDoc, localContent1);
/* 174 */         Content localContent2 = this.writer.getAnnotationDocTreeHeader(
/* 175 */           (MemberDoc)this.members
/* 175 */           .get(this.currentMemberIndex), 
/* 175 */           localContent1);
/*     */ 
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
/*     */   public AnnotationTypeFieldWriter getWriter()
/*     */   {
/* 238 */     return this.writer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.AnnotationTypeFieldBuilder
 * JD-Core Version:    0.6.2
 */