/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotPreparedException;
/*     */ import com.sun.jdi.InconsistentDebugInfoException;
/*     */ import com.sun.jdi.ObjectCollectedException;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VMOutOfMemoryException;
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.event.ClassUnloadEvent;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ 
/*     */ public class InternalEventHandler
/*     */   implements Runnable
/*     */ {
/*     */   EventQueueImpl queue;
/*     */   VirtualMachineImpl vm;
/*     */ 
/*     */   InternalEventHandler(VirtualMachineImpl paramVirtualMachineImpl, EventQueueImpl paramEventQueueImpl)
/*     */   {
/*  39 */     this.vm = paramVirtualMachineImpl;
/*  40 */     this.queue = paramEventQueueImpl;
/*  41 */     Thread localThread = new Thread(paramVirtualMachineImpl.threadGroupForJDI(), this, "JDI Internal Event Handler");
/*     */ 
/*  43 */     localThread.setDaemon(true);
/*  44 */     localThread.start();
/*     */   }
/*     */ 
/*     */   public void run() {
/*  48 */     if ((this.vm.traceFlags & 0x4) != 0)
/*  49 */       this.vm.printTrace("Internal event handler running");
/*     */     try
/*     */     {
/*     */       while (true)
/*     */         try {
/*  54 */           EventSet localEventSet = this.queue.removeInternal();
/*  55 */           EventIterator localEventIterator = localEventSet.eventIterator();
/*  56 */           if (localEventIterator.hasNext()) {
/*  57 */             Event localEvent = localEventIterator.nextEvent();
/*     */             Object localObject;
/*  58 */             if ((localEvent instanceof ClassUnloadEvent)) {
/*  59 */               localObject = (ClassUnloadEvent)localEvent;
/*  60 */               this.vm.removeReferenceType(((ClassUnloadEvent)localObject).classSignature());
/*     */ 
/*  62 */               if ((this.vm.traceFlags & 0x4) != 0)
/*  63 */                 this.vm.printTrace("Handled Unload Event for " + ((ClassUnloadEvent)localObject)
/*  64 */                   .classSignature());
/*     */             }
/*  66 */             else if ((localEvent instanceof ClassPrepareEvent)) {
/*  67 */               localObject = (ClassPrepareEvent)localEvent;
/*  68 */               ((ReferenceTypeImpl)((ClassPrepareEvent)localObject).referenceType())
/*  69 */                 .markPrepared();
/*     */ 
/*  71 */               if ((this.vm.traceFlags & 0x4) != 0) {
/*  72 */                 this.vm.printTrace("Handled Prepare Event for " + ((ClassPrepareEvent)localObject)
/*  73 */                   .referenceType().name());
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */           else;
/*     */         }
/*     */         catch (VMOutOfMemoryException localVMOutOfMemoryException)
/*     */         {
/*  88 */           localVMOutOfMemoryException.printStackTrace();
/*     */         } catch (InconsistentDebugInfoException localInconsistentDebugInfoException) {
/*  90 */           localInconsistentDebugInfoException.printStackTrace();
/*     */         }
/*     */         catch (ObjectCollectedException localObjectCollectedException)
/*     */         {
/* 101 */           localObjectCollectedException.printStackTrace();
/*     */         } catch (ClassNotPreparedException localClassNotPreparedException) {
/* 103 */           localClassNotPreparedException.printStackTrace();
/*     */         }
/*     */     } catch (InterruptedException localInterruptedException) {
/*     */     }
/*     */     catch (VMDisconnectedException localVMDisconnectedException) {
/*     */     }
/* 109 */     if ((this.vm.traceFlags & 0x4) != 0)
/* 110 */       this.vm.printTrace("Internal event handler exiting");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.InternalEventHandler
 * JD-Core Version:    0.6.2
 */