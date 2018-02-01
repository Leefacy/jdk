package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public abstract interface ThreadReference extends ObjectReference
{
  public static final int THREAD_STATUS_UNKNOWN = -1;
  public static final int THREAD_STATUS_ZOMBIE = 0;
  public static final int THREAD_STATUS_RUNNING = 1;
  public static final int THREAD_STATUS_SLEEPING = 2;
  public static final int THREAD_STATUS_MONITOR = 3;
  public static final int THREAD_STATUS_WAIT = 4;
  public static final int THREAD_STATUS_NOT_STARTED = 5;

  public abstract String name();

  public abstract void suspend();

  public abstract void resume();

  public abstract int suspendCount();

  public abstract void stop(ObjectReference paramObjectReference)
    throws InvalidTypeException;

  public abstract void interrupt();

  public abstract int status();

  public abstract boolean isSuspended();

  public abstract boolean isAtBreakpoint();

  public abstract ThreadGroupReference threadGroup();

  public abstract int frameCount()
    throws IncompatibleThreadStateException;

  public abstract List<StackFrame> frames()
    throws IncompatibleThreadStateException;

  public abstract StackFrame frame(int paramInt)
    throws IncompatibleThreadStateException;

  public abstract List<StackFrame> frames(int paramInt1, int paramInt2)
    throws IncompatibleThreadStateException;

  public abstract List<ObjectReference> ownedMonitors()
    throws IncompatibleThreadStateException;

  public abstract List<MonitorInfo> ownedMonitorsAndFrames()
    throws IncompatibleThreadStateException;

  public abstract ObjectReference currentContendedMonitor()
    throws IncompatibleThreadStateException;

  public abstract void popFrames(StackFrame paramStackFrame)
    throws IncompatibleThreadStateException;

  public abstract void forceEarlyReturn(Value paramValue)
    throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ThreadReference
 * JD-Core Version:    0.6.2
 */