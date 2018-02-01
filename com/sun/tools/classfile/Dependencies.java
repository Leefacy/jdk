/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class Dependencies
/*     */ {
/*     */   private Dependency.Filter filter;
/*     */   private Dependency.Finder finder;
/*     */ 
/*     */   public static Dependency.Finder getDefaultFinder()
/*     */   {
/* 124 */     return new APIDependencyFinder(2);
/*     */   }
/*     */ 
/*     */   public static Dependency.Finder getAPIFinder(int paramInt)
/*     */   {
/* 141 */     return new APIDependencyFinder(paramInt);
/*     */   }
/*     */ 
/*     */   public static Dependency.Finder getClassDependencyFinder()
/*     */   {
/* 150 */     return new ClassDependencyFinder();
/*     */   }
/*     */ 
/*     */   public Dependency.Finder getFinder()
/*     */   {
/* 158 */     if (this.finder == null)
/* 159 */       this.finder = getDefaultFinder();
/* 160 */     return this.finder;
/*     */   }
/*     */ 
/*     */   public void setFinder(Dependency.Finder paramFinder)
/*     */   {
/* 168 */     paramFinder.getClass();
/* 169 */     this.finder = paramFinder;
/*     */   }
/*     */ 
/*     */   public static Dependency.Filter getDefaultFilter()
/*     */   {
/* 179 */     return DefaultFilter.instance();
/*     */   }
/*     */ 
/*     */   public static Dependency.Filter getRegexFilter(Pattern paramPattern)
/*     */   {
/* 189 */     return new TargetRegexFilter(paramPattern);
/*     */   }
/*     */ 
/*     */   public static Dependency.Filter getPackageFilter(Set<String> paramSet, boolean paramBoolean)
/*     */   {
/* 202 */     return new TargetPackageFilter(paramSet, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Dependency.Filter getFilter()
/*     */   {
/* 212 */     if (this.filter == null)
/* 213 */       this.filter = getDefaultFilter();
/* 214 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public void setFilter(Dependency.Filter paramFilter)
/*     */   {
/* 223 */     paramFilter.getClass();
/* 224 */     this.filter = paramFilter;
/*     */   }
/*     */ 
/*     */   public Set<Dependency> findAllDependencies(ClassFileReader paramClassFileReader, Set<String> paramSet, boolean paramBoolean)
/*     */     throws Dependencies.ClassFileNotFoundException
/*     */   {
/* 248 */     final HashSet localHashSet = new HashSet();
/* 249 */     Recorder local1 = new Recorder() {
/*     */       public void addDependency(Dependency paramAnonymousDependency) {
/* 251 */         localHashSet.add(paramAnonymousDependency);
/*     */       }
/*     */     };
/* 254 */     findAllDependencies(paramClassFileReader, paramSet, paramBoolean, local1);
/* 255 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public void findAllDependencies(ClassFileReader paramClassFileReader, Set<String> paramSet, boolean paramBoolean, Recorder paramRecorder)
/*     */     throws Dependencies.ClassFileNotFoundException
/*     */   {
/* 279 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 281 */     getFinder();
/* 282 */     getFilter();
/*     */ 
/* 287 */     LinkedList localLinkedList = new LinkedList(paramSet);
/*     */     String str1;
/* 290 */     while ((str1 = (String)localLinkedList.poll()) != null) {
/* 291 */       assert (!localHashSet.contains(str1));
/* 292 */       localHashSet.add(str1);
/*     */ 
/* 294 */       ClassFile localClassFile = paramClassFileReader.getClassFile(str1);
/*     */ 
/* 298 */       for (Dependency localDependency : this.finder.findDependencies(localClassFile)) {
/* 299 */         paramRecorder.addDependency(localDependency);
/* 300 */         if ((paramBoolean) && (this.filter.accepts(localDependency))) {
/* 301 */           String str2 = localDependency.getTarget().getClassName();
/* 302 */           if (!localHashSet.contains(str2))
/* 303 */             localLinkedList.add(str2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class APIDependencyFinder extends Dependencies.BasicDependencyFinder
/*     */   {
/*     */     private int showAccess;
/*     */ 
/*     */     APIDependencyFinder(int paramInt)
/*     */     {
/* 504 */       switch (paramInt) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 4:
/* 509 */         this.showAccess = paramInt;
/* 510 */         break;
/*     */       case 3:
/*     */       default:
/* 513 */         throw new IllegalArgumentException("invalid access 0x" + 
/* 513 */           Integer.toHexString(paramInt));
/*     */       }
/*     */     }
/*     */ 
/*     */     public Iterable<? extends Dependency> findDependencies(ClassFile paramClassFile)
/*     */     {
/*     */       try {
/* 519 */         Dependencies.BasicDependencyFinder.Visitor localVisitor = new Dependencies.BasicDependencyFinder.Visitor(this, paramClassFile);
/* 520 */         localVisitor.addClass(paramClassFile.super_class);
/* 521 */         localVisitor.addClasses(paramClassFile.interfaces);
/*     */         Object localObject2;
/* 523 */         for (localObject2 : paramClassFile.fields) {
/* 524 */           if (checkAccess(localObject2.access_flags))
/* 525 */             localVisitor.scan(localObject2.descriptor, localObject2.attributes);
/*     */         }
/* 527 */         for (localObject2 : paramClassFile.methods) {
/* 528 */           if (checkAccess(localObject2.access_flags)) {
/* 529 */             localVisitor.scan(localObject2.descriptor, localObject2.attributes);
/*     */ 
/* 531 */             Exceptions_attribute localExceptions_attribute = (Exceptions_attribute)localObject2.attributes
/* 531 */               .get("Exceptions");
/*     */ 
/* 532 */             if (localExceptions_attribute != null)
/* 533 */               localVisitor.addClasses(localExceptions_attribute.exception_index_table);
/*     */           }
/*     */         }
/* 536 */         return localVisitor.deps;
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 538 */         throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     boolean checkAccess(AccessFlags paramAccessFlags)
/*     */     {
/* 544 */       boolean bool1 = paramAccessFlags.is(1);
/* 545 */       boolean bool2 = paramAccessFlags.is(4);
/* 546 */       boolean bool3 = paramAccessFlags.is(2);
/* 547 */       int i = (!bool1) && (!bool2) && (!bool3) ? 1 : 0;
/*     */ 
/* 549 */       if ((this.showAccess == 1) && ((bool2) || (bool3) || (i != 0)))
/* 550 */         return false;
/* 551 */       if ((this.showAccess == 4) && ((bool3) || (i != 0)))
/* 552 */         return false;
/* 553 */       if ((this.showAccess == 0) && (bool3)) {
/* 554 */         return false;
/*     */       }
/* 556 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class BasicDependencyFinder
/*     */     implements Dependency.Finder
/*     */   {
/* 563 */     private Map<String, Dependency.Location> locations = new HashMap();
/*     */ 
/*     */     Dependency.Location getLocation(String paramString) {
/* 566 */       Object localObject = (Dependency.Location)this.locations.get(paramString);
/* 567 */       if (localObject == null)
/* 568 */         this.locations.put(paramString, localObject = new Dependencies.SimpleLocation(paramString));
/* 569 */       return localObject;
/*     */     }
/*     */ 
/*     */     class Visitor
/*     */       implements ConstantPool.Visitor<Void, Void>, Type.Visitor<Void, Void>
/*     */     {
/*     */       private ConstantPool constant_pool;
/*     */       private Dependency.Location origin;
/*     */       Set<Dependency> deps;
/*     */ 
/*     */       Visitor(ClassFile arg2)
/*     */       {
/*     */         try
/*     */         {
/*     */           Object localObject;
/* 579 */           this.constant_pool = localObject.constant_pool;
/* 580 */           this.origin = Dependencies.BasicDependencyFinder.this.getLocation(localObject.getName());
/* 581 */           this.deps = new HashSet();
/*     */         } catch (ConstantPoolException localConstantPoolException) {
/* 583 */           throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */         }
/*     */       }
/*     */ 
/*     */       void scan(Descriptor paramDescriptor, Attributes paramAttributes) {
/*     */         try {
/* 589 */           scan(new Signature(paramDescriptor.index).getType(this.constant_pool));
/* 590 */           scan(paramAttributes);
/*     */         } catch (ConstantPoolException localConstantPoolException) {
/* 592 */           throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */         }
/*     */       }
/*     */ 
/*     */       void scan(ConstantPool.CPInfo paramCPInfo) {
/* 597 */         paramCPInfo.accept(this, null);
/*     */       }
/*     */ 
/*     */       void scan(Type paramType) {
/* 601 */         paramType.accept(this, null);
/*     */       }
/*     */ 
/*     */       void scan(Attributes paramAttributes) {
/*     */         try {
/* 606 */           Signature_attribute localSignature_attribute = (Signature_attribute)paramAttributes.get("Signature");
/* 607 */           if (localSignature_attribute != null) {
/* 608 */             scan(localSignature_attribute.getParsedSignature().getType(this.constant_pool));
/*     */           }
/* 610 */           scan(
/* 611 */             (RuntimeVisibleAnnotations_attribute)paramAttributes
/* 611 */             .get("RuntimeVisibleAnnotations"));
/*     */ 
/* 612 */           scan(
/* 613 */             (RuntimeVisibleParameterAnnotations_attribute)paramAttributes
/* 613 */             .get("RuntimeVisibleParameterAnnotations"));
/*     */         }
/*     */         catch (ConstantPoolException localConstantPoolException) {
/* 615 */           throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */         }
/*     */       }
/*     */ 
/*     */       private void scan(RuntimeAnnotations_attribute paramRuntimeAnnotations_attribute) throws ConstantPoolException {
/* 620 */         if (paramRuntimeAnnotations_attribute == null) {
/* 621 */           return;
/*     */         }
/* 623 */         for (int i = 0; i < paramRuntimeAnnotations_attribute.annotations.length; i++) {
/* 624 */           int j = paramRuntimeAnnotations_attribute.annotations[i].type_index;
/* 625 */           scan(new Signature(j).getType(this.constant_pool));
/*     */         }
/*     */       }
/*     */ 
/*     */       private void scan(RuntimeParameterAnnotations_attribute paramRuntimeParameterAnnotations_attribute) throws ConstantPoolException {
/* 630 */         if (paramRuntimeParameterAnnotations_attribute == null) {
/* 631 */           return;
/*     */         }
/* 633 */         for (int i = 0; i < paramRuntimeParameterAnnotations_attribute.parameter_annotations.length; i++)
/* 634 */           for (int j = 0; j < paramRuntimeParameterAnnotations_attribute.parameter_annotations[i].length; j++) {
/* 635 */             int k = paramRuntimeParameterAnnotations_attribute.parameter_annotations[i][j].type_index;
/* 636 */             scan(new Signature(k).getType(this.constant_pool));
/*     */           }
/*     */       }
/*     */ 
/*     */       void addClass(int paramInt) throws ConstantPoolException
/*     */       {
/* 642 */         if (paramInt != 0) {
/* 643 */           String str = this.constant_pool.getClassInfo(paramInt).getBaseName();
/* 644 */           if (str != null)
/* 645 */             addDependency(str);
/*     */         }
/*     */       }
/*     */ 
/*     */       void addClasses(int[] paramArrayOfInt) throws ConstantPoolException {
/* 650 */         for (int k : paramArrayOfInt)
/* 651 */           addClass(k);
/*     */       }
/*     */ 
/*     */       private void addDependency(String paramString) {
/* 655 */         this.deps.add(new Dependencies.SimpleDependency(this.origin, Dependencies.BasicDependencyFinder.this.getLocation(paramString)));
/*     */       }
/*     */ 
/*     */       public Void visitClass(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info, Void paramVoid)
/*     */       {
/*     */         try
/*     */         {
/* 662 */           if (paramCONSTANT_Class_info.getName().startsWith("["))
/* 663 */             new Signature(paramCONSTANT_Class_info.name_index).getType(this.constant_pool).accept(this, null);
/*     */           else
/* 665 */             addDependency(paramCONSTANT_Class_info.getBaseName());
/* 666 */           return null;
/*     */         } catch (ConstantPoolException localConstantPoolException) {
/* 668 */           throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */         }
/*     */       }
/*     */ 
/*     */       public Void visitDouble(ConstantPool.CONSTANT_Double_info paramCONSTANT_Double_info, Void paramVoid) {
/* 673 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramCONSTANT_Fieldref_info, Void paramVoid) {
/* 677 */         return visitRef(paramCONSTANT_Fieldref_info, paramVoid);
/*     */       }
/*     */ 
/*     */       public Void visitFloat(ConstantPool.CONSTANT_Float_info paramCONSTANT_Float_info, Void paramVoid) {
/* 681 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitInteger(ConstantPool.CONSTANT_Integer_info paramCONSTANT_Integer_info, Void paramVoid) {
/* 685 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramCONSTANT_InterfaceMethodref_info, Void paramVoid) {
/* 689 */         return visitRef(paramCONSTANT_InterfaceMethodref_info, paramVoid);
/*     */       }
/*     */ 
/*     */       public Void visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramCONSTANT_InvokeDynamic_info, Void paramVoid) {
/* 693 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitLong(ConstantPool.CONSTANT_Long_info paramCONSTANT_Long_info, Void paramVoid) {
/* 697 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramCONSTANT_MethodHandle_info, Void paramVoid) {
/* 701 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitMethodType(ConstantPool.CONSTANT_MethodType_info paramCONSTANT_MethodType_info, Void paramVoid) {
/* 705 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitMethodref(ConstantPool.CONSTANT_Methodref_info paramCONSTANT_Methodref_info, Void paramVoid) {
/* 709 */         return visitRef(paramCONSTANT_Methodref_info, paramVoid);
/*     */       }
/*     */ 
/*     */       public Void visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info, Void paramVoid) {
/*     */         try {
/* 714 */           new Signature(paramCONSTANT_NameAndType_info.type_index).getType(this.constant_pool).accept(this, null);
/* 715 */           return null;
/*     */         } catch (ConstantPoolException localConstantPoolException) {
/* 717 */           throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */         }
/*     */       }
/*     */ 
/*     */       public Void visitString(ConstantPool.CONSTANT_String_info paramCONSTANT_String_info, Void paramVoid) {
/* 722 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitUtf8(ConstantPool.CONSTANT_Utf8_info paramCONSTANT_Utf8_info, Void paramVoid) {
/* 726 */         return null;
/*     */       }
/*     */ 
/*     */       private Void visitRef(ConstantPool.CPRefInfo paramCPRefInfo, Void paramVoid) {
/*     */         try {
/* 731 */           visitClass(paramCPRefInfo.getClassInfo(), paramVoid);
/* 732 */           return null;
/*     */         } catch (ConstantPoolException localConstantPoolException) {
/* 734 */           throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */         }
/*     */       }
/*     */ 
/*     */       private void findDependencies(Type paramType)
/*     */       {
/* 741 */         if (paramType != null)
/* 742 */           paramType.accept(this, null);
/*     */       }
/*     */ 
/*     */       private void findDependencies(List<? extends Type> paramList) {
/* 746 */         if (paramList != null)
/* 747 */           for (Type localType : paramList)
/* 748 */             localType.accept(this, null);
/*     */       }
/*     */ 
/*     */       public Void visitSimpleType(Type.SimpleType paramSimpleType, Void paramVoid)
/*     */       {
/* 753 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitArrayType(Type.ArrayType paramArrayType, Void paramVoid) {
/* 757 */         findDependencies(paramArrayType.elemType);
/* 758 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitMethodType(Type.MethodType paramMethodType, Void paramVoid) {
/* 762 */         findDependencies(paramMethodType.paramTypes);
/* 763 */         findDependencies(paramMethodType.returnType);
/* 764 */         findDependencies(paramMethodType.throwsTypes);
/* 765 */         findDependencies(paramMethodType.typeParamTypes);
/* 766 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitClassSigType(Type.ClassSigType paramClassSigType, Void paramVoid) {
/* 770 */         findDependencies(paramClassSigType.superclassType);
/* 771 */         findDependencies(paramClassSigType.superinterfaceTypes);
/* 772 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitClassType(Type.ClassType paramClassType, Void paramVoid) {
/* 776 */         findDependencies(paramClassType.outerType);
/* 777 */         addDependency(paramClassType.getBinaryName());
/* 778 */         findDependencies(paramClassType.typeArgs);
/* 779 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitTypeParamType(Type.TypeParamType paramTypeParamType, Void paramVoid) {
/* 783 */         findDependencies(paramTypeParamType.classBound);
/* 784 */         findDependencies(paramTypeParamType.interfaceBounds);
/* 785 */         return null;
/*     */       }
/*     */ 
/*     */       public Void visitWildcardType(Type.WildcardType paramWildcardType, Void paramVoid) {
/* 789 */         findDependencies(paramWildcardType.boundType);
/* 790 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ClassDependencyFinder extends Dependencies.BasicDependencyFinder
/*     */   {
/*     */     public Iterable<? extends Dependency> findDependencies(ClassFile paramClassFile)
/*     */     {
/* 470 */       Dependencies.BasicDependencyFinder.Visitor localVisitor = new Dependencies.BasicDependencyFinder.Visitor(this, paramClassFile);
/* 471 */       for (Object localObject1 = paramClassFile.constant_pool.entries().iterator(); ((Iterator)localObject1).hasNext(); ) { ConstantPool.CPInfo localCPInfo = (ConstantPool.CPInfo)((Iterator)localObject1).next();
/* 472 */         localVisitor.scan(localCPInfo); }
/*     */       try
/*     */       {
/* 475 */         localVisitor.addClass(paramClassFile.super_class);
/* 476 */         localVisitor.addClasses(paramClassFile.interfaces);
/* 477 */         localVisitor.scan(paramClassFile.attributes);
/*     */         Object localObject2;
/* 479 */         for (localObject2 : paramClassFile.fields) {
/* 480 */           localVisitor.scan(localObject2.descriptor, localObject2.attributes);
/*     */         }
/* 482 */         for (localObject2 : paramClassFile.methods) {
/* 483 */           localVisitor.scan(localObject2.descriptor, localObject2.attributes);
/*     */ 
/* 485 */           Exceptions_attribute localExceptions_attribute = (Exceptions_attribute)localObject2.attributes
/* 485 */             .get("Exceptions");
/*     */ 
/* 486 */           if (localExceptions_attribute != null)
/* 487 */             localVisitor.addClasses(localExceptions_attribute.exception_index_table);
/*     */         }
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException) {
/* 491 */         throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */       }
/*     */ 
/* 494 */       return localVisitor.deps;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ClassFileError extends Error
/*     */   {
/*     */     private static final long serialVersionUID = 4111110813961313203L;
/*     */ 
/*     */     public ClassFileError(Throwable paramThrowable)
/*     */     {
/*  89 */       initCause(paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ClassFileNotFoundException extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 3632265927794475048L;
/*     */     public final String className;
/*     */ 
/*     */     public ClassFileNotFoundException(String paramString)
/*     */     {
/*  70 */       super();
/*  71 */       this.className = paramString;
/*     */     }
/*     */ 
/*     */     public ClassFileNotFoundException(String paramString, Throwable paramThrowable) {
/*  75 */       this(paramString);
/*  76 */       initCause(paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface ClassFileReader
/*     */   {
/*     */     public abstract ClassFile getClassFile(String paramString)
/*     */       throws Dependencies.ClassFileNotFoundException;
/*     */   }
/*     */ 
/*     */   static class DefaultFilter
/*     */     implements Dependency.Filter
/*     */   {
/*     */     private static DefaultFilter instance;
/*     */ 
/*     */     static DefaultFilter instance()
/*     */     {
/* 406 */       if (instance == null)
/* 407 */         instance = new DefaultFilter();
/* 408 */       return instance;
/*     */     }
/*     */ 
/*     */     public boolean accepts(Dependency paramDependency) {
/* 412 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Recorder
/*     */   {
/*     */     public abstract void addDependency(Dependency paramDependency);
/*     */   }
/*     */ 
/*     */   static class SimpleDependency
/*     */     implements Dependency
/*     */   {
/*     */     private Dependency.Location origin;
/*     */     private Dependency.Location target;
/*     */ 
/*     */     public SimpleDependency(Dependency.Location paramLocation1, Dependency.Location paramLocation2)
/*     */     {
/* 362 */       this.origin = paramLocation1;
/* 363 */       this.target = paramLocation2;
/*     */     }
/*     */ 
/*     */     public Dependency.Location getOrigin() {
/* 367 */       return this.origin;
/*     */     }
/*     */ 
/*     */     public Dependency.Location getTarget() {
/* 371 */       return this.target;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 376 */       if (this == paramObject)
/* 377 */         return true;
/* 378 */       if (!(paramObject instanceof SimpleDependency))
/* 379 */         return false;
/* 380 */       SimpleDependency localSimpleDependency = (SimpleDependency)paramObject;
/* 381 */       return (this.origin.equals(localSimpleDependency.origin)) && (this.target.equals(localSimpleDependency.target));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 386 */       return this.origin.hashCode() * 31 + this.target.hashCode();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 391 */       return this.origin + ":" + this.target;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SimpleLocation
/*     */     implements Dependency.Location
/*     */   {
/*     */     private String name;
/*     */     private String className;
/*     */ 
/*     */     public SimpleLocation(String paramString)
/*     */     {
/* 317 */       this.name = paramString;
/* 318 */       this.className = paramString.replace('/', '.');
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 322 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String getClassName() {
/* 326 */       return this.className;
/*     */     }
/*     */ 
/*     */     public String getPackageName() {
/* 330 */       int i = this.name.lastIndexOf('/');
/* 331 */       return i > 0 ? this.name.substring(0, i).replace('/', '.') : "";
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 336 */       if (this == paramObject)
/* 337 */         return true;
/* 338 */       if (!(paramObject instanceof SimpleLocation))
/* 339 */         return false;
/* 340 */       return this.name.equals(((SimpleLocation)paramObject).name);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 345 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 350 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TargetPackageFilter
/*     */     implements Dependency.Filter
/*     */   {
/*     */     private final Set<String> packageNames;
/*     */     private final boolean matchSubpackages;
/*     */ 
/*     */     TargetPackageFilter(Set<String> paramSet, boolean paramBoolean)
/*     */     {
/* 438 */       for (String str : paramSet) {
/* 439 */         if (str.length() == 0)
/* 440 */           throw new IllegalArgumentException();
/*     */       }
/* 442 */       this.packageNames = paramSet;
/* 443 */       this.matchSubpackages = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean accepts(Dependency paramDependency) {
/* 447 */       String str1 = paramDependency.getTarget().getPackageName();
/* 448 */       if (this.packageNames.contains(str1)) {
/* 449 */         return true;
/*     */       }
/* 451 */       if (this.matchSubpackages) {
/* 452 */         for (String str2 : this.packageNames) {
/* 453 */           if (str1.startsWith(str2 + ".")) {
/* 454 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 458 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TargetRegexFilter
/*     */     implements Dependency.Filter
/*     */   {
/*     */     private final Pattern pattern;
/*     */ 
/*     */     TargetRegexFilter(Pattern paramPattern)
/*     */     {
/* 422 */       this.pattern = paramPattern;
/*     */     }
/*     */ 
/*     */     public boolean accepts(Dependency paramDependency) {
/* 426 */       return this.pattern.matcher(paramDependency.getTarget().getClassName()).matches();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Dependencies
 * JD-Core Version:    0.6.2
 */