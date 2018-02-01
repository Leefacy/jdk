/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationDesc.ElementValuePair;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   public static ProgramElementDoc[] excludeDeprecatedMembers(ProgramElementDoc[] paramArrayOfProgramElementDoc)
/*     */   {
/*  63 */     return toProgramElementDocArray(excludeDeprecatedMembersAsList(paramArrayOfProgramElementDoc));
/*     */   }
/*     */ 
/*     */   public static List<ProgramElementDoc> excludeDeprecatedMembersAsList(ProgramElementDoc[] paramArrayOfProgramElementDoc)
/*     */   {
/*  77 */     ArrayList localArrayList = new ArrayList();
/*  78 */     for (int i = 0; i < paramArrayOfProgramElementDoc.length; i++) {
/*  79 */       if (paramArrayOfProgramElementDoc[i].tags("deprecated").length == 0) {
/*  80 */         localArrayList.add(paramArrayOfProgramElementDoc[i]);
/*     */       }
/*     */     }
/*  83 */     Collections.sort(localArrayList);
/*  84 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static ProgramElementDoc[] toProgramElementDocArray(List<ProgramElementDoc> paramList)
/*     */   {
/*  91 */     ProgramElementDoc[] arrayOfProgramElementDoc = new ProgramElementDoc[paramList.size()];
/*  92 */     for (int i = 0; i < paramList.size(); i++) {
/*  93 */       arrayOfProgramElementDoc[i] = ((ProgramElementDoc)paramList.get(i));
/*     */     }
/*  95 */     return arrayOfProgramElementDoc;
/*     */   }
/*     */ 
/*     */   public static boolean nonPublicMemberFound(ProgramElementDoc[] paramArrayOfProgramElementDoc)
/*     */   {
/* 105 */     for (int i = 0; i < paramArrayOfProgramElementDoc.length; i++) {
/* 106 */       if (!paramArrayOfProgramElementDoc[i].isPublic()) {
/* 107 */         return true;
/*     */       }
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public static MethodDoc findMethod(ClassDoc paramClassDoc, MethodDoc paramMethodDoc)
/*     */   {
/* 121 */     MethodDoc[] arrayOfMethodDoc = paramClassDoc.methods();
/* 122 */     for (int i = 0; i < arrayOfMethodDoc.length; i++) {
/* 123 */       if (executableMembersEqual(paramMethodDoc, arrayOfMethodDoc[i])) {
/* 124 */         return arrayOfMethodDoc[i];
/*     */       }
/*     */     }
/*     */ 
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean executableMembersEqual(ExecutableMemberDoc paramExecutableMemberDoc1, ExecutableMemberDoc paramExecutableMemberDoc2)
/*     */   {
/* 138 */     if ((!(paramExecutableMemberDoc1 instanceof MethodDoc)) || (!(paramExecutableMemberDoc2 instanceof MethodDoc))) {
/* 139 */       return false;
/*     */     }
/* 141 */     MethodDoc localMethodDoc1 = (MethodDoc)paramExecutableMemberDoc1;
/* 142 */     MethodDoc localMethodDoc2 = (MethodDoc)paramExecutableMemberDoc2;
/* 143 */     if ((localMethodDoc1.isStatic()) && (localMethodDoc2.isStatic())) {
/* 144 */       Parameter[] arrayOfParameter1 = localMethodDoc1.parameters();
/*     */       Parameter[] arrayOfParameter2;
/* 146 */       if ((localMethodDoc1.name().equals(localMethodDoc2.name())) && 
/* 147 */         ((arrayOfParameter2 = localMethodDoc2
/* 147 */         .parameters()).length == arrayOfParameter1.length))
/*     */       {
/* 150 */         for (int i = 0; (i < arrayOfParameter1.length) && (
/* 151 */           (arrayOfParameter1[i].typeName().equals(arrayOfParameter2[i]
/* 152 */           .typeName())) || 
/* 153 */           ((arrayOfParameter2[i]
/* 153 */           .type() instanceof TypeVariable)) || 
/* 154 */           ((arrayOfParameter1[i]
/* 154 */           .type() instanceof TypeVariable))); i++);
/* 158 */         if (i == arrayOfParameter1.length) {
/* 159 */           return true;
/*     */         }
/*     */       }
/* 162 */       return false;
/*     */     }
/*     */ 
/* 165 */     return (localMethodDoc1.overrides(localMethodDoc2)) || 
/* 165 */       (localMethodDoc2
/* 165 */       .overrides(localMethodDoc1)) || 
/* 165 */       (paramExecutableMemberDoc1 == paramExecutableMemberDoc2);
/*     */   }
/*     */ 
/*     */   public static boolean isCoreClass(ClassDoc paramClassDoc)
/*     */   {
/* 176 */     return (paramClassDoc.containingClass() == null) || (paramClassDoc.isStatic());
/*     */   }
/*     */ 
/*     */   public static boolean matches(ProgramElementDoc paramProgramElementDoc1, ProgramElementDoc paramProgramElementDoc2)
/*     */   {
/* 181 */     if (((paramProgramElementDoc1 instanceof ExecutableMemberDoc)) && ((paramProgramElementDoc2 instanceof ExecutableMemberDoc)))
/*     */     {
/* 183 */       ExecutableMemberDoc localExecutableMemberDoc1 = (ExecutableMemberDoc)paramProgramElementDoc1;
/* 184 */       ExecutableMemberDoc localExecutableMemberDoc2 = (ExecutableMemberDoc)paramProgramElementDoc2;
/* 185 */       return executableMembersEqual(localExecutableMemberDoc1, localExecutableMemberDoc2);
/*     */     }
/* 187 */     return paramProgramElementDoc1.name().equals(paramProgramElementDoc2.name());
/*     */   }
/*     */ 
/*     */   public static void copyDocFiles(Configuration paramConfiguration, PackageDoc paramPackageDoc)
/*     */   {
/* 205 */     copyDocFiles(paramConfiguration, DocPath.forPackage(paramPackageDoc).resolve(DocPaths.DOC_FILES));
/*     */   }
/*     */ 
/*     */   public static void copyDocFiles(Configuration paramConfiguration, DocPath paramDocPath) {
/*     */     try {
/* 210 */       i = 1;
/* 211 */       for (DocFile localDocFile1 : DocFile.list(paramConfiguration, StandardLocation.SOURCE_PATH, paramDocPath))
/* 212 */         if (localDocFile1.isDirectory())
/*     */         {
/* 215 */           DocFile localDocFile2 = localDocFile1;
/* 216 */           DocFile localDocFile3 = DocFile.createFileForOutput(paramConfiguration, paramDocPath);
/* 217 */           if (!localDocFile2.isSameFile(localDocFile3))
/*     */           {
/* 221 */             for (DocFile localDocFile4 : localDocFile2.list()) {
/* 222 */               DocFile localDocFile5 = localDocFile3.resolve(localDocFile4.getName());
/* 223 */               if (localDocFile4.isFile()) {
/* 224 */                 if ((localDocFile5.exists()) && (i == 0)) {
/* 225 */                   paramConfiguration.message.warning((SourcePosition)null, "doclet.Copy_Overwrite_warning", new Object[] { localDocFile4
/* 227 */                     .getPath(), localDocFile3.getPath() });
/*     */                 } else {
/* 229 */                   paramConfiguration.message.notice("doclet.Copying_File_0_To_Dir_1", new Object[] { localDocFile4
/* 231 */                     .getPath(), localDocFile3.getPath() });
/* 232 */                   localDocFile5.copyFile(localDocFile4);
/*     */                 }
/* 234 */               } else if ((localDocFile4.isDirectory()) && 
/* 235 */                 (paramConfiguration.copydocfilesubdirs) && 
/* 236 */                 (!paramConfiguration
/* 236 */                 .shouldExcludeDocFileDir(localDocFile4
/* 236 */                 .getName()))) {
/* 237 */                 copyDocFiles(paramConfiguration, paramDocPath.resolve(localDocFile4.getName()));
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 242 */             i = 0;
/*     */           }
/*     */         }
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*     */       int i;
/* 245 */       throw new DocletAbortException(localSecurityException);
/*     */     } catch (IOException localIOException) {
/* 247 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static List<Type> getAllInterfaces(Type paramType, Configuration paramConfiguration, boolean paramBoolean)
/*     */   {
/* 276 */     LinkedHashMap localLinkedHashMap = paramBoolean ? new TreeMap() : new LinkedHashMap();
/* 277 */     Type[] arrayOfType = null;
/* 278 */     Type localType1 = null;
/* 279 */     if ((paramType instanceof ParameterizedType)) {
/* 280 */       arrayOfType = ((ParameterizedType)paramType).interfaceTypes();
/* 281 */       localType1 = ((ParameterizedType)paramType).superclassType();
/* 282 */     } else if ((paramType instanceof ClassDoc)) {
/* 283 */       arrayOfType = ((ClassDoc)paramType).interfaceTypes();
/* 284 */       localType1 = ((ClassDoc)paramType).superclassType();
/*     */     } else {
/* 286 */       arrayOfType = paramType.asClassDoc().interfaceTypes();
/* 287 */       localType1 = paramType.asClassDoc().superclassType();
/*     */     }
/*     */     Iterator localIterator;
/* 290 */     for (int i = 0; i < arrayOfType.length; i++) {
/* 291 */       Type localType2 = arrayOfType[i];
/* 292 */       ClassDoc localClassDoc = localType2.asClassDoc();
/* 293 */       if ((localClassDoc.isPublic()) || (paramConfiguration == null) || 
/* 295 */         (isLinkable(localClassDoc, paramConfiguration)))
/*     */       {
/* 298 */         localLinkedHashMap.put(localClassDoc, localType2);
/* 299 */         List localList = getAllInterfaces(localType2, paramConfiguration, paramBoolean);
/* 300 */         for (localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 301 */           Type localType3 = (Type)localIterator.next();
/* 302 */           localLinkedHashMap.put(localType3.asClassDoc(), localType3);
/*     */         }
/*     */       }
/*     */     }
/* 305 */     if (localType1 == null) {
/* 306 */       return new ArrayList(localLinkedHashMap.values());
/*     */     }
/* 308 */     addAllInterfaceTypes(localLinkedHashMap, localType1, 
/* 310 */       interfaceTypesOf(localType1), 
/* 310 */       false, paramConfiguration);
/*     */ 
/* 312 */     ArrayList localArrayList = new ArrayList(localLinkedHashMap.values());
/* 313 */     if (paramBoolean) {
/* 314 */       Collections.sort(localArrayList, new TypeComparator(null));
/*     */     }
/* 316 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static Type[] interfaceTypesOf(Type paramType) {
/* 320 */     if ((paramType instanceof AnnotatedType)) {
/* 321 */       paramType = ((AnnotatedType)paramType).underlyingType();
/*     */     }
/*     */ 
/* 324 */     return (paramType instanceof ClassDoc) ? ((ClassDoc)paramType)
/* 323 */       .interfaceTypes() : ((ParameterizedType)paramType)
/* 324 */       .interfaceTypes();
/*     */   }
/*     */ 
/*     */   public static List<Type> getAllInterfaces(Type paramType, Configuration paramConfiguration) {
/* 328 */     return getAllInterfaces(paramType, paramConfiguration, true);
/*     */   }
/*     */ 
/*     */   private static void findAllInterfaceTypes(Map<ClassDoc, Type> paramMap, ClassDoc paramClassDoc, boolean paramBoolean, Configuration paramConfiguration)
/*     */   {
/* 333 */     Type localType = paramClassDoc.superclassType();
/* 334 */     if (localType == null)
/* 335 */       return;
/* 336 */     addAllInterfaceTypes(paramMap, localType, 
/* 337 */       interfaceTypesOf(localType), 
/* 337 */       paramBoolean, paramConfiguration);
/*     */   }
/*     */ 
/*     */   private static void findAllInterfaceTypes(Map<ClassDoc, Type> paramMap, ParameterizedType paramParameterizedType, Configuration paramConfiguration)
/*     */   {
/* 343 */     Type localType = paramParameterizedType.superclassType();
/* 344 */     if (localType == null)
/* 345 */       return;
/* 346 */     addAllInterfaceTypes(paramMap, localType, 
/* 347 */       interfaceTypesOf(localType), 
/* 347 */       false, paramConfiguration);
/*     */   }
/*     */ 
/*     */   private static void addAllInterfaceTypes(Map<ClassDoc, Type> paramMap, Type paramType, Type[] paramArrayOfType, boolean paramBoolean, Configuration paramConfiguration)
/*     */   {
/*     */     Iterator localIterator;
/* 354 */     for (int i = 0; i < paramArrayOfType.length; i++) {
/* 355 */       Object localObject = paramArrayOfType[i];
/* 356 */       ClassDoc localClassDoc = ((Type)localObject).asClassDoc();
/* 357 */       if (!localClassDoc.isPublic()) { if (paramConfiguration != null)
/*     */         {
/* 359 */           if (!isLinkable(localClassDoc, paramConfiguration));
/*     */         }
/*     */       } else {
/* 362 */         if (paramBoolean)
/* 363 */           localObject = ((Type)localObject).asClassDoc();
/* 364 */         paramMap.put(localClassDoc, localObject);
/* 365 */         List localList = getAllInterfaces((Type)localObject, paramConfiguration);
/* 366 */         for (localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 367 */           Type localType = (Type)localIterator.next();
/* 368 */           paramMap.put(localType.asClassDoc(), localType);
/*     */         }
/*     */       }
/*     */     }
/* 371 */     if ((paramType instanceof AnnotatedType)) {
/* 372 */       paramType = ((AnnotatedType)paramType).underlyingType();
/*     */     }
/* 374 */     if ((paramType instanceof ParameterizedType))
/* 375 */       findAllInterfaceTypes(paramMap, (ParameterizedType)paramType, paramConfiguration);
/* 376 */     else if (((ClassDoc)paramType).typeParameters().length == 0)
/* 377 */       findAllInterfaceTypes(paramMap, (ClassDoc)paramType, paramBoolean, paramConfiguration);
/*     */     else
/* 379 */       findAllInterfaceTypes(paramMap, (ClassDoc)paramType, true, paramConfiguration);
/*     */   }
/*     */ 
/*     */   public static String quote(String paramString)
/*     */   {
/* 386 */     return "\"" + paramString + "\"";
/*     */   }
/*     */ 
/*     */   public static String getPackageName(PackageDoc paramPackageDoc)
/*     */   {
/* 396 */     return (paramPackageDoc == null) || (paramPackageDoc.name().length() == 0) ? "<Unnamed>" : paramPackageDoc
/* 396 */       .name();
/*     */   }
/*     */ 
/*     */   public static String getPackageFileHeadName(PackageDoc paramPackageDoc)
/*     */   {
/* 406 */     return (paramPackageDoc == null) || (paramPackageDoc.name().length() == 0) ? "default" : paramPackageDoc
/* 406 */       .name();
/*     */   }
/*     */ 
/*     */   public static String replaceText(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 417 */     if ((paramString2 == null) || (paramString3 == null) || (paramString2.equals(paramString3))) {
/* 418 */       return paramString1;
/*     */     }
/* 420 */     return paramString1.replace(paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   public static boolean isDocumentedAnnotation(AnnotationTypeDoc paramAnnotationTypeDoc)
/*     */   {
/* 432 */     AnnotationDesc[] arrayOfAnnotationDesc = paramAnnotationTypeDoc.annotations();
/* 433 */     for (int i = 0; i < arrayOfAnnotationDesc.length; i++) {
/* 434 */       if (arrayOfAnnotationDesc[i].annotationType().qualifiedName().equals(Documented.class
/* 435 */         .getName())) {
/* 436 */         return true;
/*     */       }
/*     */     }
/* 439 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isDeclarationTarget(AnnotationDesc paramAnnotationDesc)
/*     */   {
/* 444 */     AnnotationDesc.ElementValuePair[] arrayOfElementValuePair = paramAnnotationDesc.elementValues();
/* 445 */     if ((arrayOfElementValuePair == null) || (arrayOfElementValuePair.length != 1) || 
/* 447 */       (!"value"
/* 447 */       .equals(arrayOfElementValuePair[0]
/* 447 */       .element().name())) || 
/* 448 */       (!(arrayOfElementValuePair[0]
/* 448 */       .value().value() instanceof AnnotationValue[]))) {
/* 449 */       return true;
/*     */     }
/* 451 */     AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])arrayOfElementValuePair[0].value().value();
/* 452 */     for (int i = 0; i < arrayOfAnnotationValue.length; i++) {
/* 453 */       Object localObject = arrayOfAnnotationValue[i].value();
/* 454 */       if (!(localObject instanceof FieldDoc)) {
/* 455 */         return true;
/*     */       }
/* 457 */       FieldDoc localFieldDoc = (FieldDoc)localObject;
/* 458 */       if (isJava5DeclarationElementType(localFieldDoc)) {
/* 459 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isDeclarationAnnotation(AnnotationTypeDoc paramAnnotationTypeDoc, boolean paramBoolean)
/*     */   {
/* 477 */     if (!paramBoolean)
/* 478 */       return false;
/* 479 */     AnnotationDesc[] arrayOfAnnotationDesc = paramAnnotationTypeDoc.annotations();
/*     */ 
/* 481 */     if (arrayOfAnnotationDesc.length == 0)
/* 482 */       return true;
/* 483 */     for (int i = 0; i < arrayOfAnnotationDesc.length; i++) {
/* 484 */       if (arrayOfAnnotationDesc[i].annotationType().qualifiedName().equals(Target.class
/* 485 */         .getName())) {
/* 486 */         if (isDeclarationTarget(arrayOfAnnotationDesc[i]))
/* 487 */           return true;
/*     */       }
/*     */     }
/* 490 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isLinkable(ClassDoc paramClassDoc, Configuration paramConfiguration)
/*     */   {
/* 511 */     return ((paramClassDoc
/* 509 */       .isIncluded()) && (paramConfiguration.isGeneratedDoc(paramClassDoc))) || (
/* 510 */       (paramConfiguration.extern
/* 510 */       .isExternal(paramClassDoc)) && 
/* 510 */       (
/* 511 */       (paramClassDoc
/* 511 */       .isPublic()) || (paramClassDoc.isProtected())));
/*     */   }
/*     */ 
/*     */   public static Type getFirstVisibleSuperClass(ClassDoc paramClassDoc, Configuration paramConfiguration)
/*     */   {
/* 524 */     if (paramClassDoc == null) {
/* 525 */       return null;
/*     */     }
/* 527 */     Type localType = paramClassDoc.superclassType();
/* 528 */     ClassDoc localClassDoc = paramClassDoc.superclass();
/* 529 */     while ((localType != null) && 
/* 530 */       (!localClassDoc
/* 530 */       .isPublic()) && 
/* 531 */       (!isLinkable(localClassDoc, paramConfiguration)) && 
/* 532 */       (!localClassDoc.superclass().qualifiedName().equals(localClassDoc.qualifiedName())))
/*     */     {
/* 534 */       localType = localClassDoc.superclassType();
/* 535 */       localClassDoc = localClassDoc.superclass();
/*     */     }
/* 537 */     if (paramClassDoc.equals(localClassDoc)) {
/* 538 */       return null;
/*     */     }
/* 540 */     return localType;
/*     */   }
/*     */ 
/*     */   public static ClassDoc getFirstVisibleSuperClassCD(ClassDoc paramClassDoc, Configuration paramConfiguration)
/*     */   {
/* 553 */     if (paramClassDoc == null) {
/* 554 */       return null;
/*     */     }
/* 556 */     ClassDoc localClassDoc = paramClassDoc.superclass();
/* 557 */     while ((localClassDoc != null) && 
/* 558 */       (!localClassDoc
/* 558 */       .isPublic()) && 
/* 559 */       (!isLinkable(localClassDoc, paramConfiguration)))
/*     */     {
/* 560 */       localClassDoc = localClassDoc.superclass();
/*     */     }
/* 562 */     if (paramClassDoc.equals(localClassDoc)) {
/* 563 */       return null;
/*     */     }
/* 565 */     return localClassDoc;
/*     */   }
/*     */ 
/*     */   public static String getTypeName(Configuration paramConfiguration, ClassDoc paramClassDoc, boolean paramBoolean)
/*     */   {
/* 578 */     String str = "";
/* 579 */     if (paramClassDoc.isOrdinaryClass())
/* 580 */       str = "doclet.Class";
/* 581 */     else if (paramClassDoc.isInterface())
/* 582 */       str = "doclet.Interface";
/* 583 */     else if (paramClassDoc.isException())
/* 584 */       str = "doclet.Exception";
/* 585 */     else if (paramClassDoc.isError())
/* 586 */       str = "doclet.Error";
/* 587 */     else if (paramClassDoc.isAnnotationType())
/* 588 */       str = "doclet.AnnotationType";
/* 589 */     else if (paramClassDoc.isEnum()) {
/* 590 */       str = "doclet.Enum";
/*     */     }
/* 592 */     return paramConfiguration.getText(paramBoolean ? 
/* 593 */       StringUtils.toLowerCase(str) : 
/* 593 */       str);
/*     */   }
/*     */ 
/*     */   public static String replaceTabs(Configuration paramConfiguration, String paramString)
/*     */   {
/* 605 */     if (paramString.indexOf("\t") == -1) {
/* 606 */       return paramString;
/*     */     }
/* 608 */     int i = paramConfiguration.sourcetab;
/* 609 */     String str = paramConfiguration.tabSpaces;
/* 610 */     int j = paramString.length();
/* 611 */     StringBuilder localStringBuilder = new StringBuilder(j);
/* 612 */     int k = 0;
/* 613 */     int m = 0;
/* 614 */     for (int n = 0; n < j; n++) {
/* 615 */       int i1 = paramString.charAt(n);
/* 616 */       switch (i1) { case 10:
/*     */       case 13:
/* 618 */         m = 0;
/* 619 */         break;
/*     */       case 9:
/* 621 */         localStringBuilder.append(paramString, k, n);
/* 622 */         int i2 = i - m % i;
/* 623 */         localStringBuilder.append(str, 0, i2);
/* 624 */         m += i2;
/* 625 */         k = n + 1;
/* 626 */         break;
/*     */       case 11:
/*     */       case 12:
/*     */       default:
/* 628 */         m++;
/*     */       }
/*     */     }
/* 631 */     localStringBuilder.append(paramString, k, j);
/* 632 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static String normalizeNewlines(String paramString) {
/* 636 */     StringBuilder localStringBuilder = new StringBuilder();
/* 637 */     int i = paramString.length();
/* 638 */     String str = DocletConstants.NL;
/* 639 */     int j = 0;
/* 640 */     for (int k = 0; k < i; k++) {
/* 641 */       int m = paramString.charAt(k);
/* 642 */       switch (m) {
/*     */       case 10:
/* 644 */         localStringBuilder.append(paramString, j, k);
/* 645 */         localStringBuilder.append(str);
/* 646 */         j = k + 1;
/* 647 */         break;
/*     */       case 13:
/* 649 */         localStringBuilder.append(paramString, j, k);
/* 650 */         localStringBuilder.append(str);
/* 651 */         if ((k + 1 < i) && (paramString.charAt(k + 1) == '\n'))
/* 652 */           k++;
/* 653 */         j = k + 1;
/*     */       }
/*     */     }
/*     */ 
/* 657 */     localStringBuilder.append(paramString, j, i);
/* 658 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static void setEnumDocumentation(Configuration paramConfiguration, ClassDoc paramClassDoc)
/*     */   {
/* 667 */     MethodDoc[] arrayOfMethodDoc = paramClassDoc.methods();
/* 668 */     for (int i = 0; i < arrayOfMethodDoc.length; i++) {
/* 669 */       MethodDoc localMethodDoc = arrayOfMethodDoc[i];
/*     */       Object localObject;
/* 670 */       if ((localMethodDoc.name().equals("values")) && 
/* 671 */         (localMethodDoc
/* 671 */         .parameters().length == 0)) {
/* 672 */         localObject = new StringBuilder();
/* 673 */         ((StringBuilder)localObject).append(paramConfiguration.getText("doclet.enum_values_doc.main", paramClassDoc.name()));
/* 674 */         ((StringBuilder)localObject).append("\n@return ");
/* 675 */         ((StringBuilder)localObject).append(paramConfiguration.getText("doclet.enum_values_doc.return"));
/* 676 */         localMethodDoc.setRawCommentText(((StringBuilder)localObject).toString());
/* 677 */       } else if ((localMethodDoc.name().equals("valueOf")) && 
/* 678 */         (localMethodDoc
/* 678 */         .parameters().length == 1)) {
/* 679 */         localObject = localMethodDoc.parameters()[0].type();
/* 680 */         if ((localObject != null) && 
/* 681 */           (((Type)localObject)
/* 681 */           .qualifiedTypeName().equals(String.class.getName()))) {
/* 682 */           StringBuilder localStringBuilder = new StringBuilder();
/* 683 */           localStringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.main", paramClassDoc.name()));
/* 684 */           localStringBuilder.append("\n@param name ");
/* 685 */           localStringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.param_name"));
/* 686 */           localStringBuilder.append("\n@return ");
/* 687 */           localStringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.return"));
/* 688 */           localStringBuilder.append("\n@throws IllegalArgumentException ");
/* 689 */           localStringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.throws_ila"));
/* 690 */           localStringBuilder.append("\n@throws NullPointerException ");
/* 691 */           localStringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.throws_npe"));
/* 692 */           localMethodDoc.setRawCommentText(localStringBuilder.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isDeprecated(Doc paramDoc)
/*     */   {
/* 705 */     if (paramDoc.tags("deprecated").length > 0)
/* 706 */       return true;
/*     */     AnnotationDesc[] arrayOfAnnotationDesc;
/* 709 */     if ((paramDoc instanceof PackageDoc))
/* 710 */       arrayOfAnnotationDesc = ((PackageDoc)paramDoc).annotations();
/*     */     else
/* 712 */       arrayOfAnnotationDesc = ((ProgramElementDoc)paramDoc).annotations();
/* 713 */     for (int i = 0; i < arrayOfAnnotationDesc.length; i++) {
/* 714 */       if (arrayOfAnnotationDesc[i].annotationType().qualifiedName().equals(Deprecated.class
/* 715 */         .getName())) {
/* 716 */         return true;
/*     */       }
/*     */     }
/* 719 */     return false;
/*     */   }
/*     */ 
/*     */   public static String propertyNameFromMethodName(Configuration paramConfiguration, String paramString)
/*     */   {
/* 729 */     String str = null;
/* 730 */     if ((paramString.startsWith("get")) || (paramString.startsWith("set")))
/* 731 */       str = paramString.substring(3);
/* 732 */     else if (paramString.startsWith("is")) {
/* 733 */       str = paramString.substring(2);
/*     */     }
/* 735 */     if ((str == null) || (str.isEmpty())) {
/* 736 */       return "";
/*     */     }
/*     */ 
/* 739 */     return str.substring(0, 1).toLowerCase(paramConfiguration.getLocale()) + str
/* 739 */       .substring(1);
/*     */   }
/*     */ 
/*     */   public static ClassDoc[] filterOutPrivateClasses(ClassDoc[] paramArrayOfClassDoc, boolean paramBoolean)
/*     */   {
/* 753 */     if (!paramBoolean) {
/* 754 */       return paramArrayOfClassDoc;
/*     */     }
/* 756 */     ArrayList localArrayList = new ArrayList(paramArrayOfClassDoc.length);
/*     */ 
/* 758 */     for (ClassDoc localClassDoc : paramArrayOfClassDoc)
/* 759 */       if ((!localClassDoc.isPrivate()) && (!localClassDoc.isPackagePrivate()))
/*     */       {
/* 762 */         Tag[] arrayOfTag = localClassDoc.tags("treatAsPrivate");
/* 763 */         if ((arrayOfTag == null) || (arrayOfTag.length <= 0))
/*     */         {
/* 766 */           localArrayList.add(localClassDoc);
/*     */         }
/*     */       }
/* 769 */     return (ClassDoc[])localArrayList.toArray(new ClassDoc[0]);
/*     */   }
/*     */ 
/*     */   public static boolean isJava5DeclarationElementType(FieldDoc paramFieldDoc)
/*     */   {
/* 790 */     return (paramFieldDoc.name().contentEquals(ElementType.ANNOTATION_TYPE.name())) || 
/* 784 */       (paramFieldDoc
/* 784 */       .name().contentEquals(ElementType.CONSTRUCTOR.name())) || 
/* 785 */       (paramFieldDoc
/* 785 */       .name().contentEquals(ElementType.FIELD.name())) || 
/* 786 */       (paramFieldDoc
/* 786 */       .name().contentEquals(ElementType.LOCAL_VARIABLE.name())) || 
/* 787 */       (paramFieldDoc
/* 787 */       .name().contentEquals(ElementType.METHOD.name())) || 
/* 788 */       (paramFieldDoc
/* 788 */       .name().contentEquals(ElementType.PACKAGE.name())) || 
/* 789 */       (paramFieldDoc
/* 789 */       .name().contentEquals(ElementType.PARAMETER.name())) || 
/* 790 */       (paramFieldDoc
/* 790 */       .name().contentEquals(ElementType.TYPE.name()));
/*     */   }
/*     */ 
/*     */   private static class TypeComparator
/*     */     implements Comparator<Type>
/*     */   {
/*     */     public int compare(Type paramType1, Type paramType2)
/*     */     {
/* 257 */       return paramType1.qualifiedTypeName().compareToIgnoreCase(paramType2
/* 258 */         .qualifiedTypeName());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.Util
 * JD-Core Version:    0.6.2
 */