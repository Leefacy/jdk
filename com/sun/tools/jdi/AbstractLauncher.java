/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.VirtualMachineManager;
/*     */ import com.sun.jdi.connect.Connector.Argument;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.VMStartException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import com.sun.jdi.connect.spi.TransportService.ListenKey;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ abstract class AbstractLauncher extends ConnectorImpl
/*     */   implements LaunchingConnector
/*     */ {
/*     */   ThreadGroup grp;
/*     */ 
/*     */   public abstract VirtualMachine launch(Map<String, ? extends Connector.Argument> paramMap)
/*     */     throws IOException, IllegalConnectorArgumentsException, VMStartException;
/*     */ 
/*     */   public abstract String name();
/*     */ 
/*     */   public abstract String description();
/*     */ 
/*     */   AbstractLauncher()
/*     */   {
/*  55 */     this.grp = Thread.currentThread().getThreadGroup();
/*  56 */     ThreadGroup localThreadGroup = null;
/*  57 */     while ((localThreadGroup = this.grp.getParent()) != null)
/*  58 */       this.grp = localThreadGroup;
/*     */   }
/*     */ 
/*     */   String[] tokenizeCommand(String paramString, char paramChar)
/*     */   {
/*  63 */     String str = String.valueOf(paramChar);
/*     */ 
/*  68 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, paramChar + " \t\r\n\f", true);
/*     */ 
/*  71 */     Object localObject1 = null;
/*  72 */     Object localObject2 = null;
/*  73 */     ArrayList localArrayList = new ArrayList();
/*  74 */     while (localStringTokenizer.hasMoreTokens()) {
/*  75 */       localObject3 = localStringTokenizer.nextToken();
/*  76 */       if (localObject1 != null) {
/*  77 */         if (((String)localObject3).equals(str)) {
/*  78 */           localArrayList.add(localObject1);
/*  79 */           localObject1 = null;
/*     */         } else {
/*  81 */           localObject1 = (String)localObject1 + (String)localObject3;
/*     */         }
/*  83 */       } else if (localObject2 != null) {
/*  84 */         if (((String)localObject3).equals(str))
/*  85 */           localObject1 = localObject2;
/*  86 */         else if ((((String)localObject3).length() == 1) && 
/*  87 */           (Character.isWhitespace(((String)localObject3)
/*  87 */           .charAt(0))))
/*     */         {
/*  88 */           localArrayList.add(localObject2);
/*     */         }
/*  90 */         else throw new InternalException("Unexpected token: " + (String)localObject3);
/*     */ 
/*  92 */         localObject2 = null;
/*     */       }
/*  94 */       else if (((String)localObject3).equals(str)) {
/*  95 */         localObject1 = "";
/*  96 */       } else if ((((String)localObject3).length() != 1) || 
/*  97 */         (!Character.isWhitespace(((String)localObject3)
/*  97 */         .charAt(0))))
/*     */       {
/* 100 */         localObject2 = localObject3;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 108 */     if (localObject2 != null) {
/* 109 */       localArrayList.add(localObject2);
/*     */     }
/*     */ 
/* 116 */     if (localObject1 != null) {
/* 117 */       localArrayList.add(localObject1);
/*     */     }
/*     */ 
/* 120 */     Object localObject3 = new String[localArrayList.size()];
/* 121 */     for (int i = 0; i < localArrayList.size(); i++) {
/* 122 */       localObject3[i] = ((String)localArrayList.get(i));
/*     */     }
/* 124 */     return localObject3;
/*     */   }
/*     */ 
/*     */   protected VirtualMachine launch(String[] paramArrayOfString, String paramString, TransportService.ListenKey paramListenKey, TransportService paramTransportService)
/*     */     throws IOException, VMStartException
/*     */   {
/* 131 */     Helper localHelper = new Helper(paramArrayOfString, paramString, paramListenKey, paramTransportService);
/* 132 */     localHelper.launchAndAccept();
/*     */ 
/* 135 */     VirtualMachineManager localVirtualMachineManager = Bootstrap.virtualMachineManager();
/*     */ 
/* 137 */     return localVirtualMachineManager.createVirtualMachine(localHelper.connection(), localHelper
/* 138 */       .process());
/*     */   }
/*     */ 
/*     */   private class Helper
/*     */   {
/*     */     private final String address;
/*     */     private TransportService.ListenKey listenKey;
/*     */     private TransportService ts;
/*     */     private final String[] commandArray;
/* 153 */     private Process process = null;
/* 154 */     private Connection connection = null;
/* 155 */     private IOException acceptException = null;
/* 156 */     private boolean exited = false;
/*     */ 
/*     */     Helper(String[] paramString, String paramListenKey, TransportService.ListenKey paramTransportService, TransportService arg5)
/*     */     {
/* 160 */       this.commandArray = paramString;
/* 161 */       this.address = paramListenKey;
/* 162 */       this.listenKey = paramTransportService;
/*     */       Object localObject;
/* 163 */       this.ts = localObject;
/*     */     }
/*     */ 
/*     */     String commandString() {
/* 167 */       String str = "";
/* 168 */       for (int i = 0; i < this.commandArray.length; i++) {
/* 169 */         if (i > 0) {
/* 170 */           str = str + " ";
/*     */         }
/* 172 */         str = str + this.commandArray[i];
/*     */       }
/* 174 */       return str;
/*     */     }
/*     */ 
/*     */     synchronized void launchAndAccept()
/*     */       throws IOException, VMStartException
/*     */     {
/* 180 */       this.process = Runtime.getRuntime().exec(this.commandArray);
/*     */ 
/* 182 */       Thread localThread1 = acceptConnection();
/* 183 */       Thread localThread2 = monitorTarget();
/*     */       try {
/* 185 */         while ((this.connection == null) && (this.acceptException == null) && (!this.exited))
/*     */         {
/* 188 */           wait();
/*     */         }
/*     */ 
/* 191 */         if (this.exited)
/*     */         {
/* 193 */           throw new VMStartException("VM initialization failed for: " + 
/* 193 */             commandString(), this.process);
/*     */         }
/* 195 */         if (this.acceptException != null)
/*     */         {
/* 197 */           throw this.acceptException;
/*     */         }
/*     */       } catch (InterruptedException localInterruptedException) {
/* 200 */         throw new InterruptedIOException("Interrupted during accept");
/*     */       } finally {
/* 202 */         localThread1.interrupt();
/* 203 */         localThread2.interrupt();
/*     */       }
/*     */     }
/*     */ 
/*     */     Process process() {
/* 208 */       return this.process;
/*     */     }
/*     */ 
/*     */     Connection connection() {
/* 212 */       return this.connection;
/*     */     }
/*     */ 
/*     */     synchronized void notifyOfExit() {
/* 216 */       this.exited = true;
/* 217 */       notify();
/*     */     }
/*     */ 
/*     */     synchronized void notifyOfConnection(Connection paramConnection) {
/* 221 */       this.connection = paramConnection;
/* 222 */       notify();
/*     */     }
/*     */ 
/*     */     synchronized void notifyOfAcceptException(IOException paramIOException) {
/* 226 */       this.acceptException = paramIOException;
/* 227 */       notify();
/*     */     }
/*     */ 
/*     */     Thread monitorTarget() {
/* 231 */       Thread local1 = new Thread(AbstractLauncher.this.grp, "launched target monitor")
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 235 */             AbstractLauncher.Helper.this.process.waitFor();
/*     */ 
/* 239 */             AbstractLauncher.Helper.this.notifyOfExit();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/*     */         }
/*     */       };
/* 245 */       local1.setDaemon(true);
/* 246 */       local1.start();
/* 247 */       return local1;
/*     */     }
/*     */ 
/*     */     Thread acceptConnection() {
/* 251 */       Thread local2 = new Thread(AbstractLauncher.this.grp, "connection acceptor")
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 255 */             Connection localConnection = AbstractLauncher.Helper.this.ts.accept(AbstractLauncher.Helper.this.listenKey, 0L, 0L);
/*     */ 
/* 259 */             AbstractLauncher.Helper.this.notifyOfConnection(localConnection);
/*     */           }
/*     */           catch (InterruptedIOException localInterruptedIOException) {
/*     */           }
/*     */           catch (IOException localIOException) {
/* 264 */             AbstractLauncher.Helper.this.notifyOfAcceptException(localIOException);
/*     */           }
/*     */         }
/*     */       };
/* 268 */       local2.setDaemon(true);
/* 269 */       local2.start();
/* 270 */       return local2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.AbstractLauncher
 * JD-Core Version:    0.6.2
 */