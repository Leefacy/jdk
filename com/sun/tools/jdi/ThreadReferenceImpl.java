/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidStackFrameException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.MonitorInfo;
/*     */ import com.sun.jdi.NativeMethodException;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.BreakpointRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ThreadReferenceImpl extends ObjectReferenceImpl
/*     */   implements ThreadReference, VMListener
/*     */ {
/*     */   static final int SUSPEND_STATUS_SUSPENDED = 1;
/*     */   static final int SUSPEND_STATUS_BREAK = 2;
/*  38 */   private int suspendedZombieCount = 0;
/*     */   private ThreadGroupReference threadGroup;
/*     */   private LocalCache localCache;
/* 116 */   private List<WeakReference<ThreadListener>> listeners = new ArrayList();
/*     */ 
/*     */   private void resetLocalCache()
/*     */   {
/* 103 */     this.localCache = new LocalCache(null);
/*     */   }
/*     */ 
/*     */   protected ObjectReferenceImpl.Cache newCache()
/*     */   {
/* 112 */     return new Cache(null);
/*     */   }
/*     */ 
/*     */   ThreadReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong)
/*     */   {
/* 120 */     super(paramVirtualMachine, paramLong);
/* 121 */     resetLocalCache();
/* 122 */     this.vm.state().addListener(this);
/*     */   }
/*     */ 
/*     */   protected String description() {
/* 126 */     return "ThreadReference " + uniqueID();
/*     */   }
/*     */ 
/*     */   public boolean vmNotSuspended(VMAction paramVMAction)
/*     */   {
/* 133 */     if (paramVMAction.resumingThread() == null)
/*     */     {
/* 135 */       synchronized (this.vm.state()) {
/* 136 */         processThreadAction(new ThreadAction(this, 2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 151 */     return super.vmNotSuspended(paramVMAction);
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 160 */     String str = null;
/*     */     try {
/* 162 */       Cache localCache1 = (Cache)getCache();
/*     */ 
/* 164 */       if (localCache1 != null) {
/* 165 */         str = localCache1.name;
/*     */       }
/* 167 */       if (str == null) {
/* 168 */         str = JDWP.ThreadReference.Name.process(this.vm, this).threadName;
/*     */ 
/* 170 */         if (localCache1 != null)
/* 171 */           localCache1.name = str;
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 175 */       throw localJDWPException.toJDIException();
/*     */     }
/* 177 */     return str;
/*     */   }
/*     */ 
/*     */   PacketStream sendResumingCommand(CommandSender paramCommandSender)
/*     */   {
/* 185 */     synchronized (this.vm.state()) {
/* 186 */       processThreadAction(new ThreadAction(this, 2));
/*     */ 
/* 188 */       return paramCommandSender.send();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void suspend() {
/*     */     try {
/* 194 */       JDWP.ThreadReference.Suspend.process(this.vm, this);
/*     */     } catch (JDWPException localJDWPException) {
/* 196 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resume()
/*     */   {
/* 207 */     if (this.suspendedZombieCount > 0) {
/* 208 */       this.suspendedZombieCount -= 1;
/*     */       return;
/*     */     }
/*     */     PacketStream localPacketStream;
/* 213 */     synchronized (this.vm.state()) {
/* 214 */       processThreadAction(new ThreadAction(this, 2));
/*     */ 
/* 216 */       localPacketStream = JDWP.ThreadReference.Resume.enqueueCommand(this.vm, this);
/*     */     }
/*     */     try {
/* 219 */       JDWP.ThreadReference.Resume.waitForReply(this.vm, localPacketStream);
/*     */     } catch (JDWPException localJDWPException) {
/* 221 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int suspendCount()
/*     */   {
/* 229 */     if (this.suspendedZombieCount > 0) {
/* 230 */       return this.suspendedZombieCount;
/*     */     }
/*     */     try
/*     */     {
/* 234 */       return JDWP.ThreadReference.SuspendCount.process(this.vm, this).suspendCount;
/*     */     } catch (JDWPException localJDWPException) {
/* 236 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stop(ObjectReference paramObjectReference) throws InvalidTypeException {
/* 241 */     validateMirror(paramObjectReference);
/*     */ 
/* 243 */     List localList = this.vm.classesByName("java.lang.Throwable");
/* 244 */     ClassTypeImpl localClassTypeImpl = (ClassTypeImpl)localList.get(0);
/* 245 */     if ((paramObjectReference == null) || 
/* 246 */       (!localClassTypeImpl
/* 246 */       .isAssignableFrom(paramObjectReference)))
/*     */     {
/* 247 */       throw new InvalidTypeException("Not an instance of Throwable");
/*     */     }
/*     */     try
/*     */     {
/* 251 */       JDWP.ThreadReference.Stop.process(this.vm, this, (ObjectReferenceImpl)paramObjectReference);
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 254 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void interrupt() {
/*     */     try {
/* 260 */       JDWP.ThreadReference.Interrupt.process(this.vm, this);
/*     */     } catch (JDWPException localJDWPException) {
/* 262 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private JDWP.ThreadReference.Status jdwpStatus() {
/* 267 */     LocalCache localLocalCache = this.localCache;
/* 268 */     JDWP.ThreadReference.Status localStatus = localLocalCache.status;
/*     */     try {
/* 270 */       if (localStatus == null) {
/* 271 */         localStatus = JDWP.ThreadReference.Status.process(this.vm, this);
/* 272 */         if ((localStatus.suspendStatus & 0x1) != 0)
/*     */         {
/* 274 */           localLocalCache.status = localStatus;
/*     */         }
/*     */       }
/*     */     } catch (JDWPException localJDWPException) {
/* 278 */       throw localJDWPException.toJDIException();
/*     */     }
/* 280 */     return localStatus;
/*     */   }
/*     */ 
/*     */   public int status() {
/* 284 */     return jdwpStatus().threadStatus;
/*     */   }
/*     */ 
/*     */   public boolean isSuspended()
/*     */   {
/* 289 */     return (this.suspendedZombieCount > 0) || 
/* 289 */       ((jdwpStatus().suspendStatus & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isAtBreakpoint()
/*     */   {
/*     */     try
/*     */     {
/* 297 */       StackFrame localStackFrame = frame(0);
/* 298 */       Location localLocation = localStackFrame.location();
/* 299 */       List localList = this.vm.eventRequestManager().breakpointRequests();
/* 300 */       Iterator localIterator = localList.iterator();
/* 301 */       while (localIterator.hasNext()) {
/* 302 */         BreakpointRequest localBreakpointRequest = (BreakpointRequest)localIterator.next();
/* 303 */         if (localLocation.equals(localBreakpointRequest.location())) {
/* 304 */           return true;
/*     */         }
/*     */       }
/* 307 */       return false;
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 309 */       return false;
/*     */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*     */     }
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */   public ThreadGroupReference threadGroup()
/*     */   {
/* 320 */     if (this.threadGroup == null) {
/*     */       try {
/* 322 */         this.threadGroup = 
/* 323 */           JDWP.ThreadReference.ThreadGroup.process(this.vm, this).group;
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 325 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */     }
/* 328 */     return this.threadGroup;
/*     */   }
/*     */ 
/*     */   public int frameCount() throws IncompatibleThreadStateException {
/* 332 */     LocalCache localLocalCache = this.localCache;
/*     */     try {
/* 334 */       if (localLocalCache.frameCount == -1)
/* 335 */         localLocalCache.frameCount = 
/* 336 */           JDWP.ThreadReference.FrameCount.process(this.vm, this).frameCount;
/*     */     }
/*     */     catch (JDWPException localJDWPException)
/*     */     {
/* 339 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/* 342 */         throw new IncompatibleThreadStateException();
/*     */       }
/*     */     }
/* 344 */     throw localJDWPException.toJDIException();
/*     */ 
/* 347 */     return localLocalCache.frameCount;
/*     */   }
/*     */ 
/*     */   public List<StackFrame> frames() throws IncompatibleThreadStateException {
/* 351 */     return privateFrames(0, -1);
/*     */   }
/*     */ 
/*     */   public StackFrame frame(int paramInt) throws IncompatibleThreadStateException {
/* 355 */     List localList = privateFrames(paramInt, 1);
/* 356 */     return (StackFrame)localList.get(0);
/*     */   }
/*     */ 
/*     */   private boolean isSubrange(LocalCache paramLocalCache, int paramInt1, int paramInt2)
/*     */   {
/* 366 */     if (paramInt1 < paramLocalCache.framesStart) {
/* 367 */       return false;
/*     */     }
/* 369 */     if (paramInt2 == -1) {
/* 370 */       return paramLocalCache.framesLength == -1;
/*     */     }
/* 372 */     if (paramLocalCache.framesLength == -1)
/*     */     {
/* 374 */       if (paramInt1 + paramInt2 > paramLocalCache.framesStart + paramLocalCache.frames
/* 374 */         .size()) {
/* 375 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 377 */       return true;
/*     */     }
/* 379 */     return paramInt1 + paramInt2 <= paramLocalCache.framesStart + paramLocalCache.framesLength;
/*     */   }
/*     */ 
/*     */   public List<StackFrame> frames(int paramInt1, int paramInt2) throws IncompatibleThreadStateException
/*     */   {
/* 384 */     if (paramInt2 < 0) {
/* 385 */       throw new IndexOutOfBoundsException("length must be greater than or equal to zero");
/*     */     }
/*     */ 
/* 388 */     return privateFrames(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private synchronized List<StackFrame> privateFrames(int paramInt1, int paramInt2)
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 400 */     LocalCache localLocalCache = this.localCache;
/*     */     try
/*     */     {
/*     */       int j;
/* 402 */       if ((localLocalCache.frames == null) || (!isSubrange(localLocalCache, paramInt1, paramInt2)))
/*     */       {
/* 405 */         JDWP.ThreadReference.Frames.Frame[] arrayOfFrame = JDWP.ThreadReference.Frames.process(this.vm, this, paramInt1, paramInt2).frames;
/*     */ 
/* 406 */         j = arrayOfFrame.length;
/* 407 */         localLocalCache.frames = new ArrayList(j);
/*     */ 
/* 409 */         for (int k = 0; k < j; k++) {
/* 410 */           if (arrayOfFrame[k].location == null) {
/* 411 */             throw new InternalException("Invalid frame location");
/*     */           }
/* 413 */           StackFrameImpl localStackFrameImpl = new StackFrameImpl(this.vm, this, arrayOfFrame[k].frameID, arrayOfFrame[k].location);
/*     */ 
/* 417 */           localLocalCache.frames.add(localStackFrameImpl);
/*     */         }
/* 419 */         localLocalCache.framesStart = paramInt1;
/* 420 */         localLocalCache.framesLength = paramInt2;
/* 421 */         return Collections.unmodifiableList(localLocalCache.frames);
/*     */       }
/* 423 */       int i = paramInt1 - localLocalCache.framesStart;
/*     */ 
/* 425 */       if (paramInt2 == -1)
/* 426 */         j = localLocalCache.frames.size() - i;
/*     */       else {
/* 428 */         j = i + paramInt2;
/*     */       }
/* 430 */       return Collections.unmodifiableList(localLocalCache.frames.subList(i, j));
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 433 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/* 436 */         throw new IncompatibleThreadStateException();
/*     */       }
/*     */     }
/* 438 */     throw localJDWPException.toJDIException();
/*     */   }
/*     */ 
/*     */   public List<ObjectReference> ownedMonitors()
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 444 */     LocalCache localLocalCache = this.localCache;
/*     */     try {
/* 446 */       if (localLocalCache.ownedMonitors == null) {
/* 447 */         localLocalCache.ownedMonitors = Arrays.asList(
/* 449 */           (ObjectReference[])JDWP.ThreadReference.OwnedMonitors.process(this.vm, this).owned);
/*     */ 
/* 450 */         if ((this.vm.traceFlags & 0x10) != 0)
/* 451 */           this.vm.printTrace(description() + " temporarily caching owned monitors" + " (count = " + localLocalCache.ownedMonitors
/* 453 */             .size() + ")");
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 457 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/* 460 */         throw new IncompatibleThreadStateException();
/*     */       }
/*     */     }
/* 462 */     throw localJDWPException.toJDIException();
/*     */ 
/* 465 */     return localLocalCache.ownedMonitors;
/*     */   }
/*     */ 
/*     */   public ObjectReference currentContendedMonitor() throws IncompatibleThreadStateException
/*     */   {
/* 470 */     LocalCache localLocalCache = this.localCache;
/*     */     try {
/* 472 */       if ((localLocalCache.contendedMonitor == null) && (!localLocalCache.triedCurrentContended))
/*     */       {
/* 474 */         localLocalCache.contendedMonitor = 
/* 475 */           JDWP.ThreadReference.CurrentContendedMonitor.process(this.vm, this).monitor;
/*     */ 
/* 476 */         localLocalCache.triedCurrentContended = true;
/* 477 */         if ((localLocalCache.contendedMonitor != null) && ((this.vm.traceFlags & 0x10) != 0))
/*     */         {
/* 479 */           this.vm.printTrace(description() + " temporarily caching contended monitor" + " (id = " + localLocalCache.contendedMonitor
/* 481 */             .uniqueID() + ")");
/*     */         }
/*     */       }
/*     */     } catch (JDWPException localJDWPException) {
/* 485 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/* 488 */         throw new IncompatibleThreadStateException();
/*     */       }
/*     */     }
/* 490 */     throw localJDWPException.toJDIException();
/*     */ 
/* 493 */     return localLocalCache.contendedMonitor;
/*     */   }
/*     */ 
/*     */   public List<MonitorInfo> ownedMonitorsAndFrames() throws IncompatibleThreadStateException {
/* 497 */     LocalCache localLocalCache = this.localCache;
/*     */     try {
/* 499 */       if (localLocalCache.ownedMonitorsInfo == null)
/*     */       {
/* 501 */         JDWP.ThreadReference.OwnedMonitorsStackDepthInfo.monitor[] arrayOfmonitor = JDWP.ThreadReference.OwnedMonitorsStackDepthInfo.process(this.vm, this).owned;
/*     */ 
/* 503 */         localLocalCache.ownedMonitorsInfo = new ArrayList(arrayOfmonitor.length);
/*     */ 
/* 505 */         for (int i = 0; i < arrayOfmonitor.length; i++) {
/* 506 */           JDWP.ThreadReference.OwnedMonitorsStackDepthInfo.monitor localmonitor = arrayOfmonitor[i];
/*     */ 
/* 508 */           MonitorInfoImpl localMonitorInfoImpl = new MonitorInfoImpl(this.vm, arrayOfmonitor[i].monitor, this, arrayOfmonitor[i].stack_depth);
/* 509 */           localLocalCache.ownedMonitorsInfo.add(localMonitorInfoImpl);
/*     */         }
/*     */ 
/* 512 */         if ((this.vm.traceFlags & 0x10) != 0)
/* 513 */           this.vm.printTrace(description() + " temporarily caching owned monitors" + " (count = " + localLocalCache.ownedMonitorsInfo
/* 515 */             .size() + ")");
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException)
/*     */     {
/* 520 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/* 523 */         throw new IncompatibleThreadStateException();
/*     */       }
/*     */     }
/* 525 */     throw localJDWPException.toJDIException();
/*     */ 
/* 528 */     return localLocalCache.ownedMonitorsInfo;
/*     */   }
/*     */ 
/*     */   public void popFrames(StackFrame paramStackFrame)
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 535 */     if (!paramStackFrame.thread().equals(this)) {
/* 536 */       throw new IllegalArgumentException("frame does not belong to this thread");
/*     */     }
/* 538 */     if (!this.vm.canPopFrames()) {
/* 539 */       throw new UnsupportedOperationException("target does not support popping frames");
/*     */     }
/*     */ 
/* 542 */     ((StackFrameImpl)paramStackFrame).pop();
/*     */   }
/*     */ 
/*     */   public void forceEarlyReturn(Value paramValue)
/*     */     throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException
/*     */   {
/* 548 */     if (!this.vm.canForceEarlyReturn()) {
/* 549 */       throw new UnsupportedOperationException("target does not support the forcing of a method to return early");
/*     */     }
/*     */ 
/* 553 */     validateMirrorOrNull(paramValue);
/*     */     StackFrameImpl localStackFrameImpl;
/*     */     try
/*     */     {
/* 557 */       localStackFrameImpl = (StackFrameImpl)frame(0);
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 559 */       throw new InvalidStackFrameException("No more frames on the stack");
/*     */     }
/* 561 */     localStackFrameImpl.validateStackFrame();
/* 562 */     MethodImpl localMethodImpl = (MethodImpl)localStackFrameImpl.location().method();
/* 563 */     ValueImpl localValueImpl = ValueImpl.prepareForAssignment(paramValue, localMethodImpl
/* 564 */       .getReturnValueContainer());
/*     */     try
/*     */     {
/* 567 */       JDWP.ThreadReference.ForceEarlyReturn.process(this.vm, this, localValueImpl);
/*     */     } catch (JDWPException localJDWPException) {
/* 569 */       switch (localJDWPException.errorCode()) {
/*     */       case 32:
/* 571 */         throw new NativeMethodException();
/*     */       case 13:
/*     */       case 15:
/* 573 */       case 31: }  } throw new IncompatibleThreadStateException("Thread not suspended");
/*     */ 
/* 576 */     throw new IncompatibleThreadStateException("Thread has not started or has finished");
/*     */ 
/* 579 */     throw new InvalidStackFrameException("No more frames on the stack");
/*     */ 
/* 582 */     throw localJDWPException.toJDIException();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 589 */     return "instance of " + referenceType().name() + "(name='" + 
/* 589 */       name() + "', " + "id=" + uniqueID() + ")";
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 593 */     return 116;
/*     */   }
/*     */ 
/*     */   void addListener(ThreadListener paramThreadListener) {
/* 597 */     synchronized (this.vm.state()) {
/* 598 */       this.listeners.add(new WeakReference(paramThreadListener));
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeListener(ThreadListener paramThreadListener) {
/* 603 */     synchronized (this.vm.state()) {
/* 604 */       Iterator localIterator = this.listeners.iterator();
/* 605 */       while (localIterator.hasNext()) {
/* 606 */         WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 607 */         if (paramThreadListener.equals(localWeakReference.get())) {
/* 608 */           localIterator.remove();
/* 609 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processThreadAction(ThreadAction paramThreadAction)
/*     */   {
/* 621 */     synchronized (this.vm.state()) {
/* 622 */       Iterator localIterator = this.listeners.iterator();
/* 623 */       while (localIterator.hasNext()) {
/* 624 */         WeakReference localWeakReference = (WeakReference)localIterator.next();
/* 625 */         ThreadListener localThreadListener = (ThreadListener)localWeakReference.get();
/* 626 */         if (localThreadListener != null) {
/* 627 */           switch (paramThreadAction.id()) {
/*     */           case 2:
/* 629 */             if (!localThreadListener.threadResumable(paramThreadAction)) {
/* 630 */               localIterator.remove();
/*     */             }
/*     */             break;
/*     */           }
/*     */         }
/*     */         else {
/* 636 */           localIterator.remove();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 641 */       resetLocalCache();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Cache extends ObjectReferenceImpl.Cache
/*     */   {
/* 109 */     String name = null;
/*     */   }
/*     */ 
/*     */   private static class LocalCache
/*     */   {
/*  67 */     JDWP.ThreadReference.Status status = null;
/*  68 */     List<StackFrame> frames = null;
/*  69 */     int framesStart = -1;
/*  70 */     int framesLength = 0;
/*  71 */     int frameCount = -1;
/*  72 */     List<ObjectReference> ownedMonitors = null;
/*  73 */     List<MonitorInfo> ownedMonitorsInfo = null;
/*  74 */     ObjectReference contendedMonitor = null;
/*  75 */     boolean triedCurrentContended = false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ThreadReferenceImpl
 * JD-Core Version:    0.6.2
 */