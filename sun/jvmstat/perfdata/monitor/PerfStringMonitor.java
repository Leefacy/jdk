/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import sun.jvmstat.monitor.StringMonitor;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ 
/*     */ public class PerfStringMonitor extends PerfByteArrayMonitor
/*     */   implements StringMonitor
/*     */ {
/*  41 */   private static Charset defaultCharset = Charset.defaultCharset();
/*     */ 
/*     */   public PerfStringMonitor(String paramString, Variability paramVariability, boolean paramBoolean, ByteBuffer paramByteBuffer)
/*     */   {
/*  54 */     this(paramString, paramVariability, paramBoolean, paramByteBuffer, paramByteBuffer.limit());
/*     */   }
/*     */ 
/*     */   public PerfStringMonitor(String paramString, Variability paramVariability, boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*  69 */     super(paramString, Units.STRING, paramVariability, paramBoolean, paramByteBuffer, paramInt);
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/*  81 */     return stringValue();
/*     */   }
/*     */ 
/*     */   public String stringValue()
/*     */   {
/*  90 */     String str = "";
/*  91 */     byte[] arrayOfByte = byteArrayValue();
/*     */ 
/*  94 */     if ((arrayOfByte == null) || (arrayOfByte.length <= 1) || (arrayOfByte[0] == 0)) {
/*  95 */       return str;
/*     */     }
/*     */ 
/*  99 */     for (int i = 0; (i < arrayOfByte.length) && (arrayOfByte[i] != 0); i++);
/* 101 */     return new String(arrayOfByte, 0, i, defaultCharset);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.PerfStringMonitor
 * JD-Core Version:    0.6.2
 */