package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface Type extends Mirror
{
  public abstract String signature();

  public abstract String name();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Type
 * JD-Core Version:    0.6.2
 */