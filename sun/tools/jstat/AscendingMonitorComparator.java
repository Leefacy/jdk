/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import sun.jvmstat.monitor.Monitor;
/*    */ 
/*    */ class AscendingMonitorComparator
/*    */   implements Comparator<Monitor>
/*    */ {
/*    */   public int compare(Monitor paramMonitor1, Monitor paramMonitor2)
/*    */   {
/* 39 */     String str1 = paramMonitor1.getName();
/* 40 */     String str2 = paramMonitor2.getName();
/* 41 */     return str1.compareTo(str2);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.AscendingMonitorComparator
 * JD-Core Version:    0.6.2
 */