/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTask;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;
/*     */ 
/*     */ public class MonitoredHostProvider extends MonitoredHost
/*     */ {
/*     */   private static final int DEFAULT_POLLING_INTERVAL = 1000;
/*     */   private ArrayList<HostListener> listeners;
/*     */   private NotifierTask task;
/*     */   private HashSet<Integer> activeVms;
/*     */   private LocalVmManager vmManager;
/*     */ 
/*     */   public MonitoredHostProvider(HostIdentifier paramHostIdentifier)
/*     */   {
/*  55 */     this.hostId = paramHostIdentifier;
/*  56 */     this.listeners = new ArrayList();
/*  57 */     this.interval = 1000;
/*  58 */     this.activeVms = new HashSet();
/*  59 */     this.vmManager = new LocalVmManager();
/*     */   }
/*     */ 
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier)
/*     */     throws MonitorException
/*     */   {
/*  67 */     return getMonitoredVm(paramVmIdentifier, 1000);
/*     */   }
/*     */ 
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt)
/*     */     throws MonitorException
/*     */   {
/*     */     try
/*     */     {
/*  76 */       VmIdentifier localVmIdentifier = this.hostId.resolve(paramVmIdentifier);
/*  77 */       return new LocalMonitoredVm(localVmIdentifier, paramInt);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException)
/*     */     {
/*  85 */       throw new IllegalArgumentException("Malformed URI: " + paramVmIdentifier
/*  85 */         .toString(), localURISyntaxException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void detach(MonitoredVm paramMonitoredVm)
/*     */   {
/*  93 */     paramMonitoredVm.detach();
/*     */   }
/*     */ 
/*     */   public void addHostListener(HostListener paramHostListener)
/*     */   {
/* 100 */     synchronized (this.listeners) {
/* 101 */       this.listeners.add(paramHostListener);
/* 102 */       if (this.task == null) {
/* 103 */         this.task = new NotifierTask(null);
/* 104 */         LocalEventTimer localLocalEventTimer = LocalEventTimer.getInstance();
/* 105 */         localLocalEventTimer.schedule(this.task, this.interval, this.interval);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeHostListener(HostListener paramHostListener)
/*     */   {
/* 114 */     synchronized (this.listeners) {
/* 115 */       this.listeners.remove(paramHostListener);
/* 116 */       if ((this.listeners.isEmpty()) && (this.task != null)) {
/* 117 */         this.task.cancel();
/* 118 */         this.task = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInterval(int paramInt)
/*     */   {
/* 127 */     synchronized (this.listeners) {
/* 128 */       if (paramInt == this.interval) {
/* 129 */         return;
/*     */       }
/*     */ 
/* 132 */       int i = this.interval;
/* 133 */       super.setInterval(paramInt);
/*     */ 
/* 135 */       if (this.task != null) {
/* 136 */         this.task.cancel();
/* 137 */         NotifierTask localNotifierTask = this.task;
/* 138 */         this.task = new NotifierTask(null);
/* 139 */         LocalEventTimer localLocalEventTimer = LocalEventTimer.getInstance();
/* 140 */         CountedTimerTaskUtils.reschedule(localLocalEventTimer, localNotifierTask, this.task, i, paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<Integer> activeVms()
/*     */   {
/* 150 */     return this.vmManager.activeVms();
/*     */   }
/*     */ 
/*     */   private void fireVmStatusChangedEvents(Set paramSet1, Set paramSet2, Set paramSet3)
/*     */   {
/* 165 */     ArrayList localArrayList = null;
/* 166 */     VmStatusChangeEvent localVmStatusChangeEvent = null;
/*     */ 
/* 168 */     synchronized (this.listeners) {
/* 169 */       localArrayList = (ArrayList)this.listeners.clone();
/*     */     }
/*     */ 
/* 172 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) {
/* 173 */       HostListener localHostListener = (HostListener)((Iterator)???).next();
/* 174 */       if (localVmStatusChangeEvent == null) {
/* 175 */         localVmStatusChangeEvent = new VmStatusChangeEvent(this, paramSet1, paramSet2, paramSet3);
/*     */       }
/* 177 */       localHostListener.vmStatusChanged(localVmStatusChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class NotifierTask extends CountedTimerTask {
/*     */     private NotifierTask() {
/*     */     }
/*     */ 
/*     */     public void run() {
/* 186 */       super.run();
/*     */ 
/* 189 */       HashSet localHashSet1 = MonitoredHostProvider.this.activeVms;
/*     */ 
/* 192 */       MonitoredHostProvider.this.activeVms = ((HashSet)MonitoredHostProvider.this.vmManager.activeVms());
/*     */ 
/* 194 */       if (MonitoredHostProvider.this.activeVms.isEmpty()) {
/* 195 */         return;
/*     */       }
/* 197 */       HashSet localHashSet2 = new HashSet();
/* 198 */       HashSet localHashSet3 = new HashSet();
/*     */ 
/* 200 */       for (Iterator localIterator = MonitoredHostProvider.this.activeVms.iterator(); localIterator.hasNext(); ) {
/* 201 */         localObject = (Integer)localIterator.next();
/* 202 */         if (!localHashSet1.contains(localObject))
/*     */         {
/* 204 */           localHashSet2.add(localObject);
/*     */         }
/*     */       }
/*     */       Object localObject;
/* 208 */       for (localIterator = localHashSet1.iterator(); localIterator.hasNext(); )
/*     */       {
/* 210 */         localObject = localIterator.next();
/* 211 */         if (!MonitoredHostProvider.this.activeVms.contains(localObject))
/*     */         {
/* 213 */           localHashSet3.add(localObject);
/*     */         }
/*     */       }
/*     */ 
/* 217 */       if ((!localHashSet2.isEmpty()) || (!localHashSet3.isEmpty()))
/* 218 */         MonitoredHostProvider.this.fireVmStatusChangedEvents(MonitoredHostProvider.this.activeVms, localHashSet2, localHashSet3);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.local.MonitoredHostProvider
 * JD-Core Version:    0.6.2
 */