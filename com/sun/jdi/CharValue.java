package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface CharValue extends PrimitiveValue, Comparable<CharValue>
{
  public abstract char value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.CharValue
 * JD-Core Version:    0.6.2
 */