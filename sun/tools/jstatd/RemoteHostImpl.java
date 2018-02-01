/*     */ package sun.tools.jstatd;
/*     */ 
/*     */ import java.net.URISyntaxException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostEvent;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.remote.BufferedMonitoredVm;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
/*     */ import sun.jvmstat.monitor.remote.RemoteVm;
/*     */ 
/*     */ public class RemoteHostImpl
/*     */   implements RemoteHost, HostListener
/*     */ {
/*     */   private MonitoredHost monitoredHost;
/*     */   private Set<Integer> activeVms;
/*     */ 
/*     */   public RemoteHostImpl()
/*     */     throws MonitorException
/*     */   {
/*     */     try
/*     */     {
/*  56 */       this.monitoredHost = MonitoredHost.getMonitoredHost("localhost");
/*     */     } catch (URISyntaxException localURISyntaxException) {
/*     */     }
/*  59 */     this.activeVms = this.monitoredHost.activeVms();
/*  60 */     this.monitoredHost.addHostListener(this);
/*     */   }
/*     */ 
/*     */   public RemoteVm attachVm(int paramInt, String paramString) throws RemoteException, MonitorException
/*     */   {
/*  65 */     Integer localInteger = new Integer(paramInt);
/*  66 */     RemoteVm localRemoteVm = null;
/*  67 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/*  69 */     localStringBuffer.append("local://").append(paramInt).append("@localhost");
/*  70 */     if (paramString != null) {
/*  71 */       localStringBuffer.append("?mode=" + paramString);
/*     */     }
/*     */ 
/*  74 */     String str = localStringBuffer.toString();
/*     */     try
/*     */     {
/*  77 */       VmIdentifier localVmIdentifier = new VmIdentifier(str);
/*  78 */       MonitoredVm localMonitoredVm = this.monitoredHost.getMonitoredVm(localVmIdentifier);
/*  79 */       RemoteVmImpl localRemoteVmImpl = new RemoteVmImpl((BufferedMonitoredVm)localMonitoredVm);
/*  80 */       localRemoteVm = (RemoteVm)UnicastRemoteObject.exportObject(localRemoteVmImpl, 0);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException) {
/*  83 */       throw new RuntimeException("Malformed VmIdentifier URI: " + str, localURISyntaxException);
/*     */     }
/*     */ 
/*  86 */     return localRemoteVm;
/*     */   }
/*     */ 
/*     */   public void detachVm(RemoteVm paramRemoteVm) throws RemoteException {
/*  90 */     paramRemoteVm.detach();
/*     */   }
/*     */ 
/*     */   public int[] activeVms() throws MonitorException {
/*  94 */     Object[] arrayOfObject = null;
/*  95 */     int[] arrayOfInt = null;
/*     */ 
/*  97 */     arrayOfObject = this.monitoredHost.activeVms().toArray();
/*  98 */     arrayOfInt = new int[arrayOfObject.length];
/*     */ 
/* 100 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 101 */       arrayOfInt[i] = ((Integer)arrayOfObject[i]).intValue();
/*     */     }
/* 103 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void vmStatusChanged(VmStatusChangeEvent paramVmStatusChangeEvent) {
/* 107 */     synchronized (this.activeVms) {
/* 108 */       this.activeVms.retainAll(paramVmStatusChangeEvent.getActive());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disconnected(HostEvent paramHostEvent)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstatd.RemoteHostImpl
 * JD-Core Version:    0.6.2
 */