/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ class AllClassesQuery extends QueryHandler
/*     */ {
/*     */   boolean excludePlatform;
/*     */   boolean oqlSupported;
/*     */ 
/*     */   public AllClassesQuery(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  50 */     this.excludePlatform = paramBoolean1;
/*  51 */     this.oqlSupported = paramBoolean2;
/*     */   }
/*     */ 
/*     */   public void run() {
/*  55 */     if (this.excludePlatform)
/*  56 */       startHtml("All Classes (excluding platform)");
/*     */     else {
/*  58 */       startHtml("All Classes (including platform)");
/*     */     }
/*     */ 
/*  61 */     Iterator localIterator = this.snapshot.getClasses();
/*  62 */     Object localObject = null;
/*  63 */     while (localIterator.hasNext()) {
/*  64 */       JavaClass localJavaClass = (JavaClass)localIterator.next();
/*  65 */       if ((!this.excludePlatform) || (!PlatformClasses.isPlatformClass(localJavaClass)))
/*     */       {
/*  69 */         String str1 = localJavaClass.getName();
/*  70 */         int i = str1.lastIndexOf(".");
/*     */         String str2;
/*  72 */         if (str1.startsWith("["))
/*  73 */           str2 = "<Arrays>";
/*  74 */         else if (i == -1)
/*  75 */           str2 = "<Default Package>";
/*     */         else {
/*  77 */           str2 = str1.substring(0, i);
/*     */         }
/*  79 */         if (!str2.equals(localObject)) {
/*  80 */           this.out.print("<h2>Package ");
/*  81 */           print(str2);
/*  82 */           this.out.println("</h2>");
/*     */         }
/*  84 */         localObject = str2;
/*  85 */         printClass(localJavaClass);
/*  86 */         if (localJavaClass.getId() != -1L) {
/*  87 */           print(" [" + localJavaClass.getIdString() + "]");
/*     */         }
/*  89 */         this.out.println("<br>");
/*     */       }
/*     */     }
/*  92 */     this.out.println("<h2>Other Queries</h2>");
/*  93 */     this.out.println("<ul>");
/*     */ 
/*  95 */     this.out.println("<li>");
/*  96 */     printAnchorStart();
/*  97 */     if (this.excludePlatform) {
/*  98 */       this.out.print("allClassesWithPlatform/\">");
/*  99 */       print("All classes including platform");
/*     */     } else {
/* 101 */       this.out.print("\">");
/* 102 */       print("All classes excluding platform");
/*     */     }
/* 104 */     this.out.println("</a>");
/*     */ 
/* 106 */     this.out.println("<li>");
/* 107 */     printAnchorStart();
/* 108 */     this.out.print("showRoots/\">");
/* 109 */     print("Show all members of the rootset");
/* 110 */     this.out.println("</a>");
/*     */ 
/* 112 */     this.out.println("<li>");
/* 113 */     printAnchorStart();
/* 114 */     this.out.print("showInstanceCounts/includePlatform/\">");
/* 115 */     print("Show instance counts for all classes (including platform)");
/* 116 */     this.out.println("</a>");
/*     */ 
/* 118 */     this.out.println("<li>");
/* 119 */     printAnchorStart();
/* 120 */     this.out.print("showInstanceCounts/\">");
/* 121 */     print("Show instance counts for all classes (excluding platform)");
/* 122 */     this.out.println("</a>");
/*     */ 
/* 124 */     this.out.println("<li>");
/* 125 */     printAnchorStart();
/* 126 */     this.out.print("histo/\">");
/* 127 */     print("Show heap histogram");
/* 128 */     this.out.println("</a>");
/*     */ 
/* 130 */     this.out.println("<li>");
/* 131 */     printAnchorStart();
/* 132 */     this.out.print("finalizerSummary/\">");
/* 133 */     print("Show finalizer summary");
/* 134 */     this.out.println("</a>");
/*     */ 
/* 136 */     if (this.oqlSupported) {
/* 137 */       this.out.println("<li>");
/* 138 */       printAnchorStart();
/* 139 */       this.out.print("oql/\">");
/* 140 */       print("Execute Object Query Language (OQL) query");
/* 141 */       this.out.println("</a>");
/*     */     }
/*     */ 
/* 144 */     this.out.println("</ul>");
/*     */ 
/* 146 */     endHtml();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.AllClassesQuery
 * JD-Core Version:    0.6.2
 */