package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface LongValue extends PrimitiveValue, Comparable<LongValue>
{
  public abstract long value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.LongValue
 * JD-Core Version:    0.6.2
 */