/*      */ package com.sun.tools.example.debug.tty;
/*      */ 
/*      */ import com.sun.jdi.AbsentInformationException;
/*      */ import com.sun.jdi.ArrayReference;
/*      */ import com.sun.jdi.ArrayType;
/*      */ import com.sun.jdi.ClassType;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.IncompatibleThreadStateException;
/*      */ import com.sun.jdi.InterfaceType;
/*      */ import com.sun.jdi.InvalidTypeException;
/*      */ import com.sun.jdi.InvocationException;
/*      */ import com.sun.jdi.LocalVariable;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.PathSearchingVirtualMachine;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.StackFrame;
/*      */ import com.sun.jdi.StringReference;
/*      */ import com.sun.jdi.ThreadGroupReference;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.Value;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import com.sun.jdi.VirtualMachineManager;
/*      */ import com.sun.jdi.connect.Connector;
/*      */ import com.sun.jdi.connect.Connector.Argument;
/*      */ import com.sun.jdi.connect.Transport;
/*      */ import com.sun.jdi.request.EventRequestManager;
/*      */ import com.sun.jdi.request.MethodEntryRequest;
/*      */ import com.sun.jdi.request.MethodExitRequest;
/*      */ import com.sun.jdi.request.StepRequest;
/*      */ import com.sun.tools.example.debug.expr.ExpressionParser;
/*      */ import com.sun.tools.example.debug.expr.ExpressionParser.GetFrame;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ class Commands
/*      */ {
/* 1272 */   static String methodTraceCommand = null;
/*      */ 
/*      */   private Value evaluate(String paramString)
/*      */   {
/*  102 */     Value localValue = null;
/*  103 */     ExpressionParser.GetFrame local1 = null;
/*      */     try {
/*  105 */       final ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  106 */       if ((localThreadInfo != null) && (localThreadInfo.getCurrentFrame() != null)) {
/*  107 */         local1 = new ExpressionParser.GetFrame()
/*      */         {
/*      */           public StackFrame get() throws IncompatibleThreadStateException {
/*  110 */             return localThreadInfo.getCurrentFrame();
/*      */           }
/*      */         };
/*      */       }
/*  114 */       localValue = ExpressionParser.evaluate(paramString, Env.vm(), local1);
/*      */     } catch (InvocationException localInvocationException) {
/*  116 */       MessageOutput.println("Exception in expression:", localInvocationException
/*  117 */         .exception().referenceType().name());
/*      */     } catch (Exception localException) {
/*  119 */       String str1 = localException.getMessage();
/*  120 */       if (str1 == null) {
/*  121 */         MessageOutput.printException(str1, localException);
/*      */       } else {
/*      */         String str2;
/*      */         try {
/*  125 */           str2 = MessageOutput.format(str1);
/*      */         } catch (MissingResourceException localMissingResourceException) {
/*  127 */           str2 = localException.toString();
/*      */         }
/*  129 */         MessageOutput.printDirectln(str2);
/*      */       }
/*      */     }
/*  132 */     return localValue;
/*      */   }
/*      */ 
/*      */   private String getStringValue() {
/*  136 */     Value localValue = null;
/*  137 */     String str1 = null;
/*      */     try {
/*  139 */       localValue = ExpressionParser.getMassagedValue();
/*  140 */       str1 = localValue.toString();
/*      */     } catch (com.sun.tools.example.debug.expr.ParseException localParseException) {
/*  142 */       String str2 = localParseException.getMessage();
/*  143 */       if (str2 == null) {
/*  144 */         MessageOutput.printException(str2, localParseException);
/*      */       } else {
/*      */         String str3;
/*      */         try {
/*  148 */           str3 = MessageOutput.format(str2);
/*      */         } catch (MissingResourceException localMissingResourceException) {
/*  150 */           str3 = localParseException.toString();
/*      */         }
/*  152 */         MessageOutput.printDirectln(str3);
/*      */       }
/*      */     }
/*  155 */     return str1;
/*      */   }
/*      */ 
/*      */   private ThreadInfo doGetThread(String paramString) {
/*  159 */     ThreadInfo localThreadInfo = ThreadInfo.getThreadInfo(paramString);
/*  160 */     if (localThreadInfo == null) {
/*  161 */       MessageOutput.println("is not a valid thread id", paramString);
/*      */     }
/*  163 */     return localThreadInfo;
/*      */   }
/*      */ 
/*      */   String typedName(Method paramMethod) {
/*  167 */     StringBuffer localStringBuffer = new StringBuffer();
/*  168 */     localStringBuffer.append(paramMethod.name());
/*  169 */     localStringBuffer.append("(");
/*      */ 
/*  171 */     List localList = paramMethod.argumentTypeNames();
/*  172 */     int i = localList.size() - 1;
/*      */ 
/*  174 */     for (int j = 0; j < i; j++) {
/*  175 */       localStringBuffer.append((String)localList.get(j));
/*  176 */       localStringBuffer.append(", ");
/*      */     }
/*  178 */     if (i >= 0)
/*      */     {
/*  180 */       String str = (String)localList.get(i);
/*  181 */       if (paramMethod.isVarArgs())
/*      */       {
/*  183 */         localStringBuffer.append(str.substring(0, str.length() - 2));
/*  184 */         localStringBuffer.append("...");
/*      */       } else {
/*  186 */         localStringBuffer.append(str);
/*      */       }
/*      */     }
/*  189 */     localStringBuffer.append(")");
/*  190 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   void commandConnectors(VirtualMachineManager paramVirtualMachineManager) {
/*  194 */     List localList = paramVirtualMachineManager.allConnectors();
/*  195 */     if (localList.isEmpty()) {
/*  196 */       MessageOutput.println("Connectors available");
/*      */     }
/*  198 */     for (Connector localConnector : localList)
/*      */     {
/*  200 */       String str = localConnector
/*  200 */         .transport() == null ? "null" : localConnector.transport().name();
/*  201 */       MessageOutput.println();
/*  202 */       MessageOutput.println("Connector and Transport name", new Object[] { localConnector
/*  203 */         .name(), str });
/*  204 */       MessageOutput.println("Connector description", localConnector.description());
/*      */ 
/*  206 */       for (Connector.Argument localArgument : localConnector.defaultArguments().values()) {
/*  207 */         MessageOutput.println();
/*      */ 
/*  209 */         boolean bool = localArgument.mustSpecify();
/*  210 */         if ((localArgument.value() == null) || (localArgument.value() == ""))
/*      */         {
/*  212 */           MessageOutput.println(bool ? "Connector required argument nodefault" : "Connector argument nodefault", localArgument
/*  214 */             .name());
/*      */         }
/*  216 */         else MessageOutput.println(bool ? "Connector required argument default" : "Connector argument default", new Object[] { localArgument
/*  219 */             .name(), localArgument.value() });
/*      */ 
/*  221 */         MessageOutput.println("Connector description", localArgument.description());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandClasses()
/*      */   {
/*  229 */     StringBuffer localStringBuffer = new StringBuffer();
/*  230 */     for (ReferenceType localReferenceType : Env.vm().allClasses()) {
/*  231 */       localStringBuffer.append(localReferenceType.name());
/*  232 */       localStringBuffer.append("\n");
/*      */     }
/*  234 */     MessageOutput.print("** classes list **", localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   void commandClass(StringTokenizer paramStringTokenizer)
/*      */   {
/*  239 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  240 */       MessageOutput.println("No class specified.");
/*  241 */       return;
/*      */     }
/*      */ 
/*  244 */     String str = paramStringTokenizer.nextToken();
/*  245 */     int i = 0;
/*      */ 
/*  247 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  248 */       if (paramStringTokenizer.nextToken().toLowerCase().equals("all")) {
/*  249 */         i = 1;
/*      */       } else {
/*  251 */         MessageOutput.println("Invalid option on class command");
/*  252 */         return;
/*      */       }
/*      */     }
/*  255 */     ReferenceType localReferenceType = Env.getReferenceTypeFromToken(str);
/*  256 */     if (localReferenceType == null) {
/*  257 */       MessageOutput.println("is not a valid id or class name", str);
/*      */       return;
/*      */     }
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     Iterator localIterator;
/*      */     Object localObject4;
/*  260 */     if ((localReferenceType instanceof ClassType)) {
/*  261 */       localObject1 = (ClassType)localReferenceType;
/*  262 */       MessageOutput.println("Class:", ((ClassType)localObject1).name());
/*      */ 
/*  264 */       localObject2 = ((ClassType)localObject1).superclass();
/*  265 */       while (localObject2 != null) {
/*  266 */         MessageOutput.println("extends:", ((ClassType)localObject2).name());
/*  267 */         localObject2 = i != 0 ? ((ClassType)localObject2).superclass() : null;
/*      */       }
/*      */ 
/*  271 */       localObject3 = i != 0 ? ((ClassType)localObject1)
/*  271 */         .allInterfaces() : ((ClassType)localObject1).interfaces();
/*  272 */       for (localIterator = ((List)localObject3).iterator(); localIterator.hasNext(); ) { localObject4 = (InterfaceType)localIterator.next();
/*  273 */         MessageOutput.println("implements:", ((InterfaceType)localObject4).name());
/*      */       }
/*      */ 
/*  276 */       for (localIterator = ((ClassType)localObject1).subclasses().iterator(); localIterator.hasNext(); ) { localObject4 = (ClassType)localIterator.next();
/*  277 */         MessageOutput.println("subclass:", ((ClassType)localObject4).name());
/*      */       }
/*  279 */       for (localIterator = ((ClassType)localObject1).nestedTypes().iterator(); localIterator.hasNext(); ) { localObject4 = (ReferenceType)localIterator.next();
/*  280 */         MessageOutput.println("nested:", ((ReferenceType)localObject4).name()); }
/*      */     }
/*  282 */     else if ((localReferenceType instanceof InterfaceType)) {
/*  283 */       localObject1 = (InterfaceType)localReferenceType;
/*  284 */       MessageOutput.println("Interface:", ((InterfaceType)localObject1).name());
/*  285 */       for (localObject2 = ((InterfaceType)localObject1).superinterfaces().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (InterfaceType)((Iterator)localObject2).next();
/*  286 */         MessageOutput.println("extends:", ((InterfaceType)localObject3).name());
/*      */       }
/*  288 */       for (localObject2 = ((InterfaceType)localObject1).subinterfaces().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (InterfaceType)((Iterator)localObject2).next();
/*  289 */         MessageOutput.println("subinterface:", ((InterfaceType)localObject3).name());
/*      */       }
/*  291 */       for (localObject2 = ((InterfaceType)localObject1).implementors().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (ClassType)((Iterator)localObject2).next();
/*  292 */         MessageOutput.println("implementor:", ((ClassType)localObject3).name());
/*      */       }
/*  294 */       for (localObject2 = ((InterfaceType)localObject1).nestedTypes().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (ReferenceType)((Iterator)localObject2).next();
/*  295 */         MessageOutput.println("nested:", ((ReferenceType)localObject3).name()); }
/*      */     }
/*      */     else {
/*  298 */       localObject1 = (ArrayType)localReferenceType;
/*  299 */       MessageOutput.println("Array:", ((ArrayType)localObject1).name());
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandMethods(StringTokenizer paramStringTokenizer) {
/*  304 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  305 */       MessageOutput.println("No class specified.");
/*  306 */       return;
/*      */     }
/*      */ 
/*  309 */     String str = paramStringTokenizer.nextToken();
/*  310 */     ReferenceType localReferenceType = Env.getReferenceTypeFromToken(str);
/*  311 */     if (localReferenceType != null) {
/*  312 */       StringBuffer localStringBuffer = new StringBuffer();
/*  313 */       for (Method localMethod : localReferenceType.allMethods()) {
/*  314 */         localStringBuffer.append(localMethod.declaringType().name());
/*  315 */         localStringBuffer.append(" ");
/*  316 */         localStringBuffer.append(typedName(localMethod));
/*  317 */         localStringBuffer.append('\n');
/*      */       }
/*  319 */       MessageOutput.print("** methods list **", localStringBuffer.toString());
/*      */     } else {
/*  321 */       MessageOutput.println("is not a valid id or class name", str);
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandFields(StringTokenizer paramStringTokenizer) {
/*  326 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  327 */       MessageOutput.println("No class specified.");
/*  328 */       return;
/*      */     }
/*      */ 
/*  331 */     String str1 = paramStringTokenizer.nextToken();
/*  332 */     ReferenceType localReferenceType = Env.getReferenceTypeFromToken(str1);
/*  333 */     if (localReferenceType != null) {
/*  334 */       List localList1 = localReferenceType.allFields();
/*  335 */       List localList2 = localReferenceType.visibleFields();
/*  336 */       StringBuffer localStringBuffer = new StringBuffer();
/*  337 */       for (Field localField : localList1)
/*      */       {
/*      */         String str2;
/*  339 */         if (!localList2.contains(localField))
/*  340 */           str2 = MessageOutput.format("list field typename and name hidden", new Object[] { localField
/*  341 */             .typeName(), localField
/*  342 */             .name() });
/*  343 */         else if (!localField.declaringType().equals(localReferenceType))
/*  344 */           str2 = MessageOutput.format("list field typename and name inherited", new Object[] { localField
/*  345 */             .typeName(), localField
/*  346 */             .name(), localField
/*  347 */             .declaringType().name() });
/*      */         else {
/*  349 */           str2 = MessageOutput.format("list field typename and name", new Object[] { localField
/*  350 */             .typeName(), localField
/*  351 */             .name() });
/*      */         }
/*  353 */         localStringBuffer.append(str2);
/*      */       }
/*  355 */       MessageOutput.print("** fields list **", localStringBuffer.toString());
/*      */     } else {
/*  357 */       MessageOutput.println("is not a valid id or class name", str1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void printThreadGroup(ThreadGroupReference paramThreadGroupReference) {
/*  362 */     ThreadIterator localThreadIterator = new ThreadIterator(paramThreadGroupReference);
/*      */ 
/*  364 */     MessageOutput.println("Thread Group:", paramThreadGroupReference.name());
/*  365 */     int i = 0;
/*  366 */     int j = 0;
/*      */     ThreadReference localThreadReference;
/*  367 */     while (localThreadIterator.hasNext()) {
/*  368 */       localThreadReference = localThreadIterator.next();
/*  369 */       i = Math.max(i, 
/*  370 */         Env.description(localThreadReference)
/*  370 */         .length());
/*  371 */       j = Math.max(j, localThreadReference
/*  372 */         .name().length());
/*      */     }
/*      */ 
/*  375 */     localThreadIterator = new ThreadIterator(paramThreadGroupReference);
/*  376 */     while (localThreadIterator.hasNext()) {
/*  377 */       localThreadReference = localThreadIterator.next();
/*  378 */       if (localThreadReference.threadGroup() != null)
/*      */       {
/*  382 */         if (!localThreadReference.threadGroup().equals(paramThreadGroupReference)) {
/*  383 */           paramThreadGroupReference = localThreadReference.threadGroup();
/*  384 */           MessageOutput.println("Thread Group:", paramThreadGroupReference.name());
/*      */         }
/*      */ 
/*  394 */         StringBuffer localStringBuffer1 = new StringBuffer(Env.description(localThreadReference));
/*  395 */         for (int k = localStringBuffer1.length(); k < i; k++) {
/*  396 */           localStringBuffer1.append(" ");
/*      */         }
/*  398 */         StringBuffer localStringBuffer2 = new StringBuffer(localThreadReference.name());
/*  399 */         for (int m = localStringBuffer2.length(); m < j; m++)
/*  400 */           localStringBuffer2.append(" ");
/*      */         String str;
/*  408 */         switch (localThreadReference.status()) {
/*      */         case -1:
/*  410 */           if (localThreadReference.isAtBreakpoint())
/*  411 */             str = "Thread description name unknownStatus BP";
/*      */           else {
/*  413 */             str = "Thread description name unknownStatus";
/*      */           }
/*  415 */           break;
/*      */         case 0:
/*  417 */           if (localThreadReference.isAtBreakpoint())
/*  418 */             str = "Thread description name zombieStatus BP";
/*      */           else {
/*  420 */             str = "Thread description name zombieStatus";
/*      */           }
/*  422 */           break;
/*      */         case 1:
/*  424 */           if (localThreadReference.isAtBreakpoint())
/*  425 */             str = "Thread description name runningStatus BP";
/*      */           else {
/*  427 */             str = "Thread description name runningStatus";
/*      */           }
/*  429 */           break;
/*      */         case 2:
/*  431 */           if (localThreadReference.isAtBreakpoint())
/*  432 */             str = "Thread description name sleepingStatus BP";
/*      */           else {
/*  434 */             str = "Thread description name sleepingStatus";
/*      */           }
/*  436 */           break;
/*      */         case 3:
/*  438 */           if (localThreadReference.isAtBreakpoint())
/*  439 */             str = "Thread description name waitingStatus BP";
/*      */           else {
/*  441 */             str = "Thread description name waitingStatus";
/*      */           }
/*  443 */           break;
/*      */         case 4:
/*  445 */           if (localThreadReference.isAtBreakpoint())
/*  446 */             str = "Thread description name condWaitstatus BP";
/*      */           else {
/*  448 */             str = "Thread description name condWaitstatus";
/*      */           }
/*  450 */           break;
/*      */         default:
/*  452 */           throw new InternalError(MessageOutput.format("Invalid thread status."));
/*      */         }
/*  454 */         MessageOutput.println(str, new Object[] { localStringBuffer1
/*  455 */           .toString(), localStringBuffer2
/*  456 */           .toString() });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*  461 */   void commandThreads(StringTokenizer paramStringTokenizer) { if (!paramStringTokenizer.hasMoreTokens()) {
/*  462 */       printThreadGroup(ThreadInfo.group());
/*  463 */       return;
/*      */     }
/*  465 */     String str = paramStringTokenizer.nextToken();
/*  466 */     ThreadGroupReference localThreadGroupReference = ThreadGroupIterator.find(str);
/*  467 */     if (localThreadGroupReference == null)
/*  468 */       MessageOutput.println("is not a valid threadgroup name", str);
/*      */     else
/*  470 */       printThreadGroup(localThreadGroupReference);
/*      */   }
/*      */ 
/*      */   void commandThreadGroups()
/*      */   {
/*  475 */     ThreadGroupIterator localThreadGroupIterator = new ThreadGroupIterator();
/*  476 */     int i = 0;
/*  477 */     while (localThreadGroupIterator.hasNext()) {
/*  478 */       ThreadGroupReference localThreadGroupReference = localThreadGroupIterator.nextThreadGroup();
/*  479 */       i++;
/*  480 */       MessageOutput.println("thread group number description name", new Object[] { new Integer(i), 
/*  482 */         Env.description(localThreadGroupReference), 
/*  482 */         localThreadGroupReference
/*  483 */         .name() });
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandThread(StringTokenizer paramStringTokenizer) {
/*  488 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  489 */       MessageOutput.println("Thread number not specified.");
/*  490 */       return;
/*      */     }
/*  492 */     ThreadInfo localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  493 */     if (localThreadInfo != null)
/*  494 */       ThreadInfo.setCurrentThreadInfo(localThreadInfo);
/*      */   }
/*      */ 
/*      */   void commandThreadGroup(StringTokenizer paramStringTokenizer)
/*      */   {
/*  499 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  500 */       MessageOutput.println("Threadgroup name not specified.");
/*  501 */       return;
/*      */     }
/*  503 */     String str = paramStringTokenizer.nextToken();
/*  504 */     ThreadGroupReference localThreadGroupReference = ThreadGroupIterator.find(str);
/*  505 */     if (localThreadGroupReference == null)
/*  506 */       MessageOutput.println("is not a valid threadgroup name", str);
/*      */     else
/*  508 */       ThreadInfo.setThreadGroup(localThreadGroupReference);
/*      */   }
/*      */ 
/*      */   void commandRun(StringTokenizer paramStringTokenizer)
/*      */   {
/*  521 */     VMConnection localVMConnection = Env.connection();
/*  522 */     if (!localVMConnection.isLaunch()) {
/*  523 */       if (!paramStringTokenizer.hasMoreTokens())
/*  524 */         commandCont();
/*      */       else {
/*  526 */         MessageOutput.println("run <args> command is valid only with launched VMs");
/*      */       }
/*  528 */       return;
/*      */     }
/*  530 */     if (localVMConnection.isOpen()) {
/*  531 */       MessageOutput.println("VM already running. use cont to continue after events.");
/*      */       return;
/*      */     }
/*      */     String str;
/*  540 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  541 */       str = paramStringTokenizer.nextToken("");
/*  542 */       boolean bool = localVMConnection.setConnectorArg("main", str);
/*  543 */       if (!bool) {
/*  544 */         MessageOutput.println("Unable to set main class and arguments");
/*  545 */         return;
/*      */       }
/*      */     } else {
/*  548 */       str = localVMConnection.connectorArg("main");
/*  549 */       if (str.length() == 0) {
/*  550 */         MessageOutput.println("Main class and arguments must be specified");
/*  551 */         return;
/*      */       }
/*      */     }
/*  554 */     MessageOutput.println("run", str);
/*      */ 
/*  559 */     localVMConnection.open();
/*      */   }
/*      */ 
/*      */   void commandLoad(StringTokenizer paramStringTokenizer)
/*      */   {
/*  564 */     MessageOutput.println("The load command is no longer supported.");
/*      */   }
/*      */ 
/*      */   private List<ThreadReference> allThreads(ThreadGroupReference paramThreadGroupReference) {
/*  568 */     ArrayList localArrayList = new ArrayList();
/*  569 */     localArrayList.addAll(paramThreadGroupReference.threads());
/*  570 */     for (ThreadGroupReference localThreadGroupReference : paramThreadGroupReference.threadGroups()) {
/*  571 */       localArrayList.addAll(allThreads(localThreadGroupReference));
/*      */     }
/*  573 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   void commandSuspend(StringTokenizer paramStringTokenizer) {
/*  577 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  578 */       Env.vm().suspend();
/*  579 */       MessageOutput.println("All threads suspended.");
/*      */     } else {
/*  581 */       while (paramStringTokenizer.hasMoreTokens()) {
/*  582 */         ThreadInfo localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  583 */         if (localThreadInfo != null)
/*  584 */           localThreadInfo.getThread().suspend();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandResume(StringTokenizer paramStringTokenizer)
/*      */   {
/*  591 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  592 */       ThreadInfo.invalidateAll();
/*  593 */       Env.vm().resume();
/*  594 */       MessageOutput.println("All threads resumed.");
/*      */     } else {
/*  596 */       while (paramStringTokenizer.hasMoreTokens()) {
/*  597 */         ThreadInfo localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  598 */         if (localThreadInfo != null) {
/*  599 */           localThreadInfo.invalidate();
/*  600 */           localThreadInfo.getThread().resume();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandCont() {
/*  607 */     if (ThreadInfo.getCurrentThreadInfo() == null) {
/*  608 */       MessageOutput.println("Nothing suspended.");
/*  609 */       return;
/*      */     }
/*  611 */     ThreadInfo.invalidateAll();
/*  612 */     Env.vm().resume();
/*      */   }
/*      */ 
/*      */   void clearPreviousStep(ThreadReference paramThreadReference)
/*      */   {
/*  620 */     EventRequestManager localEventRequestManager = Env.vm().eventRequestManager();
/*  621 */     for (StepRequest localStepRequest : localEventRequestManager.stepRequests())
/*  622 */       if (localStepRequest.thread().equals(paramThreadReference)) {
/*  623 */         localEventRequestManager.deleteEventRequest(localStepRequest);
/*  624 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   void commandStep(StringTokenizer paramStringTokenizer)
/*      */   {
/*  632 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  633 */     if (localThreadInfo == null) {
/*  634 */       MessageOutput.println("Nothing suspended.");
/*      */       return;
/*      */     }
/*      */     int i;
/*  638 */     if ((paramStringTokenizer.hasMoreTokens()) && 
/*  639 */       (paramStringTokenizer
/*  639 */       .nextToken().toLowerCase().equals("up")))
/*  640 */       i = 3;
/*      */     else {
/*  642 */       i = 1;
/*      */     }
/*      */ 
/*  645 */     clearPreviousStep(localThreadInfo.getThread());
/*  646 */     EventRequestManager localEventRequestManager = Env.vm().eventRequestManager();
/*  647 */     StepRequest localStepRequest = localEventRequestManager.createStepRequest(localThreadInfo.getThread(), -2, i);
/*      */ 
/*  649 */     if (i == 1) {
/*  650 */       Env.addExcludes(localStepRequest);
/*      */     }
/*      */ 
/*  653 */     localStepRequest.addCountFilter(1);
/*  654 */     localStepRequest.enable();
/*  655 */     ThreadInfo.invalidateAll();
/*  656 */     Env.vm().resume();
/*      */   }
/*      */ 
/*      */   void commandStepi()
/*      */   {
/*  663 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  664 */     if (localThreadInfo == null) {
/*  665 */       MessageOutput.println("Nothing suspended.");
/*  666 */       return;
/*      */     }
/*  668 */     clearPreviousStep(localThreadInfo.getThread());
/*  669 */     EventRequestManager localEventRequestManager = Env.vm().eventRequestManager();
/*  670 */     StepRequest localStepRequest = localEventRequestManager.createStepRequest(localThreadInfo.getThread(), -1, 1);
/*      */ 
/*  673 */     Env.addExcludes(localStepRequest);
/*      */ 
/*  675 */     localStepRequest.addCountFilter(1);
/*  676 */     localStepRequest.enable();
/*  677 */     ThreadInfo.invalidateAll();
/*  678 */     Env.vm().resume();
/*      */   }
/*      */ 
/*      */   void commandNext() {
/*  682 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  683 */     if (localThreadInfo == null) {
/*  684 */       MessageOutput.println("Nothing suspended.");
/*  685 */       return;
/*      */     }
/*  687 */     clearPreviousStep(localThreadInfo.getThread());
/*  688 */     EventRequestManager localEventRequestManager = Env.vm().eventRequestManager();
/*  689 */     StepRequest localStepRequest = localEventRequestManager.createStepRequest(localThreadInfo.getThread(), -2, 2);
/*      */ 
/*  692 */     Env.addExcludes(localStepRequest);
/*      */ 
/*  694 */     localStepRequest.addCountFilter(1);
/*  695 */     localStepRequest.enable();
/*  696 */     ThreadInfo.invalidateAll();
/*  697 */     Env.vm().resume();
/*      */   }
/*      */ 
/*      */   void doKill(ThreadReference paramThreadReference, StringTokenizer paramStringTokenizer) {
/*  701 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  702 */       MessageOutput.println("No exception object specified.");
/*  703 */       return;
/*      */     }
/*  705 */     String str = paramStringTokenizer.nextToken("");
/*  706 */     Value localValue = evaluate(str);
/*  707 */     if ((localValue != null) && ((localValue instanceof ObjectReference)))
/*      */       try {
/*  709 */         paramThreadReference.stop((ObjectReference)localValue);
/*  710 */         MessageOutput.println("killed", paramThreadReference.toString());
/*      */       } catch (InvalidTypeException localInvalidTypeException) {
/*  712 */         MessageOutput.println("Invalid exception object");
/*      */       }
/*      */     else
/*  715 */       MessageOutput.println("Expression must evaluate to an object");
/*      */   }
/*      */ 
/*      */   void doKillThread(final ThreadReference paramThreadReference, final StringTokenizer paramStringTokenizer)
/*      */   {
/*  721 */     new AsyncExecution(paramThreadReference)
/*      */     {
/*      */       void action() {
/*  724 */         Commands.this.doKill(paramThreadReference, paramStringTokenizer);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   void commandKill(StringTokenizer paramStringTokenizer) {
/*  730 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  731 */       MessageOutput.println("Usage: kill <thread id> <throwable>");
/*  732 */       return;
/*      */     }
/*  734 */     ThreadInfo localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  735 */     if (localThreadInfo != null) {
/*  736 */       MessageOutput.println("killing thread:", localThreadInfo.getThread().name());
/*  737 */       doKillThread(localThreadInfo.getThread(), paramStringTokenizer);
/*  738 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   void listCaughtExceptions() {
/*  743 */     int i = 1;
/*      */ 
/*  746 */     for (EventRequestSpec localEventRequestSpec : Env.specList.eventRequestSpecs()) {
/*  747 */       if ((localEventRequestSpec instanceof ExceptionSpec)) {
/*  748 */         if (i != 0) {
/*  749 */           i = 0;
/*  750 */           MessageOutput.println("Exceptions caught:");
/*      */         }
/*  752 */         MessageOutput.println("tab", localEventRequestSpec.toString());
/*      */       }
/*      */     }
/*  755 */     if (i != 0)
/*  756 */       MessageOutput.println("No exceptions caught.");
/*      */   }
/*      */ 
/*      */   private EventRequestSpec parseExceptionSpec(StringTokenizer paramStringTokenizer)
/*      */   {
/*  761 */     String str1 = paramStringTokenizer.nextToken();
/*  762 */     boolean bool1 = false;
/*  763 */     boolean bool2 = false;
/*  764 */     EventRequestSpec localEventRequestSpec = null;
/*  765 */     String str2 = null;
/*      */ 
/*  767 */     if (str1.equals("uncaught")) {
/*  768 */       bool1 = false;
/*  769 */       bool2 = true;
/*  770 */     } else if (str1.equals("caught")) {
/*  771 */       bool1 = true;
/*  772 */       bool2 = false;
/*  773 */     } else if (str1.equals("all")) {
/*  774 */       bool1 = true;
/*  775 */       bool2 = true;
/*      */     }
/*      */     else
/*      */     {
/*  785 */       bool1 = true;
/*  786 */       bool2 = true;
/*  787 */       str2 = str1;
/*      */     }
/*  789 */     if ((str2 == null) && (paramStringTokenizer.hasMoreTokens())) {
/*  790 */       str2 = paramStringTokenizer.nextToken();
/*      */     }
/*  792 */     if ((str2 != null) && ((bool1) || (bool2))) {
/*      */       try {
/*  794 */         localEventRequestSpec = Env.specList.createExceptionCatch(str2, bool1, bool2);
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException)
/*      */       {
/*  798 */         MessageOutput.println("is not a valid class name", str2);
/*      */       }
/*      */     }
/*  801 */     return localEventRequestSpec;
/*      */   }
/*      */ 
/*      */   void commandCatchException(StringTokenizer paramStringTokenizer) {
/*  805 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  806 */       listCaughtExceptions();
/*      */     } else {
/*  808 */       EventRequestSpec localEventRequestSpec = parseExceptionSpec(paramStringTokenizer);
/*  809 */       if (localEventRequestSpec != null)
/*  810 */         resolveNow(localEventRequestSpec);
/*      */       else
/*  812 */         MessageOutput.println("Usage: catch exception");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandIgnoreException(StringTokenizer paramStringTokenizer)
/*      */   {
/*  818 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  819 */       listCaughtExceptions();
/*      */     } else {
/*  821 */       EventRequestSpec localEventRequestSpec = parseExceptionSpec(paramStringTokenizer);
/*  822 */       if (Env.specList.delete(localEventRequestSpec)) {
/*  823 */         MessageOutput.println("Removed:", localEventRequestSpec.toString());
/*      */       } else {
/*  825 */         if (localEventRequestSpec != null) {
/*  826 */           MessageOutput.println("Not found:", localEventRequestSpec.toString());
/*      */         }
/*  828 */         MessageOutput.println("Usage: ignore exception");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandUp(StringTokenizer paramStringTokenizer) {
/*  834 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  835 */     if (localThreadInfo == null) {
/*  836 */       MessageOutput.println("Current thread not set.");
/*  837 */       return;
/*      */     }
/*      */ 
/*  840 */     int i = 1;
/*  841 */     if (paramStringTokenizer.hasMoreTokens()) { String str = paramStringTokenizer.nextToken();
/*      */       int j;
/*      */       try {
/*  845 */         NumberFormat localNumberFormat = NumberFormat.getNumberInstance();
/*  846 */         localNumberFormat.setParseIntegerOnly(true);
/*  847 */         Number localNumber = localNumberFormat.parse(str);
/*  848 */         j = localNumber.intValue();
/*      */       } catch (java.text.ParseException localParseException) {
/*  850 */         j = 0;
/*      */       }
/*  852 */       if (j <= 0) {
/*  853 */         MessageOutput.println("Usage: up [n frames]");
/*  854 */         return;
/*      */       }
/*  856 */       i = j;
/*      */     }
/*      */     try
/*      */     {
/*  860 */       localThreadInfo.up(i);
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  862 */       MessageOutput.println("Current thread isnt suspended.");
/*      */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  864 */       MessageOutput.println("End of stack.");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandDown(StringTokenizer paramStringTokenizer) {
/*  869 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  870 */     if (localThreadInfo == null) {
/*  871 */       MessageOutput.println("Current thread not set.");
/*  872 */       return;
/*      */     }
/*      */ 
/*  875 */     int i = 1;
/*  876 */     if (paramStringTokenizer.hasMoreTokens()) { String str = paramStringTokenizer.nextToken();
/*      */       int j;
/*      */       try {
/*  880 */         NumberFormat localNumberFormat = NumberFormat.getNumberInstance();
/*  881 */         localNumberFormat.setParseIntegerOnly(true);
/*  882 */         Number localNumber = localNumberFormat.parse(str);
/*  883 */         j = localNumber.intValue();
/*      */       } catch (java.text.ParseException localParseException) {
/*  885 */         j = 0;
/*      */       }
/*  887 */       if (j <= 0) {
/*  888 */         MessageOutput.println("Usage: down [n frames]");
/*  889 */         return;
/*      */       }
/*  891 */       i = j;
/*      */     }
/*      */     try
/*      */     {
/*  895 */       localThreadInfo.down(i);
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  897 */       MessageOutput.println("Current thread isnt suspended.");
/*      */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  899 */       MessageOutput.println("End of stack.");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void dumpStack(ThreadInfo paramThreadInfo, boolean paramBoolean) {
/*  904 */     List localList = null;
/*      */     try {
/*  906 */       localList = paramThreadInfo.getStack();
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  908 */       MessageOutput.println("Current thread isnt suspended.");
/*  909 */       return;
/*      */     }
/*  911 */     if (localList == null) {
/*  912 */       MessageOutput.println("Thread is not running (no stack).");
/*      */     } else {
/*  914 */       int i = localList.size();
/*  915 */       for (int j = paramThreadInfo.getCurrentFrameIndex(); j < i; j++) {
/*  916 */         StackFrame localStackFrame = (StackFrame)localList.get(j);
/*  917 */         dumpFrame(j, paramBoolean, localStackFrame);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void dumpFrame(int paramInt, boolean paramBoolean, StackFrame paramStackFrame) {
/*  923 */     Location localLocation = paramStackFrame.location();
/*  924 */     long l1 = -1L;
/*  925 */     if (paramBoolean) {
/*  926 */       l1 = localLocation.codeIndex();
/*      */     }
/*  928 */     Method localMethod = localLocation.method();
/*      */ 
/*  930 */     long l2 = localLocation.lineNumber();
/*  931 */     String str = null;
/*  932 */     if (localMethod.isNative())
/*  933 */       str = MessageOutput.format("native method");
/*  934 */     else if (l2 != -1L) {
/*      */       try
/*      */       {
/*  937 */         str = localLocation.sourceName() + 
/*  937 */           MessageOutput.format("line number", new Object[] { new Long(l2) });
/*      */       }
/*      */       catch (AbsentInformationException localAbsentInformationException)
/*      */       {
/*  940 */         str = MessageOutput.format("unknown");
/*      */       }
/*      */     }
/*  943 */     if (l1 != -1L) {
/*  944 */       MessageOutput.println("stack frame dump with pc", new Object[] { new Integer(paramInt + 1), localMethod
/*  946 */         .declaringType().name(), localMethod
/*  947 */         .name(), str, new Long(l1) });
/*      */     }
/*      */     else
/*      */     {
/*  951 */       MessageOutput.println("stack frame dump", new Object[] { new Integer(paramInt + 1), localMethod
/*  953 */         .declaringType().name(), localMethod
/*  954 */         .name(), str });
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandWhere(StringTokenizer paramStringTokenizer, boolean paramBoolean)
/*      */   {
/*      */     Object localObject1;
/*  960 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  961 */       localObject1 = ThreadInfo.getCurrentThreadInfo();
/*  962 */       if (localObject1 == null) {
/*  963 */         MessageOutput.println("No thread specified.");
/*  964 */         return;
/*      */       }
/*  966 */       dumpStack((ThreadInfo)localObject1, paramBoolean);
/*      */     } else {
/*  968 */       localObject1 = paramStringTokenizer.nextToken();
/*      */       Object localObject2;
/*  969 */       if (((String)localObject1).toLowerCase().equals("all")) {
/*  970 */         for (localObject2 = ThreadInfo.threads().iterator(); ((Iterator)localObject2).hasNext(); ) { ThreadInfo localThreadInfo = (ThreadInfo)((Iterator)localObject2).next();
/*  971 */           MessageOutput.println("Thread:", localThreadInfo
/*  972 */             .getThread().name());
/*  973 */           dumpStack(localThreadInfo, paramBoolean); }
/*      */       }
/*      */       else {
/*  976 */         localObject2 = doGetThread((String)localObject1);
/*  977 */         if (localObject2 != null) {
/*  978 */           ThreadInfo.setCurrentThreadInfo((ThreadInfo)localObject2);
/*  979 */           dumpStack((ThreadInfo)localObject2, paramBoolean);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandInterrupt(StringTokenizer paramStringTokenizer)
/*      */   {
/*      */     ThreadInfo localThreadInfo;
/*  986 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  987 */       localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*  988 */       if (localThreadInfo == null) {
/*  989 */         MessageOutput.println("No thread specified.");
/*  990 */         return;
/*      */       }
/*  992 */       localThreadInfo.getThread().interrupt();
/*      */     } else {
/*  994 */       localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  995 */       if (localThreadInfo != null)
/*  996 */         localThreadInfo.getThread().interrupt();
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandMemory()
/*      */   {
/* 1002 */     MessageOutput.println("The memory command is no longer supported.");
/*      */   }
/*      */ 
/*      */   void commandGC() {
/* 1006 */     MessageOutput.println("The gc command is no longer necessary.");
/*      */   }
/*      */ 
/*      */   static String locationString(Location paramLocation)
/*      */   {
/* 1014 */     return MessageOutput.format("locationString", new Object[] { paramLocation
/* 1015 */       .declaringType().name(), paramLocation
/* 1016 */       .method().name(), new Integer(paramLocation
/* 1017 */       .lineNumber()), new Long(paramLocation
/* 1018 */       .codeIndex()) });
/*      */   }
/*      */ 
/*      */   void listBreakpoints() {
/* 1022 */     int i = 1;
/*      */ 
/* 1025 */     for (EventRequestSpec localEventRequestSpec : Env.specList.eventRequestSpecs()) {
/* 1026 */       if ((localEventRequestSpec instanceof BreakpointSpec)) {
/* 1027 */         if (i != 0) {
/* 1028 */           i = 0;
/* 1029 */           MessageOutput.println("Breakpoints set:");
/*      */         }
/* 1031 */         MessageOutput.println("tab", localEventRequestSpec.toString());
/*      */       }
/*      */     }
/* 1034 */     if (i != 0)
/* 1035 */       MessageOutput.println("No breakpoints set.");
/*      */   }
/*      */ 
/*      */   private void printBreakpointCommandUsage(String paramString1, String paramString2)
/*      */   {
/* 1041 */     MessageOutput.println("printbreakpointcommandusage", new Object[] { paramString1, paramString2 });
/*      */   }
/*      */ 
/*      */   protected BreakpointSpec parseBreakpointSpec(StringTokenizer paramStringTokenizer, String paramString1, String paramString2)
/*      */   {
/* 1047 */     BreakpointSpec localBreakpointSpec = null;
/*      */     try
/*      */     {
/* 1049 */       String str1 = paramStringTokenizer.nextToken(":( \t\n\r");
/*      */       String str2;
/*      */       try
/*      */       {
/* 1055 */         str2 = paramStringTokenizer.nextToken("").trim();
/*      */       } catch (NoSuchElementException localNoSuchElementException) {
/* 1057 */         str2 = null;
/*      */       }
/*      */       String str4;
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 1060 */       if ((str2 != null) && (str2.startsWith(":"))) {
/* 1061 */         paramStringTokenizer = new StringTokenizer(str2.substring(1));
/* 1062 */         String str3 = str1;
/* 1063 */         str4 = paramStringTokenizer.nextToken();
/*      */ 
/* 1065 */         localObject1 = NumberFormat.getNumberInstance();
/* 1066 */         ((NumberFormat)localObject1).setParseIntegerOnly(true);
/* 1067 */         localObject2 = ((NumberFormat)localObject1).parse(str4);
/* 1068 */         int j = ((Number)localObject2).intValue();
/*      */ 
/* 1070 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1071 */           printBreakpointCommandUsage(paramString1, paramString2);
/* 1072 */           return null;
/*      */         }
/*      */         try {
/* 1075 */           localBreakpointSpec = Env.specList.createBreakpoint(str3, j);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException2) {
/* 1078 */           MessageOutput.println("is not a valid class name", str3);
/*      */         }
/*      */       }
/*      */       else {
/* 1082 */         int i = str1.lastIndexOf(".");
/* 1083 */         if ((i <= 0) || 
/* 1084 */           (i >= str1
/* 1084 */           .length() - 1)) {
/* 1085 */           printBreakpointCommandUsage(paramString1, paramString2);
/* 1086 */           return null;
/*      */         }
/* 1088 */         str4 = str1.substring(i + 1);
/* 1089 */         localObject1 = str1.substring(0, i);
/* 1090 */         localObject2 = null;
/* 1091 */         if (str2 != null) {
/* 1092 */           if ((!str2.startsWith("(")) || (!str2.endsWith(")"))) {
/* 1093 */             MessageOutput.println("Invalid method specification:", str4 + str2);
/*      */ 
/* 1095 */             printBreakpointCommandUsage(paramString1, paramString2);
/* 1096 */             return null;
/*      */           }
/*      */ 
/* 1099 */           str2 = str2.substring(1, str2.length() - 1);
/*      */ 
/* 1101 */           localObject2 = new ArrayList();
/* 1102 */           paramStringTokenizer = new StringTokenizer(str2, ",");
/* 1103 */           while (paramStringTokenizer.hasMoreTokens())
/* 1104 */             ((List)localObject2).add(paramStringTokenizer.nextToken());
/*      */         }
/*      */         try
/*      */         {
/* 1108 */           localBreakpointSpec = Env.specList.createBreakpoint((String)localObject1, str4, (List)localObject2);
/*      */         }
/*      */         catch (MalformedMemberNameException localMalformedMemberNameException)
/*      */         {
/* 1112 */           MessageOutput.println("is not a valid method name", str4);
/*      */         } catch (ClassNotFoundException localClassNotFoundException1) {
/* 1114 */           MessageOutput.println("is not a valid class name", (String)localObject1);
/*      */         }
/*      */       }
/*      */     } catch (Exception localException) {
/* 1118 */       printBreakpointCommandUsage(paramString1, paramString2);
/* 1119 */       return null;
/*      */     }
/* 1121 */     return localBreakpointSpec;
/*      */   }
/*      */ 
/*      */   private void resolveNow(EventRequestSpec paramEventRequestSpec) {
/* 1125 */     boolean bool = Env.specList.addEagerlyResolve(paramEventRequestSpec);
/* 1126 */     if ((bool) && (!paramEventRequestSpec.isResolved()))
/* 1127 */       MessageOutput.println("Deferring.", paramEventRequestSpec.toString());
/*      */   }
/*      */ 
/*      */   void commandStop(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1133 */     int i = 2;
/*      */     String str;
/* 1135 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 1136 */       str = paramStringTokenizer.nextToken();
/* 1137 */       if ((str.equals("go")) && (paramStringTokenizer.hasMoreTokens())) {
/* 1138 */         i = 0;
/* 1139 */         str = paramStringTokenizer.nextToken();
/* 1140 */       } else if ((str.equals("thread")) && (paramStringTokenizer.hasMoreTokens())) {
/* 1141 */         i = 1;
/* 1142 */         str = paramStringTokenizer.nextToken();
/*      */       }
/*      */     } else {
/* 1145 */       listBreakpoints();
/* 1146 */       return;
/*      */     }
/*      */ 
/* 1149 */     BreakpointSpec localBreakpointSpec = parseBreakpointSpec(paramStringTokenizer, "stop at", "stop in");
/* 1150 */     if (localBreakpointSpec != null)
/*      */     {
/* 1154 */       if ((str.equals("at")) && (localBreakpointSpec.isMethodBreakpoint())) {
/* 1155 */         MessageOutput.println("Use stop at to set a breakpoint at a line number");
/* 1156 */         printBreakpointCommandUsage("stop at", "stop in");
/* 1157 */         return;
/*      */       }
/* 1159 */       localBreakpointSpec.suspendPolicy = i;
/* 1160 */       resolveNow(localBreakpointSpec);
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandClear(StringTokenizer paramStringTokenizer) {
/* 1165 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1166 */       listBreakpoints();
/* 1167 */       return;
/*      */     }
/*      */ 
/* 1170 */     BreakpointSpec localBreakpointSpec = parseBreakpointSpec(paramStringTokenizer, "clear", "clear");
/* 1171 */     if (localBreakpointSpec != null)
/* 1172 */       if (Env.specList.delete(localBreakpointSpec))
/* 1173 */         MessageOutput.println("Removed:", localBreakpointSpec.toString());
/*      */       else
/* 1175 */         MessageOutput.println("Not found:", localBreakpointSpec.toString());
/*      */   }
/*      */ 
/*      */   private List<WatchpointSpec> parseWatchpointSpec(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1181 */     ArrayList localArrayList = new ArrayList();
/* 1182 */     int i = 0;
/* 1183 */     int j = 0;
/* 1184 */     int k = 2;
/*      */ 
/* 1186 */     String str1 = paramStringTokenizer.nextToken();
/* 1187 */     if (str1.equals("go")) {
/* 1188 */       k = 0;
/* 1189 */       str1 = paramStringTokenizer.nextToken();
/* 1190 */     } else if (str1.equals("thread")) {
/* 1191 */       k = 1;
/* 1192 */       str1 = paramStringTokenizer.nextToken();
/*      */     }
/* 1194 */     if (str1.equals("access")) {
/* 1195 */       i = 1;
/* 1196 */       str1 = paramStringTokenizer.nextToken();
/* 1197 */     } else if (str1.equals("all")) {
/* 1198 */       i = 1;
/* 1199 */       j = 1;
/* 1200 */       str1 = paramStringTokenizer.nextToken();
/*      */     } else {
/* 1202 */       j = 1;
/*      */     }
/* 1204 */     int m = str1.lastIndexOf('.');
/* 1205 */     if (m < 0) {
/* 1206 */       MessageOutput.println("Class containing field must be specified.");
/* 1207 */       return localArrayList;
/*      */     }
/* 1209 */     String str2 = str1.substring(0, m);
/* 1210 */     str1 = str1.substring(m + 1);
/*      */     try
/*      */     {
/*      */       WatchpointSpec localWatchpointSpec;
/* 1214 */       if (i != 0) {
/* 1215 */         localWatchpointSpec = Env.specList.createAccessWatchpoint(str2, str1);
/*      */ 
/* 1217 */         localWatchpointSpec.suspendPolicy = k;
/* 1218 */         localArrayList.add(localWatchpointSpec);
/*      */       }
/* 1220 */       if (j != 0) {
/* 1221 */         localWatchpointSpec = Env.specList.createModificationWatchpoint(str2, str1);
/*      */ 
/* 1223 */         localWatchpointSpec.suspendPolicy = k;
/* 1224 */         localArrayList.add(localWatchpointSpec);
/*      */       }
/*      */     } catch (MalformedMemberNameException localMalformedMemberNameException) {
/* 1227 */       MessageOutput.println("is not a valid field name", str1);
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 1229 */       MessageOutput.println("is not a valid class name", str2);
/*      */     }
/* 1231 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   void commandWatch(StringTokenizer paramStringTokenizer) {
/* 1235 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1236 */       MessageOutput.println("Field to watch not specified");
/* 1237 */       return;
/*      */     }
/*      */ 
/* 1240 */     for (WatchpointSpec localWatchpointSpec : parseWatchpointSpec(paramStringTokenizer))
/* 1241 */       resolveNow(localWatchpointSpec);
/*      */   }
/*      */ 
/*      */   void commandUnwatch(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1246 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1247 */       MessageOutput.println("Field to unwatch not specified");
/* 1248 */       return;
/*      */     }
/*      */ 
/* 1251 */     for (WatchpointSpec localWatchpointSpec : parseWatchpointSpec(paramStringTokenizer))
/* 1252 */       if (Env.specList.delete(localWatchpointSpec))
/* 1253 */         MessageOutput.println("Removed:", localWatchpointSpec.toString());
/*      */       else
/* 1255 */         MessageOutput.println("Not found:", localWatchpointSpec.toString());
/*      */   }
/*      */ 
/*      */   void turnOnExitTrace(ThreadInfo paramThreadInfo, int paramInt)
/*      */   {
/* 1261 */     EventRequestManager localEventRequestManager = Env.vm().eventRequestManager();
/* 1262 */     MethodExitRequest localMethodExitRequest = localEventRequestManager.createMethodExitRequest();
/* 1263 */     if (paramThreadInfo != null) {
/* 1264 */       localMethodExitRequest.addThreadFilter(paramThreadInfo.getThread());
/*      */     }
/* 1266 */     Env.addExcludes(localMethodExitRequest);
/* 1267 */     localMethodExitRequest.setSuspendPolicy(paramInt);
/* 1268 */     localMethodExitRequest.enable();
/*      */   }
/*      */ 
/*      */   void commandTrace(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1276 */     int i = 2;
/* 1277 */     ThreadInfo localThreadInfo = null;
/* 1278 */     String str2 = " ";
/*      */ 
/* 1284 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 1285 */       String str1 = paramStringTokenizer.nextToken();
/* 1286 */       if (str1.equals("go")) {
/* 1287 */         i = 0;
/* 1288 */         str2 = " go ";
/* 1289 */         if (paramStringTokenizer.hasMoreTokens())
/* 1290 */           str1 = paramStringTokenizer.nextToken();
/*      */       }
/* 1292 */       else if (str1.equals("thread"))
/*      */       {
/* 1294 */         i = 1;
/* 1295 */         if (paramStringTokenizer.hasMoreTokens())
/* 1296 */           str1 = paramStringTokenizer.nextToken();
/*      */       }
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 1300 */       if (str1.equals("method")) {
/* 1301 */         localObject1 = null;
/*      */ 
/* 1303 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1304 */           localObject2 = paramStringTokenizer.nextToken();
/* 1305 */           if ((((String)localObject2).equals("exits")) || (((String)localObject2).equals("exit"))) {
/* 1306 */             if (paramStringTokenizer.hasMoreTokens()) {
/* 1307 */               localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*      */             }
/* 1309 */             if (((String)localObject2).equals("exit")) {
/*      */               StackFrame localStackFrame;
/*      */               try {
/* 1312 */                 localStackFrame = ThreadInfo.getCurrentThreadInfo().getCurrentFrame();
/*      */               } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/* 1314 */                 MessageOutput.println("Current thread isnt suspended.");
/* 1315 */                 return;
/*      */               }
/* 1317 */               Env.setAtExitMethod(localStackFrame.location().method());
/* 1318 */               localObject1 = MessageOutput.format("trace" + str2 + "method exit " + "in effect for", 
/* 1321 */                 Env.atExitMethod().toString());
/*      */             } else {
/* 1323 */               localObject1 = MessageOutput.format("trace" + str2 + "method exits " + "in effect");
/*      */             }
/*      */ 
/* 1327 */             commandUntrace(new StringTokenizer("methods"));
/* 1328 */             turnOnExitTrace(localThreadInfo, i);
/* 1329 */             methodTraceCommand = (String)localObject1;
/* 1330 */             return;
/*      */           }
/*      */         } else {
/* 1333 */           MessageOutput.println("Can only trace");
/* 1334 */           return;
/*      */         }
/*      */       }
/* 1337 */       if (str1.equals("methods"))
/*      */       {
/* 1340 */         localObject2 = Env.vm().eventRequestManager();
/* 1341 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1342 */           localThreadInfo = doGetThread(paramStringTokenizer.nextToken());
/*      */         }
/* 1344 */         if (localThreadInfo != null)
/*      */         {
/* 1362 */           localObject1 = ((EventRequestManager)localObject2).createMethodEntryRequest();
/* 1363 */           ((MethodEntryRequest)localObject1).addThreadFilter(localThreadInfo.getThread());
/*      */         } else {
/* 1365 */           commandUntrace(new StringTokenizer("methods"));
/* 1366 */           localObject1 = ((EventRequestManager)localObject2).createMethodEntryRequest();
/*      */         }
/* 1368 */         Env.addExcludes((MethodEntryRequest)localObject1);
/* 1369 */         ((MethodEntryRequest)localObject1).setSuspendPolicy(i);
/* 1370 */         ((MethodEntryRequest)localObject1).enable();
/* 1371 */         turnOnExitTrace(localThreadInfo, i);
/* 1372 */         methodTraceCommand = MessageOutput.format("trace" + str2 + "methods in effect");
/*      */ 
/* 1375 */         return;
/*      */       }
/*      */ 
/* 1378 */       MessageOutput.println("Can only trace");
/* 1379 */       return;
/*      */     }
/*      */ 
/* 1383 */     if (methodTraceCommand != null)
/* 1384 */       MessageOutput.printDirectln(methodTraceCommand);
/*      */   }
/*      */ 
/*      */   void commandUntrace(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1394 */     String str = null;
/* 1395 */     EventRequestManager localEventRequestManager = Env.vm().eventRequestManager();
/* 1396 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 1397 */       str = paramStringTokenizer.nextToken();
/*      */     }
/* 1399 */     if ((str == null) || (str.equals("methods"))) {
/* 1400 */       localEventRequestManager.deleteEventRequests(localEventRequestManager.methodEntryRequests());
/* 1401 */       localEventRequestManager.deleteEventRequests(localEventRequestManager.methodExitRequests());
/* 1402 */       Env.setAtExitMethod(null);
/* 1403 */       methodTraceCommand = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandList(StringTokenizer paramStringTokenizer) {
/* 1408 */     StackFrame localStackFrame = null;
/* 1409 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/* 1410 */     if (localThreadInfo == null) {
/* 1411 */       MessageOutput.println("No thread specified.");
/* 1412 */       return;
/*      */     }
/*      */     try {
/* 1415 */       localStackFrame = localThreadInfo.getCurrentFrame();
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/* 1417 */       MessageOutput.println("Current thread isnt suspended.");
/* 1418 */       return;
/*      */     }
/*      */ 
/* 1421 */     if (localStackFrame == null) {
/* 1422 */       MessageOutput.println("No frames on the current call stack");
/* 1423 */       return;
/*      */     }
/*      */ 
/* 1426 */     Location localLocation = localStackFrame.location();
/* 1427 */     if (localLocation.method().isNative()) {
/* 1428 */       MessageOutput.println("Current method is native");
/* 1429 */       return;
/*      */     }
/*      */ 
/* 1432 */     String str1 = null;
/*      */     try {
/* 1434 */       str1 = localLocation.sourceName();
/*      */ 
/* 1436 */       ReferenceType localReferenceType = localLocation.declaringType();
/* 1437 */       Object localObject1 = localLocation.lineNumber();
/*      */       Object localObject4;
/* 1439 */       if (paramStringTokenizer.hasMoreTokens()) {
/* 1440 */         String str2 = paramStringTokenizer.nextToken();
/*      */         try
/*      */         {
/* 1444 */           NumberFormat localNumberFormat = NumberFormat.getNumberInstance();
/* 1445 */           localNumberFormat.setParseIntegerOnly(true);
/* 1446 */           localObject4 = localNumberFormat.parse(str2);
/* 1447 */           localObject1 = ((Number)localObject4).intValue();
/*      */         }
/*      */         catch (java.text.ParseException localParseException) {
/* 1450 */           localObject4 = localReferenceType.methodsByName(str2);
/* 1451 */           if ((localObject4 == null) || (((List)localObject4).size() == 0)) {
/* 1452 */             MessageOutput.println("is not a valid line number or method name for", new Object[] { str2, localReferenceType
/* 1453 */               .name() });
/* 1454 */             return;
/* 1455 */           }if (((List)localObject4).size() > 1) {
/* 1456 */             MessageOutput.println("is an ambiguous method name in", new Object[] { str2, localReferenceType
/* 1457 */               .name() });
/* 1458 */             return;
/*      */           }
/* 1460 */           localLocation = ((Method)((List)localObject4).get(0)).location();
/* 1461 */           localObject1 = localLocation.lineNumber();
/*      */         }
/*      */       }
/* 1464 */       Object localObject2 = Math.max(localObject1 - 4, 1);
/* 1465 */       Object localObject3 = localObject2 + 9;
/* 1466 */       if (localObject1 < 0)
/* 1467 */         MessageOutput.println("Line number information not available for");
/* 1468 */       else if (Env.sourceLine(localLocation, localObject1) == null)
/* 1469 */         MessageOutput.println("is an invalid line number for", new Object[] { new Integer(localObject1), localReferenceType
/* 1471 */           .name() });
/*      */       else {
/* 1473 */         for (localObject4 = localObject2; localObject4 <= localObject3; localObject4++) {
/* 1474 */           String str3 = Env.sourceLine(localLocation, localObject4);
/* 1475 */           if (str3 == null) {
/*      */             break;
/*      */           }
/* 1478 */           if (localObject4 == localObject1) {
/* 1479 */             MessageOutput.println("source line number current line and line", new Object[] { new Integer(localObject4), str3 });
/*      */           }
/*      */           else
/*      */           {
/* 1483 */             MessageOutput.println("source line number and line", new Object[] { new Integer(localObject4), str3 });
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (AbsentInformationException localAbsentInformationException)
/*      */     {
/* 1490 */       MessageOutput.println("No source information available for:", localLocation.toString());
/*      */     } catch (FileNotFoundException localFileNotFoundException) {
/* 1492 */       MessageOutput.println("Source file not found:", str1);
/*      */     } catch (IOException localIOException) {
/* 1494 */       MessageOutput.println("I/O exception occurred:", localIOException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandLines(StringTokenizer paramStringTokenizer) {
/* 1499 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1500 */       MessageOutput.println("Specify class and method");
/*      */     } else {
/* 1502 */       String str = paramStringTokenizer.nextToken();
/* 1503 */       Object localObject1 = paramStringTokenizer.hasMoreTokens() ? paramStringTokenizer.nextToken() : null;
/*      */       try {
/* 1505 */         ReferenceType localReferenceType = Env.getReferenceTypeFromToken(str);
/*      */         Iterator localIterator;
/*      */         Object localObject2;
/* 1506 */         if (localReferenceType != null) {
/* 1507 */           List localList = null;
/* 1508 */           if (localObject1 == null) {
/* 1509 */             localList = localReferenceType.allLineLocations();
/*      */           } else {
/* 1511 */             for (localIterator = localReferenceType.allMethods().iterator(); localIterator.hasNext(); ) { localObject2 = (Method)localIterator.next();
/* 1512 */               if (((Method)localObject2).name().equals(localObject1)) {
/* 1513 */                 localList = ((Method)localObject2).allLineLocations();
/*      */               }
/*      */             }
/* 1516 */             if (localList == null) {
/* 1517 */               MessageOutput.println("is not a valid method name", localObject1);
/*      */             }
/*      */           }
/* 1520 */           for (localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject2 = (Location)localIterator.next();
/* 1521 */             MessageOutput.printDirectln(localObject2.toString()); }
/*      */         }
/*      */         else {
/* 1524 */           MessageOutput.println("is not a valid id or class name", str);
/*      */         }
/*      */       } catch (AbsentInformationException localAbsentInformationException) {
/* 1527 */         MessageOutput.println("Line number information not available for", str);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandClasspath(StringTokenizer paramStringTokenizer) {
/* 1533 */     if ((Env.vm() instanceof PathSearchingVirtualMachine)) {
/* 1534 */       PathSearchingVirtualMachine localPathSearchingVirtualMachine = (PathSearchingVirtualMachine)Env.vm();
/* 1535 */       MessageOutput.println("base directory:", localPathSearchingVirtualMachine.baseDirectory());
/* 1536 */       MessageOutput.println("classpath:", localPathSearchingVirtualMachine.classPath().toString());
/* 1537 */       MessageOutput.println("bootclasspath:", localPathSearchingVirtualMachine.bootClassPath().toString());
/*      */     } else {
/* 1539 */       MessageOutput.println("The VM does not use paths");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandUse(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1545 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1546 */       MessageOutput.printDirectln(Env.getSourcePath());
/*      */     }
/*      */     else
/*      */     {
/* 1553 */       Env.setSourcePath(paramStringTokenizer.nextToken("").trim());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void printVar(LocalVariable paramLocalVariable, Value paramValue)
/*      */   {
/* 1559 */     MessageOutput.println("expr is value", new Object[] { paramLocalVariable
/* 1560 */       .name(), paramValue == null ? "null" : paramValue
/* 1561 */       .toString() });
/*      */   }
/*      */ 
/*      */   void commandLocals()
/*      */   {
/* 1567 */     ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/* 1568 */     if (localThreadInfo == null) {
/* 1569 */       MessageOutput.println("No default thread specified:");
/* 1570 */       return;
/*      */     }
/*      */     try {
/* 1573 */       StackFrame localStackFrame = localThreadInfo.getCurrentFrame();
/* 1574 */       if (localStackFrame == null) {
/* 1575 */         throw new AbsentInformationException();
/*      */       }
/* 1577 */       List localList = localStackFrame.visibleVariables();
/*      */ 
/* 1579 */       if (localList.size() == 0) {
/* 1580 */         MessageOutput.println("No local variables");
/* 1581 */         return;
/*      */       }
/* 1583 */       localMap = localStackFrame.getValues(localList);
/*      */ 
/* 1585 */       MessageOutput.println("Method arguments:");
/* 1586 */       for (localIterator = localList.iterator(); localIterator.hasNext(); ) { localLocalVariable = (LocalVariable)localIterator.next();
/* 1587 */         if (localLocalVariable.isArgument()) {
/* 1588 */           localValue = (Value)localMap.get(localLocalVariable);
/* 1589 */           printVar(localLocalVariable, localValue);
/*      */         }
/*      */       }
/* 1592 */       MessageOutput.println("Local variables:");
/* 1593 */       for (localIterator = localList.iterator(); localIterator.hasNext(); ) { localLocalVariable = (LocalVariable)localIterator.next();
/* 1594 */         if (!localLocalVariable.isArgument()) {
/* 1595 */           localValue = (Value)localMap.get(localLocalVariable);
/* 1596 */           printVar(localLocalVariable, localValue);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (AbsentInformationException localAbsentInformationException)
/*      */     {
/*      */       Map localMap;
/*      */       Iterator localIterator;
/*      */       LocalVariable localLocalVariable;
/*      */       Value localValue;
/* 1600 */       MessageOutput.println("Local variable information not available.");
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/* 1602 */       MessageOutput.println("Current thread isnt suspended.");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void dump(ObjectReference paramObjectReference, ReferenceType paramReferenceType1, ReferenceType paramReferenceType2)
/*      */   {
/* 1608 */     for (Object localObject1 = paramReferenceType1.fields().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Field)((Iterator)localObject1).next();
/* 1609 */       StringBuffer localStringBuffer = new StringBuffer();
/* 1610 */       localStringBuffer.append("    ");
/* 1611 */       if (!paramReferenceType1.equals(paramReferenceType2)) {
/* 1612 */         localStringBuffer.append(paramReferenceType1.name());
/* 1613 */         localStringBuffer.append(".");
/*      */       }
/* 1615 */       localStringBuffer.append(((Field)localObject2).name());
/* 1616 */       localStringBuffer.append(MessageOutput.format("colon space"));
/* 1617 */       localStringBuffer.append(paramObjectReference.getValue((Field)localObject2));
/* 1618 */       MessageOutput.printDirectln(localStringBuffer.toString());
/*      */     }
/*      */     Object localObject2;
/* 1620 */     if ((paramReferenceType1 instanceof ClassType)) {
/* 1621 */       localObject1 = ((ClassType)paramReferenceType1).superclass();
/* 1622 */       if (localObject1 != null)
/* 1623 */         dump(paramObjectReference, (ReferenceType)localObject1, paramReferenceType2);
/*      */     }
/* 1625 */     else if ((paramReferenceType1 instanceof InterfaceType)) {
/* 1626 */       for (localObject1 = ((InterfaceType)paramReferenceType1).superinterfaces().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (InterfaceType)((Iterator)localObject1).next();
/* 1627 */         dump(paramObjectReference, (ReferenceType)localObject2, paramReferenceType2);
/*      */       }
/*      */ 
/*      */     }
/* 1631 */     else if ((paramObjectReference instanceof ArrayReference)) {
/* 1632 */       localObject1 = ((ArrayReference)paramObjectReference).getValues().iterator();
/* 1633 */       while (((Iterator)localObject1).hasNext()) {
/* 1634 */         MessageOutput.printDirect(((Value)((Iterator)localObject1).next()).toString());
/* 1635 */         if (((Iterator)localObject1).hasNext()) {
/* 1636 */           MessageOutput.printDirect(", ");
/*      */         }
/*      */       }
/* 1639 */       MessageOutput.println();
/*      */     }
/*      */   }
/*      */ 
/*      */   void doPrint(StringTokenizer paramStringTokenizer, boolean paramBoolean)
/*      */   {
/* 1647 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1648 */       MessageOutput.println("No objects specified.");
/* 1649 */       return;
/*      */     }
/*      */ 
/* 1652 */     while (paramStringTokenizer.hasMoreTokens()) {
/* 1653 */       String str = paramStringTokenizer.nextToken("");
/* 1654 */       Value localValue = evaluate(str);
/* 1655 */       if (localValue == null) {
/* 1656 */         MessageOutput.println("expr is null", str.toString());
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject;
/* 1657 */         if ((paramBoolean) && ((localValue instanceof ObjectReference)) && (!(localValue instanceof StringReference)))
/*      */         {
/* 1659 */           localObject = (ObjectReference)localValue;
/* 1660 */           ReferenceType localReferenceType = ((ObjectReference)localObject).referenceType();
/* 1661 */           MessageOutput.println("expr is value", new Object[] { str
/* 1662 */             .toString(), 
/* 1663 */             MessageOutput.format("grouping begin character") });
/*      */ 
/* 1664 */           dump((ObjectReference)localObject, localReferenceType, localReferenceType);
/* 1665 */           MessageOutput.println("grouping end character");
/*      */         } else {
/* 1667 */           localObject = getStringValue();
/* 1668 */           if (localObject != null)
/* 1669 */             MessageOutput.println("expr is value", new Object[] { str.toString(), localObject });
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandPrint(final StringTokenizer paramStringTokenizer, final boolean paramBoolean)
/*      */   {
/* 1677 */     new AsyncExecution(paramStringTokenizer)
/*      */     {
/*      */       void action() {
/* 1680 */         Commands.this.doPrint(paramStringTokenizer, paramBoolean);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   void commandSet(StringTokenizer paramStringTokenizer) {
/* 1686 */     String str = paramStringTokenizer.nextToken("");
/*      */ 
/* 1691 */     if (str.indexOf('=') == -1) {
/* 1692 */       MessageOutput.println("Invalid assignment syntax");
/* 1693 */       MessageOutput.printPrompt();
/* 1694 */       return;
/*      */     }
/*      */ 
/* 1701 */     commandPrint(new StringTokenizer(str), false);
/*      */   }
/*      */ 
/*      */   void doLock(StringTokenizer paramStringTokenizer) {
/* 1705 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1706 */       MessageOutput.println("No object specified.");
/* 1707 */       return;
/*      */     }
/*      */ 
/* 1710 */     String str1 = paramStringTokenizer.nextToken("");
/* 1711 */     Value localValue = evaluate(str1);
/*      */     try
/*      */     {
/* 1714 */       if ((localValue != null) && ((localValue instanceof ObjectReference))) {
/* 1715 */         ObjectReference localObjectReference = (ObjectReference)localValue;
/* 1716 */         String str2 = getStringValue();
/* 1717 */         if (str2 != null) {
/* 1718 */           MessageOutput.println("Monitor information for expr", new Object[] { str1
/* 1719 */             .trim(), str2 });
/*      */         }
/*      */ 
/* 1722 */         ThreadReference localThreadReference1 = localObjectReference.owningThread();
/* 1723 */         if (localThreadReference1 == null)
/* 1724 */           MessageOutput.println("Not owned");
/*      */         else {
/* 1726 */           MessageOutput.println("Owned by:", new Object[] { localThreadReference1
/* 1727 */             .name(), new Integer(localObjectReference
/* 1728 */             .entryCount()) });
/*      */         }
/* 1730 */         List localList = localObjectReference.waitingThreads();
/* 1731 */         if (localList.size() == 0)
/* 1732 */           MessageOutput.println("No waiters");
/*      */         else
/* 1734 */           for (ThreadReference localThreadReference2 : localList)
/* 1735 */             MessageOutput.println("Waiting thread:", localThreadReference2.name());
/*      */       }
/*      */       else
/*      */       {
/* 1739 */         MessageOutput.println("Expression must evaluate to an object");
/*      */       }
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/* 1742 */       MessageOutput.println("Threads must be suspended");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandLock(final StringTokenizer paramStringTokenizer) {
/* 1747 */     new AsyncExecution(paramStringTokenizer)
/*      */     {
/*      */       void action() {
/* 1750 */         Commands.this.doLock(paramStringTokenizer);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private void printThreadLockInfo(ThreadInfo paramThreadInfo) {
/* 1756 */     ThreadReference localThreadReference = paramThreadInfo.getThread();
/*      */     try {
/* 1758 */       MessageOutput.println("Monitor information for thread", localThreadReference.name());
/* 1759 */       List localList = localThreadReference.ownedMonitors();
/* 1760 */       if (localList.size() == 0)
/* 1761 */         MessageOutput.println("No monitors owned");
/*      */       else {
/* 1763 */         for (localObject = localList.iterator(); ((Iterator)localObject).hasNext(); ) { ObjectReference localObjectReference = (ObjectReference)((Iterator)localObject).next();
/* 1764 */           MessageOutput.println("Owned monitor:", localObjectReference.toString());
/*      */         }
/*      */       }
/* 1767 */       Object localObject = localThreadReference.currentContendedMonitor();
/* 1768 */       if (localObject == null)
/* 1769 */         MessageOutput.println("Not waiting for a monitor");
/*      */       else
/* 1771 */         MessageOutput.println("Waiting for monitor:", localObject.toString());
/*      */     }
/*      */     catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/* 1774 */       MessageOutput.println("Threads must be suspended");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandThreadlocks(StringTokenizer paramStringTokenizer) {
/* 1779 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1780 */       localObject1 = ThreadInfo.getCurrentThreadInfo();
/* 1781 */       if (localObject1 == null)
/* 1782 */         MessageOutput.println("Current thread not set.");
/*      */       else {
/* 1784 */         printThreadLockInfo((ThreadInfo)localObject1);
/*      */       }
/* 1786 */       return;
/*      */     }
/* 1788 */     Object localObject1 = paramStringTokenizer.nextToken();
/*      */     Object localObject2;
/* 1789 */     if (((String)localObject1).toLowerCase().equals("all")) {
/* 1790 */       for (localObject2 = ThreadInfo.threads().iterator(); ((Iterator)localObject2).hasNext(); ) { ThreadInfo localThreadInfo = (ThreadInfo)((Iterator)localObject2).next();
/* 1791 */         printThreadLockInfo(localThreadInfo); }
/*      */     }
/*      */     else {
/* 1794 */       localObject2 = doGetThread((String)localObject1);
/* 1795 */       if (localObject2 != null) {
/* 1796 */         ThreadInfo.setCurrentThreadInfo((ThreadInfo)localObject2);
/* 1797 */         printThreadLockInfo((ThreadInfo)localObject2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void doDisableGC(StringTokenizer paramStringTokenizer) {
/* 1803 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1804 */       MessageOutput.println("No object specified.");
/* 1805 */       return;
/*      */     }
/*      */ 
/* 1808 */     String str1 = paramStringTokenizer.nextToken("");
/* 1809 */     Value localValue = evaluate(str1);
/* 1810 */     if ((localValue != null) && ((localValue instanceof ObjectReference))) {
/* 1811 */       ObjectReference localObjectReference = (ObjectReference)localValue;
/* 1812 */       localObjectReference.disableCollection();
/* 1813 */       String str2 = getStringValue();
/* 1814 */       if (str2 != null)
/* 1815 */         MessageOutput.println("GC Disabled for", str2);
/*      */     }
/*      */     else {
/* 1818 */       MessageOutput.println("Expression must evaluate to an object");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandDisableGC(final StringTokenizer paramStringTokenizer) {
/* 1823 */     new AsyncExecution(paramStringTokenizer)
/*      */     {
/*      */       void action() {
/* 1826 */         Commands.this.doDisableGC(paramStringTokenizer);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   void doEnableGC(StringTokenizer paramStringTokenizer) {
/* 1832 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1833 */       MessageOutput.println("No object specified.");
/* 1834 */       return;
/*      */     }
/*      */ 
/* 1837 */     String str1 = paramStringTokenizer.nextToken("");
/* 1838 */     Value localValue = evaluate(str1);
/* 1839 */     if ((localValue != null) && ((localValue instanceof ObjectReference))) {
/* 1840 */       ObjectReference localObjectReference = (ObjectReference)localValue;
/* 1841 */       localObjectReference.enableCollection();
/* 1842 */       String str2 = getStringValue();
/* 1843 */       if (str2 != null)
/* 1844 */         MessageOutput.println("GC Enabled for", str2);
/*      */     }
/*      */     else {
/* 1847 */       MessageOutput.println("Expression must evaluate to an object");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandEnableGC(final StringTokenizer paramStringTokenizer) {
/* 1852 */     new AsyncExecution(paramStringTokenizer)
/*      */     {
/*      */       void action() {
/* 1855 */         Commands.this.doEnableGC(paramStringTokenizer);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   void doSave(StringTokenizer paramStringTokenizer) {
/* 1861 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1862 */       MessageOutput.println("No save index specified.");
/* 1863 */       return;
/*      */     }
/*      */ 
/* 1866 */     String str1 = paramStringTokenizer.nextToken();
/*      */ 
/* 1868 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1869 */       MessageOutput.println("No expression specified.");
/* 1870 */       return;
/*      */     }
/* 1872 */     String str2 = paramStringTokenizer.nextToken("");
/* 1873 */     Value localValue = evaluate(str2);
/* 1874 */     if (localValue != null) {
/* 1875 */       Env.setSavedValue(str1, localValue);
/* 1876 */       String str3 = getStringValue();
/* 1877 */       if (str3 != null)
/* 1878 */         MessageOutput.println("saved", str3);
/*      */     }
/*      */     else {
/* 1881 */       MessageOutput.println("Expression cannot be void");
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandSave(final StringTokenizer paramStringTokenizer) {
/* 1886 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1887 */       Set localSet = Env.getSaveKeys();
/* 1888 */       if (localSet.isEmpty()) {
/* 1889 */         MessageOutput.println("No saved values");
/* 1890 */         return;
/*      */       }
/* 1892 */       for (String str : localSet) {
/* 1893 */         Value localValue = Env.getSavedValue(str);
/* 1894 */         if (((localValue instanceof ObjectReference)) && 
/* 1895 */           (((ObjectReference)localValue)
/* 1895 */           .isCollected())) {
/* 1896 */           MessageOutput.println("expr is value <collected>", new Object[] { str, localValue
/* 1897 */             .toString() });
/*      */         }
/* 1899 */         else if (localValue == null)
/* 1900 */           MessageOutput.println("expr is null", str);
/*      */         else
/* 1902 */           MessageOutput.println("expr is value", new Object[] { str, localValue
/* 1903 */             .toString() });
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1908 */       new AsyncExecution(paramStringTokenizer)
/*      */       {
/*      */         void action() {
/* 1911 */           Commands.this.doSave(paramStringTokenizer);
/*      */         }
/*      */       };
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandBytecodes(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1919 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1920 */       MessageOutput.println("No class specified.");
/* 1921 */       return;
/*      */     }
/* 1923 */     String str1 = paramStringTokenizer.nextToken();
/*      */ 
/* 1925 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1926 */       MessageOutput.println("No method specified.");
/* 1927 */       return;
/*      */     }
/*      */ 
/* 1930 */     String str2 = paramStringTokenizer.nextToken();
/*      */ 
/* 1932 */     List localList = Env.vm().classesByName(str1);
/*      */ 
/* 1934 */     if (localList.size() == 0) {
/* 1935 */       if (str1.indexOf('.') < 0)
/* 1936 */         MessageOutput.println("not found (try the full name)", str1);
/*      */       else {
/* 1938 */         MessageOutput.println("not found", str1);
/*      */       }
/* 1940 */       return;
/*      */     }
/*      */ 
/* 1943 */     ReferenceType localReferenceType = (ReferenceType)localList.get(0);
/* 1944 */     if (!(localReferenceType instanceof ClassType)) {
/* 1945 */       MessageOutput.println("not a class", str1);
/* 1946 */       return;
/*      */     }
/*      */ 
/* 1949 */     byte[] arrayOfByte = null;
/* 1950 */     for (Object localObject = localReferenceType.methodsByName(str2).iterator(); ((Iterator)localObject).hasNext(); ) { Method localMethod = (Method)((Iterator)localObject).next();
/* 1951 */       if (!localMethod.isAbstract()) {
/* 1952 */         arrayOfByte = localMethod.bytecodes();
/* 1953 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1957 */     localObject = new StringBuffer(80);
/* 1958 */     ((StringBuffer)localObject).append("0000: ");
/* 1959 */     for (int i = 0; i < arrayOfByte.length; i++) {
/* 1960 */       if ((i > 0) && (i % 16 == 0)) {
/* 1961 */         MessageOutput.printDirectln(((StringBuffer)localObject).toString());
/* 1962 */         ((StringBuffer)localObject).setLength(0);
/* 1963 */         ((StringBuffer)localObject).append(String.valueOf(i));
/* 1964 */         ((StringBuffer)localObject).append(": ");
/* 1965 */         j = ((StringBuffer)localObject).length();
/* 1966 */         for (int k = 0; k < 6 - j; k++) {
/* 1967 */           ((StringBuffer)localObject).insert(0, '0');
/*      */         }
/*      */       }
/* 1970 */       int j = 0xFF & arrayOfByte[i];
/* 1971 */       String str3 = Integer.toHexString(j);
/* 1972 */       if (str3.length() == 1) {
/* 1973 */         ((StringBuffer)localObject).append('0');
/*      */       }
/* 1975 */       ((StringBuffer)localObject).append(str3);
/* 1976 */       ((StringBuffer)localObject).append(' ');
/*      */     }
/* 1978 */     if (((StringBuffer)localObject).length() > 6)
/* 1979 */       MessageOutput.printDirectln(((StringBuffer)localObject).toString());
/*      */   }
/*      */ 
/*      */   void commandExclude(StringTokenizer paramStringTokenizer)
/*      */   {
/* 1984 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1985 */       MessageOutput.printDirectln(Env.excludesString());
/*      */     } else {
/* 1987 */       String str = paramStringTokenizer.nextToken("");
/* 1988 */       if (str.equals("none")) {
/* 1989 */         str = "";
/*      */       }
/* 1991 */       Env.setExcludes(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandRedefine(StringTokenizer paramStringTokenizer) {
/* 1996 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1997 */       MessageOutput.println("Specify classes to redefine");
/*      */     } else {
/* 1999 */       String str1 = paramStringTokenizer.nextToken();
/* 2000 */       List localList = Env.vm().classesByName(str1);
/* 2001 */       if (localList.size() == 0) {
/* 2002 */         MessageOutput.println("No class named", str1);
/* 2003 */         return;
/*      */       }
/* 2005 */       if (localList.size() > 1) {
/* 2006 */         MessageOutput.println("More than one class named", str1);
/* 2007 */         return;
/*      */       }
/* 2009 */       Env.setSourcePath(Env.getSourcePath());
/* 2010 */       ReferenceType localReferenceType = (ReferenceType)localList.get(0);
/* 2011 */       if (!paramStringTokenizer.hasMoreTokens()) {
/* 2012 */         MessageOutput.println("Specify file name for class", str1);
/* 2013 */         return;
/*      */       }
/* 2015 */       String str2 = paramStringTokenizer.nextToken();
/* 2016 */       File localFile = new File(str2);
/* 2017 */       byte[] arrayOfByte = new byte[(int)localFile.length()];
/*      */       try {
/* 2019 */         FileInputStream localFileInputStream = new FileInputStream(localFile);
/* 2020 */         localFileInputStream.read(arrayOfByte);
/* 2021 */         localFileInputStream.close();
/*      */       } catch (Exception localException) {
/* 2023 */         MessageOutput.println("Error reading file", new Object[] { str2, localException
/* 2024 */           .toString() });
/* 2025 */         return;
/*      */       }
/* 2027 */       HashMap localHashMap = new HashMap();
/*      */ 
/* 2029 */       localHashMap.put(localReferenceType, arrayOfByte);
/*      */       try {
/* 2031 */         Env.vm().redefineClasses(localHashMap);
/*      */       } catch (Throwable localThrowable) {
/* 2033 */         MessageOutput.println("Error redefining class to file", new Object[] { str1, str2, localThrowable });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandPopFrames(StringTokenizer paramStringTokenizer, boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/*      */     ThreadInfo localThreadInfo;
/* 2044 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 2045 */       localObject = paramStringTokenizer.nextToken();
/* 2046 */       localThreadInfo = doGetThread((String)localObject);
/* 2047 */       if (localThreadInfo == null)
/* 2048 */         return;
/*      */     }
/*      */     else {
/* 2051 */       localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/* 2052 */       if (localThreadInfo == null) {
/* 2053 */         MessageOutput.println("No thread specified.");
/* 2054 */         return;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 2059 */       localObject = localThreadInfo.getCurrentFrame();
/* 2060 */       localThreadInfo.getThread().popFrames((StackFrame)localObject);
/* 2061 */       localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/* 2062 */       ThreadInfo.setCurrentThreadInfo(localThreadInfo);
/* 2063 */       if (paramBoolean)
/* 2064 */         commandStepi();
/*      */     }
/*      */     catch (Throwable localThrowable) {
/* 2067 */       MessageOutput.println("Error popping frame", localThrowable.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   void commandExtension(StringTokenizer paramStringTokenizer) {
/* 2072 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 2073 */       MessageOutput.println("No class specified.");
/* 2074 */       return;
/*      */     }
/*      */ 
/* 2077 */     String str1 = paramStringTokenizer.nextToken();
/* 2078 */     ReferenceType localReferenceType = Env.getReferenceTypeFromToken(str1);
/* 2079 */     String str2 = null;
/* 2080 */     if (localReferenceType != null)
/*      */       try {
/* 2082 */         str2 = localReferenceType.sourceDebugExtension();
/* 2083 */         MessageOutput.println("sourcedebugextension", str2);
/*      */       } catch (AbsentInformationException localAbsentInformationException) {
/* 2085 */         MessageOutput.println("No sourcedebugextension specified");
/*      */       }
/*      */     else
/* 2088 */       MessageOutput.println("is not a valid id or class name", str1);
/*      */   }
/*      */ 
/*      */   void commandVersion(String paramString, VirtualMachineManager paramVirtualMachineManager)
/*      */   {
/* 2094 */     MessageOutput.println("minus version", new Object[] { paramString, new Integer(paramVirtualMachineManager
/* 2096 */       .majorInterfaceVersion()), new Integer(paramVirtualMachineManager
/* 2097 */       .minorInterfaceVersion()), 
/* 2098 */       System.getProperty("java.version") });
/*      */ 
/* 2099 */     if (Env.connection() != null)
/*      */       try {
/* 2101 */         MessageOutput.printDirectln(Env.vm().description());
/*      */       } catch (VMNotConnectedException localVMNotConnectedException) {
/* 2103 */         MessageOutput.println("No VM connected");
/*      */       }
/*      */   }
/*      */ 
/*      */   abstract class AsyncExecution
/*      */   {
/*      */     abstract void action();
/*      */ 
/*      */     AsyncExecution()
/*      */     {
/*   53 */       execute();
/*      */     }
/*      */ 
/*      */     void execute()
/*      */     {
/*   60 */       final ThreadInfo localThreadInfo = ThreadInfo.getCurrentThreadInfo();
/*   61 */       final int i = localThreadInfo == null ? 0 : localThreadInfo.getCurrentFrameIndex();
/*   62 */       Thread local1 = new Thread("asynchronous jdb command")
/*      */       {
/*      */         public void run() {
/*      */           try {
/*   66 */             Commands.AsyncExecution.this.action();
/*      */           }
/*      */           catch (UnsupportedOperationException localUnsupportedOperationException) {
/*   69 */             MessageOutput.println("Operation is not supported on the target VM");
/*      */           } catch (Exception localException) {
/*   71 */             MessageOutput.println("Internal exception during operation:", localException
/*   72 */               .getMessage());
/*      */           }
/*      */           finally
/*      */           {
/*   79 */             if (localThreadInfo != null) {
/*   80 */               ThreadInfo.setCurrentThreadInfo(localThreadInfo);
/*      */               try {
/*   82 */                 localThreadInfo.setCurrentFrameIndex(i);
/*      */               } catch (IncompatibleThreadStateException localIncompatibleThreadStateException4) {
/*   84 */                 MessageOutput.println("Current thread isnt suspended.");
/*      */               } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException4) {
/*   86 */                 MessageOutput.println("Requested stack frame is no longer active:", new Object[] { new Integer(i) });
/*      */               }
/*      */             }
/*      */ 
/*   90 */             MessageOutput.printPrompt();
/*      */           }
/*      */         }
/*      */       };
/*   94 */       local1.start();
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.Commands
 * JD-Core Version:    0.6.2
 */