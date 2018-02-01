/*     */ package sun.jvmstat.monitor.event;
/*     */ 
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ 
/*     */ public class VmStatusChangeEvent extends HostEvent
/*     */ {
/*     */   protected Set active;
/*     */   protected Set started;
/*     */   protected Set terminated;
/*     */ 
/*     */   public VmStatusChangeEvent(MonitoredHost paramMonitoredHost, Set paramSet1, Set paramSet2, Set paramSet3)
/*     */   {
/*  76 */     super(paramMonitoredHost);
/*  77 */     this.active = paramSet1;
/*  78 */     this.started = paramSet2;
/*  79 */     this.terminated = paramSet3;
/*     */   }
/*     */ 
/*     */   public Set getActive()
/*     */   {
/*  93 */     return this.active;
/*     */   }
/*     */ 
/*     */   public Set getStarted()
/*     */   {
/* 108 */     return this.started;
/*     */   }
/*     */ 
/*     */   public Set getTerminated()
/*     */   {
/* 123 */     return this.terminated;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.event.VmStatusChangeEvent
 * JD-Core Version:    0.6.2
 */