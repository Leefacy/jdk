package com.sun.jdi.event;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface ClassPrepareEvent extends Event
{
  public abstract ThreadReference thread();

  public abstract ReferenceType referenceType();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.ClassPrepareEvent
 * JD-Core Version:    0.6.2
 */