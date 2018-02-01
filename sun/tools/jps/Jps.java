/*    */ package sun.tools.jps;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.Set;
/*    */ import sun.jvmstat.monitor.HostIdentifier;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.MonitoredHost;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ import sun.jvmstat.monitor.MonitoredVmUtil;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ 
/*    */ public class Jps
/*    */ {
/*    */   private static Arguments arguments;
/*    */ 
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/*    */     try
/*    */     {
/* 44 */       arguments = new Arguments(paramArrayOfString);
/*    */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 46 */       System.err.println(localIllegalArgumentException.getMessage());
/* 47 */       Arguments.printUsage(System.err);
/* 48 */       System.exit(1);
/*    */     }
/*    */ 
/* 51 */     if (arguments.isHelp()) {
/* 52 */       Arguments.printUsage(System.err);
/* 53 */       System.exit(0);
/*    */     }
/*    */     try
/*    */     {
/* 57 */       HostIdentifier localHostIdentifier = arguments.hostId();
/*    */ 
/* 59 */       localObject1 = MonitoredHost.getMonitoredHost(localHostIdentifier);
/*    */ 
/* 62 */       Set localSet = ((MonitoredHost)localObject1).activeVms();
/*    */ 
/* 64 */       for (Integer localInteger : localSet) {
/* 65 */         StringBuilder localStringBuilder = new StringBuilder();
/* 66 */         Object localObject2 = null;
/*    */ 
/* 68 */         int i = localInteger.intValue();
/*    */ 
/* 70 */         localStringBuilder.append(String.valueOf(i));
/*    */ 
/* 72 */         if (arguments.isQuiet()) {
/* 73 */           System.out.println(localStringBuilder);
/*    */         }
/*    */         else
/*    */         {
/* 77 */           MonitoredVm localMonitoredVm = null;
/* 78 */           String str1 = "//" + i + "?mode=r";
/*    */ 
/* 80 */           String str2 = null;
/*    */           try
/*    */           {
/* 90 */             str2 = " -- process information unavailable";
/* 91 */             VmIdentifier localVmIdentifier = new VmIdentifier(str1);
/* 92 */             localMonitoredVm = ((MonitoredHost)localObject1).getMonitoredVm(localVmIdentifier, 0);
/*    */ 
/* 94 */             str2 = " -- main class information unavailable";
/* 95 */             localStringBuilder.append(" " + MonitoredVmUtil.mainClass(localMonitoredVm, arguments
/* 96 */               .showLongPaths()));
/*    */             String str3;
/* 98 */             if (arguments.showMainArgs()) {
/* 99 */               str2 = " -- main args information unavailable";
/* 100 */               str3 = MonitoredVmUtil.mainArgs(localMonitoredVm);
/* 101 */               if ((str3 != null) && (str3.length() > 0)) {
/* 102 */                 localStringBuilder.append(" " + str3);
/*    */               }
/*    */             }
/* 105 */             if (arguments.showVmArgs()) {
/* 106 */               str2 = " -- jvm args information unavailable";
/* 107 */               str3 = MonitoredVmUtil.jvmArgs(localMonitoredVm);
/* 108 */               if ((str3 != null) && (str3.length() > 0)) {
/* 109 */                 localStringBuilder.append(" " + str3);
/*    */               }
/*    */             }
/* 112 */             if (arguments.showVmFlags()) {
/* 113 */               str2 = " -- jvm flags information unavailable";
/* 114 */               str3 = MonitoredVmUtil.jvmFlags(localMonitoredVm);
/* 115 */               if ((str3 != null) && (str3.length() > 0)) {
/* 116 */                 localStringBuilder.append(" " + str3);
/*    */               }
/*    */             }
/*    */ 
/* 120 */             str2 = " -- detach failed";
/* 121 */             ((MonitoredHost)localObject1).detach(localMonitoredVm);
/*    */ 
/* 123 */             System.out.println(localStringBuilder);
/*    */ 
/* 125 */             str2 = null;
/*    */           }
/*    */           catch (URISyntaxException localURISyntaxException) {
/* 128 */             localObject2 = localURISyntaxException;
/* 129 */             if (!$assertionsDisabled) throw new AssertionError(); 
/*    */           }
/* 131 */           catch (Exception localException) { localObject2 = localException;
/*    */           } finally {
/* 133 */             if (str2 != null)
/*    */             {
/* 141 */               localStringBuilder.append(str2);
/* 142 */               if ((arguments.isDebug()) && 
/* 143 */                 (localObject2 != null) && 
/* 144 */                 (localObject2
/* 144 */                 .getMessage() != null)) {
/* 145 */                 localStringBuilder.append("\n\t");
/* 146 */                 localStringBuilder.append(localObject2.getMessage());
/*    */               }
/*    */ 
/* 149 */               System.out.println(localStringBuilder);
/* 150 */               if (!arguments.printStackTrace()) continue;
/* 151 */               localObject2.printStackTrace();
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (MonitorException localMonitorException)
/*    */     {
/*    */       Object localObject1;
/* 158 */       if (localMonitorException.getMessage() != null) {
/* 159 */         System.err.println(localMonitorException.getMessage());
/*    */       } else {
/* 161 */         localObject1 = localMonitorException.getCause();
/* 162 */         if ((localObject1 != null) && (((Throwable)localObject1).getMessage() != null))
/* 163 */           System.err.println(((Throwable)localObject1).getMessage());
/*    */         else {
/* 165 */           localMonitorException.printStackTrace();
/*    */         }
/*    */       }
/* 168 */       System.exit(1);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jps.Jps
 * JD-Core Version:    0.6.2
 */