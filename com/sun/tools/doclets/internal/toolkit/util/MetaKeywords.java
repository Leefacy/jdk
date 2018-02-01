/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class MetaKeywords
/*     */ {
/*     */   private final Configuration configuration;
/*     */ 
/*     */   public MetaKeywords(Configuration paramConfiguration)
/*     */   {
/*  58 */     this.configuration = paramConfiguration;
/*     */   }
/*     */ 
/*     */   public String[] getMetaKeywords(ClassDoc paramClassDoc)
/*     */   {
/*  74 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/*  77 */     if (this.configuration.keywords) {
/*  78 */       localArrayList.addAll(getClassKeyword(paramClassDoc));
/*  79 */       localArrayList.addAll(getMemberKeywords(paramClassDoc.fields()));
/*  80 */       localArrayList.addAll(getMemberKeywords(paramClassDoc.methods()));
/*     */     }
/*  82 */     return (String[])localArrayList.toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   protected ArrayList<String> getClassKeyword(ClassDoc paramClassDoc)
/*     */   {
/*  90 */     String str = paramClassDoc.isInterface() ? "interface" : "class";
/*  91 */     ArrayList localArrayList = new ArrayList(1);
/*  92 */     localArrayList.add(paramClassDoc.qualifiedName() + " " + str);
/*  93 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public String[] getMetaKeywords(PackageDoc paramPackageDoc)
/*     */   {
/* 100 */     if (this.configuration.keywords) {
/* 101 */       String str = Util.getPackageName(paramPackageDoc);
/* 102 */       return new String[] { str + " " + "package" };
/*     */     }
/* 104 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public String[] getMetaKeywords(Profile paramProfile)
/*     */   {
/* 114 */     if (this.configuration.keywords) {
/* 115 */       String str = paramProfile.name;
/* 116 */       return new String[] { str + " " + "profile" };
/*     */     }
/* 118 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public String[] getOverviewMetaKeywords(String paramString1, String paramString2)
/*     */   {
/* 126 */     if (this.configuration.keywords) {
/* 127 */       String str = this.configuration.getText(paramString1);
/* 128 */       String[] arrayOfString = { str };
/* 129 */       if (paramString2.length() > 0)
/*     */       {
/*     */         int tmp46_45 = 0;
/*     */         String[] tmp46_43 = arrayOfString; tmp46_43[tmp46_45] = (tmp46_43[tmp46_45] + ", " + paramString2);
/*     */       }
/* 132 */       return arrayOfString;
/*     */     }
/* 134 */     return new String[0];
/*     */   }
/*     */ 
/*     */   protected ArrayList<String> getMemberKeywords(MemberDoc[] paramArrayOfMemberDoc)
/*     */   {
/* 148 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 150 */     for (int i = 0; i < paramArrayOfMemberDoc.length; i++)
/*     */     {
/* 152 */       String str = paramArrayOfMemberDoc[i].name() + (paramArrayOfMemberDoc[i]
/* 152 */         .isMethod() ? "()" : "");
/* 153 */       if (!localArrayList.contains(str)) {
/* 154 */         localArrayList.add(str);
/*     */       }
/*     */     }
/* 157 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.MetaKeywords
 * JD-Core Version:    0.6.2
 */