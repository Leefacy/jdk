/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.BooleanValue;
/*     */ import com.sun.jdi.ByteValue;
/*     */ import com.sun.jdi.CharValue;
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.DoubleValue;
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.FloatValue;
/*     */ import com.sun.jdi.IntegerValue;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.LongValue;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.PrimitiveValue;
/*     */ import com.sun.jdi.ShortValue;
/*     */ import com.sun.jdi.Value;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ class PacketStream
/*     */ {
/*     */   final VirtualMachineImpl vm;
/*  34 */   private int inCursor = 0;
/*     */   final Packet pkt;
/*  36 */   private ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
/*  37 */   private boolean isCommitted = false;
/*     */ 
/*     */   PacketStream(VirtualMachineImpl paramVirtualMachineImpl, int paramInt1, int paramInt2) {
/*  40 */     this.vm = paramVirtualMachineImpl;
/*  41 */     this.pkt = new Packet();
/*  42 */     this.pkt.cmdSet = ((short)paramInt1);
/*  43 */     this.pkt.cmd = ((short)paramInt2);
/*     */   }
/*     */ 
/*     */   PacketStream(VirtualMachineImpl paramVirtualMachineImpl, Packet paramPacket) {
/*  47 */     this.vm = paramVirtualMachineImpl;
/*  48 */     this.pkt = paramPacket;
/*  49 */     this.isCommitted = true;
/*     */   }
/*     */ 
/*     */   int id() {
/*  53 */     return this.pkt.id;
/*     */   }
/*     */ 
/*     */   void send() {
/*  57 */     if (!this.isCommitted) {
/*  58 */       this.pkt.data = this.dataStream.toByteArray();
/*  59 */       this.vm.sendToTarget(this.pkt);
/*  60 */       this.isCommitted = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void waitForReply() throws JDWPException {
/*  65 */     if (!this.isCommitted) {
/*  66 */       throw new InternalException("waitForReply without send");
/*     */     }
/*     */ 
/*  69 */     this.vm.waitForTargetReply(this.pkt);
/*     */ 
/*  71 */     if (this.pkt.errorCode != 0)
/*  72 */       throw new JDWPException(this.pkt.errorCode);
/*     */   }
/*     */ 
/*     */   void writeBoolean(boolean paramBoolean)
/*     */   {
/*  77 */     if (paramBoolean)
/*  78 */       this.dataStream.write(1);
/*     */     else
/*  80 */       this.dataStream.write(0);
/*     */   }
/*     */ 
/*     */   void writeByte(byte paramByte)
/*     */   {
/*  85 */     this.dataStream.write(paramByte);
/*     */   }
/*     */ 
/*     */   void writeChar(char paramChar) {
/*  89 */     this.dataStream.write((byte)(paramChar >>> '\b' & 0xFF));
/*  90 */     this.dataStream.write((byte)(paramChar >>> '\000' & 0xFF));
/*     */   }
/*     */ 
/*     */   void writeShort(short paramShort) {
/*  94 */     this.dataStream.write((byte)(paramShort >>> 8 & 0xFF));
/*  95 */     this.dataStream.write((byte)(paramShort >>> 0 & 0xFF));
/*     */   }
/*     */ 
/*     */   void writeInt(int paramInt) {
/*  99 */     this.dataStream.write((byte)(paramInt >>> 24 & 0xFF));
/* 100 */     this.dataStream.write((byte)(paramInt >>> 16 & 0xFF));
/* 101 */     this.dataStream.write((byte)(paramInt >>> 8 & 0xFF));
/* 102 */     this.dataStream.write((byte)(paramInt >>> 0 & 0xFF));
/*     */   }
/*     */ 
/*     */   void writeLong(long paramLong) {
/* 106 */     this.dataStream.write((byte)(int)(paramLong >>> 56 & 0xFF));
/* 107 */     this.dataStream.write((byte)(int)(paramLong >>> 48 & 0xFF));
/* 108 */     this.dataStream.write((byte)(int)(paramLong >>> 40 & 0xFF));
/* 109 */     this.dataStream.write((byte)(int)(paramLong >>> 32 & 0xFF));
/*     */ 
/* 111 */     this.dataStream.write((byte)(int)(paramLong >>> 24 & 0xFF));
/* 112 */     this.dataStream.write((byte)(int)(paramLong >>> 16 & 0xFF));
/* 113 */     this.dataStream.write((byte)(int)(paramLong >>> 8 & 0xFF));
/* 114 */     this.dataStream.write((byte)(int)(paramLong >>> 0 & 0xFF));
/*     */   }
/*     */ 
/*     */   void writeFloat(float paramFloat) {
/* 118 */     writeInt(Float.floatToIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   void writeDouble(double paramDouble) {
/* 122 */     writeLong(Double.doubleToLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   void writeID(int paramInt, long paramLong) {
/* 126 */     switch (paramInt) {
/*     */     case 8:
/* 128 */       writeLong(paramLong);
/* 129 */       break;
/*     */     case 4:
/* 131 */       writeInt((int)paramLong);
/* 132 */       break;
/*     */     case 2:
/* 134 */       writeShort((short)(int)paramLong);
/* 135 */       break;
/*     */     default:
/* 137 */       throw new UnsupportedOperationException("JDWP: ID size not supported: " + paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeNullObjectRef() {
/* 142 */     writeObjectRef(0L);
/*     */   }
/*     */ 
/*     */   void writeObjectRef(long paramLong) {
/* 146 */     writeID(this.vm.sizeofObjectRef, paramLong);
/*     */   }
/*     */ 
/*     */   void writeClassRef(long paramLong) {
/* 150 */     writeID(this.vm.sizeofClassRef, paramLong);
/*     */   }
/*     */ 
/*     */   void writeMethodRef(long paramLong) {
/* 154 */     writeID(this.vm.sizeofMethodRef, paramLong);
/*     */   }
/*     */ 
/*     */   void writeFieldRef(long paramLong) {
/* 158 */     writeID(this.vm.sizeofFieldRef, paramLong);
/*     */   }
/*     */ 
/*     */   void writeFrameRef(long paramLong) {
/* 162 */     writeID(this.vm.sizeofFrameRef, paramLong);
/*     */   }
/*     */ 
/*     */   void writeByteArray(byte[] paramArrayOfByte) {
/* 166 */     this.dataStream.write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   void writeString(String paramString) {
/*     */     try {
/* 171 */       byte[] arrayOfByte = paramString.getBytes("UTF8");
/* 172 */       writeInt(arrayOfByte.length);
/* 173 */       writeByteArray(arrayOfByte);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 175 */       throw new InternalException("Cannot convert string to UTF8 bytes");
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeLocation(Location paramLocation) {
/* 180 */     ReferenceTypeImpl localReferenceTypeImpl = (ReferenceTypeImpl)paramLocation.declaringType();
/*     */     byte b;
/* 182 */     if ((localReferenceTypeImpl instanceof ClassType))
/* 183 */       b = 1;
/* 184 */     else if ((localReferenceTypeImpl instanceof InterfaceType))
/*     */     {
/* 186 */       b = 2;
/*     */     }
/* 188 */     else throw new InternalException("Invalid Location");
/*     */ 
/* 190 */     writeByte(b);
/* 191 */     writeClassRef(localReferenceTypeImpl.ref());
/* 192 */     writeMethodRef(((MethodImpl)paramLocation.method()).ref());
/* 193 */     writeLong(paramLocation.codeIndex());
/*     */   }
/*     */ 
/*     */   void writeValue(Value paramValue) {
/*     */     try {
/* 198 */       writeValueChecked(paramValue);
/*     */     } catch (InvalidTypeException localInvalidTypeException) {
/* 200 */       throw new RuntimeException("Internal error: Invalid Tag/Type pair");
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeValueChecked(Value paramValue) throws InvalidTypeException
/*     */   {
/* 206 */     writeByte(ValueImpl.typeValueKey(paramValue));
/* 207 */     writeUntaggedValue(paramValue);
/*     */   }
/*     */ 
/*     */   void writeUntaggedValue(Value paramValue) {
/*     */     try {
/* 212 */       writeUntaggedValueChecked(paramValue);
/*     */     } catch (InvalidTypeException localInvalidTypeException) {
/* 214 */       throw new RuntimeException("Internal error: Invalid Tag/Type pair");
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeUntaggedValueChecked(Value paramValue) throws InvalidTypeException
/*     */   {
/* 220 */     byte b = ValueImpl.typeValueKey(paramValue);
/* 221 */     if (isObjectTag(b)) {
/* 222 */       if (paramValue == null) {
/* 223 */         writeObjectRef(0L);
/*     */       } else {
/* 225 */         if (!(paramValue instanceof ObjectReference)) {
/* 226 */           throw new InvalidTypeException();
/*     */         }
/* 228 */         writeObjectRef(((ObjectReferenceImpl)paramValue).ref());
/*     */       }
/*     */     }
/* 231 */     else switch (b) {
/*     */       case 66:
/* 233 */         if (!(paramValue instanceof ByteValue)) {
/* 234 */           throw new InvalidTypeException();
/*     */         }
/* 236 */         writeByte(((PrimitiveValue)paramValue).byteValue());
/* 237 */         break;
/*     */       case 67:
/* 240 */         if (!(paramValue instanceof CharValue)) {
/* 241 */           throw new InvalidTypeException();
/*     */         }
/* 243 */         writeChar(((PrimitiveValue)paramValue).charValue());
/* 244 */         break;
/*     */       case 70:
/* 247 */         if (!(paramValue instanceof FloatValue)) {
/* 248 */           throw new InvalidTypeException();
/*     */         }
/* 250 */         writeFloat(((PrimitiveValue)paramValue).floatValue());
/* 251 */         break;
/*     */       case 68:
/* 254 */         if (!(paramValue instanceof DoubleValue)) {
/* 255 */           throw new InvalidTypeException();
/*     */         }
/* 257 */         writeDouble(((PrimitiveValue)paramValue).doubleValue());
/* 258 */         break;
/*     */       case 73:
/* 261 */         if (!(paramValue instanceof IntegerValue)) {
/* 262 */           throw new InvalidTypeException();
/*     */         }
/* 264 */         writeInt(((PrimitiveValue)paramValue).intValue());
/* 265 */         break;
/*     */       case 74:
/* 268 */         if (!(paramValue instanceof LongValue)) {
/* 269 */           throw new InvalidTypeException();
/*     */         }
/* 271 */         writeLong(((PrimitiveValue)paramValue).longValue());
/* 272 */         break;
/*     */       case 83:
/* 275 */         if (!(paramValue instanceof ShortValue)) {
/* 276 */           throw new InvalidTypeException();
/*     */         }
/* 278 */         writeShort(((PrimitiveValue)paramValue).shortValue());
/* 279 */         break;
/*     */       case 90:
/* 282 */         if (!(paramValue instanceof BooleanValue)) {
/* 283 */           throw new InvalidTypeException();
/*     */         }
/* 285 */         writeBoolean(((PrimitiveValue)paramValue).booleanValue());
/*     */       case 69:
/*     */       case 71:
/*     */       case 72:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*     */       case 88:
/*     */       case 89: }   } 
/* 297 */   byte readByte() { byte b = this.pkt.data[this.inCursor];
/* 298 */     this.inCursor += 1;
/* 299 */     return b;
/*     */   }
/*     */ 
/*     */   boolean readBoolean()
/*     */   {
/* 306 */     int i = readByte();
/* 307 */     return i != 0;
/*     */   }
/*     */ 
/*     */   char readChar()
/*     */   {
/* 316 */     int i = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 317 */     int j = this.pkt.data[(this.inCursor++)] & 0xFF;
/*     */ 
/* 319 */     return (char)((i << 8) + j);
/*     */   }
/*     */ 
/*     */   short readShort()
/*     */   {
/* 328 */     int i = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 329 */     int j = this.pkt.data[(this.inCursor++)] & 0xFF;
/*     */ 
/* 331 */     return (short)((i << 8) + j);
/*     */   }
/*     */ 
/*     */   int readInt()
/*     */   {
/* 340 */     int i = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 341 */     int j = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 342 */     int k = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 343 */     int m = this.pkt.data[(this.inCursor++)] & 0xFF;
/*     */ 
/* 345 */     return (i << 24) + (j << 16) + (k << 8) + m;
/*     */   }
/*     */ 
/*     */   long readLong()
/*     */   {
/* 355 */     long l1 = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 356 */     long l2 = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 357 */     long l3 = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 358 */     long l4 = this.pkt.data[(this.inCursor++)] & 0xFF;
/*     */ 
/* 360 */     long l5 = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 361 */     long l6 = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 362 */     long l7 = this.pkt.data[(this.inCursor++)] & 0xFF;
/* 363 */     long l8 = this.pkt.data[(this.inCursor++)] & 0xFF;
/*     */ 
/* 365 */     return (l1 << 56) + (l2 << 48) + (l3 << 40) + (l4 << 32) + (l5 << 24) + (l6 << 16) + (l7 << 8) + l8;
/*     */   }
/*     */ 
/*     */   float readFloat()
/*     */   {
/* 373 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */ 
/*     */   double readDouble()
/*     */   {
/* 380 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   String readString()
/*     */   {
/* 389 */     int i = readInt();
/*     */     String str;
/*     */     try
/*     */     {
/* 392 */       str = new String(this.pkt.data, this.inCursor, i, "UTF8");
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 394 */       System.err.println(localUnsupportedEncodingException);
/* 395 */       str = "Conversion error!";
/*     */     }
/* 397 */     this.inCursor += i;
/* 398 */     return str;
/*     */   }
/*     */ 
/*     */   private long readID(int paramInt) {
/* 402 */     switch (paramInt) {
/*     */     case 8:
/* 404 */       return readLong();
/*     */     case 4:
/* 406 */       return readInt();
/*     */     case 2:
/* 408 */       return readShort();
/*     */     }
/* 410 */     throw new UnsupportedOperationException("JDWP: ID size not supported: " + paramInt);
/*     */   }
/*     */ 
/*     */   long readObjectRef()
/*     */   {
/* 418 */     return readID(this.vm.sizeofObjectRef);
/*     */   }
/*     */ 
/*     */   long readClassRef() {
/* 422 */     return readID(this.vm.sizeofClassRef);
/*     */   }
/*     */ 
/*     */   ObjectReferenceImpl readTaggedObjectReference() {
/* 426 */     int i = readByte();
/* 427 */     return this.vm.objectMirror(readObjectRef(), i);
/*     */   }
/*     */ 
/*     */   ObjectReferenceImpl readObjectReference() {
/* 431 */     return this.vm.objectMirror(readObjectRef());
/*     */   }
/*     */ 
/*     */   StringReferenceImpl readStringReference() {
/* 435 */     long l = readObjectRef();
/* 436 */     return this.vm.stringMirror(l);
/*     */   }
/*     */ 
/*     */   ArrayReferenceImpl readArrayReference() {
/* 440 */     long l = readObjectRef();
/* 441 */     return this.vm.arrayMirror(l);
/*     */   }
/*     */ 
/*     */   ThreadReferenceImpl readThreadReference() {
/* 445 */     long l = readObjectRef();
/* 446 */     return this.vm.threadMirror(l);
/*     */   }
/*     */ 
/*     */   ThreadGroupReferenceImpl readThreadGroupReference() {
/* 450 */     long l = readObjectRef();
/* 451 */     return this.vm.threadGroupMirror(l);
/*     */   }
/*     */ 
/*     */   ClassLoaderReferenceImpl readClassLoaderReference() {
/* 455 */     long l = readObjectRef();
/* 456 */     return this.vm.classLoaderMirror(l);
/*     */   }
/*     */ 
/*     */   ClassObjectReferenceImpl readClassObjectReference() {
/* 460 */     long l = readObjectRef();
/* 461 */     return this.vm.classObjectMirror(l);
/*     */   }
/*     */ 
/*     */   ReferenceTypeImpl readReferenceType() {
/* 465 */     byte b = readByte();
/* 466 */     long l = readObjectRef();
/* 467 */     return this.vm.referenceType(l, b);
/*     */   }
/*     */ 
/*     */   long readMethodRef()
/*     */   {
/* 474 */     return readID(this.vm.sizeofMethodRef);
/*     */   }
/*     */ 
/*     */   long readFieldRef()
/*     */   {
/* 481 */     return readID(this.vm.sizeofFieldRef);
/*     */   }
/*     */ 
/*     */   Field readField()
/*     */   {
/* 488 */     ReferenceTypeImpl localReferenceTypeImpl = readReferenceType();
/* 489 */     long l = readFieldRef();
/* 490 */     return localReferenceTypeImpl.getFieldMirror(l);
/*     */   }
/*     */ 
/*     */   long readFrameRef()
/*     */   {
/* 497 */     return readID(this.vm.sizeofFrameRef);
/*     */   }
/*     */ 
/*     */   ValueImpl readValue()
/*     */   {
/* 504 */     byte b = readByte();
/* 505 */     return readUntaggedValue(b);
/*     */   }
/*     */ 
/*     */   ValueImpl readUntaggedValue(byte paramByte) {
/* 509 */     Object localObject = null;
/*     */ 
/* 511 */     if (isObjectTag(paramByte))
/* 512 */       localObject = this.vm.objectMirror(readObjectRef(), paramByte);
/*     */     else
/* 514 */       switch (paramByte) {
/*     */       case 66:
/* 516 */         localObject = new ByteValueImpl(this.vm, readByte());
/* 517 */         break;
/*     */       case 67:
/* 520 */         localObject = new CharValueImpl(this.vm, readChar());
/* 521 */         break;
/*     */       case 70:
/* 524 */         localObject = new FloatValueImpl(this.vm, readFloat());
/* 525 */         break;
/*     */       case 68:
/* 528 */         localObject = new DoubleValueImpl(this.vm, readDouble());
/* 529 */         break;
/*     */       case 73:
/* 532 */         localObject = new IntegerValueImpl(this.vm, readInt());
/* 533 */         break;
/*     */       case 74:
/* 536 */         localObject = new LongValueImpl(this.vm, readLong());
/* 537 */         break;
/*     */       case 83:
/* 540 */         localObject = new ShortValueImpl(this.vm, readShort());
/* 541 */         break;
/*     */       case 90:
/* 544 */         localObject = new BooleanValueImpl(this.vm, readBoolean());
/* 545 */         break;
/*     */       case 86:
/* 548 */         localObject = new VoidValueImpl(this.vm);
/*     */       case 69:
/*     */       case 71:
/*     */       case 72:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 84:
/*     */       case 85:
/*     */       case 87:
/*     */       case 88:
/* 552 */       case 89: }  return localObject;
/*     */   }
/*     */ 
/*     */   Location readLocation()
/*     */   {
/* 559 */     byte b = readByte();
/* 560 */     long l1 = readObjectRef();
/* 561 */     long l2 = readMethodRef();
/* 562 */     long l3 = readLong();
/* 563 */     if (l1 != 0L)
/*     */     {
/* 565 */       ReferenceTypeImpl localReferenceTypeImpl = this.vm.referenceType(l1, b);
/* 566 */       return new LocationImpl(this.vm, localReferenceTypeImpl, l2, l3);
/*     */     }
/*     */ 
/* 569 */     return null;
/*     */   }
/*     */ 
/*     */   byte[] readByteArray(int paramInt)
/*     */   {
/* 574 */     byte[] arrayOfByte = new byte[paramInt];
/* 575 */     System.arraycopy(this.pkt.data, this.inCursor, arrayOfByte, 0, paramInt);
/* 576 */     this.inCursor += paramInt;
/* 577 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   List<Value> readArrayRegion() {
/* 581 */     byte b = readByte();
/* 582 */     int i = readInt();
/* 583 */     ArrayList localArrayList = new ArrayList(i);
/* 584 */     boolean bool = isObjectTag(b);
/* 585 */     for (int j = 0; j < i; j++)
/*     */     {
/* 592 */       if (bool) {
/* 593 */         b = readByte();
/*     */       }
/* 595 */       ValueImpl localValueImpl = readUntaggedValue(b);
/* 596 */       localArrayList.add(localValueImpl);
/*     */     }
/*     */ 
/* 599 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   void writeArrayRegion(List<Value> paramList) {
/* 603 */     writeInt(paramList.size());
/* 604 */     for (int i = 0; i < paramList.size(); i++) {
/* 605 */       Value localValue = (Value)paramList.get(i);
/* 606 */       writeUntaggedValue(localValue);
/*     */     }
/*     */   }
/*     */ 
/*     */   int skipBytes(int paramInt) {
/* 611 */     this.inCursor += paramInt;
/* 612 */     return paramInt;
/*     */   }
/*     */ 
/*     */   byte command() {
/* 616 */     return (byte)this.pkt.cmd;
/*     */   }
/*     */ 
/*     */   static boolean isObjectTag(byte paramByte) {
/* 620 */     return (paramByte == 76) || (paramByte == 91) || (paramByte == 115) || (paramByte == 116) || (paramByte == 103) || (paramByte == 108) || (paramByte == 99);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.PacketStream
 * JD-Core Version:    0.6.2
 */