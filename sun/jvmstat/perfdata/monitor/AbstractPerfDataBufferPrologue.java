/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.IntBuffer;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ 
/*     */ public abstract class AbstractPerfDataBufferPrologue
/*     */ {
/*     */   protected ByteBuffer byteBuffer;
/*     */   static final int PERFDATA_PROLOG_OFFSET = 0;
/*     */   static final int PERFDATA_PROLOG_MAGIC_OFFSET = 0;
/*     */   static final int PERFDATA_PROLOG_BYTEORDER_OFFSET = 4;
/*     */   static final int PERFDATA_PROLOG_BYTEORDER_SIZE = 1;
/*     */   static final int PERFDATA_PROLOG_MAJOR_OFFSET = 5;
/*     */   static final int PERFDATA_PROLOG_MAJOR_SIZE = 1;
/*     */   static final int PERFDATA_PROLOG_MINOR_OFFSET = 6;
/*     */   static final int PERFDATA_PROLOG_MINOR_SIZE = 1;
/*     */   static final int PERFDATA_PROLOG_RESERVEDB1_OFFSET = 7;
/*     */   static final int PERFDATA_PROLOG_RESERVEDB1_SIZE = 1;
/*     */   static final int PERFDATA_PROLOG_SIZE = 8;
/*     */   static final byte PERFDATA_BIG_ENDIAN = 0;
/*     */   static final byte PERFDATA_LITTLE_ENDIAN = 1;
/*     */   static final int PERFDATA_MAGIC = -889274176;
/*     */   public static final String PERFDATA_MAJOR_NAME = "sun.perfdata.majorVersion";
/*     */   public static final String PERFDATA_MINOR_NAME = "sun.perfdata.minorVersion";
/*     */ 
/*     */   public AbstractPerfDataBufferPrologue(ByteBuffer paramByteBuffer)
/*     */     throws MonitorException
/*     */   {
/*  95 */     this.byteBuffer = paramByteBuffer.duplicate();
/*     */ 
/*  98 */     if (getMagic() != -889274176)
/*     */     {
/* 100 */       throw new MonitorVersionException("Bad Magic: " + 
/* 100 */         Integer.toHexString(getMagic()));
/*     */     }
/*     */ 
/* 104 */     this.byteBuffer.order(getByteOrder());
/*     */   }
/*     */ 
/*     */   public int getMagic()
/*     */   {
/* 114 */     ByteOrder localByteOrder = this.byteBuffer.order();
/* 115 */     this.byteBuffer.order(ByteOrder.BIG_ENDIAN);
/*     */ 
/* 118 */     this.byteBuffer.position(0);
/* 119 */     int i = this.byteBuffer.getInt();
/*     */ 
/* 122 */     this.byteBuffer.order(localByteOrder);
/* 123 */     return i;
/*     */   }
/*     */ 
/*     */   public ByteOrder getByteOrder()
/*     */   {
/* 133 */     this.byteBuffer.position(4);
/*     */ 
/* 135 */     int i = this.byteBuffer.get();
/*     */ 
/* 137 */     if (i == 0) {
/* 138 */       return ByteOrder.BIG_ENDIAN;
/*     */     }
/* 140 */     return ByteOrder.LITTLE_ENDIAN;
/*     */   }
/*     */ 
/*     */   public int getMajorVersion()
/*     */   {
/* 151 */     this.byteBuffer.position(5);
/* 152 */     return this.byteBuffer.get();
/*     */   }
/*     */ 
/*     */   public int getMinorVersion()
/*     */   {
/* 162 */     this.byteBuffer.position(6);
/* 163 */     return this.byteBuffer.get();
/*     */   }
/*     */ 
/*     */   public abstract boolean isAccessible();
/*     */ 
/*     */   public abstract boolean supportsAccessible();
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 191 */     return 8;
/*     */   }
/*     */ 
/*     */   public IntBuffer majorVersionBuffer()
/*     */   {
/* 202 */     int[] arrayOfInt = new int[1];
/* 203 */     arrayOfInt[0] = getMajorVersion();
/* 204 */     IntBuffer localIntBuffer = IntBuffer.wrap(arrayOfInt);
/* 205 */     localIntBuffer.limit(1);
/* 206 */     return localIntBuffer;
/*     */   }
/*     */ 
/*     */   public IntBuffer minorVersionBuffer()
/*     */   {
/* 217 */     int[] arrayOfInt = new int[1];
/* 218 */     arrayOfInt[0] = getMinorVersion();
/* 219 */     IntBuffer localIntBuffer = IntBuffer.wrap(arrayOfInt);
/* 220 */     localIntBuffer.limit(1);
/* 221 */     return localIntBuffer;
/*     */   }
/*     */ 
/*     */   public static int getMagic(ByteBuffer paramByteBuffer)
/*     */   {
/* 231 */     int i = paramByteBuffer.position();
/* 232 */     ByteOrder localByteOrder = paramByteBuffer.order();
/*     */ 
/* 235 */     paramByteBuffer.order(ByteOrder.BIG_ENDIAN);
/* 236 */     paramByteBuffer.position(0);
/* 237 */     int j = paramByteBuffer.getInt();
/*     */ 
/* 240 */     paramByteBuffer.order(localByteOrder);
/* 241 */     paramByteBuffer.position(i);
/*     */ 
/* 243 */     return j;
/*     */   }
/*     */ 
/*     */   public static int getMajorVersion(ByteBuffer paramByteBuffer)
/*     */   {
/* 253 */     int i = paramByteBuffer.position();
/*     */ 
/* 255 */     paramByteBuffer.position(5);
/* 256 */     int j = paramByteBuffer.get();
/*     */ 
/* 259 */     paramByteBuffer.position(i);
/*     */ 
/* 261 */     return j;
/*     */   }
/*     */ 
/*     */   public static int getMinorVersion(ByteBuffer paramByteBuffer)
/*     */   {
/* 271 */     int i = paramByteBuffer.position();
/*     */ 
/* 273 */     paramByteBuffer.position(6);
/* 274 */     int j = paramByteBuffer.get();
/*     */ 
/* 277 */     paramByteBuffer.position(i);
/*     */ 
/* 279 */     return j;
/*     */   }
/*     */ 
/*     */   public static ByteOrder getByteOrder(ByteBuffer paramByteBuffer)
/*     */   {
/* 289 */     int i = paramByteBuffer.position();
/*     */ 
/* 291 */     paramByteBuffer.position(4);
/* 292 */     ByteOrder localByteOrder = paramByteBuffer.get() == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
/*     */ 
/* 297 */     paramByteBuffer.position(i);
/* 298 */     return localByteOrder;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.AbstractPerfDataBufferPrologue
 * JD-Core Version:    0.6.2
 */