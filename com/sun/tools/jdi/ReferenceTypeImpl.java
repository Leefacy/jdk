/*      */ package com.sun.tools.jdi;
/*      */ 
/*      */ import com.sun.jdi.AbsentInformationException;
/*      */ import com.sun.jdi.ClassLoaderReference;
/*      */ import com.sun.jdi.ClassNotLoadedException;
/*      */ import com.sun.jdi.ClassObjectReference;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.InterfaceType;
/*      */ import com.sun.jdi.InternalException;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.Type;
/*      */ import com.sun.jdi.Value;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import java.io.File;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public abstract class ReferenceTypeImpl extends TypeImpl
/*      */   implements ReferenceType
/*      */ {
/*      */   protected long ref;
/*   36 */   private String signature = null;
/*   37 */   private String genericSignature = null;
/*   38 */   private boolean genericSignatureGotten = false;
/*   39 */   private String baseSourceName = null;
/*   40 */   private String baseSourceDir = null;
/*   41 */   private String baseSourcePath = null;
/*   42 */   protected int modifiers = -1;
/*   43 */   private SoftReference<List<Field>> fieldsRef = null;
/*   44 */   private SoftReference<List<Method>> methodsRef = null;
/*   45 */   private SoftReference<SDE> sdeRef = null;
/*      */ 
/*   47 */   private boolean isClassLoaderCached = false;
/*   48 */   private ClassLoaderReference classLoader = null;
/*   49 */   private ClassObjectReference classObject = null;
/*      */ 
/*   51 */   private int status = 0;
/*   52 */   private boolean isPrepared = false;
/*      */ 
/*   55 */   private boolean versionNumberGotten = false;
/*      */   private int majorVersion;
/*      */   private int minorVersion;
/*   59 */   private boolean constantPoolInfoGotten = false;
/*      */   private int constanPoolCount;
/*      */   private byte[] constantPoolBytes;
/*   62 */   private SoftReference<byte[]> constantPoolBytesRef = null;
/*      */   private static final String ABSENT_BASE_SOURCE_NAME = "**ABSENT_BASE_SOURCE_NAME**";
/*   68 */   static final SDE NO_SDE_INFO_MARK = new SDE();
/*      */   private static final int INITIALIZED_OR_FAILED = 12;
/*      */ 
/*      */   protected ReferenceTypeImpl(VirtualMachine paramVirtualMachine, long paramLong)
/*      */   {
/*   76 */     super(paramVirtualMachine);
/*   77 */     this.ref = paramLong;
/*   78 */     this.genericSignatureGotten = false;
/*      */   }
/*      */ 
/*      */   void noticeRedefineClass()
/*      */   {
/*   84 */     this.baseSourceName = null;
/*   85 */     this.baseSourcePath = null;
/*   86 */     this.modifiers = -1;
/*   87 */     this.fieldsRef = null;
/*   88 */     this.methodsRef = null;
/*   89 */     this.sdeRef = null;
/*   90 */     this.versionNumberGotten = false;
/*   91 */     this.constantPoolInfoGotten = false;
/*      */   }
/*      */ 
/*      */   Method getMethodMirror(long paramLong) {
/*   95 */     if (paramLong == 0L)
/*      */     {
/*   97 */       return new ObsoleteMethodImpl(this.vm, this);
/*      */     }
/*      */ 
/*  102 */     Iterator localIterator = methods().iterator();
/*  103 */     while (localIterator.hasNext()) {
/*  104 */       MethodImpl localMethodImpl = (MethodImpl)localIterator.next();
/*  105 */       if (localMethodImpl.ref() == paramLong) {
/*  106 */         return localMethodImpl;
/*      */       }
/*      */     }
/*  109 */     throw new IllegalArgumentException("Invalid method id: " + paramLong);
/*      */   }
/*      */ 
/*      */   Field getFieldMirror(long paramLong)
/*      */   {
/*  116 */     Iterator localIterator = fields().iterator();
/*  117 */     while (localIterator.hasNext()) {
/*  118 */       FieldImpl localFieldImpl = (FieldImpl)localIterator.next();
/*  119 */       if (localFieldImpl.ref() == paramLong) {
/*  120 */         return localFieldImpl;
/*      */       }
/*      */     }
/*  123 */     throw new IllegalArgumentException("Invalid field id: " + paramLong);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/*  127 */     if ((paramObject != null) && ((paramObject instanceof ReferenceTypeImpl))) {
/*  128 */       ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)paramObject;
/*      */ 
/*  130 */       return (ref() == localReferenceTypeImpl.ref()) && 
/*  130 */         (this.vm
/*  130 */         .equals(localReferenceTypeImpl
/*  130 */         .virtualMachine()));
/*      */     }
/*  132 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  137 */     return (int)ref();
/*      */   }
/*      */ 
/*      */   public int compareTo(ReferenceType paramReferenceType)
/*      */   {
/*  149 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)paramReferenceType;
/*  150 */     int i = name().compareTo(localReferenceTypeImpl.name());
/*  151 */     if (i == 0) {
/*  152 */       long l1 = ref();
/*  153 */       long l2 = localReferenceTypeImpl.ref();
/*      */ 
/*  155 */       if (l1 == l2)
/*      */       {
/*  158 */         i = this.vm.sequenceNumber - 
/*  158 */           ((VirtualMachineImpl)localReferenceTypeImpl
/*  158 */           .virtualMachine()).sequenceNumber;
/*      */       }
/*  160 */       else i = l1 < l2 ? -1 : 1;
/*      */     }
/*      */ 
/*  163 */     return i;
/*      */   }
/*      */ 
/*      */   public String signature() {
/*  167 */     if (this.signature == null)
/*      */     {
/*  170 */       if (this.vm.canGet1_5LanguageFeatures())
/*      */       {
/*  175 */         genericSignature();
/*      */       }
/*      */       else try {
/*  178 */           this.signature = 
/*  179 */             JDWP.ReferenceType.Signature.process(this.vm, this).signature;
/*      */         }
/*      */         catch (JDWPException localJDWPException) {
/*  181 */           throw localJDWPException.toJDIException();
/*      */         }
/*      */     }
/*      */ 
/*  185 */     return this.signature;
/*      */   }
/*      */ 
/*      */   public String genericSignature()
/*      */   {
/*  190 */     if ((this.vm.canGet1_5LanguageFeatures()) && (!this.genericSignatureGotten))
/*      */     {
/*      */       JDWP.ReferenceType.SignatureWithGeneric localSignatureWithGeneric;
/*      */       try
/*      */       {
/*  196 */         localSignatureWithGeneric = JDWP.ReferenceType.SignatureWithGeneric.process(this.vm, this);
/*      */       }
/*      */       catch (JDWPException localJDWPException) {
/*  198 */         throw localJDWPException.toJDIException();
/*      */       }
/*  200 */       this.signature = localSignatureWithGeneric.signature;
/*  201 */       setGenericSignature(localSignatureWithGeneric.genericSignature);
/*      */     }
/*  203 */     return this.genericSignature;
/*      */   }
/*      */ 
/*      */   public ClassLoaderReference classLoader() {
/*  207 */     if (!this.isClassLoaderCached)
/*      */     {
/*      */       try
/*      */       {
/*  211 */         this.classLoader = 
/*  213 */           JDWP.ReferenceType.ClassLoader.process(this.vm, this).classLoader;
/*      */ 
/*  214 */         this.isClassLoaderCached = true;
/*      */       } catch (JDWPException localJDWPException) {
/*  216 */         throw localJDWPException.toJDIException();
/*      */       }
/*      */     }
/*  219 */     return this.classLoader;
/*      */   }
/*      */ 
/*      */   public boolean isPublic() {
/*  223 */     if (this.modifiers == -1) {
/*  224 */       getModifiers();
/*      */     }
/*  226 */     return (this.modifiers & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean isProtected() {
/*  230 */     if (this.modifiers == -1) {
/*  231 */       getModifiers();
/*      */     }
/*  233 */     return (this.modifiers & 0x4) > 0;
/*      */   }
/*      */ 
/*      */   public boolean isPrivate() {
/*  237 */     if (this.modifiers == -1) {
/*  238 */       getModifiers();
/*      */     }
/*  240 */     return (this.modifiers & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean isPackagePrivate() {
/*  244 */     return (!isPublic()) && (!isPrivate()) && (!isProtected());
/*      */   }
/*      */ 
/*      */   public boolean isAbstract() {
/*  248 */     if (this.modifiers == -1) {
/*  249 */       getModifiers();
/*      */     }
/*  251 */     return (this.modifiers & 0x400) > 0;
/*      */   }
/*      */ 
/*      */   public boolean isFinal() {
/*  255 */     if (this.modifiers == -1) {
/*  256 */       getModifiers();
/*      */     }
/*  258 */     return (this.modifiers & 0x10) > 0;
/*      */   }
/*      */ 
/*      */   public boolean isStatic() {
/*  262 */     if (this.modifiers == -1) {
/*  263 */       getModifiers();
/*      */     }
/*  265 */     return (this.modifiers & 0x8) > 0;
/*      */   }
/*      */ 
/*      */   public boolean isPrepared()
/*      */   {
/*  273 */     if (this.status == 0) {
/*  274 */       updateStatus();
/*      */     }
/*  276 */     return this.isPrepared;
/*      */   }
/*      */ 
/*      */   public boolean isVerified()
/*      */   {
/*  281 */     if ((this.status & 0x1) == 0) {
/*  282 */       updateStatus();
/*      */     }
/*  284 */     return (this.status & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isInitialized()
/*      */   {
/*  290 */     if ((this.status & 0xC) == 0) {
/*  291 */       updateStatus();
/*      */     }
/*  293 */     return (this.status & 0x4) != 0;
/*      */   }
/*      */ 
/*      */   public boolean failedToInitialize()
/*      */   {
/*  299 */     if ((this.status & 0xC) == 0) {
/*  300 */       updateStatus();
/*      */     }
/*  302 */     return (this.status & 0x8) != 0;
/*      */   }
/*      */ 
/*      */   public List<Field> fields() {
/*  306 */     Object localObject1 = this.fieldsRef == null ? null : (List)this.fieldsRef.get();
/*  307 */     if (localObject1 == null)
/*      */     {
/*      */       Object localObject2;
/*      */       Object localObject3;
/*      */       FieldImpl localFieldImpl;
/*  308 */       if (this.vm.canGet1_5LanguageFeatures())
/*      */       {
/*      */         try {
/*  311 */           localObject2 = JDWP.ReferenceType.FieldsWithGeneric.process(this.vm, this).declared;
/*      */         } catch (JDWPException localJDWPException1) {
/*  313 */           throw localJDWPException1.toJDIException();
/*      */         }
/*  315 */         localObject1 = new ArrayList(localObject2.length);
/*  316 */         for (int i = 0; i < localObject2.length; i++) {
/*  317 */           localObject3 = localObject2[i];
/*      */ 
/*  320 */           localFieldImpl = new FieldImpl(this.vm, this, localObject3.fieldID, localObject3.name, localObject3.signature, localObject3.genericSignature, localObject3.modBits);
/*      */ 
/*  324 */           ((List)localObject1).add(localFieldImpl);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         try {
/*  330 */           localObject2 = JDWP.ReferenceType.Fields.process(this.vm, this).declared;
/*      */         }
/*      */         catch (JDWPException localJDWPException2) {
/*  332 */           throw localJDWPException2.toJDIException();
/*      */         }
/*  334 */         localObject1 = new ArrayList(localObject2.length);
/*  335 */         for (int j = 0; j < localObject2.length; j++) {
/*  336 */           localObject3 = localObject2[j];
/*      */ 
/*  338 */           localFieldImpl = new FieldImpl(this.vm, this, localObject3.fieldID, localObject3.name, localObject3.signature, null, localObject3.modBits);
/*      */ 
/*  342 */           ((List)localObject1).add(localFieldImpl);
/*      */         }
/*      */       }
/*      */ 
/*  346 */       localObject1 = Collections.unmodifiableList((List)localObject1);
/*  347 */       this.fieldsRef = new SoftReference(localObject1);
/*      */     }
/*  349 */     return localObject1;
/*      */   }
/*      */ 
/*      */   abstract List<? extends ReferenceType> inheritedTypes();
/*      */ 
/*      */   void addVisibleFields(List<Field> paramList, Map<String, Field> paramMap, List<String> paramList1) {
/*  355 */     for (Field localField1 : visibleFields()) {
/*  356 */       String str = localField1.name();
/*  357 */       if (!paramList1.contains(str)) {
/*  358 */         Field localField2 = (Field)paramMap.get(str);
/*  359 */         if (localField2 == null) {
/*  360 */           paramList.add(localField1);
/*  361 */           paramMap.put(str, localField1);
/*  362 */         } else if (!localField1.equals(localField2)) {
/*  363 */           paramList1.add(str);
/*  364 */           paramMap.remove(str);
/*  365 */           paramList.remove(localField2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<Field> visibleFields()
/*      */   {
/*  380 */     ArrayList localArrayList1 = new ArrayList();
/*  381 */     HashMap localHashMap = new HashMap();
/*      */ 
/*  384 */     ArrayList localArrayList2 = new ArrayList();
/*      */ 
/*  387 */     List localList = inheritedTypes();
/*  388 */     Iterator localIterator1 = localList.iterator();
/*  389 */     while (localIterator1.hasNext())
/*      */     {
/*  393 */       localObject = (ReferenceTypeImpl)localIterator1.next();
/*  394 */       ((ReferenceTypeImpl)localObject).addVisibleFields(localArrayList1, localHashMap, localArrayList2);
/*      */     }
/*      */ 
/*  401 */     Object localObject = new ArrayList(fields());
/*  402 */     for (Field localField1 : (List)localObject) {
/*  403 */       Field localField2 = (Field)localHashMap.get(localField1.name());
/*  404 */       if (localField2 != null) {
/*  405 */         localArrayList1.remove(localField2);
/*      */       }
/*      */     }
/*  408 */     ((List)localObject).addAll(localArrayList1);
/*  409 */     return localObject;
/*      */   }
/*      */ 
/*      */   void addAllFields(List<Field> paramList, Set<ReferenceType> paramSet)
/*      */   {
/*  414 */     if (!paramSet.contains(this)) {
/*  415 */       paramSet.add(this);
/*      */ 
/*  418 */       paramList.addAll(fields());
/*      */ 
/*  421 */       List localList = inheritedTypes();
/*  422 */       Iterator localIterator = localList.iterator();
/*  423 */       while (localIterator.hasNext()) {
/*  424 */         ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)localIterator.next();
/*  425 */         localReferenceTypeImpl.addAllFields(paramList, paramSet);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*  430 */   public List<Field> allFields() { ArrayList localArrayList = new ArrayList();
/*  431 */     HashSet localHashSet = new HashSet();
/*  432 */     addAllFields(localArrayList, localHashSet);
/*  433 */     return localArrayList; }
/*      */ 
/*      */   public Field fieldByName(String paramString)
/*      */   {
/*  437 */     List localList = visibleFields();
/*      */ 
/*  439 */     for (int i = 0; i < localList.size(); i++) {
/*  440 */       Field localField = (Field)localList.get(i);
/*      */ 
/*  442 */       if (localField.name().equals(paramString)) {
/*  443 */         return localField;
/*      */       }
/*      */     }
/*      */ 
/*  447 */     return null;
/*      */   }
/*      */ 
/*      */   public List<Method> methods() {
/*  451 */     Object localObject = this.methodsRef == null ? null : (List)this.methodsRef.get();
/*  452 */     if (localObject == null) {
/*  453 */       if (!this.vm.canGet1_5LanguageFeatures()) {
/*  454 */         localObject = methods1_4();
/*      */       }
/*      */       else {
/*      */         JDWP.ReferenceType.MethodsWithGeneric.MethodInfo[] arrayOfMethodInfo;
/*      */         try {
/*  459 */           arrayOfMethodInfo = JDWP.ReferenceType.MethodsWithGeneric.process(this.vm, this).declared;
/*      */         }
/*      */         catch (JDWPException localJDWPException) {
/*  461 */           throw localJDWPException.toJDIException();
/*      */         }
/*  463 */         localObject = new ArrayList(arrayOfMethodInfo.length);
/*  464 */         for (int i = 0; i < arrayOfMethodInfo.length; i++)
/*      */         {
/*  466 */           JDWP.ReferenceType.MethodsWithGeneric.MethodInfo localMethodInfo = arrayOfMethodInfo[i];
/*      */ 
/*  468 */           MethodImpl localMethodImpl = MethodImpl.createMethodImpl(this.vm, this, localMethodInfo.methodID, localMethodInfo.name, localMethodInfo.signature, localMethodInfo.genericSignature, localMethodInfo.modBits);
/*      */ 
/*  473 */           ((List)localObject).add(localMethodImpl);
/*      */         }
/*      */       }
/*  476 */       localObject = Collections.unmodifiableList((List)localObject);
/*  477 */       this.methodsRef = new SoftReference(localObject);
/*      */     }
/*  479 */     return localObject;
/*      */   }
/*      */ 
/*      */   private List<Method> methods1_4()
/*      */   {
/*      */     JDWP.ReferenceType.Methods.MethodInfo[] arrayOfMethodInfo;
/*      */     try
/*      */     {
/*  487 */       arrayOfMethodInfo = JDWP.ReferenceType.Methods.process(this.vm, this).declared;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  489 */       throw localJDWPException.toJDIException();
/*      */     }
/*  491 */     ArrayList localArrayList = new ArrayList(arrayOfMethodInfo.length);
/*  492 */     for (int i = 0; i < arrayOfMethodInfo.length; i++) {
/*  493 */       JDWP.ReferenceType.Methods.MethodInfo localMethodInfo = arrayOfMethodInfo[i];
/*      */ 
/*  495 */       MethodImpl localMethodImpl = MethodImpl.createMethodImpl(this.vm, this, localMethodInfo.methodID, localMethodInfo.name, localMethodInfo.signature, null, localMethodInfo.modBits);
/*      */ 
/*  500 */       localArrayList.add(localMethodImpl);
/*      */     }
/*  502 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   void addToMethodMap(Map<String, Method> paramMap, List<Method> paramList)
/*      */   {
/*  510 */     for (Method localMethod : paramList)
/*  511 */       paramMap.put(localMethod.name().concat(localMethod.signature()), localMethod);
/*      */   }
/*      */ 
/*      */   abstract void addVisibleMethods(Map<String, Method> paramMap, Set<InterfaceType> paramSet);
/*      */ 
/*      */   public List<Method> visibleMethods()
/*      */   {
/*  522 */     HashMap localHashMap = new HashMap();
/*  523 */     addVisibleMethods(localHashMap, new HashSet());
/*      */ 
/*  531 */     List localList = allMethods();
/*  532 */     localList.retainAll(localHashMap.values());
/*  533 */     return localList;
/*      */   }
/*      */ 
/*      */   public abstract List<Method> allMethods();
/*      */ 
/*      */   public List<Method> methodsByName(String paramString) {
/*  539 */     List localList = visibleMethods();
/*  540 */     ArrayList localArrayList = new ArrayList(localList.size());
/*  541 */     for (Method localMethod : localList) {
/*  542 */       if (localMethod.name().equals(paramString)) {
/*  543 */         localArrayList.add(localMethod);
/*      */       }
/*      */     }
/*  546 */     localArrayList.trimToSize();
/*  547 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public List<Method> methodsByName(String paramString1, String paramString2) {
/*  551 */     List localList = visibleMethods();
/*  552 */     ArrayList localArrayList = new ArrayList(localList.size());
/*  553 */     for (Method localMethod : localList) {
/*  554 */       if ((localMethod.name().equals(paramString1)) && 
/*  555 */         (localMethod
/*  555 */         .signature().equals(paramString2))) {
/*  556 */         localArrayList.add(localMethod);
/*      */       }
/*      */     }
/*  559 */     localArrayList.trimToSize();
/*  560 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   List<InterfaceType> getInterfaces()
/*      */   {
/*      */     InterfaceTypeImpl[] arrayOfInterfaceTypeImpl;
/*      */     try {
/*  567 */       arrayOfInterfaceTypeImpl = JDWP.ReferenceType.Interfaces.process(this.vm, this).interfaces;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  569 */       throw localJDWPException.toJDIException();
/*      */     }
/*  571 */     return Arrays.asList((InterfaceType[])arrayOfInterfaceTypeImpl);
/*      */   }
/*      */ 
/*      */   public List<ReferenceType> nestedTypes() {
/*  575 */     List localList = this.vm.allClasses();
/*  576 */     ArrayList localArrayList = new ArrayList();
/*  577 */     String str1 = name();
/*  578 */     int i = str1.length();
/*  579 */     Iterator localIterator = localList.iterator();
/*  580 */     while (localIterator.hasNext()) {
/*  581 */       ReferenceType localReferenceType = (ReferenceType)localIterator.next();
/*  582 */       String str2 = localReferenceType.name();
/*  583 */       int j = str2.length();
/*      */ 
/*  585 */       if ((j > i) && (str2.startsWith(str1))) {
/*  586 */         int k = str2.charAt(i);
/*  587 */         if ((k == 36) || (k == 35)) {
/*  588 */           localArrayList.add(localReferenceType);
/*      */         }
/*      */       }
/*      */     }
/*  592 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public Value getValue(Field paramField) {
/*  596 */     ArrayList localArrayList = new ArrayList(1);
/*  597 */     localArrayList.add(paramField);
/*  598 */     Map localMap = getValues(localArrayList);
/*  599 */     return (Value)localMap.get(paramField);
/*      */   }
/*      */ 
/*      */   void validateFieldAccess(Field paramField)
/*      */   {
/*  608 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)paramField.declaringType();
/*  609 */     if (!localReferenceTypeImpl.isAssignableFrom(this))
/*  610 */       throw new IllegalArgumentException("Invalid field");
/*      */   }
/*      */ 
/*      */   void validateFieldSet(Field paramField)
/*      */   {
/*  615 */     validateFieldAccess(paramField);
/*  616 */     if (paramField.isFinal())
/*  617 */       throw new IllegalArgumentException("Cannot set value of final field");
/*      */   }
/*      */ 
/*      */   public Map<Field, Value> getValues(List<? extends Field> paramList)
/*      */   {
/*  625 */     validateMirrors(paramList);
/*      */ 
/*  627 */     int i = paramList.size();
/*  628 */     JDWP.ReferenceType.GetValues.Field[] arrayOfField = new JDWP.ReferenceType.GetValues.Field[i];
/*      */     Object localObject;
/*  631 */     for (int j = 0; j < i; j++) {
/*  632 */       localObject = (FieldImpl)paramList.get(j);
/*      */ 
/*  634 */       validateFieldAccess((Field)localObject);
/*      */ 
/*  637 */       if (!((FieldImpl)localObject).isStatic()) {
/*  638 */         throw new IllegalArgumentException("Attempt to use non-static field with ReferenceType");
/*      */       }
/*      */ 
/*  641 */       arrayOfField[j] = new JDWP.ReferenceType.GetValues.Field(((FieldImpl)localObject)
/*  642 */         .ref());
/*      */     }
/*      */ 
/*  645 */     HashMap localHashMap = new HashMap(i);
/*      */     try
/*      */     {
/*  650 */       localObject = JDWP.ReferenceType.GetValues.process(this.vm, this, arrayOfField).values;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  652 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/*  655 */     if (i != localObject.length) {
/*  656 */       throw new InternalException("Wrong number of values returned from target VM");
/*      */     }
/*      */ 
/*  659 */     for (int k = 0; k < i; k++) {
/*  660 */       FieldImpl localFieldImpl = (FieldImpl)paramList.get(k);
/*  661 */       localHashMap.put(localFieldImpl, localObject[k]);
/*      */     }
/*      */ 
/*  664 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   public ClassObjectReference classObject() {
/*  668 */     if (this.classObject == null)
/*      */     {
/*  671 */       synchronized (this) {
/*  672 */         if (this.classObject == null) {
/*      */           try {
/*  674 */             this.classObject = 
/*  675 */               JDWP.ReferenceType.ClassObject.process(this.vm, this).classObject;
/*      */           }
/*      */           catch (JDWPException localJDWPException) {
/*  677 */             throw localJDWPException.toJDIException();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  682 */     return this.classObject;
/*      */   }
/*      */ 
/*      */   SDE.Stratum stratum(String paramString) {
/*  686 */     SDE localSDE = sourceDebugExtensionInfo();
/*  687 */     if (!localSDE.isValid()) {
/*  688 */       localSDE = NO_SDE_INFO_MARK;
/*      */     }
/*  690 */     return localSDE.stratum(paramString);
/*      */   }
/*      */ 
/*      */   public String sourceName() throws AbsentInformationException {
/*  694 */     return (String)sourceNames(this.vm.getDefaultStratum()).get(0);
/*      */   }
/*      */ 
/*      */   public List<String> sourceNames(String paramString) throws AbsentInformationException
/*      */   {
/*  699 */     SDE.Stratum localStratum = stratum(paramString);
/*  700 */     if (localStratum.isJava()) {
/*  701 */       ArrayList localArrayList = new ArrayList(1);
/*  702 */       localArrayList.add(baseSourceName());
/*  703 */       return localArrayList;
/*      */     }
/*  705 */     return localStratum.sourceNames(this);
/*      */   }
/*      */ 
/*      */   public List<String> sourcePaths(String paramString) throws AbsentInformationException
/*      */   {
/*  710 */     SDE.Stratum localStratum = stratum(paramString);
/*  711 */     if (localStratum.isJava()) {
/*  712 */       ArrayList localArrayList = new ArrayList(1);
/*  713 */       localArrayList.add(baseSourceDir() + baseSourceName());
/*  714 */       return localArrayList;
/*      */     }
/*  716 */     return localStratum.sourcePaths(this);
/*      */   }
/*      */ 
/*      */   String baseSourceName() throws AbsentInformationException {
/*  720 */     String str = this.baseSourceName;
/*  721 */     if (str == null)
/*      */     {
/*      */       try
/*      */       {
/*  726 */         str = JDWP.ReferenceType.SourceFile.process(this.vm, this).sourceFile;
/*      */       }
/*      */       catch (JDWPException localJDWPException) {
/*  728 */         if (localJDWPException.errorCode() == 101)
/*  729 */           str = "**ABSENT_BASE_SOURCE_NAME**";
/*      */         else {
/*  731 */           throw localJDWPException.toJDIException();
/*      */         }
/*      */       }
/*  734 */       this.baseSourceName = str;
/*      */     }
/*  736 */     if (str == "**ABSENT_BASE_SOURCE_NAME**") {
/*  737 */       throw new AbsentInformationException();
/*      */     }
/*  739 */     return str;
/*      */   }
/*      */ 
/*      */   String baseSourcePath() throws AbsentInformationException {
/*  743 */     String str = this.baseSourcePath;
/*  744 */     if (str == null) {
/*  745 */       str = baseSourceDir() + baseSourceName();
/*  746 */       this.baseSourcePath = str;
/*      */     }
/*  748 */     return str;
/*      */   }
/*      */ 
/*      */   String baseSourceDir() {
/*  752 */     if (this.baseSourceDir == null) {
/*  753 */       String str = name();
/*  754 */       StringBuffer localStringBuffer = new StringBuffer(str.length() + 10);
/*  755 */       int i = 0;
/*      */       int j;
/*  758 */       while ((j = str.indexOf('.', i)) > 0) {
/*  759 */         localStringBuffer.append(str.substring(i, j));
/*  760 */         localStringBuffer.append(File.separatorChar);
/*  761 */         i = j + 1;
/*      */       }
/*  763 */       this.baseSourceDir = localStringBuffer.toString();
/*      */     }
/*  765 */     return this.baseSourceDir;
/*      */   }
/*      */ 
/*      */   public String sourceDebugExtension() throws AbsentInformationException
/*      */   {
/*  770 */     if (!this.vm.canGetSourceDebugExtension()) {
/*  771 */       throw new UnsupportedOperationException();
/*      */     }
/*  773 */     SDE localSDE = sourceDebugExtensionInfo();
/*  774 */     if (localSDE == NO_SDE_INFO_MARK) {
/*  775 */       throw new AbsentInformationException();
/*      */     }
/*  777 */     return localSDE.sourceDebugExtension;
/*      */   }
/*      */ 
/*      */   private SDE sourceDebugExtensionInfo() {
/*  781 */     if (!this.vm.canGetSourceDebugExtension()) {
/*  782 */       return NO_SDE_INFO_MARK;
/*      */     }
/*  784 */     SDE localSDE = this.sdeRef == null ? null : (SDE)this.sdeRef.get();
/*  785 */     if (localSDE == null) {
/*  786 */       String str = null;
/*      */       try
/*      */       {
/*  789 */         str = JDWP.ReferenceType.SourceDebugExtension.process(this.vm, this).extension;
/*      */       }
/*      */       catch (JDWPException localJDWPException) {
/*  791 */         if (localJDWPException.errorCode() != 101) {
/*  792 */           this.sdeRef = new SoftReference(NO_SDE_INFO_MARK);
/*  793 */           throw localJDWPException.toJDIException();
/*      */         }
/*      */       }
/*  796 */       if (str == null)
/*  797 */         localSDE = NO_SDE_INFO_MARK;
/*      */       else {
/*  799 */         localSDE = new SDE(str);
/*      */       }
/*  801 */       this.sdeRef = new SoftReference(localSDE);
/*      */     }
/*  803 */     return localSDE;
/*      */   }
/*      */ 
/*      */   public List<String> availableStrata() {
/*  807 */     SDE localSDE = sourceDebugExtensionInfo();
/*  808 */     if (localSDE.isValid()) {
/*  809 */       return localSDE.availableStrata();
/*      */     }
/*  811 */     ArrayList localArrayList = new ArrayList();
/*  812 */     localArrayList.add("Java");
/*  813 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public String defaultStratum()
/*      */   {
/*  821 */     SDE localSDE = sourceDebugExtensionInfo();
/*  822 */     if (localSDE.isValid()) {
/*  823 */       return localSDE.defaultStratumId;
/*      */     }
/*  825 */     return "Java";
/*      */   }
/*      */ 
/*      */   public int modifiers()
/*      */   {
/*  830 */     if (this.modifiers == -1) {
/*  831 */       getModifiers();
/*      */     }
/*  833 */     return this.modifiers;
/*      */   }
/*      */ 
/*      */   public List<Location> allLineLocations() throws AbsentInformationException
/*      */   {
/*  838 */     return allLineLocations(this.vm.getDefaultStratum(), null);
/*      */   }
/*      */ 
/*      */   public List<Location> allLineLocations(String paramString1, String paramString2) throws AbsentInformationException
/*      */   {
/*  843 */     int i = 0;
/*  844 */     SDE.Stratum localStratum = stratum(paramString1);
/*  845 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  847 */     for (Iterator localIterator = methods().iterator(); localIterator.hasNext(); ) {
/*  848 */       MethodImpl localMethodImpl = (MethodImpl)localIterator.next();
/*      */       try {
/*  850 */         localArrayList.addAll(localMethodImpl
/*  851 */           .allLineLocations(localStratum, paramString2));
/*      */       }
/*      */       catch (AbsentInformationException localAbsentInformationException) {
/*  853 */         i = 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  861 */     if ((i != 0) && (localArrayList.size() == 0)) {
/*  862 */       throw new AbsentInformationException();
/*      */     }
/*  864 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public List<Location> locationsOfLine(int paramInt) throws AbsentInformationException
/*      */   {
/*  869 */     return locationsOfLine(this.vm.getDefaultStratum(), null, paramInt);
/*      */   }
/*      */ 
/*      */   public List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt)
/*      */     throws AbsentInformationException
/*      */   {
/*  879 */     int i = 0;
/*      */ 
/*  881 */     int j = 0;
/*  882 */     List localList = methods();
/*  883 */     SDE.Stratum localStratum = stratum(paramString1);
/*      */ 
/*  885 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  887 */     Iterator localIterator = localList.iterator();
/*  888 */     while (localIterator.hasNext()) {
/*  889 */       MethodImpl localMethodImpl = (MethodImpl)localIterator.next();
/*      */ 
/*  892 */       if ((!localMethodImpl.isAbstract()) && 
/*  893 */         (!localMethodImpl
/*  893 */         .isNative())) {
/*      */         try {
/*  895 */           localArrayList.addAll(localMethodImpl
/*  896 */             .locationsOfLine(localStratum, paramString2, paramInt));
/*      */ 
/*  899 */           j = 1;
/*      */         } catch (AbsentInformationException localAbsentInformationException) {
/*  901 */           i = 1;
/*      */         }
/*      */       }
/*      */     }
/*  905 */     if ((i != 0) && (j == 0)) {
/*  906 */       throw new AbsentInformationException();
/*      */     }
/*  908 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public List<ObjectReference> instances(long paramLong) {
/*  912 */     if (!this.vm.canGetInstanceInfo()) {
/*  913 */       throw new UnsupportedOperationException("target does not support getting instances");
/*      */     }
/*      */ 
/*  917 */     if (paramLong < 0L) {
/*  918 */       throw new IllegalArgumentException("maxInstances is less than zero: " + paramLong);
/*      */     }
/*      */ 
/*  921 */     int i = paramLong > 2147483647L ? 2147483647 : (int)paramLong;
/*      */     try
/*      */     {
/*  926 */       return Arrays.asList(
/*  928 */         (ObjectReference[])JDWP.ReferenceType.Instances.process(this.vm, this, i).instances);
/*      */     }
/*      */     catch (JDWPException localJDWPException)
/*      */     {
/*  930 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void getClassFileVersion() {
/*  935 */     if (!this.vm.canGetClassFileVersion()) {
/*  936 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*  939 */     if (this.versionNumberGotten)
/*      */       return;
/*      */     JDWP.ReferenceType.ClassFileVersion localClassFileVersion;
/*      */     try {
/*  943 */       localClassFileVersion = JDWP.ReferenceType.ClassFileVersion.process(this.vm, this);
/*      */     } catch (JDWPException localJDWPException) {
/*  945 */       if (localJDWPException.errorCode() == 101) {
/*  946 */         this.majorVersion = 0;
/*  947 */         this.minorVersion = 0;
/*  948 */         this.versionNumberGotten = true;
/*  949 */         return;
/*      */       }
/*  951 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/*  954 */     this.majorVersion = localClassFileVersion.majorVersion;
/*  955 */     this.minorVersion = localClassFileVersion.minorVersion;
/*  956 */     this.versionNumberGotten = true;
/*      */   }
/*      */ 
/*      */   public int majorVersion()
/*      */   {
/*      */     try {
/*  962 */       getClassFileVersion();
/*      */     } catch (RuntimeException localRuntimeException) {
/*  964 */       throw localRuntimeException;
/*      */     }
/*  966 */     return this.majorVersion;
/*      */   }
/*      */ 
/*      */   public int minorVersion() {
/*      */     try {
/*  971 */       getClassFileVersion();
/*      */     } catch (RuntimeException localRuntimeException) {
/*  973 */       throw localRuntimeException;
/*      */     }
/*  975 */     return this.minorVersion;
/*      */   }
/*      */ 
/*      */   private void getConstantPoolInfo()
/*      */   {
/*  980 */     if (!this.vm.canGetConstantPool()) {
/*  981 */       throw new UnsupportedOperationException();
/*      */     }
/*  983 */     if (this.constantPoolInfoGotten)
/*      */       return;
/*      */     JDWP.ReferenceType.ConstantPool localConstantPool;
/*      */     try {
/*  987 */       localConstantPool = JDWP.ReferenceType.ConstantPool.process(this.vm, this);
/*      */     } catch (JDWPException localJDWPException) {
/*  989 */       if (localJDWPException.errorCode() == 101) {
/*  990 */         this.constanPoolCount = 0;
/*  991 */         this.constantPoolBytesRef = null;
/*  992 */         this.constantPoolInfoGotten = true;
/*  993 */         return;
/*      */       }
/*  995 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/*  999 */     this.constanPoolCount = localConstantPool.count;
/* 1000 */     byte[] arrayOfByte = localConstantPool.bytes;
/* 1001 */     this.constantPoolBytesRef = new SoftReference(arrayOfByte);
/* 1002 */     this.constantPoolInfoGotten = true;
/*      */   }
/*      */ 
/*      */   public int constantPoolCount()
/*      */   {
/*      */     try {
/* 1008 */       getConstantPoolInfo();
/*      */     } catch (RuntimeException localRuntimeException) {
/* 1010 */       throw localRuntimeException;
/*      */     }
/* 1012 */     return this.constanPoolCount;
/*      */   }
/*      */ 
/*      */   public byte[] constantPool() {
/*      */     try {
/* 1017 */       getConstantPoolInfo();
/*      */     } catch (RuntimeException localRuntimeException) {
/* 1019 */       throw localRuntimeException;
/*      */     }
/* 1021 */     if (this.constantPoolBytesRef != null) {
/* 1022 */       byte[] arrayOfByte = (byte[])this.constantPoolBytesRef.get();
/*      */ 
/* 1028 */       return (byte[])arrayOfByte.clone();
/*      */     }
/* 1030 */     return null;
/*      */   }
/*      */ 
/*      */   void getModifiers()
/*      */   {
/* 1037 */     if (this.modifiers != -1)
/* 1038 */       return;
/*      */     try
/*      */     {
/* 1041 */       this.modifiers = 
/* 1042 */         JDWP.ReferenceType.Modifiers.process(this.vm, this).modBits;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/* 1044 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */   }
/*      */ 
/*      */   void decodeStatus(int paramInt) {
/* 1049 */     this.status = paramInt;
/* 1050 */     if ((paramInt & 0x2) != 0)
/* 1051 */       this.isPrepared = true;
/*      */   }
/*      */ 
/*      */   void updateStatus()
/*      */   {
/*      */     try {
/* 1057 */       decodeStatus(JDWP.ReferenceType.Status.process(this.vm, this).status);
/*      */     } catch (JDWPException localJDWPException) {
/* 1059 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */   }
/*      */ 
/*      */   void markPrepared() {
/* 1064 */     this.isPrepared = true;
/*      */   }
/*      */ 
/*      */   long ref() {
/* 1068 */     return this.ref;
/*      */   }
/*      */ 
/*      */   int indexOf(Method paramMethod)
/*      */   {
/* 1074 */     return methods().indexOf(paramMethod);
/*      */   }
/*      */ 
/*      */   int indexOf(Field paramField)
/*      */   {
/* 1079 */     return fields().indexOf(paramField);
/*      */   }
/*      */ 
/*      */   abstract boolean isAssignableTo(ReferenceType paramReferenceType);
/*      */ 
/*      */   boolean isAssignableFrom(ReferenceType paramReferenceType)
/*      */   {
/* 1089 */     return ((ReferenceTypeImpl)paramReferenceType).isAssignableTo(this);
/*      */   }
/*      */ 
/*      */   boolean isAssignableFrom(ObjectReference paramObjectReference)
/*      */   {
/* 1094 */     return (paramObjectReference == null) || 
/* 1094 */       (isAssignableFrom(paramObjectReference
/* 1094 */       .referenceType()));
/*      */   }
/*      */ 
/*      */   void setStatus(int paramInt) {
/* 1098 */     decodeStatus(paramInt);
/*      */   }
/*      */ 
/*      */   void setSignature(String paramString) {
/* 1102 */     this.signature = paramString;
/*      */   }
/*      */ 
/*      */   void setGenericSignature(String paramString) {
/* 1106 */     if ((paramString != null) && (paramString.length() == 0))
/* 1107 */       this.genericSignature = null;
/*      */     else {
/* 1109 */       this.genericSignature = paramString;
/*      */     }
/* 1111 */     this.genericSignatureGotten = true;
/*      */   }
/*      */ 
/*      */   private static boolean isPrimitiveArray(String paramString) {
/* 1115 */     int i = paramString.lastIndexOf('[');
/*      */     boolean bool;
/* 1123 */     if (i < 0) {
/* 1124 */       bool = false;
/*      */     } else {
/* 1126 */       int j = paramString.charAt(i + 1);
/* 1127 */       bool = j != 76;
/*      */     }
/* 1129 */     return bool;
/*      */   }
/*      */ 
/*      */   Type findType(String paramString)
/*      */     throws ClassNotLoadedException
/*      */   {
/*      */     Object localObject;
/* 1134 */     if (paramString.length() == 1)
/*      */     {
/* 1136 */       int i = paramString.charAt(0);
/* 1137 */       if (i == 86)
/* 1138 */         localObject = this.vm.theVoidType();
/*      */       else {
/* 1140 */         localObject = this.vm.primitiveTypeMirror((byte)i);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1145 */       ClassLoaderReferenceImpl localClassLoaderReferenceImpl = (ClassLoaderReferenceImpl)classLoader();
/* 1146 */       if ((localClassLoaderReferenceImpl == null) || 
/* 1147 */         (isPrimitiveArray(paramString)))
/*      */       {
/* 1150 */         localObject = this.vm.findBootType(paramString);
/*      */       }
/*      */       else {
/* 1153 */         localObject = localClassLoaderReferenceImpl.findType(paramString);
/*      */       }
/*      */     }
/* 1156 */     return localObject;
/*      */   }
/*      */ 
/*      */   String loaderString() {
/* 1160 */     if (classLoader() != null) {
/* 1161 */       return "loaded by " + classLoader().toString();
/*      */     }
/* 1163 */     return "no class loader";
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ReferenceTypeImpl
 * JD-Core Version:    0.6.2
 */