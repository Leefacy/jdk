/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.MonitorStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.event.VmEvent;
/*     */ import sun.jvmstat.monitor.event.VmListener;
/*     */ import sun.jvmstat.perfdata.monitor.AbstractMonitoredVm;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTask;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStatus;
/*     */ 
/*     */ public class LocalMonitoredVm extends AbstractMonitoredVm
/*     */ {
/*     */   private ArrayList<VmListener> listeners;
/*     */   private NotifierTask task;
/*     */ 
/*     */   public LocalMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt)
/*     */     throws MonitorException
/*     */   {
/*  67 */     super(paramVmIdentifier, paramInt);
/*  68 */     this.pdb = new PerfDataBuffer(paramVmIdentifier);
/*  69 */     this.listeners = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/*  76 */     if (this.interval > 0)
/*     */     {
/*  82 */       if (this.task != null) {
/*  83 */         this.task.cancel();
/*  84 */         this.task = null;
/*     */       }
/*     */     }
/*  87 */     super.detach();
/*     */   }
/*     */ 
/*     */   public void addVmListener(VmListener paramVmListener)
/*     */   {
/*  94 */     synchronized (this.listeners) {
/*  95 */       this.listeners.add(paramVmListener);
/*  96 */       if (this.task == null) {
/*  97 */         this.task = new NotifierTask(null);
/*  98 */         LocalEventTimer localLocalEventTimer = LocalEventTimer.getInstance();
/*  99 */         localLocalEventTimer.schedule(this.task, this.interval, this.interval);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeVmListener(VmListener paramVmListener)
/*     */   {
/* 108 */     synchronized (this.listeners) {
/* 109 */       this.listeners.remove(paramVmListener);
/* 110 */       if ((this.listeners.isEmpty()) && (this.task != null)) {
/* 111 */         this.task.cancel();
/* 112 */         this.task = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInterval(int paramInt)
/*     */   {
/* 121 */     synchronized (this.listeners) {
/* 122 */       if (paramInt == this.interval) {
/* 123 */         return;
/*     */       }
/*     */ 
/* 126 */       int i = this.interval;
/* 127 */       super.setInterval(paramInt);
/*     */ 
/* 129 */       if (this.task != null) {
/* 130 */         this.task.cancel();
/* 131 */         NotifierTask localNotifierTask = this.task;
/* 132 */         this.task = new NotifierTask(null);
/* 133 */         LocalEventTimer localLocalEventTimer = LocalEventTimer.getInstance();
/* 134 */         CountedTimerTaskUtils.reschedule(localLocalEventTimer, localNotifierTask, this.task, i, paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireMonitorStatusChangedEvents(List paramList1, List paramList2)
/*     */   {
/* 147 */     MonitorStatusChangeEvent localMonitorStatusChangeEvent = null;
/* 148 */     ArrayList localArrayList = null;
/*     */ 
/* 150 */     synchronized (this.listeners) {
/* 151 */       localArrayList = (ArrayList)this.listeners.clone();
/*     */     }
/*     */ 
/* 154 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) {
/* 155 */       VmListener localVmListener = (VmListener)((Iterator)???).next();
/*     */ 
/* 157 */       if (localMonitorStatusChangeEvent == null) {
/* 158 */         localMonitorStatusChangeEvent = new MonitorStatusChangeEvent(this, paramList1, paramList2);
/*     */       }
/* 160 */       localVmListener.monitorStatusChanged(localMonitorStatusChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireMonitorsUpdatedEvents()
/*     */   {
/* 168 */     VmEvent localVmEvent = null;
/* 169 */     ArrayList localArrayList = null;
/*     */ 
/* 171 */     synchronized (this.listeners) {
/* 172 */       localArrayList = (ArrayList)cast(this.listeners.clone());
/*     */     }
/*     */ 
/* 175 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { VmListener localVmListener = (VmListener)((Iterator)???).next();
/*     */ 
/* 177 */       if (localVmEvent == null) {
/* 178 */         localVmEvent = new VmEvent(this);
/*     */       }
/* 180 */       localVmListener.monitorsUpdated(localVmEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   static <T> T cast(Object paramObject)
/*     */   {
/* 211 */     return paramObject;
/*     */   }
/*     */ 
/*     */   private class NotifierTask extends CountedTimerTask
/*     */   {
/*     */     private NotifierTask()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 190 */       super.run();
/*     */       try {
/* 192 */         MonitorStatus localMonitorStatus = LocalMonitoredVm.this.getMonitorStatus();
/* 193 */         List localList1 = localMonitorStatus.getInserted();
/* 194 */         List localList2 = localMonitorStatus.getRemoved();
/*     */ 
/* 196 */         if ((!localList1.isEmpty()) || (!localList2.isEmpty())) {
/* 197 */           LocalMonitoredVm.this.fireMonitorStatusChangedEvents(localList1, localList2);
/*     */         }
/* 199 */         LocalMonitoredVm.this.fireMonitorsUpdatedEvents();
/*     */       }
/*     */       catch (MonitorException localMonitorException) {
/* 202 */         System.err.println("Exception updating monitors for " + LocalMonitoredVm.this
/* 203 */           .getVmIdentifier());
/* 204 */         localMonitorException.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.local.LocalMonitoredVm
 * JD-Core Version:    0.6.2
 */