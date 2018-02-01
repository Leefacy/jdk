/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class ValueTaglet extends BaseInlineTaglet
/*     */ {
/*     */   public ValueTaglet()
/*     */   {
/*  59 */     this.name = "value";
/*     */   }
/*     */ 
/*     */   public boolean inMethod()
/*     */   {
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inConstructor()
/*     */   {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inOverview()
/*     */   {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inPackage()
/*     */   {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inType()
/*     */   {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   private FieldDoc getFieldDoc(Configuration paramConfiguration, Tag paramTag, String paramString)
/*     */   {
/* 123 */     if ((paramString == null) || (paramString.length() == 0))
/*     */     {
/* 125 */       if ((paramTag.holder() instanceof FieldDoc)) {
/* 126 */         return (FieldDoc)paramTag.holder();
/*     */       }
/*     */ 
/* 130 */       return null;
/*     */     }
/*     */ 
/* 133 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "#");
/* 134 */     String str = null;
/* 135 */     ClassDoc localClassDoc = null;
/* 136 */     if (localStringTokenizer.countTokens() == 1)
/*     */     {
/* 138 */       localObject = paramTag.holder();
/* 139 */       if ((localObject instanceof MemberDoc))
/* 140 */         localClassDoc = ((MemberDoc)localObject).containingClass();
/* 141 */       else if ((localObject instanceof ClassDoc)) {
/* 142 */         localClassDoc = (ClassDoc)localObject;
/*     */       }
/* 144 */       str = localStringTokenizer.nextToken();
/*     */     }
/*     */     else {
/* 147 */       localClassDoc = paramConfiguration.root.classNamed(localStringTokenizer.nextToken());
/* 148 */       str = localStringTokenizer.nextToken();
/*     */     }
/* 150 */     if (localClassDoc == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     Object localObject = localClassDoc.fields();
/* 154 */     for (int i = 0; i < localObject.length; i++) {
/* 155 */       if (localObject[i].name().equals(str)) {
/* 156 */         return localObject[i];
/*     */       }
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*     */   {
/* 166 */     FieldDoc localFieldDoc = getFieldDoc(paramTagletWriter
/* 167 */       .configuration(), paramTag, paramTag.text());
/* 168 */     if (localFieldDoc == null) {
/* 169 */       if (paramTag.text().isEmpty())
/*     */       {
/* 171 */         paramTagletWriter.getMsgRetriever().warning(paramTag.holder().position(), "doclet.value_tag_invalid_use", new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 175 */         paramTagletWriter.getMsgRetriever().warning(paramTag.holder().position(), "doclet.value_tag_invalid_reference", new Object[] { paramTag
/* 176 */           .text() });
/*     */       }
/*     */     } else { if (localFieldDoc.constantValue() != null) {
/* 179 */         return paramTagletWriter.valueTagOutput(localFieldDoc, localFieldDoc
/* 180 */           .constantValueExpression(), 
/* 181 */           !localFieldDoc
/* 181 */           .equals(paramTag
/* 181 */           .holder()));
/*     */       }
/*     */ 
/* 184 */       paramTagletWriter.getMsgRetriever().warning(paramTag.holder().position(), "doclet.value_tag_invalid_constant", new Object[] { localFieldDoc
/* 185 */         .name() });
/*     */     }
/* 187 */     return paramTagletWriter.getOutputInstance();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.ValueTaglet
 * JD-Core Version:    0.6.2
 */