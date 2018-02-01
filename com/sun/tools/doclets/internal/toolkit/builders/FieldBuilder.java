/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.FieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class FieldBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final FieldWriter writer;
/*     */   private final List<ProgramElementDoc> fields;
/*     */   private int currentFieldIndex;
/*     */ 
/*     */   private FieldBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, FieldWriter paramFieldWriter)
/*     */   {
/*  84 */     super(paramContext);
/*  85 */     this.classDoc = paramClassDoc;
/*  86 */     this.writer = paramFieldWriter;
/*  87 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 2, this.configuration);
/*     */ 
/*  92 */     this.fields = new ArrayList(this.visibleMemberMap
/*  93 */       .getLeafClassMembers(this.configuration));
/*     */ 
/*  95 */     if (this.configuration.getMemberComparator() != null)
/*  96 */       Collections.sort(this.fields, this.configuration.getMemberComparator());
/*     */   }
/*     */ 
/*     */   public static FieldBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, FieldWriter paramFieldWriter)
/*     */   {
/* 110 */     return new FieldBuilder(paramContext, paramClassDoc, paramFieldWriter);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 117 */     return "FieldDetails";
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
/* 145 */     return this.fields.size() > 0;
/*     */   }
/*     */ 
/*     */   public void buildFieldDoc(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 155 */     if (this.writer == null) {
/* 156 */       return;
/*     */     }
/* 158 */     int i = this.fields.size();
/* 159 */     if (i > 0) {
/* 160 */       Content localContent1 = this.writer.getFieldDetailsTreeHeader(this.classDoc, paramContent);
/*     */ 
/* 162 */       for (this.currentFieldIndex = 0; this.currentFieldIndex < i; 
/* 163 */         this.currentFieldIndex += 1) {
/* 164 */         Content localContent2 = this.writer.getFieldDocTreeHeader(
/* 165 */           (FieldDoc)this.fields
/* 165 */           .get(this.currentFieldIndex), 
/* 165 */           localContent1);
/*     */ 
/* 167 */         buildChildren(paramXMLNode, localContent2);
/* 168 */         localContent1.addContent(this.writer.getFieldDoc(localContent2, this.currentFieldIndex == i - 1));
/*     */       }
/*     */ 
/* 171 */       paramContent.addContent(this.writer
/* 172 */         .getFieldDetails(localContent1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 183 */     paramContent.addContent(this.writer
/* 184 */       .getSignature((FieldDoc)this.fields
/* 184 */       .get(this.currentFieldIndex)));
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 194 */     this.writer.addDeprecated(
/* 195 */       (FieldDoc)this.fields
/* 195 */       .get(this.currentFieldIndex), 
/* 195 */       paramContent);
/*     */   }
/*     */ 
/*     */   public void buildFieldComments(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 206 */     if (!this.configuration.nocomment)
/* 207 */       this.writer.addComments((FieldDoc)this.fields.get(this.currentFieldIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 218 */     this.writer.addTags((FieldDoc)this.fields.get(this.currentFieldIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public FieldWriter getWriter()
/*     */   {
/* 227 */     return this.writer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.FieldBuilder
 * JD-Core Version:    0.6.2
 */