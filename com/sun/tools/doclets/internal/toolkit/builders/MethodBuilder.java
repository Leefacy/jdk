/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class MethodBuilder extends AbstractMemberBuilder
/*     */ {
/*     */   private int currentMethodIndex;
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final MethodWriter writer;
/*     */   private List<ProgramElementDoc> methods;
/*     */ 
/*     */   private MethodBuilder(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, MethodWriter paramMethodWriter)
/*     */   {
/*  85 */     super(paramContext);
/*  86 */     this.classDoc = paramClassDoc;
/*  87 */     this.writer = paramMethodWriter;
/*  88 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 4, this.configuration);
/*     */ 
/*  92 */     this.methods = new ArrayList(this.visibleMemberMap
/*  93 */       .getLeafClassMembers(this.configuration));
/*     */ 
/*  95 */     if (this.configuration.getMemberComparator() != null)
/*  96 */       Collections.sort(this.methods, this.configuration.getMemberComparator());
/*     */   }
/*     */ 
/*     */   public static MethodBuilder getInstance(AbstractBuilder.Context paramContext, ClassDoc paramClassDoc, MethodWriter paramMethodWriter)
/*     */   {
/* 111 */     return new MethodBuilder(paramContext, paramClassDoc, paramMethodWriter);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 118 */     return "MethodDetails";
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> members(ClassDoc paramClassDoc)
/*     */   {
/* 130 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */ 
/*     */   public VisibleMemberMap getVisibleMemberMap()
/*     */   {
/* 139 */     return this.visibleMemberMap;
/*     */   }
/*     */ 
/*     */   public boolean hasMembersToDocument()
/*     */   {
/* 146 */     return this.methods.size() > 0;
/*     */   }
/*     */ 
/*     */   public void buildMethodDoc(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 156 */     if (this.writer == null) {
/* 157 */       return;
/*     */     }
/* 159 */     int i = this.methods.size();
/* 160 */     if (i > 0) {
/* 161 */       Content localContent1 = this.writer.getMethodDetailsTreeHeader(this.classDoc, paramContent);
/*     */ 
/* 163 */       for (this.currentMethodIndex = 0; this.currentMethodIndex < i; 
/* 164 */         this.currentMethodIndex += 1) {
/* 165 */         Content localContent2 = this.writer.getMethodDocTreeHeader(
/* 166 */           (MethodDoc)this.methods
/* 166 */           .get(this.currentMethodIndex), 
/* 166 */           localContent1);
/*     */ 
/* 168 */         buildChildren(paramXMLNode, localContent2);
/* 169 */         localContent1.addContent(this.writer.getMethodDoc(localContent2, this.currentMethodIndex == i - 1));
/*     */       }
/*     */ 
/* 172 */       paramContent.addContent(this.writer
/* 173 */         .getMethodDetails(localContent1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 184 */     paramContent.addContent(this.writer
/* 185 */       .getSignature((MethodDoc)this.methods
/* 185 */       .get(this.currentMethodIndex)));
/*     */   }
/*     */ 
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 195 */     this.writer.addDeprecated(
/* 196 */       (MethodDoc)this.methods
/* 196 */       .get(this.currentMethodIndex), 
/* 196 */       paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMethodComments(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 207 */     if (!this.configuration.nocomment) {
/* 208 */       MethodDoc localMethodDoc = (MethodDoc)this.methods.get(this.currentMethodIndex);
/*     */ 
/* 210 */       if (localMethodDoc.inlineTags().length == 0) {
/* 211 */         DocFinder.Output localOutput = DocFinder.search(new DocFinder.Input(localMethodDoc));
/*     */ 
/* 213 */         localMethodDoc = (localOutput.inlineTags != null) && (localOutput.inlineTags.length > 0) ? (MethodDoc)localOutput.holder : localMethodDoc;
/*     */       }
/*     */ 
/* 219 */       this.writer.addComments(localMethodDoc.containingClass(), localMethodDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 230 */     this.writer.addTags((MethodDoc)this.methods.get(this.currentMethodIndex), paramContent);
/*     */   }
/*     */ 
/*     */   public MethodWriter getWriter()
/*     */   {
/* 240 */     return this.writer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.MethodBuilder
 * JD-Core Version:    0.6.2
 */