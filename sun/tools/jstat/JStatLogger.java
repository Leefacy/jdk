/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.StringMonitor;
/*     */ 
/*     */ public class JStatLogger
/*     */ {
/*     */   private MonitoredVm monitoredVm;
/*  44 */   private volatile boolean active = true;
/*     */ 
/*     */   public JStatLogger(MonitoredVm paramMonitoredVm) {
/*  47 */     this.monitoredVm = paramMonitoredVm;
/*     */   }
/*     */ 
/*     */   public void printNames(String paramString, Comparator<Monitor> paramComparator, boolean paramBoolean, PrintStream paramPrintStream)
/*     */     throws MonitorException, PatternSyntaxException
/*     */   {
/*  58 */     List localList = this.monitoredVm.findByPattern(paramString);
/*  59 */     Collections.sort(localList, paramComparator);
/*     */ 
/*  61 */     for (Monitor localMonitor : localList)
/*  62 */       if ((localMonitor.isSupported()) || (paramBoolean))
/*     */       {
/*  65 */         paramPrintStream.println(localMonitor.getName());
/*     */       }
/*     */   }
/*     */ 
/*     */   public void printSnapShot(String paramString, Comparator<Monitor> paramComparator, boolean paramBoolean1, boolean paramBoolean2, PrintStream paramPrintStream)
/*     */     throws MonitorException, PatternSyntaxException
/*     */   {
/*  78 */     List localList = this.monitoredVm.findByPattern(paramString);
/*  79 */     Collections.sort(localList, paramComparator);
/*     */ 
/*  81 */     printList(localList, paramBoolean1, paramBoolean2, paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void printList(List<Monitor> paramList, boolean paramBoolean1, boolean paramBoolean2, PrintStream paramPrintStream)
/*     */     throws MonitorException
/*     */   {
/*  92 */     for (Monitor localMonitor : paramList)
/*     */     {
/*  94 */       if ((localMonitor.isSupported()) || (paramBoolean2))
/*     */       {
/*  98 */         StringBuilder localStringBuilder = new StringBuilder();
/*  99 */         localStringBuilder.append(localMonitor.getName()).append("=");
/*     */ 
/* 101 */         if ((localMonitor instanceof StringMonitor))
/* 102 */           localStringBuilder.append("\"").append(localMonitor.getValue()).append("\"");
/*     */         else {
/* 104 */           localStringBuilder.append(localMonitor.getValue());
/*     */         }
/*     */ 
/* 107 */         if (paramBoolean1) {
/* 108 */           localStringBuilder.append(" ").append(localMonitor.getUnits());
/* 109 */           localStringBuilder.append(" ").append(localMonitor.getVariability());
/* 110 */           localStringBuilder.append(" ").append(localMonitor.isSupported() ? "Supported" : "Unsupported");
/*     */         }
/*     */ 
/* 113 */         paramPrintStream.println(localStringBuilder);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stopLogging()
/*     */   {
/* 121 */     this.active = false;
/*     */   }
/*     */ 
/*     */   public void logSamples(OutputFormatter paramOutputFormatter, int paramInt1, int paramInt2, int paramInt3, PrintStream paramPrintStream)
/*     */     throws MonitorException
/*     */   {
/* 131 */     long l = 0L;
/* 132 */     int i = 0;
/*     */ 
/* 135 */     int j = paramInt1;
/* 136 */     if (j == 0)
/*     */     {
/* 138 */       paramPrintStream.println(paramOutputFormatter.getHeader());
/* 139 */       j = -1;
/*     */     }
/*     */ 
/* 142 */     while (this.active)
/*     */     {
/* 144 */       if (j > 0) { i--; if (i <= 0) {
/* 145 */           i = j;
/* 146 */           paramPrintStream.println(paramOutputFormatter.getHeader());
/*     */         }
/*     */       }
/* 149 */       paramPrintStream.println(paramOutputFormatter.getRow());
/*     */ 
/* 152 */       if ((paramInt3 > 0) && (++l >= paramInt3))
/*     */         break;
/*     */       try
/*     */       {
/* 156 */         Thread.sleep(paramInt2);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.JStatLogger
 * JD-Core Version:    0.6.2
 */