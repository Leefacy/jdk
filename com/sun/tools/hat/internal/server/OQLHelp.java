/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ class OQLHelp extends QueryHandler
/*    */ {
/*    */   public void run()
/*    */   {
/* 49 */     Object localObject = getClass().getResourceAsStream("/com/sun/tools/hat/resources/oqlhelp.html");
/* 50 */     int i = -1;
/*    */     try {
/* 52 */       localObject = new BufferedInputStream((InputStream)localObject);
/* 53 */       while ((i = ((InputStream)localObject).read()) != -1)
/* 54 */         this.out.print((char)i);
/*    */     }
/*    */     catch (Exception localException) {
/* 57 */       printException(localException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.OQLHelp
 * JD-Core Version:    0.6.2
 */