/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ 
/*     */ public abstract class AbstractExecutableMemberWriter extends AbstractMemberWriter
/*     */ {
/*     */   public AbstractExecutableMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc)
/*     */   {
/*  49 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public AbstractExecutableMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  53 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */   protected void addTypeParameters(ExecutableMemberDoc paramExecutableMemberDoc, Content paramContent)
/*     */   {
/*  64 */     Content localContent = getTypeParameters(paramExecutableMemberDoc);
/*  65 */     if (!localContent.isEmpty()) {
/*  66 */       paramContent.addContent(localContent);
/*  67 */       paramContent.addContent(this.writer.getSpace());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Content getTypeParameters(ExecutableMemberDoc paramExecutableMemberDoc)
/*     */   {
/*  78 */     LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER_TYPE_PARAMS, paramExecutableMemberDoc);
/*     */ 
/*  80 */     return this.writer.getTypeParameterLinks(localLinkInfoImpl);
/*     */   }
/*     */ 
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/*  87 */     ExecutableMemberDoc localExecutableMemberDoc = (ExecutableMemberDoc)paramProgramElementDoc;
/*  88 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, localExecutableMemberDoc, localExecutableMemberDoc
/*  89 */       .qualifiedName() + localExecutableMemberDoc.flatSignature());
/*     */   }
/*     */ 
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 102 */     ExecutableMemberDoc localExecutableMemberDoc = (ExecutableMemberDoc)paramProgramElementDoc;
/* 103 */     String str = localExecutableMemberDoc.name();
/* 104 */     HtmlTree localHtmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 105 */       .getDocLink(paramKind, paramClassDoc, localExecutableMemberDoc, str, false));
/*     */ 
/* 107 */     HtmlTree localHtmlTree2 = HtmlTree.CODE(localHtmlTree1);
/* 108 */     addParameters(localExecutableMemberDoc, false, localHtmlTree2, str.length() - 1);
/* 109 */     paramContent.addContent(localHtmlTree2);
/*     */   }
/*     */ 
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent)
/*     */   {
/* 121 */     paramContent.addContent(this.writer
/* 122 */       .getDocLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 123 */       .name(), false));
/*     */   }
/*     */ 
/*     */   protected void addParam(ExecutableMemberDoc paramExecutableMemberDoc, Parameter paramParameter, boolean paramBoolean, Content paramContent)
/*     */   {
/* 136 */     if (paramParameter.type() != null) {
/* 137 */       Content localContent = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.EXECUTABLE_MEMBER_PARAM, paramParameter
/* 139 */         .type()).varargs(paramBoolean));
/* 140 */       paramContent.addContent(localContent);
/*     */     }
/* 142 */     if (paramParameter.name().length() > 0) {
/* 143 */       paramContent.addContent(this.writer.getSpace());
/* 144 */       paramContent.addContent(paramParameter.name());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addReceiverAnnotations(ExecutableMemberDoc paramExecutableMemberDoc, Type paramType, AnnotationDesc[] paramArrayOfAnnotationDesc, Content paramContent)
/*     */   {
/* 158 */     this.writer.addReceiverAnnotationInfo(paramExecutableMemberDoc, paramArrayOfAnnotationDesc, paramContent);
/* 159 */     paramContent.addContent(this.writer.getSpace());
/* 160 */     paramContent.addContent(paramType.typeName());
/* 161 */     LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE, paramType);
/*     */ 
/* 163 */     paramContent.addContent(this.writer.getTypeParameterLinks(localLinkInfoImpl));
/* 164 */     paramContent.addContent(this.writer.getSpace());
/* 165 */     paramContent.addContent("this");
/*     */   }
/*     */ 
/*     */   protected void addParameters(ExecutableMemberDoc paramExecutableMemberDoc, Content paramContent, int paramInt)
/*     */   {
/* 176 */     addParameters(paramExecutableMemberDoc, true, paramContent, paramInt);
/*     */   }
/*     */ 
/*     */   protected void addParameters(ExecutableMemberDoc paramExecutableMemberDoc, boolean paramBoolean, Content paramContent, int paramInt)
/*     */   {
/* 188 */     paramContent.addContent("(");
/* 189 */     String str1 = "";
/* 190 */     Parameter[] arrayOfParameter = paramExecutableMemberDoc.parameters();
/* 191 */     String str2 = makeSpace(paramInt + 1);
/* 192 */     Type localType = paramExecutableMemberDoc.receiverType();
/* 193 */     if ((paramBoolean) && ((localType instanceof AnnotatedType))) {
/* 194 */       AnnotationDesc[] arrayOfAnnotationDesc = localType.asAnnotatedType().annotations();
/* 195 */       if (arrayOfAnnotationDesc.length > 0) {
/* 196 */         addReceiverAnnotations(paramExecutableMemberDoc, localType, arrayOfAnnotationDesc, paramContent);
/* 197 */         str1 = "," + DocletConstants.NL + str2;
/*     */       }
/*     */     }
/*     */     boolean bool;
/* 201 */     for (int i = 0; i < arrayOfParameter.length; i++) {
/* 202 */       paramContent.addContent(str1);
/* 203 */       Parameter localParameter = arrayOfParameter[i];
/* 204 */       if (!localParameter.name().startsWith("this$")) {
/* 205 */         if (paramBoolean)
/*     */         {
/* 207 */           bool = this.writer
/* 207 */             .addAnnotationInfo(str2
/* 207 */             .length(), paramExecutableMemberDoc, localParameter, paramContent);
/*     */ 
/* 209 */           if (bool) {
/* 210 */             paramContent.addContent(DocletConstants.NL);
/* 211 */             paramContent.addContent(str2);
/*     */           }
/*     */         }
/* 214 */         addParam(paramExecutableMemberDoc, localParameter, (i == arrayOfParameter.length - 1) && 
/* 215 */           (paramExecutableMemberDoc
/* 215 */           .isVarArgs()), paramContent);
/* 216 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 220 */     for (int j = i + 1; j < arrayOfParameter.length; j++) {
/* 221 */       paramContent.addContent(",");
/* 222 */       paramContent.addContent(DocletConstants.NL);
/* 223 */       paramContent.addContent(str2);
/* 224 */       if (paramBoolean)
/*     */       {
/* 226 */         bool = this.writer
/* 226 */           .addAnnotationInfo(str2
/* 226 */           .length(), paramExecutableMemberDoc, arrayOfParameter[j], paramContent);
/*     */ 
/* 228 */         if (bool) {
/* 229 */           paramContent.addContent(DocletConstants.NL);
/* 230 */           paramContent.addContent(str2);
/*     */         }
/*     */       }
/* 233 */       addParam(paramExecutableMemberDoc, arrayOfParameter[j], (j == arrayOfParameter.length - 1) && (paramExecutableMemberDoc.isVarArgs()), paramContent);
/*     */     }
/*     */ 
/* 236 */     paramContent.addContent(")");
/*     */   }
/*     */ 
/*     */   protected void addExceptions(ExecutableMemberDoc paramExecutableMemberDoc, Content paramContent, int paramInt)
/*     */   {
/* 246 */     Type[] arrayOfType = paramExecutableMemberDoc.thrownExceptionTypes();
/* 247 */     if (arrayOfType.length > 0) {
/* 248 */       LinkInfoImpl localLinkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramExecutableMemberDoc);
/*     */ 
/* 250 */       String str = makeSpace(paramInt + 1 - 7);
/* 251 */       paramContent.addContent(DocletConstants.NL);
/* 252 */       paramContent.addContent(str);
/* 253 */       paramContent.addContent("throws ");
/* 254 */       str = makeSpace(paramInt + 1);
/* 255 */       Content localContent1 = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, arrayOfType[0]));
/*     */ 
/* 257 */       paramContent.addContent(localContent1);
/* 258 */       for (int i = 1; i < arrayOfType.length; i++) {
/* 259 */         paramContent.addContent(",");
/* 260 */         paramContent.addContent(DocletConstants.NL);
/* 261 */         paramContent.addContent(str);
/* 262 */         Content localContent2 = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, arrayOfType[i]));
/*     */ 
/* 264 */         paramContent.addContent(localContent2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ClassDoc implementsMethodInIntfac(MethodDoc paramMethodDoc, ClassDoc[] paramArrayOfClassDoc)
/*     */   {
/* 271 */     for (int i = 0; i < paramArrayOfClassDoc.length; i++) {
/* 272 */       MethodDoc[] arrayOfMethodDoc = paramArrayOfClassDoc[i].methods();
/* 273 */       if (arrayOfMethodDoc.length > 0) {
/* 274 */         for (int j = 0; j < arrayOfMethodDoc.length; j++) {
/* 275 */           if ((arrayOfMethodDoc[j].name().equals(paramMethodDoc.name())) && 
/* 276 */             (arrayOfMethodDoc[j]
/* 276 */             .signature().equals(paramMethodDoc.signature()))) {
/* 277 */             return paramArrayOfClassDoc[i];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 282 */     return null;
/*     */   }
/*     */ 
/*     */   protected String getErasureAnchor(ExecutableMemberDoc paramExecutableMemberDoc)
/*     */   {
/* 294 */     StringBuilder localStringBuilder = new StringBuilder(paramExecutableMemberDoc.name() + "(");
/* 295 */     Parameter[] arrayOfParameter = paramExecutableMemberDoc.parameters();
/* 296 */     int i = 0;
/* 297 */     for (int j = 0; j < arrayOfParameter.length; j++) {
/* 298 */       if (j > 0) {
/* 299 */         localStringBuilder.append(",");
/*     */       }
/* 301 */       Type localType = arrayOfParameter[j].type();
/* 302 */       i = (i != 0) || (localType.asTypeVariable() != null) ? 1 : 0;
/* 303 */       localStringBuilder.append(localType.isPrimitive() ? localType
/* 304 */         .typeName() : localType.asClassDoc().qualifiedName());
/* 305 */       localStringBuilder.append(localType.dimension());
/*     */     }
/* 307 */     localStringBuilder.append(")");
/* 308 */     return i != 0 ? this.writer.getName(localStringBuilder.toString()) : null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.AbstractExecutableMemberWriter
 * JD-Core Version:    0.6.2
 */