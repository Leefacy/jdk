/*     */ package com.sun.tools.hat;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.ReachableExcludesImpl;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.parser.Reader;
/*     */ import com.sun.tools.hat.internal.server.QueryListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Main
/*     */ {
/*  49 */   private static String VERSION_STRING = "jhat version 2.0";
/*     */ 
/*     */   private static void usage(String paramString) {
/*  52 */     if (paramString != null) {
/*  53 */       System.err.println("ERROR: " + paramString);
/*     */     }
/*  55 */     System.err.println("Usage:  jhat [-stack <bool>] [-refs <bool>] [-port <port>] [-baseline <file>] [-debug <int>] [-version] [-h|-help] <file>");
/*  56 */     System.err.println();
/*  57 */     System.err.println("\t-J<flag>          Pass <flag> directly to the runtime system. For");
/*  58 */     System.err.println("\t\t\t  example, -J-mx512m to use a maximum heap size of 512MB");
/*  59 */     System.err.println("\t-stack false:     Turn off tracking object allocation call stack.");
/*  60 */     System.err.println("\t-refs false:      Turn off tracking of references to objects");
/*  61 */     System.err.println("\t-port <port>:     Set the port for the HTTP server.  Defaults to 7000");
/*  62 */     System.err.println("\t-exclude <file>:  Specify a file that lists data members that should");
/*  63 */     System.err.println("\t\t\t  be excluded from the reachableFrom query.");
/*  64 */     System.err.println("\t-baseline <file>: Specify a baseline object dump.  Objects in");
/*  65 */     System.err.println("\t\t\t  both heap dumps with the same ID and same class will");
/*  66 */     System.err.println("\t\t\t  be marked as not being \"new\".");
/*  67 */     System.err.println("\t-debug <int>:     Set debug level.");
/*  68 */     System.err.println("\t\t\t    0:  No debug output");
/*  69 */     System.err.println("\t\t\t    1:  Debug hprof file parsing");
/*  70 */     System.err.println("\t\t\t    2:  Debug hprof file parsing, no server");
/*  71 */     System.err.println("\t-version          Report version number");
/*  72 */     System.err.println("\t-h|-help          Print this help and exit");
/*  73 */     System.err.println("\t<file>            The file to read");
/*  74 */     System.err.println();
/*  75 */     System.err.println("For a dump file that contains multiple heap dumps,");
/*  76 */     System.err.println("you may specify which dump in the file");
/*  77 */     System.err.println("by appending \"#<number>\" to the file name, i.e. \"foo.hprof#3\".");
/*  78 */     System.err.println();
/*  79 */     System.err.println("All boolean options default to \"true\"");
/*  80 */     System.exit(1);
/*     */   }
/*     */ 
/*     */   private static boolean booleanValue(String paramString)
/*     */   {
/*  87 */     if ("true".equalsIgnoreCase(paramString))
/*  88 */       return true;
/*  89 */     if ("false".equalsIgnoreCase(paramString)) {
/*  90 */       return false;
/*     */     }
/*  92 */     usage("Boolean value must be true or false");
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  98 */     if (paramArrayOfString.length < 1) {
/*  99 */       usage("No arguments supplied");
/*     */     }
/*     */ 
/* 102 */     boolean bool1 = false;
/* 103 */     int i = 7000;
/* 104 */     boolean bool2 = true;
/* 105 */     boolean bool3 = true;
/* 106 */     Object localObject1 = null;
/* 107 */     Object localObject2 = null;
/* 108 */     int j = 0;
/* 109 */     for (int k = 0; ; k += 2) {
/* 110 */       if (k > paramArrayOfString.length - 1) {
/* 111 */         usage("Option parsing error");
/*     */       }
/* 113 */       if ("-version".equals(paramArrayOfString[k])) {
/* 114 */         System.out.print(VERSION_STRING);
/* 115 */         System.out.println(" (java version " + System.getProperty("java.version") + ")");
/* 116 */         System.exit(0);
/*     */       }
/*     */ 
/* 119 */       if (("-h".equals(paramArrayOfString[k])) || ("-help".equals(paramArrayOfString[k]))) {
/* 120 */         usage(null);
/*     */       }
/*     */ 
/* 123 */       if (k == paramArrayOfString.length - 1) {
/*     */         break;
/*     */       }
/* 126 */       localObject3 = paramArrayOfString[k];
/* 127 */       localObject4 = paramArrayOfString[(k + 1)];
/* 128 */       if ("-stack".equals(localObject3))
/* 129 */         bool2 = booleanValue((String)localObject4);
/* 130 */       else if ("-refs".equals(localObject3))
/* 131 */         bool3 = booleanValue((String)localObject4);
/* 132 */       else if ("-port".equals(localObject3))
/* 133 */         i = Integer.parseInt((String)localObject4, 10);
/* 134 */       else if ("-exclude".equals(localObject3))
/* 135 */         localObject2 = localObject4;
/* 136 */       else if ("-baseline".equals(localObject3))
/* 137 */         localObject1 = localObject4;
/* 138 */       else if ("-debug".equals(localObject3))
/* 139 */         j = Integer.parseInt((String)localObject4, 10);
/* 140 */       else if ("-parseonly".equals(localObject3))
/*     */       {
/* 142 */         bool1 = booleanValue((String)localObject4);
/*     */       }
/*     */     }
/* 145 */     String str = paramArrayOfString[(paramArrayOfString.length - 1)];
/* 146 */     Object localObject3 = null;
/* 147 */     Object localObject4 = null;
/* 148 */     if (localObject2 != null) {
/* 149 */       localObject4 = new File(localObject2);
/* 150 */       if (!((File)localObject4).exists()) {
/* 151 */         System.out.println("Exclude file " + localObject4 + " does not exist.  Aborting.");
/*     */ 
/* 153 */         System.exit(1);
/*     */       }
/*     */     }
/*     */ 
/* 157 */     System.out.println("Reading from " + str + "...");
/*     */     try {
/* 159 */       localObject3 = Reader.readFile(str, bool2, j);
/*     */     } catch (IOException localIOException1) {
/* 161 */       localIOException1.printStackTrace();
/* 162 */       System.exit(1);
/*     */     } catch (RuntimeException localRuntimeException1) {
/* 164 */       localRuntimeException1.printStackTrace();
/* 165 */       System.exit(1);
/*     */     }
/* 167 */     System.out.println("Snapshot read, resolving...");
/* 168 */     ((Snapshot)localObject3).resolve(bool3);
/* 169 */     System.out.println("Snapshot resolved.");
/*     */ 
/* 171 */     if (localObject4 != null) {
/* 172 */       ((Snapshot)localObject3).setReachableExcludes(new ReachableExcludesImpl((File)localObject4));
/*     */     }
/*     */ 
/* 175 */     if (localObject1 != null) {
/* 176 */       System.out.println("Reading baseline snapshot...");
/* 177 */       localObject5 = null;
/*     */       try {
/* 179 */         localObject5 = Reader.readFile(localObject1, false, j);
/*     */       }
/*     */       catch (IOException localIOException2) {
/* 182 */         localIOException2.printStackTrace();
/* 183 */         System.exit(1);
/*     */       } catch (RuntimeException localRuntimeException2) {
/* 185 */         localRuntimeException2.printStackTrace();
/* 186 */         System.exit(1);
/*     */       }
/* 188 */       ((Snapshot)localObject5).resolve(false);
/* 189 */       System.out.println("Discovering new objects...");
/* 190 */       ((Snapshot)localObject3).markNewRelativeTo((Snapshot)localObject5);
/* 191 */       localObject5 = null;
/*     */     }
/* 193 */     if (j == 2) {
/* 194 */       System.out.println("No server, -debug 2 was used.");
/* 195 */       System.exit(0);
/*     */     }
/*     */ 
/* 198 */     if (bool1)
/*     */     {
/* 200 */       System.out.println("-parseonly is true, exiting..");
/* 201 */       System.exit(0);
/*     */     }
/*     */ 
/* 204 */     Object localObject5 = new QueryListener(i);
/* 205 */     ((QueryListener)localObject5).setModel((Snapshot)localObject3);
/* 206 */     Thread localThread = new Thread((Runnable)localObject5, "Query Listener");
/* 207 */     localThread.setPriority(6);
/* 208 */     localThread.start();
/* 209 */     System.out.println("Started HTTP server on port " + i);
/* 210 */     System.out.println("Server is ready.");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.Main
 * JD-Core Version:    0.6.2
 */