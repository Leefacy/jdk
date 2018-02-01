/*     */ package sun.tools.jstack;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
/*     */ 
/*     */ public class JStack
/*     */ {
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/*  44 */     if (paramArrayOfString.length == 0) {
/*  45 */       usage(1);
/*     */     }
/*     */ 
/*  48 */     int i = 0;
/*  49 */     boolean bool1 = false;
/*  50 */     boolean bool2 = false;
/*     */ 
/*  53 */     int j = 0;
/*  54 */     while (j < paramArrayOfString.length) {
/*  55 */       String str = paramArrayOfString[j];
/*  56 */       if (!str.startsWith("-")) {
/*     */         break;
/*     */       }
/*  59 */       if ((str.equals("-help")) || (str.equals("-h"))) {
/*  60 */         usage(0);
/*     */       }
/*  62 */       else if (str.equals("-F")) {
/*  63 */         i = 1;
/*     */       }
/*  66 */       else if (str.equals("-m")) {
/*  67 */         bool1 = true;
/*     */       }
/*  69 */       else if (str.equals("-l"))
/*  70 */         bool2 = true;
/*     */       else {
/*  72 */         usage(1);
/*     */       }
/*     */ 
/*  76 */       j++;
/*     */     }
/*     */ 
/*  80 */     if (bool1) {
/*  81 */       i = 1;
/*     */     }
/*     */ 
/*  86 */     int k = paramArrayOfString.length - j;
/*  87 */     if ((k == 0) || (k > 2)) {
/*  88 */       usage(1);
/*     */     }
/*  90 */     if (k == 2) {
/*  91 */       i = 1;
/*     */     }
/*  94 */     else if (!paramArrayOfString[j].matches("[0-9]+"))
/*  95 */       i = 1;
/*     */     Object localObject;
/* 100 */     if (i != 0)
/*     */     {
/* 102 */       localObject = new String[k];
/* 103 */       for (int m = j; m < paramArrayOfString.length; m++) {
/* 104 */         localObject[(m - j)] = paramArrayOfString[m];
/*     */       }
/* 106 */       runJStackTool(bool1, bool2, (String[])localObject);
/*     */     }
/*     */     else {
/* 109 */       localObject = paramArrayOfString[j];
/*     */       String[] arrayOfString;
/* 111 */       if (bool2)
/* 112 */         arrayOfString = new String[] { "-l" };
/*     */       else {
/* 114 */         arrayOfString = new String[0];
/*     */       }
/* 116 */       runThreadDump((String)localObject, arrayOfString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void runJStackTool(boolean paramBoolean1, boolean paramBoolean2, String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/* 123 */     Class localClass = loadSAClass();
/* 124 */     if (localClass == null) {
/* 125 */       usage(1);
/*     */     }
/*     */ 
/* 129 */     if (paramBoolean1) {
/* 130 */       paramArrayOfString = prepend("-m", paramArrayOfString);
/*     */     }
/* 132 */     if (paramBoolean2) {
/* 133 */       paramArrayOfString = prepend("-l", paramArrayOfString);
/*     */     }
/*     */ 
/* 136 */     Class[] arrayOfClass = { [Ljava.lang.String.class };
/* 137 */     Method localMethod = localClass.getDeclaredMethod("main", arrayOfClass);
/*     */ 
/* 139 */     Object[] arrayOfObject = { paramArrayOfString };
/* 140 */     localMethod.invoke(null, arrayOfObject);
/*     */   }
/*     */ 
/*     */   private static Class<?> loadSAClass()
/*     */   {
/*     */     try
/*     */     {
/* 153 */       return Class.forName("sun.jvm.hotspot.tools.JStack", true, 
/* 154 */         ClassLoader.getSystemClassLoader()); } catch (Exception localException) {
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   private static void runThreadDump(String paramString, String[] paramArrayOfString) throws Exception
/*     */   {
/* 161 */     VirtualMachine localVirtualMachine = null;
/*     */     try {
/* 163 */       localVirtualMachine = VirtualMachine.attach(paramString);
/*     */     } catch (Exception localException) {
/* 165 */       localObject = localException.getMessage();
/* 166 */       if (localObject != null)
/* 167 */         System.err.println(paramString + ": " + (String)localObject);
/*     */       else {
/* 169 */         localException.printStackTrace();
/*     */       }
/* 171 */       if (((localException instanceof AttachNotSupportedException)) && 
/* 172 */         (loadSAClass() != null)) {
/* 173 */         System.err.println("The -F option can be used when the target process is not responding");
/*     */       }
/*     */ 
/* 176 */       System.exit(1);
/*     */     }
/*     */ 
/* 181 */     InputStream localInputStream = ((HotSpotVirtualMachine)localVirtualMachine).remoteDataDump((Object[])paramArrayOfString);
/*     */ 
/* 184 */     Object localObject = new byte[256];
/*     */     int i;
/*     */     do
/*     */     {
/* 187 */       i = localInputStream.read((byte[])localObject);
/* 188 */       if (i > 0) {
/* 189 */         String str = new String((byte[])localObject, 0, i, "UTF-8");
/* 190 */         System.out.print(str);
/*     */       }
/*     */     }
/* 192 */     while (i > 0);
/* 193 */     localInputStream.close();
/* 194 */     localVirtualMachine.detach();
/*     */   }
/*     */ 
/*     */   private static String[] prepend(String paramString, String[] paramArrayOfString)
/*     */   {
/* 199 */     String[] arrayOfString = new String[paramArrayOfString.length + 1];
/* 200 */     arrayOfString[0] = paramString;
/* 201 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 1, paramArrayOfString.length);
/* 202 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static void usage(int paramInt)
/*     */   {
/* 207 */     System.err.println("Usage:");
/* 208 */     System.err.println("    jstack [-l] <pid>");
/* 209 */     System.err.println("        (to connect to running process)");
/*     */ 
/* 211 */     if (loadSAClass() != null) {
/* 212 */       System.err.println("    jstack -F [-m] [-l] <pid>");
/* 213 */       System.err.println("        (to connect to a hung process)");
/* 214 */       System.err.println("    jstack [-m] [-l] <executable> <core>");
/* 215 */       System.err.println("        (to connect to a core file)");
/* 216 */       System.err.println("    jstack [-m] [-l] [server_id@]<remote server IP or hostname>");
/* 217 */       System.err.println("        (to connect to a remote debug server)");
/*     */     }
/*     */ 
/* 220 */     System.err.println("");
/* 221 */     System.err.println("Options:");
/*     */ 
/* 223 */     if (loadSAClass() != null) {
/* 224 */       System.err.println("    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)");
/*     */ 
/* 226 */       System.err.println("    -m  to print both java and native frames (mixed mode)");
/*     */     }
/*     */ 
/* 229 */     System.err.println("    -l  long listing. Prints additional information about locks");
/* 230 */     System.err.println("    -h or -help to print this help message");
/* 231 */     System.exit(paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstack.JStack
 * JD-Core Version:    0.6.2
 */