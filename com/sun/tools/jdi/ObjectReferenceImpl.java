/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.InvocationException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ObjectReferenceImpl extends ValueImpl
/*     */   implements ObjectReference, VMListener
/*     */ {
/*     */   protected long ref;
/*  37 */   private ReferenceType type = null;
/*  38 */   private int gcDisableCount = 0;
/*  39 */   boolean addedListener = false;
/*     */ 
/*  46 */   private static final Cache noInitCache = new Cache();
/*  47 */   private static final Cache markerCache = new Cache();
/*  48 */   private Cache cache = noInitCache;
/*     */ 
/*     */   private void disableCache() {
/*  51 */     synchronized (this.vm.state()) {
/*  52 */       this.cache = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void enableCache() {
/*  57 */     synchronized (this.vm.state()) {
/*  58 */       this.cache = markerCache;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Cache newCache()
/*     */   {
/*  64 */     return new Cache();
/*     */   }
/*     */ 
/*     */   protected Cache getCache() {
/*  68 */     synchronized (this.vm.state()) {
/*  69 */       if (this.cache == noInitCache) {
/*  70 */         if (this.vm.state().isSuspended())
/*     */         {
/*  73 */           enableCache();
/*     */         }
/*  75 */         else disableCache();
/*     */       }
/*     */ 
/*  78 */       if (this.cache == markerCache) {
/*  79 */         this.cache = newCache();
/*     */       }
/*  81 */       return this.cache;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ClassTypeImpl invokableReferenceType(Method paramMethod)
/*     */   {
/*  89 */     return (ClassTypeImpl)referenceType();
/*     */   }
/*     */ 
/*     */   ObjectReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  93 */     super(paramVirtualMachine);
/*     */ 
/*  95 */     this.ref = paramLong;
/*     */   }
/*     */ 
/*     */   protected String description() {
/*  99 */     return "ObjectReference " + uniqueID();
/*     */   }
/*     */ 
/*     */   public boolean vmSuspended(VMAction paramVMAction)
/*     */   {
/* 106 */     enableCache();
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean vmNotSuspended(VMAction paramVMAction)
/*     */   {
/* 112 */     synchronized (this.vm.state()) {
/* 113 */       if ((this.cache != null) && ((this.vm.traceFlags & 0x10) != 0)) {
/* 114 */         this.vm.printTrace("Clearing temporary cache for " + description());
/*     */       }
/* 116 */       disableCache();
/* 117 */       if (this.addedListener)
/*     */       {
/* 123 */         this.addedListener = false;
/* 124 */         return false;
/*     */       }
/* 126 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 132 */     if ((paramObject != null) && ((paramObject instanceof ObjectReferenceImpl))) {
/* 133 */       ObjectReferenceImpl localObjectReferenceImpl = (ObjectReferenceImpl)paramObject;
/*     */ 
/* 135 */       return (ref() == localObjectReferenceImpl.ref()) && 
/* 135 */         (super
/* 135 */         .equals(paramObject));
/*     */     }
/*     */ 
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 142 */     return (int)ref();
/*     */   }
/*     */ 
/*     */   public Type type() {
/* 146 */     return referenceType();
/*     */   }
/*     */ 
/*     */   public ReferenceType referenceType() {
/* 150 */     if (this.type == null) {
/*     */       try
/*     */       {
/* 153 */         JDWP.ObjectReference.ReferenceType localReferenceType = JDWP.ObjectReference.ReferenceType.process(this.vm, this);
/*     */ 
/* 154 */         this.type = this.vm.referenceType(localReferenceType.typeID, localReferenceType.refTypeTag);
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 157 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */     }
/* 160 */     return this.type;
/*     */   }
/*     */ 
/*     */   public Value getValue(Field paramField) {
/* 164 */     ArrayList localArrayList = new ArrayList(1);
/* 165 */     localArrayList.add(paramField);
/* 166 */     Map localMap = getValues(localArrayList);
/* 167 */     return (Value)localMap.get(paramField);
/*     */   }
/*     */ 
/*     */   public Map<Field, Value> getValues(List<? extends Field> paramList) {
/* 171 */     validateMirrors(paramList);
/*     */ 
/* 173 */     ArrayList localArrayList1 = new ArrayList(0);
/* 174 */     int i = paramList.size();
/* 175 */     ArrayList localArrayList2 = new ArrayList(i);
/*     */ 
/* 177 */     for (int j = 0; j < i; j++) {
/* 178 */       localObject2 = (Field)paramList.get(j);
/*     */ 
/* 181 */       ((ReferenceTypeImpl)referenceType()).validateFieldAccess((Field)localObject2);
/*     */ 
/* 186 */       if (((Field)localObject2).isStatic())
/* 187 */         localArrayList1.add(localObject2);
/*     */       else
/* 189 */         localArrayList2.add(localObject2);
/*     */     }
/*     */     Object localObject1;
/* 194 */     if (localArrayList1.size() > 0)
/* 195 */       localObject1 = referenceType().getValues(localArrayList1);
/*     */     else {
/* 197 */       localObject1 = new HashMap(i);
/*     */     }
/*     */ 
/* 200 */     i = localArrayList2.size();
/*     */ 
/* 202 */     Object localObject2 = new JDWP.ObjectReference.GetValues.Field[i];
/*     */ 
/* 204 */     for (int k = 0; k < i; k++) {
/* 205 */       FieldImpl localFieldImpl1 = (FieldImpl)localArrayList2.get(k);
/* 206 */       localObject2[k] = new JDWP.ObjectReference.GetValues.Field(localFieldImpl1
/* 207 */         .ref());
/*     */     }
/*     */     ValueImpl[] arrayOfValueImpl;
/*     */     try
/*     */     {
/* 212 */       arrayOfValueImpl = JDWP.ObjectReference.GetValues.process(this.vm, this, (JDWP.ObjectReference.GetValues.Field[])localObject2).values;
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 214 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 217 */     if (i != arrayOfValueImpl.length) {
/* 218 */       throw new InternalException("Wrong number of values returned from target VM");
/*     */     }
/*     */ 
/* 221 */     for (int m = 0; m < i; m++) {
/* 222 */       FieldImpl localFieldImpl2 = (FieldImpl)localArrayList2.get(m);
/* 223 */       ((Map)localObject1).put(localFieldImpl2, arrayOfValueImpl[m]);
/*     */     }
/*     */ 
/* 226 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void setValue(Field paramField, Value paramValue)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 232 */     validateMirror(paramField);
/* 233 */     validateMirrorOrNull(paramValue);
/*     */ 
/* 236 */     ((ReferenceTypeImpl)referenceType()).validateFieldSet(paramField);
/*     */     Object localObject;
/* 238 */     if (paramField.isStatic()) {
/* 239 */       localObject = referenceType();
/* 240 */       if ((localObject instanceof ClassType)) {
/* 241 */         ((ClassType)localObject).setValue(paramField, paramValue);
/* 242 */         return;
/*     */       }
/* 244 */       throw new IllegalArgumentException("Invalid type for static field set");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 250 */       localObject = new JDWP.ObjectReference.SetValues.FieldValue[1];
/*     */ 
/* 252 */       localObject[0] = new JDWP.ObjectReference.SetValues.FieldValue(((FieldImpl)paramField)
/* 253 */         .ref(), 
/* 255 */         ValueImpl.prepareForAssignment(paramValue, (FieldImpl)paramField));
/*     */       try
/*     */       {
/* 258 */         JDWP.ObjectReference.SetValues.process(this.vm, this, (JDWP.ObjectReference.SetValues.FieldValue[])localObject);
/*     */       } catch (JDWPException localJDWPException) {
/* 260 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ClassNotLoadedException localClassNotLoadedException)
/*     */     {
/* 271 */       if (paramValue != null)
/* 272 */         throw localClassNotLoadedException;
/*     */     }
/*     */   }
/*     */ 
/*     */   void validateMethodInvocation(Method paramMethod, int paramInt)
/*     */     throws InvalidTypeException, InvocationException
/*     */   {
/* 284 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)paramMethod.declaringType();
/* 285 */     if (!localReferenceTypeImpl.isAssignableFrom(this)) {
/* 286 */       throw new IllegalArgumentException("Invalid method");
/*     */     }
/*     */ 
/* 289 */     if ((localReferenceTypeImpl instanceof ClassTypeImpl))
/* 290 */       validateClassMethodInvocation(paramMethod, paramInt);
/* 291 */     else if ((localReferenceTypeImpl instanceof InterfaceTypeImpl))
/* 292 */       validateIfaceMethodInvocation(paramMethod, paramInt);
/*     */     else
/* 294 */       throw new InvalidTypeException();
/*     */   }
/*     */ 
/*     */   void validateClassMethodInvocation(Method paramMethod, int paramInt)
/*     */     throws InvalidTypeException, InvocationException
/*     */   {
/* 302 */     ClassTypeImpl localClassTypeImpl1 = invokableReferenceType(paramMethod);
/*     */ 
/* 307 */     if (paramMethod.isConstructor()) {
/* 308 */       throw new IllegalArgumentException("Cannot invoke constructor");
/*     */     }
/*     */ 
/* 314 */     if ((isNonVirtual(paramInt)) && 
/* 315 */       (paramMethod.isAbstract()))
/* 316 */       throw new IllegalArgumentException("Abstract method");
/*     */     ClassTypeImpl localClassTypeImpl2;
/* 326 */     if (isNonVirtual(paramInt))
/*     */     {
/* 328 */       localClassTypeImpl2 = localClassTypeImpl1;
/*     */     }
/*     */     else
/*     */     {
/* 335 */       Method localMethod = localClassTypeImpl1.concreteMethodByName(paramMethod.name(), paramMethod
/* 336 */         .signature());
/*     */ 
/* 338 */       localClassTypeImpl2 = (ClassTypeImpl)localMethod.declaringType();
/*     */     }
/*     */   }
/*     */ 
/*     */   void validateIfaceMethodInvocation(Method paramMethod, int paramInt)
/*     */     throws InvalidTypeException, InvocationException
/*     */   {
/* 351 */     if ((isNonVirtual(paramInt)) && (!paramMethod.isDefault()))
/* 352 */       throw new IllegalArgumentException("Not a default method");
/*     */   }
/*     */ 
/*     */   PacketStream sendInvokeCommand(final ThreadReferenceImpl paramThreadReferenceImpl, final ClassTypeImpl paramClassTypeImpl, final MethodImpl paramMethodImpl, final ValueImpl[] paramArrayOfValueImpl, final int paramInt)
/*     */   {
/* 361 */     CommandSender local1 = new CommandSender()
/*     */     {
/*     */       public PacketStream send() {
/* 364 */         return JDWP.ObjectReference.InvokeMethod.enqueueCommand(ObjectReferenceImpl.this.vm, ObjectReferenceImpl.this, paramThreadReferenceImpl, paramClassTypeImpl, paramMethodImpl
/* 367 */           .ref(), paramArrayOfValueImpl, paramInt);
/*     */       }
/*     */     };
/*     */     PacketStream localPacketStream;
/* 372 */     if ((paramInt & 0x1) != 0)
/* 373 */       localPacketStream = paramThreadReferenceImpl.sendResumingCommand(local1);
/*     */     else {
/* 375 */       localPacketStream = this.vm.sendResumingCommand(local1);
/*     */     }
/* 377 */     return localPacketStream;
/*     */   }
/*     */ 
/*     */   public Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt)
/*     */     throws InvalidTypeException, IncompatibleThreadStateException, InvocationException, ClassNotLoadedException
/*     */   {
/* 386 */     validateMirror(paramThreadReference);
/* 387 */     validateMirror(paramMethod);
/* 388 */     validateMirrorsOrNulls(paramList);
/*     */ 
/* 390 */     MethodImpl localMethodImpl = (MethodImpl)paramMethod;
/* 391 */     ThreadReferenceImpl localThreadReferenceImpl = (ThreadReferenceImpl)paramThreadReference;
/*     */ 
/* 393 */     if (localMethodImpl.isStatic()) {
/* 394 */       if ((referenceType() instanceof InterfaceType)) {
/* 395 */         localObject = (InterfaceType)referenceType();
/* 396 */         return ((InterfaceType)localObject).invokeMethod(localThreadReferenceImpl, localMethodImpl, paramList, paramInt);
/* 397 */       }if ((referenceType() instanceof ClassType)) {
/* 398 */         localObject = (ClassType)referenceType();
/* 399 */         return ((ClassType)localObject).invokeMethod(localThreadReferenceImpl, localMethodImpl, paramList, paramInt);
/*     */       }
/* 401 */       throw new IllegalArgumentException("Invalid type for static method invocation");
/*     */     }
/*     */ 
/* 405 */     validateMethodInvocation(localMethodImpl, paramInt);
/*     */ 
/* 407 */     Object localObject = localMethodImpl.validateAndPrepareArgumentsForInvoke(paramList);
/*     */ 
/* 410 */     ValueImpl[] arrayOfValueImpl = (ValueImpl[])((List)localObject).toArray(new ValueImpl[0]);
/*     */     JDWP.ObjectReference.InvokeMethod localInvokeMethod;
/*     */     try {
/* 414 */       PacketStream localPacketStream = sendInvokeCommand(localThreadReferenceImpl, 
/* 414 */         invokableReferenceType(localMethodImpl), 
/* 414 */         localMethodImpl, arrayOfValueImpl, paramInt);
/*     */ 
/* 416 */       localInvokeMethod = JDWP.ObjectReference.InvokeMethod.waitForReply(this.vm, localPacketStream);
/*     */     } catch (JDWPException localJDWPException) {
/* 418 */       if (localJDWPException.errorCode() == 10) {
/* 419 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 421 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 429 */     if ((paramInt & 0x1) == 0) {
/* 430 */       this.vm.notifySuspend();
/*     */     }
/*     */ 
/* 433 */     if (localInvokeMethod.exception != null) {
/* 434 */       throw new InvocationException(localInvokeMethod.exception);
/*     */     }
/* 436 */     return localInvokeMethod.returnValue;
/*     */   }
/*     */ 
/*     */   public synchronized void disableCollection()
/*     */   {
/* 442 */     if (this.gcDisableCount == 0) {
/*     */       try {
/* 444 */         JDWP.ObjectReference.DisableCollection.process(this.vm, this);
/*     */       } catch (JDWPException localJDWPException) {
/* 446 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */     }
/* 449 */     this.gcDisableCount += 1;
/*     */   }
/*     */ 
/*     */   public synchronized void enableCollection()
/*     */   {
/* 454 */     this.gcDisableCount -= 1;
/*     */ 
/* 456 */     if (this.gcDisableCount == 0)
/*     */       try {
/* 458 */         JDWP.ObjectReference.EnableCollection.process(this.vm, this);
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 461 */         if (localJDWPException.errorCode() != 20) {
/* 462 */           throw localJDWPException.toJDIException();
/*     */         }
/* 464 */         return;
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean isCollected()
/*     */   {
/*     */     try {
/* 471 */       return JDWP.ObjectReference.IsCollected.process(this.vm, this).isCollected;
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 474 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public long uniqueID() {
/* 479 */     return ref();
/*     */   }
/*     */ 
/*     */   JDWP.ObjectReference.MonitorInfo jdwpMonitorInfo() throws IncompatibleThreadStateException
/*     */   {
/* 484 */     JDWP.ObjectReference.MonitorInfo localMonitorInfo = null;
/*     */     try
/*     */     {
/*     */       Cache localCache;
/* 490 */       synchronized (this.vm.state()) {
/* 491 */         localCache = getCache();
/*     */ 
/* 493 */         if (localCache != null) {
/* 494 */           localMonitorInfo = localCache.monitorInfo;
/*     */ 
/* 498 */           if ((localMonitorInfo == null) && (!this.vm.state().hasListener(this)))
/*     */           {
/* 505 */             this.vm.state().addListener(this);
/* 506 */             this.addedListener = true;
/*     */           }
/*     */         }
/*     */       }
/* 510 */       if (localMonitorInfo == null) {
/* 511 */         localMonitorInfo = JDWP.ObjectReference.MonitorInfo.process(this.vm, this);
/* 512 */         if (localCache != null) {
/* 513 */           localCache.monitorInfo = localMonitorInfo;
/* 514 */           if ((this.vm.traceFlags & 0x10) != 0)
/* 515 */             this.vm.printTrace("ObjectReference " + uniqueID() + " temporarily caching monitor info");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException)
/*     */     {
/* 521 */       if (localJDWPException.errorCode() == 13) {
/* 522 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 524 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 527 */     return localMonitorInfo;
/*     */   }
/*     */ 
/*     */   public List<ThreadReference> waitingThreads() throws IncompatibleThreadStateException {
/* 531 */     return Arrays.asList((ThreadReference[])jdwpMonitorInfo().waiters);
/*     */   }
/*     */ 
/*     */   public ThreadReference owningThread() throws IncompatibleThreadStateException {
/* 535 */     return jdwpMonitorInfo().owner;
/*     */   }
/*     */ 
/*     */   public int entryCount() throws IncompatibleThreadStateException {
/* 539 */     return jdwpMonitorInfo().entryCount;
/*     */   }
/*     */ 
/*     */   public List<ObjectReference> referringObjects(long paramLong)
/*     */   {
/* 544 */     if (!this.vm.canGetInstanceInfo()) {
/* 545 */       throw new UnsupportedOperationException("target does not support getting referring objects");
/*     */     }
/*     */ 
/* 549 */     if (paramLong < 0L) {
/* 550 */       throw new IllegalArgumentException("maxReferrers is less than zero: " + paramLong);
/*     */     }
/*     */ 
/* 554 */     int i = paramLong > 2147483647L ? 2147483647 : (int)paramLong;
/*     */     try
/*     */     {
/* 559 */       return Arrays.asList(
/* 560 */         (ObjectReference[])JDWP.ObjectReference.ReferringObjects.process(this.vm, this, i).referringObjects);
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 562 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */   }
/*     */ 
/*     */   long ref() {
/* 567 */     return this.ref;
/*     */   }
/*     */ 
/*     */   boolean isClassObject()
/*     */   {
/* 574 */     return referenceType().name().equals("java.lang.Class");
/*     */   }
/*     */ 
/*     */   ValueImpl prepareForAssignmentTo(ValueContainer paramValueContainer)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 581 */     validateAssignment(paramValueContainer);
/* 582 */     return this;
/*     */   }
/*     */ 
/*     */   void validateAssignment(ValueContainer paramValueContainer)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 596 */     if (paramValueContainer.signature().length() == 1) {
/* 597 */       throw new InvalidTypeException("Can't assign object value to primitive");
/*     */     }
/* 599 */     if ((paramValueContainer.signature().charAt(0) == '[') && 
/* 600 */       (type().signature().charAt(0) != '[')) {
/* 601 */       throw new InvalidTypeException("Can't assign non-array value to an array");
/*     */     }
/* 603 */     if ("void".equals(paramValueContainer.typeName())) {
/* 604 */       throw new InvalidTypeException("Can't assign object value to a void");
/*     */     }
/*     */ 
/* 608 */     ReferenceTypeImpl localReferenceTypeImpl1 = (ReferenceTypeImpl)paramValueContainer.type();
/* 609 */     ReferenceTypeImpl localReferenceTypeImpl2 = (ReferenceTypeImpl)referenceType();
/* 610 */     if (!localReferenceTypeImpl2.isAssignableTo(localReferenceTypeImpl1)) {
/* 611 */       JNITypeParser localJNITypeParser = new JNITypeParser(localReferenceTypeImpl1.signature());
/* 612 */       String str = localJNITypeParser.typeName();
/*     */ 
/* 614 */       throw new InvalidTypeException("Can't assign " + 
/* 614 */         type().name() + " to " + str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 621 */     return "instance of " + referenceType().name() + "(id=" + uniqueID() + ")";
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 625 */     return 76;
/*     */   }
/*     */ 
/*     */   private static boolean isNonVirtual(int paramInt) {
/* 629 */     return (paramInt & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   protected static class Cache
/*     */   {
/*  43 */     JDWP.ObjectReference.MonitorInfo monitorInfo = null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ObjectReferenceImpl
 * JD-Core Version:    0.6.2
 */