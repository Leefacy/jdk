/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ public class FinalizerObjectsQuery extends QueryHandler
/*    */ {
/*    */   public void run()
/*    */   {
/* 40 */     Enumeration localEnumeration = this.snapshot.getFinalizerObjects();
/* 41 */     startHtml("Objects pending finalization");
/*    */ 
/* 43 */     this.out.println("<a href='/finalizerSummary/'>Finalizer summary</a>");
/*    */ 
/* 45 */     this.out.println("<h1>Objects pending finalization</h1>");
/*    */ 
/* 47 */     while (localEnumeration.hasMoreElements()) {
/* 48 */       printThing((JavaHeapObject)localEnumeration.nextElement());
/* 49 */       this.out.println("<br>");
/*    */     }
/*    */ 
/* 52 */     endHtml();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.FinalizerObjectsQuery
 * JD-Core Version:    0.6.2
 */