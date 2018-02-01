/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class VisibleMemberMap
/*     */ {
/*  51 */   private boolean noVisibleMembers = true;
/*     */   public static final int INNERCLASSES = 0;
/*     */   public static final int ENUM_CONSTANTS = 1;
/*     */   public static final int FIELDS = 2;
/*     */   public static final int CONSTRUCTORS = 3;
/*     */   public static final int METHODS = 4;
/*     */   public static final int ANNOTATION_TYPE_FIELDS = 5;
/*     */   public static final int ANNOTATION_TYPE_MEMBER_OPTIONAL = 6;
/*     */   public static final int ANNOTATION_TYPE_MEMBER_REQUIRED = 7;
/*     */   public static final int PROPERTIES = 8;
/*     */   public static final int NUM_MEMBER_TYPES = 9;
/*     */   public static final String STARTLEVEL = "start";
/*  73 */   private final List<ClassDoc> visibleClasses = new ArrayList();
/*     */ 
/*  80 */   private final Map<Object, Map<ProgramElementDoc, String>> memberNameMap = new HashMap();
/*     */ 
/*  85 */   private final Map<ClassDoc, ClassMembers> classMap = new HashMap();
/*     */   private final ClassDoc classdoc;
/*     */   private final int kind;
/*     */   private final Configuration configuration;
/* 103 */   private static final Map<ClassDoc, ProgramElementDoc[]> propertiesCache = new HashMap();
/*     */ 
/* 105 */   private static final Map<ProgramElementDoc, ProgramElementDoc> classPropertiesMap = new HashMap();
/*     */ 
/* 107 */   private static final Map<ProgramElementDoc, GetterSetter> getterSetterMap = new HashMap();
/*     */ 
/*     */   public VisibleMemberMap(ClassDoc paramClassDoc, int paramInt, Configuration paramConfiguration)
/*     */   {
/* 124 */     this.classdoc = paramClassDoc;
/* 125 */     this.kind = paramInt;
/* 126 */     this.configuration = paramConfiguration;
/* 127 */     new ClassMembers(paramClassDoc, "start", null).build();
/*     */   }
/*     */ 
/*     */   public List<ClassDoc> getVisibleClassesList()
/*     */   {
/* 136 */     sort(this.visibleClasses);
/* 137 */     return this.visibleClasses;
/*     */   }
/*     */ 
/*     */   public ProgramElementDoc getPropertyMemberDoc(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 146 */     return (ProgramElementDoc)classPropertiesMap.get(paramProgramElementDoc);
/*     */   }
/*     */ 
/*     */   public ProgramElementDoc getGetterForProperty(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 155 */     return ((GetterSetter)getterSetterMap.get(paramProgramElementDoc)).getGetter();
/*     */   }
/*     */ 
/*     */   public ProgramElementDoc getSetterForProperty(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 164 */     return ((GetterSetter)getterSetterMap.get(paramProgramElementDoc)).getSetter();
/*     */   }
/*     */ 
/*     */   private List<ProgramElementDoc> getInheritedPackagePrivateMethods(Configuration paramConfiguration)
/*     */   {
/* 175 */     ArrayList localArrayList = new ArrayList();
/* 176 */     for (Iterator localIterator = this.visibleClasses.iterator(); localIterator.hasNext(); ) {
/* 177 */       ClassDoc localClassDoc = (ClassDoc)localIterator.next();
/* 178 */       if ((localClassDoc != this.classdoc) && 
/* 179 */         (localClassDoc
/* 179 */         .isPackagePrivate()) && 
/* 180 */         (!Util.isLinkable(localClassDoc, paramConfiguration)))
/*     */       {
/* 183 */         localArrayList.addAll(getMembersFor(localClassDoc));
/*     */       }
/*     */     }
/* 186 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> getLeafClassMembers(Configuration paramConfiguration)
/*     */   {
/* 197 */     List localList = getMembersFor(this.classdoc);
/* 198 */     localList.addAll(getInheritedPackagePrivateMethods(paramConfiguration));
/* 199 */     return localList;
/*     */   }
/*     */ 
/*     */   public List<ProgramElementDoc> getMembersFor(ClassDoc paramClassDoc)
/*     */   {
/* 210 */     ClassMembers localClassMembers = (ClassMembers)this.classMap.get(paramClassDoc);
/* 211 */     if (localClassMembers == null) {
/* 212 */       return new ArrayList();
/*     */     }
/* 214 */     return localClassMembers.getMembers();
/*     */   }
/*     */ 
/*     */   private void sort(List<ClassDoc> paramList)
/*     */   {
/* 222 */     ArrayList localArrayList1 = new ArrayList();
/* 223 */     ArrayList localArrayList2 = new ArrayList();
/* 224 */     for (int i = 0; i < paramList.size(); i++) {
/* 225 */       ClassDoc localClassDoc = (ClassDoc)paramList.get(i);
/* 226 */       if (localClassDoc.isClass())
/* 227 */         localArrayList1.add(localClassDoc);
/*     */       else {
/* 229 */         localArrayList2.add(localClassDoc);
/*     */       }
/*     */     }
/* 232 */     paramList.clear();
/* 233 */     paramList.addAll(localArrayList1);
/* 234 */     paramList.addAll(localArrayList2);
/*     */   }
/*     */ 
/*     */   private void fillMemberLevelMap(List<ProgramElementDoc> paramList, String paramString) {
/* 238 */     for (int i = 0; i < paramList.size(); i++) {
/* 239 */       Object localObject1 = getMemberKey((ProgramElementDoc)paramList.get(i));
/* 240 */       Object localObject2 = (Map)this.memberNameMap.get(localObject1);
/* 241 */       if (localObject2 == null) {
/* 242 */         localObject2 = new HashMap();
/* 243 */         this.memberNameMap.put(localObject1, localObject2);
/*     */       }
/* 245 */       ((Map)localObject2).put(paramList.get(i), paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void purgeMemberLevelMap(List<ProgramElementDoc> paramList, String paramString) {
/* 250 */     for (int i = 0; i < paramList.size(); i++) {
/* 251 */       Object localObject = getMemberKey((ProgramElementDoc)paramList.get(i));
/* 252 */       Map localMap = (Map)this.memberNameMap.get(localObject);
/* 253 */       if (paramString.equals(localMap.get(paramList.get(i))))
/* 254 */         localMap.remove(paramList.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean noVisibleMembers()
/*     */   {
/* 743 */     return this.noVisibleMembers;
/*     */   }
/*     */ 
/*     */   private ClassMember getClassMember(MethodDoc paramMethodDoc) {
/* 747 */     for (Iterator localIterator = this.memberNameMap.keySet().iterator(); localIterator.hasNext(); ) {
/* 748 */       Object localObject = localIterator.next();
/* 749 */       if (!(localObject instanceof String))
/*     */       {
/* 751 */         if (((ClassMember)localObject).isEqual(paramMethodDoc))
/* 752 */           return (ClassMember)localObject;
/*     */       }
/*     */     }
/* 755 */     return new ClassMember(paramMethodDoc);
/*     */   }
/*     */ 
/*     */   private Object getMemberKey(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 762 */     if (paramProgramElementDoc.isConstructor())
/* 763 */       return paramProgramElementDoc.name() + ((ExecutableMemberDoc)paramProgramElementDoc).signature();
/* 764 */     if (paramProgramElementDoc.isMethod())
/* 765 */       return getClassMember((MethodDoc)paramProgramElementDoc);
/* 766 */     if ((paramProgramElementDoc.isField()) || (paramProgramElementDoc.isEnumConstant()) || (paramProgramElementDoc.isAnnotationTypeElement())) {
/* 767 */       return paramProgramElementDoc.name();
/*     */     }
/* 769 */     String str = paramProgramElementDoc.name();
/*     */ 
/* 771 */     str = str.indexOf('.') != 0 ? str.substring(str.lastIndexOf('.'), str.length()) : str;
/* 772 */     return "clint" + str;
/*     */   }
/*     */ 
/*     */   private class ClassMember
/*     */   {
/* 267 */     private Set<ProgramElementDoc> members = new HashSet();
/*     */ 
/*     */     public ClassMember(ProgramElementDoc arg2)
/*     */     {
/*     */       Object localObject;
/* 268 */       this.members.add(localObject);
/*     */     }
/*     */ 
/*     */     public void addMember(ProgramElementDoc paramProgramElementDoc) {
/* 272 */       this.members.add(paramProgramElementDoc);
/*     */     }
/*     */ 
/*     */     public boolean isEqual(MethodDoc paramMethodDoc) {
/* 276 */       for (Iterator localIterator = this.members.iterator(); localIterator.hasNext(); ) {
/* 277 */         MethodDoc localMethodDoc = (MethodDoc)localIterator.next();
/* 278 */         if (Util.executableMembersEqual(paramMethodDoc, localMethodDoc)) {
/* 279 */           this.members.add(paramMethodDoc);
/* 280 */           return true;
/*     */         }
/*     */       }
/* 283 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ClassMembers
/*     */   {
/*     */     private ClassDoc mappingClass;
/* 302 */     private List<ProgramElementDoc> members = new ArrayList();
/*     */     private String level;
/* 665 */     private final Pattern pattern = Pattern.compile("[sg]et\\p{Upper}.*");
/*     */ 
/*     */     public List<ProgramElementDoc> getMembers()
/*     */     {
/* 315 */       return this.members;
/*     */     }
/*     */ 
/*     */     private ClassMembers(ClassDoc paramString, String arg3) {
/* 319 */       this.mappingClass = paramString;
/*     */       Object localObject;
/* 320 */       this.level = localObject;
/* 321 */       if ((VisibleMemberMap.this.classMap.containsKey(paramString)) && 
/* 322 */         (localObject
/* 322 */         .startsWith(((ClassMembers)VisibleMemberMap.this.classMap
/* 322 */         .get(paramString)).level)))
/*     */       {
/* 325 */         VisibleMemberMap.this.purgeMemberLevelMap(getClassMembers(paramString, false), 
/* 326 */           ((ClassMembers)VisibleMemberMap.this.classMap
/* 326 */           .get(paramString)).level);
/* 327 */         VisibleMemberMap.this.classMap.remove(paramString);
/* 328 */         VisibleMemberMap.this.visibleClasses.remove(paramString);
/*     */       }
/* 330 */       if (!VisibleMemberMap.this.classMap.containsKey(paramString)) {
/* 331 */         VisibleMemberMap.this.classMap.put(paramString, this);
/* 332 */         VisibleMemberMap.this.visibleClasses.add(paramString);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void build()
/*     */     {
/* 338 */       if (VisibleMemberMap.this.kind == 3)
/* 339 */         addMembers(this.mappingClass);
/*     */       else
/* 341 */         mapClass();
/*     */     }
/*     */ 
/*     */     private void mapClass()
/*     */     {
/* 346 */       addMembers(this.mappingClass);
/* 347 */       ClassDoc[] arrayOfClassDoc = this.mappingClass.interfaces();
/*     */       Object localObject;
/* 348 */       for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 349 */         localObject = this.level + 1;
/* 350 */         ClassMembers localClassMembers = new ClassMembers(VisibleMemberMap.this, arrayOfClassDoc[i], (String)localObject);
/* 351 */         localClassMembers.mapClass();
/*     */       }
/* 353 */       if (this.mappingClass.isClass()) {
/* 354 */         ClassDoc localClassDoc = this.mappingClass.superclass();
/* 355 */         if ((localClassDoc != null) && (!this.mappingClass.equals(localClassDoc))) {
/* 356 */           localObject = new ClassMembers(VisibleMemberMap.this, localClassDoc, this.level + "c");
/*     */ 
/* 358 */           ((ClassMembers)localObject).mapClass();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void addMembers(ClassDoc paramClassDoc)
/*     */     {
/* 372 */       List localList = getClassMembers(paramClassDoc, true);
/* 373 */       ArrayList localArrayList = new ArrayList();
/* 374 */       for (int i = 0; i < localList.size(); i++) {
/* 375 */         ProgramElementDoc localProgramElementDoc = (ProgramElementDoc)localList.get(i);
/* 376 */         if ((!found(this.members, localProgramElementDoc)) && 
/* 377 */           (memberIsVisible(localProgramElementDoc)) && 
/* 378 */           (!isOverridden(localProgramElementDoc, this.level)) && 
/* 379 */           (!isTreatedAsPrivate(localProgramElementDoc)))
/*     */         {
/* 380 */           localArrayList.add(localProgramElementDoc);
/*     */         }
/*     */       }
/* 383 */       if (localArrayList.size() > 0) {
/* 384 */         VisibleMemberMap.this.noVisibleMembers = false;
/*     */       }
/* 386 */       this.members.addAll(localArrayList);
/* 387 */       VisibleMemberMap.this.fillMemberLevelMap(getClassMembers(paramClassDoc, false), this.level);
/*     */     }
/*     */ 
/*     */     private boolean isTreatedAsPrivate(ProgramElementDoc paramProgramElementDoc) {
/* 391 */       if (!VisibleMemberMap.this.configuration.javafx) {
/* 392 */         return false;
/*     */       }
/*     */ 
/* 395 */       Tag[] arrayOfTag = paramProgramElementDoc.tags("@treatAsPrivate");
/* 396 */       boolean bool = (arrayOfTag != null) && (arrayOfTag.length > 0);
/* 397 */       return bool;
/*     */     }
/*     */ 
/*     */     private boolean memberIsVisible(ProgramElementDoc paramProgramElementDoc)
/*     */     {
/* 407 */       if (paramProgramElementDoc.containingClass().equals(VisibleMemberMap.this.classdoc))
/*     */       {
/* 410 */         return true;
/* 411 */       }if (paramProgramElementDoc.isPrivate())
/*     */       {
/* 414 */         return false;
/* 415 */       }if (paramProgramElementDoc.isPackagePrivate())
/*     */       {
/* 418 */         return paramProgramElementDoc.containingClass().containingPackage().equals(VisibleMemberMap.this.classdoc
/* 419 */           .containingPackage());
/*     */       }
/*     */ 
/* 422 */       return true;
/*     */     }
/*     */ 
/*     */     private List<ProgramElementDoc> getClassMembers(ClassDoc paramClassDoc, boolean paramBoolean)
/*     */     {
/* 430 */       if ((paramClassDoc.isEnum()) && (VisibleMemberMap.this.kind == 3))
/*     */       {
/* 433 */         return Arrays.asList(new ProgramElementDoc[0]);
/*     */       }
/* 435 */       Object localObject = null;
/* 436 */       switch (VisibleMemberMap.this.kind) {
/*     */       case 5:
/* 438 */         localObject = paramClassDoc.fields(paramBoolean);
/* 439 */         break;
/*     */       case 6:
/* 442 */         localObject = paramClassDoc.isAnnotationType() ? 
/* 442 */           filter((AnnotationTypeDoc)paramClassDoc, false) : 
/* 442 */           new AnnotationTypeElementDoc[0];
/*     */ 
/* 444 */         break;
/*     */       case 7:
/* 447 */         localObject = paramClassDoc.isAnnotationType() ? 
/* 447 */           filter((AnnotationTypeDoc)paramClassDoc, true) : 
/* 447 */           new AnnotationTypeElementDoc[0];
/*     */ 
/* 449 */         break;
/*     */       case 0:
/* 451 */         localObject = paramClassDoc.innerClasses(paramBoolean);
/* 452 */         break;
/*     */       case 1:
/* 454 */         localObject = paramClassDoc.enumConstants();
/* 455 */         break;
/*     */       case 2:
/* 457 */         localObject = paramClassDoc.fields(paramBoolean);
/* 458 */         break;
/*     */       case 3:
/* 460 */         localObject = paramClassDoc.constructors();
/* 461 */         break;
/*     */       case 4:
/* 463 */         localObject = paramClassDoc.methods(paramBoolean);
/* 464 */         checkOnPropertiesTags((MethodDoc[])localObject);
/* 465 */         break;
/*     */       case 8:
/* 467 */         localObject = properties(paramClassDoc, paramBoolean);
/* 468 */         break;
/*     */       default:
/* 470 */         localObject = new ProgramElementDoc[0];
/*     */       }
/*     */ 
/* 473 */       if (VisibleMemberMap.this.configuration.nodeprecated) {
/* 474 */         return Util.excludeDeprecatedMembersAsList((ProgramElementDoc[])localObject);
/*     */       }
/* 476 */       return Arrays.asList((Object[])localObject);
/*     */     }
/*     */ 
/*     */     private AnnotationTypeElementDoc[] filter(AnnotationTypeDoc paramAnnotationTypeDoc, boolean paramBoolean)
/*     */     {
/* 492 */       AnnotationTypeElementDoc[] arrayOfAnnotationTypeElementDoc = paramAnnotationTypeDoc.elements();
/* 493 */       ArrayList localArrayList = new ArrayList();
/* 494 */       for (int i = 0; i < arrayOfAnnotationTypeElementDoc.length; i++) {
/* 495 */         if (((paramBoolean) && (arrayOfAnnotationTypeElementDoc[i].defaultValue() == null)) || ((!paramBoolean) && 
/* 496 */           (arrayOfAnnotationTypeElementDoc[i]
/* 496 */           .defaultValue() != null))) {
/* 497 */           localArrayList.add(arrayOfAnnotationTypeElementDoc[i]);
/*     */         }
/*     */       }
/* 500 */       return (AnnotationTypeElementDoc[])localArrayList.toArray(new AnnotationTypeElementDoc[0]);
/*     */     }
/*     */ 
/*     */     private boolean found(List<ProgramElementDoc> paramList, ProgramElementDoc paramProgramElementDoc) {
/* 504 */       for (int i = 0; i < paramList.size(); i++) {
/* 505 */         ProgramElementDoc localProgramElementDoc = (ProgramElementDoc)paramList.get(i);
/* 506 */         if (Util.matches(localProgramElementDoc, paramProgramElementDoc)) {
/* 507 */           return true;
/*     */         }
/*     */       }
/* 510 */       return false;
/*     */     }
/*     */ 
/*     */     private boolean isOverridden(ProgramElementDoc paramProgramElementDoc, String paramString)
/*     */     {
/* 520 */       Map localMap = (Map)VisibleMemberMap.this.memberNameMap.get(VisibleMemberMap.this.getMemberKey(paramProgramElementDoc));
/* 521 */       if (localMap == null)
/* 522 */         return false;
/* 523 */       String str = null;
/* 524 */       Iterator localIterator = localMap.values().iterator();
/* 525 */       while (localIterator.hasNext()) {
/* 526 */         str = (String)localIterator.next();
/* 527 */         if ((str.equals("start")) || (
/* 528 */           (paramString
/* 528 */           .startsWith(str)) && 
/* 529 */           (!paramString
/* 529 */           .equals(str))))
/*     */         {
/* 530 */           return true;
/*     */         }
/*     */       }
/* 533 */       return false;
/*     */     }
/*     */ 
/*     */     private ProgramElementDoc[] properties(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 537 */       MethodDoc[] arrayOfMethodDoc = paramClassDoc.methods(paramBoolean);
/* 538 */       FieldDoc[] arrayOfFieldDoc = paramClassDoc.fields(false);
/*     */ 
/* 540 */       if (VisibleMemberMap.propertiesCache.containsKey(paramClassDoc)) {
/* 541 */         return (ProgramElementDoc[])VisibleMemberMap.propertiesCache.get(paramClassDoc);
/*     */       }
/*     */ 
/* 544 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 546 */       for (MethodDoc localMethodDoc1 : arrayOfMethodDoc)
/*     */       {
/* 548 */         if (isPropertyMethod(localMethodDoc1))
/*     */         {
/* 552 */           MethodDoc localMethodDoc2 = getterForField(arrayOfMethodDoc, localMethodDoc1);
/* 553 */           MethodDoc localMethodDoc3 = setterForField(arrayOfMethodDoc, localMethodDoc1);
/* 554 */           FieldDoc localFieldDoc = fieldForProperty(arrayOfFieldDoc, localMethodDoc1);
/*     */ 
/* 556 */           addToPropertiesMap(localMethodDoc3, localMethodDoc2, localMethodDoc1, localFieldDoc);
/* 557 */           VisibleMemberMap.getterSetterMap.put(localMethodDoc1, new VisibleMemberMap.GetterSetter(VisibleMemberMap.this, localMethodDoc2, localMethodDoc3));
/* 558 */           localArrayList.add(localMethodDoc1);
/*     */         }
/*     */       }
/* 561 */       ??? = (ProgramElementDoc[])localArrayList
/* 561 */         .toArray(new ProgramElementDoc[localArrayList
/* 561 */         .size()]);
/* 562 */       VisibleMemberMap.propertiesCache.put(paramClassDoc, ???);
/* 563 */       return ???;
/*     */     }
/*     */ 
/*     */     private void addToPropertiesMap(MethodDoc paramMethodDoc1, MethodDoc paramMethodDoc2, MethodDoc paramMethodDoc3, FieldDoc paramFieldDoc)
/*     */     {
/* 570 */       if ((paramFieldDoc == null) || 
/* 571 */         (paramFieldDoc
/* 571 */         .getRawCommentText() == null) || 
/* 572 */         (paramFieldDoc
/* 572 */         .getRawCommentText().length() == 0)) {
/* 573 */         addToPropertiesMap(paramMethodDoc1, paramMethodDoc3);
/* 574 */         addToPropertiesMap(paramMethodDoc2, paramMethodDoc3);
/* 575 */         addToPropertiesMap(paramMethodDoc3, paramMethodDoc3);
/*     */       } else {
/* 577 */         addToPropertiesMap(paramMethodDoc2, paramFieldDoc);
/* 578 */         addToPropertiesMap(paramMethodDoc1, paramFieldDoc);
/* 579 */         addToPropertiesMap(paramMethodDoc3, paramFieldDoc);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void addToPropertiesMap(ProgramElementDoc paramProgramElementDoc1, ProgramElementDoc paramProgramElementDoc2)
/*     */     {
/* 585 */       if ((null == paramProgramElementDoc1) || (null == paramProgramElementDoc2)) {
/* 586 */         return;
/*     */       }
/* 588 */       String str = paramProgramElementDoc1.getRawCommentText();
/*     */ 
/* 594 */       if ((null == str) || (0 == str.length()) || 
/* 595 */         (paramProgramElementDoc1
/* 595 */         .equals(paramProgramElementDoc2)))
/*     */       {
/* 596 */         VisibleMemberMap.classPropertiesMap.put(paramProgramElementDoc1, paramProgramElementDoc2);
/*     */       }
/*     */     }
/*     */ 
/*     */     private MethodDoc getterForField(MethodDoc[] paramArrayOfMethodDoc, MethodDoc paramMethodDoc)
/*     */     {
/* 602 */       String str1 = paramMethodDoc.name();
/*     */ 
/* 604 */       String str2 = str1
/* 604 */         .substring(0, str1
/* 605 */         .lastIndexOf("Property"));
/*     */ 
/* 608 */       String str3 = "" + 
/* 607 */         Character.toUpperCase(str2
/* 607 */         .charAt(0)) + 
/* 607 */         str2
/* 608 */         .substring(1);
/*     */ 
/* 610 */       String str5 = paramMethodDoc.returnType().toString();
/*     */       String str4;
/* 611 */       if (("boolean".equals(str5)) || 
/* 612 */         (str5
/* 612 */         .endsWith("BooleanProperty")))
/*     */       {
/* 613 */         str4 = "(is|get)" + str3;
/*     */       }
/* 615 */       else str4 = "get" + str3;
/*     */ 
/* 618 */       for (MethodDoc localMethodDoc : paramArrayOfMethodDoc) {
/* 619 */         if ((Pattern.matches(str4, localMethodDoc.name())) && 
/* 620 */           (0 == localMethodDoc.parameters().length) && (
/* 621 */           (localMethodDoc
/* 621 */           .isPublic()) || (localMethodDoc.isProtected()))) {
/* 622 */           return localMethodDoc;
/*     */         }
/*     */       }
/*     */ 
/* 626 */       return null;
/*     */     }
/*     */ 
/*     */     private MethodDoc setterForField(MethodDoc[] paramArrayOfMethodDoc, MethodDoc paramMethodDoc)
/*     */     {
/* 631 */       String str1 = paramMethodDoc.name();
/*     */ 
/* 633 */       String str2 = str1
/* 633 */         .substring(0, str1
/* 634 */         .lastIndexOf("Property"));
/*     */ 
/* 637 */       String str3 = "" + 
/* 636 */         Character.toUpperCase(str2
/* 636 */         .charAt(0)) + 
/* 636 */         str2
/* 637 */         .substring(1);
/*     */ 
/* 638 */       String str4 = "set" + str3;
/*     */ 
/* 640 */       for (MethodDoc localMethodDoc : paramArrayOfMethodDoc) {
/* 641 */         if ((str4.equals(localMethodDoc.name())) && 
/* 642 */           (1 == localMethodDoc.parameters().length) && 
/* 643 */           ("void"
/* 643 */           .equals(localMethodDoc
/* 643 */           .returnType().simpleTypeName())) && (
/* 644 */           (localMethodDoc
/* 644 */           .isPublic()) || (localMethodDoc.isProtected()))) {
/* 645 */           return localMethodDoc;
/*     */         }
/*     */       }
/*     */ 
/* 649 */       return null;
/*     */     }
/*     */ 
/*     */     private FieldDoc fieldForProperty(FieldDoc[] paramArrayOfFieldDoc, MethodDoc paramMethodDoc)
/*     */     {
/* 654 */       for (FieldDoc localFieldDoc : paramArrayOfFieldDoc) {
/* 655 */         String str1 = localFieldDoc.name();
/* 656 */         String str2 = str1 + "Property";
/* 657 */         if (str2.equals(paramMethodDoc.name())) {
/* 658 */           return localFieldDoc;
/*     */         }
/*     */       }
/* 661 */       return null;
/*     */     }
/*     */ 
/*     */     private boolean isPropertyMethod(MethodDoc paramMethodDoc)
/*     */     {
/* 667 */       if (!paramMethodDoc.name().endsWith("Property")) {
/* 668 */         return false;
/*     */       }
/*     */ 
/* 671 */       if (!memberIsVisible(paramMethodDoc)) {
/* 672 */         return false;
/*     */       }
/*     */ 
/* 675 */       if (this.pattern.matcher(paramMethodDoc.name()).matches()) {
/* 676 */         return false;
/*     */       }
/*     */ 
/* 680 */       return (0 == paramMethodDoc.parameters().length) && 
/* 680 */         (!"void"
/* 680 */         .equals(paramMethodDoc
/* 680 */         .returnType().simpleTypeName()));
/*     */     }
/*     */ 
/*     */     private void checkOnPropertiesTags(MethodDoc[] paramArrayOfMethodDoc) {
/* 684 */       for (MethodDoc localMethodDoc : paramArrayOfMethodDoc)
/* 685 */         if (localMethodDoc.isIncluded())
/* 686 */           for (Tag localTag : localMethodDoc.tags()) {
/* 687 */             String str = localTag.name();
/* 688 */             if ((str.equals("@propertySetter")) || 
/* 689 */               (str
/* 689 */               .equals("@propertyGetter")) || 
/* 690 */               (str
/* 690 */               .equals("@propertyDescription")))
/*     */             {
/* 691 */               if (isPropertyGetterOrSetter(paramArrayOfMethodDoc, localMethodDoc)) break;
/* 692 */               VisibleMemberMap.this.configuration.message.warning(localTag.position(), "doclet.javafx_tag_misuse", new Object[0]); break;
/*     */             }
/*     */           }
/*     */     }
/*     */ 
/*     */     private boolean isPropertyGetterOrSetter(MethodDoc[] paramArrayOfMethodDoc, MethodDoc paramMethodDoc)
/*     */     {
/* 704 */       boolean bool = false;
/* 705 */       String str1 = Util.propertyNameFromMethodName(VisibleMemberMap.this.configuration, paramMethodDoc.name());
/* 706 */       if (!str1.isEmpty()) {
/* 707 */         String str2 = str1 + "Property";
/* 708 */         for (MethodDoc localMethodDoc : paramArrayOfMethodDoc) {
/* 709 */           if (localMethodDoc.name().equals(str2)) {
/* 710 */             bool = true;
/* 711 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 715 */       return bool;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class GetterSetter {
/*     */     private final ProgramElementDoc getter;
/*     */     private final ProgramElementDoc setter;
/*     */ 
/* 724 */     public GetterSetter(ProgramElementDoc paramProgramElementDoc1, ProgramElementDoc arg3) { this.getter = paramProgramElementDoc1;
/*     */       Object localObject;
/* 725 */       this.setter = localObject; }
/*     */ 
/*     */     public ProgramElementDoc getGetter()
/*     */     {
/* 729 */       return this.getter;
/*     */     }
/*     */ 
/*     */     public ProgramElementDoc getSetter() {
/* 733 */       return this.setter;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap
 * JD-Core Version:    0.6.2
 */