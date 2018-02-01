/*     */ package com.sun.tools.doclets.internal.toolkit.util.links;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ public abstract class LinkFactory
/*     */ {
/*     */   protected abstract Content newContent();
/*     */ 
/*     */   public Content getLink(LinkInfo paramLinkInfo)
/*     */   {
/*     */     Object localObject1;
/*  58 */     if (paramLinkInfo.type != null) {
/*  59 */       localObject1 = paramLinkInfo.type;
/*  60 */       Object localObject2 = newContent();
/*     */       Object localObject3;
/*  61 */       if (((Type)localObject1).isPrimitive())
/*     */       {
/*  63 */         ((Content)localObject2).addContent(((Type)localObject1).typeName()); } else {
/*  64 */         if ((((Type)localObject1).asAnnotatedType() != null) && (((Type)localObject1).dimension().length() == 0)) {
/*  65 */           ((Content)localObject2).addContent(getTypeAnnotationLinks(paramLinkInfo));
/*  66 */           paramLinkInfo.type = ((Type)localObject1).asAnnotatedType().underlyingType();
/*  67 */           ((Content)localObject2).addContent(getLink(paramLinkInfo));
/*  68 */           return localObject2;
/*     */         }
/*     */         Object localObject4;
/*  69 */         if (((Type)localObject1).asWildcardType() != null)
/*     */         {
/*  71 */           paramLinkInfo.isTypeBound = true;
/*  72 */           ((Content)localObject2).addContent("?");
/*  73 */           localObject3 = ((Type)localObject1).asWildcardType();
/*  74 */           localObject4 = ((WildcardType)localObject3).extendsBounds();
/*  75 */           for (int i = 0; i < localObject4.length; i++) {
/*  76 */             ((Content)localObject2).addContent(i > 0 ? ", " : " extends ");
/*  77 */             setBoundsLinkInfo(paramLinkInfo, localObject4[i]);
/*  78 */             ((Content)localObject2).addContent(getLink(paramLinkInfo));
/*     */           }
/*  80 */           Type[] arrayOfType = ((WildcardType)localObject3).superBounds();
/*  81 */           for (int k = 0; k < arrayOfType.length; k++) {
/*  82 */             ((Content)localObject2).addContent(k > 0 ? ", " : " super ");
/*  83 */             setBoundsLinkInfo(paramLinkInfo, arrayOfType[k]);
/*  84 */             ((Content)localObject2).addContent(getLink(paramLinkInfo));
/*     */           }
/*  86 */         } else if (((Type)localObject1).asTypeVariable() != null) {
/*  87 */           ((Content)localObject2).addContent(getTypeAnnotationLinks(paramLinkInfo));
/*  88 */           paramLinkInfo.isTypeBound = true;
/*     */ 
/*  90 */           localObject3 = ((Type)localObject1).asTypeVariable().owner();
/*  91 */           if ((!paramLinkInfo.excludeTypeParameterLinks) && ((localObject3 instanceof ClassDoc)))
/*     */           {
/*  93 */             paramLinkInfo.classDoc = ((ClassDoc)localObject3);
/*  94 */             localObject4 = newContent();
/*  95 */             ((Content)localObject4).addContent(((Type)localObject1).typeName());
/*  96 */             paramLinkInfo.label = ((Content)localObject4);
/*  97 */             ((Content)localObject2).addContent(getClassLink(paramLinkInfo));
/*     */           }
/*     */           else {
/* 100 */             ((Content)localObject2).addContent(((Type)localObject1).typeName());
/*     */           }
/*     */ 
/* 103 */           localObject4 = ((Type)localObject1).asTypeVariable().bounds();
/* 104 */           if (!paramLinkInfo.excludeTypeBounds) {
/* 105 */             paramLinkInfo.excludeTypeBounds = true;
/* 106 */             for (int j = 0; j < localObject4.length; j++) {
/* 107 */               ((Content)localObject2).addContent(j > 0 ? " & " : " extends ");
/* 108 */               setBoundsLinkInfo(paramLinkInfo, localObject4[j]);
/* 109 */               ((Content)localObject2).addContent(getLink(paramLinkInfo));
/*     */             }
/*     */           }
/* 112 */         } else if (((Type)localObject1).asClassDoc() != null)
/*     */         {
/* 114 */           if ((paramLinkInfo.isTypeBound) && (paramLinkInfo.excludeTypeBoundsLinks))
/*     */           {
/* 118 */             ((Content)localObject2).addContent(((Type)localObject1).typeName());
/* 119 */             ((Content)localObject2).addContent(getTypeParameterLinks(paramLinkInfo));
/* 120 */             return localObject2;
/*     */           }
/* 122 */           paramLinkInfo.classDoc = ((Type)localObject1).asClassDoc();
/* 123 */           localObject2 = newContent();
/* 124 */           ((Content)localObject2).addContent(getClassLink(paramLinkInfo));
/* 125 */           if (paramLinkInfo.includeTypeAsSepLink) {
/* 126 */             ((Content)localObject2).addContent(getTypeParameterLinks(paramLinkInfo, false));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 131 */       if (paramLinkInfo.isVarArg) {
/* 132 */         if (((Type)localObject1).dimension().length() > 2)
/*     */         {
/* 135 */           ((Content)localObject2).addContent(((Type)localObject1).dimension().substring(2));
/*     */         }
/* 137 */         ((Content)localObject2).addContent("...");
/*     */       } else {
/* 139 */         while ((localObject1 != null) && (((Type)localObject1).dimension().length() > 0)) {
/* 140 */           if (((Type)localObject1).asAnnotatedType() != null) {
/* 141 */             paramLinkInfo.type = ((Type)localObject1);
/* 142 */             ((Content)localObject2).addContent(" ");
/* 143 */             ((Content)localObject2).addContent(getTypeAnnotationLinks(paramLinkInfo));
/* 144 */             ((Content)localObject2).addContent("[]");
/* 145 */             localObject1 = ((Type)localObject1).asAnnotatedType().underlyingType().getElementType();
/*     */           } else {
/* 147 */             ((Content)localObject2).addContent("[]");
/* 148 */             localObject1 = ((Type)localObject1).getElementType();
/*     */           }
/*     */         }
/* 151 */         paramLinkInfo.type = ((Type)localObject1);
/* 152 */         localObject3 = newContent();
/* 153 */         ((Content)localObject3).addContent(getTypeAnnotationLinks(paramLinkInfo));
/* 154 */         ((Content)localObject3).addContent((Content)localObject2);
/* 155 */         localObject2 = localObject3;
/*     */       }
/* 157 */       return localObject2;
/* 158 */     }if (paramLinkInfo.classDoc != null)
/*     */     {
/* 160 */       localObject1 = newContent();
/* 161 */       ((Content)localObject1).addContent(getClassLink(paramLinkInfo));
/* 162 */       if (paramLinkInfo.includeTypeAsSepLink) {
/* 163 */         ((Content)localObject1).addContent(getTypeParameterLinks(paramLinkInfo, false));
/*     */       }
/* 165 */       return localObject1;
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   private void setBoundsLinkInfo(LinkInfo paramLinkInfo, Type paramType)
/*     */   {
/* 172 */     paramLinkInfo.classDoc = null;
/* 173 */     paramLinkInfo.label = null;
/* 174 */     paramLinkInfo.type = paramType;
/*     */   }
/*     */ 
/*     */   protected abstract Content getClassLink(LinkInfo paramLinkInfo);
/*     */ 
/*     */   protected abstract Content getTypeParameterLink(LinkInfo paramLinkInfo, Type paramType);
/*     */ 
/*     */   protected abstract Content getTypeAnnotationLink(LinkInfo paramLinkInfo, AnnotationDesc paramAnnotationDesc);
/*     */ 
/*     */   public Content getTypeParameterLinks(LinkInfo paramLinkInfo)
/*     */   {
/* 205 */     return getTypeParameterLinks(paramLinkInfo, true);
/*     */   }
/*     */ 
/*     */   public Content getTypeParameterLinks(LinkInfo paramLinkInfo, boolean paramBoolean)
/*     */   {
/* 217 */     Content localContent = newContent();
/*     */     Object localObject;
/* 219 */     if (paramLinkInfo.executableMemberDoc != null)
/* 220 */       localObject = paramLinkInfo.executableMemberDoc.typeParameters();
/* 221 */     else if ((paramLinkInfo.type != null) && 
/* 222 */       (paramLinkInfo.type
/* 222 */       .asParameterizedType() != null))
/* 223 */       localObject = paramLinkInfo.type.asParameterizedType().typeArguments();
/* 224 */     else if (paramLinkInfo.classDoc != null) {
/* 225 */       localObject = paramLinkInfo.classDoc.typeParameters();
/*     */     }
/*     */     else {
/* 228 */       return localContent;
/*     */     }
/* 230 */     if (((paramLinkInfo.includeTypeInClassLinkLabel) && (paramBoolean)) || ((paramLinkInfo.includeTypeAsSepLink) && (!paramBoolean) && (localObject.length > 0)))
/*     */     {
/* 234 */       localContent.addContent("<");
/* 235 */       for (int i = 0; i < localObject.length; i++) {
/* 236 */         if (i > 0) {
/* 237 */           localContent.addContent(",");
/*     */         }
/* 239 */         localContent.addContent(getTypeParameterLink(paramLinkInfo, localObject[i]));
/*     */       }
/* 241 */       localContent.addContent(">");
/*     */     }
/* 243 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getTypeAnnotationLinks(LinkInfo paramLinkInfo) {
/* 247 */     Content localContent = newContent();
/* 248 */     if (paramLinkInfo.type.asAnnotatedType() == null)
/* 249 */       return localContent;
/* 250 */     AnnotationDesc[] arrayOfAnnotationDesc = paramLinkInfo.type.asAnnotatedType().annotations();
/* 251 */     for (int i = 0; i < arrayOfAnnotationDesc.length; i++) {
/* 252 */       if (i > 0) {
/* 253 */         localContent.addContent(" ");
/*     */       }
/* 255 */       localContent.addContent(getTypeAnnotationLink(paramLinkInfo, arrayOfAnnotationDesc[i]));
/*     */     }
/*     */ 
/* 258 */     localContent.addContent(" ");
/* 259 */     return localContent;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.links.LinkFactory
 * JD-Core Version:    0.6.2
 */