/*    */ package com.sun.tools.internal.ws;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.WsimportTool;
/*    */ 
/*    */ public class WsImport
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Throwable
/*    */   {
/* 42 */     System.exit(Invoker.invoke("com.sun.tools.internal.ws.wscompile.WsimportTool", args));
/*    */   }
/*    */ 
/*    */   public static int doMain(String[] args)
/*    */     throws Throwable
/*    */   {
/* 59 */     return new WsimportTool(System.out).run(args) ? 0 : 1;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.WsImport
 * JD-Core Version:    0.6.2
 */