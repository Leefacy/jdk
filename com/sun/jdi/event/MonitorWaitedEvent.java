package com.sun.jdi.event;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface MonitorWaitedEvent extends LocatableEvent
{
  public abstract ThreadReference thread();

  public abstract ObjectReference monitor();

  public abstract boolean timedout();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.MonitorWaitedEvent
 * JD-Core Version:    0.6.2
 */