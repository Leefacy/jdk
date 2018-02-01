/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.InvocationException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ abstract class InvokableTypeImpl extends ReferenceTypeImpl
/*     */ {
/*     */   InvokableTypeImpl(VirtualMachine paramVirtualMachine, long paramLong)
/*     */   {
/*  60 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */ 
/*     */   public final Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt)
/*     */     throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException
/*     */   {
/* 102 */     validateMirror(paramThreadReference);
/* 103 */     validateMirror(paramMethod);
/* 104 */     validateMirrorsOrNulls(paramList);
/* 105 */     MethodImpl localMethodImpl = (MethodImpl)paramMethod;
/* 106 */     ThreadReferenceImpl localThreadReferenceImpl = (ThreadReferenceImpl)paramThreadReference;
/* 107 */     validateMethodInvocation(localMethodImpl);
/* 108 */     List localList = localMethodImpl.validateAndPrepareArgumentsForInvoke(paramList);
/* 109 */     ValueImpl[] arrayOfValueImpl = (ValueImpl[])localList.toArray(new ValueImpl[0]);
/*     */     InvocationResult localInvocationResult;
/*     */     try
/*     */     {
/* 112 */       PacketStream localPacketStream = sendInvokeCommand(localThreadReferenceImpl, localMethodImpl, arrayOfValueImpl, paramInt);
/* 113 */       localInvocationResult = waitForReply(localPacketStream);
/*     */     } catch (JDWPException localJDWPException) {
/* 115 */       if (localJDWPException.errorCode() == 10) {
/* 116 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 118 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 125 */     if ((paramInt & 0x1) == 0) {
/* 126 */       this.vm.notifySuspend();
/*     */     }
/* 128 */     if (localInvocationResult.getException() != null) {
/* 129 */       throw new InvocationException(localInvocationResult.getException());
/*     */     }
/* 131 */     return localInvocationResult.getResult();
/*     */   }
/*     */ 
/*     */   boolean isAssignableTo(ReferenceType paramReferenceType)
/*     */   {
/* 137 */     ClassTypeImpl localClassTypeImpl = (ClassTypeImpl)superclass();
/* 138 */     if (equals(paramReferenceType))
/* 139 */       return true;
/* 140 */     if ((localClassTypeImpl != null) && (localClassTypeImpl.isAssignableTo(paramReferenceType))) {
/* 141 */       return true;
/*     */     }
/* 143 */     List localList = interfaces();
/* 144 */     Iterator localIterator = localList.iterator();
/* 145 */     while (localIterator.hasNext()) {
/* 146 */       InterfaceTypeImpl localInterfaceTypeImpl = (InterfaceTypeImpl)localIterator.next();
/* 147 */       if (localInterfaceTypeImpl.isAssignableTo(paramReferenceType)) {
/* 148 */         return true;
/*     */       }
/*     */     }
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   final void addVisibleMethods(Map<String, Method> paramMap, Set<InterfaceType> paramSet)
/*     */   {
/* 162 */     Iterator localIterator = interfaces().iterator();
/* 163 */     while (localIterator.hasNext()) {
/* 164 */       localObject = (InterfaceTypeImpl)localIterator.next();
/* 165 */       if (!paramSet.contains(localObject)) {
/* 166 */         ((InterfaceTypeImpl)localObject).addVisibleMethods(paramMap, paramSet);
/* 167 */         paramSet.add(localObject);
/*     */       }
/*     */     }
/* 170 */     Object localObject = (ClassTypeImpl)superclass();
/* 171 */     if (localObject != null) {
/* 172 */       ((ClassTypeImpl)localObject).addVisibleMethods(paramMap, paramSet);
/*     */     }
/* 174 */     addToMethodMap(paramMap, methods());
/*     */   }
/*     */ 
/*     */   final void addInterfaces(List<InterfaceType> paramList) {
/* 178 */     List localList = interfaces();
/* 179 */     paramList.addAll(interfaces());
/* 180 */     Iterator localIterator = localList.iterator();
/* 181 */     while (localIterator.hasNext()) {
/* 182 */       localObject = (InterfaceTypeImpl)localIterator.next();
/* 183 */       ((InterfaceTypeImpl)localObject).addInterfaces(paramList);
/*     */     }
/* 185 */     Object localObject = (ClassTypeImpl)superclass();
/* 186 */     if (localObject != null)
/* 187 */       ((ClassTypeImpl)localObject).addInterfaces(paramList);
/*     */   }
/*     */ 
/*     */   final List<InterfaceType> getAllInterfaces()
/*     */   {
/* 196 */     ArrayList localArrayList = new ArrayList();
/* 197 */     addInterfaces(localArrayList);
/* 198 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public final List<Method> allMethods()
/*     */   {
/* 207 */     ArrayList localArrayList = new ArrayList(methods());
/* 208 */     ClassType localClassType = superclass();
/* 209 */     while (localClassType != null) {
/* 210 */       localArrayList.addAll(localClassType.methods());
/* 211 */       localClassType = localClassType.superclass();
/*     */     }
/*     */ 
/* 217 */     for (InterfaceType localInterfaceType : getAllInterfaces()) {
/* 218 */       localArrayList.addAll(localInterfaceType.methods());
/*     */     }
/* 220 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   final List<ReferenceType> inheritedTypes()
/*     */   {
/* 225 */     ArrayList localArrayList = new ArrayList();
/* 226 */     if (superclass() != null) {
/* 227 */       localArrayList.add(0, superclass());
/*     */     }
/* 229 */     for (ReferenceType localReferenceType : interfaces()) {
/* 230 */       localArrayList.add(localReferenceType);
/*     */     }
/* 232 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private PacketStream sendInvokeCommand(ThreadReferenceImpl paramThreadReferenceImpl, MethodImpl paramMethodImpl, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*     */   {
/* 239 */     CommandSender localCommandSender = getInvokeMethodSender(paramThreadReferenceImpl, paramMethodImpl, paramArrayOfValueImpl, paramInt);
/*     */     PacketStream localPacketStream;
/* 241 */     if ((paramInt & 0x1) != 0)
/* 242 */       localPacketStream = paramThreadReferenceImpl.sendResumingCommand(localCommandSender);
/*     */     else {
/* 244 */       localPacketStream = this.vm.sendResumingCommand(localCommandSender);
/*     */     }
/* 246 */     return localPacketStream;
/*     */   }
/*     */ 
/*     */   private void validateMethodInvocation(Method paramMethod)
/*     */     throws InvalidTypeException, InvocationException
/*     */   {
/* 252 */     if (!canInvoke(paramMethod)) {
/* 253 */       throw new IllegalArgumentException("Invalid method");
/*     */     }
/*     */ 
/* 258 */     if (!paramMethod.isStatic())
/* 259 */       throw new IllegalArgumentException("Cannot invoke instance method on a class/interface type");
/* 260 */     if (paramMethod.isStaticInitializer())
/* 261 */       throw new IllegalArgumentException("Cannot invoke static initializer");
/*     */   }
/*     */ 
/*     */   abstract CommandSender getInvokeMethodSender(ThreadReferenceImpl paramThreadReferenceImpl, MethodImpl paramMethodImpl, ValueImpl[] paramArrayOfValueImpl, int paramInt);
/*     */ 
/*     */   abstract InvocationResult waitForReply(PacketStream paramPacketStream)
/*     */     throws JDWPException;
/*     */ 
/*     */   abstract ClassType superclass();
/*     */ 
/*     */   abstract List<InterfaceType> interfaces();
/*     */ 
/*     */   abstract boolean canInvoke(Method paramMethod);
/*     */ 
/*     */   static abstract interface InvocationResult
/*     */   {
/*     */     public abstract ObjectReferenceImpl getException();
/*     */ 
/*     */     public abstract ValueImpl getResult();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.InvokableTypeImpl
 * JD-Core Version:    0.6.2
 */