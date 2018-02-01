package com.sun.jdi.event;

import com.sun.jdi.Mirror;
import java.util.Set;
import jdk.Exported;

@Exported
public abstract interface EventSet extends Mirror, Set<Event>
{
  public abstract int suspendPolicy();

  public abstract EventIterator eventIterator();

  public abstract void resume();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.EventSet
 * JD-Core Version:    0.6.2
 */