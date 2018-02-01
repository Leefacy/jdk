package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface IntegerValue extends PrimitiveValue, Comparable<IntegerValue>
{
  public abstract int value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.IntegerValue
 * JD-Core Version:    0.6.2
 */