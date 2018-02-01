/*     */ package sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.AgentLoadException;
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachOperationFailedException;
/*     */ import com.sun.tools.attach.spi.AttachProvider;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class WindowsVirtualMachine extends HotSpotVirtualMachine
/*     */ {
/* 193 */   private static byte[] stub = generateStub();
/*     */   private volatile long hProcess;
/*     */ 
/*     */   WindowsVirtualMachine(AttachProvider paramAttachProvider, String paramString)
/*     */     throws AttachNotSupportedException, IOException
/*     */   {
/*  48 */     super(paramAttachProvider, paramString);
/*     */     int i;
/*     */     try
/*     */     {
/*  52 */       i = Integer.parseInt(paramString);
/*     */     } catch (NumberFormatException localNumberFormatException) {
/*  54 */       throw new AttachNotSupportedException("Invalid process identifier");
/*     */     }
/*  56 */     this.hProcess = openProcess(i);
/*     */     try
/*     */     {
/*  62 */       enqueue(this.hProcess, stub, null, null, new Object[0]);
/*     */     } catch (IOException localIOException) {
/*  64 */       throw new AttachNotSupportedException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void detach() throws IOException {
/*  69 */     synchronized (this) {
/*  70 */       if (this.hProcess != -1L) {
/*  71 */         closeProcess(this.hProcess);
/*  72 */         this.hProcess = -1L;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   InputStream execute(String paramString, Object[] paramArrayOfObject)
/*     */     throws AgentLoadException, IOException
/*     */   {
/*  80 */     assert (paramArrayOfObject.length <= 3);
/*     */ 
/*  83 */     int i = new Random().nextInt();
/*  84 */     String str1 = "\\\\.\\pipe\\javatool" + i;
/*  85 */     long l = createPipe(str1);
/*     */ 
/*  89 */     if (this.hProcess == -1L) {
/*  90 */       closePipe(l);
/*  91 */       throw new IOException("Detached from target VM");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  96 */       enqueue(this.hProcess, stub, paramString, str1, paramArrayOfObject);
/*     */ 
/* 100 */       connectPipe(l);
/*     */ 
/* 103 */       PipedInputStream localPipedInputStream = new PipedInputStream(l);
/*     */ 
/* 106 */       int j = readInt(localPipedInputStream);
/* 107 */       if (j != 0)
/*     */       {
/* 109 */         String str2 = readErrorMessage(localPipedInputStream);
/*     */ 
/* 111 */         if (paramString.equals("load")) {
/* 112 */           throw new AgentLoadException("Failed to load agent library");
/*     */         }
/* 114 */         if (str2 == null) {
/* 115 */           throw new AttachOperationFailedException("Command failed in target VM");
/*     */         }
/* 117 */         throw new AttachOperationFailedException(str2);
/*     */       }
/*     */ 
/* 123 */       return localPipedInputStream;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 126 */       closePipe(l);
/* 127 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   static native void init();
/*     */ 
/*     */   static native byte[] generateStub();
/*     */ 
/*     */   static native long openProcess(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static native void closeProcess(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static native long createPipe(String paramString)
/*     */     throws IOException;
/*     */ 
/*     */   static native void closePipe(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static native void connectPipe(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static native int readPipe(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   static native void enqueue(long paramLong, byte[] paramArrayOfByte, String paramString1, String paramString2, Object[] paramArrayOfObject)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/* 191 */     System.loadLibrary("attach");
/* 192 */     init();
/*     */   }
/*     */ 
/*     */   private class PipedInputStream extends InputStream
/*     */   {
/*     */     private long hPipe;
/*     */ 
/*     */     public PipedInputStream(long arg2)
/*     */     {
/*     */       Object localObject;
/* 137 */       this.hPipe = localObject;
/*     */     }
/*     */ 
/*     */     public synchronized int read() throws IOException {
/* 141 */       byte[] arrayOfByte = new byte[1];
/* 142 */       int i = read(arrayOfByte, 0, 1);
/* 143 */       if (i == 1) {
/* 144 */         return arrayOfByte[0] & 0xFF;
/*     */       }
/* 146 */       return -1;
/*     */     }
/*     */ 
/*     */     public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */     {
/* 151 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */       {
/* 153 */         throw new IndexOutOfBoundsException();
/* 154 */       }if (paramInt2 == 0) {
/* 155 */         return 0;
/*     */       }
/* 157 */       return WindowsVirtualMachine.readPipe(this.hPipe, paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 161 */       if (this.hPipe != -1L) {
/* 162 */         WindowsVirtualMachine.closePipe(this.hPipe);
/* 163 */         this.hPipe = -1L;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.attach.WindowsVirtualMachine
 * JD-Core Version:    0.6.2
 */