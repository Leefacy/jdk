/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkInfo;
/*     */ import java.util.List;
/*     */ 
/*     */ public class LinkFactoryImpl extends LinkFactory
/*     */ {
/*     */   private HtmlDocletWriter m_writer;
/*     */ 
/*     */   public LinkFactoryImpl(HtmlDocletWriter paramHtmlDocletWriter)
/*     */   {
/*  54 */     this.m_writer = paramHtmlDocletWriter;
/*     */   }
/*     */ 
/*     */   protected Content newContent()
/*     */   {
/*  61 */     return new ContentBuilder();
/*     */   }
/*     */ 
/*     */   protected Content getClassLink(LinkInfo paramLinkInfo)
/*     */   {
/*  68 */     LinkInfoImpl localLinkInfoImpl = (LinkInfoImpl)paramLinkInfo;
/*  69 */     int i = (paramLinkInfo.label == null) || (paramLinkInfo.label.isEmpty()) ? 1 : 0;
/*  70 */     ClassDoc localClassDoc = localLinkInfoImpl.classDoc;
/*     */ 
/*  74 */     if (localLinkInfoImpl.type != null);
/*  75 */     String str = (localLinkInfoImpl.where == null) || 
/*  74 */       (localLinkInfoImpl.where
/*  74 */       .length() == 0) ? 
/*  75 */       getClassToolTip(localClassDoc, 
/*  77 */       !localClassDoc
/*  77 */       .qualifiedTypeName().equals(localLinkInfoImpl.type.qualifiedTypeName())) : "";
/*     */ 
/*  79 */     Content localContent = localLinkInfoImpl.getClassLinkLabel(this.m_writer.configuration);
/*  80 */     ConfigurationImpl localConfigurationImpl = this.m_writer.configuration;
/*  81 */     ContentBuilder localContentBuilder = new ContentBuilder();
/*     */     Object localObject;
/*  82 */     if (localClassDoc.isIncluded()) {
/*  83 */       if (localConfigurationImpl.isGeneratedDoc(localClassDoc)) {
/*  84 */         localObject = getPath(localLinkInfoImpl);
/*  85 */         if ((paramLinkInfo.linkToSelf) || 
/*  86 */           (!DocPath.forName(localClassDoc)
/*  86 */           .equals(this.m_writer.filename))) {
/*  87 */           localContentBuilder.addContent(this.m_writer.getHyperLink(((DocPath)localObject)
/*  88 */             .fragment(localLinkInfoImpl.where), 
/*  88 */             localContent, localLinkInfoImpl.isStrong, localLinkInfoImpl.styleName, str, localLinkInfoImpl.target));
/*     */ 
/*  92 */           if ((i != 0) && (!localLinkInfoImpl.excludeTypeParameterLinks)) {
/*  93 */             localContentBuilder.addContent(getTypeParameterLinks(paramLinkInfo));
/*     */           }
/*  95 */           return localContentBuilder;
/*     */         }
/*     */       }
/*     */     } else {
/*  99 */       localObject = this.m_writer.getCrossClassLink(localClassDoc
/* 100 */         .qualifiedName(), localLinkInfoImpl.where, localContent, localLinkInfoImpl.isStrong, localLinkInfoImpl.styleName, true);
/*     */ 
/* 103 */       if (localObject != null) {
/* 104 */         localContentBuilder.addContent((Content)localObject);
/* 105 */         if ((i != 0) && (!localLinkInfoImpl.excludeTypeParameterLinks)) {
/* 106 */           localContentBuilder.addContent(getTypeParameterLinks(paramLinkInfo));
/*     */         }
/* 108 */         return localContentBuilder;
/*     */       }
/*     */     }
/*     */ 
/* 112 */     localContentBuilder.addContent(localContent);
/* 113 */     if ((i != 0) && (!localLinkInfoImpl.excludeTypeParameterLinks)) {
/* 114 */       localContentBuilder.addContent(getTypeParameterLinks(paramLinkInfo));
/*     */     }
/* 116 */     return localContentBuilder;
/*     */   }
/*     */ 
/*     */   protected Content getTypeParameterLink(LinkInfo paramLinkInfo, Type paramType)
/*     */   {
/* 125 */     LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.m_writer.configuration, ((LinkInfoImpl)paramLinkInfo)
/* 125 */       .getContext(), paramType);
/* 126 */     localLinkInfoImpl.excludeTypeBounds = paramLinkInfo.excludeTypeBounds;
/* 127 */     localLinkInfoImpl.excludeTypeParameterLinks = paramLinkInfo.excludeTypeParameterLinks;
/* 128 */     localLinkInfoImpl.linkToSelf = paramLinkInfo.linkToSelf;
/* 129 */     localLinkInfoImpl.isJava5DeclarationLocation = false;
/* 130 */     return getLink(localLinkInfoImpl);
/*     */   }
/*     */ 
/*     */   protected Content getTypeAnnotationLink(LinkInfo paramLinkInfo, AnnotationDesc paramAnnotationDesc)
/*     */   {
/* 135 */     throw new RuntimeException("Not implemented yet!");
/*     */   }
/*     */ 
/*     */   public Content getTypeAnnotationLinks(LinkInfo paramLinkInfo) {
/* 139 */     ContentBuilder localContentBuilder = new ContentBuilder();
/*     */     AnnotationDesc[] arrayOfAnnotationDesc;
/* 141 */     if ((paramLinkInfo.type instanceof AnnotatedType))
/* 142 */       arrayOfAnnotationDesc = paramLinkInfo.type.asAnnotatedType().annotations();
/* 143 */     else if ((paramLinkInfo.type instanceof TypeVariable))
/* 144 */       arrayOfAnnotationDesc = paramLinkInfo.type.asTypeVariable().annotations();
/*     */     else {
/* 146 */       return localContentBuilder;
/*     */     }
/*     */ 
/* 149 */     if (arrayOfAnnotationDesc.length == 0) {
/* 150 */       return localContentBuilder;
/*     */     }
/* 152 */     List localList = this.m_writer.getAnnotations(0, arrayOfAnnotationDesc, false, paramLinkInfo.isJava5DeclarationLocation);
/*     */ 
/* 154 */     int i = 1;
/* 155 */     for (Content localContent : localList) {
/* 156 */       if (i == 0) {
/* 157 */         localContentBuilder.addContent(" ");
/*     */       }
/* 159 */       localContentBuilder.addContent(localContent);
/* 160 */       i = 0;
/*     */     }
/* 162 */     if (!localList.isEmpty()) {
/* 163 */       localContentBuilder.addContent(" ");
/*     */     }
/*     */ 
/* 166 */     return localContentBuilder;
/*     */   }
/*     */ 
/*     */   private String getClassToolTip(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 176 */     ConfigurationImpl localConfigurationImpl = this.m_writer.configuration;
/* 177 */     if (paramBoolean)
/* 178 */       return localConfigurationImpl.getText("doclet.Href_Type_Param_Title", paramClassDoc
/* 179 */         .name());
/* 180 */     if (paramClassDoc.isInterface())
/* 181 */       return localConfigurationImpl.getText("doclet.Href_Interface_Title", 
/* 182 */         Util.getPackageName(paramClassDoc
/* 182 */         .containingPackage()));
/* 183 */     if (paramClassDoc.isAnnotationType())
/* 184 */       return localConfigurationImpl.getText("doclet.Href_Annotation_Title", 
/* 185 */         Util.getPackageName(paramClassDoc
/* 185 */         .containingPackage()));
/* 186 */     if (paramClassDoc.isEnum()) {
/* 187 */       return localConfigurationImpl.getText("doclet.Href_Enum_Title", 
/* 188 */         Util.getPackageName(paramClassDoc
/* 188 */         .containingPackage()));
/*     */     }
/* 190 */     return localConfigurationImpl.getText("doclet.Href_Class_Title", 
/* 191 */       Util.getPackageName(paramClassDoc
/* 191 */       .containingPackage()));
/*     */   }
/*     */ 
/*     */   private DocPath getPath(LinkInfoImpl paramLinkInfoImpl)
/*     */   {
/* 204 */     if (paramLinkInfoImpl.context == LinkInfoImpl.Kind.PACKAGE_FRAME)
/*     */     {
/* 207 */       return DocPath.forName(paramLinkInfoImpl.classDoc);
/*     */     }
/* 209 */     return this.m_writer.pathToRoot.resolve(DocPath.forClass(paramLinkInfoImpl.classDoc));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.LinkFactoryImpl
 * JD-Core Version:    0.6.2
 */