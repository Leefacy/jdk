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
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class RawCommandLineLauncher extends AbstractLauncher
/*     */   implements LaunchingConnector
/*     */ {
/*     */   private static final String ARG_COMMAND = "command";
/*     */   private static final String ARG_ADDRESS = "address";
/*     */   private static final String ARG_QUOTE = "quote";
/*     */   TransportService transportService;
/*     */   Transport transport;
/*     */ 
/*     */   public TransportService transportService()
/*     */   {
/*  45 */     return this.transportService;
/*     */   }
/*     */ 
/*     */   public Transport transport() {
/*  49 */     return this.transport;
/*     */   }
/*     */ 
/*     */   public RawCommandLineLauncher()
/*     */   {
/*     */     try
/*     */     {
/*  56 */       Class localClass = Class.forName("com.sun.tools.jdi.SharedMemoryTransportService");
/*  57 */       this.transportService = ((TransportService)localClass.newInstance());
/*  58 */       this.transport = new Transport() {
/*     */         public String name() {
/*  60 */           return "dt_shmem";
/*     */         } } ;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/*     */     }
/*  69 */     if (this.transportService == null) {
/*  70 */       this.transportService = new SocketTransportService();
/*  71 */       this.transport = new Transport() {
/*     */         public String name() {
/*  73 */           return "dt_socket";
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*  78 */     addStringArgument("command", 
/*  80 */       getString("raw.command.label"), 
/*  81 */       getString("raw.command"), 
/*  81 */       "", true);
/*     */ 
/*  84 */     addStringArgument("quote", 
/*  86 */       getString("raw.quote.label"), 
/*  87 */       getString("raw.quote"), 
/*  87 */       "\"", true);
/*     */ 
/*  91 */     addStringArgument("address", 
/*  93 */       getString("raw.address.label"), 
/*  94 */       getString("raw.address"), 
/*  94 */       "", true);
/*     */   }
/*     */ 
/*     */   public VirtualMachine launch(Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException, VMStartException
/*     */   {
/* 105 */     String str1 = argument("command", paramMap).value();
/* 106 */     String str2 = argument("address", paramMap).value();
/*     */ 
/* 108 */     String str3 = argument("quote", paramMap).value();
/*     */ 
/* 110 */     if (str3.length() > 1) {
/* 111 */       throw new IllegalConnectorArgumentsException("Invalid length", "quote");
/*     */     }
/*     */ 
/* 115 */     TransportService.ListenKey localListenKey = this.transportService.startListening(str2);
/*     */     try
/*     */     {
/* 118 */       return launch(tokenizeCommand(str1, str3.charAt(0)), str2, localListenKey, this.transportService);
/*     */     }
/*     */     finally {
/* 121 */       this.transportService.stopListening(localListenKey);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String name() {
/* 126 */     return "com.sun.jdi.RawCommandLineLaunch";
/*     */   }
/*     */ 
/*     */   public String description() {
/* 130 */     return getString("raw.description");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.RawCommandLineLauncher
 * JD-Core Version:    0.6.2
 */