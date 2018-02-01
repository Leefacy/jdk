/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TargetVM
/*     */   implements Runnable
/*     */ {
/*  37 */   private Map<String, Packet> waitingQueue = new HashMap(32, 0.75F);
/*  38 */   private boolean shouldListen = true;
/*  39 */   private List<EventQueue> eventQueues = Collections.synchronizedList(new ArrayList(2));
/*     */   private VirtualMachineImpl vm;
/*     */   private Connection connection;
/*     */   private Thread readerThread;
/*  43 */   private EventController eventController = null;
/*  44 */   private boolean eventsHeld = false;
/*     */   private static final int OVERLOADED_QUEUE = 2000;
/*     */   private static final int UNDERLOADED_QUEUE = 100;
/*     */ 
/*     */   TargetVM(VirtualMachineImpl paramVirtualMachineImpl, Connection paramConnection)
/*     */   {
/*  54 */     this.vm = paramVirtualMachineImpl;
/*  55 */     this.connection = paramConnection;
/*  56 */     this.readerThread = new Thread(paramVirtualMachineImpl.threadGroupForJDI(), this, "JDI Target VM Interface");
/*     */ 
/*  58 */     this.readerThread.setDaemon(true);
/*     */   }
/*     */ 
/*     */   void start() {
/*  62 */     this.readerThread.start();
/*     */   }
/*     */ 
/*     */   private void dumpPacket(Packet paramPacket, boolean paramBoolean) {
/*  66 */     String str1 = paramBoolean ? "Sending" : "Receiving";
/*  67 */     if (paramBoolean) {
/*  68 */       this.vm.printTrace(str1 + " Command. id=" + paramPacket.id + ", length=" + paramPacket.data.length + ", commandSet=" + paramPacket.cmdSet + ", command=" + paramPacket.cmd + ", flags=" + paramPacket.flags);
/*     */     }
/*     */     else
/*     */     {
/*  74 */       localObject = (paramPacket.flags & 0x80) != 0 ? "Reply" : "Event";
/*     */ 
/*  76 */       this.vm.printTrace(str1 + " " + (String)localObject + ". id=" + paramPacket.id + ", length=" + paramPacket.data.length + ", errorCode=" + paramPacket.errorCode + ", flags=" + paramPacket.flags);
/*     */     }
/*     */ 
/*  81 */     Object localObject = new StringBuffer(80);
/*  82 */     ((StringBuffer)localObject).append("0000: ");
/*  83 */     for (int i = 0; i < paramPacket.data.length; i++) {
/*  84 */       if ((i > 0) && (i % 16 == 0)) {
/*  85 */         this.vm.printTrace(((StringBuffer)localObject).toString());
/*  86 */         ((StringBuffer)localObject).setLength(0);
/*  87 */         ((StringBuffer)localObject).append(String.valueOf(i));
/*  88 */         ((StringBuffer)localObject).append(": ");
/*  89 */         j = ((StringBuffer)localObject).length();
/*  90 */         for (int k = 0; k < 6 - j; k++) {
/*  91 */           ((StringBuffer)localObject).insert(0, '0');
/*     */         }
/*     */       }
/*  94 */       int j = 0xFF & paramPacket.data[i];
/*  95 */       String str2 = Integer.toHexString(j);
/*  96 */       if (str2.length() == 1) {
/*  97 */         ((StringBuffer)localObject).append('0');
/*     */       }
/*  99 */       ((StringBuffer)localObject).append(str2);
/* 100 */       ((StringBuffer)localObject).append(' ');
/*     */     }
/* 102 */     if (((StringBuffer)localObject).length() > 6)
/* 103 */       this.vm.printTrace(((StringBuffer)localObject).toString());
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 108 */     if ((this.vm.traceFlags & 0x1) != 0) {
/* 109 */       this.vm.printTrace("Target VM interface thread running");
/*     */     }
/* 111 */     Packet localPacket1 = null;
/*     */ 
/* 114 */     while (this.shouldListen)
/*     */     {
/* 116 */       int i = 0;
/*     */       try {
/* 118 */         byte[] arrayOfByte = this.connection.readPacket();
/* 119 */         if (arrayOfByte.length == 0) {
/* 120 */           i = 1;
/*     */         }
/* 122 */         localPacket1 = Packet.fromByteArray(arrayOfByte);
/*     */       } catch (IOException localIOException1) {
/* 124 */         i = 1;
/*     */       }
/*     */ 
/* 127 */       if (i != 0) {
/* 128 */         this.shouldListen = false;
/*     */         try {
/* 130 */           this.connection.close();
/*     */         }
/*     */         catch (IOException localIOException2) {
/*     */         }
/*     */       }
/* 135 */       if ((this.vm.traceFlags & VirtualMachineImpl.TRACE_RAW_RECEIVES) != 0) {
/* 136 */         dumpPacket(localPacket1, false);
/*     */       }
/*     */ 
/* 139 */       if ((localPacket1.flags & 0x80) == 0)
/*     */       {
/* 141 */         handleVMCommand(localPacket1);
/*     */       }
/*     */       else
/*     */       {
/* 147 */         this.vm.state().notifyCommandComplete(localPacket1.id);
/* 148 */         String str = String.valueOf(localPacket1.id);
/*     */         Packet localPacket2;
/* 150 */         synchronized (this.waitingQueue) {
/* 151 */           localPacket2 = (Packet)this.waitingQueue.get(str);
/*     */ 
/* 153 */           if (localPacket2 != null) {
/* 154 */             this.waitingQueue.remove(str);
/*     */           }
/*     */         }
/* 157 */         if (localPacket2 == null)
/*     */         {
/* 161 */           System.err.println("Recieved reply with no sender!");
/*     */         }
/*     */         else {
/* 164 */           localPacket2.errorCode = localPacket1.errorCode;
/* 165 */           localPacket2.data = localPacket1.data;
/* 166 */           localPacket2.replied = true;
/*     */ 
/* 168 */           synchronized (localPacket2) {
/* 169 */             localPacket2.notify();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 175 */     this.vm.vmManager.disposeVirtualMachine(this.vm);
/*     */ 
/* 180 */     synchronized (this.eventQueues) {
/* 181 */       ??? = this.eventQueues.iterator();
/* 182 */       while (((Iterator)???).hasNext()) {
/* 183 */         ((EventQueueImpl)((Iterator)???).next()).close();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 189 */     synchronized (this.waitingQueue) {
/* 190 */       ??? = this.waitingQueue.values().iterator();
/* 191 */       while (((Iterator)???).hasNext()) {
/* 192 */         Packet localPacket3 = (Packet)((Iterator)???).next();
/* 193 */         synchronized (localPacket3) {
/* 194 */           localPacket3.notify();
/*     */         }
/*     */       }
/* 197 */       this.waitingQueue.clear();
/*     */     }
/*     */ 
/* 200 */     if ((this.vm.traceFlags & 0x1) != 0)
/* 201 */       this.vm.printTrace("Target VM interface thread exiting");
/*     */   }
/*     */ 
/*     */   protected void handleVMCommand(Packet paramPacket)
/*     */   {
/* 206 */     switch (paramPacket.cmdSet) {
/*     */     case 64:
/* 208 */       handleEventCmdSet(paramPacket);
/* 209 */       break;
/*     */     default:
/* 212 */       System.err.println("Ignoring cmd " + paramPacket.id + "/" + paramPacket.cmdSet + "/" + paramPacket.cmd + " from the VM");
/*     */ 
/* 214 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleEventCmdSet(Packet paramPacket)
/*     */   {
/* 228 */     EventSetImpl localEventSetImpl = new EventSetImpl(this.vm, paramPacket);
/*     */ 
/* 230 */     if (localEventSetImpl != null)
/* 231 */       queueEventSet(localEventSetImpl);
/*     */   }
/*     */ 
/*     */   private EventController eventController()
/*     */   {
/* 236 */     if (this.eventController == null) {
/* 237 */       this.eventController = new EventController(this.vm);
/*     */     }
/* 239 */     return this.eventController;
/*     */   }
/*     */ 
/*     */   private synchronized void controlEventFlow(int paramInt) {
/* 243 */     if ((!this.eventsHeld) && (paramInt > 2000)) {
/* 244 */       eventController().hold();
/* 245 */       this.eventsHeld = true;
/* 246 */     } else if ((this.eventsHeld) && (paramInt < 100)) {
/* 247 */       eventController().release();
/* 248 */       this.eventsHeld = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   void notifyDequeueEventSet() {
/* 253 */     int i = 0;
/* 254 */     synchronized (this.eventQueues) {
/* 255 */       Iterator localIterator = this.eventQueues.iterator();
/* 256 */       while (localIterator.hasNext()) {
/* 257 */         EventQueueImpl localEventQueueImpl = (EventQueueImpl)localIterator.next();
/* 258 */         i = Math.max(i, localEventQueueImpl.size());
/*     */       }
/*     */     }
/* 261 */     controlEventFlow(i);
/*     */   }
/*     */ 
/*     */   private void queueEventSet(EventSet paramEventSet) {
/* 265 */     int i = 0;
/*     */ 
/* 267 */     synchronized (this.eventQueues) {
/* 268 */       Iterator localIterator = this.eventQueues.iterator();
/* 269 */       while (localIterator.hasNext()) {
/* 270 */         EventQueueImpl localEventQueueImpl = (EventQueueImpl)localIterator.next();
/* 271 */         localEventQueueImpl.enqueue(paramEventSet);
/* 272 */         i = Math.max(i, localEventQueueImpl.size());
/*     */       }
/*     */     }
/*     */ 
/* 276 */     controlEventFlow(i);
/*     */   }
/*     */ 
/*     */   void send(Packet paramPacket) {
/* 280 */     String str = String.valueOf(paramPacket.id);
/*     */ 
/* 282 */     synchronized (this.waitingQueue) {
/* 283 */       this.waitingQueue.put(str, paramPacket);
/*     */     }
/*     */ 
/* 286 */     if ((this.vm.traceFlags & VirtualMachineImpl.TRACE_RAW_SENDS) != 0) {
/* 287 */       dumpPacket(paramPacket, true);
/*     */     }
/*     */     try
/*     */     {
/* 291 */       this.connection.writePacket(paramPacket.toByteArray());
/*     */     } catch (IOException localIOException) {
/* 293 */       throw new VMDisconnectedException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   void waitForReply(Packet paramPacket) {
/* 298 */     synchronized (paramPacket) {
/* 299 */       while ((!paramPacket.replied) && (this.shouldListen)) try {
/* 300 */           paramPacket.wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         } if (!paramPacket.replied)
/* 304 */         throw new VMDisconnectedException();
/*     */     }
/*     */   }
/*     */ 
/*     */   void addEventQueue(EventQueueImpl paramEventQueueImpl)
/*     */   {
/* 310 */     if ((this.vm.traceFlags & 0x4) != 0) {
/* 311 */       this.vm.printTrace("New event queue added");
/*     */     }
/* 313 */     this.eventQueues.add(paramEventQueueImpl);
/*     */   }
/*     */ 
/*     */   void stopListening() {
/* 317 */     if ((this.vm.traceFlags & 0x4) != 0) {
/* 318 */       this.vm.printTrace("Target VM i/f closing event queues");
/*     */     }
/* 320 */     this.shouldListen = false;
/*     */     try {
/* 322 */       this.connection.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class EventController extends Thread
/*     */   {
/*     */     VirtualMachineImpl vm;
/* 328 */     int controlRequest = 0;
/*     */ 
/*     */     EventController(VirtualMachineImpl paramVirtualMachineImpl) {
/* 331 */       super("JDI Event Control Thread");
/* 332 */       this.vm = paramVirtualMachineImpl;
/* 333 */       setDaemon(true);
/* 334 */       setPriority(7);
/* 335 */       super.start();
/*     */     }
/*     */ 
/*     */     synchronized void hold() {
/* 339 */       this.controlRequest += 1;
/* 340 */       notifyAll();
/*     */     }
/*     */ 
/*     */     synchronized void release() {
/* 344 */       this.controlRequest -= 1;
/* 345 */       notifyAll();
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       while (true)
/*     */       {
/*     */         int i;
/* 351 */         synchronized (this) {
/* 352 */           if (this.controlRequest == 0) {
/*     */             try { wait(); } catch (InterruptedException localInterruptedException) {  } continue;
/*     */           }
/* 355 */           i = this.controlRequest;
/* 356 */           this.controlRequest = 0;
/*     */         }
/*     */         try {
/* 359 */           if (i > 0)
/* 360 */             JDWP.VirtualMachine.HoldEvents.process(this.vm);
/*     */           else {
/* 362 */             JDWP.VirtualMachine.ReleaseEvents.process(this.vm);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (JDWPException localJDWPException)
/*     */         {
/* 369 */           localJDWPException.toJDIException().printStackTrace(System.err);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.TargetVM
 * JD-Core Version:    0.6.2
 */