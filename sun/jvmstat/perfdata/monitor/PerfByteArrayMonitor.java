/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.jvmstat.monitor.AbstractMonitor;
/*     */ import sun.jvmstat.monitor.ByteArrayMonitor;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ 
/*     */ public class PerfByteArrayMonitor extends AbstractMonitor
/*     */   implements ByteArrayMonitor
/*     */ {
/*     */   ByteBuffer bb;
/*     */ 
/*     */   public PerfByteArrayMonitor(String paramString, Units paramUnits, Variability paramVariability, boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*  64 */     super(paramString, paramUnits, paramVariability, paramBoolean, paramInt);
/*  65 */     this.bb = paramByteBuffer;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/*  78 */     return byteArrayValue();
/*     */   }
/*     */ 
/*     */   public byte[] byteArrayValue()
/*     */   {
/*  88 */     this.bb.position(0);
/*  89 */     byte[] arrayOfByte = new byte[this.bb.limit()];
/*     */ 
/*  92 */     this.bb.get(arrayOfByte);
/*     */ 
/*  94 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte byteAt(int paramInt)
/*     */   {
/* 104 */     this.bb.position(paramInt);
/* 105 */     return this.bb.get();
/*     */   }
/*     */ 
/*     */   public int getMaximumLength()
/*     */   {
/* 114 */     return this.bb.limit();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.PerfByteArrayMonitor
 * JD-Core Version:    0.6.2
 */