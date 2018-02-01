package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface DoubleValue extends PrimitiveValue, Comparable<DoubleValue>
{
  public abstract double value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.DoubleValue
 * JD-Core Version:    0.6.2
 */