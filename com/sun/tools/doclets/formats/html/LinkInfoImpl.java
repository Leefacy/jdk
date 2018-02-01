/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkInfo;
/*     */ 
/*     */ public class LinkInfoImpl extends LinkInfo
/*     */ {
/*     */   public final ConfigurationImpl configuration;
/* 218 */   public Kind context = Kind.DEFAULT;
/*     */ 
/* 223 */   public String where = "";
/*     */ 
/* 228 */   public String styleName = "";
/*     */ 
/* 233 */   public String target = "";
/*     */ 
/*     */   public LinkInfoImpl(ConfigurationImpl paramConfigurationImpl, Kind paramKind, ExecutableMemberDoc paramExecutableMemberDoc)
/*     */   {
/* 245 */     this.configuration = paramConfigurationImpl;
/* 246 */     this.executableMemberDoc = paramExecutableMemberDoc;
/* 247 */     setContext(paramKind);
/*     */   }
/*     */ 
/*     */   protected Content newContent()
/*     */   {
/* 254 */     return new ContentBuilder();
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl(ConfigurationImpl paramConfigurationImpl, Kind paramKind, ClassDoc paramClassDoc)
/*     */   {
/* 266 */     this.configuration = paramConfigurationImpl;
/* 267 */     this.classDoc = paramClassDoc;
/* 268 */     setContext(paramKind);
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl(ConfigurationImpl paramConfigurationImpl, Kind paramKind, Type paramType)
/*     */   {
/* 280 */     this.configuration = paramConfigurationImpl;
/* 281 */     this.type = paramType;
/* 282 */     setContext(paramKind);
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl label(String paramString)
/*     */   {
/* 291 */     this.label = new StringContent(paramString);
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl label(Content paramContent)
/*     */   {
/* 299 */     this.label = paramContent;
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl strong(boolean paramBoolean)
/*     */   {
/* 307 */     this.isStrong = paramBoolean;
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl styleName(String paramString)
/*     */   {
/* 316 */     this.styleName = paramString;
/* 317 */     return this;
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl target(String paramString)
/*     */   {
/* 325 */     this.target = paramString;
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl varargs(boolean paramBoolean)
/*     */   {
/* 333 */     this.isVarArg = paramBoolean;
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */   public LinkInfoImpl where(String paramString)
/*     */   {
/* 341 */     this.where = paramString;
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   public Kind getContext()
/*     */   {
/* 349 */     return this.context;
/*     */   }
/*     */ 
/*     */   public final void setContext(Kind paramKind)
/*     */   {
/* 362 */     switch (1.$SwitchMap$com$sun$tools$doclets$formats$html$LinkInfoImpl$Kind[paramKind.ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/* 371 */       this.includeTypeInClassLinkLabel = false;
/* 372 */       break;
/*     */     case 9:
/* 375 */       this.excludeTypeParameterLinks = true;
/* 376 */       this.excludeTypeBounds = true;
/* 377 */       break;
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/* 385 */       this.excludeTypeParameterLinks = true;
/* 386 */       this.excludeTypeBounds = true;
/* 387 */       this.includeTypeInClassLinkLabel = false;
/* 388 */       this.includeTypeAsSepLink = true;
/* 389 */       break;
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/* 395 */       this.excludeTypeParameterLinks = true;
/* 396 */       this.includeTypeAsSepLink = true;
/* 397 */       this.includeTypeInClassLinkLabel = false;
/* 398 */       break;
/*     */     case 20:
/* 401 */       this.includeTypeAsSepLink = true;
/* 402 */       this.includeTypeInClassLinkLabel = false;
/* 403 */       break;
/*     */     case 21:
/*     */     case 22:
/* 407 */       this.excludeTypeBounds = true;
/* 408 */       break;
/*     */     case 23:
/* 410 */       this.excludeTypeBounds = true;
/*     */     }
/*     */ 
/* 413 */     this.context = paramKind;
/* 414 */     if ((this.type != null) && 
/* 415 */       (this.type
/* 415 */       .asTypeVariable() != null) && 
/* 416 */       ((this.type
/* 416 */       .asTypeVariable().owner() instanceof ExecutableMemberDoc)))
/* 417 */       this.excludeTypeParameterLinks = true;
/*     */   }
/*     */ 
/*     */   public boolean isLinkable()
/*     */   {
/* 429 */     return Util.isLinkable(this.classDoc, this.configuration);
/*     */   }
/*     */ 
/*     */   public static enum Kind
/*     */   {
/*  45 */     DEFAULT, 
/*     */ 
/*  50 */     ALL_CLASSES_FRAME, 
/*     */ 
/*  55 */     CLASS, 
/*     */ 
/*  60 */     MEMBER, 
/*     */ 
/*  65 */     CLASS_USE, 
/*     */ 
/*  70 */     INDEX, 
/*     */ 
/*  75 */     CONSTANT_SUMMARY, 
/*     */ 
/*  80 */     SERIALIZED_FORM, 
/*     */ 
/*  85 */     SERIAL_MEMBER, 
/*     */ 
/*  90 */     PACKAGE, 
/*     */ 
/*  95 */     SEE_TAG, 
/*     */ 
/* 100 */     VALUE_TAG, 
/*     */ 
/* 105 */     TREE, 
/*     */ 
/* 110 */     PACKAGE_FRAME, 
/*     */ 
/* 115 */     CLASS_HEADER, 
/*     */ 
/* 120 */     CLASS_SIGNATURE, 
/*     */ 
/* 125 */     RETURN_TYPE, 
/*     */ 
/* 130 */     SUMMARY_RETURN_TYPE, 
/*     */ 
/* 135 */     EXECUTABLE_MEMBER_PARAM, 
/*     */ 
/* 140 */     SUPER_INTERFACES, 
/*     */ 
/* 145 */     IMPLEMENTED_INTERFACES, 
/*     */ 
/* 150 */     IMPLEMENTED_CLASSES, 
/*     */ 
/* 155 */     SUBINTERFACES, 
/*     */ 
/* 160 */     SUBCLASSES, 
/*     */ 
/* 165 */     CLASS_SIGNATURE_PARENT_NAME, 
/*     */ 
/* 170 */     METHOD_DOC_COPY, 
/*     */ 
/* 175 */     METHOD_SPECIFIED_BY, 
/*     */ 
/* 180 */     METHOD_OVERRIDES, 
/*     */ 
/* 185 */     ANNOTATION, 
/*     */ 
/* 190 */     FIELD_DOC_COPY, 
/*     */ 
/* 195 */     CLASS_TREE_PARENT, 
/*     */ 
/* 200 */     MEMBER_TYPE_PARAMS, 
/*     */ 
/* 205 */     CLASS_USE_HEADER, 
/*     */ 
/* 210 */     PROPERTY_DOC_COPY;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.LinkInfoImpl
 * JD-Core Version:    0.6.2
 */