package com.sun.jdi.event;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import jdk.Exported;

@Exported
public abstract interface WatchpointEvent extends LocatableEvent
{
  public abstract Field field();

  public abstract ObjectReference object();

  public abstract Value valueCurrent();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.WatchpointEvent
 * JD-Core Version:    0.6.2
 */