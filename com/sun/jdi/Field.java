package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface Field extends TypeComponent, Comparable<Field>
{
  public abstract String typeName();

  public abstract Type type()
    throws ClassNotLoadedException;

  public abstract boolean isTransient();

  public abstract boolean isVolatile();

  public abstract boolean isEnumConstant();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Field
 * JD-Core Version:    0.6.2
 */