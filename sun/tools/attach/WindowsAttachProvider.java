/*     */ package sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class WindowsAttachProvider extends HotSpotAttachProvider
/*     */ {
/*     */   private static final long FS_PERSISTENT_ACLS = 8L;
/*     */   private static volatile boolean wasTempPathChecked;
/*     */   private static boolean isTempPathSecure;
/*     */ 
/*     */   public WindowsAttachProvider()
/*     */   {
/*  40 */     String str1 = System.getProperty("os.name");
/*  41 */     if ((str1.startsWith("Windows 9")) || (str1.equals("Windows Me"))) {
/*  42 */       throw new RuntimeException("This provider is not supported on this version of Windows");
/*     */     }
/*     */ 
/*  45 */     String str2 = System.getProperty("os.arch");
/*  46 */     if ((!str2.equals("x86")) && (!str2.equals("amd64")))
/*  47 */       throw new RuntimeException("This provider is not supported on this processor architecture");
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/*  53 */     return "sun";
/*     */   }
/*     */ 
/*     */   public String type() {
/*  57 */     return "windows";
/*     */   }
/*     */ 
/*     */   public VirtualMachine attachVirtualMachine(String paramString)
/*     */     throws AttachNotSupportedException, IOException
/*     */   {
/*  63 */     checkAttachPermission();
/*     */ 
/*  67 */     testAttachable(paramString);
/*     */ 
/*  69 */     return new WindowsVirtualMachine(this, paramString);
/*     */   }
/*     */ 
/*     */   public List<VirtualMachineDescriptor> listVirtualMachines()
/*     */   {
/*  75 */     if (isTempPathSecure()) {
/*  76 */       return super.listVirtualMachines();
/*     */     }
/*  78 */     return listJavaProcesses();
/*     */   }
/*     */ 
/*     */   private static boolean isTempPathSecure()
/*     */   {
/*  86 */     if (!wasTempPathChecked) {
/*  87 */       synchronized (WindowsAttachProvider.class) {
/*  88 */         if (!wasTempPathChecked)
/*     */         {
/*  91 */           String str = tempPath();
/*  92 */           if ((str != null) && (str.length() >= 3) && 
/*  93 */             (str
/*  93 */             .charAt(1) == 
/*  93 */             ':') && (str.charAt(2) == '\\'))
/*     */           {
/*  96 */             long l = volumeFlags(str.substring(0, 3));
/*  97 */             isTempPathSecure = (l & 0x8) != 0L;
/*     */           }
/*  99 */           wasTempPathChecked = true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 104 */     return isTempPathSecure;
/*     */   }
/*     */ 
/*     */   private static native String tempPath();
/*     */ 
/*     */   private static native long volumeFlags(String paramString);
/*     */ 
/*     */   private List<VirtualMachineDescriptor> listJavaProcesses()
/*     */   {
/* 129 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 133 */     String str1 = "localhost";
/*     */     try {
/* 135 */       str1 = InetAddress.getLocalHost().getHostName();
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException)
/*     */     {
/*     */     }
/*     */ 
/* 143 */     int[] arrayOfInt = new int[1024];
/* 144 */     int i = enumProcesses(arrayOfInt, arrayOfInt.length);
/* 145 */     for (int j = 0; j < i; j++)
/* 146 */       if (isLibraryLoadedByProcess("jvm.dll", arrayOfInt[j])) {
/* 147 */         String str2 = Integer.toString(arrayOfInt[j]);
/*     */         try {
/* 149 */           new WindowsVirtualMachine(this, str2).detach();
/*     */ 
/* 153 */           String str3 = str2 + "@" + str1;
/*     */ 
/* 155 */           localArrayList.add(new HotSpotAttachProvider.HotSpotVirtualMachineDescriptor(this, str2, str3));
/*     */         }
/*     */         catch (AttachNotSupportedException localAttachNotSupportedException) {
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/*     */       }
/* 162 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static native int enumProcesses(int[] paramArrayOfInt, int paramInt);
/*     */ 
/*     */   private static native boolean isLibraryLoadedByProcess(String paramString, int paramInt);
/*     */ 
/*     */   static
/*     */   {
/* 175 */     System.loadLibrary("attach");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.attach.WindowsAttachProvider
 * JD-Core Version:    0.6.2
 */