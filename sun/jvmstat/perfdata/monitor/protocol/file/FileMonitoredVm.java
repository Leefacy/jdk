/*    */ package sun.jvmstat.perfdata.monitor.protocol.file;
/*    */ 
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.monitor.event.VmListener;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractMonitoredVm;
/*    */ 
/*    */ public class FileMonitoredVm extends AbstractMonitoredVm
/*    */ {
/*    */   public FileMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt)
/*    */     throws MonitorException
/*    */   {
/* 56 */     super(paramVmIdentifier, paramInt);
/* 57 */     this.pdb = new PerfDataBuffer(paramVmIdentifier);
/*    */   }
/*    */ 
/*    */   public void addVmListener(VmListener paramVmListener)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void removeVmListener(VmListener paramVmListener)
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.file.FileMonitoredVm
 * JD-Core Version:    0.6.2
 */