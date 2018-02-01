package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface ShortValue extends PrimitiveValue, Comparable<ShortValue>
{
  public abstract short value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ShortValue
 * JD-Core Version:    0.6.2
 */