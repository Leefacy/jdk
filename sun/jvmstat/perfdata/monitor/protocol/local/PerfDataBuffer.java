/*    */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.MappedByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileChannel.MapMode;
/*    */ import java.security.AccessController;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
/*    */ import sun.misc.Perf;
/*    */ import sun.misc.Perf.GetPerfAction;
/*    */ 
/*    */ public class PerfDataBuffer extends AbstractPerfDataBuffer
/*    */ {
/* 51 */   private static final Perf perf = (Perf)AccessController.doPrivileged(new Perf.GetPerfAction());
/*    */ 
/*    */   public PerfDataBuffer(VmIdentifier paramVmIdentifier)
/*    */     throws MonitorException
/*    */   {
/*    */     try
/*    */     {
/* 64 */       ByteBuffer localByteBuffer = perf.attach(paramVmIdentifier.getLocalVmId(), paramVmIdentifier.getMode());
/* 65 */       createPerfDataBuffer(localByteBuffer, paramVmIdentifier.getLocalVmId());
/*    */     }
/*    */     catch (IllegalArgumentException localIllegalArgumentException)
/*    */     {
/*    */       try
/*    */       {
/* 72 */         String str = PerfDataFile.getTempDirectory() + "hsperfdata_" + 
/* 72 */           Integer.toString(paramVmIdentifier
/* 72 */           .getLocalVmId());
/*    */ 
/* 74 */         File localFile = new File(str);
/*    */ 
/* 76 */         FileChannel localFileChannel = new RandomAccessFile(localFile, "r").getChannel();
/* 77 */         MappedByteBuffer localMappedByteBuffer = localFileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, 
/* 78 */           (int)localFileChannel
/* 78 */           .size());
/* 79 */         localFileChannel.close();
/* 80 */         createPerfDataBuffer(localMappedByteBuffer, paramVmIdentifier.getLocalVmId());
/*    */       }
/*    */       catch (FileNotFoundException localFileNotFoundException)
/*    */       {
/* 84 */         throw new MonitorException(paramVmIdentifier.getLocalVmId() + " not found", localIllegalArgumentException);
/*    */       }
/*    */       catch (IOException localIOException2)
/*    */       {
/* 88 */         throw new MonitorException("Could not map 1.4.1 file for " + paramVmIdentifier
/* 88 */           .getLocalVmId(), localIOException2);
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException1) {
/* 92 */       throw new MonitorException("Could not attach to " + paramVmIdentifier
/* 92 */         .getLocalVmId(), localIOException1);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.local.PerfDataBuffer
 * JD-Core Version:    0.6.2
 */