package com.sun.jdi.event;

import com.sun.jdi.Value;
import jdk.Exported;

@Exported
public abstract interface ModificationWatchpointEvent extends WatchpointEvent
{
  public abstract Value valueToBe();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.ModificationWatchpointEvent
 * JD-Core Version:    0.6.2
 */