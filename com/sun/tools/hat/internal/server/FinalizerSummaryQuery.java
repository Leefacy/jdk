/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class FinalizerSummaryQuery extends QueryHandler
/*     */ {
/*     */   public void run()
/*     */   {
/*  40 */     Enumeration localEnumeration = this.snapshot.getFinalizerObjects();
/*  41 */     startHtml("Finalizer Summary");
/*     */ 
/*  43 */     this.out.println("<p align='center'>");
/*  44 */     this.out.println("<b><a href='/'>All Classes (excluding platform)</a></b>");
/*  45 */     this.out.println("</p>");
/*     */ 
/*  47 */     printFinalizerSummary(localEnumeration);
/*  48 */     endHtml();
/*     */   }
/*     */ 
/*     */   private void printFinalizerSummary(Enumeration paramEnumeration)
/*     */   {
/*  78 */     int i = 0;
/*  79 */     HashMap localHashMap = new HashMap();
/*     */ 
/*  81 */     while (paramEnumeration.hasMoreElements()) {
/*  82 */       localObject = (JavaHeapObject)paramEnumeration.nextElement();
/*  83 */       i++;
/*  84 */       JavaClass localJavaClass = ((JavaHeapObject)localObject).getClazz();
/*  85 */       if (!localHashMap.containsKey(localJavaClass)) {
/*  86 */         localHashMap.put(localJavaClass, new HistogramElement(localJavaClass));
/*     */       }
/*  88 */       HistogramElement localHistogramElement = (HistogramElement)localHashMap.get(localJavaClass);
/*  89 */       localHistogramElement.updateCount();
/*     */     }
/*     */ 
/*  92 */     this.out.println("<p align='center'>");
/*  93 */     this.out.println("<b>");
/*  94 */     this.out.println("Total ");
/*  95 */     if (i != 0)
/*  96 */       this.out.print("<a href='/finalizerObjects/'>instances</a>");
/*     */     else {
/*  98 */       this.out.print("instances");
/*     */     }
/* 100 */     this.out.println(" pending finalization: ");
/* 101 */     this.out.print(i);
/* 102 */     this.out.println("</b></p><hr>");
/*     */ 
/* 104 */     if (i == 0) {
/* 105 */       return;
/*     */     }
/*     */ 
/* 109 */     Object localObject = new HistogramElement[localHashMap.size()];
/* 110 */     localHashMap.values().toArray((Object[])localObject);
/* 111 */     Arrays.sort((Object[])localObject, new Comparator() {
/*     */       public int compare(FinalizerSummaryQuery.HistogramElement paramAnonymousHistogramElement1, FinalizerSummaryQuery.HistogramElement paramAnonymousHistogramElement2) {
/* 113 */         return paramAnonymousHistogramElement1.compare(paramAnonymousHistogramElement2);
/*     */       }
/*     */     });
/* 117 */     this.out.println("<table border=1 align=center>");
/* 118 */     this.out.println("<tr><th>Count</th><th>Class</th></tr>");
/* 119 */     for (int j = 0; j < localObject.length; j++) {
/* 120 */       this.out.println("<tr><td>");
/* 121 */       this.out.println(localObject[j].getCount());
/* 122 */       this.out.println("</td><td>");
/* 123 */       printClass(localObject[j].getClazz());
/* 124 */       this.out.println("</td><tr>");
/*     */     }
/* 126 */     this.out.println("</table>");
/*     */   }
/*     */ 
/*     */   private static class HistogramElement
/*     */   {
/*     */     private JavaClass clazz;
/*     */     private long count;
/*     */ 
/*     */     public HistogramElement(JavaClass paramJavaClass)
/*     */     {
/*  53 */       this.clazz = paramJavaClass;
/*     */     }
/*     */ 
/*     */     public void updateCount() {
/*  57 */       this.count += 1L;
/*     */     }
/*     */ 
/*     */     public int compare(HistogramElement paramHistogramElement) {
/*  61 */       long l = paramHistogramElement.count - this.count;
/*  62 */       return l > 0L ? 1 : l == 0L ? 0 : -1;
/*     */     }
/*     */ 
/*     */     public JavaClass getClazz() {
/*  66 */       return this.clazz;
/*     */     }
/*     */ 
/*     */     public long getCount() {
/*  70 */       return this.count;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.FinalizerSummaryQuery
 * JD-Core Version:    0.6.2
 */