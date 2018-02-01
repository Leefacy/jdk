/*      */ package com.sun.tools.jdi;
/*      */ 
/*      */ import com.sun.jdi.Location;
/*      */ import java.util.List;
/*      */ 
/*      */ class JDWP
/*      */ {
/*      */   static class ArrayReference
/*      */   {
/*      */     static final int COMMAND_SET = 13;
/*      */ 
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */       final List<?> values;
/*      */ 
/*      */       static GetValues process(VirtualMachineImpl paramVirtualMachineImpl, ArrayReferenceImpl paramArrayReferenceImpl, int paramInt1, int paramInt2)
/*      */         throws JDWPException
/*      */       {
/* 6153 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayReferenceImpl, paramInt1, paramInt2);
/* 6154 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ArrayReferenceImpl paramArrayReferenceImpl, int paramInt1, int paramInt2)
/*      */       {
/* 6161 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 13, 2);
/* 6162 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6163 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ArrayReference.GetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6165 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6166 */           localPacketStream.vm.printTrace("Sending:                 arrayObject(ArrayReferenceImpl): " + (paramArrayReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramArrayReferenceImpl.ref()).toString()));
/*      */         }
/* 6168 */         localPacketStream.writeObjectRef(paramArrayReferenceImpl.ref());
/* 6169 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6170 */           localPacketStream.vm.printTrace("Sending:                 firstIndex(int): " + paramInt1);
/*      */         }
/* 6172 */         localPacketStream.writeInt(paramInt1);
/* 6173 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6174 */           localPacketStream.vm.printTrace("Sending:                 length(int): " + paramInt2);
/*      */         }
/* 6176 */         localPacketStream.writeInt(paramInt2);
/* 6177 */         localPacketStream.send();
/* 6178 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static GetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6183 */         paramPacketStream.waitForReply();
/* 6184 */         return new GetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private GetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6196 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6197 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ArrayReference.GetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 6199 */         this.values = paramPacketStream.readArrayRegion();
/* 6200 */         if (paramVirtualMachineImpl.traceReceives)
/* 6201 */           paramVirtualMachineImpl.printReceiveTrace(4, "values(List<?>): " + this.values);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Length
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final int arrayLength;
/*      */ 
/*      */       static Length process(VirtualMachineImpl paramVirtualMachineImpl, ArrayReferenceImpl paramArrayReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 6100 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayReferenceImpl);
/* 6101 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ArrayReferenceImpl paramArrayReferenceImpl)
/*      */       {
/* 6106 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 13, 1);
/* 6107 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6108 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ArrayReference.Length" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6110 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6111 */           localPacketStream.vm.printTrace("Sending:                 arrayObject(ArrayReferenceImpl): " + (paramArrayReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramArrayReferenceImpl.ref()).toString()));
/*      */         }
/* 6113 */         localPacketStream.writeObjectRef(paramArrayReferenceImpl.ref());
/* 6114 */         localPacketStream.send();
/* 6115 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Length waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6120 */         paramPacketStream.waitForReply();
/* 6121 */         return new Length(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Length(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6131 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6132 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ArrayReference.Length" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 6134 */         this.arrayLength = paramPacketStream.readInt();
/* 6135 */         if (paramVirtualMachineImpl.traceReceives)
/* 6136 */           paramVirtualMachineImpl.printReceiveTrace(4, "arrayLength(int): " + this.arrayLength);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */ 
/*      */       static SetValues process(VirtualMachineImpl paramVirtualMachineImpl, ArrayReferenceImpl paramArrayReferenceImpl, int paramInt, ValueImpl[] paramArrayOfValueImpl)
/*      */         throws JDWPException
/*      */       {
/* 6222 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayReferenceImpl, paramInt, paramArrayOfValueImpl);
/* 6223 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ArrayReferenceImpl paramArrayReferenceImpl, int paramInt, ValueImpl[] paramArrayOfValueImpl)
/*      */       {
/* 6230 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 13, 3);
/* 6231 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6232 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ArrayReference.SetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6234 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6235 */           localPacketStream.vm.printTrace("Sending:                 arrayObject(ArrayReferenceImpl): " + (paramArrayReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramArrayReferenceImpl.ref()).toString()));
/*      */         }
/* 6237 */         localPacketStream.writeObjectRef(paramArrayReferenceImpl.ref());
/* 6238 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6239 */           localPacketStream.vm.printTrace("Sending:                 firstIndex(int): " + paramInt);
/*      */         }
/* 6241 */         localPacketStream.writeInt(paramInt);
/* 6242 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6243 */           localPacketStream.vm.printTrace("Sending:                 values(ValueImpl[]): ");
/*      */         }
/* 6245 */         localPacketStream.writeInt(paramArrayOfValueImpl.length);
/* 6246 */         for (int i = 0; i < paramArrayOfValueImpl.length; i++) {
/* 6247 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6248 */             localPacketStream.vm.printTrace("Sending:                     values[i](ValueImpl): " + paramArrayOfValueImpl[i]);
/*      */           }
/* 6250 */           localPacketStream.writeUntaggedValue(paramArrayOfValueImpl[i]);
/*      */         }
/* 6252 */         localPacketStream.send();
/* 6253 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6258 */         paramPacketStream.waitForReply();
/* 6259 */         return new SetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6264 */         if (paramVirtualMachineImpl.traceReceives)
/* 6265 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ArrayReference.SetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ArrayType
/*      */   {
/*      */     static final int COMMAND_SET = 4;
/*      */ 
/*      */     static class NewInstance
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final ObjectReferenceImpl newArray;
/*      */ 
/*      */       static NewInstance process(VirtualMachineImpl paramVirtualMachineImpl, ArrayTypeImpl paramArrayTypeImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 3552 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayTypeImpl, paramInt);
/* 3553 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ArrayTypeImpl paramArrayTypeImpl, int paramInt)
/*      */       {
/* 3559 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 4, 1);
/* 3560 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3561 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ArrayType.NewInstance" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3563 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3564 */           localPacketStream.vm.printTrace("Sending:                 arrType(ArrayTypeImpl): " + (paramArrayTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramArrayTypeImpl.ref()).toString()));
/*      */         }
/* 3566 */         localPacketStream.writeClassRef(paramArrayTypeImpl.ref());
/* 3567 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3568 */           localPacketStream.vm.printTrace("Sending:                 length(int): " + paramInt);
/*      */         }
/* 3570 */         localPacketStream.writeInt(paramInt);
/* 3571 */         localPacketStream.send();
/* 3572 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static NewInstance waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3577 */         paramPacketStream.waitForReply();
/* 3578 */         return new NewInstance(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private NewInstance(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3588 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3589 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ArrayType.NewInstance" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3591 */         this.newArray = paramPacketStream.readTaggedObjectReference();
/* 3592 */         if (paramVirtualMachineImpl.traceReceives)
/* 3593 */           paramVirtualMachineImpl.printReceiveTrace(4, "newArray(ObjectReferenceImpl): " + (this.newArray == null ? "NULL" : new StringBuilder().append("ref=").append(this.newArray.ref()).toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ClassLoaderReference
/*      */   {
/*      */     static final int COMMAND_SET = 14;
/*      */ 
/*      */     static class VisibleClasses
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final ClassInfo[] classes;
/*      */ 
/*      */       static VisibleClasses process(VirtualMachineImpl paramVirtualMachineImpl, ClassLoaderReferenceImpl paramClassLoaderReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 6297 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramClassLoaderReferenceImpl);
/* 6298 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassLoaderReferenceImpl paramClassLoaderReferenceImpl)
/*      */       {
/* 6303 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 14, 1);
/* 6304 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6305 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ClassLoaderReference.VisibleClasses" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6307 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6308 */           localPacketStream.vm.printTrace("Sending:                 classLoaderObject(ClassLoaderReferenceImpl): " + (paramClassLoaderReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassLoaderReferenceImpl.ref()).toString()));
/*      */         }
/* 6310 */         localPacketStream.writeObjectRef(paramClassLoaderReferenceImpl.ref());
/* 6311 */         localPacketStream.send();
/* 6312 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static VisibleClasses waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6317 */         paramPacketStream.waitForReply();
/* 6318 */         return new VisibleClasses(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private VisibleClasses(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6353 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6354 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ClassLoaderReference.VisibleClasses" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 6356 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6357 */           paramVirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/* 6359 */         int i = paramPacketStream.readInt();
/* 6360 */         this.classes = new ClassInfo[i];
/* 6361 */         for (int j = 0; j < i; j++) {
/* 6362 */           if (paramVirtualMachineImpl.traceReceives) {
/* 6363 */             paramVirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/* 6365 */           this.classes[j] = new ClassInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */         final long typeID;
/*      */ 
/*      */         private ClassInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 6335 */           this.refTypeTag = paramPacketStream.readByte();
/* 6336 */           if (paramVirtualMachineImpl.traceReceives) {
/* 6337 */             paramVirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/* 6339 */           this.typeID = paramPacketStream.readClassRef();
/* 6340 */           if (paramVirtualMachineImpl.traceReceives)
/* 6341 */             paramVirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ClassObjectReference
/*      */   {
/*      */     static final int COMMAND_SET = 17;
/*      */ 
/*      */     static class ReflectedType
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final byte refTypeTag;
/*      */       final long typeID;
/*      */ 
/*      */       static ReflectedType process(VirtualMachineImpl paramVirtualMachineImpl, ClassObjectReferenceImpl paramClassObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 7362 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramClassObjectReferenceImpl);
/* 7363 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassObjectReferenceImpl paramClassObjectReferenceImpl)
/*      */       {
/* 7368 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 17, 1);
/* 7369 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7370 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ClassObjectReference.ReflectedType" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 7372 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7373 */           localPacketStream.vm.printTrace("Sending:                 classObject(ClassObjectReferenceImpl): " + (paramClassObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 7375 */         localPacketStream.writeObjectRef(paramClassObjectReferenceImpl.ref());
/* 7376 */         localPacketStream.send();
/* 7377 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ReflectedType waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 7382 */         paramPacketStream.waitForReply();
/* 7383 */         return new ReflectedType(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ReflectedType(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 7399 */         if (paramVirtualMachineImpl.traceReceives) {
/* 7400 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ClassObjectReference.ReflectedType" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 7402 */         this.refTypeTag = paramPacketStream.readByte();
/* 7403 */         if (paramVirtualMachineImpl.traceReceives) {
/* 7404 */           paramVirtualMachineImpl.printReceiveTrace(4, "refTypeTag(byte): " + this.refTypeTag);
/*      */         }
/* 7406 */         this.typeID = paramPacketStream.readClassRef();
/* 7407 */         if (paramVirtualMachineImpl.traceReceives)
/* 7408 */           paramVirtualMachineImpl.printReceiveTrace(4, "typeID(long): ref=" + this.typeID);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ClassStatus
/*      */   {
/*      */     static final int VERIFIED = 1;
/*      */     static final int PREPARED = 2;
/*      */     static final int INITIALIZED = 4;
/*      */     static final int ERROR = 8;
/*      */   }
/*      */ 
/*      */   static class ClassType
/*      */   {
/*      */     static final int COMMAND_SET = 3;
/*      */ 
/*      */     static class InvokeMethod
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */       final ValueImpl returnValue;
/*      */       final ObjectReferenceImpl exception;
/*      */ 
/*      */       static InvokeMethod process(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 3320 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramClassTypeImpl, paramThreadReferenceImpl, paramLong, paramArrayOfValueImpl, paramInt);
/* 3321 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */       {
/* 3330 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 3, 3);
/* 3331 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3332 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ClassType.InvokeMethod" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3334 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3335 */           localPacketStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + (paramClassTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassTypeImpl.ref()).toString()));
/*      */         }
/* 3337 */         localPacketStream.writeClassRef(paramClassTypeImpl.ref());
/* 3338 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3339 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 3341 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 3342 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3343 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 3345 */         localPacketStream.writeMethodRef(paramLong);
/* 3346 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3347 */           localPacketStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 3349 */         localPacketStream.writeInt(paramArrayOfValueImpl.length);
/* 3350 */         for (int i = 0; i < paramArrayOfValueImpl.length; i++) {
/* 3351 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3352 */             localPacketStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + paramArrayOfValueImpl[i]);
/*      */           }
/* 3354 */           localPacketStream.writeValue(paramArrayOfValueImpl[i]);
/*      */         }
/* 3356 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3357 */           localPacketStream.vm.printTrace("Sending:                 options(int): " + paramInt);
/*      */         }
/* 3359 */         localPacketStream.writeInt(paramInt);
/* 3360 */         localPacketStream.send();
/* 3361 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static InvokeMethod waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3366 */         paramPacketStream.waitForReply();
/* 3367 */         return new InvokeMethod(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private InvokeMethod(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3382 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3383 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ClassType.InvokeMethod" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3385 */         this.returnValue = paramPacketStream.readValue();
/* 3386 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3387 */           paramVirtualMachineImpl.printReceiveTrace(4, "returnValue(ValueImpl): " + this.returnValue);
/*      */         }
/* 3389 */         this.exception = paramPacketStream.readTaggedObjectReference();
/* 3390 */         if (paramVirtualMachineImpl.traceReceives)
/* 3391 */           paramVirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + (this.exception == null ? "NULL" : new StringBuilder().append("ref=").append(this.exception.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class NewInstance
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */       final ObjectReferenceImpl newObject;
/*      */       final ObjectReferenceImpl exception;
/*      */ 
/*      */       static NewInstance process(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 3460 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramClassTypeImpl, paramThreadReferenceImpl, paramLong, paramArrayOfValueImpl, paramInt);
/* 3461 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */       {
/* 3470 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 3, 4);
/* 3471 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3472 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ClassType.NewInstance" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3474 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3475 */           localPacketStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + (paramClassTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassTypeImpl.ref()).toString()));
/*      */         }
/* 3477 */         localPacketStream.writeClassRef(paramClassTypeImpl.ref());
/* 3478 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3479 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 3481 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 3482 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3483 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 3485 */         localPacketStream.writeMethodRef(paramLong);
/* 3486 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3487 */           localPacketStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 3489 */         localPacketStream.writeInt(paramArrayOfValueImpl.length);
/* 3490 */         for (int i = 0; i < paramArrayOfValueImpl.length; i++) {
/* 3491 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3492 */             localPacketStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + paramArrayOfValueImpl[i]);
/*      */           }
/* 3494 */           localPacketStream.writeValue(paramArrayOfValueImpl[i]);
/*      */         }
/* 3496 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3497 */           localPacketStream.vm.printTrace("Sending:                 options(int): " + paramInt);
/*      */         }
/* 3499 */         localPacketStream.writeInt(paramInt);
/* 3500 */         localPacketStream.send();
/* 3501 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static NewInstance waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3506 */         paramPacketStream.waitForReply();
/* 3507 */         return new NewInstance(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private NewInstance(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3523 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3524 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ClassType.NewInstance" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3526 */         this.newObject = paramPacketStream.readTaggedObjectReference();
/* 3527 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3528 */           paramVirtualMachineImpl.printReceiveTrace(4, "newObject(ObjectReferenceImpl): " + (this.newObject == null ? "NULL" : new StringBuilder().append("ref=").append(this.newObject.ref()).toString()));
/*      */         }
/* 3530 */         this.exception = paramPacketStream.readTaggedObjectReference();
/* 3531 */         if (paramVirtualMachineImpl.traceReceives)
/* 3532 */           paramVirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + (this.exception == null ? "NULL" : new StringBuilder().append("ref=").append(this.exception.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */ 
/*      */       static SetValues process(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl, FieldValue[] paramArrayOfFieldValue)
/*      */         throws JDWPException
/*      */       {
/* 3211 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramClassTypeImpl, paramArrayOfFieldValue);
/* 3212 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl, FieldValue[] paramArrayOfFieldValue)
/*      */       {
/* 3218 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 3, 2);
/* 3219 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3220 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ClassType.SetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3222 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3223 */           localPacketStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + (paramClassTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassTypeImpl.ref()).toString()));
/*      */         }
/* 3225 */         localPacketStream.writeClassRef(paramClassTypeImpl.ref());
/* 3226 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3227 */           localPacketStream.vm.printTrace("Sending:                 values(FieldValue[]): ");
/*      */         }
/* 3229 */         localPacketStream.writeInt(paramArrayOfFieldValue.length);
/* 3230 */         for (int i = 0; i < paramArrayOfFieldValue.length; i++) {
/* 3231 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3232 */             localPacketStream.vm.printTrace("Sending:                     values[i](FieldValue): ");
/*      */           }
/* 3234 */           paramArrayOfFieldValue[i].write(localPacketStream);
/*      */         }
/* 3236 */         localPacketStream.send();
/* 3237 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3242 */         paramPacketStream.waitForReply();
/* 3243 */         return new SetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3248 */         if (paramVirtualMachineImpl.traceReceives)
/* 3249 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ClassType.SetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */ 
/*      */       static class FieldValue
/*      */       {
/*      */         final long fieldID;
/*      */         final ValueImpl value;
/*      */ 
/*      */         FieldValue(long paramLong, ValueImpl paramValueImpl)
/*      */         {
/* 3191 */           this.fieldID = paramLong;
/* 3192 */           this.value = paramValueImpl;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 3196 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3197 */             paramPacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 3199 */           paramPacketStream.writeFieldRef(this.fieldID);
/* 3200 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3201 */             paramPacketStream.vm.printTrace("Sending:                     value(ValueImpl): " + this.value);
/*      */           }
/* 3203 */           paramPacketStream.writeUntaggedValue(this.value);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Superclass
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final ClassTypeImpl superclass;
/*      */ 
/*      */       static Superclass process(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 3120 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramClassTypeImpl);
/* 3121 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassTypeImpl paramClassTypeImpl)
/*      */       {
/* 3126 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 3, 1);
/* 3127 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3128 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ClassType.Superclass" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3130 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3131 */           localPacketStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + (paramClassTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassTypeImpl.ref()).toString()));
/*      */         }
/* 3133 */         localPacketStream.writeClassRef(paramClassTypeImpl.ref());
/* 3134 */         localPacketStream.send();
/* 3135 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Superclass waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3140 */         paramPacketStream.waitForReply();
/* 3141 */         return new Superclass(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Superclass(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3151 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3152 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ClassType.Superclass" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3154 */         this.superclass = paramVirtualMachineImpl.classType(paramPacketStream.readClassRef());
/* 3155 */         if (paramVirtualMachineImpl.traceReceives)
/* 3156 */           paramVirtualMachineImpl.printReceiveTrace(4, "superclass(ClassTypeImpl): " + (this.superclass == null ? "NULL" : new StringBuilder().append("ref=").append(this.superclass.ref()).toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Error
/*      */   {
/*      */     static final int NONE = 0;
/*      */     static final int INVALID_THREAD = 10;
/*      */     static final int INVALID_THREAD_GROUP = 11;
/*      */     static final int INVALID_PRIORITY = 12;
/*      */     static final int THREAD_NOT_SUSPENDED = 13;
/*      */     static final int THREAD_SUSPENDED = 14;
/*      */     static final int THREAD_NOT_ALIVE = 15;
/*      */     static final int INVALID_OBJECT = 20;
/*      */     static final int INVALID_CLASS = 21;
/*      */     static final int CLASS_NOT_PREPARED = 22;
/*      */     static final int INVALID_METHODID = 23;
/*      */     static final int INVALID_LOCATION = 24;
/*      */     static final int INVALID_FIELDID = 25;
/*      */     static final int INVALID_FRAMEID = 30;
/*      */     static final int NO_MORE_FRAMES = 31;
/*      */     static final int OPAQUE_FRAME = 32;
/*      */     static final int NOT_CURRENT_FRAME = 33;
/*      */     static final int TYPE_MISMATCH = 34;
/*      */     static final int INVALID_SLOT = 35;
/*      */     static final int DUPLICATE = 40;
/*      */     static final int NOT_FOUND = 41;
/*      */     static final int INVALID_MONITOR = 50;
/*      */     static final int NOT_MONITOR_OWNER = 51;
/*      */     static final int INTERRUPT = 52;
/*      */     static final int INVALID_CLASS_FORMAT = 60;
/*      */     static final int CIRCULAR_CLASS_DEFINITION = 61;
/*      */     static final int FAILS_VERIFICATION = 62;
/*      */     static final int ADD_METHOD_NOT_IMPLEMENTED = 63;
/*      */     static final int SCHEMA_CHANGE_NOT_IMPLEMENTED = 64;
/*      */     static final int INVALID_TYPESTATE = 65;
/*      */     static final int HIERARCHY_CHANGE_NOT_IMPLEMENTED = 66;
/*      */     static final int DELETE_METHOD_NOT_IMPLEMENTED = 67;
/*      */     static final int UNSUPPORTED_VERSION = 68;
/*      */     static final int NAMES_DONT_MATCH = 69;
/*      */     static final int CLASS_MODIFIERS_CHANGE_NOT_IMPLEMENTED = 70;
/*      */     static final int METHOD_MODIFIERS_CHANGE_NOT_IMPLEMENTED = 71;
/*      */     static final int NOT_IMPLEMENTED = 99;
/*      */     static final int NULL_POINTER = 100;
/*      */     static final int ABSENT_INFORMATION = 101;
/*      */     static final int INVALID_EVENT_TYPE = 102;
/*      */     static final int ILLEGAL_ARGUMENT = 103;
/*      */     static final int OUT_OF_MEMORY = 110;
/*      */     static final int ACCESS_DENIED = 111;
/*      */     static final int VM_DEAD = 112;
/*      */     static final int INTERNAL = 113;
/*      */     static final int UNATTACHED_THREAD = 115;
/*      */     static final int INVALID_TAG = 500;
/*      */     static final int ALREADY_INVOKING = 502;
/*      */     static final int INVALID_INDEX = 503;
/*      */     static final int INVALID_LENGTH = 504;
/*      */     static final int INVALID_STRING = 506;
/*      */     static final int INVALID_CLASS_LOADER = 507;
/*      */     static final int INVALID_ARRAY = 508;
/*      */     static final int TRANSPORT_LOAD = 509;
/*      */     static final int TRANSPORT_INIT = 510;
/*      */     static final int NATIVE_METHOD = 511;
/*      */     static final int INVALID_COUNT = 512;
/*      */   }
/*      */ 
/*      */   static class Event
/*      */   {
/*      */     static final int COMMAND_SET = 64;
/*      */ 
/*      */     static class Composite
/*      */     {
/*      */       static final int COMMAND = 100;
/*      */       final byte suspendPolicy;
/*      */       final Events[] events;
/*      */ 
/*      */       Composite(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 8578 */         if (paramVirtualMachineImpl.traceReceives) {
/* 8579 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.Event.Composite" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 8581 */         this.suspendPolicy = paramPacketStream.readByte();
/* 8582 */         if (paramVirtualMachineImpl.traceReceives) {
/* 8583 */           paramVirtualMachineImpl.printReceiveTrace(4, "suspendPolicy(byte): " + this.suspendPolicy);
/*      */         }
/* 8585 */         if (paramVirtualMachineImpl.traceReceives) {
/* 8586 */           paramVirtualMachineImpl.printReceiveTrace(4, "events(Events[]): ");
/*      */         }
/* 8588 */         int i = paramPacketStream.readInt();
/* 8589 */         this.events = new Events[i];
/* 8590 */         for (int j = 0; j < i; j++) {
/* 8591 */           if (paramVirtualMachineImpl.traceReceives) {
/* 8592 */             paramVirtualMachineImpl.printReceiveTrace(5, "events[i](Events): ");
/*      */           }
/* 8594 */           this.events[j] = new Events(paramVirtualMachineImpl, paramPacketStream);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class Events
/*      */       {
/*      */         final byte eventKind;
/*      */         EventsCommon aEventsCommon;
/*      */ 
/*      */         Events(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 7520 */           this.eventKind = paramPacketStream.readByte();
/* 7521 */           if (paramVirtualMachineImpl.traceReceives) {
/* 7522 */             paramVirtualMachineImpl.printReceiveTrace(5, "eventKind(byte): " + this.eventKind);
/*      */           }
/* 7524 */           switch (this.eventKind) {
/*      */           case 90:
/* 7526 */             this.aEventsCommon = new VMStart(paramVirtualMachineImpl, paramPacketStream);
/* 7527 */             break;
/*      */           case 1:
/* 7529 */             this.aEventsCommon = new SingleStep(paramVirtualMachineImpl, paramPacketStream);
/* 7530 */             break;
/*      */           case 2:
/* 7532 */             this.aEventsCommon = new Breakpoint(paramVirtualMachineImpl, paramPacketStream);
/* 7533 */             break;
/*      */           case 40:
/* 7535 */             this.aEventsCommon = new MethodEntry(paramVirtualMachineImpl, paramPacketStream);
/* 7536 */             break;
/*      */           case 41:
/* 7538 */             this.aEventsCommon = new MethodExit(paramVirtualMachineImpl, paramPacketStream);
/* 7539 */             break;
/*      */           case 42:
/* 7541 */             this.aEventsCommon = new MethodExitWithReturnValue(paramVirtualMachineImpl, paramPacketStream);
/* 7542 */             break;
/*      */           case 43:
/* 7544 */             this.aEventsCommon = new MonitorContendedEnter(paramVirtualMachineImpl, paramPacketStream);
/* 7545 */             break;
/*      */           case 44:
/* 7547 */             this.aEventsCommon = new MonitorContendedEntered(paramVirtualMachineImpl, paramPacketStream);
/* 7548 */             break;
/*      */           case 45:
/* 7550 */             this.aEventsCommon = new MonitorWait(paramVirtualMachineImpl, paramPacketStream);
/* 7551 */             break;
/*      */           case 46:
/* 7553 */             this.aEventsCommon = new MonitorWaited(paramVirtualMachineImpl, paramPacketStream);
/* 7554 */             break;
/*      */           case 4:
/* 7556 */             this.aEventsCommon = new Exception(paramVirtualMachineImpl, paramPacketStream);
/* 7557 */             break;
/*      */           case 6:
/* 7559 */             this.aEventsCommon = new ThreadStart(paramVirtualMachineImpl, paramPacketStream);
/* 7560 */             break;
/*      */           case 7:
/* 7562 */             this.aEventsCommon = new ThreadDeath(paramVirtualMachineImpl, paramPacketStream);
/* 7563 */             break;
/*      */           case 8:
/* 7565 */             this.aEventsCommon = new ClassPrepare(paramVirtualMachineImpl, paramPacketStream);
/* 7566 */             break;
/*      */           case 9:
/* 7568 */             this.aEventsCommon = new ClassUnload(paramVirtualMachineImpl, paramPacketStream);
/* 7569 */             break;
/*      */           case 20:
/* 7571 */             this.aEventsCommon = new FieldAccess(paramVirtualMachineImpl, paramPacketStream);
/* 7572 */             break;
/*      */           case 21:
/* 7574 */             this.aEventsCommon = new FieldModification(paramVirtualMachineImpl, paramPacketStream);
/* 7575 */             break;
/*      */           case 99:
/* 7577 */             this.aEventsCommon = new VMDeath(paramVirtualMachineImpl, paramPacketStream);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class Breakpoint extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 2;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 7668 */             return 2;
/*      */           }
/*      */ 
/*      */           Breakpoint(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7687 */             this.requestID = paramPacketStream.readInt();
/* 7688 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7689 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7691 */             this.thread = paramPacketStream.readThreadReference();
/* 7692 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7693 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7695 */             this.location = paramPacketStream.readLocation();
/* 7696 */             if (paramVirtualMachineImpl.traceReceives)
/* 7697 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ClassPrepare extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 8;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final byte refTypeTag;
/*      */           final long typeID;
/*      */           final String signature;
/*      */           final int status;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8266 */             return 8;
/*      */           }
/*      */ 
/*      */           ClassPrepare(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8316 */             this.requestID = paramPacketStream.readInt();
/* 8317 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8318 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8320 */             this.thread = paramPacketStream.readThreadReference();
/* 8321 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8322 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 8324 */             this.refTypeTag = paramPacketStream.readByte();
/* 8325 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8326 */               paramVirtualMachineImpl.printReceiveTrace(6, "refTypeTag(byte): " + this.refTypeTag);
/*      */             }
/* 8328 */             this.typeID = paramPacketStream.readClassRef();
/* 8329 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8330 */               paramVirtualMachineImpl.printReceiveTrace(6, "typeID(long): ref=" + this.typeID);
/*      */             }
/* 8332 */             this.signature = paramPacketStream.readString();
/* 8333 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8334 */               paramVirtualMachineImpl.printReceiveTrace(6, "signature(String): " + this.signature);
/*      */             }
/* 8336 */             this.status = paramPacketStream.readInt();
/* 8337 */             if (paramVirtualMachineImpl.traceReceives)
/* 8338 */               paramVirtualMachineImpl.printReceiveTrace(6, "status(int): " + this.status);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ClassUnload extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 9;
/*      */           final int requestID;
/*      */           final String signature;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8352 */             return 9;
/*      */           }
/*      */ 
/*      */           ClassUnload(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8366 */             this.requestID = paramPacketStream.readInt();
/* 8367 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8368 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8370 */             this.signature = paramPacketStream.readString();
/* 8371 */             if (paramVirtualMachineImpl.traceReceives)
/* 8372 */               paramVirtualMachineImpl.printReceiveTrace(6, "signature(String): " + this.signature);
/*      */           }
/*      */         }
/*      */ 
/*      */         static abstract class EventsCommon
/*      */         {
/*      */           abstract byte eventKind();
/*      */         }
/*      */ 
/*      */         static class Exception extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 4;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final ObjectReferenceImpl exception;
/*      */           final Location catchLocation;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8089 */             return 4;
/*      */           }
/*      */ 
/*      */           Exception(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8147 */             this.requestID = paramPacketStream.readInt();
/* 8148 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8149 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8151 */             this.thread = paramPacketStream.readThreadReference();
/* 8152 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8153 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 8155 */             this.location = paramPacketStream.readLocation();
/* 8156 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8157 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8159 */             this.exception = paramPacketStream.readTaggedObjectReference();
/* 8160 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8161 */               paramVirtualMachineImpl.printReceiveTrace(6, "exception(ObjectReferenceImpl): " + (this.exception == null ? "NULL" : new StringBuilder().append("ref=").append(this.exception.ref()).toString()));
/*      */             }
/* 8163 */             this.catchLocation = paramPacketStream.readLocation();
/* 8164 */             if (paramVirtualMachineImpl.traceReceives)
/* 8165 */               paramVirtualMachineImpl.printReceiveTrace(6, "catchLocation(Location): " + this.catchLocation);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class FieldAccess extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 20;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final byte refTypeTag;
/*      */           final long typeID;
/*      */           final long fieldID;
/*      */           final ObjectReferenceImpl object;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8387 */             return 20;
/*      */           }
/*      */ 
/*      */           FieldAccess(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8427 */             this.requestID = paramPacketStream.readInt();
/* 8428 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8429 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8431 */             this.thread = paramPacketStream.readThreadReference();
/* 8432 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8433 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 8435 */             this.location = paramPacketStream.readLocation();
/* 8436 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8437 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8439 */             this.refTypeTag = paramPacketStream.readByte();
/* 8440 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8441 */               paramVirtualMachineImpl.printReceiveTrace(6, "refTypeTag(byte): " + this.refTypeTag);
/*      */             }
/* 8443 */             this.typeID = paramPacketStream.readClassRef();
/* 8444 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8445 */               paramVirtualMachineImpl.printReceiveTrace(6, "typeID(long): ref=" + this.typeID);
/*      */             }
/* 8447 */             this.fieldID = paramPacketStream.readFieldRef();
/* 8448 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8449 */               paramVirtualMachineImpl.printReceiveTrace(6, "fieldID(long): " + this.fieldID);
/*      */             }
/* 8451 */             this.object = paramPacketStream.readTaggedObjectReference();
/* 8452 */             if (paramVirtualMachineImpl.traceReceives)
/* 8453 */               paramVirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));  }  } 
/*      */         static class FieldModification extends JDWP.Event.Composite.Events.EventsCommon { static final byte ALT_ID = 21;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final byte refTypeTag;
/*      */           final long typeID;
/*      */           final long fieldID;
/*      */           final ObjectReferenceImpl object;
/*      */           final ValueImpl valueToBe;
/*      */ 
/* 8466 */           byte eventKind() { return 21; }
/*      */ 
/*      */ 
/*      */           FieldModification(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8511 */             this.requestID = paramPacketStream.readInt();
/* 8512 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8513 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8515 */             this.thread = paramPacketStream.readThreadReference();
/* 8516 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8517 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 8519 */             this.location = paramPacketStream.readLocation();
/* 8520 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8521 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8523 */             this.refTypeTag = paramPacketStream.readByte();
/* 8524 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8525 */               paramVirtualMachineImpl.printReceiveTrace(6, "refTypeTag(byte): " + this.refTypeTag);
/*      */             }
/* 8527 */             this.typeID = paramPacketStream.readClassRef();
/* 8528 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8529 */               paramVirtualMachineImpl.printReceiveTrace(6, "typeID(long): ref=" + this.typeID);
/*      */             }
/* 8531 */             this.fieldID = paramPacketStream.readFieldRef();
/* 8532 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8533 */               paramVirtualMachineImpl.printReceiveTrace(6, "fieldID(long): " + this.fieldID);
/*      */             }
/* 8535 */             this.object = paramPacketStream.readTaggedObjectReference();
/* 8536 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8537 */               paramVirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));
/*      */             }
/* 8539 */             this.valueToBe = paramPacketStream.readValue();
/* 8540 */             if (paramVirtualMachineImpl.traceReceives)
/* 8541 */               paramVirtualMachineImpl.printReceiveTrace(6, "valueToBe(ValueImpl): " + this.valueToBe);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class MethodEntry extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 40;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 7715 */             return 40;
/*      */           }
/*      */ 
/*      */           MethodEntry(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7734 */             this.requestID = paramPacketStream.readInt();
/* 7735 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7736 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7738 */             this.thread = paramPacketStream.readThreadReference();
/* 7739 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7740 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7742 */             this.location = paramPacketStream.readLocation();
/* 7743 */             if (paramVirtualMachineImpl.traceReceives)
/* 7744 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class MethodExit extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 41;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 7760 */             return 41;
/*      */           }
/*      */ 
/*      */           MethodExit(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7779 */             this.requestID = paramPacketStream.readInt();
/* 7780 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7781 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7783 */             this.thread = paramPacketStream.readThreadReference();
/* 7784 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7785 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7787 */             this.location = paramPacketStream.readLocation();
/* 7788 */             if (paramVirtualMachineImpl.traceReceives)
/* 7789 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class MethodExitWithReturnValue extends JDWP.Event.Composite.Events.EventsCommon {
/*      */           static final byte ALT_ID = 42;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final ValueImpl value;
/*      */ 
/*      */           byte eventKind() {
/* 7805 */             return 42;
/*      */           }
/*      */ 
/*      */           MethodExitWithReturnValue(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7829 */             this.requestID = paramPacketStream.readInt();
/* 7830 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7831 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7833 */             this.thread = paramPacketStream.readThreadReference();
/* 7834 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7835 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7837 */             this.location = paramPacketStream.readLocation();
/* 7838 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7839 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 7841 */             this.value = paramPacketStream.readValue();
/* 7842 */             if (paramVirtualMachineImpl.traceReceives)
/* 7843 */               paramVirtualMachineImpl.printReceiveTrace(6, "value(ValueImpl): " + this.value); 
/*      */           }
/*      */         }
/*      */ 
/*      */         static class MonitorContendedEnter extends JDWP.Event.Composite.Events.EventsCommon {
/*      */           static final byte ALT_ID = 43;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */ 
/* 7858 */           byte eventKind() { return 43; }
/*      */ 
/*      */ 
/*      */           MonitorContendedEnter(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7882 */             this.requestID = paramPacketStream.readInt();
/* 7883 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7884 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7886 */             this.thread = paramPacketStream.readThreadReference();
/* 7887 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7888 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7890 */             this.object = paramPacketStream.readTaggedObjectReference();
/* 7891 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7892 */               paramVirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));
/*      */             }
/* 7894 */             this.location = paramPacketStream.readLocation();
/* 7895 */             if (paramVirtualMachineImpl.traceReceives)
/* 7896 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location); 
/*      */           }
/*      */         }
/*      */ 
/*      */         static class MonitorContendedEntered extends JDWP.Event.Composite.Events.EventsCommon {
/*      */           static final byte ALT_ID = 44;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */ 
/* 7911 */           byte eventKind() { return 44; }
/*      */ 
/*      */ 
/*      */           MonitorContendedEntered(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7935 */             this.requestID = paramPacketStream.readInt();
/* 7936 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7937 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7939 */             this.thread = paramPacketStream.readThreadReference();
/* 7940 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7941 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7943 */             this.object = paramPacketStream.readTaggedObjectReference();
/* 7944 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7945 */               paramVirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));
/*      */             }
/* 7947 */             this.location = paramPacketStream.readLocation();
/* 7948 */             if (paramVirtualMachineImpl.traceReceives)
/* 7949 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);  } 
/*      */         }
/*      */         static class MonitorWait extends JDWP.Event.Composite.Events.EventsCommon { static final byte ALT_ID = 45;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */           final long timeout;
/*      */ 
/* 7963 */           byte eventKind() { return 45; }
/*      */ 
/*      */ 
/*      */           MonitorWait(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7992 */             this.requestID = paramPacketStream.readInt();
/* 7993 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7994 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7996 */             this.thread = paramPacketStream.readThreadReference();
/* 7997 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7998 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 8000 */             this.object = paramPacketStream.readTaggedObjectReference();
/* 8001 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8002 */               paramVirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));
/*      */             }
/* 8004 */             this.location = paramPacketStream.readLocation();
/* 8005 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8006 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8008 */             this.timeout = paramPacketStream.readLong();
/* 8009 */             if (paramVirtualMachineImpl.traceReceives)
/* 8010 */               paramVirtualMachineImpl.printReceiveTrace(6, "timeout(long): " + this.timeout); 
/*      */           } } 
/*      */         static class MonitorWaited extends JDWP.Event.Composite.Events.EventsCommon {
/*      */           static final byte ALT_ID = 46;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */           final boolean timed_out;
/*      */ 
/* 8025 */           byte eventKind() { return 46; }
/*      */ 
/*      */ 
/*      */           MonitorWaited(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8054 */             this.requestID = paramPacketStream.readInt();
/* 8055 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8056 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8058 */             this.thread = paramPacketStream.readThreadReference();
/* 8059 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8060 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 8062 */             this.object = paramPacketStream.readTaggedObjectReference();
/* 8063 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8064 */               paramVirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));
/*      */             }
/* 8066 */             this.location = paramPacketStream.readLocation();
/* 8067 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8068 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8070 */             this.timed_out = paramPacketStream.readBoolean();
/* 8071 */             if (paramVirtualMachineImpl.traceReceives)
/* 8072 */               paramVirtualMachineImpl.printReceiveTrace(6, "timed_out(boolean): " + this.timed_out);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class SingleStep extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 1;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 7627 */             return 1;
/*      */           }
/*      */ 
/*      */           SingleStep(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7646 */             this.requestID = paramPacketStream.readInt();
/* 7647 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7648 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7650 */             this.thread = paramPacketStream.readThreadReference();
/* 7651 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7652 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 7654 */             this.location = paramPacketStream.readLocation();
/* 7655 */             if (paramVirtualMachineImpl.traceReceives)
/* 7656 */               paramVirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ThreadDeath extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 7;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8232 */             return 7;
/*      */           }
/*      */ 
/*      */           ThreadDeath(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8246 */             this.requestID = paramPacketStream.readInt();
/* 8247 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8248 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8250 */             this.thread = paramPacketStream.readThreadReference();
/* 8251 */             if (paramVirtualMachineImpl.traceReceives)
/* 8252 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ThreadStart extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 6;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8193 */             return 6;
/*      */           }
/*      */ 
/*      */           ThreadStart(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8207 */             this.requestID = paramPacketStream.readInt();
/* 8208 */             if (paramVirtualMachineImpl.traceReceives) {
/* 8209 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8211 */             this.thread = paramPacketStream.readThreadReference();
/* 8212 */             if (paramVirtualMachineImpl.traceReceives)
/* 8213 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */           }
/*      */         }
/*      */ 
/*      */         static class VMDeath extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 99;
/*      */           final int requestID;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 8549 */             return 99;
/*      */           }
/*      */ 
/*      */           VMDeath(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 8558 */             this.requestID = paramPacketStream.readInt();
/* 8559 */             if (paramVirtualMachineImpl.traceReceives)
/* 8560 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class VMStart extends JDWP.Event.Composite.Events.EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 90;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */ 
/*      */           byte eventKind()
/*      */           {
/* 7594 */             return 90;
/*      */           }
/*      */ 
/*      */           VMStart(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */           {
/* 7609 */             this.requestID = paramPacketStream.readInt();
/* 7610 */             if (paramVirtualMachineImpl.traceReceives) {
/* 7611 */               paramVirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7613 */             this.thread = paramPacketStream.readThreadReference();
/* 7614 */             if (paramVirtualMachineImpl.traceReceives)
/* 7615 */               paramVirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EventKind
/*      */   {
/*      */     static final int SINGLE_STEP = 1;
/*      */     static final int BREAKPOINT = 2;
/*      */     static final int FRAME_POP = 3;
/*      */     static final int EXCEPTION = 4;
/*      */     static final int USER_DEFINED = 5;
/*      */     static final int THREAD_START = 6;
/*      */     static final int THREAD_DEATH = 7;
/*      */     static final int THREAD_END = 7;
/*      */     static final int CLASS_PREPARE = 8;
/*      */     static final int CLASS_UNLOAD = 9;
/*      */     static final int CLASS_LOAD = 10;
/*      */     static final int FIELD_ACCESS = 20;
/*      */     static final int FIELD_MODIFICATION = 21;
/*      */     static final int EXCEPTION_CATCH = 30;
/*      */     static final int METHOD_ENTRY = 40;
/*      */     static final int METHOD_EXIT = 41;
/*      */     static final int METHOD_EXIT_WITH_RETURN_VALUE = 42;
/*      */     static final int MONITOR_CONTENDED_ENTER = 43;
/*      */     static final int MONITOR_CONTENDED_ENTERED = 44;
/*      */     static final int MONITOR_WAIT = 45;
/*      */     static final int MONITOR_WAITED = 46;
/*      */     static final int VM_START = 90;
/*      */     static final int VM_INIT = 90;
/*      */     static final int VM_DEATH = 99;
/*      */     static final int VM_DISCONNECTED = 100;
/*      */   }
/*      */ 
/*      */   static class EventRequest
/*      */   {
/*      */     static final int COMMAND_SET = 15;
/*      */ 
/*      */     static class Clear
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */ 
/*      */       static Clear process(VirtualMachineImpl paramVirtualMachineImpl, byte paramByte, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 6946 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramByte, paramInt);
/* 6947 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, byte paramByte, int paramInt)
/*      */       {
/* 6953 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 15, 2);
/* 6954 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6955 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.EventRequest.Clear" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6957 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6958 */           localPacketStream.vm.printTrace("Sending:                 eventKind(byte): " + paramByte);
/*      */         }
/* 6960 */         localPacketStream.writeByte(paramByte);
/* 6961 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6962 */           localPacketStream.vm.printTrace("Sending:                 requestID(int): " + paramInt);
/*      */         }
/* 6964 */         localPacketStream.writeInt(paramInt);
/* 6965 */         localPacketStream.send();
/* 6966 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Clear waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6971 */         paramPacketStream.waitForReply();
/* 6972 */         return new Clear(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Clear(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6977 */         if (paramVirtualMachineImpl.traceReceives)
/* 6978 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.EventRequest.Clear" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ClearAllBreakpoints
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */ 
/*      */       static ClearAllBreakpoints process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/* 6991 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/* 6992 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/* 6996 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 15, 3);
/* 6997 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6998 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.EventRequest.ClearAllBreakpoints" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 7000 */         localPacketStream.send();
/* 7001 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ClearAllBreakpoints waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 7006 */         paramPacketStream.waitForReply();
/* 7007 */         return new ClearAllBreakpoints(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ClearAllBreakpoints(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 7012 */         if (paramVirtualMachineImpl.traceReceives)
/* 7013 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.EventRequest.ClearAllBreakpoints" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Set
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final int requestID;
/*      */ 
/*      */       static Set process(VirtualMachineImpl paramVirtualMachineImpl, byte paramByte1, byte paramByte2, Modifier[] paramArrayOfModifier)
/*      */         throws JDWPException
/*      */       {
/* 6874 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramByte1, paramByte2, paramArrayOfModifier);
/* 6875 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, byte paramByte1, byte paramByte2, Modifier[] paramArrayOfModifier)
/*      */       {
/* 6882 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 15, 1);
/* 6883 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6884 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.EventRequest.Set" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6886 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6887 */           localPacketStream.vm.printTrace("Sending:                 eventKind(byte): " + paramByte1);
/*      */         }
/* 6889 */         localPacketStream.writeByte(paramByte1);
/* 6890 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6891 */           localPacketStream.vm.printTrace("Sending:                 suspendPolicy(byte): " + paramByte2);
/*      */         }
/* 6893 */         localPacketStream.writeByte(paramByte2);
/* 6894 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6895 */           localPacketStream.vm.printTrace("Sending:                 modifiers(Modifier[]): ");
/*      */         }
/* 6897 */         localPacketStream.writeInt(paramArrayOfModifier.length);
/* 6898 */         for (int i = 0; i < paramArrayOfModifier.length; i++) {
/* 6899 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6900 */             localPacketStream.vm.printTrace("Sending:                     modifiers[i](Modifier): ");
/*      */           }
/* 6902 */           paramArrayOfModifier[i].write(localPacketStream);
/*      */         }
/* 6904 */         localPacketStream.send();
/* 6905 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Set waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6910 */         paramPacketStream.waitForReply();
/* 6911 */         return new Set(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Set(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6921 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6922 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.EventRequest.Set" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 6924 */         this.requestID = paramPacketStream.readInt();
/* 6925 */         if (paramVirtualMachineImpl.traceReceives)
/* 6926 */           paramVirtualMachineImpl.printReceiveTrace(4, "requestID(int): " + this.requestID);
/*      */       }
/*      */ 
/*      */       static class Modifier
/*      */       {
/*      */         final byte modKind;
/*      */         ModifierCommon aModifierCommon;
/*      */ 
/*      */         Modifier(byte paramByte, ModifierCommon paramModifierCommon)
/*      */         {
/* 6398 */           this.modKind = paramByte;
/* 6399 */           this.aModifierCommon = paramModifierCommon;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 6403 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6404 */             paramPacketStream.vm.printTrace("Sending:                     modKind(byte): " + this.modKind);
/*      */           }
/* 6406 */           paramPacketStream.writeByte(this.modKind);
/* 6407 */           this.aModifierCommon.write(paramPacketStream);
/*      */         }
/*      */ 
/*      */         static class ClassExclude extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 6;
/*      */           final String classPattern;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(String paramString)
/*      */           {
/* 6593 */             return new JDWP.EventRequest.Set.Modifier((byte)6, new ClassExclude(paramString));
/*      */           }
/*      */ 
/*      */           ClassExclude(String paramString)
/*      */           {
/* 6606 */             this.classPattern = paramString;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6610 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6611 */               paramPacketStream.vm.printTrace("Sending:                         classPattern(String): " + this.classPattern);
/*      */             }
/* 6613 */             paramPacketStream.writeString(this.classPattern);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ClassMatch extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 5;
/*      */           final String classPattern;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(String paramString)
/*      */           {
/* 6554 */             return new JDWP.EventRequest.Set.Modifier((byte)5, new ClassMatch(paramString));
/*      */           }
/*      */ 
/*      */           ClassMatch(String paramString)
/*      */           {
/* 6567 */             this.classPattern = paramString;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6571 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6572 */               paramPacketStream.vm.printTrace("Sending:                         classPattern(String): " + this.classPattern);
/*      */             }
/* 6574 */             paramPacketStream.writeString(this.classPattern);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ClassOnly extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 4;
/*      */           final ReferenceTypeImpl clazz;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(ReferenceTypeImpl paramReferenceTypeImpl)
/*      */           {
/* 6519 */             return new JDWP.EventRequest.Set.Modifier((byte)4, new ClassOnly(paramReferenceTypeImpl));
/*      */           }
/*      */ 
/*      */           ClassOnly(ReferenceTypeImpl paramReferenceTypeImpl)
/*      */           {
/* 6528 */             this.clazz = paramReferenceTypeImpl;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6532 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6533 */               paramPacketStream.vm.printTrace("Sending:                         clazz(ReferenceTypeImpl): " + (this.clazz == null ? "NULL" : new StringBuilder().append("ref=").append(this.clazz.ref()).toString()));
/*      */             }
/* 6535 */             paramPacketStream.writeClassRef(this.clazz.ref());
/*      */           }
/*      */         }
/*      */ 
/*      */         static class Conditional extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 2;
/*      */           final int exprID;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(int paramInt)
/*      */           {
/* 6452 */             return new JDWP.EventRequest.Set.Modifier((byte)2, new Conditional(paramInt));
/*      */           }
/*      */ 
/*      */           Conditional(int paramInt)
/*      */           {
/* 6461 */             this.exprID = paramInt;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6465 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6466 */               paramPacketStream.vm.printTrace("Sending:                         exprID(int): " + this.exprID);
/*      */             }
/* 6468 */             paramPacketStream.writeInt(this.exprID);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class Count extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 1;
/*      */           final int count;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(int paramInt)
/*      */           {
/* 6426 */             return new JDWP.EventRequest.Set.Modifier((byte)1, new Count(paramInt));
/*      */           }
/*      */ 
/*      */           Count(int paramInt)
/*      */           {
/* 6435 */             this.count = paramInt;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6439 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6440 */               paramPacketStream.vm.printTrace("Sending:                         count(int): " + this.count);
/*      */             }
/* 6442 */             paramPacketStream.writeInt(this.count);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ExceptionOnly extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 8;
/*      */           final ReferenceTypeImpl exceptionOrNull;
/*      */           final boolean caught;
/*      */           final boolean uncaught;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(ReferenceTypeImpl paramReferenceTypeImpl, boolean paramBoolean1, boolean paramBoolean2)
/*      */           {
/* 6656 */             return new JDWP.EventRequest.Set.Modifier((byte)8, new ExceptionOnly(paramReferenceTypeImpl, paramBoolean1, paramBoolean2));
/*      */           }
/*      */ 
/*      */           ExceptionOnly(ReferenceTypeImpl paramReferenceTypeImpl, boolean paramBoolean1, boolean paramBoolean2)
/*      */           {
/* 6685 */             this.exceptionOrNull = paramReferenceTypeImpl;
/* 6686 */             this.caught = paramBoolean1;
/* 6687 */             this.uncaught = paramBoolean2;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6691 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6692 */               paramPacketStream.vm.printTrace("Sending:                         exceptionOrNull(ReferenceTypeImpl): " + (this.exceptionOrNull == null ? "NULL" : new StringBuilder().append("ref=").append(this.exceptionOrNull.ref()).toString()));
/*      */             }
/* 6694 */             paramPacketStream.writeClassRef(this.exceptionOrNull.ref());
/* 6695 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6696 */               paramPacketStream.vm.printTrace("Sending:                         caught(boolean): " + this.caught);
/*      */             }
/* 6698 */             paramPacketStream.writeBoolean(this.caught);
/* 6699 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6700 */               paramPacketStream.vm.printTrace("Sending:                         uncaught(boolean): " + this.uncaught);
/*      */             }
/* 6702 */             paramPacketStream.writeBoolean(this.uncaught);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class FieldOnly extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 9;
/*      */           final ReferenceTypeImpl declaring;
/*      */           final long fieldID;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(ReferenceTypeImpl paramReferenceTypeImpl, long paramLong) {
/* 6715 */             return new JDWP.EventRequest.Set.Modifier((byte)9, new FieldOnly(paramReferenceTypeImpl, paramLong));
/*      */           }
/*      */ 
/*      */           FieldOnly(ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */           {
/* 6729 */             this.declaring = paramReferenceTypeImpl;
/* 6730 */             this.fieldID = paramLong;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6734 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6735 */               paramPacketStream.vm.printTrace("Sending:                         declaring(ReferenceTypeImpl): " + (this.declaring == null ? "NULL" : new StringBuilder().append("ref=").append(this.declaring.ref()).toString()));
/*      */             }
/* 6737 */             paramPacketStream.writeClassRef(this.declaring.ref());
/* 6738 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6739 */               paramPacketStream.vm.printTrace("Sending:                         fieldID(long): " + this.fieldID);
/*      */             }
/* 6741 */             paramPacketStream.writeFieldRef(this.fieldID);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class InstanceOnly extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 11;
/*      */           final ObjectReferenceImpl instance;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(ObjectReferenceImpl paramObjectReferenceImpl)
/*      */           {
/* 6808 */             return new JDWP.EventRequest.Set.Modifier((byte)11, new InstanceOnly(paramObjectReferenceImpl));
/*      */           }
/*      */ 
/*      */           InstanceOnly(ObjectReferenceImpl paramObjectReferenceImpl)
/*      */           {
/* 6817 */             this.instance = paramObjectReferenceImpl;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6821 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6822 */               paramPacketStream.vm.printTrace("Sending:                         instance(ObjectReferenceImpl): " + (this.instance == null ? "NULL" : new StringBuilder().append("ref=").append(this.instance.ref()).toString()));
/*      */             }
/* 6824 */             paramPacketStream.writeObjectRef(this.instance.ref());
/*      */           }
/*      */         }
/*      */ 
/*      */         static class LocationOnly extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 7;
/*      */           final Location loc;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(Location paramLocation)
/*      */           {
/* 6627 */             return new JDWP.EventRequest.Set.Modifier((byte)7, new LocationOnly(paramLocation));
/*      */           }
/*      */ 
/*      */           LocationOnly(Location paramLocation)
/*      */           {
/* 6636 */             this.loc = paramLocation;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6640 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6641 */               paramPacketStream.vm.printTrace("Sending:                         loc(Location): " + this.loc);
/*      */             }
/* 6643 */             paramPacketStream.writeLocation(this.loc);
/*      */           }
/*      */         }
/*      */ 
/*      */         static abstract class ModifierCommon
/*      */         {
/*      */           abstract void write(PacketStream paramPacketStream);
/*      */         }
/*      */ 
/*      */         static class SourceNameMatch extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 12;
/*      */           final String sourceNamePattern;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(String paramString)
/*      */           {
/* 6844 */             return new JDWP.EventRequest.Set.Modifier((byte)12, new SourceNameMatch(paramString));
/*      */           }
/*      */ 
/*      */           SourceNameMatch(String paramString)
/*      */           {
/* 6857 */             this.sourceNamePattern = paramString;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6861 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6862 */               paramPacketStream.vm.printTrace("Sending:                         sourceNamePattern(String): " + this.sourceNamePattern);
/*      */             }
/* 6864 */             paramPacketStream.writeString(this.sourceNamePattern);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class Step extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 10;
/*      */           final ThreadReferenceImpl thread;
/*      */           final int size;
/*      */           final int depth;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(ThreadReferenceImpl paramThreadReferenceImpl, int paramInt1, int paramInt2)
/*      */           {
/* 6755 */             return new JDWP.EventRequest.Set.Modifier((byte)10, new Step(paramThreadReferenceImpl, paramInt1, paramInt2));
/*      */           }
/*      */ 
/*      */           Step(ThreadReferenceImpl paramThreadReferenceImpl, int paramInt1, int paramInt2)
/*      */           {
/* 6776 */             this.thread = paramThreadReferenceImpl;
/* 6777 */             this.size = paramInt1;
/* 6778 */             this.depth = paramInt2;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6782 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6783 */               paramPacketStream.vm.printTrace("Sending:                         thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 6785 */             paramPacketStream.writeObjectRef(this.thread.ref());
/* 6786 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6787 */               paramPacketStream.vm.printTrace("Sending:                         size(int): " + this.size);
/*      */             }
/* 6789 */             paramPacketStream.writeInt(this.size);
/* 6790 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6791 */               paramPacketStream.vm.printTrace("Sending:                         depth(int): " + this.depth);
/*      */             }
/* 6793 */             paramPacketStream.writeInt(this.depth);
/*      */           }
/*      */         }
/*      */ 
/*      */         static class ThreadOnly extends JDWP.EventRequest.Set.Modifier.ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 3;
/*      */           final ThreadReferenceImpl thread;
/*      */ 
/*      */           static JDWP.EventRequest.Set.Modifier create(ThreadReferenceImpl paramThreadReferenceImpl)
/*      */           {
/* 6481 */             return new JDWP.EventRequest.Set.Modifier((byte)3, new ThreadOnly(paramThreadReferenceImpl));
/*      */           }
/*      */ 
/*      */           ThreadOnly(ThreadReferenceImpl paramThreadReferenceImpl)
/*      */           {
/* 6490 */             this.thread = paramThreadReferenceImpl;
/*      */           }
/*      */ 
/*      */           void write(PacketStream paramPacketStream) {
/* 6494 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6495 */               paramPacketStream.vm.printTrace("Sending:                         thread(ThreadReferenceImpl): " + (this.thread == null ? "NULL" : new StringBuilder().append("ref=").append(this.thread.ref()).toString()));
/*      */             }
/* 6497 */             paramPacketStream.writeObjectRef(this.thread.ref());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Field
/*      */   {
/*      */     static final int COMMAND_SET = 8;
/*      */   }
/*      */ 
/*      */   static class InterfaceType
/*      */   {
/*      */     static final int COMMAND_SET = 5;
/*      */ 
/*      */     static class InvokeMethod
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final ValueImpl returnValue;
/*      */       final ObjectReferenceImpl exception;
/*      */ 
/*      */       static InvokeMethod process(VirtualMachineImpl paramVirtualMachineImpl, InterfaceTypeImpl paramInterfaceTypeImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 3668 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramInterfaceTypeImpl, paramThreadReferenceImpl, paramLong, paramArrayOfValueImpl, paramInt);
/* 3669 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, InterfaceTypeImpl paramInterfaceTypeImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */       {
/* 3678 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 5, 1);
/* 3679 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3680 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.InterfaceType.InvokeMethod" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3682 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3683 */           localPacketStream.vm.printTrace("Sending:                 clazz(InterfaceTypeImpl): " + (paramInterfaceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramInterfaceTypeImpl.ref()).toString()));
/*      */         }
/* 3685 */         localPacketStream.writeClassRef(paramInterfaceTypeImpl.ref());
/* 3686 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3687 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 3689 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 3690 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3691 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 3693 */         localPacketStream.writeMethodRef(paramLong);
/* 3694 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3695 */           localPacketStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 3697 */         localPacketStream.writeInt(paramArrayOfValueImpl.length);
/* 3698 */         for (int i = 0; i < paramArrayOfValueImpl.length; i++) {
/* 3699 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3700 */             localPacketStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + paramArrayOfValueImpl[i]);
/*      */           }
/* 3702 */           localPacketStream.writeValue(paramArrayOfValueImpl[i]);
/*      */         }
/* 3704 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3705 */           localPacketStream.vm.printTrace("Sending:                 options(int): " + paramInt);
/*      */         }
/* 3707 */         localPacketStream.writeInt(paramInt);
/* 3708 */         localPacketStream.send();
/* 3709 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static InvokeMethod waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3714 */         paramPacketStream.waitForReply();
/* 3715 */         return new InvokeMethod(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private InvokeMethod(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3730 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3731 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.InterfaceType.InvokeMethod" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3733 */         this.returnValue = paramPacketStream.readValue();
/* 3734 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3735 */           paramVirtualMachineImpl.printReceiveTrace(4, "returnValue(ValueImpl): " + this.returnValue);
/*      */         }
/* 3737 */         this.exception = paramPacketStream.readTaggedObjectReference();
/* 3738 */         if (paramVirtualMachineImpl.traceReceives)
/* 3739 */           paramVirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + (this.exception == null ? "NULL" : new StringBuilder().append("ref=").append(this.exception.ref()).toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class InvokeOptions
/*      */   {
/*      */     static final int INVOKE_SINGLE_THREADED = 1;
/*      */     static final int INVOKE_NONVIRTUAL = 2;
/*      */   }
/*      */ 
/*      */   static class Method
/*      */   {
/*      */     static final int COMMAND_SET = 6;
/*      */ 
/*      */     static class Bytecodes
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */       final byte[] bytes;
/*      */ 
/*      */       static Bytecodes process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 4008 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramLong);
/* 4009 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */       {
/* 4015 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 6, 3);
/* 4016 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4017 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.Method.Bytecodes" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4019 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4020 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 4022 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 4023 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4024 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 4026 */         localPacketStream.writeMethodRef(paramLong);
/* 4027 */         localPacketStream.send();
/* 4028 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Bytecodes waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4033 */         paramPacketStream.waitForReply();
/* 4034 */         return new Bytecodes(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Bytecodes(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4041 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4042 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.Method.Bytecodes" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4044 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4045 */           paramVirtualMachineImpl.printReceiveTrace(4, "bytes(byte[]): ");
/*      */         }
/* 4047 */         int i = paramPacketStream.readInt();
/* 4048 */         this.bytes = new byte[i];
/* 4049 */         for (int j = 0; j < i; j++) {
/* 4050 */           this.bytes[j] = paramPacketStream.readByte();
/* 4051 */           if (paramVirtualMachineImpl.traceReceives)
/* 4052 */             paramVirtualMachineImpl.printReceiveTrace(5, "bytes[i](byte): " + this.bytes[j]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class IsObsolete
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */       final boolean isObsolete;
/*      */ 
/*      */       static IsObsolete process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 4073 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramLong);
/* 4074 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */       {
/* 4080 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 6, 4);
/* 4081 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4082 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.Method.IsObsolete" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4084 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4085 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 4087 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 4088 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4089 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 4091 */         localPacketStream.writeMethodRef(paramLong);
/* 4092 */         localPacketStream.send();
/* 4093 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static IsObsolete waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4098 */         paramPacketStream.waitForReply();
/* 4099 */         return new IsObsolete(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private IsObsolete(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4111 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4112 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.Method.IsObsolete" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4114 */         this.isObsolete = paramPacketStream.readBoolean();
/* 4115 */         if (paramVirtualMachineImpl.traceReceives)
/* 4116 */           paramVirtualMachineImpl.printReceiveTrace(4, "isObsolete(boolean): " + this.isObsolete);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class LineTable
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final long start;
/*      */       final long end;
/*      */       final LineInfo[] lines;
/*      */ 
/*      */       static LineTable process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 3764 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramLong);
/* 3765 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */       {
/* 3771 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 6, 1);
/* 3772 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3773 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.Method.LineTable" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3775 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3776 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 3778 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 3779 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3780 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 3782 */         localPacketStream.writeMethodRef(paramLong);
/* 3783 */         localPacketStream.send();
/* 3784 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static LineTable waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3789 */         paramPacketStream.waitForReply();
/* 3790 */         return new LineTable(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private LineTable(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3835 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3836 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.Method.LineTable" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3838 */         this.start = paramPacketStream.readLong();
/* 3839 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3840 */           paramVirtualMachineImpl.printReceiveTrace(4, "start(long): " + this.start);
/*      */         }
/* 3842 */         this.end = paramPacketStream.readLong();
/* 3843 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3844 */           paramVirtualMachineImpl.printReceiveTrace(4, "end(long): " + this.end);
/*      */         }
/* 3846 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3847 */           paramVirtualMachineImpl.printReceiveTrace(4, "lines(LineInfo[]): ");
/*      */         }
/* 3849 */         int i = paramPacketStream.readInt();
/* 3850 */         this.lines = new LineInfo[i];
/* 3851 */         for (int j = 0; j < i; j++) {
/* 3852 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3853 */             paramVirtualMachineImpl.printReceiveTrace(5, "lines[i](LineInfo): ");
/*      */           }
/* 3855 */           this.lines[j] = new LineInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class LineInfo
/*      */       {
/*      */         final long lineCodeIndex;
/*      */         final int lineNumber;
/*      */ 
/*      */         private LineInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 3807 */           this.lineCodeIndex = paramPacketStream.readLong();
/* 3808 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3809 */             paramVirtualMachineImpl.printReceiveTrace(5, "lineCodeIndex(long): " + this.lineCodeIndex);
/*      */           }
/* 3811 */           this.lineNumber = paramPacketStream.readInt();
/* 3812 */           if (paramVirtualMachineImpl.traceReceives)
/* 3813 */             paramVirtualMachineImpl.printReceiveTrace(5, "lineNumber(int): " + this.lineNumber);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class VariableTable
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */       final int argCnt;
/*      */       final SlotInfo[] slots;
/*      */ 
/*      */       static VariableTable process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 3873 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramLong);
/* 3874 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */       {
/* 3880 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 6, 2);
/* 3881 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3882 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.Method.VariableTable" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3884 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3885 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 3887 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 3888 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3889 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 3891 */         localPacketStream.writeMethodRef(paramLong);
/* 3892 */         localPacketStream.send();
/* 3893 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static VariableTable waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3898 */         paramPacketStream.waitForReply();
/* 3899 */         return new VariableTable(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private VariableTable(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3974 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3975 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.Method.VariableTable" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3977 */         this.argCnt = paramPacketStream.readInt();
/* 3978 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3979 */           paramVirtualMachineImpl.printReceiveTrace(4, "argCnt(int): " + this.argCnt);
/*      */         }
/* 3981 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3982 */           paramVirtualMachineImpl.printReceiveTrace(4, "slots(SlotInfo[]): ");
/*      */         }
/* 3984 */         int i = paramPacketStream.readInt();
/* 3985 */         this.slots = new SlotInfo[i];
/* 3986 */         for (int j = 0; j < i; j++) {
/* 3987 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3988 */             paramVirtualMachineImpl.printReceiveTrace(5, "slots[i](SlotInfo): ");
/*      */           }
/* 3990 */           this.slots[j] = new SlotInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class SlotInfo
/*      */       {
/*      */         final long codeIndex;
/*      */         final String name;
/*      */         final String signature;
/*      */         final int length;
/*      */         final int slot;
/*      */ 
/*      */         private SlotInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 3938 */           this.codeIndex = paramPacketStream.readLong();
/* 3939 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3940 */             paramVirtualMachineImpl.printReceiveTrace(5, "codeIndex(long): " + this.codeIndex);
/*      */           }
/* 3942 */           this.name = paramPacketStream.readString();
/* 3943 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3944 */             paramVirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 3946 */           this.signature = paramPacketStream.readString();
/* 3947 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3948 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 3950 */           this.length = paramPacketStream.readInt();
/* 3951 */           if (paramVirtualMachineImpl.traceReceives) {
/* 3952 */             paramVirtualMachineImpl.printReceiveTrace(5, "length(int): " + this.length);
/*      */           }
/* 3954 */           this.slot = paramPacketStream.readInt();
/* 3955 */           if (paramVirtualMachineImpl.traceReceives)
/* 3956 */             paramVirtualMachineImpl.printReceiveTrace(5, "slot(int): " + this.slot);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class VariableTableWithGeneric
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */       final int argCnt;
/*      */       final SlotInfo[] slots;
/*      */ 
/*      */       static VariableTableWithGeneric process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 4139 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramLong);
/* 4140 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong)
/*      */       {
/* 4146 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 6, 5);
/* 4147 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4148 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.Method.VariableTableWithGeneric" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4150 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4151 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 4153 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 4154 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4155 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 4157 */         localPacketStream.writeMethodRef(paramLong);
/* 4158 */         localPacketStream.send();
/* 4159 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static VariableTableWithGeneric waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4164 */         paramPacketStream.waitForReply();
/* 4165 */         return new VariableTableWithGeneric(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private VariableTableWithGeneric(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4250 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4251 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.Method.VariableTableWithGeneric" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4253 */         this.argCnt = paramPacketStream.readInt();
/* 4254 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4255 */           paramVirtualMachineImpl.printReceiveTrace(4, "argCnt(int): " + this.argCnt);
/*      */         }
/* 4257 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4258 */           paramVirtualMachineImpl.printReceiveTrace(4, "slots(SlotInfo[]): ");
/*      */         }
/* 4260 */         int i = paramPacketStream.readInt();
/* 4261 */         this.slots = new SlotInfo[i];
/* 4262 */         for (int j = 0; j < i; j++) {
/* 4263 */           if (paramVirtualMachineImpl.traceReceives) {
/* 4264 */             paramVirtualMachineImpl.printReceiveTrace(5, "slots[i](SlotInfo): ");
/*      */           }
/* 4266 */           this.slots[j] = new SlotInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class SlotInfo
/*      */       {
/*      */         final long codeIndex;
/*      */         final String name;
/*      */         final String signature;
/*      */         final String genericSignature;
/*      */         final int length;
/*      */         final int slot;
/*      */ 
/*      */         private SlotInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 4210 */           this.codeIndex = paramPacketStream.readLong();
/* 4211 */           if (paramVirtualMachineImpl.traceReceives) {
/* 4212 */             paramVirtualMachineImpl.printReceiveTrace(5, "codeIndex(long): " + this.codeIndex);
/*      */           }
/* 4214 */           this.name = paramPacketStream.readString();
/* 4215 */           if (paramVirtualMachineImpl.traceReceives) {
/* 4216 */             paramVirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 4218 */           this.signature = paramPacketStream.readString();
/* 4219 */           if (paramVirtualMachineImpl.traceReceives) {
/* 4220 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 4222 */           this.genericSignature = paramPacketStream.readString();
/* 4223 */           if (paramVirtualMachineImpl.traceReceives) {
/* 4224 */             paramVirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 4226 */           this.length = paramPacketStream.readInt();
/* 4227 */           if (paramVirtualMachineImpl.traceReceives) {
/* 4228 */             paramVirtualMachineImpl.printReceiveTrace(5, "length(int): " + this.length);
/*      */           }
/* 4230 */           this.slot = paramPacketStream.readInt();
/* 4231 */           if (paramVirtualMachineImpl.traceReceives)
/* 4232 */             paramVirtualMachineImpl.printReceiveTrace(5, "slot(int): " + this.slot);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ObjectReference
/*      */   {
/*      */     static final int COMMAND_SET = 9;
/*      */ 
/*      */     static class DisableCollection
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */ 
/*      */       static DisableCollection process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 4784 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl);
/* 4785 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 4790 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 7);
/* 4791 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4792 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.DisableCollection" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4794 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4795 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4797 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4798 */         localPacketStream.send();
/* 4799 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static DisableCollection waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4804 */         paramPacketStream.waitForReply();
/* 4805 */         return new DisableCollection(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private DisableCollection(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4810 */         if (paramVirtualMachineImpl.traceReceives)
/* 4811 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.DisableCollection" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class EnableCollection
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */ 
/*      */       static EnableCollection process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 4830 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl);
/* 4831 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 4836 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 8);
/* 4837 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4838 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.EnableCollection" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4840 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4841 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4843 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4844 */         localPacketStream.send();
/* 4845 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static EnableCollection waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4850 */         paramPacketStream.waitForReply();
/* 4851 */         return new EnableCollection(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private EnableCollection(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4856 */         if (paramVirtualMachineImpl.traceReceives)
/* 4857 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.EnableCollection" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */       final ValueImpl[] values;
/*      */ 
/*      */       static GetValues process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, Field[] paramArrayOfField)
/*      */         throws JDWPException
/*      */       {
/* 4375 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl, paramArrayOfField);
/* 4376 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, Field[] paramArrayOfField)
/*      */       {
/* 4382 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 2);
/* 4383 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4384 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.GetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4386 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4387 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4389 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4390 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4391 */           localPacketStream.vm.printTrace("Sending:                 fields(Field[]): ");
/*      */         }
/* 4393 */         localPacketStream.writeInt(paramArrayOfField.length);
/* 4394 */         for (int i = 0; i < paramArrayOfField.length; i++) {
/* 4395 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4396 */             localPacketStream.vm.printTrace("Sending:                     fields[i](Field): ");
/*      */           }
/* 4398 */           paramArrayOfField[i].write(localPacketStream);
/*      */         }
/* 4400 */         localPacketStream.send();
/* 4401 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static GetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4406 */         paramPacketStream.waitForReply();
/* 4407 */         return new GetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private GetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4420 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4421 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.GetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4423 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4424 */           paramVirtualMachineImpl.printReceiveTrace(4, "values(ValueImpl[]): ");
/*      */         }
/* 4426 */         int i = paramPacketStream.readInt();
/* 4427 */         this.values = new ValueImpl[i];
/* 4428 */         for (int j = 0; j < i; j++) {
/* 4429 */           this.values[j] = paramPacketStream.readValue();
/* 4430 */           if (paramVirtualMachineImpl.traceReceives)
/* 4431 */             paramVirtualMachineImpl.printReceiveTrace(5, "values[i](ValueImpl): " + this.values[j]);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class Field
/*      */       {
/*      */         final long fieldID;
/*      */ 
/*      */         Field(long paramLong)
/*      */         {
/* 4360 */           this.fieldID = paramLong;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 4364 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4365 */             paramPacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 4367 */           paramPacketStream.writeFieldRef(this.fieldID);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class InvokeMethod
/*      */     {
/*      */       static final int COMMAND = 6;
/*      */       final ValueImpl returnValue;
/*      */       final ObjectReferenceImpl exception;
/*      */ 
/*      */       static InvokeMethod process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, ThreadReferenceImpl paramThreadReferenceImpl, ClassTypeImpl paramClassTypeImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 4676 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl, paramThreadReferenceImpl, paramClassTypeImpl, paramLong, paramArrayOfValueImpl, paramInt);
/* 4677 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, ThreadReferenceImpl paramThreadReferenceImpl, ClassTypeImpl paramClassTypeImpl, long paramLong, ValueImpl[] paramArrayOfValueImpl, int paramInt)
/*      */       {
/* 4687 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 6);
/* 4688 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4689 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.InvokeMethod" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4691 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4692 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4694 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4695 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4696 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 4698 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 4699 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4700 */           localPacketStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + (paramClassTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramClassTypeImpl.ref()).toString()));
/*      */         }
/* 4702 */         localPacketStream.writeClassRef(paramClassTypeImpl.ref());
/* 4703 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4704 */           localPacketStream.vm.printTrace("Sending:                 methodID(long): " + paramLong);
/*      */         }
/* 4706 */         localPacketStream.writeMethodRef(paramLong);
/* 4707 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4708 */           localPacketStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 4710 */         localPacketStream.writeInt(paramArrayOfValueImpl.length);
/* 4711 */         for (int i = 0; i < paramArrayOfValueImpl.length; i++) {
/* 4712 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4713 */             localPacketStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + paramArrayOfValueImpl[i]);
/*      */           }
/* 4715 */           localPacketStream.writeValue(paramArrayOfValueImpl[i]);
/*      */         }
/* 4717 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4718 */           localPacketStream.vm.printTrace("Sending:                 options(int): " + paramInt);
/*      */         }
/* 4720 */         localPacketStream.writeInt(paramInt);
/* 4721 */         localPacketStream.send();
/* 4722 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static InvokeMethod waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4727 */         paramPacketStream.waitForReply();
/* 4728 */         return new InvokeMethod(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private InvokeMethod(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4743 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4744 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.InvokeMethod" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4746 */         this.returnValue = paramPacketStream.readValue();
/* 4747 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4748 */           paramVirtualMachineImpl.printReceiveTrace(4, "returnValue(ValueImpl): " + this.returnValue);
/*      */         }
/* 4750 */         this.exception = paramPacketStream.readTaggedObjectReference();
/* 4751 */         if (paramVirtualMachineImpl.traceReceives)
/* 4752 */           paramVirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + (this.exception == null ? "NULL" : new StringBuilder().append("ref=").append(this.exception.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class IsCollected
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */       final boolean isCollected;
/*      */ 
/*      */       static IsCollected process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 4872 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl);
/* 4873 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 4878 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 9);
/* 4879 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4880 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.IsCollected" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4882 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4883 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4885 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4886 */         localPacketStream.send();
/* 4887 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static IsCollected waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4892 */         paramPacketStream.waitForReply();
/* 4893 */         return new IsCollected(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private IsCollected(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4903 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4904 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.IsCollected" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4906 */         this.isCollected = paramPacketStream.readBoolean();
/* 4907 */         if (paramVirtualMachineImpl.traceReceives)
/* 4908 */           paramVirtualMachineImpl.printReceiveTrace(4, "isCollected(boolean): " + this.isCollected);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class MonitorInfo
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */       final ThreadReferenceImpl owner;
/*      */       final int entryCount;
/*      */       final ThreadReferenceImpl[] waiters;
/*      */ 
/*      */       static MonitorInfo process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 4542 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl);
/* 4543 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 4548 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 5);
/* 4549 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4550 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.MonitorInfo" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4552 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4553 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4555 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4556 */         localPacketStream.send();
/* 4557 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static MonitorInfo waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4562 */         paramPacketStream.waitForReply();
/* 4563 */         return new MonitorInfo(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private MonitorInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4584 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4585 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.MonitorInfo" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4587 */         this.owner = paramPacketStream.readThreadReference();
/* 4588 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4589 */           paramVirtualMachineImpl.printReceiveTrace(4, "owner(ThreadReferenceImpl): " + (this.owner == null ? "NULL" : new StringBuilder().append("ref=").append(this.owner.ref()).toString()));
/*      */         }
/* 4591 */         this.entryCount = paramPacketStream.readInt();
/* 4592 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4593 */           paramVirtualMachineImpl.printReceiveTrace(4, "entryCount(int): " + this.entryCount);
/*      */         }
/* 4595 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4596 */           paramVirtualMachineImpl.printReceiveTrace(4, "waiters(ThreadReferenceImpl[]): ");
/*      */         }
/* 4598 */         int i = paramPacketStream.readInt();
/* 4599 */         this.waiters = new ThreadReferenceImpl[i];
/* 4600 */         for (int j = 0; j < i; j++) {
/* 4601 */           this.waiters[j] = paramPacketStream.readThreadReference();
/* 4602 */           if (paramVirtualMachineImpl.traceReceives)
/* 4603 */             paramVirtualMachineImpl.printReceiveTrace(5, "waiters[i](ThreadReferenceImpl): " + (this.waiters[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.waiters[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ReferenceType
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final byte refTypeTag;
/*      */       final long typeID;
/*      */ 
/*      */       static ReferenceType process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 4291 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl);
/* 4292 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 4297 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 1);
/* 4298 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4299 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.ReferenceType" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4301 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4302 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4304 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4305 */         localPacketStream.send();
/* 4306 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ReferenceType waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4311 */         paramPacketStream.waitForReply();
/* 4312 */         return new ReferenceType(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ReferenceType(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4328 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4329 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.ReferenceType" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4331 */         this.refTypeTag = paramPacketStream.readByte();
/* 4332 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4333 */           paramVirtualMachineImpl.printReceiveTrace(4, "refTypeTag(byte): " + this.refTypeTag);
/*      */         }
/* 4335 */         this.typeID = paramPacketStream.readClassRef();
/* 4336 */         if (paramVirtualMachineImpl.traceReceives)
/* 4337 */           paramVirtualMachineImpl.printReceiveTrace(4, "typeID(long): ref=" + this.typeID);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ReferringObjects
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */       final ObjectReferenceImpl[] referringObjects;
/*      */ 
/*      */       static ReferringObjects process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 4930 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl, paramInt);
/* 4931 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, int paramInt)
/*      */       {
/* 4937 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 10);
/* 4938 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4939 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.ReferringObjects" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4941 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4942 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4944 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4945 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4946 */           localPacketStream.vm.printTrace("Sending:                 maxReferrers(int): " + paramInt);
/*      */         }
/* 4948 */         localPacketStream.writeInt(paramInt);
/* 4949 */         localPacketStream.send();
/* 4950 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ReferringObjects waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4955 */         paramPacketStream.waitForReply();
/* 4956 */         return new ReferringObjects(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ReferringObjects(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4966 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4967 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.ReferringObjects" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 4969 */         if (paramVirtualMachineImpl.traceReceives) {
/* 4970 */           paramVirtualMachineImpl.printReceiveTrace(4, "referringObjects(ObjectReferenceImpl[]): ");
/*      */         }
/* 4972 */         int i = paramPacketStream.readInt();
/* 4973 */         this.referringObjects = new ObjectReferenceImpl[i];
/* 4974 */         for (int j = 0; j < i; j++) {
/* 4975 */           this.referringObjects[j] = paramPacketStream.readTaggedObjectReference();
/* 4976 */           if (paramVirtualMachineImpl.traceReceives)
/* 4977 */             paramVirtualMachineImpl.printReceiveTrace(5, "referringObjects[i](ObjectReferenceImpl): " + (this.referringObjects[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.referringObjects[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */ 
/*      */       static SetValues process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, FieldValue[] paramArrayOfFieldValue)
/*      */         throws JDWPException
/*      */       {
/* 4487 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl, paramArrayOfFieldValue);
/* 4488 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl, FieldValue[] paramArrayOfFieldValue)
/*      */       {
/* 4494 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 9, 3);
/* 4495 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4496 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ObjectReference.SetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 4498 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4499 */           localPacketStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 4501 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 4502 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4503 */           localPacketStream.vm.printTrace("Sending:                 values(FieldValue[]): ");
/*      */         }
/* 4505 */         localPacketStream.writeInt(paramArrayOfFieldValue.length);
/* 4506 */         for (int i = 0; i < paramArrayOfFieldValue.length; i++) {
/* 4507 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4508 */             localPacketStream.vm.printTrace("Sending:                     values[i](FieldValue): ");
/*      */           }
/* 4510 */           paramArrayOfFieldValue[i].write(localPacketStream);
/*      */         }
/* 4512 */         localPacketStream.send();
/* 4513 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 4518 */         paramPacketStream.waitForReply();
/* 4519 */         return new SetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 4524 */         if (paramVirtualMachineImpl.traceReceives)
/* 4525 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ObjectReference.SetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */ 
/*      */       static class FieldValue
/*      */       {
/*      */         final long fieldID;
/*      */         final ValueImpl value;
/*      */ 
/*      */         FieldValue(long paramLong, ValueImpl paramValueImpl)
/*      */         {
/* 4467 */           this.fieldID = paramLong;
/* 4468 */           this.value = paramValueImpl;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 4472 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4473 */             paramPacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 4475 */           paramPacketStream.writeFieldRef(this.fieldID);
/* 4476 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 4477 */             paramPacketStream.vm.printTrace("Sending:                     value(ValueImpl): " + this.value);
/*      */           }
/* 4479 */           paramPacketStream.writeUntaggedValue(this.value);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ReferenceType
/*      */   {
/*      */     static final int COMMAND_SET = 2;
/*      */ 
/*      */     static class ClassFileVersion
/*      */     {
/*      */       static final int COMMAND = 17;
/*      */       final int majorVersion;
/*      */       final int minorVersion;
/*      */ 
/*      */       static ClassFileVersion process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2985 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2986 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2991 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 17);
/* 2992 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2993 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.ClassFileVersion" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2995 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2996 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2998 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2999 */         localPacketStream.send();
/* 3000 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ClassFileVersion waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3005 */         paramPacketStream.waitForReply();
/* 3006 */         return new ClassFileVersion(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ClassFileVersion(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3021 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3022 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.ClassFileVersion" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3024 */         this.majorVersion = paramPacketStream.readInt();
/* 3025 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3026 */           paramVirtualMachineImpl.printReceiveTrace(4, "majorVersion(int): " + this.majorVersion);
/*      */         }
/* 3028 */         this.minorVersion = paramPacketStream.readInt();
/* 3029 */         if (paramVirtualMachineImpl.traceReceives)
/* 3030 */           paramVirtualMachineImpl.printReceiveTrace(4, "minorVersion(int): " + this.minorVersion);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ClassLoader
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */       final ClassLoaderReferenceImpl classLoader;
/*      */ 
/*      */       static ClassLoader process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 1824 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 1825 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 1830 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 2);
/* 1831 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1832 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.ClassLoader" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1834 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1835 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 1837 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 1838 */         localPacketStream.send();
/* 1839 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ClassLoader waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1844 */         paramPacketStream.waitForReply();
/* 1845 */         return new ClassLoader(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ClassLoader(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1855 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1856 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.ClassLoader" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 1858 */         this.classLoader = paramPacketStream.readClassLoaderReference();
/* 1859 */         if (paramVirtualMachineImpl.traceReceives)
/* 1860 */           paramVirtualMachineImpl.printReceiveTrace(4, "classLoader(ClassLoaderReferenceImpl): " + (this.classLoader == null ? "NULL" : new StringBuilder().append("ref=").append(this.classLoader.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ClassObject
/*      */     {
/*      */       static final int COMMAND = 11;
/*      */       final ClassObjectReferenceImpl classObject;
/*      */ 
/*      */       static ClassObject process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2497 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2498 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2503 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 11);
/* 2504 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2505 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.ClassObject" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2507 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2508 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2510 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2511 */         localPacketStream.send();
/* 2512 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ClassObject waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2517 */         paramPacketStream.waitForReply();
/* 2518 */         return new ClassObject(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ClassObject(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2528 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2529 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.ClassObject" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2531 */         this.classObject = paramPacketStream.readClassObjectReference();
/* 2532 */         if (paramVirtualMachineImpl.traceReceives)
/* 2533 */           paramVirtualMachineImpl.printReceiveTrace(4, "classObject(ClassObjectReferenceImpl): " + (this.classObject == null ? "NULL" : new StringBuilder().append("ref=").append(this.classObject.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ConstantPool
/*      */     {
/*      */       static final int COMMAND = 18;
/*      */       final int count;
/*      */       final byte[] bytes;
/*      */ 
/*      */       static ConstantPool process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 3049 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 3050 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 3055 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 18);
/* 3056 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3057 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.ConstantPool" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 3059 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 3060 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 3062 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 3063 */         localPacketStream.send();
/* 3064 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ConstantPool waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 3069 */         paramPacketStream.waitForReply();
/* 3070 */         return new ConstantPool(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ConstantPool(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 3085 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3086 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.ConstantPool" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 3088 */         this.count = paramPacketStream.readInt();
/* 3089 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3090 */           paramVirtualMachineImpl.printReceiveTrace(4, "count(int): " + this.count);
/*      */         }
/* 3092 */         if (paramVirtualMachineImpl.traceReceives) {
/* 3093 */           paramVirtualMachineImpl.printReceiveTrace(4, "bytes(byte[]): ");
/*      */         }
/* 3095 */         int i = paramPacketStream.readInt();
/* 3096 */         this.bytes = new byte[i];
/* 3097 */         for (int j = 0; j < i; j++) {
/* 3098 */           this.bytes[j] = paramPacketStream.readByte();
/* 3099 */           if (paramVirtualMachineImpl.traceReceives)
/* 3100 */             paramVirtualMachineImpl.printReceiveTrace(5, "bytes[i](byte): " + this.bytes[j]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Fields
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */       final FieldInfo[] declared;
/*      */ 
/*      */       static Fields process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 1933 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 1934 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 1939 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 4);
/* 1940 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1941 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Fields" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1943 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1944 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 1946 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 1947 */         localPacketStream.send();
/* 1948 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Fields waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1953 */         paramPacketStream.waitForReply();
/* 1954 */         return new Fields(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Fields(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2013 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2014 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Fields" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2016 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2017 */           paramVirtualMachineImpl.printReceiveTrace(4, "declared(FieldInfo[]): ");
/*      */         }
/* 2019 */         int i = paramPacketStream.readInt();
/* 2020 */         this.declared = new FieldInfo[i];
/* 2021 */         for (int j = 0; j < i; j++) {
/* 2022 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2023 */             paramVirtualMachineImpl.printReceiveTrace(5, "declared[i](FieldInfo): ");
/*      */           }
/* 2025 */           this.declared[j] = new FieldInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class FieldInfo
/*      */       {
/*      */         final long fieldID;
/*      */         final String name;
/*      */         final String signature;
/*      */         final int modBits;
/*      */ 
/*      */         private FieldInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 1987 */           this.fieldID = paramPacketStream.readFieldRef();
/* 1988 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1989 */             paramVirtualMachineImpl.printReceiveTrace(5, "fieldID(long): " + this.fieldID);
/*      */           }
/* 1991 */           this.name = paramPacketStream.readString();
/* 1992 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1993 */             paramVirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 1995 */           this.signature = paramPacketStream.readString();
/* 1996 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1997 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 1999 */           this.modBits = paramPacketStream.readInt();
/* 2000 */           if (paramVirtualMachineImpl.traceReceives)
/* 2001 */             paramVirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class FieldsWithGeneric
/*      */     {
/*      */       static final int COMMAND = 14;
/*      */       final FieldInfo[] declared;
/*      */ 
/*      */       static FieldsWithGeneric process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2674 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2675 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2680 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 14);
/* 2681 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2682 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.FieldsWithGeneric" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2684 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2685 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2687 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2688 */         localPacketStream.send();
/* 2689 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static FieldsWithGeneric waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2694 */         paramPacketStream.waitForReply();
/* 2695 */         return new FieldsWithGeneric(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private FieldsWithGeneric(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2764 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2765 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.FieldsWithGeneric" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2767 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2768 */           paramVirtualMachineImpl.printReceiveTrace(4, "declared(FieldInfo[]): ");
/*      */         }
/* 2770 */         int i = paramPacketStream.readInt();
/* 2771 */         this.declared = new FieldInfo[i];
/* 2772 */         for (int j = 0; j < i; j++) {
/* 2773 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2774 */             paramVirtualMachineImpl.printReceiveTrace(5, "declared[i](FieldInfo): ");
/*      */           }
/* 2776 */           this.declared[j] = new FieldInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class FieldInfo
/*      */       {
/*      */         final long fieldID;
/*      */         final String name;
/*      */         final String signature;
/*      */         final String genericSignature;
/*      */         final int modBits;
/*      */ 
/*      */         private FieldInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 2734 */           this.fieldID = paramPacketStream.readFieldRef();
/* 2735 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2736 */             paramVirtualMachineImpl.printReceiveTrace(5, "fieldID(long): " + this.fieldID);
/*      */           }
/* 2738 */           this.name = paramPacketStream.readString();
/* 2739 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2740 */             paramVirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 2742 */           this.signature = paramPacketStream.readString();
/* 2743 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2744 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 2746 */           this.genericSignature = paramPacketStream.readString();
/* 2747 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2748 */             paramVirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 2750 */           this.modBits = paramPacketStream.readInt();
/* 2751 */           if (paramVirtualMachineImpl.traceReceives)
/* 2752 */             paramVirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 6;
/*      */       final ValueImpl[] values;
/*      */ 
/*      */       static GetValues process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, Field[] paramArrayOfField)
/*      */         throws JDWPException
/*      */       {
/* 2174 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramArrayOfField);
/* 2175 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, Field[] paramArrayOfField)
/*      */       {
/* 2181 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 6);
/* 2182 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2183 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.GetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2185 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2186 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2188 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2189 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2190 */           localPacketStream.vm.printTrace("Sending:                 fields(Field[]): ");
/*      */         }
/* 2192 */         localPacketStream.writeInt(paramArrayOfField.length);
/* 2193 */         for (int i = 0; i < paramArrayOfField.length; i++) {
/* 2194 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2195 */             localPacketStream.vm.printTrace("Sending:                     fields[i](Field): ");
/*      */           }
/* 2197 */           paramArrayOfField[i].write(localPacketStream);
/*      */         }
/* 2199 */         localPacketStream.send();
/* 2200 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static GetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2205 */         paramPacketStream.waitForReply();
/* 2206 */         return new GetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private GetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2217 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2218 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.GetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2220 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2221 */           paramVirtualMachineImpl.printReceiveTrace(4, "values(ValueImpl[]): ");
/*      */         }
/* 2223 */         int i = paramPacketStream.readInt();
/* 2224 */         this.values = new ValueImpl[i];
/* 2225 */         for (int j = 0; j < i; j++) {
/* 2226 */           this.values[j] = paramPacketStream.readValue();
/* 2227 */           if (paramVirtualMachineImpl.traceReceives)
/* 2228 */             paramVirtualMachineImpl.printReceiveTrace(5, "values[i](ValueImpl): " + this.values[j]);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class Field
/*      */       {
/*      */         final long fieldID;
/*      */ 
/*      */         Field(long paramLong)
/*      */         {
/* 2159 */           this.fieldID = paramLong;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 2163 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2164 */             paramPacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 2166 */           paramPacketStream.writeFieldRef(this.fieldID);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Instances
/*      */     {
/*      */       static final int COMMAND = 16;
/*      */       final ObjectReferenceImpl[] instances;
/*      */ 
/*      */       static Instances process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/* 2921 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl, paramInt);
/* 2922 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl, int paramInt)
/*      */       {
/* 2928 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 16);
/* 2929 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2930 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Instances" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2932 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2933 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2935 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2936 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2937 */           localPacketStream.vm.printTrace("Sending:                 maxInstances(int): " + paramInt);
/*      */         }
/* 2939 */         localPacketStream.writeInt(paramInt);
/* 2940 */         localPacketStream.send();
/* 2941 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Instances waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2946 */         paramPacketStream.waitForReply();
/* 2947 */         return new Instances(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Instances(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2957 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2958 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Instances" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2960 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2961 */           paramVirtualMachineImpl.printReceiveTrace(4, "instances(ObjectReferenceImpl[]): ");
/*      */         }
/* 2963 */         int i = paramPacketStream.readInt();
/* 2964 */         this.instances = new ObjectReferenceImpl[i];
/* 2965 */         for (int j = 0; j < i; j++) {
/* 2966 */           this.instances[j] = paramPacketStream.readTaggedObjectReference();
/* 2967 */           if (paramVirtualMachineImpl.traceReceives)
/* 2968 */             paramVirtualMachineImpl.printReceiveTrace(5, "instances[i](ObjectReferenceImpl): " + (this.instances[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.instances[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Interfaces
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */       final InterfaceTypeImpl[] interfaces;
/*      */ 
/*      */       static Interfaces process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2440 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2441 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2446 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 10);
/* 2447 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2448 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Interfaces" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2450 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2451 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2453 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2454 */         localPacketStream.send();
/* 2455 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Interfaces waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2460 */         paramPacketStream.waitForReply();
/* 2461 */         return new Interfaces(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Interfaces(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2471 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2472 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Interfaces" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2474 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2475 */           paramVirtualMachineImpl.printReceiveTrace(4, "interfaces(InterfaceTypeImpl[]): ");
/*      */         }
/* 2477 */         int i = paramPacketStream.readInt();
/* 2478 */         this.interfaces = new InterfaceTypeImpl[i];
/* 2479 */         for (int j = 0; j < i; j++) {
/* 2480 */           this.interfaces[j] = paramVirtualMachineImpl.interfaceType(paramPacketStream.readClassRef());
/* 2481 */           if (paramVirtualMachineImpl.traceReceives)
/* 2482 */             paramVirtualMachineImpl.printReceiveTrace(5, "interfaces[i](InterfaceTypeImpl): " + (this.interfaces[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.interfaces[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Methods
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */       final MethodInfo[] declared;
/*      */ 
/*      */       static Methods process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2044 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2045 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2050 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 5);
/* 2051 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2052 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Methods" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2054 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2055 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2057 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2058 */         localPacketStream.send();
/* 2059 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Methods waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2064 */         paramPacketStream.waitForReply();
/* 2065 */         return new Methods(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Methods(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2124 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2125 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Methods" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2127 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2128 */           paramVirtualMachineImpl.printReceiveTrace(4, "declared(MethodInfo[]): ");
/*      */         }
/* 2130 */         int i = paramPacketStream.readInt();
/* 2131 */         this.declared = new MethodInfo[i];
/* 2132 */         for (int j = 0; j < i; j++) {
/* 2133 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2134 */             paramVirtualMachineImpl.printReceiveTrace(5, "declared[i](MethodInfo): ");
/*      */           }
/* 2136 */           this.declared[j] = new MethodInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class MethodInfo
/*      */       {
/*      */         final long methodID;
/*      */         final String name;
/*      */         final String signature;
/*      */         final int modBits;
/*      */ 
/*      */         private MethodInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 2098 */           this.methodID = paramPacketStream.readMethodRef();
/* 2099 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2100 */             paramVirtualMachineImpl.printReceiveTrace(5, "methodID(long): " + this.methodID);
/*      */           }
/* 2102 */           this.name = paramPacketStream.readString();
/* 2103 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2104 */             paramVirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 2106 */           this.signature = paramPacketStream.readString();
/* 2107 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2108 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 2110 */           this.modBits = paramPacketStream.readInt();
/* 2111 */           if (paramVirtualMachineImpl.traceReceives)
/* 2112 */             paramVirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class MethodsWithGeneric
/*      */     {
/*      */       static final int COMMAND = 15;
/*      */       final MethodInfo[] declared;
/*      */ 
/*      */       static MethodsWithGeneric process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2800 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2801 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2806 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 15);
/* 2807 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2808 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.MethodsWithGeneric" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2810 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2811 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2813 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2814 */         localPacketStream.send();
/* 2815 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static MethodsWithGeneric waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2820 */         paramPacketStream.waitForReply();
/* 2821 */         return new MethodsWithGeneric(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private MethodsWithGeneric(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2890 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2891 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.MethodsWithGeneric" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2893 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2894 */           paramVirtualMachineImpl.printReceiveTrace(4, "declared(MethodInfo[]): ");
/*      */         }
/* 2896 */         int i = paramPacketStream.readInt();
/* 2897 */         this.declared = new MethodInfo[i];
/* 2898 */         for (int j = 0; j < i; j++) {
/* 2899 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2900 */             paramVirtualMachineImpl.printReceiveTrace(5, "declared[i](MethodInfo): ");
/*      */           }
/* 2902 */           this.declared[j] = new MethodInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class MethodInfo
/*      */       {
/*      */         final long methodID;
/*      */         final String name;
/*      */         final String signature;
/*      */         final String genericSignature;
/*      */         final int modBits;
/*      */ 
/*      */         private MethodInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 2860 */           this.methodID = paramPacketStream.readMethodRef();
/* 2861 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2862 */             paramVirtualMachineImpl.printReceiveTrace(5, "methodID(long): " + this.methodID);
/*      */           }
/* 2864 */           this.name = paramPacketStream.readString();
/* 2865 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2866 */             paramVirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 2868 */           this.signature = paramPacketStream.readString();
/* 2869 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2870 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 2872 */           this.genericSignature = paramPacketStream.readString();
/* 2873 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2874 */             paramVirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 2876 */           this.modBits = paramPacketStream.readInt();
/* 2877 */           if (paramVirtualMachineImpl.traceReceives)
/* 2878 */             paramVirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Modifiers
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */       final int modBits;
/*      */ 
/*      */       static Modifiers process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 1878 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 1879 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 1884 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 3);
/* 1885 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1886 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Modifiers" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1888 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1889 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 1891 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 1892 */         localPacketStream.send();
/* 1893 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Modifiers waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1898 */         paramPacketStream.waitForReply();
/* 1899 */         return new Modifiers(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Modifiers(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1910 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1911 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Modifiers" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 1913 */         this.modBits = paramPacketStream.readInt();
/* 1914 */         if (paramVirtualMachineImpl.traceReceives)
/* 1915 */           paramVirtualMachineImpl.printReceiveTrace(4, "modBits(int): " + this.modBits);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class NestedTypes
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */       final TypeInfo[] classes;
/*      */ 
/*      */       static NestedTypes process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2296 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2297 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2302 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 8);
/* 2303 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2304 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.NestedTypes" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2306 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2307 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2309 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2310 */         localPacketStream.send();
/* 2311 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static NestedTypes waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2316 */         paramPacketStream.waitForReply();
/* 2317 */         return new NestedTypes(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private NestedTypes(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2352 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2353 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.NestedTypes" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2355 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2356 */           paramVirtualMachineImpl.printReceiveTrace(4, "classes(TypeInfo[]): ");
/*      */         }
/* 2358 */         int i = paramPacketStream.readInt();
/* 2359 */         this.classes = new TypeInfo[i];
/* 2360 */         for (int j = 0; j < i; j++) {
/* 2361 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2362 */             paramVirtualMachineImpl.printReceiveTrace(5, "classes[i](TypeInfo): ");
/*      */           }
/* 2364 */           this.classes[j] = new TypeInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class TypeInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */         final long typeID;
/*      */ 
/*      */         private TypeInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 2334 */           this.refTypeTag = paramPacketStream.readByte();
/* 2335 */           if (paramVirtualMachineImpl.traceReceives) {
/* 2336 */             paramVirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/* 2338 */           this.typeID = paramPacketStream.readClassRef();
/* 2339 */           if (paramVirtualMachineImpl.traceReceives)
/* 2340 */             paramVirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Signature
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final String signature;
/*      */ 
/*      */       static Signature process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 1772 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 1773 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 1778 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 1);
/* 1779 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1780 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Signature" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1782 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1783 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 1785 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 1786 */         localPacketStream.send();
/* 1787 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Signature waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1792 */         paramPacketStream.waitForReply();
/* 1793 */         return new Signature(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Signature(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1803 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1804 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Signature" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 1806 */         this.signature = paramPacketStream.readString();
/* 1807 */         if (paramVirtualMachineImpl.traceReceives)
/* 1808 */           paramVirtualMachineImpl.printReceiveTrace(4, "signature(String): " + this.signature);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SignatureWithGeneric
/*      */     {
/*      */       static final int COMMAND = 13;
/*      */       final String signature;
/*      */       final String genericSignature;
/*      */ 
/*      */       static SignatureWithGeneric process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2605 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2606 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2611 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 13);
/* 2612 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2613 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.SignatureWithGeneric" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2615 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2616 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2618 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2619 */         localPacketStream.send();
/* 2620 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SignatureWithGeneric waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2625 */         paramPacketStream.waitForReply();
/* 2626 */         return new SignatureWithGeneric(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SignatureWithGeneric(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2642 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2643 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.SignatureWithGeneric" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2645 */         this.signature = paramPacketStream.readString();
/* 2646 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2647 */           paramVirtualMachineImpl.printReceiveTrace(4, "signature(String): " + this.signature);
/*      */         }
/* 2649 */         this.genericSignature = paramPacketStream.readString();
/* 2650 */         if (paramVirtualMachineImpl.traceReceives)
/* 2651 */           paramVirtualMachineImpl.printReceiveTrace(4, "genericSignature(String): " + this.genericSignature);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SourceDebugExtension
/*      */     {
/*      */       static final int COMMAND = 12;
/*      */       final String extension;
/*      */ 
/*      */       static SourceDebugExtension process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2549 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2550 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2555 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 12);
/* 2556 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2557 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.SourceDebugExtension" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2559 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2560 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2562 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2563 */         localPacketStream.send();
/* 2564 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SourceDebugExtension waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2569 */         paramPacketStream.waitForReply();
/* 2570 */         return new SourceDebugExtension(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SourceDebugExtension(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2580 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2581 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.SourceDebugExtension" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2583 */         this.extension = paramPacketStream.readString();
/* 2584 */         if (paramVirtualMachineImpl.traceReceives)
/* 2585 */           paramVirtualMachineImpl.printReceiveTrace(4, "extension(String): " + this.extension);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SourceFile
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */       final String sourceFile;
/*      */ 
/*      */       static SourceFile process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2244 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2245 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2250 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 7);
/* 2251 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2252 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.SourceFile" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2254 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2255 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2257 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2258 */         localPacketStream.send();
/* 2259 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SourceFile waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2264 */         paramPacketStream.waitForReply();
/* 2265 */         return new SourceFile(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SourceFile(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2276 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2277 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.SourceFile" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2279 */         this.sourceFile = paramPacketStream.readString();
/* 2280 */         if (paramVirtualMachineImpl.traceReceives)
/* 2281 */           paramVirtualMachineImpl.printReceiveTrace(4, "sourceFile(String): " + this.sourceFile);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Status
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */       final int status;
/*      */ 
/*      */       static Status process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 2387 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramReferenceTypeImpl);
/* 2388 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl paramReferenceTypeImpl)
/*      */       {
/* 2393 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 2, 9);
/* 2394 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2395 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ReferenceType.Status" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 2397 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 2398 */           localPacketStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + (paramReferenceTypeImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramReferenceTypeImpl.ref()).toString()));
/*      */         }
/* 2400 */         localPacketStream.writeClassRef(paramReferenceTypeImpl.ref());
/* 2401 */         localPacketStream.send();
/* 2402 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Status waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 2407 */         paramPacketStream.waitForReply();
/* 2408 */         return new Status(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Status(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 2419 */         if (paramVirtualMachineImpl.traceReceives) {
/* 2420 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ReferenceType.Status" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 2422 */         this.status = paramPacketStream.readInt();
/* 2423 */         if (paramVirtualMachineImpl.traceReceives)
/* 2424 */           paramVirtualMachineImpl.printReceiveTrace(4, "status(int): " + this.status);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StackFrame
/*      */   {
/*      */     static final int COMMAND_SET = 16;
/*      */ 
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final ValueImpl[] values;
/*      */ 
/*      */       static GetValues process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, SlotInfo[] paramArrayOfSlotInfo)
/*      */         throws JDWPException
/*      */       {
/* 7070 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramLong, paramArrayOfSlotInfo);
/* 7071 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, SlotInfo[] paramArrayOfSlotInfo)
/*      */       {
/* 7078 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 16, 1);
/* 7079 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7080 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.StackFrame.GetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 7082 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7083 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 7085 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 7086 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7087 */           localPacketStream.vm.printTrace("Sending:                 frame(long): " + paramLong);
/*      */         }
/* 7089 */         localPacketStream.writeFrameRef(paramLong);
/* 7090 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7091 */           localPacketStream.vm.printTrace("Sending:                 slots(SlotInfo[]): ");
/*      */         }
/* 7093 */         localPacketStream.writeInt(paramArrayOfSlotInfo.length);
/* 7094 */         for (int i = 0; i < paramArrayOfSlotInfo.length; i++) {
/* 7095 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7096 */             localPacketStream.vm.printTrace("Sending:                     slots[i](SlotInfo): ");
/*      */           }
/* 7098 */           paramArrayOfSlotInfo[i].write(localPacketStream);
/*      */         }
/* 7100 */         localPacketStream.send();
/* 7101 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static GetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 7106 */         paramPacketStream.waitForReply();
/* 7107 */         return new GetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private GetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 7118 */         if (paramVirtualMachineImpl.traceReceives) {
/* 7119 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.StackFrame.GetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 7121 */         if (paramVirtualMachineImpl.traceReceives) {
/* 7122 */           paramVirtualMachineImpl.printReceiveTrace(4, "values(ValueImpl[]): ");
/*      */         }
/* 7124 */         int i = paramPacketStream.readInt();
/* 7125 */         this.values = new ValueImpl[i];
/* 7126 */         for (int j = 0; j < i; j++) {
/* 7127 */           this.values[j] = paramPacketStream.readValue();
/* 7128 */           if (paramVirtualMachineImpl.traceReceives)
/* 7129 */             paramVirtualMachineImpl.printReceiveTrace(5, "values[i](ValueImpl): " + this.values[j]);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class SlotInfo
/*      */       {
/*      */         final int slot;
/*      */         final byte sigbyte;
/*      */ 
/*      */         SlotInfo(int paramInt, byte paramByte)
/*      */         {
/* 7049 */           this.slot = paramInt;
/* 7050 */           this.sigbyte = paramByte;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 7054 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7055 */             paramPacketStream.vm.printTrace("Sending:                     slot(int): " + this.slot);
/*      */           }
/* 7057 */           paramPacketStream.writeInt(this.slot);
/* 7058 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7059 */             paramPacketStream.vm.printTrace("Sending:                     sigbyte(byte): " + this.sigbyte);
/*      */           }
/* 7061 */           paramPacketStream.writeByte(this.sigbyte);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class PopFrames
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */ 
/*      */       static PopFrames process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 7311 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramLong);
/* 7312 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong)
/*      */       {
/* 7318 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 16, 4);
/* 7319 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7320 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.StackFrame.PopFrames" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 7322 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7323 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 7325 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 7326 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7327 */           localPacketStream.vm.printTrace("Sending:                 frame(long): " + paramLong);
/*      */         }
/* 7329 */         localPacketStream.writeFrameRef(paramLong);
/* 7330 */         localPacketStream.send();
/* 7331 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static PopFrames waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 7336 */         paramPacketStream.waitForReply();
/* 7337 */         return new PopFrames(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private PopFrames(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 7342 */         if (paramVirtualMachineImpl.traceReceives)
/* 7343 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.StackFrame.PopFrames" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */ 
/*      */       static SetValues process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, SlotInfo[] paramArrayOfSlotInfo)
/*      */         throws JDWPException
/*      */       {
/* 7186 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramLong, paramArrayOfSlotInfo);
/* 7187 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, SlotInfo[] paramArrayOfSlotInfo)
/*      */       {
/* 7194 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 16, 2);
/* 7195 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7196 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.StackFrame.SetValues" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 7198 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7199 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 7201 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 7202 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7203 */           localPacketStream.vm.printTrace("Sending:                 frame(long): " + paramLong);
/*      */         }
/* 7205 */         localPacketStream.writeFrameRef(paramLong);
/* 7206 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7207 */           localPacketStream.vm.printTrace("Sending:                 slotValues(SlotInfo[]): ");
/*      */         }
/* 7209 */         localPacketStream.writeInt(paramArrayOfSlotInfo.length);
/* 7210 */         for (int i = 0; i < paramArrayOfSlotInfo.length; i++) {
/* 7211 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7212 */             localPacketStream.vm.printTrace("Sending:                     slotValues[i](SlotInfo): ");
/*      */           }
/* 7214 */           paramArrayOfSlotInfo[i].write(localPacketStream);
/*      */         }
/* 7216 */         localPacketStream.send();
/* 7217 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SetValues waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 7222 */         paramPacketStream.waitForReply();
/* 7223 */         return new SetValues(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SetValues(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 7228 */         if (paramVirtualMachineImpl.traceReceives)
/* 7229 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.StackFrame.SetValues" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */ 
/*      */       static class SlotInfo
/*      */       {
/*      */         final int slot;
/*      */         final ValueImpl slotValue;
/*      */ 
/*      */         SlotInfo(int paramInt, ValueImpl paramValueImpl)
/*      */         {
/* 7165 */           this.slot = paramInt;
/* 7166 */           this.slotValue = paramValueImpl;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 7170 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7171 */             paramPacketStream.vm.printTrace("Sending:                     slot(int): " + this.slot);
/*      */           }
/* 7173 */           paramPacketStream.writeInt(this.slot);
/* 7174 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7175 */             paramPacketStream.vm.printTrace("Sending:                     slotValue(ValueImpl): " + this.slotValue);
/*      */           }
/* 7177 */           paramPacketStream.writeValue(this.slotValue);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ThisObject
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */       final ObjectReferenceImpl objectThis;
/*      */ 
/*      */       static ThisObject process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong)
/*      */         throws JDWPException
/*      */       {
/* 7246 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramLong);
/* 7247 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong)
/*      */       {
/* 7253 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 16, 3);
/* 7254 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7255 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.StackFrame.ThisObject" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 7257 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7258 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 7260 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 7261 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 7262 */           localPacketStream.vm.printTrace("Sending:                 frame(long): " + paramLong);
/*      */         }
/* 7264 */         localPacketStream.writeFrameRef(paramLong);
/* 7265 */         localPacketStream.send();
/* 7266 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ThisObject waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 7271 */         paramPacketStream.waitForReply();
/* 7272 */         return new ThisObject(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ThisObject(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 7282 */         if (paramVirtualMachineImpl.traceReceives) {
/* 7283 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.StackFrame.ThisObject" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 7285 */         this.objectThis = paramPacketStream.readTaggedObjectReference();
/* 7286 */         if (paramVirtualMachineImpl.traceReceives)
/* 7287 */           paramVirtualMachineImpl.printReceiveTrace(4, "objectThis(ObjectReferenceImpl): " + (this.objectThis == null ? "NULL" : new StringBuilder().append("ref=").append(this.objectThis.ref()).toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StepDepth
/*      */   {
/*      */     static final int INTO = 0;
/*      */     static final int OVER = 1;
/*      */     static final int OUT = 2;
/*      */   }
/*      */ 
/*      */   static class StepSize
/*      */   {
/*      */     static final int MIN = 0;
/*      */     static final int LINE = 1;
/*      */   }
/*      */ 
/*      */   static class StringReference
/*      */   {
/*      */     static final int COMMAND_SET = 10;
/*      */ 
/*      */     static class Value
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final String stringValue;
/*      */ 
/*      */       static Value process(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 4997 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramObjectReferenceImpl);
/* 4998 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 5003 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 10, 1);
/* 5004 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5005 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.StringReference.Value" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5007 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5008 */           localPacketStream.vm.printTrace("Sending:                 stringObject(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 5010 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 5011 */         localPacketStream.send();
/* 5012 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Value waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5017 */         paramPacketStream.waitForReply();
/* 5018 */         return new Value(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Value(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5028 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5029 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.StringReference.Value" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5031 */         this.stringValue = paramPacketStream.readString();
/* 5032 */         if (paramVirtualMachineImpl.traceReceives)
/* 5033 */           paramVirtualMachineImpl.printReceiveTrace(4, "stringValue(String): " + this.stringValue);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SuspendPolicy
/*      */   {
/*      */     static final int NONE = 0;
/*      */     static final int EVENT_THREAD = 1;
/*      */     static final int ALL = 2;
/*      */   }
/*      */ 
/*      */   static class SuspendStatus
/*      */   {
/*      */     static final int SUSPEND_STATUS_SUSPENDED = 1;
/*      */   }
/*      */ 
/*      */   static class Tag
/*      */   {
/*      */     static final int ARRAY = 91;
/*      */     static final int BYTE = 66;
/*      */     static final int CHAR = 67;
/*      */     static final int OBJECT = 76;
/*      */     static final int FLOAT = 70;
/*      */     static final int DOUBLE = 68;
/*      */     static final int INT = 73;
/*      */     static final int LONG = 74;
/*      */     static final int SHORT = 83;
/*      */     static final int VOID = 86;
/*      */     static final int BOOLEAN = 90;
/*      */     static final int STRING = 115;
/*      */     static final int THREAD = 116;
/*      */     static final int THREAD_GROUP = 103;
/*      */     static final int CLASS_LOADER = 108;
/*      */     static final int CLASS_OBJECT = 99;
/*      */   }
/*      */ 
/*      */   static class ThreadGroupReference
/*      */   {
/*      */     static final int COMMAND_SET = 12;
/*      */ 
/*      */     static class Children
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */       final ThreadReferenceImpl[] childThreads;
/*      */       final ThreadGroupReferenceImpl[] childGroups;
/*      */ 
/*      */       static Children process(VirtualMachineImpl paramVirtualMachineImpl, ThreadGroupReferenceImpl paramThreadGroupReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 6022 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadGroupReferenceImpl);
/* 6023 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadGroupReferenceImpl paramThreadGroupReferenceImpl)
/*      */       {
/* 6028 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 12, 3);
/* 6029 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6030 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadGroupReference.Children" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 6032 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 6033 */           localPacketStream.vm.printTrace("Sending:                 group(ThreadGroupReferenceImpl): " + (paramThreadGroupReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadGroupReferenceImpl.ref()).toString()));
/*      */         }
/* 6035 */         localPacketStream.writeObjectRef(paramThreadGroupReferenceImpl.ref());
/* 6036 */         localPacketStream.send();
/* 6037 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Children waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 6042 */         paramPacketStream.waitForReply();
/* 6043 */         return new Children(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Children(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 6058 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6059 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadGroupReference.Children" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 6061 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6062 */           paramVirtualMachineImpl.printReceiveTrace(4, "childThreads(ThreadReferenceImpl[]): ");
/*      */         }
/* 6064 */         int i = paramPacketStream.readInt();
/* 6065 */         this.childThreads = new ThreadReferenceImpl[i];
/* 6066 */         for (int j = 0; j < i; j++) {
/* 6067 */           this.childThreads[j] = paramPacketStream.readThreadReference();
/* 6068 */           if (paramVirtualMachineImpl.traceReceives) {
/* 6069 */             paramVirtualMachineImpl.printReceiveTrace(5, "childThreads[i](ThreadReferenceImpl): " + (this.childThreads[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.childThreads[j].ref()).toString()));
/*      */           }
/*      */         }
/* 6072 */         if (paramVirtualMachineImpl.traceReceives) {
/* 6073 */           paramVirtualMachineImpl.printReceiveTrace(4, "childGroups(ThreadGroupReferenceImpl[]): ");
/*      */         }
/* 6075 */         j = paramPacketStream.readInt();
/* 6076 */         this.childGroups = new ThreadGroupReferenceImpl[j];
/* 6077 */         for (int k = 0; k < j; k++) {
/* 6078 */           this.childGroups[k] = paramPacketStream.readThreadGroupReference();
/* 6079 */           if (paramVirtualMachineImpl.traceReceives)
/* 6080 */             paramVirtualMachineImpl.printReceiveTrace(5, "childGroups[i](ThreadGroupReferenceImpl): " + (this.childGroups[k] == null ? "NULL" : new StringBuilder().append("ref=").append(this.childGroups[k].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Name
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final String groupName;
/*      */ 
/*      */       static Name process(VirtualMachineImpl paramVirtualMachineImpl, ThreadGroupReferenceImpl paramThreadGroupReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5915 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadGroupReferenceImpl);
/* 5916 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadGroupReferenceImpl paramThreadGroupReferenceImpl)
/*      */       {
/* 5921 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 12, 1);
/* 5922 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5923 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadGroupReference.Name" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5925 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5926 */           localPacketStream.vm.printTrace("Sending:                 group(ThreadGroupReferenceImpl): " + (paramThreadGroupReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadGroupReferenceImpl.ref()).toString()));
/*      */         }
/* 5928 */         localPacketStream.writeObjectRef(paramThreadGroupReferenceImpl.ref());
/* 5929 */         localPacketStream.send();
/* 5930 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Name waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5935 */         paramPacketStream.waitForReply();
/* 5936 */         return new Name(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Name(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5946 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5947 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadGroupReference.Name" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5949 */         this.groupName = paramPacketStream.readString();
/* 5950 */         if (paramVirtualMachineImpl.traceReceives)
/* 5951 */           paramVirtualMachineImpl.printReceiveTrace(4, "groupName(String): " + this.groupName);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Parent
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */       final ThreadGroupReferenceImpl parentGroup;
/*      */ 
/*      */       static Parent process(VirtualMachineImpl paramVirtualMachineImpl, ThreadGroupReferenceImpl paramThreadGroupReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5965 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadGroupReferenceImpl);
/* 5966 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadGroupReferenceImpl paramThreadGroupReferenceImpl)
/*      */       {
/* 5971 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 12, 2);
/* 5972 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5973 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadGroupReference.Parent" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5975 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5976 */           localPacketStream.vm.printTrace("Sending:                 group(ThreadGroupReferenceImpl): " + (paramThreadGroupReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadGroupReferenceImpl.ref()).toString()));
/*      */         }
/* 5978 */         localPacketStream.writeObjectRef(paramThreadGroupReferenceImpl.ref());
/* 5979 */         localPacketStream.send();
/* 5980 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Parent waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5985 */         paramPacketStream.waitForReply();
/* 5986 */         return new Parent(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Parent(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5998 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5999 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadGroupReference.Parent" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 6001 */         this.parentGroup = paramPacketStream.readThreadGroupReference();
/* 6002 */         if (paramVirtualMachineImpl.traceReceives)
/* 6003 */           paramVirtualMachineImpl.printReceiveTrace(4, "parentGroup(ThreadGroupReferenceImpl): " + (this.parentGroup == null ? "NULL" : new StringBuilder().append("ref=").append(this.parentGroup.ref()).toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ThreadReference
/*      */   {
/*      */     static final int COMMAND_SET = 11;
/*      */ 
/*      */     static class CurrentContendedMonitor
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */       final ObjectReferenceImpl monitor;
/*      */ 
/*      */       static CurrentContendedMonitor process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5542 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5543 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5548 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 9);
/* 5549 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5550 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.CurrentContendedMonitor" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5552 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5553 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5555 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5556 */         localPacketStream.send();
/* 5557 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static CurrentContendedMonitor waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5562 */         paramPacketStream.waitForReply();
/* 5563 */         return new CurrentContendedMonitor(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private CurrentContendedMonitor(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5574 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5575 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.CurrentContendedMonitor" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5577 */         this.monitor = paramPacketStream.readTaggedObjectReference();
/* 5578 */         if (paramVirtualMachineImpl.traceReceives)
/* 5579 */           paramVirtualMachineImpl.printReceiveTrace(4, "monitor(ObjectReferenceImpl): " + (this.monitor == null ? "NULL" : new StringBuilder().append("ref=").append(this.monitor.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ForceEarlyReturn
/*      */     {
/*      */       static final int COMMAND = 14;
/*      */ 
/*      */       static ForceEarlyReturn process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, ValueImpl paramValueImpl)
/*      */         throws JDWPException
/*      */       {
/* 5864 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramValueImpl);
/* 5865 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, ValueImpl paramValueImpl)
/*      */       {
/* 5871 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 14);
/* 5872 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5873 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.ForceEarlyReturn" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5875 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5876 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5878 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5879 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5880 */           localPacketStream.vm.printTrace("Sending:                 value(ValueImpl): " + paramValueImpl);
/*      */         }
/* 5882 */         localPacketStream.writeValue(paramValueImpl);
/* 5883 */         localPacketStream.send();
/* 5884 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ForceEarlyReturn waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5889 */         paramPacketStream.waitForReply();
/* 5890 */         return new ForceEarlyReturn(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ForceEarlyReturn(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5895 */         if (paramVirtualMachineImpl.traceReceives)
/* 5896 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.ForceEarlyReturn" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class FrameCount
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */       final int frameCount;
/*      */ 
/*      */       static FrameCount process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5424 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5425 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5430 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 7);
/* 5431 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5432 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.FrameCount" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5434 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5435 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5437 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5438 */         localPacketStream.send();
/* 5439 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static FrameCount waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5444 */         paramPacketStream.waitForReply();
/* 5445 */         return new FrameCount(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private FrameCount(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5455 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5456 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.FrameCount" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5458 */         this.frameCount = paramPacketStream.readInt();
/* 5459 */         if (paramVirtualMachineImpl.traceReceives)
/* 5460 */           paramVirtualMachineImpl.printReceiveTrace(4, "frameCount(int): " + this.frameCount);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Frames
/*      */     {
/*      */       static final int COMMAND = 6;
/*      */       final Frame[] frames;
/*      */ 
/*      */       static Frames process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, int paramInt1, int paramInt2)
/*      */         throws JDWPException
/*      */       {
/* 5330 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramInt1, paramInt2);
/* 5331 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, int paramInt1, int paramInt2)
/*      */       {
/* 5338 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 6);
/* 5339 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5340 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Frames" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5342 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5343 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5345 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5346 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5347 */           localPacketStream.vm.printTrace("Sending:                 startFrame(int): " + paramInt1);
/*      */         }
/* 5349 */         localPacketStream.writeInt(paramInt1);
/* 5350 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5351 */           localPacketStream.vm.printTrace("Sending:                 length(int): " + paramInt2);
/*      */         }
/* 5353 */         localPacketStream.writeInt(paramInt2);
/* 5354 */         localPacketStream.send();
/* 5355 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Frames waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5360 */         paramPacketStream.waitForReply();
/* 5361 */         return new Frames(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Frames(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5395 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5396 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Frames" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5398 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5399 */           paramVirtualMachineImpl.printReceiveTrace(4, "frames(Frame[]): ");
/*      */         }
/* 5401 */         int i = paramPacketStream.readInt();
/* 5402 */         this.frames = new Frame[i];
/* 5403 */         for (int j = 0; j < i; j++) {
/* 5404 */           if (paramVirtualMachineImpl.traceReceives) {
/* 5405 */             paramVirtualMachineImpl.printReceiveTrace(5, "frames[i](Frame): ");
/*      */           }
/* 5407 */           this.frames[j] = new Frame(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class Frame
/*      */       {
/*      */         final long frameID;
/*      */         final Location location;
/*      */ 
/*      */         private Frame(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 5377 */           this.frameID = paramPacketStream.readFrameRef();
/* 5378 */           if (paramVirtualMachineImpl.traceReceives) {
/* 5379 */             paramVirtualMachineImpl.printReceiveTrace(5, "frameID(long): " + this.frameID);
/*      */           }
/* 5381 */           this.location = paramPacketStream.readLocation();
/* 5382 */           if (paramVirtualMachineImpl.traceReceives)
/* 5383 */             paramVirtualMachineImpl.printReceiveTrace(5, "location(Location): " + this.location);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Interrupt
/*      */     {
/*      */       static final int COMMAND = 11;
/*      */ 
/*      */       static Interrupt process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5641 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5642 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5647 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 11);
/* 5648 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5649 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Interrupt" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5651 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5652 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5654 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5655 */         localPacketStream.send();
/* 5656 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Interrupt waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5661 */         paramPacketStream.waitForReply();
/* 5662 */         return new Interrupt(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Interrupt(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5667 */         if (paramVirtualMachineImpl.traceReceives)
/* 5668 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Interrupt" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Name
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final String threadName;
/*      */ 
/*      */       static Name process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5052 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5053 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5058 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 1);
/* 5059 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5060 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Name" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5062 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5063 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5065 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5066 */         localPacketStream.send();
/* 5067 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Name waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5072 */         paramPacketStream.waitForReply();
/* 5073 */         return new Name(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Name(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5083 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5084 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Name" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5086 */         this.threadName = paramPacketStream.readString();
/* 5087 */         if (paramVirtualMachineImpl.traceReceives)
/* 5088 */           paramVirtualMachineImpl.printReceiveTrace(4, "threadName(String): " + this.threadName);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class OwnedMonitors
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */       final ObjectReferenceImpl[] owned;
/*      */ 
/*      */       static OwnedMonitors process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5478 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5479 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5484 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 8);
/* 5485 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5486 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitors" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5488 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5489 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5491 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5492 */         localPacketStream.send();
/* 5493 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static OwnedMonitors waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5498 */         paramPacketStream.waitForReply();
/* 5499 */         return new OwnedMonitors(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private OwnedMonitors(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5509 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5510 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitors" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5512 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5513 */           paramVirtualMachineImpl.printReceiveTrace(4, "owned(ObjectReferenceImpl[]): ");
/*      */         }
/* 5515 */         int i = paramPacketStream.readInt();
/* 5516 */         this.owned = new ObjectReferenceImpl[i];
/* 5517 */         for (int j = 0; j < i; j++) {
/* 5518 */           this.owned[j] = paramPacketStream.readTaggedObjectReference();
/* 5519 */           if (paramVirtualMachineImpl.traceReceives)
/* 5520 */             paramVirtualMachineImpl.printReceiveTrace(5, "owned[i](ObjectReferenceImpl): " + (this.owned[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.owned[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class OwnedMonitorsStackDepthInfo
/*      */     {
/*      */       static final int COMMAND = 13;
/*      */       final monitor[] owned;
/*      */ 
/*      */       static OwnedMonitorsStackDepthInfo process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5742 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5743 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5748 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 13);
/* 5749 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5750 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitorsStackDepthInfo" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5752 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5753 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5755 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5756 */         localPacketStream.send();
/* 5757 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static OwnedMonitorsStackDepthInfo waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5762 */         paramPacketStream.waitForReply();
/* 5763 */         return new OwnedMonitorsStackDepthInfo(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private OwnedMonitorsStackDepthInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5797 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5798 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitorsStackDepthInfo" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5800 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5801 */           paramVirtualMachineImpl.printReceiveTrace(4, "owned(monitor[]): ");
/*      */         }
/* 5803 */         int i = paramPacketStream.readInt();
/* 5804 */         this.owned = new monitor[i];
/* 5805 */         for (int j = 0; j < i; j++) {
/* 5806 */           if (paramVirtualMachineImpl.traceReceives) {
/* 5807 */             paramVirtualMachineImpl.printReceiveTrace(5, "owned[i](monitor): ");
/*      */           }
/* 5809 */           this.owned[j] = new monitor(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class monitor
/*      */       {
/*      */         final ObjectReferenceImpl monitor;
/*      */         final int stack_depth;
/*      */ 
/*      */         private monitor(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 5779 */           this.monitor = paramPacketStream.readTaggedObjectReference();
/* 5780 */           if (paramVirtualMachineImpl.traceReceives) {
/* 5781 */             paramVirtualMachineImpl.printReceiveTrace(5, "monitor(ObjectReferenceImpl): " + (this.monitor == null ? "NULL" : new StringBuilder().append("ref=").append(this.monitor.ref()).toString()));
/*      */           }
/* 5783 */           this.stack_depth = paramPacketStream.readInt();
/* 5784 */           if (paramVirtualMachineImpl.traceReceives)
/* 5785 */             paramVirtualMachineImpl.printReceiveTrace(5, "stack_depth(int): " + this.stack_depth);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Resume
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */ 
/*      */       static Resume process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5169 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5170 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5175 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 3);
/* 5176 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5177 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Resume" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5179 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5180 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5182 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5183 */         localPacketStream.send();
/* 5184 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Resume waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5189 */         paramPacketStream.waitForReply();
/* 5190 */         return new Resume(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Resume(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5195 */         if (paramVirtualMachineImpl.traceReceives)
/* 5196 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Resume" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Status
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */       final int threadStatus;
/*      */       final int suspendStatus;
/*      */ 
/*      */       static Status process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5213 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5214 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5219 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 4);
/* 5220 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5221 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Status" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5223 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5224 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5226 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5227 */         localPacketStream.send();
/* 5228 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Status waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5233 */         paramPacketStream.waitForReply();
/* 5234 */         return new Status(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Status(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5251 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5252 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Status" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5254 */         this.threadStatus = paramPacketStream.readInt();
/* 5255 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5256 */           paramVirtualMachineImpl.printReceiveTrace(4, "threadStatus(int): " + this.threadStatus);
/*      */         }
/* 5258 */         this.suspendStatus = paramPacketStream.readInt();
/* 5259 */         if (paramVirtualMachineImpl.traceReceives)
/* 5260 */           paramVirtualMachineImpl.printReceiveTrace(4, "suspendStatus(int): " + this.suspendStatus);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Stop
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */ 
/*      */       static Stop process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5595 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl, paramObjectReferenceImpl);
/* 5596 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl, ObjectReferenceImpl paramObjectReferenceImpl)
/*      */       {
/* 5602 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 10);
/* 5603 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5604 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Stop" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5606 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5607 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5609 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5610 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5611 */           localPacketStream.vm.printTrace("Sending:                 throwable(ObjectReferenceImpl): " + (paramObjectReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramObjectReferenceImpl.ref()).toString()));
/*      */         }
/* 5613 */         localPacketStream.writeObjectRef(paramObjectReferenceImpl.ref());
/* 5614 */         localPacketStream.send();
/* 5615 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Stop waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5620 */         paramPacketStream.waitForReply();
/* 5621 */         return new Stop(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Stop(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5626 */         if (paramVirtualMachineImpl.traceReceives)
/* 5627 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Stop" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Suspend
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */ 
/*      */       static Suspend process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5123 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5124 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5129 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 2);
/* 5130 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5131 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.Suspend" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5133 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5134 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5136 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5137 */         localPacketStream.send();
/* 5138 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Suspend waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5143 */         paramPacketStream.waitForReply();
/* 5144 */         return new Suspend(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Suspend(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5149 */         if (paramVirtualMachineImpl.traceReceives)
/* 5150 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.Suspend" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SuspendCount
/*      */     {
/*      */       static final int COMMAND = 12;
/*      */       final int suspendCount;
/*      */ 
/*      */       static SuspendCount process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5684 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5685 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5690 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 12);
/* 5691 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5692 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.SuspendCount" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5694 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5695 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5697 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5698 */         localPacketStream.send();
/* 5699 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SuspendCount waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5704 */         paramPacketStream.waitForReply();
/* 5705 */         return new SuspendCount(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SuspendCount(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5715 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5716 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.SuspendCount" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5718 */         this.suspendCount = paramPacketStream.readInt();
/* 5719 */         if (paramVirtualMachineImpl.traceReceives)
/* 5720 */           paramVirtualMachineImpl.printReceiveTrace(4, "suspendCount(int): " + this.suspendCount);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ThreadGroup
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */       final ThreadGroupReferenceImpl group;
/*      */ 
/*      */       static ThreadGroup process(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */         throws JDWPException
/*      */       {
/* 5274 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramThreadReferenceImpl);
/* 5275 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ThreadReferenceImpl paramThreadReferenceImpl)
/*      */       {
/* 5280 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 11, 5);
/* 5281 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5282 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.ThreadReference.ThreadGroup" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 5284 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 5285 */           localPacketStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + (paramThreadReferenceImpl == null ? "NULL" : new StringBuilder().append("ref=").append(paramThreadReferenceImpl.ref()).toString()));
/*      */         }
/* 5287 */         localPacketStream.writeObjectRef(paramThreadReferenceImpl.ref());
/* 5288 */         localPacketStream.send();
/* 5289 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ThreadGroup waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 5294 */         paramPacketStream.waitForReply();
/* 5295 */         return new ThreadGroup(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ThreadGroup(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 5305 */         if (paramVirtualMachineImpl.traceReceives) {
/* 5306 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.ThreadReference.ThreadGroup" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 5308 */         this.group = paramPacketStream.readThreadGroupReference();
/* 5309 */         if (paramVirtualMachineImpl.traceReceives)
/* 5310 */           paramVirtualMachineImpl.printReceiveTrace(4, "group(ThreadGroupReferenceImpl): " + (this.group == null ? "NULL" : new StringBuilder().append("ref=").append(this.group.ref()).toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ThreadStatus
/*      */   {
/*      */     static final int ZOMBIE = 0;
/*      */     static final int RUNNING = 1;
/*      */     static final int SLEEPING = 2;
/*      */     static final int MONITOR = 3;
/*      */     static final int WAIT = 4;
/*      */   }
/*      */ 
/*      */   static class TypeTag
/*      */   {
/*      */     static final int CLASS = 1;
/*      */     static final int INTERFACE = 2;
/*      */     static final int ARRAY = 3;
/*      */   }
/*      */ 
/*      */   static class VirtualMachine
/*      */   {
/*      */     static final int COMMAND_SET = 1;
/*      */ 
/*      */     static class AllClasses
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */       final ClassInfo[] classes;
/*      */ 
/*      */       static AllClasses process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  203 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  204 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  208 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 3);
/*  209 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  210 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.AllClasses" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  212 */         localPacketStream.send();
/*  213 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static AllClasses waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  218 */         paramPacketStream.waitForReply();
/*  219 */         return new AllClasses(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private AllClasses(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  273 */         if (paramVirtualMachineImpl.traceReceives) {
/*  274 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.AllClasses" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  276 */         if (paramVirtualMachineImpl.traceReceives) {
/*  277 */           paramVirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/*  279 */         int i = paramPacketStream.readInt();
/*  280 */         this.classes = new ClassInfo[i];
/*  281 */         for (int j = 0; j < i; j++) {
/*  282 */           if (paramVirtualMachineImpl.traceReceives) {
/*  283 */             paramVirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/*  285 */           this.classes[j] = new ClassInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */         final long typeID;
/*      */         final String signature;
/*      */         final int status;
/*      */ 
/*      */         private ClassInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/*  247 */           this.refTypeTag = paramPacketStream.readByte();
/*  248 */           if (paramVirtualMachineImpl.traceReceives) {
/*  249 */             paramVirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/*  251 */           this.typeID = paramPacketStream.readClassRef();
/*  252 */           if (paramVirtualMachineImpl.traceReceives) {
/*  253 */             paramVirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/*  255 */           this.signature = paramPacketStream.readString();
/*  256 */           if (paramVirtualMachineImpl.traceReceives) {
/*  257 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/*  259 */           this.status = paramPacketStream.readInt();
/*  260 */           if (paramVirtualMachineImpl.traceReceives)
/*  261 */             paramVirtualMachineImpl.printReceiveTrace(5, "status(int): " + this.status);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class AllClassesWithGeneric
/*      */     {
/*      */       static final int COMMAND = 20;
/*      */       final ClassInfo[] classes;
/*      */ 
/*      */       static AllClassesWithGeneric process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/* 1586 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/* 1587 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/* 1591 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 20);
/* 1592 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1593 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.AllClassesWithGeneric" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1595 */         localPacketStream.send();
/* 1596 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static AllClassesWithGeneric waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1601 */         paramPacketStream.waitForReply();
/* 1602 */         return new AllClassesWithGeneric(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private AllClassesWithGeneric(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1666 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1667 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.AllClassesWithGeneric" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 1669 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1670 */           paramVirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/* 1672 */         int i = paramPacketStream.readInt();
/* 1673 */         this.classes = new ClassInfo[i];
/* 1674 */         for (int j = 0; j < i; j++) {
/* 1675 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1676 */             paramVirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/* 1678 */           this.classes[j] = new ClassInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */         final long typeID;
/*      */         final String signature;
/*      */         final String genericSignature;
/*      */         final int status;
/*      */ 
/*      */         private ClassInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/* 1636 */           this.refTypeTag = paramPacketStream.readByte();
/* 1637 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1638 */             paramVirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/* 1640 */           this.typeID = paramPacketStream.readClassRef();
/* 1641 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1642 */             paramVirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/* 1644 */           this.signature = paramPacketStream.readString();
/* 1645 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1646 */             paramVirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 1648 */           this.genericSignature = paramPacketStream.readString();
/* 1649 */           if (paramVirtualMachineImpl.traceReceives) {
/* 1650 */             paramVirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 1652 */           this.status = paramPacketStream.readInt();
/* 1653 */           if (paramVirtualMachineImpl.traceReceives)
/* 1654 */             paramVirtualMachineImpl.printReceiveTrace(5, "status(int): " + this.status);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class AllThreads
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */       final ThreadReferenceImpl[] threads;
/*      */ 
/*      */       static AllThreads process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  304 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  305 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  309 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 4);
/*  310 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  311 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.AllThreads" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  313 */         localPacketStream.send();
/*  314 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static AllThreads waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  319 */         paramPacketStream.waitForReply();
/*  320 */         return new AllThreads(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private AllThreads(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  330 */         if (paramVirtualMachineImpl.traceReceives) {
/*  331 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.AllThreads" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  333 */         if (paramVirtualMachineImpl.traceReceives) {
/*  334 */           paramVirtualMachineImpl.printReceiveTrace(4, "threads(ThreadReferenceImpl[]): ");
/*      */         }
/*  336 */         int i = paramPacketStream.readInt();
/*  337 */         this.threads = new ThreadReferenceImpl[i];
/*  338 */         for (int j = 0; j < i; j++) {
/*  339 */           this.threads[j] = paramPacketStream.readThreadReference();
/*  340 */           if (paramVirtualMachineImpl.traceReceives)
/*  341 */             paramVirtualMachineImpl.printReceiveTrace(5, "threads[i](ThreadReferenceImpl): " + (this.threads[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.threads[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Capabilities
/*      */     {
/*      */       static final int COMMAND = 12;
/*      */       final boolean canWatchFieldModification;
/*      */       final boolean canWatchFieldAccess;
/*      */       final boolean canGetBytecodes;
/*      */       final boolean canGetSyntheticAttribute;
/*      */       final boolean canGetOwnedMonitorInfo;
/*      */       final boolean canGetCurrentContendedMonitor;
/*      */       final boolean canGetMonitorInfo;
/*      */ 
/*      */       static Capabilities process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  731 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  732 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  736 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 12);
/*  737 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  738 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.Capabilities" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  740 */         localPacketStream.send();
/*  741 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Capabilities waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  746 */         paramPacketStream.waitForReply();
/*  747 */         return new Capabilities(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Capabilities(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  792 */         if (paramVirtualMachineImpl.traceReceives) {
/*  793 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.Capabilities" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  795 */         this.canWatchFieldModification = paramPacketStream.readBoolean();
/*  796 */         if (paramVirtualMachineImpl.traceReceives) {
/*  797 */           paramVirtualMachineImpl.printReceiveTrace(4, "canWatchFieldModification(boolean): " + this.canWatchFieldModification);
/*      */         }
/*  799 */         this.canWatchFieldAccess = paramPacketStream.readBoolean();
/*  800 */         if (paramVirtualMachineImpl.traceReceives) {
/*  801 */           paramVirtualMachineImpl.printReceiveTrace(4, "canWatchFieldAccess(boolean): " + this.canWatchFieldAccess);
/*      */         }
/*  803 */         this.canGetBytecodes = paramPacketStream.readBoolean();
/*  804 */         if (paramVirtualMachineImpl.traceReceives) {
/*  805 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetBytecodes(boolean): " + this.canGetBytecodes);
/*      */         }
/*  807 */         this.canGetSyntheticAttribute = paramPacketStream.readBoolean();
/*  808 */         if (paramVirtualMachineImpl.traceReceives) {
/*  809 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetSyntheticAttribute(boolean): " + this.canGetSyntheticAttribute);
/*      */         }
/*  811 */         this.canGetOwnedMonitorInfo = paramPacketStream.readBoolean();
/*  812 */         if (paramVirtualMachineImpl.traceReceives) {
/*  813 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetOwnedMonitorInfo(boolean): " + this.canGetOwnedMonitorInfo);
/*      */         }
/*  815 */         this.canGetCurrentContendedMonitor = paramPacketStream.readBoolean();
/*  816 */         if (paramVirtualMachineImpl.traceReceives) {
/*  817 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetCurrentContendedMonitor(boolean): " + this.canGetCurrentContendedMonitor);
/*      */         }
/*  819 */         this.canGetMonitorInfo = paramPacketStream.readBoolean();
/*  820 */         if (paramVirtualMachineImpl.traceReceives)
/*  821 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetMonitorInfo(boolean): " + this.canGetMonitorInfo);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class CapabilitiesNew
/*      */     {
/*      */       static final int COMMAND = 17;
/*      */       final boolean canWatchFieldModification;
/*      */       final boolean canWatchFieldAccess;
/*      */       final boolean canGetBytecodes;
/*      */       final boolean canGetSyntheticAttribute;
/*      */       final boolean canGetOwnedMonitorInfo;
/*      */       final boolean canGetCurrentContendedMonitor;
/*      */       final boolean canGetMonitorInfo;
/*      */       final boolean canRedefineClasses;
/*      */       final boolean canAddMethod;
/*      */       final boolean canUnrestrictedlyRedefineClasses;
/*      */       final boolean canPopFrames;
/*      */       final boolean canUseInstanceFilters;
/*      */       final boolean canGetSourceDebugExtension;
/*      */       final boolean canRequestVMDeathEvent;
/*      */       final boolean canSetDefaultStratum;
/*      */       final boolean canGetInstanceInfo;
/*      */       final boolean canRequestMonitorEvents;
/*      */       final boolean canGetMonitorFrameInfo;
/*      */       final boolean canUseSourceNameFilters;
/*      */       final boolean canGetConstantPool;
/*      */       final boolean canForceEarlyReturn;
/*      */       final boolean reserved22;
/*      */       final boolean reserved23;
/*      */       final boolean reserved24;
/*      */       final boolean reserved25;
/*      */       final boolean reserved26;
/*      */       final boolean reserved27;
/*      */       final boolean reserved28;
/*      */       final boolean reserved29;
/*      */       final boolean reserved30;
/*      */       final boolean reserved31;
/*      */       final boolean reserved32;
/*      */ 
/*      */       static CapabilitiesNew process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/* 1107 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/* 1108 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/* 1112 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 17);
/* 1113 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1114 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.CapabilitiesNew" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1116 */         localPacketStream.send();
/* 1117 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static CapabilitiesNew waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1122 */         paramPacketStream.waitForReply();
/* 1123 */         return new CapabilitiesNew(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private CapabilitiesNew(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1296 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1297 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.CapabilitiesNew" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 1299 */         this.canWatchFieldModification = paramPacketStream.readBoolean();
/* 1300 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1301 */           paramVirtualMachineImpl.printReceiveTrace(4, "canWatchFieldModification(boolean): " + this.canWatchFieldModification);
/*      */         }
/* 1303 */         this.canWatchFieldAccess = paramPacketStream.readBoolean();
/* 1304 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1305 */           paramVirtualMachineImpl.printReceiveTrace(4, "canWatchFieldAccess(boolean): " + this.canWatchFieldAccess);
/*      */         }
/* 1307 */         this.canGetBytecodes = paramPacketStream.readBoolean();
/* 1308 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1309 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetBytecodes(boolean): " + this.canGetBytecodes);
/*      */         }
/* 1311 */         this.canGetSyntheticAttribute = paramPacketStream.readBoolean();
/* 1312 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1313 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetSyntheticAttribute(boolean): " + this.canGetSyntheticAttribute);
/*      */         }
/* 1315 */         this.canGetOwnedMonitorInfo = paramPacketStream.readBoolean();
/* 1316 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1317 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetOwnedMonitorInfo(boolean): " + this.canGetOwnedMonitorInfo);
/*      */         }
/* 1319 */         this.canGetCurrentContendedMonitor = paramPacketStream.readBoolean();
/* 1320 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1321 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetCurrentContendedMonitor(boolean): " + this.canGetCurrentContendedMonitor);
/*      */         }
/* 1323 */         this.canGetMonitorInfo = paramPacketStream.readBoolean();
/* 1324 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1325 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetMonitorInfo(boolean): " + this.canGetMonitorInfo);
/*      */         }
/* 1327 */         this.canRedefineClasses = paramPacketStream.readBoolean();
/* 1328 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1329 */           paramVirtualMachineImpl.printReceiveTrace(4, "canRedefineClasses(boolean): " + this.canRedefineClasses);
/*      */         }
/* 1331 */         this.canAddMethod = paramPacketStream.readBoolean();
/* 1332 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1333 */           paramVirtualMachineImpl.printReceiveTrace(4, "canAddMethod(boolean): " + this.canAddMethod);
/*      */         }
/* 1335 */         this.canUnrestrictedlyRedefineClasses = paramPacketStream.readBoolean();
/* 1336 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1337 */           paramVirtualMachineImpl.printReceiveTrace(4, "canUnrestrictedlyRedefineClasses(boolean): " + this.canUnrestrictedlyRedefineClasses);
/*      */         }
/* 1339 */         this.canPopFrames = paramPacketStream.readBoolean();
/* 1340 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1341 */           paramVirtualMachineImpl.printReceiveTrace(4, "canPopFrames(boolean): " + this.canPopFrames);
/*      */         }
/* 1343 */         this.canUseInstanceFilters = paramPacketStream.readBoolean();
/* 1344 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1345 */           paramVirtualMachineImpl.printReceiveTrace(4, "canUseInstanceFilters(boolean): " + this.canUseInstanceFilters);
/*      */         }
/* 1347 */         this.canGetSourceDebugExtension = paramPacketStream.readBoolean();
/* 1348 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1349 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetSourceDebugExtension(boolean): " + this.canGetSourceDebugExtension);
/*      */         }
/* 1351 */         this.canRequestVMDeathEvent = paramPacketStream.readBoolean();
/* 1352 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1353 */           paramVirtualMachineImpl.printReceiveTrace(4, "canRequestVMDeathEvent(boolean): " + this.canRequestVMDeathEvent);
/*      */         }
/* 1355 */         this.canSetDefaultStratum = paramPacketStream.readBoolean();
/* 1356 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1357 */           paramVirtualMachineImpl.printReceiveTrace(4, "canSetDefaultStratum(boolean): " + this.canSetDefaultStratum);
/*      */         }
/* 1359 */         this.canGetInstanceInfo = paramPacketStream.readBoolean();
/* 1360 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1361 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetInstanceInfo(boolean): " + this.canGetInstanceInfo);
/*      */         }
/* 1363 */         this.canRequestMonitorEvents = paramPacketStream.readBoolean();
/* 1364 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1365 */           paramVirtualMachineImpl.printReceiveTrace(4, "canRequestMonitorEvents(boolean): " + this.canRequestMonitorEvents);
/*      */         }
/* 1367 */         this.canGetMonitorFrameInfo = paramPacketStream.readBoolean();
/* 1368 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1369 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetMonitorFrameInfo(boolean): " + this.canGetMonitorFrameInfo);
/*      */         }
/* 1371 */         this.canUseSourceNameFilters = paramPacketStream.readBoolean();
/* 1372 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1373 */           paramVirtualMachineImpl.printReceiveTrace(4, "canUseSourceNameFilters(boolean): " + this.canUseSourceNameFilters);
/*      */         }
/* 1375 */         this.canGetConstantPool = paramPacketStream.readBoolean();
/* 1376 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1377 */           paramVirtualMachineImpl.printReceiveTrace(4, "canGetConstantPool(boolean): " + this.canGetConstantPool);
/*      */         }
/* 1379 */         this.canForceEarlyReturn = paramPacketStream.readBoolean();
/* 1380 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1381 */           paramVirtualMachineImpl.printReceiveTrace(4, "canForceEarlyReturn(boolean): " + this.canForceEarlyReturn);
/*      */         }
/* 1383 */         this.reserved22 = paramPacketStream.readBoolean();
/* 1384 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1385 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved22(boolean): " + this.reserved22);
/*      */         }
/* 1387 */         this.reserved23 = paramPacketStream.readBoolean();
/* 1388 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1389 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved23(boolean): " + this.reserved23);
/*      */         }
/* 1391 */         this.reserved24 = paramPacketStream.readBoolean();
/* 1392 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1393 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved24(boolean): " + this.reserved24);
/*      */         }
/* 1395 */         this.reserved25 = paramPacketStream.readBoolean();
/* 1396 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1397 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved25(boolean): " + this.reserved25);
/*      */         }
/* 1399 */         this.reserved26 = paramPacketStream.readBoolean();
/* 1400 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1401 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved26(boolean): " + this.reserved26);
/*      */         }
/* 1403 */         this.reserved27 = paramPacketStream.readBoolean();
/* 1404 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1405 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved27(boolean): " + this.reserved27);
/*      */         }
/* 1407 */         this.reserved28 = paramPacketStream.readBoolean();
/* 1408 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1409 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved28(boolean): " + this.reserved28);
/*      */         }
/* 1411 */         this.reserved29 = paramPacketStream.readBoolean();
/* 1412 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1413 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved29(boolean): " + this.reserved29);
/*      */         }
/* 1415 */         this.reserved30 = paramPacketStream.readBoolean();
/* 1416 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1417 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved30(boolean): " + this.reserved30);
/*      */         }
/* 1419 */         this.reserved31 = paramPacketStream.readBoolean();
/* 1420 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1421 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved31(boolean): " + this.reserved31);
/*      */         }
/* 1423 */         this.reserved32 = paramPacketStream.readBoolean();
/* 1424 */         if (paramVirtualMachineImpl.traceReceives)
/* 1425 */           paramVirtualMachineImpl.printReceiveTrace(4, "reserved32(boolean): " + this.reserved32);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ClassPaths
/*      */     {
/*      */       static final int COMMAND = 13;
/*      */       final String baseDir;
/*      */       final String[] classpaths;
/*      */       final String[] bootclasspaths;
/*      */ 
/*      */       static ClassPaths process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  836 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  837 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  841 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 13);
/*  842 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  843 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.ClassPaths" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  845 */         localPacketStream.send();
/*  846 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ClassPaths waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  851 */         paramPacketStream.waitForReply();
/*  852 */         return new ClassPaths(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ClassPaths(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  873 */         if (paramVirtualMachineImpl.traceReceives) {
/*  874 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.ClassPaths" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  876 */         this.baseDir = paramPacketStream.readString();
/*  877 */         if (paramVirtualMachineImpl.traceReceives) {
/*  878 */           paramVirtualMachineImpl.printReceiveTrace(4, "baseDir(String): " + this.baseDir);
/*      */         }
/*  880 */         if (paramVirtualMachineImpl.traceReceives) {
/*  881 */           paramVirtualMachineImpl.printReceiveTrace(4, "classpaths(String[]): ");
/*      */         }
/*  883 */         int i = paramPacketStream.readInt();
/*  884 */         this.classpaths = new String[i];
/*  885 */         for (int j = 0; j < i; j++) {
/*  886 */           this.classpaths[j] = paramPacketStream.readString();
/*  887 */           if (paramVirtualMachineImpl.traceReceives) {
/*  888 */             paramVirtualMachineImpl.printReceiveTrace(5, "classpaths[i](String): " + this.classpaths[j]);
/*      */           }
/*      */         }
/*  891 */         if (paramVirtualMachineImpl.traceReceives) {
/*  892 */           paramVirtualMachineImpl.printReceiveTrace(4, "bootclasspaths(String[]): ");
/*      */         }
/*  894 */         j = paramPacketStream.readInt();
/*  895 */         this.bootclasspaths = new String[j];
/*  896 */         for (int k = 0; k < j; k++) {
/*  897 */           this.bootclasspaths[k] = paramPacketStream.readString();
/*  898 */           if (paramVirtualMachineImpl.traceReceives)
/*  899 */             paramVirtualMachineImpl.printReceiveTrace(5, "bootclasspaths[i](String): " + this.bootclasspaths[k]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ClassesBySignature
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */       final ClassInfo[] classes;
/*      */ 
/*      */       static ClassesBySignature process(VirtualMachineImpl paramVirtualMachineImpl, String paramString)
/*      */         throws JDWPException
/*      */       {
/*  111 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramString);
/*  112 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, String paramString)
/*      */       {
/*  117 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 2);
/*  118 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  119 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.ClassesBySignature" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  121 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/*  122 */           localPacketStream.vm.printTrace("Sending:                 signature(String): " + paramString);
/*      */         }
/*  124 */         localPacketStream.writeString(paramString);
/*  125 */         localPacketStream.send();
/*  126 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ClassesBySignature waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  131 */         paramPacketStream.waitForReply();
/*  132 */         return new ClassesBySignature(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ClassesBySignature(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  177 */         if (paramVirtualMachineImpl.traceReceives) {
/*  178 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.ClassesBySignature" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  180 */         if (paramVirtualMachineImpl.traceReceives) {
/*  181 */           paramVirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/*  183 */         int i = paramPacketStream.readInt();
/*  184 */         this.classes = new ClassInfo[i];
/*  185 */         for (int j = 0; j < i; j++) {
/*  186 */           if (paramVirtualMachineImpl.traceReceives) {
/*  187 */             paramVirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/*  189 */           this.classes[j] = new ClassInfo(paramVirtualMachineImpl, paramPacketStream, null);
/*      */         }
/*      */       }
/*      */ 
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */         final long typeID;
/*      */         final int status;
/*      */ 
/*      */         private ClassInfo(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */         {
/*  155 */           this.refTypeTag = paramPacketStream.readByte();
/*  156 */           if (paramVirtualMachineImpl.traceReceives) {
/*  157 */             paramVirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/*  159 */           this.typeID = paramPacketStream.readClassRef();
/*  160 */           if (paramVirtualMachineImpl.traceReceives) {
/*  161 */             paramVirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/*  163 */           this.status = paramPacketStream.readInt();
/*  164 */           if (paramVirtualMachineImpl.traceReceives)
/*  165 */             paramVirtualMachineImpl.printReceiveTrace(5, "status(int): " + this.status);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class CreateString
/*      */     {
/*      */       static final int COMMAND = 11;
/*      */       final StringReferenceImpl stringObject;
/*      */ 
/*      */       static CreateString process(VirtualMachineImpl paramVirtualMachineImpl, String paramString)
/*      */         throws JDWPException
/*      */       {
/*  678 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramString);
/*  679 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, String paramString)
/*      */       {
/*  684 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 11);
/*  685 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  686 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.CreateString" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  688 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/*  689 */           localPacketStream.vm.printTrace("Sending:                 utf(String): " + paramString);
/*      */         }
/*  691 */         localPacketStream.writeString(paramString);
/*  692 */         localPacketStream.send();
/*  693 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static CreateString waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  698 */         paramPacketStream.waitForReply();
/*  699 */         return new CreateString(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private CreateString(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  709 */         if (paramVirtualMachineImpl.traceReceives) {
/*  710 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.CreateString" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  712 */         this.stringObject = paramPacketStream.readStringReference();
/*  713 */         if (paramVirtualMachineImpl.traceReceives)
/*  714 */           paramVirtualMachineImpl.printReceiveTrace(4, "stringObject(StringReferenceImpl): " + (this.stringObject == null ? "NULL" : new StringBuilder().append("ref=").append(this.stringObject.ref()).toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Dispose
/*      */     {
/*      */       static final int COMMAND = 6;
/*      */ 
/*      */       static Dispose process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  430 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  431 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  435 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 6);
/*  436 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  437 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.Dispose" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  439 */         localPacketStream.send();
/*  440 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Dispose waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  445 */         paramPacketStream.waitForReply();
/*  446 */         return new Dispose(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Dispose(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  451 */         if (paramVirtualMachineImpl.traceReceives)
/*  452 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.Dispose" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class DisposeObjects
/*      */     {
/*      */       static final int COMMAND = 14;
/*      */ 
/*      */       static DisposeObjects process(VirtualMachineImpl paramVirtualMachineImpl, Request[] paramArrayOfRequest)
/*      */         throws JDWPException
/*      */       {
/*  973 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayOfRequest);
/*  974 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, Request[] paramArrayOfRequest)
/*      */       {
/*  979 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 14);
/*  980 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  981 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.DisposeObjects" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  983 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/*  984 */           localPacketStream.vm.printTrace("Sending:                 requests(Request[]): ");
/*      */         }
/*  986 */         localPacketStream.writeInt(paramArrayOfRequest.length);
/*  987 */         for (int i = 0; i < paramArrayOfRequest.length; i++) {
/*  988 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/*  989 */             localPacketStream.vm.printTrace("Sending:                     requests[i](Request): ");
/*      */           }
/*  991 */           paramArrayOfRequest[i].write(localPacketStream);
/*      */         }
/*  993 */         localPacketStream.send();
/*  994 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static DisposeObjects waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  999 */         paramPacketStream.waitForReply();
/* 1000 */         return new DisposeObjects(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private DisposeObjects(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1005 */         if (paramVirtualMachineImpl.traceReceives)
/* 1006 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.DisposeObjects" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */ 
/*      */       static class Request
/*      */       {
/*      */         final ObjectReferenceImpl object;
/*      */         final int refCnt;
/*      */ 
/*      */         Request(ObjectReferenceImpl paramObjectReferenceImpl, int paramInt)
/*      */         {
/*  954 */           this.object = paramObjectReferenceImpl;
/*  955 */           this.refCnt = paramInt;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/*  959 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/*  960 */             paramPacketStream.vm.printTrace("Sending:                     object(ObjectReferenceImpl): " + (this.object == null ? "NULL" : new StringBuilder().append("ref=").append(this.object.ref()).toString()));
/*      */           }
/*  962 */           paramPacketStream.writeObjectRef(this.object.ref());
/*  963 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/*  964 */             paramPacketStream.vm.printTrace("Sending:                     refCnt(int): " + this.refCnt);
/*      */           }
/*  966 */           paramPacketStream.writeInt(this.refCnt);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Exit
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */ 
/*      */       static Exit process(VirtualMachineImpl paramVirtualMachineImpl, int paramInt)
/*      */         throws JDWPException
/*      */       {
/*  636 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramInt);
/*  637 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, int paramInt)
/*      */       {
/*  642 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 10);
/*  643 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  644 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.Exit" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  646 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/*  647 */           localPacketStream.vm.printTrace("Sending:                 exitCode(int): " + paramInt);
/*      */         }
/*  649 */         localPacketStream.writeInt(paramInt);
/*  650 */         localPacketStream.send();
/*  651 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Exit waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  656 */         paramPacketStream.waitForReply();
/*  657 */         return new Exit(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Exit(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  662 */         if (paramVirtualMachineImpl.traceReceives)
/*  663 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.Exit" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class HoldEvents
/*      */     {
/*      */       static final int COMMAND = 15;
/*      */ 
/*      */       static HoldEvents process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/* 1029 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/* 1030 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/* 1034 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 15);
/* 1035 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1036 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.HoldEvents" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1038 */         localPacketStream.send();
/* 1039 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static HoldEvents waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1044 */         paramPacketStream.waitForReply();
/* 1045 */         return new HoldEvents(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private HoldEvents(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1050 */         if (paramVirtualMachineImpl.traceReceives)
/* 1051 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.HoldEvents" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class IDSizes
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */       final int fieldIDSize;
/*      */       final int methodIDSize;
/*      */       final int objectIDSize;
/*      */       final int referenceTypeIDSize;
/*      */       final int frameIDSize;
/*      */ 
/*      */       static IDSizes process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  467 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  468 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  472 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 7);
/*  473 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  474 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.IDSizes" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  476 */         localPacketStream.send();
/*  477 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static IDSizes waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  482 */         paramPacketStream.waitForReply();
/*  483 */         return new IDSizes(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private IDSizes(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  513 */         if (paramVirtualMachineImpl.traceReceives) {
/*  514 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.IDSizes" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  516 */         this.fieldIDSize = paramPacketStream.readInt();
/*  517 */         if (paramVirtualMachineImpl.traceReceives) {
/*  518 */           paramVirtualMachineImpl.printReceiveTrace(4, "fieldIDSize(int): " + this.fieldIDSize);
/*      */         }
/*  520 */         this.methodIDSize = paramPacketStream.readInt();
/*  521 */         if (paramVirtualMachineImpl.traceReceives) {
/*  522 */           paramVirtualMachineImpl.printReceiveTrace(4, "methodIDSize(int): " + this.methodIDSize);
/*      */         }
/*  524 */         this.objectIDSize = paramPacketStream.readInt();
/*  525 */         if (paramVirtualMachineImpl.traceReceives) {
/*  526 */           paramVirtualMachineImpl.printReceiveTrace(4, "objectIDSize(int): " + this.objectIDSize);
/*      */         }
/*  528 */         this.referenceTypeIDSize = paramPacketStream.readInt();
/*  529 */         if (paramVirtualMachineImpl.traceReceives) {
/*  530 */           paramVirtualMachineImpl.printReceiveTrace(4, "referenceTypeIDSize(int): " + this.referenceTypeIDSize);
/*      */         }
/*  532 */         this.frameIDSize = paramPacketStream.readInt();
/*  533 */         if (paramVirtualMachineImpl.traceReceives)
/*  534 */           paramVirtualMachineImpl.printReceiveTrace(4, "frameIDSize(int): " + this.frameIDSize);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class InstanceCounts
/*      */     {
/*      */       static final int COMMAND = 21;
/*      */       final long[] counts;
/*      */ 
/*      */       static InstanceCounts process(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl[] paramArrayOfReferenceTypeImpl)
/*      */         throws JDWPException
/*      */       {
/* 1697 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayOfReferenceTypeImpl);
/* 1698 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ReferenceTypeImpl[] paramArrayOfReferenceTypeImpl)
/*      */       {
/* 1703 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 21);
/* 1704 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1705 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.InstanceCounts" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1707 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1708 */           localPacketStream.vm.printTrace("Sending:                 refTypesCount(ReferenceTypeImpl[]): ");
/*      */         }
/* 1710 */         localPacketStream.writeInt(paramArrayOfReferenceTypeImpl.length);
/* 1711 */         for (int i = 0; i < paramArrayOfReferenceTypeImpl.length; i++) {
/* 1712 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1713 */             localPacketStream.vm.printTrace("Sending:                     refTypesCount[i](ReferenceTypeImpl): " + (paramArrayOfReferenceTypeImpl[i] == null ? "NULL" : new StringBuilder().append("ref=").append(paramArrayOfReferenceTypeImpl[i].ref()).toString()));
/*      */           }
/* 1715 */           localPacketStream.writeClassRef(paramArrayOfReferenceTypeImpl[i].ref());
/*      */         }
/* 1717 */         localPacketStream.send();
/* 1718 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static InstanceCounts waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1723 */         paramPacketStream.waitForReply();
/* 1724 */         return new InstanceCounts(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private InstanceCounts(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1734 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1735 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.InstanceCounts" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/* 1737 */         if (paramVirtualMachineImpl.traceReceives) {
/* 1738 */           paramVirtualMachineImpl.printReceiveTrace(4, "counts(long[]): ");
/*      */         }
/* 1740 */         int i = paramPacketStream.readInt();
/* 1741 */         this.counts = new long[i];
/* 1742 */         for (int j = 0; j < i; j++) {
/* 1743 */           this.counts[j] = paramPacketStream.readLong();
/* 1744 */           if (paramVirtualMachineImpl.traceReceives)
/* 1745 */             paramVirtualMachineImpl.printReceiveTrace(5, "counts[i](long): " + this.counts[j]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class RedefineClasses
/*      */     {
/*      */       static final int COMMAND = 18;
/*      */ 
/*      */       static RedefineClasses process(VirtualMachineImpl paramVirtualMachineImpl, ClassDef[] paramArrayOfClassDef)
/*      */         throws JDWPException
/*      */       {
/* 1491 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramArrayOfClassDef);
/* 1492 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, ClassDef[] paramArrayOfClassDef)
/*      */       {
/* 1497 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 18);
/* 1498 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1499 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.RedefineClasses" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1501 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1502 */           localPacketStream.vm.printTrace("Sending:                 classes(ClassDef[]): ");
/*      */         }
/* 1504 */         localPacketStream.writeInt(paramArrayOfClassDef.length);
/* 1505 */         for (int i = 0; i < paramArrayOfClassDef.length; i++) {
/* 1506 */           if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1507 */             localPacketStream.vm.printTrace("Sending:                     classes[i](ClassDef): ");
/*      */           }
/* 1509 */           paramArrayOfClassDef[i].write(localPacketStream);
/*      */         }
/* 1511 */         localPacketStream.send();
/* 1512 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static RedefineClasses waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1517 */         paramPacketStream.waitForReply();
/* 1518 */         return new RedefineClasses(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private RedefineClasses(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1523 */         if (paramVirtualMachineImpl.traceReceives)
/* 1524 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.RedefineClasses" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */ 
/*      */       static class ClassDef
/*      */       {
/*      */         final ReferenceTypeImpl refType;
/*      */         final byte[] classfile;
/*      */ 
/*      */         ClassDef(ReferenceTypeImpl paramReferenceTypeImpl, byte[] paramArrayOfByte)
/*      */         {
/* 1466 */           this.refType = paramReferenceTypeImpl;
/* 1467 */           this.classfile = paramArrayOfByte;
/*      */         }
/*      */ 
/*      */         private void write(PacketStream paramPacketStream) {
/* 1471 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1472 */             paramPacketStream.vm.printTrace("Sending:                     refType(ReferenceTypeImpl): " + (this.refType == null ? "NULL" : new StringBuilder().append("ref=").append(this.refType.ref()).toString()));
/*      */           }
/* 1474 */           paramPacketStream.writeClassRef(this.refType.ref());
/* 1475 */           if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1476 */             paramPacketStream.vm.printTrace("Sending:                     classfile(byte[]): ");
/*      */           }
/* 1478 */           paramPacketStream.writeInt(this.classfile.length);
/* 1479 */           for (int i = 0; i < this.classfile.length; i++) {
/* 1480 */             if ((paramPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1481 */               paramPacketStream.vm.printTrace("Sending:                         classfile[i](byte): " + this.classfile[i]);
/*      */             }
/* 1483 */             paramPacketStream.writeByte(this.classfile[i]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ReleaseEvents
/*      */     {
/*      */       static final int COMMAND = 16;
/*      */ 
/*      */       static ReleaseEvents process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/* 1067 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/* 1068 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/* 1072 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 16);
/* 1073 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1074 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.ReleaseEvents" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1076 */         localPacketStream.send();
/* 1077 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static ReleaseEvents waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1082 */         paramPacketStream.waitForReply();
/* 1083 */         return new ReleaseEvents(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private ReleaseEvents(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1088 */         if (paramVirtualMachineImpl.traceReceives)
/* 1089 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.ReleaseEvents" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Resume
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */ 
/*      */       static Resume process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  594 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  595 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  599 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 9);
/*  600 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  601 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.Resume" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  603 */         localPacketStream.send();
/*  604 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Resume waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  609 */         paramPacketStream.waitForReply();
/*  610 */         return new Resume(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Resume(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  615 */         if (paramVirtualMachineImpl.traceReceives)
/*  616 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.Resume" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SetDefaultStratum
/*      */     {
/*      */       static final int COMMAND = 19;
/*      */ 
/*      */       static SetDefaultStratum process(VirtualMachineImpl paramVirtualMachineImpl, String paramString)
/*      */         throws JDWPException
/*      */       {
/* 1539 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl, paramString);
/* 1540 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl, String paramString)
/*      */       {
/* 1545 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 19);
/* 1546 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1547 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.SetDefaultStratum" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/* 1549 */         if ((localPacketStream.vm.traceFlags & 0x1) != 0) {
/* 1550 */           localPacketStream.vm.printTrace("Sending:                 stratumID(String): " + paramString);
/*      */         }
/* 1552 */         localPacketStream.writeString(paramString);
/* 1553 */         localPacketStream.send();
/* 1554 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static SetDefaultStratum waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/* 1559 */         paramPacketStream.waitForReply();
/* 1560 */         return new SetDefaultStratum(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private SetDefaultStratum(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/* 1565 */         if (paramVirtualMachineImpl.traceReceives)
/* 1566 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.SetDefaultStratum" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Suspend
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */ 
/*      */       static Suspend process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  555 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  556 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  560 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 8);
/*  561 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  562 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.Suspend" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  564 */         localPacketStream.send();
/*  565 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Suspend waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  570 */         paramPacketStream.waitForReply();
/*  571 */         return new Suspend(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Suspend(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  576 */         if (paramVirtualMachineImpl.traceReceives)
/*  577 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.Suspend" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */       }
/*      */     }
/*      */ 
/*      */     static class TopLevelThreadGroups
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */       final ThreadGroupReferenceImpl[] groups;
/*      */ 
/*      */       static TopLevelThreadGroups process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*  357 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*  358 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*  362 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 5);
/*  363 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  364 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.TopLevelThreadGroups" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*  366 */         localPacketStream.send();
/*  367 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static TopLevelThreadGroups waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*  372 */         paramPacketStream.waitForReply();
/*  373 */         return new TopLevelThreadGroups(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private TopLevelThreadGroups(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*  383 */         if (paramVirtualMachineImpl.traceReceives) {
/*  384 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.TopLevelThreadGroups" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*  386 */         if (paramVirtualMachineImpl.traceReceives) {
/*  387 */           paramVirtualMachineImpl.printReceiveTrace(4, "groups(ThreadGroupReferenceImpl[]): ");
/*      */         }
/*  389 */         int i = paramPacketStream.readInt();
/*  390 */         this.groups = new ThreadGroupReferenceImpl[i];
/*  391 */         for (int j = 0; j < i; j++) {
/*  392 */           this.groups[j] = paramPacketStream.readThreadGroupReference();
/*  393 */           if (paramVirtualMachineImpl.traceReceives)
/*  394 */             paramVirtualMachineImpl.printReceiveTrace(5, "groups[i](ThreadGroupReferenceImpl): " + (this.groups[j] == null ? "NULL" : new StringBuilder().append("ref=").append(this.groups[j].ref()).toString()));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class Version
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final String description;
/*      */       final int jdwpMajor;
/*      */       final int jdwpMinor;
/*      */       final String vmVersion;
/*      */       final String vmName;
/*      */ 
/*      */       static Version process(VirtualMachineImpl paramVirtualMachineImpl)
/*      */         throws JDWPException
/*      */       {
/*   25 */         PacketStream localPacketStream = enqueueCommand(paramVirtualMachineImpl);
/*   26 */         return waitForReply(paramVirtualMachineImpl, localPacketStream);
/*      */       }
/*      */ 
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl paramVirtualMachineImpl) {
/*   30 */         PacketStream localPacketStream = new PacketStream(paramVirtualMachineImpl, 1, 1);
/*   31 */         if ((paramVirtualMachineImpl.traceFlags & 0x1) != 0) {
/*   32 */           paramVirtualMachineImpl.printTrace("Sending Command(id=" + localPacketStream.pkt.id + ") JDWP.VirtualMachine.Version" + (localPacketStream.pkt.flags != 0 ? ", FLAGS=" + localPacketStream.pkt.flags : ""));
/*      */         }
/*   34 */         localPacketStream.send();
/*   35 */         return localPacketStream;
/*      */       }
/*      */ 
/*      */       static Version waitForReply(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream) throws JDWPException
/*      */       {
/*   40 */         paramPacketStream.waitForReply();
/*   41 */         return new Version(paramVirtualMachineImpl, paramPacketStream);
/*      */       }
/*      */ 
/*      */       private Version(VirtualMachineImpl paramVirtualMachineImpl, PacketStream paramPacketStream)
/*      */       {
/*   71 */         if (paramVirtualMachineImpl.traceReceives) {
/*   72 */           paramVirtualMachineImpl.printTrace("Receiving Command(id=" + paramPacketStream.pkt.id + ") JDWP.VirtualMachine.Version" + (paramPacketStream.pkt.flags != 0 ? ", FLAGS=" + paramPacketStream.pkt.flags : "") + (paramPacketStream.pkt.errorCode != 0 ? ", ERROR CODE=" + paramPacketStream.pkt.errorCode : ""));
/*      */         }
/*   74 */         this.description = paramPacketStream.readString();
/*   75 */         if (paramVirtualMachineImpl.traceReceives) {
/*   76 */           paramVirtualMachineImpl.printReceiveTrace(4, "description(String): " + this.description);
/*      */         }
/*   78 */         this.jdwpMajor = paramPacketStream.readInt();
/*   79 */         if (paramVirtualMachineImpl.traceReceives) {
/*   80 */           paramVirtualMachineImpl.printReceiveTrace(4, "jdwpMajor(int): " + this.jdwpMajor);
/*      */         }
/*   82 */         this.jdwpMinor = paramPacketStream.readInt();
/*   83 */         if (paramVirtualMachineImpl.traceReceives) {
/*   84 */           paramVirtualMachineImpl.printReceiveTrace(4, "jdwpMinor(int): " + this.jdwpMinor);
/*      */         }
/*   86 */         this.vmVersion = paramPacketStream.readString();
/*   87 */         if (paramVirtualMachineImpl.traceReceives) {
/*   88 */           paramVirtualMachineImpl.printReceiveTrace(4, "vmVersion(String): " + this.vmVersion);
/*      */         }
/*   90 */         this.vmName = paramPacketStream.readString();
/*   91 */         if (paramVirtualMachineImpl.traceReceives)
/*   92 */           paramVirtualMachineImpl.printReceiveTrace(4, "vmName(String): " + this.vmName);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.JDWP
 * JD-Core Version:    0.6.2
 */