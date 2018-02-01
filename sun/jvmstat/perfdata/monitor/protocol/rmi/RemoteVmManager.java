/*     */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*     */ 
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
/*     */ 
/*     */ public class RemoteVmManager
/*     */ {
/*     */   private RemoteHost remoteHost;
/*     */   private String user;
/*     */ 
/*     */   public RemoteVmManager(RemoteHost paramRemoteHost)
/*     */   {
/*  63 */     this(paramRemoteHost, null);
/*     */   }
/*     */ 
/*     */   public RemoteVmManager(RemoteHost paramRemoteHost, String paramString)
/*     */   {
/*  79 */     this.user = paramString;
/*  80 */     this.remoteHost = paramRemoteHost;
/*     */   }
/*     */ 
/*     */   public Set<Integer> activeVms()
/*     */     throws MonitorException
/*     */   {
/*  96 */     int[] arrayOfInt = null;
/*     */     try
/*     */     {
/*  99 */       arrayOfInt = this.remoteHost.activeVms();
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/* 103 */       throw new MonitorException("Error communicating with remote host: " + localRemoteException
/* 103 */         .getMessage(), localRemoteException);
/*     */     }
/*     */ 
/* 106 */     HashSet localHashSet = new HashSet(arrayOfInt.length);
/*     */ 
/* 108 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 109 */       localHashSet.add(new Integer(arrayOfInt[i]));
/*     */     }
/*     */ 
/* 112 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.rmi.RemoteVmManager
 * JD-Core Version:    0.6.2
 */