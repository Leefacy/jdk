package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface VoidValue extends Value
{
  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VoidValue
 * JD-Core Version:    0.6.2
 */