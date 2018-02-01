/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.PropertyWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PropertyBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final PropertyWriter writer;
/*     */   private final List<ProgramElementDoc> properties;
/*     */   private int currentPropertyIndex;
/*     */ 
/*     */   private PropertyBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, PropertyWriter paramPropertyWriter)
/*     */   {
/*  84 */     super(paramContext);
/*  85 */     this.classDoc = paramClassDoc;
/*  86 */     this.writer = paramPropertyWriter;
/*  87 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 8, this.configuration);
/*     */ 
/*  92 */     this.properties = new ArrayList(this.visibleMemberMap
/*  93 */       .getMembersFor(paramClassDoc));
/*     */ 
/*  94 */     if (this.configuration.getMemberComparator() != null)
/*  95 */       Collections.sort(this.properties, this.configuration.getMemberComparator());
/*     */   }
/*     */ 
/*     */   public static PropertyBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, PropertyWriter paramPropertyWriter)
/*     */   {
/* 109 */     return new PropertyBuilder(paramContext, paramClassDoc, paramPropertyWriter);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 116 */     return "PropertyDetails";
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
/* 144 */     return this.properties.size() > 0;
/*     */   }
/*     */ 
/*     */   public void buildPropertyDoc(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 154 */     if (this.writer == null) {
/* 155 */       return;
/*     */     }
/* 157 */     int i = this.properties.size();
/* 158 */     if (i > 0) {
/* 159 */       Content localContent1 = this.writer.getPropertyDetailsTreeHeader(this.classDoc, paramContent);
/*     */ 
/* 161 */       for (this.currentPropertyIndex = 0; this.currentPropertyIndex < i; 
/* 162 */         this.currentPropertyIndex += 1) {
/* 163 */         Content localContent2 = this.writer.getPropertyDocTreeHeader(
/* 164 */           (MethodDoc)this.properties
/* 164 */           .get(this.currentPropertyIndex), 
/* 164 */           localContent1);
/*     */ 
/* 166 */         buildChildren(paramXMLNode, localContent2);
/* 167 */         localContent1.addContent(this.writer.getPropertyDoc(localContent2, this.currentPropertyIndex == i - 1));
/*     */       }
/*     */ 
/* 170 */       paramContent.addContent(this.writer
/* 171 */         .getPropertyDetails(localContent1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 182 */     paramContent.addContent(this.writer
/* 183 */       .getSignature((MethodDoc)this.properties
/* 183 */       .get(this.currentPropertyIndex)));
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 193 */     this.writer.addDeprecated(
/* 194 */       (MethodDoc)this.properties
/* 194 */       .get(this.currentPropertyIndex), 
/* 194 */       paramContent);
/*     */   }
/*     */ 
/*     */   public void buildPropertyComments(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 205 */     if (!this.configuration.nocomment)
/* 206 */       this.writer.addComments((MethodDoc)this.properties.get(this.currentPropertyIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 217 */     this.writer.addTags((MethodDoc)this.properties.get(this.currentPropertyIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public PropertyWriter getWriter()
/*     */   {
/* 226 */     return this.writer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.PropertyBuilder
 * JD-Core Version:    0.6.2
 */