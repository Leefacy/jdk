package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface Value extends Mirror
{
  public abstract Type type();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Value
 * JD-Core Version:    0.6.2
 */