package com.sun.jdi.event;

import com.sun.jdi.Method;
import jdk.Exported;

@Exported
public abstract interface MethodEntryEvent extends LocatableEvent
{
  public abstract Method method();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.MethodEntryEvent
 * JD-Core Version:    0.6.2
 */