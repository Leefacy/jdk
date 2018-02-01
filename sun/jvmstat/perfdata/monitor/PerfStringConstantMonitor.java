/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import sun.jvmstat.monitor.Variability;
/*    */ 
/*    */ public class PerfStringConstantMonitor extends PerfStringMonitor
/*    */ {
/*    */   String data;
/*    */ 
/*    */   public PerfStringConstantMonitor(String paramString, boolean paramBoolean, ByteBuffer paramByteBuffer)
/*    */   {
/* 57 */     super(paramString, Variability.CONSTANT, paramBoolean, paramByteBuffer);
/* 58 */     this.data = super.stringValue();
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 65 */     return this.data;
/*    */   }
/*    */ 
/*    */   public String stringValue()
/*    */   {
/* 72 */     return this.data;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.PerfStringConstantMonitor
 * JD-Core Version:    0.6.2
 */