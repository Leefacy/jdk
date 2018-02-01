/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.util.TimerTask;
/*    */ 
/*    */ public class CountedTimerTask extends TimerTask
/*    */ {
/*    */   volatile long executionCount;
/*    */ 
/*    */   public long executionCount()
/*    */   {
/* 42 */     return this.executionCount;
/*    */   }
/*    */ 
/*    */   public void run() {
/* 46 */     this.executionCount += 1L;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.CountedTimerTask
 * JD-Core Version:    0.6.2
 */