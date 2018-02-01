/*     */ package sun.tools.jinfo;
/*     */ 
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
/*     */ 
/*     */ public class JInfo
/*     */ {
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/*  45 */     if (paramArrayOfString.length == 0) {
/*  46 */       usage(1);
/*     */     }
/*     */ 
/*  49 */     int i = 1;
/*  50 */     String str1 = paramArrayOfString[0];
/*  51 */     if (str1.startsWith("-")) {
/*  52 */       if ((str1.equals("-flags")) || 
/*  53 */         (str1
/*  53 */         .equals("-sysprops")))
/*     */       {
/*  57 */         if ((paramArrayOfString.length != 2) && (paramArrayOfString.length != 3))
/*  58 */           usage(1);
/*     */       }
/*  60 */       else if (str1.equals("-flag"))
/*     */       {
/*  62 */         i = 0;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/*  66 */         if ((str1.equals("-help")) || (str1.equals("-h")))
/*  67 */           j = 0;
/*     */         else {
/*  69 */           j = 1;
/*     */         }
/*  71 */         usage(j);
/*     */       }
/*     */     }
/*     */ 
/*  75 */     if (i != 0) {
/*  76 */       runTool(paramArrayOfString);
/*     */     }
/*  78 */     else if (paramArrayOfString.length == 3) {
/*  79 */       String str2 = paramArrayOfString[2];
/*  80 */       String str3 = paramArrayOfString[1];
/*  81 */       flag(str2, str3);
/*     */     }
/*     */     else
/*     */     {
/*     */       int k;
/*  84 */       if ((str1.equals("-help")) || (str1.equals("-h")))
/*  85 */         k = 0;
/*     */       else {
/*  87 */         k = 1;
/*     */       }
/*  89 */       usage(k);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void runTool(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/*  96 */     String str = "sun.jvm.hotspot.tools.JInfo";
/*     */ 
/*  98 */     Class localClass = loadClass(str);
/*  99 */     if (localClass == null) {
/* 100 */       usage(1);
/*     */     }
/*     */ 
/* 104 */     Class[] arrayOfClass = { [Ljava.lang.String.class };
/* 105 */     Method localMethod = localClass.getDeclaredMethod("main", arrayOfClass);
/*     */ 
/* 107 */     Object[] arrayOfObject = { paramArrayOfString };
/* 108 */     localMethod.invoke(null, arrayOfObject);
/*     */   }
/*     */ 
/*     */   private static Class<?> loadClass(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 120 */       return Class.forName(paramString, true, 
/* 121 */         ClassLoader.getSystemClassLoader()); } catch (Exception localException) {
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   private static void flag(String paramString1, String paramString2) throws IOException {
/* 127 */     VirtualMachine localVirtualMachine = attach(paramString1);
/*     */ 
/* 130 */     int i = paramString2.indexOf('=');
/*     */     String str1;
/*     */     InputStream localInputStream;
/* 131 */     if (i != -1) {
/* 132 */       str1 = paramString2.substring(0, i);
/* 133 */       String str2 = paramString2.substring(i + 1);
/* 134 */       localInputStream = ((HotSpotVirtualMachine)localVirtualMachine).setFlag(str1, str2);
/*     */     } else {
/* 136 */       int j = paramString2.charAt(0);
/* 137 */       switch (j) {
/*     */       case 43:
/* 139 */         str1 = paramString2.substring(1);
/* 140 */         localInputStream = ((HotSpotVirtualMachine)localVirtualMachine).setFlag(str1, "1");
/* 141 */         break;
/*     */       case 45:
/* 143 */         str1 = paramString2.substring(1);
/* 144 */         localInputStream = ((HotSpotVirtualMachine)localVirtualMachine).setFlag(str1, "0");
/* 145 */         break;
/*     */       default:
/* 147 */         str1 = paramString2;
/* 148 */         localInputStream = ((HotSpotVirtualMachine)localVirtualMachine).printFlag(str1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 153 */     drain(localVirtualMachine, localInputStream);
/*     */   }
/*     */ 
/*     */   private static VirtualMachine attach(String paramString)
/*     */   {
/*     */     try {
/* 159 */       return VirtualMachine.attach(paramString);
/*     */     } catch (Exception localException) {
/* 161 */       String str = localException.getMessage();
/* 162 */       if (str != null)
/* 163 */         System.err.println(paramString + ": " + str);
/*     */       else {
/* 165 */         localException.printStackTrace();
/*     */       }
/* 167 */       System.exit(1);
/* 168 */     }return null;
/*     */   }
/*     */ 
/*     */   private static void drain(VirtualMachine paramVirtualMachine, InputStream paramInputStream) throws IOException
/*     */   {
/* 175 */     byte[] arrayOfByte = new byte[256];
/*     */     int i;
/*     */     do
/*     */     {
/* 178 */       i = paramInputStream.read(arrayOfByte);
/* 179 */       if (i > 0) {
/* 180 */         String str = new String(arrayOfByte, 0, i, "UTF-8");
/* 181 */         System.out.print(str);
/*     */       }
/*     */     }
/* 183 */     while (i > 0);
/* 184 */     paramInputStream.close();
/* 185 */     paramVirtualMachine.detach();
/*     */   }
/*     */ 
/*     */   private static void usage(int paramInt)
/*     */   {
/* 192 */     Class localClass = loadClass("sun.jvm.hotspot.tools.JInfo");
/* 193 */     int i = localClass != null ? 1 : 0;
/*     */ 
/* 195 */     System.err.println("Usage:");
/* 196 */     if (i != 0) {
/* 197 */       System.err.println("    jinfo [option] <pid>");
/* 198 */       System.err.println("        (to connect to running process)");
/* 199 */       System.err.println("    jinfo [option] <executable <core>");
/* 200 */       System.err.println("        (to connect to a core file)");
/* 201 */       System.err.println("    jinfo [option] [server_id@]<remote server IP or hostname>");
/* 202 */       System.err.println("        (to connect to remote debug server)");
/* 203 */       System.err.println("");
/* 204 */       System.err.println("where <option> is one of:");
/* 205 */       System.err.println("    -flag <name>         to print the value of the named VM flag");
/* 206 */       System.err.println("    -flag [+|-]<name>    to enable or disable the named VM flag");
/* 207 */       System.err.println("    -flag <name>=<value> to set the named VM flag to the given value");
/* 208 */       System.err.println("    -flags               to print VM flags");
/* 209 */       System.err.println("    -sysprops            to print Java system properties");
/* 210 */       System.err.println("    <no option>          to print both of the above");
/* 211 */       System.err.println("    -h | -help           to print this help message");
/*     */     } else {
/* 213 */       System.err.println("    jinfo <option> <pid>");
/* 214 */       System.err.println("       (to connect to a running process)");
/* 215 */       System.err.println("");
/* 216 */       System.err.println("where <option> is one of:");
/* 217 */       System.err.println("    -flag <name>         to print the value of the named VM flag");
/* 218 */       System.err.println("    -flag [+|-]<name>    to enable or disable the named VM flag");
/* 219 */       System.err.println("    -flag <name>=<value> to set the named VM flag to the given value");
/* 220 */       System.err.println("    -h | -help           to print this help message");
/*     */     }
/*     */ 
/* 223 */     System.exit(paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jinfo.JInfo
 * JD-Core Version:    0.6.2
 */