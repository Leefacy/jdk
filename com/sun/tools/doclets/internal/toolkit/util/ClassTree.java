/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ClassTree
/*     */ {
/*  54 */   private List<ClassDoc> baseclasses = new ArrayList();
/*     */ 
/*  59 */   private Map<ClassDoc, List<ClassDoc>> subclasses = new HashMap();
/*     */ 
/*  66 */   private List<ClassDoc> baseinterfaces = new ArrayList();
/*     */ 
/*  71 */   private Map<ClassDoc, List<ClassDoc>> subinterfaces = new HashMap();
/*     */ 
/*  73 */   private List<ClassDoc> baseEnums = new ArrayList();
/*  74 */   private Map<ClassDoc, List<ClassDoc>> subEnums = new HashMap();
/*     */ 
/*  76 */   private List<ClassDoc> baseAnnotationTypes = new ArrayList();
/*  77 */   private Map<ClassDoc, List<ClassDoc>> subAnnotationTypes = new HashMap();
/*     */ 
/*  82 */   private Map<ClassDoc, List<ClassDoc>> implementingclasses = new HashMap();
/*     */ 
/*     */   public ClassTree(Configuration paramConfiguration, boolean paramBoolean)
/*     */   {
/*  92 */     paramConfiguration.message.notice("doclet.Building_Tree", new Object[0]);
/*  93 */     buildTree(paramConfiguration.root.classes(), paramConfiguration);
/*     */   }
/*     */ 
/*     */   public ClassTree(RootDoc paramRootDoc, Configuration paramConfiguration)
/*     */   {
/* 103 */     buildTree(paramRootDoc.classes(), paramConfiguration);
/*     */   }
/*     */ 
/*     */   public ClassTree(ClassDoc[] paramArrayOfClassDoc, Configuration paramConfiguration)
/*     */   {
/* 113 */     buildTree(paramArrayOfClassDoc, paramConfiguration);
/*     */   }
/*     */ 
/*     */   private void buildTree(ClassDoc[] paramArrayOfClassDoc, Configuration paramConfiguration)
/*     */   {
/* 126 */     for (int i = 0; i < paramArrayOfClassDoc.length; i++)
/*     */     {
/* 130 */       if ((!paramConfiguration.nodeprecated) || (
/* 131 */         (!Util.isDeprecated(paramArrayOfClassDoc[i])) && 
/* 132 */         (!Util.isDeprecated(paramArrayOfClassDoc[i]
/* 132 */         .containingPackage()))))
/*     */       {
/* 136 */         if ((!paramConfiguration.javafx) || 
/* 137 */           (paramArrayOfClassDoc[i]
/* 137 */           .tags("treatAsPrivate").length <= 0))
/*     */         {
/* 141 */           if (paramArrayOfClassDoc[i].isEnum()) {
/* 142 */             processType(paramArrayOfClassDoc[i], paramConfiguration, this.baseEnums, this.subEnums);
/* 143 */           } else if (paramArrayOfClassDoc[i].isClass()) {
/* 144 */             processType(paramArrayOfClassDoc[i], paramConfiguration, this.baseclasses, this.subclasses);
/* 145 */           } else if (paramArrayOfClassDoc[i].isInterface()) {
/* 146 */             processInterface(paramArrayOfClassDoc[i]);
/* 147 */             List localList = (List)this.implementingclasses.get(paramArrayOfClassDoc[i]);
/* 148 */             if (localList != null)
/* 149 */               Collections.sort(localList);
/*     */           }
/* 151 */           else if (paramArrayOfClassDoc[i].isAnnotationType()) {
/* 152 */             processType(paramArrayOfClassDoc[i], paramConfiguration, this.baseAnnotationTypes, this.subAnnotationTypes);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 157 */     Collections.sort(this.baseinterfaces);
/* 158 */     for (Iterator localIterator = this.subinterfaces.values().iterator(); localIterator.hasNext(); ) {
/* 159 */       Collections.sort((List)localIterator.next());
/*     */     }
/* 161 */     for (localIterator = this.subclasses.values().iterator(); localIterator.hasNext(); )
/* 162 */       Collections.sort((List)localIterator.next());
/*     */   }
/*     */ 
/*     */   private void processType(ClassDoc paramClassDoc, Configuration paramConfiguration, List<ClassDoc> paramList, Map<ClassDoc, List<ClassDoc>> paramMap)
/*     */   {
/* 180 */     ClassDoc localClassDoc = Util.getFirstVisibleSuperClassCD(paramClassDoc, paramConfiguration);
/* 181 */     if (localClassDoc != null) {
/* 182 */       if (!add(paramMap, localClassDoc, paramClassDoc)) {
/* 183 */         return;
/*     */       }
/* 185 */       processType(localClassDoc, paramConfiguration, paramList, paramMap);
/*     */     }
/* 188 */     else if (!paramList.contains(paramClassDoc)) {
/* 189 */       paramList.add(paramClassDoc);
/*     */     }
/*     */ 
/* 192 */     List localList = Util.getAllInterfaces(paramClassDoc, paramConfiguration);
/* 193 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); )
/* 194 */       add(this.implementingclasses, ((Type)localIterator.next()).asClassDoc(), paramClassDoc);
/*     */   }
/*     */ 
/*     */   private void processInterface(ClassDoc paramClassDoc)
/*     */   {
/* 207 */     ClassDoc[] arrayOfClassDoc = paramClassDoc.interfaces();
/* 208 */     if (arrayOfClassDoc.length > 0) {
/* 209 */       for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 210 */         if (!add(this.subinterfaces, arrayOfClassDoc[i], paramClassDoc)) {
/* 211 */           return;
/*     */         }
/* 213 */         processInterface(arrayOfClassDoc[i]);
/*     */       }
/*     */ 
/*     */     }
/* 219 */     else if (!this.baseinterfaces.contains(paramClassDoc))
/* 220 */       this.baseinterfaces.add(paramClassDoc);
/*     */   }
/*     */ 
/*     */   private boolean add(Map<ClassDoc, List<ClassDoc>> paramMap, ClassDoc paramClassDoc1, ClassDoc paramClassDoc2)
/*     */   {
/* 235 */     Object localObject = (List)paramMap.get(paramClassDoc1);
/* 236 */     if (localObject == null) {
/* 237 */       localObject = new ArrayList();
/* 238 */       paramMap.put(paramClassDoc1, localObject);
/*     */     }
/* 240 */     if (((List)localObject).contains(paramClassDoc2)) {
/* 241 */       return false;
/*     */     }
/* 243 */     ((List)localObject).add(paramClassDoc2);
/*     */ 
/* 245 */     return true;
/*     */   }
/*     */ 
/*     */   private List<ClassDoc> get(Map<ClassDoc, List<ClassDoc>> paramMap, ClassDoc paramClassDoc)
/*     */   {
/* 257 */     List localList = (List)paramMap.get(paramClassDoc);
/* 258 */     if (localList == null) {
/* 259 */       return new ArrayList();
/*     */     }
/* 261 */     return localList;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> subclasses(ClassDoc paramClassDoc)
/*     */   {
/* 270 */     return get(this.subclasses, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> subinterfaces(ClassDoc paramClassDoc)
/*     */   {
/* 279 */     return get(this.subinterfaces, paramClassDoc);
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> implementingclasses(ClassDoc paramClassDoc)
/*     */   {
/* 288 */     List localList1 = get(this.implementingclasses, paramClassDoc);
/* 289 */     List localList2 = allSubs(paramClassDoc, false);
/*     */ 
/* 293 */     ListIterator localListIterator2 = localList2.listIterator();
/*     */ 
/* 295 */     while (localListIterator2.hasNext())
/*     */     {
/* 297 */       ListIterator localListIterator1 = implementingclasses(
/* 297 */         (ClassDoc)localListIterator2
/* 297 */         .next()).listIterator();
/* 298 */       while (localListIterator1.hasNext()) {
/* 299 */         ClassDoc localClassDoc = (ClassDoc)localListIterator1.next();
/* 300 */         if (!localList1.contains(localClassDoc)) {
/* 301 */           localList1.add(localClassDoc);
/*     */         }
/*     */       }
/*     */     }
/* 305 */     Collections.sort(localList1);
/* 306 */     return localList1;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> subs(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 317 */     if (paramBoolean)
/* 318 */       return get(this.subEnums, paramClassDoc);
/* 319 */     if (paramClassDoc.isAnnotationType())
/* 320 */       return get(this.subAnnotationTypes, paramClassDoc);
/* 321 */     if (paramClassDoc.isInterface())
/* 322 */       return get(this.subinterfaces, paramClassDoc);
/* 323 */     if (paramClassDoc.isClass()) {
/* 324 */       return get(this.subclasses, paramClassDoc);
/*     */     }
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> allSubs(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 340 */     List localList1 = subs(paramClassDoc, paramBoolean);
/* 341 */     for (int i = 0; i < localList1.size(); i++) {
/* 342 */       paramClassDoc = (ClassDoc)localList1.get(i);
/* 343 */       List localList2 = subs(paramClassDoc, paramBoolean);
/* 344 */       for (int j = 0; j < localList2.size(); j++) {
/* 345 */         ClassDoc localClassDoc = (ClassDoc)localList2.get(j);
/* 346 */         if (!localList1.contains(localClassDoc)) {
/* 347 */           localList1.add(localClassDoc);
/*     */         }
/*     */       }
/*     */     }
/* 351 */     Collections.sort(localList1);
/* 352 */     return localList1;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> baseclasses()
/*     */   {
/* 361 */     return this.baseclasses;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> baseinterfaces()
/*     */   {
/* 369 */     return this.baseinterfaces;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> baseEnums()
/*     */   {
/* 377 */     return this.baseEnums;
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> baseAnnotationTypes()
/*     */   {
/* 385 */     return this.baseAnnotationTypes;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.ClassTree
 * JD-Core Version:    0.6.2
 */