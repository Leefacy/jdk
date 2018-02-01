/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import com.sun.tools.hat.internal.oql.OQLEngine;
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ 
/*    */ public class QueryListener
/*    */   implements Runnable
/*    */ {
/*    */   private Snapshot snapshot;
/*    */   private OQLEngine engine;
/*    */   private int port;
/*    */ 
/*    */   public QueryListener(int paramInt)
/*    */   {
/* 66 */     this.port = paramInt;
/* 67 */     this.snapshot = null;
/* 68 */     this.engine = null;
/*    */   }
/*    */ 
/*    */   public void setModel(Snapshot paramSnapshot) {
/* 72 */     this.snapshot = paramSnapshot;
/* 73 */     if (OQLEngine.isOQLSupported())
/* 74 */       this.engine = new OQLEngine(paramSnapshot);
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try {
/* 80 */       waitForRequests();
/*    */     } catch (IOException localIOException) {
/* 82 */       localIOException.printStackTrace();
/* 83 */       System.exit(1);
/*    */     }
/*    */   }
/*    */ 
/*    */   private void waitForRequests() throws IOException {
/* 88 */     ServerSocket localServerSocket = new ServerSocket(this.port);
/* 89 */     Object localObject = null;
/*    */     while (true) {
/* 91 */       Socket localSocket = localServerSocket.accept();
/* 92 */       Thread localThread = new Thread(new HttpReader(localSocket, this.snapshot, this.engine));
/* 93 */       if (this.snapshot == null) {
/* 94 */         localThread.setPriority(6);
/*    */       } else {
/* 96 */         localThread.setPriority(4);
/* 97 */         if (localObject != null) {
/*    */           try {
/* 99 */             localObject.setPriority(3);
/*    */           }
/*    */           catch (Throwable localThrowable)
/*    */           {
/*    */           }
/*    */         }
/*    */       }
/* 106 */       localThread.start();
/* 107 */       localObject = localThread;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.QueryListener
 * JD-Core Version:    0.6.2
 */