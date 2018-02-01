package com.sun.jdi.event;

import java.util.Iterator;
import jdk.Exported;

@Exported
public abstract interface EventIterator extends Iterator<Event>
{
  public abstract Event nextEvent();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.EventIterator
 * JD-Core Version:    0.6.2
 */