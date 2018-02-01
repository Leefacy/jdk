/*     */ package sun.jvmstat.monitor;
/*     */ 
/*     */ public class MonitoredVmUtil
/*     */ {
/* 164 */   private static int IS_ATTACHABLE = 0;
/* 165 */   private static int IS_KERNEL_VM = 1;
/*     */ 
/*     */   public static String vmVersion(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/*  52 */     StringMonitor localStringMonitor = (StringMonitor)paramMonitoredVm
/*  52 */       .findByName("java.property.java.vm.version");
/*     */ 
/*  53 */     return localStringMonitor == null ? "Unknown" : localStringMonitor.stringValue();
/*     */   }
/*     */ 
/*     */   public static String commandLine(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/*  65 */     StringMonitor localStringMonitor = (StringMonitor)paramMonitoredVm.findByName("sun.rt.javaCommand");
/*  66 */     return localStringMonitor == null ? "Unknown" : localStringMonitor.stringValue();
/*     */   }
/*     */ 
/*     */   public static String mainArgs(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/*  80 */     String str = commandLine(paramMonitoredVm);
/*     */ 
/*  82 */     int i = str.indexOf(' ');
/*  83 */     if (i > 0)
/*  84 */       return str.substring(i + 1);
/*  85 */     if (str.compareTo("Unknown") == 0) {
/*  86 */       return str;
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public static String mainClass(MonitoredVm paramMonitoredVm, boolean paramBoolean)
/*     */     throws MonitorException
/*     */   {
/* 105 */     String str1 = commandLine(paramMonitoredVm);
/* 106 */     String str2 = str1;
/*     */ 
/* 108 */     int i = str1.indexOf(' ');
/* 109 */     if (i > 0) {
/* 110 */       str2 = str1.substring(0, i);
/*     */     }
/* 112 */     if (!paramBoolean)
/*     */     {
/* 118 */       int j = str2.lastIndexOf('/');
/* 119 */       if (j > 0) {
/* 120 */         return str2.substring(j + 1);
/*     */       }
/*     */ 
/* 123 */       j = str2.lastIndexOf('\\');
/* 124 */       if (j > 0) {
/* 125 */         return str2.substring(j + 1);
/*     */       }
/*     */ 
/* 128 */       int k = str2.lastIndexOf('.');
/* 129 */       if (k > 0) {
/* 130 */         return str2.substring(k + 1);
/*     */       }
/*     */     }
/* 133 */     return str2;
/*     */   }
/*     */ 
/*     */   public static String jvmArgs(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/* 145 */     StringMonitor localStringMonitor = (StringMonitor)paramMonitoredVm.findByName("java.rt.vmArgs");
/* 146 */     return localStringMonitor == null ? "Unknown" : localStringMonitor.stringValue();
/*     */   }
/*     */ 
/*     */   public static String jvmFlags(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/* 159 */     StringMonitor localStringMonitor = (StringMonitor)paramMonitoredVm
/* 159 */       .findByName("java.rt.vmFlags");
/*     */ 
/* 160 */     return localStringMonitor == null ? "Unknown" : localStringMonitor.stringValue();
/*     */   }
/*     */ 
/*     */   public static boolean isAttachable(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException
/*     */   {
/* 174 */     StringMonitor localStringMonitor = (StringMonitor)paramMonitoredVm
/* 174 */       .findByName("sun.rt.jvmCapabilities");
/*     */ 
/* 175 */     if (localStringMonitor == null) {
/* 176 */       return false;
/*     */     }
/* 178 */     return localStringMonitor.stringValue().charAt(IS_ATTACHABLE) == '1';
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.MonitoredVmUtil
 * JD-Core Version:    0.6.2
 */