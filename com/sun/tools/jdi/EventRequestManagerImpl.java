/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.NativeMethodException;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.AccessWatchpointRequest;
/*     */ import com.sun.jdi.request.BreakpointRequest;
/*     */ import com.sun.jdi.request.ClassPrepareRequest;
/*     */ import com.sun.jdi.request.ClassUnloadRequest;
/*     */ import com.sun.jdi.request.DuplicateRequestException;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.ExceptionRequest;
/*     */ import com.sun.jdi.request.InvalidRequestStateException;
/*     */ import com.sun.jdi.request.MethodEntryRequest;
/*     */ import com.sun.jdi.request.MethodExitRequest;
/*     */ import com.sun.jdi.request.ModificationWatchpointRequest;
/*     */ import com.sun.jdi.request.MonitorContendedEnterRequest;
/*     */ import com.sun.jdi.request.MonitorContendedEnteredRequest;
/*     */ import com.sun.jdi.request.MonitorWaitRequest;
/*     */ import com.sun.jdi.request.MonitorWaitedRequest;
/*     */ import com.sun.jdi.request.StepRequest;
/*     */ import com.sun.jdi.request.ThreadDeathRequest;
/*     */ import com.sun.jdi.request.ThreadStartRequest;
/*     */ import com.sun.jdi.request.VMDeathRequest;
/*     */ import com.sun.jdi.request.WatchpointRequest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ class EventRequestManagerImpl extends MirrorImpl
/*     */   implements EventRequestManager
/*     */ {
/*     */   List<? extends EventRequest>[] requestLists;
/*  47 */   private static int methodExitEventCmd = 0;
/*     */ 
/*     */   static int JDWPtoJDISuspendPolicy(byte paramByte) {
/*  50 */     switch (paramByte) {
/*     */     case 2:
/*  52 */       return 2;
/*     */     case 1:
/*  54 */       return 1;
/*     */     case 0:
/*  56 */       return 0;
/*     */     }
/*  58 */     throw new IllegalArgumentException("Illegal policy constant: " + paramByte);
/*     */   }
/*     */ 
/*     */   static byte JDItoJDWPSuspendPolicy(int paramInt)
/*     */   {
/*  63 */     switch (paramInt) {
/*     */     case 2:
/*  65 */       return 2;
/*     */     case 1:
/*  67 */       return 1;
/*     */     case 0:
/*  69 */       return 0;
/*     */     }
/*  71 */     throw new IllegalArgumentException("Illegal policy constant: " + paramInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  79 */     return this == paramObject;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  83 */     return System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */   EventRequestManagerImpl(VirtualMachine paramVirtualMachine)
/*     */   {
/* 720 */     super(paramVirtualMachine);
/*     */ 
/* 722 */     java.lang.reflect.Field[] arrayOfField = JDWP.EventKind.class
/* 722 */       .getDeclaredFields();
/* 723 */     int i = 0;
/* 724 */     for (int j = 0; j < arrayOfField.length; j++) {
/*     */       int k;
/*     */       try {
/* 727 */         k = arrayOfField[j].getInt(null);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 729 */         throw new RuntimeException("Got: " + localIllegalAccessException);
/*     */       }
/* 731 */       if (k > i) {
/* 732 */         i = k;
/*     */       }
/*     */     }
/* 735 */     this.requestLists = new List[i + 1];
/* 736 */     for (j = 0; j <= i; j++)
/* 737 */       this.requestLists[j] = new ArrayList();
/*     */   }
/*     */ 
/*     */   public ClassPrepareRequest createClassPrepareRequest()
/*     */   {
/* 742 */     return new ClassPrepareRequestImpl();
/*     */   }
/*     */ 
/*     */   public ClassUnloadRequest createClassUnloadRequest() {
/* 746 */     return new ClassUnloadRequestImpl();
/*     */   }
/*     */ 
/*     */   public ExceptionRequest createExceptionRequest(ReferenceType paramReferenceType, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 752 */     validateMirrorOrNull(paramReferenceType);
/* 753 */     return new ExceptionRequestImpl(paramReferenceType, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   public StepRequest createStepRequest(ThreadReference paramThreadReference, int paramInt1, int paramInt2)
/*     */   {
/* 758 */     validateMirror(paramThreadReference);
/* 759 */     return new StepRequestImpl(paramThreadReference, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public ThreadDeathRequest createThreadDeathRequest() {
/* 763 */     return new ThreadDeathRequestImpl();
/*     */   }
/*     */ 
/*     */   public ThreadStartRequest createThreadStartRequest() {
/* 767 */     return new ThreadStartRequestImpl();
/*     */   }
/*     */ 
/*     */   public MethodEntryRequest createMethodEntryRequest() {
/* 771 */     return new MethodEntryRequestImpl();
/*     */   }
/*     */ 
/*     */   public MethodExitRequest createMethodExitRequest() {
/* 775 */     return new MethodExitRequestImpl();
/*     */   }
/*     */ 
/*     */   public MonitorContendedEnterRequest createMonitorContendedEnterRequest() {
/* 779 */     if (!this.vm.canRequestMonitorEvents()) {
/* 780 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */ 
/* 783 */     return new MonitorContendedEnterRequestImpl();
/*     */   }
/*     */ 
/*     */   public MonitorContendedEnteredRequest createMonitorContendedEnteredRequest() {
/* 787 */     if (!this.vm.canRequestMonitorEvents()) {
/* 788 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */ 
/* 791 */     return new MonitorContendedEnteredRequestImpl();
/*     */   }
/*     */ 
/*     */   public MonitorWaitRequest createMonitorWaitRequest() {
/* 795 */     if (!this.vm.canRequestMonitorEvents()) {
/* 796 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */ 
/* 799 */     return new MonitorWaitRequestImpl();
/*     */   }
/*     */ 
/*     */   public MonitorWaitedRequest createMonitorWaitedRequest() {
/* 803 */     if (!this.vm.canRequestMonitorEvents()) {
/* 804 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */ 
/* 807 */     return new MonitorWaitedRequestImpl();
/*     */   }
/*     */ 
/*     */   public BreakpointRequest createBreakpointRequest(Location paramLocation) {
/* 811 */     validateMirror(paramLocation);
/* 812 */     if (paramLocation.codeIndex() == -1L) {
/* 813 */       throw new NativeMethodException("Cannot set breakpoints on native methods");
/*     */     }
/* 815 */     return new BreakpointRequestImpl(paramLocation);
/*     */   }
/*     */ 
/*     */   public AccessWatchpointRequest createAccessWatchpointRequest(com.sun.jdi.Field paramField)
/*     */   {
/* 820 */     validateMirror(paramField);
/* 821 */     if (!this.vm.canWatchFieldAccess()) {
/* 822 */       throw new UnsupportedOperationException("target VM does not support access watchpoints");
/*     */     }
/*     */ 
/* 825 */     return new AccessWatchpointRequestImpl(paramField);
/*     */   }
/*     */ 
/*     */   public ModificationWatchpointRequest createModificationWatchpointRequest(com.sun.jdi.Field paramField)
/*     */   {
/* 830 */     validateMirror(paramField);
/* 831 */     if (!this.vm.canWatchFieldModification()) {
/* 832 */       throw new UnsupportedOperationException("target VM does not support modification watchpoints");
/*     */     }
/*     */ 
/* 835 */     return new ModificationWatchpointRequestImpl(paramField);
/*     */   }
/*     */ 
/*     */   public VMDeathRequest createVMDeathRequest() {
/* 839 */     if (!this.vm.canRequestVMDeathEvent()) {
/* 840 */       throw new UnsupportedOperationException("target VM does not support requesting VM death events");
/*     */     }
/*     */ 
/* 843 */     return new VMDeathRequestImpl();
/*     */   }
/*     */ 
/*     */   public void deleteEventRequest(EventRequest paramEventRequest) {
/* 847 */     validateMirror(paramEventRequest);
/* 848 */     ((EventRequestImpl)paramEventRequest).delete();
/*     */   }
/*     */ 
/*     */   public void deleteEventRequests(List<? extends EventRequest> paramList) {
/* 852 */     validateMirrors(paramList);
/*     */ 
/* 854 */     Iterator localIterator = new ArrayList(paramList).iterator();
/* 855 */     while (localIterator.hasNext())
/* 856 */       ((EventRequestImpl)localIterator.next()).delete();
/*     */   }
/*     */ 
/*     */   public void deleteAllBreakpoints()
/*     */   {
/* 861 */     requestList(2).clear();
/*     */     try
/*     */     {
/* 864 */       JDWP.EventRequest.ClearAllBreakpoints.process(this.vm);
/*     */     } catch (JDWPException localJDWPException) {
/* 866 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<StepRequest> stepRequests() {
/* 871 */     return unmodifiableRequestList(1);
/*     */   }
/*     */ 
/*     */   public List<ClassPrepareRequest> classPrepareRequests() {
/* 875 */     return unmodifiableRequestList(8);
/*     */   }
/*     */ 
/*     */   public List<ClassUnloadRequest> classUnloadRequests() {
/* 879 */     return unmodifiableRequestList(9);
/*     */   }
/*     */ 
/*     */   public List<ThreadStartRequest> threadStartRequests() {
/* 883 */     return unmodifiableRequestList(6);
/*     */   }
/*     */ 
/*     */   public List<ThreadDeathRequest> threadDeathRequests() {
/* 887 */     return unmodifiableRequestList(7);
/*     */   }
/*     */ 
/*     */   public List<ExceptionRequest> exceptionRequests() {
/* 891 */     return unmodifiableRequestList(4);
/*     */   }
/*     */ 
/*     */   public List<BreakpointRequest> breakpointRequests() {
/* 895 */     return unmodifiableRequestList(2);
/*     */   }
/*     */ 
/*     */   public List<AccessWatchpointRequest> accessWatchpointRequests() {
/* 899 */     return unmodifiableRequestList(20);
/*     */   }
/*     */ 
/*     */   public List<ModificationWatchpointRequest> modificationWatchpointRequests() {
/* 903 */     return unmodifiableRequestList(21);
/*     */   }
/*     */ 
/*     */   public List<MethodEntryRequest> methodEntryRequests() {
/* 907 */     return unmodifiableRequestList(40);
/*     */   }
/*     */ 
/*     */   public List<MethodExitRequest> methodExitRequests() {
/* 911 */     return unmodifiableRequestList(methodExitEventCmd);
/*     */   }
/*     */ 
/*     */   public List<MonitorContendedEnterRequest> monitorContendedEnterRequests()
/*     */   {
/* 916 */     return unmodifiableRequestList(43);
/*     */   }
/*     */ 
/*     */   public List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests() {
/* 920 */     return unmodifiableRequestList(44);
/*     */   }
/*     */ 
/*     */   public List<MonitorWaitRequest> monitorWaitRequests() {
/* 924 */     return unmodifiableRequestList(45);
/*     */   }
/*     */ 
/*     */   public List<MonitorWaitedRequest> monitorWaitedRequests() {
/* 928 */     return unmodifiableRequestList(46);
/*     */   }
/*     */ 
/*     */   public List<VMDeathRequest> vmDeathRequests() {
/* 932 */     return unmodifiableRequestList(99);
/*     */   }
/*     */ 
/*     */   List<? extends EventRequest> unmodifiableRequestList(int paramInt) {
/* 936 */     return Collections.unmodifiableList(requestList(paramInt));
/*     */   }
/*     */ 
/*     */   EventRequest request(int paramInt1, int paramInt2) {
/* 940 */     List localList = requestList(paramInt1);
/* 941 */     for (int i = localList.size() - 1; i >= 0; i--) {
/* 942 */       EventRequestImpl localEventRequestImpl = (EventRequestImpl)localList.get(i);
/* 943 */       if (localEventRequestImpl.id == paramInt2) {
/* 944 */         return localEventRequestImpl;
/*     */       }
/*     */     }
/* 947 */     return null;
/*     */   }
/*     */ 
/*     */   List<? extends EventRequest> requestList(int paramInt) {
/* 951 */     return this.requestLists[paramInt];
/*     */   }
/*     */ 
/*     */   class AccessWatchpointRequestImpl extends EventRequestManagerImpl.WatchpointRequestImpl
/*     */     implements AccessWatchpointRequest
/*     */   {
/*     */     AccessWatchpointRequestImpl(com.sun.jdi.Field arg2)
/*     */     {
/* 672 */       super(localField);
/* 673 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 677 */       return 20;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 681 */       return "access watchpoint request " + this.field + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class BreakpointRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl
/*     */     implements BreakpointRequest
/*     */   {
/*     */     private final Location location;
/*     */ 
/*     */     BreakpointRequestImpl(Location arg2)
/*     */     {
/* 322 */       super();
/*     */       Location localLocation;
/* 323 */       this.location = localLocation;
/* 324 */       this.filters.add(0, 
/* 325 */         JDWP.EventRequest.Set.Modifier.LocationOnly.create(localLocation));
/*     */ 
/* 326 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     public Location location() {
/* 330 */       return this.location;
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 334 */       return 2;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 338 */       return "breakpoint request " + location() + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ClassPrepareRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements ClassPrepareRequest {
/*     */     ClassPrepareRequestImpl() {
/* 344 */       super();
/* 345 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 349 */       return 8;
/*     */     }
/*     */ 
/*     */     public synchronized void addSourceNameFilter(String paramString) {
/* 353 */       if ((isEnabled()) || (this.deleted)) {
/* 354 */         throw invalidState();
/*     */       }
/* 356 */       if (!this.vm.canUseSourceNameFilters()) {
/* 357 */         throw new UnsupportedOperationException("target does not support source name filters");
/*     */       }
/*     */ 
/* 360 */       if (paramString == null) {
/* 361 */         throw new NullPointerException();
/*     */       }
/*     */ 
/* 364 */       this.filters.add(
/* 365 */         JDWP.EventRequest.Set.Modifier.SourceNameMatch.create(paramString));
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 369 */       return "class prepare request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ClassUnloadRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements ClassUnloadRequest {
/*     */     ClassUnloadRequestImpl() {
/* 375 */       super();
/* 376 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 380 */       return 9;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 384 */       return "class unload request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class ClassVisibleEventRequestImpl extends EventRequestManagerImpl.ThreadVisibleEventRequestImpl
/*     */   {
/*     */     ClassVisibleEventRequestImpl()
/*     */     {
/* 271 */       super();
/*     */     }
/*     */     public synchronized void addClassFilter(ReferenceType paramReferenceType) {
/* 274 */       validateMirror(paramReferenceType);
/* 275 */       if ((isEnabled()) || (this.deleted)) {
/* 276 */         throw invalidState();
/*     */       }
/* 278 */       this.filters.add(
/* 279 */         JDWP.EventRequest.Set.Modifier.ClassOnly.create((ReferenceTypeImpl)paramReferenceType));
/*     */     }
/*     */ 
/*     */     public synchronized void addClassFilter(String paramString)
/*     */     {
/* 283 */       if ((isEnabled()) || (this.deleted)) {
/* 284 */         throw invalidState();
/*     */       }
/* 286 */       if (paramString == null) {
/* 287 */         throw new NullPointerException();
/*     */       }
/* 289 */       this.filters.add(
/* 290 */         JDWP.EventRequest.Set.Modifier.ClassMatch.create(paramString));
/*     */     }
/*     */ 
/*     */     public synchronized void addClassExclusionFilter(String paramString)
/*     */     {
/* 294 */       if ((isEnabled()) || (this.deleted)) {
/* 295 */         throw invalidState();
/*     */       }
/* 297 */       if (paramString == null) {
/* 298 */         throw new NullPointerException();
/*     */       }
/* 300 */       this.filters.add(
/* 301 */         JDWP.EventRequest.Set.Modifier.ClassExclude.create(paramString));
/*     */     }
/*     */ 
/*     */     public synchronized void addInstanceFilter(ObjectReference paramObjectReference)
/*     */     {
/* 305 */       validateMirror(paramObjectReference);
/* 306 */       if ((isEnabled()) || (this.deleted)) {
/* 307 */         throw invalidState();
/*     */       }
/* 309 */       if (!this.vm.canUseInstanceFilters()) {
/* 310 */         throw new UnsupportedOperationException("target does not support instance filters");
/*     */       }
/*     */ 
/* 313 */       this.filters.add(
/* 314 */         JDWP.EventRequest.Set.Modifier.InstanceOnly.create((ObjectReferenceImpl)paramObjectReference));
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class EventRequestImpl extends MirrorImpl
/*     */     implements EventRequest
/*     */   {
/*     */     int id;
/*  94 */     List<Object> filters = new ArrayList();
/*     */ 
/*  96 */     boolean isEnabled = false;
/*  97 */     boolean deleted = false;
/*  98 */     byte suspendPolicy = 2;
/*  99 */     private Map<Object, Object> clientProperties = null;
/*     */ 
/*     */     EventRequestImpl() {
/* 102 */       super();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 110 */       return this == paramObject;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 114 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     abstract int eventCmd();
/*     */ 
/*     */     InvalidRequestStateException invalidState() {
/* 120 */       return new InvalidRequestStateException(toString());
/*     */     }
/*     */ 
/*     */     String state()
/*     */     {
/* 125 */       return isEnabled() ? " (enabled)" : this.deleted ? " (deleted)" : 
/* 125 */         " (disabled)";
/*     */     }
/*     */ 
/*     */     List requestList()
/*     */     {
/* 132 */       return EventRequestManagerImpl.this.requestList(eventCmd());
/*     */     }
/*     */ 
/*     */     void delete()
/*     */     {
/* 139 */       if (!this.deleted) {
/* 140 */         requestList().remove(this);
/* 141 */         disable();
/* 142 */         this.deleted = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 147 */       return this.isEnabled;
/*     */     }
/*     */ 
/*     */     public void enable() {
/* 151 */       setEnabled(true);
/*     */     }
/*     */ 
/*     */     public void disable() {
/* 155 */       setEnabled(false);
/*     */     }
/*     */ 
/*     */     public synchronized void setEnabled(boolean paramBoolean) {
/* 159 */       if (this.deleted) {
/* 160 */         throw invalidState();
/*     */       }
/* 162 */       if (paramBoolean != this.isEnabled)
/* 163 */         if (this.isEnabled)
/* 164 */           clear();
/*     */         else
/* 166 */           set();
/*     */     }
/*     */ 
/*     */     public synchronized void addCountFilter(int paramInt)
/*     */     {
/* 173 */       if ((isEnabled()) || (this.deleted)) {
/* 174 */         throw invalidState();
/*     */       }
/* 176 */       if (paramInt < 1) {
/* 177 */         throw new IllegalArgumentException("count is less than one");
/*     */       }
/* 179 */       this.filters.add(JDWP.EventRequest.Set.Modifier.Count.create(paramInt));
/*     */     }
/*     */ 
/*     */     public void setSuspendPolicy(int paramInt) {
/* 183 */       if ((isEnabled()) || (this.deleted)) {
/* 184 */         throw invalidState();
/*     */       }
/* 186 */       this.suspendPolicy = EventRequestManagerImpl.JDItoJDWPSuspendPolicy(paramInt);
/*     */     }
/*     */ 
/*     */     public int suspendPolicy() {
/* 190 */       return EventRequestManagerImpl.JDWPtoJDISuspendPolicy(this.suspendPolicy);
/*     */     }
/*     */ 
/*     */     synchronized void set()
/*     */     {
/* 198 */       JDWP.EventRequest.Set.Modifier[] arrayOfModifier = (JDWP.EventRequest.Set.Modifier[])this.filters
/* 198 */         .toArray(
/* 199 */         new JDWP.EventRequest.Set.Modifier[this.filters
/* 199 */         .size()]);
/*     */       try {
/* 201 */         this.id = JDWP.EventRequest.Set.process(this.vm, (byte)eventCmd(), this.suspendPolicy, arrayOfModifier).requestID;
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 204 */         throw localJDWPException.toJDIException();
/*     */       }
/* 206 */       this.isEnabled = true;
/*     */     }
/*     */ 
/*     */     synchronized void clear() {
/*     */       try {
/* 211 */         JDWP.EventRequest.Clear.process(this.vm, (byte)eventCmd(), this.id);
/*     */       } catch (JDWPException localJDWPException) {
/* 213 */         throw localJDWPException.toJDIException();
/*     */       }
/* 215 */       this.isEnabled = false;
/*     */     }
/*     */ 
/*     */     private Map<Object, Object> getProperties()
/*     */     {
/* 224 */       if (this.clientProperties == null) {
/* 225 */         this.clientProperties = new HashMap(2);
/*     */       }
/* 227 */       return this.clientProperties;
/*     */     }
/*     */ 
/*     */     public final Object getProperty(Object paramObject)
/*     */     {
/* 239 */       if (this.clientProperties == null) {
/* 240 */         return null;
/*     */       }
/* 242 */       return getProperties().get(paramObject);
/*     */     }
/*     */ 
/*     */     public final void putProperty(Object paramObject1, Object paramObject2)
/*     */     {
/* 252 */       if (paramObject2 != null)
/* 253 */         getProperties().put(paramObject1, paramObject2);
/*     */       else
/* 255 */         getProperties().remove(paramObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   class ExceptionRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl
/*     */     implements ExceptionRequest
/*     */   {
/* 390 */     ReferenceType exception = null;
/* 391 */     boolean caught = true;
/* 392 */     boolean uncaught = true;
/*     */ 
/*     */     ExceptionRequestImpl(ReferenceType paramBoolean1, boolean paramBoolean2, boolean arg4) {
/* 395 */       super();
/* 396 */       this.exception = paramBoolean1;
/* 397 */       this.caught = paramBoolean2;
/*     */       boolean bool;
/* 398 */       this.uncaught = bool;
/*     */       Object localObject;
/* 401 */       if (this.exception == null)
/* 402 */         localObject = new ClassTypeImpl(this.vm, 0L);
/*     */       else {
/* 404 */         localObject = (ReferenceTypeImpl)this.exception;
/*     */       }
/* 406 */       this.filters.add(
/* 407 */         JDWP.EventRequest.Set.Modifier.ExceptionOnly.create((ReferenceTypeImpl)localObject, this.caught, this.uncaught));
/*     */ 
/* 409 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     public ReferenceType exception() {
/* 413 */       return this.exception;
/*     */     }
/*     */ 
/*     */     public boolean notifyCaught() {
/* 417 */       return this.caught;
/*     */     }
/*     */ 
/*     */     public boolean notifyUncaught() {
/* 421 */       return this.uncaught;
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 425 */       return 4;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 429 */       return "exception request " + exception() + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MethodEntryRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements MethodEntryRequest {
/*     */     MethodEntryRequestImpl() {
/* 435 */       super();
/* 436 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 440 */       return 40;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 444 */       return "method entry request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MethodExitRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements MethodExitRequest {
/*     */     MethodExitRequestImpl() {
/* 450 */       super();
/* 451 */       if (EventRequestManagerImpl.methodExitEventCmd == 0)
/*     */       {
/* 462 */         if (this.vm.canGetMethodReturnValues())
/* 463 */           EventRequestManagerImpl.access$002(42);
/*     */         else {
/* 465 */           EventRequestManagerImpl.access$002(41);
/*     */         }
/*     */       }
/* 468 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 472 */       return EventRequestManagerImpl.methodExitEventCmd;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 476 */       return "method exit request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ModificationWatchpointRequestImpl extends EventRequestManagerImpl.WatchpointRequestImpl
/*     */     implements ModificationWatchpointRequest
/*     */   {
/*     */     ModificationWatchpointRequestImpl(com.sun.jdi.Field arg2)
/*     */     {
/* 688 */       super(localField);
/* 689 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 693 */       return 21;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 697 */       return "modification watchpoint request " + this.field + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorContendedEnterRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl
/*     */     implements MonitorContendedEnterRequest
/*     */   {
/*     */     MonitorContendedEnterRequestImpl()
/*     */     {
/* 482 */       super();
/* 483 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 487 */       return 43;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 491 */       return "monitor contended enter request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorContendedEnteredRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements MonitorContendedEnteredRequest {
/*     */     MonitorContendedEnteredRequestImpl() {
/* 497 */       super();
/* 498 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 502 */       return 44;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 506 */       return "monitor contended entered request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorWaitRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements MonitorWaitRequest {
/*     */     MonitorWaitRequestImpl() {
/* 512 */       super();
/* 513 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 517 */       return 45;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 521 */       return "monitor wait request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorWaitedRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements MonitorWaitedRequest {
/*     */     MonitorWaitedRequestImpl() {
/* 527 */       super();
/* 528 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 532 */       return 46;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 536 */       return "monitor waited request " + state();
/*     */     }
/*     */   }
/*     */   class StepRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl implements StepRequest {
/*     */     ThreadReferenceImpl thread;
/*     */     int size;
/*     */     int depth;
/*     */ 
/* 546 */     StepRequestImpl(ThreadReference paramInt1, int paramInt2, int arg4) { super();
/* 547 */       this.thread = ((ThreadReferenceImpl)paramInt1);
/* 548 */       this.size = paramInt2;
/*     */       int i;
/* 549 */       this.depth = i;
/*     */       int j;
/* 555 */       switch (paramInt2) {
/*     */       case -1:
/* 557 */         j = 0;
/* 558 */         break;
/*     */       case -2:
/* 560 */         j = 1;
/* 561 */         break;
/*     */       default:
/* 563 */         throw new IllegalArgumentException("Invalid step size");
/*     */       }
/*     */       int k;
/* 567 */       switch (i) {
/*     */       case 1:
/* 569 */         k = 0;
/* 570 */         break;
/*     */       case 2:
/* 572 */         k = 1;
/* 573 */         break;
/*     */       case 3:
/* 575 */         k = 2;
/* 576 */         break;
/*     */       default:
/* 578 */         throw new IllegalArgumentException("Invalid step depth");
/*     */       }
/*     */ 
/* 584 */       List localList = EventRequestManagerImpl.this.stepRequests();
/* 585 */       Iterator localIterator = localList.iterator();
/* 586 */       while (localIterator.hasNext()) {
/* 587 */         StepRequest localStepRequest = (StepRequest)localIterator.next();
/* 588 */         if ((localStepRequest != this) && 
/* 589 */           (localStepRequest
/* 589 */           .isEnabled()) && 
/* 590 */           (localStepRequest
/* 590 */           .thread().equals(paramInt1))) {
/* 591 */           throw new DuplicateRequestException("Only one step request allowed per thread");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 596 */       this.filters.add(
/* 597 */         JDWP.EventRequest.Set.Modifier.Step.create(this.thread, j, k));
/*     */ 
/* 598 */       requestList().add(this); }
/*     */ 
/*     */     public int depth()
/*     */     {
/* 602 */       return this.depth;
/*     */     }
/*     */ 
/*     */     public int size() {
/* 606 */       return this.size;
/*     */     }
/*     */ 
/*     */     public ThreadReference thread() {
/* 610 */       return this.thread;
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 614 */       return 1;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 618 */       return "step request " + thread() + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ThreadDeathRequestImpl extends EventRequestManagerImpl.ThreadVisibleEventRequestImpl implements ThreadDeathRequest {
/*     */     ThreadDeathRequestImpl() {
/* 624 */       super();
/* 625 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 629 */       return 7;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 633 */       return "thread death request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ThreadStartRequestImpl extends EventRequestManagerImpl.ThreadVisibleEventRequestImpl implements ThreadStartRequest {
/*     */     ThreadStartRequestImpl() {
/* 639 */       super();
/* 640 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 644 */       return 6;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 648 */       return "thread start request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class ThreadVisibleEventRequestImpl extends EventRequestManagerImpl.EventRequestImpl
/*     */   {
/*     */     ThreadVisibleEventRequestImpl()
/*     */     {
/* 260 */       super();
/*     */     }
/* 262 */     public synchronized void addThreadFilter(ThreadReference paramThreadReference) { validateMirror(paramThreadReference);
/* 263 */       if ((isEnabled()) || (this.deleted)) {
/* 264 */         throw invalidState();
/*     */       }
/* 266 */       this.filters.add(
/* 267 */         JDWP.EventRequest.Set.Modifier.ThreadOnly.create((ThreadReferenceImpl)paramThreadReference));
/*     */     }
/*     */   }
/*     */ 
/*     */   class VMDeathRequestImpl extends EventRequestManagerImpl.EventRequestImpl
/*     */     implements VMDeathRequest
/*     */   {
/*     */     VMDeathRequestImpl()
/*     */     {
/* 703 */       super();
/* 704 */       requestList().add(this);
/*     */     }
/*     */ 
/*     */     int eventCmd() {
/* 708 */       return 99;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 712 */       return "VM death request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class WatchpointRequestImpl extends EventRequestManagerImpl.ClassVisibleEventRequestImpl
/*     */     implements WatchpointRequest
/*     */   {
/*     */     final com.sun.jdi.Field field;
/*     */ 
/*     */     WatchpointRequestImpl(com.sun.jdi.Field arg2)
/*     */     {
/* 656 */       super();
/*     */       Object localObject;
/* 657 */       this.field = localObject;
/* 658 */       this.filters.add(0, 
/* 659 */         JDWP.EventRequest.Set.Modifier.FieldOnly.create(
/* 660 */         (ReferenceTypeImpl)localObject
/* 660 */         .declaringType(), ((FieldImpl)localObject)
/* 661 */         .ref()));
/*     */     }
/*     */ 
/*     */     public com.sun.jdi.Field field() {
/* 665 */       return this.field;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.EventRequestManagerImpl
 * JD-Core Version:    0.6.2
 */