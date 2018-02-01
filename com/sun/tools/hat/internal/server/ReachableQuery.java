/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*    */ import com.sun.tools.hat.internal.model.JavaThing;
/*    */ import com.sun.tools.hat.internal.model.ReachableObjects;
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ class ReachableQuery extends QueryHandler
/*    */ {
/*    */   public void run()
/*    */   {
/* 51 */     startHtml("Objects Reachable From " + this.query);
/* 52 */     long l1 = parseHex(this.query);
/* 53 */     JavaHeapObject localJavaHeapObject = this.snapshot.findThing(l1);
/*    */ 
/* 55 */     ReachableObjects localReachableObjects = new ReachableObjects(localJavaHeapObject, this.snapshot
/* 55 */       .getReachableExcludes());
/*    */ 
/* 57 */     long l2 = localReachableObjects.getTotalSize();
/* 58 */     JavaThing[] arrayOfJavaThing = localReachableObjects.getReachables();
/* 59 */     long l3 = arrayOfJavaThing.length;
/*    */ 
/* 61 */     this.out.print("<strong>");
/* 62 */     printThing(localJavaHeapObject);
/* 63 */     this.out.println("</strong><br>");
/* 64 */     this.out.println("<br>");
/* 65 */     for (int i = 0; i < arrayOfJavaThing.length; i++) {
/* 66 */       printThing(arrayOfJavaThing[i]);
/* 67 */       this.out.println("<br>");
/*    */     }
/*    */ 
/* 70 */     printFields(localReachableObjects.getUsedFields(), "Data Members Followed");
/* 71 */     printFields(localReachableObjects.getExcludedFields(), "Excluded Data Members");
/* 72 */     this.out.println("<h2>Total of " + l3 + " instances occupying " + l2 + " bytes.</h2>");
/*    */ 
/* 74 */     endHtml();
/*    */   }
/*    */ 
/*    */   private void printFields(String[] paramArrayOfString, String paramString) {
/* 78 */     if (paramArrayOfString.length == 0) {
/* 79 */       return;
/*    */     }
/* 81 */     this.out.print("<h3>");
/* 82 */     print(paramString);
/* 83 */     this.out.println("</h3>");
/*    */ 
/* 85 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 86 */       print(paramArrayOfString[i]);
/* 87 */       this.out.println("<br>");
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.ReachableQuery
 * JD-Core Version:    0.6.2
 */