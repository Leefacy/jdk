package com.sun.jdi.event;

import com.sun.jdi.Method;
import com.sun.jdi.Value;
import jdk.Exported;

@Exported
public abstract interface MethodExitEvent extends LocatableEvent
{
  public abstract Method method();

  public abstract Value returnValue();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.MethodExitEvent
 * JD-Core Version:    0.6.2
 */