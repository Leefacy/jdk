/*     */ package sun.tools.jcmd;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class Arguments
/*     */ {
/*  33 */   private boolean listProcesses = false;
/*  34 */   private boolean listCounters = false;
/*  35 */   private boolean showUsage = false;
/*  36 */   private int pid = -1;
/*  37 */   private String command = null;
/*     */   private String processSubstring;
/*     */ 
/*     */   public boolean isListProcesses()
/*     */   {
/*  40 */     return this.listProcesses; } 
/*  41 */   public boolean isListCounters() { return this.listCounters; } 
/*  42 */   public boolean isShowUsage() { return this.showUsage; } 
/*  43 */   public int getPid() { return this.pid; } 
/*  44 */   public String getCommand() { return this.command; } 
/*  45 */   public String getProcessSubstring() { return this.processSubstring; }
/*     */ 
/*     */   public Arguments(String[] paramArrayOfString) {
/*  48 */     if ((paramArrayOfString.length == 0) || (paramArrayOfString[0].equals("-l"))) {
/*  49 */       this.listProcesses = true;
/*  50 */       return;
/*     */     }
/*     */ 
/*  53 */     if ((paramArrayOfString[0].equals("-h")) || (paramArrayOfString[0].equals("-help"))) {
/*  54 */       this.showUsage = true;
/*  55 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  59 */       this.pid = Integer.parseInt(paramArrayOfString[0]);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/*  62 */       if (paramArrayOfString[0].charAt(0) != '-')
/*     */       {
/*  64 */         this.processSubstring = paramArrayOfString[0];
/*     */       }
/*     */     }
/*     */ 
/*  68 */     StringBuilder localStringBuilder = new StringBuilder();
/*  69 */     for (int i = 1; i < paramArrayOfString.length; i++) {
/*  70 */       if (paramArrayOfString[i].equals("-f")) {
/*  71 */         if (paramArrayOfString.length == i + 1) {
/*  72 */           throw new IllegalArgumentException("No file specified for parameter -f");
/*     */         }
/*  74 */         if (paramArrayOfString.length == i + 2) {
/*     */           try {
/*  76 */             readCommandFile(paramArrayOfString[(i + 1)]);
/*     */           } catch (IOException localIOException) {
/*  78 */             throw new IllegalArgumentException("Could not read from file specified with -f option: " + paramArrayOfString[(i + 1)]);
/*     */           }
/*     */ 
/*  82 */           return;
/*     */         }
/*  84 */         throw new IllegalArgumentException("Options after -f are not allowed");
/*     */       }
/*     */ 
/*  87 */       if (paramArrayOfString[i].equals("PerfCounter.print"))
/*  88 */         this.listCounters = true;
/*     */       else {
/*  90 */         localStringBuilder.append(paramArrayOfString[i]).append(" ");
/*     */       }
/*     */     }
/*     */ 
/*  94 */     if ((this.listCounters != true) && (localStringBuilder.length() == 0)) {
/*  95 */       throw new IllegalArgumentException("No command specified");
/*     */     }
/*     */ 
/*  98 */     this.command = localStringBuilder.toString().trim();
/*     */   }
/*     */ 
/*     */   private void readCommandFile(String paramString) throws IOException {
/* 102 */     BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString)); Object localObject1 = null;
/*     */     try { StringBuilder localStringBuilder = new StringBuilder();
/*     */       String str;
/* 105 */       while ((str = localBufferedReader.readLine()) != null) {
/* 106 */         localStringBuilder.append(str).append("\n");
/*     */       }
/* 108 */       this.command = localStringBuilder.toString();
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 102 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally
/*     */     {
/* 109 */       if (localBufferedReader != null) if (localObject1 != null) try { localBufferedReader.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localBufferedReader.close();  
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void usage()
/*     */   {
/* 113 */     System.out.println("Usage: jcmd <pid | main class> <command ...|PerfCounter.print|-f file>");
/* 114 */     System.out.println("   or: jcmd -l                                                    ");
/* 115 */     System.out.println("   or: jcmd -h                                                    ");
/* 116 */     System.out.println("                                                                  ");
/* 117 */     System.out.println("  command must be a valid jcmd command for the selected jvm.      ");
/* 118 */     System.out.println("  Use the command \"help\" to see which commands are available.   ");
/* 119 */     System.out.println("  If the pid is 0, commands will be sent to all Java processes.   ");
/* 120 */     System.out.println("  The main class argument will be used to match (either partially ");
/* 121 */     System.out.println("  or fully) the class used to start Java.                         ");
/* 122 */     System.out.println("  If no options are given, lists Java processes (same as -p).     ");
/* 123 */     System.out.println("                                                                  ");
/* 124 */     System.out.println("  PerfCounter.print display the counters exposed by this process  ");
/* 125 */     System.out.println("  -f  read and execute commands from the file                     ");
/* 126 */     System.out.println("  -l  list JVM processes on the local machine                     ");
/* 127 */     System.out.println("  -h  this help                                                   ");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jcmd.Arguments
 * JD-Core Version:    0.6.2
 */