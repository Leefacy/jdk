/*     */ package com.sun.tools.hat.internal.parser;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.ArrayTypeCodes;
/*     */ import com.sun.tools.hat.internal.model.JavaBoolean;
/*     */ import com.sun.tools.hat.internal.model.JavaByte;
/*     */ import com.sun.tools.hat.internal.model.JavaChar;
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaDouble;
/*     */ import com.sun.tools.hat.internal.model.JavaField;
/*     */ import com.sun.tools.hat.internal.model.JavaFloat;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaInt;
/*     */ import com.sun.tools.hat.internal.model.JavaLong;
/*     */ import com.sun.tools.hat.internal.model.JavaObject;
/*     */ import com.sun.tools.hat.internal.model.JavaObjectArray;
/*     */ import com.sun.tools.hat.internal.model.JavaObjectRef;
/*     */ import com.sun.tools.hat.internal.model.JavaShort;
/*     */ import com.sun.tools.hat.internal.model.JavaStatic;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.JavaValueArray;
/*     */ import com.sun.tools.hat.internal.model.Root;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.model.StackFrame;
/*     */ import com.sun.tools.hat.internal.model.StackTrace;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Date;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class HprofReader extends Reader
/*     */   implements ArrayTypeCodes
/*     */ {
/*     */   static final int MAGIC_NUMBER = 1245795905;
/*  51 */   private static final String[] VERSIONS = { "", "", "" };
/*     */   private static final int VERSION_JDK12BETA3 = 0;
/*     */   private static final int VERSION_JDK12BETA4 = 1;
/*     */   private static final int VERSION_JDK6 = 2;
/*     */   static final int HPROF_UTF8 = 1;
/*     */   static final int HPROF_LOAD_CLASS = 2;
/*     */   static final int HPROF_UNLOAD_CLASS = 3;
/*     */   static final int HPROF_FRAME = 4;
/*     */   static final int HPROF_TRACE = 5;
/*     */   static final int HPROF_ALLOC_SITES = 6;
/*     */   static final int HPROF_HEAP_SUMMARY = 7;
/*     */   static final int HPROF_START_THREAD = 10;
/*     */   static final int HPROF_END_THREAD = 11;
/*     */   static final int HPROF_HEAP_DUMP = 12;
/*     */   static final int HPROF_CPU_SAMPLES = 13;
/*     */   static final int HPROF_CONTROL_SETTINGS = 14;
/*     */   static final int HPROF_LOCKSTATS_WAIT_TIME = 16;
/*     */   static final int HPROF_LOCKSTATS_HOLD_TIME = 17;
/*     */   static final int HPROF_GC_ROOT_UNKNOWN = 255;
/*     */   static final int HPROF_GC_ROOT_JNI_GLOBAL = 1;
/*     */   static final int HPROF_GC_ROOT_JNI_LOCAL = 2;
/*     */   static final int HPROF_GC_ROOT_JAVA_FRAME = 3;
/*     */   static final int HPROF_GC_ROOT_NATIVE_STACK = 4;
/*     */   static final int HPROF_GC_ROOT_STICKY_CLASS = 5;
/*     */   static final int HPROF_GC_ROOT_THREAD_BLOCK = 6;
/*     */   static final int HPROF_GC_ROOT_MONITOR_USED = 7;
/*     */   static final int HPROF_GC_ROOT_THREAD_OBJ = 8;
/*     */   static final int HPROF_GC_CLASS_DUMP = 32;
/*     */   static final int HPROF_GC_INSTANCE_DUMP = 33;
/*     */   static final int HPROF_GC_OBJ_ARRAY_DUMP = 34;
/*     */   static final int HPROF_GC_PRIM_ARRAY_DUMP = 35;
/*     */   static final int HPROF_HEAP_DUMP_SEGMENT = 28;
/*     */   static final int HPROF_HEAP_DUMP_END = 44;
/*     */   private static final int T_CLASS = 2;
/*     */   private int version;
/*     */   private int debugLevel;
/*     */   private long currPos;
/*     */   private int dumpsToSkip;
/*     */   private boolean callStack;
/*     */   private int identifierSize;
/*     */   private Hashtable<Long, String> names;
/*     */   private Hashtable<Integer, ThreadObject> threadObjects;
/*     */   private Hashtable<Long, String> classNameFromObjectID;
/*     */   private Hashtable<Integer, String> classNameFromSerialNo;
/*     */   private Hashtable<Long, StackFrame> stackFrames;
/*     */   private Hashtable<Integer, StackTrace> stackTraces;
/*     */   private Snapshot snapshot;
/*     */ 
/*     */   public HprofReader(String paramString, PositionDataInputStream paramPositionDataInputStream, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 157 */     super(paramPositionDataInputStream);
/* 158 */     RandomAccessFile localRandomAccessFile = new RandomAccessFile(paramString, "r");
/* 159 */     this.snapshot = new Snapshot(MappedReadBuffer.create(localRandomAccessFile));
/* 160 */     this.dumpsToSkip = (paramInt1 - 1);
/* 161 */     this.callStack = paramBoolean;
/* 162 */     this.debugLevel = paramInt2;
/* 163 */     this.names = new Hashtable();
/* 164 */     this.threadObjects = new Hashtable(43);
/* 165 */     this.classNameFromObjectID = new Hashtable();
/* 166 */     if (paramBoolean) {
/* 167 */       this.stackFrames = new Hashtable(43);
/* 168 */       this.stackTraces = new Hashtable(43);
/* 169 */       this.classNameFromSerialNo = new Hashtable();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Snapshot read() throws IOException {
/* 174 */     this.currPos = 4L;
/* 175 */     this.version = readVersionHeader();
/* 176 */     this.identifierSize = this.in.readInt();
/* 177 */     this.snapshot.setIdentifierSize(this.identifierSize);
/* 178 */     if (this.version >= 1)
/* 179 */       this.snapshot.setNewStyleArrayClass(true);
/*     */     else {
/* 181 */       this.snapshot.setNewStyleArrayClass(false);
/*     */     }
/*     */ 
/* 184 */     this.currPos += 4L;
/* 185 */     if ((this.identifierSize != 4) && (this.identifierSize != 8)) {
/* 186 */       throw new IOException("I'm sorry, but I can't deal with an identifier size of " + this.identifierSize + ".  I can only deal with 4 or 8.");
/*     */     }
/* 188 */     System.out.println("Dump file created " + new Date(this.in.readLong()));
/* 189 */     this.currPos += 8L;
/*     */     while (true)
/*     */     {
/*     */       int i;
/*     */       try {
/* 194 */         i = this.in.readUnsignedByte();
/*     */       } catch (EOFException localEOFException1) {
/* 196 */         break;
/*     */       }
/* 198 */       this.in.readInt();
/*     */ 
/* 201 */       long l1 = this.in.readInt() & 0xFFFFFFFF;
/* 202 */       if (this.debugLevel > 0) {
/* 203 */         System.out.println("Read record type " + i + ", length " + l1 + " at position " + 
/* 205 */           toHex(this.currPos));
/*     */       }
/*     */ 
/* 207 */       if (l1 < 0L)
/*     */       {
/* 209 */         throw new IOException("Bad record length of " + l1 + " at byte " + 
/* 209 */           toHex(this.currPos + 5L) + 
/* 209 */           " of file.");
/*     */       }
/*     */ 
/* 212 */       this.currPos += 9L + l1;
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 213 */       switch (i) {
/*     */       case 1:
/* 215 */         long l2 = readID();
/* 216 */         localObject1 = new byte[(int)l1 - this.identifierSize];
/* 217 */         this.in.readFully((byte[])localObject1);
/* 218 */         this.names.put(new Long(l2), new String((byte[])localObject1));
/* 219 */         break;
/*     */       case 2:
/* 222 */         int j = this.in.readInt();
/* 223 */         long l4 = readID();
/* 224 */         int n = this.in.readInt();
/* 225 */         long l5 = readID();
/* 226 */         localObject2 = new Long(l4);
/* 227 */         String str3 = getNameFromID(l5).replace('/', '.');
/* 228 */         this.classNameFromObjectID.put(localObject2, str3);
/* 229 */         if (this.classNameFromSerialNo != null)
/* 230 */           this.classNameFromSerialNo.put(new Integer(j), str3); break;
/*     */       case 12:
/* 236 */         if (this.dumpsToSkip <= 0) {
/*     */           try {
/* 238 */             readHeapDump(l1, this.currPos);
/*     */           } catch (EOFException localEOFException2) {
/* 240 */             handleEOF(localEOFException2, this.snapshot);
/*     */           }
/* 242 */           if (this.debugLevel > 0) {
/* 243 */             System.out.println("    Finished processing instances in heap dump.");
/*     */           }
/* 245 */           return this.snapshot;
/*     */         }
/* 247 */         this.dumpsToSkip -= 1;
/* 248 */         skipBytes(l1);
/*     */ 
/* 250 */         break;
/*     */       case 44:
/* 254 */         if (this.version >= 2) {
/* 255 */           if (this.dumpsToSkip <= 0) {
/* 256 */             skipBytes(l1);
/* 257 */             return this.snapshot;
/*     */           }
/*     */ 
/* 260 */           this.dumpsToSkip -= 1;
/*     */         }
/*     */         else
/*     */         {
/* 264 */           warn("Ignoring unrecognized record type " + i);
/*     */         }
/* 266 */         skipBytes(l1);
/* 267 */         break;
/*     */       case 28:
/* 271 */         if (this.version >= 2) {
/* 272 */           if (this.dumpsToSkip <= 0) {
/*     */             try
/*     */             {
/* 275 */               readHeapDump(l1, this.currPos);
/*     */             } catch (EOFException localEOFException3) {
/* 277 */               handleEOF(localEOFException3, this.snapshot);
/*     */             }
/*     */           }
/*     */           else
/* 281 */             skipBytes(l1);
/*     */         }
/*     */         else
/*     */         {
/* 285 */           warn("Ignoring unrecognized record type " + i);
/* 286 */           skipBytes(l1);
/*     */         }
/* 288 */         break;
/*     */       case 4:
/* 292 */         if (this.stackFrames == null) {
/* 293 */           skipBytes(l1);
/*     */         } else {
/* 295 */           long l3 = readID();
/* 296 */           localObject1 = getNameFromID(readID());
/* 297 */           String str1 = getNameFromID(readID());
/* 298 */           String str2 = getNameFromID(readID());
/* 299 */           int i2 = this.in.readInt();
/* 300 */           localObject2 = (String)this.classNameFromSerialNo.get(new Integer(i2));
/* 301 */           int i3 = this.in.readInt();
/* 302 */           if (i3 < -3) {
/* 303 */             warn("Weird stack frame line number:  " + i3);
/* 304 */             i3 = -1;
/*     */           }
/* 306 */           this.stackFrames.put(new Long(l3), new StackFrame((String)localObject1, str1, (String)localObject2, str2, i3));
/*     */         }
/*     */ 
/* 311 */         break;
/*     */       case 5:
/* 314 */         if (this.stackTraces == null) {
/* 315 */           skipBytes(l1);
/*     */         } else {
/* 317 */           int k = this.in.readInt();
/* 318 */           int m = this.in.readInt();
/* 319 */           localObject1 = new StackFrame[this.in.readInt()];
/* 320 */           for (int i1 = 0; i1 < localObject1.length; i1++) {
/* 321 */             long l6 = readID();
/* 322 */             localObject1[i1] = ((StackFrame)this.stackFrames.get(new Long(l6)));
/* 323 */             if (localObject1[i1] == null) {
/* 324 */               throw new IOException("Stack frame " + toHex(l6) + " not found");
/*     */             }
/*     */           }
/* 327 */           this.stackTraces.put(new Integer(k), new StackTrace((StackFrame[])localObject1));
/*     */         }
/*     */ 
/* 330 */         break;
/*     */       case 3:
/*     */       case 6:
/*     */       case 7:
/*     */       case 10:
/*     */       case 11:
/*     */       case 13:
/*     */       case 14:
/*     */       case 16:
/*     */       case 17:
/* 343 */         skipBytes(l1);
/* 344 */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 15:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/*     */       case 33:
/*     */       case 34:
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case 38:
/*     */       case 39:
/*     */       case 40:
/*     */       case 41:
/*     */       case 42:
/*     */       case 43:
/*     */       default:
/* 347 */         skipBytes(l1);
/* 348 */         warn("Ignoring unrecognized record type " + i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 353 */     return this.snapshot;
/*     */   }
/*     */ 
/*     */   private void skipBytes(long paramLong) throws IOException {
/* 357 */     this.in.skipBytes((int)paramLong);
/*     */   }
/*     */ 
/*     */   private int readVersionHeader() throws IOException {
/* 361 */     int i = VERSIONS.length;
/* 362 */     boolean[] arrayOfBoolean = new boolean[VERSIONS.length];
/* 363 */     for (int j = 0; j < i; j++) {
/* 364 */       arrayOfBoolean[j] = true;
/*     */     }
/*     */ 
/* 367 */     j = 0;
/* 368 */     while (i > 0) {
/* 369 */       int k = (char)this.in.readByte();
/* 370 */       this.currPos += 1L;
/* 371 */       for (int m = 0; m < VERSIONS.length; m++) {
/* 372 */         if (arrayOfBoolean[m] != 0) {
/* 373 */           if (k != VERSIONS[m].charAt(j)) {
/* 374 */             arrayOfBoolean[m] = false;
/* 375 */             i--;
/* 376 */           } else if (j == VERSIONS[m].length() - 1) {
/* 377 */             return m;
/*     */           }
/*     */         }
/*     */       }
/* 381 */       j++;
/*     */     }
/* 383 */     throw new IOException("Version string not recognized at byte " + (j + 3));
/*     */   }
/*     */ 
/*     */   private void readHeapDump(long paramLong1, long paramLong2) throws IOException {
/* 387 */     while (paramLong1 > 0L) {
/* 388 */       int i = this.in.readUnsignedByte();
/* 389 */       if (this.debugLevel > 0) {
/* 390 */         System.out.println("    Read heap sub-record type " + i + " at position " + 
/* 392 */           toHex(paramLong2 - paramLong1));
/*     */       }
/*     */ 
/* 394 */       paramLong1 -= 1L;
/*     */       long l1;
/*     */       int n;
/*     */       int m;
/*     */       Object localObject;
/*     */       StackTrace localStackTrace;
/*     */       ThreadObject localThreadObject;
/*     */       int j;
/* 395 */       switch (i) {
/*     */       case 255:
/* 397 */         l1 = readID();
/* 398 */         paramLong1 -= this.identifierSize;
/* 399 */         this.snapshot.addRoot(new Root(l1, 0L, 1, ""));
/* 400 */         break;
/*     */       case 8:
/* 403 */         l1 = readID();
/* 404 */         int k = this.in.readInt();
/* 405 */         n = this.in.readInt();
/* 406 */         paramLong1 -= this.identifierSize + 8;
/* 407 */         this.threadObjects.put(new Integer(k), new ThreadObject(l1, n));
/*     */ 
/* 409 */         break;
/*     */       case 1:
/* 412 */         l1 = readID();
/* 413 */         long l2 = readID();
/* 414 */         paramLong1 -= 2 * this.identifierSize;
/* 415 */         this.snapshot.addRoot(new Root(l1, 0L, 4, ""));
/* 416 */         break;
/*     */       case 2:
/* 419 */         l1 = readID();
/* 420 */         m = this.in.readInt();
/* 421 */         n = this.in.readInt();
/* 422 */         paramLong1 -= this.identifierSize + 8;
/* 423 */         localObject = getThreadObjectFromSequence(m);
/* 424 */         localStackTrace = getStackTraceFromSerial(((ThreadObject)localObject).stackSeq);
/* 425 */         if (localStackTrace != null) {
/* 426 */           localStackTrace = localStackTrace.traceForDepth(n + 1);
/*     */         }
/* 428 */         this.snapshot.addRoot(new Root(l1, ((ThreadObject)localObject).threadId, 3, "", localStackTrace));
/*     */ 
/* 430 */         break;
/*     */       case 3:
/* 433 */         l1 = readID();
/* 434 */         m = this.in.readInt();
/* 435 */         n = this.in.readInt();
/* 436 */         paramLong1 -= this.identifierSize + 8;
/* 437 */         localObject = getThreadObjectFromSequence(m);
/* 438 */         localStackTrace = getStackTraceFromSerial(((ThreadObject)localObject).stackSeq);
/* 439 */         if (localStackTrace != null) {
/* 440 */           localStackTrace = localStackTrace.traceForDepth(n + 1);
/*     */         }
/* 442 */         this.snapshot.addRoot(new Root(l1, ((ThreadObject)localObject).threadId, 7, "", localStackTrace));
/*     */ 
/* 444 */         break;
/*     */       case 4:
/* 447 */         l1 = readID();
/* 448 */         m = this.in.readInt();
/* 449 */         paramLong1 -= this.identifierSize + 4;
/* 450 */         localThreadObject = getThreadObjectFromSequence(m);
/* 451 */         localObject = getStackTraceFromSerial(localThreadObject.stackSeq);
/* 452 */         this.snapshot.addRoot(new Root(l1, localThreadObject.threadId, 8, "", (StackTrace)localObject));
/*     */ 
/* 454 */         break;
/*     */       case 5:
/* 457 */         l1 = readID();
/* 458 */         paramLong1 -= this.identifierSize;
/* 459 */         this.snapshot.addRoot(new Root(l1, 0L, 2, ""));
/* 460 */         break;
/*     */       case 6:
/* 463 */         l1 = readID();
/* 464 */         m = this.in.readInt();
/* 465 */         paramLong1 -= this.identifierSize + 4;
/* 466 */         localThreadObject = getThreadObjectFromSequence(m);
/* 467 */         localObject = getStackTraceFromSerial(localThreadObject.stackSeq);
/* 468 */         this.snapshot.addRoot(new Root(l1, localThreadObject.threadId, 5, "", (StackTrace)localObject));
/*     */ 
/* 470 */         break;
/*     */       case 7:
/* 473 */         l1 = readID();
/* 474 */         paramLong1 -= this.identifierSize;
/* 475 */         this.snapshot.addRoot(new Root(l1, 0L, 6, ""));
/* 476 */         break;
/*     */       case 32:
/* 479 */         j = readClass();
/* 480 */         paramLong1 -= j;
/* 481 */         break;
/*     */       case 33:
/* 484 */         j = readInstance();
/* 485 */         paramLong1 -= j;
/* 486 */         break;
/*     */       case 34:
/* 489 */         j = readArray(false);
/* 490 */         paramLong1 -= j;
/* 491 */         break;
/*     */       case 35:
/* 494 */         j = readArray(true);
/* 495 */         paramLong1 -= j;
/* 496 */         break;
/*     */       default:
/* 499 */         throw new IOException("Unrecognized heap dump sub-record type:  " + i);
/*     */       }
/*     */     }
/*     */ 
/* 503 */     if (paramLong1 != 0L) {
/* 504 */       warn("Error reading heap dump or heap dump segment:  Byte count is " + paramLong1 + " instead of 0");
/* 505 */       skipBytes(paramLong1);
/*     */     }
/* 507 */     if (this.debugLevel > 0)
/* 508 */       System.out.println("    Finished heap sub-records.");
/*     */   }
/*     */ 
/*     */   private long readID()
/*     */     throws IOException
/*     */   {
/* 514 */     return this.identifierSize == 4 ? Snapshot.SMALL_ID_MASK & this.in
/* 514 */       .readInt() : this.in.readLong();
/*     */   }
/*     */ 
/*     */   private int readValue(JavaThing[] paramArrayOfJavaThing)
/*     */     throws IOException
/*     */   {
/* 523 */     byte b = this.in.readByte();
/* 524 */     return 1 + readValueForType(b, paramArrayOfJavaThing);
/*     */   }
/*     */ 
/*     */   private int readValueForType(byte paramByte, JavaThing[] paramArrayOfJavaThing) throws IOException
/*     */   {
/* 529 */     if (this.version >= 1) {
/* 530 */       paramByte = signatureFromTypeId(paramByte);
/*     */     }
/* 532 */     return readValueForTypeSignature(paramByte, paramArrayOfJavaThing);
/*     */   }
/*     */ 
/*     */   private int readValueForTypeSignature(byte paramByte, JavaThing[] paramArrayOfJavaThing)
/*     */     throws IOException
/*     */   {
/*     */     byte b;
/*     */     int i;
/* 537 */     switch (paramByte) {
/*     */     case 76:
/*     */     case 91:
/* 540 */       long l1 = readID();
/* 541 */       if (paramArrayOfJavaThing != null) {
/* 542 */         paramArrayOfJavaThing[0] = new JavaObjectRef(l1);
/*     */       }
/* 544 */       return this.identifierSize;
/*     */     case 90:
/* 547 */       b = this.in.readByte();
/* 548 */       if ((b != 0) && (b != 1)) {
/* 549 */         warn("Illegal boolean value read");
/*     */       }
/* 551 */       if (paramArrayOfJavaThing != null) {
/* 552 */         paramArrayOfJavaThing[0] = new JavaBoolean(b != 0);
/*     */       }
/* 554 */       return 1;
/*     */     case 66:
/* 557 */       b = this.in.readByte();
/* 558 */       if (paramArrayOfJavaThing != null) {
/* 559 */         paramArrayOfJavaThing[0] = new JavaByte(b);
/*     */       }
/* 561 */       return 1;
/*     */     case 83:
/* 564 */       i = this.in.readShort();
/* 565 */       if (paramArrayOfJavaThing != null) {
/* 566 */         paramArrayOfJavaThing[0] = new JavaShort(i);
/*     */       }
/* 568 */       return 2;
/*     */     case 67:
/* 571 */       i = this.in.readChar();
/* 572 */       if (paramArrayOfJavaThing != null) {
/* 573 */         paramArrayOfJavaThing[0] = new JavaChar(i);
/*     */       }
/* 575 */       return 2;
/*     */     case 73:
/* 578 */       int j = this.in.readInt();
/* 579 */       if (paramArrayOfJavaThing != null) {
/* 580 */         paramArrayOfJavaThing[0] = new JavaInt(j);
/*     */       }
/* 582 */       return 4;
/*     */     case 74:
/* 585 */       long l2 = this.in.readLong();
/* 586 */       if (paramArrayOfJavaThing != null) {
/* 587 */         paramArrayOfJavaThing[0] = new JavaLong(l2);
/*     */       }
/* 589 */       return 8;
/*     */     case 70:
/* 592 */       float f = this.in.readFloat();
/* 593 */       if (paramArrayOfJavaThing != null) {
/* 594 */         paramArrayOfJavaThing[0] = new JavaFloat(f);
/*     */       }
/* 596 */       return 4;
/*     */     case 68:
/* 599 */       double d = this.in.readDouble();
/* 600 */       if (paramArrayOfJavaThing != null) {
/* 601 */         paramArrayOfJavaThing[0] = new JavaDouble(d);
/*     */       }
/* 603 */       return 8;
/*     */     case 69:
/*     */     case 71:
/*     */     case 72:
/*     */     case 75:
/*     */     case 77:
/*     */     case 78:
/*     */     case 79:
/*     */     case 80:
/*     */     case 81:
/*     */     case 82:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 87:
/*     */     case 88:
/* 606 */     case 89: } throw new IOException("Bad value signature:  " + paramByte);
/*     */   }
/*     */ 
/*     */   private ThreadObject getThreadObjectFromSequence(int paramInt)
/*     */     throws IOException
/*     */   {
/* 613 */     ThreadObject localThreadObject = (ThreadObject)this.threadObjects.get(new Integer(paramInt));
/* 614 */     if (localThreadObject == null) {
/* 615 */       throw new IOException("Thread " + paramInt + " not found for JNI local ref");
/*     */     }
/*     */ 
/* 618 */     return localThreadObject;
/*     */   }
/*     */ 
/*     */   private String getNameFromID(long paramLong) throws IOException {
/* 622 */     return getNameFromID(new Long(paramLong));
/*     */   }
/*     */ 
/*     */   private String getNameFromID(Long paramLong) throws IOException {
/* 626 */     if (paramLong.longValue() == 0L) {
/* 627 */       return "";
/*     */     }
/* 629 */     String str = (String)this.names.get(paramLong);
/* 630 */     if (str == null) {
/* 631 */       warn("Name not found at " + toHex(paramLong.longValue()));
/* 632 */       return "unresolved name " + toHex(paramLong.longValue());
/*     */     }
/* 634 */     return str;
/*     */   }
/*     */ 
/*     */   private StackTrace getStackTraceFromSerial(int paramInt) throws IOException {
/* 638 */     if (this.stackTraces == null) {
/* 639 */       return null;
/*     */     }
/* 641 */     StackTrace localStackTrace = (StackTrace)this.stackTraces.get(new Integer(paramInt));
/* 642 */     if (localStackTrace == null) {
/* 643 */       warn("Stack trace not found for serial # " + paramInt);
/*     */     }
/* 645 */     return localStackTrace;
/*     */   }
/*     */ 
/*     */   private int readClass()
/*     */     throws IOException
/*     */   {
/* 653 */     long l1 = readID();
/* 654 */     StackTrace localStackTrace = getStackTraceFromSerial(this.in.readInt());
/* 655 */     long l2 = readID();
/* 656 */     long l3 = readID();
/* 657 */     long l4 = readID();
/* 658 */     long l5 = readID();
/* 659 */     long l6 = readID();
/* 660 */     long l7 = readID();
/* 661 */     int i = this.in.readInt();
/* 662 */     int j = 7 * this.identifierSize + 8;
/*     */ 
/* 664 */     int k = this.in.readUnsignedShort();
/* 665 */     j += 2;
/* 666 */     for (int m = 0; m < k; m++) {
/* 667 */       int n = this.in.readUnsignedShort();
/* 668 */       j += 2;
/* 669 */       j += readValue(null);
/*     */     }
/*     */ 
/* 672 */     m = this.in.readUnsignedShort();
/* 673 */     j += 2;
/* 674 */     JavaThing[] arrayOfJavaThing = new JavaThing[1];
/* 675 */     JavaStatic[] arrayOfJavaStatic = new JavaStatic[m];
/*     */     Object localObject;
/* 676 */     for (int i1 = 0; i1 < m; i1++) {
/* 677 */       long l8 = readID();
/* 678 */       j += this.identifierSize;
/* 679 */       byte b1 = this.in.readByte();
/* 680 */       j++;
/* 681 */       j += readValueForType(b1, arrayOfJavaThing);
/* 682 */       String str2 = getNameFromID(l8);
/* 683 */       if (this.version >= 1) {
/* 684 */         b1 = signatureFromTypeId(b1);
/*     */       }
/* 686 */       String str3 = "" + (char)b1;
/* 687 */       localObject = new JavaField(str2, str3);
/* 688 */       arrayOfJavaStatic[i1] = new JavaStatic((JavaField)localObject, arrayOfJavaThing[0]);
/*     */     }
/*     */ 
/* 691 */     i1 = this.in.readUnsignedShort();
/* 692 */     j += 2;
/* 693 */     JavaField[] arrayOfJavaField = new JavaField[i1];
/* 694 */     for (int i2 = 0; i2 < i1; i2++) {
/* 695 */       long l9 = readID();
/* 696 */       j += this.identifierSize;
/* 697 */       byte b2 = this.in.readByte();
/* 698 */       j++;
/* 699 */       localObject = getNameFromID(l9);
/* 700 */       if (this.version >= 1) {
/* 701 */         b2 = signatureFromTypeId(b2);
/*     */       }
/* 703 */       String str4 = "" + (char)b2;
/* 704 */       arrayOfJavaField[i2] = new JavaField((String)localObject, str4);
/*     */     }
/* 706 */     String str1 = (String)this.classNameFromObjectID.get(new Long(l1));
/* 707 */     if (str1 == null) {
/* 708 */       warn("Class name not found for " + toHex(l1));
/* 709 */       str1 = "unknown-name@" + toHex(l1);
/*     */     }
/* 711 */     JavaClass localJavaClass = new JavaClass(l1, str1, l2, l3, l4, l5, arrayOfJavaField, arrayOfJavaStatic, i);
/*     */ 
/* 714 */     this.snapshot.addClass(l1, localJavaClass);
/* 715 */     this.snapshot.setSiteTrace(localJavaClass, localStackTrace);
/*     */ 
/* 717 */     return j;
/*     */   }
/*     */ 
/*     */   private String toHex(long paramLong) {
/* 721 */     return Misc.toHex(paramLong);
/*     */   }
/*     */ 
/*     */   private int readInstance()
/*     */     throws IOException
/*     */   {
/* 729 */     long l1 = this.in.position();
/* 730 */     long l2 = readID();
/* 731 */     StackTrace localStackTrace = getStackTraceFromSerial(this.in.readInt());
/* 732 */     long l3 = readID();
/* 733 */     int i = this.in.readInt();
/* 734 */     int j = 2 * this.identifierSize + 8 + i;
/* 735 */     JavaObject localJavaObject = new JavaObject(l3, l1);
/* 736 */     skipBytes(i);
/* 737 */     this.snapshot.addHeapObject(l2, localJavaObject);
/* 738 */     this.snapshot.setSiteTrace(localJavaObject, localStackTrace);
/* 739 */     return j;
/*     */   }
/*     */ 
/*     */   private int readArray(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 747 */     long l1 = this.in.position();
/* 748 */     long l2 = readID();
/* 749 */     StackTrace localStackTrace = getStackTraceFromSerial(this.in.readInt());
/* 750 */     int i = this.in.readInt();
/* 751 */     int j = this.identifierSize + 8;
/*     */     long l3;
/* 753 */     if (paramBoolean) {
/* 754 */       l3 = this.in.readByte();
/* 755 */       j++;
/*     */     } else {
/* 757 */       l3 = readID();
/* 758 */       j += this.identifierSize;
/*     */     }
/*     */ 
/* 762 */     byte b = 0;
/* 763 */     int k = 0;
/* 764 */     if ((paramBoolean) || (this.version < 1)) {
/* 765 */       switch ((int)l3) {
/*     */       case 4:
/* 767 */         b = 90;
/* 768 */         k = 1;
/* 769 */         break;
/*     */       case 5:
/* 772 */         b = 67;
/* 773 */         k = 2;
/* 774 */         break;
/*     */       case 6:
/* 777 */         b = 70;
/* 778 */         k = 4;
/* 779 */         break;
/*     */       case 7:
/* 782 */         b = 68;
/* 783 */         k = 8;
/* 784 */         break;
/*     */       case 8:
/* 787 */         b = 66;
/* 788 */         k = 1;
/* 789 */         break;
/*     */       case 9:
/* 792 */         b = 83;
/* 793 */         k = 2;
/* 794 */         break;
/*     */       case 10:
/* 797 */         b = 73;
/* 798 */         k = 4;
/* 799 */         break;
/*     */       case 11:
/* 802 */         b = 74;
/* 803 */         k = 8;
/*     */       }
/*     */ 
/* 807 */       if ((this.version >= 1) && (b == 0))
/* 808 */         throw new IOException("Unrecognized typecode:  " + l3);
/*     */     }
/*     */     int m;
/*     */     Object localObject;
/* 812 */     if (b != 0) {
/* 813 */       m = k * i;
/* 814 */       j += m;
/* 815 */       localObject = new JavaValueArray(b, l1);
/* 816 */       skipBytes(m);
/* 817 */       this.snapshot.addHeapObject(l2, (JavaHeapObject)localObject);
/* 818 */       this.snapshot.setSiteTrace((JavaHeapObject)localObject, localStackTrace);
/*     */     } else {
/* 820 */       m = i * this.identifierSize;
/* 821 */       j += m;
/* 822 */       localObject = new JavaObjectArray(l3, l1);
/* 823 */       skipBytes(m);
/* 824 */       this.snapshot.addHeapObject(l2, (JavaHeapObject)localObject);
/* 825 */       this.snapshot.setSiteTrace((JavaHeapObject)localObject, localStackTrace);
/*     */     }
/* 827 */     return j;
/*     */   }
/*     */ 
/*     */   private byte signatureFromTypeId(byte paramByte) throws IOException {
/* 831 */     switch (paramByte) {
/*     */     case 2:
/* 833 */       return 76;
/*     */     case 4:
/* 836 */       return 90;
/*     */     case 5:
/* 839 */       return 67;
/*     */     case 6:
/* 842 */       return 70;
/*     */     case 7:
/* 845 */       return 68;
/*     */     case 8:
/* 848 */       return 66;
/*     */     case 9:
/* 851 */       return 83;
/*     */     case 10:
/* 854 */       return 73;
/*     */     case 11:
/* 857 */       return 74;
/*     */     case 3:
/*     */     }
/* 860 */     throw new IOException("Invalid type id of " + paramByte);
/*     */   }
/*     */ 
/*     */   private void handleEOF(EOFException paramEOFException, Snapshot paramSnapshot)
/*     */   {
/* 866 */     if (this.debugLevel > 0) {
/* 867 */       paramEOFException.printStackTrace();
/*     */     }
/* 869 */     warn("Unexpected EOF. Will miss information...");
/*     */ 
/* 871 */     paramSnapshot.setUnresolvedObjectsOK(true);
/*     */   }
/*     */ 
/*     */   private void warn(String paramString) {
/* 875 */     System.out.println("WARNING: " + paramString);
/*     */   }
/*     */ 
/*     */   private class ThreadObject
/*     */   {
/*     */     long threadId;
/*     */     int stackSeq;
/*     */ 
/*     */     ThreadObject(long arg2, int arg4)
/*     */     {
/* 887 */       this.threadId = ???;
/*     */       int i;
/* 888 */       this.stackSeq = i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.parser.HprofReader
 * JD-Core Version:    0.6.2
 */