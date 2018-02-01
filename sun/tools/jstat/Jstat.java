/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostEvent;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ 
/*     */ public class Jstat
/*     */ {
/*     */   private static Arguments arguments;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*     */     try
/*     */     {
/*  46 */       arguments = new Arguments(paramArrayOfString);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  48 */       System.err.println(localIllegalArgumentException.getMessage());
/*  49 */       Arguments.printUsage(System.err);
/*  50 */       System.exit(1);
/*     */     }
/*     */ 
/*  53 */     if (arguments.isHelp()) {
/*  54 */       Arguments.printUsage(System.out);
/*  55 */       System.exit(0);
/*     */     }
/*     */ 
/*  58 */     if (arguments.isOptions()) {
/*  59 */       OptionLister localOptionLister = new OptionLister(arguments.optionsSources());
/*  60 */       localOptionLister.print(System.out);
/*  61 */       System.exit(0);
/*     */     }
/*     */     try
/*     */     {
/*  65 */       if (arguments.isList())
/*  66 */         logNames();
/*  67 */       else if (arguments.isSnap())
/*  68 */         logSnapShot();
/*     */       else
/*  70 */         logSamples();
/*     */     }
/*     */     catch (MonitorException localMonitorException) {
/*  73 */       if (localMonitorException.getMessage() != null) {
/*  74 */         System.err.println(localMonitorException.getMessage());
/*     */       } else {
/*  76 */         Throwable localThrowable = localMonitorException.getCause();
/*  77 */         if ((localThrowable != null) && (localThrowable.getMessage() != null))
/*  78 */           System.err.println(localThrowable.getMessage());
/*     */         else {
/*  80 */           localMonitorException.printStackTrace();
/*     */         }
/*     */       }
/*  83 */       System.exit(1);
/*     */     }
/*  85 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   static void logNames() throws MonitorException {
/*  89 */     VmIdentifier localVmIdentifier = arguments.vmId();
/*  90 */     int i = arguments.sampleInterval();
/*  91 */     MonitoredHost localMonitoredHost = MonitoredHost.getMonitoredHost(localVmIdentifier);
/*  92 */     MonitoredVm localMonitoredVm = localMonitoredHost.getMonitoredVm(localVmIdentifier, i);
/*  93 */     JStatLogger localJStatLogger = new JStatLogger(localMonitoredVm);
/*  94 */     localJStatLogger.printNames(arguments.counterNames(), arguments.comparator(), arguments
/*  95 */       .showUnsupported(), System.out);
/*  96 */     localMonitoredHost.detach(localMonitoredVm);
/*     */   }
/*     */ 
/*     */   static void logSnapShot() throws MonitorException {
/* 100 */     VmIdentifier localVmIdentifier = arguments.vmId();
/* 101 */     int i = arguments.sampleInterval();
/* 102 */     MonitoredHost localMonitoredHost = MonitoredHost.getMonitoredHost(localVmIdentifier);
/* 103 */     MonitoredVm localMonitoredVm = localMonitoredHost.getMonitoredVm(localVmIdentifier, i);
/* 104 */     JStatLogger localJStatLogger = new JStatLogger(localMonitoredVm);
/* 105 */     localJStatLogger.printSnapShot(arguments.counterNames(), arguments.comparator(), arguments
/* 106 */       .isVerbose(), arguments.showUnsupported(), System.out);
/*     */ 
/* 108 */     localMonitoredHost.detach(localMonitoredVm);
/*     */   }
/*     */ 
/*     */   static void logSamples() throws MonitorException {
/* 112 */     VmIdentifier localVmIdentifier = arguments.vmId();
/* 113 */     int i = arguments.sampleInterval();
/*     */ 
/* 115 */     final MonitoredHost localMonitoredHost = MonitoredHost.getMonitoredHost(localVmIdentifier);
/*     */ 
/* 116 */     MonitoredVm localMonitoredVm = localMonitoredHost.getMonitoredVm(localVmIdentifier, i);
/* 117 */     final JStatLogger localJStatLogger = new JStatLogger(localMonitoredVm);
/* 118 */     Object localObject1 = null;
/*     */ 
/* 120 */     if (arguments.isSpecialOption()) {
/* 121 */       localObject2 = arguments.optionFormat();
/* 122 */       localObject1 = new OptionOutputFormatter(localMonitoredVm, (OptionFormat)localObject2);
/*     */     } else {
/* 124 */       localObject2 = localMonitoredVm.findByPattern(arguments.counterNames());
/* 125 */       Collections.sort((List)localObject2, arguments.comparator());
/* 126 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 128 */       for (Iterator localIterator = ((List)localObject2).iterator(); localIterator.hasNext(); ) {
/* 129 */         Monitor localMonitor = (Monitor)localIterator.next();
/* 130 */         if ((!localMonitor.isSupported()) && (!arguments.showUnsupported())) {
/* 131 */           localIterator.remove();
/*     */         }
/* 134 */         else if (localMonitor.getVariability() == Variability.CONSTANT) {
/* 135 */           localIterator.remove();
/* 136 */           if (arguments.printConstants()) localArrayList.add(localMonitor); 
/*     */         }
/* 137 */         else if ((localMonitor.getUnits() == Units.STRING) && 
/* 138 */           (!arguments
/* 138 */           .printStrings())) {
/* 139 */           localIterator.remove();
/*     */         }
/*     */       }
/*     */ 
/* 143 */       if (!localArrayList.isEmpty()) {
/* 144 */         localJStatLogger.printList(localArrayList, arguments.isVerbose(), arguments
/* 145 */           .showUnsupported(), System.out);
/* 146 */         if (!((List)localObject2).isEmpty()) {
/* 147 */           System.out.println();
/*     */         }
/*     */       }
/*     */ 
/* 151 */       if (((List)localObject2).isEmpty()) {
/* 152 */         localMonitoredHost.detach(localMonitoredVm);
/* 153 */         return;
/*     */       }
/*     */ 
/* 157 */       localObject1 = new RawOutputFormatter((List)localObject2, arguments
/* 157 */         .printStrings());
/*     */     }
/*     */ 
/* 161 */     Runtime.getRuntime().addShutdownHook(new Thread() {
/*     */       public void run() {
/* 163 */         this.val$logger.stopLogging();
/*     */       }
/*     */     });
/* 168 */     Object localObject2 = new HostListener() {
/*     */       public void vmStatusChanged(VmStatusChangeEvent paramAnonymousVmStatusChangeEvent) {
/* 170 */         Integer localInteger = new Integer(this.val$vmId.getLocalVmId());
/* 171 */         if (paramAnonymousVmStatusChangeEvent.getTerminated().contains(localInteger))
/* 172 */           localJStatLogger.stopLogging();
/* 173 */         else if (!paramAnonymousVmStatusChangeEvent.getActive().contains(localInteger))
/* 174 */           localJStatLogger.stopLogging();
/*     */       }
/*     */ 
/*     */       public void disconnected(HostEvent paramAnonymousHostEvent)
/*     */       {
/* 179 */         if (localMonitoredHost == paramAnonymousHostEvent.getMonitoredHost())
/* 180 */           localJStatLogger.stopLogging();
/*     */       }
/*     */     };
/* 185 */     if (localVmIdentifier.getLocalVmId() != 0) {
/* 186 */       localMonitoredHost.addHostListener((HostListener)localObject2);
/*     */     }
/*     */ 
/* 189 */     localJStatLogger.logSamples((OutputFormatter)localObject1, arguments.headerRate(), arguments
/* 190 */       .sampleInterval(), arguments.sampleCount(), System.out);
/*     */ 
/* 194 */     if (localObject2 != null) {
/* 195 */       localMonitoredHost.removeHostListener((HostListener)localObject2);
/*     */     }
/* 197 */     localMonitoredHost.detach(localMonitoredVm);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Jstat
 * JD-Core Version:    0.6.2
 */