/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class Group
/*     */ {
/*  64 */   private Map<String, String> regExpGroupMap = new HashMap();
/*     */ 
/*  70 */   private List<String> sortedRegExpList = new ArrayList();
/*     */ 
/*  75 */   private List<String> groupList = new ArrayList();
/*     */ 
/*  81 */   private Map<String, String> pkgNameGroupMap = new HashMap();
/*     */   private final Configuration configuration;
/*     */ 
/*     */   public Group(Configuration paramConfiguration)
/*     */   {
/* 100 */     this.configuration = paramConfiguration;
/*     */   }
/*     */ 
/*     */   public boolean checkPackageGroups(String paramString1, String paramString2)
/*     */   {
/* 119 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, ":");
/* 120 */     if (this.groupList.contains(paramString1)) {
/* 121 */       this.configuration.message.warning("doclet.Groupname_already_used", new Object[] { paramString1 });
/* 122 */       return false;
/*     */     }
/* 124 */     this.groupList.add(paramString1);
/* 125 */     while (localStringTokenizer.hasMoreTokens()) {
/* 126 */       String str = localStringTokenizer.nextToken();
/* 127 */       if (str.length() == 0) {
/* 128 */         this.configuration.message.warning("doclet.Error_in_packagelist", new Object[] { paramString1, paramString2 });
/* 129 */         return false;
/*     */       }
/* 131 */       if (str.endsWith("*")) {
/* 132 */         str = str.substring(0, str.length() - 1);
/* 133 */         if (foundGroupFormat(this.regExpGroupMap, str)) {
/* 134 */           return false;
/*     */         }
/* 136 */         this.regExpGroupMap.put(str, paramString1);
/* 137 */         this.sortedRegExpList.add(str);
/*     */       } else {
/* 139 */         if (foundGroupFormat(this.pkgNameGroupMap, str)) {
/* 140 */           return false;
/*     */         }
/* 142 */         this.pkgNameGroupMap.put(str, paramString1);
/*     */       }
/*     */     }
/* 145 */     Collections.sort(this.sortedRegExpList, new MapKeyComparator(null));
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   boolean foundGroupFormat(Map<String, ?> paramMap, String paramString)
/*     */   {
/* 158 */     if (paramMap.containsKey(paramString)) {
/* 159 */       this.configuration.message.error("doclet.Same_package_name_used", new Object[] { paramString });
/* 160 */       return true;
/*     */     }
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   public Map<String, List<PackageDoc>> groupPackages(PackageDoc[] paramArrayOfPackageDoc)
/*     */   {
/* 179 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 183 */     String str1 = (this.pkgNameGroupMap
/* 181 */       .isEmpty()) && (this.regExpGroupMap.isEmpty()) ? this.configuration.message
/* 182 */       .getText("doclet.Packages", new Object[0]) : 
/* 182 */       this.configuration.message
/* 183 */       .getText("doclet.Other_Packages", new Object[0]);
/*     */ 
/* 185 */     if (!this.groupList.contains(str1)) {
/* 186 */       this.groupList.add(str1);
/*     */     }
/* 188 */     for (int i = 0; i < paramArrayOfPackageDoc.length; i++) {
/* 189 */       PackageDoc localPackageDoc = paramArrayOfPackageDoc[i];
/* 190 */       String str2 = localPackageDoc.name();
/* 191 */       String str3 = (String)this.pkgNameGroupMap.get(str2);
/*     */ 
/* 194 */       if (str3 == null) {
/* 195 */         str3 = regExpGroupName(str2);
/*     */       }
/*     */ 
/* 199 */       if (str3 == null) {
/* 200 */         str3 = str1;
/*     */       }
/* 202 */       getPkgList(localHashMap, str3).add(localPackageDoc);
/*     */     }
/* 204 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   String regExpGroupName(String paramString)
/*     */   {
/* 215 */     for (int i = 0; i < this.sortedRegExpList.size(); i++) {
/* 216 */       String str = (String)this.sortedRegExpList.get(i);
/* 217 */       if (paramString.startsWith(str)) {
/* 218 */         return (String)this.regExpGroupMap.get(str);
/*     */       }
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   List<PackageDoc> getPkgList(Map<String, List<PackageDoc>> paramMap, String paramString)
/*     */   {
/* 232 */     Object localObject = (List)paramMap.get(paramString);
/* 233 */     if (localObject == null) {
/* 234 */       localObject = new ArrayList();
/* 235 */       paramMap.put(paramString, localObject);
/*     */     }
/* 237 */     return localObject;
/*     */   }
/*     */ 
/*     */   public List<String> getGroupList()
/*     */   {
/* 245 */     return this.groupList;
/*     */   }
/*     */ 
/*     */   private static class MapKeyComparator
/*     */     implements Comparator<String>
/*     */   {
/*     */     public int compare(String paramString1, String paramString2)
/*     */     {
/*  95 */       return paramString2.length() - paramString1.length();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.Group
 * JD-Core Version:    0.6.2
 */