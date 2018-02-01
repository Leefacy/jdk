/*     */ package sun.jvmstat.perfdata.monitor.v2_0;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBufferPrologue;
/*     */ 
/*     */ public class PerfDataBufferPrologue extends AbstractPerfDataBufferPrologue
/*     */ {
/*     */   private static final int SUPPORTED_MAJOR_VERSION = 2;
/*     */   private static final int SUPPORTED_MINOR_VERSION = 0;
/*     */   static final int PERFDATA_PROLOG_ACCESSIBLE_OFFSET = 7;
/*     */   static final int PERFDATA_PROLOG_ACCESSIBLE_SIZE = 1;
/*     */   static final int PERFDATA_PROLOG_USED_OFFSET = 8;
/*     */   static final int PERFDATA_PROLOG_USED_SIZE = 4;
/*     */   static final int PERFDATA_PROLOG_OVERFLOW_OFFSET = 12;
/*     */   static final int PERFDATA_PROLOG_OVERFLOW_SIZE = 4;
/*     */   static final int PERFDATA_PROLOG_MODTIMESTAMP_OFFSET = 16;
/*     */   static final int PERFDATA_PROLOG_MODTIMESTAMP_SIZE = 8;
/*     */   static final int PERFDATA_PROLOG_ENTRYOFFSET_OFFSET = 24;
/*     */   static final int PERFDATA_PROLOG_ENTRYOFFSET_SIZE = 4;
/*     */   static final int PERFDATA_PROLOG_NUMENTRIES_OFFSET = 28;
/*     */   static final int PERFDATA_PROLOG_NUMENTRIES_SIZE = 4;
/*     */   static final int PERFDATA_PROLOG_SIZE = 32;
/*     */   static final String PERFDATA_BUFFER_SIZE_NAME = "sun.perfdata.size";
/*     */   static final String PERFDATA_BUFFER_USED_NAME = "sun.perfdata.used";
/*     */   static final String PERFDATA_OVERFLOW_NAME = "sun.perfdata.overflow";
/*     */   static final String PERFDATA_MODTIMESTAMP_NAME = "sun.perfdata.timestamp";
/*     */   static final String PERFDATA_NUMENTRIES_NAME = "sun.perfdata.entries";
/*     */ 
/*     */   public PerfDataBufferPrologue(ByteBuffer paramByteBuffer)
/*     */     throws MonitorException
/*     */   {
/*  95 */     super(paramByteBuffer);
/*  96 */     assert ((getMajorVersion() == 2) && (getMinorVersion() == 0));
/*     */   }
/*     */ 
/*     */   public boolean supportsAccessible()
/*     */   {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isAccessible()
/*     */   {
/* 110 */     assert (supportsAccessible());
/* 111 */     this.byteBuffer.position(7);
/* 112 */     int i = this.byteBuffer.get();
/* 113 */     return i != 0;
/*     */   }
/*     */ 
/*     */   public int getUsed()
/*     */   {
/* 122 */     this.byteBuffer.position(8);
/* 123 */     return this.byteBuffer.getInt();
/*     */   }
/*     */ 
/*     */   public int getBufferSize()
/*     */   {
/* 132 */     return this.byteBuffer.capacity();
/*     */   }
/*     */ 
/*     */   public int getOverflow()
/*     */   {
/* 144 */     this.byteBuffer.position(12);
/* 145 */     return this.byteBuffer.getInt();
/*     */   }
/*     */ 
/*     */   public long getModificationTimeStamp()
/*     */   {
/* 157 */     this.byteBuffer.position(16);
/* 158 */     return this.byteBuffer.getLong();
/*     */   }
/*     */ 
/*     */   public int getEntryOffset()
/*     */   {
/* 165 */     this.byteBuffer.position(24);
/* 166 */     return this.byteBuffer.getInt();
/*     */   }
/*     */ 
/*     */   public int getNumEntries()
/*     */   {
/* 173 */     this.byteBuffer.position(28);
/* 174 */     return this.byteBuffer.getInt();
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 181 */     return 32;
/*     */   }
/*     */ 
/*     */   IntBuffer usedBuffer()
/*     */   {
/* 193 */     this.byteBuffer.position(8);
/* 194 */     IntBuffer localIntBuffer = this.byteBuffer.asIntBuffer();
/* 195 */     localIntBuffer.limit(1);
/* 196 */     return localIntBuffer;
/*     */   }
/*     */ 
/*     */   IntBuffer sizeBuffer()
/*     */   {
/* 208 */     IntBuffer localIntBuffer = IntBuffer.allocate(1);
/* 209 */     localIntBuffer.put(this.byteBuffer.capacity());
/* 210 */     return localIntBuffer;
/*     */   }
/*     */ 
/*     */   IntBuffer overflowBuffer()
/*     */   {
/* 222 */     this.byteBuffer.position(12);
/* 223 */     IntBuffer localIntBuffer = this.byteBuffer.asIntBuffer();
/* 224 */     localIntBuffer.limit(1);
/* 225 */     return localIntBuffer;
/*     */   }
/*     */ 
/*     */   LongBuffer modificationTimeStampBuffer()
/*     */   {
/* 237 */     this.byteBuffer.position(16);
/* 238 */     LongBuffer localLongBuffer = this.byteBuffer.asLongBuffer();
/* 239 */     localLongBuffer.limit(1);
/* 240 */     return localLongBuffer;
/*     */   }
/*     */ 
/*     */   IntBuffer numEntriesBuffer()
/*     */   {
/* 252 */     this.byteBuffer.position(28);
/* 253 */     IntBuffer localIntBuffer = this.byteBuffer.asIntBuffer();
/* 254 */     localIntBuffer.limit(1);
/* 255 */     return localIntBuffer;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.v2_0.PerfDataBufferPrologue
 * JD-Core Version:    0.6.2
 */