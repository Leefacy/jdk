package com.sun.jdi.event;

import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface VMStartEvent extends Event
{
  public abstract ThreadReference thread();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.VMStartEvent
 * JD-Core Version:    0.6.2
 */