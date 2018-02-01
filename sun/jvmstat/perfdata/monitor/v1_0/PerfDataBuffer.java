/*     */ package sun.jvmstat.perfdata.monitor.v1_0;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import sun.jvmstat.monitor.IntegerMonitor;
/*     */ import sun.jvmstat.monitor.LongMonitor;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.StringMonitor;
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
/*  49 */   private static final int syncWaitMs = Integer.getInteger("sun.jvmstat.perdata.syncWaitMs", 5000)
/*  49 */     .intValue();
/*  50 */   private static final ArrayList EMPTY_LIST = new ArrayList(0);
/*     */   private static final int PERFDATA_ENTRYLENGTH_OFFSET = 0;
/*     */   private static final int PERFDATA_ENTRYLENGTH_SIZE = 4;
/*     */   private static final int PERFDATA_NAMELENGTH_OFFSET = 4;
/*     */   private static final int PERFDATA_NAMELENGTH_SIZE = 4;
/*     */   private static final int PERFDATA_VECTORLENGTH_OFFSET = 8;
/*     */   private static final int PERFDATA_VECTORLENGTH_SIZE = 4;
/*     */   private static final int PERFDATA_DATATYPE_OFFSET = 12;
/*     */   private static final int PERFDATA_DATATYPE_SIZE = 1;
/*     */   private static final int PERFDATA_FLAGS_OFFSET = 13;
/*     */   private static final int PERFDATA_FLAGS_SIZE = 1;
/*     */   private static final int PERFDATA_DATAUNITS_OFFSET = 14;
/*     */   private static final int PERFDATA_DATAUNITS_SIZE = 1;
/*     */   private static final int PERFDATA_DATAATTR_OFFSET = 15;
/*     */   private static final int PERFDATA_DATAATTR_SIZE = 1;
/*     */   private static final int PERFDATA_NAME_OFFSET = 16;
/*     */   PerfDataBufferPrologue prologue;
/*     */   int nextEntry;
/*     */   int pollForEntry;
/*     */   int perfDataItem;
/*     */   long lastModificationTime;
/*     */   int lastUsed;
/*     */   IntegerMonitor overflow;
/*     */   ArrayList<Monitor> insertedMonitors;
/*     */ 
/*     */   public PerfDataBuffer(ByteBuffer paramByteBuffer, int paramInt)
/*     */     throws MonitorException
/*     */   {
/*  94 */     super(paramByteBuffer, paramInt);
/*  95 */     this.prologue = new PerfDataBufferPrologue(paramByteBuffer);
/*  96 */     this.buffer.order(this.prologue.getByteOrder());
/*     */   }
/*     */ 
/*     */   protected void buildMonitorMap(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 103 */     assert (Thread.holdsLock(this));
/*     */ 
/* 106 */     this.buffer.rewind();
/*     */ 
/* 109 */     buildPseudoMonitors(paramMap);
/*     */ 
/* 112 */     this.buffer.position(this.prologue.getSize());
/* 113 */     this.nextEntry = this.buffer.position();
/* 114 */     this.perfDataItem = 0;
/*     */ 
/* 116 */     int i = this.prologue.getUsed();
/* 117 */     long l = this.prologue.getModificationTimeStamp();
/*     */ 
/* 119 */     Monitor localMonitor = getNextMonitorEntry();
/* 120 */     while (localMonitor != null) {
/* 121 */       paramMap.put(localMonitor.getName(), localMonitor);
/* 122 */       localMonitor = getNextMonitorEntry();
/*     */     }
/*     */ 
/* 133 */     this.lastUsed = i;
/* 134 */     this.lastModificationTime = l;
/*     */ 
/* 137 */     synchWithTarget(paramMap);
/*     */ 
/* 140 */     kludge(paramMap);
/*     */ 
/* 142 */     this.insertedMonitors = new ArrayList(paramMap.values());
/*     */   }
/*     */ 
/*     */   protected void getNewMonitors(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 149 */     assert (Thread.holdsLock(this));
/*     */ 
/* 151 */     int i = this.prologue.getUsed();
/* 152 */     long l = this.prologue.getModificationTimeStamp();
/*     */ 
/* 154 */     if ((i > this.lastUsed) || (this.lastModificationTime > l))
/*     */     {
/* 156 */       this.lastUsed = i;
/* 157 */       this.lastModificationTime = l;
/*     */ 
/* 159 */       Monitor localMonitor = getNextMonitorEntry();
/* 160 */       while (localMonitor != null) {
/* 161 */         String str = localMonitor.getName();
/*     */ 
/* 164 */         if (!paramMap.containsKey(str)) {
/* 165 */           paramMap.put(str, localMonitor);
/*     */ 
/* 173 */           if (this.insertedMonitors != null) {
/* 174 */             this.insertedMonitors.add(localMonitor);
/*     */           }
/*     */         }
/* 177 */         localMonitor = getNextMonitorEntry();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected MonitorStatus getMonitorStatus(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 186 */     assert (Thread.holdsLock(this));
/* 187 */     assert (this.insertedMonitors != null);
/*     */ 
/* 190 */     getNewMonitors(paramMap);
/*     */ 
/* 193 */     ArrayList localArrayList1 = EMPTY_LIST;
/* 194 */     ArrayList localArrayList2 = this.insertedMonitors;
/*     */ 
/* 196 */     this.insertedMonitors = new ArrayList();
/* 197 */     return new MonitorStatus(localArrayList2, localArrayList1);
/*     */   }
/*     */ 
/*     */   protected void buildPseudoMonitors(Map<String, Monitor> paramMap)
/*     */   {
/* 204 */     Object localObject = null;
/* 205 */     String str = null;
/* 206 */     IntBuffer localIntBuffer = null;
/*     */ 
/* 208 */     str = "sun.perfdata.majorVersion";
/* 209 */     localIntBuffer = this.prologue.majorVersionBuffer();
/* 210 */     localObject = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, localIntBuffer);
/*     */ 
/* 212 */     paramMap.put(str, localObject);
/*     */ 
/* 214 */     str = "sun.perfdata.minorVersion";
/* 215 */     localIntBuffer = this.prologue.minorVersionBuffer();
/* 216 */     localObject = new PerfIntegerMonitor(str, Units.NONE, Variability.CONSTANT, false, localIntBuffer);
/*     */ 
/* 218 */     paramMap.put(str, localObject);
/*     */ 
/* 220 */     str = "sun.perfdata.size";
/* 221 */     localIntBuffer = this.prologue.sizeBuffer();
/* 222 */     localObject = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, localIntBuffer);
/*     */ 
/* 224 */     paramMap.put(str, localObject);
/*     */ 
/* 226 */     str = "sun.perfdata.used";
/* 227 */     localIntBuffer = this.prologue.usedBuffer();
/* 228 */     localObject = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, localIntBuffer);
/*     */ 
/* 230 */     paramMap.put(str, localObject);
/*     */ 
/* 232 */     str = "sun.perfdata.overflow";
/* 233 */     localIntBuffer = this.prologue.overflowBuffer();
/* 234 */     localObject = new PerfIntegerMonitor(str, Units.BYTES, Variability.MONOTONIC, false, localIntBuffer);
/*     */ 
/* 236 */     paramMap.put(str, localObject);
/* 237 */     this.overflow = ((IntegerMonitor)localObject);
/*     */ 
/* 239 */     str = "sun.perfdata.timestamp";
/* 240 */     LongBuffer localLongBuffer = this.prologue.modificationTimeStampBuffer();
/* 241 */     localObject = new PerfLongMonitor(str, Units.TICKS, Variability.MONOTONIC, false, localLongBuffer);
/*     */ 
/* 243 */     paramMap.put(str, localObject);
/*     */   }
/*     */ 
/*     */   protected void synchWithTarget(Map<String, Monitor> paramMap)
/*     */     throws MonitorException
/*     */   {
/* 262 */     long l = System.currentTimeMillis() + syncWaitMs;
/*     */ 
/* 264 */     String str = "hotspot.rt.hrt.ticks";
/* 265 */     LongMonitor localLongMonitor = (LongMonitor)pollFor(paramMap, str, l);
/*     */ 
/* 271 */     log("synchWithTarget: " + this.lvmid + " ");
/* 272 */     while (localLongMonitor.longValue() == 0L) {
/* 273 */       log(".");
/*     */       try {
/* 275 */         Thread.sleep(20L); } catch (InterruptedException localInterruptedException) {
/*     */       }
/* 277 */       if (System.currentTimeMillis() > l) {
/* 278 */         lognl("failed: " + this.lvmid);
/* 279 */         throw new MonitorException("Could Not Synchronize with target");
/*     */       }
/*     */     }
/* 282 */     lognl("success: " + this.lvmid);
/*     */   }
/*     */ 
/*     */   protected Monitor pollFor(Map<String, Monitor> paramMap, String paramString, long paramLong)
/*     */     throws MonitorException
/*     */   {
/* 292 */     Monitor localMonitor = null;
/*     */ 
/* 294 */     log("polling for: " + this.lvmid + "," + paramString + " ");
/*     */ 
/* 296 */     this.pollForEntry = this.nextEntry;
/* 297 */     while ((localMonitor = (Monitor)paramMap.get(paramString)) == null) {
/* 298 */       log(".");
/*     */       try {
/* 300 */         Thread.sleep(20L); } catch (InterruptedException localInterruptedException) {
/*     */       }
/* 302 */       long l = System.currentTimeMillis();
/* 303 */       if ((l > paramLong) || (this.overflow.intValue() > 0)) {
/* 304 */         lognl("failed: " + this.lvmid + "," + paramString);
/* 305 */         dumpAll(paramMap, this.lvmid);
/* 306 */         throw new MonitorException("Could not find expected counter");
/*     */       }
/*     */ 
/* 309 */       getNewMonitors(paramMap);
/*     */     }
/* 311 */     lognl("success: " + this.lvmid + "," + paramString);
/* 312 */     return localMonitor;
/*     */   }
/*     */ 
/*     */   protected void kludge(Map<String, Monitor> paramMap)
/*     */   {
/* 321 */     if (Boolean.getBoolean("sun.jvmstat.perfdata.disableKludge"))
/*     */     {
/* 323 */       return;
/*     */     }
/*     */ 
/* 326 */     String str = "java.vm.version";
/* 327 */     StringMonitor localStringMonitor1 = (StringMonitor)paramMap.get(str);
/* 328 */     if (localStringMonitor1 == null) {
/* 329 */       localStringMonitor1 = (StringMonitor)findByAlias(str);
/*     */     }
/*     */ 
/* 332 */     str = "java.vm.name";
/* 333 */     StringMonitor localStringMonitor2 = (StringMonitor)paramMap.get(str);
/* 334 */     if (localStringMonitor2 == null) {
/* 335 */       localStringMonitor2 = (StringMonitor)findByAlias(str);
/*     */     }
/*     */ 
/* 338 */     str = "hotspot.vm.args";
/* 339 */     StringMonitor localStringMonitor3 = (StringMonitor)paramMap.get(str);
/* 340 */     if (localStringMonitor3 == null) {
/* 341 */       localStringMonitor3 = (StringMonitor)findByAlias(str);
/*     */     }
/*     */ 
/* 344 */     assert ((localStringMonitor2 != null) && (localStringMonitor1 != null) && (localStringMonitor3 != null));
/*     */ 
/* 346 */     if ((localStringMonitor2.stringValue().indexOf("HotSpot") >= 0) && 
/* 347 */       (localStringMonitor1.stringValue().startsWith("1.4.2")))
/* 348 */       kludgeMantis(paramMap, localStringMonitor3);
/*     */   }
/*     */ 
/*     */   private void kludgeMantis(Map<String, Monitor> paramMap, StringMonitor paramStringMonitor)
/*     */   {
/* 370 */     String str1 = "hotspot.gc.collector.0.name";
/* 371 */     StringMonitor localStringMonitor1 = (StringMonitor)paramMap.get(str1);
/*     */ 
/* 373 */     if (localStringMonitor1.stringValue().compareTo("PSScavenge") == 0) {
/* 374 */       int i = 1;
/*     */ 
/* 381 */       str1 = "hotspot.vm.flags";
/* 382 */       StringMonitor localStringMonitor2 = (StringMonitor)paramMap.get(str1);
/* 383 */       String str2 = localStringMonitor2.stringValue() + " " + paramStringMonitor.stringValue();
/*     */ 
/* 390 */       int j = str2.lastIndexOf("+AggressiveHeap");
/* 391 */       int k = str2.lastIndexOf("-UseAdaptiveSizePolicy");
/*     */ 
/* 393 */       if (j != -1)
/*     */       {
/* 399 */         if ((k != -1) && (k > j)) {
/* 400 */           i = 0;
/*     */         }
/*     */ 
/*     */       }
/* 410 */       else if (k != -1) {
/* 411 */         i = 0;
/*     */       }
/*     */ 
/* 415 */       if (i != 0)
/*     */       {
/* 419 */         String str3 = "hotspot.gc.generation.0.space.0.size";
/* 420 */         String str4 = "hotspot.gc.generation.0.space.1.size";
/* 421 */         String str5 = "hotspot.gc.generation.0.space.2.size";
/* 422 */         paramMap.remove(str3);
/* 423 */         paramMap.remove(str4);
/* 424 */         paramMap.remove(str5);
/*     */ 
/* 427 */         String str6 = "hotspot.gc.generation.0.capacity.max";
/* 428 */         LongMonitor localLongMonitor = (LongMonitor)paramMap.get(str6);
/*     */ 
/* 441 */         PerfLongMonitor localPerfLongMonitor = null;
/*     */ 
/* 443 */         LongBuffer localLongBuffer = LongBuffer.allocate(1);
/* 444 */         localLongBuffer.put(localLongMonitor.longValue());
/* 445 */         localPerfLongMonitor = new PerfLongMonitor(str3, Units.BYTES, Variability.CONSTANT, false, localLongBuffer);
/*     */ 
/* 447 */         paramMap.put(str3, localPerfLongMonitor);
/*     */ 
/* 449 */         localPerfLongMonitor = new PerfLongMonitor(str4, Units.BYTES, Variability.CONSTANT, false, localLongBuffer);
/*     */ 
/* 451 */         paramMap.put(str4, localPerfLongMonitor);
/*     */ 
/* 453 */         localPerfLongMonitor = new PerfLongMonitor(str5, Units.BYTES, Variability.CONSTANT, false, localLongBuffer);
/*     */ 
/* 455 */         paramMap.put(str5, localPerfLongMonitor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Monitor getNextMonitorEntry()
/*     */     throws MonitorException
/*     */   {
/* 467 */     Object localObject1 = null;
/*     */ 
/* 470 */     if (this.nextEntry % 4 != 0) {
/* 471 */       throw new MonitorStructureException("Entry index not properly aligned: " + this.nextEntry);
/*     */     }
/*     */ 
/* 476 */     if ((this.nextEntry < 0) || (this.nextEntry > this.buffer.limit()))
/*     */     {
/* 479 */       throw new MonitorStructureException("Entry index out of bounds: nextEntry = " + this.nextEntry + ", limit = " + this.buffer
/* 479 */         .limit());
/*     */     }
/*     */ 
/* 483 */     if (this.nextEntry == this.buffer.limit()) {
/* 484 */       lognl("getNextMonitorEntry(): nextEntry == buffer.limit(): returning");
/*     */ 
/* 486 */       return null;
/*     */     }
/*     */ 
/* 489 */     this.buffer.position(this.nextEntry);
/*     */ 
/* 491 */     int i = this.buffer.position();
/* 492 */     int j = this.buffer.getInt();
/*     */ 
/* 495 */     if ((j < 0) || (j > this.buffer.limit())) {
/* 496 */       throw new MonitorStructureException("Invalid entry length: entryLength = " + j);
/*     */     }
/*     */ 
/* 501 */     if (i + j > this.buffer.limit())
/*     */     {
/* 506 */       throw new MonitorStructureException("Entry extends beyond end of buffer:  entryStart = " + i + " entryLength = " + j + " buffer limit = " + this.buffer
/* 506 */         .limit());
/*     */     }
/*     */ 
/* 509 */     if (j == 0)
/*     */     {
/* 511 */       return null;
/*     */     }
/*     */ 
/* 514 */     int k = this.buffer.getInt();
/* 515 */     int m = this.buffer.getInt();
/* 516 */     int n = this.buffer.get();
/* 517 */     int i1 = this.buffer.get();
/* 518 */     Units localUnits = Units.toUnits(this.buffer.get());
/* 519 */     Variability localVariability = Variability.toVariability(this.buffer.get());
/* 520 */     boolean bool = (i1 & 0x1) != 0;
/*     */ 
/* 523 */     if ((k <= 0) || (k > j)) {
/* 524 */       throw new MonitorStructureException("Invalid Monitor name length: " + k);
/*     */     }
/*     */ 
/* 528 */     if ((m < 0) || (m > j)) {
/* 529 */       throw new MonitorStructureException("Invalid Monitor vector length: " + m);
/*     */     }
/*     */ 
/* 536 */     byte[] arrayOfByte = new byte[k - 1];
/* 537 */     for (int i2 = 0; i2 < k - 1; i2++) {
/* 538 */       arrayOfByte[i2] = this.buffer.get();
/*     */     }
/*     */ 
/* 542 */     String str = new String(arrayOfByte, 0, k - 1);
/*     */ 
/* 544 */     if (localVariability == Variability.INVALID) {
/* 545 */       throw new MonitorDataException("Invalid variability attribute: entry index = " + this.perfDataItem + " name = " + str);
/*     */     }
/*     */ 
/* 549 */     if (localUnits == Units.INVALID)
/* 550 */       throw new MonitorDataException("Invalid units attribute:  entry index = " + this.perfDataItem + " name = " + str);
/*     */     int i3;
/*     */     Object localObject2;
/* 556 */     if (m == 0)
/*     */     {
/* 558 */       if (n == BasicType.LONG.intValue()) {
/* 559 */         i3 = i + j - 8;
/* 560 */         this.buffer.position(i3);
/* 561 */         localObject2 = this.buffer.asLongBuffer();
/* 562 */         ((LongBuffer)localObject2).limit(1);
/* 563 */         localObject1 = new PerfLongMonitor(str, localUnits, localVariability, bool, (LongBuffer)localObject2);
/* 564 */         this.perfDataItem += 1;
/*     */       }
/*     */       else {
/* 567 */         throw new MonitorTypeException("Invalid Monitor type: entry index = " + this.perfDataItem + " name = " + str + " type = " + n);
/*     */       }
/*     */ 
/*     */     }
/* 574 */     else if (n == BasicType.BYTE.intValue()) {
/* 575 */       if (localUnits != Units.STRING)
/*     */       {
/* 577 */         throw new MonitorTypeException("Invalid Monitor type: entry index = " + this.perfDataItem + " name = " + str + " type = " + n);
/*     */       }
/*     */ 
/* 583 */       i3 = i + 16 + k;
/* 584 */       this.buffer.position(i3);
/* 585 */       localObject2 = this.buffer.slice();
/* 586 */       ((ByteBuffer)localObject2).limit(m);
/* 587 */       ((ByteBuffer)localObject2).position(0);
/*     */ 
/* 589 */       if (localVariability == Variability.CONSTANT) {
/* 590 */         localObject1 = new PerfStringConstantMonitor(str, bool, (ByteBuffer)localObject2);
/*     */       }
/* 592 */       else if (localVariability == Variability.VARIABLE) {
/* 593 */         localObject1 = new PerfStringVariableMonitor(str, bool, (ByteBuffer)localObject2, m - 1);
/*     */       }
/*     */       else
/*     */       {
/* 597 */         throw new MonitorDataException("Invalid variability attribute: entry index = " + this.perfDataItem + " name = " + str + " variability = " + localVariability);
/*     */       }
/*     */ 
/* 603 */       this.perfDataItem += 1;
/*     */     }
/*     */     else {
/* 606 */       throw new MonitorTypeException("Invalid Monitor type: entry index = " + this.perfDataItem + " name = " + str + " type = " + n);
/*     */     }
/*     */ 
/* 614 */     this.nextEntry = (i + j);
/* 615 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void dumpAll(Map paramMap, int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void lognl(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void log(String paramString)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.v1_0.PerfDataBuffer
 * JD-Core Version:    0.6.2
 */