package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface BooleanValue extends PrimitiveValue
{
  public abstract boolean value();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.BooleanValue
 * JD-Core Version:    0.6.2
 */