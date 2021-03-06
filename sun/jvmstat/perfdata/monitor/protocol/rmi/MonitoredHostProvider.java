/*     */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Timer;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostEvent;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
/*     */ import sun.jvmstat.monitor.remote.RemoteVm;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTask;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;
/*     */ 
/*     */ public class MonitoredHostProvider extends MonitoredHost
/*     */ {
/*     */   private static final String serverName = "/JStatRemoteHost";
/*     */   private static final int DEFAULT_POLLING_INTERVAL = 1000;
/*     */   private ArrayList<HostListener> listeners;
/*     */   private NotifierTask task;
/*     */   private HashSet<Integer> activeVms;
/*     */   private RemoteVmManager vmManager;
/*     */   private RemoteHost remoteHost;
/*     */   private Timer timer;
/*     */ 
/*     */   public MonitoredHostProvider(HostIdentifier paramHostIdentifier)
/*     */     throws MonitorException
/*     */   {
/*  65 */     this.hostId = paramHostIdentifier;
/*  66 */     this.listeners = new ArrayList();
/*  67 */     this.interval = 1000;
/*  68 */     this.activeVms = new HashSet();
/*     */ 
/*  71 */     Object localObject = "/JStatRemoteHost";
/*  72 */     String str2 = paramHostIdentifier.getPath();
/*     */ 
/*  74 */     if ((str2 != null) && (str2.length() > 0))
/*  75 */       localObject = str2;
/*     */     String str1;
/*  78 */     if (paramHostIdentifier.getPort() != -1)
/*  79 */       str1 = "rmi://" + paramHostIdentifier.getHost() + ":" + paramHostIdentifier.getPort() + (String)localObject;
/*     */     else {
/*  81 */       str1 = "rmi://" + paramHostIdentifier.getHost() + (String)localObject;
/*     */     }
/*     */     try
/*     */     {
/*  85 */       this.remoteHost = ((RemoteHost)Naming.lookup(str1));
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/*  98 */       str3 = "RMI Registry not available at " + paramHostIdentifier
/*  98 */         .getHost();
/*     */ 
/* 100 */       if (paramHostIdentifier.getPort() == -1) {
/* 101 */         str3 = str3 + ":" + 1099;
/*     */       }
/*     */       else {
/* 104 */         str3 = str3 + ":" + paramHostIdentifier.getPort();
/*     */       }
/*     */ 
/* 107 */       if (localRemoteException.getMessage() != null) {
/* 108 */         throw new MonitorException(str3 + "\n" + localRemoteException.getMessage(), localRemoteException);
/*     */       }
/* 110 */       throw new MonitorException(str3, localRemoteException);
/*     */     }
/*     */     catch (NotBoundException localNotBoundException)
/*     */     {
/* 115 */       String str3 = localNotBoundException.getMessage();
/* 116 */       if (str3 == null) str3 = str1;
/* 117 */       throw new MonitorException("RMI Server " + str3 + " not available", localNotBoundException);
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException)
/*     */     {
/* 121 */       localMalformedURLException.printStackTrace();
/* 122 */       throw new IllegalArgumentException("Malformed URL: " + str1);
/*     */     }
/* 124 */     this.vmManager = new RemoteVmManager(this.remoteHost);
/* 125 */     this.timer = new Timer(true);
/*     */   }
/*     */ 
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier)
/*     */     throws MonitorException
/*     */   {
/* 133 */     return getMonitoredVm(paramVmIdentifier, 1000);
/*     */   }
/*     */ 
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt)
/*     */     throws MonitorException
/*     */   {
/* 141 */     VmIdentifier localVmIdentifier = null;
/*     */     try {
/* 143 */       localVmIdentifier = this.hostId.resolve(paramVmIdentifier);
/* 144 */       RemoteVm localRemoteVm = this.remoteHost.attachVm(paramVmIdentifier.getLocalVmId(), paramVmIdentifier
/* 145 */         .getMode());
/* 146 */       RemoteMonitoredVm localRemoteMonitoredVm = new RemoteMonitoredVm(localRemoteVm, localVmIdentifier, this.timer, paramInt);
/*     */ 
/* 148 */       localRemoteMonitoredVm.attach();
/* 149 */       return localRemoteMonitoredVm;
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/* 153 */       throw new MonitorException("Remote Exception attaching to " + localVmIdentifier
/* 153 */         .toString(), localRemoteException);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException)
/*     */     {
/* 161 */       throw new IllegalArgumentException("Malformed URI: " + paramVmIdentifier
/* 161 */         .toString(), localURISyntaxException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void detach(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/* 169 */     RemoteMonitoredVm localRemoteMonitoredVm = (RemoteMonitoredVm)paramMonitoredVm;
/* 170 */     localRemoteMonitoredVm.detach();
/*     */     try {
/* 172 */       this.remoteHost.detachVm(localRemoteMonitoredVm.getRemoteVm());
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/* 176 */       throw new MonitorException("Remote Exception detaching from " + paramMonitoredVm
/* 176 */         .getVmIdentifier().toString(), localRemoteException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addHostListener(HostListener paramHostListener)
/*     */   {
/* 184 */     synchronized (this.listeners) {
/* 185 */       this.listeners.add(paramHostListener);
/* 186 */       if (this.task == null) {
/* 187 */         this.task = new NotifierTask(null);
/* 188 */         this.timer.schedule(this.task, 0L, this.interval);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeHostListener(HostListener paramHostListener)
/*     */   {
/* 202 */     synchronized (this.listeners) {
/* 203 */       this.listeners.remove(paramHostListener);
/* 204 */       if ((this.listeners.isEmpty()) && (this.task != null)) {
/* 205 */         this.task.cancel();
/* 206 */         this.task = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInterval(int paramInt) {
/* 212 */     synchronized (this.listeners) {
/* 213 */       if (paramInt == this.interval) {
/* 214 */         return;
/*     */       }
/*     */ 
/* 217 */       int i = this.interval;
/* 218 */       super.setInterval(paramInt);
/*     */ 
/* 220 */       if (this.task != null) {
/* 221 */         this.task.cancel();
/* 222 */         NotifierTask localNotifierTask = this.task;
/* 223 */         this.task = new NotifierTask(null);
/* 224 */         CountedTimerTaskUtils.reschedule(this.timer, localNotifierTask, this.task, i, paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<Integer> activeVms()
/*     */     throws MonitorException
/*     */   {
/* 234 */     return this.vmManager.activeVms();
/*     */   }
/*     */ 
/*     */   private void fireVmStatusChangedEvents(Set paramSet1, Set paramSet2, Set paramSet3)
/*     */   {
/* 251 */     ArrayList localArrayList = null;
/* 252 */     VmStatusChangeEvent localVmStatusChangeEvent = null;
/*     */ 
/* 254 */     synchronized (this.listeners) {
/* 255 */       localArrayList = (ArrayList)this.listeners.clone();
/*     */     }
/*     */ 
/* 258 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) {
/* 259 */       HostListener localHostListener = (HostListener)((Iterator)???).next();
/* 260 */       if (localVmStatusChangeEvent == null) {
/* 261 */         localVmStatusChangeEvent = new VmStatusChangeEvent(this, paramSet1, paramSet2, paramSet3);
/*     */       }
/* 263 */       localHostListener.vmStatusChanged(localVmStatusChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   void fireDisconnectedEvents()
/*     */   {
/* 271 */     ArrayList localArrayList = null;
/* 272 */     HostEvent localHostEvent = null;
/*     */ 
/* 274 */     synchronized (this.listeners) {
/* 275 */       localArrayList = (ArrayList)this.listeners.clone();
/*     */     }
/*     */ 
/* 278 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) {
/* 279 */       HostListener localHostListener = (HostListener)((Iterator)???).next();
/* 280 */       if (localHostEvent == null) {
/* 281 */         localHostEvent = new HostEvent(this);
/*     */       }
/* 283 */       localHostListener.disconnected(localHostEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class NotifierTask extends CountedTimerTask {
/*     */     private NotifierTask() {
/*     */     }
/*     */ 
/*     */     public void run() {
/* 292 */       super.run();
/*     */ 
/* 295 */       HashSet localHashSet1 = MonitoredHostProvider.this.activeVms;
/*     */       try
/*     */       {
/* 299 */         MonitoredHostProvider.this.activeVms = ((HashSet)MonitoredHostProvider.this.vmManager.activeVms());
/*     */       }
/*     */       catch (MonitorException localMonitorException)
/*     */       {
/* 303 */         System.err.println("MonitoredHostProvider: polling task caught MonitorException:");
/*     */ 
/* 305 */         localMonitorException.printStackTrace();
/*     */ 
/* 308 */         MonitoredHostProvider.this.setLastException(localMonitorException);
/* 309 */         MonitoredHostProvider.this.fireDisconnectedEvents();
/*     */       }
/*     */ 
/* 312 */       if (MonitoredHostProvider.this.activeVms.isEmpty()) {
/* 313 */         return;
/*     */       }
/*     */ 
/* 316 */       HashSet localHashSet2 = new HashSet();
/* 317 */       HashSet localHashSet3 = new HashSet();
/*     */ 
/* 319 */       for (Iterator localIterator = MonitoredHostProvider.this.activeVms.iterator(); localIterator.hasNext(); ) {
/* 320 */         localObject = (Integer)localIterator.next();
/* 321 */         if (!localHashSet1.contains(localObject))
/*     */         {
/* 323 */           localHashSet2.add(localObject);
/*     */         }
/*     */       }
/*     */       Object localObject;
/* 327 */       for (localIterator = localHashSet1.iterator(); localIterator.hasNext(); )
/*     */       {
/* 329 */         localObject = localIterator.next();
/* 330 */         if (!MonitoredHostProvider.this.activeVms.contains(localObject))
/*     */         {
/* 332 */           localHashSet3.add(localObject);
/*     */         }
/*     */       }
/*     */ 
/* 336 */       if ((!localHashSet2.isEmpty()) || (!localHashSet3.isEmpty()))
/* 337 */         MonitoredHostProvider.this.fireVmStatusChangedEvents(MonitoredHostProvider.this.activeVms, localHashSet2, localHashSet3);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.rmi.MonitoredHostProvider
 * JD-Core Version:    0.6.2
 */