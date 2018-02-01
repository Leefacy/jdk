/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ class InstancesCountQuery extends QueryHandler
/*     */ {
/*     */   private boolean excludePlatform;
/*     */ 
/*     */   public InstancesCountQuery(boolean paramBoolean)
/*     */   {
/*  52 */     this.excludePlatform = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void run() {
/*  56 */     if (this.excludePlatform)
/*  57 */       startHtml("Instance Counts for All Classes (excluding platform)");
/*     */     else {
/*  59 */       startHtml("Instance Counts for All Classes (including platform)");
/*     */     }
/*     */ 
/*  62 */     Object localObject1 = this.snapshot.getClassesArray();
/*  63 */     if (this.excludePlatform) {
/*  64 */       int i = 0;
/*  65 */       for (int j = 0; j < localObject1.length; j++) {
/*  66 */         if (!PlatformClasses.isPlatformClass(localObject1[j])) {
/*  67 */           localObject1[(i++)] = localObject1[j];
/*     */         }
/*     */       }
/*  70 */       JavaClass[] arrayOfJavaClass = new JavaClass[i];
/*  71 */       System.arraycopy(localObject1, 0, arrayOfJavaClass, 0, arrayOfJavaClass.length);
/*  72 */       localObject1 = arrayOfJavaClass;
/*     */     }
/*  74 */     ArraySorter.sort((Object[])localObject1, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  76 */         JavaClass localJavaClass1 = (JavaClass)paramAnonymousObject1;
/*  77 */         JavaClass localJavaClass2 = (JavaClass)paramAnonymousObject2;
/*     */ 
/*  79 */         int i = localJavaClass1.getInstancesCount(false) - localJavaClass2
/*  79 */           .getInstancesCount(false);
/*     */ 
/*  80 */         if (i != 0) {
/*  81 */           return -i;
/*     */         }
/*  83 */         String str1 = localJavaClass1.getName();
/*  84 */         String str2 = localJavaClass2.getName();
/*  85 */         if (str1.startsWith("[") != str2.startsWith("["))
/*     */         {
/*  87 */           if (str1.startsWith("[")) {
/*  88 */             return 1;
/*     */           }
/*  90 */           return -1;
/*     */         }
/*     */ 
/*  93 */         return str1.compareTo(str2);
/*     */       }
/*     */     });
/*  97 */     Object localObject2 = null;
/*  98 */     long l1 = 0L;
/*  99 */     long l2 = 0L;
/* 100 */     for (int k = 0; k < localObject1.length; k++) {
/* 101 */       Object localObject3 = localObject1[k];
/* 102 */       int m = localObject3.getInstancesCount(false);
/* 103 */       print("" + m);
/* 104 */       printAnchorStart();
/* 105 */       print("instances/" + encodeForURL(localObject1[k]));
/* 106 */       this.out.print("\"> ");
/* 107 */       if (m == 1)
/* 108 */         print("instance");
/*     */       else {
/* 110 */         print("instances");
/*     */       }
/* 112 */       this.out.print("</a> ");
/* 113 */       if (this.snapshot.getHasNewSet()) {
/* 114 */         Enumeration localEnumeration = localObject3.getInstances(false);
/* 115 */         int n = 0;
/* 116 */         while (localEnumeration.hasMoreElements()) {
/* 117 */           JavaHeapObject localJavaHeapObject = (JavaHeapObject)localEnumeration.nextElement();
/* 118 */           if (localJavaHeapObject.isNew()) {
/* 119 */             n++;
/*     */           }
/*     */         }
/* 122 */         print("(");
/* 123 */         printAnchorStart();
/* 124 */         print("newInstances/" + encodeForURL(localObject1[k]));
/* 125 */         this.out.print("\">");
/* 126 */         print("" + n + " new");
/* 127 */         this.out.print("</a>) ");
/*     */       }
/* 129 */       print("of ");
/* 130 */       printClass(localObject1[k]);
/* 131 */       this.out.println("<br>");
/* 132 */       l2 += m;
/* 133 */       l1 += localObject1[k].getTotalInstanceSize();
/*     */     }
/* 135 */     this.out.println("<h2>Total of " + l2 + " instances occupying " + l1 + " bytes.</h2>");
/*     */ 
/* 137 */     this.out.println("<h2>Other Queries</h2>");
/* 138 */     this.out.println("<ul>");
/*     */ 
/* 140 */     this.out.print("<li>");
/* 141 */     printAnchorStart();
/* 142 */     if (!this.excludePlatform) {
/* 143 */       this.out.print("showInstanceCounts/\">");
/* 144 */       print("Show instance counts for all classes (excluding platform)");
/*     */     } else {
/* 146 */       this.out.print("showInstanceCounts/includePlatform/\">");
/* 147 */       print("Show instance counts for all classes (including platform)");
/*     */     }
/* 149 */     this.out.println("</a>");
/*     */ 
/* 151 */     this.out.print("<li>");
/* 152 */     printAnchorStart();
/* 153 */     this.out.print("allClassesWithPlatform/\">");
/* 154 */     print("Show All Classes (including platform)");
/* 155 */     this.out.println("</a>");
/*     */ 
/* 157 */     this.out.print("<li>");
/* 158 */     printAnchorStart();
/* 159 */     this.out.print("\">");
/* 160 */     print("Show All Classes (excluding platform)");
/* 161 */     this.out.println("</a>");
/*     */ 
/* 163 */     this.out.println("</ul>");
/*     */ 
/* 165 */     endHtml();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.InstancesCountQuery
 * JD-Core Version:    0.6.2
 */