/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DeprecatedAPIListBuilder
/*     */ {
/*     */   public static final int NUM_TYPES = 12;
/*     */   public static final int PACKAGE = 0;
/*     */   public static final int INTERFACE = 1;
/*     */   public static final int CLASS = 2;
/*     */   public static final int ENUM = 3;
/*     */   public static final int EXCEPTION = 4;
/*     */   public static final int ERROR = 5;
/*     */   public static final int ANNOTATION_TYPE = 6;
/*     */   public static final int FIELD = 7;
/*     */   public static final int METHOD = 8;
/*     */   public static final int CONSTRUCTOR = 9;
/*     */   public static final int ENUM_CONSTANT = 10;
/*     */   public static final int ANNOTATION_TYPE_MEMBER = 11;
/*     */   private List<List<Doc>> deprecatedLists;
/*     */ 
/*     */   public DeprecatedAPIListBuilder(Configuration paramConfiguration)
/*     */   {
/*  72 */     this.deprecatedLists = new ArrayList();
/*  73 */     for (int i = 0; i < 12; i++) {
/*  74 */       this.deprecatedLists.add(i, new ArrayList());
/*     */     }
/*  76 */     buildDeprecatedAPIInfo(paramConfiguration);
/*     */   }
/*     */ 
/*     */   private void buildDeprecatedAPIInfo(Configuration paramConfiguration)
/*     */   {
/*  87 */     PackageDoc[] arrayOfPackageDoc = paramConfiguration.packages;
/*     */ 
/*  89 */     for (int i = 0; i < arrayOfPackageDoc.length; i++) {
/*  90 */       PackageDoc localPackageDoc = arrayOfPackageDoc[i];
/*  91 */       if (Util.isDeprecated(localPackageDoc)) {
/*  92 */         getList(0).add(localPackageDoc);
/*     */       }
/*     */     }
/*  95 */     ClassDoc[] arrayOfClassDoc = paramConfiguration.root.classes();
/*  96 */     for (int j = 0; j < arrayOfClassDoc.length; j++) {
/*  97 */       ClassDoc localClassDoc = arrayOfClassDoc[j];
/*  98 */       if (Util.isDeprecated(localClassDoc)) {
/*  99 */         if (localClassDoc.isOrdinaryClass())
/* 100 */           getList(2).add(localClassDoc);
/* 101 */         else if (localClassDoc.isInterface())
/* 102 */           getList(1).add(localClassDoc);
/* 103 */         else if (localClassDoc.isException())
/* 104 */           getList(4).add(localClassDoc);
/* 105 */         else if (localClassDoc.isEnum())
/* 106 */           getList(3).add(localClassDoc);
/* 107 */         else if (localClassDoc.isError())
/* 108 */           getList(5).add(localClassDoc);
/* 109 */         else if (localClassDoc.isAnnotationType()) {
/* 110 */           getList(6).add(localClassDoc);
/*     */         }
/*     */       }
/* 113 */       composeDeprecatedList(getList(7), localClassDoc.fields());
/* 114 */       composeDeprecatedList(getList(8), localClassDoc.methods());
/* 115 */       composeDeprecatedList(getList(9), localClassDoc.constructors());
/* 116 */       if (localClassDoc.isEnum()) {
/* 117 */         composeDeprecatedList(getList(10), localClassDoc.enumConstants());
/*     */       }
/* 119 */       if (localClassDoc.isAnnotationType()) {
/* 120 */         composeDeprecatedList(getList(11), ((AnnotationTypeDoc)localClassDoc)
/* 121 */           .elements());
/*     */       }
/*     */     }
/* 124 */     sortDeprecatedLists();
/*     */   }
/*     */ 
/*     */   private void composeDeprecatedList(List<Doc> paramList, MemberDoc[] paramArrayOfMemberDoc)
/*     */   {
/* 134 */     for (int i = 0; i < paramArrayOfMemberDoc.length; i++)
/* 135 */       if (Util.isDeprecated(paramArrayOfMemberDoc[i]))
/* 136 */         paramList.add(paramArrayOfMemberDoc[i]);
/*     */   }
/*     */ 
/*     */   private void sortDeprecatedLists()
/*     */   {
/* 146 */     for (int i = 0; i < 12; i++)
/* 147 */       Collections.sort(getList(i));
/*     */   }
/*     */ 
/*     */   public List<Doc> getList(int paramInt)
/*     */   {
/* 157 */     return (List)this.deprecatedLists.get(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean hasDocumentation(int paramInt)
/*     */   {
/* 166 */     return ((List)this.deprecatedLists.get(paramInt)).size() > 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.DeprecatedAPIListBuilder
 * JD-Core Version:    0.6.2
 */