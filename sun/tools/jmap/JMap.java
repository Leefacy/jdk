/*     */ package sun.tools.jmap;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
/*     */ 
/*     */ public class JMap
/*     */ {
/*  47 */   private static String HISTO_OPTION = "-histo";
/*  48 */   private static String LIVE_HISTO_OPTION = "-histo:live";
/*  49 */   private static String DUMP_OPTION_PREFIX = "-dump:";
/*     */ 
/*  52 */   private static String SA_TOOL_OPTIONS = "-heap|-heap:format=b|-clstats|-finalizerinfo";
/*     */ 
/*  56 */   private static String FORCE_SA_OPTION = "-F";
/*     */ 
/*  59 */   private static String DEFAULT_OPTION = "-pmap";
/*     */   private static final String LIVE_OBJECTS_OPTION = "-live";
/*     */   private static final String ALL_OBJECTS_OPTION = "-all";
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/*  62 */     if (paramArrayOfString.length == 0) {
/*  63 */       usage(1);
/*     */     }
/*     */ 
/*  67 */     int i = 0;
/*     */ 
/*  70 */     Object localObject1 = null;
/*     */ 
/*  74 */     int j = 0;
/*  75 */     while (j < paramArrayOfString.length) {
/*  76 */       String str = paramArrayOfString[j];
/*  77 */       if (!str.startsWith("-")) {
/*     */         break;
/*     */       }
/*  80 */       if ((str.equals("-help")) || (str.equals("-h"))) {
/*  81 */         usage(0);
/*  82 */       } else if (str.equals(FORCE_SA_OPTION)) {
/*  83 */         i = 1;
/*     */       } else {
/*  85 */         if (localObject1 != null) {
/*  86 */           usage(1);
/*     */         }
/*  88 */         localObject1 = str;
/*     */       }
/*  90 */       j++;
/*     */     }
/*     */ 
/*  94 */     if (localObject1 == null) {
/*  95 */       localObject1 = DEFAULT_OPTION;
/*     */     }
/*  97 */     if (((String)localObject1).matches(SA_TOOL_OPTIONS)) {
/*  98 */       i = 1;
/*     */     }
/*     */ 
/* 104 */     int k = paramArrayOfString.length - j;
/* 105 */     if ((k == 0) || (k > 2)) {
/* 106 */       usage(1);
/*     */     }
/*     */ 
/* 109 */     if ((j == 0) || (k != 1)) {
/* 110 */       i = 1;
/*     */     }
/* 115 */     else if (!paramArrayOfString[j].matches("[0-9]+"))
/* 116 */       i = 1;
/*     */     Object localObject2;
/* 124 */     if (i != 0)
/*     */     {
/* 126 */       localObject2 = new String[k];
/* 127 */       for (int m = j; m < paramArrayOfString.length; m++) {
/* 128 */         localObject2[(m - j)] = paramArrayOfString[m];
/*     */       }
/* 130 */       runTool((String)localObject1, (String[])localObject2);
/*     */     }
/*     */     else {
/* 133 */       localObject2 = paramArrayOfString[1];
/*     */ 
/* 137 */       if (((String)localObject1).equals(HISTO_OPTION))
/* 138 */         histo((String)localObject2, false);
/* 139 */       else if (((String)localObject1).equals(LIVE_HISTO_OPTION))
/* 140 */         histo((String)localObject2, true);
/* 141 */       else if (((String)localObject1).startsWith(DUMP_OPTION_PREFIX))
/* 142 */         dump((String)localObject2, (String)localObject1);
/*     */       else
/* 144 */         usage(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void runTool(String paramString, String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/* 151 */     String[][] arrayOfString; = { { "-pmap", "sun.jvm.hotspot.tools.PMap" }, { "-heap", "sun.jvm.hotspot.tools.HeapSummary" }, { "-heap:format=b", "sun.jvm.hotspot.tools.HeapDumper" }, { "-histo", "sun.jvm.hotspot.tools.ObjectHistogram" }, { "-clstats", "sun.jvm.hotspot.tools.ClassLoaderStats" }, { "-finalizerinfo", "sun.jvm.hotspot.tools.FinalizerInfo" } };
/*     */ 
/* 160 */     String str1 = null;
/*     */ 
/* 163 */     if (paramString.startsWith(DUMP_OPTION_PREFIX))
/*     */     {
/* 165 */       String str2 = parseDumpOptions(paramString);
/* 166 */       if (str2 == null) {
/* 167 */         usage(1);
/*     */       }
/*     */ 
/* 171 */       str1 = "sun.jvm.hotspot.tools.HeapDumper";
/*     */ 
/* 174 */       paramArrayOfString = prepend(str2, paramArrayOfString);
/* 175 */       paramArrayOfString = prepend("-f", paramArrayOfString);
/*     */     } else {
/* 177 */       int i = 0;
/* 178 */       while (i < arrayOfString;.length) {
/* 179 */         if (paramString.equals(arrayOfString;[i][0])) {
/* 180 */           str1 = arrayOfString;[i][1];
/* 181 */           break;
/*     */         }
/* 183 */         i++;
/*     */       }
/*     */     }
/* 186 */     if (str1 == null) {
/* 187 */       usage(1);
/*     */     }
/*     */ 
/* 191 */     Class localClass = loadClass(str1);
/* 192 */     if (localClass == null) {
/* 193 */       usage(1);
/*     */     }
/*     */ 
/* 197 */     Class[] arrayOfClass = { [Ljava.lang.String.class };
/* 198 */     Method localMethod = localClass.getDeclaredMethod("main", arrayOfClass);
/*     */ 
/* 200 */     Object[] arrayOfObject = { paramArrayOfString };
/* 201 */     localMethod.invoke(null, arrayOfObject);
/*     */   }
/*     */ 
/*     */   private static Class<?> loadClass(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 213 */       return Class.forName(paramString, true, 
/* 214 */         ClassLoader.getSystemClassLoader()); } catch (Exception localException) {
/*     */     }
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */   private static void histo(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 222 */     VirtualMachine localVirtualMachine = attach(paramString);
/*     */ 
/* 224 */     InputStream localInputStream = ((HotSpotVirtualMachine)localVirtualMachine)
/* 224 */       .heapHisto(new Object[] { paramBoolean ? LIVE_OBJECTS_OPTION : ALL_OBJECTS_OPTION });
/*     */ 
/* 225 */     drain(localVirtualMachine, localInputStream);
/*     */   }
/*     */ 
/*     */   private static void dump(String paramString1, String paramString2) throws IOException
/*     */   {
/* 230 */     String str = parseDumpOptions(paramString2);
/* 231 */     if (str == null) {
/* 232 */       usage(1);
/*     */     }
/*     */ 
/* 239 */     str = new File(str).getCanonicalPath();
/*     */ 
/* 242 */     boolean bool = isDumpLiveObjects(paramString2);
/*     */ 
/* 244 */     VirtualMachine localVirtualMachine = attach(paramString1);
/* 245 */     System.out.println("Dumping heap to " + str + " ...");
/*     */ 
/* 247 */     InputStream localInputStream = ((HotSpotVirtualMachine)localVirtualMachine)
/* 247 */       .dumpHeap(new Object[] { str, bool ? LIVE_OBJECTS_OPTION : ALL_OBJECTS_OPTION });
/*     */ 
/* 249 */     drain(localVirtualMachine, localInputStream);
/*     */   }
/*     */ 
/*     */   private static String parseDumpOptions(String paramString)
/*     */   {
/* 256 */     assert (paramString.startsWith(DUMP_OPTION_PREFIX));
/*     */ 
/* 258 */     String str1 = null;
/*     */ 
/* 261 */     String[] arrayOfString = paramString.substring(DUMP_OPTION_PREFIX.length()).split(",");
/*     */ 
/* 263 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 264 */       String str2 = arrayOfString[i];
/*     */ 
/* 266 */       if (!str2.equals("format=b"))
/*     */       {
/* 268 */         if (!str2.equals("live"))
/*     */         {
/* 273 */           if (str2.startsWith("file=")) {
/* 274 */             str1 = str2.substring(5);
/* 275 */             if (str1.length() == 0)
/* 276 */               return null;
/*     */           }
/*     */           else {
/* 279 */             return null;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 283 */     return str1;
/*     */   }
/*     */ 
/*     */   private static boolean isDumpLiveObjects(String paramString)
/*     */   {
/* 288 */     String[] arrayOfString1 = paramString.substring(DUMP_OPTION_PREFIX.length()).split(",");
/* 289 */     for (String str : arrayOfString1) {
/* 290 */       if (str.equals("live")) {
/* 291 */         return true;
/*     */       }
/*     */     }
/* 294 */     return false;
/*     */   }
/*     */ 
/*     */   private static VirtualMachine attach(String paramString)
/*     */   {
/*     */     try {
/* 300 */       return VirtualMachine.attach(paramString);
/*     */     } catch (Exception localException) {
/* 302 */       String str = localException.getMessage();
/* 303 */       if (str != null)
/* 304 */         System.err.println(paramString + ": " + str);
/*     */       else {
/* 306 */         localException.printStackTrace();
/*     */       }
/* 308 */       if (((localException instanceof AttachNotSupportedException)) && (haveSA())) {
/* 309 */         System.err.println("The -F option can be used when the target process is not responding");
/*     */       }
/*     */ 
/* 312 */       System.exit(1);
/* 313 */     }return null;
/*     */   }
/*     */ 
/*     */   private static void drain(VirtualMachine paramVirtualMachine, InputStream paramInputStream) throws IOException
/*     */   {
/* 320 */     byte[] arrayOfByte = new byte[256];
/*     */     int i;
/*     */     do
/*     */     {
/* 323 */       i = paramInputStream.read(arrayOfByte);
/* 324 */       if (i > 0) {
/* 325 */         String str = new String(arrayOfByte, 0, i, "UTF-8");
/* 326 */         System.out.print(str);
/*     */       }
/*     */     }
/* 328 */     while (i > 0);
/* 329 */     paramInputStream.close();
/* 330 */     paramVirtualMachine.detach();
/*     */   }
/*     */ 
/*     */   private static String[] prepend(String paramString, String[] paramArrayOfString)
/*     */   {
/* 335 */     String[] arrayOfString = new String[paramArrayOfString.length + 1];
/* 336 */     arrayOfString[0] = paramString;
/* 337 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 1, paramArrayOfString.length);
/* 338 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static boolean haveSA()
/*     */   {
/* 343 */     Class localClass = loadClass("sun.jvm.hotspot.tools.HeapSummary");
/* 344 */     return localClass != null;
/*     */   }
/*     */ 
/*     */   private static void usage(int paramInt)
/*     */   {
/* 349 */     System.err.println("Usage:");
/* 350 */     if (haveSA()) {
/* 351 */       System.err.println("    jmap [option] <pid>");
/* 352 */       System.err.println("        (to connect to running process)");
/* 353 */       System.err.println("    jmap [option] <executable <core>");
/* 354 */       System.err.println("        (to connect to a core file)");
/* 355 */       System.err.println("    jmap [option] [server_id@]<remote server IP or hostname>");
/* 356 */       System.err.println("        (to connect to remote debug server)");
/* 357 */       System.err.println("");
/* 358 */       System.err.println("where <option> is one of:");
/* 359 */       System.err.println("    <none>               to print same info as Solaris pmap");
/* 360 */       System.err.println("    -heap                to print java heap summary");
/* 361 */       System.err.println("    -histo[:live]        to print histogram of java object heap; if the \"live\"");
/* 362 */       System.err.println("                         suboption is specified, only count live objects");
/* 363 */       System.err.println("    -clstats             to print class loader statistics");
/* 364 */       System.err.println("    -finalizerinfo       to print information on objects awaiting finalization");
/* 365 */       System.err.println("    -dump:<dump-options> to dump java heap in hprof binary format");
/* 366 */       System.err.println("                         dump-options:");
/* 367 */       System.err.println("                           live         dump only live objects; if not specified,");
/* 368 */       System.err.println("                                        all objects in the heap are dumped.");
/* 369 */       System.err.println("                           format=b     binary format");
/* 370 */       System.err.println("                           file=<file>  dump heap to <file>");
/* 371 */       System.err.println("                         Example: jmap -dump:live,format=b,file=heap.bin <pid>");
/* 372 */       System.err.println("    -F                   force. Use with -dump:<dump-options> <pid> or -histo");
/* 373 */       System.err.println("                         to force a heap dump or histogram when <pid> does not");
/* 374 */       System.err.println("                         respond. The \"live\" suboption is not supported");
/* 375 */       System.err.println("                         in this mode.");
/* 376 */       System.err.println("    -h | -help           to print this help message");
/* 377 */       System.err.println("    -J<flag>             to pass <flag> directly to the runtime system");
/*     */     } else {
/* 379 */       System.err.println("    jmap -histo <pid>");
/* 380 */       System.err.println("      (to connect to running process and print histogram of java object heap");
/* 381 */       System.err.println("    jmap -dump:<dump-options> <pid>");
/* 382 */       System.err.println("      (to connect to running process and dump java heap)");
/* 383 */       System.err.println("");
/* 384 */       System.err.println("    dump-options:");
/* 385 */       System.err.println("      format=b     binary default");
/* 386 */       System.err.println("      file=<file>  dump heap to <file>");
/* 387 */       System.err.println("");
/* 388 */       System.err.println("    Example:       jmap -dump:format=b,file=heap.bin <pid>");
/*     */     }
/*     */ 
/* 391 */     System.exit(paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jmap.JMap
 * JD-Core Version:    0.6.2
 */