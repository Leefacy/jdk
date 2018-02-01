/*     */ package sun.jvmstat.perfdata.monitor.v2_0;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import sun.jvmstat.monitor.IntegerMonitor;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorDataException;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStatus;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStructureException;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorTypeException;
/*     */ import sun.jvmstat.perfdata.monitor.PerfDataBufferImpl;
/*     */ import sun.jvmstat.perfdata.monitor.PerfIntegerMonitor;
/*     */ import sun.jvmstat.perfdata.monitor.PerfLongMonitor;
/*     */ import sun.jvmstat.perfdata.monitor.PerfStringConstantMonitor;
/*     */ import sun.jvmstat.perfdata.monitor.PerfStringVariableMonitor;
/*     */ 
/*     */ public class PerfDataBuffer extends PerfDataBufferImpl
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  67 */   private static final int syncWaitMs = Integer.getInteger("sun.jvmstat.perdata.syncWaitMs", 5000)
/*  67 */     .intValue();
/*  68 */   private static final ArrayList EMPTY_LIST = new ArrayList(0);
/*     */   private static final int PERFDATA_ENTRYLENGTH_OFFSET = 0;
/*     */   private static final int PERFDATA_ENTRYLENGTH_SIZE = 4;
/*     */   private static final int PERFDATA_NAMEOFFSET_OFFSET = 4;
/*     */   private static final int PERFDATA_NAMEOFFSET_SIZE = 4;
/*     */   private static final int PERFDATA_VECTORLENGTH_OFFSET = 8;
/*     */   private static final int PERFDATA_VECTORLENGTH_SIZE = 4;
/*     */   private static final int PERFDATA_DATATYPE_OFFSET = 12;
/*     */   private static final int PERFDATA_DATATYPE_SIZE = 1;
/*     */   private static final int PERFDATA_FLAGS_OFFSET = 13;
/*     */   private static final int PERFDATA_FLAGS_SIZE = 1;
/*     */   private static final int PERFDATA_DATAUNITS_OFFSET = 14;
/*     */   private static final int PERFDATA_DATAUNITS_SIZE = 1;
/*     */   private static final int PERFDATA_DATAVAR_OFFSET = 15;
/*     */   private static final int PERFDATA_DATAVAR_SIZE = 1;
/*     */   private static final int PERFDATA_DATAOFFSET_OFFSET = 16;
/*     */   private static final int PERFDATA_DATAOFFSET_SIZE = 4;
/*     */   PerfDataBufferPrologue prologue;
/*     */   int nextEntry;
/*     */   long lastNumEntries;
/*     */   IntegerMonitor overflow;
/*     */   ArrayList<Monitor> insertedMonitors;
/*     */ 
/*     */   public PerfDataBuffer(ByteBuffer paramByteBuffer, int paramInt)
/*     */     throws MonitorException
/*     */   {
/* 112 */     super(paramByteBuffer, paramInt);
/* 113 */     this.prologue = new PerfDataBufferPrologue(paramByteBuffer);
/* 114 */     this.buffer.order(this.prologue.getByteOrder());
/*     */   }
/*     */ 
/*     */   protected void buildMonitorMap(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 121 */     assert (Thread.holdsLock(this));
/*     */ 
/* 124 */     this.buffer.rewind();
/*     */ 
/* 127 */     buildPseudoMonitors(paramMap);
/*     */ 
/* 131 */     synchWithTarget();
/*     */ 
/* 134 */     this.nextEntry = this.prologue.getEntryOffset();
/*     */ 
/* 137 */     int i = this.prologue.getNumEntries();
/*     */ 
/* 140 */     Monitor localMonitor = getNextMonitorEntry();
/* 141 */     while (localMonitor != null) {
/* 142 */       paramMap.put(localMonitor.getName(), localMonitor);
/* 143 */       localMonitor = getNextMonitorEntry();
/*     */     }
/*     */ 
/* 157 */     this.lastNumEntries = i;
/*     */ 
/* 160 */     this.insertedMonitors = new ArrayList(paramMap.values());
/*     */   }
/*     */ 
/*     */   protected void getNewMonitors(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 167 */     assert (Thread.holdsLock(this));
/*     */ 
/* 169 */     int i = this.prologue.getNumEntries();
/*     */ 
/* 171 */     if (i > this.lastNumEntries) {
/* 172 */       this.lastNumEntries = i;
/* 173 */       Monitor localMonitor = getNextMonitorEntry();
/*     */ 
/* 175 */       while (localMonitor != null) {
/* 176 */         String str = localMonitor.getName();
/*     */ 
/* 179 */         if (!paramMap.containsKey(str)) {
/* 180 */           paramMap.put(str, localMonitor);
/* 181 */           if (this.insertedMonitors != null) {
/* 182 */             this.insertedMonitors.add(localMonitor);
/*     */           }
/*     */         }
/* 185 */         localMonitor = getNextMonitorEntry();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected MonitorStatus getMonitorStatus(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 194 */     assert (Thread.holdsLock(this));
/* 195 */     assert (this.insertedMonitors != null);
/*     */ 
/* 198 */     getNewMonitors(paramMap);
/*     */ 
/* 201 */     ArrayList localArrayList1 = EMPTY_LIST;
/* 202 */     ArrayList localArrayList2 = this.insertedMonitors;
/*     */ 
/* 204 */     this.insertedMonitors = new ArrayList();
/* 205 */     return new MonitorStatus(localArrayList2, localArrayList1);
/*     */   }
/*     */ 
/*     */   protected void buildPseudoMonitors(Map<String, Monitor> paramMap)
/*     */   {
/* 212 */     Object localObject = null;
/* 213 */     String str = null;
/* 214 */     IntBuffer localIntBuffer = null;
/*     */ 
/* 216 */     str = "sun.perfdata.majorVersion";
/* 217 */     localIntBuffer = this.prologue.majorVersionBuffer();
/* 218 */     localObject = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, localIntBuffer);
/*     */ 
/* 220 */     paramMap.put(str, localObject);
/*     */ 
/* 222 */     str = "sun.perfdata.minorVersion";
/* 223 */     localIntBuffer = this.prologue.minorVersionBuffer();
/* 224 */     localObject = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, localIntBuffer);
/*     */ 
/* 226 */     paramMap.put(str, localObject);
/*     */ 
/* 228 */     str = "sun.perfdata.size";
/* 229 */     localIntBuffer = this.prologue.sizeBuffer();
/* 230 */     localObject = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, localIntBuffer);
/*     */ 
/* 232 */     paramMap.put(str, localObject);
/*     */ 
/* 234 */     str = "sun.perfdata.used";
/* 235 */     localIntBuffer = this.prologue.usedBuffer();
/* 236 */     localObject = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, localIntBuffer);
/*     */ 
/* 238 */     paramMap.put(str, localObject);
/*     */ 
/* 240 */     str = "sun.perfdata.overflow";
/* 241 */     localIntBuffer = this.prologue.overflowBuffer();
/* 242 */     localObject = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, localIntBuffer);
/*     */ 
/* 244 */     paramMap.put(str, localObject);
/* 245 */     this.overflow = ((IntegerMonitor)localObject);
/*     */ 
/* 247 */     str = "sun.perfdata.timestamp";
/* 248 */     LongBuffer localLongBuffer = this.prologue.modificationTimeStampBuffer();
/* 249 */     localObject = new PerfLongMonitor(str, Units.TICKS, Variability.MONOTONIC, false, localLongBuffer);
/*     */ 
/* 251 */     paramMap.put(str, localObject);
/*     */   }
/*     */ 
/*     */   protected void synchWithTarget()
/*     */     throws MonitorException
/*     */   {
/* 264 */     long l = System.currentTimeMillis() + syncWaitMs;
/*     */ 
/* 267 */     log("synchWithTarget: " + this.lvmid + " ");
/* 268 */     while (!this.prologue.isAccessible())
/*     */     {
/* 270 */       log(".");
/*     */       try
/*     */       {
/* 273 */         Thread.sleep(20L); } catch (InterruptedException localInterruptedException) {
/*     */       }
/* 275 */       if (System.currentTimeMillis() > l) {
/* 276 */         logln("failed: " + this.lvmid);
/* 277 */         throw new MonitorException("Could not synchronize with target");
/*     */       }
/*     */     }
/* 280 */     logln("success: " + this.lvmid);
/*     */   }
/*     */ 
/*     */   protected Monitor getNextMonitorEntry()
/*     */     throws MonitorException
/*     */   {
/* 290 */     Object localObject1 = null;
/*     */ 
/* 293 */     if (this.nextEntry % 4 != 0)
/*     */     {
/* 296 */       throw new MonitorStructureException("Misaligned entry index: " + 
/* 296 */         Integer.toHexString(this.nextEntry));
/*     */     }
/*     */ 
/* 300 */     if ((this.nextEntry < 0) || (this.nextEntry > this.buffer.limit()))
/*     */     {
/* 304 */       throw new MonitorStructureException("Entry index out of bounds: " + 
/* 303 */         Integer.toHexString(this.nextEntry) + 
/* 303 */         ", limit = " + 
/* 304 */         Integer.toHexString(this.buffer
/* 304 */         .limit()));
/*     */     }
/*     */ 
/* 308 */     if (this.nextEntry == this.buffer.limit()) {
/* 309 */       logln("getNextMonitorEntry(): nextEntry == buffer.limit(): returning");
/*     */ 
/* 311 */       return null;
/*     */     }
/*     */ 
/* 314 */     this.buffer.position(this.nextEntry);
/*     */ 
/* 316 */     int i = this.buffer.position();
/* 317 */     int j = this.buffer.getInt();
/*     */ 
/* 320 */     if ((j < 0) || (j > this.buffer.limit()))
/*     */     {
/* 323 */       throw new MonitorStructureException("Invalid entry length: entryLength = " + j + " (0x" + 
/* 323 */         Integer.toHexString(j) + 
/* 323 */         ")");
/*     */     }
/*     */ 
/* 327 */     if (i + j > this.buffer.limit())
/*     */     {
/* 332 */       throw new MonitorStructureException("Entry extends beyond end of buffer:  entryStart = 0x" + 
/* 330 */         Integer.toHexString(i) + 
/* 330 */         " entryLength = 0x" + 
/* 331 */         Integer.toHexString(j) + 
/* 331 */         " buffer limit = 0x" + 
/* 332 */         Integer.toHexString(this.buffer
/* 332 */         .limit()));
/*     */     }
/*     */ 
/* 335 */     if (j == 0)
/*     */     {
/* 337 */       return null;
/*     */     }
/*     */ 
/* 341 */     int k = this.buffer.getInt();
/* 342 */     int m = this.buffer.getInt();
/* 343 */     byte b1 = this.buffer.get();
/* 344 */     byte b2 = this.buffer.get();
/* 345 */     byte b3 = this.buffer.get();
/* 346 */     byte b4 = this.buffer.get();
/* 347 */     int n = this.buffer.getInt();
/*     */ 
/* 349 */     dump_entry_fixed(i, k, m, b1, b2, b3, b4, n);
/*     */ 
/* 353 */     Units localUnits = Units.toUnits(b3);
/* 354 */     Variability localVariability = Variability.toVariability(b4);
/* 355 */     TypeCode localTypeCode = null;
/* 356 */     boolean bool = (b2 & 0x1) != 0;
/*     */     try
/*     */     {
/* 359 */       localTypeCode = TypeCode.toTypeCode(b1);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 365 */       throw new MonitorStructureException("Illegal type code encountered: entry_offset = 0x" + 
/* 364 */         Integer.toHexString(this.nextEntry) + 
/* 364 */         ", type_code = " + 
/* 365 */         Integer.toHexString(b1));
/*     */     }
/*     */ 
/* 369 */     if (k > j)
/*     */     {
/* 373 */       throw new MonitorStructureException("Field extends beyond entry bounds entry_offset = 0x" + 
/* 372 */         Integer.toHexString(this.nextEntry) + 
/* 372 */         ", name_offset = 0x" + 
/* 373 */         Integer.toHexString(k));
/*     */     }
/*     */ 
/* 377 */     if (n > j)
/*     */     {
/* 381 */       throw new MonitorStructureException("Field extends beyond entry bounds: entry_offset = 0x" + 
/* 380 */         Integer.toHexString(this.nextEntry) + 
/* 380 */         ", data_offset = 0x" + 
/* 381 */         Integer.toHexString(n));
/*     */     }
/*     */ 
/* 385 */     if (localVariability == Variability.INVALID)
/*     */     {
/* 389 */       throw new MonitorDataException("Invalid variability attribute: entry_offset = 0x" + 
/* 388 */         Integer.toHexString(this.nextEntry) + 
/* 388 */         ", variability = 0x" + 
/* 389 */         Integer.toHexString(b4));
/*     */     }
/*     */ 
/* 392 */     if (localUnits == Units.INVALID)
/*     */     {
/* 396 */       throw new MonitorDataException("Invalid units attribute: entry_offset = 0x" + 
/* 395 */         Integer.toHexString(this.nextEntry) + 
/* 395 */         ", units = 0x" + 
/* 396 */         Integer.toHexString(b3));
/*     */     }
/*     */ 
/* 407 */     assert (this.buffer.position() == i + k);
/* 408 */     assert (n > k);
/*     */ 
/* 411 */     int i1 = n - k;
/*     */ 
/* 414 */     assert (i1 < j);
/*     */ 
/* 418 */     byte[] arrayOfByte = new byte[i1];
/* 419 */     int i2 = 0;
/*     */     int i3;
/* 421 */     while (((i3 = this.buffer.get()) != 0) && (i2 < i1)) {
/* 422 */       arrayOfByte[(i2++)] = i3;
/*     */     }
/*     */ 
/* 425 */     assert (i2 < i1);
/*     */ 
/* 428 */     assert (this.buffer.position() <= i + n);
/*     */ 
/* 431 */     String str = new String(arrayOfByte, 0, i2);
/*     */ 
/* 437 */     int i4 = j - n;
/*     */ 
/* 440 */     this.buffer.position(i + n);
/*     */ 
/* 442 */     dump_entry_variable(str, this.buffer, i4);
/*     */     Object localObject2;
/* 444 */     if (m == 0)
/*     */     {
/* 446 */       if (localTypeCode == TypeCode.LONG) {
/* 447 */         localObject2 = this.buffer.asLongBuffer();
/* 448 */         ((LongBuffer)localObject2).limit(1);
/* 449 */         localObject1 = new PerfLongMonitor(str, localUnits, localVariability, bool, (LongBuffer)localObject2);
/*     */       }
/*     */       else
/*     */       {
/* 461 */         throw new MonitorTypeException("Unexpected type code encountered: entry_offset = 0x" + 
/* 458 */           Integer.toHexString(this.nextEntry) + 
/* 458 */           ", name = " + str + ", type_code = " + localTypeCode + " (0x" + 
/* 461 */           Integer.toHexString(b1) + 
/* 461 */           ")");
/*     */       }
/*     */ 
/*     */     }
/* 465 */     else if (localTypeCode == TypeCode.BYTE) {
/* 466 */       if (localUnits != Units.STRING)
/*     */       {
/* 476 */         throw new MonitorTypeException("Unexpected vector type encounterd: entry_offset = " + 
/* 471 */           Integer.toHexString(this.nextEntry) + 
/* 471 */           ", name = " + str + ", type_code = " + localTypeCode + " (0x" + 
/* 474 */           Integer.toHexString(b1) + 
/* 474 */           ")" + ", units = " + localUnits + " (0x" + 
/* 476 */           Integer.toHexString(b3) + 
/* 476 */           ")");
/*     */       }
/*     */ 
/* 479 */       localObject2 = this.buffer.slice();
/* 480 */       ((ByteBuffer)localObject2).limit(m);
/*     */ 
/* 482 */       if (localVariability == Variability.CONSTANT) {
/* 483 */         localObject1 = new PerfStringConstantMonitor(str, bool, (ByteBuffer)localObject2);
/*     */       }
/* 485 */       else if (localVariability == Variability.VARIABLE) {
/* 486 */         localObject1 = new PerfStringVariableMonitor(str, bool, (ByteBuffer)localObject2, m - 1);
/*     */       } else {
/* 488 */         if (localVariability == Variability.MONOTONIC)
/*     */         {
/* 496 */           throw new MonitorDataException("Unexpected variability attribute: entry_offset = 0x" + 
/* 493 */             Integer.toHexString(this.nextEntry) + 
/* 493 */             " name = " + str + ", variability = " + localVariability + " (0x" + 
/* 496 */             Integer.toHexString(b4) + 
/* 496 */             ")");
/*     */         }
/*     */ 
/* 499 */         if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 509 */       throw new MonitorTypeException("Unexpected type code encountered: entry_offset = 0x" + 
/* 506 */         Integer.toHexString(this.nextEntry) + 
/* 506 */         ", name = " + str + ", type_code = " + localTypeCode + " (0x" + 
/* 509 */         Integer.toHexString(b1) + 
/* 509 */         ")");
/*     */     }
/*     */ 
/* 514 */     this.nextEntry = (i + j);
/* 515 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void dumpAll(Map<String, Monitor> paramMap, int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void dump_entry_fixed(int paramInt1, int paramInt2, int paramInt3, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void dump_entry_variable(String paramString, ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void logln(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void log(String paramString)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.v2_0.PerfDataBuffer
 * JD-Core Version:    0.6.2
 */