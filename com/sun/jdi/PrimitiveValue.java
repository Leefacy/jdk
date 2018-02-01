package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface PrimitiveValue extends Value
{
  public abstract boolean booleanValue();

  public abstract byte byteValue();

  public abstract char charValue();

  public abstract short shortValue();

  public abstract int intValue();

  public abstract long longValue();

  public abstract float floatValue();

  public abstract double doubleValue();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.PrimitiveValue
 * JD-Core Version:    0.6.2
 */