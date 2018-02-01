package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface MonitorInfo extends Mirror
{
  public abstract ObjectReference monitor();

  public abstract int stackDepth();

  public abstract ThreadReference thread();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.MonitorInfo
 * JD-Core Version:    0.6.2
 */