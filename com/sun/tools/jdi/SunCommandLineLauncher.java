/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.Connector.Argument;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.Transport;
/*     */ import com.sun.jdi.connect.VMStartException;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import com.sun.jdi.connect.spi.TransportService.ListenKey;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class SunCommandLineLauncher extends AbstractLauncher
/*     */   implements LaunchingConnector
/*     */ {
/*     */   private static final String ARG_HOME = "home";
/*     */   private static final String ARG_OPTIONS = "options";
/*     */   private static final String ARG_MAIN = "main";
/*     */   private static final String ARG_INIT_SUSPEND = "suspend";
/*     */   private static final String ARG_QUOTE = "quote";
/*     */   private static final String ARG_VM_EXEC = "vmexec";
/*     */   TransportService transportService;
/*     */   Transport transport;
/*  49 */   boolean usingSharedMemory = false;
/*     */ 
/*     */   TransportService transportService() {
/*  52 */     return this.transportService;
/*     */   }
/*     */ 
/*     */   public Transport transport() {
/*  56 */     return this.transport;
/*     */   }
/*     */ 
/*     */   public SunCommandLineLauncher()
/*     */   {
/*     */     try
/*     */     {
/*  67 */       Class localClass = Class.forName("com.sun.tools.jdi.SharedMemoryTransportService");
/*  68 */       this.transportService = ((TransportService)localClass.newInstance());
/*  69 */       this.transport = new Transport() {
/*     */         public String name() {
/*  71 */           return "dt_shmem";
/*     */         }
/*     */       };
/*  74 */       this.usingSharedMemory = true;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
/*     */     } catch (InstantiationException localInstantiationException) {
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*     */     }
/*  80 */     if (this.transportService == null) {
/*  81 */       this.transportService = new SocketTransportService();
/*  82 */       this.transport = new Transport() {
/*     */         public String name() {
/*  84 */           return "dt_socket";
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*  89 */     addStringArgument("home", 
/*  91 */       getString("sun.home.label"), 
/*  92 */       getString("sun.home"), 
/*  93 */       System.getProperty("java.home"), 
/*  93 */       false);
/*     */ 
/*  95 */     addStringArgument("options", 
/*  97 */       getString("sun.options.label"), 
/*  98 */       getString("sun.options"), 
/*  98 */       "", false);
/*     */ 
/* 101 */     addStringArgument("main", 
/* 103 */       getString("sun.main.label"), 
/* 104 */       getString("sun.main"), 
/* 104 */       "", true);
/*     */ 
/* 108 */     addBooleanArgument("suspend", 
/* 110 */       getString("sun.init_suspend.label"), 
/* 111 */       getString("sun.init_suspend"), 
/* 111 */       true, false);
/*     */ 
/* 115 */     addStringArgument("quote", 
/* 117 */       getString("sun.quote.label"), 
/* 118 */       getString("sun.quote"), 
/* 118 */       "\"", true);
/*     */ 
/* 121 */     addStringArgument("vmexec", 
/* 123 */       getString("sun.vm_exec.label"), 
/* 124 */       getString("sun.vm_exec"), 
/* 124 */       "java", true);
/*     */   }
/*     */ 
/*     */   static boolean hasWhitespace(String paramString)
/*     */   {
/* 130 */     int i = paramString.length();
/* 131 */     for (int j = 0; j < i; j++) {
/* 132 */       if (Character.isWhitespace(paramString.charAt(j))) {
/* 133 */         return true;
/*     */       }
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   public VirtualMachine launch(Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException, VMStartException
/*     */   {
/* 146 */     String str1 = argument("home", paramMap).value();
/* 147 */     String str2 = argument("options", paramMap).value();
/* 148 */     String str3 = argument("main", paramMap).value();
/*     */ 
/* 150 */     boolean bool = ((ConnectorImpl.BooleanArgumentImpl)argument("suspend", paramMap))
/* 150 */       .booleanValue();
/* 151 */     String str4 = argument("quote", paramMap).value();
/* 152 */     String str5 = argument("vmexec", paramMap).value();
/* 153 */     String str6 = null;
/*     */ 
/* 155 */     if (str4.length() > 1) {
/* 156 */       throw new IllegalConnectorArgumentsException("Invalid length", "quote");
/*     */     }
/*     */ 
/* 160 */     if ((str2.indexOf("-Djava.compiler=") != -1) && 
/* 161 */       (str2
/* 161 */       .toLowerCase().indexOf("-djava.compiler=none") == -1))
/* 162 */       throw new IllegalConnectorArgumentsException("Cannot debug with a JIT compiler", "options");
/*     */     TransportService.ListenKey localListenKey;
/* 177 */     if (this.usingSharedMemory) {
/* 178 */       localObject1 = new Random();
/* 179 */       int i = 0;
/*     */       while (true)
/*     */         try
/*     */         {
/* 183 */           String str8 = "javadebug" + 
/* 183 */             String.valueOf(((Random)localObject1)
/* 183 */             .nextInt(100000));
/*     */ 
/* 184 */           localListenKey = transportService().startListening(str8);
/*     */         }
/*     */         catch (IOException localIOException) {
/* 187 */           i++; if (i > 5)
/* 188 */             throw localIOException;
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/* 193 */       localListenKey = transportService().startListening();
/* 195 */     }Object localObject1 = localListenKey.address();
/*     */     VirtualMachine localVirtualMachine;
/*     */     try {
/* 198 */       if (str1.length() > 0)
/* 199 */         str6 = str1 + File.separator + "bin" + File.separator + str5;
/*     */       else {
/* 201 */         str6 = str5;
/*     */       }
/*     */ 
/* 204 */       if (hasWhitespace(str6)) {
/* 205 */         str6 = str4 + str6 + str4;
/*     */       }
/*     */ 
/* 208 */       String str7 = "transport=" + transport().name() + ",address=" + (String)localObject1 + ",suspend=" + (bool ? 'y' : 'n');
/*     */ 
/* 212 */       if (hasWhitespace(str7)) {
/* 213 */         str7 = str4 + str7 + str4;
/*     */       }
/*     */ 
/* 216 */       String str9 = str6 + ' ' + str2 + ' ' + "-Xdebug " + "-Xrunjdwp:" + str7 + ' ' + str3;
/*     */ 
/* 223 */       localVirtualMachine = launch(tokenizeCommand(str9, str4.charAt(0)), (String)localObject1, localListenKey, 
/* 224 */         transportService());
/*     */     } finally {
/* 226 */       transportService().stopListening(localListenKey);
/*     */     }
/*     */ 
/* 229 */     return localVirtualMachine;
/*     */   }
/*     */ 
/*     */   public String name() {
/* 233 */     return "com.sun.jdi.CommandLineLaunch";
/*     */   }
/*     */ 
/*     */   public String description() {
/* 237 */     return getString("sun.description");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SunCommandLineLauncher
 * JD-Core Version:    0.6.2
 */