/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ class VMState
/*     */ {
/*     */   private final VirtualMachineImpl vm;
/*  37 */   private final List<WeakReference<VMListener>> listeners = new ArrayList();
/*  38 */   private boolean notifyingListeners = false;
/*     */ 
/*  46 */   private int lastCompletedCommandId = 0;
/*  47 */   private int lastResumeCommandId = 0;
/*     */ 
/*  55 */   private Cache cache = null;
/*  56 */   private static final Cache markerCache = new Cache(null);
/*     */ 
/*     */   private void disableCache() {
/*  59 */     synchronized (this) {
/*  60 */       this.cache = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void enableCache() {
/*  65 */     synchronized (this) {
/*  66 */       this.cache = markerCache;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Cache getCache() {
/*  71 */     synchronized (this) {
/*  72 */       if (this.cache == markerCache) {
/*  73 */         this.cache = new Cache(null);
/*     */       }
/*  75 */       return this.cache;
/*     */     }
/*     */   }
/*     */ 
/*     */   VMState(VirtualMachineImpl paramVirtualMachineImpl) {
/*  80 */     this.vm = paramVirtualMachineImpl;
/*     */   }
/*     */ 
/*     */   boolean isSuspended()
/*     */   {
/*  88 */     return this.cache != null;
/*     */   }
/*     */ 
/*     */   synchronized void notifyCommandComplete(int paramInt)
/*     */   {
/*  96 */     this.lastCompletedCommandId = paramInt;
/*     */   }
/*     */ 
/*     */   synchronized void freeze() {
/* 100 */     if ((this.cache == null) && (this.lastCompletedCommandId >= this.lastResumeCommandId))
/*     */     {
/* 106 */       processVMAction(new VMAction(this.vm, 1));
/* 107 */       enableCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized PacketStream thawCommand(CommandSender paramCommandSender) {
/* 112 */     PacketStream localPacketStream = paramCommandSender.send();
/* 113 */     this.lastResumeCommandId = localPacketStream.id();
/* 114 */     thaw();
/* 115 */     return localPacketStream;
/*     */   }
/*     */ 
/*     */   void thaw()
/*     */   {
/* 122 */     thaw(null);
/*     */   }
/*     */ 
/*     */   synchronized void thaw(ThreadReference paramThreadReference)
/*     */   {
/* 131 */     if (this.cache != null) {
/* 132 */       if ((this.vm.traceFlags & 0x10) != 0) {
/* 133 */         this.vm.printTrace("Clearing VM suspended cache");
/*     */       }
/* 135 */       disableCache();
/*     */     }
/* 137 */     processVMAction(new VMAction(this.vm, paramThreadReference, 2));
/*     */   }
/*     */ 
/*     */   private synchronized void processVMAction(VMAction paramVMAction) {
/* 141 */     if (!this.notifyingListeners)
/*     */     {
/* 143 */       this.notifyingListeners = true;
/*     */ 
/* 145 */       Iterator localIterator = this.listeners.iterator();
/* 146 */       while (localIterator.hasNext()) {
/* 147 */         WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 148 */         VMListener localVMListener = (VMListener)localWeakReference.get();
/* 149 */         if (localVMListener != null) {
/* 150 */           boolean bool = true;
/* 151 */           switch (paramVMAction.id()) {
/*     */           case 1:
/* 153 */             bool = localVMListener.vmSuspended(paramVMAction);
/* 154 */             break;
/*     */           case 2:
/* 156 */             bool = localVMListener.vmNotSuspended(paramVMAction);
/*     */           }
/*     */ 
/* 159 */           if (!bool)
/* 160 */             localIterator.remove();
/*     */         }
/*     */         else
/*     */         {
/* 164 */           localIterator.remove();
/*     */         }
/*     */       }
/*     */ 
/* 168 */       this.notifyingListeners = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void addListener(VMListener paramVMListener) {
/* 173 */     this.listeners.add(new WeakReference(paramVMListener));
/*     */   }
/*     */ 
/*     */   synchronized boolean hasListener(VMListener paramVMListener) {
/* 177 */     return this.listeners.contains(paramVMListener);
/*     */   }
/*     */ 
/*     */   synchronized void removeListener(VMListener paramVMListener) {
/* 181 */     Iterator localIterator = this.listeners.iterator();
/* 182 */     while (localIterator.hasNext()) {
/* 183 */       WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 184 */       if (paramVMListener.equals(localWeakReference.get())) {
/* 185 */         localIterator.remove();
/* 186 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   List<ThreadReference> allThreads() {
/* 192 */     List localList = null;
/*     */     try {
/* 194 */       Cache localCache = getCache();
/*     */ 
/* 196 */       if (localCache != null)
/*     */       {
/* 198 */         localList = localCache.threads;
/*     */       }
/* 200 */       if (localList == null) {
/* 201 */         localList = Arrays.asList(
/* 202 */           (ThreadReference[])JDWP.VirtualMachine.AllThreads.process(this.vm).threads);
/*     */ 
/* 203 */         if (localCache != null) {
/* 204 */           localCache.threads = localList;
/* 205 */           if ((this.vm.traceFlags & 0x10) != 0)
/* 206 */             this.vm.printTrace("Caching all threads (count = " + localList
/* 207 */               .size() + ") while VM suspended");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 212 */       throw localJDWPException.toJDIException();
/*     */     }
/* 214 */     return localList;
/*     */   }
/*     */ 
/*     */   List<ThreadGroupReference> topLevelThreadGroups()
/*     */   {
/* 219 */     List localList = null;
/*     */     try {
/* 221 */       Cache localCache = getCache();
/*     */ 
/* 223 */       if (localCache != null) {
/* 224 */         localList = localCache.groups;
/*     */       }
/* 226 */       if (localList == null) {
/* 227 */         localList = Arrays.asList(
/* 229 */           (ThreadGroupReference[])JDWP.VirtualMachine.TopLevelThreadGroups.process(this.vm).groups);
/*     */ 
/* 230 */         if (localCache != null) {
/* 231 */           localCache.groups = localList;
/* 232 */           if ((this.vm.traceFlags & 0x10) != 0)
/* 233 */             this.vm.printTrace("Caching top level thread groups (count = " + localList
/* 235 */               .size() + ") while VM suspended");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 240 */       throw localJDWPException.toJDIException();
/*     */     }
/* 242 */     return localList;
/*     */   }
/*     */ 
/*     */   private static class Cache
/*     */   {
/*  51 */     List<ThreadGroupReference> groups = null;
/*  52 */     List<ThreadReference> threads = null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VMState
 * JD-Core Version:    0.6.2
 */