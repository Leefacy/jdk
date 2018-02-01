package com.sun.jdi.event;

import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface ThreadDeathEvent extends Event
{
  public abstract ThreadReference thread();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.ThreadDeathEvent
 * JD-Core Version:    0.6.2
 */