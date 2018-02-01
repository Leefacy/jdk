/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.event.BreakpointEvent;
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.event.ClassUnloadEvent;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import com.sun.jdi.event.ExceptionEvent;
/*     */ import com.sun.jdi.event.LocatableEvent;
/*     */ import com.sun.jdi.event.MethodEntryEvent;
/*     */ import com.sun.jdi.event.MethodExitEvent;
/*     */ import com.sun.jdi.event.StepEvent;
/*     */ import com.sun.jdi.event.ThreadDeathEvent;
/*     */ import com.sun.jdi.event.ThreadStartEvent;
/*     */ import com.sun.jdi.event.VMDeathEvent;
/*     */ import com.sun.jdi.event.VMDisconnectEvent;
/*     */ import com.sun.jdi.event.VMStartEvent;
/*     */ import com.sun.jdi.event.WatchpointEvent;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class EventHandler
/*     */   implements Runnable
/*     */ {
/*     */   EventNotifier notifier;
/*     */   Thread thread;
/*  45 */   volatile boolean connected = true;
/*  46 */   boolean completed = false;
/*     */   String shutdownMessageKey;
/*     */   boolean stopOnVMStart;
/* 126 */   private boolean vmDied = false;
/*     */ 
/*     */   EventHandler(EventNotifier paramEventNotifier, boolean paramBoolean)
/*     */   {
/*  51 */     this.notifier = paramEventNotifier;
/*  52 */     this.stopOnVMStart = paramBoolean;
/*  53 */     this.thread = new Thread(this, "event-handler");
/*  54 */     this.thread.start();
/*     */   }
/*     */ 
/*     */   synchronized void shutdown() {
/*  58 */     this.connected = false;
/*  59 */     this.thread.interrupt();
/*  60 */     while (!this.completed) try {
/*  61 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       } 
/*     */   }
/*     */ 
/*  67 */   public void run() { EventQueue localEventQueue = Env.vm().eventQueue();
/*     */     while (true) if (this.connected) {
/*     */         try {
/*  70 */           EventSet localEventSet = localEventQueue.remove();
/*  71 */           int i = 0;
/*  72 */           EventIterator localEventIterator = localEventSet.eventIterator();
/*  73 */           while (localEventIterator.hasNext()) {
/*  74 */             i |= (!handleEvent(localEventIterator.nextEvent()) ? 1 : 0);
/*     */           }
/*     */ 
/*  77 */           if (i != 0) {
/*  78 */             localEventSet.resume();
/*  79 */           } else if (localEventSet.suspendPolicy() == 2) {
/*  80 */             setCurrentThread(localEventSet);
/*  81 */             this.notifier.vmInterrupted();
/*     */           }
/*     */         } catch (InterruptedException localInterruptedException) {
/*     */         }
/*     */         catch (VMDisconnectedException localVMDisconnectedException) {
/*  86 */           handleDisconnectedException();
/*     */         }
/*     */       }
/*     */ 
/*  90 */     synchronized (this) {
/*  91 */       this.completed = true;
/*  92 */       notifyAll();
/*     */     } }
/*     */ 
/*     */   private boolean handleEvent(Event paramEvent)
/*     */   {
/*  97 */     this.notifier.receivedEvent(paramEvent);
/*     */ 
/*  99 */     if ((paramEvent instanceof ExceptionEvent))
/* 100 */       return exceptionEvent(paramEvent);
/* 101 */     if ((paramEvent instanceof BreakpointEvent))
/* 102 */       return breakpointEvent(paramEvent);
/* 103 */     if ((paramEvent instanceof WatchpointEvent))
/* 104 */       return fieldWatchEvent(paramEvent);
/* 105 */     if ((paramEvent instanceof StepEvent))
/* 106 */       return stepEvent(paramEvent);
/* 107 */     if ((paramEvent instanceof MethodEntryEvent))
/* 108 */       return methodEntryEvent(paramEvent);
/* 109 */     if ((paramEvent instanceof MethodExitEvent))
/* 110 */       return methodExitEvent(paramEvent);
/* 111 */     if ((paramEvent instanceof ClassPrepareEvent))
/* 112 */       return classPrepareEvent(paramEvent);
/* 113 */     if ((paramEvent instanceof ClassUnloadEvent))
/* 114 */       return classUnloadEvent(paramEvent);
/* 115 */     if ((paramEvent instanceof ThreadStartEvent))
/* 116 */       return threadStartEvent(paramEvent);
/* 117 */     if ((paramEvent instanceof ThreadDeathEvent))
/* 118 */       return threadDeathEvent(paramEvent);
/* 119 */     if ((paramEvent instanceof VMStartEvent)) {
/* 120 */       return vmStartEvent(paramEvent);
/*     */     }
/* 122 */     return handleExitEvent(paramEvent);
/*     */   }
/*     */ 
/*     */   private boolean handleExitEvent(Event paramEvent)
/*     */   {
/* 128 */     if ((paramEvent instanceof VMDeathEvent)) {
/* 129 */       this.vmDied = true;
/* 130 */       return vmDeathEvent(paramEvent);
/* 131 */     }if ((paramEvent instanceof VMDisconnectEvent)) {
/* 132 */       this.connected = false;
/* 133 */       if (!this.vmDied) {
/* 134 */         vmDisconnectEvent(paramEvent);
/*     */       }
/* 136 */       Env.shutdown(this.shutdownMessageKey);
/* 137 */       return false;
/*     */     }
/* 139 */     throw new InternalError(MessageOutput.format("Unexpected event type", new Object[] { paramEvent
/* 140 */       .getClass() }));
/*     */   }
/*     */ 
/*     */   synchronized void handleDisconnectedException()
/*     */   {
/* 151 */     EventQueue localEventQueue = Env.vm().eventQueue();
/* 152 */     while (this.connected)
/*     */       try {
/* 154 */         EventSet localEventSet = localEventQueue.remove();
/* 155 */         EventIterator localEventIterator = localEventSet.eventIterator();
/* 156 */         while (localEventIterator.hasNext())
/* 157 */           handleExitEvent((Event)localEventIterator.next());
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */       catch (InternalError localInternalError)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   private ThreadReference eventThread(Event paramEvent) {
/* 168 */     if ((paramEvent instanceof ClassPrepareEvent))
/* 169 */       return ((ClassPrepareEvent)paramEvent).thread();
/* 170 */     if ((paramEvent instanceof LocatableEvent))
/* 171 */       return ((LocatableEvent)paramEvent).thread();
/* 172 */     if ((paramEvent instanceof ThreadStartEvent))
/* 173 */       return ((ThreadStartEvent)paramEvent).thread();
/* 174 */     if ((paramEvent instanceof ThreadDeathEvent))
/* 175 */       return ((ThreadDeathEvent)paramEvent).thread();
/* 176 */     if ((paramEvent instanceof VMStartEvent)) {
/* 177 */       return ((VMStartEvent)paramEvent).thread();
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   private void setCurrentThread(EventSet paramEventSet)
/*     */   {
/*     */     ThreadReference localThreadReference;
/* 185 */     if (paramEventSet.size() > 0)
/*     */     {
/* 190 */       Event localEvent = (Event)paramEventSet.iterator().next();
/* 191 */       localThreadReference = eventThread(localEvent);
/*     */     } else {
/* 193 */       localThreadReference = null;
/*     */     }
/* 195 */     setCurrentThread(localThreadReference);
/*     */   }
/*     */ 
/*     */   private void setCurrentThread(ThreadReference paramThreadReference) {
/* 199 */     ThreadInfo.invalidateAll();
/* 200 */     ThreadInfo.setCurrentThread(paramThreadReference);
/*     */   }
/*     */ 
/*     */   private boolean vmStartEvent(Event paramEvent) {
/* 204 */     VMStartEvent localVMStartEvent = (VMStartEvent)paramEvent;
/* 205 */     this.notifier.vmStartEvent(localVMStartEvent);
/* 206 */     return this.stopOnVMStart;
/*     */   }
/*     */ 
/*     */   private boolean breakpointEvent(Event paramEvent) {
/* 210 */     BreakpointEvent localBreakpointEvent = (BreakpointEvent)paramEvent;
/* 211 */     this.notifier.breakpointEvent(localBreakpointEvent);
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean methodEntryEvent(Event paramEvent) {
/* 216 */     MethodEntryEvent localMethodEntryEvent = (MethodEntryEvent)paramEvent;
/* 217 */     this.notifier.methodEntryEvent(localMethodEntryEvent);
/* 218 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean methodExitEvent(Event paramEvent) {
/* 222 */     MethodExitEvent localMethodExitEvent = (MethodExitEvent)paramEvent;
/* 223 */     return this.notifier.methodExitEvent(localMethodExitEvent);
/*     */   }
/*     */ 
/*     */   private boolean fieldWatchEvent(Event paramEvent) {
/* 227 */     WatchpointEvent localWatchpointEvent = (WatchpointEvent)paramEvent;
/* 228 */     this.notifier.fieldWatchEvent(localWatchpointEvent);
/* 229 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean stepEvent(Event paramEvent) {
/* 233 */     StepEvent localStepEvent = (StepEvent)paramEvent;
/* 234 */     this.notifier.stepEvent(localStepEvent);
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean classPrepareEvent(Event paramEvent) {
/* 239 */     ClassPrepareEvent localClassPrepareEvent = (ClassPrepareEvent)paramEvent;
/* 240 */     this.notifier.classPrepareEvent(localClassPrepareEvent);
/*     */ 
/* 242 */     if (!Env.specList.resolve(localClassPrepareEvent)) {
/* 243 */       MessageOutput.lnprint("Stopping due to deferred breakpoint errors.");
/* 244 */       return true;
/*     */     }
/* 246 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean classUnloadEvent(Event paramEvent)
/*     */   {
/* 251 */     ClassUnloadEvent localClassUnloadEvent = (ClassUnloadEvent)paramEvent;
/* 252 */     this.notifier.classUnloadEvent(localClassUnloadEvent);
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean exceptionEvent(Event paramEvent) {
/* 257 */     ExceptionEvent localExceptionEvent = (ExceptionEvent)paramEvent;
/* 258 */     this.notifier.exceptionEvent(localExceptionEvent);
/* 259 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean threadDeathEvent(Event paramEvent) {
/* 263 */     ThreadDeathEvent localThreadDeathEvent = (ThreadDeathEvent)paramEvent;
/* 264 */     ThreadInfo.removeThread(localThreadDeathEvent.thread());
/* 265 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean threadStartEvent(Event paramEvent) {
/* 269 */     ThreadStartEvent localThreadStartEvent = (ThreadStartEvent)paramEvent;
/* 270 */     ThreadInfo.addThread(localThreadStartEvent.thread());
/* 271 */     this.notifier.threadStartEvent(localThreadStartEvent);
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean vmDeathEvent(Event paramEvent) {
/* 276 */     this.shutdownMessageKey = "The application exited";
/* 277 */     this.notifier.vmDeathEvent((VMDeathEvent)paramEvent);
/* 278 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean vmDisconnectEvent(Event paramEvent) {
/* 282 */     this.shutdownMessageKey = "The application has been disconnected";
/* 283 */     this.notifier.vmDisconnectEvent((VMDisconnectEvent)paramEvent);
/* 284 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.EventHandler
 * JD-Core Version:    0.6.2
 */