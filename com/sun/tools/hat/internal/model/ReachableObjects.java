/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ReachableObjects
/*     */ {
/*     */   private JavaHeapObject root;
/*     */   private JavaThing[] reachables;
/*     */   private String[] excludedFields;
/*     */   private String[] usedFields;
/*     */   private long totalSize;
/*     */ 
/*     */   public ReachableObjects(JavaHeapObject paramJavaHeapObject, final ReachableExcludes paramReachableExcludes)
/*     */   {
/*  49 */     this.root = paramJavaHeapObject;
/*     */ 
/*  51 */     final Hashtable localHashtable1 = new Hashtable();
/*  52 */     final Hashtable localHashtable2 = new Hashtable();
/*  53 */     final Hashtable localHashtable3 = new Hashtable();
/*  54 */     AbstractJavaHeapObjectVisitor local1 = new AbstractJavaHeapObjectVisitor()
/*     */     {
/*     */       public void visit(JavaHeapObject paramAnonymousJavaHeapObject) {
/*  57 */         if ((paramAnonymousJavaHeapObject != null) && (paramAnonymousJavaHeapObject.getSize() > 0) && (localHashtable1.get(paramAnonymousJavaHeapObject) == null)) {
/*  58 */           localHashtable1.put(paramAnonymousJavaHeapObject, paramAnonymousJavaHeapObject);
/*  59 */           paramAnonymousJavaHeapObject.visitReferencedObjects(this);
/*     */         }
/*     */       }
/*     */ 
/*     */       public boolean mightExclude() {
/*  64 */         return paramReachableExcludes != null;
/*     */       }
/*     */ 
/*     */       public boolean exclude(JavaClass paramAnonymousJavaClass, JavaField paramAnonymousJavaField) {
/*  68 */         if (paramReachableExcludes == null) {
/*  69 */           return false;
/*     */         }
/*  71 */         String str = paramAnonymousJavaClass.getName() + "." + paramAnonymousJavaField.getName();
/*  72 */         if (paramReachableExcludes.isExcluded(str)) {
/*  73 */           localHashtable2.put(str, str);
/*  74 */           return true;
/*     */         }
/*  76 */         localHashtable3.put(str, str);
/*  77 */         return false;
/*     */       }
/*     */     };
/*  83 */     local1.visit(paramJavaHeapObject);
/*  84 */     localHashtable1.remove(paramJavaHeapObject);
/*     */ 
/*  87 */     JavaThing[] arrayOfJavaThing = new JavaThing[localHashtable1.size()];
/*  88 */     int i = 0;
/*  89 */     for (Enumeration localEnumeration = localHashtable1.elements(); localEnumeration.hasMoreElements(); ) {
/*  90 */       arrayOfJavaThing[(i++)] = ((JavaThing)localEnumeration.nextElement());
/*     */     }
/*  92 */     ArraySorter.sort(arrayOfJavaThing, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  94 */         JavaThing localJavaThing1 = (JavaThing)paramAnonymousObject1;
/*  95 */         JavaThing localJavaThing2 = (JavaThing)paramAnonymousObject2;
/*  96 */         int i = localJavaThing2.getSize() - localJavaThing1.getSize();
/*  97 */         if (i != 0) {
/*  98 */           return i;
/*     */         }
/* 100 */         return localJavaThing1.compareTo(localJavaThing2);
/*     */       }
/*     */     });
/* 103 */     this.reachables = arrayOfJavaThing;
/*     */ 
/* 105 */     this.totalSize = paramJavaHeapObject.getSize();
/* 106 */     for (i = 0; i < arrayOfJavaThing.length; i++) {
/* 107 */       this.totalSize += arrayOfJavaThing[i].getSize();
/*     */     }
/*     */ 
/* 110 */     this.excludedFields = getElements(localHashtable2);
/* 111 */     this.usedFields = getElements(localHashtable3);
/*     */   }
/*     */ 
/*     */   public JavaHeapObject getRoot() {
/* 115 */     return this.root;
/*     */   }
/*     */ 
/*     */   public JavaThing[] getReachables() {
/* 119 */     return this.reachables;
/*     */   }
/*     */ 
/*     */   public long getTotalSize() {
/* 123 */     return this.totalSize;
/*     */   }
/*     */ 
/*     */   public String[] getExcludedFields() {
/* 127 */     return this.excludedFields;
/*     */   }
/*     */ 
/*     */   public String[] getUsedFields() {
/* 131 */     return this.usedFields;
/*     */   }
/*     */ 
/*     */   private String[] getElements(Hashtable paramHashtable) {
/* 135 */     Object[] arrayOfObject = paramHashtable.keySet().toArray();
/* 136 */     int i = arrayOfObject.length;
/* 137 */     String[] arrayOfString = new String[i];
/* 138 */     System.arraycopy(arrayOfObject, 0, arrayOfString, 0, i);
/* 139 */     ArraySorter.sortArrayOfStrings(arrayOfString);
/* 140 */     return arrayOfString;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.ReachableObjects
 * JD-Core Version:    0.6.2
 */