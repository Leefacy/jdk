package com.sun.jdi.event;

import com.sun.jdi.Locatable;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface LocatableEvent extends Event, Locatable
{
  public abstract ThreadReference thread();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.LocatableEvent
 * JD-Core Version:    0.6.2
 */