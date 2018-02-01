/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ 
/*     */ public class Arguments
/*     */ {
/*  44 */   private static final boolean debug = Boolean.getBoolean("jstat.debug");
/*     */ 
/*  46 */   private static final boolean showUnsupported = Boolean.getBoolean("jstat.showUnsupported")
/*  46 */     ;
/*     */   private static final String JVMSTAT_USERDIR = ".jvmstat";
/*     */   private static final String OPTIONS_FILENAME = "jstat_options";
/*     */   private static final String UNSUPPORTED_OPTIONS_FILENAME = "jstat_unsupported_options";
/*     */   private static final String ALL_NAMES = "\\w*";
/*     */   private Comparator<Monitor> comparator;
/*     */   private int headerRate;
/*     */   private boolean help;
/*     */   private boolean list;
/*     */   private boolean options;
/*     */   private boolean constants;
/*     */   private boolean constantsOnly;
/*     */   private boolean strings;
/*     */   private boolean timestamp;
/*     */   private boolean snap;
/*     */   private boolean verbose;
/*     */   private String specialOption;
/*     */   private String names;
/*     */   private OptionFormat optionFormat;
/*  69 */   private int count = -1;
/*  70 */   private int interval = -1;
/*     */   private String vmIdString;
/*     */   private VmIdentifier vmId;
/*     */ 
/*     */   public static void printUsage(PrintStream paramPrintStream)
/*     */   {
/*  76 */     paramPrintStream.println("Usage: jstat -help|-options");
/*  77 */     paramPrintStream.println("       jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]");
/*  78 */     paramPrintStream.println();
/*  79 */     paramPrintStream.println("Definitions:");
/*  80 */     paramPrintStream.println("  <option>      An option reported by the -options option");
/*  81 */     paramPrintStream.println("  <vmid>        Virtual Machine Identifier. A vmid takes the following form:");
/*  82 */     paramPrintStream.println("                     <lvmid>[@<hostname>[:<port>]]");
/*  83 */     paramPrintStream.println("                Where <lvmid> is the local vm identifier for the target");
/*  84 */     paramPrintStream.println("                Java virtual machine, typically a process id; <hostname> is");
/*  85 */     paramPrintStream.println("                the name of the host running the target Java virtual machine;");
/*  86 */     paramPrintStream.println("                and <port> is the port number for the rmiregistry on the");
/*  87 */     paramPrintStream.println("                target host. See the jvmstat documentation for a more complete");
/*  88 */     paramPrintStream.println("                description of the Virtual Machine Identifier.");
/*  89 */     paramPrintStream.println("  <lines>       Number of samples between header lines.");
/*  90 */     paramPrintStream.println("  <interval>    Sampling interval. The following forms are allowed:");
/*  91 */     paramPrintStream.println("                    <n>[\"ms\"|\"s\"]");
/*  92 */     paramPrintStream.println("                Where <n> is an integer and the suffix specifies the units as ");
/*  93 */     paramPrintStream.println("                milliseconds(\"ms\") or seconds(\"s\"). The default units are \"ms\".");
/*  94 */     paramPrintStream.println("  <count>       Number of samples to take before terminating.");
/*  95 */     paramPrintStream.println("  -J<flag>      Pass <flag> directly to the runtime system.");
/*     */   }
/*     */ 
/*     */   private static int toMillis(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 110 */     String[] arrayOfString = { "ms", "s" };
/*     */ 
/* 112 */     String str1 = null;
/* 113 */     String str2 = paramString;
/*     */ 
/* 115 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 116 */       int j = paramString.indexOf(arrayOfString[i]);
/* 117 */       if (j > 0) {
/* 118 */         str1 = paramString.substring(j);
/* 119 */         str2 = paramString.substring(0, j);
/* 120 */         break;
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 125 */       i = Integer.parseInt(str2);
/*     */ 
/* 127 */       if ((str1 == null) || (str1.compareTo("ms") == 0))
/* 128 */         return i;
/* 129 */       if (str1.compareTo("s") == 0) {
/* 130 */         return i * 1000;
/*     */       }
/* 132 */       throw new IllegalArgumentException("Unknow time unit: " + str1);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 136 */     throw new IllegalArgumentException("Could not convert interval: " + paramString);
/*     */   }
/*     */ 
/*     */   public Arguments(String[] paramArrayOfString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 142 */     int i = 0;
/*     */ 
/* 144 */     if (paramArrayOfString.length < 1) {
/* 145 */       throw new IllegalArgumentException("invalid argument count");
/*     */     }
/*     */ 
/* 148 */     if ((paramArrayOfString[0].compareTo("-?") == 0) || 
/* 149 */       (paramArrayOfString[0]
/* 149 */       .compareTo("-help") == 0))
/*     */     {
/* 150 */       this.help = true;
/* 151 */       return;
/* 152 */     }if (paramArrayOfString[0].compareTo("-options") == 0) {
/* 153 */       this.options = true;
/* 154 */       return;
/* 155 */     }if (paramArrayOfString[0].compareTo("-list") == 0) {
/* 156 */       this.list = true;
/* 157 */       if (paramArrayOfString.length > 2) {
/* 158 */         throw new IllegalArgumentException("invalid argument count");
/*     */       }
/*     */ 
/* 161 */       i++;
/*     */     }
/*     */     Object localObject;
/* 164 */     for (; (i < paramArrayOfString.length) && (paramArrayOfString[i].startsWith("-")); i++) {
/* 165 */       String str = paramArrayOfString[i];
/*     */ 
/* 167 */       if (str.compareTo("-a") == 0) {
/* 168 */         this.comparator = new AscendingMonitorComparator();
/* 169 */       } else if (str.compareTo("-d") == 0) {
/* 170 */         this.comparator = new DescendingMonitorComparator();
/* 171 */       } else if (str.compareTo("-t") == 0) {
/* 172 */         this.timestamp = true;
/* 173 */       } else if (str.compareTo("-v") == 0) {
/* 174 */         this.verbose = true;
/* 175 */       } else if ((str.compareTo("-constants") == 0) || 
/* 176 */         (str
/* 176 */         .compareTo("-c") == 0))
/*     */       {
/* 177 */         this.constants = true;
/* 178 */       } else if ((str.compareTo("-strings") == 0) || 
/* 179 */         (str
/* 179 */         .compareTo("-s") == 0))
/*     */       {
/* 180 */         this.strings = true;
/* 181 */       } else if (str.startsWith("-h"))
/*     */       {
/* 183 */         if (str.compareTo("-h") != 0) {
/* 184 */           localObject = str.substring(2);
/*     */         } else {
/* 186 */           i++;
/* 187 */           if (i >= paramArrayOfString.length) {
/* 188 */             throw new IllegalArgumentException("-h requires an integer argument");
/*     */           }
/*     */ 
/* 191 */           localObject = paramArrayOfString[i];
/*     */         }
/*     */         try {
/* 194 */           this.headerRate = Integer.parseInt((String)localObject);
/*     */         } catch (NumberFormatException localNumberFormatException2) {
/* 196 */           this.headerRate = -1;
/*     */         }
/* 198 */         if (this.headerRate < 0) {
/* 199 */           throw new IllegalArgumentException("illegal -h argument: " + (String)localObject);
/*     */         }
/*     */       }
/* 202 */       else if (str.startsWith("-name")) {
/* 203 */         if (str.startsWith("-name=")) {
/* 204 */           this.names = str.substring(7);
/*     */         } else {
/* 206 */           i++;
/* 207 */           if (i >= paramArrayOfString.length) {
/* 208 */             throw new IllegalArgumentException("option argument expected");
/*     */           }
/*     */ 
/* 211 */           this.names = paramArrayOfString[i];
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 225 */         localObject = null;
/* 226 */         int j = paramArrayOfString[i].indexOf('@');
/* 227 */         if (j < 0)
/* 228 */           localObject = paramArrayOfString[i];
/*     */         else {
/* 230 */           localObject = paramArrayOfString[i].substring(0, j);
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 235 */           int k = Integer.parseInt((String)localObject);
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException3)
/*     */         {
/* 241 */           if ((i == 0) && (paramArrayOfString[i].compareTo("-snap") == 0))
/* 242 */             this.snap = true;
/* 243 */           else if (i == 0)
/* 244 */             this.specialOption = paramArrayOfString[i].substring(1);
/*     */           else {
/* 246 */             throw new IllegalArgumentException("illegal argument: " + paramArrayOfString[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 254 */     if ((this.specialOption == null) && (!this.list) && (!this.snap) && (this.names == null)) {
/* 255 */       throw new IllegalArgumentException("-<option> required");
/*     */     }
/*     */ 
/* 258 */     switch (paramArrayOfString.length - i) {
/*     */     case 3:
/* 260 */       if (this.snap)
/* 261 */         throw new IllegalArgumentException("invalid argument count");
/*     */       try
/*     */       {
/* 264 */         this.count = Integer.parseInt(paramArrayOfString[(paramArrayOfString.length - 1)]);
/*     */       } catch (NumberFormatException localNumberFormatException1) {
/* 266 */         throw new IllegalArgumentException("illegal count value: " + paramArrayOfString[(paramArrayOfString.length - 1)]);
/*     */       }
/*     */ 
/* 269 */       this.interval = toMillis(paramArrayOfString[(paramArrayOfString.length - 2)]);
/* 270 */       this.vmIdString = paramArrayOfString[(paramArrayOfString.length - 3)];
/* 271 */       break;
/*     */     case 2:
/* 273 */       if (this.snap) {
/* 274 */         throw new IllegalArgumentException("invalid argument count");
/*     */       }
/* 276 */       this.interval = toMillis(paramArrayOfString[(paramArrayOfString.length - 1)]);
/* 277 */       this.vmIdString = paramArrayOfString[(paramArrayOfString.length - 2)];
/* 278 */       break;
/*     */     case 1:
/* 280 */       this.vmIdString = paramArrayOfString[(paramArrayOfString.length - 1)];
/* 281 */       break;
/*     */     case 0:
/* 283 */       if (!this.list) {
/* 284 */         throw new IllegalArgumentException("invalid argument count");
/*     */       }
/*     */       break;
/*     */     default:
/* 288 */       throw new IllegalArgumentException("invalid argument count");
/*     */     }
/*     */ 
/* 292 */     if ((this.count == -1) && (this.interval == -1))
/*     */     {
/* 294 */       this.count = 1;
/* 295 */       this.interval = 0;
/*     */     }
/*     */ 
/* 299 */     if (this.comparator == null) {
/* 300 */       this.comparator = new AscendingMonitorComparator();
/*     */     }
/*     */ 
/* 304 */     this.names = (this.names == null ? "\\w*" : this.names.replace(',', '|'));
/*     */     try
/*     */     {
/* 308 */       Pattern localPattern = Pattern.compile(this.names);
/*     */     }
/*     */     catch (PatternSyntaxException localPatternSyntaxException) {
/* 311 */       throw new IllegalArgumentException("Bad name pattern: " + localPatternSyntaxException
/* 311 */         .getMessage());
/*     */     }
/*     */ 
/* 315 */     if (this.specialOption != null) {
/* 316 */       OptionFinder localOptionFinder = new OptionFinder(optionsSources());
/* 317 */       this.optionFormat = localOptionFinder.getOptionFormat(this.specialOption, this.timestamp);
/* 318 */       if (this.optionFormat == null) {
/* 319 */         throw new IllegalArgumentException("Unknown option: -" + this.specialOption);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 326 */       this.vmId = new VmIdentifier(this.vmIdString);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 328 */       localObject = new IllegalArgumentException("Malformed VM Identifier: " + this.vmIdString);
/*     */ 
/* 330 */       ((IllegalArgumentException)localObject).initCause(localURISyntaxException);
/* 331 */       throw ((Throwable)localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Comparator<Monitor> comparator() {
/* 336 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   public boolean isHelp() {
/* 340 */     return this.help;
/*     */   }
/*     */ 
/*     */   public boolean isList() {
/* 344 */     return this.list;
/*     */   }
/*     */ 
/*     */   public boolean isSnap() {
/* 348 */     return this.snap;
/*     */   }
/*     */ 
/*     */   public boolean isOptions() {
/* 352 */     return this.options;
/*     */   }
/*     */ 
/*     */   public boolean isVerbose() {
/* 356 */     return this.verbose;
/*     */   }
/*     */ 
/*     */   public boolean printConstants() {
/* 360 */     return this.constants;
/*     */   }
/*     */ 
/*     */   public boolean isConstantsOnly() {
/* 364 */     return this.constantsOnly;
/*     */   }
/*     */ 
/*     */   public boolean printStrings() {
/* 368 */     return this.strings;
/*     */   }
/*     */ 
/*     */   public boolean showUnsupported() {
/* 372 */     return showUnsupported;
/*     */   }
/*     */ 
/*     */   public int headerRate() {
/* 376 */     return this.headerRate;
/*     */   }
/*     */ 
/*     */   public String counterNames() {
/* 380 */     return this.names;
/*     */   }
/*     */ 
/*     */   public VmIdentifier vmId() {
/* 384 */     return this.vmId;
/*     */   }
/*     */ 
/*     */   public String vmIdString() {
/* 388 */     return this.vmIdString;
/*     */   }
/*     */ 
/*     */   public int sampleInterval() {
/* 392 */     return this.interval;
/*     */   }
/*     */ 
/*     */   public int sampleCount() {
/* 396 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isTimestamp() {
/* 400 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public boolean isSpecialOption() {
/* 404 */     return this.specialOption != null;
/*     */   }
/*     */ 
/*     */   public String specialOption() {
/* 408 */     return this.specialOption;
/*     */   }
/*     */ 
/*     */   public OptionFormat optionFormat() {
/* 412 */     return this.optionFormat;
/*     */   }
/*     */ 
/*     */   public List<URL> optionsSources() {
/* 416 */     ArrayList localArrayList = new ArrayList();
/* 417 */     int i = 0;
/*     */ 
/* 419 */     String str1 = "jstat_options";
/*     */     try
/*     */     {
/* 422 */       String str2 = System.getProperty("user.home");
/* 423 */       String str3 = str2 + "/" + ".jvmstat";
/* 424 */       File localFile = new File(str3 + "/" + str1);
/* 425 */       localArrayList.add(localFile.toURI().toURL());
/*     */     } catch (Exception localException) {
/* 427 */       if (debug) {
/* 428 */         System.err.println(localException.getMessage());
/* 429 */         localException.printStackTrace();
/*     */       }
/*     */ 
/* 432 */       throw new IllegalArgumentException("Internal Error: Bad URL: " + localException
/* 432 */         .getMessage());
/*     */     }
/* 434 */     URL localURL = getClass().getResource("resources/" + str1);
/* 435 */     assert (localURL != null);
/* 436 */     localArrayList.add(localURL);
/*     */ 
/* 438 */     if (showUnsupported) {
/* 439 */       localURL = getClass().getResource("resources/jstat_unsupported_options");
/* 440 */       assert (localURL != null);
/* 441 */       localArrayList.add(localURL);
/*     */     }
/* 443 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Arguments
 * JD-Core Version:    0.6.2
 */