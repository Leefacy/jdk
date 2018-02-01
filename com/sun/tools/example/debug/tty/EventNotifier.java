package com.sun.tools.example.debug.tty;

import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.event.WatchpointEvent;

abstract interface EventNotifier
{
  public abstract void vmStartEvent(VMStartEvent paramVMStartEvent);

  public abstract void vmDeathEvent(VMDeathEvent paramVMDeathEvent);

  public abstract void vmDisconnectEvent(VMDisconnectEvent paramVMDisconnectEvent);

  public abstract void threadStartEvent(ThreadStartEvent paramThreadStartEvent);

  public abstract void threadDeathEvent(ThreadDeathEvent paramThreadDeathEvent);

  public abstract void classPrepareEvent(ClassPrepareEvent paramClassPrepareEvent);

  public abstract void classUnloadEvent(ClassUnloadEvent paramClassUnloadEvent);

  public abstract void breakpointEvent(BreakpointEvent paramBreakpointEvent);

  public abstract void fieldWatchEvent(WatchpointEvent paramWatchpointEvent);

  public abstract void stepEvent(StepEvent paramStepEvent);

  public abstract void exceptionEvent(ExceptionEvent paramExceptionEvent);

  public abstract void methodEntryEvent(MethodEntryEvent paramMethodEntryEvent);

  public abstract boolean methodExitEvent(MethodExitEvent paramMethodExitEvent);

  public abstract void vmInterrupted();

  public abstract void receivedEvent(Event paramEvent);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.EventNotifier
 * JD-Core Version:    0.6.2
 */