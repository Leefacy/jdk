package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface LocalVariable extends Mirror, Comparable<LocalVariable>
{
  public abstract String name();

  public abstract String typeName();

  public abstract Type type()
    throws ClassNotLoadedException;

  public abstract String signature();

  public abstract String genericSignature();

  public abstract boolean isVisible(StackFrame paramStackFrame);

  public abstract boolean isArgument();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.LocalVariable
 * JD-Core Version:    0.6.2
 */