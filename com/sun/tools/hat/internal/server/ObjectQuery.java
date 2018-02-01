/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaField;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObjectArray;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.JavaValueArray;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.model.StackTrace;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ class ObjectQuery extends ClassQuery
/*     */ {
/*     */   public void run()
/*     */   {
/*  54 */     startHtml("Object at " + this.query);
/*  55 */     long l = parseHex(this.query);
/*  56 */     JavaHeapObject localJavaHeapObject = this.snapshot.findThing(l);
/*     */ 
/*  63 */     if (localJavaHeapObject == null) {
/*  64 */       error("object not found");
/*  65 */     } else if ((localJavaHeapObject instanceof JavaClass)) {
/*  66 */       printFullClass((JavaClass)localJavaHeapObject);
/*  67 */     } else if ((localJavaHeapObject instanceof JavaValueArray)) {
/*  68 */       print(((JavaValueArray)localJavaHeapObject).valueString(true));
/*  69 */       printAllocationSite(localJavaHeapObject);
/*  70 */       printReferencesTo(localJavaHeapObject);
/*  71 */     } else if ((localJavaHeapObject instanceof JavaObjectArray)) {
/*  72 */       printFullObjectArray((JavaObjectArray)localJavaHeapObject);
/*  73 */       printAllocationSite(localJavaHeapObject);
/*  74 */       printReferencesTo(localJavaHeapObject);
/*  75 */     } else if ((localJavaHeapObject instanceof JavaObject)) {
/*  76 */       printFullObject((JavaObject)localJavaHeapObject);
/*  77 */       printAllocationSite(localJavaHeapObject);
/*  78 */       printReferencesTo(localJavaHeapObject);
/*     */     }
/*     */     else {
/*  81 */       print(localJavaHeapObject.toString());
/*  82 */       printReferencesTo(localJavaHeapObject);
/*     */     }
/*  84 */     endHtml();
/*     */   }
/*     */ 
/*     */   private void printFullObject(JavaObject paramJavaObject)
/*     */   {
/*  89 */     this.out.print("<h1>instance of ");
/*  90 */     print(paramJavaObject.toString());
/*  91 */     this.out.print(" <small>(" + paramJavaObject.getSize() + " bytes)</small>");
/*  92 */     this.out.println("</h1>\n");
/*     */ 
/*  94 */     this.out.println("<h2>Class:</h2>");
/*  95 */     printClass(paramJavaObject.getClazz());
/*     */ 
/*  97 */     this.out.println("<h2>Instance data members:</h2>");
/*  98 */     JavaThing[] arrayOfJavaThing = paramJavaObject.getFields();
/*  99 */     final JavaField[] arrayOfJavaField = paramJavaObject.getClazz().getFieldsForInstance();
/* 100 */     Integer[] arrayOfInteger = new Integer[arrayOfJavaThing.length];
/* 101 */     for (int i = 0; i < arrayOfJavaThing.length; i++) {
/* 102 */       arrayOfInteger[i] = new Integer(i);
/*     */     }
/* 104 */     ArraySorter.sort(arrayOfInteger, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/* 106 */         JavaField localJavaField1 = arrayOfJavaField[((Integer)paramAnonymousObject1).intValue()];
/* 107 */         JavaField localJavaField2 = arrayOfJavaField[((Integer)paramAnonymousObject2).intValue()];
/* 108 */         return localJavaField1.getName().compareTo(localJavaField2.getName());
/*     */       }
/*     */     });
/* 111 */     for (i = 0; i < arrayOfJavaThing.length; i++) {
/* 112 */       int j = arrayOfInteger[i].intValue();
/* 113 */       printField(arrayOfJavaField[j]);
/* 114 */       this.out.print(" : ");
/* 115 */       printThing(arrayOfJavaThing[j]);
/* 116 */       this.out.println("<br>");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printFullObjectArray(JavaObjectArray paramJavaObjectArray) {
/* 121 */     JavaThing[] arrayOfJavaThing = paramJavaObjectArray.getElements();
/* 122 */     this.out.println("<h1>Array of " + arrayOfJavaThing.length + " objects</h1>");
/*     */ 
/* 124 */     this.out.println("<h2>Class:</h2>");
/* 125 */     printClass(paramJavaObjectArray.getClazz());
/*     */ 
/* 127 */     this.out.println("<h2>Values</h2>");
/* 128 */     for (int i = 0; i < arrayOfJavaThing.length; i++) {
/* 129 */       this.out.print("" + i + " : ");
/* 130 */       printThing(arrayOfJavaThing[i]);
/* 131 */       this.out.println("<br>");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printAllocationSite(JavaHeapObject paramJavaHeapObject)
/*     */   {
/* 139 */     StackTrace localStackTrace = paramJavaHeapObject.getAllocatedFrom();
/* 140 */     if ((localStackTrace == null) || (localStackTrace.getFrames().length == 0)) {
/* 141 */       return;
/*     */     }
/* 143 */     this.out.println("<h2>Object allocated from:</h2>");
/* 144 */     printStackTrace(localStackTrace);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.ObjectQuery
 * JD-Core Version:    0.6.2
 */