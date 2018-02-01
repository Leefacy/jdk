/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class MonitorStatus
/*    */ {
/*    */   protected List inserted;
/*    */   protected List removed;
/*    */ 
/*    */   public MonitorStatus(List paramList1, List paramList2)
/*    */   {
/* 56 */     this.inserted = paramList1;
/* 57 */     this.removed = paramList2;
/*    */   }
/*    */ 
/*    */   public List getInserted()
/*    */   {
/* 66 */     return this.inserted;
/*    */   }
/*    */ 
/*    */   public List getRemoved()
/*    */   {
/* 75 */     return this.removed;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.MonitorStatus
 * JD-Core Version:    0.6.2
 */