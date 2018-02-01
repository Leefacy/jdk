/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import sun.jvmstat.monitor.Variability;
/*    */ 
/*    */ public class PerfStringVariableMonitor extends PerfStringMonitor
/*    */ {
/*    */   public PerfStringVariableMonitor(String paramString, boolean paramBoolean, ByteBuffer paramByteBuffer)
/*    */   {
/* 51 */     this(paramString, paramBoolean, paramByteBuffer, paramByteBuffer.limit());
/*    */   }
/*    */ 
/*    */   public PerfStringVariableMonitor(String paramString, boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt)
/*    */   {
/* 66 */     super(paramString, Variability.VARIABLE, paramBoolean, paramByteBuffer, paramInt + 1);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.PerfStringVariableMonitor
 * JD-Core Version:    0.6.2
 */