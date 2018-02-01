/*     */ package sun.applet;
/*     */ 
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ class AppContextCreator extends Thread
/*     */ {
/* 858 */   Object syncObject = new Object();
/* 859 */   AppContext appContext = null;
/* 860 */   volatile boolean created = false;
/*     */ 
/*     */   AppContextCreator(ThreadGroup paramThreadGroup) {
/* 863 */     super(paramThreadGroup, "AppContextCreator");
/*     */   }
/*     */ 
/*     */   public void run() {
/* 867 */     this.appContext = SunToolkit.createNewAppContext();
/* 868 */     this.created = true;
/* 869 */     synchronized (this.syncObject) {
/* 870 */       this.syncObject.notifyAll();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.applet.AppContextCreator
 * JD-Core Version:    0.6.2
 */