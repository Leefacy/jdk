/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class ClassUseMapper
/*     */ {
/*     */   private final ClassTree classtree;
/*  51 */   public Map<String, Set<PackageDoc>> classToPackage = new HashMap();
/*     */ 
/*  56 */   public Map<String, List<PackageDoc>> classToPackageAnnotations = new HashMap();
/*     */ 
/*  62 */   public Map<String, Set<ClassDoc>> classToClass = new HashMap();
/*     */ 
/*  69 */   public Map<String, List<ClassDoc>> classToSubclass = new HashMap();
/*     */ 
/*  76 */   public Map<String, List<ClassDoc>> classToSubinterface = new HashMap();
/*     */ 
/*  83 */   public Map<String, List<ClassDoc>> classToImplementingClass = new HashMap();
/*     */ 
/*  89 */   public Map<String, List<FieldDoc>> classToField = new HashMap();
/*     */ 
/*  95 */   public Map<String, List<MethodDoc>> classToMethodReturn = new HashMap();
/*     */ 
/* 102 */   public Map<String, List<ExecutableMemberDoc>> classToMethodArgs = new HashMap();
/*     */ 
/* 108 */   public Map<String, List<ExecutableMemberDoc>> classToMethodThrows = new HashMap();
/*     */ 
/* 115 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorArgs = new HashMap();
/*     */ 
/* 121 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorThrows = new HashMap();
/*     */ 
/* 126 */   public Map<String, List<ConstructorDoc>> classToConstructorAnnotations = new HashMap();
/*     */ 
/* 131 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorParamAnnotation = new HashMap();
/*     */ 
/* 136 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorDocArgTypeParam = new HashMap();
/*     */ 
/* 141 */   public Map<String, List<ClassDoc>> classToClassTypeParam = new HashMap();
/*     */ 
/* 146 */   public Map<String, List<ClassDoc>> classToClassAnnotations = new HashMap();
/*     */ 
/* 151 */   public Map<String, List<MethodDoc>> classToExecMemberDocTypeParam = new HashMap();
/*     */ 
/* 156 */   public Map<String, List<ExecutableMemberDoc>> classToExecMemberDocArgTypeParam = new HashMap();
/*     */ 
/* 161 */   public Map<String, List<MethodDoc>> classToExecMemberDocAnnotations = new HashMap();
/*     */ 
/* 167 */   public Map<String, List<MethodDoc>> classToExecMemberDocReturnTypeParam = new HashMap();
/*     */ 
/* 172 */   public Map<String, List<ExecutableMemberDoc>> classToExecMemberDocParamAnnotation = new HashMap();
/*     */ 
/* 177 */   public Map<String, List<FieldDoc>> classToFieldDocTypeParam = new HashMap();
/*     */ 
/* 182 */   public Map<String, List<FieldDoc>> annotationToFieldDoc = new HashMap();
/*     */ 
/*     */   public ClassUseMapper(RootDoc paramRootDoc, ClassTree paramClassTree)
/*     */   {
/* 186 */     this.classtree = paramClassTree;
/*     */ 
/* 189 */     for (Object localObject1 = paramClassTree.baseclasses().iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 190 */       subclasses((ClassDoc)((Iterator)localObject1).next());
/*     */     }
/* 192 */     for (localObject1 = paramClassTree.baseinterfaces().iterator(); ((Iterator)localObject1).hasNext(); )
/*     */     {
/* 194 */       implementingClasses((ClassDoc)((Iterator)localObject1).next());
/*     */     }
/*     */ 
/* 197 */     localObject1 = paramRootDoc.classes();
/* 198 */     for (int i = 0; i < localObject1.length; i++) {
/* 199 */       PackageDoc localPackageDoc = localObject1[i].containingPackage();
/* 200 */       mapAnnotations(this.classToPackageAnnotations, localPackageDoc, localPackageDoc);
/* 201 */       Object localObject2 = localObject1[i];
/* 202 */       mapTypeParameters(this.classToClassTypeParam, localObject2, localObject2);
/* 203 */       mapAnnotations(this.classToClassAnnotations, localObject2, localObject2);
/* 204 */       FieldDoc[] arrayOfFieldDoc = localObject2.fields();
/* 205 */       for (int j = 0; j < arrayOfFieldDoc.length; j++) {
/* 206 */         FieldDoc localFieldDoc = arrayOfFieldDoc[j];
/* 207 */         mapTypeParameters(this.classToFieldDocTypeParam, localFieldDoc, localFieldDoc);
/* 208 */         mapAnnotations(this.annotationToFieldDoc, localFieldDoc, localFieldDoc);
/* 209 */         if (!localFieldDoc.type().isPrimitive()) {
/* 210 */           add(this.classToField, localFieldDoc.type().asClassDoc(), localFieldDoc);
/*     */         }
/*     */       }
/* 213 */       ConstructorDoc[] arrayOfConstructorDoc = localObject2.constructors();
/* 214 */       for (int k = 0; k < arrayOfConstructorDoc.length; k++) {
/* 215 */         mapAnnotations(this.classToConstructorAnnotations, arrayOfConstructorDoc[k], arrayOfConstructorDoc[k]);
/* 216 */         mapExecutable(arrayOfConstructorDoc[k]);
/*     */       }
/* 218 */       MethodDoc[] arrayOfMethodDoc = localObject2.methods();
/* 219 */       for (int m = 0; m < arrayOfMethodDoc.length; m++) {
/* 220 */         MethodDoc localMethodDoc = arrayOfMethodDoc[m];
/* 221 */         mapExecutable(localMethodDoc);
/* 222 */         mapTypeParameters(this.classToExecMemberDocTypeParam, localMethodDoc, localMethodDoc);
/* 223 */         mapAnnotations(this.classToExecMemberDocAnnotations, localMethodDoc, localMethodDoc);
/* 224 */         if ((!localMethodDoc.returnType().isPrimitive()) && (!(localMethodDoc.returnType() instanceof TypeVariable))) {
/* 225 */           mapTypeParameters(this.classToExecMemberDocReturnTypeParam, localMethodDoc
/* 226 */             .returnType(), localMethodDoc);
/* 227 */           add(this.classToMethodReturn, localMethodDoc.returnType().asClassDoc(), localMethodDoc);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Collection<ClassDoc> subclasses(ClassDoc paramClassDoc)
/*     */   {
/* 237 */     Object localObject = (Collection)this.classToSubclass.get(paramClassDoc.qualifiedName());
/* 238 */     if (localObject == null) {
/* 239 */       localObject = new TreeSet();
/* 240 */       List localList = this.classtree.subclasses(paramClassDoc);
/*     */       Iterator localIterator;
/* 241 */       if (localList != null) {
/* 242 */         ((Collection)localObject).addAll(localList);
/* 243 */         for (localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 244 */           ((Collection)localObject).addAll(subclasses((ClassDoc)localIterator.next()));
/*     */         }
/*     */       }
/* 247 */       addAll(this.classToSubclass, paramClassDoc, (Collection)localObject);
/*     */     }
/* 249 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Collection<ClassDoc> subinterfaces(ClassDoc paramClassDoc)
/*     */   {
/* 256 */     Object localObject = (Collection)this.classToSubinterface.get(paramClassDoc.qualifiedName());
/* 257 */     if (localObject == null) {
/* 258 */       localObject = new TreeSet();
/* 259 */       List localList = this.classtree.subinterfaces(paramClassDoc);
/*     */       Iterator localIterator;
/* 260 */       if (localList != null) {
/* 261 */         ((Collection)localObject).addAll(localList);
/* 262 */         for (localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 263 */           ((Collection)localObject).addAll(subinterfaces((ClassDoc)localIterator.next()));
/*     */         }
/*     */       }
/* 266 */       addAll(this.classToSubinterface, paramClassDoc, (Collection)localObject);
/*     */     }
/* 268 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Collection<ClassDoc> implementingClasses(ClassDoc paramClassDoc)
/*     */   {
/* 278 */     Object localObject = (Collection)this.classToImplementingClass.get(paramClassDoc.qualifiedName());
/* 279 */     if (localObject == null) {
/* 280 */       localObject = new TreeSet();
/* 281 */       List localList = this.classtree.implementingclasses(paramClassDoc);
/* 282 */       if (localList != null) {
/* 283 */         ((Collection)localObject).addAll(localList);
/* 284 */         for (localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 285 */           ((Collection)localObject).addAll(subclasses((ClassDoc)localIterator.next()));
/*     */         }
/*     */       }
/* 288 */       for (Iterator localIterator = subinterfaces(paramClassDoc).iterator(); localIterator.hasNext(); ) {
/* 289 */         ((Collection)localObject).addAll(implementingClasses((ClassDoc)localIterator.next()));
/*     */       }
/* 291 */       addAll(this.classToImplementingClass, paramClassDoc, (Collection)localObject);
/*     */     }
/* 293 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void mapExecutable(ExecutableMemberDoc paramExecutableMemberDoc)
/*     */   {
/* 301 */     Parameter[] arrayOfParameter = paramExecutableMemberDoc.parameters();
/* 302 */     boolean bool = paramExecutableMemberDoc.isConstructor();
/* 303 */     ArrayList localArrayList = new ArrayList();
/* 304 */     for (int i = 0; i < arrayOfParameter.length; i++) {
/* 305 */       Type localType = arrayOfParameter[i].type();
/*     */ 
/* 307 */       if ((!arrayOfParameter[i].type().isPrimitive()) && 
/* 308 */         (!localArrayList
/* 308 */         .contains(localType)) && 
/* 308 */         (!(localType instanceof TypeVariable)))
/*     */       {
/* 310 */         add(bool ? this.classToConstructorArgs : this.classToMethodArgs, localType
/* 311 */           .asClassDoc(), paramExecutableMemberDoc);
/* 312 */         localArrayList.add(localType);
/* 313 */         mapTypeParameters(bool ? this.classToConstructorDocArgTypeParam : this.classToExecMemberDocArgTypeParam, localType, paramExecutableMemberDoc);
/*     */       }
/*     */ 
/* 317 */       mapAnnotations(bool ? this.classToConstructorParamAnnotation : this.classToExecMemberDocParamAnnotation, arrayOfParameter[i], paramExecutableMemberDoc);
/*     */     }
/*     */ 
/* 323 */     ClassDoc[] arrayOfClassDoc = paramExecutableMemberDoc.thrownExceptions();
/* 324 */     for (int j = 0; j < arrayOfClassDoc.length; j++)
/* 325 */       add(bool ? this.classToConstructorThrows : this.classToMethodThrows, arrayOfClassDoc[j], paramExecutableMemberDoc);
/*     */   }
/*     */ 
/*     */   private <T> List<T> refList(Map<String, List<T>> paramMap, ClassDoc paramClassDoc)
/*     */   {
/* 331 */     Object localObject = (List)paramMap.get(paramClassDoc.qualifiedName());
/* 332 */     if (localObject == null) {
/* 333 */       ArrayList localArrayList = new ArrayList();
/* 334 */       localObject = localArrayList;
/* 335 */       paramMap.put(paramClassDoc.qualifiedName(), localObject);
/*     */     }
/* 337 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Set<PackageDoc> packageSet(ClassDoc paramClassDoc) {
/* 341 */     Object localObject = (Set)this.classToPackage.get(paramClassDoc.qualifiedName());
/* 342 */     if (localObject == null) {
/* 343 */       localObject = new TreeSet();
/* 344 */       this.classToPackage.put(paramClassDoc.qualifiedName(), localObject);
/*     */     }
/* 346 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Set<ClassDoc> classSet(ClassDoc paramClassDoc) {
/* 350 */     Object localObject = (Set)this.classToClass.get(paramClassDoc.qualifiedName());
/* 351 */     if (localObject == null) {
/* 352 */       TreeSet localTreeSet = new TreeSet();
/* 353 */       localObject = localTreeSet;
/* 354 */       this.classToClass.put(paramClassDoc.qualifiedName(), localObject);
/*     */     }
/* 356 */     return localObject;
/*     */   }
/*     */ 
/*     */   private <T extends ProgramElementDoc> void add(Map<String, List<T>> paramMap, ClassDoc paramClassDoc, T paramT)
/*     */   {
/* 361 */     refList(paramMap, paramClassDoc).add(paramT);
/*     */ 
/* 364 */     packageSet(paramClassDoc).add(paramT.containingPackage());
/*     */ 
/* 366 */     classSet(paramClassDoc).add((paramT instanceof MemberDoc) ? ((MemberDoc)paramT)
/* 367 */       .containingClass() : (ClassDoc)paramT);
/*     */   }
/*     */ 
/*     */   private void addAll(Map<String, List<ClassDoc>> paramMap, ClassDoc paramClassDoc, Collection<ClassDoc> paramCollection)
/*     */   {
/* 372 */     if (paramCollection == null) {
/* 373 */       return;
/*     */     }
/*     */ 
/* 376 */     refList(paramMap, paramClassDoc).addAll(paramCollection);
/*     */ 
/* 378 */     Set localSet1 = packageSet(paramClassDoc);
/* 379 */     Set localSet2 = classSet(paramClassDoc);
/*     */ 
/* 381 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) {
/* 382 */       ClassDoc localClassDoc = (ClassDoc)localIterator.next();
/* 383 */       localSet1.add(localClassDoc.containingPackage());
/* 384 */       localSet2.add(localClassDoc);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T extends ProgramElementDoc> void mapTypeParameters(Map<String, List<T>> paramMap, Object paramObject, T paramT)
/*     */   {
/*     */     TypeVariable[] arrayOfTypeVariable;
/*     */     int m;
/* 400 */     if ((paramObject instanceof ClassDoc)) {
/* 401 */       arrayOfTypeVariable = ((ClassDoc)paramObject).typeParameters();
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject;
/* 402 */       if ((paramObject instanceof WildcardType)) {
/* 403 */         localObject = ((WildcardType)paramObject).extendsBounds();
/* 404 */         for (int j = 0; j < localObject.length; j++) {
/* 405 */           addTypeParameterToMap(paramMap, localObject[j], paramT);
/*     */         }
/* 407 */         Type[] arrayOfType1 = ((WildcardType)paramObject).superBounds();
/* 408 */         for (m = 0; m < arrayOfType1.length; m++) {
/* 409 */           addTypeParameterToMap(paramMap, arrayOfType1[m], paramT);
/*     */         }
/* 411 */         return;
/* 412 */       }if ((paramObject instanceof ParameterizedType)) {
/* 413 */         localObject = ((ParameterizedType)paramObject).typeArguments();
/* 414 */         for (int k = 0; k < localObject.length; k++) {
/* 415 */           addTypeParameterToMap(paramMap, localObject[k], paramT);
/*     */         }
/* 417 */         return;
/* 418 */       }if ((paramObject instanceof ExecutableMemberDoc)) {
/* 419 */         arrayOfTypeVariable = ((ExecutableMemberDoc)paramObject).typeParameters(); } else {
/* 420 */         if ((paramObject instanceof FieldDoc)) {
/* 421 */           localObject = ((FieldDoc)paramObject).type();
/* 422 */           mapTypeParameters(paramMap, localObject, paramT);
/* 423 */           return;
/*     */         }
/* 425 */         return;
/*     */       }
/*     */     }
/* 427 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 428 */       Type[] arrayOfType2 = arrayOfTypeVariable[i].bounds();
/* 429 */       for (m = 0; m < arrayOfType2.length; m++)
/* 430 */         addTypeParameterToMap(paramMap, arrayOfType2[m], paramT);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T extends ProgramElementDoc> void mapAnnotations(Map<String, List<T>> paramMap, Object paramObject, T paramT)
/*     */   {
/* 446 */     int i = 0;
/*     */     AnnotationDesc[] arrayOfAnnotationDesc;
/* 447 */     if ((paramObject instanceof ProgramElementDoc)) {
/* 448 */       arrayOfAnnotationDesc = ((ProgramElementDoc)paramObject).annotations();
/* 449 */     } else if ((paramObject instanceof PackageDoc)) {
/* 450 */       arrayOfAnnotationDesc = ((PackageDoc)paramObject).annotations();
/* 451 */       i = 1;
/* 452 */     } else if ((paramObject instanceof Parameter)) {
/* 453 */       arrayOfAnnotationDesc = ((Parameter)paramObject).annotations();
/*     */     } else {
/* 455 */       throw new DocletAbortException("should not happen");
/*     */     }
/* 457 */     for (int j = 0; j < arrayOfAnnotationDesc.length; j++) {
/* 458 */       AnnotationTypeDoc localAnnotationTypeDoc = arrayOfAnnotationDesc[j].annotationType();
/* 459 */       if (i != 0)
/* 460 */         refList(paramMap, localAnnotationTypeDoc).add(paramT);
/*     */       else
/* 462 */         add(paramMap, localAnnotationTypeDoc, paramT);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T extends PackageDoc> void mapAnnotations(Map<String, List<T>> paramMap, PackageDoc paramPackageDoc, T paramT)
/*     */   {
/* 478 */     AnnotationDesc[] arrayOfAnnotationDesc = paramPackageDoc.annotations();
/* 479 */     for (int i = 0; i < arrayOfAnnotationDesc.length; i++) {
/* 480 */       AnnotationTypeDoc localAnnotationTypeDoc = arrayOfAnnotationDesc[i].annotationType();
/* 481 */       refList(paramMap, localAnnotationTypeDoc).add(paramT);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T extends ProgramElementDoc> void addTypeParameterToMap(Map<String, List<T>> paramMap, Type paramType, T paramT)
/*     */   {
/* 487 */     if ((paramType instanceof ClassDoc))
/* 488 */       add(paramMap, (ClassDoc)paramType, paramT);
/* 489 */     else if ((paramType instanceof ParameterizedType)) {
/* 490 */       add(paramMap, ((ParameterizedType)paramType).asClassDoc(), paramT);
/*     */     }
/* 492 */     mapTypeParameters(paramMap, paramType, paramT);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper
 * JD-Core Version:    0.6.2
 */