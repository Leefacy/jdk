package com.sun.jdi.event;

import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import jdk.Exported;

@Exported
public abstract interface ExceptionEvent extends LocatableEvent
{
  public abstract ObjectReference exception();

  public abstract Location catchLocation();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.ExceptionEvent
 * JD-Core Version:    0.6.2
 */