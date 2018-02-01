/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaClass;
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class HistogramQuery extends QueryHandler
/*    */ {
/*    */   public void run()
/*    */   {
/* 45 */     JavaClass[] arrayOfJavaClass = this.snapshot.getClassesArray();
/*    */     Object localObject;
/* 47 */     if (this.query.equals("count"))
/* 48 */       localObject = new Comparator()
/*    */       {
/*    */         public int compare(JavaClass paramAnonymousJavaClass1, JavaClass paramAnonymousJavaClass2) {
/* 51 */           long l = paramAnonymousJavaClass2.getInstancesCount(false) - paramAnonymousJavaClass1
/* 51 */             .getInstancesCount(false);
/*    */ 
/* 52 */           return l < 0L ? -1 : l == 0L ? 0 : 1;
/*    */         }
/*    */       };
/* 55 */     else if (this.query.equals("class")) {
/* 56 */       localObject = new Comparator() {
/*    */         public int compare(JavaClass paramAnonymousJavaClass1, JavaClass paramAnonymousJavaClass2) {
/* 58 */           return paramAnonymousJavaClass1.getName().compareTo(paramAnonymousJavaClass2.getName());
/*    */         }
/*    */       };
/*    */     }
/*    */     else {
/* 63 */       localObject = new Comparator()
/*    */       {
/*    */         public int compare(JavaClass paramAnonymousJavaClass1, JavaClass paramAnonymousJavaClass2) {
/* 66 */           long l = paramAnonymousJavaClass2.getTotalInstanceSize() - paramAnonymousJavaClass1
/* 66 */             .getTotalInstanceSize();
/* 67 */           return l < 0L ? -1 : l == 0L ? 0 : 1;
/*    */         }
/*    */       };
/*    */     }
/* 71 */     Arrays.sort(arrayOfJavaClass, (Comparator)localObject);
/*    */ 
/* 73 */     startHtml("Heap Histogram");
/*    */ 
/* 75 */     this.out.println("<p align='center'>");
/* 76 */     this.out.println("<b><a href='/'>All Classes (excluding platform)</a></b>");
/* 77 */     this.out.println("</p>");
/*    */ 
/* 79 */     this.out.println("<table align=center border=1>");
/* 80 */     this.out.println("<tr><th><a href='/histo/class'>Class</a></th>");
/* 81 */     this.out.println("<th><a href='/histo/count'>Instance Count</a></th>");
/* 82 */     this.out.println("<th><a href='/histo/size'>Total Size</a></th></tr>");
/* 83 */     for (int i = 0; i < arrayOfJavaClass.length; i++) {
/* 84 */       JavaClass localJavaClass = arrayOfJavaClass[i];
/* 85 */       this.out.println("<tr><td>");
/* 86 */       printClass(localJavaClass);
/* 87 */       this.out.println("</td>");
/* 88 */       this.out.println("<td>");
/* 89 */       this.out.println(localJavaClass.getInstancesCount(false));
/* 90 */       this.out.println("</td>");
/* 91 */       this.out.println("<td>");
/* 92 */       this.out.println(localJavaClass.getTotalInstanceSize());
/* 93 */       this.out.println("</td></tr>");
/*    */     }
/* 95 */     this.out.println("</table>");
/*    */ 
/* 97 */     endHtml();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.HistogramQuery
 * JD-Core Version:    0.6.2
 */