/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassLoaderReference;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ClassLoaderReferenceImpl extends ObjectReferenceImpl
/*     */   implements ClassLoaderReference, VMListener
/*     */ {
/*     */   protected ObjectReferenceImpl.Cache newCache()
/*     */   {
/*  40 */     return new Cache(null);
/*     */   }
/*     */ 
/*     */   ClassLoaderReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  44 */     super(paramVirtualMachine, paramLong);
/*  45 */     this.vm.state().addListener(this);
/*     */   }
/*     */ 
/*     */   protected String description() {
/*  49 */     return "ClassLoaderReference " + uniqueID();
/*     */   }
/*     */ 
/*     */   public List<ReferenceType> definedClasses() {
/*  53 */     ArrayList localArrayList = new ArrayList();
/*  54 */     for (ReferenceType localReferenceType : this.vm.allClasses()) {
/*  55 */       if ((localReferenceType.isPrepared()) && 
/*  56 */         (equals(localReferenceType
/*  56 */         .classLoader()))) {
/*  57 */         localArrayList.add(localReferenceType);
/*     */       }
/*     */     }
/*  60 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public List<ReferenceType> visibleClasses() {
/*  64 */     Object localObject = null;
/*     */     try {
/*  66 */       Cache localCache = (Cache)getCache();
/*     */ 
/*  68 */       if (localCache != null) {
/*  69 */         localObject = localCache.visibleClasses;
/*     */       }
/*  71 */       if (localObject == null)
/*     */       {
/*  74 */         JDWP.ClassLoaderReference.VisibleClasses.ClassInfo[] arrayOfClassInfo = JDWP.ClassLoaderReference.VisibleClasses.process(this.vm, this).classes;
/*     */ 
/*  75 */         localObject = new ArrayList(arrayOfClassInfo.length);
/*  76 */         for (int i = 0; i < arrayOfClassInfo.length; i++) {
/*  77 */           ((List)localObject).add(this.vm.referenceType(arrayOfClassInfo[i].typeID, arrayOfClassInfo[i].refTypeTag));
/*     */         }
/*     */ 
/*  80 */         localObject = Collections.unmodifiableList((List)localObject);
/*  81 */         if (localCache != null) {
/*  82 */           localCache.visibleClasses = ((List)localObject);
/*  83 */           if ((this.vm.traceFlags & 0x10) != 0)
/*  84 */             this.vm.printTrace(description() + " temporarily caching visible classes (count = " + ((List)localObject)
/*  86 */               .size() + ")");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/*  91 */       throw localJDWPException.toJDIException();
/*     */     }
/*  93 */     return localObject;
/*     */   }
/*     */ 
/*     */   Type findType(String paramString) throws ClassNotLoadedException {
/*  97 */     List localList = visibleClasses();
/*  98 */     Iterator localIterator = localList.iterator();
/*  99 */     while (localIterator.hasNext()) {
/* 100 */       localObject = (ReferenceType)localIterator.next();
/* 101 */       if (((ReferenceType)localObject).signature().equals(paramString)) {
/* 102 */         return localObject;
/*     */       }
/*     */     }
/* 105 */     Object localObject = new JNITypeParser(paramString);
/*     */ 
/* 107 */     throw new ClassNotLoadedException(((JNITypeParser)localObject).typeName(), "Class " + ((JNITypeParser)localObject)
/* 107 */       .typeName() + " not loaded");
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 111 */     return 108;
/*     */   }
/*     */ 
/*     */   private static class Cache extends ObjectReferenceImpl.Cache
/*     */   {
/*  36 */     List<ReferenceType> visibleClasses = null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ClassLoaderReferenceImpl
 * JD-Core Version:    0.6.2
 */