/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.PathSearchingVirtualMachine;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.VirtualMachineManager;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.Connector.Argument;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.ListeningConnector;
/*     */ import com.sun.jdi.connect.VMStartException;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.ThreadDeathRequest;
/*     */ import com.sun.jdi.request.ThreadStartRequest;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ class VMConnection
/*     */ {
/*     */   private VirtualMachine vm;
/*  50 */   private Process process = null;
/*  51 */   private int outputCompleteCount = 0;
/*     */   private final Connector connector;
/*     */   private final Map<String, Connector.Argument> connectorArgs;
/*     */   private final int traceFlags;
/*     */ 
/*     */   synchronized void notifyOutputComplete()
/*     */   {
/*  58 */     this.outputCompleteCount += 1;
/*  59 */     notifyAll();
/*     */   }
/*     */ 
/*     */   synchronized void waitOutputComplete()
/*     */   {
/*  64 */     if (this.process != null)
/*  65 */       while (this.outputCompleteCount < 2) try {
/*  66 */           wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         } 
/*     */   }
/*     */ 
/*  73 */   private Connector findConnector(String paramString) { for (Connector localConnector : Bootstrap.virtualMachineManager().allConnectors()) {
/*  74 */       if (localConnector.name().equals(paramString)) {
/*  75 */         return localConnector;
/*     */       }
/*     */     }
/*  78 */     return null; }
/*     */ 
/*     */   private Map<String, Connector.Argument> parseConnectorArgs(Connector paramConnector, String paramString)
/*     */   {
/*  82 */     Map localMap = paramConnector.defaultArguments();
/*     */ 
/*  91 */     String str1 = "(quote=[^,]+,)|(\\w+=)(((\"[^\"]*\")|('[^']*')|([^,'\"]+))+,)";
/*     */ 
/*  97 */     Pattern localPattern = Pattern.compile(str1);
/*  98 */     Matcher localMatcher = localPattern.matcher(paramString);
/*  99 */     while (localMatcher.find()) {
/* 100 */       int i = localMatcher.start();
/* 101 */       int j = localMatcher.end();
/* 102 */       if (i > 0)
/*     */       {
/* 107 */         throw new IllegalArgumentException(
/* 107 */           MessageOutput.format("Illegal connector argument", paramString));
/*     */       }
/*     */ 
/* 111 */       String str2 = paramString.substring(i, j);
/* 112 */       int k = str2.indexOf('=');
/* 113 */       String str3 = str2.substring(0, k);
/* 114 */       String str4 = str2.substring(k + 1, str2
/* 115 */         .length() - 1);
/*     */ 
/* 122 */       if (str3.equals("options")) {
/* 123 */         localObject = new StringBuilder();
/* 124 */         for (String str5 : splitStringAtNonEnclosedWhiteSpace(str4)) {
/* 125 */           while ((isEnclosed(str5, "\"")) || (isEnclosed(str5, "'"))) {
/* 126 */             str5 = str5.substring(1, str5.length() - 1);
/*     */           }
/* 128 */           ((StringBuilder)localObject).append(str5);
/* 129 */           ((StringBuilder)localObject).append(" ");
/*     */         }
/* 131 */         str4 = ((StringBuilder)localObject).toString();
/*     */       }
/*     */ 
/* 134 */       Object localObject = (Connector.Argument)localMap.get(str3);
/* 135 */       if (localObject == null)
/*     */       {
/* 137 */         throw new IllegalArgumentException(
/* 137 */           MessageOutput.format("Argument is not defined for connector:", new Object[] { str3, paramConnector
/* 138 */           .name() }));
/*     */       }
/* 140 */       ((Connector.Argument)localObject).setValue(str4);
/*     */ 
/* 142 */       paramString = paramString.substring(j);
/* 143 */       localMatcher = localPattern.matcher(paramString);
/*     */     }
/* 145 */     if ((!paramString.equals(",")) && (paramString.length() > 0))
/*     */     {
/* 151 */       throw new IllegalArgumentException(
/* 151 */         MessageOutput.format("Illegal connector argument", paramString));
/*     */     }
/*     */ 
/* 153 */     return localMap;
/*     */   }
/*     */ 
/*     */   private static boolean isEnclosed(String paramString1, String paramString2) {
/* 157 */     if (paramString1.indexOf(paramString2) == 0) {
/* 158 */       int i = paramString1.lastIndexOf(paramString2);
/* 159 */       if ((i > 0) && (i == paramString1.length() - 1)) {
/* 160 */         return true;
/*     */       }
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   private static List<String> splitStringAtNonEnclosedWhiteSpace(String paramString) throws IllegalArgumentException {
/* 167 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 169 */     int i = 0;
/* 170 */     int j = 0;
/*     */ 
/* 184 */     int k = 32;
/*     */ 
/* 186 */     if (paramString == null)
/*     */     {
/* 188 */       throw new IllegalArgumentException(
/* 188 */         MessageOutput.format("value string is null"));
/*     */     }
/*     */ 
/* 192 */     char[] arrayOfChar = paramString.toCharArray();
/*     */ 
/* 194 */     for (int m = 0; m < arrayOfChar.length; m++) {
/* 195 */       switch (arrayOfChar[m])
/*     */       {
/*     */       case ' ':
/* 199 */         if (!isLastChar(arrayOfChar, m)) continue;
/* 200 */         j = m;
/*     */ 
/* 202 */         break;
/*     */       case '"':
/*     */       case '\'':
/* 208 */         if (k == arrayOfChar[m])
/*     */         {
/* 210 */           if (isNextCharWhitespace(arrayOfChar, m))
/*     */           {
/* 213 */             j = m;
/*     */ 
/* 215 */             k = 32;
/*     */ 
/* 217 */             break;
/*     */           }
/*     */         }
/* 220 */         if (k != 32) {
/*     */           continue;
/*     */         }
/* 223 */         if (!isPreviousCharWhitespace(arrayOfChar, m)) continue;
/* 224 */         i = m;
/*     */ 
/* 226 */         if (paramString.indexOf(arrayOfChar[m], m + 1) >= 0)
/*     */         {
/* 229 */           k = arrayOfChar[m]; continue;
/*     */         }
/*     */ 
/* 233 */         if (!isNextCharWhitespace(arrayOfChar, m)) continue;
/* 234 */         j = m;
/*     */ 
/* 236 */         break;
/*     */       default:
/* 245 */         if (k != 32)
/*     */           continue;
/* 247 */         if (isPreviousCharWhitespace(arrayOfChar, m))
/*     */         {
/* 249 */           i = m;
/*     */         }
/* 251 */         if (!isNextCharWhitespace(arrayOfChar, m))
/*     */           continue;
/* 253 */         j = m;
/*     */       }
/*     */ 
/* 263 */       if (i > j)
/*     */       {
/* 265 */         throw new IllegalArgumentException(
/* 265 */           MessageOutput.format("Illegal option values"));
/*     */       }
/*     */ 
/* 269 */       localArrayList.add(paramString.substring(i, ++j));
/*     */ 
/* 272 */       m = i = j;
/*     */     }
/*     */ 
/* 276 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static boolean isPreviousCharWhitespace(char[] paramArrayOfChar, int paramInt) {
/* 280 */     return isCharWhitespace(paramArrayOfChar, paramInt - 1);
/*     */   }
/*     */ 
/*     */   private static boolean isNextCharWhitespace(char[] paramArrayOfChar, int paramInt) {
/* 284 */     return isCharWhitespace(paramArrayOfChar, paramInt + 1);
/*     */   }
/*     */ 
/*     */   private static boolean isCharWhitespace(char[] paramArrayOfChar, int paramInt) {
/* 288 */     if ((paramInt < 0) || (paramInt >= paramArrayOfChar.length))
/*     */     {
/* 290 */       return true;
/*     */     }
/* 292 */     if (paramArrayOfChar[paramInt] == ' ') {
/* 293 */       return true;
/*     */     }
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isLastChar(char[] paramArrayOfChar, int paramInt) {
/* 299 */     return paramInt + 1 == paramArrayOfChar.length;
/*     */   }
/*     */ 
/*     */   VMConnection(String paramString, int paramInt)
/*     */   {
/* 305 */     int i = paramString.indexOf(':');
/*     */     String str1;
/*     */     String str2;
/* 306 */     if (i == -1) {
/* 307 */       str1 = paramString;
/* 308 */       str2 = "";
/*     */     } else {
/* 310 */       str1 = paramString.substring(0, i);
/* 311 */       str2 = paramString.substring(i + 1);
/*     */     }
/*     */ 
/* 314 */     this.connector = findConnector(str1);
/* 315 */     if (this.connector == null)
/*     */     {
/* 317 */       throw new IllegalArgumentException(
/* 317 */         MessageOutput.format("No connector named:", str1));
/*     */     }
/*     */ 
/* 320 */     this.connectorArgs = parseConnectorArgs(this.connector, str2);
/* 321 */     this.traceFlags = paramInt;
/*     */   }
/*     */ 
/*     */   synchronized VirtualMachine open() {
/* 325 */     if ((this.connector instanceof LaunchingConnector))
/* 326 */       this.vm = launchTarget();
/* 327 */     else if ((this.connector instanceof AttachingConnector))
/* 328 */       this.vm = attachTarget();
/* 329 */     else if ((this.connector instanceof ListeningConnector)) {
/* 330 */       this.vm = listenTarget();
/*     */     }
/*     */     else {
/* 333 */       throw new InternalError(
/* 333 */         MessageOutput.format("Invalid connect type"));
/*     */     }
/*     */ 
/* 335 */     this.vm.setDebugTraceMode(this.traceFlags);
/* 336 */     if (this.vm.canBeModified()) {
/* 337 */       setEventRequests(this.vm);
/* 338 */       resolveEventRequests();
/*     */     }
/*     */ 
/* 346 */     if (Env.getSourcePath().length() == 0) {
/* 347 */       if ((this.vm instanceof PathSearchingVirtualMachine)) {
/* 348 */         PathSearchingVirtualMachine localPathSearchingVirtualMachine = (PathSearchingVirtualMachine)this.vm;
/*     */ 
/* 350 */         Env.setSourcePath(localPathSearchingVirtualMachine.classPath());
/*     */       } else {
/* 352 */         Env.setSourcePath(".");
/*     */       }
/*     */     }
/*     */ 
/* 356 */     return this.vm;
/*     */   }
/*     */ 
/*     */   boolean setConnectorArg(String paramString1, String paramString2)
/*     */   {
/* 363 */     if (this.vm != null) {
/* 364 */       return false;
/*     */     }
/*     */ 
/* 367 */     Connector.Argument localArgument = (Connector.Argument)this.connectorArgs.get(paramString1);
/* 368 */     if (localArgument == null) {
/* 369 */       return false;
/*     */     }
/* 371 */     localArgument.setValue(paramString2);
/* 372 */     return true;
/*     */   }
/*     */ 
/*     */   String connectorArg(String paramString) {
/* 376 */     Connector.Argument localArgument = (Connector.Argument)this.connectorArgs.get(paramString);
/* 377 */     if (localArgument == null) {
/* 378 */       return "";
/*     */     }
/* 380 */     return localArgument.value();
/*     */   }
/*     */ 
/*     */   public synchronized VirtualMachine vm() {
/* 384 */     if (this.vm == null) {
/* 385 */       throw new VMNotConnectedException();
/*     */     }
/* 387 */     return this.vm;
/*     */   }
/*     */ 
/*     */   boolean isOpen()
/*     */   {
/* 392 */     return this.vm != null;
/*     */   }
/*     */ 
/*     */   boolean isLaunch() {
/* 396 */     return this.connector instanceof LaunchingConnector;
/*     */   }
/*     */ 
/*     */   public void disposeVM() {
/*     */     try {
/* 401 */       if (this.vm != null) {
/* 402 */         this.vm.dispose();
/* 403 */         this.vm = null;
/*     */       }
/*     */ 
/* 406 */       if (this.process != null) {
/* 407 */         this.process.destroy();
/* 408 */         this.process = null;
/*     */       }
/* 410 */       waitOutputComplete();
/*     */     }
/*     */     finally
/*     */     {
/* 406 */       if (this.process != null) {
/* 407 */         this.process.destroy();
/* 408 */         this.process = null;
/*     */       }
/* 410 */       waitOutputComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setEventRequests(VirtualMachine paramVirtualMachine) {
/* 415 */     EventRequestManager localEventRequestManager = paramVirtualMachine.eventRequestManager();
/*     */ 
/* 425 */     Commands localCommands = new Commands();
/* 426 */     localCommands
/* 427 */       .commandCatchException(new StringTokenizer("uncaught java.lang.Throwable"));
/*     */ 
/* 429 */     ThreadStartRequest localThreadStartRequest = localEventRequestManager.createThreadStartRequest();
/* 430 */     localThreadStartRequest.enable();
/* 431 */     ThreadDeathRequest localThreadDeathRequest = localEventRequestManager.createThreadDeathRequest();
/* 432 */     localThreadDeathRequest.enable();
/*     */   }
/*     */ 
/*     */   private void resolveEventRequests() {
/* 436 */     Env.specList.resolveAll();
/*     */   }
/*     */ 
/*     */   private void dumpStream(InputStream paramInputStream) throws IOException {
/* 440 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/*     */     try
/*     */     {
/*     */       int i;
/* 444 */       while ((i = localBufferedReader.read()) != -1)
/* 445 */         MessageOutput.printDirect((char)i);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 449 */       String str = localIOException.getMessage();
/* 450 */       if (!str.startsWith("Bad file number"))
/* 451 */         throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void displayRemoteOutput(final InputStream paramInputStream)
/*     */   {
/* 465 */     Thread local1 = new Thread("output reader")
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 469 */           VMConnection.this.dumpStream(paramInputStream);
/*     */         } catch (IOException localIOException) {
/* 471 */           MessageOutput.fatalError("Failed reading output");
/*     */         } finally {
/* 473 */           VMConnection.this.notifyOutputComplete();
/*     */         }
/*     */       }
/*     */     };
/* 477 */     local1.setPriority(9);
/* 478 */     local1.start();
/*     */   }
/*     */ 
/*     */   private void dumpFailedLaunchInfo(Process paramProcess) {
/*     */     try {
/* 483 */       dumpStream(paramProcess.getErrorStream());
/* 484 */       dumpStream(paramProcess.getInputStream());
/*     */     } catch (IOException localIOException) {
/* 486 */       MessageOutput.println("Unable to display process output:", localIOException
/* 487 */         .getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private VirtualMachine launchTarget()
/*     */   {
/* 493 */     LaunchingConnector localLaunchingConnector = (LaunchingConnector)this.connector;
/*     */     try {
/* 495 */       VirtualMachine localVirtualMachine = localLaunchingConnector.launch(this.connectorArgs);
/* 496 */       this.process = localVirtualMachine.process();
/* 497 */       displayRemoteOutput(this.process.getErrorStream());
/* 498 */       displayRemoteOutput(this.process.getInputStream());
/* 499 */       return localVirtualMachine;
/*     */     } catch (IOException localIOException) {
/* 501 */       localIOException.printStackTrace();
/* 502 */       MessageOutput.fatalError("Unable to launch target VM.");
/*     */     } catch (IllegalConnectorArgumentsException localIllegalConnectorArgumentsException) {
/* 504 */       localIllegalConnectorArgumentsException.printStackTrace();
/* 505 */       MessageOutput.fatalError("Internal debugger error.");
/*     */     } catch (VMStartException localVMStartException) {
/* 507 */       MessageOutput.println("vmstartexception", localVMStartException.getMessage());
/* 508 */       MessageOutput.println();
/* 509 */       dumpFailedLaunchInfo(localVMStartException.process());
/* 510 */       MessageOutput.fatalError("Target VM failed to initialize.");
/*     */     }
/* 512 */     return null;
/*     */   }
/*     */ 
/*     */   private VirtualMachine attachTarget()
/*     */   {
/* 517 */     AttachingConnector localAttachingConnector = (AttachingConnector)this.connector;
/*     */     try {
/* 519 */       return localAttachingConnector.attach(this.connectorArgs);
/*     */     } catch (IOException localIOException) {
/* 521 */       localIOException.printStackTrace();
/* 522 */       MessageOutput.fatalError("Unable to attach to target VM.");
/*     */     } catch (IllegalConnectorArgumentsException localIllegalConnectorArgumentsException) {
/* 524 */       localIllegalConnectorArgumentsException.printStackTrace();
/* 525 */       MessageOutput.fatalError("Internal debugger error.");
/*     */     }
/* 527 */     return null;
/*     */   }
/*     */ 
/*     */   private VirtualMachine listenTarget()
/*     */   {
/* 532 */     ListeningConnector localListeningConnector = (ListeningConnector)this.connector;
/*     */     try {
/* 534 */       String str = localListeningConnector.startListening(this.connectorArgs);
/* 535 */       MessageOutput.println("Listening at address:", str);
/* 536 */       this.vm = localListeningConnector.accept(this.connectorArgs);
/* 537 */       localListeningConnector.stopListening(this.connectorArgs);
/* 538 */       return this.vm;
/*     */     } catch (IOException localIOException) {
/* 540 */       localIOException.printStackTrace();
/* 541 */       MessageOutput.fatalError("Unable to attach to target VM.");
/*     */     } catch (IllegalConnectorArgumentsException localIllegalConnectorArgumentsException) {
/* 543 */       localIllegalConnectorArgumentsException.printStackTrace();
/* 544 */       MessageOutput.fatalError("Internal debugger error.");
/*     */     }
/* 546 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.VMConnection
 * JD-Core Version:    0.6.2
 */