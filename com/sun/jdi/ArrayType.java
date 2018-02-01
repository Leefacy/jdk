package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface ArrayType extends ReferenceType
{
  public abstract ArrayReference newInstance(int paramInt);

  public abstract String componentSignature();

  public abstract String componentTypeName();

  public abstract Type componentType()
    throws ClassNotLoadedException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ArrayType
 * JD-Core Version:    0.6.2
 */