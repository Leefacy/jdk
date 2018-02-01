/*    */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.rmi.RemoteException;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.remote.RemoteVm;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
/*    */ 
/*    */ public class PerfDataBuffer extends AbstractPerfDataBuffer
/*    */ {
/*    */   private RemoteVm rvm;
/*    */ 
/*    */   public PerfDataBuffer(RemoteVm paramRemoteVm, int paramInt)
/*    */     throws MonitorException
/*    */   {
/* 61 */     this.rvm = paramRemoteVm;
/*    */     try {
/* 63 */       ByteBuffer localByteBuffer = ByteBuffer.allocate(paramRemoteVm.getCapacity());
/* 64 */       sample(localByteBuffer);
/* 65 */       createPerfDataBuffer(localByteBuffer, paramInt);
/*    */     }
/*    */     catch (RemoteException localRemoteException) {
/* 68 */       throw new MonitorException("Could not read data for remote JVM " + paramInt, localRemoteException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void sample(ByteBuffer paramByteBuffer)
/*    */     throws RemoteException
/*    */   {
/* 85 */     assert (paramByteBuffer != null);
/* 86 */     assert (this.rvm != null);
/* 87 */     synchronized (paramByteBuffer) {
/* 88 */       paramByteBuffer.clear();
/* 89 */       paramByteBuffer.put(this.rvm.getBytes());
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.rmi.PerfDataBuffer
 * JD-Core Version:    0.6.2
 */