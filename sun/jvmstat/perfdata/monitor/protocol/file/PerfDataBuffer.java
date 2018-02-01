/*    */ package sun.jvmstat.perfdata.monitor.protocol.file;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.nio.MappedByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileChannel.MapMode;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
/*    */ 
/*    */ public class PerfDataBuffer extends AbstractPerfDataBuffer
/*    */ {
/*    */   public PerfDataBuffer(VmIdentifier paramVmIdentifier)
/*    */     throws MonitorException
/*    */   {
/* 56 */     File localFile = new File(paramVmIdentifier.getURI());
/* 57 */     String str = paramVmIdentifier.getMode();
/*    */     try
/*    */     {
/* 60 */       FileChannel localFileChannel = new RandomAccessFile(localFile, str).getChannel();
/* 61 */       MappedByteBuffer localMappedByteBuffer = null;
/*    */ 
/* 63 */       if (str.compareTo("r") == 0)
/* 64 */         localMappedByteBuffer = localFileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, (int)localFileChannel.size());
/* 65 */       else if (str.compareTo("rw") == 0)
/* 66 */         localMappedByteBuffer = localFileChannel.map(FileChannel.MapMode.READ_WRITE, 0L, (int)localFileChannel.size());
/*    */       else {
/* 68 */         throw new IllegalArgumentException("Invalid mode: " + str);
/*    */       }
/*    */ 
/* 71 */       localFileChannel.close();
/*    */ 
/* 73 */       createPerfDataBuffer(localMappedByteBuffer, 0);
/*    */     } catch (FileNotFoundException localFileNotFoundException) {
/* 75 */       throw new MonitorException("Could not find " + paramVmIdentifier.toString());
/*    */     } catch (IOException localIOException) {
/* 77 */       throw new MonitorException("Could not read " + paramVmIdentifier.toString());
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.file.PerfDataBuffer
 * JD-Core Version:    0.6.2
 */