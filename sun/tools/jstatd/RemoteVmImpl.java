/*    */ package sun.tools.jstatd;
/*    */ 
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.monitor.remote.BufferedMonitoredVm;
/*    */ import sun.jvmstat.monitor.remote.RemoteVm;
/*    */ 
/*    */ public class RemoteVmImpl
/*    */   implements RemoteVm
/*    */ {
/*    */   private BufferedMonitoredVm mvm;
/*    */ 
/*    */   RemoteVmImpl(BufferedMonitoredVm paramBufferedMonitoredVm)
/*    */   {
/* 47 */     this.mvm = paramBufferedMonitoredVm;
/*    */   }
/*    */ 
/*    */   public byte[] getBytes() {
/* 51 */     return this.mvm.getBytes();
/*    */   }
/*    */ 
/*    */   public int getCapacity() {
/* 55 */     return this.mvm.getCapacity();
/*    */   }
/*    */ 
/*    */   public void detach() {
/* 59 */     this.mvm.detach();
/*    */   }
/*    */ 
/*    */   public int getLocalVmId() {
/* 63 */     return this.mvm.getVmIdentifier().getLocalVmId();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstatd.RemoteVmImpl
 * JD-Core Version:    0.6.2
 */