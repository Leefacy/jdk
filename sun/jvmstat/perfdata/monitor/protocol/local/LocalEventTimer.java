/*    */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*    */ 
/*    */ import java.util.Timer;
/*    */ 
/*    */ public class LocalEventTimer extends Timer
/*    */ {
/*    */   private static LocalEventTimer instance;
/*    */ 
/*    */   private LocalEventTimer()
/*    */   {
/* 48 */     super(true);
/*    */   }
/*    */ 
/*    */   public static synchronized LocalEventTimer getInstance()
/*    */   {
/* 57 */     if (instance == null) {
/* 58 */       instance = new LocalEventTimer();
/*    */     }
/* 60 */     return instance;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.local.LocalEventTimer
 * JD-Core Version:    0.6.2
 */