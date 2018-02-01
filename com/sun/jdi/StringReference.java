package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface StringReference extends ObjectReference
{
  public abstract String value();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.StringReference
 * JD-Core Version:    0.6.2
 */