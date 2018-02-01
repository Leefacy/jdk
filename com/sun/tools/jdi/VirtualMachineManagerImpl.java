/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.JDIPermission;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.VirtualMachineManager;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.ListeningConnector;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.ServiceLoader;
/*     */ 
/*     */ public class VirtualMachineManagerImpl
/*     */   implements VirtualMachineManagerService
/*     */ {
/*  44 */   private List<Connector> connectors = new ArrayList();
/*  45 */   private LaunchingConnector defaultConnector = null;
/*  46 */   private List<VirtualMachine> targets = new ArrayList();
/*     */   private final ThreadGroup mainGroupForJDI;
/*  48 */   private ResourceBundle messages = null;
/*  49 */   private int vmSequenceNumber = 0;
/*     */   private static final int majorVersion = 1;
/*     */   private static final int minorVersion = 8;
/*  53 */   private static final Object lock = new Object();
/*     */   private static VirtualMachineManagerImpl vmm;
/*     */ 
/*     */   public static VirtualMachineManager virtualMachineManager()
/*     */   {
/*  57 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  58 */     if (localSecurityManager != null) {
/*  59 */       JDIPermission localJDIPermission = new JDIPermission("virtualMachineManager");
/*     */ 
/*  61 */       localSecurityManager.checkPermission(localJDIPermission);
/*     */     }
/*  63 */     synchronized (lock) {
/*  64 */       if (vmm == null) {
/*  65 */         vmm = new VirtualMachineManagerImpl();
/*     */       }
/*     */     }
/*  68 */     return vmm;
/*     */   }
/*     */ 
/*     */   protected VirtualMachineManagerImpl()
/*     */   {
/*  76 */     Object localObject1 = Thread.currentThread().getThreadGroup();
/*  77 */     ThreadGroup localThreadGroup = null;
/*  78 */     while ((localThreadGroup = ((ThreadGroup)localObject1).getParent()) != null) {
/*  79 */       localObject1 = localThreadGroup;
/*     */     }
/*  81 */     this.mainGroupForJDI = new ThreadGroup((ThreadGroup)localObject1, "JDI main");
/*     */ 
/*  87 */     ServiceLoader localServiceLoader = ServiceLoader.load(Connector.class, Connector.class
/*  87 */       .getClassLoader());
/*     */ 
/*  89 */     Iterator localIterator1 = localServiceLoader.iterator();
/*     */ 
/*  91 */     while (localIterator1.hasNext())
/*     */     {
/*     */       try
/*     */       {
/*  95 */         localObject2 = (Connector)localIterator1.next();
/*     */       } catch (ThreadDeath localThreadDeath1) {
/*  97 */         throw localThreadDeath1;
/*     */       } catch (Exception localException1) {
/*  99 */         System.err.println(localException1);
/* 100 */         continue;
/*     */       } catch (Error localError1) {
/* 102 */         System.err.println(localError1);
/* 103 */       }continue;
/*     */ 
/* 106 */       addConnector((Connector)localObject2);
/*     */     }
/*     */ 
/* 114 */     Object localObject2 = ServiceLoader.load(TransportService.class, TransportService.class
/* 115 */       .getClassLoader());
/*     */ 
/* 118 */     Iterator localIterator2 = ((ServiceLoader)localObject2)
/* 118 */       .iterator();
/*     */ 
/* 120 */     while (localIterator2.hasNext())
/*     */     {
/*     */       TransportService localTransportService;
/*     */       try {
/* 124 */         localTransportService = (TransportService)localIterator2.next();
/*     */       } catch (ThreadDeath localThreadDeath2) {
/* 126 */         throw localThreadDeath2;
/*     */       } catch (Exception localException2) {
/* 128 */         System.err.println(localException2);
/* 129 */         continue;
/*     */       } catch (Error localError2) {
/* 131 */         System.err.println(localError2);
/* 132 */       }continue;
/*     */ 
/* 135 */       addConnector(GenericAttachingConnector.create(localTransportService));
/* 136 */       addConnector(GenericListeningConnector.create(localTransportService));
/*     */     }
/*     */ 
/* 140 */     if (allConnectors().size() == 0) {
/* 141 */       throw new Error("no Connectors loaded");
/*     */     }
/*     */ 
/* 149 */     int i = 0;
/* 150 */     List localList = launchingConnectors();
/* 151 */     for (LaunchingConnector localLaunchingConnector : localList) {
/* 152 */       if (localLaunchingConnector.name().equals("com.sun.jdi.CommandLineLaunch")) {
/* 153 */         setDefaultConnector(localLaunchingConnector);
/* 154 */         i = 1;
/* 155 */         break;
/*     */       }
/*     */     }
/* 158 */     if ((i == 0) && (localList.size() > 0))
/* 159 */       setDefaultConnector((LaunchingConnector)localList.get(0));
/*     */   }
/*     */ 
/*     */   public LaunchingConnector defaultConnector()
/*     */   {
/* 165 */     if (this.defaultConnector == null) {
/* 166 */       throw new Error("no default LaunchingConnector");
/*     */     }
/* 168 */     return this.defaultConnector;
/*     */   }
/*     */ 
/*     */   public void setDefaultConnector(LaunchingConnector paramLaunchingConnector) {
/* 172 */     this.defaultConnector = paramLaunchingConnector;
/*     */   }
/*     */ 
/*     */   public List<LaunchingConnector> launchingConnectors() {
/* 176 */     ArrayList localArrayList = new ArrayList(this.connectors.size());
/* 177 */     for (Connector localConnector : this.connectors) {
/* 178 */       if ((localConnector instanceof LaunchingConnector)) {
/* 179 */         localArrayList.add((LaunchingConnector)localConnector);
/*     */       }
/*     */     }
/* 182 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List<AttachingConnector> attachingConnectors() {
/* 186 */     ArrayList localArrayList = new ArrayList(this.connectors.size());
/* 187 */     for (Connector localConnector : this.connectors) {
/* 188 */       if ((localConnector instanceof AttachingConnector)) {
/* 189 */         localArrayList.add((AttachingConnector)localConnector);
/*     */       }
/*     */     }
/* 192 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List<ListeningConnector> listeningConnectors() {
/* 196 */     ArrayList localArrayList = new ArrayList(this.connectors.size());
/* 197 */     for (Connector localConnector : this.connectors) {
/* 198 */       if ((localConnector instanceof ListeningConnector)) {
/* 199 */         localArrayList.add((ListeningConnector)localConnector);
/*     */       }
/*     */     }
/* 202 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List<Connector> allConnectors() {
/* 206 */     return Collections.unmodifiableList(this.connectors);
/*     */   }
/*     */ 
/*     */   public List<VirtualMachine> connectedVirtualMachines() {
/* 210 */     return Collections.unmodifiableList(this.targets);
/*     */   }
/*     */ 
/*     */   public void addConnector(Connector paramConnector) {
/* 214 */     this.connectors.add(paramConnector);
/*     */   }
/*     */ 
/*     */   public void removeConnector(Connector paramConnector) {
/* 218 */     this.connectors.remove(paramConnector);
/*     */   }
/*     */ 
/*     */   public synchronized VirtualMachine createVirtualMachine(Connection paramConnection, Process paramProcess)
/*     */     throws IOException
/*     */   {
/* 225 */     if (!paramConnection.isOpen()) {
/* 226 */       throw new IllegalStateException("connection is not open");
/*     */     }
/*     */     VirtualMachineImpl localVirtualMachineImpl;
/*     */     try
/*     */     {
/* 231 */       localVirtualMachineImpl = new VirtualMachineImpl(this, paramConnection, paramProcess, ++this.vmSequenceNumber);
/*     */     }
/*     */     catch (VMDisconnectedException localVMDisconnectedException) {
/* 234 */       throw new IOException(localVMDisconnectedException.getMessage());
/*     */     }
/* 236 */     this.targets.add(localVirtualMachineImpl);
/* 237 */     return localVirtualMachineImpl;
/*     */   }
/*     */ 
/*     */   public VirtualMachine createVirtualMachine(Connection paramConnection) throws IOException {
/* 241 */     return createVirtualMachine(paramConnection, null);
/*     */   }
/*     */ 
/*     */   public void addVirtualMachine(VirtualMachine paramVirtualMachine) {
/* 245 */     this.targets.add(paramVirtualMachine);
/*     */   }
/*     */ 
/*     */   void disposeVirtualMachine(VirtualMachine paramVirtualMachine) {
/* 249 */     this.targets.remove(paramVirtualMachine);
/*     */   }
/*     */ 
/*     */   public int majorInterfaceVersion() {
/* 253 */     return 1;
/*     */   }
/*     */ 
/*     */   public int minorInterfaceVersion() {
/* 257 */     return 8;
/*     */   }
/*     */ 
/*     */   ThreadGroup mainGroupForJDI() {
/* 261 */     return this.mainGroupForJDI;
/*     */   }
/*     */ 
/*     */   String getString(String paramString) {
/* 265 */     if (this.messages == null) {
/* 266 */       this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */     }
/* 268 */     return this.messages.getString(paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VirtualMachineManagerImpl
 * JD-Core Version:    0.6.2
 */