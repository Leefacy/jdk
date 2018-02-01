/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.Root;
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import com.sun.tools.hat.internal.model.StackTrace;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ class RootStackQuery extends QueryHandler
/*    */ {
/*    */   public void run()
/*    */   {
/* 51 */     int i = (int)parseHex(this.query);
/* 52 */     Root localRoot = this.snapshot.getRootAt(i);
/* 53 */     if (localRoot == null) {
/* 54 */       error("Root at " + i + " not found");
/* 55 */       return;
/*    */     }
/* 57 */     StackTrace localStackTrace = localRoot.getStackTrace();
/* 58 */     if ((localStackTrace == null) || (localStackTrace.getFrames().length == 0)) {
/* 59 */       error("No stack trace for " + localRoot.getDescription());
/* 60 */       return;
/*    */     }
/* 62 */     startHtml("Stack Trace for " + localRoot.getDescription());
/* 63 */     this.out.println("<p>");
/* 64 */     printStackTrace(localStackTrace);
/* 65 */     this.out.println("</p>");
/* 66 */     endHtml();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.RootStackQuery
 * JD-Core Version:    0.6.2
 */