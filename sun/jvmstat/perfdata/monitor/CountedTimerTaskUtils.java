/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.util.Timer;
/*    */ 
/*    */ public class CountedTimerTaskUtils
/*    */ {
/*    */   private static final boolean DEBUG = false;
/*    */ 
/*    */   public static void reschedule(Timer paramTimer, CountedTimerTask paramCountedTimerTask1, CountedTimerTask paramCountedTimerTask2, int paramInt1, int paramInt2)
/*    */   {
/* 57 */     long l1 = System.currentTimeMillis();
/* 58 */     long l2 = paramCountedTimerTask1.scheduledExecutionTime();
/* 59 */     long l3 = l1 - l2;
/*    */ 
/* 73 */     long l4 = 0L;
/* 74 */     if (paramCountedTimerTask1.executionCount() > 0L) {
/* 75 */       long l5 = paramInt2 - l3;
/* 76 */       l4 = l5 >= 0L ? l5 : 0L;
/*    */     }
/*    */ 
/* 85 */     paramTimer.schedule(paramCountedTimerTask2, l4, paramInt2);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils
 * JD-Core Version:    0.6.2
 */