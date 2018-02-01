/*     */ package sun.tools.jps;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.URISyntaxException;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ 
/*     */ public class Arguments
/*     */ {
/*  41 */   private static final boolean debug = Boolean.getBoolean("jps.debug");
/*  42 */   private static final boolean printStackTrace = Boolean.getBoolean("jps.printStackTrace");
/*     */   private boolean help;
/*     */   private boolean quiet;
/*     */   private boolean longPaths;
/*     */   private boolean vmArgs;
/*     */   private boolean vmFlags;
/*     */   private boolean mainArgs;
/*     */   private String hostname;
/*     */   private HostIdentifier hostId;
/*     */ 
/*     */   public static void printUsage(PrintStream paramPrintStream)
/*     */   {
/*  55 */     paramPrintStream.println("usage: jps [-help]");
/*  56 */     paramPrintStream.println("       jps [-q] [-mlvV] [<hostid>]");
/*  57 */     paramPrintStream.println();
/*  58 */     paramPrintStream.println("Definitions:");
/*  59 */     paramPrintStream.println("    <hostid>:      <hostname>[:<port>]");
/*     */   }
/*     */ 
/*     */   public Arguments(String[] paramArrayOfString) throws IllegalArgumentException {
/*  63 */     int i = 0;
/*     */ 
/*  65 */     if ((paramArrayOfString.length == 1) && (
/*  66 */       (paramArrayOfString[0].compareTo("-?") == 0) || 
/*  67 */       (paramArrayOfString[0]
/*  67 */       .compareTo("-help") == 0)))
/*     */     {
/*  68 */       this.help = true;
/*  69 */       return;
/*     */     }
/*     */ 
/*  73 */     for (i = 0; (i < paramArrayOfString.length) && (paramArrayOfString[i].startsWith("-")); 
/*  74 */       i++) {
/*  75 */       String str = paramArrayOfString[i];
/*     */ 
/*  77 */       if (str.compareTo("-q") == 0)
/*  78 */         this.quiet = true;
/*  79 */       else if (str.startsWith("-")) {
/*  80 */         for (int j = 1; j < str.length(); j++) {
/*  81 */           switch (str.charAt(j)) {
/*     */           case 'm':
/*  83 */             this.mainArgs = true;
/*  84 */             break;
/*     */           case 'l':
/*  86 */             this.longPaths = true;
/*  87 */             break;
/*     */           case 'v':
/*  89 */             this.vmArgs = true;
/*  90 */             break;
/*     */           case 'V':
/*  92 */             this.vmFlags = true;
/*  93 */             break;
/*     */           default:
/*  95 */             throw new IllegalArgumentException("illegal argument: " + paramArrayOfString[i]);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 100 */         throw new IllegalArgumentException("illegal argument: " + paramArrayOfString[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 105 */     switch (paramArrayOfString.length - i) {
/*     */     case 0:
/* 107 */       this.hostname = null;
/* 108 */       break;
/*     */     case 1:
/* 110 */       this.hostname = paramArrayOfString[(paramArrayOfString.length - 1)];
/* 111 */       break;
/*     */     default:
/* 113 */       throw new IllegalArgumentException("invalid argument count");
/*     */     }
/*     */     try
/*     */     {
/* 117 */       this.hostId = new HostIdentifier(this.hostname);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 119 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Malformed Host Identifier: " + this.hostname);
/*     */ 
/* 122 */       localIllegalArgumentException.initCause(localURISyntaxException);
/* 123 */       throw localIllegalArgumentException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDebug() {
/* 128 */     return debug;
/*     */   }
/*     */ 
/*     */   public boolean printStackTrace() {
/* 132 */     return printStackTrace;
/*     */   }
/*     */ 
/*     */   public boolean isHelp() {
/* 136 */     return this.help;
/*     */   }
/*     */ 
/*     */   public boolean isQuiet() {
/* 140 */     return this.quiet;
/*     */   }
/*     */ 
/*     */   public boolean showLongPaths() {
/* 144 */     return this.longPaths;
/*     */   }
/*     */ 
/*     */   public boolean showVmArgs() {
/* 148 */     return this.vmArgs;
/*     */   }
/*     */ 
/*     */   public boolean showVmFlags() {
/* 152 */     return this.vmFlags;
/*     */   }
/*     */ 
/*     */   public boolean showMainArgs() {
/* 156 */     return this.mainArgs;
/*     */   }
/*     */ 
/*     */   public String hostname() {
/* 160 */     return this.hostname;
/*     */   }
/*     */ 
/*     */   public HostIdentifier hostId() {
/* 164 */     return this.hostId;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jps.Arguments
 * JD-Core Version:    0.6.2
 */