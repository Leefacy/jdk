package com.sun.jdi.event;

import com.sun.jdi.Mirror;
import jdk.Exported;

@Exported
public abstract interface EventQueue extends Mirror
{
  public abstract EventSet remove()
    throws InterruptedException;

  public abstract EventSet remove(long paramLong)
    throws InterruptedException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.EventQueue
 * JD-Core Version:    0.6.2
 */