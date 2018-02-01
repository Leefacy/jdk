/*     */ package sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachPermission;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import com.sun.tools.attach.spi.AttachProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.MonitoredVmUtil;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ 
/*     */ public abstract class HotSpotAttachProvider extends AttachProvider
/*     */ {
/*     */   private static final String JVM_VERSION = "java.property.java.vm.version";
/*     */ 
/*     */   public void checkAttachPermission()
/*     */   {
/*  60 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  61 */     if (localSecurityManager != null)
/*  62 */       localSecurityManager.checkPermission(new AttachPermission("attachVirtualMachine"));
/*     */   }
/*     */ 
/*     */   public List<VirtualMachineDescriptor> listVirtualMachines()
/*     */   {
/*  74 */     ArrayList localArrayList = new ArrayList();
/*     */     MonitoredHost localMonitoredHost;
/*     */     Set localSet;
/*     */     try
/*     */     {
/*  80 */       localMonitoredHost = MonitoredHost.getMonitoredHost(new HostIdentifier((String)null));
/*  81 */       localSet = localMonitoredHost.activeVms();
/*     */     } catch (Throwable localThrowable1) {
/*  83 */       if ((localThrowable1 instanceof ExceptionInInitializerError)) {
/*  84 */         localObject1 = localThrowable1.getCause();
/*     */       }
/*  86 */       if ((localObject1 instanceof ThreadDeath)) {
/*  87 */         throw ((ThreadDeath)localObject1);
/*     */       }
/*  89 */       if ((localObject1 instanceof SecurityException)) {
/*  90 */         return localArrayList;
/*     */       }
/*  92 */       throw new InternalError((Throwable)localObject1);
/*     */     }
/*     */ 
/*  95 */     for (Object localObject1 = localSet.iterator(); ((Iterator)localObject1).hasNext(); ) { Integer localInteger = (Integer)((Iterator)localObject1).next();
/*  96 */       String str1 = localInteger.toString();
/*  97 */       String str2 = str1;
/*  98 */       boolean bool = false;
/*  99 */       MonitoredVm localMonitoredVm = null;
/*     */       try {
/* 101 */         localMonitoredVm = localMonitoredHost.getMonitoredVm(new VmIdentifier(str1));
/*     */         try {
/* 103 */           bool = MonitoredVmUtil.isAttachable(localMonitoredVm);
/*     */ 
/* 105 */           str2 = MonitoredVmUtil.commandLine(localMonitoredVm);
/*     */         } catch (Exception localException) {
/*     */         }
/* 108 */         if (bool)
/* 109 */           localArrayList.add(new HotSpotVirtualMachineDescriptor(this, str1, str2));
/*     */       }
/*     */       catch (Throwable localThrowable2) {
/* 112 */         if ((localThrowable2 instanceof ThreadDeath))
/* 113 */           throw ((ThreadDeath)localThrowable2);
/*     */       }
/*     */       finally {
/* 116 */         if (localMonitoredVm != null) {
/* 117 */           localMonitoredVm.detach();
/*     */         }
/*     */       }
/*     */     }
/* 121 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   void testAttachable(String paramString)
/*     */     throws AttachNotSupportedException
/*     */   {
/* 138 */     MonitoredVm localMonitoredVm = null;
/*     */     try {
/* 140 */       VmIdentifier localVmIdentifier = new VmIdentifier(paramString);
/* 141 */       localObject1 = MonitoredHost.getMonitoredHost(localVmIdentifier);
/* 142 */       localMonitoredVm = ((MonitoredHost)localObject1).getMonitoredVm(localVmIdentifier);
/*     */ 
/* 144 */       if (MonitoredVmUtil.isAttachable(localMonitoredVm))
/*     */         return;
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */       Object localObject1;
/* 149 */       if ((localThrowable instanceof ThreadDeath)) {
/* 150 */         localObject1 = (ThreadDeath)localThrowable;
/* 151 */         throw ((Throwable)localObject1);
/*     */       }
/*     */ 
/* 154 */       return;
/*     */     } finally {
/* 156 */       if (localMonitoredVm != null) {
/* 157 */         localMonitoredVm.detach();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 162 */     throw new AttachNotSupportedException("The VM does not support the attach mechanism");
/*     */   }
/*     */ 
/*     */   static class HotSpotVirtualMachineDescriptor extends VirtualMachineDescriptor
/*     */   {
/*     */     HotSpotVirtualMachineDescriptor(AttachProvider paramAttachProvider, String paramString1, String paramString2)
/*     */     {
/* 174 */       super(paramString1, paramString2);
/*     */     }
/*     */ 
/*     */     public boolean isAttachable() {
/* 178 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.attach.HotSpotAttachProvider
 * JD-Core Version:    0.6.2
 */