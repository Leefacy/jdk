/*     */ package sun.tools.jcmd;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachOperationFailedException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.MonitoredVmUtil;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
/*     */ import sun.tools.jstat.JStatLogger;
/*     */ 
/*     */ public class JCmd
/*     */ {
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  53 */     Arguments localArguments = null;
/*     */     try {
/*  55 */       localArguments = new Arguments(paramArrayOfString);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*  57 */       System.err.println("Error parsing arguments: " + localIllegalArgumentException.getMessage() + "\n");
/*     */ 
/*  59 */       Arguments.usage();
/*  60 */       System.exit(1);
/*     */     }
/*     */ 
/*  63 */     if (localArguments.isShowUsage()) {
/*  64 */       Arguments.usage();
/*  65 */       System.exit(1);
/*     */     }
/*     */     Object localObject2;
/*  68 */     if (localArguments.isListProcesses()) {
/*  69 */       localObject1 = VirtualMachine.list();
/*  70 */       for (localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (VirtualMachineDescriptor)((Iterator)localObject2).next();
/*  71 */         System.out.println(((VirtualMachineDescriptor)localObject3).id() + " " + ((VirtualMachineDescriptor)localObject3).displayName());
/*     */       }
/*  73 */       System.exit(0);
/*     */     }
/*     */ 
/*  76 */     Object localObject1 = new ArrayList();
/*     */     Object localObject4;
/*  77 */     if (localArguments.getPid() == 0)
/*     */     {
/*  79 */       localObject2 = VirtualMachine.list();
/*  80 */       for (localObject3 = ((List)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (VirtualMachineDescriptor)((Iterator)localObject3).next();
/*  81 */         if (!isJCmdProcess((VirtualMachineDescriptor)localObject4))
/*  82 */           ((List)localObject1).add(((VirtualMachineDescriptor)localObject4).id());
/*     */       }
/*     */     }
/*  85 */     else if (localArguments.getProcessSubstring() != null)
/*     */     {
/*  87 */       localObject2 = VirtualMachine.list();
/*  88 */       for (localObject3 = ((List)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (VirtualMachineDescriptor)((Iterator)localObject3).next();
/*  89 */         if (!isJCmdProcess((VirtualMachineDescriptor)localObject4))
/*     */         {
/*     */           try
/*     */           {
/*  93 */             String str = getMainClass((VirtualMachineDescriptor)localObject4);
/*  94 */             if ((str != null) && 
/*  95 */               (str
/*  95 */               .indexOf(localArguments
/*  95 */               .getProcessSubstring()) != -1))
/*  96 */               ((List)localObject1).add(((VirtualMachineDescriptor)localObject4).id());
/*     */           }
/*     */           catch (MonitorException|URISyntaxException localMonitorException) {
/*  99 */             if (localMonitorException.getMessage() != null) {
/* 100 */               System.err.println(localMonitorException.getMessage());
/*     */             } else {
/* 102 */               Throwable localThrowable = localMonitorException.getCause();
/* 103 */               if ((localThrowable != null) && (localThrowable.getMessage() != null))
/* 104 */                 System.err.println(localThrowable.getMessage());
/*     */               else
/* 106 */                 localMonitorException.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 111 */       if (((List)localObject1).isEmpty()) {
/* 112 */         System.err.println("Could not find any processes matching : '" + localArguments
/* 113 */           .getProcessSubstring() + "'");
/* 114 */         System.exit(1);
/*     */       }
/* 116 */     } else if (localArguments.getPid() == -1) {
/* 117 */       System.err.println("Invalid pid specified");
/* 118 */       System.exit(1);
/*     */     }
/*     */     else {
/* 121 */       ((List)localObject1).add(localArguments.getPid() + "");
/*     */     }
/*     */ 
/* 124 */     int i = 1;
/* 125 */     for (Object localObject3 = ((List)localObject1).iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (String)((Iterator)localObject3).next();
/* 126 */       System.out.println((String)localObject4 + ":");
/* 127 */       if (localArguments.isListCounters())
/* 128 */         listCounters((String)localObject4);
/*     */       else {
/*     */         try {
/* 131 */           executeCommandForPid((String)localObject4, localArguments.getCommand());
/*     */         } catch (AttachOperationFailedException localAttachOperationFailedException) {
/* 133 */           System.err.println(localAttachOperationFailedException.getMessage());
/* 134 */           i = 0;
/*     */         } catch (Exception localException) {
/* 136 */           localException.printStackTrace();
/* 137 */           i = 0;
/*     */         }
/*     */       }
/*     */     }
/* 141 */     System.exit(i != 0 ? 0 : 1);
/*     */   }
/*     */ 
/*     */   private static void executeCommandForPid(String paramString1, String paramString2)
/*     */     throws AttachNotSupportedException, IOException, UnsupportedEncodingException
/*     */   {
/* 147 */     VirtualMachine localVirtualMachine = VirtualMachine.attach(paramString1);
/*     */ 
/* 151 */     HotSpotVirtualMachine localHotSpotVirtualMachine = (HotSpotVirtualMachine)localVirtualMachine;
/* 152 */     String[] arrayOfString1 = paramString2.split("\\n");
/* 153 */     for (String str1 : arrayOfString1) {
/* 154 */       if (str1.trim().equals("stop")) {
/*     */         break;
/*     */       }
/* 157 */       InputStream localInputStream = localHotSpotVirtualMachine.executeJCmd(str1); Object localObject1 = null;
/*     */       try { byte[] arrayOfByte = new byte[256];
/*     */ 
/* 161 */         int m = 0;
/*     */         int k;
/*     */         do { k = localInputStream.read(arrayOfByte);
/* 164 */           if (k > 0) {
/* 165 */             String str2 = new String(arrayOfByte, 0, k, "UTF-8");
/* 166 */             System.out.print(str2);
/* 167 */             m = 1;
/*     */           } }
/* 169 */         while (k > 0);
/* 170 */         if (m == 0)
/* 171 */           System.out.println("Command executed successfully");
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 157 */         localObject1 = localThrowable2; throw localThrowable2;
/*     */       }
/*     */       finally
/*     */       {
/* 173 */         if (localInputStream != null) if (localObject1 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localInputStream.close(); 
/*     */       }
/*     */     }
/* 175 */     localVirtualMachine.detach();
/*     */   }
/*     */ 
/*     */   private static void listCounters(String paramString)
/*     */   {
/* 180 */     VmIdentifier localVmIdentifier = null;
/*     */     try {
/* 182 */       localVmIdentifier = new VmIdentifier(paramString);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 184 */       System.err.println("Malformed VM Identifier: " + paramString);
/* 185 */       return;
/*     */     }
/*     */     try {
/* 188 */       MonitoredHost localMonitoredHost = MonitoredHost.getMonitoredHost(localVmIdentifier);
/* 189 */       MonitoredVm localMonitoredVm = localMonitoredHost.getMonitoredVm(localVmIdentifier, -1);
/* 190 */       JStatLogger localJStatLogger = new JStatLogger(localMonitoredVm);
/* 191 */       localJStatLogger.printSnapShot("\\w*", new AscendingMonitorComparator(), false, true, System.out);
/*     */ 
/* 196 */       localMonitoredHost.detach(localMonitoredVm);
/*     */     } catch (MonitorException localMonitorException) {
/* 198 */       localMonitorException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isJCmdProcess(VirtualMachineDescriptor paramVirtualMachineDescriptor) {
/*     */     try {
/* 204 */       String str = getMainClass(paramVirtualMachineDescriptor);
/* 205 */       return (str != null) && (str.equals(JCmd.class.getName())); } catch (URISyntaxException|MonitorException localURISyntaxException) {
/*     */     }
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   private static String getMainClass(VirtualMachineDescriptor paramVirtualMachineDescriptor) throws URISyntaxException, MonitorException
/*     */   {
/*     */     try
/*     */     {
/* 214 */       String str = null;
/* 215 */       VmIdentifier localVmIdentifier = new VmIdentifier(paramVirtualMachineDescriptor.id());
/* 216 */       MonitoredHost localMonitoredHost = MonitoredHost.getMonitoredHost(localVmIdentifier);
/* 217 */       MonitoredVm localMonitoredVm = localMonitoredHost.getMonitoredVm(localVmIdentifier, -1);
/* 218 */       str = MonitoredVmUtil.mainClass(localMonitoredVm, true);
/* 219 */       localMonitoredHost.detach(localMonitoredVm);
/* 220 */       return str;
/*     */     }
/*     */     catch (NullPointerException localNullPointerException)
/*     */     {
/*     */     }
/*     */ 
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   static class AscendingMonitorComparator
/*     */     implements Comparator<Monitor>
/*     */   {
/*     */     public int compare(Monitor paramMonitor1, Monitor paramMonitor2)
/*     */     {
/* 239 */       String str1 = paramMonitor1.getName();
/* 240 */       String str2 = paramMonitor2.getName();
/* 241 */       return str1.compareTo(str2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jcmd.JCmd
 * JD-Core Version:    0.6.2
 */