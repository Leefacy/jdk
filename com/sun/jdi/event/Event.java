package com.sun.jdi.event;

import com.sun.jdi.Mirror;
import com.sun.jdi.request.EventRequest;
import jdk.Exported;

@Exported
public abstract interface Event extends Mirror
{
  public abstract EventRequest request();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.Event
 * JD-Core Version:    0.6.2
 */