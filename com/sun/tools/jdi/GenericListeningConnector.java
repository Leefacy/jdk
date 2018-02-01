/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.VirtualMachineManager;
/*     */ import com.sun.jdi.connect.Connector.Argument;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.ListeningConnector;
/*     */ import com.sun.jdi.connect.Transport;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import com.sun.jdi.connect.spi.TransportService.Capabilities;
/*     */ import com.sun.jdi.connect.spi.TransportService.ListenKey;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class GenericListeningConnector extends ConnectorImpl
/*     */   implements ListeningConnector
/*     */ {
/*     */   static final String ARG_ADDRESS = "address";
/*     */   static final String ARG_TIMEOUT = "timeout";
/*     */   Map<Map<String, ? extends Connector.Argument>, TransportService.ListenKey> listenMap;
/*     */   TransportService transportService;
/*     */   Transport transport;
/*     */ 
/*     */   private GenericListeningConnector(TransportService paramTransportService, boolean paramBoolean)
/*     */   {
/*  60 */     this.transportService = paramTransportService;
/*  61 */     this.transport = new Transport() {
/*     */       public String name() {
/*  63 */         return GenericListeningConnector.this.transportService.name();
/*     */       }
/*     */     };
/*  67 */     if (paramBoolean) {
/*  68 */       addStringArgument("address", 
/*  70 */         getString("generic_listening.address.label"), 
/*  71 */         getString("generic_listening.address"), 
/*  71 */         "", false);
/*     */     }
/*     */ 
/*  76 */     addIntegerArgument("timeout", 
/*  78 */       getString("generic_listening.timeout.label"), 
/*  79 */       getString("generic_listening.timeout"), 
/*  79 */       "", false, 0, 2147483647);
/*     */ 
/*  84 */     this.listenMap = new HashMap(10);
/*     */   }
/*     */ 
/*     */   protected GenericListeningConnector(TransportService paramTransportService)
/*     */   {
/*  93 */     this(paramTransportService, false);
/*     */   }
/*     */ 
/*     */   public static GenericListeningConnector create(TransportService paramTransportService)
/*     */   {
/* 101 */     return new GenericListeningConnector(paramTransportService, true);
/*     */   }
/*     */ 
/*     */   public String startListening(String paramString, Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException
/*     */   {
/* 107 */     TransportService.ListenKey localListenKey = (TransportService.ListenKey)this.listenMap.get(paramMap);
/* 108 */     if (localListenKey != null)
/*     */     {
/* 110 */       throw new IllegalConnectorArgumentsException("Already listening", new ArrayList(paramMap
/* 110 */         .keySet()));
/*     */     }
/*     */ 
/* 113 */     localListenKey = this.transportService.startListening(paramString);
/* 114 */     this.listenMap.put(paramMap, localListenKey);
/* 115 */     return localListenKey.address();
/*     */   }
/*     */ 
/*     */   public String startListening(Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException
/*     */   {
/* 122 */     String str = argument("address", paramMap).value();
/* 123 */     return startListening(str, paramMap);
/*     */   }
/*     */ 
/*     */   public void stopListening(Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException
/*     */   {
/* 129 */     TransportService.ListenKey localListenKey = (TransportService.ListenKey)this.listenMap.get(paramMap);
/* 130 */     if (localListenKey == null)
/*     */     {
/* 132 */       throw new IllegalConnectorArgumentsException("Not listening", new ArrayList(paramMap
/* 132 */         .keySet()));
/*     */     }
/* 134 */     this.transportService.stopListening(localListenKey);
/* 135 */     this.listenMap.remove(paramMap);
/*     */   }
/*     */ 
/*     */   public VirtualMachine accept(Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException
/*     */   {
/* 142 */     String str = argument("timeout", paramMap).value();
/* 143 */     int i = 0;
/* 144 */     if (str.length() > 0) {
/* 145 */       i = Integer.decode(str).intValue();
/*     */     }
/*     */ 
/* 148 */     TransportService.ListenKey localListenKey = (TransportService.ListenKey)this.listenMap.get(paramMap);
/*     */     Connection localConnection;
/* 150 */     if (localListenKey != null) {
/* 151 */       localConnection = this.transportService.accept(localListenKey, i, 0L);
/*     */     }
/*     */     else
/*     */     {
/* 158 */       startListening(paramMap);
/* 159 */       localListenKey = (TransportService.ListenKey)this.listenMap.get(paramMap);
/* 160 */       assert (localListenKey != null);
/* 161 */       localConnection = this.transportService.accept(localListenKey, i, 0L);
/* 162 */       stopListening(paramMap);
/*     */     }
/* 164 */     return Bootstrap.virtualMachineManager().createVirtualMachine(localConnection);
/*     */   }
/*     */ 
/*     */   public boolean supportsMultipleConnections() {
/* 168 */     return this.transportService.capabilities().supportsMultipleConnections();
/*     */   }
/*     */ 
/*     */   public String name() {
/* 172 */     return this.transport.name() + "Listen";
/*     */   }
/*     */ 
/*     */   public String description() {
/* 176 */     return this.transportService.description();
/*     */   }
/*     */ 
/*     */   public Transport transport() {
/* 180 */     return this.transport;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.GenericListeningConnector
 * JD-Core Version:    0.6.2
 */