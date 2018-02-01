/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.Locatable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.event.AccessWatchpointEvent;
/*     */ import com.sun.jdi.event.BreakpointEvent;
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.event.ClassUnloadEvent;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import com.sun.jdi.event.ExceptionEvent;
/*     */ import com.sun.jdi.event.MethodEntryEvent;
/*     */ import com.sun.jdi.event.MethodExitEvent;
/*     */ import com.sun.jdi.event.ModificationWatchpointEvent;
/*     */ import com.sun.jdi.event.MonitorContendedEnterEvent;
/*     */ import com.sun.jdi.event.MonitorContendedEnteredEvent;
/*     */ import com.sun.jdi.event.MonitorWaitEvent;
/*     */ import com.sun.jdi.event.MonitorWaitedEvent;
/*     */ import com.sun.jdi.event.StepEvent;
/*     */ import com.sun.jdi.event.ThreadDeathEvent;
/*     */ import com.sun.jdi.event.ThreadStartEvent;
/*     */ import com.sun.jdi.event.VMDeathEvent;
/*     */ import com.sun.jdi.event.VMDisconnectEvent;
/*     */ import com.sun.jdi.event.VMStartEvent;
/*     */ import com.sun.jdi.event.WatchpointEvent;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ public class EventSetImpl extends ArrayList<Event>
/*     */   implements EventSet
/*     */ {
/*     */   private static final long serialVersionUID = -4857338819787924570L;
/*     */   private VirtualMachineImpl vm;
/*     */   private Packet pkt;
/*     */   private byte suspendPolicy;
/*     */   private EventSetImpl internalEventSet;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  58 */     String str = "event set, policy:" + this.suspendPolicy + ", count:" + 
/*  58 */       size() + " = {";
/*  59 */     int i = 1;
/*  60 */     for (Event localEvent : this) {
/*  61 */       if (i == 0) {
/*  62 */         str = str + ", ";
/*     */       }
/*  64 */       str = str + localEvent.toString();
/*  65 */       i = 0;
/*     */     }
/*  67 */     str = str + "}";
/*  68 */     return str;
/*     */   }
/*     */ 
/*     */   EventSetImpl(VirtualMachine paramVirtualMachine, Packet paramPacket)
/*     */   {
/* 570 */     this.vm = ((VirtualMachineImpl)paramVirtualMachine);
/*     */ 
/* 572 */     this.pkt = paramPacket;
/*     */   }
/*     */ 
/*     */   EventSetImpl(VirtualMachine paramVirtualMachine, byte paramByte)
/*     */   {
/* 579 */     this(paramVirtualMachine, null);
/* 580 */     this.suspendPolicy = 0;
/* 581 */     switch (paramByte) {
/*     */     case 100:
/* 583 */       addEvent(new VMDisconnectEventImpl());
/* 584 */       break;
/*     */     default:
/* 587 */       throw new InternalException("Bad singleton event code");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addEvent(EventImpl paramEventImpl)
/*     */   {
/* 594 */     super.add(paramEventImpl);
/*     */   }
/*     */ 
/*     */   synchronized void build()
/*     */   {
/* 604 */     if (this.pkt == null) {
/* 605 */       return;
/*     */     }
/* 607 */     PacketStream localPacketStream = new PacketStream(this.vm, this.pkt);
/* 608 */     JDWP.Event.Composite localComposite = new JDWP.Event.Composite(this.vm, localPacketStream);
/* 609 */     this.suspendPolicy = localComposite.suspendPolicy;
/* 610 */     if ((this.vm.traceFlags & 0x4) != 0) {
/* 611 */       switch (this.suspendPolicy) {
/*     */       case 2:
/* 613 */         this.vm.printTrace("EventSet: SUSPEND_ALL");
/* 614 */         break;
/*     */       case 1:
/* 617 */         this.vm.printTrace("EventSet: SUSPEND_EVENT_THREAD");
/* 618 */         break;
/*     */       case 0:
/* 621 */         this.vm.printTrace("EventSet: SUSPEND_NONE");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 626 */     ThreadReference localThreadReference = null;
/* 627 */     for (int i = 0; i < localComposite.events.length; i++) {
/* 628 */       EventImpl localEventImpl = createEvent(localComposite.events[i]);
/* 629 */       if ((this.vm.traceFlags & 0x4) != 0) {
/*     */         try {
/* 631 */           this.vm.printTrace("Event: " + localEventImpl);
/*     */         }
/*     */         catch (VMDisconnectedException localVMDisconnectedException)
/*     */         {
/*     */         }
/*     */       }
/* 637 */       switch (1.$SwitchMap$com$sun$tools$jdi$EventDestination[localEventImpl.destination().ordinal()])
/*     */       {
/*     */       case 1:
/* 643 */         if (((localEventImpl instanceof ThreadedEventImpl)) && (this.suspendPolicy == 1))
/*     */         {
/* 645 */           localThreadReference = ((ThreadedEventImpl)localEventImpl).thread(); } break;
/*     */       case 2:
/* 649 */         addEvent(localEventImpl);
/* 650 */         break;
/*     */       case 3:
/* 652 */         if (this.internalEventSet == null) {
/* 653 */           this.internalEventSet = new EventSetImpl(this.vm, null);
/*     */         }
/* 655 */         this.internalEventSet.addEvent(localEventImpl);
/* 656 */         break;
/*     */       default:
/* 658 */         throw new InternalException("Invalid event destination");
/*     */       }
/*     */     }
/* 661 */     this.pkt = null;
/*     */ 
/* 664 */     if (super.size() == 0)
/*     */     {
/* 667 */       if (this.suspendPolicy == 2)
/* 668 */         this.vm.resume();
/* 669 */       else if (this.suspendPolicy == 1)
/*     */       {
/* 671 */         if (localThreadReference != null) {
/* 672 */           localThreadReference.resume();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 677 */       this.suspendPolicy = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   EventSet userFilter()
/*     */   {
/* 687 */     return this;
/*     */   }
/*     */ 
/*     */   EventSet internalFilter()
/*     */   {
/* 694 */     return this.internalEventSet;
/*     */   }
/*     */ 
/*     */   EventImpl createEvent(JDWP.Event.Composite.Events paramEvents) {
/* 698 */     JDWP.Event.Composite.Events.EventsCommon localEventsCommon = paramEvents.aEventsCommon;
/* 699 */     switch (paramEvents.eventKind) {
/*     */     case 6:
/* 701 */       return new ThreadStartEventImpl((JDWP.Event.Composite.Events.ThreadStart)localEventsCommon);
/*     */     case 7:
/* 705 */       return new ThreadDeathEventImpl((JDWP.Event.Composite.Events.ThreadDeath)localEventsCommon);
/*     */     case 4:
/* 709 */       return new ExceptionEventImpl((JDWP.Event.Composite.Events.Exception)localEventsCommon);
/*     */     case 2:
/* 713 */       return new BreakpointEventImpl((JDWP.Event.Composite.Events.Breakpoint)localEventsCommon);
/*     */     case 40:
/* 717 */       return new MethodEntryEventImpl((JDWP.Event.Composite.Events.MethodEntry)localEventsCommon);
/*     */     case 41:
/* 721 */       return new MethodExitEventImpl((JDWP.Event.Composite.Events.MethodExit)localEventsCommon);
/*     */     case 42:
/* 725 */       return new MethodExitEventImpl((JDWP.Event.Composite.Events.MethodExitWithReturnValue)localEventsCommon);
/*     */     case 20:
/* 729 */       return new AccessWatchpointEventImpl((JDWP.Event.Composite.Events.FieldAccess)localEventsCommon);
/*     */     case 21:
/* 733 */       return new ModificationWatchpointEventImpl((JDWP.Event.Composite.Events.FieldModification)localEventsCommon);
/*     */     case 1:
/* 737 */       return new StepEventImpl((JDWP.Event.Composite.Events.SingleStep)localEventsCommon);
/*     */     case 8:
/* 741 */       return new ClassPrepareEventImpl((JDWP.Event.Composite.Events.ClassPrepare)localEventsCommon);
/*     */     case 9:
/* 745 */       return new ClassUnloadEventImpl((JDWP.Event.Composite.Events.ClassUnload)localEventsCommon);
/*     */     case 43:
/* 749 */       return new MonitorContendedEnterEventImpl((JDWP.Event.Composite.Events.MonitorContendedEnter)localEventsCommon);
/*     */     case 44:
/* 753 */       return new MonitorContendedEnteredEventImpl((JDWP.Event.Composite.Events.MonitorContendedEntered)localEventsCommon);
/*     */     case 45:
/* 757 */       return new MonitorWaitEventImpl((JDWP.Event.Composite.Events.MonitorWait)localEventsCommon);
/*     */     case 46:
/* 761 */       return new MonitorWaitedEventImpl((JDWP.Event.Composite.Events.MonitorWaited)localEventsCommon);
/*     */     case 90:
/* 765 */       return new VMStartEventImpl((JDWP.Event.Composite.Events.VMStart)localEventsCommon);
/*     */     case 99:
/* 769 */       return new VMDeathEventImpl((JDWP.Event.Composite.Events.VMDeath)localEventsCommon);
/*     */     }
/*     */ 
/* 774 */     System.err.println("Ignoring event cmd " + paramEvents.eventKind + " from the VM");
/*     */ 
/* 776 */     return null;
/*     */   }
/*     */ 
/*     */   public VirtualMachine virtualMachine()
/*     */   {
/* 781 */     return this.vm;
/*     */   }
/*     */ 
/*     */   public int suspendPolicy() {
/* 785 */     return EventRequestManagerImpl.JDWPtoJDISuspendPolicy(this.suspendPolicy);
/*     */   }
/*     */ 
/*     */   private ThreadReference eventThread() {
/* 789 */     for (Event localEvent : this) {
/* 790 */       if ((localEvent instanceof ThreadedEventImpl)) {
/* 791 */         return ((ThreadedEventImpl)localEvent).thread();
/*     */       }
/*     */     }
/* 794 */     return null;
/*     */   }
/*     */ 
/*     */   public void resume() {
/* 798 */     switch (suspendPolicy()) {
/*     */     case 2:
/* 800 */       this.vm.resume();
/* 801 */       break;
/*     */     case 1:
/* 803 */       ThreadReference localThreadReference = eventThread();
/* 804 */       if (localThreadReference == null) {
/* 805 */         throw new InternalException("Inconsistent suspend policy");
/*     */       }
/* 807 */       localThreadReference.resume();
/* 808 */       break;
/*     */     case 0:
/* 811 */       break;
/*     */     default:
/* 813 */       throw new InternalException("Invalid suspend policy");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<Event> iterator() {
/* 818 */     return new Itr();
/*     */   }
/*     */ 
/*     */   public EventIterator eventIterator() {
/* 822 */     return new Itr();
/*     */   }
/*     */ 
/*     */   public Spliterator<Event> spliterator()
/*     */   {
/* 856 */     return Spliterators.spliterator(this, 1);
/*     */   }
/*     */ 
/*     */   public boolean add(Event paramEvent)
/*     */   {
/* 862 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean remove(Object paramObject) {
/* 865 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean addAll(Collection<? extends Event> paramCollection) {
/* 868 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean removeAll(Collection<?> paramCollection) {
/* 871 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean retainAll(Collection<?> paramCollection) {
/* 874 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public void clear() {
/* 877 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   class AccessWatchpointEventImpl extends EventSetImpl.WatchpointEventImpl
/*     */     implements AccessWatchpointEvent
/*     */   {
/*     */     AccessWatchpointEventImpl(JDWP.Event.Composite.Events.FieldAccess arg2)
/*     */     {
/* 526 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location, localEventsCommon.refTypeTag, localEventsCommon.typeID, localEventsCommon.fieldID, localEventsCommon.object);
/*     */     }
/*     */ 
/*     */     String eventName()
/*     */     {
/* 531 */       return "AccessWatchpoint";
/*     */     }
/*     */   }
/*     */ 
/*     */   class BreakpointEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements BreakpointEvent
/*     */   {
/*     */     BreakpointEventImpl(JDWP.Event.Composite.Events.Breakpoint arg2)
/*     */     {
/* 220 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 224 */       return "BreakpointEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class ClassPrepareEventImpl extends EventSetImpl.ThreadedEventImpl
/*     */     implements ClassPrepareEvent
/*     */   {
/*     */     private ReferenceType referenceType;
/*     */ 
/*     */     ClassPrepareEventImpl(JDWP.Event.Composite.Events.ClassPrepare arg2)
/*     */     {
/* 367 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread);
/* 368 */       this.referenceType = this.vm.referenceType(localEventsCommon.typeID, localEventsCommon.refTypeTag, localEventsCommon.signature);
/*     */ 
/* 370 */       ((ReferenceTypeImpl)this.referenceType).setStatus(localEventsCommon.status);
/*     */     }
/*     */ 
/*     */     public ReferenceType referenceType() {
/* 374 */       return this.referenceType;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 378 */       return "ClassPrepareEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class ClassUnloadEventImpl extends EventSetImpl.EventImpl implements ClassUnloadEvent {
/*     */     private String classSignature;
/*     */ 
/*     */     ClassUnloadEventImpl(JDWP.Event.Composite.Events.ClassUnload arg2) {
/* 386 */       super(localEventsCommon, localEventsCommon.requestID);
/* 387 */       this.classSignature = localEventsCommon.signature;
/*     */     }
/*     */ 
/*     */     public String className()
/*     */     {
/* 392 */       return this.classSignature.substring(1, this.classSignature.length() - 1)
/* 392 */         .replace('/', '.');
/*     */     }
/*     */ 
/*     */     public String classSignature()
/*     */     {
/* 396 */       return this.classSignature;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 400 */       return "ClassUnloadEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class EventImpl extends MirrorImpl
/*     */     implements Event
/*     */   {
/*     */     private final byte eventCmd;
/*     */     private final int requestID;
/*     */     private final EventRequest request;
/*     */ 
/*     */     protected EventImpl(JDWP.Event.Composite.Events.EventsCommon paramInt, int arg3)
/*     */     {
/*  83 */       super();
/*  84 */       this.eventCmd = paramInt.eventKind();
/*     */       int i;
/*  85 */       this.requestID = i;
/*     */ 
/*  87 */       EventRequestManagerImpl localEventRequestManagerImpl = EventSetImpl.this.vm
/*  87 */         .eventRequestManagerImpl();
/*  88 */       this.request = localEventRequestManagerImpl.request(this.eventCmd, i);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/*  95 */       return this == paramObject;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/*  99 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     protected EventImpl(byte arg2)
/*     */     {
/* 106 */       super();
/*     */       byte b;
/* 107 */       this.eventCmd = b;
/* 108 */       this.requestID = 0;
/* 109 */       this.request = null;
/*     */     }
/*     */ 
/*     */     public EventRequest request() {
/* 113 */       return this.request;
/*     */     }
/*     */ 
/*     */     int requestID() {
/* 117 */       return this.requestID;
/*     */     }
/*     */ 
/*     */     EventDestination destination()
/*     */     {
/* 137 */       if (this.requestID == 0)
/*     */       {
/* 141 */         return EventDestination.CLIENT_EVENT;
/*     */       }
/*     */ 
/* 145 */       if (this.request == null)
/*     */       {
/* 147 */         EventRequestManagerImpl localEventRequestManagerImpl = this.vm.getInternalEventRequestManager();
/* 148 */         if (localEventRequestManagerImpl.request(this.eventCmd, this.requestID) != null)
/*     */         {
/* 150 */           return EventDestination.INTERNAL_EVENT;
/*     */         }
/* 152 */         return EventDestination.UNKNOWN_EVENT;
/*     */       }
/*     */ 
/* 156 */       if (this.request.isEnabled()) {
/* 157 */         return EventDestination.CLIENT_EVENT;
/*     */       }
/* 159 */       return EventDestination.UNKNOWN_EVENT;
/*     */     }
/*     */ 
/*     */     abstract String eventName();
/*     */ 
/*     */     public String toString() {
/* 165 */       return eventName();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ExceptionEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements ExceptionEvent
/*     */   {
/*     */     private ObjectReference exception;
/*     */     private Location catchLocation;
/*     */ 
/*     */     ExceptionEventImpl(JDWP.Event.Composite.Events.Exception arg2)
/*     */     {
/* 410 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/* 411 */       this.exception = localEventsCommon.exception;
/* 412 */       this.catchLocation = localEventsCommon.catchLocation;
/*     */     }
/*     */ 
/*     */     public ObjectReference exception() {
/* 416 */       return this.exception;
/*     */     }
/*     */ 
/*     */     public Location catchLocation() {
/* 420 */       return this.catchLocation;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 424 */       return "ExceptionEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   public class Itr
/*     */     implements EventIterator
/*     */   {
/* 829 */     int cursor = 0;
/*     */ 
/*     */     public Itr() {  } 
/* 832 */     public boolean hasNext() { return this.cursor != EventSetImpl.this.size(); }
/*     */ 
/*     */     public Event next()
/*     */     {
/*     */       try {
/* 837 */         Event localEvent = (Event)EventSetImpl.this.get(this.cursor);
/* 838 */         this.cursor += 1;
/* 839 */         return localEvent; } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */       }
/* 841 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public Event nextEvent()
/*     */     {
/* 846 */       return next();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 850 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class LocatableEventImpl extends EventSetImpl.ThreadedEventImpl
/*     */     implements Locatable
/*     */   {
/*     */     private Location location;
/*     */ 
/*     */     LocatableEventImpl(JDWP.Event.Composite.Events.EventsCommon paramInt, int paramThreadReference, ThreadReference paramLocation, Location arg5)
/*     */     {
/* 195 */       super(paramInt, paramThreadReference, paramLocation);
/*     */       Object localObject;
/* 196 */       this.location = localObject;
/*     */     }
/*     */ 
/*     */     public Location location() {
/* 200 */       return this.location;
/*     */     }
/*     */ 
/*     */     public Method method()
/*     */     {
/* 207 */       return this.location.method();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 213 */       return eventName() + "@" + (
/* 212 */         location() == null ? " null" : location().toString()) + " in thread " + 
/* 213 */         thread().name();
/*     */     }
/*     */   }
/*     */ 
/*     */   class MethodEntryEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements MethodEntryEvent
/*     */   {
/*     */     MethodEntryEventImpl(JDWP.Event.Composite.Events.MethodEntry arg2)
/*     */     {
/* 241 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 245 */       return "MethodEntryEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class MethodExitEventImpl extends EventSetImpl.LocatableEventImpl implements MethodExitEvent
/*     */   {
/* 251 */     private Value returnVal = null;
/*     */ 
/*     */     MethodExitEventImpl(JDWP.Event.Composite.Events.MethodExit arg2) {
/* 254 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/*     */     }
/*     */ 
/*     */     MethodExitEventImpl(JDWP.Event.Composite.Events.MethodExitWithReturnValue arg2) {
/* 258 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/* 259 */       this.returnVal = localEventsCommon.value;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 263 */       return "MethodExitEvent";
/*     */     }
/*     */ 
/*     */     public Value returnValue() {
/* 267 */       if (!this.vm.canGetMethodReturnValues()) {
/* 268 */         throw new UnsupportedOperationException("target does not support return values in MethodExit events");
/*     */       }
/*     */ 
/* 271 */       return this.returnVal;
/*     */     }
/*     */   }
/*     */ 
/*     */   class ModificationWatchpointEventImpl extends EventSetImpl.WatchpointEventImpl
/*     */     implements ModificationWatchpointEvent
/*     */   {
/*     */     Value newValue;
/*     */ 
/*     */     ModificationWatchpointEventImpl(JDWP.Event.Composite.Events.FieldModification arg2)
/*     */     {
/* 541 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location, localEventsCommon.refTypeTag, localEventsCommon.typeID, localEventsCommon.fieldID, localEventsCommon.object);
/*     */ 
/* 543 */       this.newValue = localEventsCommon.valueToBe;
/*     */     }
/*     */ 
/*     */     public Value valueToBe() {
/* 547 */       return this.newValue;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 551 */       return "ModificationWatchpoint";
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorContendedEnterEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements MonitorContendedEnterEvent
/*     */   {
/* 278 */     private ObjectReference monitor = null;
/*     */ 
/*     */     MonitorContendedEnterEventImpl(JDWP.Event.Composite.Events.MonitorContendedEnter arg2) {
/* 281 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/* 282 */       this.monitor = localEventsCommon.object;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 286 */       return "MonitorContendedEnter";
/*     */     }
/*     */ 
/*     */     public ObjectReference monitor() {
/* 290 */       return this.monitor;
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorContendedEnteredEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements MonitorContendedEnteredEvent
/*     */   {
/* 297 */     private ObjectReference monitor = null;
/*     */ 
/*     */     MonitorContendedEnteredEventImpl(JDWP.Event.Composite.Events.MonitorContendedEntered arg2) {
/* 300 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/* 301 */       this.monitor = localEventsCommon.object;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 305 */       return "MonitorContendedEntered";
/*     */     }
/*     */ 
/*     */     public ObjectReference monitor() {
/* 309 */       return this.monitor;
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorWaitEventImpl extends EventSetImpl.LocatableEventImpl implements MonitorWaitEvent
/*     */   {
/* 316 */     private ObjectReference monitor = null;
/*     */     private long timeout;
/*     */ 
/*     */     MonitorWaitEventImpl(JDWP.Event.Composite.Events.MonitorWait arg2) {
/* 320 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/* 321 */       this.monitor = localEventsCommon.object;
/* 322 */       this.timeout = localEventsCommon.timeout;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 326 */       return "MonitorWait";
/*     */     }
/*     */ 
/*     */     public ObjectReference monitor() {
/* 330 */       return this.monitor;
/*     */     }
/*     */ 
/*     */     public long timeout() {
/* 334 */       return this.timeout;
/*     */     }
/*     */   }
/*     */ 
/*     */   class MonitorWaitedEventImpl extends EventSetImpl.LocatableEventImpl implements MonitorWaitedEvent {
/* 340 */     private ObjectReference monitor = null;
/*     */     private boolean timed_out;
/*     */ 
/*     */     MonitorWaitedEventImpl(JDWP.Event.Composite.Events.MonitorWaited arg2) {
/* 344 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/* 345 */       this.monitor = localEventsCommon.object;
/* 346 */       this.timed_out = localEventsCommon.timed_out;
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 350 */       return "MonitorWaited";
/*     */     }
/*     */ 
/*     */     public ObjectReference monitor() {
/* 354 */       return this.monitor;
/*     */     }
/*     */ 
/*     */     public boolean timedout() {
/* 358 */       return this.timed_out;
/*     */     }
/*     */   }
/*     */ 
/*     */   class StepEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements StepEvent
/*     */   {
/*     */     StepEventImpl(JDWP.Event.Composite.Events.SingleStep arg2)
/*     */     {
/* 230 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread, localEventsCommon.location);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 234 */       return "StepEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class ThreadDeathEventImpl extends EventSetImpl.ThreadedEventImpl
/*     */     implements ThreadDeathEvent
/*     */   {
/*     */     ThreadDeathEventImpl(JDWP.Event.Composite.Events.ThreadDeath arg2)
/*     */     {
/* 431 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 435 */       return "ThreadDeathEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class ThreadStartEventImpl extends EventSetImpl.ThreadedEventImpl implements ThreadStartEvent
/*     */   {
/*     */     ThreadStartEventImpl(JDWP.Event.Composite.Events.ThreadStart arg2) {
/* 442 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 446 */       return "ThreadStartEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class ThreadedEventImpl extends EventSetImpl.EventImpl
/*     */   {
/*     */     private ThreadReference thread;
/*     */ 
/*     */     ThreadedEventImpl(JDWP.Event.Composite.Events.EventsCommon paramInt, int paramThreadReference, ThreadReference arg4)
/*     */     {
/* 175 */       super(paramInt, paramThreadReference);
/*     */       Object localObject;
/* 176 */       this.thread = localObject;
/*     */     }
/*     */ 
/*     */     public ThreadReference thread() {
/* 180 */       return this.thread;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 184 */       return eventName() + " in thread " + this.thread.name();
/*     */     }
/*     */   }
/*     */ 
/*     */   class VMDeathEventImpl extends EventSetImpl.EventImpl
/*     */     implements VMDeathEvent
/*     */   {
/*     */     VMDeathEventImpl(JDWP.Event.Composite.Events.VMDeath arg2)
/*     */     {
/* 464 */       super(localEventsCommon, localEventsCommon.requestID);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 468 */       return "VMDeathEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class VMDisconnectEventImpl extends EventSetImpl.EventImpl implements VMDisconnectEvent
/*     */   {
/*     */     VMDisconnectEventImpl()
/*     */     {
/* 476 */       super((byte)100);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 480 */       return "VMDisconnectEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   class VMStartEventImpl extends EventSetImpl.ThreadedEventImpl
/*     */     implements VMStartEvent
/*     */   {
/*     */     VMStartEventImpl(JDWP.Event.Composite.Events.VMStart arg2)
/*     */     {
/* 453 */       super(localEventsCommon, localEventsCommon.requestID, localEventsCommon.thread);
/*     */     }
/*     */ 
/*     */     String eventName() {
/* 457 */       return "VMStartEvent";
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class WatchpointEventImpl extends EventSetImpl.LocatableEventImpl
/*     */     implements WatchpointEvent
/*     */   {
/*     */     private final ReferenceTypeImpl refType;
/*     */     private final long fieldID;
/*     */     private final ObjectReference object;
/* 489 */     private Field field = null;
/*     */ 
/*     */     WatchpointEventImpl(JDWP.Event.Composite.Events.EventsCommon paramInt, int paramThreadReference, ThreadReference paramLocation, Location paramByte, byte paramLong1, long arg7, long arg9, ObjectReference arg11)
/*     */     {
/* 496 */       super(paramInt, paramThreadReference, paramLocation, paramByte);
/* 497 */       this.refType = this.vm.referenceType(???, paramLong1);
/*     */       Object localObject1;
/* 498 */       this.fieldID = localObject1;
/*     */       Object localObject2;
/* 499 */       this.object = localObject2;
/*     */     }
/*     */ 
/*     */     public Field field() {
/* 503 */       if (this.field == null) {
/* 504 */         this.field = this.refType.getFieldMirror(this.fieldID);
/*     */       }
/* 506 */       return this.field;
/*     */     }
/*     */ 
/*     */     public ObjectReference object() {
/* 510 */       return this.object;
/*     */     }
/*     */ 
/*     */     public Value valueCurrent() {
/* 514 */       if (this.object == null) {
/* 515 */         return this.refType.getValue(field());
/*     */       }
/* 517 */       return this.object.getValue(field());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.EventSetImpl
 * JD-Core Version:    0.6.2
 */