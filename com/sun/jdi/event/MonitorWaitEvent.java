package com.sun.jdi.event;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface MonitorWaitEvent extends LocatableEvent
{
  public abstract ThreadReference thread();

  public abstract ObjectReference monitor();

  public abstract long timeout();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.MonitorWaitEvent
 * JD-Core Version:    0.6.2
 */