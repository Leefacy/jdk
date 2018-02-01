/*    */ package sun.jvmstat.monitor.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ 
/*    */ public class VmEvent extends EventObject
/*    */ {
/*    */   public VmEvent(MonitoredVm paramMonitoredVm)
/*    */   {
/* 45 */     super(paramMonitoredVm);
/*    */   }
/*    */ 
/*    */   public MonitoredVm getMonitoredVm()
/*    */   {
/* 54 */     return (MonitoredVm)this.source;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.event.VmEvent
 * JD-Core Version:    0.6.2
 */