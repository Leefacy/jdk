/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidStackFrameException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class StackFrameImpl extends MirrorImpl
/*     */   implements StackFrame, ThreadListener
/*     */ {
/*  44 */   private boolean isValid = true;
/*     */   private final ThreadReferenceImpl thread;
/*     */   private final long id;
/*     */   private final Location location;
/*  49 */   private Map<String, LocalVariable> visibleVariables = null;
/*  50 */   private ObjectReference thisObject = null;
/*     */ 
/*     */   StackFrameImpl(VirtualMachine paramVirtualMachine, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, Location paramLocation)
/*     */   {
/*  54 */     super(paramVirtualMachine);
/*  55 */     this.thread = paramThreadReferenceImpl;
/*  56 */     this.id = paramLong;
/*  57 */     this.location = paramLocation;
/*  58 */     paramThreadReferenceImpl.addListener(this);
/*     */   }
/*     */ 
/*     */   public boolean threadResumable(ThreadAction paramThreadAction)
/*     */   {
/*  67 */     synchronized (this.vm.state()) {
/*  68 */       if (this.isValid) {
/*  69 */         this.isValid = false;
/*  70 */         return false;
/*     */       }
/*  72 */       throw new InternalException("Invalid stack frame thread listener");
/*     */     }
/*     */   }
/*     */ 
/*     */   void validateStackFrame()
/*     */   {
/*  79 */     if (!this.isValid)
/*  80 */       throw new InvalidStackFrameException("Thread has been resumed");
/*     */   }
/*     */ 
/*     */   public Location location()
/*     */   {
/*  89 */     validateStackFrame();
/*  90 */     return this.location;
/*     */   }
/*     */ 
/*     */   public ThreadReference thread()
/*     */   {
/*  98 */     validateStackFrame();
/*  99 */     return this.thread;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 103 */     if ((paramObject != null) && ((paramObject instanceof StackFrameImpl))) {
/* 104 */       StackFrameImpl localStackFrameImpl = (StackFrameImpl)paramObject;
/*     */ 
/* 108 */       return (this.id == localStackFrameImpl.id) && 
/* 106 */         (thread().equals(localStackFrameImpl.thread())) && 
/* 107 */         (location().equals(localStackFrameImpl.location())) && 
/* 108 */         (super
/* 108 */         .equals(paramObject));
/*     */     }
/*     */ 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 115 */     return (thread().hashCode() << 4) + (int)this.id;
/*     */   }
/*     */ 
/*     */   public ObjectReference thisObject() {
/* 119 */     validateStackFrame();
/* 120 */     MethodImpl localMethodImpl = (MethodImpl)this.location.method();
/* 121 */     if ((localMethodImpl.isStatic()) || (localMethodImpl.isNative())) {
/* 122 */       return null;
/*     */     }
/* 124 */     if (this.thisObject == null)
/*     */     {
/*     */       PacketStream localPacketStream;
/* 128 */       synchronized (this.vm.state()) {
/* 129 */         validateStackFrame();
/*     */ 
/* 131 */         localPacketStream = JDWP.StackFrame.ThisObject.enqueueCommand(this.vm, this.thread, this.id);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 136 */         this.thisObject = 
/* 137 */           JDWP.StackFrame.ThisObject.waitForReply(this.vm, localPacketStream).objectThis;
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 139 */         switch (localJDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/*     */         case 30:
/* 143 */           throw new InvalidStackFrameException();
/*     */         }
/*     */       }
/* 145 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 150 */     return this.thisObject;
/*     */   }
/*     */ 
/*     */   private void createVisibleVariables()
/*     */     throws AbsentInformationException
/*     */   {
/* 158 */     if (this.visibleVariables == null) {
/* 159 */       List localList = this.location.method().variables();
/* 160 */       HashMap localHashMap = new HashMap(localList.size());
/*     */ 
/* 162 */       for (LocalVariable localLocalVariable1 : localList) {
/* 163 */         String str = localLocalVariable1.name();
/* 164 */         if (localLocalVariable1.isVisible(this)) {
/* 165 */           LocalVariable localLocalVariable2 = (LocalVariable)localHashMap.get(str);
/* 166 */           if ((localLocalVariable2 == null) || 
/* 167 */             (((LocalVariableImpl)localLocalVariable1)
/* 167 */             .hides(localLocalVariable2)))
/*     */           {
/* 168 */             localHashMap.put(str, localLocalVariable1);
/*     */           }
/*     */         }
/*     */       }
/* 172 */       this.visibleVariables = localHashMap;
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<LocalVariable> visibleVariables()
/*     */     throws AbsentInformationException
/*     */   {
/* 181 */     validateStackFrame();
/* 182 */     createVisibleVariables();
/* 183 */     ArrayList localArrayList = new ArrayList(this.visibleVariables.values());
/* 184 */     Collections.sort(localArrayList);
/* 185 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public LocalVariable visibleVariableByName(String paramString)
/*     */     throws AbsentInformationException
/*     */   {
/* 193 */     validateStackFrame();
/* 194 */     createVisibleVariables();
/* 195 */     return (LocalVariable)this.visibleVariables.get(paramString);
/*     */   }
/*     */ 
/*     */   public Value getValue(LocalVariable paramLocalVariable) {
/* 199 */     ArrayList localArrayList = new ArrayList(1);
/* 200 */     localArrayList.add(paramLocalVariable);
/* 201 */     return (Value)getValues(localArrayList).get(paramLocalVariable);
/*     */   }
/*     */ 
/*     */   public Map<LocalVariable, Value> getValues(List<? extends LocalVariable> paramList) {
/* 205 */     validateStackFrame();
/* 206 */     validateMirrors(paramList);
/*     */ 
/* 208 */     int i = paramList.size();
/* 209 */     JDWP.StackFrame.GetValues.SlotInfo[] arrayOfSlotInfo = new JDWP.StackFrame.GetValues.SlotInfo[i];
/*     */ 
/* 212 */     for (int j = 0; j < i; j++) {
/* 213 */       LocalVariableImpl localLocalVariableImpl1 = (LocalVariableImpl)paramList.get(j);
/* 214 */       if (!localLocalVariableImpl1.isVisible(this)) {
/* 215 */         throw new IllegalArgumentException(localLocalVariableImpl1.name() + " is not valid at this frame location");
/*     */       }
/*     */ 
/* 218 */       arrayOfSlotInfo[j] = new JDWP.StackFrame.GetValues.SlotInfo(localLocalVariableImpl1.slot(), 
/* 219 */         (byte)localLocalVariableImpl1
/* 219 */         .signature().charAt(0));
/*     */     }
/*     */     PacketStream localPacketStream;
/* 225 */     synchronized (this.vm.state()) {
/* 226 */       validateStackFrame();
/* 227 */       localPacketStream = JDWP.StackFrame.GetValues.enqueueCommand(this.vm, this.thread, this.id, arrayOfSlotInfo);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 233 */       ??? = JDWP.StackFrame.GetValues.waitForReply(this.vm, localPacketStream).values;
/*     */     } catch (JDWPException localJDWPException) {
/* 235 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/*     */       case 30:
/* 239 */         throw new InvalidStackFrameException();
/*     */       }
/*     */     }
/* 241 */     throw localJDWPException.toJDIException();
/*     */ 
/* 245 */     if (i != ???.length) {
/* 246 */       throw new InternalException("Wrong number of values returned from target VM");
/*     */     }
/*     */ 
/* 249 */     HashMap localHashMap = new HashMap(i);
/* 250 */     for (int k = 0; k < i; k++) {
/* 251 */       LocalVariableImpl localLocalVariableImpl2 = (LocalVariableImpl)paramList.get(k);
/* 252 */       localHashMap.put(localLocalVariableImpl2, ???[k]);
/*     */     }
/* 254 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public void setValue(LocalVariable paramLocalVariable, Value paramValue)
/*     */     throws InvalidTypeException, ClassNotLoadedException
/*     */   {
/* 260 */     validateStackFrame();
/* 261 */     validateMirror(paramLocalVariable);
/* 262 */     validateMirrorOrNull(paramValue);
/*     */ 
/* 264 */     LocalVariableImpl localLocalVariableImpl = (LocalVariableImpl)paramLocalVariable;
/* 265 */     ValueImpl localValueImpl = (ValueImpl)paramValue;
/*     */ 
/* 267 */     if (!localLocalVariableImpl.isVisible(this)) {
/* 268 */       throw new IllegalArgumentException(localLocalVariableImpl.name() + " is not valid at this frame location");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 274 */       localValueImpl = ValueImpl.prepareForAssignment(localValueImpl, localLocalVariableImpl);
/*     */ 
/* 276 */       JDWP.StackFrame.SetValues.SlotInfo[] arrayOfSlotInfo = new JDWP.StackFrame.SetValues.SlotInfo[1];
/*     */ 
/* 278 */       arrayOfSlotInfo[0] = new JDWP.StackFrame.SetValues.SlotInfo(localLocalVariableImpl
/* 279 */         .slot(), localValueImpl);
/*     */       PacketStream localPacketStream;
/* 284 */       synchronized (this.vm.state()) {
/* 285 */         validateStackFrame();
/*     */ 
/* 287 */         localPacketStream = JDWP.StackFrame.SetValues.enqueueCommand(this.vm, this.thread, this.id, arrayOfSlotInfo);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 292 */         JDWP.StackFrame.SetValues.waitForReply(this.vm, localPacketStream);
/*     */       } catch (JDWPException localJDWPException) {
/* 294 */         switch (localJDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/*     */         case 30:
/* 298 */           throw new InvalidStackFrameException();
/*     */         }
/*     */       }
/* 300 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */     catch (ClassNotLoadedException localClassNotLoadedException)
/*     */     {
/* 312 */       if (localValueImpl != null)
/* 313 */         throw localClassNotLoadedException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<Value> getArgumentValues()
/*     */   {
/* 319 */     validateStackFrame();
/* 320 */     MethodImpl localMethodImpl = (MethodImpl)this.location.method();
/* 321 */     List localList = localMethodImpl.argumentSignatures();
/* 322 */     int i = localList.size();
/* 323 */     JDWP.StackFrame.GetValues.SlotInfo[] arrayOfSlotInfo = new JDWP.StackFrame.GetValues.SlotInfo[i];
/*     */     int j;
/* 327 */     if (localMethodImpl.isStatic())
/* 328 */       j = 0;
/*     */     else {
/* 330 */       j = 1;
/*     */     }
/* 332 */     for (int k = 0; k < i; k++) {
/* 333 */       int m = ((String)localList.get(k)).charAt(0);
/* 334 */       arrayOfSlotInfo[k] = new JDWP.StackFrame.GetValues.SlotInfo(j++, (byte)m);
/* 335 */       if ((m == 74) || (m == 68))
/* 336 */         j++;
/*     */     }
/*     */     PacketStream localPacketStream;
/* 343 */     synchronized (this.vm.state()) {
/* 344 */       validateStackFrame();
/* 345 */       localPacketStream = JDWP.StackFrame.GetValues.enqueueCommand(this.vm, this.thread, this.id, arrayOfSlotInfo);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 350 */       ??? = JDWP.StackFrame.GetValues.waitForReply(this.vm, localPacketStream).values;
/*     */     } catch (JDWPException localJDWPException) {
/* 352 */       switch (localJDWPException.errorCode()) {
/*     */       case 10:
/*     */       case 13:
/*     */       case 30:
/* 356 */         throw new InvalidStackFrameException();
/*     */       }
/*     */     }
/* 358 */     throw localJDWPException.toJDIException();
/*     */ 
/* 362 */     if (i != ???.length) {
/* 363 */       throw new InternalException("Wrong number of values returned from target VM");
/*     */     }
/*     */ 
/* 366 */     return Arrays.asList((Value[])???);
/*     */   }
/*     */ 
/*     */   void pop() throws IncompatibleThreadStateException {
/* 370 */     validateStackFrame();
/*     */ 
/* 372 */     CommandSender local1 = new CommandSender()
/*     */     {
/*     */       public PacketStream send() {
/* 375 */         return JDWP.StackFrame.PopFrames.enqueueCommand(StackFrameImpl.this.vm, StackFrameImpl.this.thread, 
/* 376 */           StackFrameImpl.this.id);
/*     */       }
/*     */     };
/*     */     try {
/* 380 */       PacketStream localPacketStream = this.thread.sendResumingCommand(local1);
/* 381 */       JDWP.StackFrame.PopFrames.waitForReply(this.vm, localPacketStream);
/*     */     } catch (JDWPException localJDWPException) {
/* 383 */       switch (localJDWPException.errorCode()) {
/*     */       case 13:
/* 385 */         throw new IncompatibleThreadStateException("Thread not current or suspended");
/*     */       case 10:
/*     */       case 31: } 
/* 388 */     }throw new IncompatibleThreadStateException("zombie");
/*     */ 
/* 390 */     throw new InvalidStackFrameException("No more frames on the stack");
/*     */ 
/* 393 */     throw localJDWPException.toJDIException();
/*     */ 
/* 398 */     this.vm.state().freeze();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 402 */     return this.location.toString() + " in thread " + this.thread.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.StackFrameImpl
 * JD-Core Version:    0.6.2
 */