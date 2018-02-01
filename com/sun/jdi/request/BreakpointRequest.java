package com.sun.jdi.request;

import com.sun.jdi.Locatable;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface BreakpointRequest extends EventRequest, Locatable
{
  public abstract Location location();

  public abstract void addThreadFilter(ThreadReference paramThreadReference);

  public abstract void addInstanceFilter(ObjectReference paramObjectReference);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.BreakpointRequest
 * JD-Core Version:    0.6.2
 */