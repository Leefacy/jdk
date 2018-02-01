package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface ByteValue extends PrimitiveValue, Comparable<ByteValue>
{
  public abstract byte value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ByteValue
 * JD-Core Version:    0.6.2
 */