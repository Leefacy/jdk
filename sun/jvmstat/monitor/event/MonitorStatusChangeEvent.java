/*    */ package sun.jvmstat.monitor.event;
/*    */ 
/*    */ import java.util.List;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ 
/*    */ public class MonitorStatusChangeEvent extends VmEvent
/*    */ {
/*    */   protected List inserted;
/*    */   protected List removed;
/*    */ 
/*    */   public MonitorStatusChangeEvent(MonitoredVm paramMonitoredVm, List paramList1, List paramList2)
/*    */   {
/* 63 */     super(paramMonitoredVm);
/* 64 */     this.inserted = paramList1;
/* 65 */     this.removed = paramList2;
/*    */   }
/*    */ 
/*    */   public List getInserted()
/*    */   {
/* 78 */     return this.inserted;
/*    */   }
/*    */ 
/*    */   public List getRemoved()
/*    */   {
/* 90 */     return this.removed;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.event.MonitorStatusChangeEvent
 * JD-Core Version:    0.6.2
 */