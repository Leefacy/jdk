/*      */ package com.sun.tools.example.debug.tty;
/*      */ 
/*      */ import com.sun.jdi.Bootstrap;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.IncompatibleThreadStateException;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.StackFrame;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.VMCannotBeModifiedException;
/*      */ import com.sun.jdi.VMDisconnectedException;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import com.sun.jdi.VirtualMachineManager;
/*      */ import com.sun.jdi.connect.Connector;
/*      */ import com.sun.jdi.connect.Transport;
/*      */ import com.sun.jdi.event.BreakpointEvent;
/*      */ import com.sun.jdi.event.ClassPrepareEvent;
/*      */ import com.sun.jdi.event.ClassUnloadEvent;
/*      */ import com.sun.jdi.event.Event;
/*      */ import com.sun.jdi.event.ExceptionEvent;
/*      */ import com.sun.jdi.event.LocatableEvent;
/*      */ import com.sun.jdi.event.MethodEntryEvent;
/*      */ import com.sun.jdi.event.MethodExitEvent;
/*      */ import com.sun.jdi.event.ModificationWatchpointEvent;
/*      */ import com.sun.jdi.event.StepEvent;
/*      */ import com.sun.jdi.event.ThreadDeathEvent;
/*      */ import com.sun.jdi.event.ThreadStartEvent;
/*      */ import com.sun.jdi.event.VMDeathEvent;
/*      */ import com.sun.jdi.event.VMDisconnectEvent;
/*      */ import com.sun.jdi.event.VMStartEvent;
/*      */ import com.sun.jdi.event.WatchpointEvent;
/*      */ import com.sun.jdi.request.EventRequest;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ public class TTY
/*      */   implements EventNotifier
/*      */ {
/*   46 */   EventHandler handler = null;
/*      */ 
/*   51 */   private List<String> monitorCommands = new ArrayList();
/*   52 */   private int monitorCount = 0;
/*      */   private static final String progname = "jdb";
/*  260 */   private static final String[][] commandList = { { "!!", "n", "y" }, { "?", "y", "y" }, { "bytecodes", "n", "y" }, { "catch", "y", "n" }, { "class", "n", "y" }, { "classes", "n", "y" }, { "classpath", "n", "y" }, { "clear", "y", "n" }, { "connectors", "y", "y" }, { "cont", "n", "n" }, { "disablegc", "n", "n" }, { "down", "n", "y" }, { "dump", "n", "y" }, { "enablegc", "n", "n" }, { "eval", "n", "y" }, { "exclude", "y", "n" }, { "exit", "y", "y" }, { "extension", "n", "y" }, { "fields", "n", "y" }, { "gc", "n", "n" }, { "help", "y", "y" }, { "ignore", "y", "n" }, { "interrupt", "n", "n" }, { "kill", "n", "n" }, { "lines", "n", "y" }, { "list", "n", "y" }, { "load", "n", "y" }, { "locals", "n", "y" }, { "lock", "n", "n" }, { "memory", "n", "y" }, { "methods", "n", "y" }, { "monitor", "n", "n" }, { "next", "n", "n" }, { "pop", "n", "n" }, { "print", "n", "y" }, { "quit", "y", "y" }, { "read", "y", "y" }, { "redefine", "n", "n" }, { "reenter", "n", "n" }, { "resume", "n", "n" }, { "run", "y", "n" }, { "save", "n", "n" }, { "set", "n", "n" }, { "sourcepath", "y", "y" }, { "step", "n", "n" }, { "stepi", "n", "n" }, { "stop", "y", "n" }, { "suspend", "n", "n" }, { "thread", "n", "y" }, { "threadgroup", "n", "y" }, { "threadgroups", "n", "y" }, { "threadlocks", "n", "y" }, { "threads", "n", "y" }, { "trace", "n", "n" }, { "unmonitor", "n", "n" }, { "untrace", "n", "n" }, { "unwatch", "y", "n" }, { "up", "n", "y" }, { "use", "y", "y" }, { "version", "y", "y" }, { "watch", "y", "n" }, { "where", "n", "y" }, { "wherei", "n", "y" } };
/*      */ 
/*      */   public void vmStartEvent(VMStartEvent paramVMStartEvent)
/*      */   {
/*   61 */     Thread.yield();
/*   62 */     MessageOutput.lnprint("VM Started:");
/*      */   }
/*      */ 
/*      */   public void vmDeathEvent(VMDeathEvent paramVMDeathEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void vmDisconnectEvent(VMDisconnectEvent paramVMDisconnectEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void threadStartEvent(ThreadStartEvent paramThreadStartEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void threadDeathEvent(ThreadDeathEvent paramThreadDeathEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void classPrepareEvent(ClassPrepareEvent paramClassPrepareEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void classUnloadEvent(ClassUnloadEvent paramClassUnloadEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void breakpointEvent(BreakpointEvent paramBreakpointEvent)
/*      */   {
/*   91 */     Thread.yield();
/*   92 */     MessageOutput.lnprint("Breakpoint hit:");
/*      */   }
/*      */ 
/*      */   public void fieldWatchEvent(WatchpointEvent paramWatchpointEvent)
/*      */   {
/*   97 */     Field localField = paramWatchpointEvent.field();
/*   98 */     ObjectReference localObjectReference = paramWatchpointEvent.object();
/*   99 */     Thread.yield();
/*      */ 
/*  101 */     if ((paramWatchpointEvent instanceof ModificationWatchpointEvent))
/*  102 */       MessageOutput.lnprint("Field access encountered before after", new Object[] { localField, paramWatchpointEvent
/*  104 */         .valueCurrent(), ((ModificationWatchpointEvent)paramWatchpointEvent)
/*  105 */         .valueToBe() });
/*      */     else
/*  107 */       MessageOutput.lnprint("Field access encountered", localField.toString());
/*      */   }
/*      */ 
/*      */   public void stepEvent(StepEvent paramStepEvent)
/*      */   {
/*  113 */     Thread.yield();
/*  114 */     MessageOutput.lnprint("Step completed:");
/*      */   }
/*      */ 
/*      */   public void exceptionEvent(ExceptionEvent paramExceptionEvent)
/*      */   {
/*  119 */     Thread.yield();
/*  120 */     Location localLocation = paramExceptionEvent.catchLocation();
/*  121 */     if (localLocation == null)
/*  122 */       MessageOutput.lnprint("Exception occurred uncaught", paramExceptionEvent
/*  123 */         .exception().referenceType().name());
/*      */     else
/*  125 */       MessageOutput.lnprint("Exception occurred caught", new Object[] { paramExceptionEvent
/*  126 */         .exception().referenceType().name(), 
/*  127 */         Commands.locationString(localLocation) });
/*      */   }
/*      */ 
/*      */   public void methodEntryEvent(MethodEntryEvent paramMethodEntryEvent)
/*      */   {
/*  133 */     Thread.yield();
/*      */ 
/*  139 */     if (paramMethodEntryEvent.request().suspendPolicy() != 0)
/*      */     {
/*  141 */       MessageOutput.lnprint("Method entered:");
/*      */     }
/*      */     else {
/*  144 */       MessageOutput.print("Method entered:");
/*  145 */       printLocationOfEvent(paramMethodEntryEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean methodExitEvent(MethodExitEvent paramMethodExitEvent)
/*      */   {
/*  151 */     Thread.yield();
/*      */ 
/*  155 */     Method localMethod1 = Env.atExitMethod();
/*  156 */     Method localMethod2 = paramMethodExitEvent.method();
/*      */ 
/*  158 */     if ((localMethod1 == null) || (localMethod1.equals(localMethod2)))
/*      */     {
/*  162 */       if (paramMethodExitEvent.request().suspendPolicy() != 0)
/*      */       {
/*  164 */         MessageOutput.println();
/*      */       }
/*  166 */       if (Env.vm().canGetMethodReturnValues())
/*  167 */         MessageOutput.print("Method exitedValue:", paramMethodExitEvent.returnValue() + "");
/*      */       else {
/*  169 */         MessageOutput.print("Method exited:");
/*      */       }
/*      */ 
/*  172 */       if (paramMethodExitEvent.request().suspendPolicy() == 0)
/*      */       {
/*  174 */         printLocationOfEvent(paramMethodExitEvent);
/*      */       }
/*      */ 
/*  191 */       return true;
/*      */     }
/*      */ 
/*  195 */     return false;
/*      */   }
/*      */ 
/*      */   public void vmInterrupted()
/*      */   {
/*  200 */     Thread.yield();
/*  201 */     printCurrentLocation();
/*  202 */     for (String str : this.monitorCommands) {
/*  203 */       StringTokenizer localStringTokenizer = new StringTokenizer(str);
/*  204 */       localStringTokenizer.nextToken();
/*  205 */       executeCommand(localStringTokenizer);
/*      */     }
/*  207 */     MessageOutput.printPrompt();
/*      */   }
/*      */ 
/*      */   public void receivedEvent(Event paramEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void printBaseLocation(String paramString, Location paramLocation) {
/*  215 */     MessageOutput.println("location", new Object[] { paramString, 
/*  217 */       Commands.locationString(paramLocation) });
/*      */   }
/*      */ 
/*      */   private void printCurrentLocation()
/*      */   {
/*  221 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*      */     StackFrame localStackFrame;
/*      */     try {
/*  224 */       localStackFrame = localThreadInfo.getCurrentFrame();
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  226 */       MessageOutput.println("<location unavailable>");
/*  227 */       return;
/*      */     }
/*  229 */     if (localStackFrame == null) {
/*  230 */       MessageOutput.println("No frames on the current call stack");
/*      */     } else {
/*  232 */       Location localLocation = localStackFrame.location();
/*  233 */       printBaseLocation(localThreadInfo.getThread().name(), localLocation);
/*      */ 
/*  235 */       if (localLocation.lineNumber() != -1) {
/*      */         String str;
/*      */         try {
/*  238 */           str = Env.sourceLine(localLocation, localLocation.lineNumber());
/*      */         } catch (IOException localIOException) {
/*  240 */           str = null;
/*      */         }
/*  242 */         if (str != null) {
/*  243 */           MessageOutput.println("source line number and line", new Object[] { new Integer(localLocation
/*  244 */             .lineNumber()), str });
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  249 */     MessageOutput.println();
/*      */   }
/*      */ 
/*      */   private void printLocationOfEvent(LocatableEvent paramLocatableEvent) {
/*  253 */     printBaseLocation(paramLocatableEvent.thread().name(), paramLocatableEvent.location());
/*      */   }
/*      */ 
/*      */   void help() {
/*  257 */     MessageOutput.println("zz help text");
/*      */   }
/*      */ 
/*      */   private int isCommand(String paramString)
/*      */   {
/*  342 */     int i = 0;
/*  343 */     int j = commandList.length - 1;
/*  344 */     while (i <= j) {
/*  345 */       int k = i + j >>> 1;
/*  346 */       String str = commandList[k][0];
/*  347 */       int m = str.compareTo(paramString);
/*  348 */       if (m < 0)
/*  349 */         i = k + 1;
/*  350 */       else if (m > 0) {
/*  351 */         j = k - 1;
/*      */       }
/*      */       else {
/*  354 */         return k;
/*      */       }
/*      */     }
/*  357 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   private boolean isDisconnectCmd(int paramInt)
/*      */   {
/*  364 */     if ((paramInt < 0) || (paramInt >= commandList.length)) {
/*  365 */       return false;
/*      */     }
/*  367 */     return commandList[paramInt][1].equals("y");
/*      */   }
/*      */ 
/*      */   private boolean isReadOnlyCmd(int paramInt)
/*      */   {
/*  374 */     if ((paramInt < 0) || (paramInt >= commandList.length)) {
/*  375 */       return false;
/*      */     }
/*  377 */     return commandList[paramInt][2].equals("y");
/*      */   }
/*      */ 
/*      */   void executeCommand(StringTokenizer paramStringTokenizer)
/*      */   {
/*  382 */     String str = paramStringTokenizer.nextToken().toLowerCase();
/*      */ 
/*  384 */     int i = 1;
/*      */ 
/*  390 */     if (!str.startsWith("#"))
/*      */     {
/*      */       Object localObject;
/*  395 */       if ((Character.isDigit(str.charAt(0))) && (paramStringTokenizer.hasMoreTokens())) {
/*      */         try {
/*  397 */           int j = Integer.parseInt(str);
/*  398 */           localObject = paramStringTokenizer.nextToken("");
/*  399 */           while (j-- > 0) {
/*  400 */             executeCommand(new StringTokenizer((String)localObject));
/*  401 */             i = 0;
/*      */           }
/*      */         } catch (NumberFormatException localNumberFormatException) {
/*  404 */           MessageOutput.println("Unrecognized command.  Try help...", str);
/*      */         }
/*      */       } else {
/*  407 */         int k = isCommand(str);
/*      */ 
/*  411 */         if (k < 0) {
/*  412 */           MessageOutput.println("Unrecognized command.  Try help...", str);
/*  413 */         } else if ((!Env.connection().isOpen()) && (!isDisconnectCmd(k))) {
/*  414 */           MessageOutput.println("Command not valid until the VM is started with the run command", str);
/*      */         }
/*  416 */         else if ((Env.connection().isOpen()) && (!Env.vm().canBeModified()) && 
/*  417 */           (!isReadOnlyCmd(k)))
/*      */         {
/*  418 */           MessageOutput.println("Command is not supported on a read-only VM connection", str);
/*      */         }
/*      */         else
/*      */         {
/*  422 */           localObject = new Commands();
/*      */           try {
/*  424 */             if (str.equals("print")) {
/*  425 */               ((Commands)localObject).commandPrint(paramStringTokenizer, false);
/*  426 */               i = 0;
/*  427 */             } else if (str.equals("eval")) {
/*  428 */               ((Commands)localObject).commandPrint(paramStringTokenizer, false);
/*  429 */               i = 0;
/*  430 */             } else if (str.equals("set")) {
/*  431 */               ((Commands)localObject).commandSet(paramStringTokenizer);
/*  432 */               i = 0;
/*  433 */             } else if (str.equals("dump")) {
/*  434 */               ((Commands)localObject).commandPrint(paramStringTokenizer, true);
/*  435 */               i = 0;
/*  436 */             } else if (str.equals("locals")) {
/*  437 */               ((Commands)localObject).commandLocals();
/*  438 */             } else if (str.equals("classes")) {
/*  439 */               ((Commands)localObject).commandClasses();
/*  440 */             } else if (str.equals("class")) {
/*  441 */               ((Commands)localObject).commandClass(paramStringTokenizer);
/*  442 */             } else if (str.equals("connectors")) {
/*  443 */               ((Commands)localObject).commandConnectors(Bootstrap.virtualMachineManager());
/*  444 */             } else if (str.equals("methods")) {
/*  445 */               ((Commands)localObject).commandMethods(paramStringTokenizer);
/*  446 */             } else if (str.equals("fields")) {
/*  447 */               ((Commands)localObject).commandFields(paramStringTokenizer);
/*  448 */             } else if (str.equals("threads")) {
/*  449 */               ((Commands)localObject).commandThreads(paramStringTokenizer);
/*  450 */             } else if (str.equals("thread")) {
/*  451 */               ((Commands)localObject).commandThread(paramStringTokenizer);
/*  452 */             } else if (str.equals("suspend")) {
/*  453 */               ((Commands)localObject).commandSuspend(paramStringTokenizer);
/*  454 */             } else if (str.equals("resume")) {
/*  455 */               ((Commands)localObject).commandResume(paramStringTokenizer);
/*  456 */             } else if (str.equals("cont")) {
/*  457 */               ((Commands)localObject).commandCont();
/*  458 */             } else if (str.equals("threadgroups")) {
/*  459 */               ((Commands)localObject).commandThreadGroups();
/*  460 */             } else if (str.equals("threadgroup")) {
/*  461 */               ((Commands)localObject).commandThreadGroup(paramStringTokenizer);
/*  462 */             } else if (str.equals("catch")) {
/*  463 */               ((Commands)localObject).commandCatchException(paramStringTokenizer);
/*  464 */             } else if (str.equals("ignore")) {
/*  465 */               ((Commands)localObject).commandIgnoreException(paramStringTokenizer);
/*  466 */             } else if (str.equals("step")) {
/*  467 */               ((Commands)localObject).commandStep(paramStringTokenizer);
/*  468 */             } else if (str.equals("stepi")) {
/*  469 */               ((Commands)localObject).commandStepi();
/*  470 */             } else if (str.equals("next")) {
/*  471 */               ((Commands)localObject).commandNext();
/*  472 */             } else if (str.equals("kill")) {
/*  473 */               ((Commands)localObject).commandKill(paramStringTokenizer);
/*  474 */             } else if (str.equals("interrupt")) {
/*  475 */               ((Commands)localObject).commandInterrupt(paramStringTokenizer);
/*  476 */             } else if (str.equals("trace")) {
/*  477 */               ((Commands)localObject).commandTrace(paramStringTokenizer);
/*  478 */             } else if (str.equals("untrace")) {
/*  479 */               ((Commands)localObject).commandUntrace(paramStringTokenizer);
/*  480 */             } else if (str.equals("where")) {
/*  481 */               ((Commands)localObject).commandWhere(paramStringTokenizer, false);
/*  482 */             } else if (str.equals("wherei")) {
/*  483 */               ((Commands)localObject).commandWhere(paramStringTokenizer, true);
/*  484 */             } else if (str.equals("up")) {
/*  485 */               ((Commands)localObject).commandUp(paramStringTokenizer);
/*  486 */             } else if (str.equals("down")) {
/*  487 */               ((Commands)localObject).commandDown(paramStringTokenizer);
/*  488 */             } else if (str.equals("load")) {
/*  489 */               ((Commands)localObject).commandLoad(paramStringTokenizer);
/*  490 */             } else if (str.equals("run")) {
/*  491 */               ((Commands)localObject).commandRun(paramStringTokenizer);
/*      */ 
/*  498 */               if ((this.handler == null) && (Env.connection().isOpen()))
/*  499 */                 this.handler = new EventHandler(this, false);
/*      */             }
/*  501 */             else if (str.equals("memory")) {
/*  502 */               ((Commands)localObject).commandMemory();
/*  503 */             } else if (str.equals("gc")) {
/*  504 */               ((Commands)localObject).commandGC();
/*  505 */             } else if (str.equals("stop")) {
/*  506 */               ((Commands)localObject).commandStop(paramStringTokenizer);
/*  507 */             } else if (str.equals("clear")) {
/*  508 */               ((Commands)localObject).commandClear(paramStringTokenizer);
/*  509 */             } else if (str.equals("watch")) {
/*  510 */               ((Commands)localObject).commandWatch(paramStringTokenizer);
/*  511 */             } else if (str.equals("unwatch")) {
/*  512 */               ((Commands)localObject).commandUnwatch(paramStringTokenizer);
/*  513 */             } else if (str.equals("list")) {
/*  514 */               ((Commands)localObject).commandList(paramStringTokenizer);
/*  515 */             } else if (str.equals("lines")) {
/*  516 */               ((Commands)localObject).commandLines(paramStringTokenizer);
/*  517 */             } else if (str.equals("classpath")) {
/*  518 */               ((Commands)localObject).commandClasspath(paramStringTokenizer);
/*  519 */             } else if ((str.equals("use")) || (str.equals("sourcepath"))) {
/*  520 */               ((Commands)localObject).commandUse(paramStringTokenizer);
/*  521 */             } else if (str.equals("monitor")) {
/*  522 */               monitorCommand(paramStringTokenizer);
/*  523 */             } else if (str.equals("unmonitor")) {
/*  524 */               unmonitorCommand(paramStringTokenizer);
/*  525 */             } else if (str.equals("lock")) {
/*  526 */               ((Commands)localObject).commandLock(paramStringTokenizer);
/*  527 */               i = 0;
/*  528 */             } else if (str.equals("threadlocks")) {
/*  529 */               ((Commands)localObject).commandThreadlocks(paramStringTokenizer);
/*  530 */             } else if (str.equals("disablegc")) {
/*  531 */               ((Commands)localObject).commandDisableGC(paramStringTokenizer);
/*  532 */               i = 0;
/*  533 */             } else if (str.equals("enablegc")) {
/*  534 */               ((Commands)localObject).commandEnableGC(paramStringTokenizer);
/*  535 */               i = 0;
/*  536 */             } else if (str.equals("save")) {
/*  537 */               ((Commands)localObject).commandSave(paramStringTokenizer);
/*  538 */               i = 0;
/*  539 */             } else if (str.equals("bytecodes")) {
/*  540 */               ((Commands)localObject).commandBytecodes(paramStringTokenizer);
/*  541 */             } else if (str.equals("redefine")) {
/*  542 */               ((Commands)localObject).commandRedefine(paramStringTokenizer);
/*  543 */             } else if (str.equals("pop")) {
/*  544 */               ((Commands)localObject).commandPopFrames(paramStringTokenizer, false);
/*  545 */             } else if (str.equals("reenter")) {
/*  546 */               ((Commands)localObject).commandPopFrames(paramStringTokenizer, true);
/*  547 */             } else if (str.equals("extension")) {
/*  548 */               ((Commands)localObject).commandExtension(paramStringTokenizer);
/*  549 */             } else if (str.equals("exclude")) {
/*  550 */               ((Commands)localObject).commandExclude(paramStringTokenizer);
/*  551 */             } else if (str.equals("read")) {
/*  552 */               readCommand(paramStringTokenizer);
/*  553 */             } else if ((str.equals("help")) || (str.equals("?"))) {
/*  554 */               help();
/*  555 */             } else if (str.equals("version")) {
/*  556 */               ((Commands)localObject).commandVersion("jdb", 
/*  557 */                 Bootstrap.virtualMachineManager());
/*  558 */             } else if ((str.equals("quit")) || (str.equals("exit"))) {
/*  559 */               if (this.handler != null) {
/*  560 */                 this.handler.shutdown();
/*      */               }
/*  562 */               Env.shutdown();
/*      */             } else {
/*  564 */               MessageOutput.println("Unrecognized command.  Try help...", str);
/*      */             }
/*      */           } catch (VMCannotBeModifiedException localVMCannotBeModifiedException) {
/*  567 */             MessageOutput.println("Command is not supported on a read-only VM connection", str);
/*      */           } catch (UnsupportedOperationException localUnsupportedOperationException) {
/*  569 */             MessageOutput.println("Command is not supported on the target VM", str);
/*      */           } catch (VMNotConnectedException localVMNotConnectedException) {
/*  571 */             MessageOutput.println("Command not valid until the VM is started with the run command", str);
/*      */           }
/*      */           catch (Exception localException) {
/*  574 */             MessageOutput.printException("Internal exception:", localException);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  579 */     if (i != 0)
/*  580 */       MessageOutput.printPrompt();
/*      */   }
/*      */ 
/*      */   void monitorCommand(StringTokenizer paramStringTokenizer)
/*      */   {
/*  588 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  589 */       this.monitorCount += 1;
/*  590 */       this.monitorCommands.add(this.monitorCount + ": " + paramStringTokenizer.nextToken(""));
/*      */     } else {
/*  592 */       for (String str : this.monitorCommands)
/*  593 */         MessageOutput.printDirectln(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   void unmonitorCommand(StringTokenizer paramStringTokenizer)
/*      */   {
/*  599 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  600 */       String str1 = paramStringTokenizer.nextToken();
/*      */       try
/*      */       {
/*  603 */         int i = Integer.parseInt(str1);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/*  605 */         MessageOutput.println("Not a monitor number:", str1);
/*  606 */         return;
/*      */       }
/*  608 */       String str2 = str1 + ":";
/*  609 */       for (String str3 : this.monitorCommands) {
/*  610 */         StringTokenizer localStringTokenizer = new StringTokenizer(str3);
/*  611 */         if (localStringTokenizer.nextToken().equals(str2)) {
/*  612 */           this.monitorCommands.remove(str3);
/*  613 */           MessageOutput.println("Unmonitoring", str3);
/*  614 */           return;
/*      */         }
/*      */       }
/*  617 */       MessageOutput.println("No monitor numbered:", str1);
/*      */     } else {
/*  619 */       MessageOutput.println("Usage: unmonitor <monitor#>");
/*      */     }
/*      */   }
/*      */ 
/*      */   void readCommand(StringTokenizer paramStringTokenizer)
/*      */   {
/*  625 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  626 */       String str = paramStringTokenizer.nextToken();
/*  627 */       if (!readCommandFile(new File(str)))
/*  628 */         MessageOutput.println("Could not open:", str);
/*      */     }
/*      */     else {
/*  631 */       MessageOutput.println("Usage: read <command-filename>");
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean readCommandFile(File paramFile)
/*      */   {
/*  640 */     BufferedReader localBufferedReader = null;
/*      */     try {
/*  642 */       if (paramFile.canRead())
/*      */       {
/*  644 */         MessageOutput.println("*** Reading commands from", paramFile.getPath());
/*  645 */         localBufferedReader = new BufferedReader(new FileReader(paramFile));
/*      */         String str;
/*  647 */         while ((str = localBufferedReader.readLine()) != null) {
/*  648 */           StringTokenizer localStringTokenizer = new StringTokenizer(str);
/*  649 */           if (localStringTokenizer.hasMoreTokens())
/*  650 */             executeCommand(localStringTokenizer);
/*      */         }
/*      */       }
/*      */     } catch (IOException localIOException) {
/*      */     }
/*      */     finally {
/*  656 */       if (localBufferedReader != null)
/*      */         try {
/*  658 */           localBufferedReader.close();
/*      */         }
/*      */         catch (Exception localException3) {
/*      */         }
/*      */     }
/*  663 */     return localBufferedReader != null;
/*      */   }
/*      */ 
/*      */   String readStartupCommandFile(String paramString1, String paramString2, String paramString3)
/*      */   {
/*  674 */     File localFile = new File(paramString1, paramString2);
/*  675 */     if (!localFile.exists()) {
/*  676 */       return null;
/*      */     }
/*      */     String str;
/*      */     try
/*      */     {
/*  681 */       str = localFile.getCanonicalPath();
/*      */     } catch (IOException localIOException) {
/*  683 */       MessageOutput.println("Could not open:", localFile.getPath());
/*  684 */       return null;
/*      */     }
/*  686 */     if (((paramString3 == null) || (!paramString3.equals(str))) && 
/*  687 */       (!readCommandFile(localFile))) {
/*  688 */       MessageOutput.println("Could not open:", localFile.getPath());
/*      */     }
/*      */ 
/*  691 */     return str;
/*      */   }
/*      */ 
/*      */   public TTY()
/*      */     throws Exception
/*      */   {
/*  697 */     MessageOutput.println("Initializing progname", "jdb");
/*      */ 
/*  699 */     if ((Env.connection().isOpen()) && (Env.vm().canBeModified()))
/*      */     {
/*  705 */       this.handler = new EventHandler(this, true);
/*      */     }
/*      */     try {
/*  708 */       BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*      */ 
/*  711 */       Object localObject1 = null;
/*      */ 
/*  713 */       Thread.currentThread().setPriority(5);
/*      */ 
/*  733 */       String str1 = System.getProperty("user.home");
/*      */       Object localObject2;
/*  736 */       if ((localObject2 = readStartupCommandFile(str1, "jdb.ini", null)) == null)
/*      */       {
/*  738 */         localObject2 = readStartupCommandFile(str1, ".jdbrc", null);
/*      */       }
/*      */ 
/*  741 */       String str2 = System.getProperty("user.dir");
/*  742 */       if (readStartupCommandFile(str2, "jdb.ini", (String)localObject2) == null)
/*      */       {
/*  744 */         readStartupCommandFile(str2, ".jdbrc", (String)localObject2);
/*      */       }
/*      */ 
/*  749 */       MessageOutput.printPrompt();
/*      */       while (true) {
/*  751 */         str1 = localBufferedReader.readLine();
/*  752 */         if (str1 == null) {
/*  753 */           MessageOutput.println("Input stream closed.");
/*  754 */           str1 = "quit";
/*      */         }
/*      */ 
/*  757 */         if ((str1.startsWith("!!")) && (localObject1 != null)) {
/*  758 */           str1 = (String)localObject1 + str1.substring(2);
/*  759 */           MessageOutput.printDirectln(str1);
/*      */         }
/*      */ 
/*  762 */         localObject2 = new StringTokenizer(str1);
/*  763 */         if (((StringTokenizer)localObject2).hasMoreTokens()) {
/*  764 */           localObject1 = str1;
/*  765 */           executeCommand((StringTokenizer)localObject2);
/*      */         } else {
/*  767 */           MessageOutput.printPrompt();
/*      */         }
/*      */       }
/*      */     } catch (VMDisconnectedException localVMDisconnectedException) {
/*  771 */       this.handler.handleDisconnectedException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void usage() {
/*  776 */     MessageOutput.println("zz usage text", new Object[] { "jdb", File.pathSeparator });
/*      */ 
/*  778 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   static void usageError(String paramString) {
/*  782 */     MessageOutput.println(paramString);
/*  783 */     MessageOutput.println();
/*  784 */     usage();
/*      */   }
/*      */ 
/*      */   static void usageError(String paramString1, String paramString2) {
/*  788 */     MessageOutput.println(paramString1, paramString2);
/*  789 */     MessageOutput.println();
/*  790 */     usage();
/*      */   }
/*      */ 
/*      */   private static boolean supportsSharedMemory()
/*      */   {
/*  795 */     for (Connector localConnector : Bootstrap.virtualMachineManager().allConnectors()) {
/*  796 */       if (localConnector.transport() != null)
/*      */       {
/*  799 */         if ("dt_shmem".equals(localConnector.transport().name()))
/*  800 */           return true;
/*      */       }
/*      */     }
/*  803 */     return false;
/*      */   }
/*      */ 
/*      */   private static String addressToSocketArgs(String paramString) {
/*  807 */     int i = paramString.indexOf(':');
/*  808 */     if (i != -1) {
/*  809 */       String str1 = paramString.substring(0, i);
/*  810 */       String str2 = paramString.substring(i + 1);
/*  811 */       return "hostname=" + str1 + ",port=" + str2;
/*      */     }
/*  813 */     return "port=" + paramString;
/*      */   }
/*      */ 
/*      */   private static boolean hasWhitespace(String paramString)
/*      */   {
/*  818 */     int i = paramString.length();
/*  819 */     for (int j = 0; j < i; j++) {
/*  820 */       if (Character.isWhitespace(paramString.charAt(j))) {
/*  821 */         return true;
/*      */       }
/*      */     }
/*  824 */     return false;
/*      */   }
/*      */ 
/*      */   private static String addArgument(String paramString1, String paramString2) {
/*  828 */     if ((hasWhitespace(paramString2)) || (paramString2.indexOf(',') != -1))
/*      */     {
/*  830 */       StringBuffer localStringBuffer = new StringBuffer(paramString1);
/*  831 */       localStringBuffer.append('"');
/*  832 */       for (int i = 0; i < paramString2.length(); i++) {
/*  833 */         char c = paramString2.charAt(i);
/*  834 */         if (c == '"') {
/*  835 */           localStringBuffer.append('\\');
/*      */         }
/*  837 */         localStringBuffer.append(c);
/*      */       }
/*  839 */       localStringBuffer.append("\" ");
/*  840 */       return localStringBuffer.toString();
/*      */     }
/*  842 */     return paramString1 + paramString2 + ' ';
/*      */   }
/*      */ 
/*      */   public static void main(String[] paramArrayOfString) throws MissingResourceException
/*      */   {
/*  847 */     String str1 = "";
/*  848 */     String str2 = "";
/*  849 */     int i = 0;
/*  850 */     boolean bool = false;
/*  851 */     String str3 = null;
/*      */ 
/*  854 */     MessageOutput.textResources = ResourceBundle.getBundle("com.sun.tools.example.debug.tty.TTYResources", 
/*  855 */       Locale.getDefault());
/*      */ 
/*  857 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/*  858 */       String str4 = paramArrayOfString[j];
/*      */       Object localObject;
/*  859 */       if (str4.equals("-dbgtrace")) {
/*  860 */         if ((j == paramArrayOfString.length - 1) || 
/*  861 */           (!Character.isDigit(paramArrayOfString[(j + 1)]
/*  861 */           .charAt(0))))
/*      */         {
/*  862 */           i = 16777215;
/*      */         } else {
/*  864 */           localObject = "";
/*      */           try {
/*  866 */             localObject = paramArrayOfString[(++j)];
/*  867 */             i = Integer.decode((String)localObject).intValue();
/*      */           } catch (NumberFormatException localNumberFormatException) {
/*  869 */             usageError("dbgtrace flag value must be an integer:", (String)localObject);
/*      */ 
/*  871 */             return;
/*      */           }
/*      */         }
/*      */       } else { if (str4.equals("-X")) {
/*  875 */           usageError("Use java minus X to see");
/*  876 */           return;
/*      */         }
/*      */ 
/*  879 */         if ((str4
/*  879 */           .equals("-v")) || 
/*  879 */           (str4.startsWith("-v:")) || 
/*  880 */           (str4
/*  880 */           .startsWith("-verbose")) || 
/*  881 */           (str4
/*  881 */           .startsWith("-D")) || 
/*  884 */           (str4
/*  884 */           .startsWith("-X")) || 
/*  887 */           (str4
/*  887 */           .equals("-noasyncgc")) || 
/*  887 */           (str4.equals("-prof")) || 
/*  888 */           (str4
/*  888 */           .equals("-verify")) || 
/*  888 */           (str4.equals("-noverify")) || 
/*  889 */           (str4
/*  889 */           .equals("-verifyremote")) || 
/*  890 */           (str4
/*  890 */           .equals("-verbosegc")) || 
/*  891 */           (str4
/*  891 */           .startsWith("-ms")) || 
/*  891 */           (str4.startsWith("-mx")) || 
/*  892 */           (str4
/*  892 */           .startsWith("-ss")) || 
/*  892 */           (str4.startsWith("-oss")))
/*      */         {
/*  894 */           str2 = addArgument(str2, str4); } else {
/*  895 */           if (str4.equals("-tclassic")) {
/*  896 */             usageError("Classic VM no longer supported.");
/*  897 */             return;
/*  898 */           }if (str4.equals("-tclient"))
/*      */           {
/*  900 */             str2 = "-client " + str2;
/*  901 */           } else if (str4.equals("-tserver"))
/*      */           {
/*  903 */             str2 = "-server " + str2;
/*  904 */           } else if (str4.equals("-sourcepath")) {
/*  905 */             if (j == paramArrayOfString.length - 1) {
/*  906 */               usageError("No sourcepath specified.");
/*  907 */               return;
/*      */             }
/*  909 */             Env.setSourcePath(paramArrayOfString[(++j)]);
/*  910 */           } else if (str4.equals("-classpath")) {
/*  911 */             if (j == paramArrayOfString.length - 1) {
/*  912 */               usageError("No classpath specified.");
/*  913 */               return;
/*      */             }
/*  915 */             str2 = addArgument(str2, str4);
/*  916 */             str2 = addArgument(str2, paramArrayOfString[(++j)]);
/*  917 */           } else if (str4.equals("-attach")) {
/*  918 */             if (str3 != null) {
/*  919 */               usageError("cannot redefine existing connection", str4);
/*  920 */               return;
/*      */             }
/*  922 */             if (j == paramArrayOfString.length - 1) {
/*  923 */               usageError("No attach address specified.");
/*  924 */               return;
/*      */             }
/*  926 */             localObject = paramArrayOfString[(++j)];
/*      */ 
/*  934 */             if (supportsSharedMemory()) {
/*  935 */               str3 = "com.sun.jdi.SharedMemoryAttach:name=" + (String)localObject;
/*      */             }
/*      */             else {
/*  938 */               String str5 = addressToSocketArgs((String)localObject);
/*  939 */               str3 = "com.sun.jdi.SocketAttach:" + str5;
/*      */             }
/*  941 */           } else if ((str4.equals("-listen")) || (str4.equals("-listenany"))) {
/*  942 */             if (str3 != null) {
/*  943 */               usageError("cannot redefine existing connection", str4);
/*  944 */               return;
/*      */             }
/*  946 */             localObject = null;
/*  947 */             if (str4.equals("-listen")) {
/*  948 */               if (j == paramArrayOfString.length - 1) {
/*  949 */                 usageError("No attach address specified.");
/*  950 */                 return;
/*      */               }
/*  952 */               localObject = paramArrayOfString[(++j)];
/*      */             }
/*      */ 
/*  961 */             if (supportsSharedMemory()) {
/*  962 */               str3 = "com.sun.jdi.SharedMemoryListen:";
/*  963 */               if (localObject != null)
/*  964 */                 str3 = str3 + "name=" + (String)localObject;
/*      */             }
/*      */             else {
/*  967 */               str3 = "com.sun.jdi.SocketListen:";
/*  968 */               if (localObject != null)
/*  969 */                 str3 = str3 + addressToSocketArgs((String)localObject);
/*      */             }
/*      */           }
/*  972 */           else if (str4.equals("-launch")) {
/*  973 */             bool = true; } else {
/*  974 */             if (str4.equals("-listconnectors")) {
/*  975 */               localObject = new Commands();
/*  976 */               ((Commands)localObject).commandConnectors(Bootstrap.virtualMachineManager());
/*  977 */               return;
/*  978 */             }if (str4.equals("-connect"))
/*      */             {
/*  985 */               if (str3 != null) {
/*  986 */                 usageError("cannot redefine existing connection", str4);
/*  987 */                 return;
/*      */               }
/*  989 */               if (j == paramArrayOfString.length - 1) {
/*  990 */                 usageError("No connect specification.");
/*  991 */                 return;
/*      */               }
/*  993 */               str3 = paramArrayOfString[(++j)];
/*  994 */             } else if (str4.equals("-help")) {
/*  995 */               usage();
/*  996 */             } else if (str4.equals("-version")) {
/*  997 */               localObject = new Commands();
/*  998 */               ((Commands)localObject).commandVersion("jdb", 
/*  999 */                 Bootstrap.virtualMachineManager());
/* 1000 */               System.exit(0); } else {
/* 1001 */               if (str4.startsWith("-")) {
/* 1002 */                 usageError("invalid option", str4);
/* 1003 */                 return;
/*      */               }
/*      */ 
/* 1006 */               str1 = addArgument("", str4);
/* 1007 */               for (j++; j < paramArrayOfString.length; j++) {
/* 1008 */                 str1 = addArgument(str1, paramArrayOfString[j]);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1035 */     if (str3 == null)
/* 1036 */       str3 = "com.sun.jdi.CommandLineLaunch:";
/* 1037 */     else if ((!str3.endsWith(",")) && (!str3.endsWith(":"))) {
/* 1038 */       str3 = str3 + ",";
/*      */     }
/*      */ 
/* 1041 */     str1 = str1.trim();
/* 1042 */     str2 = str2.trim();
/*      */ 
/* 1044 */     if (str1.length() > 0) {
/* 1045 */       if (!str3.startsWith("com.sun.jdi.CommandLineLaunch:")) {
/* 1046 */         usageError("Cannot specify command line with connector:", str3);
/*      */ 
/* 1048 */         return;
/*      */       }
/* 1050 */       str3 = str3 + "main=" + str1 + ",";
/*      */     }
/*      */ 
/* 1053 */     if (str2.length() > 0) {
/* 1054 */       if (!str3.startsWith("com.sun.jdi.CommandLineLaunch:")) {
/* 1055 */         usageError("Cannot specify target vm arguments with connector:", str3);
/*      */ 
/* 1057 */         return;
/*      */       }
/* 1059 */       str3 = str3 + "options=" + str2 + ",";
/*      */     }
/*      */     try
/*      */     {
/* 1063 */       if (!str3.endsWith(",")) {
/* 1064 */         str3 = str3 + ",";
/*      */       }
/* 1066 */       Env.init(str3, bool, i);
/* 1067 */       new TTY();
/*      */     } catch (Exception localException) {
/* 1069 */       MessageOutput.printException("Internal exception:", localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.TTY
 * JD-Core Version:    0.6.2
 */