/*     */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Timer;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.MonitorStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.event.VmEvent;
/*     */ import sun.jvmstat.monitor.event.VmListener;
/*     */ import sun.jvmstat.monitor.remote.RemoteVm;
/*     */ import sun.jvmstat.perfdata.monitor.AbstractMonitoredVm;
/*     */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTask;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStatus;
/*     */ 
/*     */ public class RemoteMonitoredVm extends AbstractMonitoredVm
/*     */ {
/*     */   private ArrayList<VmListener> listeners;
/*     */   private NotifierTask notifierTask;
/*     */   private SamplerTask samplerTask;
/*     */   private Timer timer;
/*     */   private RemoteVm rvm;
/*     */   private ByteBuffer updateBuffer;
/*     */ 
/*     */   public RemoteMonitoredVm(RemoteVm paramRemoteVm, VmIdentifier paramVmIdentifier, Timer paramTimer, int paramInt)
/*     */     throws MonitorException
/*     */   {
/*  69 */     super(paramVmIdentifier, paramInt);
/*  70 */     this.rvm = paramRemoteVm;
/*  71 */     this.pdb = new PerfDataBuffer(paramRemoteVm, paramVmIdentifier.getLocalVmId());
/*  72 */     this.listeners = new ArrayList();
/*  73 */     this.timer = paramTimer;
/*     */   }
/*     */ 
/*     */   public void attach()
/*     */     throws MonitorException
/*     */   {
/*  80 */     this.updateBuffer = this.pdb.getByteBuffer().duplicate();
/*     */ 
/*  83 */     if (this.interval > 0) {
/*  84 */       this.samplerTask = new SamplerTask(null);
/*  85 */       this.timer.schedule(this.samplerTask, 0L, this.interval);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/*     */     try
/*     */     {
/*  94 */       if (this.interval > 0) {
/*  95 */         if (this.samplerTask != null) {
/*  96 */           this.samplerTask.cancel();
/*  97 */           this.samplerTask = null;
/*     */         }
/*  99 */         if (this.notifierTask != null) {
/* 100 */           this.notifierTask.cancel();
/* 101 */           this.notifierTask = null;
/*     */         }
/* 103 */         sample();
/*     */       }
/*     */     }
/*     */     catch (RemoteException localRemoteException) {
/* 107 */       System.err.println("Could not read data for remote JVM " + this.vmid);
/* 108 */       localRemoteException.printStackTrace();
/*     */     }
/*     */     finally {
/* 111 */       super.detach();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void sample()
/*     */     throws RemoteException
/*     */   {
/* 125 */     assert (this.updateBuffer != null);
/* 126 */     ((PerfDataBuffer)this.pdb).sample(this.updateBuffer);
/*     */   }
/*     */ 
/*     */   public RemoteVm getRemoteVm()
/*     */   {
/* 135 */     return this.rvm;
/*     */   }
/*     */ 
/*     */   public void addVmListener(VmListener paramVmListener)
/*     */   {
/* 142 */     synchronized (this.listeners) {
/* 143 */       this.listeners.add(paramVmListener);
/* 144 */       if (this.notifierTask == null) {
/* 145 */         this.notifierTask = new NotifierTask(null);
/* 146 */         this.timer.schedule(this.notifierTask, 0L, this.interval);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeVmListener(VmListener paramVmListener)
/*     */   {
/* 155 */     synchronized (this.listeners) {
/* 156 */       this.listeners.remove(paramVmListener);
/* 157 */       if ((this.listeners.isEmpty()) && (this.notifierTask != null)) {
/* 158 */         this.notifierTask.cancel();
/* 159 */         this.notifierTask = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInterval(int paramInt)
/*     */   {
/* 168 */     synchronized (this.listeners) {
/* 169 */       if (paramInt == this.interval) {
/* 170 */         return;
/*     */       }
/*     */ 
/* 173 */       int i = this.interval;
/* 174 */       super.setInterval(paramInt);
/*     */       Object localObject1;
/* 176 */       if (this.samplerTask != null) {
/* 177 */         this.samplerTask.cancel();
/* 178 */         localObject1 = this.samplerTask;
/* 179 */         this.samplerTask = new SamplerTask(null);
/* 180 */         CountedTimerTaskUtils.reschedule(this.timer, (CountedTimerTask)localObject1, this.samplerTask, i, paramInt);
/*     */       }
/*     */ 
/* 184 */       if (this.notifierTask != null) {
/* 185 */         this.notifierTask.cancel();
/* 186 */         localObject1 = this.notifierTask;
/* 187 */         this.notifierTask = new NotifierTask(null);
/* 188 */         CountedTimerTaskUtils.reschedule(this.timer, (CountedTimerTask)localObject1, this.notifierTask, i, paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireMonitorStatusChangedEvents(List paramList1, List paramList2)
/*     */   {
/* 202 */     ArrayList localArrayList = null;
/* 203 */     MonitorStatusChangeEvent localMonitorStatusChangeEvent = null;
/*     */ 
/* 205 */     synchronized (this.listeners) {
/* 206 */       localArrayList = (ArrayList)this.listeners.clone();
/*     */     }
/*     */ 
/* 209 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) {
/* 210 */       VmListener localVmListener = (VmListener)((Iterator)???).next();
/* 211 */       if (localMonitorStatusChangeEvent == null) {
/* 212 */         localMonitorStatusChangeEvent = new MonitorStatusChangeEvent(this, paramList1, paramList2);
/*     */       }
/* 214 */       localVmListener.monitorStatusChanged(localMonitorStatusChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireMonitorsUpdatedEvents()
/*     */   {
/* 222 */     ArrayList localArrayList = null;
/* 223 */     VmEvent localVmEvent = null;
/*     */ 
/* 225 */     synchronized (this.listeners) {
/* 226 */       localArrayList = (ArrayList)this.listeners.clone();
/*     */     }
/*     */ 
/* 229 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) {
/* 230 */       VmListener localVmListener = (VmListener)((Iterator)???).next();
/* 231 */       if (localVmEvent == null) {
/* 232 */         localVmEvent = new VmEvent(this);
/*     */       }
/* 234 */       localVmListener.monitorsUpdated(localVmEvent);
/*     */     }
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
/* 255 */       super.run();
/*     */       try {
/* 257 */         MonitorStatus localMonitorStatus = RemoteMonitoredVm.this.getMonitorStatus();
/*     */ 
/* 259 */         List localList1 = localMonitorStatus.getInserted();
/* 260 */         List localList2 = localMonitorStatus.getRemoved();
/*     */ 
/* 262 */         if ((!localList1.isEmpty()) || (!localList2.isEmpty())) {
/* 263 */           RemoteMonitoredVm.this.fireMonitorStatusChangedEvents(localList1, localList2);
/*     */         }
/*     */       }
/*     */       catch (MonitorException localMonitorException)
/*     */       {
/* 268 */         System.err.println("Exception updating monitors for " + RemoteMonitoredVm.this
/* 269 */           .getVmIdentifier());
/* 270 */         localMonitorException.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SamplerTask extends CountedTimerTask
/*     */   {
/*     */     private SamplerTask()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 284 */       super.run();
/*     */       try {
/* 286 */         RemoteMonitoredVm.this.sample();
/* 287 */         RemoteMonitoredVm.this.fireMonitorsUpdatedEvents();
/*     */       }
/*     */       catch (RemoteException localRemoteException)
/*     */       {
/* 291 */         System.err.println("Exception taking sample for " + RemoteMonitoredVm.this
/* 292 */           .getVmIdentifier());
/* 293 */         localRemoteException.printStackTrace();
/* 294 */         cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.rmi.RemoteMonitoredVm
 * JD-Core Version:    0.6.2
 */