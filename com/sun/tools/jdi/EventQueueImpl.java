/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class EventQueueImpl extends MirrorImpl
/*     */   implements EventQueue
/*     */ {
/*  40 */   LinkedList<EventSet> eventSets = new LinkedList();
/*     */   TargetVM target;
/*  43 */   boolean closed = false;
/*     */ 
/*     */   EventQueueImpl(VirtualMachine paramVirtualMachine, TargetVM paramTargetVM) {
/*  46 */     super(paramVirtualMachine);
/*  47 */     this.target = paramTargetVM;
/*  48 */     paramTargetVM.addEventQueue(this);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  55 */     return this == paramObject;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  59 */     return System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */   synchronized void enqueue(EventSet paramEventSet) {
/*  63 */     this.eventSets.add(paramEventSet);
/*  64 */     notifyAll();
/*     */   }
/*     */ 
/*     */   synchronized int size() {
/*  68 */     return this.eventSets.size();
/*     */   }
/*     */ 
/*     */   synchronized void close() {
/*  72 */     if (!this.closed) {
/*  73 */       this.closed = true;
/*     */ 
/*  76 */       enqueue(new EventSetImpl(this.vm, (byte)100));
/*     */     }
/*     */   }
/*     */ 
/*     */   public EventSet remove() throws InterruptedException
/*     */   {
/*  82 */     return remove(0L);
/*     */   }
/*     */ 
/*     */   public EventSet remove(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/*  90 */     if (paramLong < 0L) {
/*  91 */       throw new IllegalArgumentException("Timeout cannot be negative");
/*     */     }
/*     */     EventSet localEventSet;
/*     */     while (true)
/*     */     {
/*  96 */       EventSetImpl localEventSetImpl = removeUnfiltered(paramLong);
/*  97 */       if (localEventSetImpl == null) {
/*  98 */         localEventSet = null;
/*     */       }
/*     */       else
/*     */       {
/* 107 */         localEventSet = localEventSetImpl.userFilter();
/* 108 */         if (!localEventSet.isEmpty()) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 113 */     if ((localEventSet != null) && (localEventSet.suspendPolicy() == 2)) {
/* 114 */       this.vm.notifySuspend();
/*     */     }
/*     */ 
/* 117 */     return localEventSet;
/*     */   }
/*     */ 
/*     */   EventSet removeInternal() throws InterruptedException
/*     */   {
/*     */     EventSet localEventSet;
/*     */     do
/* 124 */       localEventSet = removeUnfiltered(0L).internalFilter();
/* 125 */     while ((localEventSet == null) || (localEventSet.isEmpty()));
/*     */ 
/* 134 */     return localEventSet;
/*     */   }
/*     */ 
/*     */   private TimerThread startTimerThread(long paramLong) {
/* 138 */     TimerThread localTimerThread = new TimerThread(paramLong);
/* 139 */     localTimerThread.setDaemon(true);
/* 140 */     localTimerThread.start();
/* 141 */     return localTimerThread;
/*     */   }
/*     */ 
/*     */   private boolean shouldWait(TimerThread paramTimerThread)
/*     */   {
/* 146 */     return (!this.closed) && (this.eventSets.isEmpty()) && ((paramTimerThread == null) || 
/* 146 */       (!paramTimerThread
/* 146 */       .timedOut()));
/*     */   }
/*     */ 
/*     */   private EventSetImpl removeUnfiltered(long paramLong) throws InterruptedException
/*     */   {
/* 151 */     EventSetImpl localEventSetImpl = null;
/*     */ 
/* 157 */     this.vm.waitInitCompletion();
/*     */ 
/* 159 */     synchronized (this) {
/* 160 */       if (!this.eventSets.isEmpty())
/*     */       {
/* 165 */         localEventSetImpl = (EventSetImpl)this.eventSets.removeFirst();
/*     */       }
/*     */       else
/*     */       {
/* 182 */         TimerThread localTimerThread = null;
/*     */         try {
/* 184 */           if (paramLong > 0L) {
/* 185 */             localTimerThread = startTimerThread(paramLong);
/*     */           }
/*     */ 
/* 188 */           while (shouldWait(localTimerThread))
/* 189 */             wait();
/*     */         }
/*     */         finally {
/* 192 */           if ((localTimerThread != null) && (!localTimerThread.timedOut())) {
/* 193 */             localTimerThread.interrupt();
/*     */           }
/*     */         }
/*     */ 
/* 197 */         if (this.eventSets.isEmpty()) {
/* 198 */           if (this.closed)
/* 199 */             throw new VMDisconnectedException();
/*     */         }
/*     */         else {
/* 202 */           localEventSetImpl = (EventSetImpl)this.eventSets.removeFirst();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 209 */     if (localEventSetImpl != null) {
/* 210 */       this.target.notifyDequeueEventSet();
/* 211 */       localEventSetImpl.build();
/*     */     }
/* 213 */     return localEventSetImpl;
/*     */   }
/*     */ 
/* 217 */   private class TimerThread extends Thread { private boolean timedOut = false;
/*     */     private long timeout;
/*     */ 
/*     */     TimerThread(long arg2) {
/* 221 */       super("JDI Event Queue Timer");
/*     */       Object localObject;
/* 222 */       this.timeout = localObject;
/*     */     }
/*     */ 
/*     */     boolean timedOut() {
/* 226 */       return this.timedOut;
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       try {
/* 231 */         Thread.sleep(this.timeout);
/* 232 */         EventQueueImpl localEventQueueImpl = EventQueueImpl.this;
/* 233 */         synchronized (localEventQueueImpl) {
/* 234 */           this.timedOut = true;
/* 235 */           localEventQueueImpl.notifyAll();
/*     */         }
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.EventQueueImpl
 * JD-Core Version:    0.6.2
 */