/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ClassDocCatalog
/*     */ {
/*     */   private Set<String> packageSet;
/*     */   private Map<String, Set<ClassDoc>> allClasses;
/*     */   private Map<String, Set<ClassDoc>> ordinaryClasses;
/*     */   private Map<String, Set<ClassDoc>> exceptions;
/*     */   private Map<String, Set<ClassDoc>> enums;
/*     */   private Map<String, Set<ClassDoc>> annotationTypes;
/*     */   private Map<String, Set<ClassDoc>> errors;
/*     */   private Map<String, Set<ClassDoc>> interfaces;
/*     */   private Configuration configuration;
/*     */ 
/*     */   public ClassDocCatalog(ClassDoc[] paramArrayOfClassDoc, Configuration paramConfiguration)
/*     */   {
/* 101 */     init();
/* 102 */     this.configuration = paramConfiguration;
/* 103 */     for (int i = 0; i < paramArrayOfClassDoc.length; i++)
/* 104 */       addClassDoc(paramArrayOfClassDoc[i]);
/*     */   }
/*     */ 
/*     */   public ClassDocCatalog()
/*     */   {
/* 113 */     init();
/*     */   }
/*     */ 
/*     */   private void init() {
/* 117 */     this.allClasses = new HashMap();
/* 118 */     this.ordinaryClasses = new HashMap();
/* 119 */     this.exceptions = new HashMap();
/* 120 */     this.enums = new HashMap();
/* 121 */     this.annotationTypes = new HashMap();
/* 122 */     this.errors = new HashMap();
/* 123 */     this.interfaces = new HashMap();
/* 124 */     this.packageSet = new HashSet();
/*     */   }
/*     */ 
/*     */   public void addClassDoc(ClassDoc paramClassDoc)
/*     */   {
/* 132 */     if (paramClassDoc == null) {
/* 133 */       return;
/*     */     }
/* 135 */     addClass(paramClassDoc, this.allClasses);
/* 136 */     if (paramClassDoc.isOrdinaryClass())
/* 137 */       addClass(paramClassDoc, this.ordinaryClasses);
/* 138 */     else if (paramClassDoc.isException())
/* 139 */       addClass(paramClassDoc, this.exceptions);
/* 140 */     else if (paramClassDoc.isEnum())
/* 141 */       addClass(paramClassDoc, this.enums);
/* 142 */     else if (paramClassDoc.isAnnotationType())
/* 143 */       addClass(paramClassDoc, this.annotationTypes);
/* 144 */     else if (paramClassDoc.isError())
/* 145 */       addClass(paramClassDoc, this.errors);
/* 146 */     else if (paramClassDoc.isInterface())
/* 147 */       addClass(paramClassDoc, this.interfaces);
/*     */   }
/*     */ 
/*     */   private void addClass(ClassDoc paramClassDoc, Map<String, Set<ClassDoc>> paramMap)
/*     */   {
/* 158 */     PackageDoc localPackageDoc = paramClassDoc.containingPackage();
/* 159 */     if ((localPackageDoc.isIncluded()) || ((this.configuration.nodeprecated) && (Util.isDeprecated(localPackageDoc))))
/*     */     {
/* 163 */       return;
/*     */     }
/* 165 */     String str = Util.getPackageName(localPackageDoc);
/* 166 */     Object localObject = (Set)paramMap.get(str);
/* 167 */     if (localObject == null) {
/* 168 */       this.packageSet.add(str);
/* 169 */       localObject = new HashSet();
/*     */     }
/* 171 */     ((Set)localObject).add(paramClassDoc);
/* 172 */     paramMap.put(str, localObject);
/*     */   }
/*     */ 
/*     */   private ClassDoc[] getArray(Map<String, Set<ClassDoc>> paramMap, String paramString)
/*     */   {
/* 177 */     Set localSet = (Set)paramMap.get(paramString);
/* 178 */     if (localSet == null) {
/* 179 */       return new ClassDoc[0];
/*     */     }
/* 181 */     return (ClassDoc[])localSet.toArray(new ClassDoc[0]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] allClasses(PackageDoc paramPackageDoc)
/*     */   {
/* 193 */     return paramPackageDoc.isIncluded() ? paramPackageDoc
/* 192 */       .allClasses() : 
/* 193 */       getArray(this.allClasses, 
/* 193 */       Util.getPackageName(paramPackageDoc));
/*     */   }
/*     */ 
/*     */   public ClassDoc[] allClasses(String paramString)
/*     */   {
/* 203 */     return getArray(this.allClasses, paramString);
/*     */   }
/*     */ 
/*     */   public String[] packageNames()
/*     */   {
/* 211 */     return (String[])this.packageSet.toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   public boolean isKnownPackage(String paramString)
/*     */   {
/* 221 */     return this.packageSet.contains(paramString);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] errors(String paramString)
/*     */   {
/* 232 */     return getArray(this.errors, paramString);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] exceptions(String paramString)
/*     */   {
/* 242 */     return getArray(this.exceptions, paramString);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] enums(String paramString)
/*     */   {
/* 252 */     return getArray(this.enums, paramString);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] annotationTypes(String paramString)
/*     */   {
/* 262 */     return getArray(this.annotationTypes, paramString);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] interfaces(String paramString)
/*     */   {
/* 272 */     return getArray(this.interfaces, paramString);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] ordinaryClasses(String paramString)
/*     */   {
/* 282 */     return getArray(this.ordinaryClasses, paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog
 * JD-Core Version:    0.6.2
 */