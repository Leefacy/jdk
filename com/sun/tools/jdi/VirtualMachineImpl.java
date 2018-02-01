/*      */ package com.sun.tools.jdi;
/*      */ 
/*      */ import com.sun.jdi.BooleanType;
/*      */ import com.sun.jdi.BooleanValue;
/*      */ import com.sun.jdi.ByteType;
/*      */ import com.sun.jdi.ByteValue;
/*      */ import com.sun.jdi.CharType;
/*      */ import com.sun.jdi.CharValue;
/*      */ import com.sun.jdi.ClassNotLoadedException;
/*      */ import com.sun.jdi.DoubleType;
/*      */ import com.sun.jdi.DoubleValue;
/*      */ import com.sun.jdi.FloatType;
/*      */ import com.sun.jdi.FloatValue;
/*      */ import com.sun.jdi.IntegerType;
/*      */ import com.sun.jdi.IntegerValue;
/*      */ import com.sun.jdi.InternalException;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.LongType;
/*      */ import com.sun.jdi.LongValue;
/*      */ import com.sun.jdi.Mirror;
/*      */ import com.sun.jdi.PathSearchingVirtualMachine;
/*      */ import com.sun.jdi.PrimitiveType;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.ShortType;
/*      */ import com.sun.jdi.ShortValue;
/*      */ import com.sun.jdi.StringReference;
/*      */ import com.sun.jdi.ThreadGroupReference;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.Type;
/*      */ import com.sun.jdi.VMDisconnectedException;
/*      */ import com.sun.jdi.VirtualMachineManager;
/*      */ import com.sun.jdi.VoidType;
/*      */ import com.sun.jdi.VoidValue;
/*      */ import com.sun.jdi.connect.spi.Connection;
/*      */ import com.sun.jdi.event.EventQueue;
/*      */ import com.sun.jdi.request.BreakpointRequest;
/*      */ import com.sun.jdi.request.EventRequest;
/*      */ import com.sun.jdi.request.EventRequestManager;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ class VirtualMachineImpl extends MirrorImpl
/*      */   implements PathSearchingVirtualMachine, ThreadListener
/*      */ {
/*      */   public final int sizeofFieldRef;
/*      */   public final int sizeofMethodRef;
/*      */   public final int sizeofObjectRef;
/*      */   public final int sizeofClassRef;
/*      */   public final int sizeofFrameRef;
/*      */   final int sequenceNumber;
/*      */   private final TargetVM target;
/*      */   private final EventQueueImpl eventQueue;
/*      */   private final EventRequestManagerImpl internalEventRequestManager;
/*      */   private final EventRequestManagerImpl eventRequestManager;
/*      */   final VirtualMachineManagerImpl vmManager;
/*      */   private final ThreadGroup threadGroupForJDI;
/*   63 */   int traceFlags = 0;
/*      */ 
/*   65 */   static int TRACE_RAW_SENDS = 16777216;
/*   66 */   static int TRACE_RAW_RECEIVES = 33554432;
/*      */ 
/*   68 */   boolean traceReceives = false;
/*      */   private Map<Long, ReferenceType> typesByID;
/*      */   private TreeSet<ReferenceType> typesBySignature;
/*   76 */   private boolean retrievedAllTypes = false;
/*      */ 
/*   79 */   private String defaultStratum = null;
/*      */ 
/*   83 */   private final Map<Long, SoftObjectReference> objectsByID = new HashMap();
/*   84 */   private final ReferenceQueue<ObjectReferenceImpl> referenceQueue = new ReferenceQueue();
/*      */   private static final int DISPOSE_THRESHOLD = 50;
/*   87 */   private final List<SoftObjectReference> batchedDisposeRequests = Collections.synchronizedList(new ArrayList(60))
/*   87 */     ;
/*      */   private JDWP.VirtualMachine.Version versionInfo;
/*      */   private JDWP.VirtualMachine.ClassPaths pathInfo;
/*   92 */   private JDWP.VirtualMachine.Capabilities capabilities = null;
/*   93 */   private JDWP.VirtualMachine.CapabilitiesNew capabilitiesNew = null;
/*      */   private BooleanType theBooleanType;
/*      */   private ByteType theByteType;
/*      */   private CharType theCharType;
/*      */   private ShortType theShortType;
/*      */   private IntegerType theIntegerType;
/*      */   private LongType theLongType;
/*      */   private FloatType theFloatType;
/*      */   private DoubleType theDoubleType;
/*      */   private VoidType theVoidType;
/*      */   private VoidValue voidVal;
/*      */   private Process process;
/*  114 */   private VMState state = new VMState(this);
/*      */ 
/*  116 */   private Object initMonitor = new Object();
/*  117 */   private boolean initComplete = false;
/*  118 */   private boolean shutdown = false;
/*      */ 
/*      */   private void notifyInitCompletion() {
/*  121 */     synchronized (this.initMonitor) {
/*  122 */       this.initComplete = true;
/*  123 */       this.initMonitor.notifyAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   void waitInitCompletion() {
/*  128 */     synchronized (this.initMonitor) {
/*  129 */       while (!this.initComplete)
/*      */         try {
/*  131 */           this.initMonitor.wait();
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   VMState state() {
/*  140 */     return this.state;
/*      */   }
/*      */ 
/*      */   public boolean threadResumable(ThreadAction paramThreadAction)
/*      */   {
/*  151 */     this.state.thaw(paramThreadAction.thread());
/*  152 */     return true;
/*      */   }
/*      */ 
/*      */   VirtualMachineImpl(VirtualMachineManager paramVirtualMachineManager, Connection paramConnection, Process paramProcess, int paramInt)
/*      */   {
/*  158 */     super(null);
/*  159 */     this.vm = this;
/*      */ 
/*  161 */     this.vmManager = ((VirtualMachineManagerImpl)paramVirtualMachineManager);
/*  162 */     this.process = paramProcess;
/*  163 */     this.sequenceNumber = paramInt;
/*      */ 
/*  168 */     this.threadGroupForJDI = new ThreadGroup(this.vmManager.mainGroupForJDI(), "JDI [" + 
/*  170 */       hashCode() + "]");
/*      */ 
/*  176 */     this.target = new TargetVM(this, paramConnection);
/*      */ 
/*  182 */     EventQueueImpl localEventQueueImpl = new EventQueueImpl(this, this.target);
/*  183 */     new InternalEventHandler(this, localEventQueueImpl);
/*      */ 
/*  187 */     this.eventQueue = new EventQueueImpl(this, this.target);
/*  188 */     this.eventRequestManager = new EventRequestManagerImpl(this);
/*      */ 
/*  190 */     this.target.start();
/*      */     JDWP.VirtualMachine.IDSizes localIDSizes;
/*      */     try
/*      */     {
/*  198 */       localIDSizes = JDWP.VirtualMachine.IDSizes.process(this.vm);
/*      */     } catch (JDWPException localJDWPException) {
/*  200 */       throw localJDWPException.toJDIException();
/*      */     }
/*  202 */     this.sizeofFieldRef = localIDSizes.fieldIDSize;
/*  203 */     this.sizeofMethodRef = localIDSizes.methodIDSize;
/*  204 */     this.sizeofObjectRef = localIDSizes.objectIDSize;
/*  205 */     this.sizeofClassRef = localIDSizes.referenceTypeIDSize;
/*  206 */     this.sizeofFrameRef = localIDSizes.frameIDSize;
/*      */ 
/*  220 */     this.internalEventRequestManager = new EventRequestManagerImpl(this);
/*  221 */     Object localObject = this.internalEventRequestManager.createClassPrepareRequest();
/*  222 */     ((EventRequest)localObject).setSuspendPolicy(0);
/*  223 */     ((EventRequest)localObject).enable();
/*  224 */     localObject = this.internalEventRequestManager.createClassUnloadRequest();
/*  225 */     ((EventRequest)localObject).setSuspendPolicy(0);
/*  226 */     ((EventRequest)localObject).enable();
/*      */ 
/*  232 */     notifyInitCompletion();
/*      */   }
/*      */ 
/*      */   EventRequestManagerImpl getInternalEventRequestManager() {
/*  236 */     return this.internalEventRequestManager;
/*      */   }
/*      */ 
/*      */   void validateVM()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  261 */     return this == paramObject;
/*      */   }
/*      */ 
/*      */   public int hashCode() {
/*  265 */     return System.identityHashCode(this);
/*      */   }
/*      */ 
/*      */   public List<ReferenceType> classesByName(String paramString) {
/*  269 */     validateVM();
/*  270 */     String str = JNITypeParser.typeNameToSignature(paramString);
/*      */     List localList;
/*  272 */     if (this.retrievedAllTypes)
/*  273 */       localList = findReferenceTypes(str);
/*      */     else {
/*  275 */       localList = retrieveClassesBySignature(str);
/*      */     }
/*  277 */     return Collections.unmodifiableList(localList);
/*      */   }
/*      */ 
/*      */   public List<ReferenceType> allClasses() {
/*  281 */     validateVM();
/*      */ 
/*  283 */     if (!this.retrievedAllTypes)
/*  284 */       retrieveAllClasses();
/*      */     ArrayList localArrayList;
/*  287 */     synchronized (this) {
/*  288 */       localArrayList = new ArrayList(this.typesBySignature);
/*      */     }
/*  290 */     return Collections.unmodifiableList(localArrayList);
/*      */   }
/*      */ 
/*      */   public void redefineClasses(Map<? extends ReferenceType, byte[]> paramMap)
/*      */   {
/*  296 */     int i = paramMap.size();
/*  297 */     JDWP.VirtualMachine.RedefineClasses.ClassDef[] arrayOfClassDef = new JDWP.VirtualMachine.RedefineClasses.ClassDef[i];
/*      */ 
/*  299 */     validateVM();
/*  300 */     if (!canRedefineClasses()) {
/*  301 */       throw new UnsupportedOperationException();
/*      */     }
/*  303 */     Iterator localIterator = paramMap.entrySet().iterator();
/*      */     Object localObject2;
/*  304 */     for (int j = 0; localIterator.hasNext(); j++) {
/*  305 */       localObject1 = (Map.Entry)localIterator.next();
/*  306 */       localObject2 = (ReferenceTypeImpl)((Map.Entry)localObject1).getKey();
/*  307 */       validateMirror((Mirror)localObject2);
/*  308 */       arrayOfClassDef[j] = new JDWP.VirtualMachine.RedefineClasses.ClassDef((ReferenceTypeImpl)localObject2, 
/*  309 */         (byte[])(byte[])((Map.Entry)localObject1)
/*  309 */         .getValue());
/*      */     }
/*      */ 
/*  313 */     this.vm.state().thaw();
/*      */     try
/*      */     {
/*  317 */       JDWP.VirtualMachine.RedefineClasses.process(this.vm, arrayOfClassDef);
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  319 */       switch (localJDWPException.errorCode()) {
/*      */       case 60:
/*  321 */         throw new ClassFormatError("class not in class file format");
/*      */       case 61:
/*      */       case 62:
/*      */       case 68:
/*      */       case 63:
/*      */       case 64:
/*      */       case 66:
/*      */       case 67:
/*      */       case 70:
/*      */       case 71:
/*      */       case 69:
/*  324 */       case 65: }  } throw new ClassCircularityError("circularity has been detected while initializing a class");
/*      */ 
/*  327 */     throw new VerifyError("verifier detected internal inconsistency or security problem");
/*      */ 
/*  330 */     throw new UnsupportedClassVersionError("version numbers of class are not supported");
/*      */ 
/*  333 */     throw new UnsupportedOperationException("add method not implemented");
/*      */ 
/*  336 */     throw new UnsupportedOperationException("schema change not implemented");
/*      */ 
/*  339 */     throw new UnsupportedOperationException("hierarchy change not implemented");
/*      */ 
/*  342 */     throw new UnsupportedOperationException("delete method not implemented");
/*      */ 
/*  345 */     throw new UnsupportedOperationException("changes to class modifiers not implemented");
/*      */ 
/*  348 */     throw new UnsupportedOperationException("changes to method modifiers not implemented");
/*      */ 
/*  351 */     throw new NoClassDefFoundError("class names do not match");
/*      */ 
/*  354 */     throw localJDWPException.toJDIException();
/*      */ 
/*  359 */     ArrayList localArrayList = new ArrayList();
/*  360 */     Object localObject1 = eventRequestManager();
/*  361 */     localIterator = ((EventRequestManager)localObject1).breakpointRequests().iterator();
/*  362 */     while (localIterator.hasNext()) {
/*  363 */       localObject2 = (BreakpointRequest)localIterator.next();
/*  364 */       if (paramMap.containsKey(((BreakpointRequest)localObject2).location().declaringType())) {
/*  365 */         localArrayList.add(localObject2);
/*      */       }
/*      */     }
/*  368 */     ((EventRequestManager)localObject1).deleteEventRequests(localArrayList);
/*      */ 
/*  371 */     localIterator = paramMap.keySet().iterator();
/*  372 */     while (localIterator.hasNext()) {
/*  373 */       localObject2 = (ReferenceTypeImpl)localIterator.next();
/*  374 */       ((ReferenceTypeImpl)localObject2).noticeRedefineClass();
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<ThreadReference> allThreads() {
/*  379 */     validateVM();
/*  380 */     return this.state.allThreads();
/*      */   }
/*      */ 
/*      */   public List<ThreadGroupReference> topLevelThreadGroups() {
/*  384 */     validateVM();
/*  385 */     return this.state.topLevelThreadGroups();
/*      */   }
/*      */ 
/*      */   PacketStream sendResumingCommand(CommandSender paramCommandSender)
/*      */   {
/*  394 */     return this.state.thawCommand(paramCommandSender);
/*      */   }
/*      */ 
/*      */   void notifySuspend()
/*      */   {
/*  402 */     this.state.freeze();
/*      */   }
/*      */ 
/*      */   public void suspend() {
/*  406 */     validateVM();
/*      */     try {
/*  408 */       JDWP.VirtualMachine.Suspend.process(this.vm);
/*      */     } catch (JDWPException localJDWPException) {
/*  410 */       throw localJDWPException.toJDIException();
/*      */     }
/*  412 */     notifySuspend();
/*      */   }
/*      */ 
/*      */   public void resume() {
/*  416 */     validateVM();
/*  417 */     CommandSender local1 = new CommandSender()
/*      */     {
/*      */       public PacketStream send() {
/*  420 */         return JDWP.VirtualMachine.Resume.enqueueCommand(VirtualMachineImpl.this.vm);
/*      */       }
/*      */     };
/*      */     try {
/*  424 */       PacketStream localPacketStream = this.state.thawCommand(local1);
/*  425 */       JDWP.VirtualMachine.Resume.waitForReply(this.vm, localPacketStream);
/*      */     }
/*      */     catch (VMDisconnectedException localVMDisconnectedException)
/*      */     {
/*      */     }
/*      */     catch (JDWPException localJDWPException)
/*      */     {
/*  445 */       switch (localJDWPException.errorCode()) {
/*      */       case 112:
/*  447 */         return;
/*      */       }
/*      */     }
/*  449 */     throw localJDWPException.toJDIException();
/*      */   }
/*      */ 
/*      */   public EventQueue eventQueue()
/*      */   {
/*  460 */     return this.eventQueue;
/*      */   }
/*      */ 
/*      */   public EventRequestManager eventRequestManager() {
/*  464 */     validateVM();
/*  465 */     return this.eventRequestManager;
/*      */   }
/*      */ 
/*      */   EventRequestManagerImpl eventRequestManagerImpl() {
/*  469 */     return this.eventRequestManager;
/*      */   }
/*      */ 
/*      */   public BooleanValue mirrorOf(boolean paramBoolean) {
/*  473 */     validateVM();
/*  474 */     return new BooleanValueImpl(this, paramBoolean);
/*      */   }
/*      */ 
/*      */   public ByteValue mirrorOf(byte paramByte) {
/*  478 */     validateVM();
/*  479 */     return new ByteValueImpl(this, paramByte);
/*      */   }
/*      */ 
/*      */   public CharValue mirrorOf(char paramChar) {
/*  483 */     validateVM();
/*  484 */     return new CharValueImpl(this, paramChar);
/*      */   }
/*      */ 
/*      */   public ShortValue mirrorOf(short paramShort) {
/*  488 */     validateVM();
/*  489 */     return new ShortValueImpl(this, paramShort);
/*      */   }
/*      */ 
/*      */   public IntegerValue mirrorOf(int paramInt) {
/*  493 */     validateVM();
/*  494 */     return new IntegerValueImpl(this, paramInt);
/*      */   }
/*      */ 
/*      */   public LongValue mirrorOf(long paramLong) {
/*  498 */     validateVM();
/*  499 */     return new LongValueImpl(this, paramLong);
/*      */   }
/*      */ 
/*      */   public FloatValue mirrorOf(float paramFloat) {
/*  503 */     validateVM();
/*  504 */     return new FloatValueImpl(this, paramFloat);
/*      */   }
/*      */ 
/*      */   public DoubleValue mirrorOf(double paramDouble) {
/*  508 */     validateVM();
/*  509 */     return new DoubleValueImpl(this, paramDouble);
/*      */   }
/*      */ 
/*      */   public StringReference mirrorOf(String paramString) {
/*  513 */     validateVM();
/*      */     try
/*      */     {
/*  516 */       return JDWP.VirtualMachine.CreateString.process(this.vm, paramString).stringObject;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  518 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public VoidValue mirrorOfVoid() {
/*  523 */     if (this.voidVal == null) {
/*  524 */       this.voidVal = new VoidValueImpl(this);
/*      */     }
/*  526 */     return this.voidVal;
/*      */   }
/*      */ 
/*      */   public long[] instanceCounts(List<? extends ReferenceType> paramList) {
/*  530 */     if (!canGetInstanceInfo()) {
/*  531 */       throw new UnsupportedOperationException("target does not support getting instances");
/*      */     }
/*      */ 
/*  535 */     ReferenceTypeImpl[] arrayOfReferenceTypeImpl = new ReferenceTypeImpl[paramList.size()];
/*  536 */     int i = 0;
/*  537 */     for (ReferenceType localReferenceType : paramList) {
/*  538 */       validateMirror(localReferenceType);
/*  539 */       arrayOfReferenceTypeImpl[(i++)] = ((ReferenceTypeImpl)localReferenceType);
/*      */     }
/*      */     long[] arrayOfLong;
/*      */     try {
/*  543 */       arrayOfLong = JDWP.VirtualMachine.InstanceCounts.process(this.vm, arrayOfReferenceTypeImpl).counts;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  545 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/*  548 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   public void dispose() {
/*  552 */     validateVM();
/*  553 */     this.shutdown = true;
/*      */     try {
/*  555 */       JDWP.VirtualMachine.Dispose.process(this.vm);
/*      */     } catch (JDWPException localJDWPException) {
/*  557 */       throw localJDWPException.toJDIException();
/*      */     }
/*  559 */     this.target.stopListening();
/*      */   }
/*      */ 
/*      */   public void exit(int paramInt) {
/*  563 */     validateVM();
/*  564 */     this.shutdown = true;
/*      */     try {
/*  566 */       JDWP.VirtualMachine.Exit.process(this.vm, paramInt);
/*      */     } catch (JDWPException localJDWPException) {
/*  568 */       throw localJDWPException.toJDIException();
/*      */     }
/*  570 */     this.target.stopListening();
/*      */   }
/*      */ 
/*      */   public Process process() {
/*  574 */     validateVM();
/*  575 */     return this.process;
/*      */   }
/*      */ 
/*      */   private JDWP.VirtualMachine.Version versionInfo() {
/*      */     try {
/*  580 */       if (this.versionInfo == null)
/*      */       {
/*  582 */         this.versionInfo = JDWP.VirtualMachine.Version.process(this.vm);
/*      */       }
/*  584 */       return this.versionInfo;
/*      */     } catch (JDWPException localJDWPException) {
/*  586 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */   }
/*      */ 
/*  590 */   public String description() { validateVM();
/*      */ 
/*  592 */     return MessageFormat.format(this.vmManager.getString("version_format"), new Object[] { "" + this.vmManager
/*  593 */       .majorInterfaceVersion(), "" + this.vmManager
/*  594 */       .minorInterfaceVersion(), 
/*  595 */       versionInfo().description }); }
/*      */ 
/*      */   public String version()
/*      */   {
/*  599 */     validateVM();
/*  600 */     return versionInfo().vmVersion;
/*      */   }
/*      */ 
/*      */   public String name() {
/*  604 */     validateVM();
/*  605 */     return versionInfo().vmName;
/*      */   }
/*      */ 
/*      */   public boolean canWatchFieldModification() {
/*  609 */     validateVM();
/*  610 */     return capabilities().canWatchFieldModification;
/*      */   }
/*      */   public boolean canWatchFieldAccess() {
/*  613 */     validateVM();
/*  614 */     return capabilities().canWatchFieldAccess;
/*      */   }
/*      */   public boolean canGetBytecodes() {
/*  617 */     validateVM();
/*  618 */     return capabilities().canGetBytecodes;
/*      */   }
/*      */   public boolean canGetSyntheticAttribute() {
/*  621 */     validateVM();
/*  622 */     return capabilities().canGetSyntheticAttribute;
/*      */   }
/*      */   public boolean canGetOwnedMonitorInfo() {
/*  625 */     validateVM();
/*  626 */     return capabilities().canGetOwnedMonitorInfo;
/*      */   }
/*      */   public boolean canGetCurrentContendedMonitor() {
/*  629 */     validateVM();
/*  630 */     return capabilities().canGetCurrentContendedMonitor;
/*      */   }
/*      */   public boolean canGetMonitorInfo() {
/*  633 */     validateVM();
/*  634 */     return capabilities().canGetMonitorInfo;
/*      */   }
/*      */ 
/*      */   private boolean hasNewCapabilities()
/*      */   {
/*  639 */     return (versionInfo().jdwpMajor > 1) || 
/*  639 */       (versionInfo().jdwpMinor >= 4);
/*      */   }
/*      */ 
/*      */   boolean canGet1_5LanguageFeatures()
/*      */   {
/*  644 */     return (versionInfo().jdwpMajor > 1) || 
/*  644 */       (versionInfo().jdwpMinor >= 5);
/*      */   }
/*      */ 
/*      */   public boolean canUseInstanceFilters() {
/*  648 */     validateVM();
/*      */ 
/*  650 */     return (hasNewCapabilities()) && 
/*  650 */       (capabilitiesNew().canUseInstanceFilters);
/*      */   }
/*      */   public boolean canRedefineClasses() {
/*  653 */     validateVM();
/*      */ 
/*  655 */     return (hasNewCapabilities()) && 
/*  655 */       (capabilitiesNew().canRedefineClasses);
/*      */   }
/*      */   public boolean canAddMethod() {
/*  658 */     validateVM();
/*      */ 
/*  660 */     return (hasNewCapabilities()) && 
/*  660 */       (capabilitiesNew().canAddMethod);
/*      */   }
/*      */   public boolean canUnrestrictedlyRedefineClasses() {
/*  663 */     validateVM();
/*      */ 
/*  665 */     return (hasNewCapabilities()) && 
/*  665 */       (capabilitiesNew().canUnrestrictedlyRedefineClasses);
/*      */   }
/*      */   public boolean canPopFrames() {
/*  668 */     validateVM();
/*      */ 
/*  670 */     return (hasNewCapabilities()) && 
/*  670 */       (capabilitiesNew().canPopFrames);
/*      */   }
/*      */ 
/*      */   public boolean canGetMethodReturnValues() {
/*  674 */     return (versionInfo().jdwpMajor > 1) || 
/*  674 */       (versionInfo().jdwpMinor >= 6);
/*      */   }
/*      */   public boolean canGetInstanceInfo() {
/*  677 */     if ((versionInfo().jdwpMajor < 1) || 
/*  678 */       (versionInfo().jdwpMinor < 6)) {
/*  679 */       return false;
/*      */     }
/*  681 */     validateVM();
/*      */ 
/*  683 */     return (hasNewCapabilities()) && 
/*  683 */       (capabilitiesNew().canGetInstanceInfo);
/*      */   }
/*      */   public boolean canUseSourceNameFilters() {
/*  686 */     if ((versionInfo().jdwpMajor < 1) || 
/*  687 */       (versionInfo().jdwpMinor < 6)) {
/*  688 */       return false;
/*      */     }
/*  690 */     return true;
/*      */   }
/*      */   public boolean canForceEarlyReturn() {
/*  693 */     validateVM();
/*      */ 
/*  695 */     return (hasNewCapabilities()) && 
/*  695 */       (capabilitiesNew().canForceEarlyReturn);
/*      */   }
/*      */   public boolean canBeModified() {
/*  698 */     return true;
/*      */   }
/*      */   public boolean canGetSourceDebugExtension() {
/*  701 */     validateVM();
/*      */ 
/*  703 */     return (hasNewCapabilities()) && 
/*  703 */       (capabilitiesNew().canGetSourceDebugExtension);
/*      */   }
/*      */   public boolean canGetClassFileVersion() {
/*  706 */     if ((versionInfo().jdwpMajor < 1) && 
/*  707 */       (versionInfo().jdwpMinor < 6)) {
/*  708 */       return false;
/*      */     }
/*  710 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean canGetConstantPool() {
/*  714 */     validateVM();
/*      */ 
/*  716 */     return (hasNewCapabilities()) && 
/*  716 */       (capabilitiesNew().canGetConstantPool);
/*      */   }
/*      */   public boolean canRequestVMDeathEvent() {
/*  719 */     validateVM();
/*      */ 
/*  721 */     return (hasNewCapabilities()) && 
/*  721 */       (capabilitiesNew().canRequestVMDeathEvent);
/*      */   }
/*      */   public boolean canRequestMonitorEvents() {
/*  724 */     validateVM();
/*      */ 
/*  726 */     return (hasNewCapabilities()) && 
/*  726 */       (capabilitiesNew().canRequestMonitorEvents);
/*      */   }
/*      */   public boolean canGetMonitorFrameInfo() {
/*  729 */     validateVM();
/*      */ 
/*  731 */     return (hasNewCapabilities()) && 
/*  731 */       (capabilitiesNew().canGetMonitorFrameInfo);
/*      */   }
/*      */ 
/*      */   public void setDebugTraceMode(int paramInt) {
/*  735 */     validateVM();
/*  736 */     this.traceFlags = paramInt;
/*  737 */     this.traceReceives = ((paramInt & 0x2) != 0);
/*      */   }
/*      */ 
/*      */   void printTrace(String paramString) {
/*  741 */     System.err.println("[JDI: " + paramString + "]");
/*      */   }
/*      */ 
/*      */   void printReceiveTrace(int paramInt, String paramString) {
/*  745 */     StringBuffer localStringBuffer = new StringBuffer("Receiving:");
/*  746 */     for (int i = paramInt; i > 0; i--) {
/*  747 */       localStringBuffer.append("    ");
/*      */     }
/*  749 */     localStringBuffer.append(paramString);
/*  750 */     printTrace(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   private synchronized ReferenceTypeImpl addReferenceType(long paramLong, int paramInt, String paramString)
/*      */   {
/*  756 */     if (this.typesByID == null) {
/*  757 */       initReferenceTypes();
/*      */     }
/*  759 */     Object localObject = null;
/*  760 */     switch (paramInt) {
/*      */     case 1:
/*  762 */       localObject = new ClassTypeImpl(this.vm, paramLong);
/*  763 */       break;
/*      */     case 2:
/*  765 */       localObject = new InterfaceTypeImpl(this.vm, paramLong);
/*  766 */       break;
/*      */     case 3:
/*  768 */       localObject = new ArrayTypeImpl(this.vm, paramLong);
/*  769 */       break;
/*      */     default:
/*  771 */       throw new InternalException("Invalid reference type tag");
/*      */     }
/*      */ 
/*  780 */     if (paramString != null) {
/*  781 */       ((ReferenceTypeImpl)localObject).setSignature(paramString);
/*      */     }
/*      */ 
/*  784 */     this.typesByID.put(new Long(paramLong), localObject);
/*  785 */     this.typesBySignature.add(localObject);
/*      */ 
/*  787 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  788 */       this.vm.printTrace("Caching new ReferenceType, sig=" + paramString + ", id=" + paramLong);
/*      */     }
/*      */ 
/*  792 */     return localObject;
/*      */   }
/*      */ 
/*      */   synchronized void removeReferenceType(String paramString) {
/*  796 */     if (this.typesByID == null) {
/*  797 */       return;
/*      */     }
/*      */ 
/*  804 */     Iterator localIterator = this.typesBySignature.iterator();
/*  805 */     int i = 0;
/*  806 */     while (localIterator.hasNext()) {
/*  807 */       ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)localIterator.next();
/*  808 */       int j = paramString.compareTo(localReferenceTypeImpl.signature());
/*  809 */       if (j == 0) {
/*  810 */         i++;
/*  811 */         localIterator.remove();
/*  812 */         this.typesByID.remove(new Long(localReferenceTypeImpl.ref()));
/*  813 */         if ((this.vm.traceFlags & 0x8) != 0) {
/*  814 */           this.vm.printTrace("Uncaching ReferenceType, sig=" + paramString + ", id=" + localReferenceTypeImpl
/*  815 */             .ref());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  827 */     if (i > 1)
/*  828 */       retrieveClassesBySignature(paramString);
/*      */   }
/*      */ 
/*      */   private synchronized List<ReferenceType> findReferenceTypes(String paramString)
/*      */   {
/*  833 */     if (this.typesByID == null) {
/*  834 */       return new ArrayList(0);
/*      */     }
/*  836 */     Iterator localIterator = this.typesBySignature.iterator();
/*  837 */     ArrayList localArrayList = new ArrayList();
/*  838 */     while (localIterator.hasNext()) {
/*  839 */       ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)localIterator.next();
/*  840 */       int i = paramString.compareTo(localReferenceTypeImpl.signature());
/*  841 */       if (i == 0) {
/*  842 */         localArrayList.add(localReferenceTypeImpl);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  848 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private void initReferenceTypes() {
/*  852 */     this.typesByID = new HashMap(300);
/*  853 */     this.typesBySignature = new TreeSet();
/*      */   }
/*      */ 
/*      */   ReferenceTypeImpl referenceType(long paramLong, byte paramByte) {
/*  857 */     return referenceType(paramLong, paramByte, null);
/*      */   }
/*      */ 
/*      */   ClassTypeImpl classType(long paramLong) {
/*  861 */     return (ClassTypeImpl)referenceType(paramLong, 1, null);
/*      */   }
/*      */ 
/*      */   InterfaceTypeImpl interfaceType(long paramLong) {
/*  865 */     return (InterfaceTypeImpl)referenceType(paramLong, 2, null);
/*      */   }
/*      */ 
/*      */   ArrayTypeImpl arrayType(long paramLong) {
/*  869 */     return (ArrayTypeImpl)referenceType(paramLong, 3, null);
/*      */   }
/*      */ 
/*      */   ReferenceTypeImpl referenceType(long paramLong, int paramInt, String paramString)
/*      */   {
/*  874 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  875 */       localObject1 = new StringBuffer();
/*  876 */       ((StringBuffer)localObject1).append("Looking up ");
/*  877 */       if (paramInt == 1)
/*  878 */         ((StringBuffer)localObject1).append("Class");
/*  879 */       else if (paramInt == 2)
/*  880 */         ((StringBuffer)localObject1).append("Interface");
/*  881 */       else if (paramInt == 3)
/*  882 */         ((StringBuffer)localObject1).append("ArrayType");
/*      */       else {
/*  884 */         ((StringBuffer)localObject1).append("UNKNOWN TAG: " + paramInt);
/*      */       }
/*  886 */       if (paramString != null) {
/*  887 */         ((StringBuffer)localObject1).append(", signature='" + paramString + "'");
/*      */       }
/*  889 */       ((StringBuffer)localObject1).append(", id=" + paramLong);
/*  890 */       this.vm.printTrace(((StringBuffer)localObject1).toString());
/*      */     }
/*  892 */     if (paramLong == 0L) {
/*  893 */       return null;
/*      */     }
/*  895 */     Object localObject1 = null;
/*  896 */     synchronized (this) {
/*  897 */       if (this.typesByID != null) {
/*  898 */         localObject1 = (ReferenceTypeImpl)this.typesByID.get(new Long(paramLong));
/*      */       }
/*  900 */       if (localObject1 == null) {
/*  901 */         localObject1 = addReferenceType(paramLong, paramInt, paramString);
/*      */       }
/*      */     }
/*  904 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private JDWP.VirtualMachine.Capabilities capabilities()
/*      */   {
/*  909 */     if (this.capabilities == null) {
/*      */       try {
/*  911 */         this.capabilities = 
/*  912 */           JDWP.VirtualMachine.Capabilities.process(this.vm);
/*      */       }
/*      */       catch (JDWPException localJDWPException) {
/*  914 */         throw localJDWPException.toJDIException();
/*      */       }
/*      */     }
/*  917 */     return this.capabilities;
/*      */   }
/*      */ 
/*      */   private JDWP.VirtualMachine.CapabilitiesNew capabilitiesNew() {
/*  921 */     if (this.capabilitiesNew == null) {
/*      */       try {
/*  923 */         this.capabilitiesNew = 
/*  924 */           JDWP.VirtualMachine.CapabilitiesNew.process(this.vm);
/*      */       }
/*      */       catch (JDWPException localJDWPException) {
/*  926 */         throw localJDWPException.toJDIException();
/*      */       }
/*      */     }
/*  929 */     return this.capabilitiesNew;
/*      */   }
/*      */ 
/*      */   private List<ReferenceType> retrieveClassesBySignature(String paramString) {
/*  933 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  934 */       this.vm.printTrace("Retrieving matching ReferenceTypes, sig=" + paramString);
/*      */     }
/*      */     JDWP.VirtualMachine.ClassesBySignature.ClassInfo[] arrayOfClassInfo;
/*      */     try
/*      */     {
/*  939 */       arrayOfClassInfo = JDWP.VirtualMachine.ClassesBySignature.process(this.vm, paramString).classes;
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/*  941 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/*  944 */     int i = arrayOfClassInfo.length;
/*  945 */     ArrayList localArrayList = new ArrayList(i);
/*      */ 
/*  948 */     synchronized (this) {
/*  949 */       for (int j = 0; j < i; j++) {
/*  950 */         JDWP.VirtualMachine.ClassesBySignature.ClassInfo localClassInfo = arrayOfClassInfo[j];
/*      */ 
/*  952 */         ReferenceTypeImpl localReferenceTypeImpl = referenceType(localClassInfo.typeID, localClassInfo.refTypeTag, paramString);
/*      */ 
/*  955 */         localReferenceTypeImpl.setStatus(localClassInfo.status);
/*  956 */         localArrayList.add(localReferenceTypeImpl);
/*      */       }
/*      */     }
/*  959 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private void retrieveAllClasses1_4() {
/*      */     JDWP.VirtualMachine.AllClasses.ClassInfo[] arrayOfClassInfo;
/*      */     try {
/*  965 */       arrayOfClassInfo = JDWP.VirtualMachine.AllClasses.process(this.vm).classes;
/*      */     } catch (JDWPException localJDWPException) {
/*  967 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/*  972 */     synchronized (this) {
/*  973 */       if (!this.retrievedAllTypes)
/*      */       {
/*  975 */         int i = arrayOfClassInfo.length;
/*  976 */         for (int j = 0; j < i; j++) {
/*  977 */           JDWP.VirtualMachine.AllClasses.ClassInfo localClassInfo = arrayOfClassInfo[j];
/*      */ 
/*  979 */           ReferenceTypeImpl localReferenceTypeImpl = referenceType(localClassInfo.typeID, localClassInfo.refTypeTag, localClassInfo.signature);
/*      */ 
/*  982 */           localReferenceTypeImpl.setStatus(localClassInfo.status);
/*      */         }
/*  984 */         this.retrievedAllTypes = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void retrieveAllClasses() {
/*  990 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  991 */       this.vm.printTrace("Retrieving all ReferenceTypes");
/*      */     }
/*      */ 
/*  994 */     if (!this.vm.canGet1_5LanguageFeatures())
/*      */     {
/*  995 */       retrieveAllClasses1_4();
/*      */       return;
/*      */     }
/*      */ 
/*      */     JDWP.VirtualMachine.AllClassesWithGeneric.ClassInfo[] arrayOfClassInfo;
/*      */     try
/*      */     {
/* 1006 */       arrayOfClassInfo = JDWP.VirtualMachine.AllClassesWithGeneric.process(this.vm).classes;
/*      */     } catch (JDWPException localJDWPException) {
/* 1008 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */ 
/* 1013 */     synchronized (this) {
/* 1014 */       if (!this.retrievedAllTypes)
/*      */       {
/* 1016 */         int i = arrayOfClassInfo.length;
/* 1017 */         for (int j = 0; j < i; j++) {
/* 1018 */           JDWP.VirtualMachine.AllClassesWithGeneric.ClassInfo localClassInfo = arrayOfClassInfo[j];
/*      */ 
/* 1020 */           ReferenceTypeImpl localReferenceTypeImpl = referenceType(localClassInfo.typeID, localClassInfo.refTypeTag, localClassInfo.signature);
/*      */ 
/* 1023 */           localReferenceTypeImpl.setGenericSignature(localClassInfo.genericSignature);
/* 1024 */           localReferenceTypeImpl.setStatus(localClassInfo.status);
/*      */         }
/* 1026 */         this.retrievedAllTypes = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void sendToTarget(Packet paramPacket) {
/* 1032 */     this.target.send(paramPacket);
/*      */   }
/*      */ 
/*      */   void waitForTargetReply(Packet paramPacket) {
/* 1036 */     this.target.waitForReply(paramPacket);
/*      */ 
/* 1040 */     processBatchedDisposes();
/*      */   }
/*      */ 
/*      */   Type findBootType(String paramString) throws ClassNotLoadedException {
/* 1044 */     List localList = retrieveClassesBySignature(paramString);
/* 1045 */     Iterator localIterator = localList.iterator();
/* 1046 */     while (localIterator.hasNext()) {
/* 1047 */       localObject = (ReferenceType)localIterator.next();
/* 1048 */       if (((ReferenceType)localObject).classLoader() == null) {
/* 1049 */         return localObject;
/*      */       }
/*      */     }
/* 1052 */     Object localObject = new JNITypeParser(paramString);
/*      */ 
/* 1054 */     throw new ClassNotLoadedException(((JNITypeParser)localObject).typeName(), "Type " + ((JNITypeParser)localObject)
/* 1054 */       .typeName() + " not loaded");
/*      */   }
/*      */ 
/*      */   BooleanType theBooleanType() {
/* 1058 */     if (this.theBooleanType == null) {
/* 1059 */       synchronized (this) {
/* 1060 */         if (this.theBooleanType == null) {
/* 1061 */           this.theBooleanType = new BooleanTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1065 */     return this.theBooleanType;
/*      */   }
/*      */ 
/*      */   ByteType theByteType() {
/* 1069 */     if (this.theByteType == null) {
/* 1070 */       synchronized (this) {
/* 1071 */         if (this.theByteType == null) {
/* 1072 */           this.theByteType = new ByteTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1076 */     return this.theByteType;
/*      */   }
/*      */ 
/*      */   CharType theCharType() {
/* 1080 */     if (this.theCharType == null) {
/* 1081 */       synchronized (this) {
/* 1082 */         if (this.theCharType == null) {
/* 1083 */           this.theCharType = new CharTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1087 */     return this.theCharType;
/*      */   }
/*      */ 
/*      */   ShortType theShortType() {
/* 1091 */     if (this.theShortType == null) {
/* 1092 */       synchronized (this) {
/* 1093 */         if (this.theShortType == null) {
/* 1094 */           this.theShortType = new ShortTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1098 */     return this.theShortType;
/*      */   }
/*      */ 
/*      */   IntegerType theIntegerType() {
/* 1102 */     if (this.theIntegerType == null) {
/* 1103 */       synchronized (this) {
/* 1104 */         if (this.theIntegerType == null) {
/* 1105 */           this.theIntegerType = new IntegerTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1109 */     return this.theIntegerType;
/*      */   }
/*      */ 
/*      */   LongType theLongType() {
/* 1113 */     if (this.theLongType == null) {
/* 1114 */       synchronized (this) {
/* 1115 */         if (this.theLongType == null) {
/* 1116 */           this.theLongType = new LongTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1120 */     return this.theLongType;
/*      */   }
/*      */ 
/*      */   FloatType theFloatType() {
/* 1124 */     if (this.theFloatType == null) {
/* 1125 */       synchronized (this) {
/* 1126 */         if (this.theFloatType == null) {
/* 1127 */           this.theFloatType = new FloatTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1131 */     return this.theFloatType;
/*      */   }
/*      */ 
/*      */   DoubleType theDoubleType() {
/* 1135 */     if (this.theDoubleType == null) {
/* 1136 */       synchronized (this) {
/* 1137 */         if (this.theDoubleType == null) {
/* 1138 */           this.theDoubleType = new DoubleTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1142 */     return this.theDoubleType;
/*      */   }
/*      */ 
/*      */   VoidType theVoidType() {
/* 1146 */     if (this.theVoidType == null) {
/* 1147 */       synchronized (this) {
/* 1148 */         if (this.theVoidType == null) {
/* 1149 */           this.theVoidType = new VoidTypeImpl(this);
/*      */         }
/*      */       }
/*      */     }
/* 1153 */     return this.theVoidType;
/*      */   }
/*      */ 
/*      */   PrimitiveType primitiveTypeMirror(byte paramByte) {
/* 1157 */     switch (paramByte) {
/*      */     case 90:
/* 1159 */       return theBooleanType();
/*      */     case 66:
/* 1161 */       return theByteType();
/*      */     case 67:
/* 1163 */       return theCharType();
/*      */     case 83:
/* 1165 */       return theShortType();
/*      */     case 73:
/* 1167 */       return theIntegerType();
/*      */     case 74:
/* 1169 */       return theLongType();
/*      */     case 70:
/* 1171 */       return theFloatType();
/*      */     case 68:
/* 1173 */       return theDoubleType();
/*      */     case 69:
/*      */     case 71:
/*      */     case 72:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/* 1175 */     case 89: } throw new IllegalArgumentException("Unrecognized primitive tag " + paramByte);
/*      */   }
/*      */ 
/*      */   private void processBatchedDisposes()
/*      */   {
/* 1180 */     if (this.shutdown) {
/* 1181 */       return;
/*      */     }
/*      */ 
/* 1184 */     JDWP.VirtualMachine.DisposeObjects.Request[] arrayOfRequest = null;
/* 1185 */     synchronized (this.batchedDisposeRequests) {
/* 1186 */       int i = this.batchedDisposeRequests.size();
/* 1187 */       if (i >= 50) {
/* 1188 */         if ((this.traceFlags & 0x10) != 0) {
/* 1189 */           printTrace("Dispose threashold reached. Will dispose " + i + " object references...");
/*      */         }
/*      */ 
/* 1192 */         arrayOfRequest = new JDWP.VirtualMachine.DisposeObjects.Request[i];
/* 1193 */         for (int j = 0; j < arrayOfRequest.length; j++) {
/* 1194 */           SoftObjectReference localSoftObjectReference = (SoftObjectReference)this.batchedDisposeRequests.get(j);
/* 1195 */           if ((this.traceFlags & 0x10) != 0) {
/* 1196 */             printTrace("Disposing object " + localSoftObjectReference.key().longValue() + " (ref count = " + localSoftObjectReference
/* 1197 */               .count() + ")");
/*      */           }
/*      */ 
/* 1203 */           arrayOfRequest[j] = new JDWP.VirtualMachine.DisposeObjects.Request(new ObjectReferenceImpl(this, localSoftObjectReference
/* 1205 */             .key().longValue()), localSoftObjectReference
/* 1206 */             .count());
/*      */         }
/* 1208 */         this.batchedDisposeRequests.clear();
/*      */       }
/*      */     }
/* 1211 */     if (arrayOfRequest != null)
/*      */       try {
/* 1213 */         JDWP.VirtualMachine.DisposeObjects.process(this.vm, arrayOfRequest);
/*      */       } catch (JDWPException localJDWPException) {
/* 1215 */         throw localJDWPException.toJDIException();
/*      */       }
/*      */   }
/*      */ 
/*      */   private void batchForDispose(SoftObjectReference paramSoftObjectReference)
/*      */   {
/* 1221 */     if ((this.traceFlags & 0x10) != 0) {
/* 1222 */       printTrace("Batching object " + paramSoftObjectReference.key().longValue() + " for dispose (ref count = " + paramSoftObjectReference
/* 1223 */         .count() + ")");
/*      */     }
/* 1225 */     this.batchedDisposeRequests.add(paramSoftObjectReference);
/*      */   }
/*      */ 
/*      */   private void processQueue()
/*      */   {
/*      */     Reference localReference;
/* 1233 */     while ((localReference = this.referenceQueue.poll()) != null) {
/* 1234 */       SoftObjectReference localSoftObjectReference = (SoftObjectReference)localReference;
/* 1235 */       removeObjectMirror(localSoftObjectReference);
/* 1236 */       batchForDispose(localSoftObjectReference);
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized ObjectReferenceImpl objectMirror(long paramLong, int paramInt)
/*      */   {
/* 1243 */     processQueue();
/*      */ 
/* 1245 */     if (paramLong == 0L) {
/* 1246 */       return null;
/*      */     }
/* 1248 */     Object localObject = null;
/* 1249 */     Long localLong = new Long(paramLong);
/*      */ 
/* 1254 */     SoftObjectReference localSoftObjectReference = (SoftObjectReference)this.objectsByID.get(localLong);
/* 1255 */     if (localSoftObjectReference != null) {
/* 1256 */       localObject = localSoftObjectReference.object();
/*      */     }
/*      */ 
/* 1263 */     if (localObject == null) {
/* 1264 */       switch (paramInt) {
/*      */       case 76:
/* 1266 */         localObject = new ObjectReferenceImpl(this.vm, paramLong);
/* 1267 */         break;
/*      */       case 115:
/* 1269 */         localObject = new StringReferenceImpl(this.vm, paramLong);
/* 1270 */         break;
/*      */       case 91:
/* 1272 */         localObject = new ArrayReferenceImpl(this.vm, paramLong);
/* 1273 */         break;
/*      */       case 116:
/* 1275 */         ThreadReferenceImpl localThreadReferenceImpl = new ThreadReferenceImpl(this.vm, paramLong);
/*      */ 
/* 1277 */         localThreadReferenceImpl.addListener(this);
/* 1278 */         localObject = localThreadReferenceImpl;
/* 1279 */         break;
/*      */       case 103:
/* 1281 */         localObject = new ThreadGroupReferenceImpl(this.vm, paramLong);
/* 1282 */         break;
/*      */       case 108:
/* 1284 */         localObject = new ClassLoaderReferenceImpl(this.vm, paramLong);
/* 1285 */         break;
/*      */       case 99:
/* 1287 */         localObject = new ClassObjectReferenceImpl(this.vm, paramLong);
/* 1288 */         break;
/*      */       default:
/* 1290 */         throw new IllegalArgumentException("Invalid object tag: " + paramInt);
/*      */       }
/* 1292 */       localSoftObjectReference = new SoftObjectReference(localLong, (ObjectReferenceImpl)localObject, this.referenceQueue);
/*      */ 
/* 1298 */       this.objectsByID.put(localLong, localSoftObjectReference);
/* 1299 */       if ((this.traceFlags & 0x10) != 0)
/* 1300 */         printTrace("Creating new " + localObject
/* 1301 */           .getClass().getName() + " (id = " + paramLong + ")");
/*      */     }
/*      */     else {
/* 1304 */       localSoftObjectReference.incrementCount();
/*      */     }
/*      */ 
/* 1307 */     return localObject;
/*      */   }
/*      */ 
/*      */   synchronized void removeObjectMirror(ObjectReferenceImpl paramObjectReferenceImpl)
/*      */   {
/* 1313 */     processQueue();
/*      */ 
/* 1315 */     SoftObjectReference localSoftObjectReference = (SoftObjectReference)this.objectsByID.remove(new Long(paramObjectReferenceImpl.ref()));
/* 1316 */     if (localSoftObjectReference != null) {
/* 1317 */       batchForDispose(localSoftObjectReference);
/*      */     }
/*      */     else
/*      */     {
/* 1323 */       throw new InternalException("ObjectReference " + paramObjectReferenceImpl.ref() + " not found in object cache");
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void removeObjectMirror(SoftObjectReference paramSoftObjectReference)
/*      */   {
/* 1333 */     this.objectsByID.remove(paramSoftObjectReference.key());
/*      */   }
/*      */ 
/*      */   ObjectReferenceImpl objectMirror(long paramLong) {
/* 1337 */     return objectMirror(paramLong, 76);
/*      */   }
/*      */ 
/*      */   StringReferenceImpl stringMirror(long paramLong) {
/* 1341 */     return (StringReferenceImpl)objectMirror(paramLong, 115);
/*      */   }
/*      */ 
/*      */   ArrayReferenceImpl arrayMirror(long paramLong) {
/* 1345 */     return (ArrayReferenceImpl)objectMirror(paramLong, 91);
/*      */   }
/*      */ 
/*      */   ThreadReferenceImpl threadMirror(long paramLong) {
/* 1349 */     return (ThreadReferenceImpl)objectMirror(paramLong, 116);
/*      */   }
/*      */ 
/*      */   ThreadGroupReferenceImpl threadGroupMirror(long paramLong) {
/* 1353 */     return (ThreadGroupReferenceImpl)objectMirror(paramLong, 103);
/*      */   }
/*      */ 
/*      */   ClassLoaderReferenceImpl classLoaderMirror(long paramLong)
/*      */   {
/* 1358 */     return (ClassLoaderReferenceImpl)objectMirror(paramLong, 108);
/*      */   }
/*      */ 
/*      */   ClassObjectReferenceImpl classObjectMirror(long paramLong)
/*      */   {
/* 1363 */     return (ClassObjectReferenceImpl)objectMirror(paramLong, 99);
/*      */   }
/*      */ 
/*      */   private JDWP.VirtualMachine.ClassPaths getClasspath()
/*      */   {
/* 1371 */     if (this.pathInfo == null) {
/*      */       try {
/* 1373 */         this.pathInfo = JDWP.VirtualMachine.ClassPaths.process(this.vm);
/*      */       } catch (JDWPException localJDWPException) {
/* 1375 */         throw localJDWPException.toJDIException();
/*      */       }
/*      */     }
/* 1378 */     return this.pathInfo;
/*      */   }
/*      */ 
/*      */   public List<String> classPath() {
/* 1382 */     return Arrays.asList(getClasspath().classpaths);
/*      */   }
/*      */ 
/*      */   public List<String> bootClassPath() {
/* 1386 */     return Arrays.asList(getClasspath().bootclasspaths);
/*      */   }
/*      */ 
/*      */   public String baseDirectory() {
/* 1390 */     return getClasspath().baseDir;
/*      */   }
/*      */ 
/*      */   public void setDefaultStratum(String paramString) {
/* 1394 */     this.defaultStratum = paramString;
/* 1395 */     if (paramString == null)
/* 1396 */       paramString = "";
/*      */     try
/*      */     {
/* 1399 */       JDWP.VirtualMachine.SetDefaultStratum.process(this.vm, paramString);
/*      */     }
/*      */     catch (JDWPException localJDWPException) {
/* 1402 */       throw localJDWPException.toJDIException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getDefaultStratum() {
/* 1407 */     return this.defaultStratum;
/*      */   }
/*      */ 
/*      */   ThreadGroup threadGroupForJDI() {
/* 1411 */     return this.threadGroupForJDI;
/*      */   }
/*      */ 
/*      */   private static class SoftObjectReference extends SoftReference<ObjectReferenceImpl> {
/*      */     int count;
/*      */     Long key;
/*      */ 
/*      */     SoftObjectReference(Long paramLong, ObjectReferenceImpl paramObjectReferenceImpl, ReferenceQueue<ObjectReferenceImpl> paramReferenceQueue) {
/* 1420 */       super(paramReferenceQueue);
/* 1421 */       this.count = 1;
/* 1422 */       this.key = paramLong;
/*      */     }
/*      */ 
/*      */     int count() {
/* 1426 */       return this.count;
/*      */     }
/*      */ 
/*      */     void incrementCount() {
/* 1430 */       this.count += 1;
/*      */     }
/*      */ 
/*      */     Long key() {
/* 1434 */       return this.key;
/*      */     }
/*      */ 
/*      */     ObjectReferenceImpl object() {
/* 1438 */       return (ObjectReferenceImpl)get();
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VirtualMachineImpl
 * JD-Core Version:    0.6.2
 */