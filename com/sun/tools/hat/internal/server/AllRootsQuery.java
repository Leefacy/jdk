/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.Root;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ class AllRootsQuery extends QueryHandler
/*     */ {
/*     */   public void run()
/*     */   {
/*  53 */     startHtml("All Members of the Rootset");
/*     */ 
/*  55 */     Root[] arrayOfRoot = this.snapshot.getRootsArray();
/*  56 */     ArraySorter.sort(arrayOfRoot, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  58 */         Root localRoot1 = (Root)paramAnonymousObject1;
/*  59 */         Root localRoot2 = (Root)paramAnonymousObject2;
/*  60 */         int i = localRoot1.getType() - localRoot2.getType();
/*  61 */         if (i != 0) {
/*  62 */           return -i;
/*     */         }
/*  64 */         return localRoot1.getDescription().compareTo(localRoot2.getDescription());
/*     */       }
/*     */     });
/*  68 */     int i = 0;
/*     */ 
/*  70 */     for (int j = 0; j < arrayOfRoot.length; j++) {
/*  71 */       Root localRoot = arrayOfRoot[j];
/*     */ 
/*  73 */       if (localRoot.getType() != i) {
/*  74 */         i = localRoot.getType();
/*  75 */         this.out.print("<h2>");
/*  76 */         print(localRoot.getTypeName() + " References");
/*  77 */         this.out.println("</h2>");
/*     */       }
/*     */ 
/*  80 */       printRoot(localRoot);
/*  81 */       if (localRoot.getReferer() != null) {
/*  82 */         this.out.print("<small> (from ");
/*  83 */         printThingAnchorTag(localRoot.getReferer().getId());
/*  84 */         print(localRoot.getReferer().toString());
/*  85 */         this.out.print(")</a></small>");
/*     */       }
/*  87 */       this.out.print(" :<br>");
/*     */ 
/*  89 */       JavaHeapObject localJavaHeapObject = this.snapshot.findThing(localRoot.getId());
/*  90 */       if (localJavaHeapObject != null) {
/*  91 */         print("--> ");
/*  92 */         printThing(localJavaHeapObject);
/*  93 */         this.out.println("<br>");
/*     */       }
/*     */     }
/*     */ 
/*  97 */     this.out.println("<h2>Other Queries</h2>");
/*  98 */     this.out.println("<ul>");
/*  99 */     this.out.println("<li>");
/* 100 */     printAnchorStart();
/* 101 */     this.out.print("\">");
/* 102 */     print("Show All Classes");
/* 103 */     this.out.println("</a>");
/* 104 */     this.out.println("</ul>");
/*     */ 
/* 106 */     endHtml();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.AllRootsQuery
 * JD-Core Version:    0.6.2
 */