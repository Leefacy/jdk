/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ParamTaglet extends BaseTaglet
/*     */   implements InheritableTaglet
/*     */ {
/*     */   public ParamTaglet()
/*     */   {
/*  51 */     this.name = "param";
/*     */   }
/*     */ 
/*     */   private static Map<String, String> getRankMap(Object[] paramArrayOfObject)
/*     */   {
/*  63 */     if (paramArrayOfObject == null) {
/*  64 */       return null;
/*     */     }
/*  66 */     HashMap localHashMap = new HashMap();
/*  67 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*     */     {
/*  70 */       String str = (paramArrayOfObject[i] instanceof Parameter) ? ((Parameter)paramArrayOfObject[i])
/*  69 */         .name() : ((TypeVariable)paramArrayOfObject[i])
/*  70 */         .typeName();
/*  71 */       localHashMap.put(str, String.valueOf(i));
/*     */     }
/*  73 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput)
/*     */   {
/*  80 */     if (paramInput.tagId == null) {
/*  81 */       paramInput.isTypeVariableParamTag = ((ParamTag)paramInput.tag).isTypeParameter();
/*     */ 
/*  84 */       localObject1 = paramInput.isTypeVariableParamTag ? 
/*  83 */         (Object[])((MethodDoc)paramInput.tag
/*  83 */         .holder()).typeParameters() : 
/*  84 */         (Object[])((MethodDoc)paramInput.tag
/*  84 */         .holder()).parameters();
/*  85 */       localObject2 = ((ParamTag)paramInput.tag).parameterName();
/*     */ 
/*  87 */       for (i = 0; i < localObject1.length; i++)
/*     */       {
/*  90 */         String str = (localObject1[i] instanceof Parameter) ? ((Parameter)localObject1[i])
/*  89 */           .name() : ((TypeVariable)localObject1[i])
/*  90 */           .typeName();
/*  91 */         if (str.equals(localObject2)) {
/*  92 */           paramInput.tagId = String.valueOf(i);
/*  93 */           break;
/*     */         }
/*     */       }
/*  96 */       if (i == localObject1.length)
/*     */       {
/* 101 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 105 */     Object localObject1 = paramInput.isTypeVariableParamTag ? ((MethodDoc)paramInput.element)
/* 105 */       .typeParamTags() : ((MethodDoc)paramInput.element).paramTags();
/* 106 */     Object localObject2 = getRankMap(paramInput.isTypeVariableParamTag ? 
/* 107 */       (Object[])((MethodDoc)paramInput.element)
/* 107 */       .typeParameters() : 
/* 108 */       (Object[])((MethodDoc)paramInput.element)
/* 108 */       .parameters());
/* 109 */     for (int i = 0; i < localObject1.length; i++)
/* 110 */       if ((((Map)localObject2).containsKey(localObject1[i].parameterName())) && 
/* 111 */         (((String)((Map)localObject2)
/* 111 */         .get(localObject1[i]
/* 111 */         .parameterName())).equals(paramInput.tagId))) {
/* 112 */         paramOutput.holder = paramInput.element;
/* 113 */         paramOutput.holderTag = localObject1[i];
/* 114 */         paramOutput.inlineTags = (paramInput.isFirstSentence ? localObject1[i]
/* 115 */           .firstSentenceTags() : localObject1[i].inlineTags());
/* 116 */         return;
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean inField()
/*     */   {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inMethod()
/*     */   {
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inOverview()
/*     */   {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inPackage()
/*     */   {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean inType()
/*     */   {
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isInlineTag()
/*     */   {
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*     */   {
/* 170 */     if ((paramDoc instanceof ExecutableMemberDoc)) {
/* 171 */       localObject = (ExecutableMemberDoc)paramDoc;
/* 172 */       Content localContent = getTagletOutput(false, (Doc)localObject, paramTagletWriter, ((ExecutableMemberDoc)localObject)
/* 173 */         .typeParameters(), ((ExecutableMemberDoc)localObject).typeParamTags());
/* 174 */       localContent.addContent(getTagletOutput(true, (Doc)localObject, paramTagletWriter, ((ExecutableMemberDoc)localObject)
/* 175 */         .parameters(), ((ExecutableMemberDoc)localObject).paramTags()));
/* 176 */       return localContent;
/*     */     }
/* 178 */     Object localObject = (ClassDoc)paramDoc;
/* 179 */     return getTagletOutput(false, (Doc)localObject, paramTagletWriter, ((ClassDoc)localObject)
/* 180 */       .typeParameters(), ((ClassDoc)localObject).typeParamTags());
/*     */   }
/*     */ 
/*     */   private Content getTagletOutput(boolean paramBoolean, Doc paramDoc, TagletWriter paramTagletWriter, Object[] paramArrayOfObject, ParamTag[] paramArrayOfParamTag)
/*     */   {
/* 197 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 198 */     HashSet localHashSet = new HashSet();
/* 199 */     if (paramArrayOfParamTag.length > 0) {
/* 200 */       localContent.addContent(
/* 201 */         processParamTags(paramBoolean, paramArrayOfParamTag, 
/* 202 */         getRankMap(paramArrayOfObject), 
/* 202 */         paramTagletWriter, localHashSet));
/*     */     }
/*     */ 
/* 205 */     if (localHashSet.size() != paramArrayOfObject.length)
/*     */     {
/* 208 */       localContent.addContent(getInheritedTagletOutput(paramBoolean, paramDoc, paramTagletWriter, paramArrayOfObject, localHashSet));
/*     */     }
/*     */ 
/* 211 */     return localContent;
/*     */   }
/*     */ 
/*     */   private Content getInheritedTagletOutput(boolean paramBoolean, Doc paramDoc, TagletWriter paramTagletWriter, Object[] paramArrayOfObject, Set<String> paramSet)
/*     */   {
/* 221 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 222 */     if ((!paramSet.contains(null)) && ((paramDoc instanceof MethodDoc)))
/*     */     {
/* 224 */       for (int i = 0; i < paramArrayOfObject.length; i++)
/* 225 */         if (!paramSet.contains(String.valueOf(i)))
/*     */         {
/* 231 */           DocFinder.Output localOutput = DocFinder.search(new DocFinder.Input((MethodDoc)paramDoc, this, 
/* 232 */             String.valueOf(i), 
/* 232 */             !paramBoolean));
/* 233 */           if ((localOutput.inlineTags != null) && (localOutput.inlineTags.length > 0))
/*     */           {
/* 235 */             localContent.addContent(
/* 236 */               processParamTag(paramBoolean, paramTagletWriter, (ParamTag)localOutput.holderTag, paramBoolean ? ((Parameter)paramArrayOfObject[i])
/* 239 */               .name() : ((TypeVariable)paramArrayOfObject[i])
/* 240 */               .typeName(), paramSet
/* 241 */               .size() == 0));
/*     */           }
/* 243 */           paramSet.add(String.valueOf(i));
/*     */         }
/*     */     }
/* 246 */     return localContent;
/*     */   }
/*     */ 
/*     */   private Content processParamTags(boolean paramBoolean, ParamTag[] paramArrayOfParamTag, Map<String, String> paramMap, TagletWriter paramTagletWriter, Set<String> paramSet)
/*     */   {
/* 270 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 271 */     if (paramArrayOfParamTag.length > 0) {
/* 272 */       for (int i = 0; i < paramArrayOfParamTag.length; i++) {
/* 273 */         ParamTag localParamTag = paramArrayOfParamTag[i];
/*     */ 
/* 275 */         String str1 = "<" + localParamTag.parameterName() + ">";
/* 276 */         if (!paramMap.containsKey(localParamTag.parameterName())) {
/* 277 */           paramTagletWriter.getMsgRetriever().warning(localParamTag.position(), paramBoolean ? "doclet.Parameters_warn" : "doclet.Type_Parameters_warn", new Object[] { str1 });
/*     */         }
/*     */ 
/* 283 */         String str2 = (String)paramMap.get(localParamTag.parameterName());
/* 284 */         if ((str2 != null) && (paramSet.contains(str2))) {
/* 285 */           paramTagletWriter.getMsgRetriever().warning(localParamTag.position(), paramBoolean ? "doclet.Parameters_dup_warn" : "doclet.Type_Parameters_dup_warn", new Object[] { str1 });
/*     */         }
/*     */ 
/* 291 */         localContent.addContent(processParamTag(paramBoolean, paramTagletWriter, localParamTag, localParamTag
/* 292 */           .parameterName(), paramSet.size() == 0));
/* 293 */         paramSet.add(str2);
/*     */       }
/*     */     }
/* 296 */     return localContent;
/*     */   }
/*     */ 
/*     */   private Content processParamTag(boolean paramBoolean1, TagletWriter paramTagletWriter, ParamTag paramParamTag, String paramString, boolean paramBoolean2)
/*     */   {
/* 314 */     Content localContent = paramTagletWriter.getOutputInstance();
/* 315 */     String str = paramTagletWriter.configuration().getText(paramBoolean1 ? "doclet.Parameters" : "doclet.TypeParameters");
/*     */ 
/* 317 */     if (paramBoolean2) {
/* 318 */       localContent.addContent(paramTagletWriter.getParamHeader(str));
/*     */     }
/* 320 */     localContent.addContent(paramTagletWriter.paramTagOutput(paramParamTag, paramString));
/*     */ 
/* 322 */     return localContent;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.ParamTaglet
 * JD-Core Version:    0.6.2
 */