package com.sun.jdi.event;

import jdk.Exported;

@Exported
public abstract interface ClassUnloadEvent extends Event
{
  public abstract String className();

  public abstract String classSignature();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.event.ClassUnloadEvent
 * JD-Core Version:    0.6.2
 */