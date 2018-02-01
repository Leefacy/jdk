/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.AbstractJavaHeapObjectVisitor;
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class RefsByTypeQuery extends QueryHandler
/*     */ {
/*     */   public void run()
/*     */   {
/*  44 */     JavaClass localJavaClass1 = this.snapshot.findClass(this.query);
/*  45 */     if (localJavaClass1 == null) {
/*  46 */       error("class not found: " + this.query);
/*     */     } else {
/*  48 */       HashMap localHashMap1 = new HashMap();
/*  49 */       final HashMap localHashMap2 = new HashMap();
/*  50 */       Enumeration localEnumeration1 = localJavaClass1.getInstances(false);
/*  51 */       while (localEnumeration1.hasMoreElements()) {
/*  52 */         JavaHeapObject localJavaHeapObject1 = (JavaHeapObject)localEnumeration1.nextElement();
/*  53 */         if (localJavaHeapObject1.getId() != -1L)
/*     */         {
/*  56 */           Enumeration localEnumeration2 = localJavaHeapObject1.getReferers();
/*  57 */           while (localEnumeration2.hasMoreElements()) {
/*  58 */             JavaHeapObject localJavaHeapObject2 = (JavaHeapObject)localEnumeration2.nextElement();
/*  59 */             JavaClass localJavaClass2 = localJavaHeapObject2.getClazz();
/*  60 */             if (localJavaClass2 == null) {
/*  61 */               System.out.println("null class for " + localJavaHeapObject2);
/*     */             }
/*     */             else {
/*  64 */               Long localLong = (Long)localHashMap1.get(localJavaClass2);
/*  65 */               if (localLong == null)
/*  66 */                 localLong = new Long(1L);
/*     */               else {
/*  68 */                 localLong = new Long(localLong.longValue() + 1L);
/*     */               }
/*  70 */               localHashMap1.put(localJavaClass2, localLong);
/*     */             }
/*     */           }
/*  72 */           localJavaHeapObject1.visitReferencedObjects(new AbstractJavaHeapObjectVisitor()
/*     */           {
/*     */             public void visit(JavaHeapObject paramAnonymousJavaHeapObject) {
/*  75 */               JavaClass localJavaClass = paramAnonymousJavaHeapObject.getClazz();
/*  76 */               Long localLong = (Long)localHashMap2.get(localJavaClass);
/*  77 */               if (localLong == null)
/*  78 */                 localLong = new Long(1L);
/*     */               else {
/*  80 */                 localLong = new Long(localLong.longValue() + 1L);
/*     */               }
/*  82 */               localHashMap2.put(localJavaClass, localLong);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */ 
/*  88 */       startHtml("References by Type");
/*  89 */       this.out.println("<p align='center'>");
/*  90 */       printClass(localJavaClass1);
/*  91 */       if (localJavaClass1.getId() != -1L) {
/*  92 */         println("[" + localJavaClass1.getIdString() + "]");
/*     */       }
/*  94 */       this.out.println("</p>");
/*     */ 
/*  96 */       if (localHashMap1.size() != 0) {
/*  97 */         this.out.println("<h3 align='center'>Referrers by Type</h3>");
/*  98 */         print(localHashMap1);
/*     */       }
/*     */ 
/* 101 */       if (localHashMap2.size() != 0) {
/* 102 */         this.out.println("<h3 align='center'>Referees by Type</h3>");
/* 103 */         print(localHashMap2);
/*     */       }
/*     */ 
/* 106 */       endHtml();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void print(final Map<JavaClass, Long> paramMap) {
/* 111 */     this.out.println("<table border='1' align='center'>");
/* 112 */     Set localSet = paramMap.keySet();
/* 113 */     JavaClass[] arrayOfJavaClass = new JavaClass[localSet.size()];
/* 114 */     localSet.toArray(arrayOfJavaClass);
/* 115 */     Arrays.sort(arrayOfJavaClass, new Comparator() {
/*     */       public int compare(JavaClass paramAnonymousJavaClass1, JavaClass paramAnonymousJavaClass2) {
/* 117 */         Long localLong1 = (Long)paramMap.get(paramAnonymousJavaClass1);
/* 118 */         Long localLong2 = (Long)paramMap.get(paramAnonymousJavaClass2);
/* 119 */         return localLong2.compareTo(localLong1);
/*     */       }
/*     */     });
/* 123 */     this.out.println("<tr><th>Class</th><th>Count</th></tr>");
/* 124 */     for (int i = 0; i < arrayOfJavaClass.length; i++) {
/* 125 */       JavaClass localJavaClass = arrayOfJavaClass[i];
/* 126 */       this.out.println("<tr><td>");
/* 127 */       this.out.print("<a href='/refsByType/");
/* 128 */       print(localJavaClass.getIdString());
/* 129 */       this.out.print("'>");
/* 130 */       print(localJavaClass.getName());
/* 131 */       this.out.println("</a>");
/* 132 */       this.out.println("</td><td>");
/* 133 */       this.out.println(paramMap.get(localJavaClass));
/* 134 */       this.out.println("</td></tr>");
/*     */     }
/* 136 */     this.out.println("</table>");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.RefsByTypeQuery
 * JD-Core Version:    0.6.2
 */