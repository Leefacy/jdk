package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface FloatValue extends PrimitiveValue, Comparable<FloatValue>
{
  public abstract float value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.FloatValue
 * JD-Core Version:    0.6.2
 */