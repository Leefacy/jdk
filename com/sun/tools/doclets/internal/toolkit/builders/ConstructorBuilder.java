/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstructorWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ConstructorBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   public static final String NAME = "ConstructorDetails";
/*     */   private int currentConstructorIndex;
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final ConstructorWriter writer;
/*     */   private final List<ProgramElementDoc> constructors;
/*     */ 
/*     */   private ConstructorBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, ConstructorWriter paramConstructorWriter)
/*     */   {
/*  89 */     super(paramContext);
/*  90 */     this.classDoc = paramClassDoc;
/*  91 */     this.writer = paramConstructorWriter;
/*  92 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 3, this.configuration);
/*     */ 
/*  97 */     this.constructors = new ArrayList(this.visibleMemberMap
/*  98 */       .getMembersFor(paramClassDoc));
/*     */ 
/*  99 */     for (int i = 0; i < this.constructors.size(); i++) {
/* 100 */       if ((((ProgramElementDoc)this.constructors.get(i)).isProtected()) || 
/* 101 */         (((ProgramElementDoc)this.constructors
/* 101 */         .get(i))
/* 101 */         .isPrivate())) {
/* 102 */         paramConstructorWriter.setFoundNonPubConstructor(true);
/*     */       }
/*     */     }
/* 105 */     if (this.configuration.getMemberComparator() != null)
/* 106 */       Collections.sort(this.constructors, this.configuration.getMemberComparator());
/*     */   }
/*     */ 
/*     */   public static ConstructorBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, ConstructorWriter paramConstructorWriter)
/*     */   {
/* 119 */     return new ConstructorBuilder(paramContext, paramClassDoc, paramConstructorWriter);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 126 */     return "ConstructorDetails";
/*     */   }
/*     */ 
/*     */   public boolean hasMembersToDocument()
/*     */   {
/* 133 */     return this.constructors.size() > 0;
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> members(ClassDoc paramClassDoc)
/*     */   {
/* 144 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */ 
/*     */   public ConstructorWriter getWriter()
/*     */   {
/* 153 */     return this.writer;
/*     */   }
/*     */ 
/*     */   public void buildConstructorDoc(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 163 */     if (this.writer == null) {
/* 164 */       return;
/*     */     }
/* 166 */     int i = this.constructors.size();
/* 167 */     if (i > 0) {
/* 168 */       Content localContent1 = this.writer.getConstructorDetailsTreeHeader(this.classDoc, paramContent);
/*     */ 
/* 170 */       for (this.currentConstructorIndex = 0; this.currentConstructorIndex < i; 
/* 171 */         this.currentConstructorIndex += 1) {
/* 172 */         Content localContent2 = this.writer.getConstructorDocTreeHeader(
/* 173 */           (ConstructorDoc)this.constructors
/* 173 */           .get(this.currentConstructorIndex), 
/* 173 */           localContent1);
/*     */ 
/* 175 */         buildChildren(paramXMLNode, localContent2);
/* 176 */         localContent1.addContent(this.writer.getConstructorDoc(localContent2, this.currentConstructorIndex == i - 1));
/*     */       }
/*     */ 
/* 179 */       paramContent.addContent(this.writer
/* 180 */         .getConstructorDetails(localContent1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 191 */     paramContent.addContent(this.writer
/* 192 */       .getSignature(
/* 193 */       (ConstructorDoc)this.constructors
/* 193 */       .get(this.currentConstructorIndex)));
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 203 */     this.writer.addDeprecated(
/* 204 */       (ConstructorDoc)this.constructors
/* 204 */       .get(this.currentConstructorIndex), 
/* 204 */       paramContent);
/*     */   }
/*     */ 
/*     */   public void buildConstructorComments(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 215 */     if (!this.configuration.nocomment)
/* 216 */       this.writer.addComments(
/* 217 */         (ConstructorDoc)this.constructors
/* 217 */         .get(this.currentConstructorIndex), 
/* 217 */         paramContent);
/*     */   }
/*     */ 
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 229 */     this.writer.addTags((ConstructorDoc)this.constructors.get(this.currentConstructorIndex), paramContent);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.ConstructorBuilder
 * JD-Core Version:    0.6.2
 */