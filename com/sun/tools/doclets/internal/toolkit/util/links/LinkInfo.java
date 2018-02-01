/*     */ package com.sun.tools.doclets.internal.toolkit.util.links;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ public abstract class LinkInfo
/*     */ {
/*     */   public ClassDoc classDoc;
/*     */   public ExecutableMemberDoc executableMemberDoc;
/*     */   public Type type;
/*  65 */   public boolean isVarArg = false;
/*     */ 
/*  70 */   public boolean isTypeBound = false;
/*     */ 
/*  76 */   public boolean isJava5DeclarationLocation = true;
/*     */   public Content label;
/*  86 */   public boolean isStrong = false;
/*     */ 
/*  91 */   public boolean includeTypeInClassLinkLabel = true;
/*     */ 
/*  96 */   public boolean includeTypeAsSepLink = false;
/*     */ 
/* 101 */   public boolean excludeTypeBounds = false;
/*     */ 
/* 106 */   public boolean excludeTypeParameterLinks = false;
/*     */ 
/* 111 */   public boolean excludeTypeBoundsLinks = false;
/*     */ 
/* 117 */   public boolean linkToSelf = true;
/*     */ 
/*     */   protected abstract Content newContent();
/*     */ 
/*     */   public abstract boolean isLinkable();
/*     */ 
/*     */   public Content getClassLinkLabel(Configuration paramConfiguration)
/*     */   {
/* 142 */     if ((this.label != null) && (!this.label.isEmpty()))
/* 143 */       return this.label;
/* 144 */     if (isLinkable()) {
/* 145 */       localContent = newContent();
/* 146 */       localContent.addContent(this.classDoc.name());
/* 147 */       return localContent;
/*     */     }
/* 149 */     Content localContent = newContent();
/* 150 */     localContent.addContent(paramConfiguration.getClassName(this.classDoc));
/* 151 */     return localContent;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.links.LinkInfo
 * JD-Core Version:    0.6.2
 */