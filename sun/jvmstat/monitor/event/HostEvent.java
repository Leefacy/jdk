/*    */ package sun.jvmstat.monitor.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import sun.jvmstat.monitor.MonitoredHost;
/*    */ 
/*    */ public class HostEvent extends EventObject
/*    */ {
/*    */   public HostEvent(MonitoredHost paramMonitoredHost)
/*    */   {
/* 45 */     super(paramMonitoredHost);
/*    */   }
/*    */ 
/*    */   public MonitoredHost getMonitoredHost()
/*    */   {
/* 54 */     return (MonitoredHost)this.source;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.event.HostEvent
 * JD-Core Version:    0.6.2
 */