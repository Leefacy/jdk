/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.ReferenceChain;
/*     */ import com.sun.tools.hat.internal.model.Root;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ class RootsQuery extends QueryHandler
/*     */ {
/*     */   private boolean includeWeak;
/*     */ 
/*     */   public RootsQuery(boolean paramBoolean)
/*     */   {
/*  52 */     this.includeWeak = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void run() {
/*  56 */     long l = parseHex(this.query);
/*  57 */     JavaHeapObject localJavaHeapObject1 = this.snapshot.findThing(l);
/*  58 */     if (localJavaHeapObject1 == null) {
/*  59 */       startHtml("Object not found for rootset");
/*  60 */       error("object not found");
/*  61 */       endHtml();
/*  62 */       return;
/*     */     }
/*  64 */     if (this.includeWeak) {
/*  65 */       startHtml("Rootset references to " + localJavaHeapObject1 + " (includes weak refs)");
/*     */     }
/*     */     else {
/*  68 */       startHtml("Rootset references to " + localJavaHeapObject1 + " (excludes weak refs)");
/*     */     }
/*     */ 
/*  71 */     this.out.flush();
/*     */ 
/*  74 */     ReferenceChain[] arrayOfReferenceChain = this.snapshot
/*  74 */       .rootsetReferencesTo(localJavaHeapObject1, this.includeWeak);
/*     */ 
/*  75 */     ArraySorter.sort(arrayOfReferenceChain, new Comparer() {
/*     */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  77 */         ReferenceChain localReferenceChain1 = (ReferenceChain)paramAnonymousObject1;
/*  78 */         ReferenceChain localReferenceChain2 = (ReferenceChain)paramAnonymousObject2;
/*  79 */         Root localRoot1 = localReferenceChain1.getObj().getRoot();
/*  80 */         Root localRoot2 = localReferenceChain2.getObj().getRoot();
/*  81 */         int i = localRoot1.getType() - localRoot2.getType();
/*  82 */         if (i != 0) {
/*  83 */           return -i;
/*     */         }
/*  85 */         return localReferenceChain1.getDepth() - localReferenceChain2.getDepth();
/*     */       }
/*     */     });
/*  89 */     this.out.print("<h1>References to ");
/*  90 */     printThing(localJavaHeapObject1);
/*  91 */     this.out.println("</h1>");
/*  92 */     int i = 0;
/*  93 */     for (int j = 0; j < arrayOfReferenceChain.length; j++) {
/*  94 */       Object localObject = arrayOfReferenceChain[j];
/*  95 */       Root localRoot = ((ReferenceChain)localObject).getObj().getRoot();
/*  96 */       if (localRoot.getType() != i) {
/*  97 */         i = localRoot.getType();
/*  98 */         this.out.print("<h2>");
/*  99 */         print(localRoot.getTypeName() + " References");
/* 100 */         this.out.println("</h2>");
/*     */       }
/* 102 */       this.out.print("<h3>");
/* 103 */       printRoot(localRoot);
/* 104 */       if (localRoot.getReferer() != null) {
/* 105 */         this.out.print("<small> (from ");
/* 106 */         printThingAnchorTag(localRoot.getReferer().getId());
/* 107 */         print(localRoot.getReferer().toString());
/* 108 */         this.out.print(")</a></small>");
/*     */       }
/*     */ 
/* 111 */       this.out.print(" :</h3>");
/* 112 */       while (localObject != null) {
/* 113 */         ReferenceChain localReferenceChain = ((ReferenceChain)localObject).getNext();
/* 114 */         JavaHeapObject localJavaHeapObject2 = ((ReferenceChain)localObject).getObj();
/* 115 */         print("--> ");
/* 116 */         printThing(localJavaHeapObject2);
/* 117 */         if (localReferenceChain != null) {
/* 118 */           print(" (" + localJavaHeapObject2
/* 119 */             .describeReferenceTo(localReferenceChain
/* 119 */             .getObj(), this.snapshot) + ":)");
/*     */         }
/*     */ 
/* 122 */         this.out.println("<br>");
/* 123 */         localObject = localReferenceChain;
/*     */       }
/*     */     }
/*     */ 
/* 127 */     this.out.println("<h2>Other queries</h2>");
/*     */ 
/* 129 */     if (this.includeWeak) {
/* 130 */       printAnchorStart();
/* 131 */       this.out.print("roots/");
/* 132 */       printHex(l);
/* 133 */       this.out.print("\">");
/* 134 */       this.out.println("Exclude weak refs</a><br>");
/* 135 */       endHtml();
/*     */     }
/*     */ 
/* 138 */     if (!this.includeWeak) {
/* 139 */       printAnchorStart();
/* 140 */       this.out.print("allRoots/");
/* 141 */       printHex(l);
/* 142 */       this.out.print("\">");
/* 143 */       this.out.println("Include weak refs</a><br>");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.RootsQuery
 * JD-Core Version:    0.6.2
 */